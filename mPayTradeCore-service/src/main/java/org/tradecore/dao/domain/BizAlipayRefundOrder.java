package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAlipayRefundOrder extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 1672370574965415758L;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.id
     */
    private String            id;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.acquirer_id
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.merchant_id
     */
    private String            merchantId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.alipay_trade_no
     */
    private String            alipayTradeNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.out_trade_no
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.total_amount
     */
    private Money             totalAmount;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.refund_amount
     */
    private Money             refundAmount;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.send_back_fee
     */
    private Long              sendBackFee;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.refund_reason
     */
    private String            refundReason;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.out_request_no
     */
    private String            outRequestNo;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.refund_status
     */
    private String            refundStatus;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.merchant_detail
     */
    private String            merchantDetail;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.fund_change
     */
    private String            fundChange;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.refund_detail_item_list
     */
    private String            refundDetailItemList;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.buyer_user_id
     */
    private String            buyerUserId;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.check_status
     */
    private String            checkStatus;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.check_date
     */
    private String            checkDate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.error_detail
     */
    private String            errorDetail;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.create_date
     */
    private String            createDate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.gmt_refund_pay
     */
    private Date              gmtRefundPay;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.gmt_create
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ALIPAY_REFUND_ORDER.gmt_update
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

    public Long getSendBackFee() {
        return sendBackFee;
    }

    public void setSendBackFee(Long sendBackFee) {
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

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail == null ? null : errorDetail.trim();
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