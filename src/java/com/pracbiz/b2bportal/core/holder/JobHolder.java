//*****************************************************************************
//
// File Name       :  JobHolder.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 9:45:18 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class JobHolder extends BaseHolder
{
    private static final long serialVersionUID = 3991222159037118941L;
    
    private BigDecimal jobOid;
    private String jobName;
    private String jobGroup;
    private String triggerName;
    private String triggerGroup;
    private String jobDescription;
    private String cronExpression;
    private Date updateDate;
    private String updateBy;
    private Boolean enabled;
    
    
    public BigDecimal getJobOid()
    {
        return jobOid;
    }


    public void setJobOid(BigDecimal jobOid)
    {
        this.jobOid = jobOid;
    }


    public String getJobName()
    {
        return jobName;
    }


    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }


    public String getJobGroup()
    {
        return jobGroup;
    }


    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }


    public String getTriggerName()
    {
        return triggerName;
    }


    public void setTriggerName(String triggerName)
    {
        this.triggerName = triggerName;
    }


    public String getTriggerGroup()
    {
        return triggerGroup;
    }


    public void setTriggerGroup(String triggerGroup)
    {
        this.triggerGroup = triggerGroup;
    }


    public String getJobDescription()
    {
        return jobDescription;
    }


    public void setJobDescription(String jobDescription)
    {
        this.jobDescription = jobDescription;
    }


    public String getCronExpression()
    {
        return cronExpression;
    }


    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }


    @JSON(format="yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date) updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date) updateDate.clone();
    }


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }

    
    public Boolean getEnabled()
    {
        return enabled;
    }


    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }


    @Override
    public String getCustomIdentification()
    {
        return jobOid == null ? null : jobOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getJobName();
    }

}
