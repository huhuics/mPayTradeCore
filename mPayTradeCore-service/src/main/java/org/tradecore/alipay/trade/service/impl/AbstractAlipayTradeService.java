/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.common.util.LogUtil;

import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.utils.Utils;

/**
 * 支付宝交易服务抽象类
 * @author HuHui
 * @version $Id: AbstractAlipayTradeService.java, v 0.1 2016年8月4日 下午9:38:21 HuHui Exp $
 */
public abstract class AbstractAlipayTradeService extends AbstractAlipayService {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AbstractAlipayTradeService.class);

    protected AlipayTradeQueryResponse loopQuery(AlipayTradeQueryRequest alipayQueryRequest) {

        LogUtil.info(logger, "收到轮询查询订单请求,outTradeNo={0}", alipayQueryRequest.getBizContent());

        AlipayTradeQueryResponse queryResult = null;

        for (int i = 0; i < Configs.getMaxQueryRetry(); i++) {
            Utils.sleep(Configs.getQueryDuration());

            AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) getResponse(alipayQueryRequest);

            if (response != null) {

            }
        }

        return queryResult;
    }

    protected boolean isStopQuery(AlipayTradeQueryResponse response){
        
        if(StringUtils.equals(response.getCode(), AlipayBizResultEnum.SUCCESS.getCode())){
            //如果查询到交易成功、交易结束、交易关闭，则返回true
            if(StringUtils.equals(response.getTradeStatus(), cs2))
        }
        
        return false;
    }
}
