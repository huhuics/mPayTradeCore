/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import java.util.List;

import org.tradecore.alipay.trade.model.SubMerchant;
import org.tradecore.common.util.AssertUtil;

import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;

/**
 * 支付宝条码支付和扫码支付请求参数的父类<br>
 * 必填都是只商户必填
 * @author HuHui
 * @version $Id: DefaultPayRequest.java, v 0.1 2016年7月12日 下午9:43:35 HuHui Exp $
 */
public class DefaultPayRequest extends BaseRequest {

    /**  */
    private static final long   serialVersionUID = 1558715761988486700L;

    /** (必填)收单机构编号 */
    @JSONField(name = "acquirer_id")
    protected String            acquirerId;

    /** (必填)商户识别号 */
    @JSONField(name = "merchant_id")
    protected String            merchantId;

    /** (必填)支付场景，条码支付场景为bar_code，扫码支付为scan_code */
    @JSONField(name = "scene")
    protected String            scene;

    /**
     *  (必填)商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，需保证商户系统端不能重复，建议通过数据库sequence生成， 
     */
    @JSONField(name = "out_trade_no")
    protected String            outTradeNo;

    /**
     * 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)。<br>
     * 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
     */
    @JSONField(name = "seller_id")
    protected String            sellerId;

    /**
     * (必填)订单总金额，整形，此处单位为元，精确到小数点后2位，不能超过1亿元<br>
     * 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
     */
    @JSONField(name = "total_amount")
    protected String            totalAmount;

    /**
     * 订单可打折金额，此处单位为元，精确到小数点后2位<br>
     * 可以配合商家平台配置折扣活动，如果订单部分商品参与打折，可以将部分商品总价填写至此字段，默认全部商品可打折<br>
     * 如果该值未传入,但传入了【订单总金额】,【不可打折金额】 则该值默认为【订单总金额】- 【不可打折金额】
     */
    @JSONField(name = "discountable_amount")
    protected String            discountableAmount;

    /**
     * 订单不可打折金额，此处单位为元，精确到小数点后2位，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段<br>
     * 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
     */
    @JSONField(name = "undiscountable_amount")
    protected String            undiscountableAmount;

    /**
     * (必填)订单标题，粗略描述用户的支付目的。如“喜士多（浦东店）消费”
     */
    @JSONField(name = "subject")
    protected String            subject;

    /**
     * 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
     */
    @JSONField(name = "body")
    protected String            body;

    /**
     * 应用授权令牌
     */
    @JSONField(name = "app_auth_token")
    protected String            appAuthToken;

    /**
     * 商品明细列表，需填写购买商品详细信息
     */
    @JSONField(name = "goods_detail")
    protected List<GoodsDetail> goodsDetailList;

    /**
     * 商户操作员编号，添加此参数可以为商户操作员做销售统计
     */
    @JSONField(name = "operator_id")
    protected String            operatorId;

    /**
     * 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
     */
    @JSONField(name = "store_id")
    protected String            storeId;

    /**
     * 支付宝商家平台中配置的商户门店号
     */
    @JSONField(name = "alipay_store_id")
    protected String            alipayStoreId;

    /**
     * 商户机具终端编号，当以机具方式接入支付宝时必传
     */
    @JSONField(name = "terminal_id")
    protected String            terminalId;

    /**
     * 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)
     */
    @JSONField(name = "extend_params")
    protected ExtendParams      extendParams;

    /**
     * (推荐使用，相对时间) 支付超时时间，5m 5分钟
     */
    @JSONField(name = "timeout_express")
    protected String            timeoutExpress;

    /** 二级商户 */
    @JSONField(name = "sub_merchant")
    protected SubMerchant       subMerchant;

    /**
     * 参数校验
     * @return 校验是否成功
     */
    @Override
    public boolean validate() {

        AssertUtil.assertNotEmpty(acquirerId, "收单机构编号不能为空");

        AssertUtil.assertNotEmpty(merchantId, "商户标识号不能为空");

        AssertUtil.assertNotEmpty(scene, "交易场景不能为空");

        AssertUtil.assertNotEmpty(outTradeNo, "商户订单号不能为空");

        AssertUtil.assertNotEmpty(totalAmount, "订单总金额不能为空");

        AssertUtil.assertNotEmpty(subject, "订单标题不能为空");

        return true;
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscountableAmount() {
        return discountableAmount;
    }

    public void setDiscountableAmount(String discountableAmount) {
        this.discountableAmount = discountableAmount;
    }

    public String getUndiscountableAmount() {
        return undiscountableAmount;
    }

    public void setUndiscountableAmount(String undiscountableAmount) {
        this.undiscountableAmount = undiscountableAmount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public List<GoodsDetail> getGoodsDetailList() {
        return goodsDetailList;
    }

    public void setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
        this.goodsDetailList = goodsDetailList;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAlipayStoreId() {
        return alipayStoreId;
    }

    public void setAlipayStoreId(String alipayStoreId) {
        this.alipayStoreId = alipayStoreId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public ExtendParams getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(ExtendParams extendParams) {
        this.extendParams = extendParams;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    public SubMerchant getSubMerchant() {
        return subMerchant;
    }

    public void setSubMerchant(SubMerchant subMerchant) {
        this.subMerchant = subMerchant;
    }

    public void setSubMerchantId(String merchantId) {
        SubMerchant subMerchant = new SubMerchant();
        subMerchant.setMerchantId(merchantId);
        this.subMerchant = subMerchant;
    }

}
