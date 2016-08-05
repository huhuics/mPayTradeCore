/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.convertor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.TradeNoFormater;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;

/**
 * 将请求转换为Domian对象
 * @author HuHui
 * @version $Id: Convertor.java, v 0.1 2016年8月4日 下午4:51:26 HuHui Exp $
 */
public class Convertor {

    /**
     * 将payRequest转化为domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param payRequest
     * @return
     */
    public static BizAlipayPayOrder convert2PayOrder(PayRequest payRequest) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(payRequest.getAcquirerId());
        payOrder.setMerchantId(payRequest.getMerchantId());
        payOrder.setOutTradeNo(payRequest.getOutTradeNo());
        payOrder.setTradeNo(TradeNoFormater.format(payRequest.getAcquirerId(), payRequest.getMerchantId(), payRequest.getOutTradeNo()));

        //封装payDetail
        payOrder.setScene(payRequest.getScene());

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

    /**
     * 将precreateRequest转化为domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param precreateRequest
     * @return
     */
    public static BizAlipayPayOrder convert2PayOrder(PrecreateRequest precreateRequest) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(precreateRequest.getAcquirerId());
        payOrder.setMerchantId(precreateRequest.getMerchantId());
        payOrder.setOutTradeNo(precreateRequest.getOutTradeNo());
        payOrder.setTradeNo(TradeNoFormater.format(precreateRequest.getAcquirerId(), precreateRequest.getMerchantId(), precreateRequest.getOutTradeNo()));

        //封装payDetail
        payOrder.setScene(precreateRequest.getScene());

        payOrder.setSellerId(precreateRequest.getSellerId());
        payOrder.setTotalAmount(new Money(precreateRequest.getTotalAmount()));

        //判断金额是否为空
        if (StringUtils.isNotBlank(precreateRequest.getDiscountableAmount())) {
            payOrder.setDiscountableAmount(new Money(precreateRequest.getDiscountableAmount()));
        }

        if (StringUtils.isNotBlank(precreateRequest.getUndiscountableAmount())) {
            payOrder.setUndiscountableAmount(new Money(precreateRequest.getUndiscountableAmount()));
        }

        payOrder.setSubject(precreateRequest.getSubject());
        payOrder.setBody(precreateRequest.getBody());
        payOrder.setAppAuthToken(precreateRequest.getAppAuthToken());
        payOrder.setGoodsDetail(JSON.toJSONString(precreateRequest.getGoodsDetailList()));

        //封装merchantDetail
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.OPERATOR_ID, precreateRequest.getOperatorId());
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, precreateRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, precreateRequest.getTerminalId());
        merchantDetailMap.put(JSONFieldConstant.ALIPAY_STORE_ID, precreateRequest.getAlipayStoreId());
        payOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        if (precreateRequest.getExtendParams() != null) {
            payOrder.setExtendParams(precreateRequest.getExtendParams().toString());
        }

        payOrder.setTimeoutExpress(precreateRequest.getTimeoutExpress());
        payOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        // TODO: 创建日期要改成从配置参数中读取
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());

        //设置out_notify_url
        payOrder.setOutNotifyUrl(precreateRequest.getOutNotifyUrl());

        return payOrder;
    }

}
