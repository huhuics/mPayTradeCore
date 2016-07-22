/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.controller;

import java.util.HashMap;
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
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;

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
    private static final Logger logger                   = LoggerFactory.getLogger(MerchantController.class);

    /** 商户服务接口 */
    @Resource
    private MerchantService     merchantService;

    /** 商户创建返回json字段名 */
    private static final String MERCHANT_CREATE_RESPONSE = "merchant_create_response";

    /** 商户查询返回json字段名 */
    private static final String MERCHANT_QUERY_RESPONSE  = "merchant_query_response";

    /**
     * 商户入驻
     * @param request
     * @param map
     * @return
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

            //验签
            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            //参数转换
            merchantCreateRequest = buildCreateRequest(paraMap.get(ParamConstant.BIZ_CONTENT));

            createResponse = merchantService.create(merchantCreateRequest);

        } catch (Exception e) {
            LogUtil.error(e, logger, "商户入驻HTTP调用异常,merchantCreateRequest={0}", merchantCreateRequest);
            createResponse.setBizFailed();
        }

        LogUtil.info(logger, "返回商户入驻响应,createResponse={0}", createResponse);

        return buildResponse(MERCHANT_CREATE_RESPONSE, createResponse);
    }

    /**
     * 商户查询
     * @param request
     * @param map
     * @return
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

            AssertUtil.assertTrue(verify(paraMap), "验签不通过");

            //参数转换
            merchantQueryRequest = buildQueryRequest(paraMap.get(ParamConstant.BIZ_CONTENT));

            queryResponse = merchantService.query(merchantQueryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "商户信息查询HTTP调用异常,merchantQueryRequest={0}", merchantQueryRequest);
            queryResponse.setBizFailed();
        }

        LogUtil.info(logger, "返回商户查询响应,queryResponse={0}", queryResponse);

        return buildResponse(MERCHANT_QUERY_RESPONSE, queryResponse);
    }

    /**
     * 创建json返回数据
     * @param responseName
     * @param object
     * @return
     */
    private String buildResponse(String responseName, Object object) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put(responseName, object);

        return JSON.toJSONString(resultMap);
    }

    private MerchantQueryRequest buildQueryRequest(String bizContent) {

        MerchantQueryRequest queryRequest = new MerchantQueryRequest();

        LogUtil.info(logger, "商户查询报文原始业务参数,biz_content={0}", bizContent);

        queryRequest = JSON.parseObject(bizContent, MerchantQueryRequest.class);

        LogUtil.info(logger, "商户查询参数转换结果:queryRequest={0}", queryRequest);

        return queryRequest;

    }

    /**
     * 将WebRequest中的参数转换为MerchantCreateRequest
     * @param request
     * @return
     */
    private MerchantCreateRequest buildCreateRequest(String bizContent) {

        MerchantCreateRequest createRequest = new MerchantCreateRequest();

        LogUtil.info(logger, "商户入驻报文原始业务参数,biz_content={0}", bizContent);

        createRequest = JSON.parseObject(bizContent, MerchantCreateRequest.class);

        LogUtil.info(logger, "商户入驻参数转换结果:createRequest={0}", createRequest);

        return createRequest;
    }

}
