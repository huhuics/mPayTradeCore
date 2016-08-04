/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

/**
 * 格式化订单号<br>
 * 订单号=收单机构号+商户识别号+商户订单号。在结算中心全局唯一
 * @author HuHui
 * @version $Id: TradeNoFormater.java, v 0.1 2016年8月4日 下午5:16:34 HuHui Exp $
 */
public class TradeNoFormater {

    public static String format(String acquirerId, String merchantId, String outTradeNo) {
        return new StringBuilder(acquirerId).append(merchantId).append(outTradeNo).toString();
    }

}
