/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.common.ftp;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFtp上传下载<br>
 * 使用该工具类完成文件上传或下载，一定要调用disconnect()方法关闭连接，释放资源
 * @author HuHui
 * @version $Id: SftpClient.java, v 0.1 2016年7月3日 下午11:44:04 HuHui Exp $
 */
public class SftpClient {

    private Logger              logger  = LoggerFactory.getLogger(SftpClient.class);

    private Session             session = null;

    private ChannelSftp         channel = null;

    private static final String TYPE    = "sftp";

    private SftpClient() {

    }

    public static SftpClient connect(String host, int port, String username, String password, int timeout, int aliveMax) {
        return new SftpClient().init(host, port, username, password, timeout, aliveMax);
    }

    public SftpClient init(String host, int port, String username, String password, int timeout, int aliveMax) {
        try {
            Properties config = new Properties();
            JSch jsch = new JSch();//创建一个JSch对象
            session = jsch.getSession(username, host, port);//获取一个Session对象
            if (password != null) {
                session.setPassword(password);
            }

            config.put("userauth.gssapi-with-mic", "no");
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.setTimeout(timeout);
            session.setServerAliveCountMax(aliveMax);
            session.connect();//通过Session建立连接

            channel = (ChannelSftp) session.openChannel(TYPE);//打开sftp通道
            channel.connect();//建立SFTP通道的连接

            LogUtil.info(logger, "SSH Channel Connected");

        } catch (JSchException e) {
            LogUtil.error(e, logger, "SFTP初始化出错!");
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
        LogUtil.info(logger, "SSH Channel disconnected.");
    }

    /**
     * 发送文件
     * @param src 文件源地址
     * @param dst 文件目的地址
     */
    public void put(String src, String dst) {
        try {
            channel.put(src, dst, new FileProgressMonitor());
        } catch (SftpException e) {
            LogUtil.error(logger, "发送文件失败!");
            throw new RuntimeException(e);
        }
    }

    public void get(String src, String dst) {
        try {
            channel.get(src, dst, new FileProgressMonitor());
        } catch (Exception e) {
            LogUtil.error(logger, "获取文件失败!");
            throw new RuntimeException(e);
        }
    }

}
