//*****************************************************************************
//
// File Name       :  GrnHeaderExtendedServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-27 $
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


import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GiHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.mapper.GiHeaderExtendedMapper;
import com.pracbiz.b2bportal.core.service.GiHeaderExtendedService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GiHeaderExtendedServiceImpl extends
    DBActionServiceDefaultImpl<GiHeaderExtendedHolder> implements
    GiHeaderExtendedService, BaseService<GiHeaderExtendedHolder>
{
    @Autowired
    private GiHeaderExtendedMapper mapper;

    
    @Override
    public void delete(GiHeaderExtendedHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    
    @Override
    public void insert(GiHeaderExtendedHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    
    @Override
    public List<GiHeaderExtendedHolder> select(GiHeaderExtendedHolder param)
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void updateByPrimaryKey(GiHeaderExtendedHolder oldObj,
        GiHeaderExtendedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    
    @Override
    public void updateByPrimaryKeySelective(GiHeaderExtendedHolder oldObj,
        GiHeaderExtendedHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public List<GiHeaderExtendedHolder> selectHeaderExtendedByKey(
        BigDecimal giOid) throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }

        GiHeaderExtendedHolder parameter = new GiHeaderExtendedHolder();
        parameter.setGiOid(giOid);

        List<GiHeaderExtendedHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

}
