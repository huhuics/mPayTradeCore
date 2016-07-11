/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.alipay.enums.AlipaySceneEnum;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.alipay.trade.request.QueryRequest;
import org.tradecore.alipay.trade.request.RefundRequest;
import org.tradecore.common.util.LogUtil;
import org.tradecore.service.test.BaseTest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;

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
        payRequest.setAcquirerId("acquire_id_" + Math.random() * 10000000L);
        payRequest.setMerchantId("mechant_id_" + Math.random() * 10000000L);
        payRequest.setScene(AlipaySceneEnum.BAR_CODE.getCode());
        //支付条码
        payRequest.setAuthCode("280826073747515806");
        payRequest.setOutTradeNo("tradepay" + System.currentTimeMillis() + (long) (Math.random() * 10000000L));
        payRequest.setTotalAmount("0.05");
        payRequest.setSubject("胡辉测试交易" + Math.random() * 10000000L);
        payRequest.setStoreId("store_id_" + Math.random() * 10000000L);
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
     * 测试订单查询
     */
    @Test
    public void testQuery() {
        Assert.assertNotNull(tradeService);

        //组装参数
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setAcquirerId("acquire_id_2535400.4703792045");
        queryRequest.setMerchantId("mechant_id_2382725.100804911");
        queryRequest.setOutTradeNo("tradepay14681526897512625016");
        queryRequest.setAlipayTradeNo("");

        AlipayF2FQueryResult ret = tradeService.query(queryRequest);

        LogUtil.info(logger, "订单查询结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getTradeStatus().equals(TradeStatus.SUCCESS));
    }

    /**
     * 测试订单退款
     */
    @Test
    public void testRefund() {
        Assert.assertNotNull(tradeService);

        //组装参数
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setAcquirerId("acquire_id_2535400.4703792045");
        refundRequest.setMerchantId("mechant_id_2382725.100804911");
        refundRequest.setOutTradeNo("tradepay1468155933244153356");
        refundRequest.setRefundAmount("0.01");
        refundRequest.setRefundReason("正常退款");
        refundRequest.setStoreId("store_id");

        AlipayF2FRefundResult ret = tradeService.refund(refundRequest);

        LogUtil.info(logger, "订单退款结果ret={0}", JSON.toJSONString(ret, SerializerFeature.UseSingleQuotes));

        Assert.assertTrue(ret.getTradeStatus().equals(TradeStatus.SUCCESS));

    }
}
