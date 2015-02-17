package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class AuditSessionHolder extends BaseHolder
{
    private static final long serialVersionUID = 2430918334743554275L;

    private BigDecimal sessionOid;

    private String sessionId;

    private Date startDate;

    private Date endDate;

    private String sessionSummary;

    private BigDecimal userOid;

    private String loginId;

    private String bkendOpnId;

    public BigDecimal getSessionOid()
    {
        return sessionOid;
    }

    public void setSessionOid(BigDecimal sessionOid)
    {
        this.sessionOid = sessionOid;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId == null ? null : sessionId.trim();
    }
    
    @JSON(format = "yyyy-MM-dd hh:mm:ss")
    public Date getStartDate()
    {
        return startDate == null ? null : (Date) startDate.clone();
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate == null ? null : (Date) startDate.clone();
    }

    @JSON(format = "yyyy-MM-dd hh:mm:ss")
    public Date getEndDate()
    {
        return endDate == null ? null : (Date) endDate.clone();
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate == null ? null : (Date) endDate.clone();
    }

    public String getSessionSummary()
    {
        return sessionSummary;
    }

    public void setSessionSummary(String sessionSummary)
    {
        this.sessionSummary = sessionSummary == null ? null : sessionSummary
            .trim();
    }

    public BigDecimal getUserOid()
    {
        return userOid;
    }

    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId == null ? null : loginId.trim();
    }

    public String getBkendOpnId()
    {
        return bkendOpnId;
    }

    public void setBkendOpnId(String bkendOpnId)
    {
        this.bkendOpnId = bkendOpnId == null ? null : bkendOpnId.trim();
    }

   
    @Override
    public String getCustomIdentification()
    {
        return sessionOid == null ? null : sessionOid.toString();
    }
}