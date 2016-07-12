package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAlipayCancelOrder extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 9171074305029654001L;
    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.ID
     */
    private String            id;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.ACQUIRER_ID
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.MERCHANT_ID
     */
    private String            merchantId;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.ALIPAY_TRADE_NO
     */
    private String            alipayTradeNo;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.OUT_TRADE_NO
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.TOTAL_AMOUNT
     */
    private Money             totalAmount;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.RETRY_FLAG
     */
    private String            retryFlag;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.ACTION
     */
    private String            action;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.CANCEL_STATUS
     */
    private String            cancelStatus;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.RETURN_DETAIL
     */
    private String            returnDetail;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.CREATE_DATE
     */
    private String            createDate;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.GMT_CREATE
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ALIPAY_CANCEL_ORDER.GMT_UPDATE
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

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    public String getAlipayTradeNo() {
        return alipayTradeNo;
    }

    public void setAlipayTradeNo(String alipayTradeNo) {
        this.alipayTradeNo = alipayTradeNo == null ? null : alipayTradeNo.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(String retryFlag) {
        this.retryFlag = retryFlag == null ? null : retryFlag.trim();
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus == null ? null : cancelStatus.trim();
    }

    public String getReturnDetail() {
        return returnDetail;
    }

    public void setReturnDetail(String returnDetail) {
        this.returnDetail = returnDetail == null ? null : returnDetail.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
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