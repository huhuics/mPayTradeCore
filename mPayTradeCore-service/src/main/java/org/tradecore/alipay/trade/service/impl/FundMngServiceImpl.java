package org.tradecore.alipay.trade.service.impl;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tradecore.alipay.enums.ReportReturnCodeEnum;
import org.tradecore.alipay.enums.ReportStateCodeEnum;
import org.tradecore.alipay.trade.constants.ReportConstant;
import org.tradecore.alipay.trade.service.FundMngService;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.dao.BankDetailDAO;
import org.tradecore.dao.BizPayOrgClearDAO;
import org.tradecore.dao.CollectDetailDAO;
import org.tradecore.dao.SysParmDAO;
import org.tradecore.dao.WorkCalendarDAO;
import org.tradecore.dao.domain.BankDetailInfo;
import org.tradecore.dao.domain.BizPayOrgClearInfo;
import org.tradecore.dao.domain.CollectDetailInfo;
import org.tradecore.dao.domain.WorkCalendar;

/**
 * 
 * 类描述： 资金管理平台接口数据查询
 * 创建者： chenwenjing
 * 项目名称： alipay-busi
 * 创建时间： 2016-10-13 上午9:31:42
 * 版本号： v1.0
 */
@Service
public class FundMngServiceImpl implements FundMngService {
    private static Logger              logger          = LoggerFactory.getLogger(FundMngServiceImpl.class);

    private static final String        xmlHead         = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    //查询日期响应报文
    private static final MessageFormat DAY_RESPONSE    = new MessageFormat(ReportConstant.DAYRESPONSEXML);
    //查询报表数据响应报文
    private static final MessageFormat REPORT_RESPONSE = new MessageFormat(ReportConstant.REPORTRESPONSEXML);

    @Resource
    public SysParmDAO                  sysParmDAO;

    @Resource
    private BankDetailDAO              bankDetailDAO;

    @Resource
    private CollectDetailDAO           collectDetailDAO;

    @Resource
    private WorkCalendarDAO            workCalendarDAO;

    @Resource
    private BizPayOrgClearDAO          bizPayOrgClearDAO;

    @Override
    public Map<String, Object> query(HttpServletRequest request) {
        long beginTime = System.currentTimeMillis();
        LogUtil.info(logger,"接收资金管理平台请求时间【" + beginTime + "】");
        Map<String, Object> responseMap = new HashMap<String, Object>();

        //获取请求带过来的参数
        String interfacetype = request.getParameter("interfacetype");//接口类型
        String querytype = request.getParameter("querytype");//查询类型
        String wrkdate = request.getParameter("wrkdate");//工作日
        LogUtil.info(logger,"接收资金管理平台请求参数【interfacetype=" + interfacetype + "】,【querytype=" + querytype + "】,【wrkdate=" + wrkdate + "】");
        //校验三个参数
        String msg = validateParam(interfacetype, querytype, wrkdate);
        if (StringUtils.isNotBlank(msg)) {
            //不为空，校验不通过
            responseMap = getDayResponse(ReportReturnCodeEnum.FAIL.getValue(), msg, msg, "", false);
        } else {
            //为空，校验通过
            if (ReportConstant.QUERYDAY.equals(interfacetype)) {
                //查询工作日
                String workDate = sysParmDAO.getValue("CURRENT_DAY");
                if (null == workDate) {
                    responseMap = getDayResponse(ReportReturnCodeEnum.FAIL.getValue(), ReportReturnCodeEnum.FAIL.getDisplayName(), "获取工作日失败", "", false);
                } else {
                    if (ReportConstant.NEXTDATE.equals(querytype)) {//下一个工作日
                        String returnCode = null;
                        String returnMsg = null;
                        String message = null;
                        boolean isSuccess = true;
                        if (StringUtils.isNotBlank(wrkdate)) {
                            workDate = wrkdate;//（4）如果interfacetype=1，且querytype=12，且wrkdate不为空，则返回wrkdate的下一个工作日；
                        }
                        while (true) {
                            String nextDay;
                            try {
                                nextDay = DateUtil.getTomorrowDateString(workDate);
                                WorkCalendar workCalendar = workCalendarDAO.get(nextDay);
                                if (null == workCalendar) {
                                    workDate = "";
                                    returnCode = ReportReturnCodeEnum.FAIL.getValue();
                                    returnMsg = ReportReturnCodeEnum.FAIL.getDisplayName();
                                    isSuccess = false;
                                    message = "获取下一个工作日失败";
                                    break;
                                }
                                if ("1".equals(workCalendar.getWorkDayType())) {
                                    //工作日
                                    workDate = workCalendar.getDayId();
                                    returnCode = ReportReturnCodeEnum.SUCCESS.getValue();
                                    returnMsg = ReportReturnCodeEnum.SUCCESS.getDisplayName();
                                    message = "获取下一个工作日成功";
                                    break;
                                } else {
                                    workDate = nextDay;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        responseMap = getDayResponse(returnCode, returnMsg, message, workDate, isSuccess);
                    } else {
                        responseMap = getDayResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), "获取工作日成功", workDate, true);
                    }
                }

            } else {
                //查询报表数据

                if (ReportConstant.BANKDATA.equals(querytype)) {
                    //查询付款行数据
                    List<BankDetailInfo> bankDetails = bankDetailDAO.getListByDay(wrkdate);
                    if (null != bankDetails && bankDetails.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        String signl = "|";
                        for (BankDetailInfo bankDetailInfo : bankDetails) {
                            sb.append("\r\n");//换行
                            sb.append(bankDetailInfo.getDay());
                            sb.append(signl);
                            sb.append(bankDetailInfo.getAccBankNo());//行号
                            sb.append(signl);
                            sb.append(bankDetailInfo.getAccNo());//账号
                            sb.append(signl);
                            sb.append(bankDetailInfo.getAccName());//户名
                            sb.append(signl);
                            sb.append(bankDetailInfo.getInMoney() == null ? 0 : bankDetailInfo.getInMoney());//入_交易资金
                            sb.append(signl);
                            sb.append(bankDetailInfo.getOutMoney() == null ? 0 : bankDetailInfo.getOutMoney());//出_交易资金
                            sb.append(signl);
                            sb.append(bankDetailInfo.getInRefund() == null ? 0 : bankDetailInfo.getInRefund());//入_交易资金
                            sb.append(signl);
                            sb.append(bankDetailInfo.getOutRefund() == null ? 0 : bankDetailInfo.getOutRefund());//出_交易资金
                            sb.append(signl);
                            sb.append(bankDetailInfo.getInOther() == null ? 0 : bankDetailInfo.getInOther());//其他收入
                            sb.append(signl);
                            sb.append(bankDetailInfo.getOutOther() == null ? 0 : bankDetailInfo.getOutOther());//其他支出
                            sb.append(signl);
                            sb.append(bankDetailInfo.getInInterest() == null ? 0 : bankDetailInfo.getInInterest());//利息收入
                        }
                        sb.append("\r\n");//换行
                        if (bankDetails.size() == 1) {
                            sb.toString().replaceAll("\r\n", "");
                        }
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.SUCCESS.getValue(),
                            ReportStateCodeEnum.SUCCESS.getDisplayName(), sb.toString(), ReportStateCodeEnum.SUCCESS.getDisplayName(), true);
                    } else {
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.NOREPORT.getValue(),
                            ReportStateCodeEnum.NOREPORT.getDisplayName(), "", ReportStateCodeEnum.NOREPORT.getDisplayName(), false);
                    }
                } else if (ReportConstant.COLLECTDATA.equals(querytype)) {
                    //查询归集行数据
                    List<CollectDetailInfo> collectDetails = collectDetailDAO.getListInDay(wrkdate);
                    if (null != collectDetails && collectDetails.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        String signl = "|";
                        for (CollectDetailInfo collectDetailInfo : collectDetails) {
                            sb.append("\r\n");//换行
                            sb.append(collectDetailInfo.getDay());
                            sb.append(signl);
                            sb.append(collectDetailInfo.getAccBankNo());//行号
                            sb.append(signl);
                            sb.append(collectDetailInfo.getAccNo());//账号
                            sb.append(signl);
                            sb.append(collectDetailInfo.getAccName());//户名
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInMechMoney() == null ? 0 : collectDetailInfo.getInMechMoney());//入_商户资金
                            sb.append(signl);
                            sb.append(collectDetailInfo.getOutMechMoney() == null ? 0 : collectDetailInfo.getOutMechMoney());//出_商户资金（清算金额）
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInFee() == null ? 0 : collectDetailInfo.getInFee());//应收手续费
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInFee() == null ? 0 : collectDetailInfo.getInFee());//实收手续费
                            sb.append(signl);
                            sb.append(collectDetailInfo.getOutRefund() == null ? 0 : collectDetailInfo.getOutRefund());//出_退款资金（银行退款金额）
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInInterest() == null ? 0 : collectDetailInfo.getInInterest());//利息收入
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInBail() == null ? 0 : collectDetailInfo.getInBail());//当天入账的备付金
                            sb.append(signl);
                            sb.append(collectDetailInfo.getInOther() == null ? 0 : collectDetailInfo.getInOther());//入_其他支出
                            sb.append(signl);
                            sb.append(collectDetailInfo.getOutOther() == null ? 0 : collectDetailInfo.getOutOther());//出_其他收入
                        }
                        sb.append("\r\n");//换行
                        if (collectDetails.size() == 1) {
                            sb.toString().replaceAll("\r\n", "");
                        }
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.SUCCESS.getValue(),
                            ReportStateCodeEnum.SUCCESS.getDisplayName(), sb.toString(), ReportStateCodeEnum.SUCCESS.getDisplayName(), true);
                    } else {
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.NOREPORT.getValue(),
                            ReportStateCodeEnum.NOREPORT.getDisplayName(), "", ReportStateCodeEnum.NOREPORT.getDisplayName(), false);
                    }
                } else {
                    //查询转清机构清算数据
                    List<BizPayOrgClearInfo> payOrgClearDetails = bizPayOrgClearDAO.getListByDay(wrkdate);
                    //查询支付宝APPID
                    String pId = sysParmDAO.getValue("PID");
                    if (null != payOrgClearDetails && payOrgClearDetails.size() > 0) {
                        StringBuffer sb = new StringBuffer();
                        String signl = "|";
                        for (BizPayOrgClearInfo bizPayOrgClearInfo : payOrgClearDetails) {
                            sb.append("\r\n");//换行
                            sb.append(bizPayOrgClearInfo.getDay());//对账日期
                            sb.append(signl);
                            sb.append(pId);//转清机构编号
                            sb.append(signl);
                            sb.append(bizPayOrgClearInfo.getSucMoney() == null ? 0 : bizPayOrgClearInfo.getSucMoney());//交易资金
                            sb.append(signl);
                            sb.append(bizPayOrgClearInfo.getRefundMoney() == null ? 0 : bizPayOrgClearInfo.getRefundMoney());//退款资金
                            sb.append(signl);
                            sb.append(bizPayOrgClearInfo.getAllFee() == null ? 0 : bizPayOrgClearInfo.getAllFee());//手续费
                        }
                        sb.append("\r\n");//换行
                        if (payOrgClearDetails.size() == 1) {
                            sb.toString().replaceAll("\r\n", "");
                        }
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.SUCCESS.getValue(),
                            ReportStateCodeEnum.SUCCESS.getDisplayName(), sb.toString(), ReportStateCodeEnum.SUCCESS.getDisplayName(), true);
                    } else {
                        responseMap = getReportResponse(ReportReturnCodeEnum.SUCCESS.getValue(), ReportReturnCodeEnum.SUCCESS.getDisplayName(), ReportStateCodeEnum.NOREPORT.getValue(),
                            ReportStateCodeEnum.NOREPORT.getDisplayName(), "", ReportStateCodeEnum.NOREPORT.getDisplayName(), false);
                    }
                }

            }
        }
        return responseMap;
    }

    /**
     * 校验请求参数
     * @param interfacetype
     * @param querytype
     * @param wrkdate
     * @return
     */

    private String validateParam(String interfacetype, String querytype, String wrkdate) {
        String msg = "";
        if (!StringUtils.isNotBlank(interfacetype))
            msg = "参数有误：参数接口类型【interfacetype】不能为空";
        if (!StringUtils.isNotBlank(querytype))
            msg = "参数有误：参数查询类型【querytype】不能为空";
        if (!(ReportConstant.QUERYDAY.equals(interfacetype) || ReportConstant.QUERYREPORT.equals(interfacetype)))
            msg = "参数有误：参数接口类型【interfacetype】值只能为" + ReportConstant.QUERYDAY + "或" + ReportConstant.QUERYREPORT;
        if (!(ReportConstant.CURRENTDATE.equals(querytype) || ReportConstant.NEXTDATE.equals(querytype) || ReportConstant.BANKDATA.equals(querytype) || ReportConstant.COLLECTDATA.equals(querytype) || ReportConstant.PAYORGDATA
            .equals(querytype)))
            msg = "参数有误：参数查询类型【querytype】值只能为" + ReportConstant.CURRENTDATE + "或" + ReportConstant.NEXTDATE + "或" + ReportConstant.BANKDATA + "或" + ReportConstant.COLLECTDATA + "或"
                  + ReportConstant.PAYORGDATA;
        if (ReportConstant.QUERYREPORT.equals(interfacetype) && !StringUtils.isNotBlank(wrkdate))
            msg = "参数有误：如果参数接口类型【interfacetype=" + ReportConstant.QUERYREPORT + "】则参数工作日【wrkdate】不为空";
        return msg;
    }

    private Map<String, Object> getDayResponse(String returnCode, String returnMsg, String desc, String day, boolean result) {
        //应答内容
        String responseXml = DAY_RESPONSE.format(new Object[] { returnCode, returnMsg, day });
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("responseXml", xmlHead + responseXml);
        responseMap.put("desc", desc);
        responseMap.put("result", result);
        return responseMap;
    }

    private Map<String, Object> getReportResponse(String returnCode, String returnMsg, String statusCode, String statusDesc, String datas, String desc, boolean result) {
        //应答内容
        String responseXml = REPORT_RESPONSE.format(new Object[] { returnCode, returnMsg, statusCode, statusDesc, datas });
        Map<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put("responseXml", xmlHead + responseXml);
        responseMap.put("desc", desc);
        responseMap.put("result", result);
        return responseMap;
    }

}
