package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.AuditTrailHolder;
import com.pracbiz.b2bportal.core.mapper.AuditTrailMapper;
import com.pracbiz.b2bportal.core.service.AuditTrailService;

public class AuditTrailServiceImpl extends DBActionServiceDefaultImpl<AuditTrailHolder>
        implements AuditTrailService
{
    @Autowired
    private AuditTrailMapper mapper;
    
    @Override
    public List<AuditTrailHolder> select(AuditTrailHolder param)
            throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public List<AuditTrailHolder> selectWithBLOBs(AuditTrailHolder param)
            throws Exception
    {
        return mapper.selectWithBLOBs(param);
    }

    @Override
    public void delete(AuditTrailHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }

    @Override
    public void insert(AuditTrailHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }

    @Override
    public void updateByPrimaryKey(AuditTrailHolder oldObj,
            AuditTrailHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }

    @Override
    public void updateByPrimaryKeySelective(AuditTrailHolder oldObj,
            AuditTrailHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

    @Override
    public void updateByPrimaryKeyWithBLOBs(AuditTrailHolder oldObj,
            AuditTrailHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeyWithBLOBs(newObj);
    }

    @Override
    public int getCountOfSummary(AuditTrailHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }

    @Override
    public List<AuditTrailHolder> getListOfSummary(AuditTrailHolder param)
            throws Exception
    {
        return mapper.getListOfSummary(param);
    }

    @Override
    public AuditTrailHolder selectAuditTrailWithBlobsByKey(BigDecimal auditTrailOid)
            throws Exception
    {
        if(auditTrailOid == null)
        {
            return null;
        }
        AuditTrailHolder param = new AuditTrailHolder();
        param.setAuditTrailOid(auditTrailOid);
        List<AuditTrailHolder> list = this.selectWithBLOBs(param);
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

}
