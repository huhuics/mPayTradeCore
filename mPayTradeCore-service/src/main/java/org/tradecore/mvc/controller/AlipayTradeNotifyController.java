/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.trade.request.NotifyRequest;
import org.tradecore.alipay.trade.service.TradeNotifyService;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;

/**
 * 接收支付宝扫码支付结果通知
 * @author HuHui
 * @version $Id: AlipayNotifyController.java, v 0.1 2016年7月13日 下午2:27:48 HuHui Exp $
 */
@Controller
@RequestMapping("/tradeNotify")
public class AlipayTradeNotifyController {

    /** 日志 */
    private static final Logger logger  = LoggerFactory.getLogger(AlipayTradeNotifyController.class);

    /** 给支付宝返回字符 */
    private static final String SUCCESS = "success";

    /** 交易通知服务接口 */
    @Resource
    private TradeNotifyService  tradeNotifyService;

    /**
     * 接收支付宝扫码支付结果通知
     * @param request
     * @param map
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public String receive(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到支付宝扫码支付HTTP异步通知");

        NotifyRequest notifyRequest = convert2NotifyRequest(request);

        tradeNotifyService.receiveAndSend(notifyRequest);

        return SUCCESS;
    }

    /**
     * 将webRequest中的参数转换成NotifyRequest
     * @param request
     * @return
     */
    private NotifyRequest convert2NotifyRequest(WebRequest request) {

        NotifyRequest notifyRequest = new NotifyRequest();

        notifyRequest.setNotifyTime(request.getParameter("notify_time"));
        notifyRequest.setNotifyType(request.getParameter("notify_type"));
        notifyRequest.setNotifyTd(request.getParameter("notify_id"));
        notifyRequest.setSignType(request.getParameter("sign_type"));
        notifyRequest.setSign(request.getParameter("sign"));
        notifyRequest.setTradeNo(request.getParameter("trade_no"));
        notifyRequest.setAppId(request.getParameter("app_id"));
        notifyRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        notifyRequest.setOutBizNo(request.getParameter("out_biz_no"));
        notifyRequest.setBuyerId(request.getParameter("buyer_id"));
        notifyRequest.setBuyerLogonId(request.getParameter("buyer_logon_id"));
        notifyRequest.setSellerId(request.getParameter("seller_id"));
        notifyRequest.setSellerEmail(request.getParameter("seller_email"));
        notifyRequest.setTradeStatus(request.getParameter("trade_status"));
        notifyRequest.setTotalAmount(request.getParameter("total_amount"));
        notifyRequest.setReceiptAmount(request.getParameter("receipt_amount"));
        notifyRequest.setInvoiceAmount(request.getParameter("invoice_amount"));
        notifyRequest.setBuyerPayAmount(request.getParameter("buyer_pay_amount"));
        notifyRequest.setPointAmount(request.getParameter("point_amount"));
        notifyRequest.setRefundFee(request.getParameter("refund_fee"));
        notifyRequest.setSendBackFee(request.getParameter("send_back_fee"));
        notifyRequest.setSubject(request.getParameter("subject"));
        notifyRequest.setBody(request.getParameter("body"));
        notifyRequest.setGmtCreate(DateUtil.parseDateNewFormat(request.getParameter("gmt_create")));
        notifyRequest.setGmtPayment(DateUtil.parseDateNewFormat(request.getParameter("gmt_payment")));
        notifyRequest.setGmtRefund(DateUtil.parseDateNewFormat(request.getParameter("gmt_refund")));
        notifyRequest.setGmtClose(DateUtil.parseDateNewFormat(request.getParameter("gmt_close")));
        notifyRequest.setFundBillList(request.getParameter("fund_bill_list"));

        return notifyRequest;
    }
}
