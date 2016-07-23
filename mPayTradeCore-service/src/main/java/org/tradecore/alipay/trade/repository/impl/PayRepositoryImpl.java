/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.BizResultEnum;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.request.NotifyRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;

/**
 * 条码支付仓储服务接口实现类<br>
 * 请注意，本类中所有方法均不会校验入参，参数为空将直接抛出RuntimeException，请调用者自行校验入参是否合法
 * @author HuHui
 * @version $Id: TradeRepositoryImpl.java, v 0.1 2016年7月9日 上午10:17:42 HuHui Exp $
 */
@Repository
public class PayRepositoryImpl implements PayRepository {

    /** 日志 */
    private static final Logger  logger = LoggerFactory.getLogger(PayRepositoryImpl.class);

    /** 订单DAO */
    @Resource
    private BizAlipayPayOrderDAO bizAlipayPayOrderDAO;

    @Override
    public BizAlipayPayOrder savePayOrder(PayRequest payRequest, AlipayF2FPayResult alipayF2FPayResult) {

        LogUtil.info(logger, "收到条码支付持久化请求");

        AlipayTradePayResponse response = alipayF2FPayResult.getResponse();

        //将公共参数封装成Domain对象
        BizAlipayPayOrder payOrder = convert2PayOrder(payRequest);

        LogUtil.info(logger, "条码支付请求payRequest转化为domian对象成功,payOrder={0}", payOrder);

        if (alipayF2FPayResult.isTradeSuccess()) {
            LogUtil.info(logger, "支付宝条码支付成功");

            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_SUCCESS.getCode());
            payOrder.setAlipayTradeNo(response.getTradeNo());

            //增加PayDetail字段数据
            Map<String, Object> payDetailMap = JSON.parseObject(payOrder.getPayDetail(), new TypeReference<Map<String, Object>>() {
            });
            payDetailMap.put(JSONFieldConstant.BUYER_LOGON_ID, response.getBuyerLogonId());
            payOrder.setPayDetail(JSON.toJSONString(payDetailMap));

            payOrder.setFundBillList(JSON.toJSONString(response.getFundBillList()));
            payOrder.setDiscountGoodsDetail(response.getDiscountGoodsDetail());
            payOrder.setGmtPayment(response.getGmtPayment());
            payOrder.setCheckDate(DateUtil.format(response.getGmtPayment(), DateUtil.shortFormat));
        } else {
            LogUtil.info(logger, "支付宝条码支付失败");

            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_FAILED.getCode());
        }

        if (response != null) {
            //支付宝返回信息
            payOrder.setReturnDetail(JSON.toJSONString(response.getBody(), SerializerFeature.UseSingleQuotes));
        }

        payOrder.setGmtUpdate(new Date());

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.insert(payOrder) > 0, "条码支付订单持久化失败");

        return payOrder;

    }

    @Override
    public void updateOrderStatus(QueryRequest queryRequest, AlipayF2FQueryResult alipayF2FQueryResult) {

        LogUtil.info(logger, "收到订单状态更新请求");

        if (alipayF2FQueryResult != null && alipayF2FQueryResult.getResponse() != null) {
            AlipayTradeQueryResponse response = alipayF2FQueryResult.getResponse();

            //判断业务是否业务成功
            if (StringUtils.equals(response.getCode(), BizResultEnum.SUCCESS.getCode())) {
                //1.加锁查询本地订单数据
                BizAlipayPayOrder order = selectPayOrder(queryRequest.getMerchantId(), queryRequest.getOutTradeNo(), Boolean.TRUE);

                AssertUtil.assertNotNull(order, "原订单查询为空");

                //2.判断订单状态是否一致，如果不一致则更新本地订单状态
                if (!StringUtils.equals(order.getOrderStatus(), response.getTradeStatus())) {
                    order.setOrderStatus(response.getTradeStatus());
                    order.setGmtUpdate(new Date());

                    //更新订单
                    AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(order) > 0, "支付订单更新失败");
                }

            }
        }

    }

    @Override
    public void updateOrderRefundStatus(BizAlipayPayOrder oriOrder, BizAlipayRefundOrder refundOrder) {

        LogUtil.info(logger, "收到订单退款状态更新请求");

        oriOrder.setRefundStatus(refundOrder.getRefundStatus());

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单退款状态修改失败");
    }

    @Override
    public void updateOrderCancelStatus(BizAlipayPayOrder oriOrder, BizAlipayCancelOrder cancelOrder) {

        LogUtil.info(logger, "收到订单撤销状态更新请求");

        oriOrder.setCancelStatus(cancelOrder.getCancelStatus());

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单撤销状态修改失败");

    }

    @Override
    public BizAlipayPayOrder selectPayOrder(String merchantId, String outTradeNo, boolean isLock) {

        LogUtil.info(logger, "收到订单查询请求,merchantId={0},outTradeNo={1},isLock={2}", merchantId, outTradeNo, isLock);

        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(merchantId)) {
            paramMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }

        if (StringUtils.isNotEmpty(outTradeNo)) {
            paramMap.put(QueryFieldConstant.OUT_TRADE_NO, outTradeNo);
        }

        BizAlipayPayOrder order;
        if (Boolean.valueOf(isLock)) {
            order = bizAlipayPayOrderDAO.selectForUpdate(paramMap);
        } else {
            order = bizAlipayPayOrderDAO.selectOrder(paramMap);
        }

        LogUtil.info(logger, "订单查询结果,order={0}", order);

        return order;
    }

    @Override
    public void updatePayOrder(BizAlipayPayOrder oriOrder, NotifyRequest notifyRequest) {

        LogUtil.info(logger, "收到异步响应订单更新请求");

        oriOrder.setOrderStatus(notifyRequest.getTradeStatus());

        if (StringUtils.isNotBlank(notifyRequest.getReceiptAmount())) {
            oriOrder.setReceiptAmount(new Money(notifyRequest.getReceiptAmount()));
        }

        oriOrder.setAlipayTradeNo(notifyRequest.getTradeNo());
        oriOrder.setGmtPayment(notifyRequest.getGmtPayment());
        oriOrder.setFundBillList(notifyRequest.getFundBillList());

        //封装return_detail
        Map<String, Object> returnDetailMap = new HashMap<String, Object>();
        returnDetailMap.put(JSONFieldConstant.RESPONSE, oriOrder.getReturnDetail());
        returnDetailMap.put(JSONFieldConstant.NOTIFY_RESPONSE, notifyRequest);
        oriOrder.setReturnDetail(JSON.toJSONString(returnDetailMap, SerializerFeature.UseSingleQuotes));

        oriOrder.setGmtUpdate(new Date());

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单更新失败");

        LogUtil.info(logger, "异步响应订单更新请求成功");

    }

    /**
     * 将payRequest转化为domian对象
     * @param payRequest
     * @return
     */
    private BizAlipayPayOrder convert2PayOrder(PayRequest payRequest) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(payRequest.getAcquirerId());
        payOrder.setMerchantId(payRequest.getMerchantId());
        payOrder.setOutTradeNo(payRequest.getOutTradeNo());

        //封装payDetail
        Map<String, Object> payDetailMap = new HashMap<String, Object>();
        payDetailMap.put(JSONFieldConstant.SCENE, payRequest.getScene());
        payOrder.setPayDetail(JSON.toJSONString(payDetailMap));

        payOrder.setAuthCode(payRequest.getAuthCode());
        payOrder.setSellerId(payRequest.getSellerId());
        payOrder.setTotalAmount(new Money(payRequest.getTotalAmount()));

        //判断金额是否为空
        if (StringUtils.isNotBlank(payRequest.getDiscountableAmount())) {
            payOrder.setDiscountableAmount(new Money(payRequest.getDiscountableAmount()));
        }

        if (StringUtils.isNotBlank(payRequest.getUndiscountableAmount())) {
            payOrder.setUndiscountableAmount(new Money(payRequest.getUndiscountableAmount()));
        }

        payOrder.setSubject(payRequest.getSubject());
        payOrder.setBody(payRequest.getBody());
        payOrder.setAppAuthToken(payRequest.getAppAuthToken());
        payOrder.setGoodsDetail(JSON.toJSONString(payRequest.getGoodsDetailList()));

        //封装merchantDetail
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.OPERATOR_ID, payRequest.getOperatorId());
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, payRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, payRequest.getTerminalId());
        merchantDetailMap.put(JSONFieldConstant.ALIPAY_STORE_ID, payRequest.getAlipayStoreId());
        payOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        if (payRequest.getExtendParams() != null) {
            payOrder.setExtendParams(payRequest.getExtendParams().toString());
        }

        payOrder.setTimeoutExpress(payRequest.getTimeoutExpress());
        payOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        // TODO: 创建日期要改成从配置参数中读取
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());

        return payOrder;
    }

}