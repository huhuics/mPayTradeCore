/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.util;

import java.util.Date;
import java.util.UUID;

/**
 * 生成带时间前缀的UUID作为数据库主键<br>
 * 生成的主键前8位表示年月日，后32位为UUID
 * @author HuHui
 * @version $Id: UUIDUtil.java, v 0.1 2016年7月9日 上午10:26:30 HuHui Exp $
 */
public class UUIDUtil {

    public static String geneId() {
        return DateUtil.format(new Date(), DateUtil.shortFormat) + UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
    }

}
