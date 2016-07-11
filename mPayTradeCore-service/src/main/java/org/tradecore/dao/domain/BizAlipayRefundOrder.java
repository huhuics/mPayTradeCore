package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAlipayRefundOrder extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -4916593202439536547L;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.ID
     */
    private String            id;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.ACQUIRER_ID
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.MERCHANT_ID
     */
    private String            merchantId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.ALIPAY_TRADE_NO
     */
    private String            alipayTradeNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.OUT_TRADE_NO
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.TOTAL_AMOUNT
     */
    private Money             totalAmount;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.REFUND_AMOUNT
     */
    private Money             refundAmount;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.SEND_BACK_FEE
     */
    private Money             sendBackFee;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.REFUND_REASON
     */
    private String            refundReason;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.OUT_REQUEST_NO
     */
    private String            outRequestNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.REFUND_STATUS
     */
    private String            refundStatus;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.MERCHANT_DETAIL
     */
    private String            merchantDetail;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.FUND_CHANGE
     */
    private String            fundChange;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.REFUND_DETAIL_ITEM_LIST
     */
    private String            refundDetailItemList;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.BUYER_USER_ID
     */
    private String            buyerUserId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.CHECK_STATUS
     */
    private String            checkStatus;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.CHECK_DATE
     */
    private String            checkDate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.RETURN_DETAIL
     */
    private String            returnDetail;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.CREATE_DATE
     */
    private String            createDate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.GMT_REFUND_PAY
     */
    private Date              gmtRefundPay;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.GMT_CREATE
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.GMT_UPDATE
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

    public Money getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(Money refundAmount) {
        this.refundAmount = refundAmount;
    }

    public Money getSendBackFee() {
        return sendBackFee;
    }

    public void setSendBackFee(Money sendBackFee) {
        this.sendBackFee = sendBackFee;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason == null ? null : refundReason.trim();
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo == null ? null : outRequestNo.trim();
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus == null ? null : refundStatus.trim();
    }

    public String getMerchantDetail() {
        return merchantDetail;
    }

    public void setMerchantDetail(String merchantDetail) {
        this.merchantDetail = merchantDetail == null ? null : merchantDetail.trim();
    }

    public String getFundChange() {
        return fundChange;
    }

    public void setFundChange(String fundChange) {
        this.fundChange = fundChange == null ? null : fundChange.trim();
    }

    public String getRefundDetailItemList() {
        return refundDetailItemList;
    }

    public void setRefundDetailItemList(String refundDetailItemList) {
        this.refundDetailItemList = refundDetailItemList == null ? null : refundDetailItemList.trim();
    }

    public String getBuyerUserId() {
        return buyerUserId;
    }

    public void setBuyerUserId(String buyerUserId) {
        this.buyerUserId = buyerUserId == null ? null : buyerUserId.trim();
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate == null ? null : checkDate.trim();
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

    public Date getGmtRefundPay() {
        return gmtRefundPay;
    }

    public void setGmtRefundPay(Date gmtRefundPay) {
        this.gmtRefundPay = gmtRefundPay;
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