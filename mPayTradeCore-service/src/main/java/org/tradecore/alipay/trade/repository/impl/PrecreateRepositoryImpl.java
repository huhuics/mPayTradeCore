/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.repository.PrecreateRepository;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayPayOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;

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
    public BizAlipayPayOrder savePrecreateOrder(PrecreateRequest precreateRequest, AlipayF2FPrecreateResult alipayF2FPrecreateResult) {

        LogUtil.info(logger, "收到扫码支付订单持久化请求");

        AlipayTradePrecreateResponse response = alipayF2FPrecreateResult.getResponse();

        //将公共参数封装成Domain对象
        BizAlipayPayOrder payOrder = convert2PayOrder(precreateRequest);

        if (alipayF2FPrecreateResult.isTradeSuccess()) {
            LogUtil.info(logger, "支付宝扫码支付业务成功");

            payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());
            payOrder.setQrCode(response.getQrCode());

        } else {
            LogUtil.info(logger, "支付宝扫码支付业务失败");

            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
        }

        if (response != null) {
            payOrder.setReturnDetail(JSON.toJSONString(response.getBody(), SerializerFeature.UseSingleQuotes));
        }

        payOrder.setGmtUpdate(new Date());

        //持久化本地数据
        AssertUtil.assertTrue(bizAlipayPayOrderDAO.insert(payOrder) > 0, "扫码支付订单持久化失败");

        return payOrder;
    }

    private BizAlipayPayOrder convert2PayOrder(PrecreateRequest precreateRequest) {

        BizAlipayPayOrder payOrder = new BizAlipayPayOrder();
        payOrder.setId(UUIDUtil.geneId());
        payOrder.setAcquirerId(precreateRequest.getAcquirerId());
        payOrder.setMerchantId(precreateRequest.getMerchantId());
        payOrder.setOutTradeNo(precreateRequest.getOutTradeNo());

        //封装payDetail
        payOrder.setScene(precreateRequest.getScene());

        payOrder.setSellerId(precreateRequest.getSellerId());
        payOrder.setTotalAmount(new Money(precreateRequest.getTotalAmount()));

        //判断金额是否为空
        if (StringUtils.isNotBlank(precreateRequest.getDiscountableAmount())) {
            payOrder.setDiscountableAmount(new Money(precreateRequest.getDiscountableAmount()));
        }

        if (StringUtils.isNotBlank(precreateRequest.getUndiscountableAmount())) {
            payOrder.setUndiscountableAmount(new Money(precreateRequest.getUndiscountableAmount()));
        }

        payOrder.setSubject(precreateRequest.getSubject());
        payOrder.setBody(precreateRequest.getBody());
        payOrder.setAppAuthToken(precreateRequest.getAppAuthToken());
        payOrder.setGoodsDetail(JSON.toJSONString(precreateRequest.getGoodsDetailList()));

        //封装merchantDetail
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.OPERATOR_ID, precreateRequest.getOperatorId());
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, precreateRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, precreateRequest.getTerminalId());
        merchantDetailMap.put(JSONFieldConstant.ALIPAY_STORE_ID, precreateRequest.getAlipayStoreId());
        payOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        if (precreateRequest.getExtendParams() != null) {
            payOrder.setExtendParams(precreateRequest.getExtendParams().toString());
        }

        payOrder.setTimeoutExpress(precreateRequest.getTimeoutExpress());
        payOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        // TODO: 创建日期要改成从配置参数中读取
        payOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        payOrder.setGmtCreate(new Date());

        //设置out_notify_url
        payOrder.setOutNotifyUrl(precreateRequest.getOutNotifyUrl());

        return payOrder;
    }

}
