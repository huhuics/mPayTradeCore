/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;

import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

/**
 * 支付宝交易服务类
 * @author HuHui
 * @version $Id: TradeServiceImpl.java, v 0.1 2016年7月8日 下午4:13:39 HuHui Exp $
 */
@Service
public class TradeServiceImpl implements TradeService {

    /** 日志 */
    private static final Logger       logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    /** 支付宝交易接口 */
    private static AlipayTradeService alipayTradeService;

    static {
        //1.读取配置文件
        Configs.init("config/zfbinfo.properties");

        //2.工厂方法创建静态支付服务类
        alipayTradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Override
    public AlipayF2FPayResult pay(PayRequest payRequest) {

        LogUtil.info(logger, "收到交易请求参数,payRequest={0}", payRequest);

        //1.校验参数
        AssertUtil.assertTrue(payRequest.validate(), "交易请求参数不合法");

        //2.请求参数转换成支付宝请求参数
        AlipayTradePayRequestBuilder builder = convert2Builder(payRequest);

        //3.持久化

        //4.调用支付宝支付接口
        AlipayF2FPayResult result = alipayTradeService.tradePay(builder);

        LogUtil.info(logger, "支付结果result={0}", result.getTradeStatus().toString());

        //5.根据支付宝返回结果更新本地数据

        return result;
    }

    /**
     * 将支付请求参数转换成支付宝请求
     * @param payRequest 
     * @return
     */
    private AlipayTradePayRequestBuilder convert2Builder(PayRequest payRequest) {
        return new AlipayTradePayRequestBuilder().setOutTradeNo(payRequest.getOutTradeNo()).setSubject(payRequest.getSubject())
            .setAuthCode(payRequest.getAuthCode()).setTotalAmount(payRequest.getTotalAmount()).setStoreId(payRequest.getStoreId())
            .setUndiscountableAmount(payRequest.getUndiscountableAmount()).setBody(payRequest.getBody()).setOperatorId(payRequest.getOperatorId())
            .setExtendParams(payRequest.getExtendParams()).setSellerId(payRequest.getSellerId()).setGoodsDetailList(payRequest.getGoodsDetailList())
            .setTimeoutExpress(payRequest.getTimeoutExpress());
    }

}
