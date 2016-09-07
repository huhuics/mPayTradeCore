/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退款请求
 * @author HuHui
 * @version $Id: RefundRequest.java, v 0.1 2016年7月11日 上午10:50:48 HuHui Exp $
 */
public class RefundRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = 3726649151694076583L;

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
     * (必填)退款金额，该金额不能大于订单金额
     */
    @JSONField(name = "refund_amount")
    private String            refundAmount;

    /**
     * (可选，需要支持多次退款时必填)标识一次退款请求<br>
     * 同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传
     */
    @JSONField(name = "out_request_no")
    private String            outRequestNo;

    /**
     * 退款原因，可以说明用户退款原因，方便为商家后台提供统计
     */
    @JSONField(name = "refund_reason")
    private String            refundReason;

    /**
     * 商户门店编号
     */
    @JSONField(name = "store_id")
    private String            storeId;

    /**
     * 支付宝商家平台中配置的商户门店号
     */
    @JSONField(name = "alipay_store_id")
    private String            alipayStoreId;

    /**
     * 商户机具终端编号，当以机具方式接入支付宝时必传
     */
    @JSONField(name = "terminal_id")
    private String            terminalId;

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

        AssertUtil.assertNotEmpty(refundAmount, "退款金额不能为空");

        AssertUtil.assertNotEmpty(outRequestNo, "退款流水号不能为空");

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

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlipayStoreId() {
        return alipayStoreId;
    }

    public void setAlipayStoreId(String alipayStoreId) {
        this.alipayStoreId = alipayStoreId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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
