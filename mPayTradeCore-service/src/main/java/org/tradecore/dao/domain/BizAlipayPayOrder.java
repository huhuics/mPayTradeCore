package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAlipayPayOrder extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 2592153417918971002L;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.ID
     */
    private String            id;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.ACQUIRER_ID
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.MERCHANT_ID
     */
    private String            merchantId;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.TRADE_NO
     */
    private String            tradeNo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.ALIPAY_TRADE_NO
     */
    private String            alipayTradeNo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.OUT_TRADE_NO
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.ORDER_STATUS
     */
    private String            orderStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.CANCEL_STATUS
     */
    private String            cancelStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.REFUND_STATUS
     */
    private String            refundStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.SCENE
     */
    private String            scene;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.AUTH_CODE
     */
    private String            authCode;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.ACCOUNT_DETAIL
     */
    private String            accountDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.TOTAL_AMOUNT
     */
    private Money             totalAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.RECEIPT_AMOUNT
     */
    private Money             receiptAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.DISCOUNTABLE_AMOUNT
     */
    private Money             discountableAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.UNDISCOUNTABLE_AMOUNT
     */
    private Money             undiscountableAmount;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.SUBJECT
     */
    private String            subject;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.BODY
     */
    private String            body;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.APP_AUTH_TOKEN
     */
    private String            appAuthToken;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.GOODS_DETAIL
     */
    private String            goodsDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.MERCHANT_DETAIL
     */
    private String            merchantDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.EXTEND_PARAMS
     */
    private String            extendParams;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.TIMEOUT_EXPRESS
     */
    private String            timeoutExpress;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.FUND_BILL_LIST
     */
    private String            fundBillList;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.DISCOUNT_GOODS_DETAIL
     */
    private String            discountGoodsDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.QR_CODE
     */
    private String            qrCode;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.RETURN_DETAIL
     */
    private String            returnDetail;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.OUT_NOTIFY_URL
     */
    private String            outNotifyUrl;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.NOTIFY_BODY
     */
    private String            notifyBody;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.CHECK_STATUS
     */
    private String            checkStatus;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.CHECK_DATE
     */
    private String            checkDate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.CREATE_DATE
     */
    private String            createDate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.GMT_PAYMENT
     */
    private Date              gmtPayment;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.GMT_CREATE
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ALIPAY_PAY_ORDER.GMT_UPDATE
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
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

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene == null ? null : scene.trim();
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode == null ? null : authCode.trim();
    }

    public String getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(String accountDetail) {
        this.accountDetail = accountDetail == null ? null : accountDetail.trim();
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

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken == null ? null : appAuthToken.trim();
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode == null ? null : qrCode.trim();
    }

    public String getReturnDetail() {
        return returnDetail;
    }

    public void setReturnDetail(String returnDetail) {
        this.returnDetail = returnDetail == null ? null : returnDetail.trim();
    }

    public String getOutNotifyUrl() {
        return outNotifyUrl;
    }

    public void setOutNotifyUrl(String outNotifyUrl) {
        this.outNotifyUrl = outNotifyUrl == null ? null : outNotifyUrl.trim();
    }

    public String getNotifyBody() {
        return notifyBody;
    }

    public void setNotifyBody(String notifyBody) {
        this.notifyBody = notifyBody == null ? null : notifyBody.trim();
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

    public Date getGmtPayment() {
        return gmtPayment;
    }

    public void setGmtPayment(Date gmtPayment) {
        this.gmtPayment = gmtPayment;
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