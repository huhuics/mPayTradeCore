/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;

/**
 * 业务抽象Controller
 * @author HuHui
 * @version $Id: AbstractBizController.java, v 0.1 2016年7月21日 下午3:30:20 HuHui Exp $
 */
@Controller
public abstract class AbstractBizController {

    /** 日志 */
    private static final Logger   logger         = LoggerFactory.getLogger(AbstractBizController.class);

    /** 商户服务接口 */
    @Resource
    private AcquirerService       acquirerService;

    /** 收单机构编号 */
    protected static final String ACQUIRER_ID    = "acquirer_id";

    /** 商户支付通过URL */
    protected static final String OUT_NOTIFY_URL = "notify_url";

    /**
     * 获取所有请求参数，并用TreeMap保存
     * @param request
     * @return          返回的Map对象中，参数名是按照字典顺序排序
     */
    public Map<String, String> getParameters(WebRequest request) {

        Map<String, String> paraMap = new TreeMap<String, String>();

        Iterator<String> nameItr = request.getParameterNames();

        if (nameItr != null) {
            while (nameItr.hasNext()) {
                String paraName = nameItr.next();
                String paraValue = request.getParameter(paraName);
                paraMap.put(paraName, paraValue);
            }
        }

        return paraMap;
    }

    /**
     * 验签
     * @param paraMap   参数列表
     * @return          true：验签成功；false：验签失败
     */
    public boolean verify(Map<String, String> paraMap) {

        LogUtil.info(logger, "收到验签请求");

        String acquirerId = paraMap.get(ACQUIRER_ID);

        AssertUtil.assertNotBlank(acquirerId, "收单机构号不能为空");

        boolean verifyRet = false;

        try {
            verifyRet = acquirerService.verify(acquirerId, paraMap);
        } catch (Exception e) {
            LogUtil.error(e, logger, "验签发生异常,paraStr={0}", acquirerId);
            throw new RuntimeException("验签发生异常");
        }

        LogUtil.info(logger, "验签结果verifyRet={0}", verifyRet);

        return verifyRet;
    }
}
