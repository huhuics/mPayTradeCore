/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.CreateRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundQueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;

import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

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
     * @throws Exception 
     */
    AlipayTradePayResponse pay(PayRequest payRequest) throws Exception;

    /**
     * 线上支付，创建订单接口
     * @param createRequest
     * @return
     * @throws Exception
     */
    AlipayTradeCreateResponse create(CreateRequest createRequest) throws Exception;

    /**
     * 扫码支付，预下单(生成二维码)接口
     * @param precreateRequest
     * @return
     */
    AlipayTradePrecreateResponse precreate(PrecreateRequest precreateRequest) throws Exception;

    /**
     * 订单查询
     * @param queryRequest  请求参数
     * @return              订单查询结果信息
     */
    AlipayTradeQueryResponse query(QueryRequest queryRequest) throws Exception;

    /**
     * 退款订单查询<br>
     * 可查询使用refund接口退款的订单信息
     * @param refundQueryRequest        请求参数
     * @return                          退款订单查询结果信息
     * @throws Exception                
     */
    AlipayTradeFastpayRefundQueryResponse refundQuery(RefundQueryRequest refundQueryRequest) throws Exception;

    /**
     * 订单退款
     * @param refundRequest  请求参数
     * @return               退款返回结果信息
     */
    AlipayTradeRefundResponse refund(RefundRequest refundRequest) throws Exception;

    /**
     * 订单撤销<br>
     * 只能撤销一个自然日内的订单，超过0点，将不能撤销<br>
     * 支付交易返回失败或支付系统超时，调用该接口撤销交易。<br>
     * 如果此订单用户支付失败，支付宝系统会将此订单关闭；如果用户支付成功，支付宝系统会将此订单资金退还给用户。
     * @param cancelRequest  请求参数，只支持根据商户订单号撤销订单
     * @return
     */
    AlipayTradeCancelResponse cancel(CancelRequest cancelRequest) throws Exception;

}
