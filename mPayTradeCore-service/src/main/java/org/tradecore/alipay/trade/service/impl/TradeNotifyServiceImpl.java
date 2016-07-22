/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.request.NotifyRequest;
import org.tradecore.alipay.trade.service.TradeNotifyService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.HttpUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.SecureUtil;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;

/**
 * 支付宝扫码支付异步通知服务接口实现类
 * @author HuHui
 * @version $Id: TradeNotifyServiceImpl.java, v 0.1 2016年7月13日 上午10:08:25 HuHui Exp $
 */
@Service
public class TradeNotifyServiceImpl implements TradeNotifyService {

    /**
     * 支付仓储服务
     */
    @Resource
    private PayRepository       payRepository;

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(TradeNotifyServiceImpl.class);

    @Override
    public void receiveAndSend(NotifyRequest notifyRequest) {

        LogUtil.info(logger, "收到扫码支付异步通知请求参数,notifyRequest={0}", notifyRequest);

        //1.校验参数
        AssertUtil.assertNotNull(notifyRequest, "异步通知请求不能为空");
        AssertUtil.assertTrue(notifyRequest.validate(), "异步通知请求参数不合法");

        //2.加锁查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrder(null, notifyRequest.getOutTradeNo(), Boolean.TRUE);
        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //幂等
        if (StringUtils.equals(oriOrder.getOrderStatus(), AlipayTradeStatusEnum.TRADE_SUCCESS.getCode())) {
            LogUtil.info(logger, "异步通知修改原始订单幂等,outTradeNo={0}", notifyRequest.getOutTradeNo());
            return;
        }

        //3.修改原始订单
        payRepository.updatePayOrder(oriOrder, notifyRequest);

        //4.发送给收单机构
        send(notifyRequest, oriOrder.getOutNotifyUrl());

    }

    /**
     * 发送扫码支付响应到收单机构
     * @param notifyRequest  支付宝异步通知请求参数
     * @param outNotifyUrl   收单机构异步通知地址
     */
    public void send(NotifyRequest notifyRequest, String outNotifyUrl) {

        LogUtil.info(logger, "开始发送扫码支付响应到收单机构,outNotifyUrl={0}", outNotifyUrl);

        AssertUtil.assertNotBlank(outNotifyUrl, "异步通知地址为空");

        //签名
        Map<String, String> sortedParaMap = notifyRequest.buildSortedParaMap();
        String sign = SecureUtil.sign(sortedParaMap);

        //组装参数
        NameValuePair bizParaPair = new NameValuePair(ParamConstant.NOTIFY_RESPONSE, JSON.toJSONString(notifyRequest));
        NameValuePair signPair = new NameValuePair(ParamConstant.SIGN, sign);

        //发送
        String response = HttpUtil.httpClientPost(outNotifyUrl, Arrays.asList(bizParaPair, signPair));

        LogUtil.info(logger, "完成发送扫码支付响应到收单机构,response={0}", response);
    }

}
