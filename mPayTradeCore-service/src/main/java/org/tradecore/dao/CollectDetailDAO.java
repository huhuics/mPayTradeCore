package org.tradecore.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.tradecore.dao.domain.CollectDetailInfo;

public interface CollectDetailDAO {

    /**
     * 
     * 方法描述 :获取指定日期的归集行数据  
     * @param day
     * @return List<CollectDetailInfo>
     */
    List<CollectDetailInfo> getListInDay(@Param(value = "day") String day);

    
}
