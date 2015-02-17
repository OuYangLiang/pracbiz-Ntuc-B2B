//*****************************************************************************
//
// File Name       :  AuditSessionServiceImpl.java
// Date Created    :  Sep 26, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 26, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.AuditSessionHolder;
import com.pracbiz.b2bportal.core.holder.extension.AuditSessionExHolder;
import com.pracbiz.b2bportal.core.mapper.AuditSessionMapper;
import com.pracbiz.b2bportal.core.service.AuditSessionService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class AuditSessionServiceImpl extends DBActionServiceDefaultImpl<AuditSessionHolder> implements AuditSessionService
{
    @Autowired private AuditSessionMapper mapper;
    
    @Override
    public List<AuditSessionHolder> select(AuditSessionHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(AuditSessionHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(AuditSessionHolder oldObj_,
        AuditSessionHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(AuditSessionHolder oldObj_,
        AuditSessionHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(AuditSessionHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    /** 
     * {@inheritDoc}
     * @author jiangming
     * @see com.pracbiz.b2bportal.core.service.AuditSessionService#selectAuditSessionByKey(java.math.BigDecimal)
     */
    @Override
    public AuditSessionHolder selectAuditSessionByKey(BigDecimal sessionOid)
    {
        if (sessionOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        AuditSessionHolder param = new AuditSessionHolder();
        param.setSessionOid(sessionOid);
        
        List<AuditSessionHolder> rlt = mapper.select(param);
        
        if (rlt != null && rlt.size() == 1)
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public int getCountOfSummary(AuditSessionExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<AuditSessionExHolder> getListOfSummary(
            AuditSessionExHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }

}
