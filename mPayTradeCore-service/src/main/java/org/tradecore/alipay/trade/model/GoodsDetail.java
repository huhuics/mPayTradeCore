/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.model;

import org.tradecore.dao.domain.BaseDomain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author HuHui
 * @version $Id: GoodsDetail.java, v 0.1 2016年8月18日 下午4:28:38 HuHui Exp $
 */
public class GoodsDetail extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -3125086776135938672L;

    /**
     * 支付宝定义的统一商品编号
     */
    @JSONField(name = "alipay_goods_id")
    private String            alipayGoodsId;

    /**
     * 商品描述信息
     */
    @JSONField(name = "body")
    private String            body;

    /**
     * 商品类目
     */
    @JSONField(name = "goods_category")
    private String            goodsCategory;

    /**
     * 商品的编号
     */
    @JSONField(name = "goods_id")
    private String            goodsId;

    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    private String            goodsName;

    /**
     * 商品单价，单位为元
     */
    @JSONField(name = "price")
    private String            price;

    /**
     * 商品数量
     */
    @JSONField(name = "quantity")
    private Long              quantity;

    /**
     * 商品的展示地址
     */
    @JSONField(name = "show_url")
    private String            showUrl;

    public String getAlipayGoodsId() {
        return alipayGoodsId;
    }

    public void setAlipayGoodsId(String alipayGoodsId) {
        this.alipayGoodsId = alipayGoodsId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getGoodsCategory() {
        return goodsCategory;
    }

    public void setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getShowUrl() {
        return showUrl;
    }

    public void setShowUrl(String showUrl) {
        this.showUrl = showUrl;
    }

}
