/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.DepartmentDAO;
import org.tradecore.dao.domain.Department;
import org.tradecore.service.DepartmentService;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentServiceImpl.java, v 0.1 2016年6月7日 下午8:04:27 HuHui Exp $
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentServiceImpl.class);

    /** dao */
    @Resource
    private DepartmentDAO       departmentDao;

    @Override
    public Department selectByPrimaryKey(Long id) {
        LogUtil.info(logger, "收到查询Department请求,id={0}", id);
        AssertUtil.assertNotNull(id, "查询参数id不能为空");
        Department department = departmentDao.selectByPrimaryKey(id);

        return department;
    }

}
