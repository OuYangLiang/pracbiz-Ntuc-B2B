package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class UserSessionHolder extends BaseHolder
{
    private static final long serialVersionUID = -5742806256510677804L;

    private String sessionId;

    private BigDecimal userOid;

    private Date createDate;


    public String getSessionId()
    {
        return sessionId;
    }


    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date)createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return sessionId;
    }

}