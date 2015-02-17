package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class OidGenericHolder extends BaseHolder
{
    private static final long serialVersionUID = 3600100963029735899L;
    private Long oid;


    public Long getOid()
    {
        return oid;
    }


    public void setOid(Long oid)
    {
        this.oid = oid;
    }


    @Override
    public String getCustomIdentification()
    {
        return oid == null ? null : oid.toString();
    }
}