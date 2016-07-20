/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.service.test.BaseTest;

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

        Assert.assertTrue(!ret);
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
     */
    @Test
    public void testVerify() {

        Assert.assertNotNull(acquirerService);

        String acquirerId = "10880010001";

        String oriSign = "e9zEAe4TTQ4LPLQvETPoLGXTiURcxiAKfMVQ6Hrrsx2hmyIEGvSfAQzbLxHrhyZ48wOJXTsD4FPnt+YGdK57+fP1BCbf9rIVycfjhYCqlFhbTu9pFnZgT55W+xbAFb9y7vL0MyAxwXUXvZtQVqEwW7pURtKilbcBTEW7TAxzgro=";

        Map<String, String> paraMap = new TreeMap<String, String>();
        paraMap.put("app_id", "2014072300007148");
        paraMap.put("method", "alipay.mobile.public.menu.add");
        paraMap.put("charset", "GBK");
        paraMap.put("sign_type", "RSA");
        paraMap.put("timestamp", "2014-07-24 03:07:50");
        paraMap
            .put(
                "biz_content",
                "{\"button\":[{\"actionParam\":\"ZFB_HFCZ\",\"actionType\":\"out\",\"name\":\"话费充值\"},{\"name\":\"查询\",\"subButton\":[{\"actionParam\":\"ZFB_YECX\",\"actionType\":\"out\",\"name\":\"余额查询\"},{\"actionParam\":\"ZFB_LLCX\",\"actionType\":\"out\",\"name\":\"流量查询\"},{\"actionParam\":\"ZFB_HFCX\",\"actionType\":\"out\",\"name\":\"话费查询\"}]},{\"actionParam\":\"http://m.alipay.com\",\"actionType\":\"link\",\"name\":\"最新优惠\"}]}");
        paraMap.put("version", "1.0");

        boolean verifyRet = acquirerService.verify(acquirerId, paraMap, oriSign);

        Assert.assertTrue(verifyRet);

    }
}
