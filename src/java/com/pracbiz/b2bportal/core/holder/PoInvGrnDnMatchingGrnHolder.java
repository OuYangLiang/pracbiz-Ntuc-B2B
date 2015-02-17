package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class PoInvGrnDnMatchingGrnHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = -4342949130837553462L;

    private BigDecimal matchingOid;

    private BigDecimal grnOid;
    
    private String grnNo;
    
    private Date grnDate;

    private BigDecimal grnAmt;


    public BigDecimal getMatchingOid()
    {
        return matchingOid;
    }


    public void setMatchingOid(BigDecimal matchingOid)
    {
        this.matchingOid = matchingOid;
    }


    public BigDecimal getGrnOid()
    {
        return grnOid;
    }


    public void setGrnOid(BigDecimal grnOid)
    {
        this.grnOid = grnOid;
    }


    public String getGrnNo()
    {
        return grnNo;
    }


    public void setGrnNo(String grnNo)
    {
        this.grnNo = grnNo == null ? null : grnNo.trim();
    }


    public BigDecimal getGrnAmt()
    {
        return grnAmt;
    }


    public void setGrnAmt(BigDecimal grnAmt)
    {
        this.grnAmt = grnAmt;
    }


    public Date getGrnDate()
    {
        return grnDate == null ? null : (Date) grnDate.clone();
    }


    public void setGrnDate(Date grnDate)
    {
        this.grnDate = grnDate == null ? null : (Date) grnDate.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(matchingOid + "" + grnOid);
    }
}