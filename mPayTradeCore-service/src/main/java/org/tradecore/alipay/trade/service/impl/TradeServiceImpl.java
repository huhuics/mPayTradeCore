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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.trade.constants.JSONFieldConstant;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.convertor.Convertor;
import org.tradecore.alipay.trade.repository.CancelRepository;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.CreateRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundQueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.AcquirerService;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.common.util.TradeNoFormater;
import org.tradecore.dao.domain.BizAlipayCancelOrder;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradeFastpayRefundQueryRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradeFastpayRefundQueryResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 支付宝交易服务类
 * @author HuHui
 * @version $Id: TradeServiceImpl.java, v 0.1 2016年7月8日 下午4:13:39 HuHui Exp $
 */
@Service
public class TradeServiceImpl extends AbstractAlipayTradeService implements TradeService {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceImpl.class);

    /** 支付仓储服务 */
    @Resource
    private PayRepository       payRepository;

    /** 退款仓储服务 */
    @Resource
    private RefundRepository    refundRepository;

    /** 撤销仓储服务 */
    @Resource
    private CancelRepository    cancelRepository;

    /** 收单机构服务接口 */
    @Resource
    private AcquirerService     acquirerService;

    @Override
    @Transactional
    public AlipayTradePayResponse pay(PayRequest payRequest) throws Exception {

        LogUtil.info(logger, "收到条码支付请求参数,payRequest={0}", payRequest);

        //1.校验参数
        validateRequest(payRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(payRequest.getAcquirerId(), payRequest.getMerchantId()), "商户不存在或状态非法");

        //2.幂等判断
        //组装结算中心订单号
        BizAlipayPayOrder nativePayOrder = payRepository.selectPayOrderByTradeNo(TradeNoFormater.format(payRequest.getAcquirerId(), payRequest.getMerchantId(),
            payRequest.getOutTradeNo()));
        AssertUtil.assertNull(nativePayOrder, "条码支付订单已存在");

        //3.请求参数转换成支付宝支付请求参数
        AlipayTradePayRequest alipayRequest = createAlipayRequest(payRequest);

        //4.调用支付宝条码支付接口
        AlipayTradePayResponse payResponse = (AlipayTradePayResponse) getResponse(alipayRequest);

        LogUtil.info(logger, "支付宝返回条码支付响应,payResponse={0}", JSON.toJSONString(payResponse, SerializerFeature.UseSingleQuotes));

        //5.创建条码交易订单对象
        BizAlipayPayOrder payOrder = Convertor.convert2PayOrder(payRequest);

        //6.根据调用结果分别处理
        if (isResponseSuccess(payResponse)) {
            //6.1 支付明确成功
            LogUtil.info(logger, "条码支付返回成功");
            setPayOrderSuccess(payOrder, payResponse);
        } else if (isPayProcessing(payResponse)) {
            //6.2 返回处理中，则轮询查询交易是否成功，如果超时，则调用撤销
            LogUtil.info(logger, "条码支付返回业务处理中");
            AlipayTradeQueryRequest alipayQueryRequest = createAlipayQueryRequest(payRequest.getAppAuthToken(), payRequest.getOutTradeNo());

            AlipayTradeQueryResponse loopQueryResponse = loopQuery(alipayQueryRequest);

            LogUtil.info(logger, "轮询订单结果,loopQueryResponse={0}", JSON.toJSONString(loopQueryResponse, SerializerFeature.UseSingleQuotes));

            checkQueryAndCancel(payOrder, payRequest, payResponse, loopQueryResponse);

        } else if (isResponseError(payResponse)) {
            //6.3 系统错误，则查询一次交易，如果交易没有支付成功，则调用撤销
            LogUtil.warn(logger, "条码支付返回系统错误,outTradeNo={0}", payRequest.getOutTradeNo());
            AlipayTradeQueryRequest alipayQueryRequest = createAlipayQueryRequest(payRequest.getAppAuthToken(), payRequest.getOutTradeNo());

            AlipayTradeQueryResponse queryResponse = (AlipayTradeQueryResponse) getResponse(alipayQueryRequest);

            checkQueryAndCancel(payOrder, payRequest, payResponse, queryResponse);
        } else {
            //6.4 其它情况可以明确支付失败
            LogUtil.warn(logger, "条码支付失败,outTradeNo={0}", payRequest.getOutTradeNo());
            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_FAILED.getCode());
            payOrder.setReturnDetail(JSON.toJSONString(payResponse.getBody(), SerializerFeature.UseSingleQuotes));
        }

        //7.保存订单数据
        payRepository.savePayOrder(payOrder);

        return payResponse;
    }

    @Override
    @Transactional
    public AlipayTradeCreateResponse create(CreateRequest createRequest) throws Exception {

        LogUtil.info(logger, "收到订单创建请求,createRequest={0}", createRequest);

        //1.参数校验
        validateRequest(createRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(createRequest.getAcquirerId(), createRequest.getMerchantId()), "商户不存在或状态非法");

        //  1.2幂等判断
        BizAlipayPayOrder nativePayOrder = payRepository.selectPayOrderByTradeNo(TradeNoFormater.format(createRequest.getAcquirerId(),
            createRequest.getMerchantId(), createRequest.getOutTradeNo()));
        AssertUtil.assertNull(nativePayOrder, "支付订单已存在");

        //2.请求参数转换为支付宝请求参数
        AlipayTradeCreateRequest alipayCreateRequest = createAlipayCreateRequest(createRequest);

        //3.调用支付宝订单创建接口
        AlipayTradeCreateResponse createResponse = (AlipayTradeCreateResponse) getResponse(alipayCreateRequest);

        LogUtil.info(logger, "支付宝返回订单创建业务结果,createResponse:{0}", JSON.toJSONString(createResponse, SerializerFeature.UseSingleQuotes));

        //4.创建支付订单
        BizAlipayPayOrder payOrder = Convertor.convert2PayOrder(createRequest);

        //5.根据调用结果分别处理
        if (isResponseSuccess(createResponse)) {
            LogUtil.info(logger, "订单创建返回成功");
            payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());
            payOrder.setAlipayTradeNo(createResponse.getTradeNo());
        } else if (isResponseError(createResponse)) {
            LogUtil.warn(logger, "订单创建返回系统错误,outTradeNo={0}", createRequest.getOutTradeNo());
            payOrder.setOrderStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
        } else {
            LogUtil.warn(logger, "订单创建返回失败,outTradeNo={0}", createRequest.getOutTradeNo());
            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_FAILED.getCode());
        }

        //6.保存支付宝返回信息
        if (createResponse != null) {
            payOrder.setReturnDetail(JSON.toJSONString(createResponse.getBody(), SerializerFeature.UseSingleQuotes));
        }

        //7.保存订单数据
        payRepository.savePayOrder(payOrder);

        return createResponse;
    }

    @Override
    @Transactional
    public AlipayTradePrecreateResponse precreate(PrecreateRequest precreateRequest) throws Exception {

        LogUtil.info(logger, "收到扫码支付请求参数,precreateRequest={0}", precreateRequest);

        //1.参数校验
        validateRequest(precreateRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(precreateRequest.getAcquirerId(), precreateRequest.getMerchantId()), "商户不存在或状态非法");

        //  1.2幂等判断
        BizAlipayPayOrder nativePayOrder = payRepository.selectPayOrderByTradeNo(TradeNoFormater.format(precreateRequest.getAcquirerId(),
            precreateRequest.getMerchantId(), precreateRequest.getOutTradeNo()));
        AssertUtil.assertNull(nativePayOrder, "扫码支付订单已存在");

        //2.请求参数转换成支付宝支付请求参数
        AlipayTradePrecreateRequest alipayPrecreateRequest = createAlipayPrecreateRequest(precreateRequest);

        //3.调用支付宝扫码支付接口
        AlipayTradePrecreateResponse precreateResponse = (AlipayTradePrecreateResponse) getResponse(alipayPrecreateRequest);

        LogUtil.info(logger, "支付宝返回扫码支付业务结果,precreateResponse={0}", JSON.toJSONString(precreateResponse, SerializerFeature.UseSingleQuotes));

        //4.创建扫码支付订单
        BizAlipayPayOrder payOrder = Convertor.convert2PayOrder(precreateRequest);

        //5.根据调用结果分别处理
        if (isResponseSuccess(precreateResponse)) {
            LogUtil.info(logger, "扫码支付返回成功");
            payOrder.setOrderStatus(AlipayTradeStatusEnum.WAIT_BUYER_PAY.getCode());
            payOrder.setQrCode(precreateResponse.getQrCode());
        } else if (isResponseError(precreateResponse)) {
            LogUtil.warn(logger, "扫码支付返回系统错误,outTradeNo={0}", precreateRequest.getOutTradeNo());
            payOrder.setOrderStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
        } else {
            LogUtil.warn(logger, "扫码支付返回失败,outTradeNo={0}", precreateRequest.getOutTradeNo());
            payOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_FAILED.getCode());
        }

        //6.保存支付宝返回信息
        if (precreateResponse != null) {
            payOrder.setReturnDetail(JSON.toJSONString(precreateResponse.getBody(), SerializerFeature.UseSingleQuotes));
        }

        //7.保存订单数据
        payRepository.savePayOrder(payOrder);

        return precreateResponse;
    }

    @Override
    @Transactional
    public AlipayTradeQueryResponse query(QueryRequest queryRequest) throws Exception {

        LogUtil.info(logger, "收到订单查询请求,queryRequest={0}", queryRequest);

        //1.校验参数
        validateRequest(queryRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(queryRequest.getAcquirerId(), queryRequest.getMerchantId()), "商户不存在或状态非法");

        //2.查询本地订单
        BizAlipayPayOrder nativePayOrder = payRepository.selectPayOrder(queryRequest.getMerchantId(), queryRequest.getOutTradeNo(),
            queryRequest.getAlipayTradeNo());
        AssertUtil.assertNotNull(nativePayOrder, "原订单查询为空");

        //3.转换成支付宝查询请求参数
        AlipayTradeQueryRequest alipayQueryRequest = createAlipayQueryRequest(queryRequest);

        //4.调用支付宝接口
        AlipayTradeQueryResponse queryResponse = (AlipayTradeQueryResponse) getResponse(alipayQueryRequest);

        LogUtil.info(logger, "支付宝返回订单查询结果,queryResponse={0}", JSON.toJSONString(queryResponse, SerializerFeature.UseSingleQuotes));

        //5.根据调用结果分别处理
        if (isQuerySuccess(queryResponse)) {
            LogUtil.info(logger, "订单查询返回成功");
            //如果支付宝返回订单状态与本地订单不一致，则修改本地
            if (!StringUtils.equals(nativePayOrder.getOrderStatus(), queryResponse.getTradeStatus())) {
                nativePayOrder.setOrderStatus(queryResponse.getTradeStatus());
                nativePayOrder.setAlipayTradeNo(queryResponse.getTradeNo());
                //修改本地订单
                payRepository.updatePayOrder(nativePayOrder);
            }
        } else if (isResponseError(queryResponse)) {
            LogUtil.warn(logger, "订单查询返回系统错误,outTradeNo={0},alipayTradeNo={0}", queryRequest.getOutTradeNo(), queryRequest.getAlipayTradeNo());
        } else {
            LogUtil.warn(logger, "订单查询返回失败,outTradeNo={0},alipayTradeNo={0}", queryRequest.getOutTradeNo(), queryRequest.getAlipayTradeNo());
        }

        return queryResponse;
    }

    @Override
    @Transactional
    public AlipayTradeFastpayRefundQueryResponse refundQuery(RefundQueryRequest refundQueryRequest) throws Exception {

        LogUtil.info(logger, "收到退款订单查询请求,refundQueryRequest={0}", refundQueryRequest);

        //1.校验参数
        validateRequest(refundQueryRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(refundQueryRequest.getAcquirerId(), refundQueryRequest.getMerchantId()), "商户不存在或状态非法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrder(refundQueryRequest.getMerchantId(), refundQueryRequest.getOutTradeNo(),
            refundQueryRequest.getAlipayTradeNo());

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //3.转换成支付宝退款查询请求参数
        AlipayTradeFastpayRefundQueryRequest alipayRefundQueryRequest = createAlipayRefundQueryRequest(refundQueryRequest);

        //4.调用支付宝接口
        AlipayTradeFastpayRefundQueryResponse refundQueryResponse = (AlipayTradeFastpayRefundQueryResponse) getResponse(alipayRefundQueryRequest);

        LogUtil.info(logger, "支付宝返回退款查询业务结果,refundQueryResponse={0}", JSON.toJSONString(refundQueryResponse, SerializerFeature.UseSingleQuotes));

        //5.查询本地是否有该退款请求号对应的退款订单(如果有，只能有一条记录)
        List<BizAlipayRefundOrder> nativeRefundOrders = refundRepository.selectRefundOrders(refundQueryRequest);

        //6.处理不同响应
        if (isResponseSuccess(refundQueryResponse)) {

            LogUtil.info(logger, "支付宝返回退款查询成功");

            refundRepository.updateRefundStatus(oriOrder, nativeRefundOrders, payRepository, refundQueryResponse);

        } else if (isResponseError(refundQueryResponse)) {
            LogUtil.warn(logger, "退款订单查询返回系统错误,outRequestNo={0}", refundQueryRequest.getOutRequestNo());
        } else {
            LogUtil.warn(logger, "退款订单查询返回失败,outRequestNo={0}", refundQueryRequest.getOutRequestNo());
        }

        return refundQueryResponse;
    }

    @Override
    @Transactional
    public AlipayTradeRefundResponse refund(RefundRequest refundRequest) throws Exception {

        LogUtil.info(logger, "收到订单退款请求,refundRequest={0}", refundRequest);

        //1.校验参数
        validateRequest(refundRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(refundRequest.getAcquirerId(), refundRequest.getMerchantId()), "商户不存在或状态非法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrder(refundRequest.getMerchantId(), refundRequest.getOutTradeNo(),
            refundRequest.getAlipayTradeNo());

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空，退款失败");

        //  2.1原始订单和退款请求参数校验
        AssertUtil.assertTrue(checkFee(oriOrder, refundRequest), "退款金额不能大于订单总金额");

        //3.转换成支付宝退款请求参数
        AlipayTradeRefundRequest alipayRefundRequest = createAlipayRefundRequest(refundRequest);

        //4.调用支付宝接口
        AlipayTradeRefundResponse refundResponse = (AlipayTradeRefundResponse) getResponse(alipayRefundRequest);

        LogUtil.info(logger, "支付宝返回退款业务结果,refundResponse={0}", JSON.toJSONString(refundResponse, SerializerFeature.UseSingleQuotes));

        //5.幂等控制。如果查询到退款成功记录，则不修改本地退款订单；如果没查询到，则持久化退款记录
        RefundQueryRequest queryRequest = buildRefundOrderQueryRequest(refundRequest);
        List<BizAlipayRefundOrder> refundOrders = refundRepository.selectRefundOrders(queryRequest);
        if (CollectionUtils.isEmpty(refundOrders)) {
            //5.1根据支付宝返回结果持久化退款订单，修改原订单状态
            refundRepository.saveRefundOrder(oriOrder, refundRequest, refundResponse);

            //5.2根据退款订单状态更新本地交易订单的退款状态数据
            payRepository.updateOrderRefundStatus(oriOrder);
        }

        return refundResponse;
    }

    @Override
    @Transactional
    public AlipayTradeCancelResponse cancel(CancelRequest cancelRequest) throws Exception {

        LogUtil.info(logger, "收到订单撤销请求,cancelRequest={0}", cancelRequest);

        //1.参数校验
        validateRequest(cancelRequest);

        //  1.1判断商户是否可用
        AssertUtil.assertTrue(acquirerService.isMerchantNormal(cancelRequest.getAcquirerId(), cancelRequest.getMerchantId()), "商户不存在或状态非法");

        //2.查询原始订单
        BizAlipayPayOrder oriOrder = payRepository.selectPayOrder(cancelRequest.getMerchantId(), cancelRequest.getOutTradeNo(),
            cancelRequest.getAlipayTradeNo());

        AssertUtil.assertNotNull(oriOrder, "原始订单查询为空");

        //3.判断是否在撤销时效内
        AssertUtil.assertTrue(checkCancelTime(oriOrder.getGmtCreate()), "已超过可撤销时间，订单无法进行撤销");

        //4.转换成支付宝撤销请求
        AlipayTradeCancelRequest alipayCancelRequest = createAlipayCancelRequest(cancelRequest);

        //5.调用支付宝撤销接口
        AlipayTradeCancelResponse cancelResponse = (AlipayTradeCancelResponse) getResponse(alipayCancelRequest);

        LogUtil.info(logger, "支付宝返回撤销业务结果,cancelResponse={0}", JSON.toJSONString(cancelResponse, SerializerFeature.UseSingleQuotes));

        //6.构造撤销订单
        BizAlipayCancelOrder cancelOrder = Convertor.convert2CancelOrder(oriOrder, cancelRequest);

        //7.根据响应分别处理
        if (isResponseSuccess(cancelResponse)) {
            LogUtil.info(logger, "订单撤销返回成功");
            cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());
            //撤销完成，交易状态改为TRADE_CLOSED
            oriOrder.setOrderStatus(AlipayTradeStatusEnum.TRADE_CLOSED.getCode());
            //设置其它参数
            cancelOrder.setRetryFlag(cancelResponse.getRetryFlag());
            cancelOrder.setAction(cancelResponse.getAction());
            cancelOrder.setReturnDetail(JSON.toJSONString(cancelResponse.getBody(), SerializerFeature.UseSingleQuotes));

        } else if (isResponseError(cancelResponse)) {
            LogUtil.warn(logger, "订单撤销返回系统错误");
            cancelOrder.setCancelStatus(AlipayTradeStatusEnum.UNKNOWN.getCode());
        } else {
            LogUtil.warn(logger, "订单撤销返回失败");
            cancelOrder.setCancelStatus(AlipayTradeStatusEnum.CANCEL_FAILED.getCode());
        }

        if (cancelResponse != null) {
            cancelOrder.setReturnDetail(JSON.toJSONString(cancelResponse.getBody(), SerializerFeature.UseSingleQuotes));
        }

        //8.幂等.查询本地是否有撤销成功记录，如果本地为空，则持久化撤销订单，并修改原交易订单的撤销状态；如果本地不为空，则不修改本地订单数据，直接返回支付宝响应
        List<BizAlipayCancelOrder> cancelOrders = cancelRepository.selectCancelOrder(cancelRequest.getMerchantId(), cancelRequest.getOutTradeNo(),
            cancelRequest.getAlipayTradeNo(), AlipayTradeStatusEnum.CANCEL_SUCCESS.getCode());

        if (CollectionUtils.isEmpty(cancelOrders)) {
            //8.1 本地持久化撤销数据
            cancelRepository.saveCancelOrder(cancelOrder);
            //8.2 修改原始订单的撤销状态
            payRepository.updateOrderCancelStatus(oriOrder, cancelOrder);
        }

        return cancelResponse;
    }

    /**
     * 将撤销请求转换成支付宝请求
     * @param cancelRequest
     * @return
     */
    private AlipayTradeCancelRequest createAlipayCancelRequest(CancelRequest cancelRequest) {

        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, cancelRequest.getAppAuthToken());

        request.setBizContent(JSON.toJSONString(cancelRequest));

        LogUtil.info(logger, "cancel.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 将退款订单查询请求转换成支付宝请求
     */
    private AlipayTradeFastpayRefundQueryRequest createAlipayRefundQueryRequest(RefundQueryRequest refundQueryRequest) {

        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, refundQueryRequest.getAppAuthToken());
        request.setBizContent(JSON.toJSONString(refundQueryRequest));

        LogUtil.info(logger, "refund query.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 将订单退款请求转换成支付宝请求
     * @param refundRequest
     * @return
     */
    private AlipayTradeRefundRequest createAlipayRefundRequest(RefundRequest refundRequest) {

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, refundRequest.getAppAuthToken());
        request.setBizContent(JSON.toJSONString(refundRequest));

        LogUtil.info(logger, "refund.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 将订单查询请求转换成支付宝请求
     * @param queryRequest
     * @return
     */
    private AlipayTradeQueryRequest createAlipayQueryRequest(QueryRequest queryRequest) {

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, queryRequest.getAppAuthToken());
        request.setBizContent(JSON.toJSONString(queryRequest));

        LogUtil.info(logger, "query.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 创建支付宝查询请求
     * @param appAuthToken
     * @param outTradeNo
     * @return
     */
    private AlipayTradeQueryRequest createAlipayQueryRequest(String appAuthToken, String outTradeNo) {

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, appAuthToken);

        //封装查询参数并序列化
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put(JSONFieldConstant.OUT_TRADE_NO, outTradeNo);

        request.setBizContent(JSON.toJSONString(paraMap));

        LogUtil.info(logger, "query.bizContent:{0}", request.getBizContent());

        return request;

    }

    /**
     * 创建支付宝请求
     * @param payRequest 
     * @return
     */
    private AlipayTradePayRequest createAlipayRequest(PayRequest payRequest) {

        AlipayTradePayRequest request = new AlipayTradePayRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, payRequest.getAppAuthToken());

        request.setBizContent(JSON.toJSONString(payRequest));

        LogUtil.info(logger, "pay.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 创建支付宝请求
     */
    private AlipayTradeCreateRequest createAlipayCreateRequest(CreateRequest createRequest) {

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, createRequest.getAppAuthToken());
        request.setNotifyUrl(createRequest.getNotifyUrl());

        request.setBizContent(JSON.toJSONString(createRequest));

        LogUtil.info(logger, "create.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 将扫码支付请求转换成支付宝请求
     * @param precreateRequest
     * @return
     */
    private AlipayTradePrecreateRequest createAlipayPrecreateRequest(PrecreateRequest precreateRequest) {

        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        request.putOtherTextParam(ParamConstant.APP_AUTH_TOKEN, precreateRequest.getAppAuthToken());
        request.setNotifyUrl(precreateRequest.getNotifyUrl());

        request.setBizContent(JSON.toJSONString(precreateRequest));

        LogUtil.info(logger, "precreate.bizContent:{0}", request.getBizContent());

        return request;
    }

    /**
     * 构造退款订单查询请求
     * @param refundRequest
     * @return
     */
    private RefundQueryRequest buildRefundOrderQueryRequest(RefundRequest refundRequest) {
        RefundQueryRequest refundQueryRequest = new RefundQueryRequest();

        refundQueryRequest.setAlipayTradeNo(refundRequest.getAlipayTradeNo());
        refundQueryRequest.setMerchantId(refundRequest.getMerchantId());
        refundQueryRequest.setOutRequestNo(refundRequest.getOutRequestNo());
        refundQueryRequest.setOutTradeNo(refundRequest.getOutTradeNo());
        refundQueryRequest.setRefundStatus(AlipayTradeStatusEnum.REFUND_SUCCESS.getCode());

        return refundQueryRequest;
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
