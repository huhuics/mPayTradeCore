/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 支付场景枚举
 * @author HuHui
 * @version $Id: AlipaySceneEnum.java, v 0.1 2016年7月8日 下午7:55:02 HuHui Exp $
 */
public enum OrderCheckEnum {

    UNCHECK("uncheck", "未对账"),

    CHECKED("checked", "已对账")

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private OrderCheckEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static OrderCheckEnum getByCode(String code) {
        for (OrderCheckEnum eachValue : OrderCheckEnum.values()) {
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
