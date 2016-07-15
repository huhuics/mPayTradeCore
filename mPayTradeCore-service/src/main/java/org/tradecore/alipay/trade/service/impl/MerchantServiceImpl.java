/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tradecore.alipay.enums.BizResultEnum;
import org.tradecore.alipay.enums.SubMerchantBizStatusEnum;
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.factory.AlipayClientFactory;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizMerchantInfoDAO;
import org.tradecore.dao.domain.BizMerchantInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayBossProdSubmerchantCreateRequest;
import com.alipay.api.request.AlipayBossProdSubmerchantQueryRequest;
import com.alipay.api.response.AlipayBossProdSubmerchantCreateResponse;
import com.alipay.api.response.AlipayBossProdSubmerchantQueryResponse;

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
    @Transactional
    public MerchantCreateResponse create(MerchantCreateRequest merchantCreateRequest) {

        LogUtil.info(logger, "收到商户入驻请求参数,merchantCreateRequest={0}", merchantCreateRequest);

        //1.参数校验
        AssertUtil.assertNotNull(merchantCreateRequest, "商户入驻请求不能为空");
        AssertUtil.assertTrue(merchantCreateRequest.validate(), "商户入驻请求参数不合法");

        //2.根据受理机构号和商户外部编号查询
        BizMerchantInfo oriBizMerchantInfo = selectMerchantInfoByExternalId(merchantCreateRequest.getAcquirer_id(), merchantCreateRequest.getExternal_id());

        //  2.1.幂等控制
        if (oriBizMerchantInfo != null) {
            return buildResponse(oriBizMerchantInfo.getAcquirerId(), oriBizMerchantInfo.getMerchantId());
        }

        //3.将请求转化为支付宝商户入驻请求
        AlipayBossProdSubmerchantCreateRequest alipayCreateRequest = convert2AlipayCreateRequest(merchantCreateRequest);

        //4.调用支付宝商户入驻接口
        AlipayBossProdSubmerchantCreateResponse alipayResponse = null;
        try {
            alipayResponse = alipayClient.execute(alipayCreateRequest);
        } catch (AlipayApiException e) {
            LogUtil.error(e, logger, "调用支付宝商户入驻接口异常,alipayCreateRequest={0}", JSON.toJSONString(alipayCreateRequest, SerializerFeature.UseSingleQuotes));
            throw new RuntimeException("调用支付宝商户入驻接口异常", e);
        }

        LogUtil.info(logger, "调用支付宝商户入驻接口响应alipayResponse={0}", JSON.toJSONString(alipayResponse, SerializerFeature.UseSingleQuotes));

        //5.将支付宝响应转化成BizMerchantInfo
        BizMerchantInfo bizMerchantInfo = convert2BizMerchantInfo(merchantCreateRequest, alipayResponse);

        //6.持久化商户信息
        AssertUtil.assertTrue(insert(bizMerchantInfo), "商户信息持久化失败");

        return buildResponse(merchantCreateRequest.getAcquirer_id(), alipayResponse.getSubMerchantId());
    }

    @Override
    @Transactional
    public MerchantQueryResponse query(MerchantQueryRequest merchantQueryRequest) {

        LogUtil.info(logger, "收到商户查询请求参数,merchantQueryRequest={0}", merchantQueryRequest);

        //1.参数校验
        AssertUtil.assertNotNull(merchantQueryRequest, "商户查询请求不能为空");
        AssertUtil.assertTrue(merchantQueryRequest.validate(), "商户查询请求参数不合法");

        //2.查询本地商户数据
        BizMerchantInfo nativeMerchantInfo = selectMerchantInfoByMerchantIdOrExternalId(merchantQueryRequest.getAcquirer_id(),
            merchantQueryRequest.getSub_merchant_id(), merchantQueryRequest.getExternal_id());

        LogUtil.info(logger, "本地查询商户信息结果nativeMerchantInfo={0}", nativeMerchantInfo);

        //3.本地为空则查询支付宝
        if (nativeMerchantInfo == null) {

            //3.1请求转换
            AlipayBossProdSubmerchantQueryRequest alipayQueryRequest = convert2AlipayQueryRequest(merchantQueryRequest);

            AlipayBossProdSubmerchantQueryResponse alipayResponse;
            try {
                //3.2查询支付宝商户信息
                alipayResponse = alipayClient.execute(alipayQueryRequest);
            } catch (AlipayApiException e) {
                LogUtil.error(e, logger, "调用支付宝商户查询接口异常,alipayQueryRequest={0}", JSON.toJSONString(alipayQueryRequest, SerializerFeature.UseSingleQuotes));
                throw new RuntimeException("调用支付宝商户查询接口异常", e);
            }

            LogUtil.info(logger, "调用支付宝商户查询口响应alipayResponse={0}", JSON.toJSONString(alipayResponse, SerializerFeature.UseSingleQuotes));

            //3.3将支付宝响应转化成BizMerchantInfo
            BizMerchantInfo merchantInfo = convert2BizMerchantInfo(merchantQueryRequest, alipayResponse);

            //3.4持久化商户信息
            AssertUtil.assertTrue(insert(merchantInfo), "商户信息持久化失败");

            return buildResponse(merchantInfo);

        } else {
            //本次查询不为空则直接返回
            return buildResponse(nativeMerchantInfo);
        }

    }

    /**
     * 持久化
     * @param bizMerchantInfo
     * @return
     */
    private boolean insert(BizMerchantInfo bizMerchantInfo) {
        if (bizMerchantInfo != null) {
            return bizMerchantInfoDAO.insert(bizMerchantInfo) > 0;
        }

        //返回true的意思是，如果bizMerchantInfo为null，则不插入数据库
        return true;
    }

    /**
     * 将支付宝商户查询响应转换为BizMerchantInfo
     * @param merchantQueryRequest
     * @param alipayResponse
     * @return
     */
    private BizMerchantInfo convert2BizMerchantInfo(MerchantQueryRequest merchantQueryRequest, AlipayBossProdSubmerchantQueryResponse alipayResponse) {
        //如果业务失败，则返回null
        if (!StringUtils.equals(alipayResponse.getCode(), BizResultEnum.SUCCESS.getCode())) {
            return null;
        }

        //业务成功，封装参数
        BizMerchantInfo merchantInfo = new BizMerchantInfo();
        merchantInfo.setExternalId(merchantQueryRequest.getExternal_id());
        merchantInfo.setAcquirerId(merchantQueryRequest.getAcquirer_id());
        merchantInfo.setMerchantId(alipayResponse.getSubMerchantId());
        merchantInfo.setName(alipayResponse.getName());
        merchantInfo.setAliasName(alipayResponse.getAliasName());
        merchantInfo.setServicePhone(alipayResponse.getServicePhone());
        merchantInfo.setContactName(alipayResponse.getContactName());
        merchantInfo.setContactPhone(alipayResponse.getContactPhone());
        merchantInfo.setContactMobile(alipayResponse.getContactMobile());
        merchantInfo.setContactEmail(alipayResponse.getContactEmail());
        merchantInfo.setCategoryId(alipayResponse.getCategoryId());
        //TODO:支付宝sdk中暂时未返回该字段        merchantInfo.setSource();
        merchantInfo.setMemo(alipayResponse.getMemo());
        merchantInfo.setStatus(SubMerchantBizStatusEnum.NORMAL.getCode());
        merchantInfo.setReturnDetail(JSON.toJSONString(alipayResponse.getBody(), SerializerFeature.UseSingleQuotes));
        merchantInfo.setGmtCreate(new Date());
        merchantInfo.setGmtUpdate(new Date());

        return merchantInfo;

    }

    /**
     * 将支付宝商户入驻响应转换为BizMerchantInfo
     * @param merchantCreateRequest
     * @param alipayResponse
     * @return
     */
    private BizMerchantInfo convert2BizMerchantInfo(MerchantCreateRequest merchantCreateRequest, AlipayBossProdSubmerchantCreateResponse alipayResponse) {

        //如果业务失败，则返回null
        if (!StringUtils.equals(alipayResponse.getCode(), BizResultEnum.SUCCESS.getCode())) {
            return null;
        }

        //业务成功，封装参数
        BizMerchantInfo merchantInfo = new BizMerchantInfo();
        merchantInfo.setExternalId(merchantCreateRequest.getExternal_id());
        merchantInfo.setAcquirerId(merchantCreateRequest.getAcquirer_id());
        merchantInfo.setMerchantId(alipayResponse.getSubMerchantId());
        merchantInfo.setName(merchantCreateRequest.getName());
        merchantInfo.setAliasName(merchantCreateRequest.getAlias_name());
        merchantInfo.setServicePhone(merchantCreateRequest.getService_phone());
        merchantInfo.setContactName(merchantCreateRequest.getContact_name());
        merchantInfo.setContactPhone(merchantCreateRequest.getContact_phone());
        merchantInfo.setContactMobile(merchantCreateRequest.getContact_mobile());
        merchantInfo.setContactEmail(merchantCreateRequest.getContact_email());
        merchantInfo.setCategoryId(merchantCreateRequest.getCategory_id());
        merchantInfo.setSource(merchantCreateRequest.getSource());
        merchantInfo.setMemo(merchantCreateRequest.getMemo());
        merchantInfo.setStatus(SubMerchantBizStatusEnum.NORMAL.getCode());
        merchantInfo.setReturnDetail(JSON.toJSONString(alipayResponse.getBody(), SerializerFeature.UseSingleQuotes));
        merchantInfo.setGmtCreate(new Date());
        merchantInfo.setGmtUpdate(new Date());

        return merchantInfo;
    }

    /**
     * 请求转换为支付宝商户查询请求参数
     * @param merchantQueryRequest
     * @return
     */
    private AlipayBossProdSubmerchantQueryRequest convert2AlipayQueryRequest(MerchantQueryRequest merchantQueryRequest) {
        AlipayBossProdSubmerchantQueryRequest alipayQueryRequest = new AlipayBossProdSubmerchantQueryRequest();
        alipayQueryRequest.setBizContent(JSON.toJSONString(merchantQueryRequest));

        return alipayQueryRequest;
    }

    /**
     * 请求转换成支付宝商户入驻请求参数
     * @param merchantCreateRequest
     * @return
     */
    private AlipayBossProdSubmerchantCreateRequest convert2AlipayCreateRequest(MerchantCreateRequest merchantCreateRequest) {
        AlipayBossProdSubmerchantCreateRequest alipayCreateRequest = new AlipayBossProdSubmerchantCreateRequest();
        alipayCreateRequest.setBizContent(JSON.toJSONString(merchantCreateRequest));

        return alipayCreateRequest;
    }

    /**
     * 通过externalId加锁查询
     * @param acquirerId
     * @param externalId
     * @return
     */
    private BizMerchantInfo selectMerchantInfoByExternalId(String acquirerId, String externalId) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(QueryFieldConstant.ACQUIRER_ID, acquirerId);
        paraMap.put(QueryFieldConstant.EXTERNAL_ID, externalId);

        return bizMerchantInfoDAO.selectForUpdate(paraMap);

    }

    /**
     * 通过merchantId或者externalId加锁查询
     * @param acquirerId
     * @param externalId
     * @return
     */
    private BizMerchantInfo selectMerchantInfoByMerchantIdOrExternalId(String acquirerId, String merchantId, String externalId) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(QueryFieldConstant.ACQUIRER_ID, acquirerId);

        if (StringUtils.isNotBlank(merchantId)) {
            paraMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }

        if (StringUtils.isNotBlank(externalId)) {
            paraMap.put(QueryFieldConstant.EXTERNAL_ID, externalId);
        }

        return bizMerchantInfoDAO.selectForUpdate(paraMap);

    }

    /**
     * 创建商户入驻响应
     * @param acquirerId
     * @param merchantId
     * @return
     */
    private MerchantCreateResponse buildResponse(String acquirerId, String merchantId) {

        MerchantCreateResponse createResponse = new MerchantCreateResponse();

        createResponse.setAcquirerId(acquirerId);
        createResponse.setMerchantId(merchantId);

        return createResponse;
    }

    private MerchantQueryResponse buildResponse(BizMerchantInfo merchantInfo) {

        MerchantQueryResponse queryResponse = new MerchantQueryResponse();

        queryResponse.setAcquirer_id(merchantInfo.getAcquirerId());
        queryResponse.setSub_merchant_id(merchantInfo.getMerchantId());
        queryResponse.setExternal_id(merchantInfo.getExternalId());
        queryResponse.setName(merchantInfo.getName());
        queryResponse.setAlias_name(merchantInfo.getAliasName());
        queryResponse.setService_phone(merchantInfo.getServicePhone());
        queryResponse.setContact_name(merchantInfo.getContactName());
        queryResponse.setContact_phone(merchantInfo.getContactPhone());
        queryResponse.setContact_mobile(merchantInfo.getContactMobile());
        queryResponse.setContact_email(merchantInfo.getContactEmail());
        queryResponse.setCategory_id(merchantInfo.getCategoryId());
        queryResponse.setSource(merchantInfo.getSource());
        queryResponse.setMemo(merchantInfo.getMemo());

        return queryResponse;

    }

}
