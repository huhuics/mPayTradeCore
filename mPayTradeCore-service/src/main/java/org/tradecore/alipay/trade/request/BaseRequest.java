/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.request;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 基本的查询参数类
 * @author HuHui
 * @version $Id: BaseRequest.java, v 0.1 2016年7月8日 下午3:31:41 HuHui Exp $
 */
public class BaseRequest implements Serializable {

    /**  */
    private static final long serialVersionUID = -7977401051608913047L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
