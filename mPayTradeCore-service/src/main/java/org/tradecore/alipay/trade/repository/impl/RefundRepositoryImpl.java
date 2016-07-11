/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizAlipayRefundOrderDAO;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

/**
 * 
 * @author HuHui
 * @version $Id: RefundRepositoryImpl.java, v 0.1 2016年7月11日 下午7:49:34 HuHui Exp $
 */
public class RefundRepositoryImpl implements RefundRepository {

    /** 日志 */
    private static final Logger     logger = LoggerFactory.getLogger(RefundRepositoryImpl.class);

    /** 退款DAO */
    @Resource
    private BizAlipayRefundOrderDAO bizAlipayRefundOrderDAO;

    @Override
    public List<BizAlipayRefundOrder> selectRefundOrdersByOutTradeNo(String outTradeNo) {

        LogUtil.info(logger, "收到查询退款订单请求,outTradeNo={0}", outTradeNo);

        List<BizAlipayRefundOrder> refundOrders = bizAlipayRefundOrderDAO.selectRefundOrdersByOutTradeNo(outTradeNo);

        LogUtil.info(logger, "退款订单查询结果,refundOrders={0}", refundOrders);

        return refundOrders;
    }

}
