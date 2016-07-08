package org.tradecore.dao.domain;

import java.util.Date;

import org.tradecore.common.util.Money;

public class BizAcquirerCashInfo extends BaseDomain {

    /**  */
    private static final long serialVersionUID = 3430310692961352940L;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.ID
     */
    private Short             id;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.acquirer_id
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.create_date
     */
    private String            createDate;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.amount
     */
    private Money             amount;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.gmt_create
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ACQUIRER_CASH_INFO.gmt_update
     */
    private Date              gmtUpdate;

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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
}