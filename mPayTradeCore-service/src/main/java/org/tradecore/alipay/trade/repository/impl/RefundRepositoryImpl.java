/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.RefundQueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.FormaterUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayRefundOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 退款仓储实现类
 * @author HuHui
 * @version $Id: RefundRepositoryImpl.java, v 0.1 2016年7月11日 下午7:49:34 HuHui Exp $
 */
@Repository
public class RefundRepositoryImpl implements RefundRepository {

    /** 日志 */
    private static final Logger     logger = LoggerFactory.getLogger(RefundRepositoryImpl.class);

    /** 退款DAO */
    @Resource
    private BizAlipayRefundOrderDAO bizAlipayRefundOrderDAO;

    @Override
    public void updateRefundOrder(BizAlipayPayOrder oriOrder, BizAlipayRefundOrder refundOrder, AlipayTradeRefundResponse response) {

        LogUtil.info(logger, "收到退款订单持久化请求");

        if (response != null && StringUtils.equals(response.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            LogUtil.info(logger, "支付宝退款业务成功");

            //判断金额是否为空
            if (StringUtils.isNotBlank(response.getSendBackFee())) {
                refundOrder.setSendBackFee(new Money(response.getSendBackFee()));
            }

            refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());
            refundOrder.setRefundDetailItemList(JSON.toJSONString(response.getRefundDetailItemList()));
            refundOrder.setBuyerUserId(response.getBuyerUserId());
            refundOrder.setGmtRefundPay(response.getGmtRefundPay());
            refundOrder.setCheckDate(DateUtil.format(response.getGmtRefundPay(), DateUtil.shortFormat));

            oriOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

            //如果是全额退款，修改交易订单状态为TRADE_CLOSED
            if (isTotalRefund(refundOrder.getMerchantId(), refundOrder.getOutTradeNo(), refundOrder.getAlipayTradeNo(), oriOrder.getTotalAmount(), refundOrder.getRefundAmount())) {
                oriOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
            }

        } else {

            if (response == null || StringUtils.equals(response.getCode(), AlipayBizResultEnum.UNKNOW.getCode())) {
                LogUtil.warn(logger, "订单退款返回系统错误");
                refundOrder.setRefundStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
            } else {
                LogUtil.warn(logger, "订单退款返回失败");
                refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_FAILED.getCode());
            }

            //当前订单已经成功退款的金额
            Money refundedMoney = getRefundedMoney(refundOrder.getMerchantId(), refundOrder.getOutTradeNo(), refundOrder.getAlipayTradeNo());

            //原始订单退款状态修改逻辑：1.如果是全额退款，原订单退款状态和支付宝返回一致；2.如果是部分退款，只记录退款成功，退款失败不更改原订单退款记录
            if (refundedMoney.equals(new Money(0))) {//1.没有历史退款

                if (oriOrder.getTotalAmount().equals(refundOrder.getRefundAmount())) {//1.1全额退款失败，则原订单退款状态失败
                    oriOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_FAILED.getCode());
                }
            }
        }

        if (response != null) {
            refundOrder.setFundChange(response.getFundChange());
            refundOrder.setReturnDetail(JSON.toJSONString(response.getBody(), SerializerFeature.UseSingleQuotes));
        }

        refundOrder.setAlipayTradeNo(oriOrder.getAlipayTradeNo());
        refundOrder.setGmtUpdate(new Date());

        try {
            bizAlipayRefundOrderDAO.updateByPrimaryKey(refundOrder);
        } catch (Exception e) {
            throw new RuntimeException("退款记录持久化失败", e);
        }

        LogUtil.info(logger, "退款订单持久化成功");

    }

    @Override
    public BizAlipayRefundOrder saveRefundOrder(BizAlipayRefundOrder refundOrder) {

        LogUtil.info(logger, "收到退款订单持久化请求");

        try {
            bizAlipayRefundOrderDAO.insert(refundOrder);
        } catch (SQLException e) {
            throw new RuntimeException("退款记录持久化失败", e);
        }

        LogUtil.info(logger, "退款订单持久化成功");

        return refundOrder;
    }

    @Override
    public List<BizAlipayRefundOrder> selectRefundOrders(RefundQueryRequest queryRequest) {

        LogUtil.info(logger, "收到查询退款订单请求,refundOrderQueryRequest={0}", queryRequest);

        //封装查询参数。根据商户订单号和退款状态查询所有退款订单
        Map<String, Object> paraMap = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(queryRequest.getMerchantId())) {
            paraMap.put(QueryFieldConstant.MERCHANT_ID, queryRequest.getMerchantId());
        }
        if (StringUtils.isNotEmpty(queryRequest.getOutTradeNo())) {
            paraMap.put(QueryFieldConstant.OUT_TRADE_NO, queryRequest.getOutTradeNo());
        }
        if (StringUtils.isNotEmpty(queryRequest.getAlipayTradeNo())) {
            paraMap.put(QueryFieldConstant.ALIPAY_TRADE_NO, queryRequest.getAlipayTradeNo());
        }
        if (StringUtils.isNotEmpty(queryRequest.getRefundStatus())) {
            paraMap.put(QueryFieldConstant.REFUND_STATUS, queryRequest.getRefundStatus());
        }
        if (StringUtils.isNotEmpty(queryRequest.getOutRequestNo())) {
            paraMap.put(QueryFieldConstant.OUT_REQUEST_NO, queryRequest.getOutRequestNo());
        }

        List<BizAlipayRefundOrder> refundOrders = null;
        try {
            refundOrders = bizAlipayRefundOrderDAO.selectRefundOrders(paraMap);
        } catch (Exception e) {
            throw new RuntimeException("退款订单查询失败", e);
        }

        LogUtil.info(logger, "退款订单查询结果,refundOrders={0}", refundOrders);

        return refundOrders;
    }

    @Override
    public Money getRefundedMoney(String merchantId, String outTradeNo, String alipayTradeNo) {

        LogUtil.info(logger, "收到查询已成功退款总金额请求,outTradeNo={0}", outTradeNo);

        Money totalRefundedAmount = new Money(0);

        //根据商户订单号获取该订单下所有退款成功的退款订单
        RefundQueryRequest refundQueryRequest = new RefundQueryRequest();
        refundQueryRequest.setMerchantId(merchantId);
        refundQueryRequest.setOutTradeNo(outTradeNo);
        refundQueryRequest.setAlipayTradeNo(alipayTradeNo);
        refundQueryRequest.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

        List<BizAlipayRefundOrder> refundOrders = selectRefundOrders(refundQueryRequest);

        if (CollectionUtils.isNotEmpty(refundOrders)) {
            for (BizAlipayRefundOrder order : refundOrders) {
                totalRefundedAmount.addTo(order.getRefundAmount());
            }
        }

        LogUtil.info(logger, "该订单历史已成功退款总金额totalRefundedAmount={0}", totalRefundedAmount);

        return totalRefundedAmount;
    }

    @Override
    public void updateRefundStatus(BizAlipayPayOrder payOrder, List<BizAlipayRefundOrder> refundOrders, PayRepository payRepository, AlipayTradeFastpayRefundQueryResponse refundQueryResponse) {

        LogUtil.info(logger, "收到退款状态更新请求");

        BizAlipayRefundOrder refundOrder = null;

        boolean isPayOrderModified = false;

        //1.支付宝返回退款成功
        if (StringUtils.isNotBlank(refundQueryResponse.getOutRequestNo()) && StringUtils.isNotBlank(refundQueryResponse.getRefundAmount())) {

            //1.1 本地无此退款订单
            if (CollectionUtils.isEmpty(refundOrders)) {
                LogUtil.warn(logger, "支付宝返回退款订单存在,但本地无此退款订单,outRequestNo={0}", refundQueryResponse.getOutRequestNo());

                //构造一笔退款订单并保存
                refundOrder = buildRefundOrder(payOrder, refundQueryResponse);

                try {
                    bizAlipayRefundOrderDAO.insert(refundOrder);
                } catch (Exception e) {
                    throw new RuntimeException("退款订单持久化失败", e);
                }
            } else {//1.2 本地有此退款订单
                LogUtil.info(logger, "支付宝返回退款订单存在,且本地有此订单");
                refundOrder = refundOrders.get(ParamConstant.FIRST_INDEX);

                //修改本地退款订单状态为退款成功，并填充信息
                if (!StringUtils.equals(refundOrder.getRefundStatus(), AlipayTradeStatusEnum.REFUND_SUCCESS.getCode())) {
                    refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());
                    refundOrder.setRefundReason(refundQueryResponse.getRefundReason());
                    refundOrder.setRefundAmount(new Money(refundQueryResponse.getRefundAmount()));

                    updateRefundOrder(refundOrder);
                }
            }

            //1.3 修改本地交易订单退款状态为退款成功
            if (!StringUtils.equals(payOrder.getRefundStatus(), AlipayTradeStatusEnum.REFUND_SUCCESS.getCode())) {
                payOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());
                isPayOrderModified = true;
            }

            //1.4 如果是完全退款，交易状态必须是TRADE_CLOSED
            if (payOrder.getTotalAmount().equals(new Money(refundQueryResponse.getRefundAmount()))) {
                if (!StringUtils.equals(payOrder.getOrderStatus(), AlipayTradeStatusEnum.TRADE_CLOSED.getCode())) {
                    payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
                    isPayOrderModified = true;
                }
            }

        } else {//2.支付宝返回退款失败(支付宝端无此退款记录)
            LogUtil.info(logger, "支付宝返回退款订单查询不存在");

            //2.1 如果退款订单为成功
            if (CollectionUtils.isNotEmpty(refundOrders)) {
                BizAlipayRefundOrder needlessRefundOrder = refundOrders.get(ParamConstant.FIRST_INDEX);
                if (StringUtils.equals(needlessRefundOrder.getRefundStatus(), AlipayTradeStatusEnum.REFUND_SUCCESS.getCode())) {
                    LogUtil.warn(logger, "支付宝返回退款订单查询不存在,但本地退款订单为退款成功,outRequestNo={0}", needlessRefundOrder.getOutRequestNo());
                }
            }

        }

        if (isPayOrderModified) {
            payRepository.updatePayOrder(payOrder);
        }

    }

    @Override
    public void updateRefundOrder(BizAlipayRefundOrder refundOrder) {

        LogUtil.info(logger, "收到退款订单更新请求");

        try {
            refundOrder.setGmtUpdate(new Date());
            bizAlipayRefundOrderDAO.updateByPrimaryKey(refundOrder);
        } catch (Exception e) {
            throw new RuntimeException("退款订单更新失败", e);
        }

    }

    @Override
    public void deleteRefundOrder(String id) {

        LogUtil.info(logger, "收到删除退款订单请求,id={0}", id);

        try {
            bizAlipayRefundOrderDAO.deleteByPrimaryKey(id);
        } catch (Exception e) {
            throw new RuntimeException("删除退款订单失败", e);
        }
    }

    /**
     * 是否完全退款<br>
     * 分两种请情况<ul>
     * <li>本次退款金额=订单总金额</li>
     * <li>本次退款金额+历史退款成功总金额=订单总金额</li>
     * </ul>
     */
    private boolean isTotalRefund(String merchantId, String outTradeNo, String alipayTradeNo, Money totalAmount, Money refundAmount) {

        if (totalAmount.equals(refundAmount)) {
            return true;
        } else {
            //历史退款成功总金额
            Money refundedMoney = getRefundedMoney(merchantId, outTradeNo, alipayTradeNo);

            refundedMoney.addTo(refundAmount);

            if (totalAmount.equals(refundedMoney)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 创建一笔退款订单
     */
    private BizAlipayRefundOrder buildRefundOrder(BizAlipayPayOrder payOrder, AlipayTradeFastpayRefundQueryResponse refundQueryResponse) {
        BizAlipayRefundOrder refundOrder = new BizAlipayRefundOrder();
        refundOrder.setId(UUIDUtil.geneId());
        refundOrder.setAcquirerId(payOrder.getAcquirerId());
        refundOrder.setMerchantId(payOrder.getMerchantId());
        refundOrder.setAlipayTradeNo(payOrder.getAlipayTradeNo());
        refundOrder.setOutTradeNo(payOrder.getOutTradeNo());
        refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());
        refundOrder.setTotalAmount(payOrder.getTotalAmount());
        refundOrder.setTradeNo(FormaterUtil.tradeNoFormat(payOrder.getAcquirerId(), payOrder.getMerchantId(), payOrder.getOutTradeNo()));
        refundOrder.setRefundAmount(new Money(refundQueryResponse.getRefundAmount()));
        refundOrder.setRefundReason(refundQueryResponse.getRefundReason());
        refundOrder.setOutRequestNo(refundQueryResponse.getOutRequestNo());
        refundOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());
        //TODO:时间从配置中读取
        refundOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));
        //TODO:待确认
        refundOrder.setCheckDate(DateUtil.format(new Date(), DateUtil.shortFormat));
        refundOrder.setGmtCreate(new Date());
        refundOrder.setGmtUpdate(new Date());

        return refundOrder;
    }

    /**
     * 将refundRequest转换成domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param refundRequest
     * @return
     */
    private BizAlipayRefundOrder convert2RefundOrder(RefundRequest refundRequest, BizAlipayPayOrder oriOrder) {

        BizAlipayRefundOrder refundOrder = new BizAlipayRefundOrder();
        refundOrder.setId(UUIDUtil.geneId());
        refundOrder.setAcquirerId(refundRequest.getAcquirerId());
        refundOrder.setMerchantId(refundRequest.getMerchantId());
        refundOrder.setAlipayTradeNo(refundRequest.getAlipayTradeNo());

        //为防止商户不传outTradeNo值，此处用原始订单的outTradeNo，保证退款表中outTradeNo值一定不为空
        refundOrder.setOutTradeNo(oriOrder.getOutTradeNo());
        refundOrder.setTradeNo(FormaterUtil.tradeNoFormat(refundRequest.getAcquirerId(), refundRequest.getMerchantId(), oriOrder.getOutTradeNo()));

        refundOrder.setTotalAmount(oriOrder.getTotalAmount());

        //由于refundAmount不可能为空，故此处不再校验
        refundOrder.setRefundAmount(new Money(refundRequest.getRefundAmount()));

        refundOrder.setRefundReason(refundRequest.getRefundReason());
        refundOrder.setOutRequestNo(refundRequest.getOutRequestNo());

        //封装merchantDetail参数
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, refundRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, refundRequest.getTerminalId());
        refundOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        refundOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        //TODO:时间从配置中读取
        refundOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        refundOrder.setGmtCreate(new Date());

        return refundOrder;

    }

}
