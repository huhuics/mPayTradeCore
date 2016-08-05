/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.tradecore.dao.domain.BizAlipayPayOrder;

/**
 * 扫码支付仓储服务接口
 * @author HuHui
 * @version $Id: PrecreateRepository.java, v 0.1 2016年7月12日 下午9:54:46 HuHui Exp $
 */
public interface PrecreateRepository {

    /**
     * 扫码支付请求本地持久化
     * @param payOrder           
     * @return 持久化之后的Domian对象
     */
    BizAlipayPayOrder savePrecreateOrder(BizAlipayPayOrder payOrder);

}
