/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.facade.response;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 商户入驻响应
 * @author HuHui
 * @version $Id: MerchantCreateResponse.java, v 0.1 2016年7月15日 上午12:20:25 HuHui Exp $
 */
public class MerchantCreateResponse extends BaseResponse {

    /** uid */
    private static final long serialVersionUID = 4668599991968331916L;

    /** 收单机构编号 */
    private String            acquirer_id;

    /** 商户标识号 */
    private String            merchant_id;

    public String getAcquirer_id() {
        return acquirer_id;
    }

    public void setAcquirer_id(String acquirer_id) {
        this.acquirer_id = acquirer_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    /**
     * 将不为空的参数放入TreeMap，用于签名
     */
    public Map<String, String> buildSortedParaMap() {
        Map<String, String> paraMap = super.buildSortedParaMap();
        if (StringUtils.isNotBlank(acquirer_id)) {
            paraMap.put("acquirer_id", acquirer_id);
        }
        if (StringUtils.isNotBlank(merchant_id)) {
            paraMap.put("merchant_id", merchant_id);
        }

        return paraMap;
    }

}
