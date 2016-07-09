/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.UUIDUtil;

/**
 * 
 * @author HuHui
 * @version $Id: UUIDUtilTest.java, v 0.1 2016年7月9日 上午10:29:47 HuHui Exp $
 */
public class UUIDUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(UUIDUtilTest.class);

    @Test
    public void testUUID() {
        String uuid = UUIDUtil.geneId();
        LogUtil.info(logger, "uuid={0}", uuid);
    }

}
