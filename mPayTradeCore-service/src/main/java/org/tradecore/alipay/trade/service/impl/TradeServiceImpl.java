/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tradecore.alipay.trade.repository.PayRepository;
import org.tradecore.alipay.trade.repository.RefundRepository;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Money;
import org.tradecore.dao.domain.BizAlipayPayOrder;
import org.tradecore.dao.domain.BizAlipayRefundOrder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

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

    /**
     * 条码支付仓储服务
     */
    @Resource
    private PayRepository             payRepository;

    /**
     * 退款仓储服务
     */
    @Resource
    private RefundRepository          refundRepository;

    static {
        //1.读取配置文件
        Configs.init("config/zfbinfo.properties");

        //2.工厂方法创建静态支付服务类
        alipayTradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }

    @Override
    @Transactional
    public AlipayF2FPayResult pay(PayRequest payRequest) {

        LogUtil.info(logger, "收到条码支付请求参数,payRequest={0}", payRequest);

        //1.校验参数
        AssertUtil.assertNotNull(payRequest, "条码支付请求不能为空");
        AssertUtil.assertTrue(payRequest.validate(), "支付请求参数不合法");

        //2.请求参数转换成支付宝支付请求参数
        AlipayTradePayRequestBuilder builder = convert2Builder(payRequest);

        //3.本地持久化
        BizAlipayPayOrder bizAlipayPayOrder = payRepository.savePayOrder(payRequest);

        //4.调用支付宝支付接口
        AlipayF2FPayResult alipayF2FPayResult = alipayTradeService.tradePay(builder);

        LogUtil.info(logger, "支付宝返回支付业务结果alipayF2FPayResult={0}", JSON.toJSONString(alipayF2FPayResult, SerializerFeature.UseSingleQuotes));

        //5.根据支付宝返回结果更新本地数据
        payRepository.updatePayOrder(bizAlipayPayOrder, alipayF2FPayResult);

        return alipayF2FPayResult;
    }

    @Override
    @Transactional
    public AlipayF2FPrecreateResult precreate(PrecreateRequest precreateRequest) {
        return null;
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
     * 将支付请求参数转换成支付宝请求
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

        List<BizAlipayRefundOrder> refundOrders = refundRepository.selectRefundOrdersByOutTradeNo(outTradeNo);

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
}
