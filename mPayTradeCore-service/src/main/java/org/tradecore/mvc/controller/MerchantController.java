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
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantModifyResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantModifyRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.FormaterUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.ResponseUtil;
import org.tradecore.common.util.SecureUtil;

import com.alibaba.fastjson.JSON;

/**
 * 处理收单机构的商户入驻和商户查询请求
 * @author HuHui
 * @version $Id: MerchantController.java, v 0.1 2016年7月14日 下午9:13:30 HuHui Exp $
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController extends AbstractBizController {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    /** 商户服务接口 */
    @Resource
    private MerchantService     merchantService;

    /**
     * 商户入驻
     */
    @ResponseBody
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到商户入驻HTTP请求");

        MerchantCreateResponse createResponse = new MerchantCreateResponse();

        MerchantCreateRequest merchantCreateRequest = null;

        try {

            //组装参数
            Map<String, String> paraMap = getParameters(request);

            LogUtil.info(logger, "商户入驻原始报文参数paraMap={0}", paraMap);

            //验签
            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            //参数转换
            merchantCreateRequest = buildCreateRequest(paraMap);

            //入驻
            createResponse = merchantService.create(merchantCreateRequest);

        } catch (Exception e) {
            LogUtil.error(e, logger, "商户入驻HTTP调用异常,Message={0}", e.getMessage());
            createResponse.setBizFailed(e.getMessage());
        }

        //签名
        String sign = SecureUtil.sign(createResponse.buildSortedParaMap());

        String mechCreateResponse = ResponseUtil.buildResponse(ParamConstant.MERCHANT_CREATE_RESPONSE, createResponse, sign);

        LogUtil.info(logger, "返回商户入驻响应,mechCreateResponse={0}", mechCreateResponse);

        return mechCreateResponse;
    }

    /**
     * 商户查询
     */
    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到商户信息查询HTTP请求");

        MerchantQueryResponse queryResponse = new MerchantQueryResponse();

        MerchantQueryRequest merchantQueryRequest = null;

        try {

            //组装参数
            Map<String, String> paraMap = getParameters(request);

            LogUtil.info(logger, "商户查询原始报文参数paraMap={0}", paraMap);

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            //参数转换
            merchantQueryRequest = buildQueryRequest(paraMap);

            queryResponse = merchantService.query(merchantQueryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "商户信息查询HTTP调用异常,Message={0}", e.getMessage());
            queryResponse.setBizFailed(e.getMessage());
        }

        String sign = SecureUtil.sign(queryResponse.buildSortedParaMap());

        String mechQueryResponseStr = ResponseUtil.buildResponse(ParamConstant.MERCHANT_QUERY_RESPONSE, queryResponse, sign);

        LogUtil.info(logger, "返回商户查询响应,mechQueryResponseStr={0}", mechQueryResponseStr);

        return mechQueryResponseStr;
    }

    /**
     * 商户修改
     */
    @ResponseBody
    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public String modify(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到商户信息修改HTTP请求");

        MerchantModifyResponse modifyResponse = new MerchantModifyResponse();

        MerchantModifyRequest merchantModifyRequest = null;

        try {
            //组装参数
            Map<String, String> paraMap = getParameters(request);

            LogUtil.info(logger, "商户信息修改原始报文参数paraMap={0}", paraMap);

            //验签
            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            //参数转换
            merchantModifyRequest = buildModifyRequest(paraMap);

            //修改
            modifyResponse = merchantService.modify(merchantModifyRequest);

        } catch (Exception e) {
            LogUtil.error(e, logger, "商户信息修改HTTP调用异常,Message={0}", e.getMessage());
            modifyResponse.setBizFailed(e.getMessage());
        }
        //签名
        String sign = SecureUtil.sign(modifyResponse.buildSortedParaMap());

        String mechModifyResponse = ResponseUtil.buildResponse(ParamConstant.MERCHANT_MODIFY_RESPONSE, modifyResponse, sign);

        LogUtil.info(logger, "返回商户修改响应,mechModifyResponse={0}", mechModifyResponse);

        return mechModifyResponse;
    }

    private MerchantModifyRequest buildModifyRequest(Map<String, String> paraMap) {

        LogUtil.info(logger, "收到商户信息修改报文转换请求");

        MerchantModifyRequest modifyRequest = new MerchantModifyRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        modifyRequest = JSON.parseObject(bizContent, MerchantModifyRequest.class);
        modifyRequest.setAcquirer_id(acquirerId);

        //对external_id和out_external_id进行转化
        modifyRequest.setOut_external_id(modifyRequest.getExternal_id());
        modifyRequest.setExternal_id(FormaterUtil.externalIdFormat(modifyRequest.getAcquirer_id(), modifyRequest.getOut_external_id()));

        LogUtil.info(logger, "商户信息修改参数转换完成");

        return modifyRequest;
    }

    private MerchantQueryRequest buildQueryRequest(Map<String, String> paraMap) {

        LogUtil.info(logger, "收到商户查询报文转换请求");

        MerchantQueryRequest queryRequest = new MerchantQueryRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        queryRequest = JSON.parseObject(bizContent, MerchantQueryRequest.class);
        queryRequest.setAcquirer_id(acquirerId);

        //对external_id和out_external_id进行转化
        queryRequest.setOut_external_id(queryRequest.getExternal_id());
        queryRequest.setExternal_id(FormaterUtil.externalIdFormat(queryRequest.getAcquirer_id(), queryRequest.getOut_external_id()));

        LogUtil.info(logger, "商户查询参数转换完成");

        return queryRequest;

    }

    /**
     * 将WebRequest中的参数转换为MerchantCreateRequest
     * @param request
     * @return
     */
    private MerchantCreateRequest buildCreateRequest(Map<String, String> paraMap) {

        LogUtil.info(logger, "收到商户入驻报文转换请求");

        MerchantCreateRequest createRequest = new MerchantCreateRequest();

        String bizContent = paraMap.get(ParamConstant.BIZ_CONTENT);
        String acquirerId = paraMap.get(ACQUIRER_ID);

        createRequest = JSON.parseObject(bizContent, MerchantCreateRequest.class);
        createRequest.setAcquirer_id(acquirerId);

        //对external_id和out_external_id进行转化
        createRequest.setOut_external_id(createRequest.getExternal_id());
        createRequest.setExternal_id(FormaterUtil.externalIdFormat(createRequest.getAcquirer_id(), createRequest.getOut_external_id()));

        LogUtil.info(logger, "商户入驻参数转换完成");

        return createRequest;
    }

}
