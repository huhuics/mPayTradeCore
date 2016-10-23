package org.tradecore.alipay.trade.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * 类描述：  资金管理平台接口数据查询
 * 创建者： chenwenjing
 * 项目名称： alipay-busi
 * 创建时间： 2016-10-13 上午9:31:35
 * 版本号： v1.0
 */
public interface FundMngService {
    /**
     * 
     * 方法描述 :资金管理平台数据查询接口  
     * @param request
     * @param response
     * @return Map<String,Object>
     */
    Map<String, Object> query(HttpServletRequest request);

}
