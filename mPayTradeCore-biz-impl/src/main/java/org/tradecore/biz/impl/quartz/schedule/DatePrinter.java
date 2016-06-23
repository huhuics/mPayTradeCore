/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.biz.impl.quartz.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

/**
 * 定时任务时间打印器
 * @author HuHui
 * @version $Id: DatePrinter.java, v 0.1 2016年6月1日 下午11:34:02 HuHui Exp $
 */
public class DatePrinter {

    private static final Logger logger = LoggerFactory.getLogger(DatePrinter.class);

    /**
     * 定时打印时间
     */
    public void execute() {
        LogUtil.info(logger, "当前时间:{0}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
