package org.tradecore.dao.domain;


public class BankDetailInfo extends BaseDomain {
    private Long              id;

    private String            day;

    private Long              beginningBalance;

    private Long              endingBalance;

    private Long              inMoney;

    private Long              inRefund;

    private Long              inInterest;

    private Long              inOther;

    private Long              inTotal;

    private Long              outMoney;

    private Long              outRefund;

    private Long              outOther;

    private Long              outAccountFee;

    private Long              outTotal;

    private String            accNo;

    private String            accName;

    private String            accBankNo;

    private String            accBankCode;

    private String            accBankName;

    private String            orgId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day == null ? null : day.trim();
    }

    public Long getBeginningBalance() {
        return beginningBalance;
    }

    public void setBeginningBalance(Long beginningBalance) {
        this.beginningBalance = beginningBalance;
    }

    public Long getEndingBalance() {
        return endingBalance;
    }

    public void setEndingBalance(Long endingBalance) {
        this.endingBalance = endingBalance;
    }

    public Long getInMoney() {
        return inMoney;
    }

    public void setInMoney(Long inMoney) {
        this.inMoney = inMoney;
    }

    public Long getInRefund() {
        return inRefund;
    }

    public void setInRefund(Long inRefund) {
        this.inRefund = inRefund;
    }

    public Long getInInterest() {
        return inInterest;
    }

    public void setInInterest(Long inInterest) {
        this.inInterest = inInterest;
    }

    public Long getInOther() {
        return inOther;
    }

    public void setInOther(Long inOther) {
        this.inOther = inOther;
    }

    public Long getInTotal() {
        return inTotal;
    }

    public void setInTotal(Long inTotal) {
        this.inTotal = inTotal;
    }

    public Long getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(Long outMoney) {
        this.outMoney = outMoney;
    }

    public Long getOutRefund() {
        return outRefund;
    }

    public void setOutRefund(Long outRefund) {
        this.outRefund = outRefund;
    }

    public Long getOutOther() {
        return outOther;
    }

    public void setOutOther(Long outOther) {
        this.outOther = outOther;
    }

    public Long getOutAccountFee() {
        return outAccountFee;
    }

    public void setOutAccountFee(Long outAccountFee) {
        this.outAccountFee = outAccountFee;
    }

    public Long getOutTotal() {
        return outTotal;
    }

    public void setOutTotal(Long outTotal) {
        this.outTotal = outTotal;
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

    public String getAccBankCode() {
        return accBankCode;
    }

    public void setAccBankCode(String accBankCode) {
        this.accBankCode = accBankCode == null ? null : accBankCode.trim();
    }

    public String getAccBankName() {
        return accBankName;
    }

    public void setAccBankName(String accBankName) {
        this.accBankName = accBankName == null ? null : accBankName.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
}