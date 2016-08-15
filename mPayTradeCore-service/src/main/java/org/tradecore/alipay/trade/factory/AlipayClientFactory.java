/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.factory;

import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.common.config.AlipayConfigs;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * Alipay交易相关工厂类
 * @author HuHui
 * @version $Id: AlipayClientFactory.java, v 0.1 2016年7月13日 下午7:35:49 HuHui Exp $
 */
public class AlipayClientFactory {

    /**
     *  SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
     */
    private static final AlipayClient alipayClient;

    static {

        //1.读取配置文件
        AlipayConfigs.init("config/zfbinfo.properties");

        //2.实例化AlipayClient
        alipayClient = new DefaultAlipayClient(AlipayConfigs.getOpenApiDomain(), AlipayConfigs.getAppid(), AlipayConfigs.getPrivateKey(),
            ParamConstant.ALIPAY_CONFIG_FORMAT, ParamConstant.ALIPAY_CONFIG_CHARSET, AlipayConfigs.getAlipayPublicKey());
    }

    /**
     * 返回alipayClient实例
     * @return
     */
    public static AlipayClient getAlipayClientInstance() {
        return alipayClient;
    }

}
