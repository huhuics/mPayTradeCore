/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 支付宝业务结果码枚举
 * @author HuHui
 * @version $Id: AlipayBizResultEnum.java, v 0.1 2016年7月8日 下午7:55:02 HuHui Exp $
 */
public enum AlipayBizResultEnum {

    SUCCESS("10000", "业务处理成功"),

    FAILED("40004", "业务处理失败"),

    PROCESSING("10003", "业务处理中"),

    UNKNOW("20000", "业务出现未知错误或者系统异常")

    ;

    /** 枚举代码 */
    private String code;

    /** 枚举值 */
    private String desc;

    private AlipayBizResultEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**

     * 根据代码获取枚举，如果code对应的枚举不存在，则返回null

     * @param code 枚举代码

     * @return     对应的枚举对象

     */
    public static AlipayBizResultEnum getByCode(String code) {
        for (AlipayBizResultEnum eachValue : AlipayBizResultEnum.values()) {
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
