package org.tradecore.dao;

import org.tradecore.dao.domain.BizAlipayCancelOrder;

public interface BizAlipayCancelOrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizAlipayCancelOrder record);

    int insertSelective(BizAlipayCancelOrder record);

    BizAlipayCancelOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizAlipayCancelOrder record);

    int updateByPrimaryKey(BizAlipayCancelOrder record);
}