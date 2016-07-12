/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.dao.domain.BizAlipayPayOrder;

/**
 * 撤销仓储接口实现类
 * @author HuHui
 * @version $Id: CancelRepositoryImpl.java, v 0.1 2016年7月12日 下午5:29:11 HuHui Exp $
 */
public class CancelRepositoryImpl implements CancelRepository {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(CancelRepositoryImpl.class);

    @Override
    public void saveCancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest) {
    }

}
