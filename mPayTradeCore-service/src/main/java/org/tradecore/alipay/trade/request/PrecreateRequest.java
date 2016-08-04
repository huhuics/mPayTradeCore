/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.tradecore.common.util.AssertUtil;

/**
 * 预下单(生成二维码)请求
 * @author HuHui
 * @version $Id: PrecreateRequest.java, v 0.1 2016年7月11日 上午11:46:17 HuHui Exp $
 */
public class PrecreateRequest extends DefaultPayRequest {

    /**  */
    private static final long serialVersionUID = -7844292619083824239L;

    /**
     * (必填)支付结果通知URL，此URL是由收单机构传给结算中心，结算中心将此URL保留
     */
    private String            outNotifyUrl;

    /**
     * (必填)支付结果通知URL，此URL是由结算中心填写，并传给支付宝
     */
    private String            notifyUrl;

    /**
     * 参数校验
     * @return 校验是否成功
     */
    @Override
    public boolean validate() {

        super.validate();

        AssertUtil.assertNotEmpty(outNotifyUrl, "商户支付结果通知URL不能为空");

        AssertUtil.assertNotEmpty(notifyUrl, "结算中心支付结果通知URL不能为空");

        return true;
    }

    public String getOutNotifyUrl() {
        return outNotifyUrl;
    }

    public void setOutNotifyUrl(String outNotifyUrl) {
        this.outNotifyUrl = outNotifyUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

}
