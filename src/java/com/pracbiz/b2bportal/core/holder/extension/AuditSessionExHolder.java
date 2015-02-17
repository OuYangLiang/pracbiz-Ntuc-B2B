package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.core.holder.AuditSessionHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class AuditSessionExHolder extends AuditSessionHolder
{
    private static final long serialVersionUID = 1L;

    private Date dateFrom;
    
    private Date dateTo;
    
    private BigDecimal buyerOid;
    
    private BigDecimal supplierOid;

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

}
