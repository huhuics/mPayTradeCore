package org.tradecore.dao;

import org.tradecore.dao.domain.BizAlipayRefundOrder;

public interface BizAlipayRefundOrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizAlipayRefundOrder record);

    int insertSelective(BizAlipayRefundOrder record);

    BizAlipayRefundOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizAlipayRefundOrder record);

    int updateByPrimaryKey(BizAlipayRefundOrder record);
}