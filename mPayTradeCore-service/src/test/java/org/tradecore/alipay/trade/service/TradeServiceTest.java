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
import org.tradecore.alipay.enums.AlipaySceneEnum;
import org.tradecore.alipay.trade.request.PayRequest;
import org.tradecore.service.test.BaseTest;

import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.TradeStatus;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;

/**
 * 测试支付宝交易服务类
 * @author HuHui
 * @version $Id: TradeServiceTest.java, v 0.1 2016年7月8日 下午7:39:38 HuHui Exp $
 */
public class TradeServiceTest extends BaseTest {

    @Resource
    TradeService tradeService;

    @Test
    public void testPay() {

        Assert.assertNotNull(tradeService);

        //组装参数
        PayRequest payRequest = new PayRequest();
        payRequest.setAcquirerId("acquire_id_" + Math.random() * 10000);
        payRequest.setMerchantId("mechant_id_" + Math.random() * 10000);
        payRequest.setScene(AlipaySceneEnum.BAR_CODE.getCode());
        //支付条码
        payRequest.setAuthCode("282972765893720475");
        payRequest.setOutTradeNo("tradepay" + System.currentTimeMillis() + (long) (Math.random() * 10000000L));
        payRequest.setTotalAmount("0.01");
        payRequest.setSubject("胡辉测试交易" + Math.random() * 1000);
        payRequest.setStoreId("store_id_" + Math.random() * 1000);
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
}
