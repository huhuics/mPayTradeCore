/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 交易状态枚举
 * @author HuHui
 * @version $Id: AlipayTradeStatusEnum.java, v 0.1 2016年7月8日 下午9:09:43 HuHui Exp $
 */
public enum AlipayTradeStatusEnum {

    /** 
     * 支付初始状态 
     */
    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "等待买家付款"),

    /**
     * 未付款交易超时关闭，或支付完成后全额退款
     */
    TRADE_CLOSED("TRADE_CLOSED", "交易关闭"),

    /**
     * 交易支付成功
     */
    TRADE_SUCCESS("TRADE_SUCCESS", "交易成功"),

    /**
     * 交易支付失败
     */
    TRADE_FAILED("TRADE_FAILED", "交易失败"),

    /**
     * 交易结束，不可退款<br>
     * 订单变成TRADE_FINISHED状态，只有过了退款期限这一种情况
     */
    TRADE_FINISHED("TRADE_FINISHED", "交易结束"),

    /**
     * 业务交易状态未知<br>
     * 此时不清楚该业务是否成功或者失败，需要商户自行确认
     */
    UNKNOWN("UNKNOWN", "状态未知"),

    /** 撤销成功 */
    CANCEL_SUCCESS("CANCEL_SUCCESS", "撤销成功"),

    /** 撤销失败 */
    CANCEL_FAILED("CANCEL_FAILED", "撤销失败"),

    /** 退款成功 */
    REFUND_SUCCESS("REFUND_SUCCESS", "退款成功"),

    /** 退款失败 */
    REFUND_FAILED("REFUND_FAILED", "退款失败"),

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private AlipayTradeStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static AlipayTradeStatusEnum getByCode(String code) {
        for (AlipayTradeStatusEnum eachValue : AlipayTradeStatusEnum.values()) {
            if (StringUtils.equals(code, eachValue.getCode())) {
                return eachValue;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
