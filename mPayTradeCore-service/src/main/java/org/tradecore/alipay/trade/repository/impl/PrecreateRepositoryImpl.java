/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.trade.repository.PrecreateRepository;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;

/**
 * 扫码支付仓储服务接口实现类
 * @author HuHui
 * @version $Id: PrecreateRepositoryImpl.java, v 0.1 2016年7月12日 下午11:20:05 HuHui Exp $
 */
@Repository
public class PrecreateRepositoryImpl implements PrecreateRepository {

    /** 日志 */
    private static final Logger  logger = LoggerFactory.getLogger(PrecreateRepositoryImpl.class);

    /** 订单DAO */
    @Resource
    private BizAlipayPayOrderDAO bizAlipayPayOrderDAO;

    @Override
    public BizAlipayPayOrder savePrecreateOrder(BizAlipayPayOrder payOrder) {

        LogUtil.info(logger, "收到扫码支付订单持久化请求");

        try {
            payOrder.setGmtUpdate(new Date());
            bizAlipayPayOrderDAO.insert(payOrder);
        } catch (Exception e) {
            LogUtil.error(e, logger, "扫码支付订单持久化失败,message={0}", e.getMessage());
            throw new RuntimeException("扫码支付订单持久化失败", e);
        }

        return payOrder;
    }

}
