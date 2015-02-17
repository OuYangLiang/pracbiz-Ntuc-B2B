package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SubclassHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 5594630049446400780L;
    private BigDecimal subclassOid;
    private String subclassCode;
    private String subclassDesc;
    private BigDecimal classOid;

    
    public SubclassHolder()
    {
        
    }
    
    
    public SubclassHolder(BigDecimal subclassOid, String subclassCode, String subclassDesc, BigDecimal classOid)
    {
        this.subclassOid = subclassOid;
        this.subclassCode = subclassCode;
        this.subclassDesc = subclassDesc;
        this.classOid = classOid;
    }
    

    public BigDecimal getSubclassOid()
    {
        return subclassOid;
    }


    public void setSubclassOid(BigDecimal subclassOid)
    {
        this.subclassOid = subclassOid;
    }


    public String getSubclassCode()
    {
        return subclassCode;
    }


    public void setSubclassCode(String subclassCode)
    {
        this.subclassCode = subclassCode;
    }


    public String getSubclassDesc()
    {
        return subclassDesc;
    }


    public void setSubclassDesc(String subclassDesc)
    {
        this.subclassDesc = subclassDesc;
    }


    public BigDecimal getClassOid()
    {
        return classOid;
    }


    public void setClassOid(BigDecimal classOid)
    {
        this.classOid = classOid;
    }


    @Override
    public String getCustomIdentification()
    {
        return subclassOid == null ? null : subclassOid.toString();
    }

}
