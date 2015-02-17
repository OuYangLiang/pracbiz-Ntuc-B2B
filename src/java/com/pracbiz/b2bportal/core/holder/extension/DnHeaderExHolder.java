package com.pracbiz.b2bportal.core.holder.extension;

import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;

public class DnHeaderExHolder extends DnHeaderHolder
{
    private static final long serialVersionUID = 1L;
    private String vatRateVal;
    public String getVatRateVal()
    {
        return vatRateVal;
    }
    public void setVatRateVal(String vatRateVal)
    {
        this.vatRateVal = vatRateVal;
    }
    
}
