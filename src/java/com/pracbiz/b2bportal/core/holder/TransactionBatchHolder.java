package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;
import java.util.Date;

public class TransactionBatchHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 2046091902845028616L;

    private BigDecimal batchOid;

    private String batchNo;

    private String batchFilename;

    private Date createDate;

    private Date alertSenderDate;

    public BigDecimal getBatchOid() {
        return batchOid;
    }

    public void setBatchOid(BigDecimal batchOid) {
        this.batchOid = batchOid;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    public String getBatchFilename() {
        return batchFilename;
    }

    public void setBatchFilename(String batchFilename) {
        this.batchFilename = batchFilename == null ? null : batchFilename.trim();
    }

    public Date getCreateDate() {
        return createDate == null ? null : (Date)createDate.clone();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }

    public Date getAlertSenderDate() {
        return alertSenderDate == null ? null : (Date)alertSenderDate.clone();
    }

    public void setAlertSenderDate(Date alertSenderDate) {
        this.alertSenderDate = alertSenderDate == null ? null : (Date)alertSenderDate.clone();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(batchOid);
    }
}