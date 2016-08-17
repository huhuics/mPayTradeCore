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

    public String getAcquirer_id() {
        return acquirerId;
    }

    public void setAcquirer_id(String acquirer_id) {
        this.acquirerId = acquirer_id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
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

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
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

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getApp_auth_token() {
        return app_auth_token;
    }

    public void setApp_auth_token(String app_auth_token) {
        this.app_auth_token = app_auth_token;
    }

    public String getWallet_type() {
        return wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getBiz_content() {
        return biz_content;
    }

    public void setBiz_content(String biz_content) {
        this.biz_content = biz_content;
    }

    public Map<String, String> getParaMap() {
        
        Map<String, String> paraMap = new HashMap<String, String>();
        paraMap.put("acquirer_id", );
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
