/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.util.Map;
import java.util.TreeMap;

import org.tradecore.alipay.trade.constants.ParamConstant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 构造接口响应JSON字符串
 * @author HuHui
 * @version $Id: ResponseUtil.java, v 0.1 2016年7月22日 下午8:07:24 HuHui Exp $
 */
public class ResponseUtil {

    /**
     * 创建json返回数据
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
     * 从支付宝返回的body字符串中解析出参数，去掉sign，并将其它参数封装进TreeMap再生产新的签名
     * @param body        json字符串
     * @param methodName  方法名称常量{@link ParamConstant}
     * @return
     */
    public static String buildResponse(String body, String methodName) {
        Map<String, String> parseBodyMap = JSON.parseObject(body, new TypeReference<Map<String, String>>() {
        });

        TreeMap<String, String> sortedParaMap = JSON.parseObject(parseBodyMap.get(methodName), new TypeReference<TreeMap<String, String>>() {
        });

        String sign = SecureUtil.sign(sortedParaMap);

        return buildResponse(methodName, sortedParaMap, sign);
    }

}
