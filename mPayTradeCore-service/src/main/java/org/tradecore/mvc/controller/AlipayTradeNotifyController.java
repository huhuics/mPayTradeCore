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
import org.tradecore.alipay.trade.service.TradeNotifyService;
import org.tradecore.common.util.LogUtil;

/**
 * 
 * @author HuHui
 * @version $Id: AlipayNotifyController.java, v 0.1 2016年7月13日 下午2:27:48 HuHui Exp $
 */
@Controller
@RequestMapping("/tradeNotify")
public class AlipayTradeNotifyController {

    private static final Logger logger  = LoggerFactory.getLogger(AlipayTradeNotifyController.class);

    private static final String SUCCESS = "success";

    @Resource
    private TradeNotifyService  tradeNotifyService;

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    public String receive(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到支付宝扫码支付异步通知");

        //TODO

        return SUCCESS;
    }
}
