/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.biz.impl.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.tradecore.common.facade.result.Result;
import org.tradecore.facade.api.UserFacade;
import org.tradecore.facade.dto.PageDTO;
import org.tradecore.facade.dto.UserDTO;

/**
 * 
 * @author HuHui
 * @version $Id: UserFacadeImplTest.java, v 0.1 2016年5月22日 上午1:55:00 HuHui Exp $
 */
public class UserFacadeImplTest extends BaseTest {

    @Resource
    private UserFacade userFacade;

    @Test
    public void testSelectByPrimaryKey() {
        Assert.assertNotNull(userFacade);
        Result<UserDTO> userRet = userFacade.selectByPrimaryKey(1L);
        Assert.assertNotNull(userRet);
        Assert.assertTrue(userRet.isSuccess());
        Assert.assertNotNull(userRet.getResultObj());
    }

    @Test
    public void testQueryUsersByPage() {
        int pageNum = 1;
        int pageSize = 10;
        Result<PageDTO<UserDTO>> userDTORet = userFacade.getUsers(pageNum, pageSize);
        Assert.assertNotNull(userDTORet);
        PageDTO<UserDTO> userDTOs = userDTORet.getResultObj();
        Assert.assertNotNull(userDTOs.getData());
        Assert.assertNotNull(userDTOs.getPageInfo());
        Assert.assertEquals(userDTOs.getData().size(), 10);
        Assert.assertEquals(userDTOs.getPageInfo().getPageNum(), pageNum);
        Assert.assertEquals(userDTOs.getPageInfo().getPageSize(), pageSize);
    }
}
