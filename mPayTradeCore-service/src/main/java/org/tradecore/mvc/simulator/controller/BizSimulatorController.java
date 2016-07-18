/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.simulator.controller;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.BizResultEnum;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.utils.ZxingUtils;

/**
 * 业务模拟器<br>
 * 模拟各种业务场景
 * @author HuHui
 * @version $Id: BizSimulatorController.java, v 0.1 2016年7月16日 上午12:22:31 HuHui Exp $
 */
@Controller
@RequestMapping("/simulator")
public class BizSimulatorController {

    /** 日志 */
    private static final Logger logger       = LoggerFactory.getLogger(BizSimulatorController.class);

    private static final String MENU         = "menu";

    private static final String TO_BAR_CODE  = "toBarCode";

    private static final String TO_SCAN_CODE = "toScanCode";

    private static final String TO_QUERY     = "toQuery";

    private static final String TO_REFUND    = "toRefund";

    private static final String TO_CANCEL    = "toCancel";

    private static final String RESULT       = "result";

    private static final String QUERY_RESULT = "queryResult";

    /** 交易服务接口 */
    @Resource
    private TradeService        tradeService;

    /**
     * 跳转至菜单页
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(WebRequest request, ModelMap map) {
        return MENU;
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET)
    public String result(WebRequest request, ModelMap map) {
        return RESULT;
    }

    @RequestMapping(value = "/toBarCode", method = RequestMethod.GET)
    public String toBarCode(WebRequest request, ModelMap map) {

        //组织参数
        map.put("acquirer_id", "acquire_id_" + geneRandomId());
        map.put("merchant_id", "27");
        map.put("out_trade_no", "out_trade_no_" + geneRandomId());
        map.put("subject", "结算中心条码交易测试_" + geneRandomId());
        map.put("body", "购买商品3件共20.00元");
        map.put("store_id", "store_id_" + geneRandomId());

        return TO_BAR_CODE;
    }

    @RequestMapping(value = "/toScanCode", method = RequestMethod.GET)
    public String toScanCode(WebRequest request, ModelMap map) {

        //组织参数
        map.put("acquirer_id", "acquire_id_" + geneRandomId());
        map.put("merchant_id", "27");
        map.put("out_trade_no", "out_trade_no_" + geneRandomId());
        map.put("subject", "结算中心条码交易测试_" + geneRandomId());
        map.put("body", "购买商品3件共20.00元");
        map.put("store_id", "store_id_" + geneRandomId());
        map.put("notify_url", ParamConstant.NOTIFY_URL);

        return TO_SCAN_CODE;
    }

    @RequestMapping(value = "/toQuery", method = RequestMethod.GET)
    public String toQuery(WebRequest request, ModelMap map) {

        return TO_QUERY;
    }

    @RequestMapping(value = "/toRefund", method = RequestMethod.GET)
    public String toRefund(WebRequest request, ModelMap map) {

        //组织参数
        map.put("refund_reason", "正常退款");
        map.put("out_request_no", "out_request_no_" + geneRandomId());
        map.put("store_id", "store_id_" + geneRandomId());

        return TO_REFUND;
    }

    @RequestMapping(value = "/toCancel", method = RequestMethod.GET)
    public String toCancel(WebRequest request, ModelMap map) {

        return TO_CANCEL;
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到条码支付HTTP请求");

        AlipayF2FPayResult payResult = null;

        PayRequest payRequest = buildPayRequest(request);

        try {
            payResult = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器条码支付HTTP调用异常");
            setErrorResult(map);
        }

        LogUtil.info(logger, "模拟器条码支付HTTP调用结果,payResult={0}", JSON.toJSONString(payResult, SerializerFeature.UseSingleQuotes));

        if (payResult != null && payResult.getResponse() != null) {
            AlipayTradePayResponse response = payResult.getResponse();
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", payRequest.getAcquirerId());
            map.put("merchantId", payRequest.getMerchantId());
            map.put("outTradeNo", payRequest.getOutTradeNo());
            map.put("tradeNo", response.getTradeNo());
        } else {
            setErrorResult(map);
        }

        return RESULT;
    }

    @RequestMapping(value = "/precreate", method = RequestMethod.POST)
    public String precreate(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到扫码支付HTTP请求");

        AlipayF2FPrecreateResult precreateResult = null;

        PrecreateRequest precreateRequest = buildPrecreateRequest(request);

        try {
            precreateResult = tradeService.precreate(precreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器扫码支付HTTP调用异常");
            setErrorResult(map);
        }

        LogUtil.info(logger, "模拟器扫码支付HTTP调用结果,precreateResult={0}", JSON.toJSONString(precreateResult, SerializerFeature.UseSingleQuotes));

        if (precreateResult != null && precreateResult.getResponse() != null) {
            AlipayTradePrecreateResponse response = precreateResult.getResponse();
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", precreateRequest.getAcquirerId());
            map.put("merchantId", precreateRequest.getMerchantId());
            map.put("outTradeNo", precreateRequest.getOutTradeNo());

            String qrFilePath = String.format("F:/qr/%s.png", response.getOutTradeNo());

            LogUtil.info(logger, "模拟器qrFilePath={0}", qrFilePath);

            //生成二维码图片
            ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrFilePath);
            map.put("qrFilePath", qrFilePath);
        } else {
            setErrorResult(map);
        }

        return RESULT;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到订单查询HTTP请求");

        AlipayF2FQueryResult queryResult = null;

        QueryRequest queryRequest = buildQueryRequest(request);

        try {
            queryResult = tradeService.query(queryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器订单查询HTTP调用异常");
            setErrorResult(map);
        }

        LogUtil.info(logger, "模拟器订单查询HTTP调用结果,queryResult={0}", JSON.toJSONString(queryResult, SerializerFeature.UseSingleQuotes));

        if (queryResult != null && queryResult.getResponse() != null) {
            AlipayTradeQueryResponse response = queryResult.getResponse();
            map.put("buyerLogonId", response.getBuyerLogonId());
            map.put("outTradeNo", response.getOutTradeNo());
            if (StringUtils.isNotBlank(response.getTradeStatus())) {
                map.put("tradeStatus", AlipayTradeStatusEnum.getByCode(response.getTradeStatus()).getDesc());
            }
            map.put("totalAmount", response.getTotalAmount());
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
        } else {
            setErrorResult(map);
        }

        return QUERY_RESULT;

    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到退款HTTP请求");

        AlipayF2FRefundResult refundResult = null;

        RefundRequest refundRequest = buildRefundRequest(request);

        try {
            refundResult = tradeService.refund(refundRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器退款HTTP调用异常");
            setErrorResult(map);
        }

        LogUtil.info(logger, "模拟器退款HTTP调用结果,refundResult={0}", JSON.toJSONString(refundResult, SerializerFeature.UseSingleQuotes));

        if (refundResult != null && refundResult.getResponse() != null) {
            AlipayTradeRefundResponse response = refundResult.getResponse();
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", refundRequest.getAcquirerId());
            map.put("merchantId", refundRequest.getMerchantId());
            map.put("outTradeNo", refundRequest.getOutTradeNo());
            map.put("tradeNo", response.getTradeNo());
        } else {
            setErrorResult(map);
        }

        return RESULT;

    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到撤销HTTP请求");

        AlipayTradeCancelResponse cancelResponse = null;

        CancelRequest cancelRequest = buildCancelRequest(request);

        try {
            cancelResponse = tradeService.cancel(cancelRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器撤销HTTP调用异常");
            setErrorResult(map);
        }

        LogUtil.info(logger, "模拟器撤销HTTP调用结果,cancelResult={0}", JSON.toJSONString(cancelResponse, SerializerFeature.UseSingleQuotes));

        if (cancelResponse != null) {
            map.put("code", cancelResponse.getCode());
            map.put("msg", cancelResponse.getMsg());
            map.put("subCode", cancelResponse.getSubCode());
            map.put("subMsg", cancelResponse.getSubMsg());
            map.put("acquirerId", cancelRequest.getAcquirerId());
            map.put("merchantId", cancelRequest.getMerchantId());
            map.put("outTradeNo", cancelRequest.getOutTradeNo());
            map.put("tradeNo", cancelResponse.getTradeNo());
        } else {
            setErrorResult(map);
        }

        return RESULT;
    }

    /**
     * 创建撤销请求
     * @param request
     * @return
     */
    private CancelRequest buildCancelRequest(WebRequest request) {

        CancelRequest cancelRequest = new CancelRequest();

        cancelRequest.setAcquirerId(request.getParameter("acquirer_id"));
        cancelRequest.setMerchantId(request.getParameter("merchant_id"));
        cancelRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        cancelRequest.setAppAuthToken(request.getParameter("app_auth_token"));

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

        queryRequest.setAcquirerId(request.getParameter("acquirer_id"));
        queryRequest.setMerchantId(request.getParameter("merchant_id"));
        queryRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        queryRequest.setAlipayTradeNo(request.getParameter("trade_no"));
        queryRequest.setAppAuthToken(request.getParameter("app_auth_token"));

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
        precreateRequest.setOperatorId(request.getParameter("operator_id"));
        precreateRequest.setStoreId(request.getParameter("store_id"));
        precreateRequest.setAlipayStoreId(request.getParameter("alipay_store_id"));
        precreateRequest.setTerminalId(request.getParameter("terminal_id"));
        precreateRequest.setTimeoutExpress(request.getParameter("timeout_express"));

        //支付宝通知结算中心地址
        precreateRequest.setNotifyUrl(request.getParameter("notify_url"));

        precreateRequest.setOutNotifyUrl("http://www.notify.url.out");

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
        payRequest.setAcquirerId(request.getParameter("acquirer_id"));
        payRequest.setMerchantId(request.getParameter("merchant_id"));
        payRequest.setScene(request.getParameter("scene"));
        payRequest.setOutTradeNo(request.getParameter("out_trade_no"));
        payRequest.setSellerId(request.getParameter("seller_id"));
        payRequest.setTotalAmount(request.getParameter("total_amount"));
        payRequest.setDiscountableAmount(request.getParameter("discountable_amount"));
        payRequest.setUndiscountableAmount(request.getParameter("undiscountable_amount"));
        payRequest.setSubject(request.getParameter("subject"));
        payRequest.setBody(request.getParameter("body"));
        payRequest.setAppAuthToken(request.getParameter("app_auth_token"));
        payRequest.setOperatorId(request.getParameter("operator_id"));
        payRequest.setStoreId(request.getParameter("store_id"));
        payRequest.setAlipayStoreId(request.getParameter("alipay_store_id"));
        payRequest.setTerminalId(request.getParameter("terminal_id"));
        payRequest.setTimeoutExpress(request.getParameter("timeout_express"));

        payRequest.setAuthCode(request.getParameter("auth_code"));

        LogUtil.info(logger, "创建条码支付请求成功,payRequest={0}", payRequest);

        return payRequest;
    }

    private void setErrorResult(ModelMap map) {
        map.put("code", BizResultEnum.FAILED.getCode());
        map.put("msg", "业务处理发生异常，请稍后再试");
    }

    /**
     * 随机生成id
     * @return
     */
    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }

}
