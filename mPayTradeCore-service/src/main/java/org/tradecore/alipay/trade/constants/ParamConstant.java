/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.constants;

/**
 * 参数常量
 * @author HuHui
 * @version $Id: ParamConstant.java, v 0.1 2016年7月12日 下午4:56:43 HuHui Exp $
 */
public class ParamConstant {

    public static final String APP_AUTH_TOKEN           = "app_auth_token";

    /** 支付宝配置信息格式 */
    public static final String ALIPAY_CONFIG_FORMAT     = "JSON";

    /** 支付宝配置信息编码格式 */
    public static final String ALIPAY_CONFIG_CHARSET    = "gbk";

    /** 扫码支付结果回调地址(结算中心端) */
    public static final String NOTIFY_URL               = "http://183.62.226.168:8088/mPay/tradeNotify/receive";

    /** 给收单机构的异步通知响应参数名 */
    public static final String NOTIFY_RESPONSE          = "notify_response";

    /** 接收收单机构业务参数JSON大字段名 */
    public static final String BIZ_CONTENT              = "biz_content";

    /** 收单机构编号 */
    public static final String ACQUIRER_ID              = "acquirerId";

    /** 装填 */
    public static final String STATUS                   = "status";

    /** 商户标识号 */
    public static final String MERCHANT_ID              = "merchantId";

    /** 签名 */
    public static final String SIGN                     = "sign";

    /** 商户创建返回json字段名 */
    public static final String MERCHANT_CREATE_RESPONSE = "merchant_create_response";

    /** 商户查询返回json字段名 */
    public static final String MERCHANT_QUERY_RESPONSE  = "merchant_query_response";

}
