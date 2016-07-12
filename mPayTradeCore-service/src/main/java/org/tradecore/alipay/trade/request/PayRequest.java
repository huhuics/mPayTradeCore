/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import java.util.regex.Pattern;

import org.tradecore.common.util.AssertUtil;

/**
 * 条码支付请求
 * @author HuHui
 * @version $Id: PayRequest.java, v 0.1 2016年7月8日 下午3:28:19 HuHui Exp $
 */
public class PayRequest extends DefaultPayRequest {

    /**  */
    private static final long serialVersionUID = -4651634291538225127L;

    /** (必填)付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码 */
    private String            authCode;

    /**
     * 参数校验
     * @return 校验是否成功
     */
    public boolean validate() {

        AssertUtil.assertTrue(super.validate(), "参数校验错误");

        AssertUtil.assertNotEmpty(authCode, "交易授权码不能为空");

        if (!Pattern.matches("^\\d{10,}$", authCode)) {
            throw new IllegalStateException("交易授权码格式错误");
        }

        return true;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

}
