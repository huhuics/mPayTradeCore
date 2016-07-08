package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAlipayPayOrder extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -2330876486504112476L;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.id
     */
    private String            id;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.acquirer_id
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.merchant_id
     */
    private String            merchantId;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.alipay_trade_no
     */
    private String            alipayTradeNo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.out_trade_no
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.order_status
     */
    private String            orderStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.cancel_status
     */
    private String            cancelStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.refund_status
     */
    private String            refundStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.pay_detail
     */
    private String            payDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.auth_code
     */
    private String            authCode;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.seller_id
     */
    private String            sellerId;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.total_amount
     */
    private Money             totalAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.receipt_amount
     */
    private Money             receiptAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.discountable_amount
     */
    private Money             discountableAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.undiscountable_amount
     */
    private Money             undiscountableAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.subject
     */
    private String            subject;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.body
     */
    private String            body;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.goods_detail
     */
    private String            goodsDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.merchant_detail
     */
    private String            merchantDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.extend_params
     */
    private String            extendParams;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.timeout_express
     */
    private String            timeoutExpress;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.royalty_info
     */
    private String            royaltyInfo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.fund_bill_list
     */
    private String            fundBillList;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.discount_goods_detail
     */
    private String            discountGoodsDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.error_detail
     */
    private String            errorDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.check_status
     */
    private String            checkStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.check_date
     */
    private String            checkDate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.create_date
     */
    private String            createDate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.gmt_create
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.gmt_update
     */
    private Date              gmtUpdate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.gmt_pay_success
     */
    private Date              gmtPaySuccess;

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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus == null ? null : orderStatus.trim();
    }

    public String getCancelStatus() {
        return cancelStatus;
    }

    public void setCancelStatus(String cancelStatus) {
        this.cancelStatus = cancelStatus == null ? null : cancelStatus.trim();
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus == null ? null : refundStatus.trim();
    }

    public String getPayDetail() {
        return payDetail;
    }

    public void setPayDetail(String payDetail) {
        this.payDetail = payDetail == null ? null : payDetail.trim();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId == null ? null : sellerId.trim();
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Money getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(Money receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public Money getDiscountableAmount() {
        return discountableAmount;
    }

    public void setDiscountableAmount(Money discountableAmount) {
        this.discountableAmount = discountableAmount;
    }

    public Money getUndiscountableAmount() {
        return undiscountableAmount;
    }

    public void setUndiscountableAmount(Money undiscountableAmount) {
        this.undiscountableAmount = undiscountableAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body == null ? null : body.trim();
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail == null ? null : goodsDetail.trim();
    }

    public String getMerchantDetail() {
        return merchantDetail;
    }

    public void setMerchantDetail(String merchantDetail) {
        this.merchantDetail = merchantDetail == null ? null : merchantDetail.trim();
    }

    public String getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(String extendParams) {
        this.extendParams = extendParams == null ? null : extendParams.trim();
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress == null ? null : timeoutExpress.trim();
    }

    public String getRoyaltyInfo() {
        return royaltyInfo;
    }

    public void setRoyaltyInfo(String royaltyInfo) {
        this.royaltyInfo = royaltyInfo == null ? null : royaltyInfo.trim();
    }

    public String getFundBillList() {
        return fundBillList;
    }

    public void setFundBillList(String fundBillList) {
        this.fundBillList = fundBillList == null ? null : fundBillList.trim();
    }

    public String getDiscountGoodsDetail() {
        return discountGoodsDetail;
    }

    public void setDiscountGoodsDetail(String discountGoodsDetail) {
        this.discountGoodsDetail = discountGoodsDetail == null ? null : discountGoodsDetail.trim();
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail == null ? null : errorDetail.trim();
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

    public Date getGmtPaySuccess() {
        return gmtPaySuccess;
    }

    public void setGmtPaySuccess(Date gmtPaySuccess) {
        this.gmtPaySuccess = gmtPaySuccess;
    }
}