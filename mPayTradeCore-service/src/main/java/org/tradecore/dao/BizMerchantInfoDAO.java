package org.tradecore.dao;

import org.tradecore.dao.domain.BizMerchantInfo;

public interface BizMerchantInfoDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizMerchantInfo record);

    int insertSelective(BizMerchantInfo record);

    BizMerchantInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizMerchantInfo record);

    int updateByPrimaryKey(BizMerchantInfo record);
}