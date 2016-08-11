/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.model;

import org.tradecore.dao.domain.BaseDomain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 交易支付的渠道信息
 * @author HuHui
 * @version $Id: FundBill.java, v 0.1 2016年8月11日 下午12:29:39 HuHui Exp $
 */
public class FundBill extends BaseDomain {

    /** uid */
    private static final long serialVersionUID = -4246472405738095231L;

    /**
     * 该支付工具类型所使用的金额
     */
    @JSONField(name = "amount")
    private String            amount;

    /**
     * 交易使用的资金渠道
     */
    @JSONField(name = "fund_channel")
    private String            fundChannel;

    /**
     * 渠道实际付款金额
     */
    @JSONField(name = "real_amount")
    private String            realAmount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFundChannel() {
        return fundChannel;
    }

    public void setFundChannel(String fundChannel) {
        this.fundChannel = fundChannel;
    }

    public String getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(String realAmount) {
        this.realAmount = realAmount;
    }

}
