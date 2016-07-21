/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import java.util.Map;

/**
 * 收单机构服务接口
 * @author HuHui
 * @version $Id: AcquirerService.java, v 0.1 2016年7月18日 下午8:59:50 HuHui Exp $
 */
public interface AcquirerService {

    /**
     * 根据收单机构编号查询某收单机构是否存在且状态为正常
     * @param acquirerId  收单机构编号
     * @return            该收单机构是否存在且状态正常
     */
    boolean isAcquirerNormal(String acquirerId);

    /**
     * 查询acquirerId对应的收单机构存在商户标识号为merchantId的商户状态是否正常<br>
     * 如果收单机构不存在，则返回false<br>
     * 如果商户不存在，则返回false<br>
     * 如果商户状态为ABNORMAL，则返回false
     * @param acquirerId   收单机构号
     * @param merchantId   商户标识号
     * @return
     */
    boolean isMerchantNormal(String acquirerId, String merchantId);

    /**
     * 验签
     * @param acquirerId  收单机构编号
     * @param paraMap     收单机构传过来所有参数，使用TreeMap存储
     * @return
     */
    boolean verify(String acquirerId, Map<String, String> paraMap);

}
