/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.domain.BizMerchantInfo;
import org.tradecore.service.test.BaseTest;

import com.alipay.api.response.AlipayBossProdSubmerchantCreateResponse;

/**
 * 
 * @author HuHui
 * @version $Id: MerchantServiceTest.java, v 0.1 2016年7月14日 上午10:13:37 HuHui Exp $
 */
public class MerchantServiceTest extends BaseTest {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceTest.class);

    /**
     * 商户服务接口
     */
    @Resource
    private MerchantService     merchantService;

    /**
     * 测试商户入驻
     */
    @Test
    public void testCreate() {

        Assert.assertNotNull(merchantService);

        //封装测试请求参数
        MerchantCreateRequest merchantCreateRequest = new MerchantCreateRequest();
        merchantCreateRequest.setExternal_id(geneRandomId());
        merchantCreateRequest.setAcquirer_id("acquirerId" + geneRandomId());
        merchantCreateRequest.setName("测试商户");
        merchantCreateRequest.setAlias_name("测试别名");
        merchantCreateRequest.setService_phone("95188");
        merchantCreateRequest.setContact_name("小二");
        merchantCreateRequest.setContact_phone("0571-85022088");
        merchantCreateRequest.setContact_mobile("13688888888");
        merchantCreateRequest.setContact_email("user@163.com");
        merchantCreateRequest.setCategory_id("2015110500080520");
        merchantCreateRequest.setSource("2016070723781231");
        merchantCreateRequest.setMemo("测试备注信息");

        AlipayBossProdSubmerchantCreateResponse createResponse = merchantService.create(merchantCreateRequest);

        LogUtil.info(logger, "商户入驻结果createResponse={0}:", createResponse);

        Assert.assertNotNull(createResponse);

    }

    @Test
    public void testQuery() {

        Assert.assertNotNull(merchantService);

        //封装测试请求参数
        MerchantQueryRequest queryRequest = new MerchantQueryRequest();
        queryRequest.setAcquirer_id("acquirerId1468487593003");
        queryRequest.setExternal_id("1468486584701");
        queryRequest.setSub_merchant_id("27");

        BizMerchantInfo merchantInfo = merchantService.query(queryRequest);

        LogUtil.info(logger, "商户查询结果,merchantInfo={0}", merchantInfo);

    }

    /**
     * 随机生成id
     * @return
     */
    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }

}
