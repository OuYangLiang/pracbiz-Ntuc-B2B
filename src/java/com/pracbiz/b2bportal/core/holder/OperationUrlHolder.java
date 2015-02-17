package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class OperationUrlHolder extends BaseHolder
{
    private static final long serialVersionUID = -1361750974388828103L;

    private String accessUrl;

    private String opnId;

    private Boolean implicit;

    private Boolean needAuth;


    public String getAccessUrl()
    {
        return accessUrl;
    }


    public void setAccessUrl(String accessUrl)
    {
        this.accessUrl = accessUrl == null ? null : accessUrl.trim();
    }


    public String getOpnId()
    {
        return opnId;
    }


    public void setOpnId(String opnId)
    {
        this.opnId = opnId == null ? null : opnId.trim();
    }


    public Boolean getImplicit()
    {
        return implicit;
    }


    public void setImplicit(Boolean implicit)
    {
        this.implicit = implicit;
    }


    public Boolean getNeedAuth()
    {
        return needAuth;
    }


    public void setNeedAuth(Boolean needAuth)
    {
        this.needAuth = needAuth;
    }


    @Override
    public String getCustomIdentification()
    {
        return accessUrl + opnId;
    }

}