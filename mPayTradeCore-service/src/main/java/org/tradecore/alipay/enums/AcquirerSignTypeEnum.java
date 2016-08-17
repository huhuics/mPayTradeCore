/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 收单机构与结算中心签约类型
 * @author HuHui
 * @version $Id: AcquirerSignTypeEnum.java, v 0.1 2016年8月17日 下午7:55:02 HuHui Exp $
 */
public enum AcquirerSignTypeEnum {

    TWO_PARTY("TWO_PARTY", "两方协议"),

    THREE_PARTY("THREE_PARTY", "三方协议")

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private AcquirerSignTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static AcquirerSignTypeEnum getByCode(String code) {
        for (AcquirerSignTypeEnum eachValue : AcquirerSignTypeEnum.values()) {
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
