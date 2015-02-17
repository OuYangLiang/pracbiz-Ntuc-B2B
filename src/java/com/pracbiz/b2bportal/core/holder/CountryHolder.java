package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class CountryHolder extends BaseHolder
{
    private static final long serialVersionUID = 6564240272916592259L;

    private String ctryCode;

    private String ctryDesc;


    public String getCtryCode()
    {
        return ctryCode;
    }


    public void setCtryCode(String ctryCode)
    {
        this.ctryCode = ctryCode == null ? null : ctryCode.trim();
    }


    public String getCtryDesc()
    {
        return ctryDesc;
    }


    public void setCtryDesc(String ctryDesc)
    {
        this.ctryDesc = ctryDesc == null ? null : ctryDesc.trim();
    }


    @Override
    public String getCustomIdentification()
    {
        return ctryCode;
    }
}