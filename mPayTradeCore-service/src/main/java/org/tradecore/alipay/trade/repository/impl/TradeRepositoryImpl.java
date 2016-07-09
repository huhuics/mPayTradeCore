/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.repository.TradeRepository;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;

/**
 * 交易类仓储服务接口实现类
 * @author HuHui
 * @version $Id: TradeRepositoryImpl.java, v 0.1 2016年7月9日 上午10:17:42 HuHui Exp $
 */
@Repository
public class TradeRepositoryImpl implements TradeRepository {

    /** 日志 */
    private static final Logger  logger = LoggerFactory.getLogger(TradeRepositoryImpl.class);

    /** 付款DAO */
    @Resource
    private BizAlipayPayOrderDAO bizAlipayPayOrderDAO;

    @Override
    public boolean savePayOrder(PayRequest payRequest) {
        return false;
    }

    private BizAlipayPayOrder convert2PayOrder(PayRequest payRequest) {

        LogUtil.info(logger, "收到付款持久化请求,payRequest={0}", payRequest);

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(payRequest.getAcquirerId());
        payOrder.setMerchantId(payRequest.getMerchantId());
        payOrder.setOutTradeNo(payRequest.getOutTradeNo());
        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());
        //封装payDetail
        //        payOrder.setPayDetail(payDetail);

        return payOrder;
    }

}
