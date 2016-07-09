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
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.repository.TradeRepository;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;

/**
 * 交易类仓储服务接口实现类
 * @author HuHui
 * @version $Id: TradeRepositoryImpl.java, v 0.1 2016年7月9日 上午10:17:42 HuHui Exp $
 */
@Repository
public class TradeRepositoryImpl implements TradeRepository {

    /** 日志 */
    private static final Logger  logger = LoggerFactory.getLogger(TradeRepositoryImpl.class);

    /** 付款DAO */
    @Resource
    private BizAlipayPayOrderDAO bizAlipayPayOrderDAO;

    @Override
    public BizAlipayPayOrder savePayOrder(PayRequest payRequest) {

        LogUtil.info(logger, "收到付款持久化请求,payRequest={0}", payRequest);

        BizAlipayPayOrder payOrder = convert2PayOrder(payRequest);

        LogUtil.info(logger, "付款请求payRequest转化为payOrder对象成功,payOrder={0}", payOrder);

        AssertUtil.assertTrue(bizAlipayPayOrderDAO.insert(payOrder) > 0, "支付数据持久化失败");

        return payOrder;

    }

    @Override
    public void updatePayOrder(BizAlipayPayOrder bizAlipayPayOrder, AlipayF2FPayResult alipayF2FPayResult) {

        LogUtil.info(logger, "收到交易更新请求");

        TradeStatus tradeStatus = alipayF2FPayResult.getTradeStatus();
        AlipayTradePayResponse response = alipayF2FPayResult.getResponse();

        if (tradeStatus == TradeStatus.SUCCESS) {
            LogUtil.info(logger, "支付宝支付成功!");

            bizAlipayPayOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_SUCCESS.getCode());
            bizAlipayPayOrder.setAlipayTradeNo(response.getTradeNo());

            //增加PayDetail字段数据
            Map<String, Object> payDetailMap = JSON.parseObject(bizAlipayPayOrder.getPayDetail(), new TypeReference<Map<String, Object>>() {
            });
            payDetailMap.put(JSONFieldConstant.BUYER_LOGON_ID, response.getBuyerLogonId());
            bizAlipayPayOrder.setPayDetail(JSON.toJSONString(payDetailMap));

            bizAlipayPayOrder.setFundBillList(JSON.toJSONString(response.getFundBillList()));
            bizAlipayPayOrder.setDiscountGoodsDetail(response.getDiscountGoodsDetail());
            bizAlipayPayOrder.setGmtPayment(response.getGmtPayment());

        } else {
            LogUtil.error(logger, "支付宝支付失败!");

            bizAlipayPayOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_FAILED.getCode());
        }

        //封装支付宝返回信息
        Map<String, Object> returnDetailMap = new HashMap<String, Object>();
        returnDetailMap.put(JSONFieldConstant.CODE, response.getCode());
        returnDetailMap.put(JSONFieldConstant.MSG, response.getMsg());
        returnDetailMap.put(JSONFieldConstant.SUB_CODE, response.getSubCode());
        returnDetailMap.put(JSONFieldConstant.SUB_MSG, response.getSubMsg());

        bizAlipayPayOrder.setReturnDetail(JSON.toJSONString(returnDetailMap));

        bizAlipayPayOrder.setGmtUpdate(new Date());

        //修改本地订单数据
        AssertUtil.assertTrue(bizAlipayPayOrderDAO.updateByPrimaryKey(bizAlipayPayOrder) > 0, "修改订单失败!");

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
        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());

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
        payOrder.setAppauthtoken(payRequest.getAppAuthToken());
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

        // TODO: 对账日期和创建日期要改成从配置参数中读取
        payOrder.setCheckDate(DateUtil.format(new Date(), DateUtil.shortFormat));
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());
        payOrder.setGmtUpdate(new Date());

        return payOrder;
    }
}
