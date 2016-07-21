/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author HuHui
 * @version $Id: CommonTest.java, v 0.1 2016年7月21日 上午10:32:05 HuHui Exp $
 */
public class CommonTest {

    @Test
    public void testUtf8() {
        String str = StandardCharsets.UTF_8.displayName();
        Assert.assertTrue(str.equals("UTF-8"));
    }

}
