/**
 * Beijing Jiaotong University
 * Copyright (c) 1896-2016 All Rights Reserved.
 */
package org.tradecore.alipay.trade.model;

import org.tradecore.dao.domain.BaseDomain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author HuHui
 * @version $Id: ExtendParams.java, v 0.1 2016年8月18日 下午4:35:21 HuHui Exp $
 */
public class ExtendParams extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 3688203499546269849L;

    /**
     * 使用花呗分期要进行的分期数
     */
    @JSONField(name = "hb_fq_num")
    private String            hbFqNum;

    /**
     * 使用花呗分期需要卖家承担的手续费比例的百分值，传入100代表100%
     */
    @JSONField(name = "hb_fq_seller_percent")
    private String            hbFqSellerPercent;

    /**
     * 系统商编号
    该参数作为系统商返佣数据提取的依据，请填写系统商签约协议的PID
     */
    @JSONField(name = "sys_service_provider_id")
    private String            sysServiceProviderId;

    public String getHbFqNum() {
        return hbFqNum;
    }

    public void setHbFqNum(String hbFqNum) {
        this.hbFqNum = hbFqNum;
    }

    public String getHbFqSellerPercent() {
        return hbFqSellerPercent;
    }

    public void setHbFqSellerPercent(String hbFqSellerPercent) {
        this.hbFqSellerPercent = hbFqSellerPercent;
    }

    public String getSysServiceProviderId() {
        return sysServiceProviderId;
    }

    public void setSysServiceProviderId(String sysServiceProviderId) {
        this.sysServiceProviderId = sysServiceProviderId;
    }

}
