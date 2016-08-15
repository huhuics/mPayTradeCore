package org.tradecore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.tradecore.dao.domain.BizAcquirerInfo;

public interface BizAcquirerInfoDAO {

    int deleteByPrimaryKey(Short id) throws SQLException;

    int insert(BizAcquirerInfo record) throws SQLException;

    int insertSelective(BizAcquirerInfo record) throws SQLException;

    BizAcquirerInfo selectByPrimaryKey(Short id) throws SQLException;

    int updateByPrimaryKeySelective(BizAcquirerInfo record) throws SQLException;

    int updateByPrimaryKey(BizAcquirerInfo record) throws SQLException;

    /**
     * 查询收单机构信息<br>
     * 如果需要添加查询参数，需要在对应的Mapper文件中进行参数拼装<br>
     * 注意：如果paraMap为空，将查询表中所有数据
     * @param paraMap
     * @return
     */
    @Cacheable(value = "acquirerInfoCache")
    List<BizAcquirerInfo> selectBizAcquirerInfo(Map<String, Object> paraMap) throws SQLException;
}