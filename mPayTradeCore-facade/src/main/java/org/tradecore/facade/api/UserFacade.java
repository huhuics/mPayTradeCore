/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.facade.api;

import org.tradecore.common.facade.result.Result;
import org.tradecore.facade.dto.PageDTO;
import org.tradecore.facade.dto.UserDTO;

/**
 * 
 * @author HuHui
 * @version $Id: UserFacade.java, v 0.1 2016年5月21日 下午11:30:02 HuHui Exp $
 */
public interface UserFacade {

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Result<UserDTO> selectByPrimaryKey(Long id);

    /**
     * 查询用户，带分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result<PageDTO<UserDTO>> getUsers(int pageNum, int pageSize);

}
