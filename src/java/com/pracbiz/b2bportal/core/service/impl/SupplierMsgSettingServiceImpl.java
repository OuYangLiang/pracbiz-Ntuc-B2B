package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.mapper.SupplierMsgSettingMapper;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;

public class SupplierMsgSettingServiceImpl extends
    DBActionServiceDefaultImpl<SupplierMsgSettingHolder> implements
    SupplierMsgSettingService
{
    @Autowired
    private SupplierMsgSettingMapper mapper;

    @Override
    public List<SupplierMsgSettingHolder> select(SupplierMsgSettingHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    
    @Override
    public void insert(SupplierMsgSettingHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    
    @Override
    public void updateByPrimaryKeySelective(SupplierMsgSettingHolder oldObj_,
        SupplierMsgSettingHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    
    @Override
    public void updateByPrimaryKey(SupplierMsgSettingHolder oldObj_,
        SupplierMsgSettingHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    
    @Override
    public void delete(SupplierMsgSettingHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public List<SupplierMsgSettingHolder> selectSupplierMsgSettingBySupplierOid(
        BigDecimal supplierOid) throws Exception
    {
        if(supplierOid == null)
        {
            return null;
        }

        SupplierMsgSettingHolder supplierMsgSetting = new SupplierMsgSettingHolder();
        supplierMsgSetting.setSupplierOid(supplierOid);
        return mapper.select(supplierMsgSetting);

    }


    @Override
    public SupplierMsgSettingHolder selectByKey(BigDecimal supplierOid,
        String type) throws Exception
    {
        if(supplierOid == null || null == type || type.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        SupplierMsgSettingHolder param = new SupplierMsgSettingHolder();
        param.setSupplierOid(supplierOid);
        param.setMsgType(type);
        
        List<SupplierMsgSettingHolder> rlt = this.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public void updateEmailAddressBySupplierOid(BigDecimal supplierOid, String email)
            throws Exception
    {
        if(supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rcpsAddrs", email);
        map.put("supplierOid", supplierOid);
        mapper.updateEmailAddressBySupplierOid(map);
    }
    
    
    @Override
    public void updateEmptyEmailAddressBySupplierOid(BigDecimal supplierOid, String email)
            throws Exception
    {
        if(supplierOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rcpsAddrs", email);
        map.put("supplierOid", supplierOid);
        mapper.updateEmptyEmailAddressBySupplierOid(map);
    }
    

}
