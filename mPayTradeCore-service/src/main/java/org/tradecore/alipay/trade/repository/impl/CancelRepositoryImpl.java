/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.CancelRepository;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizAlipayCancelOrderDAO;
import org.tradecore.dao.domain.BizAlipayCancelOrder;

/**
 * 撤销仓储接口实现类
 * @author HuHui
 * @version $Id: CancelRepositoryImpl.java, v 0.1 2016年7月12日 下午5:29:11 HuHui Exp $
 */
@Repository
public class CancelRepositoryImpl implements CancelRepository {

    /** 日志 */
    private static final Logger     logger = LoggerFactory.getLogger(CancelRepositoryImpl.class);

    @Resource
    private BizAlipayCancelOrderDAO bizAlipayCancelOrderDAO;

    /** 撤销DAO */
    @Override
    public BizAlipayCancelOrder saveCancelOrder(BizAlipayCancelOrder cancelOrder) {

        LogUtil.info(logger, "收到撤销订单持久化请求");

        cancelOrder.setGmtUpdate(new Date());

        //持久化撤销订单数据
        AssertUtil.assertTrue(bizAlipayCancelOrderDAO.insert(cancelOrder) > 0, "撤销请求数据持久化失败");

        LogUtil.info(logger, "撤销订单持久化成功");

        return cancelOrder;
    }

    @Override
    public List<BizAlipayCancelOrder> selectCancelOrder(String merchantId, String outTradeNo, String alipayTradeNo, String cancelStatus) {

        LogUtil.info(logger, "收到撤销订单查询请求,merchantId={0},outTradeNo={1},alipayTradeNo={2}", merchantId, outTradeNo, alipayTradeNo);

        Map<String, Object> paraMap = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(merchantId)) {
            paraMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }
        if (StringUtils.isNotEmpty(outTradeNo)) {
            paraMap.put(QueryFieldConstant.OUT_TRADE_NO, outTradeNo);
        }
        if (StringUtils.isNotEmpty(alipayTradeNo)) {
            paraMap.put(QueryFieldConstant.ALIPAY_TRADE_NO, alipayTradeNo);
        }
        if (StringUtils.isNotEmpty(cancelStatus)) {
            paraMap.put(QueryFieldConstant.CANCEL_STATUS, cancelStatus);
        }

        List<BizAlipayCancelOrder> cancelOrders = bizAlipayCancelOrderDAO.selectCancelOrders(paraMap);

        LogUtil.info(logger, "撤销订单查询结果,cancelOrders={0}", cancelOrders);

        return cancelOrders;
    }

}
