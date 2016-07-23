/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.ResponseUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 测试FastJSON
 * @author HuHui
 * @version $Id: FastJSONTest.java, v 0.1 2016年7月9日 上午11:06:22 HuHui Exp $
 */
public class FastJSONTest {

    private static final Logger logger = LoggerFactory.getLogger(FastJSONTest.class);

    @Test
    public void test() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", "owen");
        map.put("age", 25);
        map.put("sex", "男");
        String jsonStr = JSON.toJSONString(map);
        LogUtil.info(logger, "jsonStr:{0}", jsonStr);

        Map<String, Object> parseObject = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });
        parseObject.put("class", "五年三班");

        jsonStr = JSON.toJSONString(parseObject);
        LogUtil.info(logger, "jsonStr:{0}", jsonStr);

    }

    @Test
    public void testParsePara() {
        String jsonStr = "{\"alipay_trade_pay_response\":{\"xode\":\"40004\",\"esg\":\"Business Failed\",\"aay_amount\":\"0.00\",},\"sign\":\"qPCuyNU2DDluz2I8i0=\"}";

        String buildResponse = ResponseUtil.buildResponse(jsonStr, ParamConstant.ALIPAY_TRADE_PAY_RESPONSE);

        LogUtil.info(logger, "buildResponse={0}", buildResponse);
    }

    @Test
    public void testParse() {
        MerchantCreateRequest createRequest = new MerchantCreateRequest();
        createRequest.setService_phone("95534");
        createRequest.setContact_name("张三");

        String jsonStr = JSON.toJSONString(createRequest);
        LogUtil.info(logger, "jsonStr:{0}", jsonStr);

        MerchantCreateRequest parseObject = JSON.parseObject(jsonStr, MerchantCreateRequest.class);

        LogUtil.info(logger, "parseObject:{0}", parseObject);

        Map<String, Object> paraMap = JSON.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {
        });

        LogUtil.info(logger, "paraMap={0}", paraMap);

    }

    @Test
    public void testExtendParams() {
        String extend_params_str = "{\"sys_service _provider_id\":\"333444\"}";
        Map<String, String> extendParams0 = JSON.parseObject(extend_params_str, new TypeReference<Map<String, String>>() {
        });

        LogUtil.info(logger, "extendParams0={0}", extendParams0.get("sys_service _provider_id"));
    }

    @Test
    public void testGoodsList() {
        String goods_list_str = "[{\"price\":28,\"goods_id\":\"123\",\"goods_name\":\"红烧肉\",\"quantity\":1},{\"price\":12,\"goods_id\":\"1111\",\"goods_name\":\"蛋炒饭\",\"quantity\":1}]";
        List<Map<String, String>> listMap = JSON.parseObject(goods_list_str, new TypeReference<List<Map<String, String>>>() {
        });

        for (int i = 0; i < listMap.size(); i++) {
            LogUtil.info(logger, "{0}", listMap.get(i));
        }
    }
}
