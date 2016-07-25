/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.config.Resources;
import org.tradecore.common.util.LogUtil;

/**
 * 测试Resources类
 * @author HuHui
 * @version $Id: ResourcesTest.java, v 0.1 2016年7月25日 下午4:50:39 HuHui Exp $
 */
public class ResourcesTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(ResourcesTest.class);

    @Test
    public void testConfig() {
        String notifyUrl = Resources.NOTIFY_URL.getString("notify_url");
        LogUtil.info(logger, "notifyUrl={0}", notifyUrl);

        Assert.assertTrue(StringUtils.equals(notifyUrl, "http://183.62.226.168:8089/mPay/tradeNotify/receive"));
    }

}
