package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class DocClassHolder extends BaseHolder implements CoreCommonConstants
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal docOid;
    private String classCode;


    public BigDecimal getDocOid()
    {
        return docOid;
    }


    public void setDocOid(BigDecimal docOid)
    {
        this.docOid = docOid;
    }


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(docOid) + classCode;
    }
}