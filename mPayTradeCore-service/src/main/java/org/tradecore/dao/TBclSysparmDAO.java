package org.tradecore.dao;

import org.tradecore.dao.domain.TBclSysparm;

public interface TBclSysparmDAO {
    int deleteByPrimaryKey(Short id);

    int insert(TBclSysparm record);

    int insertSelective(TBclSysparm record);

    TBclSysparm selectByPrimaryKey(Short id);

    int updateByPrimaryKeySelective(TBclSysparm record);

    int updateByPrimaryKey(TBclSysparm record);
}