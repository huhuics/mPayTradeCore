/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.config;

import java.util.ResourceBundle;

import org.springframework.context.annotation.PropertySource;

/**
 * 加载src/main/resources目录下的配置文件
 * @author HuHui
 * @version $Id: Resources.java, v 0.1 2016年7月24日 下午2:48:32 HuHui Exp $
 */
@PropertySource(value = { "classpath:config/notifyurl.properties", "classpath:config/zfbinfo.properties" })
public final class Resources {

    /** notify_url配置 */
    public static final ResourceBundle NOTIFY_URL = ResourceBundle.getBundle("config/notifyurl");

}
