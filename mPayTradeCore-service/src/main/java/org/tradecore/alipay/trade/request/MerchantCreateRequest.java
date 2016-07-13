/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import org.tradecore.common.util.AssertUtil;

/**
 * 商户入驻请求接口
 * @author HuHui
 * @version $Id: MerchantCreateRequest.java, v 0.1 2016年7月13日 下午7:59:35 HuHui Exp $
 */
public class MerchantCreateRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = 3018493046457767617L;

    /**
     * (必填)商户外部编号,一个受理机构下唯一,即商户在收单机构的商户标识
     */
    private String            externalId;

    /**
     *  (必填)收单机构号
     */
    private String            acquirerId;

    /**
     *  商户识别号
     */
    private String            merchantId;

    /**
     *  (必填)商户名称
     */
    private String            name;

    /**
     *  (必填)商户简称
     */
    private String            aliasName;

    /**
     *  (必填)客服电话
     */
    private String            servicePhone;

    /**
     * 联系人名称
     */
    private String            contactName;

    /**
     * 联系人电话
     */
    private String            contactPhone;

    /**
     *  联系人手机号
     */
    private String            contactMobile;

    /**
     *  联系人邮箱
     */
    private String            contactEmail;

    /**
     *  (必填)经营类目id
     */
    private String            categoryId;

    /**
     *  商户来源标识
     */
    private String            source;

    /**
     *  商户备注
     */
    private String            memo;

    /**
     *  商户状态
     */
    private String            status;

    /**
     *  支付宝返回数据详情
     */
    private String            returnDetail;

    /**
     * 应用授权令牌
     */
    private String            appAuthToken;

    /**
     * 非空参数校验
     * @return
     */
    public boolean validate() {

        AssertUtil.assertNotEmpty(externalId, "商户外部编号不能为空");

        AssertUtil.assertNotEmpty(acquirerId, "收单机构号不能为空");

        AssertUtil.assertNotEmpty(name, "商户名称不能为空");

        AssertUtil.assertNotEmpty(aliasName, "商户简称不能为空");

        AssertUtil.assertNotEmpty(servicePhone, "客服电话不能为空");

        AssertUtil.assertNotEmpty(categoryId, "经营类目编号不能为空");

        return true;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturnDetail() {
        return returnDetail;
    }

    public void setReturnDetail(String returnDetail) {
        this.returnDetail = returnDetail;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

}
