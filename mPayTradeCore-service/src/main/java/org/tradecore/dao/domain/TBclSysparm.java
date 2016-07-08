package org.tradecore.dao.domain;

import java.util.Date;

public class TBclSysparm extends BaseDomain {

    /**  */
    private static final long serialVersionUID = -8045267983560988750L;

    /**
     *  null, T_BCL_SYSPARM.ID
     */
    private Short             id;

    /**
     *  null, T_BCL_SYSPARM.PARM_CODE
     */
    private String            parmCode;

    /**
     *  null, T_BCL_SYSPARM.PARM_NAME
     */
    private String            parmName;

    /**
     *  null, T_BCL_SYSPARM.PARM_VALUE
     */
    private String            parmValue;

    /**
     *  null, T_BCL_SYSPARM.PARM_DESC
     */
    private String            parmDesc;

    /**
     *  null, T_BCL_SYSPARM.STUTS
     */
    private String            stuts;

    /**
     *  null, T_BCL_SYSPARM.MODIFY_FLAG
     */
    private String            modifyFlag;

    /**
     *  null, T_BCL_SYSPARM.UPDATE_DATE
     */
    private Date              updateDate;

    /**
     *  null, T_BCL_SYSPARM.CREATE_TIME
     */
    private Date              createTime;

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getParmCode() {
        return parmCode;
    }

    public void setParmCode(String parmCode) {
        this.parmCode = parmCode == null ? null : parmCode.trim();
    }

    public String getParmName() {
        return parmName;
    }

    public void setParmName(String parmName) {
        this.parmName = parmName == null ? null : parmName.trim();
    }

    public String getParmValue() {
        return parmValue;
    }

    public void setParmValue(String parmValue) {
        this.parmValue = parmValue == null ? null : parmValue.trim();
    }

    public String getParmDesc() {
        return parmDesc;
    }

    public void setParmDesc(String parmDesc) {
        this.parmDesc = parmDesc == null ? null : parmDesc.trim();
    }

    public String getStuts() {
        return stuts;
    }

    public void setStuts(String stuts) {
        this.stuts = stuts == null ? null : stuts.trim();
    }

    public String getModifyFlag() {
        return modifyFlag;
    }

    public void setModifyFlag(String modifyFlag) {
        this.modifyFlag = modifyFlag == null ? null : modifyFlag.trim();
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}