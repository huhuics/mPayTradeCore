/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.trade.factory.AlipayClientFactory;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizMerchantInfoDAO;
import org.tradecore.dao.domain.BizMerchantInfo;

import com.alipay.api.AlipayClient;

/**
 * 商户服务接口实现类
 * @author HuHui
 * @version $Id: MerchantServiceImpl.java, v 0.1 2016年7月13日 下午7:52:55 HuHui Exp $
 */
@Service
public class MerchantServiceImpl implements MerchantService {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    /** 公共请求方法类 */
    private static AlipayClient alipayClient;

    @Resource
    private BizMerchantInfoDAO  bizMerchantInfoDAO;

    /**
     * 构造方法
     */
    public MerchantServiceImpl() {

        //实例化AlipayClient
        alipayClient = AlipayClientFactory.getAlipayClientInstance();
    }

    @Override
    public BizMerchantInfo create(MerchantCreateRequest merchantCreateRequest) {

        LogUtil.info(logger, "收到商户入驻请求参数,merchantCreateRequest={0}", merchantCreateRequest);

        //1.参数校验
        AssertUtil.assertNotNull(merchantCreateRequest, "商户入驻请求不能为空");
        AssertUtil.assertTrue(merchantCreateRequest.validate(), "商户入驻请求参数不合法");

        //2.根据受理机构号和商户外部编号查询

        return null;
    }

}
