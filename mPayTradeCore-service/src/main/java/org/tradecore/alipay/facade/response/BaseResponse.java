/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.facade.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Data Transfer Object(DTO)基础类
 * @author HuHui
 * @version $Id: BaseResponse.java, v 0.1 2016年7月15日 上午12:17:44 HuHui Exp $
 */
public class BaseResponse implements Serializable {

    /** uid */
    private static final long serialVersionUID = 1059891103577803036L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
