/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;

/**
 * 安全工具类
 * @author HuHui
 * @version $Id: SecureUtil.java, v 0.1 2016年7月19日 下午8:42:34 HuHui Exp $
 */
public class SecureUtil {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(SecureUtil.class);

    /**
     * 对响应进行签名<br>
     * @param sortedParaMap    必须传进来的参数是TreeMap
     * @return
     */
    public static String sign(Map<String, String> sortedParaMap) {

        String sign = null;

        try {
            sign = AlipaySignature.rsaSign(sortedParaMap, Configs.getPrivateKey(), StandardCharsets.UTF_8.displayName());
        } catch (Exception e) {
            LogUtil.error(e, logger, "加签发生异常,paraMap={0}", JSON.toJSONString(sortedParaMap));
            throw new RuntimeException("加签发生异常");
        }

        return sign;
    }

}
