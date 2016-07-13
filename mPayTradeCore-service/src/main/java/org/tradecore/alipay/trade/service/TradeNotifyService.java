/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import org.tradecore.alipay.trade.request.NotifyRequest;

/**
 * 支付宝扫码支付异步通知服务接口
 * @author HuHui
 * @version $Id: TradeNotifyService.java, v 0.1 2016年7月13日 上午9:34:49 HuHui Exp $
 */
public interface TradeNotifyService {

    /**
     * 接收支付宝异步通知，并发送给收单机构
     * @param notifyRequest
     */
    void receiveAndSend(NotifyRequest notifyRequest);

}
