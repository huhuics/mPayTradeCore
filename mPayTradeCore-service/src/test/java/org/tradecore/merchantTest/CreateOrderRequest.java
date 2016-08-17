package org.tradecore.merchantTest;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class CreateOrderRequest implements Serializable {

    /**  */
    private static final long serialVersionUID = -3644856155162617991L;

    /**
     *  收单机构号
     */
    private String            acquirerId;

    /**
     * 
     */
    private String            appId;

    /**
     * 
     */
    private String            method;

    /**
     *  
     */
    private String            format;

    /**
     *  
     */
    private String            charset;

    /**
     *  
     */
    private String            signType;

    /**
     *  
     */
    private String            sign;

    /**
     * 
     */
    private String            timestamp;

    /**
     * 
     */
    private String            version;

    /**
     *  
     */
    private String            notifyUrl;

    /**
     *  
     */
    private String            appAuthToken;

    /**
     *  
     */
    private String            walletType;

    /**
     * 
     */
    private String            bizContent;

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getBizContent() {
        return bizContent;
    }

    public void setBizContent(String bizContent) {
        this.bizContent = bizContent;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Map<String, String> getParaMap() {

        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("acquirer_id", "10880010001");
        paraMap.put("app_id", "2014072300007148");
        paraMap.put("method", "alipay.boss.prod.merch");
        paraMap.put("format", "JSON");
        paraMap.put("charset", "utf-8");
        paraMap.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        paraMap.put("version", "1.0");
        paraMap.put("notify_url", "http://api.test.alipay.net/atinterface/recieve_notify.htm");
        paraMap.put("wallet_type", "alipay");

        BizContent biz_content = new BizContent();
        biz_content.setMerchantId("27774");
        biz_content.setTradeNo("20150320010101082");
        biz_content.setTotalAmount("20.00");
        biz_content.setSubject("iphone");

        paraMap.put("biz_content", JSON.toJSONString(biz_content));

        return paraMap;
    }
}
