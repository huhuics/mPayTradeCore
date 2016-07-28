package org.tradecore.dao;

import java.util.List;
import java.util.Map;

import org.tradecore.dao.domain.BizMerchantInfo;

public interface BizMerchantInfoDAO {
    int deleteByPrimaryKey(String id);

    int insert(BizMerchantInfo record);

    int insertSelective(BizMerchantInfo record);

    BizMerchantInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BizMerchantInfo record);

    int updateByPrimaryKey(BizMerchantInfo record);

    /**
     * 条件加锁查询单条记录<br>
     * 如果需要增加查询条件，需要在对应的mapper文件中增加
     * @param paraMap
     * @return
     */
    BizMerchantInfo selectForUpdate(Map<String, Object> paraMap);

    /**
     * 查询商户信息<br>
     * 如果需要增加查询条件，需要在对应的mapper文件中增加
     * @param paraMap
     * @return
     */
    List<BizMerchantInfo> selectMerchant(Map<String, Object> paraMap);

    /**
     * 查询商户状态是否合法
     * @param paraMap
     * @return
     */
    BizMerchantInfo selectNormalMerchant(Map<String, Object> paraMap);
}