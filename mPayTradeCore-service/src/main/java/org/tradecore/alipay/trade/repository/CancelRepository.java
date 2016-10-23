/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository;

import java.util.List;

import org.tradecore.dao.domain.BizAlipayCancelOrder;

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
    BizAlipayCancelOrder saveCancelOrder(BizAlipayCancelOrder cancelOrder);

    /**
     * 更新撤销订单记录
     * @param cancelOrder
     */
    void updateCancelOrder(BizAlipayCancelOrder cancelOrder);

    /**
     * 查询撤销成功的记录
     * @param merchantId
     * @param outTradeNo
     * @param alipayTradeNo
     * @param cancelStatus
     * @return
     */
    List<BizAlipayCancelOrder> selectCancelOrder(String merchantId, String outTradeNo, String alipayTradeNo, String cancelStatus);
}
