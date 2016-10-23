package org.tradecore.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.tradecore.dao.domain.BizPayOrgClearInfo;

public interface BizPayOrgClearDAO {
    /**
     * 
     * 方法描述 :获取日期对应的所有的数据  
     * @param day
     * @return List<BizPayOrgClearInfo>
     */
     List<BizPayOrgClearInfo>  getListByDay(@Param(value = "day")String day);

}