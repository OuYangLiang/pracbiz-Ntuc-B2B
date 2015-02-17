//*****************************************************************************
//
// File Name       :  JobServiceImpl.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 10:14:47 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.JobHolder;
import com.pracbiz.b2bportal.core.mapper.JobMapper;
import com.pracbiz.b2bportal.core.service.JobService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class JobServiceImpl extends DBActionServiceDefaultImpl<JobHolder> implements JobService
{
    @Autowired
    private JobMapper mapper;
    
    @Override
    public List<JobHolder> select(JobHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(JobHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
    }


    @Override
    public void updateByPrimaryKeySelective(JobHolder oldObj_, JobHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(JobHolder oldObj_, JobHolder newObj_)
        throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(JobHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
    }


    @Override
    public int getCountOfSummary(JobHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<JobHolder> getListOfSummary(JobHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public JobHolder selectByKey(BigDecimal jobOid) throws Exception
    {
        if (null == jobOid)
        {
            throw new IllegalArgumentException();
        }
        
        JobHolder param = new JobHolder();
        param.setJobOid(jobOid);
        
        List<JobHolder> rlt = this.select(param);
        
        if (null == rlt || rlt.isEmpty())
        {
            return null;
        }
        
        return rlt.get(0);
    }

}
