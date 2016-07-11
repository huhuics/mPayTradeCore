/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;

/**
 * 支付仓储服务接口
 * @author HuHui
 * @version $Id: TradeRepository.java, v 0.1 2016年7月9日 上午10:10:07 HuHui Exp $
 */
public interface PayRepository {

    /**
     * 将条码支付请求转化为Domain对象，并持久化<br>
     * 收单机构请求第一次到结算中心，结算中心持久化订单数据，订单状态为WAIT_BUYER_PAY
     * @param   payRequest 支付请求
     * @return  转化之后的Domain对象
     */
    BizAlipayPayOrder savePayOrder(PayRequest payRequest);

    /**
     * 根据支付宝返回的支付结果，修改本地订单数据<br>
     * 该方法在条码支付中，结算中心调用支付宝支付接口，支付宝返回结果后调用<br>
     * 需要更新的订单表的字段为：
     * <ul>
     * <li>订单状态order_status</li>
     * <li>支付宝订单号alipay_trade_no</li>
     * <li>买家支付宝账号buyer_logon_id</li>
     * <li>交易支付使用的资金渠道fund_bill_list</li>
     * <li>本次交易支付所使用的单品券优惠的商品优惠信息discount_goods_detail</li>
     * <li>支付宝返回的信息return_detail</li>
     * <li>支付成功时间gmt_payment</li>
     * </ul>
     * 
     * @param bizAlipayPayOrder    本地订单数据Domain对象
     * @param alipayF2FPayResult   支付宝返回结果对象
     */
    void updatePayOrder(BizAlipayPayOrder bizAlipayPayOrder, AlipayF2FPayResult alipayF2FPayResult);

    /**
     * 如果查询支付宝端的订单状态与本地订单状态不一致，则修改本地订单状态
     * @param queryRequest           订单查询请求对象
     * @param alipayF2FQueryResult   支付宝返回订单查询结果对象
     */
    void updateOrderStatus(QueryRequest queryRequest, AlipayF2FQueryResult alipayF2FQueryResult);

    /**
     * 根据退款业务是否成功更新交易订单中的退款状态
     * @param oriOrder         退款业务原始交易订单
     * @param refundOrder      退款订单
     */
    void updateOrderRefundStatus(BizAlipayPayOrder oriOrder, BizAlipayRefundOrder refundOrder);

    /**
     * 根据商户标识号和外部商户号加锁查询订单
     * @param merchantId  商户标识号
     * @param outTradeNo  外部商户号
     * @return            订单对象
     */
    BizAlipayPayOrder selectPayOrderForUpdate(String merchantId, String outTradeNo);

}
