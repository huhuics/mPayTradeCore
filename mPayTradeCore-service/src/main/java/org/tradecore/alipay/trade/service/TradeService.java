/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;

import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

/**
 * 交易服务类
 * @author HuHui
 * @version $Id: TradeService.java, v 0.1 2016年7月8日 下午3:21:50 HuHui Exp $
 */
public interface TradeService {

    /**
     * 条码支付
     * @param payRequest  请求参数
     * @return            支付返回结果信息
     */
    AlipayF2FPayResult pay(PayRequest payRequest);

    /**
     * 订单查询
     * @param queryRequest  请求参数
     * @return              订单查询结果信息
     */
    AlipayF2FQueryResult query(QueryRequest queryRequest);

    /**
     * 订单退款
     * @param refundRequest  请求参数
     * @return               退款返回结果信息
     */
    AlipayF2FRefundResult refund(RefundRequest refundRequest);

}
