package com.pracbiz.b2bportal.core.holder;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;

public class RoleTmpHolder extends RoleHolder implements TmpHolder
{
    private static final long serialVersionUID = 4366692691412195494L;
    private String actor;
    private Date actionDate;
    private DbActionType actionType;
    private MkCtrlStatus ctrlStatus;


    public String getActor()
    {
        return actor;
    }


    public void setActor(String actor)
    {
        this.actor = actor == null ? null : actor.trim();
    }


    public DbActionType getActionType()
    {
        return actionType;
    }


    public void setActionType(DbActionType actionType)
    {
        this.actionType = actionType;
    }


    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getActionDate()
    {
        return actionDate == null ? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }


    public MkCtrlStatus getCtrlStatus()
    {
        return ctrlStatus;
    }


    public void setCtrlStatus(MkCtrlStatus ctrlStatus)
    {
        this.ctrlStatus = ctrlStatus;
    }
}