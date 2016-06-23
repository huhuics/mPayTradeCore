/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.biz.impl.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.tradecore.common.facade.result.Result;
import org.tradecore.facade.api.DepartmentFacade;
import org.tradecore.facade.dto.DepartmentDTO;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentFacadeTest.java, v 0.1 2016年6月7日 下午8:25:16 HuHui Exp $
 */
public class DepartmentFacadeTest extends BaseTest {

    @Resource
    private DepartmentFacade departmentFacade;

    @Test
    public void testGetById() {
        Assert.assertNotNull(departmentFacade);
        Result<DepartmentDTO> ret = departmentFacade.getById(1L);
        Assert.assertTrue(ret.isSuccess());
        Assert.assertNotNull(ret.getResultObj());
        Assert.assertEquals(ret.getResultObj().getName(), "Java技术部");
    }

}
