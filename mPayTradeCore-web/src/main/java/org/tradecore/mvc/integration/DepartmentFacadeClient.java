/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.integration;

import org.tradecore.common.facade.result.Result;
import org.tradecore.facade.dto.DepartmentDTO;

/**
 * 
 * @author HuHui
 * @version $Id: DepartmentFacadeClient.java, v 0.1 2016年6月7日 下午8:46:51 HuHui Exp $
 */
public interface DepartmentFacadeClient {

    Result<DepartmentDTO> getById(Long id);

}
