package com.pracbiz.b2bportal.core.holder;

import java.util.Date;

import com.pracbiz.b2bportal.core.constants.DbActionType;
import com.pracbiz.b2bportal.core.constants.MkCtrlStatus;

public interface TmpHolder
{
    public String getActor();
    
    
    public void setActor(String actor);
    
    
    public Date getActionDate();
    
    
    public void setActionDate(Date actionDate);
    
    
    public DbActionType getActionType();
    
    
    public void setActionType(DbActionType dbActionType);
    
    
    public MkCtrlStatus getCtrlStatus();
    
    
    public void setCtrlStatus(MkCtrlStatus mkCtrlStatus);
}
