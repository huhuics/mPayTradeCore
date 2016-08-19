/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订单查询请求
 * @author HuHui
 * @version $Id: QueryRequest.java, v 0.1 2016年7月10日 下午9:11:30 HuHui Exp $
 */
public class QueryRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = -8770281296107422912L;

    /** (必填)收单机构编号 */
    @JSONField(name = "acquirer_id")
    private String            acquirerId;

    /** (必填)商户识别号 */
    @JSONField(name = "merchant_id")
    private String            merchantId;

    /**
     * (特殊可选)商户网站订单系统中唯一订单号，和支付宝交易号不能同时为空
     */
    private String            outTradeNo;

    /**
     * (特殊可选)支付宝交易号，和商户订单号不能同时为空
     */
    @JSONField(name = "trade_no")
    private String            alipayTradeNo;

    /**
     * (结算中心自动生成)结算中心订单号<br>
     * 在支付宝角度看，结算中心订单号为outTradeNo
     */
    @JSONField(name = "out_trade_no")
    protected String          tradeNo;

    /**
     * 应用授权令牌
     */
    @JSONField(name = "app_auth_token")
    private String            appAuthToken;

    /**
     * 参数校验
     * @return 校验是否成功
     */
    @Override
    public void validate() {

        AssertUtil.assertNotEmpty(acquirerId, "收单机构编号不能为空");

        AssertUtil.assertNotEmpty(merchantId, "商户标识号不能为空");

        if (StringUtils.isBlank(outTradeNo) && StringUtils.isBlank(alipayTradeNo)) {
            throw new RuntimeException("商户订单号和支付宝交易号不能同时为空");
        }

    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getAlipayTradeNo() {
        return alipayTradeNo;
    }

    public void setAlipayTradeNo(String alipayTradeNo) {
        this.alipayTradeNo = alipayTradeNo;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

}
