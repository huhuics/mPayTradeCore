package org.tradecore.trade.demo;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

public class AcquirerCreateOrderTest {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AcquirerCreateOrderTest.class);

    @Test
    public void testCreate() throws Exception {

    	//结算中心url
        String outUrl = "http://127.0.0.1:8088/mPay/trade/create";
        CreateOrderRequest orderRequest = new CreateOrderRequest();
        //组装参数--以下参数皆为必填项，可选项的添加可自行增加
        orderRequest.setAcquirerId("10880010001");
        orderRequest.setAppId("2014072300007148");
        orderRequest.setMethod("alipay.boss.prod.merch");
        orderRequest.setFormat("JSON");
        orderRequest.setCharset("utf-8");
        orderRequest.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        orderRequest.setVersion("1.0");
        orderRequest.setSignType("RSA");
        orderRequest.setNotifyUrl("http://api.test.alipay.net/atinterface/recieve_notify.htm");
        orderRequest.setWalletType("alipay");

        orderRequest.setMerchantId("27774");
        orderRequest.setTradeNo(getRandomId());
        orderRequest.setBuyerLogOnId("fenall@sina.cn");
        orderRequest.setTotalAmount("20.00");
        orderRequest.setSubject("iphone");

        //根据orderRequest获取List<NameValuePair>
        List<NameValuePair> paraList = orderRequest.buildPostParaList();

        //发送请求
        String response = send(outUrl, paraList);

        LogUtil.info(logger, "订单创建HTTP调用结果,response={0}", response);

    }

    private String send(String outUrl, List<NameValuePair> paraList) {
    	
        String response = "";
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(outUrl);
        postMethod.getParams().setContentCharset("utf-8");
        
        try {
        	//设置请求参数
            NameValuePair[] pair = new NameValuePair[paraList.size()];
            for (int i = 0; i < paraList.size(); i++) {
                pair[i] = paraList.get(i);
            }
            postMethod.addParameters(pair);
            //执行调用
            client.executeMethod(postMethod);
            response = postMethod.getResponseBodyAsString();
        } catch (Exception e) {
            LogUtil.error(e, logger, "Http调用发送异常, url={0}", outUrl);
        } finally {
            postMethod.releaseConnection();
        }
        
        return response;
    }

    private String getRandomId() {

        return System.currentTimeMillis() + (long) (Math.random() * 100000000L) + "";
    }
}
