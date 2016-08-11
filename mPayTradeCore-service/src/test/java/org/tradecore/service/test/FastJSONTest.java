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
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
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

    @Test
    public void testResponsBody() {
        String body = "{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"382***@qq.com\",\"buyer_pay_amount\":\"0.01\",\"buyer_user_id\":\"2088002960279322\",\"fund_bill_list\":[{\"amount\":\"0.01\",\"fund_channel\":\"ALIPAYACCOUNT\"},{\"amount\":\"0.01\",\"fund_channel\":\"DISCOUNT\"}],\"invoice_amount\":\"0.01\",\"open_id\":\"20880032903381910673306320012232\",\"out_trade_no\":\"10880010001348302016081000011017300091\",\"point_amount\":\"0.01\",\"receipt_amount\":\"0.02\",\"send_pay_date\":\"2016-08-10 21:42:32\",\"total_amount\":\"0.02\",\"trade_no\":\"2016081021001004320274033250\",\"trade_status\":\"TRADE_SUCCESS\"},\"sign\":\"Py+Dv58NrTfKbgApvFlDan2DR6nwo8aM88mW0tqAtY3jTSK1sVPVV3ExmSR7HAmqDPnegtj5kJ8XhYdLX9YZwG3UIPtTJqsyi+9eDghwChzeKPLSxFYBrKMZ/nEQCELcGNSw5UL+h1IcJGz/oqO3EpWP/7A+ZDRovvQLaxgjX8o=\"}";

        Map<String, String> bodyMap = JSON.parseObject(body, new TypeReference<Map<String, String>>() {
        });

        Map<String, Object> responseMap = JSON.parseObject(bodyMap.get("alipay_trade_query_response"), new TypeReference<Map<String, Object>>() {
        });

        responseMap.put(JSONFieldConstant.OUT_TRADE_NO, "1234567890");

        bodyMap.put("alipay_trade_query_response", JSON.toJSONString(responseMap));

        String newBody = JSON.toJSONString(bodyMap);

        LogUtil.info(logger, "after:{0}", newBody);
    }
}
