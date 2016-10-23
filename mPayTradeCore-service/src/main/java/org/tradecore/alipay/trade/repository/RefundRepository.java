/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.util.List;

import org.tradecore.alipay.trade.request.RefundQueryRequest;
import org.tradecore.common.util.Money;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 退款仓储服务
 * @author HuHui
 * @version $Id: RefundRepository.java, v 0.1 2016年7月11日 下午7:48:00 HuHui Exp $
 */
public interface RefundRepository {

    /**
     * 根据退款结果对退款记录进行更新
     * @param oriOrder             原订单  
     * @param refundOrder          退款订单记录
     * @param refundResponse       支付宝退款结果
     * @return
     */
    void updateRefundOrder(BizAlipayPayOrder oriOrder, BizAlipayRefundOrder refundOrder, AlipayTradeRefundResponse refundResponse);

    /**
     * 保存退款记录
     * @param refundOrder
     * @return
     */
    BizAlipayRefundOrder saveRefundOrder(BizAlipayRefundOrder refundOrder);

    /**
     * 通过商户订单号、退款订单状态获取所有退款订单<br>
     * @param queryRequest   查询请求{@link RefundQueryRequest}
     * @return
     */
    List<BizAlipayRefundOrder> selectRefundOrders(RefundQueryRequest queryRequest);

    /**
     * 通过商户订单号获取当前订单所有已成功退款的总金额
     * @param merchantId     商户标识号
     * @param outTradeNo     商户订单号
     * @param alipayTradeNo  支付宝订单号
     * @return
     */
    Money getRefundedMoney(String merchantId, String outTradeNo, String alipayTradeNo);

    /**
     * 根据退款订单查询修改本地订单的退款状态（包括交易订单和退款订单的退款状态）
     * @param payOrder
     * @param refundOrders
     * @param payRepository
     * @param refundQueryResponse
     */
    void updateRefundStatus(BizAlipayPayOrder payOrder, List<BizAlipayRefundOrder> refundOrders, PayRepository payRepository, AlipayTradeFastpayRefundQueryResponse refundQueryResponse);

    /**
     * 修改退款订单
     * @param refundOrder
     */
    void updateRefundOrder(BizAlipayRefundOrder refundOrder);

    /**
     * 根据退款订单的主键id删除退款订单记录
     * @param id
     */
    void deleteRefundOrder(String id);

}
