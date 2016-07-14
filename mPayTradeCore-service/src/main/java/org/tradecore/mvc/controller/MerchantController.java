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
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alipay.api.response.AlipayBossProdSubmerchantCreateResponse;

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

        //参数转换
        MerchantCreateRequest merchantCreateRequest = convert2CreateRequest(request);

        AlipayBossProdSubmerchantCreateResponse response = null;
        try {
            response = merchantService.create(merchantCreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "商户入驻调用异常,merchantCreateRequest={0}", merchantCreateRequest);
        }

        return JSON.toJSONString(response);
    }

    /**
     * 将WebRequest中的参数转换为MerchantCreateRequest
     * @param request
     * @return
     */
    private MerchantCreateRequest convert2CreateRequest(WebRequest request) {

        return null;
    }

}
