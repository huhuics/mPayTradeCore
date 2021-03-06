/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.enums.SubMerchantBizStatusEnum;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BizAcquirerInfoDAO;
import org.tradecore.dao.BizMerchantInfoDAO;
import org.tradecore.dao.domain.BizAcquirerInfo;
import org.tradecore.dao.domain.BizMerchantInfo;

import com.alipay.api.internal.util.AlipaySignature;

/**
 * 收单机构服务接口实现类
 * @author HuHui
 * @version $Id: AcquirerServiceImpl.java, v 0.1 2016年7月18日 下午9:03:50 HuHui Exp $
 */
@Service
public class AcquirerServiceImpl implements AcquirerService {

    /** 日志 */
    private static final Logger logger   = LoggerFactory.getLogger(AcquirerServiceImpl.class);

    /** 收单机构信息表DAO */
    @Resource
    private BizAcquirerInfoDAO  bizAcquirerInfoDAO;

    /** 商户信息表DAO */
    @Resource
    private BizMerchantInfoDAO  bizMerchantInfoDAO;

    /** 集合首元素下标 */
    private static final int    ZERO_INX = 0;

    /** &分隔符 */
    private static final String SPE_CHAR = "&";

    /** =分隔符 */
    private static final String EQU_CHAR = "=";

    /** 过滤参数名 */
    private static final String SIGN     = "sign";

    @Override
    public BizAcquirerInfo selectNormalAcquirerById(String acquirerId) {

        LogUtil.info(logger, "收到收单机构查询条件:acquirerId={0}", acquirerId);

        AssertUtil.assertNotBlank(acquirerId, "收单机构编号不能为空");

        List<BizAcquirerInfo> acquirerInfos = selectBizAcquirerInfo(acquirerId, SubMerchantBizStatusEnum.NORMAL.getCode());

        if (CollectionUtils.isEmpty(acquirerInfos)) {
            return null;
        }

        return acquirerInfos.get(ParamConstant.FIRST_INDEX);
    }

    @Override
    public boolean isMerchantNormal(String acquirerId, String merchantId) {

        LogUtil.info(logger, "收单商户信息查询条件,acquirerId={0},merchantId={1}", acquirerId, merchantId);

        AssertUtil.assertNotBlank(acquirerId, "收单机构编号不能为空");
        AssertUtil.assertNotBlank(merchantId, "商户标识号不能为空");

        //组织参数
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ParamConstant.ACQUIRER_ID, acquirerId);
        paraMap.put(ParamConstant.MERCHANT_ID, merchantId);
        paraMap.put(ParamConstant.STATUS, SubMerchantBizStatusEnum.NORMAL.getCode());

        BizMerchantInfo merchant = null;
        try {
            merchant = bizMerchantInfoDAO.selectNormalMerchant(paraMap);
        } catch (Exception e) {
            throw new RuntimeException("查询商户信息失败", e);
        }

        return merchant != null;
    }

    @Override
    public boolean verify(String acquirerId, Map<String, String> paraMap) {

        LogUtil.info(logger, "收到验签请求参数");

        //1.根据收单机构号查询收单机构信息
        List<BizAcquirerInfo> acquirerInfos = selectBizAcquirerInfo(acquirerId, SubMerchantBizStatusEnum.NORMAL.getCode());

        BizAcquirerInfo acquirerInfo = null;
        if (CollectionUtils.isNotEmpty(acquirerInfos)) {
            acquirerInfo = acquirerInfos.get(ZERO_INX);
        }

        AssertUtil.assertNotNull(acquirerInfo, "查询收单机构为空");

        //2.获取商户签名
        String sign = paraMap.get(SIGN);
        AssertUtil.assertNotBlank(sign, "商户签名不能为空");

        //3.将参数转化成键值对形式的待验签字符串
        String paraStr = convert2ParaStr(paraMap);
        AssertUtil.assertTrue(StringUtils.isNotBlank(paraStr), "参数转化成键值对字符串为空");

        //4.验签
        boolean verifyRet = false;
        try {
            verifyRet = AlipaySignature.rsaCheckContent(paraStr, sign, acquirerInfo.getPubKey(), StandardCharsets.UTF_8.displayName());
        } catch (Exception e) {
            throw new RuntimeException("验签发生异常");
        }

        return verifyRet;
    }

    /**
     * 将Map中的参数转化为key1=value1&key2=value2形式<br>
     * 由于Map中的参数已经按照key的字母升序进行排序，故此处不需要再进行排序
     * @param map
     * @return
     */
    private String convert2ParaStr(Map<String, String> map) {

        StringBuilder strBuilder = new StringBuilder();

        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();

            //过滤sign参数
            if (StringUtils.equals(key, SIGN)) {
                continue;
            }

            String value = map.get(key);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            strBuilder.append(key);
            strBuilder.append(EQU_CHAR);
            strBuilder.append(value);
            strBuilder.append(SPE_CHAR);
        }

        //去掉最后一个&符号
        strBuilder.deleteCharAt(strBuilder.length() - 1);

        return strBuilder.toString();
    }

    /**
     * 通过条件查询收单机构信息
     * @param acquirerId
     * @return
     */
    private List<BizAcquirerInfo> selectBizAcquirerInfo(String acquirerId, String status) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ParamConstant.ACQUIRER_ID, acquirerId);
        paraMap.put(ParamConstant.STATUS, status);

        try {
            return bizAcquirerInfoDAO.selectBizAcquirerInfo(paraMap);
        } catch (Exception e) {
            throw new RuntimeException("查询收单机构信息失败", e);
        }
    }

    @Override
    public List<String> selectDistinctAppIds() {
        try {
            return bizAcquirerInfoDAO.selectDistinctAppIds();
        } catch (Exception e) {
            throw new RuntimeException("查询appid失败", e);
        }
    }

}
