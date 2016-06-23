package org.tradecore.biz.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.biz.impl.convertor.UserConvertor;
import org.tradecore.common.facade.result.PageList;
import org.tradecore.common.facade.result.Result;
import org.tradecore.common.util.AssertUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.Exception.MechException;
import org.tradecore.dao.domain.User;
import org.tradecore.facade.api.UserFacade;
import org.tradecore.facade.dto.PageDTO;
import org.tradecore.facade.dto.UserDTO;
import org.tradecore.service.UserService;

/**
 * UserFacade的实现类
 * @author HuHui
 * @version $Id: UserFacadeImpl.java, v 0.1 2016年5月22日 上午1:03:00 HuHui Exp $
 */
@Service("userFacade")
@SuppressWarnings("unchecked")
public class UserFacadeImpl implements UserFacade {

    /** logger */
    private static final Logger logger = LoggerFactory.getLogger(UserFacadeImpl.class);

    @Resource
    private UserService         userService;

    @Override
    public Result<UserDTO> selectByPrimaryKey(Long id) {
        LogUtil.info(logger, "收到查询参数:{0}", id);
        Result<UserDTO> result = new Result<UserDTO>();
        try {
            AssertUtil.assertNotNull(id, "查询参数id为空");
            User user = userService.selectByPrimaryKey(id);
            UserDTO userDTO = UserConvertor.convert2UserDTO(user);
            result.setResultObj(userDTO);
            result.setSuccess(true);
        } catch (Exception e) {
            LogUtil.error(e, logger, "查询用户失败, id={0}", id);
            throw new MechException("查询用户失败", e);
        }

        return result;
    }

    @Override
    public Result<PageDTO<UserDTO>> getUsers(int pageNum, int pageSize) {
        LogUtil.info(logger, "收到查询参数, pageNum={0}, pageSize={1}", pageNum, pageSize);
        Result<PageDTO<UserDTO>> result = new Result<PageDTO<UserDTO>>();
        try {
            AssertUtil.assertNotNull(pageNum, "查询参数pageNum为空");
            AssertUtil.assertNotNull(pageSize, "查询参数pageSize为空");

            PageDTO<UserDTO> userDTOs = new PageDTO<UserDTO>();

            PageList<User> users = userService.getUsers(pageNum, pageSize);

            userDTOs.setPageInfo(users.getPageInfo());
            userDTOs.setData(UserConvertor.convert2UserDTOs(users.getData()));

            result.setResultObj(userDTOs);
            result.setSuccess(true);

        } catch (Exception e) {
            LogUtil.error(e, logger, "查询用户失败, pageNum={0}, pageSize={1}", pageNum, pageSize);
            throw new MechException("查询用户失败", e);
        }

        return result;
    }

}
