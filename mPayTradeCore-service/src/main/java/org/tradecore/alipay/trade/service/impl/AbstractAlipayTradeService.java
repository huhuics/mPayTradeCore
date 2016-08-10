/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.convertor.Convertor;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.dao.domain.BizAlipayPayOrder;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayResponse;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.utils.Utils;

/**
 * 支付宝交易服务抽象类
 * @author HuHui
 * @version $Id: AbstractAlipayTradeService.java, v 0.1 2016年8月4日 下午9:38:21 HuHui Exp $
 */
public abstract class AbstractAlipayTradeService extends AbstractAlipayService {

    /** 日志 */
    private static final Logger    logger          = LoggerFactory.getLogger(AbstractAlipayTradeService.class);

    /** 线程池 */
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static final String    TRADE_STATUS    = " tradeStatus:";

    /**
     * 根据订单查询结果填充订单信息<br>
     * 如果查询订单支付成功，则更新订单信息并返回；如果支付失败，则发起撤销
     * @param payOrder
     * @param outTradeNo
     * @param appAuthToken
     * @param payResponse
     * @param queryResponse
     * @return
     */
    @Deprecated
    protected BizAlipayPayOrder checkQueryAndCancel(BizAlipayPayOrder payOrder, PayRequest payRequest, AlipayTradePayResponse payResponse,
                                                    AlipayTradeQueryResponse queryResponse) {

        LogUtil.info(logger, "收到订单查询结果校验请求,outTradeNo={0}", payRequest.getOutTradeNo());

        //1.查询为支付成功
        if (isQuerySuccess(queryResponse)) {

            LogUtil.info(logger, "订单查询结果为支付成功");

            //1.1 将查询成功信息填充至订单信息中
            setPayOrderSuccess(payOrder, queryResponse);

            //1.2 将查询成功信息填充至支付应答
            convert2PayResponse(payResponse, queryResponse);

            return payOrder;
        }

        LogUtil.info(logger, "订单查询结果为支付失败");

        //2.查询为支付失败
        AlipayTradeCancelRequest cancelRequest = new AlipayTradeCancelRequest();
        cancelRequest.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, payRequest.getAppAuthToken());

        //封装查询参数并序列化
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(JSONFieldConstant.OUT_TRADE_NO, payRequest.getOutTradeNo());

        cancelRequest.setBizContent(JSON.toJSONString(paraMap));

        AlipayTradeCancelResponse cancelResponse = getCancelPayResult(cancelRequest);

        //以下为同步撤销返回结果，不是异步撤销返回结果
        if (isResponseError(cancelResponse)) {
            //撤销失败则表示交易状态未知
            payOrder.setOrderStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
            LogUtil.warn(logger, "查询订单支付失败且撤销失败,订单状态未知");
        } else {
            //撤销成功，标记交易状态为TRADE_CLOSED，撤销状态为CANCEL_SUCCESS
            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
            payOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());
            LogUtil.warn(logger, "查询订单支付失败且撤销成功");
        }

        return payOrder;

    }

    /**
     * 获取撤销结果
     * @param cancelRequest
     * @return
     */
    protected AlipayTradeCancelResponse getCancelPayResult(AlipayTradeCancelRequest cancelRequest) {

        //远程调用支付宝撤销接口
        AlipayTradeCancelResponse cancelResponse = (AlipayTradeCancelResponse) getResponse(cancelRequest);

        //1.撤销成功，则直接返回
        if (isResponseSuccess(cancelResponse)) {
            return cancelResponse;
        }

        //2.撤销失败
        if (isNeedCancelRetry(cancelResponse)) {
            //如果需要重试，首先记录日志(需人工处理)，再调用异步撤销
            LogUtil.warn(logger, "开始异步撤销,cancelRequest.getBizContent={0}", cancelRequest.getBizContent());

            asyncCancel(cancelRequest);
        }

        return cancelResponse;
    }

    /**
     * 异步撤销
     * @param cancelRequest
     */
    protected void asyncCancel(final AlipayTradeCancelRequest cancelRequest) {

        executorService.submit(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < Configs.getMaxCancelRetry(); i++) {
                    Utils.sleep(Configs.getCancelDuration());

                    AlipayTradeCancelResponse cancelResponse = (AlipayTradeCancelResponse) getResponse(cancelRequest);

                    //如果撤销成功或者响应告知不需要重试撤销，则返回(无论撤销是成功还是失败，都需要人工处理)
                    if (isResponseSuccess(cancelResponse) || !isNeedCancelRetry(cancelResponse)) {
                        return;
                    }

                }

            }
        });

    }

    /**
     * 轮询订单
     * @param alipayQueryRequest
     * @return
     */
    @Deprecated
    protected AlipayTradeQueryResponse loopQuery(AlipayTradeQueryRequest alipayQueryRequest) {

        LogUtil.info(logger, "收到轮询订单请求,outTradeNo={0}", alipayQueryRequest.getBizContent());

        AlipayTradeQueryResponse queryResult = null;

        for (int i = 0; i < Configs.getMaxQueryRetry(); i++) {
            Utils.sleep(Configs.getQueryDuration());

            AlipayTradeQueryResponse response = (AlipayTradeQueryResponse) getResponse(alipayQueryRequest);

            if (response != null) {
                if (isStopQuery(response)) {
                    return response;
                }
                queryResult = response;
            }
        }

        return queryResult;
    }

    /**
     * 判断轮询是否结束<br>
     * 如果查询到交易成功、交易结束、交易关闭，则终止轮询
     * @param response
     * @return
     */
    protected boolean isStopQuery(AlipayTradeQueryResponse response) {

        if (StringUtils.equals(response.getCode(), AlipayBizResultEnum.SUCCESS.getCode())) {
            //如果查询到交易成功、交易结束、交易关闭，则返回true
            if (StringUtils.equals(response.getTradeStatus(), AlipayTradeStatusEnum.TRADE_SUCCESS.getCode())
                || StringUtils.equals(response.getTradeStatus(), AlipayTradeStatusEnum.TRADE_FINISHED.getCode())
                || StringUtils.equals(response.getTradeStatus(), AlipayTradeStatusEnum.TRADE_CLOSED.getCode())) {

                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    /**
     * 条码支付成功时将查询响应转化为支付响应
     */
    protected AlipayTradePayResponse convert2PayResponse(AlipayTradePayResponse payResponse, AlipayTradeQueryResponse queryResponse) {

        //可明确是业务成功
        payResponse.setCode(AlipayBizResultEnum.SUCCESS.getCode());

        //补充交易状态信息
        StringBuilder msg = new StringBuilder(queryResponse.getMsg()).append(TRADE_STATUS).append(queryResponse.getTradeStatus());
        payResponse.setMsg(msg.toString());
        payResponse.setSubCode(queryResponse.getSubCode());
        payResponse.setSubMsg(queryResponse.getSubMsg());
        payResponse.setBody(queryResponse.getBody());
        payResponse.setParams(queryResponse.getParams());

        payResponse.setGmtPayment(queryResponse.getSendPayDate());
        payResponse.setBuyerLogonId(queryResponse.getBuyerLogonId());
        payResponse.setFundBillList(queryResponse.getFundBillList());
        payResponse.setOpenId(queryResponse.getOpenId());
        payResponse.setOutTradeNo(queryResponse.getOutTradeNo());
        payResponse.setReceiptAmount(queryResponse.getReceiptAmount());
        payResponse.setTotalAmount(queryResponse.getTotalAmount());
        //支付宝订单号
        payResponse.setTradeNo(queryResponse.getTradeNo());

        return payResponse;
    }

    /**
     * 条码支付成功时设置交易数据
     */
    protected BizAlipayPayOrder setPayOrderSuccess(BizAlipayPayOrder payOrder, AlipayTradeQueryResponse payResponse) {

        payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_SUCCESS.getCode());
        payOrder.setAlipayTradeNo(payResponse.getTradeNo());

        if (StringUtils.isNotBlank(payResponse.getReceiptAmount())) {
            payOrder.setReceiptAmount(new Money(payResponse.getReceiptAmount()));
        }

        payOrder.setFundBillList(JSON.toJSONString(payResponse.getFundBillList()));
        payOrder.setDiscountGoodsDetail(payResponse.getDiscountGoodsDetail());

        //TODO: 由于查询订单响应中无支付成功字段，故此处对账时间用当前时间
        payOrder.setCheckDate(DateUtil.format(new Date(), DateUtil.shortFormat));

        //支付宝返回消息体
        payOrder.setReturnDetail(payResponse.getBody());

        payOrder.setGmtUpdate(new Date());

        return payOrder;
    }

    /**
     * 条码支付成功时设置交易数据
     */
    protected BizAlipayPayOrder setPayOrderSuccess(BizAlipayPayOrder payOrder, AlipayTradePayResponse payResponse) {

        payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_SUCCESS.getCode());
        payOrder.setAlipayTradeNo(payResponse.getTradeNo());

        payOrder.setAccountDetail(Convertor.reCreateAccountDetail(payOrder.getAccountDetail(), payResponse.getBuyerLogonId(), payResponse.getBuyerUserId()));

        if (StringUtils.isNotBlank(payResponse.getReceiptAmount())) {
            payOrder.setReceiptAmount(new Money(payResponse.getReceiptAmount()));
        }

        payOrder.setFundBillList(JSON.toJSONString(payResponse.getFundBillList()));
        payOrder.setDiscountGoodsDetail(payResponse.getDiscountGoodsDetail());
        payOrder.setGmtPayment(payResponse.getGmtPayment());
        payOrder.setCheckDate(DateUtil.format(payResponse.getGmtPayment(), DateUtil.shortFormat));

        return payOrder;
    }

    /**
     * 条码支付是否返回正在处理中
     * @param payResponse
     * @return
     */
    protected boolean isPayProcessing(AlipayTradePayResponse payResponse) {
        return payResponse != null && StringUtils.equals(payResponse.getCode(), AlipayBizResultEnum.PROCESSING.getCode());
    }

    /**
     * 订单查询是否返回“支付成功”
     * @param queryResponse
     * @return
     */
    protected boolean isQuerySuccess(AlipayTradeQueryResponse queryResponse) {
        return queryResponse != null
               && StringUtils.equals(queryResponse.getCode(), AlipayBizResultEnum.SUCCESS.getCode())
               && (StringUtils.equals(queryResponse.getTradeStatus(), AlipayTradeStatusEnum.TRADE_SUCCESS.getCode())
                   || StringUtils.equals(queryResponse.getTradeStatus(), AlipayTradeStatusEnum.TRADE_FINISHED.getCode()) || StringUtils.equals(
                   queryResponse.getTradeStatus(), AlipayTradeStatusEnum.TRADE_CLOSED.getCode()));
    }

    /**
     * 撤销是否需要重试
     * @param cancelResponse
     * @return
     */
    protected boolean isNeedCancelRetry(AlipayTradeCancelResponse cancelResponse) {
        return cancelResponse != null && StringUtils.equals(cancelResponse.getRetryFlag(), ParamConstant.Y);
    }

    /**
     * 支付宝响应是否成功
     */
    protected boolean isResponseSuccess(AlipayResponse response) {
        return response != null && StringUtils.equals(response.getCode(), AlipayBizResultEnum.SUCCESS.getCode());
    }

    /**
     * 支付宝响应是否异常或发生系统错误
     */
    protected boolean isResponseError(AlipayResponse response) {
        return response == null || StringUtils.equals(response.getCode(), AlipayBizResultEnum.UNKNOW.getCode());
    }

}
