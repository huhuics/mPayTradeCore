package org.tradecore.dao;

import org.tradecore.dao.domain.BizAcquirerCashInfoDtl;

public interface BizAcquirerCashInfoDtlDAO {
    int deleteByPrimaryKey(Short id);

    int insert(BizAcquirerCashInfoDtl record);

    int insertSelective(BizAcquirerCashInfoDtl record);

    BizAcquirerCashInfoDtl selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(BizAcquirerCashInfoDtl record);

    int updateByPrimaryKey(BizAcquirerCashInfoDtl record);
}