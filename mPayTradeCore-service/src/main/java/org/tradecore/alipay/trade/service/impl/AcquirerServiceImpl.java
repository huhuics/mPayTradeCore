/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
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

/**
 * 收单机构服务接口实现类
 * @author HuHui
 * @version $Id: AcquirerServiceImpl.java, v 0.1 2016年7月18日 下午9:03:50 HuHui Exp $
 */
@Service
public class AcquirerServiceImpl implements AcquirerService {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(AcquirerServiceImpl.class);

    /** 收单机构信息表DAO */
    @Resource
    private BizAcquirerInfoDAO  bizAcquirerInfoDAO;

    /** 商户信息表DAO */
    @Resource
    private BizMerchantInfoDAO  bizMerchantInfoDAO;

    @Override
    public boolean isAcquirerNormal(String acquirerId) {

        LogUtil.info(logger, "收到收单机构查询条件:acquirerId={0}", acquirerId);

        AssertUtil.assertNotBlank(acquirerId, "收单机构编号不能为空");

        //组织查询参数
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(ParamConstant.ACQUIRER_ID, acquirerId);
        paraMap.put(ParamConstant.STATUS, SubMerchantBizStatusEnum.NORMAL.getCode());

        List<BizAcquirerInfo> acquirerInfos = bizAcquirerInfoDAO.selectBizAcquirerInfo(paraMap);

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
}
