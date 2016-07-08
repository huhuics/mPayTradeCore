package org.tradecore.dao;

import org.tradecore.dao.domain.BizAcquirerInfo;

public interface BizAcquirerInfoDAO {
    int deleteByPrimaryKey(Short id);

    int insert(BizAcquirerInfo record);

    int insertSelective(BizAcquirerInfo record);

    BizAcquirerInfo selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(BizAcquirerInfo record);

    int updateByPrimaryKeyWithBLOBs(BizAcquirerInfo record);

    int updateByPrimaryKey(BizAcquirerInfo record);
}