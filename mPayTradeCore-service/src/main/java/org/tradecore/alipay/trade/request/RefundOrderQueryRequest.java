/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

/**
 * 查询退款订单请求
 * @author HuHui
 * @version $Id: RefundOrderQueryRequest.java, v 0.1 2016年7月25日 下午9:01:30 HuHui Exp $
 */
public class RefundOrderQueryRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = 428777095477577597L;

    private String            merchantId;

    private String            outTradeNo;

    private String            alipayTradeNo;

    private String            outRequestNo;

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

}
