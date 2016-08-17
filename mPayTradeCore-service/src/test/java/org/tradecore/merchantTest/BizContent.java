package org.tradecore.merchantTest;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.api.domain.GoodsDetail;

public class BizContent implements Serializable {
    /**  */
    private static final long   serialVersionUID = -2915514822747230281L;

    /**   */
    @JSONField(name = "merchant_id")
    private String              merchantId;

    /**
     * 
     */
    @JSONField(name = "out_trade_no")
    private String              tradeNo;

    /**
     * 
     */
    @JSONField(name = "seller_id")
    private String              sellerId;

    /**
     * 
     */
    @JSONField(name = "total_amount")
    private String              totalAmount;

    /**
     *
     */
    @JSONField(name = "discountable_amount")
    private String              discountableAmount;

    /**
     * 
     */
    @JSONField(name = "undiscountable_amount")
    private String              undiscountableAmount;

    /**
     * 
     */
    @JSONField(name = "buyer_log_on_id")
    private String              buyerLogOnId;

    /**
     * 
     */
    @JSONField(name = "subject")
    private String              subject;

    /**
     * 
     */
    @JSONField(name = "body")
    private String              body;

    /**
     * 
     */
    @JSONField(name = "buyer_id")
    private String              buyerId;

    /**
     * 
     */
    @JSONField(name = "goods_detail")
    protected List<GoodsDetail> goodsDetailList;

    /**
     * 
     */
    @JSONField(name = "operator_id")
    private String              operatorId;

    /**
     * 
     */
    @JSONField(name = "store_id")
    private String              storeId;

    /**
     * 
     */
    @JSONField(name = "terminal_id")
    private String              terminalId;

    /**
     * 
     */
    @JSONField(name = "extend_params")
    private String              extendParams;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
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

    public String getBuyerLogOnId() {
        return buyerLogOnId;
    }

    public void setBuyerLogOnId(String buyerLogOnId) {
        this.buyerLogOnId = buyerLogOnId;
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

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getExtendParams() {
        return extendParams;
    }

    public void setExtendParams(String extendParams) {
        this.extendParams = extendParams;
    }
}
