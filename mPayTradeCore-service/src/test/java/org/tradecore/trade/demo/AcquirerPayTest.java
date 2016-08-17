package org.tradecore.trade.demo;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.service.TradeServiceTest;
import org.tradecore.common.util.HttpUtil;
import org.tradecore.common.util.LogUtil;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * 
 * @author prd-wfc
 *
 */
public class AcquirerPayTest {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceTest.class);

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testPay() throws Exception {

        //组装参数
        String merchantNotifyUrl = "http://127.0.0.1:8088/mPay/trade/pay";
        AcquirerPayRequest acqPayRequest = new AcquirerPayRequest();
        acqPayRequest.bizrequest.setMerchantId("34855");
        acqPayRequest.bizrequest.setOutTradeNo("108800100333485512");
        acqPayRequest.bizrequest.setScene("bar_code");
        acqPayRequest.bizrequest.setAuthCode("280678375955593927");
        acqPayRequest.bizrequest.setSubject("结算中心条码交易测试_1471225111111");
        acqPayRequest.bizrequest.setTotalAmount("0.01");

        acqPayRequest.setAcquirerId("10880010033");
        acqPayRequest.setAppId("2016070501581962");
        acqPayRequest.setMethod("alipay.boss.prod.merch");
        acqPayRequest.setFormat("JSON");
        acqPayRequest.setCharset("utf-8");
        acqPayRequest.setSignType("RSA");

        acqPayRequest.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        acqPayRequest.setVersion("1.0");
        acqPayRequest.setNotifyUrl("http://api.test.alipay.net/atinterface/receive_notify.htm");
        acqPayRequest.setAppAuthToken("test");
        acqPayRequest.setWalletType("alipay");
        acqPayRequest.setBizContent(acqPayRequest.bizrequest.buildBizContent());

        Map<String, String> map = acqPayRequest.buildParaMap();

        //签名
        String sign = null;
        try {
            sign = AlipaySignature
                .rsaSign(
                    map,
                    "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALcfL22Dcf+R1WC7fpIlb6cYI0oPBtg2iv9K71sAMS5DShA6LavWo0RTqrsCNsSxGuexHwmWYWcPH9469fKTdEtAnACi/Hr2hQcw7vEehqnjYir8eQljqJpjSd7ZhXdDrfLsX4J2CbUEjkrMM0Q7rx/0cuI7b0yMWBRVPtnwALBvAgMBAAECgYAGtivk1aZ9+XhanUScUqbu9uGEO1zC2+zoQnTXXwBuc6TpR1iZLbq6LF7bj882Ek+sIj/C+DIFtvYyDPMquuDOOUvXm03HRWp20xB8X0fxJzQbS1MOvgxwFW4zDFr39UDrCue/nGVC/qrsWjX6IGYPYvAs2sB+jc3DTJpDJtWmQQJBAPMBZdky7FJ/gr+0KhEJdffWWjfkcdOLka6GetbZEiVWOplTdoIv4bjJFja8dRAQU+BeRmvMpvBnmFjH2RrH6nECQQDA6gP3Cri334zOPS6r1IzlqBy2QvI/11Fn/zyEe8MYtQz3Tkh5MX9R9ejK7lqIv9v1bsusG5SGYHyWMmSvnvjfAkBJ747xetD0eN9rPIHgFSTTd2CTyOnpF3oHw9r0K6+dtJK3u/E+wxrGgkhD9ysW7CDZD1YVznqsgpiTypp/z3vBAkA7deXS91MIGbdkuibwf4sOHkr7Qpc4Zj2JOHqGuz7fFq7wawibkk4UDR+7rMvq6nf5pjTQz49v+71q7g1qtC0xAkBr/ZBpOoO5kc4MZ/DR4TOeFXqaWCmDHsNxmtYPBtnTcuFLQCwH10Ab6Yo2owndvpa1PhdJ7b/h9z1Eom1jy0ba",
                    StandardCharsets.UTF_8.displayName());
        } catch (Exception e) {
            throw new RuntimeException("加签发生异常");
        }
        map.put("sign", sign);

        List<NameValuePair> paraList = acqPayRequest.buildPostParaList(map);

        String response = HttpUtil.httpClientPost(merchantNotifyUrl, paraList);
        LogUtil.info(logger, "完成发送扫码支付到中心,response={0}", response);

    }
}
