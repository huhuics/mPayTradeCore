/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.tradecore.common.util.AssertUtil;

/**
 * 撤销请求
 * @author HuHui
 * @version $Id: CancelRequest.java, v 0.1 2016年7月12日 下午2:26:06 HuHui Exp $
 */
public class CancelRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = -467059391745534004L;

    /** (必填)收单机构编号 */
    private String            acquirerId;

    /** (必填)商户识别号 */
    private String            merchantId;

    /**
     * (必填)商户网站订单系统中唯一订单号
     */
    private String            outTradeNo;

    /**
     * 应用授权令牌
     */
    private String            appAuthToken;

    /**
     * 参数校验
     * @return
     */
    public boolean validate() {

        AssertUtil.assertNotEmpty(acquirerId, "收单机构编号不能为空");

        AssertUtil.assertNotEmpty(merchantId, "商户标识号不能为空");

        AssertUtil.assertNotEmpty(outTradeNo, "商户订单号不能为空");

        return true;
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

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

}
