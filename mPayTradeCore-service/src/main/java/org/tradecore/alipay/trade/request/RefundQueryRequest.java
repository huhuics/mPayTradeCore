/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 查询退款订单请求
 * @author HuHui
 * @version $Id: RefundQueryRequest.java, v 0.1 2016年7月25日 下午9:01:30 HuHui Exp $
 */
public class RefundQueryRequest extends BaseRequest {

    /** uid */
    private static final long serialVersionUID = 428777095477577597L;

    /** (必填)收单机构编号 */
    private String            acquirerId;

    /** (必填)商户识别号 */
    private String            merchantId;

    /** (特殊可选)商户网站订单系统中唯一订单号，和支付宝交易号不能同时为空 */
    @JSONField(name = "out_trade_no")
    private String            outTradeNo;

    /** (特殊可选)支付宝交易号，和商户订单号不能同时为空 */
    @JSONField(name = "trade_no")
    private String            alipayTradeNo;

    /** (必填)退款请求号 */
    @JSONField(name = "out_request_no")
    private String            outRequestNo;

    /**
     * 应用授权令牌
     */
    @JSONField(name = "app_auth_token")
    private String            appAuthToken;

    /** 退款状态 */
    private String            refundStatus;

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

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    @Override
    public boolean validate() {

        AssertUtil.assertNotEmpty(acquirerId, "收单机构编号不能为空");

        AssertUtil.assertNotEmpty(merchantId, "商户标识号不能为空");

        AssertUtil.assertNotEmpty(outRequestNo, "退款请求号不能为空");

        if (StringUtils.isBlank(outTradeNo) && StringUtils.isBlank(alipayTradeNo)) {
            throw new RuntimeException("商户订单号和支付宝交易号不能同时为空");
        }

        return true;
    }

}
