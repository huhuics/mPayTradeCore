package org.tradecore.dao;

import java.util.List;

import org.tradecore.dao.domain.BizAlipayRefundOrder;

public interface BizAlipayRefundOrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizAlipayRefundOrder record);

    int insertSelective(BizAlipayRefundOrder record);

    BizAlipayRefundOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizAlipayRefundOrder record);

    int updateByPrimaryKey(BizAlipayRefundOrder record);

    /**
     * 通过商户订单号获取该订单号下所有退款订单
     * @param outTradeNo
     */
    List<BizAlipayRefundOrder> selectRefundOrdersByOutTradeNo(String outTradeNo);
}