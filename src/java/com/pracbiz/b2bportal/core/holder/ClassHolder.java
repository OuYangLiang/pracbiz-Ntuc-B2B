package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ClassHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 5702396347585263721L;
    private BigDecimal classOid;
    private String classCode;
    private String classDesc;
    private BigDecimal buyerOid;
    
    public ClassHolder()
    {
        
    }
    
    public ClassHolder(BigDecimal classOid, String classCode, String classDesc, BigDecimal buyerOid)
    {
        this.classOid = classOid;
        this.classCode = classCode;
        this.classDesc = classDesc;
        this.buyerOid = buyerOid;
    }

    public BigDecimal getClassOid()
    {
        return classOid;
    }


    public void setClassOid(BigDecimal classOid)
    {
        this.classOid = classOid;
    }


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    public String getClassDesc()
    {
        return classDesc;
    }


    public void setClassDesc(String classDesc)
    {
        this.classDesc = classDesc;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return classOid == null ? null : classOid.toString();
    }

}
