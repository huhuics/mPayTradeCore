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
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;

/**
 * 处理收单机构的商户入驻和商户查询请求
 * @author HuHui
 * @version $Id: MerchantController.java, v 0.1 2016年7月14日 下午9:13:30 HuHui Exp $
 */
@Controller
@RequestMapping("/merchant")
public class MerchantController {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);

    /** 商户服务接口 */
    @Resource
    private MerchantService     merchantService;

    /**
     * 商户入驻
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到商户入驻HTTP请求");

        MerchantCreateResponse createResponse = new MerchantCreateResponse();

        //参数转换
        MerchantCreateRequest merchantCreateRequest = buildCreateRequest(request);

        LogUtil.info(logger, "商户入驻参数转换结果:merchantCreateRequest={0}", merchantCreateRequest);

        try {
            createResponse = merchantService.create(merchantCreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "商户入驻HTTP调用异常,merchantCreateRequest={0}", merchantCreateRequest);
            createResponse.setBizFailed();
        }

        LogUtil.info(logger, "返回商户入驻响应,createResponse={0}", createResponse);

        return JSON.toJSONString(createResponse);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public String query(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到商户信息查询HTTP请求");

        MerchantQueryResponse queryResponse = new MerchantQueryResponse();

        //参数转换
        MerchantQueryRequest merchantQueryRequest = buildQueryRequest(request);

        try {
            queryResponse = merchantService.query(merchantQueryRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "商户信息查询HTTP调用异常,merchantQueryRequest={0}", merchantQueryRequest);
            queryResponse.setBizFailed();
        }

        LogUtil.info(logger, "返回商户查询响应,queryResponse={0}", queryResponse);

        return JSON.toJSONString(queryResponse);
    }

    private MerchantQueryRequest buildQueryRequest(WebRequest request) {

        MerchantQueryRequest queryRequest = new MerchantQueryRequest();

        queryRequest.setAcquirer_id(request.getParameter("acquirer_id"));
        queryRequest.setExternal_id(request.getParameter("external_id"));
        queryRequest.setSub_merchant_id(request.getParameter("merchant_id"));

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

        return createRequest;
    }

}
