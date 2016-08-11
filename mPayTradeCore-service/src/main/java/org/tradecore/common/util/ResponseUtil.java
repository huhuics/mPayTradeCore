/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.facade.response.BaseResponse;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.mvc.controller.AlipayTradeController;
import org.tradecore.mvc.controller.MerchantController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.api.AlipayResponse;

/**
 * 构造接口响应JSON字符串
 * @author HuHui
 * @version $Id: ResponseUtil.java, v 0.1 2016年7月22日 下午8:07:24 HuHui Exp $
 */
public class ResponseUtil {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(ResponseUtil.class);

    /**
     * 创建json返回数据<br>
     * {@link MerchantController}
     * @param responseName   响应名称，常量，在{@link ParamConstant}中定义
     * @param object         响应的对象
     * @return               响应JSON字符串
     */
    public static String buildResponse(String responseName, Object object, String sign) {

        //使用TreeMap将保证序列化后，变量名按照字典顺序排列
        Map<String, Object> resultMap = new TreeMap<String, Object>();

        resultMap.put(responseName, object);
        resultMap.put(ParamConstant.SIGN, sign);

        return JSON.toJSONString(resultMap);
    }

    /**
     * 从支付宝返回的body字符串中解析出参数，去掉sign，并将其它参数封装进TreeMap再生产新的签名<br>
     * {@link AlipayTradeController}
     * @param body        json字符串
     * @param methodName  方法名称常量{@link ParamConstant}
     * @return
     */
    public static String buildResponse(String body, String methodName) {
        Map<String, String> parseBodyMap = JSON.parseObject(body, new TypeReference<Map<String, String>>() {
        });

        TreeMap<String, Object> sortedParaMap = JSON.parseObject(parseBodyMap.get(methodName), new TypeReference<TreeMap<String, Object>>() {
        });

        String sign = SecureUtil.sign(sortedParaMap);

        return buildResponse(methodName, sortedParaMap, sign);
    }

    /**
     * 将Map中的参数拼装成key1=value1&key2=value2的形式
     * @param params
     * @return
     */
    public static String buildResponse(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) {
            return "";
        }
        StringBuffer strBuff = new StringBuffer();
        for (String key : params.keySet()) {
            strBuff.append(key);
            strBuff.append("=");
            try {
                strBuff.append(URLEncoder.encode(params.get(key), StandardCharsets.UTF_8.displayName()));
            } catch (UnsupportedEncodingException e) {
                LogUtil.error(e, logger, "编码异常={0}", e.getMessage());
            }
            strBuff.append("&");
        }

        //去除最后一个&符号
        strBuff.deleteCharAt(strBuff.length() - 1);

        return strBuff.toString();
    }

    public static String buildErrorResponse(AlipayResponse response, String responseName, String message) {

        Map<String, Object> resultMap = new TreeMap<String, Object>();

        if (response == null) {
            BaseResponse baseResponse = new BaseResponse();
            baseResponse.setCode(AlipayBizResultEnum.FAILED.getCode());
            baseResponse.setMsg(message);
            resultMap.put(responseName, JSON.toJSONString(baseResponse));
            return buildResponse(JSON.toJSONString(resultMap), responseName);
        }

        response.setCode(AlipayBizResultEnum.FAILED.getCode());
        response.setMsg(message);
        resultMap.put(responseName, JSON.toJSONString(response));
        return buildResponse(JSON.toJSONString(resultMap), responseName);
    }
}
