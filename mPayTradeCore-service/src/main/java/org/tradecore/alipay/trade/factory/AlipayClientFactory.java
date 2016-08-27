/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.factory;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.ehcache.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.common.config.AlipayConfigs;
import org.tradecore.common.util.LogUtil;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

/**
 * Alipay交易相关工厂类
 * @author HuHui
 * @version $Id: AlipayClientFactory.java, v 0.1 2016年7月13日 下午7:35:49 HuHui Exp $
 */
@Service
public class AlipayClientFactory {

    /** 日志 */
    private static final Logger       logger          = LoggerFactory.getLogger(AlipayClientFactory.class);

    /** 收单机构服务 */
    @Resource
    private AcquirerService           acquirerService;

    /** key=收单机构编号, value=alipayClient */
    private Map<String, AlipayClient> alipayClientMap = new ConcurrentHashMap<String, AlipayClient>();

    /**
     * 构造函数初始化
     */
    public AlipayClientFactory() {

        //读取配置文件
        AlipayConfigs.init("config/zfbinfo.properties");

    }

    /**
     * 返回alipayClient实例
     * @return
     */
    public AlipayClient getAlipayClientInstance(String appId) {

        if (alipayClientMap.size() == 0) {
            init();
        }

        AlipayClient alipayClient = alipayClientMap.get(appId);

        //如果appId对应的client对象为空，则重新创建client并放入alipayClientMap中
        if (alipayClient == null) {
            LogUtil.info(logger, "获取alipayClient为空,不存在此appid={0}对应的alipayClient,重新创建此appid对应的client", appId);

            AlipayClient client = null;
            try {
                client = new DefaultAlipayClient(AlipayConfigs.getOpenApiDomain(), appId, AlipayConfigs.getPrivateKey(), ParamConstant.ALIPAY_CONFIG_FORMAT,
                    ParamConstant.ALIPAY_CONFIG_CHARSET, AlipayConfigs.getAlipayPublicKey());
            } catch (Exception e) {
                throw new RuntimeException("创建支付宝远程连接异常", e);
            }
            alipayClientMap.put(appId, client);

            return client;
        }
        return alipayClient;
    }

    private void init() {
        //获取所有appId
        List<String> appIds = acquirerService.selectDistinctAppIds();

        //初始化alipayClientMap
        for (String appId : appIds) {
            AlipayClient alipayClient = null;
            try {
                alipayClient = new DefaultAlipayClient(AlipayConfigs.getOpenApiDomain(), appId, AlipayConfigs.getPrivateKey(),
                    ParamConstant.ALIPAY_CONFIG_FORMAT, ParamConstant.ALIPAY_CONFIG_CHARSET, AlipayConfigs.getAlipayPublicKey());
            } catch (Exception e) {
                throw new RuntimeException("创建支付宝远程连接异常", e);
            }

            alipayClientMap.put(appId, alipayClient);
        }
    }

}
