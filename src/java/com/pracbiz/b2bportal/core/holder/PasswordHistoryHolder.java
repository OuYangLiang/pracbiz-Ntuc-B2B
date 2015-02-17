package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class PasswordHistoryHolder extends BaseHolder
{
    private static final long serialVersionUID = -2405339459728764770L;

    private Date changeDate;

    private BigDecimal userOid;

    private String oldLoginPwd;

    private String actor;

    private String changeReason;


    public Date getChangeDate()
    {
        return changeDate == null ? null : (Date)changeDate.clone();
    }


    public void setChangeDate(Date changeDate)
    {
        this.changeDate = changeDate == null ? null : (Date)changeDate.clone();
    }


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public String getOldLoginPwd()
    {
        return oldLoginPwd;
    }


    public void setOldLoginPwd(String oldLoginPwd)
    {
        this.oldLoginPwd = oldLoginPwd == null ? null : oldLoginPwd.trim();
    }


    public String getActor()
    {
        return actor;
    }


    public void setActor(String actor)
    {
        this.actor = actor == null ? null : actor.trim();
    }


    public String getChangeReason()
    {
        return changeReason;
    }


    public void setChangeReason(String changeReason)
    {
        this.changeReason = changeReason == null ? null : changeReason.trim();
    }


    @Override
    public String getCustomIdentification()
    {
        return userOid.toString() + Long.toString(changeDate.getTime());
    }

    
    @Override
    public String getLogicalKey()
    {
        return this.getActor();
    }
}