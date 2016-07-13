/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.common.util.LogUtil;

/**
 * 接收支付宝扫码支付异步通知
 * @author HuHui
 * @version $Id: TradeNotifyController.java, v 0.1 2016年7月13日 上午10:52:46 HuHui Exp $
 */
@Controller
@RequestMapping("/tradeNotify")
public class TradeNotifyController {

    private static final Logger logger  = LoggerFactory.getLogger(TradeNotifyController.class);

    private static final String SUCCESS = "success";

    @RequestMapping(value = "/receive", method = RequestMethod.GET)
    public String receive(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "收到支付宝扫码支付异步通知");

        //TODO

        return SUCCESS;
    }

}
