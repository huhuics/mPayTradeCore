/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.constants;

import java.nio.charset.StandardCharsets;

import org.tradecore.common.config.Resources;

/**
 * 参数常量
 * @author HuHui
 * @version $Id: ParamConstant.java, v 0.1 2016年7月12日 下午4:56:43 HuHui Exp $
 */
public class ParamConstant {

    public static final String APP_AUTH_TOKEN                  = "app_auth_token";

    /** 支付宝配置信息格式 */
    public static final String ALIPAY_CONFIG_FORMAT            = "JSON";

    /** 支付宝配置信息编码格式 */
    public static final String ALIPAY_CONFIG_CHARSET           = StandardCharsets.UTF_8.displayName();

    /** 扫码支付结果回调地址(结算中心端) */
    public static final String NOTIFY_URL                      = Resources.NOTIFY_URL.getString("notify_url");

    /** 给收单机构的异步通知响应参数名 */
    public static final String NOTIFY_RESPONSE                 = "notify_response";

    /** 接收收单机构业务参数JSON大字段名 */
    public static final String BIZ_CONTENT                     = "biz_content";

    /** 收单机构编号 */
    public static final String ACQUIRER_ID                     = "acquirerId";

    /** 状态 */
    public static final String STATUS                          = "status";

    /** 商户标识号 */
    public static final String MERCHANT_ID                     = "merchantId";

    /** 签名 */
    public static final String SIGN                            = "sign";

    /** 签名方式 */
    public static final String SIGN_TYPE                       = "sign_type";

    /** 签名方式值 */
    public static final String SIGN_TYPE_VALUE                 = "RSA";

    /** 异步通知成功 */
    public static final String NOTIFY_SUCCESS                  = "success";

    /** 异步通知失败 */
    public static final String NOTIFY_FAILED                   = "failed";

    /** 时间后缀 */
    public static final String TIME_SUFFIX                     = "235959";

    /** 字符Y */
    public static final String Y                               = "Y";

    /** 第一个元素 */
    public static final int    FIRST_INDEX                     = 0;

    /** 商户创建返回json字段名 */
    public static final String MERCHANT_CREATE_RESPONSE        = "merchant_create_response";

    /** 商户查询返回json字段名 */
    public static final String MERCHANT_QUERY_RESPONSE         = "merchant_query_response";

    /** 商户信息修改返回json字段名 */
    public static final String MERCHANT_MODIFY_RESPONSE        = "merchant_modify_response";

    /** 条码交易返回json字段名 */
    public static final String ALIPAY_TRADE_PAY_RESPONSE       = "alipay_trade_pay_response";

    /** 订单创建返回json字段名 */
    public static final String ALIPAY_TRADE_CREATE_RESPONSE    = "alipay_trade_create_response";

    /** 扫码交易返回json字段名 */
    public static final String ALIPAY_TRADE_PRECREATE_RESPONSE = "alipay_trade_precreate_response";

    /** 订单查询返回json字段名 */
    public static final String ALIPAY_TRADE_QUERY_RESPONSE     = "alipay_trade_query_response";

    /** 订单撤销返回json字段名 */
    public static final String ALIPAY_TRADE_CANCEL_RESPONSE    = "alipay_trade_cancel_response";

    /** 订单退款返回json字段名 */
    public static final String ALIPAY_TRADE_REFUND_RESPONSE    = "alipay_trade_refund_response";

}
