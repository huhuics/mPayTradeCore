/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;

/**
 * 扫码支付仓储服务接口
 * @author HuHui
 * @version $Id: PrecreateRepository.java, v 0.1 2016年7月12日 下午9:54:46 HuHui Exp $
 */
public interface PrecreateRepository {

    /**
     * 根据支付宝调用结果，将扫码支付请求本地持久化
     * @param precreateRequest           扫码支付请求
     * @param alipayF2FPrecreateResult   支付宝返回调用结果
     * @return 持久化之后的Domian对象
     */
    BizAlipayPayOrder savePrecreateOrder(PrecreateRequest precreateRequest, AlipayF2FPrecreateResult alipayF2FPrecreateResult);

}
