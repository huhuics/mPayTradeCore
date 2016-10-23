package org.tradecore.mvc.controller;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tradecore.alipay.trade.constants.ReportConstant;
import org.tradecore.alipay.trade.service.FundMngService;
import org.tradecore.common.util.LogUtil;

/**
 * 
 * 类描述： 资金管理平台接口
 * 创建者： chenwenjing
 * 项目名称： mPayTradeCore-service
 * 创建时间： 2016-10-17 下午3:29:40
 * 版本号： v1.0
 */
@Controller
@RequestMapping("/fundMng")
public class FundMngController extends AbstractBizController {

    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(FundMngController.class);

    @Resource
    private FundMngService      fundMngService;

    @RequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream sos = null;

        try {
            sos = response.getOutputStream();
        } catch (IOException e) {
            LogUtil.info(logger, "资金管理平台查询接口读取流失败");
        }
        //进行数据处理
        Map<String, Object> responseMap = fundMngService.query(request);

        try {
            String resultXml = (String) responseMap.get("responseXml");
            boolean result = (boolean) responseMap.get("result");
            String desc = (String) responseMap.get("desc");
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(sos, ReportConstant.CHARSET));
            writer.write(resultXml);
            writer.flush();
            if (result) {
                //成功
                LogUtil.info(logger, "返回资金管理平台查询响应,queryResponse={0}", resultXml);
            } else {
                //失败
                LogUtil.info(logger, "[" + desc + "],写出失败响应[" + resultXml + "]");
            }
        } catch (IOException e) {
            LogUtil.error(e,logger, "写出响应失败");
        }

    }

}
