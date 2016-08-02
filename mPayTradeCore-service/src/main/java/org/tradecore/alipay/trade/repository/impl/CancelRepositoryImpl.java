/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.CancelRepository;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayCancelOrderDAO;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;

/**
 * 撤销仓储接口实现类
 * @author HuHui
 * @version $Id: CancelRepositoryImpl.java, v 0.1 2016年7月12日 下午5:29:11 HuHui Exp $
 */
@Repository
public class CancelRepositoryImpl implements CancelRepository {

    /** 日志 */
    private static final Logger     logger = LoggerFactory.getLogger(CancelRepositoryImpl.class);

    @Resource
    private BizAlipayCancelOrderDAO bizAlipayCancelOrderDAO;

    /** 撤销DAO */
    @Override
    public BizAlipayCancelOrder saveCancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest, AlipayTradeCancelResponse cancelResponse) {

        LogUtil.info(logger, "收到撤销订单持久化请求");

        BizAlipayCancelOrder cancelOrder = convert2CancelOrder(oriOrder, cancelRequest);

        if (cancelResponse != null) {
            if (StringUtils.equals(cancelResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {//撤销成功
                LogUtil.info(logger, "支付宝撤销成功");
                cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());
                //撤销完成，交易状态改为TRADE_CLOSED
                oriOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
            } else {//业务失败
                LogUtil.info(logger, "支付宝撤销失败");
                cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_FAILED.getCode());
            }

            cancelOrder.setRetryFlag(cancelResponse.getRetryFlag());
            cancelOrder.setAction(cancelResponse.getAction());
            cancelOrder.setReturnDetail(JSON.toJSONString(cancelResponse.getBody(), SerializerFeature.UseSingleQuotes));
        }

        cancelOrder.setGmtUpdate(new Date());

        //持久化撤销订单数据
        AssertUtil.assertTrue(bizAlipayCancelOrderDAO.insert(cancelOrder) > 0, "撤销请求数据持久化失败");

        LogUtil.info(logger, "撤销订单持久化成功");

        return cancelOrder;
    }

    @Override
    public List<BizAlipayCancelOrder> selectCancelOrder(String merchantId, String outTradeNo, String alipayTradeNo, String cancelStatus) {

        LogUtil.info(logger, "收到撤销订单查询请求,merchantId={0},outTradeNo={1},alipayTradeNo={2}", merchantId, outTradeNo, alipayTradeNo);

        Map<String, Object> paraMap = new HashMap<String, Object>();

        if (StringUtils.isNotEmpty(merchantId)) {
            paraMap.put(QueryFieldConstant.MERCHANT_ID, merchantId);
        }
        if (StringUtils.isNotEmpty(outTradeNo)) {
            paraMap.put(QueryFieldConstant.OUT_TRADE_NO, outTradeNo);
        }
        if (StringUtils.isNotEmpty(alipayTradeNo)) {
            paraMap.put(QueryFieldConstant.ALIPAY_TRADE_NO, alipayTradeNo);
        }
        if (StringUtils.isNotEmpty(cancelStatus)) {
            paraMap.put(QueryFieldConstant.CANCEL_STATUS, cancelStatus);
        }

        List<BizAlipayCancelOrder> cancelOrders = bizAlipayCancelOrderDAO.selectCancelOrders(paraMap);

        LogUtil.info(logger, "撤销订单查询结果,cancelOrders={0}", cancelOrders);

        return cancelOrders;
    }

    /**
     * 将cancelRequest转换成domian对象
     * @param oriOrder
     * @param cancelRequest
     * @return
     */
    private BizAlipayCancelOrder convert2CancelOrder(BizAlipayPayOrder oriOrder, CancelRequest cancelRequest) {
        BizAlipayCancelOrder cancelOrder = new BizAlipayCancelOrder();

        cancelOrder.setId(UUIDUtil.geneId());
        cancelOrder.setAcquirerId(cancelRequest.getAcquirerId());
        cancelOrder.setMerchantId(cancelRequest.getMerchantId());
        cancelOrder.setAlipayTradeNo(oriOrder.getAlipayTradeNo());

        //为防止商户不传outTradeNo值，此处用原始订单的outTradeNo，保证撤销表中outTradeNo值一定不为空
        cancelOrder.setOutTradeNo(oriOrder.getOutTradeNo());

        cancelOrder.setTotalAmount(oriOrder.getTotalAmount());

        //TODO:时间从配置中读取
        cancelOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        cancelOrder.setGmtCreate(new Date());

        return cancelOrder;

    }

}
