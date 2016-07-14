/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipaySceneEnum;
import org.tradecore.alipay.enums.AlipayTradeStatusEnum;
import org.tradecore.alipay.enums.BizResultEnum;
import org.tradecore.alipay.trade.request.CancelRequest;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.PrecreateRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.alipay.trade.service.TradeService;
import org.tradecore.common.util.LogUtil;
import org.tradecore.service.test.BaseTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.utils.ZxingUtils;

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
     */
    @Test
    public void testPay() {

        Assert.assertNotNull(tradeService);

        //组装参数
        PayRequest payRequest = new PayRequest();
        payRequest.setAcquirerId("acquire_id_" + geneRandomId());
        payRequest.setMerchantId("27");
        payRequest.setScene(AlipaySceneEnum.BAR_CODE.getCode());
        //支付条码
        payRequest.setAuthCode("282789805600701189");
        payRequest.setOutTradeNo("tradepay" + geneRandomId());
        payRequest.setTotalAmount("0.01");
        payRequest.setSubject("胡辉条码交易测试" + geneRandomId());
        payRequest.setStoreId("store_id_" + geneRandomId());
        payRequest.setBody("购买商品3件共20.00元");
        payRequest.setOperatorId("test_operator_id");
        payRequest.setTimeoutExpress("5m");//5分钟

        // 商品明细列表
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx面包", 1000, 1);//单位为分，数量为1
        goodsDetailList.add(goods1);
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);
        payRequest.setGoodsDetailList(goodsDetailList);

        AlipayF2FPayResult ret = tradeService.pay(payRequest);

        Assert.assertTrue(ret.getTradeStatus().equals(TradeStatus.SUCCESS));

    }

    /**
     * 测试扫码支付
     */
    @Test
    public void testPrecreate() {

        Assert.assertNotNull(tradeService);

        //组装参数
        PrecreateRequest payRequest = new PrecreateRequest();
        payRequest.setAcquirerId("acquire_id_" + geneRandomId());
        payRequest.setMerchantId("mechant_id_" + geneRandomId());
        payRequest.setScene(AlipaySceneEnum.SCAN_CODE.getCode());
        payRequest.setOutTradeNo("tradepay" + geneRandomId());
        payRequest.setTotalAmount("0.01");
        payRequest.setSubject("胡辉扫码交易测试" + geneRandomId());
        payRequest.setStoreId("store_id_" + geneRandomId());
        payRequest.setBody("购买商品3件共20.00元");
        payRequest.setOperatorId("test_operator_id");
        payRequest.setTimeoutExpress("15m");//15分钟

        // 商品明细列表
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "西瓜", 1000, 1);//单位为分，数量为1
        goodsDetailList.add(goods1);
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "苹果", 500, 2);
        goodsDetailList.add(goods2);
        payRequest.setGoodsDetailList(goodsDetailList);

        payRequest.setNotifyUrl("http://183.62.226.168:8088/mPay/tradeNotify/receive");
        payRequest.setOutNotifyUrl("http://www.notify.url.out");

        AlipayF2FPrecreateResult ret = tradeService.precreate(payRequest);

        String qrFilePath = String.format("F:/qr/%s.png", ret.getResponse().getOutTradeNo());

        LogUtil.info(logger, "qrFilePath={0}", qrFilePath);

        //生成二维码图片
        ZxingUtils.getQRCodeImge(ret.getResponse().getQrCode(), 256, qrFilePath);

        Assert.assertTrue(ret.getTradeStatus().equals(TradeStatus.SUCCESS));
    }

    /**
     * 测试订单查询
     */
    @Test
    public void testQuery() {
        Assert.assertNotNull(tradeService);

        //组装参数
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setAcquirerId("acquire_id_8831476.635618655");
        queryRequest.setMerchantId("mechant_id_1876214.1478672957");
        queryRequest.setOutTradeNo("tradepay14682204756429961535");
        queryRequest.setAlipayTradeNo("");

        AlipayF2FQueryResult ret = tradeService.query(queryRequest);

        LogUtil.info(logger, "订单查询结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getResponse().getTradeStatus().equals(AlipayTradeStatusEnum.TRADE_CLOSED.getCode()));
    }

    /**
     * 测试订单退款
     */
    @Test
    public void testRefund() {
        Assert.assertNotNull(tradeService);

        //组装参数
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAcquirerId("acquire_id_1468498963549");
        refundRequest.setMerchantId("27");
        refundRequest.setOutTradeNo("tradepay1468504919701");
        refundRequest.setRefundAmount("0.01");
        refundRequest.setRefundReason("正常退款");
        refundRequest.setStoreId("store_id_" + geneRandomId());
        refundRequest.setOutRequestNo("out_request_no_" + geneRandomId());

        AlipayF2FRefundResult ret = tradeService.refund(refundRequest);

        LogUtil.info(logger, "订单退款结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getTradeStatus().equals(TradeStatus.SUCCESS));

    }

    /**
     * 测试订单撤销
     */
    @Test
    public void testCancel() {
        Assert.assertNotNull(tradeService);

        //组装参数
        CancelRequest cancelRequest = new CancelRequest();
        cancelRequest.setAcquirerId("acquire_id_1468334082337");
        cancelRequest.setMerchantId("mechant_id_1468334911719");
        cancelRequest.setOutTradeNo("tradepay1468329244485");

        AlipayTradeCancelResponse ret = tradeService.cancel(cancelRequest);

        LogUtil.info(logger, "订单撤销结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getCode().equals(BizResultEnum.SUCCESS.getCode()));
    }

    private String geneRandomId() {
        return (System.currentTimeMillis() + (long) (Math.random() * 10000000L)) + "";
    }
}
