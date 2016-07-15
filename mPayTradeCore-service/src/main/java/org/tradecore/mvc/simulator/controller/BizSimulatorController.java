/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.simulator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

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
    private static final Logger logger = LoggerFactory.getLogger(BizSimulatorController.class);

    private static final String MENU   = "menu";

    /**
     * 跳转至菜单页
     * @param request
     * @param map
     * @return
     */
    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String menu(WebRequest request, ModelMap map) {
        return MENU;
    }
}
