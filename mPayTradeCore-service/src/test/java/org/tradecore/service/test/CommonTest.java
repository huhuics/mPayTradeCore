/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.ResponseUtil;

import com.alipay.api.response.AlipayTradePayResponse;

/**
 * 
 * @author HuHui
 * @version $Id: CommonTest.java, v 0.1 2016年7月21日 上午10:32:05 HuHui Exp $
 */
public class CommonTest extends BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(CommonTest.class);

    @Test
    public void testUtf8() {
        String str = StandardCharsets.UTF_8.displayName();
        Assert.assertTrue(str.equals("UTF-8"));
    }

    @Test
    public void testBuildErrorResponse() {
        AlipayTradePayResponse payResponse = new AlipayTradePayResponse();
        String ret = ResponseUtil.buildErrorResponse(payResponse, ParamConstant.ALIPAY_TRADE_PAY_RESPONSE, "参数错误");

        LogUtil.info(logger, "ret={0}", ret);
    }

}
