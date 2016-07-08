/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import org.tradecore.alipay.trade.request.PayRequest;

import com.alipay.demo.trade.model.result.AlipayF2FPayResult;

/**
 * 交易服务类
 * @author HuHui
 * @version $Id: TradeService.java, v 0.1 2016年7月8日 下午3:21:50 HuHui Exp $
 */
public interface TradeService {

    /**
     * 条码支付
     * @param payRequest
     * @return
     */
    AlipayF2FPayResult pay(PayRequest payRequest);

}
