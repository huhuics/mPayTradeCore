package org.tradecore.alipay.trade.constants;

/**
 * 
 * 类描述： 资金管理平台接口常量
 * 创建者： chenwenjing
 * 项目名称： alipay-busi
 * 创建时间： 2016-10-13 上午8:44:05
 * 版本号： v1.0
 */
public class ReportConstant {
    /** 接口调用字符集默认为 UTF-8，不能使用其它字符集 */
    public static final String CHARSET           = "UTF-8";
    /**查询工作日*/
    public static final String QUERYDAY          = "1";
    /**查询报表数据*/
    public static final String QUERYREPORT       = "2";
    /**
    11：代表返回当前系统工作日
    */
    public static final String CURRENTDATE       = "11";
    /**
    12：代表返回当前系统下一个工作日
    */
    public static final String NEXTDATE          = "12";
    /**
    21：返回指定日期的付款行报表数据
    */
    public static final String BANKDATA          = "21";
    /**
    22：返回指定日期的归集行报表数据
    */
    public static final String COLLECTDATA       = "22";
    /**
    23：返回指定日期的转清机构报表数据
    */
    public static final String PAYORGDATA        = "23";
    /**查询工作日响应报文*/
    public static final String DAYRESPONSEXML    = "<xml><RespInfo><GrpHdr><RespCode><![CDATA[{0}]]></RespCode><RespDesc><![CDATA[{1}]]></RespDesc><SysDate><![CDATA[{2}]]></SysDate></GrpHdr></RespInfo></xml>";
    /**查询报表数据响应报文*/
    public static final String REPORTRESPONSEXML = "<xml><RespInfo><GrpHdr><RespCode><![CDATA[{0}]]></RespCode><RespDesc><![CDATA[{1}]]></RespDesc><StatusCode><![CDATA[{2}]]></StatusCode><StatusDesc><![CDATA[{3}]]></StatusDesc><Datas><![CDATA[{4}]]></Datas></GrpHdr></RespInfo></xml>";
}
