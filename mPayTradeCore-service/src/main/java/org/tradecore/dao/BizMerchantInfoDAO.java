package org.tradecore.dao;

import org.tradecore.dao.domain.BizMerchantInfo;

public interface BizMerchantInfoDAO {
    int deleteByPrimaryKey(Short id);

    int insert(BizMerchantInfo record);

    int insertSelective(BizMerchantInfo record);

    BizMerchantInfo selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(BizMerchantInfo record);

    int updateByPrimaryKey(BizMerchantInfo record);
}