/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

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
     * (必填)商户外部编号,一个受理机构下唯一,即商户在收单机构的商户标识
     */
    private String            external_id;

    /**
     *  (必填)收单机构号
     */
    private String            acquirer_id;

    /**
     *  (必填)商户识别号
     */
    private String            merchant_id;

    /**
     *  (必填)商户名称
     */
    private String            name;

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
     * 联系人电话
     */
    private String            contact_phone;

    /**
     *  联系人手机号
     */
    private String            contact_mobile;

    /**
     *  联系人邮箱
     */
    private String            contact_email;

    /**
     *  (必填)经营类目id
     */
    private String            category_id;

    /**
     *  商户来源标识
     */
    private String            source;

    /**
     *  商户备注
     */
    private String            memo;

    /**
     * 非空参数校验
     * @return
     */
    @Override
    public boolean validate() {

        AssertUtil.assertNotEmpty(external_id, "商户外部编号不能为空");

        AssertUtil.assertNotEmpty(acquirer_id, "收单机构号不能为空");

        AssertUtil.assertNotEmpty(merchant_id, "商户标识号不能为空");

        AssertUtil.assertNotEmpty(name, "商户名称不能为空");

        AssertUtil.assertNotEmpty(alias_name, "商户简称不能为空");

        AssertUtil.assertNotEmpty(service_phone, "客服电话不能为空");

        AssertUtil.assertNotEmpty(category_id, "经营类目编号不能为空");

        return true;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }

    public String getContact_email() {
        return contact_email;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
