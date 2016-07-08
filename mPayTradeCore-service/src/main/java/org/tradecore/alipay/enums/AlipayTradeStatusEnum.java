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

    INIT("INIT", "已创建"),

    WAIT_BUYER_PAY("WAIT_BUYER_PAY", "等待买家付款"),

    CLOSE("CLOSE", "交易关闭"),

    SUCCESS("SUCCESS", "支付成功"),

    FINISH("FINISH", "交易结束"),

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
