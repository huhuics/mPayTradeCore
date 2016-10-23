package org.tradecore.trade.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.HttpUtil;
import org.tradecore.common.util.LogUtil;

public class FundMngTest {
    /** 日志 */
    private static final Logger logger = LoggerFactory.getLogger(FundMngTest.class);

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testQuery() throws Exception {

        //组装参数
        String url = "http://127.0.0.1:8088/mPay/fundMng/query";
        Map<String,String> map = new HashMap<String,String>();
        map.put("interfacetype", "1");//1-工作日查询 2-报表查询
        map.put("querytype", "12");//11-当前工作日查询 12-下一工作日查询 21-付款机构报表 22-归集行报表 23-转清机构清算报表
        map.put("wrkdate", "20160827");//工作日
        
        AcquirerPayRequest acqPayRequest = new AcquirerPayRequest();
        //借用已有方法构造NameValuePair
        List<NameValuePair> paraList = acqPayRequest.buildPostParaList(map);

        String responseStr = HttpUtil.httpClientPost(url, paraList);
        
		System.out.println("接收报文：【" + responseStr + "】");
		
		LogUtil.info(logger, "响应报文成功！responseXml="+responseStr);

    }
    
}
