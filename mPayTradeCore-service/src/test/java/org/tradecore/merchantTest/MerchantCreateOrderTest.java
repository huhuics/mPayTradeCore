package org.tradecore.merchantTest;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.common.util.LogUtil;

import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;

public class MerchantCreateOrderTest {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantCreateOrderTest.class);

    @Test
    public void testCreate() throws Exception {

        Map<String, String> paraMap = new HashMap<String, String>();
        String outUrl = "http://127.0.0.1:8088/mPay/trade/create";

        paraMap.put("acquirer_id", "10880010001");
        paraMap.put("app_id", "2014072300007148");
        paraMap.put("method", "alipay.boss.prod.merch");
        paraMap.put("format", "JSON");
        paraMap.put("charset", "utf-8");
        paraMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        paraMap.put("version", "1.0");
        paraMap.put("notify_url", "http://api.test.alipay.net/atinterface/recieve_notify.htm");
        paraMap.put("wallet_type", "alipay");

        BizContent biz_content = new BizContent();
        biz_content.setMerchantId("27774");
        biz_content.setTradeNo("20150320010101082");
        biz_content.setTotalAmount("20.00");
        biz_content.setSubject("iphone");
        paraMap.put(ParamConstant.SIGN_TYPE, ParamConstant.SIGN_TYPE_VALUE);

        paraMap.put("biz_content", JSON.toJSONString(biz_content));

        //签名
        String sign = null;
        try {
            sign = AlipaySignature
                .rsaSign(
                    paraMap,
                    "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALcfL22Dcf+R1WC7fpIlb6cYI0oPBtg2iv9K71sAMS5DShA6LavWo0RTqrsCNsSxGuexHwmWYWcPH9469fKTdEtAnACi/Hr2hQcw7vEehqnjYir8eQljqJpjSd7ZhXdDrfLsX4J2CbUEjkrMM0Q7rx/0cuI7b0yMWBRVPtnwALBvAgMBAAECgYAGtivk1aZ9+XhanUScUqbu9uGEO1zC2+zoQnTXXwBuc6TpR1iZLbq6LF7bj882Ek+sIj/C+DIFtvYyDPMquuDOOUvXm03HRWp20xB8X0fxJzQbS1MOvgxwFW4zDFr39UDrCue/nGVC/qrsWjX6IGYPYvAs2sB+jc3DTJpDJtWmQQJBAPMBZdky7FJ/gr+0KhEJdffWWjfkcdOLka6GetbZEiVWOplTdoIv4bjJFja8dRAQU+BeRmvMpvBnmFjH2RrH6nECQQDA6gP3Cri334zOPS6r1IzlqBy2QvI/11Fn/zyEe8MYtQz3Tkh5MX9R9ejK7lqIv9v1bsusG5SGYHyWMmSvnvjfAkBJ747xetD0eN9rPIHgFSTTd2CTyOnpF3oHw9r0K6+dtJK3u/E+wxrGgkhD9ysW7CDZD1YVznqsgpiTypp/z3vBAkA7deXS91MIGbdkuibwf4sOHkr7Qpc4Zj2JOHqGuz7fFq7wawibkk4UDR+7rMvq6nf5pjTQz49v+71q7g1qtC0xAkBr/ZBpOoO5kc4MZ/DR4TOeFXqaWCmDHsNxmtYPBtnTcuFLQCwH10Ab6Yo2owndvpa1PhdJ7b/h9z1Eom1jy0ba",
                    StandardCharsets.UTF_8.displayName());
        } catch (Exception e) {
            throw new RuntimeException("加签发生异常");
        }

        paraMap.put(ParamConstant.SIGN, sign);

        List<NameValuePair> paraList = buildPostParaList(paraMap);

        //发送
        String response = send(outUrl, paraList);

        LogUtil.info(logger, "订单创建HTTP调用结果,response={0}", response);

    }

    private List<NameValuePair> buildPostParaList(Map<String, String> paraMap) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>(paraMap.size());

        for (String key : paraMap.keySet()) {
            NameValuePair nvPair = new NameValuePair(key, paraMap.get(key));
            pairList.add(nvPair);
        }

        return pairList;
    }

    private String send(String outUrl, List<NameValuePair> paraList) {
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(outUrl);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        postMethod.getParams().setContentCharset("utf-8");
        try {
            NameValuePair[] pair = new NameValuePair[paraList.size()];
            for (int i = 0; i < paraList.size(); i++) {
                pair[i] = paraList.get(i);
            }
            postMethod.addParameters(pair);
            client.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            LogUtil.error(e, logger, "Http调用发送异常, url={0}", outUrl);
        } finally {
            postMethod.releaseConnection();
        }
        return response;
    }
}
