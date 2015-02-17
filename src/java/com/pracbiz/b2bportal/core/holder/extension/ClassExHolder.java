package com.pracbiz.b2bportal.core.holder.extension;

import com.pracbiz.b2bportal.core.holder.ClassHolder;

public class ClassExHolder extends ClassHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String buyerCode;


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }

}
