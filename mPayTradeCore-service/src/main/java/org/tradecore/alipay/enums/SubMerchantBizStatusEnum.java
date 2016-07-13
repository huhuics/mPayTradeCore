/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 二级商户业务状态枚举
 * @author HuHui
 * @version $Id: AlipaySceneEnum.java, v 0.1 2016年7月8日 下午7:55:02 HuHui Exp $
 */
public enum SubMerchantBizStatusEnum {

    NORMAL("NORMAL", "正常"),

    ABNORMAL("ABNORMAL", "停用")

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private SubMerchantBizStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static SubMerchantBizStatusEnum getByCode(String code) {
        for (SubMerchantBizStatusEnum eachValue : SubMerchantBizStatusEnum.values()) {
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
