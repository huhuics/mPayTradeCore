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
import org.tradecore.alipay.enums.OrderCheckEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.QueryFieldConstant;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.UUIDUtil;
import org.tradecore.dao.BizAlipayRefundOrderDAO;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

/**
 * 
 * @author HuHui
 * @version $Id: RefundRepositoryImpl.java, v 0.1 2016年7月11日 下午7:49:34 HuHui Exp $
 */
@Repository
public class RefundRepositoryImpl implements RefundRepository {

    /** 日志 */
    private static final Logger     logger     = LoggerFactory.getLogger(RefundRepositoryImpl.class);

    /** 退款DAO */
    @Resource
    private BizAlipayRefundOrderDAO bizAlipayRefundOrderDAO;

    /** 查询结果数量 */
    private static final int        QUERY_SIZE = 1;

    @Override
    public BizAlipayRefundOrder saveRefundOrder(BizAlipayPayOrder oriOrder, RefundRequest refundRequest) {

        LogUtil.info(logger, "收到退款订单持久化请求,refundRequest={0}", refundRequest);

        BizAlipayRefundOrder refundOrder = convert2RefundOrder(refundRequest, oriOrder.getTotalAmount());

        LogUtil.info(logger, "退款请求对象refundRequest转换成refundOrder对象成功,refundOrder={0}", refundOrder);

        AssertUtil.assertTrue(bizAlipayRefundOrderDAO.insert(refundOrder) > 0, "退款请求数据持久化失败");

        return refundOrder;
    }

    @Override
    public void updateRefundOrder(BizAlipayRefundOrder refundOrder, AlipayF2FRefundResult alipayF2FRefundResult) {

        LogUtil.info(logger, "收到退款订单更新请求");

        AlipayTradeRefundResponse response = alipayF2FRefundResult.getResponse();

        if (alipayF2FRefundResult.isTradeSuccess()) {
            LogUtil.info(logger, "支付宝退款成功");

            //判断金额是否为空
            if (StringUtils.isNotBlank(response.getSendBackFee())) {
                refundOrder.setSendBackFee(new Money(response.getSendBackFee()));
            }

            refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());
            refundOrder.setRefundDetailItemList(JSON.toJSONString(response.getRefundDetailItemList()));
            refundOrder.setBuyerUserId(response.getBuyerUserId());
            refundOrder.setGmtRefundPay(response.getGmtRefundPay());

        } else {
            LogUtil.info(logger, "支付宝退款失败");
            refundOrder.setRefundStatus(AlipayTradeStatusEnum.REFUND_FAILED.getCode());
        }

        if (response != null) {
            refundOrder.setReturnDetail(JSON.toJSONString(response.getBody(), SerializerFeature.UseSingleQuotes));
        }

        refundOrder.setGmtUpdate(new Date());

        //修改本地退款订单数据
        AssertUtil.assertTrue(bizAlipayRefundOrderDAO.updateByPrimaryKey(refundOrder) > 0, "退款订单更新失败");

    }

    @Override
    public List<BizAlipayRefundOrder> selectRefundOrdersByOutTradeNo(String outTradeNo, String refundStatus) {

        LogUtil.info(logger, "收到查询退款订单请求,outTradeNo={0},refundStatus={1}", outTradeNo, refundStatus);

        //封装查询参数。根据商户订单号和退款状态查询所有退款订单
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(QueryFieldConstant.OUT_TRADE_NO, outTradeNo);
        paraMap.put(QueryFieldConstant.REFUND_STATUS, refundStatus);

        List<BizAlipayRefundOrder> refundOrders = bizAlipayRefundOrderDAO.selectRefundOrders(paraMap);

        LogUtil.info(logger, "退款订单查询结果,refundOrders={0}", refundOrders);

        return refundOrders;
    }

    /**
     * 将refundRequest转换成domian对象
     * @param refundRequest
     * @return
     */
    private BizAlipayRefundOrder convert2RefundOrder(RefundRequest refundRequest, Money totalAmount) {

        BizAlipayRefundOrder refundOrder = new BizAlipayRefundOrder();
        refundOrder.setId(UUIDUtil.geneId());
        refundOrder.setAcquirerId(refundRequest.getAcquirerId());
        refundOrder.setMerchantId(refundRequest.getMerchantId());
        refundOrder.setAlipayTradeNo(refundRequest.getAlipayTradeNo());
        refundOrder.setOutTradeNo(refundRequest.getOutTradeNo());
        refundOrder.setTotalAmount(totalAmount);

        //由于refundAmount不可能为空，故此处不再校验
        refundOrder.setRefundAmount(new Money(refundRequest.getRefundAmount()));

        refundOrder.setRefundReason(refundRequest.getRefundReason());
        refundOrder.setOutRequestNo(refundRequest.getOutRequestNo());
        refundOrder.setRefundStatus(AlipayTradeStatusEnum.INIT.getCode());

        //封装merchantDetail参数
        Map<String, Object> merchantDetailMap = new HashMap<String, Object>();
        merchantDetailMap.put(JSONFieldConstant.STORE_ID, refundRequest.getStoreId());
        merchantDetailMap.put(JSONFieldConstant.TERMINAL_ID, refundRequest.getTerminalId());
        refundOrder.setMerchantDetail(JSON.toJSONString(merchantDetailMap));

        refundOrder.setCheckStatus(OrderCheckEnum.UNCHECK.getCode());

        //TODO:时间从配置中读取
        refundOrder.setCreateDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        refundOrder.setGmtCreate(new Date());
        refundOrder.setGmtUpdate(new Date());

        return refundOrder;

    }

}
