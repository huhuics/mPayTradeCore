package org.tradecore.alipay.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 类描述： 资金管理平台查询报表状态码
 * 创建者： chenwenjing
 * 项目名称： mPayTradeCore-service
 * 创建时间： 2016-10-17 下午3:36:55
 * 版本号： v1.0
 */
public enum ReportStateCodeEnum {
    SUCCESS("00", "处理成功"), NOREPORT("01", "报表未生成"), FAIL("99", "未知错误"), ;
    private String                                        value;
    private String                                        displayName;

    private static final Map<String, ReportStateCodeEnum> map = new HashMap<String, ReportStateCodeEnum>();
    static {
        for (ReportStateCodeEnum e : ReportStateCodeEnum.values()) {
            map.put(e.getValue(), e);
        }
    }

    ReportStateCodeEnum(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ReportStateCodeEnum getEnum(String value) {
        return map.get(value);
    }

    /**
     * 枚举转换
     */
    public static ReportStateCodeEnum parseOf(String value) {
        for (ReportStateCodeEnum item : values())
            if (item.getValue().equals(value))
                return item;
        throw new IllegalArgumentException("枚举值[" + value + "]不匹配!");
    }

}
