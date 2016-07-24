/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.service.TradeNotifyService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.HttpUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.SecureUtil;
import org.tradecore.dao.domain.BizAlipayPayOrder;

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
    public boolean receiveAndSend(Map<String, String> paraMap) {

        LogUtil.info(logger, "收到扫码支付异步通知请求参数");

        //1.校验参数
        AssertUtil.assertNotNull(paraMap, "异步通知参数不能为空");

        String outTradeNo = paraMap.get(JSONFieldConstant.OUT_TRADE_NO);

        //2.加锁查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrder(null, outTradeNo, Boolean.TRUE);
        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //幂等控制，如果原始订单为支付成功，则不修改本订单内容，直接发给收单机构；否则修改，且发送收单机构
        if (!StringUtils.equals(oriOrder.getOrderStatus(), AlipayTradeStatusEnum.TRADE_SUCCESS.getCode())) {
            LogUtil.info(logger, "异步通知修改原始订单幂等,outTradeNo={0}", outTradeNo);
            //3.修改原始订单
            payRepository.updatePayOrder(oriOrder, paraMap);
        }

        //4.发送给收单机构
        return send(paraMap, oriOrder.getOutNotifyUrl());

    }

    /**
     * 发送扫码支付响应到收单机构
     * @param notifyRequest  支付宝异步通知请求参数
     * @param outNotifyUrl   收单机构异步通知地址
     */
    public boolean send(Map<String, String> paraMap, String outNotifyUrl) {

        LogUtil.info(logger, "开始发送扫码支付响应到收单机构,outNotifyUrl={0}", outNotifyUrl);

        AssertUtil.assertNotBlank(outNotifyUrl, "异步通知地址为空,发送消息失败");

        //签名
        paraMap.remove(ParamConstant.SIGN_TYPE);
        paraMap.remove(ParamConstant.SIGN);

        String sign = SecureUtil.signNotify(paraMap);

        paraMap.put(ParamConstant.SIGN_TYPE, ParamConstant.SIGN_TYPE_VALUE);
        paraMap.put(ParamConstant.SIGN, sign);

        List<NameValuePair> paraList = buildPostParaList(paraMap);

        //发送
        String response = HttpUtil.httpClientPost(outNotifyUrl, paraList);

        LogUtil.info(logger, "完成发送扫码支付响应到收单机构,response={0}", response);

        return StringUtils.equals(response, ParamConstant.NOTIFY_SUCCESS) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * 将Map中的参数转换成NamValuePair对，并封装成List
     */
    private List<NameValuePair> buildPostParaList(Map<String, String> paraMap) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>(paraMap.size());

        for (String key : paraMap.keySet()) {
            NameValuePair nvPair = new NameValuePair(key, paraMap.get(key));
            pairList.add(nvPair);
        }

        return pairList;
    }

}
