/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.constants;

/**
 * SQL查询字段常量<br>
 * 变量名为数据库字段名，变量值为查询参数Map中key的值
 * @author HuHui
 * @version $Id: QueryFieldConstant.java, v 0.1 2016年7月11日 下午3:43:33 HuHui Exp $
 */
public class QueryFieldConstant {

    /** 商户标识号 */
    public static final String MERCHANT_ID     = "merchantId";

    /** 商户订单号 */
    public static final String OUT_TRADE_NO    = "outTradeNo";

    /** 退款状态 */
    public static final String REFUND_STATUS   = "refundStatus";

    /** 撤销状态 */
    public static final String CANCEL_STATUS   = "cancelStatus";

    /** 收单机构号 */
    public static final String ACQUIRER_ID     = "acquirerId";

    /** 商户外部编号 */
    public static final String EXTERNAL_ID     = "externalId";

    /** 某次退款实际退款金额 */
    public static final String SEND_BACK_FEE   = "sendBackFee";

    /** 支付宝订单号 */
    public static final String ALIPAY_TRADE_NO = "alipayTradeNo";

}
