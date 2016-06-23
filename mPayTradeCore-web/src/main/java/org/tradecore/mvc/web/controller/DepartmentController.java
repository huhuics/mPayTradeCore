/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.web.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.tradecore.common.facade.result.Result;
import org.tradecore.common.util.LogUtil;
import org.tradecore.facade.dto.DepartmentDTO;
import org.tradecore.mvc.integration.DepartmentFacadeClient;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentController.java, v 0.1 2016年6月7日 下午9:10:37 HuHui Exp $
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {

    private static final Logger    logger = LoggerFactory.getLogger(DepartmentController.class);

    @Resource
    private DepartmentFacadeClient departmentFacadeClient;

    @RequestMapping(value = "/showDept", method = RequestMethod.GET)
    public String toShowDepartment(WebRequest request, ModelMap map) {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Result<DepartmentDTO> ret = departmentFacadeClient.getById(id);

            map.put("department", ret.getResultObj());
        } catch (Exception e) {
            LogUtil.error(e, logger, "查询部门信息异常");
        }
        return "showDept";
    }

}
