/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipayBizResultEnum;
import org.tradecore.alipay.enums.AlipaySceneEnum;
import org.tradecore.alipay.trade.constants.ParamConstant;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.ImageUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.service.test.BaseTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

/**
 * 测试支付宝交易服务类
 * @author HuHui
 * @version $Id: TradeServiceTest.java, v 0.1 2016年7月8日 下午7:39:38 HuHui Exp $
 */
public class TradeServiceTest extends BaseTest {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(TradeServiceTest.class);

    @Resource
    TradeService                tradeService;

    /**
     * 测试条码支付
     * @throws Exception 
     */
    @Test
    public void testPay() throws Exception {

        Assert.assertNotNull(tradeService);

        //组装参数
        PayRequest payRequest = new PayRequest();
        payRequest.setAcquirerId("10880010001");
        payRequest.setMerchantId("138");
        payRequest.setScene(AlipaySceneEnum.BAR_CODE.getCode());
        //支付条码
        payRequest.setAuthCode("283730526651806251");
        payRequest.setOutTradeNo("tradepay" + geneRandomId());
        payRequest.setTotalAmount("0.01");
        payRequest.setSubject("结算中心条码交易测试" + geneRandomId());
        payRequest.setStoreId("store_id_" + geneRandomId());
        payRequest.setBody("购买商品3件共20.00元");
        payRequest.setOperatorId("test_operator_id");
        payRequest.setTimeoutExpress("5m");//5分钟

        // 商品明细列表
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        GoodsDetail goods1 = new GoodsDetail();
        goods1.setGoodsId("goods_id001");
        goods1.setGoodsName("xxx面包");
        goods1.setPrice("1000");
        goods1.setQuantity(1L);
        goodsDetailList.add(goods1);
        GoodsDetail goods2 = new GoodsDetail();
        goods2.setGoodsId("goods_id002");
        goods2.setGoodsName("xxx牙刷");
        goods2.setPrice("500");
        goods2.setQuantity(2L);
        goodsDetailList.add(goods2);
        payRequest.setGoodsDetailList(goodsDetailList);

        AlipayTradePayResponse ret = tradeService.pay(payRequest);

        Assert.assertTrue(ret.getCode().equals(AlipayBizResultEnum.SUCCESS.getCode()));

    }

    /**
     * 测试扫码支付
     * @throws Exception 
     */
    @Test
    public void testPrecreate() throws Exception {

        Assert.assertNotNull(tradeService);

        //组装参数
        PrecreateRequest payRequest = new PrecreateRequest();
        payRequest.setAcquirerId("acquire_id_" + geneRandomId());
        payRequest.setMerchantId("27");
        payRequest.setScene(AlipaySceneEnum.SCAN_CODE.getCode());
        payRequest.setOutTradeNo("tradepay" + geneRandomId());
        payRequest.setTotalAmount("0.01");
        payRequest.setSubject("结算中心扫码交易测试" + geneRandomId());
        payRequest.setStoreId("store_id_" + geneRandomId());
        payRequest.setBody("购买商品3件共20.00元");
        payRequest.setOperatorId("test_operator_id");
        payRequest.setTimeoutExpress("15m");//15分钟

        // 商品明细列表
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        GoodsDetail goods1 = new GoodsDetail();
        goods1.setGoodsId("goods_id001");
        goods1.setGoodsName("xxx面包");
        goods1.setPrice("1000");
        goods1.setQuantity(1L);
        goodsDetailList.add(goods1);
        GoodsDetail goods2 = new GoodsDetail();
        goods2.setGoodsId("goods_id002");
        goods2.setGoodsName("xxx牙刷");
        goods2.setPrice("500");
        goods2.setQuantity(2L);
        goodsDetailList.add(goods2);
        payRequest.setGoodsDetailList(goodsDetailList);

        payRequest.setNotifyUrl(ParamConstant.NOTIFY_URL);
        payRequest.setOutNotifyUrl("http://www.notify.url.out");

        AlipayTradePrecreateResponse ret = tradeService.precreate(payRequest);

        String qrFilePath = String.format("F:/qr/%s.png", ret.getOutTradeNo());

        LogUtil.info(logger, "qrFilePath={0}", qrFilePath);

        //生成二维码图片
        ImageUtil.getQRCodeImge(ret.getQrCode(), 256, qrFilePath);

        Assert.assertTrue(ret.getCode().equals(AlipayBizResultEnum.SUCCESS.getCode()));
    }

    /**
     * 测试订单查询
     * @throws Exception 
     */
    @Test
    public void testQuery() throws Exception {
        Assert.assertNotNull(tradeService);

        //组装参数
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setAcquirerId("10880010001");
        queryRequest.setMerchantId("34830");
        queryRequest.setOutTradeNo("2016081000011017300091");
        queryRequest.setAlipayTradeNo("");

        AlipayTradeQueryResponse ret = tradeService.query(queryRequest);

        LogUtil.info(logger, "订单查询结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(StringUtils.equals(ret.getCode(), AlipayBizResultEnum.SUCCESS.getCode()));
    }

    /**
     * 测试订单退款
     * @throws Exception 
     */
    @Test
    public void testRefund() throws Exception {
        Assert.assertNotNull(tradeService);

        //组装参数
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAcquirerId("10880010001");
        refundRequest.setMerchantId("106");
        refundRequest.setOutTradeNo("out_trade_no_1469009103740");
        refundRequest.setRefundAmount("0.01");
        refundRequest.setRefundReason("正常退款");
        refundRequest.setStoreId("store_id_" + geneRandomId());
        refundRequest.setOutRequestNo("out_request_no_" + geneRandomId());

        AlipayTradeRefundResponse ret = tradeService.refund(refundRequest);

        LogUtil.info(logger, "订单退款结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getCode().equals(AlipayBizResultEnum.SUCCESS.getCode()));

    }

    /**
     * 测试订单撤销
     * @throws Exception 
     */
    @Test
    public void testCancel() throws Exception {
        Assert.assertNotNull(tradeService);

        //组装参数
        CancelRequest cancelRequest = new CancelRequest();
        cancelRequest.setAcquirerId("acquire_id_1468506224394");
        cancelRequest.setMerchantId("27");
        cancelRequest.setOutTradeNo("tradepay1468509260030");

        AlipayTradeCancelResponse ret = tradeService.cancel(cancelRequest);

        LogUtil.info(logger, "订单撤销结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getCode().equals(AlipayBizResultEnum.SUCCESS.getCode()));
    }

    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }
}
