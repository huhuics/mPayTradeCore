package org.tradecore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.tradecore.dao.domain.BizAlipayRefundOrder;

public interface BizAlipayRefundOrderDAO {

    int deleteByPrimaryKey(String id) throws SQLException;

    int insert(BizAlipayRefundOrder record) throws SQLException;

    int insertSelective(BizAlipayRefundOrder record) throws SQLException;

    BizAlipayRefundOrder selectByPrimaryKey(String id) throws SQLException;

    int updateByPrimaryKeySelective(BizAlipayRefundOrder record) throws SQLException;

    int updateByPrimaryKey(BizAlipayRefundOrder record) throws SQLException;

    /**
     * 根据条件查询退款记录<br>
     * 注意：<ul>
     * <li>查询条件不能为空，否则将发生查询整张表记录</li>
     * <li>查询条件在Map参数自行添加，但是添加的参数在对应的Mapper中需要也需要添加</li>
     * </ul>
     * @param paraMap
     */
    List<BizAlipayRefundOrder> selectRefundOrders(Map<String, Object> paraMap) throws SQLException;
}