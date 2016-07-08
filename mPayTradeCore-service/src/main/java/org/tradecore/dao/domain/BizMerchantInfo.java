package org.tradecore.dao.domain;

import java.util.Date;

public class BizMerchantInfo extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 93874513828344486L;

    /**
     *  null, BIZ_MERCHANT_INFO.ID
     */
    private Short             id;

    /**
     *  null, BIZ_MERCHANT_INFO.external_id
     */
    private String            externalId;

    /**
     *  null, BIZ_MERCHANT_INFO.acquirer_id
     */
    private String            acquirerId;

    /**
     *  null, BIZ_MERCHANT_INFO.merchant_id
     */
    private String            merchantId;

    /**
     *  null, BIZ_MERCHANT_INFO.name
     */
    private String            name;

    /**
     *  null, BIZ_MERCHANT_INFO.alias_name
     */
    private String            aliasName;

    /**
     *  null, BIZ_MERCHANT_INFO.service_phone
     */
    private String            servicePhone;

    /**
     *  null, BIZ_MERCHANT_INFO.contact_name
     */
    private String            contactName;

    /**
     *  null, BIZ_MERCHANT_INFO.contact_phone
     */
    private String            contactPhone;

    /**
     *  null, BIZ_MERCHANT_INFO.contact_mobile
     */
    private String            contactMobile;

    /**
     *  null, BIZ_MERCHANT_INFO.contact_email
     */
    private String            contactEmail;

    /**
     *  null, BIZ_MERCHANT_INFO.category_id
     */
    private String            categoryId;

    /**
     *  null, BIZ_MERCHANT_INFO.memo
     */
    private String            memo;

    /**
     *  null, BIZ_MERCHANT_INFO.status
     */
    private String            status;

    /**
     *  null, BIZ_MERCHANT_INFO.gmt_create
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_MERCHANT_INFO.gmt_update
     */
    private Date              gmtUpdate;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId == null ? null : externalId.trim();
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId == null ? null : acquirerId.trim();
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