/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.config;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

/**
 * 读取支付宝远程调用配置参数
 * @author HuHui
 * @version $Id: AlipayConfigs.java, v 0.1 2016年8月15日 下午9:56:36 HuHui Exp $
 */
public class AlipayConfigs {

    /** 日志 */
    private static final Logger  logger = LoggerFactory.getLogger(AlipayConfigs.class);

    private static Configuration configs;

    private static String        openApiDomain;                                        // 支付宝openapi域名
    private static String        mcloudApiDomain;                                      // 支付宝mcloudmonitor域名
    private static String        pid;                                                  // 商户partner id
    private static String        appid;                                                // 商户应用id

    private static String        privateKey;                                           // RSA私钥，用于对商户请求报文加签
    private static String        publicKey;                                            // RSA公钥，仅用于验证开发者网关
    private static String        alipayPublicKey;                                      // 支付宝RSA公钥，用于验签支付宝应答

    private static int           maxQueryRetry;                                        // 最大查询次数
    private static long          queryDuration;                                        // 查询间隔（毫秒）

    private static int           maxCancelRetry;                                       // 最大撤销次数
    private static long          cancelDuration;                                       // 撤销间隔（毫秒）

    private static long          heartbeatDelay;                                       // 交易保障线程第一次调度延迟（秒）
    private static long          heartbeatDuration;                                    // 交易保障线程调度间隔（秒）

    private AlipayConfigs() {
        // No Constructor
    }

    // 根据文件名读取配置文件，文件后缀名必须为.properties
    public synchronized static void init(String filePath) {
        if (configs != null) {
            return;
        }

        try {
            configs = new PropertiesConfiguration(filePath);
        } catch (ConfigurationException e) {
            LogUtil.error(e, logger, "读取配置文件错误");
        }

        if (configs == null) {
            throw new IllegalStateException("can`t find file by path:" + filePath);
        }

        openApiDomain = configs.getString("open_api_domain");
        mcloudApiDomain = configs.getString("mcloud_api_domain");

        pid = configs.getString("pid");
        appid = configs.getString("appid");

        // RSA
        privateKey = configs.getString("private_key");
        publicKey = configs.getString("public_key");
        alipayPublicKey = configs.getString("alipay_public_key");

        // 查询参数
        maxQueryRetry = configs.getInt("max_query_retry");
        queryDuration = configs.getLong("query_duration");
        maxCancelRetry = configs.getInt("max_cancel_retry");
        cancelDuration = configs.getLong("cancel_duration");

        // 交易保障调度线程
        heartbeatDelay = configs.getLong("heartbeat_delay");
        heartbeatDuration = configs.getLong("heartbeat_duration");

        LogUtil.info(logger, "配置文件名:{0}", filePath);
        LogUtil.info(logger, "{0}", description());
    }

    public static String description() {
        StringBuilder sb = new StringBuilder("Configs{");
        sb.append("支付宝openapi网关: ").append(openApiDomain).append("\n");
        if (StringUtils.isNotEmpty(mcloudApiDomain)) {
            sb.append(", 支付宝mcloudapi网关域名: ").append(mcloudApiDomain).append("\n");
        }

        if (StringUtils.isNotEmpty(pid)) {
            sb.append(", pid: ").append(pid).append("\n");
        }
        sb.append(", appid: ").append(appid).append("\n");

        sb.append(", 商户RSA私钥: ").append(getKeyDescription(privateKey)).append("\n");
        sb.append(", 商户RSA公钥: ").append(getKeyDescription(publicKey)).append("\n");
        sb.append(", 支付宝RSA公钥: ").append(getKeyDescription(alipayPublicKey)).append("\n");

        sb.append(", 查询重试次数: ").append(maxQueryRetry).append("\n");
        sb.append(", 查询间隔(毫秒): ").append(queryDuration).append("\n");
        sb.append(", 撤销尝试次数: ").append(maxCancelRetry).append("\n");
        sb.append(", 撤销重试间隔(毫秒): ").append(cancelDuration).append("\n");

        sb.append(", 交易保障调度延迟(秒): ").append(heartbeatDelay).append("\n");
        sb.append(", 交易保障调度间隔(秒): ").append(heartbeatDuration).append("\n");
        sb.append("}");
        return sb.toString();
    }

    private static String getKeyDescription(String key) {
        int showLength = 6;
        if (StringUtils.isNotEmpty(key) && key.length() > showLength) {
            return new StringBuilder(key.substring(0, showLength)).append("******").append(key.substring(key.length() - showLength)).toString();
        }
        return null;
    }

    public static Configuration getConfigs() {
        return configs;
    }

    public static String getOpenApiDomain() {
        return openApiDomain;
    }

    public static String getMcloudApiDomain() {
        return mcloudApiDomain;
    }

    public static void setMcloudApiDomain(String mcloudApiDomain) {
        AlipayConfigs.mcloudApiDomain = mcloudApiDomain;
    }

    public static String getPid() {
        return pid;
    }

    public static String getAppid() {
        return appid;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public static String getAlipayPublicKey() {
        return alipayPublicKey;
    }

    public static int getMaxQueryRetry() {
        return maxQueryRetry;
    }

    public static long getQueryDuration() {
        return queryDuration;
    }

    public static int getMaxCancelRetry() {
        return maxCancelRetry;
    }

    public static long getCancelDuration() {
        return cancelDuration;
    }

    public static void setConfigs(Configuration configs) {
        AlipayConfigs.configs = configs;
    }

    public static void setOpenApiDomain(String openApiDomain) {
        AlipayConfigs.openApiDomain = openApiDomain;
    }

    public static void setPid(String pid) {
        AlipayConfigs.pid = pid;
    }

    public static void setAppid(String appid) {
        AlipayConfigs.appid = appid;
    }

    public static void setPrivateKey(String privateKey) {
        AlipayConfigs.privateKey = privateKey;
    }

    public static void setPublicKey(String publicKey) {
        AlipayConfigs.publicKey = publicKey;
    }

    public static void setAlipayPublicKey(String alipayPublicKey) {
        AlipayConfigs.alipayPublicKey = alipayPublicKey;
    }

    public static void setMaxQueryRetry(int maxQueryRetry) {
        AlipayConfigs.maxQueryRetry = maxQueryRetry;
    }

    public static void setQueryDuration(long queryDuration) {
        AlipayConfigs.queryDuration = queryDuration;
    }

    public static void setMaxCancelRetry(int maxCancelRetry) {
        AlipayConfigs.maxCancelRetry = maxCancelRetry;
    }

    public static void setCancelDuration(long cancelDuration) {
        AlipayConfigs.cancelDuration = cancelDuration;
    }

    public static long getHeartbeatDelay() {
        return heartbeatDelay;
    }

    public static void setHeartbeatDelay(long heartbeatDelay) {
        AlipayConfigs.heartbeatDelay = heartbeatDelay;
    }

    public static long getHeartbeatDuration() {
        return heartbeatDuration;
    }

    public static void setHeartbeatDuration(long heartbeatDuration) {
        AlipayConfigs.heartbeatDuration = heartbeatDuration;
    }
}
