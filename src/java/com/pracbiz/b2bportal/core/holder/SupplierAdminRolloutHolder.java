package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SupplierAdminRolloutHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal supplierOid;
    private Date passwdSendDate;
    private Date liveDate;
    private String batchNo;


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public Date getPasswdSendDate()
    {
        return passwdSendDate == null ? null : (Date) passwdSendDate.clone();
    }


    public void setPasswdSendDate(Date passwdSendDate)
    {
        this.passwdSendDate = passwdSendDate == null ? null : (Date) passwdSendDate.clone();
    }


    public Date getLiveDate()
    {
        return liveDate == null ? null : (Date) liveDate.clone();
    }


    public void setLiveDate(Date liveDate)
    {
        this.liveDate = liveDate == null ? null :(Date) liveDate.clone();
    }


    public String getBatchNo()
    {
        return batchNo;
    }


    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(supplierOid);
    }

}
