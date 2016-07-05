/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.ftp;

import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * SFtp上传、下载文件监控
 * @author HuHui
 * @version $Id: FileProgressMonitor.java, v 0.1 2016年7月4日 上午12:12:18 HuHui Exp $
 */
public class FileProgressMonitor implements SftpProgressMonitor {

    private final Logger  logger      = LoggerFactory.getLogger(FileProgressMonitor.class);

    /** 记录已传输的数据总大小 */
    private long          transfered;

    /** 记录文件总大小 */
    private long          fileSize;

    /** 打印日志时间间隔 */
    private int           minInterval = 200;

    /** 开始时间 */
    private long          start;

    private DecimalFormat df          = new DecimalFormat("#.##");

    private long          preTime;

    /** 
     * @see com.jcraft.jsch.SftpProgressMonitor#init(int, java.lang.String, java.lang.String, long)
     */
    @Override
    public void init(int op, String src, String dest, long max) {
        this.fileSize = max;
        LogUtil.info(logger, "Transferring begin.");
        start = System.currentTimeMillis();
    }

    /** 
     * @see com.jcraft.jsch.SftpProgressMonitor#count(long)
     */
    @Override
    public boolean count(long count) {
        if (fileSize != 0 && transfered == 0) {
            LogUtil.info(logger, "Transferring progress message: {0}%", df.format(0));
            preTime = System.currentTimeMillis();
        }

        transfered += count;
        if (fileSize != 0) {
            long interval = System.currentTimeMillis() - preTime;
            if (transfered == fileSize || interval > minInterval) {
                preTime = System.currentTimeMillis();
                double d = ((double) transfered * 100) / (double) fileSize;
                LogUtil.info(logger, "Transferring progress message: {0}%", df.format(d));
            }
        } else {
            LogUtil.info(logger, "Transferring progress message:{0}", transfered);
        }
        return true;
    }

    /** 
     * @see com.jcraft.jsch.SftpProgressMonitor#end()
     */
    @Override
    public void end() {
        LogUtil.info(logger, "Transferring end. used time: {0}ms", System.currentTimeMillis() - start);
    }

}
