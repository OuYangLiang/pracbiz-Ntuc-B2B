package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SupplierAdminRolloutHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierAdminRolloutMapper;
import com.pracbiz.b2bportal.core.service.SupplierAdminRolloutService;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class SupplierAdminRolloutServiceImpl extends
        DBActionServiceDefaultImpl<SupplierAdminRolloutHolder> implements SupplierAdminRolloutService
{
    @Autowired SupplierAdminRolloutMapper mapper;
    
    @Override
    public List<SupplierAdminRolloutHolder> select(
            SupplierAdminRolloutHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SupplierAdminRolloutHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(SupplierAdminRolloutHolder oldObj_,
            SupplierAdminRolloutHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(SupplierAdminRolloutHolder oldObj_,
            SupplierAdminRolloutHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(SupplierAdminRolloutHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public SupplierAdminRolloutHolder selectByKey(BigDecimal supplierOid)
            throws Exception
    {
        if (supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierAdminRolloutHolder param = new SupplierAdminRolloutHolder();
        param.setSupplierOid(supplierOid);
        List<SupplierAdminRolloutHolder> result = this.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result.get(0);
    }
}
