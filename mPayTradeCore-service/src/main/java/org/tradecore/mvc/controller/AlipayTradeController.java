/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

/**
 * 处理支付宝交易请求
 * @author HuHui
 * @version $Id: AlipayTradeController.java, v 0.1 2016年7月15日 上午10:41:06 HuHui Exp $
 */
@Controller
@RequestMapping("/trade")
public class AlipayTradeController {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AlipayTradeController.class);

    /** 交易服务接口 */
    @Resource
    private TradeService        tradeService;

    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到条码支付HTTP请求");

        AlipayF2FPayResult payResult = null;

        PayRequest payRequest = buildPayRequest(request);

        try {
            payResult = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "条码支付HTTP调用异常");
        }

        LogUtil.info(logger, "条码支付HTTP调用结果,payResult={0}", JSON.toJSONString(payResult, SerializerFeature.UseSingleQuotes));

        return payResult.getResponse().getBody();
    }

    @RequestMapping(value = "/precreate", method = RequestMethod.POST)
    public String precreate(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到扫码支付HTTP请求");

        AlipayF2FPrecreateResult precreateResult = null;

        PrecreateRequest precreateRequest = buildPrecreateRequest(request);

        try {
            precreateResult = tradeService.precreate(precreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "扫码支付HTTP调用异常");
        }

        LogUtil.info(logger, "扫码支付HTTP调用结果,precreateResult={0}", JSON.toJSONString(precreateResult, SerializerFeature.UseSingleQuotes));

        return JSON.toJSONString(precreateResult);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到订单查询HTTP请求");

        AlipayF2FQueryResult queryResult = null;

        QueryRequest queryRequest = buildQueryRequest(request);

        try {
            queryResult = tradeService.query(queryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "订单查询HTTP调用异常");
        }

        LogUtil.info(logger, "订单查询HTTP调用结果,queryResult={0}", JSON.toJSONString(queryResult, SerializerFeature.UseSingleQuotes));

        return JSON.toJSONString(queryResult);

    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到退款HTTP请求");

        AlipayF2FRefundResult refundResult = null;

        RefundRequest refundRequest = buildRefundRequest(request);

        try {
            refundResult = tradeService.refund(refundRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "退款HTTP调用异常");
        }

        LogUtil.info(logger, "退款HTTP调用结果,refundResult={0}", JSON.toJSONString(refundResult, SerializerFeature.UseSingleQuotes));

        return JSON.toJSONString(refundResult);

    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到撤销HTTP请求");

        AlipayTradeCancelResponse cancelResponse = null;

        CancelRequest cancelRequest = buildCancelRequest(request);

        try {
            cancelResponse = tradeService.cancel(cancelRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "撤销HTTP调用异常");
        }

        LogUtil.info(logger, "撤销HTTP调用结果,cancelResult={0}", JSON.toJSONString(cancelResponse, SerializerFeature.UseSingleQuotes));

        return JSON.toJSONString(cancelResponse);
    }

    /**
     * 创建撤销请求
     * @param request
     * @return
     */
    private CancelRequest buildCancelRequest(WebRequest request) {

        CancelRequest cancelRequest = new CancelRequest();

        String bizContentJsonStr = request.getParameter(ParamConstant.BIZ_CONTENT);

        LogUtil.info(logger, "订单撤销报文原始业务参数,biz_content={0}", bizContentJsonStr);

        //解析json字段
        Map<String, String> paraMap = JSON.parseObject(bizContentJsonStr, new TypeReference<Map<String, String>>() {
        });

        cancelRequest.setAcquirerId(paraMap.get("acquirer_id"));
        cancelRequest.setMerchantId(paraMap.get("merchant_id"));
        cancelRequest.setOutTradeNo(paraMap.get("out_trade_no"));
        cancelRequest.setAppAuthToken(paraMap.get("app_auth_token"));

        LogUtil.info(logger, "创建撤销请求成功,cancelRequest={0}", cancelRequest);

        return cancelRequest;
    }

    /**
     * 创建退款请求
     * @param request
     * @return
     */
    private RefundRequest buildRefundRequest(WebRequest request) {

        RefundRequest refundRequest = new RefundRequest();

        refundRequest.setAcquirerId(request.getParameter("acquirer_id"));
        refundRequest.setMerchantId(request.getParameter("merchant_id"));
        refundRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        refundRequest.setAlipayTradeNo(request.getParameter("trade_no"));
        refundRequest.setRefundAmount(request.getParameter("refund_amount"));
        refundRequest.setRefundReason(request.getParameter("refund_reason"));
        refundRequest.setOutRequestNo(request.getParameter("out_request_no"));
        refundRequest.setStoreId(request.getParameter("store_id"));
        refundRequest.setTerminalId(request.getParameter("terminal_id"));

        LogUtil.info(logger, "创建退款请求成功,refundRequest={0}", refundRequest);

        return refundRequest;
    }

    /**
     * 创建订单查询请求
     * @param request
     * @return
     */
    private QueryRequest buildQueryRequest(WebRequest request) {

        QueryRequest queryRequest = new QueryRequest();

        String bizContentJsonStr = request.getParameter(ParamConstant.BIZ_CONTENT);

        LogUtil.info(logger, "订单查询报文原始业务参数,biz_content={0}", bizContentJsonStr);

        //解析json字段
        Map<String, String> paraMap = JSON.parseObject(bizContentJsonStr, new TypeReference<Map<String, String>>() {
        });

        queryRequest.setAcquirerId(paraMap.get("acquirer_id"));
        queryRequest.setMerchantId(paraMap.get("merchant_id"));
        queryRequest.setOutTradeNo(paraMap.get("out_trade_no"));
        queryRequest.setAlipayTradeNo(paraMap.get("trade_no"));
        queryRequest.setAppAuthToken(paraMap.get("app_auth_token"));

        LogUtil.info(logger, "创建订单查询请求成功,queryRequest={0}", queryRequest);

        return queryRequest;
    }

    /**
     * 创建扫码支付请求
     * @param request
     * @return
     */
    private PrecreateRequest buildPrecreateRequest(WebRequest request) {

        PrecreateRequest precreateRequest = new PrecreateRequest();
        precreateRequest.setAcquirerId(request.getParameter("acquirer_id"));
        precreateRequest.setMerchantId(request.getParameter("merchant_id"));
        precreateRequest.setScene(request.getParameter("scene"));
        precreateRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        precreateRequest.setSellerId(request.getParameter("seller_id"));
        precreateRequest.setTotalAmount(request.getParameter("total_amount"));
        precreateRequest.setDiscountableAmount(request.getParameter("discountable_amount"));
        precreateRequest.setUndiscountableAmount(request.getParameter("undiscountable_amount"));
        precreateRequest.setSubject(request.getParameter("subject"));
        precreateRequest.setBody(request.getParameter("body"));
        precreateRequest.setAppAuthToken(request.getParameter("app_auth_token"));
        //TODO:封装成List
        //        payRequest.setGoodsDetailList(request.getParameter(""));
        precreateRequest.setOperatorId(request.getParameter("operator_id"));
        precreateRequest.setStoreId(request.getParameter("store_id"));
        precreateRequest.setAlipayStoreId(request.getParameter("alipay_store_id"));
        precreateRequest.setTerminalId(request.getParameter("terminal_id"));
        //TODO:封装成ExtendParams
        //        payRequest.setExtendParams(request.getParameter(""));
        precreateRequest.setTimeoutExpress(request.getParameter("timeout_express"));

        //结算中心通知商户地址
        precreateRequest.setOutNotifyUrl(request.getParameter("notify_url"));
        //支付宝通知结算中心地址
        precreateRequest.setNotifyUrl(ParamConstant.NOTIFY_URL);

        LogUtil.info(logger, "创建扫码支付请求成功,precreateRequest={0}", precreateRequest);

        return precreateRequest;
    }

    /**
     * 创建条码支付请求
     * @param request
     * @return
     */
    private PayRequest buildPayRequest(WebRequest request) {

        PayRequest payRequest = new PayRequest();

        String bizContentJsonStr = request.getParameter(ParamConstant.BIZ_CONTENT);

        LogUtil.info(logger, "条码支付报文原始业务参数,biz_content={0}", bizContentJsonStr);

        //解析json字段
        Map<String, String> paraMap = JSON.parseObject(bizContentJsonStr, new TypeReference<Map<String, String>>() {
        });

        payRequest.setAcquirerId(paraMap.get("acquirer_id"));
        payRequest.setMerchantId(paraMap.get("merchant_id"));
        payRequest.setScene(paraMap.get("scene"));
        payRequest.setOutTradeNo(paraMap.get("out_trade_no"));
        payRequest.setSellerId(paraMap.get("seller_id"));
        payRequest.setTotalAmount(paraMap.get("total_amount"));
        payRequest.setDiscountableAmount(paraMap.get("discountable_amount"));
        payRequest.setUndiscountableAmount(paraMap.get("undiscountable_amount"));
        payRequest.setSubject(paraMap.get("subject"));
        payRequest.setBody(paraMap.get("body"));
        payRequest.setAppAuthToken(paraMap.get("app_auth_token"));
        //TODO:封装成List
        //        payRequest.setGoodsDetailList(paraMap.get(""));
        payRequest.setOperatorId(paraMap.get("operator_id"));
        payRequest.setStoreId(paraMap.get("store_id"));
        payRequest.setAlipayStoreId(paraMap.get("alipay_store_id"));
        payRequest.setTerminalId(paraMap.get("terminal_id"));
        //TODO:封装成ExtendParams
        //        payRequest.setExtendParams(paraMap.get(""));
        payRequest.setTimeoutExpress(paraMap.get("timeout_express"));

        payRequest.setAuthCode(paraMap.get("auth_code"));

        LogUtil.info(logger, "创建条码支付请求成功,payRequest={0}", payRequest);

        return payRequest;
    }
}
