/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.service.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tradecore.common.util.DateUtil;
import org.tradecore.common.util.LogUtil;
import org.tradecore.common.util.UUIDUtil;

import com.alibaba.fastjson.JSON;

/**
 * 
 * @author HuHui
 * @version $Id: UUIDUtilTest.java, v 0.1 2016年7月9日 上午10:29:47 HuHui Exp $
 */
public class UUIDUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(UUIDUtilTest.class);

    @Test
    public void testUUID() {
        String uuid = UUIDUtil.geneId();
        LogUtil.info(logger, "uuid={0}", uuid);
    }

    @Test
    public void testCancelTime() {
        boolean b = checkCancelTime(new Date());
        LogUtil.info(logger, "b={0}", b);
    }

    private boolean checkCancelTime(Date gmtCreate) {

        //获取订单创建时间的yyyyMMdd格式
        String formatedStr = DateUtil.format(gmtCreate, DateUtil.shortFormat);

        //订单撤销截止时间
        String endDateStr = formatedStr + "235959";

        Date endDate = DateUtil.parseDateLongFormat(endDateStr);

        return endDate.after(new Date());
    }

    @Test
    public void testJson() {
        String out_request_no = "123";
        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("out_request_no", out_request_no);
        String json = JSON.toJSONString(paraMap);

        LogUtil.info(logger, "json={0}", json);
    }

}
