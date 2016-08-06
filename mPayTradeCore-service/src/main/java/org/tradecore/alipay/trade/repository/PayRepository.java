/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.sql.SQLException;
import java.util.Map;

import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

/**
 * 条码支付仓储服务接口
 * @author HuHui
 * @version $Id: TradeRepository.java, v 0.1 2016年7月9日 上午10:10:07 HuHui Exp $
 */
public interface PayRepository {

    /**
     * 并持久化交易对象
     * @param   payOrder       交易对象
     */
    BizAlipayPayOrder savePayOrder(BizAlipayPayOrder payOrder);

    /**
     * 如果查询支付宝端的订单状态与本地订单状态不一致，则修改本地订单状态
     * @param payOrder           订单对象
     */
    void updateOrderStatus(BizAlipayPayOrder payOrder) throws Exception;

    /**
     * 更新交易订单中的交易状态和退款状态
     * @param oriOrder         退款业务原始交易订单
     */
    void updateOrderRefundStatus(BizAlipayPayOrder oriOrder);

    /**
     * 根据撤销业务是否成功更新交易订单中的撤销状态
     * @param oriOrder
     * @param cancelOrder
     */
    void updateOrderCancelStatus(BizAlipayPayOrder oriOrder, BizAlipayCancelOrder cancelOrder);

    /**
     * 根据商户标识号和外部商户号或支付宝订单号查询订单<br>
     * @param merchantId     商户标识号
     * @param outTradeNo     外部商户号
     * @param alipayTradeNo  支付宝订单号
     * @return               订单对象
     * @throws SQLException 
     */
    BizAlipayPayOrder selectPayOrder(String merchantId, String outTradeNo, String alipayTradeNo) throws SQLException;

    /**
     * 根据订单号查询支付订单
     * @param acquirerId      收单机构编号
     * @param merchantId      商户标识号
     * @param outTradeNo      外部商户号
     * @return                订单对象
     * @throws SQLException
     */
    BizAlipayPayOrder selectPayOrderByTradeNo(String acquirerId, String merchantId, String outTradeNo) throws SQLException;

    /**
     * 根据支付宝异步通知参数更新本地支付订单
     * @param oriOrder       原订单
     * @param notifyRequest  异步通知参数
     */
    void updatePayOrder(BizAlipayPayOrder oriOrder, Map<String, String> paraMap);

}
