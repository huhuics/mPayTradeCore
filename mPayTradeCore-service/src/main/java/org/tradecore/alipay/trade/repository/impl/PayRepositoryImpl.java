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
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.convertor.Convertor;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

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
    public BizAlipayPayOrder savePayOrder(BizAlipayPayOrder payOrder) {

        LogUtil.info(logger, "收到支付订单持久化请求");

        try {
            bizAlipayPayOrderDAO.insert(payOrder);
        } catch (Exception e) {
            throw new RuntimeException("支付订单持久化失败", e);
        }

        return payOrder;
    }

    @Override
    public void updatePayOrder(BizAlipayPayOrder payOrder) {

        LogUtil.info(logger, "收到订单更新请求");

        try {
            payOrder.setGmtUpdate(new Date());
            bizAlipayPayOrderDAO.updateByPrimaryKey(payOrder);
        } catch (Exception e) {
            throw new RuntimeException("支付订单更新失败", e);
        }
    }

    @Override
    public void updateOrderCancelStatus(BizAlipayPayOrder oriOrder, BizAlipayCancelOrder cancelOrder) {

        LogUtil.info(logger, "收到订单撤销状态更新请求");

        try {
            oriOrder.setCancelStatus(cancelOrder.getCancelStatus());
            oriOrder.setGmtUpdate(new Date());

            bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder);
        } catch (Exception e) {
            throw new RuntimeException("交易订单修改撤销状态失败", e);
        }

    }

    @Override
    public BizAlipayPayOrder selectPayOrder(String merchantId, String outTradeNo, String alipayTradeNo) {

        LogUtil.info(logger, "收到订单查询请求,merchantId={0},outTradeNo={1},alipayTradeNo={2}", merchantId, outTradeNo, alipayTradeNo);

        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(merchantId)) {
            paramMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }

        if (StringUtils.isNotEmpty(outTradeNo)) {
            paramMap.put(QueryFieldConstant.OUT_TRADE_NO, outTradeNo);
        }

        if (StringUtils.isNotEmpty(alipayTradeNo)) {
            paramMap.put(QueryFieldConstant.ALIPAY_TRADE_NO, alipayTradeNo);
        }

        BizAlipayPayOrder order = null;

        try {
            order = bizAlipayPayOrderDAO.selectOrder(paramMap);
        } catch (Exception e) {
            throw new RuntimeException("查询订单错误", e);
        }

        LogUtil.info(logger, "订单查询结果,order={0}", order);

        return order;
    }

    @Override
    public BizAlipayPayOrder selectPayOrderByTradeNo(String tradeNo) {

        LogUtil.info(logger, "收到订单查询请求,tradeNo={0}", tradeNo);

        BizAlipayPayOrder order = null;

        try {
            order = bizAlipayPayOrderDAO.selectByTradeNo(tradeNo);
        } catch (Exception e) {
            throw new RuntimeException("订单查询异常");
        }

        LogUtil.info(logger, "订单查询结果,order={0}", order);

        return order;
    }

    @Override
    public void updatePayOrder(BizAlipayPayOrder oriOrder, Map<String, String> paraMap) {

        LogUtil.info(logger, "收到异步响应订单更新请求");

        oriOrder.setOrderStatus(paraMap.get("trade_status"));

        oriOrder.setAccountDetail(Convertor.reCreateAccountDetail(oriOrder.getAccountDetail(), paraMap.get("buyer_logon_id"), paraMap.get("buyer_id")));

        if (StringUtils.isNotBlank(paraMap.get("receipt_amount"))) {
            oriOrder.setReceiptAmount(new Money(paraMap.get("receipt_amount")));
        }

        oriOrder.setAlipayTradeNo(paraMap.get("trade_no"));
        oriOrder.setGmtPayment(DateUtil.parseDateNewFormat(paraMap.get("gmt_payment")));
        oriOrder.setCheckDate(DateUtil.format(oriOrder.getGmtPayment(), DateUtil.shortFormat));
        oriOrder.setFundBillList(paraMap.get("fund_bill_list"));

        //封装return_detail
        Map<String, Object> returnDetailMap = new HashMap<String, Object>();
        returnDetailMap.put(JSONFieldConstant.RESPONSE, oriOrder.getReturnDetail());
        returnDetailMap.put(JSONFieldConstant.NOTIFY_RESPONSE, paraMap);
        oriOrder.setReturnDetail(JSON.toJSONString(returnDetailMap, SerializerFeature.UseSingleQuotes));

        oriOrder.setGmtUpdate(new Date());

        try {
            bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder);
        } catch (Exception e) {
            throw new RuntimeException("支付订单更新失败", e);
        }

        LogUtil.info(logger, "异步响应订单更新请求成功");

    }

}