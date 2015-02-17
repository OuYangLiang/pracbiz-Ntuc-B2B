package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;

public class AuditTrailExHolder extends AuditTrailHolder
{
    private static final long serialVersionUID = 1L;
    
    private BigDecimal userTypeOid;
    
    private String userTypeDesc;
    
    private Date dateFrom;
    
    private Date dateTo;
    
    private String company;
    
    private String sessionId;
    
    private BigDecimal buyerOid;
    
    private BigDecimal supplierOid;
    
    private BigDecimal currentUserOid;
    
    private BigDecimal currentUserTypeOid;

    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }

    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }

    public String getUserTypeDesc()
    {
        return userTypeDesc;
    }

    public void setUserTypeDesc(String userTypeDesc)
    {
        this.userTypeDesc = userTypeDesc;
    }

    public Date getDateFrom()
    {
        return dateFrom == null ? null : (Date) dateFrom.clone();
    }

    public void setDateFrom(Date dateFrom)
    {
        this.dateFrom = dateFrom == null ? null : (Date) dateFrom.clone();
    }

    public Date getDateTo()
    {
        return dateTo == null ? null : (Date) dateTo.clone();
    }

    public void setDateTo(Date dateTo)
    {
        this.dateTo = dateTo == null ? null : (Date) dateTo.clone();
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }

    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }

    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }

    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }

    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }

    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }

    public BigDecimal getCurrentUserTypeOid()
    {
        return currentUserTypeOid;
    }

    public void setCurrentUserTypeOid(BigDecimal currentUserTypeOid)
    {
        this.currentUserTypeOid = currentUserTypeOid;
    }
    
    
}
