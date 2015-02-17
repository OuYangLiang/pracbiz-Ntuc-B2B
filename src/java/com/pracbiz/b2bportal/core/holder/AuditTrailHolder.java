package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.ActorAction;
import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;

public class AuditTrailHolder extends BaseHolder
{
    private static final long serialVersionUID = 6476407815644267071L;

    private BigDecimal auditTrailOid;
    
    private Date actionDate;

    private DbActionType actionType;

    private ActorAction actorAction;

    private BigDecimal actorOid;

    private String actor;

    private String clientIp;
    
    private String recordKey;

    private String recordType;

    private MkCtrlStatus recordStatus;

    private String xmlContent;
    
    private BigDecimal sessionOid;


    public BigDecimal getAuditTrailOid()
    {
        return auditTrailOid;
    }


    public void setAuditTrailOid(BigDecimal auditTrailOid)
    {
        this.auditTrailOid = auditTrailOid;
    }

    @JSON(format = "yyyy-MM-dd HH:mm:ss")
    public Date getActionDate()
    {
        return actionDate == null? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null? null : (Date)actionDate.clone();
    }


    public DbActionType getActionType()
    {
        return actionType;
    }


    public void setActionType(DbActionType actionType)
    {
        this.actionType = actionType;
    }


    public ActorAction getActorAction()
    {
        return actorAction;
    }


    public void setActorAction(ActorAction actorAction)
    {
        this.actorAction = actorAction;
    }


    public BigDecimal getActorOid()
    {
        return actorOid;
    }


    public void setActorOid(BigDecimal actorOid)
    {
        this.actorOid = actorOid;
    }


    public String getActor()
    {
        return actor;
    }


    public void setActor(String actor)
    {
        this.actor = actor == null ? null : actor.trim();
    }


    public String getClientIp()
    {
        return clientIp;
    }


    public void setClientIp(String clientIp)
    {
        this.clientIp = clientIp == null ? null : clientIp.trim();
    }


    public String getRecordType()
    {
        return recordType;
    }


    public void setRecordType(String recordType)
    {
        this.recordType = recordType == null ? null : recordType.trim();
    }


    public MkCtrlStatus getRecordStatus()
    {
        return recordStatus;
    }


    public void setRecordStatus(MkCtrlStatus recordStatus)
    {
        this.recordStatus = recordStatus;
    }


    public String getXmlContent()
    {
        return xmlContent;
    }


    public void setXmlContent(String xmlContent)
    {
        this.xmlContent = xmlContent == null ? null : xmlContent.trim();
    }


    public BigDecimal getSessionOid()
    {
        return sessionOid;
    }


    public void setSessionOid(BigDecimal sessionOid)
    {
        this.sessionOid = sessionOid;
    }


    public String getRecordKey()
    {
        return recordKey;
    }


    public void setRecordKey(String recordKey)
    {
        this.recordKey = recordKey;
    }
    
    
    @Override
    public String getCustomIdentification()
    {
        return auditTrailOid == null ? null : auditTrailOid.toString();
    }
}