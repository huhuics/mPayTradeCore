/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.mvc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author HuHui
 * @version $Id: HelloController.java, v 0.1 2016年5月22日 上午11:34:48 HuHui Exp $
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /*  @Resource
      private UserService         userService;

      @RequestMapping(value = "/showUser", method = RequestMethod.GET)
      public String toShowUser(WebRequest request, ModelMap map) {
          try {
              Long id = Long.parseLong(request.getParameter("id"));
              User user = userService.selectByPrimaryKey(id);

              map.put("user", user);
          } catch (Exception e) {
              logger.error("查询用户信息异常", e);
          }
          return "showUser";
      }*/

}
