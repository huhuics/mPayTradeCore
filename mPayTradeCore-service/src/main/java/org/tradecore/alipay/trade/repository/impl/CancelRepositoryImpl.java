/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.BizResultEnum;
import org.tradecore.alipay.trade.repository.CancelRepository;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayCancelOrderDAO;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;

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
    public BizAlipayCancelOrder saveCancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest, AlipayTradeCancelResponse cancelResponse) {

        LogUtil.info(logger, "收到撤销订单持久化请求,cancelRequest={0}", cancelRequest);

        BizAlipayCancelOrder cancelOrder = convert2CancelOrder(oriOrder, cancelRequest);

        if (cancelResponse != null && StringUtils.equals(cancelResponse.getCode(), BizResultEnum.SUCCESS.getCode())) {//撤销成功
            LogUtil.info(logger, "支付宝撤销成功");
            cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());
            cancelOrder.setRetryFlag(cancelResponse.getRetryFlag());
            cancelOrder.setAction(cancelResponse.getAction());
        } else {//业务失败
            LogUtil.info(logger, "支付宝撤销失败");
            cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_FAILED.getCode());
        }

        cancelOrder.setReturnDetail(JSON.toJSONString(cancelResponse.getBody(), SerializerFeature.UseSingleQuotes));
        cancelOrder.setGmtUpdate(new Date());

        //持久化撤销订单数据
        AssertUtil.assertTrue(bizAlipayCancelOrderDAO.insert(cancelOrder) > 0, "撤销请求数据持久化失败");

        return cancelOrder;
    }

    /**
     * 将cancelRequest转换成domian对象
     * @param oriOrder
     * @param cancelRequest
     * @return
     */
    private BizAlipayCancelOrder convert2CancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest) {
        BizAlipayCancelOrder cancelOrder = new BizAlipayCancelOrder();

        cancelOrder.setId(UUIDUtil.geneId());
        cancelOrder.setAcquirerId(cancelRequest.getAcquirerId());
        cancelOrder.setMerchantId(cancelRequest.getMerchantId());
        cancelOrder.setAlipayTradeNo(oriOrder.getAlipayTradeNo());
        cancelOrder.setOutTradeNo(cancelRequest.getOutTradeNo());
        cancelOrder.setTotalAmount(oriOrder.getTotalAmount());

        //TODO:时间从配置中读取
        cancelOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        cancelOrder.setGmtCreate(new Date());

        return cancelOrder;

    }
}
