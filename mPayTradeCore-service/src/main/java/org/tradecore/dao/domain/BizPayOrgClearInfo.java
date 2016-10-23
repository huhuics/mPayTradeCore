package org.tradecore.dao.domain;


public class BizPayOrgClearInfo extends BaseDomain {
    private Long              id;

    private String            day;

    private String            tradeState;

    private Long              sucNum;

    private Long              sucMoney;

    private Long              refundNum;

    private Long              refundMoney;

    private Long              receiptMoney;

    private Long              clearMoney;

    private Long              allFee;

    private String            clearDay;

    private String            dealing;

    private Integer           failCount;

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

    public String getTradeState() {
        return tradeState;
    }

    public void setTradeState(String tradeState) {
        this.tradeState = tradeState == null ? null : tradeState.trim();
    }

    public Long getSucNum() {
        return sucNum;
    }

    public void setSucNum(Long sucNum) {
        this.sucNum = sucNum;
    }

    public Long getSucMoney() {
        return sucMoney;
    }

    public void setSucMoney(Long sucMoney) {
        this.sucMoney = sucMoney;
    }

    public Long getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(Long refundNum) {
        this.refundNum = refundNum;
    }

    public Long getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(Long refundMoney) {
        this.refundMoney = refundMoney;
    }

    public Long getReceiptMoney() {
        return receiptMoney;
    }

    public void setReceiptMoney(Long receiptMoney) {
        this.receiptMoney = receiptMoney;
    }

    public Long getClearMoney() {
        return clearMoney;
    }

    public void setClearMoney(Long clearMoney) {
        this.clearMoney = clearMoney;
    }

    public Long getAllFee() {
        return allFee;
    }

    public void setAllFee(Long allFee) {
        this.allFee = allFee;
    }

    public String getClearDay() {
        return clearDay;
    }

    public void setClearDay(String clearDay) {
        this.clearDay = clearDay == null ? null : clearDay.trim();
    }

    public String getDealing() {
        return dealing;
    }

    public void setDealing(String dealing) {
        this.dealing = dealing == null ? null : dealing.trim();
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

}