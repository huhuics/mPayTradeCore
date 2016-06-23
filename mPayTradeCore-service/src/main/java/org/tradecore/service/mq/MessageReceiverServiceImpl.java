/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;

/**
 * 
 * @author HuHui
 * @version $Id: MessageReceiverServiceImpl.java, v 0.1 2016年5月23日 下午11:02:25 HuHui Exp $
 */
public class MessageReceiverServiceImpl implements MessageReceiverService {

    private static final Logger logger = LoggerFactory.getLogger(MessageReceiverServiceImpl.class);

    @Override
    public void getMessage(Object obj) {
        LogUtil.info(logger, "mPayTradeCore-service接收到消息,消息体obj={0}", obj);
    }

}
