package org.tradecore.dao;

import org.tradecore.dao.domain.BizAlipayPayOrder;

public interface BizAlipayPayOrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizAlipayPayOrder record);

    int insertSelective(BizAlipayPayOrder record);

    BizAlipayPayOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizAlipayPayOrder record);

    int updateByPrimaryKey(BizAlipayPayOrder record);
}