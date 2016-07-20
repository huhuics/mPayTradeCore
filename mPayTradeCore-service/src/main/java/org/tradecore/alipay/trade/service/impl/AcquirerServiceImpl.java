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
import org.tradecore.common.util.SecureUtil;
import org.tradecore.dao.BizAcquirerInfoDAO;
import org.tradecore.dao.BizMerchantInfoDAO;
import org.tradecore.dao.domain.BizAcquirerInfo;
import org.tradecore.dao.domain.BizMerchantInfo;

import com.alibaba.fastjson.JSON;

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

    @Override
    public boolean isAcquirerNormal(String acquirerId) {

        LogUtil.info(logger, "收到收单机构查询条件:acquirerId={0}", acquirerId);

        AssertUtil.assertNotBlank(acquirerId, "收单机构编号不能为空");

        List<BizAcquirerInfo> acquirerInfos = selectBizAcquirerInfo(acquirerId);

        if (CollectionUtils.isEmpty(acquirerInfos)) {
            return false;
        }

        return acquirerInfos.size() > 0;
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

        List<BizMerchantInfo> merchants = bizMerchantInfoDAO.selectMerchant(paraMap);

        if (CollectionUtils.isEmpty(merchants)) {
            return false;
        }

        return merchants.size() > 0;
    }

    @Override
    public boolean verify(String acquirerId, Map<String, String> paraMap, String oriSign) {

        LogUtil.info(logger, "收到验签请求参数,acquirerId={0},paraMap={1}", acquirerId, JSON.toJSONString(paraMap));

        //1.根据收单机构号查询收单机构信息
        List<BizAcquirerInfo> acquirerInfos = selectBizAcquirerInfo(acquirerId);

        BizAcquirerInfo acquirerInfo = null;
        if (CollectionUtils.isNotEmpty(acquirerInfos)) {
            acquirerInfo = acquirerInfos.get(ZERO_INX);
        }

        //2.将参数转化成键值对形式的字符串
        String paraStr = convert2ParaStr(paraMap);
        AssertUtil.assertTrue(StringUtils.isNotBlank(paraStr), "参数转化成键值对字符串为空");

        //3.验签
        boolean verifyRet = false;
        String alipayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
        try {
            //            verifyRet = SecureUtil.verifySign(acquirerInfo.getCheckCert(), paraStr, oriSign);
            verifyRet = SecureUtil.verifySign(alipayPublicKey.getBytes(StandardCharsets.UTF_8), paraStr, oriSign);
        } catch (Exception e) {
            LogUtil.error(e, logger, "验签发生异常,paraStr={0}", paraStr);
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
     * 通过收单机构编号查询收单机构
     * @param acquirerId
     * @return
     */
    private List<BizAcquirerInfo> selectBizAcquirerInfo(String acquirerId) {
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ParamConstant.ACQUIRER_ID, acquirerId);
        paraMap.put(ParamConstant.STATUS, SubMerchantBizStatusEnum.NORMAL.getCode());

        return bizAcquirerInfoDAO.selectBizAcquirerInfo(paraMap);
    }

}
