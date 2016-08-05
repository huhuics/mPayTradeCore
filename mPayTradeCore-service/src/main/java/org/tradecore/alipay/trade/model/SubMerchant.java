/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.model;

import org.tradecore.dao.domain.BaseDomain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 二级商户对象
 * @author HuHui
 * @version $Id: SubMerchant.java, v 0.1 2016年8月5日 下午8:17:09 HuHui Exp $
 */
public class SubMerchant extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 2897946678264670866L;

    /** 商户标识号 */
    @JSONField(name = "merchant_id")
    private String            merchantId;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

}
