/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.repository.TradeRepository;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;

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

        LogUtil.info(logger, "收到付款持久化请求,payRequest={0}", payRequest);

        BizAlipayPayOrder payOrder = convert2PayOrder(payRequest);

        LogUtil.info(logger, "付款请求payRequest转化为payOrder对象成功,payOrder={0}", payOrder);

        return bizAlipayPayOrderDAO.insert(payOrder) > 0;

    }

    /**
     * 将payRequest转化为domian对象
     * @param payRequest
     * @return
     */
    private BizAlipayPayOrder convert2PayOrder(PayRequest payRequest) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(payRequest.getAcquirerId());
        payOrder.setMerchantId(payRequest.getMerchantId());
        payOrder.setOutTradeNo(payRequest.getOutTradeNo());
        payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());

        //封装payDetail
        Map<String, Object> payDetailMap = new HashMap<String, Object>();
        payDetailMap.put(JSONFieldConstant.SCENE, payRequest.getScene());
        payOrder.setPayDetail(JSON.toJSONString(payDetailMap));

        payOrder.setAuthCode(payRequest.getAuthCode());
        payOrder.setSellerId(payRequest.getSellerId());
        payOrder.setTotalAmount(new Money(payRequest.getTotalAmount()));
        payOrder.setDiscountableAmount(new Money(payRequest.getDiscountableAmount()));
        payOrder.setUndiscountableAmount(new Money(payRequest.getUndiscountableAmount()));
        payOrder.setSubject(payRequest.getSubject());
        payOrder.setBody(payRequest.getBody());
        payOrder.setGoodsDetail(JSON.toJSONString(payRequest.getGoodsDetailList()));

        //封装merchantDetail
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.OPERATOR_ID, payRequest.getOperatorId());
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, payRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, payRequest.getTerminalId());
        merchantDetailMap.put(JSONFieldConstant.ALIPAY_STORE_ID, payRequest.getAlipayStoreId());
        payOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        payOrder.setExtendParams(payRequest.getExtendParams().toString());
        payOrder.setTimeoutExpress(payRequest.getTimeoutExpress());
        payOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        // TODO: 对账日期和创建日期要改成从配置参数中读取
        payOrder.setCheckDate(DateUtil.format(new Date(), DateUtil.shortFormat));
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());
        payOrder.setGmtUpdate(new Date());

        return payOrder;
    }
}
