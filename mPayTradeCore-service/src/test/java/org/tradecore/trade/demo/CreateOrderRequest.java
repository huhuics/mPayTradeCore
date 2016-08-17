package org.tradecore.trade.demo;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.pagehelper.StringUtil;

public class CreateOrderRequest implements Serializable {

	/** 序列化 */
	private static final long serialVersionUID = -3644856155162617991L;

	/**
	 * 收单机构号 (必填项)
	 */
	@JSONField(name = "acquirer_id")
	private String acquirerId;

	/**
	 * 支付宝分配给开发者的应用 ID (必填项)
	 */
	@JSONField(name = "app_id")
	private String appId;

	/**
	 * 接口名称 (必填项)
	 */
	@JSONField(name = "method")
	private String method;

	/**
	 * 格式 (必填项) 仅支持JSON
	 */
	@JSONField(name = "format")
	private String format;

	/**
	 * 编码格式 (必填项) utf-8
	 */
	@JSONField(name = "charset")
	private String charset;

	/**
	 * 商户生成签名字符串所使用的签名算法类型， 目前支持 RSA (必填项)
	 */
	@JSONField(name = "sign_type")
	private String signType;

	/**
	 * 签名字符串 (必填项)
	 */
	@JSONField(name = "sign")
	private String sign;

	/**
	 * 时间戳 (必填项)
	 */
	@JSONField(name = "timestamp")
	private String timestamp;

	/**
	 * 调用接口版本，固定为： 1.0 (必填项)
	 */
	@JSONField(name = "version")
	private String version;

	/**
	 * 主动通知收单机构服务器里指定的页面http路径 (必填项)
	 */
	@JSONField(name = "notify_url")
	private String notifyUrl;

	/**
     *  令牌 (可选项)
     */
	@JSONField(name = "app_auth_token")
	private String appAuthToken;

	/**
	 * 付款渠道 alipay (必填项)
	 */
	@JSONField(name = "wallet_type")
	private String walletType;

	/**
	 * 请求参数的集合 (必填项)
	 */
	private BizContent bizContent = new BizContent();

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

	public BizContent getBizContent() {
		return bizContent;
	}

	public void setBizContent(BizContent bizContent) {
		this.bizContent = bizContent;
	}

	/************************************* 内部类BizContent,表示请求参数 *************************************************/
	class BizContent implements Serializable {
		/** 序列化 */
		private static final long serialVersionUID = -2915514822747230281L;

		/**
		 * 商户识别号 (必填项)
		 */
		@JSONField(name = "merchant_id")
		private String merchantId;

		/**
		 * 商户订单号,32个字符以内 (必填项)
		 */
		@JSONField(name = "out_trade_no")
		private String tradeNo;

		/**
		 * 卖家支付宝用户ID (可选项，若不填，则使用商户签约账号对应的支付宝用户ID)
		 */
		@JSONField(name = "seller_id")
		private String sellerId;

		/**
		 * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] (必填项)
		 */
		@JSONField(name = "total_amount")
		private String totalAmount;

		/**
		 * 可打折金额. 参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] (可选项)
		 */
		@JSONField(name = "discountable_amount")
		private String discountableAmount;

		/**
		 * 不可打折金额. 不参与优惠计算的金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] (可选项)
		 */
		@JSONField(name = "undiscountable_amount")
		private String undiscountableAmount;

		/**
		 * 买家支付宝账号，和buyerId不能同时为空 (可选项)
		 */
		@JSONField(name = "buyer_logon_id")
		private String buyerLogOnId;

		/**
		 * 订单标题 (必填项)
		 */
		@JSONField(name = "subject")
		private String subject;

		/**
		 * 对交易或商品的描述 (可选项)
		 */
		@JSONField(name = "body")
		private String body;

		/**
		 * 买家的支付宝唯一用户号，和buyerLogOnId不能同时为空 (可选项)
		 */
		@JSONField(name = "buyer_id")
		private String buyerId;

		/**
		 * 订单包含的商品列表信息. Json格式 (可选项)
		 */
		@JSONField(name = "goods_detail")
		protected List<GoodsDetail> goodsDetailList;

		/**
		 * 商户操作员编号 (可选项)
		 */
		@JSONField(name = "operator_id")
		private String operatorId;

		/**
		 * 商户门店编号 (可选项)
		 */
		@JSONField(name = "store_id")
		private String storeId;

		/**
		 * 商户机具终端编号 (可选项)
		 */
		@JSONField(name = "terminal_id")
		private String terminalId;

		/**
		 * 业务扩展参数 (可选项)
		 */
		@JSONField(name = "extend_params")
		private String extendParams;

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

	/******************************CreateOrderRequest类对Bizcontent的操作接口 ***********************************/

	public String getMerchantId() {
		return this.bizContent.merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.bizContent.merchantId = merchantId;
	}

	public String getTradeNo() {
		return this.bizContent.tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.bizContent.tradeNo = tradeNo;
	}

	public String getSellerId() {
		return this.bizContent.sellerId;
	}

	public void setSellerId(String sellerId) {
		this.bizContent.sellerId = sellerId;
	}

	public String getTotalAmount() {
		return this.bizContent.totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.bizContent.totalAmount = totalAmount;
	}

	public String getDiscountableAmount() {
		return this.bizContent.discountableAmount;
	}

	public void setDiscountableAmount(String discountableAmount) {
		this.bizContent.discountableAmount = discountableAmount;
	}

	public String getUndiscountableAmount() {
		return this.bizContent.undiscountableAmount;
	}

	public void setUndiscountableAmount(String undiscountableAmount) {
		this.bizContent.undiscountableAmount = undiscountableAmount;
	}

	public String getBuyerLogOnId() {
		return this.bizContent.buyerLogOnId;
	}

	public void setBuyerLogOnId(String buyerLogOnId) {
		this.bizContent.buyerLogOnId = buyerLogOnId;
	}

	public String getSubject() {
		return this.bizContent.subject;
	}

	public void setSubject(String subject) {
		this.bizContent.subject = subject;
	}

	public String getBody() {
		return this.bizContent.body;
	}

	public void setBody(String body) {
		this.bizContent.body = body;
	}

	public String getBuyerId() {
		return this.bizContent.buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.bizContent.buyerId = buyerId;
	}

	public List<GoodsDetail> getGoodsDetailList() {
		return this.bizContent.goodsDetailList;
	}

	public void setGoodsDetailList(List<GoodsDetail> goodsDetailList) {
		this.bizContent.goodsDetailList = goodsDetailList;
	}

	public String getOperatorId() {
		return this.bizContent.operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.bizContent.operatorId = operatorId;
	}

	public String getStoreId() {
		return this.bizContent.storeId;
	}

	public void setStoreId(String storeId) {
		this.bizContent.storeId = storeId;
	}

	public String getTerminalId() {
		return this.bizContent.terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.bizContent.terminalId = terminalId;
	}

	public String getExtendParams() {
		return this.bizContent.extendParams;
	}

	public void setExtendParams(String extendParams) {
		this.bizContent.extendParams = extendParams;
	}

	/************************************ 获取HttpClient的参数 *************************************************/

	/*
	 * 构建HttpClient参数
	 * @return List<NameValuePair>
	 * @see org.apache.commons.httpclient.NameValuePair
	 */
	public List<NameValuePair> buildPostParaList() {

		Map<String, String> paraMap = null;
        //参数验证--验证必填参数是否已经填上
		validate();
		// 组装参数
		paraMap = assemblePara();
		// 签名-- rsaSign 第二个参数为私钥
		String sign = null;
		try {
			sign = AlipaySignature
					.rsaSign(
							paraMap,
							"MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALcfL22Dcf+R1WC7fpIlb6cYI0oPBtg2iv9K71sAMS5DShA6LavWo0RTqrsCNsSxGuexHwmWYWcPH9469fKTdEtAnACi/Hr2hQcw7vEehqnjYir8eQljqJpjSd7ZhXdDrfLsX4J2CbUEjkrMM0Q7rx/0cuI7b0yMWBRVPtnwALBvAgMBAAECgYAGtivk1aZ9+XhanUScUqbu9uGEO1zC2+zoQnTXXwBuc6TpR1iZLbq6LF7bj882Ek+sIj/C+DIFtvYyDPMquuDOOUvXm03HRWp20xB8X0fxJzQbS1MOvgxwFW4zDFr39UDrCue/nGVC/qrsWjX6IGYPYvAs2sB+jc3DTJpDJtWmQQJBAPMBZdky7FJ/gr+0KhEJdffWWjfkcdOLka6GetbZEiVWOplTdoIv4bjJFja8dRAQU+BeRmvMpvBnmFjH2RrH6nECQQDA6gP3Cri334zOPS6r1IzlqBy2QvI/11Fn/zyEe8MYtQz3Tkh5MX9R9ejK7lqIv9v1bsusG5SGYHyWMmSvnvjfAkBJ747xetD0eN9rPIHgFSTTd2CTyOnpF3oHw9r0K6+dtJK3u/E+wxrGgkhD9ysW7CDZD1YVznqsgpiTypp/z3vBAkA7deXS91MIGbdkuibwf4sOHkr7Qpc4Zj2JOHqGuz7fFq7wawibkk4UDR+7rMvq6nf5pjTQz49v+71q7g1qtC0xAkBr/ZBpOoO5kc4MZ/DR4TOeFXqaWCmDHsNxmtYPBtnTcuFLQCwH10Ab6Yo2owndvpa1PhdJ7b/h9z1Eom1jy0ba",
							StandardCharsets.UTF_8.displayName());
		} catch (Exception e) {
			throw new RuntimeException("加签发生异常");
		}
		paraMap.put("sign", sign);
		
        //转换为List<NameValuePair>
		List<NameValuePair> pairList = new ArrayList<NameValuePair>(
				paraMap.size());
		for (String key : paraMap.keySet()) {
			NameValuePair nvPair = new NameValuePair(key, paraMap.get(key));
			pairList.add(nvPair);
		}
		return pairList;
	}	
	
	/*
	 * 验证必填参数是否填写
	 */
	private void validate() {

		if (StringUtil.isEmpty(acquirerId)) {
			throw new RuntimeException("收单机构号不能为空");
		}
		if (StringUtil.isEmpty(appId)) {
			throw new RuntimeException("应用ID不能为空");
		}
		if (StringUtil.isEmpty(method)) {
			throw new RuntimeException("接口名称不能为空");
		}
		if (StringUtil.isEmpty(format)) {
			throw new RuntimeException("格式不能为空");
		}
		if (StringUtil.isEmpty(charset)) {
			throw new RuntimeException("编码格式不能为空");
		}
		if (StringUtil.isEmpty(timestamp)) {
			throw new RuntimeException("时间戳不能为空");
		}
		if (StringUtil.isEmpty(version)) {
			throw new RuntimeException("版本号不能为空");
		}
		if (StringUtil.isEmpty(notifyUrl)) {
			throw new RuntimeException("通知URL不能为空");
		}
		if (StringUtil.isEmpty(walletType)) {
			throw new RuntimeException("支付方式不能为空");
		}
		if (StringUtil.isEmpty(bizContent.merchantId)) {
			throw new RuntimeException("商户识别号不能为空");
		}
		if (StringUtil.isEmpty(bizContent.tradeNo)) {
			throw new RuntimeException("商户订单号不能为空");
		}
		if (StringUtil.isEmpty(bizContent.totalAmount)) {
			throw new RuntimeException("总金额不能为空");
		}
		if (StringUtil.isEmpty(bizContent.subject)) {
			throw new RuntimeException("订单标题不能为空");
		}
		if (StringUtil.isEmpty(bizContent.buyerId)
				&& StringUtil.isEmpty(bizContent.buyerLogOnId)) {
			throw new RuntimeException("卖家支付宝账号和用户好不能同时为空");
		}
	}
	
	/*
	 * 将Request的参数组装为Map<String,String>
	 * @return Map<String,String> 
	 */	
	private Map<String,String> assemblePara() {
		
		Map<String, String> paraMap = new HashMap<String, String>();	
		//组装参数--必填项
		paraMap.put("acquirer_id", this.acquirerId);		
		paraMap.put("app_id", this.appId);		
		paraMap.put("method", this.method);		
		paraMap.put("format", this.format);		
		paraMap.put("charset", this.charset);		
		paraMap.put("timestamp", this.timestamp);		
		paraMap.put("version", this.version);		
		paraMap.put("notify_url", this.notifyUrl);		
		paraMap.put("wallet_type", this.walletType);		
		paraMap.put("sign_type", this.signType);
		//组装参数--可选项
		if(StringUtil.isNotEmpty(appAuthToken)) {
			paraMap.put("app_auth_token", appAuthToken);
		}
		//BizContent序列化为JSON串
		String contentString = JSON.toJSONString(this.bizContent);
		paraMap.put("biz_content", contentString);
		
		return paraMap;
	}
}
