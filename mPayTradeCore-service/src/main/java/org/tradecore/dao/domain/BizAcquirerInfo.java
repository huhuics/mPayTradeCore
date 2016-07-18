package org.tradecore.dao.domain;

import java.util.Date;

public class BizAcquirerInfo extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -8955744965361166952L;

    /**
     *  null, BIZ_ACQUIRER_INFO.ID
     */
    private Short             id;

    /**
     *  null, BIZ_ACQUIRER_INFO.ACQUIRER_ID
     */
    private String            acquirerId;

    /**
     *  null, BIZ_ACQUIRER_INFO.ACQUIRER_NAME
     */
    private String            acquirerName;

    /**
     *  null, BIZ_ACQUIRER_INFO.ACC_NO
     */
    private String            accNo;

    /**
     *  null, BIZ_ACQUIRER_INFO.ACC_NAME
     */
    private String            accName;

    /**
     *  null, BIZ_ACQUIRER_INFO.ACC_BANK_NO
     */
    private String            accBankNo;

    /**
     *  null, BIZ_ACQUIRER_INFO.STATUS
     */
    private String            status;

    /**
     *  null, BIZ_ACQUIRER_INFO.GMT_CREATE
     */
    private Date              gmtCreate;

    /**
     *  null, BIZ_ACQUIRER_INFO.GMT_UPDATE
     */
    private Date              gmtUpdate;

    /**
     *  null, BIZ_ACQUIRER_INFO.CHECK_CERT
     */
    private byte[]            checkCert;

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

    public String getAcquirerName() {
        return acquirerName;
    }

    public void setAcquirerName(String acquirerName) {
        this.acquirerName = acquirerName == null ? null : acquirerName.trim();
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo == null ? null : accNo.trim();
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName == null ? null : accName.trim();
    }

    public String getAccBankNo() {
        return accBankNo;
    }

    public void setAccBankNo(String accBankNo) {
        this.accBankNo = accBankNo == null ? null : accBankNo.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
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

    public byte[] getCheckCert() {
        return checkCert;
    }

    public void setCheckCert(byte[] checkCert) {
        this.checkCert = checkCert;
    }
}