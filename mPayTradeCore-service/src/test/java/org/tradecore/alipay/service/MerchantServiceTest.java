/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.DefaultBizResultEnum;
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantModifyResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantModifyRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.facade.result.Result;
import org.tradecore.common.util.LogUtil;
import org.tradecore.service.test.BaseTest;

import com.alibaba.fastjson.JSON;

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
     * @throws Exception 
     */
    @Test
    public void testCreate() throws Exception {

        Assert.assertNotNull(merchantService);

        //封装测试请求参数
        MerchantCreateRequest merchantCreateRequest = new MerchantCreateRequest();
        merchantCreateRequest.setExternal_id("20160802181911");
        merchantCreateRequest.setAcquirer_id("10880010001");
        merchantCreateRequest.setName("测试商户");
        merchantCreateRequest.setAlias_name("测试别名");
        merchantCreateRequest.setService_phone("95188");
        merchantCreateRequest.setContact_name("小二");
        merchantCreateRequest.setContact_phone("0795-85022088");
        merchantCreateRequest.setContact_mobile("15013789478");
        merchantCreateRequest.setContact_email("user@126.com");
        merchantCreateRequest.setCategory_id("2015110500080520");
        merchantCreateRequest.setSource("2016070723781231");
        merchantCreateRequest.setMemo("备注信息");

        MerchantCreateResponse createResponse = merchantService.create(merchantCreateRequest);

        LogUtil.info(logger, "商户入驻结果createResponse={0}:", createResponse);

        Assert.assertNotNull(createResponse);

    }

    @Test
    public void testQuery() throws Exception {

        Assert.assertNotNull(merchantService);

        //封装测试请求参数
        MerchantQueryRequest queryRequest = new MerchantQueryRequest();
        queryRequest.setAcquirer_id("10880010001");
        queryRequest.setExternal_id("1088001000114707489");
        queryRequest.setMerchant_id("27774");
        queryRequest.setOut_external_id("14707489");

        MerchantQueryResponse queryResponse = merchantService.query(queryRequest);

        LogUtil.info(logger, "首次商户查询结果,queryResponse={0}", queryResponse);

        Thread.sleep(1000);
        queryResponse = merchantService.query(queryRequest);

        LogUtil.info(logger, "1s后商户查询结果,queryResponse={0}", queryResponse);

        Thread.sleep(5000);
        queryResponse = merchantService.query(queryRequest);

        LogUtil.info(logger, "5s后商户查询结果,queryResponse={0}", queryResponse);

    }

    @Test
    public void testUpdate() {

    }

    @Test
    public void testModify() throws Exception {

        Assert.assertNotNull(merchantService);

        //封装测试请求参数
        MerchantModifyRequest modifyreateRequest = new MerchantModifyRequest();
        modifyreateRequest.setOut_external_id("14707489");
        modifyreateRequest.setAcquirer_id("10880010001");
        modifyreateRequest.setMerchant_id("27774");
        modifyreateRequest.setName("测试商户");
        modifyreateRequest.setAlias_name("测试别名123");
        modifyreateRequest.setService_phone("9518888");
        modifyreateRequest.setContact_name("小二2");
        modifyreateRequest.setContact_phone("0795-110");
        modifyreateRequest.setContact_mobile("15013789478");
        modifyreateRequest.setContact_email("user@126.com");
        modifyreateRequest.setCategory_id("2015110500080520");
        modifyreateRequest.setSource("2016070723781231");
        modifyreateRequest.setMemo("备注信息");

        MerchantModifyResponse modifyRet = merchantService.modify(modifyreateRequest);

        Assert.assertTrue(StringUtils.equals(modifyRet.getModify_result(), DefaultBizResultEnum.SUCCESS.getCode()));
    }

    @Test
    public void testResponse() {

        Result<MerchantQueryResponse> ret = new Result<MerchantQueryResponse>();

        MerchantQueryResponse response = new MerchantQueryResponse();
        response.setAcquirer_id("acquirer_id123");
        response.setAlias_name("测试别名");
        response.setExternal_id("external_id123");

        ret.setResponse(response);
        ret.setSign("12dfadfasdf342343");

        LogUtil.info(logger, "ret={0}", JSON.toJSONString(ret));

    }

    /**
     * 随机生成id
     * @return
     */
    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }

}
