/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 撤销触发动作
 * @author HuHui
 * @version $Id: CancelAction.java, v 0.1 2016年9月6日 上午10:25:59 HuHui Exp $
 */
public enum CancelActionEnum {

    CLOSE("close", "关闭交易,无退款"),

    REFUND("refund", "产生了退款"),

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private CancelActionEnum(String code, String desc) {
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
