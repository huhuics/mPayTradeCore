/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.convertor;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.CreateRequest;
import org.tradecore.alipay.trade.request.DefaultPayRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.FormaterUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 将请求转换为Domian对象
 * @author HuHui
 * @version $Id: Convertor.java, v 0.1 2016年8月4日 下午4:51:26 HuHui Exp $
 */
public class Convertor {

    /**
     * 支付类的请求公共字段转换
     * @param request
     * @return
     */
    private static BizAlipayPayOrder convertCommonFields(DefaultPayRequest request) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();

        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(request.getAcquirerId());
        payOrder.setMerchantId(request.getMerchantId());
        payOrder.setOutTradeNo(request.getOutTradeNo());
        payOrder.setTradeNo(FormaterUtil.tradeNoFormat(request.getAcquirerId(), request.getMerchantId(), request.getOutTradeNo()));

        payOrder.setScene(request.getScene());

        //封装账号信息
        Map<String, Object> accountDetailMap = new HashMap<String, Object>();
        accountDetailMap.put(JSONFieldConstant.SELLER_ID, request.getSellerId());
        payOrder.setAccountDetail(JSON.toJSONString(accountDetailMap));

        payOrder.setTotalAmount(new Money(request.getTotalAmount()));

        //判断金额是否为空
        if (StringUtils.isNotBlank(request.getDiscountableAmount())) {
            payOrder.setDiscountableAmount(new Money(request.getDiscountableAmount()));
        }

        if (StringUtils.isNotBlank(request.getUndiscountableAmount())) {
            payOrder.setUndiscountableAmount(new Money(request.getUndiscountableAmount()));
        }

        payOrder.setSubject(request.getSubject());
        payOrder.setBody(request.getBody());
        payOrder.setAppAuthToken(request.getAppAuthToken());
        payOrder.setGoodsDetail(JSON.toJSONString(request.getGoodsDetailList()));

        //封装merchantDetail
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.OPERATOR_ID, request.getOperatorId());
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, request.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, request.getTerminalId());
        merchantDetailMap.put(JSONFieldConstant.ALIPAY_STORE_ID, request.getAlipayStoreId());
        payOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        if (request.getExtendParams() != null) {
            payOrder.setExtendParams(request.getExtendParams().toString());
        }

        payOrder.setTimeoutExpress(request.getTimeoutExpress());
        payOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        // TODO: 创建日期要改成从配置参数中读取
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());
        payOrder.setGmtUpdate(new Date());

        return payOrder;
    }

    /**
     * 将payRequest转化为domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param payRequest
     * @return
     */
    public static BizAlipayPayOrder convert2PayOrder(PayRequest payRequest) {

        BizAlipayPayOrder payOrder = convertCommonFields(payRequest);

        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());

        payOrder.setAuthCode(payRequest.getAuthCode());

        return payOrder;
    }

    /**
     * 将createRequest转换成domian对象
     * @param createRequest
     * @return
     */
    public static BizAlipayPayOrder convert2PayOrder(CreateRequest createRequest) {

        BizAlipayPayOrder payOrder = convertCommonFields(createRequest);

        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());

        payOrder.setAccountDetail(reCreateAccountDetail(payOrder.getAccountDetail(), createRequest.getBuyerLogonId(), createRequest.getBuyerId()));

        payOrder.setOutNotifyUrl(createRequest.getOutNotifyUrl());

        return payOrder;
    }

    /**
     * 向accountDetail中增加buyerLogonId和buyerId字段，并返回序列化之后的字符串
     * @param accountDetail    当前accountDetail数据，json格式
     * @param buyerLogonId
     * @param buyerId
     * @return                 json格式
     */
    public static String reCreateAccountDetail(String accountDetail, String buyerLogonId, String buyerId) {
        Map<String, Object> accountDetailMap = JSON.parseObject(accountDetail, new TypeReference<Map<String, Object>>() {
        });
        accountDetailMap.put(JSONFieldConstant.BUYER_LOGON_ID, buyerLogonId);
        accountDetailMap.put(JSONFieldConstant.BUYER_ID, buyerId);

        return JSON.toJSONString(accountDetailMap);
    }

    /**
     * 将precreateRequest转化为domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param precreateRequest
     * @return
     */
    public static BizAlipayPayOrder convert2PayOrder(PrecreateRequest precreateRequest) {

        BizAlipayPayOrder payOrder = convertCommonFields(precreateRequest);

        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());

        //设置out_notify_url
        payOrder.setOutNotifyUrl(precreateRequest.getOutNotifyUrl());

        return payOrder;
    }

    /**
     * 将cancelRequest转换成domian对象<br>
     * 只转化公共参数，有些参数，如状态、支付宝返回字段此处不转化
     * @param oriOrder
     * @param cancelRequest
     * @return
     */
    public static BizAlipayCancelOrder convert2CancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest) {
        BizAlipayCancelOrder cancelOrder = new BizAlipayCancelOrder();

        cancelOrder.setId(UUIDUtil.geneId());
        cancelOrder.setAcquirerId(cancelRequest.getAcquirerId());
        cancelOrder.setMerchantId(cancelRequest.getMerchantId());
        cancelOrder.setAlipayTradeNo(oriOrder.getAlipayTradeNo());

        //为防止商户不传outTradeNo值，此处用原始订单的outTradeNo，保证撤销表中outTradeNo值一定不为空
        cancelOrder.setOutTradeNo(oriOrder.getOutTradeNo());

        cancelOrder.setTradeNo(FormaterUtil.tradeNoFormat(cancelRequest.getAcquirerId(), cancelRequest.getMerchantId(), oriOrder.getOutTradeNo()));

        cancelOrder.setTotalAmount(oriOrder.getTotalAmount());

        //TODO:时间从配置中读取
        cancelOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        cancelOrder.setGmtCreate(new Date());
        cancelOrder.setGmtUpdate(new Date());

        return cancelOrder;

    }

}
