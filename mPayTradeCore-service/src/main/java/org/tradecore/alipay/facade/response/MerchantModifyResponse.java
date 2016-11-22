/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.facade.response;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 商户信息修改响应
 * @author HuHui
 * @version $Id: MerchantModifyResponse.java, v 0.1 2016年8月2日 下午4:40:08 HuHui Exp $
 */
public class MerchantModifyResponse extends BaseResponse {

    /**  */
    private static final long serialVersionUID = 6552349960813651314L;

    private String            acquirer_id;

    private String            merchant_id;

    /**
     * 将不为空的参数放入TreeMap，用于签名
     */
    public Map<String, Object> buildSortedParaMap() {
        Map<String, Object> paraMap = super.buildSortedParaMap();
        if (StringUtils.isNotBlank(acquirer_id)) {
            paraMap.put("acquirer_id", acquirer_id);
        }
        if (StringUtils.isNotBlank(merchant_id)) {
            paraMap.put("merchant_id", merchant_id);
        }

        return paraMap;
    }

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

}
