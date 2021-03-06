/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 通用业务结果码枚举
 * @author HuHui
 * @version $Id: DefaultBizResultEnum.java, v 0.1 2016年8月2日 下午4:45:02 HuHui Exp $
 */
public enum DefaultBizResultEnum {

    SUCCESS("success", "业务处理成功"),

    FAILED("failed", "业务处理失败"),

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private DefaultBizResultEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static DefaultBizResultEnum getByCode(String code) {
        for (DefaultBizResultEnum eachValue : DefaultBizResultEnum.values()) {
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
