/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.util.HashMap;
import java.util.Map;

import org.tradecore.alipay.trade.constants.ParamConstant;

import com.alibaba.fastjson.JSON;

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
        Map<String, Object> resultMap = new HashMap<String, Object>();

        resultMap.put(responseName, object);
        resultMap.put(ParamConstant.SIGN, sign);

        return JSON.toJSONString(resultMap);
    }

}
