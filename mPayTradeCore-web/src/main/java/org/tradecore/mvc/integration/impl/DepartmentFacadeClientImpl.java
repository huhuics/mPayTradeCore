/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.integration.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.common.facade.result.Result;
import org.tradecore.common.util.LogUtil;
import org.tradecore.facade.api.DepartmentFacade;
import org.tradecore.facade.dto.DepartmentDTO;
import org.tradecore.mvc.integration.DepartmentFacadeClient;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentFacadeClientImpl.java, v 0.1 2016年6月7日 下午8:56:11 HuHui Exp $
 */
@Service
public class DepartmentFacadeClientImpl implements DepartmentFacadeClient {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentFacadeClientImpl.class);

    @Resource
    private DepartmentFacade    departmentFacade;

    @Override
    public Result<DepartmentDTO> getById(Long id) {
        LogUtil.info(logger, "收到查询参数,id={0}", id);
        Result<DepartmentDTO> result = new Result<DepartmentDTO>();
        try {
            result = departmentFacade.getById(id);
        } catch (Exception e) {
            LogUtil.error(e, logger, "查询Department异常,id={0}", id);
        }
        return result;
    }
}
