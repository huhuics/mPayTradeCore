/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.common.util.LogUtil;
import org.tradecore.service.test.BaseTest;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * 收单机构服务单元测试
 * @author HuHui
 * @version $Id: AcquirerServiceTest.java, v 0.1 2016年7月19日 下午9:05:38 HuHui Exp $
 */
public class AcquirerServiceTest extends BaseTest {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AcquirerServiceTest.class);

    @Resource
    private AcquirerService     acquirerService;

    @Test
    public void testIsAcquirerNormal() {

        Assert.assertNotNull(acquirerService);

        String acquirerId = "1088001000";

        boolean ret = acquirerService.isAcquirerNormal(acquirerId);

        Assert.assertTrue(ret);
    }

    @Test
    public void testIsMerchantNormal() {

        Assert.assertNotNull(acquirerService);

        String acquirerId = "1088001000";

        String merchantId = "67";

        boolean ret = acquirerService.isMerchantNormal(acquirerId, merchantId);

        Assert.assertTrue(!ret);
    }

    /**
     * 测试验签
     * @throws Exception 
     */
    @Test
    public void testVerify() throws Exception {

        Assert.assertNotNull(acquirerService);

        String acquirerId = "10880010001";

        Map<String, String> paraMap = new TreeMap<String, String>();
        paraMap.put("app_id", "2014072300007148");
        paraMap.put("method", "alipay.mobile.public.menu.add");
        paraMap.put("charset", "UTF-8");
        paraMap.put("sign_type", "RSA");
        paraMap.put("timestamp", "2014-07-24 03:07:50");
        paraMap
            .put(
                "biz_content",
                "{\"button\":[{\"actionParam\":\"ZFB_HFCZ\",\"actionType\":\"out\",\"name\":\"话费充值\"},{\"name\":\"查询\",\"subButton\":[{\"actionParam\":\"ZFB_YECX\",\"actionType\":\"out\",\"name\":\"余额查询\"},{\"actionParam\":\"ZFB_LLCX\",\"actionType\":\"out\",\"name\":\"流量查询\"},{\"actionParam\":\"ZFB_HFCX\",\"actionType\":\"out\",\"name\":\"话费查询\"}]},{\"actionParam\":\"http://m.alipay.com\",\"actionType\":\"link\",\"name\":\"最新优惠\"}]}");
        paraMap.put("version", "1.0");

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALcfL22Dcf+R1WC7fpIlb6cYI0oPBtg2iv9K71sAMS5DShA6LavWo0RTqrsCNsSxGuexHwmWYWcPH9469fKTdEtAnACi/Hr2hQcw7vEehqnjYir8eQljqJpjSd7ZhXdDrfLsX4J2CbUEjkrMM0Q7rx/0cuI7b0yMWBRVPtnwALBvAgMBAAECgYAGtivk1aZ9+XhanUScUqbu9uGEO1zC2+zoQnTXXwBuc6TpR1iZLbq6LF7bj882Ek+sIj/C+DIFtvYyDPMquuDOOUvXm03HRWp20xB8X0fxJzQbS1MOvgxwFW4zDFr39UDrCue/nGVC/qrsWjX6IGYPYvAs2sB+jc3DTJpDJtWmQQJBAPMBZdky7FJ/gr+0KhEJdffWWjfkcdOLka6GetbZEiVWOplTdoIv4bjJFja8dRAQU+BeRmvMpvBnmFjH2RrH6nECQQDA6gP3Cri334zOPS6r1IzlqBy2QvI/11Fn/zyEe8MYtQz3Tkh5MX9R9ejK7lqIv9v1bsusG5SGYHyWMmSvnvjfAkBJ747xetD0eN9rPIHgFSTTd2CTyOnpF3oHw9r0K6+dtJK3u/E+wxrGgkhD9ysW7CDZD1YVznqsgpiTypp/z3vBAkA7deXS91MIGbdkuibwf4sOHkr7Qpc4Zj2JOHqGuz7fFq7wawibkk4UDR+7rMvq6nf5pjTQz49v+71q7g1qtC0xAkBr/ZBpOoO5kc4MZ/DR4TOeFXqaWCmDHsNxmtYPBtnTcuFLQCwH10Ab6Yo2owndvpa1PhdJ7b/h9z1Eom1jy0ba";

        String sign = AlipaySignature.rsaSign(paraMap, privateKey, StandardCharsets.UTF_8.displayName());
        paraMap.put("sign", sign);

        LogUtil.info(logger, "生成签名sign={0}", sign);

        boolean verifyRet = acquirerService.verify(acquirerId, paraMap, sign);

        Assert.assertTrue(verifyRet);

        boolean ret = acquirerService.isAcquirerNormal(acquirerId);

        Assert.assertTrue(ret);

        ret = acquirerService.isAcquirerNormal(acquirerId);

        Assert.assertTrue(ret);
    }
}
