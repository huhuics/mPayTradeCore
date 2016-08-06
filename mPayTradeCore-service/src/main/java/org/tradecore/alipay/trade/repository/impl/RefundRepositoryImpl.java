/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

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
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.RefundOrderQueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.TradeNoFormater;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayRefundOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 
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
    public BizAlipayRefundOrder saveRefundOrder(BizAlipayPayOrder oriOrder, RefundRequest refundRequest, AlipayTradeRefundResponse response) {

        LogUtil.info(logger, "收到退款订单持久化请求");

        //将公共参数封装成Domain对象
        BizAlipayRefundOrder refundOrder = convert2RefundOrder(refundRequest, oriOrder);

        LogUtil.info(logger, "退款请求对象refundRequest转换成refundOrder对象成功,refundOrder={0}", refundOrder);

        if (response != null && StringUtils.equals(response.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            LogUtil.info(logger, "支付宝退款成功");

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
            if (isTotalRefund(refundRequest.getMerchantId(), refundRequest.getOutTradeNo(), refundRequest.getAlipayTradeNo(), oriOrder.getTotalAmount(),
                refundOrder.getRefundAmount())) {
                oriOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
            }

        } else {

            if (response == null || StringUtils.equals(response.getCode(), AlipayBizResultEnum.UNKNOW.getCode())) {
                LogUtil.info(logger, "订单退款返回系统错误");
                refundOrder.setRefundStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
            } else {
                LogUtil.info(logger, "订单退款返回失败");
                refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_FAILED.getCode());
            }

            //当前订单已经成功退款的金额
            Money refundedMoney = getRefundedMoney(refundRequest.getMerchantId(), refundRequest.getOutTradeNo(), refundRequest.getAlipayTradeNo());

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

        AssertUtil.assertTrue(bizAlipayRefundOrderDAO.insert(refundOrder) > 0, "退款请求数据持久化失败");

        LogUtil.info(logger, "退款订单持久化成功");

        return refundOrder;
    }

    @Override
    public List<BizAlipayRefundOrder> selectRefundOrders(RefundOrderQueryRequest queryRequest) {

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

        List<BizAlipayRefundOrder> refundOrders = bizAlipayRefundOrderDAO.selectRefundOrders(paraMap);

        LogUtil.info(logger, "退款订单查询结果,refundOrders={0}", refundOrders);

        return refundOrders;
    }

    @Override
    public Money getRefundedMoney(String merchantId, String outTradeNo, String alipayTradeNo) {

        LogUtil.info(logger, "收到查询已成功退款总金额请求,outTradeNo={0}", outTradeNo);

        Money totalRefundedAmount = new Money(0);

        //根据商户订单号获取该订单下所有退款成功的退款订单
        RefundOrderQueryRequest queryRequest = new RefundOrderQueryRequest();
        queryRequest.setMerchantId(merchantId);
        queryRequest.setOutTradeNo(outTradeNo);
        queryRequest.setAlipayTradeNo(alipayTradeNo);
        queryRequest.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

        List<BizAlipayRefundOrder> refundOrders = selectRefundOrders(queryRequest);

        if (CollectionUtils.isNotEmpty(refundOrders)) {
            for (BizAlipayRefundOrder order : refundOrders) {
                totalRefundedAmount.addTo(order.getRefundAmount());
            }
        }

        LogUtil.info(logger, "已成功退款总金额totalRefundedAmount={0}", totalRefundedAmount);

        return totalRefundedAmount;
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
        refundOrder.setTradeNo(TradeNoFormater.format(refundRequest.getAcquirerId(), refundRequest.getMerchantId(), oriOrder.getOutTradeNo()));

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
