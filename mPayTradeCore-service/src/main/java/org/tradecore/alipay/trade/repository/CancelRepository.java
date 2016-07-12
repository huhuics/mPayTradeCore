/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alipay.api.response.AlipayTradeCancelResponse;

/**
 * 撤销仓储服务
 * @author HuHui
 * @version $Id: CancelRepository.java, v 0.1 2016年7月12日 下午5:22:28 HuHui Exp $
 */
public interface CancelRepository {

    /**
     * 保存撤销订单记录
     * @param oriOrder
     * @param cancelRequest
     * @param cancelResponse
     * @return
     */
    BizAlipayCancelOrder saveCancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest, AlipayTradeCancelResponse cancelResponse);

}
