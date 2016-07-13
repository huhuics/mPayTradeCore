/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.factory.AlipayClientFactory;
import org.tradecore.alipay.trade.repository.CancelRepository;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.repository.PrecreateRepository;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;

/**
 * 支付宝交易服务类
 * @author HuHui
 * @version $Id: TradeServiceImpl.java, v 0.1 2016年7月8日 下午4:13:39 HuHui Exp $
 */
@Service
public class TradeServiceImpl implements TradeService {

    /** 日志 */
    private static final Logger       logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    /** 支付宝交易接口 */
    private static AlipayTradeService alipayTradeService;

    /** 公共请求方法类 */
    private static AlipayClient       alipayClient;

    /**
     * 支付仓储服务
     */
    @Resource
    private PayRepository             payRepository;

    /**
     * 扫码支付仓储服务
     */
    @Resource
    private PrecreateRepository       precreateRepository;

    /**
     * 退款仓储服务
     */
    @Resource
    private RefundRepository          refundRepository;

    /**
     * 撤销仓储服务
     */
    @Resource
    private CancelRepository          cancelRepository;

    /**
     * 构造函数，初始化支付宝服务类
     */
    public TradeServiceImpl() {

        //工厂方法创建静态支付服务类
        alipayTradeService = AlipayClientFactory.getAlipayTradeServiceInstance();

        //实例化AlipayClient
        alipayClient = AlipayClientFactory.getAlipayClientInstance();
    }

    @Override
    @Transactional
    public AlipayF2FPayResult pay(PayRequest payRequest) {

        LogUtil.info(logger, "收到条码支付请求参数,payRequest={0}", payRequest);

        //1.校验参数
        AssertUtil.assertNotNull(payRequest, "条码支付请求不能为空");
        AssertUtil.assertTrue(payRequest.validate(), "支付支付请求参数不合法");

        //2.本地持久化
        BizAlipayPayOrder bizAlipayPayOrder = payRepository.savePayOrder(payRequest);

        //3.请求参数转换成支付宝支付请求参数
        AlipayTradePayRequestBuilder builder = convert2Builder(payRequest);

        //4.调用支付宝条码支付接口
        AlipayF2FPayResult alipayF2FPayResult = alipayTradeService.tradePay(builder);

        LogUtil.info(logger, "支付宝返回条码支付业务结果alipayF2FPayResult={0}", JSON.toJSONString(alipayF2FPayResult, SerializerFeature.UseSingleQuotes));

        AssertUtil.assertNotNull(alipayF2FPayResult, "支付宝返回条码支付结果为空");

        //5.根据支付宝返回结果更新本地数据
        payRepository.updatePayOrder(bizAlipayPayOrder, alipayF2FPayResult);

        return alipayF2FPayResult;
    }

    @Override
    @Transactional
    public AlipayF2FPrecreateResult precreate(PrecreateRequest precreateRequest) {

        LogUtil.info(logger, "收到扫码支付请求参数,precreateRequest={0}", precreateRequest);

        //1.参数校验
        AssertUtil.assertNotNull(precreateRequest, "扫码支付请求不能为空");
        AssertUtil.assertTrue(precreateRequest.validate(), "扫码支付请求参数不合法");

        //2.请求参数转换成支付宝支付请求参数
        AlipayTradePrecreateRequestBuilder builder = convert2Builder(precreateRequest);

        //3.调用支付宝扫码支付接口
        AlipayF2FPrecreateResult alipayF2FPrecreateResult = alipayTradeService.tradePrecreate(builder);

        LogUtil.info(logger, "支付宝返回扫码支付业务结果alipayF2FPrecreateResult={0}", JSON.toJSONString(alipayF2FPrecreateResult, SerializerFeature.UseSingleQuotes));

        AssertUtil.assertNotNull(alipayF2FPrecreateResult, "支付宝返回扫码业务结果为空");

        //4.根据支付宝返回结果持久化本地订单数据
        precreateRepository.savePrecreateOrder(precreateRequest, alipayF2FPrecreateResult);

        return alipayF2FPrecreateResult;
    }

    @Override
    @Transactional
    public AlipayF2FQueryResult query(QueryRequest queryRequest) {

        LogUtil.info(logger, "收到订单查询请求,queryRequest={0}", queryRequest);

        //1.校验参数
        AssertUtil.assertNotNull(queryRequest, "查询请求不能为空");
        AssertUtil.assertTrue(queryRequest.validate(), "查询请求参数不合法");

        //2.转换成支付宝查询请求参数
        AlipayTradeQueryRequestBuilder builder = convert2Builder(queryRequest);

        //3.调用支付宝接口
        AlipayF2FQueryResult alipayF2FQueryResult = alipayTradeService.queryTradeResult(builder);

        LogUtil.info(logger, "支付宝返回查询业务结果alipayF2FQueryResult={0}", JSON.toJSONString(alipayF2FQueryResult, SerializerFeature.UseSingleQuotes));

        //4.如果业务成功，则修改本地订单状态
        payRepository.updateOrderStatus(queryRequest, alipayF2FQueryResult);

        return alipayF2FQueryResult;
    }

    @Override
    @Transactional
    public AlipayF2FRefundResult refund(RefundRequest refundRequest) {

        LogUtil.info(logger, "收到订单退款请求,refundRequest={0}", refundRequest);

        //1.校验参数
        AssertUtil.assertNotNull(refundRequest, "退款请求参数不能为空");
        AssertUtil.assertTrue(refundRequest.validate(), "退款请求参数不合法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrderForUpdate(refundRequest.getMerchantId(), refundRequest.getOutTradeNo());

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //  2.1原始订单和退款请求参数校验
        AssertUtil.assertTrue(checkFee(oriOrder, refundRequest.getRefundAmount()), "退款金额校验错误");

        //TODO:退款订单幂等控制

        //3.本地持久化退款信息
        BizAlipayRefundOrder refundOrder = refundRepository.saveRefundOrder(oriOrder, refundRequest);

        //4.转换成支付宝退款请求参数
        AlipayTradeRefundRequestBuilder builder = convert2Builder(refundRequest);

        //5.调用支付宝接口
        AlipayF2FRefundResult alipayF2FRefundResult = alipayTradeService.tradeRefund(builder);

        LogUtil.info(logger, "支付宝返回退款业务结果alipayF2FRefundResult={0}", JSON.toJSONString(alipayF2FRefundResult, SerializerFeature.UseSingleQuotes));

        //6.根据支付宝返回结果更新本地数据
        refundRepository.updateRefundOrder(refundOrder, alipayF2FRefundResult);

        //7.根据退款订单状态更新本地交易订单的退款状态数据
        payRepository.updateOrderRefundStatus(oriOrder, refundOrder);

        return alipayF2FRefundResult;
    }

    @Override
    @Transactional
    public AlipayTradeCancelResponse cancel(CancelRequest cancelRequest) {

        LogUtil.info(logger, "收到订单撤销请求,cancelRequest={0}", cancelRequest);

        //1.参数校验
        AssertUtil.assertNotNull(cancelRequest, "撤销请求参数不能为空");
        AssertUtil.assertTrue(cancelRequest.validate(), "撤销请求参数不合法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrderForUpdate(cancelRequest.getMerchantId(), cancelRequest.getOutTradeNo());

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //3.判断是否在撤销时效内
        AssertUtil.assertTrue(checkCancelTime(oriOrder.getGmtCreate()), "已超过可撤销时间，订单无法进行撤销");

        //4.转换成支付宝撤销请求
        AlipayTradeCancelRequest alipayTradeCancelRequest = convert2Request(cancelRequest);

        //5.调用支付宝撤销接口
        AlipayTradeCancelResponse cancelResponse = null;
        try {
            cancelResponse = alipayClient.execute(alipayTradeCancelRequest);
        } catch (AlipayApiException e) {
            LogUtil
                .error(e, logger, "调用支付宝撤销接口异常,alipayTradeCancelRequest={0}", JSON.toJSONString(alipayTradeCancelRequest, SerializerFeature.UseSingleQuotes));
            throw new RuntimeException("调用支付宝撤销接口异常", e);
        }

        LogUtil.info(logger, "支付宝返回撤销业务结果cancelResponse={0}", JSON.toJSONString(cancelResponse, SerializerFeature.UseSingleQuotes));

        //6.本地持久化撤销数据
        BizAlipayCancelOrder cancelOrder = cancelRepository.saveCancelOrder(oriOrder, cancelRequest, cancelResponse);

        //7.修改原始订单的撤销状态
        payRepository.updateOrderCancelStatus(oriOrder, cancelOrder);

        return cancelResponse;
    }

    /**
     * 将撤销请求转换成支付宝请求
     * @param cancelRequest
     * @return
     */
    private AlipayTradeCancelRequest convert2Request(CancelRequest cancelRequest) {

        AlipayTradeCancelRequest alipayCancelRequest = new AlipayTradeCancelRequest();
        alipayCancelRequest.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, cancelRequest.getAppAuthToken());

        //封装查询参数并序列化
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(JSONFieldConstant.OUT_TRADE_NO, cancelRequest.getOutTradeNo());

        alipayCancelRequest.setBizContent(JSON.toJSONString(paraMap));

        return alipayCancelRequest;
    }

    /**
     * 将订单退款请求转换成支付宝请求
     * @param refundRequest
     * @return
     */
    private AlipayTradeRefundRequestBuilder convert2Builder(RefundRequest refundRequest) {
        return new AlipayTradeRefundRequestBuilder().setOutTradeNo(refundRequest.getOutTradeNo()).setTradeNo(refundRequest.getAlipayTradeNo())
            .setRefundAmount(refundRequest.getRefundAmount()).setRefundReason(refundRequest.getRefundReason()).setOutRequestNo(refundRequest.getOutRequestNo())
            .setStoreId(refundRequest.getStoreId()).setAppAuthToken(refundRequest.getAppAuthToken()).setAlipayStoreId(refundRequest.getAlipayStoreId())
            .setTerminalId(refundRequest.getTerminalId());
    }

    /**
     * 将订单查询请求转换成支付宝请求
     * @param queryRequest
     * @return
     */
    private AlipayTradeQueryRequestBuilder convert2Builder(QueryRequest queryRequest) {
        return new AlipayTradeQueryRequestBuilder().setOutTradeNo(queryRequest.getOutTradeNo()).setTradeNo(queryRequest.getAlipayTradeNo())
            .setAppAuthToken(queryRequest.getAppAuthToken());
    }

    /**
     * 将条码支付请求参数转换成支付宝请求
     * @param payRequest 
     * @return
     */
    private AlipayTradePayRequestBuilder convert2Builder(PayRequest payRequest) {
        return new AlipayTradePayRequestBuilder().setOutTradeNo(payRequest.getOutTradeNo()).setSubject(payRequest.getSubject())
            .setAuthCode(payRequest.getAuthCode()).setTotalAmount(payRequest.getTotalAmount()).setStoreId(payRequest.getStoreId())
            .setUndiscountableAmount(payRequest.getUndiscountableAmount()).setBody(payRequest.getBody()).setOperatorId(payRequest.getOperatorId())
            .setExtendParams(payRequest.getExtendParams()).setSellerId(payRequest.getSellerId()).setGoodsDetailList(payRequest.getGoodsDetailList())
            .setTimeoutExpress(payRequest.getTimeoutExpress()).setAppAuthToken(payRequest.getAppAuthToken());
    }

    /**
     * 将扫码支付请求转换成支付宝请求
     * @param precreateRequest
     * @return
     */
    private AlipayTradePrecreateRequestBuilder convert2Builder(PrecreateRequest precreateRequest) {
        return new AlipayTradePrecreateRequestBuilder().setSubject(precreateRequest.getSubject()).setTotalAmount(precreateRequest.getTotalAmount())
            .setOutTradeNo(precreateRequest.getOutTradeNo()).setUndiscountableAmount(precreateRequest.getUndiscountableAmount())
            .setSellerId(precreateRequest.getSellerId()).setBody(precreateRequest.getBody()).setOperatorId(precreateRequest.getOperatorId())
            .setStoreId(precreateRequest.getStoreId()).setExtendParams(precreateRequest.getExtendParams())
            .setTimeoutExpress(precreateRequest.getTimeoutExpress()).setGoodsDetailList(precreateRequest.getGoodsDetailList())
            .setNotifyUrl(precreateRequest.getNotifyUrl());
    }

    /**
     * 原始订单与退款金额校验
     * @param oriOrder      原始订单
     * @param refundAmount  本次退款金额
     * @return
     */
    private boolean checkFee(BizAlipayPayOrder oriOrder, String refundAmountStr) {
        //原订单总金额
        Money totalAmount = oriOrder.getTotalAmount();
        //本次退款金额
        Money refundAmount = new Money(refundAmountStr);

        AssertUtil.assertTrue(checkTotalFee(totalAmount, refundAmount), "退款金额不能大于订单总金额");

        AssertUtil.assertTrue(checkTotalRufundFee(oriOrder.getOutTradeNo(), totalAmount, refundAmount), "多次退款,退款总金额不能大于订单总金额");

        return true;
    }

    /**
     * 订单总金额与本次退款金额校验
     * @param totalAmount
     * @param refundAmount
     * @return
     */
    private boolean checkTotalFee(Money totalAmount, Money refundAmount) {
        return totalAmount.compareTo(refundAmount) >= 0;
    }

    /**
     * 订单总金额与所有退款总金额校验
     * @param outTradeNo
     * @param totalAmount
     * @param refundAmount
     * @return
     */
    private boolean checkTotalRufundFee(String outTradeNo, Money totalAmount, Money refundAmount) {

        //根据商户订单号获取该订单下所有退款成功的退款订单
        List<BizAlipayRefundOrder> refundOrders = refundRepository.selectRefundOrdersByOutTradeNo(outTradeNo, AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

        if (CollectionUtils.isNotEmpty(refundOrders)) {
            Money totalRefundAmount = new Money(refundAmount.getAmount());
            for (BizAlipayRefundOrder order : refundOrders) {
                totalRefundAmount.addTo(order.getRefundAmount());
            }

            return totalAmount.compareTo(totalRefundAmount) >= 0;
        }

        //如果refundOrders为空，则退回到checkTotalFee方法，因此直接返回true
        return true;
    }

    /**
     * 判断是否在撤销时效内<br>
     * 撤销发起时间必须在订单创建的自然日内，否则超过0点将不能撤销<br>
     * @param gmtCreate
     * @return
     */
    private boolean checkCancelTime(Date gmtCreate) {

        //获取订单创建时间的yyyyMMdd格式
        String formatedStr = DateUtil.format(gmtCreate, DateUtil.shortFormat);

        //订单撤销截止时间
        String endDateStr = formatedStr + "235959";

        Date endDate = DateUtil.parseDateLongFormat(endDateStr);

        return endDate.after(new Date());
    }
}
