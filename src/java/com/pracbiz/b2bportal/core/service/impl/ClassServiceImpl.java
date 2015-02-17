//*****************************************************************************
//
// File Name       :  ClassServiceImpl.java
// Date Created    :  Oct 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 10, 2013 6:30:38 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.extension.ClassExHolder;
import com.pracbiz.b2bportal.core.mapper.ClassMapper;
import com.pracbiz.b2bportal.core.service.ClassService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class ClassServiceImpl extends DBActionServiceDefaultImpl<ClassHolder>
    implements ClassService
{

    @Autowired 
    private ClassMapper mapper;
    
    @Override
    public List<ClassHolder> select(ClassHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(ClassHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(ClassHolder oldObj_,
            ClassHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(ClassHolder oldObj_, ClassHolder newObj_)
            throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(ClassHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public List<ClassExHolder> selectClassByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectClassByUserOid(userOid);
    }
    

    @Override
    public List<ClassExHolder> selectTmpClassByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectTmpClassByUserOid(userOid);
    }

    @Override
    public List<ClassExHolder> selectClassByBuyerOid(BigDecimal buyerOid)
            throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectClassByBuyerOid(buyerOid);
    }
    
    
    @Override
    public int deleteClassByBuyerOid(BigDecimal buyerOid) throws Exception
    {
        if (buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        ClassHolder clazz = new ClassHolder();
        clazz.setBuyerOid(buyerOid);
        
        return mapper.delete(clazz);
    }

    
    @Override
    public ClassHolder selectClassByItemCodeAndBuyerOid(String buyerItemCode,
            BigDecimal buyerOid) throws Exception
    {
        if (buyerItemCode == null || buyerItemCode.trim().isEmpty() || buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerItemCode", buyerItemCode);
        map.put("buyerOid", buyerOid);
        return mapper.selectClassByItemCodeAndBuyerOid(map);
    }

    
    @Override
    public void deleteByClassOid(BigDecimal classOid) throws Exception
    {
        if (classOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        ClassHolder param = new ClassHolder();
        param.setClassOid(classOid);
        delete(param);
    }

    
    @Override
    public ClassHolder selectByKey(BigDecimal classOid) throws Exception
    {
        if (classOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        ClassHolder param = new ClassHolder();
        param.setClassOid(classOid);
        List<ClassHolder> list = select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

    
    @Override
    public ClassHolder selectByBuyerOidAndClassCode(BigDecimal buyerOid,
            String classCode) throws Exception
    {
        if (buyerOid == null || classCode == null || classCode.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        ClassHolder param = new ClassHolder();
        param.setBuyerOid(buyerOid);
        param.setClassCode(classCode);
        
        List<ClassHolder> list = select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
    
    
}
