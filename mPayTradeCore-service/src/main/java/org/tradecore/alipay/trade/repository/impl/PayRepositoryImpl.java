/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.sql.SQLException;
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
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.TradeNoFormater;
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
            LogUtil.error(e, logger, "支付订单持久化失败,message={0}", e.getMessage());
            throw new RuntimeException("支付订单持久化失败", e);
        }

        return payOrder;
    }

    @Override
    public void updateOrderStatus(BizAlipayPayOrder payOrder) throws Exception {

        LogUtil.info(logger, "收到订单状态更新请求");

        try {
            payOrder.setGmtUpdate(new Date());
            bizAlipayPayOrderDAO.updateByPrimaryKey(payOrder);
        } catch (Exception e) {
            LogUtil.error(e, logger, "支付订单更新失败,message={0}", e.getMessage());
            throw new RuntimeException("支付订单更新失败", e);
        }
    }

    @Override
    public void updateOrderRefundStatus(BizAlipayPayOrder oriOrder) {

        LogUtil.info(logger, "收到订单退款状态更新请求");

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单退款状态修改失败");
    }

    @Override
    public void updateOrderCancelStatus(BizAlipayPayOrder oriOrder, BizAlipayCancelOrder cancelOrder) {

        LogUtil.info(logger, "收到订单撤销状态更新请求");

        oriOrder.setCancelStatus(cancelOrder.getCancelStatus());
        oriOrder.setGmtUpdate(new Date());

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单撤销状态修改失败");

    }

    @Override
    public BizAlipayPayOrder selectPayOrder(String merchantId, String outTradeNo, String alipayTradeNo) throws SQLException {

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
        order = bizAlipayPayOrderDAO.selectOrder(paramMap);

        LogUtil.info(logger, "订单查询结果,order={0}", order);

        return order;
    }

    @Override
    public BizAlipayPayOrder selectPayOrderByTradeNo(String acquirerId, String merchantId, String outTradeNo) throws SQLException {

        //组装结算中心订单号
        String tradeNo = TradeNoFormater.format(acquirerId, merchantId, outTradeNo);

        LogUtil.info(logger, "收到订单查询请求,tradeNo={0}", tradeNo);

        BizAlipayPayOrder order = null;

        try {
            order = bizAlipayPayOrderDAO.selectByTradeNo(tradeNo);
        } catch (Exception e) {
            LogUtil.error(e, logger, "订单查询异常,message={0}", e.getMessage());
            throw new RuntimeException("订单查询异常", e);
        }

        LogUtil.info(logger, "订单查询结果,order={0}", order);

        return order;
    }

    @Override
    public void updatePayOrder(BizAlipayPayOrder oriOrder, Map<String, String> paraMap) {

        LogUtil.info(logger, "收到异步响应订单更新请求");

        oriOrder.setOrderStatus(paraMap.get("trade_status"));

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

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(oriOrder) > 0, "支付订单更新失败");

        LogUtil.info(logger, "异步响应订单更新请求成功");

    }

}