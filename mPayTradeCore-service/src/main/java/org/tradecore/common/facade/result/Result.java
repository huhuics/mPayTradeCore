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
    private T                 response;

    /** 签名 */
    private String            sign;

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
