/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.biz.impl.convertor;

import org.tradecore.dao.domain.Department;
import org.tradecore.facade.dto.DepartmentDTO;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentConvertor.java, v 0.1 2016年6月7日 下午8:19:29 HuHui Exp $
 */
public class DepartmentConvertor {

    public static DepartmentDTO convert2DepartmentDTO(Department department) {
        if (department == null) {
            return null;
        }

        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setId(department.getId());
        departmentDTO.setName(department.getName());

        return departmentDTO;
    }

}
