/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.enums.DefaultBizResultEnum;
import org.tradecore.alipay.enums.SubMerchantBizStatusEnum;
import org.tradecore.alipay.facade.response.MerchantCreateResponse;
import org.tradecore.alipay.facade.response.MerchantModifyResponse;
import org.tradecore.alipay.facade.response.MerchantQueryResponse;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.request.MerchantCreateRequest;
import org.tradecore.alipay.trade.request.MerchantModifyRequest;
import org.tradecore.alipay.trade.request.MerchantQueryRequest;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.alipay.trade.service.MerchantService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizMerchantInfoDAO;
import org.tradecore.dao.domain.BizMerchantInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
public class MerchantServiceImpl extends AbstractAlipayService implements MerchantService {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(MerchantServiceImpl.class);

    @Resource
    private BizMerchantInfoDAO  bizMerchantInfoDAO;

    /** 收单机构服务接口 */
    @Resource
    private AcquirerService     acquirerService;

    @Override
    @Transactional
    public MerchantCreateResponse create(MerchantCreateRequest merchantCreateRequest) {

        LogUtil.info(logger, "收到商户入驻请求参数,merchantCreateRequest={0}", merchantCreateRequest);

        //1.参数校验
        validateRequest(merchantCreateRequest);

        //  1.1判断收单机构是否存在
        AssertUtil.assertTrue(acquirerService.isAcquirerNormal(merchantCreateRequest.getAcquirer_id()), "收单机构不存在或状态非法");

        //2.根据结算中心商户外部编号查询
        BizMerchantInfo oriBizMerchantInfo = selectMerchantInfoByExternalId(merchantCreateRequest.getExternal_id());

        //  2.1.幂等控制
        if (oriBizMerchantInfo != null) {
            return buildResponse(oriBizMerchantInfo.getAcquirerId(), oriBizMerchantInfo.getMerchantId(), AlipayBizResultEnum.SUCCESS.getCode(),
                AlipayBizResultEnum.SUCCESS.getDesc(), null, null);
        }

        //3.将请求转化为支付宝商户入驻请求
        AlipayBossProdSubmerchantCreateRequest alipayCreateRequest = convert2AlipayCreateRequest(merchantCreateRequest);

        //4.调用支付宝商户入驻接口
        AlipayBossProdSubmerchantCreateResponse alipayResponse = null;
        try {
            alipayResponse = (AlipayBossProdSubmerchantCreateResponse) getResponse(alipayCreateRequest);
        } catch (Exception e) {
            LogUtil.error(e, logger, "调用支付宝商户入驻接口异常,alipayCreateRequest={0}", JSON.toJSONString(alipayCreateRequest, SerializerFeature.UseSingleQuotes));
            throw new RuntimeException("调用支付宝商户入驻接口异常", e);
        }

        LogUtil.info(logger, "调用支付宝商户入驻接口响应alipayResponse={0}", JSON.toJSONString(alipayResponse, SerializerFeature.UseSingleQuotes));

        //5.将支付宝响应转化成BizMerchantInfo
        BizMerchantInfo bizMerchantInfo = convert2BizMerchantInfo(merchantCreateRequest, alipayResponse);

        //6.持久化商户信息
        AssertUtil.assertTrue(insert(bizMerchantInfo), "商户信息持久化失败");

        return buildResponse(merchantCreateRequest.getAcquirer_id(), alipayResponse.getSubMerchantId(), alipayResponse.getCode(), alipayResponse.getMsg(),
            alipayResponse.getSubCode(), alipayResponse.getSubMsg());
    }

    @Override
    @Transactional
    public MerchantQueryResponse query(MerchantQueryRequest merchantQueryRequest) {

        LogUtil.info(logger, "收到商户查询请求参数,merchantQueryRequest={0}", merchantQueryRequest);

        //1.参数校验
        validateRequest(merchantQueryRequest);

        //  1.1判断收单机构是否存在
        AssertUtil.assertTrue(acquirerService.isAcquirerNormal(merchantQueryRequest.getAcquirer_id()), "收单机构不存在或状态非法");

        //2.查询本地商户数据
        BizMerchantInfo merchantInfo = selectMerchantInfoByMerchantIdOrOutExternalId(merchantQueryRequest.getAcquirer_id(),
            merchantQueryRequest.getMerchant_id(), merchantQueryRequest.getOut_external_id());

        LogUtil.info(logger, "本地查询商户信息结果merchantInfo={0}", merchantInfo);

        //返回
        return buildResponse(merchantInfo);

    }

    @Override
    public MerchantModifyResponse modify(MerchantModifyRequest merchantModifyRequest) {

        LogUtil.info(logger, "收到商户信息修改请求参数,merchantModifyRequest={0}", merchantModifyRequest);

        //1.参数校验
        validateRequest(merchantModifyRequest);

        //2.校验收单机构
        AssertUtil.assertTrue(acquirerService.isAcquirerNormal(merchantModifyRequest.getAcquirer_id()), "收单机构不存在或状态非法");

        //3.查询本地商户数据
        BizMerchantInfo merchantInfo = selectMerchantInfoByMerchantIdOrOutExternalId(merchantModifyRequest.getAcquirer_id(),
            merchantModifyRequest.getMerchant_id(), merchantModifyRequest.getOut_external_id());

        AssertUtil.assertNotNull(merchantInfo, "商户信息查询为空,商户修改失败");

        //4.将修改请求填充Domain对象
        fillBizMerchantInfo(merchantModifyRequest, merchantInfo);

        return buildMerchantModifyResponse(merchantInfo);
    }

    /**
     * 创建商户信息修改响应
     * @param merchantInfo
     * @return
     */
    private MerchantModifyResponse buildMerchantModifyResponse(BizMerchantInfo merchantInfo) {

        MerchantModifyResponse response = new MerchantModifyResponse();

        response.setAcquirer_id(merchantInfo.getAcquirerId());
        response.setMerchant_id(merchantInfo.getMerchantId());
        response.setExternal_id(merchantInfo.getOutExternalId());

        try {
            if (bizMerchantInfoDAO.updateByPrimaryKey(merchantInfo) > 0) {
                response.setModify_result(DefaultBizResultEnum.SUCCESS.getCode());
                response.setCode(AlipayBizResultEnum.SUCCESS.getCode());
                response.setMsg(AlipayBizResultEnum.SUCCESS.getDesc());
            } else {
                response.setModify_result(DefaultBizResultEnum.FAILED.getCode());
                response.setCode(AlipayBizResultEnum.FAILED.getCode());
                response.setMsg(AlipayBizResultEnum.FAILED.getDesc());
            }
        } catch (Exception e) {
            throw new RuntimeException("更新商户信息失败", e);
        }

        return response;
    }

    /**
     * 将不为空的商户修改请求参数填充至Domain对象
     * @param merchantModifyRequest
     * @param merchantInfo
     */
    private void fillBizMerchantInfo(MerchantModifyRequest merchantModifyRequest, BizMerchantInfo merchantInfo) {

        if (StringUtils.isNotBlank(merchantModifyRequest.getAlias_name())) {
            merchantInfo.setAliasName(merchantModifyRequest.getAlias_name());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getService_phone())) {
            merchantInfo.setServicePhone(merchantModifyRequest.getService_phone());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getContact_name())) {
            merchantInfo.setContactName(merchantModifyRequest.getContact_name());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getContact_phone())) {
            merchantInfo.setContactPhone(merchantModifyRequest.getContact_phone());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getContact_mobile())) {
            merchantInfo.setContactMobile(merchantModifyRequest.getContact_mobile());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getContact_email())) {
            merchantInfo.setContactEmail(merchantModifyRequest.getContact_email());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getCategory_id())) {
            merchantInfo.setCategoryId(merchantModifyRequest.getCategory_id());
        }
        if (StringUtils.isNotBlank(merchantModifyRequest.getMemo())) {
            merchantInfo.setMemo(merchantModifyRequest.getMemo());
        }

        merchantInfo.setGmtUpdate(new Date());

    }

    /**
     * 持久化
     * @param bizMerchantInfo
     * @return
     */
    private boolean insert(BizMerchantInfo bizMerchantInfo) {
        if (bizMerchantInfo != null) {
            try {
                return bizMerchantInfoDAO.insert(bizMerchantInfo) > 0;
            } catch (Exception e) {
                throw new RuntimeException("持久化商户信息失败", e);
            }
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
    @Deprecated
    private BizMerchantInfo convert2BizMerchantInfo(MerchantQueryRequest merchantQueryRequest, AlipayBossProdSubmerchantQueryResponse alipayResponse) {
        //如果业务失败，则返回null
        if (!StringUtils.equals(alipayResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
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
        if (!StringUtils.equals(alipayResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            return null;
        }

        //业务成功，封装参数
        BizMerchantInfo merchantInfo = new BizMerchantInfo();

        merchantInfo.setAcquirerId(merchantCreateRequest.getAcquirer_id());
        merchantInfo.setExternalId(merchantCreateRequest.getExternal_id());
        merchantInfo.setOutExternalId(merchantCreateRequest.getOut_external_id());
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
    @Deprecated
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

        LogUtil.info(logger, "merchant create.bizContent:{0}", alipayCreateRequest.getBizContent());

        return alipayCreateRequest;
    }

    /**
     * 通过externalId查询
     * @param externalId
     * @return
     */
    private BizMerchantInfo selectMerchantInfoByExternalId(String externalId) {

        try {
            return bizMerchantInfoDAO.selectByExternalId(externalId);
        } catch (Exception e) {
            throw new RuntimeException("查询商户信息失败", e);
        }

    }

    /**
     * 通过merchantId或者outExternalId查询
     * @param acquirerId
     * @param outExternalId
     * @return
     */
    private BizMerchantInfo selectMerchantInfoByMerchantIdOrOutExternalId(String acquirerId, String merchantId, String outExternalId) {

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(QueryFieldConstant.ACQUIRER_ID, acquirerId);

        if (StringUtils.isNotBlank(merchantId)) {
            paraMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }

        if (StringUtils.isNotBlank(outExternalId)) {
            paraMap.put(QueryFieldConstant.OUT_EXTERNAL_ID, outExternalId);
        }

        List<BizMerchantInfo> merchants = null;
        try {
            merchants = bizMerchantInfoDAO.selectMerchant(paraMap);
        } catch (SQLException e) {
            throw new RuntimeException("查询商户信息失败", e);
        }

        if (CollectionUtils.isEmpty(merchants)) {
            return null;
        }

        return merchants.get(ParamConstant.FIRST_INDEX);

    }

    /**
     * 创建商户入驻响应
     * @param acquirerId
     * @param merchantId
     * @return
     */
    private MerchantCreateResponse buildResponse(String acquirerId, String merchantId, String code, String msg, String subCode, String subMsg) {

        MerchantCreateResponse createResponse = new MerchantCreateResponse();

        createResponse.setAcquirer_id(acquirerId);
        createResponse.setMerchant_id(merchantId);
        createResponse.setCode(code);
        createResponse.setMsg(msg);
        createResponse.setSub_code(subCode);
        createResponse.setSub_msg(subMsg);

        return createResponse;
    }

    private MerchantQueryResponse buildResponse(BizMerchantInfo merchantInfo) {

        MerchantQueryResponse queryResponse = new MerchantQueryResponse();

        if (merchantInfo == null) {
            queryResponse.setBizFailed("商户查询为空");
            return queryResponse;
        }

        queryResponse.setAcquirer_id(merchantInfo.getAcquirerId());
        queryResponse.setMerchant_id(merchantInfo.getMerchantId());
        queryResponse.setExternal_id(merchantInfo.getOutExternalId());
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
        queryResponse.setCode(AlipayBizResultEnum.SUCCESS.getCode());
        queryResponse.setMsg(AlipayBizResultEnum.SUCCESS.getDesc());

        return queryResponse;

    }

}
