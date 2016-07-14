/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.util.List;

import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

/**
 * 退款仓储服务
 * @author HuHui
 * @version $Id: RefundRepository.java, v 0.1 2016年7月11日 下午7:48:00 HuHui Exp $
 */
public interface RefundRepository {

    /**
     * 将退款请求转化为Domian对象，并持久化<br>
     * 收单机构退款请求第一次到结算中心，结算中心持久化退款数据，退款状态为INIT
     * @param oriOrder       原订单
     * @param refundRequest  退款请求
     * @return
     */
    BizAlipayRefundOrder saveRefundOrder(BizAlipayPayOrder oriOrder, RefundRequest refundRequest);

    /**
     * 根据支付宝返回的退款结果，修改本地退款订单表
     * @param refundOrder
     * @param alipayF2FRefundResult
     * @return
     */
    void updateRefundOrder(BizAlipayRefundOrder refundOrder, AlipayF2FRefundResult alipayF2FRefundResult);

    /**
     * 通过商户订单号、退款订单状态获取所有退款订单<br>
     * @param outTradeNo    商户订单号
     * @param refundStatus  退款状态
     * @return
     */
    List<BizAlipayRefundOrder> selectRefundOrdersByOutTradeNo(String outTradeNo, String refundStatus);

}
