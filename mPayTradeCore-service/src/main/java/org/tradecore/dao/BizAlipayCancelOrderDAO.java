package org.tradecore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.tradecore.dao.domain.BizAlipayCancelOrder;

public interface BizAlipayCancelOrderDAO {

    int deleteByPrimaryKey(String id) throws SQLException;

    int insert(BizAlipayCancelOrder record) throws SQLException;

    int insertSelective(BizAlipayCancelOrder record) throws SQLException;

    BizAlipayCancelOrder selectByPrimaryKey(String id) throws SQLException;

    int updateByPrimaryKeySelective(BizAlipayCancelOrder record) throws SQLException;

    int updateByPrimaryKey(BizAlipayCancelOrder record) throws SQLException;

    /**
     * 根据条件查询撤销记录<br>
     * 注意：<ul>
     * <li>查询条件不能为空，否则将发生查询整张表记录</li>
     * <li>查询条件在Map参数自行添加，但是添加的参数在对应的Mapper中需要也需要添加</li>
     * </ul>
     * @param paraMap
     */
    List<BizAlipayCancelOrder> selectCancelOrders(Map<String, Object> paraMap) throws SQLException;

}