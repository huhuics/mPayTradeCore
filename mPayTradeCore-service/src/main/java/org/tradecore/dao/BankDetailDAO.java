package org.tradecore.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.tradecore.dao.domain.BankDetailInfo;

public interface BankDetailDAO {

    /**
     * 
     * 方法描述 :获取日期对应的所有的数据
     * @param day
     * @return List<BankDetailInfo>
     */
    List<BankDetailInfo> getListByDay(@Param(value = "day") String day);

    
}
