/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.util.List;

import org.tradecore.dao.domain.BizAlipayRefundOrder;

/**
 * 退款仓储服务
 * @author HuHui
 * @version $Id: RefundRepository.java, v 0.1 2016年7月11日 下午7:48:00 HuHui Exp $
 */
public interface RefundRepository {

    /**
     * 通过商户订单号获取所有退款订单
     * @param outTradeNo
     * @return
     */
    List<BizAlipayRefundOrder> selectRefundOrdersByOutTradeNo(String outTradeNo);

}
