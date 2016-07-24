/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;

/**
 * 支付宝扫码支付结果异步通知请求
 * @author HuHui
 * @version $Id: NotifyRequest.java, v 0.1 2016年7月13日 上午9:25:19 HuHui Exp $
 */
@Deprecated
public class NotifyRequest extends BaseRequest {

    /**  */
    private static final long serialVersionUID = -9178298495401897538L;

    private String            notify_time;

    private String            notify_type;

    private String            notify_id;

    private String            sign_type;

    private String            sign;

    private String            trade_no;

    private String            app_id;

    private String            out_trade_no;

    private String            out_biz_no;

    private String            buyer_id;

    private String            buyer_logon_id;

    private String            seller_id;

    private String            seller_email;

    private String            trade_status;

    private String            total_amount;

    private String            receipt_amount;

    private String            invoice_amount;

    private String            buyer_pay_amount;

    private String            point_amount;

    private String            refund_fee;

    private String            send_back_fee;

    private String            subject;

    private String            body;

    private Date              gmt_create;

    private Date              gmt_payment;

    private Date              gmt_refund;

    private Date              gmt_close;

    private String            fund_bill_list;

    /**
     * 创建post请求需要的List<br>
     * 接收来自buildSortedParaMap方法的返回值，然后添加
     * @throws UnsupportedEncodingException 
     */
    public List<NameValuePair> buildPostParaList(Map<String, String> paraMap) {

        paraMap.put("sign", sign);
        paraMap.put("sign_type", sign_type);

        List<NameValuePair> pairList = new ArrayList<NameValuePair>(paraMap.size());

        for (String key : paraMap.keySet()) {
            NameValuePair nvPair = new NameValuePair(key, paraMap.get(key));
            pairList.add(nvPair);
        }

        return pairList;

    }

    /**
     * 将不为空的参数放入TreeMap，用于签名<br>
     * 注意：组装参数时排除了sign和sign_type两个参数
     */
    public Map<String, String> buildSortedParaMap() {
        Map<String, String> paraMap = new TreeMap<String, String>();

        if (StringUtils.isNotBlank(notify_time)) {
            paraMap.put("notify_time", notify_time);
        }
        if (StringUtils.isNotBlank(notify_type)) {
            paraMap.put("notify_type", notify_type);
        }
        if (StringUtils.isNotBlank(notify_id)) {
            paraMap.put("notify_id", notify_id);
        }
        if (StringUtils.isNotBlank(trade_no)) {
            paraMap.put("trade_no", trade_no);
        }
        if (StringUtils.isNotBlank(app_id)) {
            paraMap.put("app_id", app_id);
        }
        if (StringUtils.isNotBlank(out_trade_no)) {
            paraMap.put("out_trade_no", out_trade_no);
        }
        if (StringUtils.isNotBlank(out_biz_no)) {
            paraMap.put("out_biz_no", out_biz_no);
        }
        if (StringUtils.isNotBlank(buyer_id)) {
            paraMap.put("buyer_id", buyer_id);
        }
        if (StringUtils.isNotBlank(buyer_logon_id)) {
            paraMap.put("buyer_logon_id", buyer_logon_id);
        }
        if (StringUtils.isNotBlank(seller_id)) {
            paraMap.put("seller_id", seller_id);
        }
        if (StringUtils.isNotBlank(seller_email)) {
            paraMap.put("seller_email", seller_email);
        }
        if (StringUtils.isNotBlank(trade_status)) {
            paraMap.put("trade_status", trade_status);
        }
        if (StringUtils.isNotBlank(total_amount)) {
            paraMap.put("total_amount", total_amount);
        }
        if (StringUtils.isNotBlank(receipt_amount)) {
            paraMap.put("receipt_amount", receipt_amount);
        }
        if (StringUtils.isNotBlank(invoice_amount)) {
            paraMap.put("invoice_amount", invoice_amount);
        }
        if (StringUtils.isNotBlank(buyer_pay_amount)) {
            paraMap.put("buyer_pay_amount", buyer_pay_amount);
        }
        if (StringUtils.isNotBlank(point_amount)) {
            paraMap.put("point_amount", point_amount);
        }
        if (StringUtils.isNotBlank(refund_fee)) {
            paraMap.put("refund_fee", refund_fee);
        }
        if (StringUtils.isNotBlank(send_back_fee)) {
            paraMap.put("send_back_fee", send_back_fee);
        }
        if (StringUtils.isNotBlank(subject)) {
            paraMap.put("subject", subject);
        }
        if (StringUtils.isNotBlank(body)) {
            paraMap.put("body", body);
        }
        if (gmt_create != null) {
            paraMap.put("gmt_create", DateUtil.format(gmt_create, DateUtil.newFormat));
        }
        if (gmt_payment != null) {
            paraMap.put("gmt_payment", DateUtil.format(gmt_payment, DateUtil.newFormat));
        }
        if (gmt_refund != null) {
            paraMap.put("gmt_refund", DateUtil.format(gmt_refund, DateUtil.newFormat));
        }
        if (gmt_close != null) {
            paraMap.put("gmt_close", DateUtil.format(gmt_close, DateUtil.newFormat));
        }
        if (StringUtils.isNotBlank(fund_bill_list)) {
            paraMap.put("fund_bill_list", fund_bill_list);
        }

        return paraMap;
    }

    /**
     * 参数校验
     * @return
     */
    public boolean validate() {

        AssertUtil.assertNotEmpty(out_trade_no, "商户订单号不能为空");

        return true;
    }

    public String getNotifyTime() {
        return notify_time;
    }

    public void setNotifyTime(String notifyTime) {
        this.notify_time = notifyTime;
    }

    public String getNotifyType() {
        return notify_type;
    }

    public void setNotifyType(String notifyType) {
        this.notify_type = notifyType;
    }

    public String getNotifyId() {
        return notify_id;
    }

    public void setNotifyId(String notifyId) {
        this.notify_id = notifyId;
    }

    public String getSignType() {
        return sign_type;
    }

    public void setSignType(String signType) {
        this.sign_type = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTradeNo() {
        return trade_no;
    }

    public void setTradeNo(String tradeNo) {
        this.trade_no = tradeNo;
    }

    public String getAppId() {
        return app_id;
    }

    public void setAppId(String appId) {
        this.app_id = appId;
    }

    public String getOutTradeNo() {
        return out_trade_no;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.out_trade_no = outTradeNo;
    }

    public String getOutBizNo() {
        return out_biz_no;
    }

    public void setOutBizNo(String outBizNo) {
        this.out_biz_no = outBizNo;
    }

    public String getBuyerId() {
        return buyer_id;
    }

    public void setBuyerId(String buyerId) {
        this.buyer_id = buyerId;
    }

    public String getBuyerLogonId() {
        return buyer_logon_id;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyer_logon_id = buyerLogonId;
    }

    public String getSellerId() {
        return seller_id;
    }

    public void setSellerId(String sellerId) {
        this.seller_id = sellerId;
    }

    public String getSellerEmail() {
        return seller_email;
    }

    public void setSellerEmail(String sellerEmail) {
        this.seller_email = sellerEmail;
    }

    public String getTradeStatus() {
        return trade_status;
    }

    public void setTradeStatus(String tradeStatus) {
        this.trade_status = tradeStatus;
    }

    public String getTotalAmount() {
        return total_amount;
    }

    public void setTotalAmount(String totalAmount) {
        this.total_amount = totalAmount;
    }

    public String getReceiptAmount() {
        return receipt_amount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receipt_amount = receiptAmount;
    }

    public String getInvoiceAmount() {
        return invoice_amount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoice_amount = invoiceAmount;
    }

    public String getBuyerPayAmount() {
        return buyer_pay_amount;
    }

    public void setBuyerPayAmount(String buyerPayAmount) {
        this.buyer_pay_amount = buyerPayAmount;
    }

    public String getPointAmount() {
        return point_amount;
    }

    public void setPointAmount(String pointAmount) {
        this.point_amount = pointAmount;
    }

    public String getRefundFee() {
        return refund_fee;
    }

    public void setRefundFee(String refundFee) {
        this.refund_fee = refundFee;
    }

    public String getSendBackFee() {
        return send_back_fee;
    }

    public void setSendBackFee(String sendBackFee) {
        this.send_back_fee = sendBackFee;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFundBillList() {
        return fund_bill_list;
    }

    public void setFundBillList(String fundBillList) {
        this.fund_bill_list = fundBillList;
    }

    public Date getGmtCreate() {
        return gmt_create;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmt_create = gmtCreate;
    }

    public Date getGmtPayment() {
        return gmt_payment;
    }

    public void setGmtPayment(Date gmtPayment) {
        this.gmt_payment = gmtPayment;
    }

    public Date getGmtRefund() {
        return gmt_refund;
    }

    public void setGmtRefund(Date gmtRefund) {
        this.gmt_refund = gmtRefund;
    }

    public Date getGmtClose() {
        return gmt_close;
    }

    public void setGmtClose(Date gmtClose) {
        this.gmt_close = gmtClose;
    }

}
