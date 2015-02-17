package com.pracbiz.b2bportal.core.holder;

import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;

public class GroupTmpHolder extends GroupHolder implements TmpHolder
{
    private static final long serialVersionUID = -3524828963701203324L;
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
    
    public void addRoleGroups(List<? extends RoleGroupHolder> roleGroups)
    {
        this.setRoleGroups(roleGroups);
    }
    
    
    public void addGroupTradingPartners(List<? extends GroupTradingPartnerHolder> groupTradingPartners)
    {
        this.setGroupTradingPartners(groupTradingPartners);
    }
    
    
    public void addGroupUsers(List<? extends GroupUserHolder> groupUsers)
    {
        this.setGroupUsers(groupUsers);
    }
    
    
    public void addGroupSuppliers(
        List<? extends GroupSupplierHolder> groupSuppliers)
    {
        this.setGroupSuppliers(groupSuppliers);
    }
    

}