/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;

/**
 * 商户修改请求
 * @author HuHui
 * @version $Id: MerchantModifyRequest.java, v 0.1 2016年8月2日 下午5:03:16 HuHui Exp $
 */
public class MerchantModifyRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = -3730532500978279060L;

    /**
     *  (必填)收单机构号
     */
    private String            acquirer_id;

    /**
     * (结算中心生成)结算中心商户外部编号，全局唯一。生成规则：收单机构号+商户外部编号
     */
    private String            external_id;

    /**
     * (必填)商户外部编号,一个受理机构下唯一,即商户在收单机构的商户标识
     */
    private String            out_external_id;

    /**
     *  (必填)商户识别号
     */
    private String            merchant_id;

    /**
     *  (必填)商户识别号(传支付宝)
     */
    private String            sub_merchant_id;

    /**
     *  (必填)商户简称
     */
    private String            alias_name;

    /**
     *  (必填)客服电话
     */
    private String            service_phone;

    /**
     * 联系人名称
     */
    private String            contact_name;

    /**
     * 商户所在省份编码
     */
    private String            province_code;

    /**
     * 商户所在城市编码
     */
    private String            city_code;

    /**
     * 商户所在区县编码
     */
    private String            district_code;

    /**
     * 商户详细经营地址
     */
    private String            address;

    /**
     *  商户来源标识
     */
    private String            source;

    /**
     * 非空参数校验
     * @return
     */
    @Override
    public void validate() {

        AssertUtil.assertNotBlank(acquirer_id, "收单机构号不能为空");

        if (StringUtils.isBlank(out_external_id) && StringUtils.isBlank(sub_merchant_id)) {
            throw new RuntimeException("商户外部编号和商户识别号不能同时为空");
        }

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

    public String getAlias_name() {
        return alias_name;
    }

    public void setAlias_name(String alias_name) {
        this.alias_name = alias_name;
    }

    public String getService_phone() {
        return service_phone;
    }

    public void setService_phone(String service_phone) {
        this.service_phone = service_phone;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSub_merchant_id() {
        return sub_merchant_id;
    }

    public void setSub_merchant_id(String sub_merchant_id) {
        this.sub_merchant_id = sub_merchant_id;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
