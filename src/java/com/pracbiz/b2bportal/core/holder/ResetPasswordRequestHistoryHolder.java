package com.pracbiz.b2bportal.core.holder;

import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ResetPasswordRequestHistoryHolder extends BaseHolder
{
    private static final long serialVersionUID = -6274409207632660043L;

    private String hashCode;

    private String loginId;

    private Date requestTime;

    private String clientIp;

    private Boolean valid;


    public String getHashCode()
    {
        return hashCode;
    }


    public void setHashCode(String hashCode)
    {
        this.hashCode = hashCode == null ? null : hashCode.trim();
    }


    public String getLoginId()
    {
        return loginId;
    }


    public void setLoginId(String loginId)
    {
        this.loginId = loginId == null ? null : loginId.trim();
    }


    public Date getRequestTime()
    {
        return requestTime == null ? null : (Date)requestTime.clone();
    }


    public void setRequestTime(Date requestTime)
    {
        this.requestTime = requestTime == null ? null : (Date)requestTime.clone();
    }


    public String getClientIp()
    {
        return clientIp;
    }


    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }


    public Boolean getValid()
    {
        return valid;
    }


    public void setValid(Boolean valid)
    {
        this.valid = valid;
    }


    @Override
    public String getCustomIdentification()
    {
        return hashCode + loginId + Long.toString(requestTime.getTime());
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getLoginId();
    }
}