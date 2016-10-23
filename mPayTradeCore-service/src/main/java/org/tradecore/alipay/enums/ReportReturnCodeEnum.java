package org.tradecore.alipay.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 类描述： 资金管理平台查询报表返回码
 * 创建者： chengwenjing
 * 项目名称： mPayTradeCore-service
 * 创建时间： 2016-10-17 下午3:36:02
 * 版本号： v1.0
 */
public enum ReportReturnCodeEnum {
    SUCCESS("0000", "处理成功"), FAIL("0001", "处理失败"), ;
    private String                                         value;
    private String                                         displayName;

    private static final Map<String, ReportReturnCodeEnum> map = new HashMap<String, ReportReturnCodeEnum>();
    static {
        for (ReportReturnCodeEnum e : ReportReturnCodeEnum.values()) {
            map.put(e.getValue(), e);
        }
    }

    ReportReturnCodeEnum(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public ReportReturnCodeEnum getEnum(String value) {
        return map.get(value);
    }

    /**
     * 枚举转换
     */
    public static ReportReturnCodeEnum parseOf(String value) {
        for (ReportReturnCodeEnum item : values())
            if (item.getValue().equals(value))
                return item;
        throw new IllegalArgumentException("枚举值[" + value + "]不匹配!");
    }

}
