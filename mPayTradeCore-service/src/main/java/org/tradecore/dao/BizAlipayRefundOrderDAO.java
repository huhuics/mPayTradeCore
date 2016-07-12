package org.tradecore.dao;

import java.util.List;
import java.util.Map;

import org.tradecore.dao.domain.BizAlipayRefundOrder;

public interface BizAlipayRefundOrderDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizAlipayRefundOrder record);

    int insertSelective(BizAlipayRefundOrder record);

    BizAlipayRefundOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizAlipayRefundOrder record);

    int updateByPrimaryKey(BizAlipayRefundOrder record);

    /**
     * 根据条件查询多条退款记录<br>
     * 注意：<ul>
     * <li>查询条件不能为空，否则将发生查询整张表记录</li>
     * <li>查询条件在Map参数自行添加，但是添加的参数在对应的Mapper中需要也需要添加</li>
     * </ul>
     * @param paraMap
     */
    List<BizAlipayRefundOrder> selectRefundOrders(Map<String, Object> paraMap);
}