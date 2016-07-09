/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.tradecore.alipay.trade.request.PayRequest;

/**
 * 交易类仓储服务接口<br>
 * 完成交易请求转化成Domain对象并持久化
 * @author HuHui
 * @version $Id: TradeRepository.java, v 0.1 2016年7月9日 上午10:10:07 HuHui Exp $
 */
public interface TradeRepository {

    /**
     * 将条码支付请求转化为Domain对象，并持久化<br>
     * 收单机构请求第一次到结算中心，结算中心持久化订单数据，订单状态为WAIT_BUYER_PAY
     * @param payRequest 支付请求
     * @return boolan 是否持久化成功
     */
    boolean savePayOrder(PayRequest payRequest);

}
