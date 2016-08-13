package org.tradecore.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.tradecore.dao.domain.BizMerchantInfo;

public interface BizMerchantInfoDAO {

    int deleteByPrimaryKey(String id) throws SQLException;

    int insert(BizMerchantInfo record) throws SQLException;

    int insertSelective(BizMerchantInfo record) throws SQLException;

    BizMerchantInfo selectByPrimaryKey(String id) throws SQLException;

    int updateByPrimaryKeySelective(BizMerchantInfo record) throws SQLException;

    int updateByPrimaryKey(BizMerchantInfo record) throws SQLException;

    /**
     * 查询商户信息<br>
     * 如果需要增加查询条件，需要在对应的mapper文件中增加
     * @param paraMap
     */
    List<BizMerchantInfo> selectMerchant(Map<String, Object> paraMap) throws SQLException;

    /**
     * 查询商户状态是否合法<br>
     * 两方面判断:<ul>
     * <li>商户对应的收单机构状态是否合法</li>
     * <li>商户是否合法</li>
     * </ul>
     * @param paraMap
     */
    BizMerchantInfo selectNormalMerchant(Map<String, Object> paraMap) throws SQLException;

    /**
     * 通过结算中心商户外部编号查询商户信息
     * @param externalId    结算中心商户外部编号
     */
    BizMerchantInfo selectByExternalId(String externalId) throws SQLException;
}