/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.request.NotifyRequest;
import org.tradecore.alipay.trade.service.TradeNotifyService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
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
    public void receiveAndSend(NotifyRequest notifyRequest) {

        LogUtil.info(logger, "收到扫码支付异步通知请求参数,notifyRequest={0}", notifyRequest);

        //1.校验参数
        AssertUtil.assertNotNull(notifyRequest, "异步通知请求不能为空");
        AssertUtil.assertTrue(notifyRequest.validate(), "异步通知请求参数不合法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrderForUpdate(null, notifyRequest.getOutTradeNo());
        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //3.修改原始订单
        payRepository.updatePayOrder(oriOrder, notifyRequest);

        //4.发送给收单机构

    }

    public void send() {
    }

}
