package org.tradecore.dao;

import org.tradecore.dao.domain.BizAcquirerCashInfo;

public interface BizAcquirerCashInfoDAO {
    int deleteByPrimaryKey(Short id);

    int insert(BizAcquirerCashInfo record);

    int insertSelective(BizAcquirerCashInfo record);

    BizAcquirerCashInfo selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(BizAcquirerCashInfo record);

    int updateByPrimaryKey(BizAcquirerCashInfo record);
}