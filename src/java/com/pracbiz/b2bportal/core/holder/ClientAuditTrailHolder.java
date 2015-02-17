package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.ClientActionType;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class ClientAuditTrailHolder extends BaseHolder
{
    private static final long serialVersionUID = 1L;

    private BigDecimal auditOid;

    private Date actionDate;

    private Boolean actionStatus;

    private ClientActionType actionType;

    private String actionSummary;

    private String clientIp;

    private String batchFileName;

    private String xmlContent;


    public BigDecimal getAuditOid()
    {
        return auditOid;
    }


    public void setAuditOid(BigDecimal auditOid)
    {
        this.auditOid = auditOid;
    }


    public Date getActionDate()
    {
        return actionDate == null ? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }


    public Boolean getActionStatus()
    {
        return actionStatus;
    }


    public void setActionStatus(Boolean actionStatus)
    {
        this.actionStatus = actionStatus;
    }


    public ClientActionType getActionType()
    {
        return actionType;
    }


    public void setActionType(ClientActionType actionType)
    {
        this.actionType = actionType;
    }


    public String getClientIp()
    {
        return clientIp;
    }


    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp;
    }


    public String getBatchFileName()
    {
        return batchFileName;
    }


    public void setBatchFileName(String batchFileName)
    {
        this.batchFileName = batchFileName;
    }


    public String getXmlContent()
    {
        return xmlContent;
    }


    public void setXmlContent(String xmlContent)
    {
        this.xmlContent = xmlContent;
    }


    public String getActionSummary()
    {
        return actionSummary;
    }


    public void setActionSummary(String actionSummary)
    {
        this.actionSummary = actionSummary;
    }


    @Override
    public String getCustomIdentification()
    {
        return null;
    }

}
