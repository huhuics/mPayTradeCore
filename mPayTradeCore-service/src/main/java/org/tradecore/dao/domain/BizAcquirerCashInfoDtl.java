package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAcquirerCashInfoDtl extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 2439945755920889889L;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.ID
     */
    private Short             id;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.acquirer_id
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.amount
     */
    private Money             amount;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.out_trade_no
     */
    private String            outTradeNo;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.out_request_no
     */
    private String            outRequestNo;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.success_date
     */
    private String            successDate;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO_DTL.gmt_create
     */
    private Date              gmtCreate;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(String acquirerId) {
        this.acquirerId = acquirerId == null ? null : acquirerId.trim();
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo == null ? null : outRequestNo.trim();
    }

    public String getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(String successDate) {
        this.successDate = successDate == null ? null : successDate.trim();
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
}