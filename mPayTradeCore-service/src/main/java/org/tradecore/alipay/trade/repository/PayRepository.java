/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.util.Map;

import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;

/**
 * 条码支付仓储服务接口
 * @author HuHui
 * @version $Id: TradeRepository.java, v 0.1 2016年7月9日 上午10:10:07 HuHui Exp $
 */
public interface PayRepository {

    /**
     * 将条码支付请求转化为Domain对象，并持久化
     * @param   payRequest            支付请求
     * @param   alipayF2FPayResult    支付宝返回条码支付结果
     * @return                        转化之后的Domain对象
     */
    BizAlipayPayOrder savePayOrder(PayRequest payRequest, AlipayF2FPayResult alipayF2FPayResult);

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
     * 根据撤销业务是否成功更新交易订单中的撤销状态
     * @param oriOrder
     * @param cancelOrder
     */
    void updateOrderCancelStatus(BizAlipayPayOrder oriOrder, BizAlipayCancelOrder cancelOrder);

    /**
     * 根据商户标识号和外部商户号查询订单<br>
     * isLock为true则加锁查询，false为普通不加锁查询
     * @param merchantId  商户标识号
     * @param outTradeNo  外部商户号
     * @param isLock      是否加锁
     * @return            订单对象
     */
    BizAlipayPayOrder selectPayOrder(String merchantId, String outTradeNo, boolean isLock);

    /**
     * 根据支付宝异步通知参数更新本地支付订单
     * @param oriOrder       原订单
     * @param notifyRequest  异步通知参数
     */
    void updatePayOrder(BizAlipayPayOrder oriOrder, Map<String, String> paraMap);

}
