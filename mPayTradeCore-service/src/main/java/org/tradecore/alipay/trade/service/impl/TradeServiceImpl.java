/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.sql.SQLException;
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
import org.tradecore.alipay.trade.request.RefundOrderQueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.AcquirerService;
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
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
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

    /** 支付仓储服务 */
    @Resource
    private PayRepository             payRepository;

    /** 扫码支付仓储服务 */
    @Resource
    private PrecreateRepository       precreateRepository;

    /** 退款仓储服务 */
    @Resource
    private RefundRepository          refundRepository;

    /** 撤销仓储服务 */
    @Resource
    private CancelRepository          cancelRepository;

    /** 收单机构服务接口 */
    @Resource
    private AcquirerService           acquirerService;

    /** 构造方法，初始化支付宝服务类 */
    public TradeServiceImpl() {

        //工厂方法创建静态支付服务类
        alipayTradeService = AlipayClientFactory.getAlipayTradeServiceInstance();

        //工厂方法创建静态AlipayClient
        alipayClient = AlipayClientFactory.getAlipayClientInstance();
    }

    @Override
    @Transactional
    public AlipayTradePayResponse pay(PayRequest payRequest) throws Exception {

        LogUtil.info(logger, "收到条码支付请求参数,payRequest={0}", payRequest);

        //1.校验参数
        AssertUtil.assertNotNull(payRequest, "条码支付请求不能为空");
        payRequest.validate();

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(payRequest.getAcquirerId(), payRequest.getMerchantId()), "商户不存在或状态非法");

        //2.幂等判断
        BizAlipayPayOrder nativePayOrder = null;

        nativePayOrder = payRepository.selectPayOrderByTradeNo(payRequest.getAcquirerId(), payRequest.getMerchantId(), payRequest.getOutTradeNo());
        AssertUtil.assertNull(nativePayOrder, "条码支付订单已存在");

        //3.请求参数转换成支付宝支付请求参数
        AlipayTradePayRequest alipayRequest = convert2AlipayRequest(payRequest);

        //4.调用支付宝条码支付接口
        AlipayF2FPayResult alipayF2FPayResult = alipayTradeService.tradePay(alipayRequest);

        LogUtil.info(logger, "支付宝返回条码支付业务结果alipayF2FPayResult={0}", JSON.toJSONString(alipayF2FPayResult, SerializerFeature.UseSingleQuotes));

        AssertUtil.assertNotNull(alipayF2FPayResult, "支付宝返回条码支付结果为空");

        //5.根据支付宝返回结果保存本地数据
        payRepository.savePayOrder(payRequest, alipayF2FPayResult);

        return alipayF2FPayResult;
    }

    @Override
    @Transactional
    public AlipayF2FPrecreateResult precreate(PrecreateRequest precreateRequest) {

        LogUtil.info(logger, "收到扫码支付请求参数,precreateRequest={0}", precreateRequest);

        //1.参数校验
        AssertUtil.assertNotNull(precreateRequest, "扫码支付请求不能为空");
        AssertUtil.assertTrue(precreateRequest.validate(), "扫码支付请求参数不合法");

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(precreateRequest.getAcquirerId(), precreateRequest.getMerchantId()), "商户不存在或状态非法");

        //1.2幂等判断
        BizAlipayPayOrder nativePayOrder = null;
        try {
            nativePayOrder = payRepository.selectPayOrder(precreateRequest.getMerchantId(), precreateRequest.getOutTradeNo(), null);
        } catch (SQLException e) {
            LogUtil.error(e, logger, "查询数据异常");
            throw new RuntimeException("查询数据异常");
        }
        AssertUtil.assertNull(nativePayOrder, "扫码支付订单已存在");

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

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(queryRequest.getAcquirerId(), queryRequest.getMerchantId()), "商户不存在或状态非法");

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

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(refundRequest.getAcquirerId(), refundRequest.getMerchantId()), "商户不存在或状态非法");

        //2.加锁查询原始订单
        BizAlipayPayOrder oriOrder = null;
        try {
            oriOrder = payRepository.selectPayOrder(refundRequest.getMerchantId(), refundRequest.getOutTradeNo(), refundRequest.getAlipayTradeNo());
        } catch (SQLException e) {
            LogUtil.error(e, logger, "查询数据异常");
            throw new RuntimeException("查询数据异常");
        }

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空，退款失败");

        //  2.1原始订单和退款请求参数校验
        AssertUtil.assertTrue(checkFee(oriOrder, refundRequest), "退款金额不能大于订单总金额");

        //3.转换成支付宝退款请求参数
        AlipayTradeRefundRequestBuilder builder = convert2Builder(refundRequest);

        //4.调用支付宝接口
        AlipayF2FRefundResult alipayF2FRefundResult = alipayTradeService.tradeRefund(builder);

        LogUtil.info(logger, "支付宝返回退款业务结果alipayF2FRefundResult={0}", JSON.toJSONString(alipayF2FRefundResult, SerializerFeature.UseSingleQuotes));

        AssertUtil.assertNotNull(alipayF2FRefundResult, "支付宝返回订单退款结果为空");

        //5.幂等控制。如果查询到退款成功记录，则不修改本地退款订单；如果没查询到，则持久化退款记录
        RefundOrderQueryRequest queryRequest = buildRefundOrderQueryRequest(refundRequest);
        List<BizAlipayRefundOrder> refundOrders = refundRepository.selectRefundOrders(queryRequest);
        if (CollectionUtils.isEmpty(refundOrders)) {
            //5.1根据支付宝返回结果持久化退款订单，修改原订单状态
            refundRepository.saveRefundOrder(oriOrder, refundRequest, alipayF2FRefundResult);

            //5.2根据退款订单状态更新本地交易订单的退款状态数据
            payRepository.updateOrderRefundStatus(oriOrder);
        }

        return alipayF2FRefundResult;
    }

    @Override
    @Transactional
    public AlipayTradeCancelResponse cancel(CancelRequest cancelRequest) {

        LogUtil.info(logger, "收到订单撤销请求,cancelRequest={0}", cancelRequest);

        //1.参数校验
        AssertUtil.assertNotNull(cancelRequest, "撤销请求参数不能为空");
        AssertUtil.assertTrue(cancelRequest.validate(), "撤销请求参数不合法");

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(cancelRequest.getAcquirerId(), cancelRequest.getMerchantId()), "商户不存在或状态非法");

        //2.加锁查询原始订单
        BizAlipayPayOrder oriOrder = null;
        try {
            oriOrder = payRepository.selectPayOrder(cancelRequest.getMerchantId(), cancelRequest.getOutTradeNo(), cancelRequest.getAlipayTradeNo());
        } catch (SQLException e) {
            LogUtil.error(e, logger, "查询数据异常");
            throw new RuntimeException("查询数据异常");
        }

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

        //6.查询本地是否有撤销成功记录，如果本地为空，则持久化撤销订单，并修改原交易订单的撤销状态；如果本地不为空，则不修改本地订单数据，直接返回支付宝响应
        List<BizAlipayCancelOrder> cancelOrders = cancelRepository.selectCancelOrder(cancelRequest.getMerchantId(), cancelRequest.getOutTradeNo(),
            cancelRequest.getAlipayTradeNo(), AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());

        if (CollectionUtils.isEmpty(cancelOrders)) {
            //6.1 本地持久化撤销数据
            BizAlipayCancelOrder cancelOrder = cancelRepository.saveCancelOrder(oriOrder, cancelRequest, cancelResponse);
            //6.2 修改原始订单的撤销状态
            payRepository.updateOrderCancelStatus(oriOrder, cancelOrder);
        }

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
    private AlipayTradePayRequest convert2AlipayRequest(PayRequest payRequest) {
        /*return new AlipayTradePayRequestBuilder().setSubMerchantId(payRequest.getMerchantId()).setScene(payRequest.getScene())
            .setOutTradeNo(payRequest.getOutTradeNo()).setSellerId(payRequest.getSellerId()).setTotalAmount(payRequest.getTotalAmount())
            .setDiscountableAmount(payRequest.getDiscountableAmount()).setUndiscountableAmount(payRequest.getUndiscountableAmount())
            .setSubject(payRequest.getSubject()).setBody(payRequest.getBody()).setAppAuthToken(payRequest.getAppAuthToken())
            .setGoodsDetailList(payRequest.getGoodsDetailList()).setOperatorId(payRequest.getOperatorId()).setStoreId(payRequest.getStoreId())
            .setAlipayStoreId(payRequest.getAlipayStoreId()).setTerminalId(payRequest.getTerminalId()).setExtendParams(payRequest.getExtendParams())
            .setTimeoutExpress(payRequest.getTimeoutExpress()).setAuthCode(payRequest.getAuthCode());*/

        return null;
    }

    /**
     * 将扫码支付请求转换成支付宝请求
     * @param precreateRequest
     * @return
     */
    private AlipayTradePrecreateRequestBuilder convert2Builder(PrecreateRequest precreateRequest) {
        return new AlipayTradePrecreateRequestBuilder().setSubMerchantId(precreateRequest.getMerchantId()).setOutTradeNo(precreateRequest.getOutTradeNo())
            .setSellerId(precreateRequest.getSellerId()).setTotalAmount(precreateRequest.getTotalAmount())
            .setDiscountableAmount(precreateRequest.getDiscountableAmount()).setUndiscountableAmount(precreateRequest.getUndiscountableAmount())
            .setSubject(precreateRequest.getSubject()).setBody(precreateRequest.getBody()).setAppAuthToken(precreateRequest.getAppAuthToken())
            .setGoodsDetailList(precreateRequest.getGoodsDetailList()).setOperatorId(precreateRequest.getOperatorId())
            .setStoreId(precreateRequest.getStoreId()).setAlipayStoreId(precreateRequest.getAlipayStoreId()).setTerminalId(precreateRequest.getTerminalId())
            .setExtendParams(precreateRequest.getExtendParams()).setTimeoutExpress(precreateRequest.getTimeoutExpress())
            .setNotifyUrl(precreateRequest.getNotifyUrl());
    }

    /**
     * 构造退款订单查询请求
     * @param refundRequest
     * @return
     */
    private RefundOrderQueryRequest buildRefundOrderQueryRequest(RefundRequest refundRequest) {
        RefundOrderQueryRequest queryRequest = new RefundOrderQueryRequest();

        queryRequest.setAlipayTradeNo(refundRequest.getAlipayTradeNo());
        queryRequest.setMerchantId(refundRequest.getMerchantId());
        queryRequest.setOutRequestNo(refundRequest.getOutRequestNo());
        queryRequest.setOutTradeNo(refundRequest.getOutTradeNo());
        queryRequest.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

        return queryRequest;
    }

    /**
     * 原始订单与退款金额校验
     * @param oriOrder        原始订单
     * @param refundRequest   退款请求
     * @return
     */
    private boolean checkFee(BizAlipayPayOrder oriOrder, RefundRequest refundRequest) {

        //原订单总金额
        Money totalAmount = oriOrder.getTotalAmount();
        //本次退款金额
        Money refundAmount = new Money(refundRequest.getRefundAmount());

        return totalAmount.compareTo(refundAmount) >= 0;
    }

    /**
     * 订单总金额与所有退款总金额校验
     * @param outTradeNo
     * @param alipayTradeNo
     * @param totalAmount
     * @param refundAmount
     * @return
     */
    @Deprecated
    private boolean checkTotalRufundFee(String merchantId, String outTradeNo, String alipayTradeNo, Money totalAmount, Money refundAmount) {

        //根据商户订单号获取该订单下所有退款成功的金额
        Money refundedAmount = refundRepository.getRefundedMoney(merchantId, outTradeNo, alipayTradeNo);

        refundedAmount.addTo(refundAmount);

        return totalAmount.compareTo(refundedAmount) >= 0;

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
        String endDateStr = formatedStr + ParamConstant.TIME_SUFFIX;

        Date endDate = DateUtil.parseDateLongFormat(endDateStr);

        return endDate.after(new Date());
    }
}
