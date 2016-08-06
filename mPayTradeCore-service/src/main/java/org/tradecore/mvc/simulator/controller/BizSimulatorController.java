/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.simulator.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
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
    private static final Logger logger            = LoggerFactory.getLogger(BizSimulatorController.class);

    private static final String MENU              = "menu";

    private static final String TO_BAR_CODE       = "toBarCode";

    private static final String TO_SCAN_CODE      = "toScanCode";

    private static final String TO_QUERY          = "toQuery";

    private static final String TO_REFUND         = "toRefund";

    private static final String TO_CANCEL         = "toCancel";

    private static final String TO_MECH_CREATE    = "toMechCreate";

    private static final String TO_MECH_QUERY     = "toMechQuery";

    private static final String RESULT            = "result";

    private static final String QUERY_RESULT      = "queryResult";

    private static final String MECH_QUERY_RESULT = "mechQueryResult";

    private static final String MERCHANT_ID       = "196";

    private static final String OUT_NOTIFY_URL    = "http://168.33.50.230:8088/mPay/simulator/receive";

    /** 交易服务接口 */
    @Resource
    private TradeService        tradeService;

    /** 商户服务接口 */
    @Resource
    private MerchantService     merchantService;

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
        map.put("acquirer_id", "10880010001");
        map.put("merchant_id", MERCHANT_ID);
        map.put("out_trade_no", "out_trade_no_" + geneRandomId());
        map.put("subject", "结算中心条码交易测试_" + geneRandomId());
        map.put("body", "购买商品3件共20.00元");
        map.put("store_id", "store_id_" + geneRandomId());

        return TO_BAR_CODE;
    }

    @RequestMapping(value = "/toScanCode", method = RequestMethod.GET)
    public String toScanCode(WebRequest request, ModelMap map) {

        //组织参数
        map.put("acquirer_id", "10880010001");
        map.put("merchant_id", MERCHANT_ID);
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

    @ResponseBody
    @RequestMapping(value = "/toStressQuery", method = RequestMethod.GET)
    public String toStressQuery(WebRequest request, ModelMap map) {

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

    @RequestMapping(value = "/toMechCreate", method = RequestMethod.GET)
    public String toMechCreate(WebRequest request, ModelMap map) {

        map.put("external_id", geneRandomId());
        map.put("acquirer_id", "10880010001");
        map.put("name", "测试商户");
        map.put("alias_name", "测试别名");
        map.put("service_phone", "95188");
        map.put("contact_name", "小二");
        map.put("contact_phone", "0755-85022088");
        map.put("contact_mobile", "13688888888");
        map.put("contact_email", "user@163.com");
        map.put("category_id", "2015110500080520");
        map.put("source", "2016070723781231");
        map.put("memo", "测试备注信息");

        return TO_MECH_CREATE;
    }

    @RequestMapping(value = "/toMechQuery", method = RequestMethod.GET)
    public String toMechQuery(WebRequest request, ModelMap map) {

        return TO_MECH_QUERY;
    }

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到条码支付HTTP请求");

        AlipayTradePayResponse response = null;

        PayRequest payRequest = buildPayRequest(request);

        try {
            response = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器条码支付HTTP调用异常");
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器条码支付HTTP调用结果,response={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        if (response != null) {
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", payRequest.getAcquirerId());
            map.put("merchantId", payRequest.getMerchantId());
            map.put("outTradeNo", payRequest.getOutTradeNo());
            map.put("tradeNo", response.getTradeNo());
        }

        return RESULT;
    }

    @ResponseBody
    @RequestMapping(value = "/stressPay", method = RequestMethod.POST)
    public String stressPay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到条码支付HTTP请求");

        AlipayTradePayResponse response = null;

        PayRequest payRequest = buildPayRequest(request);

        try {
            response = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器条码支付HTTP调用异常");
        }

        LogUtil.info(logger, "模拟器条码支付HTTP调用结果,response={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        if (response != null) {
            return response.getCode();
        } else {
            return "40004";
        }

    }

    @RequestMapping(value = "/precreate", method = RequestMethod.POST)
    public String precreate(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到扫码支付HTTP请求");

        AlipayTradePrecreateResponse response = null;

        PrecreateRequest precreateRequest = buildPrecreateRequest(request);

        try {
            response = tradeService.precreate(precreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器扫码支付HTTP调用异常");
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器扫码支付HTTP调用结果,response={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        if (response != null) {
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", precreateRequest.getAcquirerId());
            map.put("merchantId", precreateRequest.getMerchantId());
            map.put("outTradeNo", precreateRequest.getOutTradeNo());

            //拼装二维码图片在服务器端的绝对地址
            String classPath = this.getClass().getResource("/").toString();
            String filePath = classPath.substring(5, classPath.length() - 8);

            //线上使用这个
            //            String qrFilePath = String.format(filePath + "qr/%s.png", response.getOutTradeNo());

            //本地使用这个
            String qrFilePath = String.format("src/main/webapp/WEB-INF/qr/%s.png", response.getOutTradeNo());

            LogUtil.info(logger, "模拟器生成二维码图片保存路径qrFilePath={0}", qrFilePath);

            //生成二维码图片
            ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrFilePath);
            map.put("qrFileName", response.getOutTradeNo() + ".png");

        }

        return RESULT;
    }

    @ResponseBody
    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public String receive(WebRequest request, ModelMap map) {
        LogUtil.info(logger, "模拟器收到异步通知,返回success");
        return ParamConstant.NOTIFY_SUCCESS;
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到订单查询HTTP请求");

        AlipayTradeQueryResponse response = null;

        QueryRequest queryRequest = buildQueryRequest(request);

        try {
            response = tradeService.query(queryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器订单查询HTTP调用异常");
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器订单查询HTTP调用结果,response={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        if (response != null) {
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
        }

        return QUERY_RESULT;

    }

    @ResponseBody
    @RequestMapping(value = "/stressQuery", method = RequestMethod.POST)
    public String stressQuery(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到订单查询HTTP请求");

        AlipayTradeQueryResponse response = null;

        QueryRequest queryRequest = buildQueryRequest(request);

        try {
            response = tradeService.query(queryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器订单查询HTTP调用异常");
        }

        LogUtil.info(logger, "模拟器订单查询HTTP调用结果,queryResult={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        return response.getCode();

    }

    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到退款HTTP请求");

        AlipayTradeRefundResponse response = null;

        RefundRequest refundRequest = buildRefundRequest(request);

        try {
            response = tradeService.refund(refundRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器退款HTTP调用异常");
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器退款HTTP调用结果,response={0}", JSON.toJSONString(response, SerializerFeature.UseSingleQuotes));

        if (response != null) {
            map.put("code", response.getCode());
            map.put("msg", response.getMsg());
            map.put("subCode", response.getSubCode());
            map.put("subMsg", response.getSubMsg());
            map.put("acquirerId", refundRequest.getAcquirerId());
            map.put("merchantId", refundRequest.getMerchantId());
            map.put("outTradeNo", refundRequest.getOutTradeNo());
            map.put("tradeNo", response.getTradeNo());
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
            setErrorResult(map, e.getMessage());
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
        }

        return RESULT;
    }

    @RequestMapping(value = "/mechCreate", method = RequestMethod.POST)
    public String mechCreate(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到商户入驻HTTP请求");

        MerchantCreateResponse createResponse = new MerchantCreateResponse();

        //参数转换
        MerchantCreateRequest merchantCreateRequest = buildCreateRequest(request);

        try {
            createResponse = merchantService.create(merchantCreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器商户入驻HTTP调用异常,merchantCreateRequest={0}", merchantCreateRequest);
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器返回商户入驻响应,createResponse={0}", createResponse);

        if (createResponse != null && StringUtils.equals(createResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            map.put("code", createResponse.getCode());
            map.put("msg", createResponse.getMsg());
            map.put("acquirerId", createResponse.getAcquirer_id());
            map.put("merchantId", createResponse.getMerchant_id());
        }

        return RESULT;
    }

    @RequestMapping(value = "/mechQuery", method = RequestMethod.POST)
    public String mechQuery(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到商户信息查询HTTP请求");

        MerchantQueryResponse queryResponse = new MerchantQueryResponse();

        //参数转换
        MerchantQueryRequest merchantQueryRequest = buildMechQueryRequest(request);

        try {
            queryResponse = merchantService.query(merchantQueryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器商户信息查询HTTP调用异常,merchantQueryRequest={0}", merchantQueryRequest);
            setErrorResult(map, e.getMessage());
        }

        LogUtil.info(logger, "模拟器返回商户查询响应,queryResponse={0}", queryResponse);

        if (queryResponse != null && StringUtils.equals(queryResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            map.put("acquirer_id", queryResponse.getAcquirer_id());
            map.put("sub_merchant_id", queryResponse.getMerchant_id());
            map.put("external_id", queryResponse.getExternal_id());
            map.put("name", queryResponse.getName());
            map.put("alias_name", queryResponse.getAlias_name());
            map.put("service_phone", queryResponse.getService_phone());
            map.put("contact_name", queryResponse.getContact_name());
            map.put("contact_phone", queryResponse.getContact_phone());
            map.put("contact_mobile", queryResponse.getContact_mobile());
            map.put("contact_email", queryResponse.getContact_email());
            map.put("category_id", queryResponse.getCategory_id());
            map.put("source", queryResponse.getSource());
            map.put("memo", queryResponse.getMemo());
            map.put("code", queryResponse.getCode());
            map.put("msg", queryResponse.getMsg());
        }

        return MECH_QUERY_RESULT;
    }

    private MerchantQueryRequest buildMechQueryRequest(WebRequest request) {

        MerchantQueryRequest queryRequest = new MerchantQueryRequest();

        queryRequest.setAcquirer_id(request.getParameter("acquirer_id"));
        queryRequest.setExternal_id(request.getParameter("external_id"));
        queryRequest.setMerchant_id(request.getParameter("merchant_id"));

        return queryRequest;

    }

    /**
     * 将WebRequest中的参数转换为MerchantCreateRequest
     * @param request
     * @return
     */
    private MerchantCreateRequest buildCreateRequest(WebRequest request) {

        MerchantCreateRequest createRequest = new MerchantCreateRequest();

        createRequest.setExternal_id(request.getParameter("external_id"));
        createRequest.setAcquirer_id(request.getParameter("acquirer_id"));
        createRequest.setName(request.getParameter("name"));
        createRequest.setAlias_name(request.getParameter("alias_name"));
        createRequest.setService_phone(request.getParameter("service_phone"));
        createRequest.setContact_name(request.getParameter("contact_name"));
        createRequest.setContact_phone(request.getParameter("contact_phone"));
        createRequest.setContact_mobile(request.getParameter("contact_mobile"));
        createRequest.setContact_email(request.getParameter("contact_email"));
        createRequest.setCategory_id(request.getParameter("category_id"));
        createRequest.setSource(request.getParameter("source"));
        createRequest.setMemo(request.getParameter("memo"));

        LogUtil.info(logger, "商户入驻参数转换结果:createRequest={0}", createRequest);

        return createRequest;
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
        cancelRequest.setAlipayTradeNo(request.getParameter("trade_no"));

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
        queryRequest.setAlipayTradeNo(request.getParameter("trade_no"));

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
        precreateRequest.setSubMerchantId(precreateRequest.getMerchantId());
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

        precreateRequest.setOutNotifyUrl(OUT_NOTIFY_URL);

        LogUtil.info(logger, "创建扫码支付请求成功,precreateRequest={0}", precreateRequest);

        return precreateRequest;
    }

    /**
     * 创建条码支付请求
     * @param request
     * @return
     */
    private PayRequest buildPayRequest(WebRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();

        LogUtil.info(logger, "parameterMap={0}", JSON.toJSONString(parameterMap));

        PayRequest payRequest = new PayRequest();
        payRequest.setAcquirerId(request.getParameter("acquirer_id"));
        payRequest.setMerchantId(request.getParameter("merchant_id"));
        payRequest.setSubMerchantId(payRequest.getMerchantId());
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

    private void setErrorResult(ModelMap map, String errorMsg) {
        map.put("code", AlipayBizResultEnum.FAILED.getCode());
        map.put("msg", errorMsg);
    }

    /**
     * 随机生成id
     * @return
     */
    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }

}
