package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class CurrencyHolder extends BaseHolder
{
    private static final long serialVersionUID = 8361128160370732660L;

    private String currCode;

    private String currDesc;


    public String getCurrCode()
    {
        return currCode;
    }


    public void setCurrCode(String currCode)
    {
        this.currCode = currCode == null ? null : currCode.trim();
    }


    public String getCurrDesc()
    {
        return currDesc;
    }


    public void setCurrDesc(String currDesc)
    {
        this.currDesc = currDesc == null ? null : currDesc.trim();
    }


    @Override
    public String getCustomIdentification()
    {
        return currCode;
    }
}