/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.facade.result;

import java.io.Serializable;

/**
 * 结果类
 * @author HuHui
 * @version $Id: Result.java, v 0.1 2016年5月22日 上午12:35:42 HuHui Exp $
 */
public class Result<T> implements Serializable {

    /** uid */
    private static final long serialVersionUID = -7226506032494666664L;

    /** 结果对象 */
    private T                 resultObj;

    /** 结果码 */
    private String            code;

    /** 结果信息 */
    private String            msg;

    /** 子结果码 */
    private String            subCode;

    /** 子信息 */
    private String            subMsg;

    /** 响应消息体 */
    private String            body;

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }

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

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
