/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.biz.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.biz.impl.convertor.DepartmentConvertor;
import org.tradecore.common.facade.result.Result;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Exception.MechException;
import org.tradecore.dao.domain.Department;
import org.tradecore.facade.api.DepartmentFacade;
import org.tradecore.facade.dto.DepartmentDTO;
import org.tradecore.service.DepartmentService;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentFacadeImpl.java, v 0.1 2016年6月7日 下午8:14:36 HuHui Exp $
 */
@Service("departmentFacade")
public class DepartmentFacadeImpl implements DepartmentFacade {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentFacadeImpl.class);

    @Resource
    private DepartmentService   departmentService;

    @Override
    public Result<DepartmentDTO> getById(Long id) {
        LogUtil.info(logger, "收到查询参数,id={0}", id);

        Result<DepartmentDTO> result = new Result<DepartmentDTO>();
        try {
            AssertUtil.assertNotNull(id, "查询参数id为空");
            Department department = null;
            department = departmentService.selectByPrimaryKey(id);

            DepartmentDTO departmentDTO = DepartmentConvertor.convert2DepartmentDTO(department);
            result.setResultObj(departmentDTO);
            result.setSuccess(true);
        } catch (Exception e) {
            LogUtil.error(e, logger, "查询Department异常,id={0}", id);
            throw new MechException("查询Department异常", e);
        }
        return result;
    }

}
