package com.pracbiz.b2bportal.base.holder;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;

public class CommonParameterHolder implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal currentUserOid;
    private String loginId;
    private Boolean mkMode;
    private String clientIp;
    private Integer timeout;
    private BigDecimal sessionOid;
    private String pageSizes;
    private String defaultPageSize;
    private Boolean operateEquativeUserType;

    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }

    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }

    public String getLoginId()
    {
        return loginId;
    }

    public void setLoginId(String loginId)
    {
        this.loginId = loginId;
    }

    public Boolean getMkMode()
    {
        return mkMode;
    }

    public void setMkMode(Boolean mkMode)
    {
        this.mkMode = mkMode;
    }

    public String getClientIp()
    {
        return clientIp;
    }

    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public BigDecimal getSessionOid()
    {
        return sessionOid;
    }

    public void setSessionOid(BigDecimal sessionOid)
    {
        this.sessionOid = sessionOid;
    }
    
    public String getPageSizes()
    {
        return pageSizes;
    }

    public void setPageSizes(String pageSizes)
    {
        this.pageSizes = pageSizes;
    }

    public String getDefaultPageSize()
    {
        return defaultPageSize;
    }

    public void setDefaultPageSize(String defaultPageSize)
    {
        this.defaultPageSize = defaultPageSize;
    }

    public Boolean getOperateEquativeUserType()
    {
        return operateEquativeUserType;
    }

    public void setOperateEquativeUserType(Boolean operateEquativeUserType)
    {
        this.operateEquativeUserType = operateEquativeUserType;
    }

    public String toString()
    {
        try
        {
            return BeanUtils.describe(this).toString();
        }
        catch (Exception exception)
        {
            return exception.getMessage();
        }
    }
}
