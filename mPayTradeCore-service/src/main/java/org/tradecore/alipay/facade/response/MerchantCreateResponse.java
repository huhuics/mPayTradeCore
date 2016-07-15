/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.facade.response;

/**
 * 商户信息DTO
 * @author HuHui
 * @version $Id: MerchantCreateResponse.java, v 0.1 2016年7月15日 上午12:20:25 HuHui Exp $
 */
public class MerchantCreateResponse extends BaseResponse {

    /** uid */
    private static final long serialVersionUID = 4668599991968331916L;

    /** 收单机构编号 */
    private String            acquirerId;

    /** 商户标识号 */
    private String            merchantId;

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

}
