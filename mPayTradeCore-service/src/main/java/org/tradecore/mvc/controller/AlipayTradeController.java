/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.controller;

import java.util.ArrayList;
import java.util.List;
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
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.ResponseUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
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
public class AlipayTradeController extends AbstractBizController {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AlipayTradeController.class);

    /** 交易服务接口 */
    @Resource
    private TradeService        tradeService;

    /**
     * 处理条码支付请求
     */
    @ResponseBody
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String pay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到条码支付HTTP请求");

        AlipayF2FPayResult payResult = null;

        try {
            Map<String, String> paraMap = getParameters(request);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            PayRequest payRequest = buildPayRequest(paraMap);

            payResult = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "条码支付HTTP调用异常");
            buildPayErrorResult(payResult);
        }

        String body = payResult.getResponse().getBody();

        String payResponse = ResponseUtil.buildResponse(body, ParamConstant.ALIPAY_TRADE_PAY_RESPONSE);

        LogUtil.info(logger, "条码支付HTTP调用结果,payResponse={0}", payResponse);

        return payResponse;
    }

    /**
     * 处理扫码支付请求
     */
    @ResponseBody
    @RequestMapping(value = "/precreate", method = RequestMethod.POST)
    public String precreate(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到扫码支付HTTP请求");

        AlipayF2FPrecreateResult precreateResult = null;

        try {
            Map<String, String> paraMap = getParameters(request);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            PrecreateRequest precreateRequest = buildPrecreateRequest(paraMap);

            precreateResult = tradeService.precreate(precreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "扫码支付HTTP调用异常");
            buildPrecreateErrorResult(precreateResult);
        }

        String body = precreateResult.getResponse().getBody();

        String precreateResponse = ResponseUtil.buildResponse(body, ParamConstant.ALIPAY_TRADE_PRECREATE_RESPONSE);

        LogUtil.info(logger, "扫码支付HTTP调用结果,precreateResponse={0}", precreateResponse);

        return precreateResponse;
    }

    /**
     * 处理订单查询请求
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到订单查询HTTP请求");

        AlipayF2FQueryResult queryResult = null;

        try {
            Map<String, String> paraMap = getParameters(request);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            QueryRequest queryRequest = buildQueryRequest(paraMap);

            queryResult = tradeService.query(queryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "订单查询HTTP调用异常");
            buildQueryErrorResult(queryResult);
        }

        String body = queryResult.getResponse().getBody();

        String queryResponse = ResponseUtil.buildResponse(body, ParamConstant.ALIPAY_TRADE_QUERY_RESPONSE);

        LogUtil.info(logger, "订单查询HTTP调用结果,queryResponse={0}", queryResponse);

        return queryResponse;

    }

    /**
     * 处理订单退款请求
     */
    @ResponseBody
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public String refund(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到退款HTTP请求");

        AlipayF2FRefundResult refundResult = null;

        try {
            Map<String, String> paraMap = getParameters(request);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            RefundRequest refundRequest = buildRefundRequest(paraMap);

            refundResult = tradeService.refund(refundRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "退款HTTP调用异常");
            buildRefundErrorResult(refundResult);
        }

        String body = refundResult.getResponse().getBody();

        String refundResponse = ResponseUtil.buildResponse(body, ParamConstant.ALIPAY_TRADE_REFUND_RESPONSE);

        LogUtil.info(logger, "退款HTTP调用结果,refundResponse={0}", refundResponse);

        return refundResponse;

    }

    /**
     * 处理订单撤销请求
     */
    @ResponseBody
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public String cancel(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到撤销HTTP请求");

        AlipayTradeCancelResponse cancelResponse = null;

        try {
            Map<String, String> paraMap = getParameters(request);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            CancelRequest cancelRequest = buildCancelRequest(paraMap);

            cancelResponse = tradeService.cancel(cancelRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "撤销HTTP调用异常");
            buildCancelResponse(cancelResponse);
        }

        String body = cancelResponse.getBody();

        String cancelRespStr = ResponseUtil.buildResponse(body, ParamConstant.ALIPAY_TRADE_CANCEL_RESPONSE);

        LogUtil.info(logger, "撤销HTTP调用结果,cancelRespStr={0}", cancelRespStr);

        return cancelRespStr;
    }

    /**
     * 创建撤销请求
     */
    private CancelRequest buildCancelRequest(Map<String, String> paraMap) {

        CancelRequest cancelRequest = new CancelRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        LogUtil.info(logger, "订单撤销报文原始业务参数:acquirerId={0},biz_content={1}", acquirerId, bizContent);

        //解析json字段
        Map<String, String> bizParaMap = JSON.parseObject(bizContent, new TypeReference<Map<String, String>>() {
        });

        cancelRequest.setAcquirerId(acquirerId);
        cancelRequest.setMerchantId(bizParaMap.get("merchant_id"));
        cancelRequest.setOutTradeNo(bizParaMap.get("out_trade_no"));
        cancelRequest.setAppAuthToken(bizParaMap.get("app_auth_token"));

        LogUtil.info(logger, "创建撤销请求成功,cancelRequest={0}", cancelRequest);

        return cancelRequest;
    }

    /**
     * 创建退款请求
     */
    private RefundRequest buildRefundRequest(Map<String, String> paraMap) {

        RefundRequest refundRequest = new RefundRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        LogUtil.info(logger, "订单退款报文原始业务参数:acquirerId={0},biz_content={1}", acquirerId, bizContent);

        Map<String, String> bizParaMap = JSON.parseObject(bizContent, new TypeReference<Map<String, String>>() {
        });

        refundRequest.setAcquirerId(acquirerId);
        refundRequest.setMerchantId(bizParaMap.get("merchant_id"));
        refundRequest.setOutTradeNo(bizParaMap.get("out_trade_no"));
        refundRequest.setAlipayTradeNo(bizParaMap.get("trade_no"));
        refundRequest.setRefundAmount(bizParaMap.get("refund_amount"));
        refundRequest.setRefundReason(bizParaMap.get("refund_reason"));
        refundRequest.setOutRequestNo(bizParaMap.get("out_request_no"));
        refundRequest.setStoreId(bizParaMap.get("store_id"));
        refundRequest.setTerminalId(bizParaMap.get("terminal_id"));

        LogUtil.info(logger, "创建退款请求成功,refundRequest={0}", refundRequest);

        return refundRequest;
    }

    /**
     * 创建订单查询请求
     */
    private QueryRequest buildQueryRequest(Map<String, String> paraMap) {

        QueryRequest queryRequest = new QueryRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        LogUtil.info(logger, "订单查询报文原始业务参数:acquirerId={0},biz_content={1}", acquirerId, bizContent);

        //解析json字段
        Map<String, String> bizParaMap = JSON.parseObject(bizContent, new TypeReference<Map<String, String>>() {
        });

        queryRequest.setAcquirerId(acquirerId);
        queryRequest.setMerchantId(bizParaMap.get("merchant_id"));
        queryRequest.setOutTradeNo(bizParaMap.get("out_trade_no"));
        queryRequest.setAlipayTradeNo(bizParaMap.get("trade_no"));
        queryRequest.setAppAuthToken(bizParaMap.get("app_auth_token"));

        LogUtil.info(logger, "创建订单查询请求成功,queryRequest={0}", queryRequest);

        return queryRequest;
    }

    /**
     * 创建扫码支付请求
     */
    private PrecreateRequest buildPrecreateRequest(Map<String, String> paraMap) {

        PrecreateRequest precreateRequest = new PrecreateRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        LogUtil.info(logger, "扫码支付报文原始业务参数:acquirerId={0},biz_content={1}", acquirerId, bizContent);

        Map<String, String> bizParaMap = JSON.parseObject(bizContent, new TypeReference<Map<String, String>>() {
        });

        precreateRequest.setAcquirerId(acquirerId);
        precreateRequest.setMerchantId(bizParaMap.get("merchant_id"));
        precreateRequest.setScene(bizParaMap.get("scene"));
        precreateRequest.setOutTradeNo(bizParaMap.get("out_trade_no"));
        precreateRequest.setSellerId(bizParaMap.get("seller_id"));
        precreateRequest.setTotalAmount(bizParaMap.get("total_amount"));
        precreateRequest.setDiscountableAmount(bizParaMap.get("discountable_amount"));
        precreateRequest.setUndiscountableAmount(bizParaMap.get("undiscountable_amount"));
        precreateRequest.setSubject(bizParaMap.get("subject"));
        precreateRequest.setBody(bizParaMap.get("body"));
        precreateRequest.setAppAuthToken(bizParaMap.get("app_auth_token"));

        //封装成List
        if (StringUtils.isNotBlank(bizParaMap.get("goods_detail"))) {
            precreateRequest.setGoodsDetailList(parseGoodsDetailList(bizParaMap.get("goods_detail")));
        }

        precreateRequest.setOperatorId(bizParaMap.get("operator_id"));
        precreateRequest.setStoreId(bizParaMap.get("store_id"));
        precreateRequest.setAlipayStoreId(bizParaMap.get("alipay_store_id"));
        precreateRequest.setTerminalId(bizParaMap.get("terminal_id"));

        if (StringUtils.isNotBlank(bizParaMap.get("extend_params"))) {
            precreateRequest.setExtendParams(parseExtendParams(bizParaMap.get("extend_params")));
        }

        precreateRequest.setTimeoutExpress(bizParaMap.get("timeout_express"));

        //结算中心通知商户地址
        precreateRequest.setOutNotifyUrl(bizParaMap.get("notify_url"));
        //支付宝通知结算中心地址
        precreateRequest.setNotifyUrl(ParamConstant.NOTIFY_URL);

        LogUtil.info(logger, "创建扫码支付请求成功,precreateRequest={0}", precreateRequest);

        return precreateRequest;
    }

    /**
     * 创建条码支付请求
     */
    private PayRequest buildPayRequest(Map<String, String> paraMap) {

        PayRequest payRequest = new PayRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        LogUtil.info(logger, "条码支付报文原始业务参数:acquirerId={0},biz_content={1}", acquirerId, bizContent);

        //解析json字段
        Map<String, String> bizParaMap = JSON.parseObject(bizContent, new TypeReference<Map<String, String>>() {
        });

        payRequest.setAcquirerId(acquirerId);
        payRequest.setMerchantId(bizParaMap.get("merchant_id"));
        payRequest.setScene(bizParaMap.get("scene"));
        payRequest.setOutTradeNo(bizParaMap.get("out_trade_no"));
        payRequest.setSellerId(bizParaMap.get("seller_id"));
        payRequest.setTotalAmount(bizParaMap.get("total_amount"));
        payRequest.setDiscountableAmount(bizParaMap.get("discountable_amount"));
        payRequest.setUndiscountableAmount(bizParaMap.get("undiscountable_amount"));
        payRequest.setSubject(bizParaMap.get("subject"));
        payRequest.setBody(bizParaMap.get("body"));
        payRequest.setAppAuthToken(bizParaMap.get("app_auth_token"));

        //封装成List
        if (StringUtils.isNotBlank(bizParaMap.get("goods_detail"))) {
            payRequest.setGoodsDetailList(parseGoodsDetailList(bizParaMap.get("goods_detail")));
        }

        payRequest.setOperatorId(bizParaMap.get("operator_id"));
        payRequest.setStoreId(bizParaMap.get("store_id"));
        payRequest.setAlipayStoreId(bizParaMap.get("alipay_store_id"));
        payRequest.setTerminalId(bizParaMap.get("terminal_id"));

        //获取ExtendParams
        if (StringUtils.isNotBlank(bizParaMap.get("extend_params"))) {
            payRequest.setExtendParams(parseExtendParams(bizParaMap.get("extend_params")));
        }

        payRequest.setTimeoutExpress(bizParaMap.get("timeout_express"));

        payRequest.setAuthCode(bizParaMap.get("auth_code"));

        LogUtil.info(logger, "创建条码支付请求成功,payRequest={0}", payRequest);

        return payRequest;
    }

    /**
     * 解析GoodsDetail
     */
    private List<GoodsDetail> parseGoodsDetailList(String goodsDetailListStr) {

        List<GoodsDetail> goodsDetails = new ArrayList<GoodsDetail>();

        List<Map<String, String>> listMap = JSON.parseObject(goodsDetailListStr, new TypeReference<List<Map<String, String>>>() {
        });

        for (int i = 0; i < listMap.size(); i++) {
            Map<String, String> goodsDetailMap = listMap.get(i);
            GoodsDetail goodDetail = new GoodsDetail();
            goodDetail.setAlipayGoodsId(goodsDetailMap.get("alipay_goods_id"));
            goodDetail.setGoodsCategory(goodsDetailMap.get("goods_category"));
            goodDetail.setGoodsId(goodsDetailMap.get("goods_id"));
            goodDetail.setGoodsName(goodsDetailMap.get("goods_name"));
            if (StringUtils.isNotBlank(goodsDetailMap.get("price"))) {
                goodDetail.setPrice(Long.parseLong(goodsDetailMap.get("price")));
            }
            if (StringUtils.isNotBlank(goodsDetailMap.get("quantity"))) {
                goodDetail.setQuantity(Double.parseDouble(goodsDetailMap.get("quantity")));
            }

            goodsDetails.add(goodDetail);
        }

        return goodsDetails;
    }

    /**
     * 解析ExtendParams
     */
    private ExtendParams parseExtendParams(String extendParamsStr) {
        Map<String, String> extendParamsMap = JSON.parseObject(extendParamsStr, new TypeReference<Map<String, String>>() {
        });
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(extendParamsMap.get("sys_service _provider_id"));

        return extendParams;
    }

    //以下方法存在的意义在于， 当发生异常，避免响应为null而抛出NullpointException

    private void buildCancelResponse(AlipayTradeCancelResponse cancelResponse) {
        if (cancelResponse == null) {
            cancelResponse = new AlipayTradeCancelResponse();
        }
    }

    private void buildRefundErrorResult(AlipayF2FRefundResult refundResult) {
        if (refundResult == null) {
            refundResult = new AlipayF2FRefundResult(new AlipayTradeRefundResponse());
        }
    }

    private void buildQueryErrorResult(AlipayF2FQueryResult queryResult) {
        if (queryResult == null) {
            queryResult = new AlipayF2FQueryResult(new AlipayTradeQueryResponse());
        }
    }

    private void buildPrecreateErrorResult(AlipayF2FPrecreateResult precreateResult) {
        if (precreateResult == null) {
            precreateResult = new AlipayF2FPrecreateResult(new AlipayTradePrecreateResponse());
        }
    }

    private void buildPayErrorResult(AlipayF2FPayResult payResult) {
        if (payResult == null) {
            payResult = new AlipayF2FPayResult(new AlipayTradePayResponse());
        }
    }

}
