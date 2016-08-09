package org.tradecore.dao.domain;

import java.util.Date;

public class BizMerchantInfo extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -3025880138657985716L;

    /**
     *  null, BIZ_MERCHANT_INFO.ID
     */
    private String            id;

    /**
     *  null, BIZ_MERCHANT_INFO.ACQUIRER_ID
     */
    private String            acquirerId;

    /**
     *  null, BIZ_MERCHANT_INFO.EXTERNAL_ID
     */
    private String            externalId;

    /**
     *  null, BIZ_MERCHANT_INFO.OUT_EXTERNAL_ID
     */
    private String            outExternalId;

    /**
     *  null, BIZ_MERCHANT_INFO.MERCHANT_ID
     */
    private String            merchantId;

    /**
     *  null, BIZ_MERCHANT_INFO.NAME
     */
    private String            name;

    /**
     *  null, BIZ_MERCHANT_INFO.ALIAS_NAME
     */
    private String            aliasName;

    /**
     *  null, BIZ_MERCHANT_INFO.SERVICE_PHONE
     */
    private String            servicePhone;

    /**
     *  null, BIZ_MERCHANT_INFO.CONTACT_NAME
     */
    private String            contactName;

    /**
     *  null, BIZ_MERCHANT_INFO.CONTACT_PHONE
     */
    private String            contactPhone;

    /**
     *  null, BIZ_MERCHANT_INFO.CONTACT_MOBILE
     */
    private String            contactMobile;

    /**
     *  null, BIZ_MERCHANT_INFO.CONTACT_EMAIL
     */
    private String            contactEmail;

    /**
     *  null, BIZ_MERCHANT_INFO.CATEGORY_ID
     */
    private String            categoryId;

    /**
     *  null, BIZ_MERCHANT_INFO.SOURCE
     */
    private String            source;

    /**
     *  null, BIZ_MERCHANT_INFO.MEMO
     */
    private String            memo;

    /**
     *  null, BIZ_MERCHANT_INFO.STATUS
     */
    private String            status;

    /**
     *  null, BIZ_MERCHANT_INFO.RETURN_DETAIL
     */
    private String            returnDetail;

    /**
     *  null, BIZ_MERCHANT_INFO.GMT_CREATE
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_MERCHANT_INFO.GMT_UPDATE
     */
    private Date              gmtUpdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId == null ? null : acquirerId.trim();
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId == null ? null : externalId.trim();
    }

    public String getOutExternalId() {
        return outExternalId;
    }

    public void setOutExternalId(String outExternalId) {
        this.outExternalId = outExternalId == null ? null : outExternalId.trim();
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName == null ? null : aliasName.trim();
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone == null ? null : servicePhone.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile == null ? null : contactMobile.trim();
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail == null ? null : contactEmail.trim();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getReturnDetail() {
        return returnDetail;
    }

    public void setReturnDetail(String returnDetail) {
        this.returnDetail = returnDetail == null ? null : returnDetail.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}