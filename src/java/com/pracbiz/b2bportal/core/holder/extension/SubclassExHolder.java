package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.holder.SubclassHolder;

public class SubclassExHolder extends SubclassHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = -2869878951770412472L;
    private String classCode;
    private BigDecimal buyerOid;
    private String buyerCode;


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }

}
