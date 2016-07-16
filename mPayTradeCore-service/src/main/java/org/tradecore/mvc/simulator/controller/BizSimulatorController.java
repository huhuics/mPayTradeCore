/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.simulator.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;

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
    private static final Logger logger   = LoggerFactory.getLogger(BizSimulatorController.class);

    private static final String MENU     = "menu";

    private static final String BAR_CODE = "barCode";

    private static final String RESULT   = "result";

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

    @RequestMapping(value = "/toBarCode", method = RequestMethod.GET)
    public String toBarCode(WebRequest request, ModelMap map) {

        //组织参数
        map.put("acquirer_id", "acquire_id_" + geneRandomId());
        map.put("merchant_id", "27");
        map.put("out_trade_no", "out_trade_no_" + geneRandomId());
        map.put("subject", "结算中心条码交易测试_" + geneRandomId());
        map.put("body", "购买商品3件共20.00元");
        map.put("store_id", "store_id_" + geneRandomId());

        return BAR_CODE;
    }

    @RequestMapping(value = "/pay", method = RequestMethod.GET)
    public String pay(WebRequest request, ModelMap map) {

        LogUtil.info(logger, "模拟器收到条码支付HTTP请求");

        AlipayF2FPayResult payResult = null;

        PayRequest payRequest = buildPayRequest(request);

        try {
            payResult = tradeService.pay(payRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "模拟器条码支付HTTP调用异常");
        }

        LogUtil.info(logger, "模拟器条码支付HTTP调用结果,payResult={0}", JSON.toJSONString(payResult, SerializerFeature.UseSingleQuotes));

        return RESULT;
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
        //TODO:封装成List
        //        payRequest.setGoodsDetailList(request.getParameter(""));
        payRequest.setOperatorId(request.getParameter("operator_id"));
        payRequest.setStoreId(request.getParameter("store_id"));
        payRequest.setAlipayStoreId(request.getParameter("alipay_store_id"));
        payRequest.setTerminalId(request.getParameter("terminal_id"));
        //TODO:封装成ExtendParams
        //        payRequest.setExtendParams(request.getParameter(""));
        payRequest.setTimeoutExpress(request.getParameter("timeout_express"));

        payRequest.setAuthCode(request.getParameter("auth_code"));

        LogUtil.info(logger, "创建条码支付请求成功,payRequest={0}", payRequest);

        return payRequest;
    }

    /**
     * 随机生成id
     * @return
     */
    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }

}
