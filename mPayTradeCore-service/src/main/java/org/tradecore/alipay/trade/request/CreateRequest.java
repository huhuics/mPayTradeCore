/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 线上支付交易创建请求
 * @author HuHui
 * @version $Id: CreateRequest.java, v 0.1 2016年8月6日 下午3:26:31 HuHui Exp $
 */
public class CreateRequest extends DefaultPayRequest {

    /** uid */
    private static final long serialVersionUID = 5787412361695890267L;

    /**
     * (特殊可选)买家支付宝账号，和buyer_id不能同时为空
     */
    @JSONField(name = "buyer_logon_id")
    private String            buyerLogonId;

    /**
     * (特殊可选)买家的支付宝唯一用户号（2088开头的16位纯数字）,和buyer_logon_id不能同时为空
     */
    @JSONField(name = "buyer_id")
    private String            buyerId;

    @Override
    public boolean validate() {

        super.validate();

        if (StringUtils.isBlank(buyerLogonId) && StringUtils.isBlank(buyerId)) {
            throw new RuntimeException("买家支付宝账号和买家用户号不能同时为空");
        }

        return true;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

}
