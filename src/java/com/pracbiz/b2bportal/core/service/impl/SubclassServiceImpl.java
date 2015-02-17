package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.SubclassExHolder;
import com.pracbiz.b2bportal.core.mapper.SubclassMapper;
import com.pracbiz.b2bportal.core.service.SubclassService;

public class SubclassServiceImpl extends DBActionServiceDefaultImpl<SubclassHolder>
        implements SubclassService
{
    @Autowired SubclassMapper mapper;
    
    @Override
    public List<SubclassHolder> select(SubclassHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(SubclassHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(SubclassHolder oldObj_,
            SubclassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(SubclassHolder oldObj_,
            SubclassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(SubclassHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<SubclassExHolder> selectSubclassExByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectSubclassExByUserOid(userOid);
    }
    

    @Override
    public List<SubclassExHolder> selectTmpSubclassExByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectTmpSubclassExByUserOid(userOid);
    }

    @Override
    public List<SubclassExHolder> selectSubclassExByBuyerOid(BigDecimal buyerOid)
            throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectSubclassExByBuyerOid(buyerOid);
    }
    
    @Override
    public int deleteSubClassByBuyerOid(BigDecimal buyerOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.deleteByBuyerOid(buyerOid);
    }

    
    @Override
    public SubclassHolder selectSubclassExByItemCodeAndBuyerOid(
            String buyerItemCode, BigDecimal buyerOid) throws Exception
    {
        if (buyerItemCode == null || buyerItemCode.trim().isEmpty() || buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerItemCode", buyerItemCode);
        map.put("buyerOid", buyerOid);
        
        return mapper.selectSubclassByItemCodeAndBuyerOid(map);
    }

    
    @Override
    public void deleteBySubclassOid(BigDecimal subclassOid) throws Exception
    {
        if (subclassOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        SubclassHolder param = new SubclassHolder();
        param.setSubclassOid(subclassOid);
        
        delete(param);
    }

    
    @Override
    public SubclassHolder selectByClassOidSubclassCode(BigDecimal classOid, String subclassCode)
            throws Exception
    {
        if (classOid == null || subclassCode == null || subclassCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        SubclassHolder param = new SubclassHolder();
        param.setClassOid(classOid);
        param.setSubclassCode(subclassCode);
        
        List<SubclassHolder> list = this.select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

}
