package org.tradecore.trade.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author prd-wfc
 *
 */
public class AcquirerPayRequest {

    public BizRequest bizrequest = new BizRequest();

    /** (必填)收单机构编号 */
    @JSONField(name = "acquirer_id")
    protected String  acquirerId;

    /** (必填)支付宝分配给开发者的应用ID */
    @JSONField(name = "app_id")
    protected String  appId;

    /** (必填)接口名称 */
    @JSONField(name = "method")
    protected String  method;

    /** (必填)仅支持JSON */
    @JSONField(name = "format")
    protected String  format;

    /** (必填)请求使用的编码格式，utf-8*/
    @JSONField(name = "charset")
    protected String  charset;

    /** (必填)商户生成签名字符串所使用的签名算法类型*/
    @JSONField(name = "sign_type")
    protected String  signType;

    /** (必填)商户请求参数的签名串 */
    @JSONField(name = "sign")
    protected String  sign;

    /** (必填)发送请求的时间 */
    @JSONField(name = "timestamp")
    protected String  timestamp;

    /** (必填)调用接口版本号 */
    @JSONField(name = "version")
    protected String  version;

    /** (必填)结算中心服务器主动通知收单机构服务器里指定页面http路径 */
    @JSONField(name = "notify_url")
    protected String  notifyUrl;

    /** (必填)应用授权令牌 */
    @JSONField(name = "app_auth_token")
    protected String  appAuthToken;

    /** (必填)付款渠道 */
    @JSONField(name = "wallet_type")
    protected String  walletType;

    /** (必填)订单标题 */
    @JSONField(name = "biz_content")
    protected String  bizContent;

    class BizRequest {

        /** (必填)商户识别号 */
        @JSONField(name = "merchant_id")
        protected String merchantId;

        /** (必填)商户订单号 */
        @JSONField(name = "out_trade_no")
        protected String outTradeNo;

        /** (必填)支付场景 */
        @JSONField(name = "scene")
        protected String scene;

        /** (必填)支付授权码 */
        @JSONField(name = "auth_code")
        protected String authCode;

        /** (必填)订单标题 */
        @JSONField(name = "subject")
        protected String subject;

        /** (必填)订单标总金额*/
        @JSONField(name = "total_amount")
        protected String totalAmount;

        public String buildBizContent() {
            Map<String, String> paraMap = new TreeMap<String, String>();

            if (StringUtils.isNotBlank(merchantId)) {
                paraMap.put("merchant_id", merchantId);
            }
            if (StringUtils.isNotBlank(outTradeNo)) {
                paraMap.put("out_trade_no", outTradeNo);
            }
            if (StringUtils.isNotBlank(scene)) {
                paraMap.put("scene", scene);
            }
            if (StringUtils.isNotBlank(authCode)) {
                paraMap.put("auth_code", authCode);
            }
            if (StringUtils.isNotBlank(subject)) {
                paraMap.put("subject", subject);
            }
            if (StringUtils.isNotBlank(totalAmount)) {
                paraMap.put("total_amount", totalAmount);
            }

            return JSON.toJSONString(paraMap);
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getOutTradeNo() {
            return outTradeNo;
        }

        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }

        public String getScene() {
            return scene;
        }

        public void setScene(String scene) {
            this.scene = scene;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        protected String getTotalAmount() {
            return totalAmount;
        }

        protected void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

    }

    /**
     * 将不为空的参数放入TreeMap<br>
     */
    public Map<String, String> buildParaMap() {
        Map<String, String> paraMap = new TreeMap<String, String>();

        if (StringUtils.isNotBlank(acquirerId)) {
            paraMap.put("acquirer_id", acquirerId);
        }
        if (StringUtils.isNotBlank(appId)) {
            paraMap.put("app_id", appId);
        }
        if (StringUtils.isNotBlank(method)) {
            paraMap.put("method", method);
        }
        if (StringUtils.isNotBlank(charset)) {
            paraMap.put("charset", charset);
        }
        if (StringUtils.isNotBlank(signType)) {
            paraMap.put("sign_type", signType);
        }
        if (StringUtils.isNotBlank(sign)) {
            paraMap.put("sign", sign);
        }
        if (StringUtils.isNotBlank(timestamp)) {
            paraMap.put("timestamp", timestamp);
        }
        if (StringUtils.isNotBlank(version)) {
            paraMap.put("version", version);
        }
        if (StringUtils.isNotBlank(notifyUrl)) {
            paraMap.put("notify_url", notifyUrl);
        }
        if (StringUtils.isNotBlank(appAuthToken)) {
            paraMap.put("app_auth_token", appAuthToken);
        }
        if (StringUtils.isNotBlank(walletType)) {
            paraMap.put("wallet_type", walletType);
        }
        if (StringUtils.isNotBlank(bizContent)) {
            paraMap.put("biz_content", bizContent);
        }

        return paraMap;
    }

    /**
     * 将Map中的参数转换成NamValuePair对，并封装成List
     */
    public List<NameValuePair> buildPostParaList(Map<String, String> paraMap) {

        List<NameValuePair> pairList = new ArrayList<NameValuePair>(paraMap.size());

        for (String key : paraMap.keySet()) {
            NameValuePair nvPair = new NameValuePair(key, paraMap.get(key));
            pairList.add(nvPair);
        }

        return pairList;
    }

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

}
