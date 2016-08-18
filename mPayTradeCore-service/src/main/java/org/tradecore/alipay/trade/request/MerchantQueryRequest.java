/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;

/**
 * 商户信息查询请求
 * @author HuHui
 * @version $Id: MerchantQueryRequest.java, v 0.1 2016年7月13日 下午9:21:54 HuHui Exp $
 */
public class MerchantQueryRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = -1718235311954569610L;

    /**
     * (必填)收单机构编号
     */
    private String            acquirer_id;

    /**
     * (结算中心生成)结算中心商户外部编号，全局唯一。生成规则：收单机构号+商户外部编号
     */
    private String            external_id;

    /**
     * (特殊可选)商户外部编号,一个受理机构下唯一,即商户在收单机构的商户标识
     */
    private String            out_external_id;

    /**
     * (特殊可选)商户标识号
     */
    private String            merchant_id;

    /**
     * 非空参数校验
     * @return
     */
    @Override
    public boolean validate() {

        AssertUtil.assertNotEmpty(acquirer_id, "收单机构编号不能为空");

        if (StringUtils.isBlank(out_external_id) && StringUtils.isBlank(merchant_id)) {
            throw new RuntimeException("商户外部编号和商户标识号不能同时为空");
        }

        return true;
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

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getOut_external_id() {
        return out_external_id;
    }

    public void setOut_external_id(String out_external_id) {
        this.out_external_id = out_external_id;
    }

}
