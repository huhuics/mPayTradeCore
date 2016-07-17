/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.facade.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.tradecore.alipay.enums.BizResultEnum;

/**
 * Data Transfer Object(DTO)基础类
 * @author HuHui
 * @version $Id: BaseResponse.java, v 0.1 2016年7月15日 上午12:17:44 HuHui Exp $
 */
public class BaseResponse implements Serializable {

    /** uid */
    private static final long serialVersionUID = 1059891103577803036L;

    /** 结果码 */
    private String            code;

    /** 结果信息 */
    private String            msg;

    /** 子结果码 */
    private String            sub_code;

    /** 子信息 */
    private String            sub_msg;

    /** 响应消息体 */
    private String            body;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSub_code() {
        return sub_code;
    }

    public void setSub_code(String sub_code) {
        this.sub_code = sub_code;
    }

    public String getSub_msg() {
        return sub_msg;
    }

    public void setSub_msg(String sub_msg) {
        this.sub_msg = sub_msg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * 当发生异常时设置code和msg的值
     */
    public void setBizFailed() {
        this.code = BizResultEnum.FAILED.getCode();
        this.msg = BizResultEnum.FAILED.getDesc();
    }

}
