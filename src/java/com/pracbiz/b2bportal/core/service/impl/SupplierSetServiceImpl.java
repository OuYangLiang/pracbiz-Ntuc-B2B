package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierSetHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierSetExHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierSetMapper;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.SupplierSetService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class SupplierSetServiceImpl extends DBActionServiceDefaultImpl<SupplierSetHolder>
        implements SupplierSetService, CoreCommonConstants, ApplicationContextAware
{
    @Autowired private SupplierSetMapper mapper;
    @Autowired private SupplierService supplierService;
    
    private ApplicationContext ctx;
    
    @Override
    public List<SupplierSetHolder> select(SupplierSetHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(SupplierSetHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(SupplierSetHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(SupplierSetHolder oldObj,
            SupplierSetHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(SupplierSetHolder oldObj,
            SupplierSetHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public int getCountOfSummary(SupplierSetExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }


    @Override
    public List<SupplierSetExHolder> getListOfSummary(SupplierSetExHolder param)
            throws Exception
    {
        return mapper.getListOfSummary(param);
    }


    @Override
    public void insertSupplierSet(CommonParameterHolder cp,
        SupplierSetExHolder supplierSetEx) throws Exception
    {
        List<BigDecimal> supplierOidList = supplierSetEx.getSupplierOidList();
        for (BigDecimal supplierOid : supplierOidList)
        { 
            supplierSetEx.addOidToSupplierList(supplierOid);
        }
        
        this.getMeBean().auditInsert(cp, supplierSetEx);
        
        for (BigDecimal supplierOid : supplierOidList)
        {
            supplierService.updateSupplierBySupplierOid(supplierSetEx.getSetOid(), supplierOid);
        }
        
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private SupplierSetService getMeBean()
    {
        return ctx.getBean("supplierSetService", SupplierSetService.class);
    }


    @Override
    public SupplierSetHolder selectBySetId(String setId) throws Exception
    {
        if (setId == null || setId.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        SupplierSetHolder param = new SupplierSetHolder();
        param.setSetId(setId);
        List<SupplierSetHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public SupplierSetHolder selectByKey(BigDecimal setOid) throws Exception
    {
        if (setOid == null)
        {
            throw new IllegalArgumentException();
        }
        SupplierSetHolder param = new SupplierSetHolder();
        param.setSetOid(setOid);
        List<SupplierSetHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public void updateSupplierSet(CommonParameterHolder cp,
            SupplierSetExHolder supplierSetEx) throws Exception
    {
        SupplierSetHolder oldSupplierSet = this.selectByKey(supplierSetEx.getSetOid());
        SupplierSetExHolder oldSupplierSetEx = new SupplierSetExHolder();
        BeanUtils.copyProperties(oldSupplierSet, oldSupplierSetEx);
        List<SupplierHolder> selectSuppliers = supplierService.selectSuppliersBySetOid(oldSupplierSet.getSetOid());
        
        if (selectSuppliers != null && !selectSuppliers.isEmpty())
        {
            for (SupplierHolder supplier : selectSuppliers)
            {
                oldSupplierSetEx.addOidToSupplierList(supplier.getSupplierOid());
                supplierService.updateSupplierBySupplierOid(null, supplier.getSupplierOid());
            }
        }
        List<BigDecimal> supplierOidList = supplierSetEx.getSupplierOidList();
        
        for (BigDecimal supplierOid : supplierOidList)
        {
            supplierSetEx.addOidToSupplierList(supplierOid);
        }
        
        this.getMeBean().auditUpdateByPrimaryKey(cp, oldSupplierSetEx, supplierSetEx);
        
        for (BigDecimal supplierOid : supplierOidList)
        {
            supplierService.updateSupplierBySupplierOid(supplierSetEx.getSetOid(), supplierOid);
        }
       
    }

    
    @Override
    public void deleteSupplierSet(CommonParameterHolder cp,
        SupplierSetHolder supplierSetEx) throws Exception
    {
        List<SupplierHolder> suppliers = supplierService.selectSuppliersBySetOid(supplierSetEx.getSetOid());
        for (SupplierHolder supplier : suppliers)
        {
            supplierSetEx.addOidToSupplierList(supplier.getSupplierOid());
            supplierService.updateSupplierBySupplierOid(null, supplier.getSupplierOid());
        }
        
        SupplierSetExHolder oldSupplierSetEx = new SupplierSetExHolder();
        BeanUtils.copyProperties(supplierSetEx, oldSupplierSetEx);
        this.getMeBean().auditDelete(cp, supplierSetEx);
        this.delete(supplierSetEx);
    }

    
}
