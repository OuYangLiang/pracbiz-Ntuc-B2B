package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.AccessActionType;
import com.pracbiz.b2bportal.core.constants.AuditAccessErrorCode;
import com.pracbiz.b2bportal.core.constants.PrincipalType;

public class AuditAccessHolder extends BaseHolder
{
    private static final long serialVersionUID = 5196426623013535903L;

    private BigDecimal accessOid;

    private PrincipalType principalType;

    private BigDecimal principalOid;

    private String loginId;

    private String clientIp;

    private Boolean success;

    private AccessActionType actionType;

    private Date actionDate;

    private Integer attemptNo;

    private AuditAccessErrorCode errorCode;
    
    private BigDecimal userTypeOid;
    
    private BigDecimal companyOid;
    
    private BigDecimal currentUserOid;

    public BigDecimal getAccessOid()
    {
        return accessOid;
    }

    public void setAccessOid(BigDecimal accessOid)
    {
        this.accessOid = accessOid;
    }

    public PrincipalType getPrincipalType()
    {
        return principalType;
    }

    public void setPrincipalType(PrincipalType principalType)
    {
        this.principalType = principalType;
    }

    public BigDecimal getPrincipalOid()
    {
        return principalOid;
    }

    public void setPrincipalOid(BigDecimal principalOid)
    {
        this.principalOid = principalOid;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public AccessActionType getActionType()
    {
        return actionType;
    }

    public void setActionType(AccessActionType actionType)
    {
        this.actionType = actionType;
    }

    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getActionDate()
    {
        return actionDate == null ? null : (Date) actionDate.clone();
    }

    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date) actionDate.clone();
    }

    public Integer getAttemptNo()
    {
        return attemptNo;
    }

    public void setAttemptNo(Integer attemptNo)
    {
        this.attemptNo = attemptNo;
    }

    public AuditAccessErrorCode getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(AuditAccessErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }

    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }

    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }

    public BigDecimal getCompanyOid()
    {
        return companyOid;
    }

    public void setCompanyOid(BigDecimal companyOid)
    {
        this.companyOid = companyOid;
    }

    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }

    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }

    @Override
    public String getCustomIdentification()
    {
        return accessOid == null ? null : accessOid.toString();
    }
}