//*****************************************************************************
//
// File Name       :  InvDetailServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-11 $
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
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.mapper.CcDetailMapper;
import com.pracbiz.b2bportal.core.service.CcDetailService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcDetailServiceImpl extends
    DBActionServiceDefaultImpl<CcDetailHolder> implements CcDetailService
{
    @Autowired
    private CcDetailMapper mapper;
    

    @Override
    public List<CcDetailHolder> selectCcDetailByKey(BigDecimal invOid)
        throws Exception
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        CcDetailHolder parameter = new CcDetailHolder();
        parameter.setInvOid(invOid);

        List<CcDetailHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts;
        }

        return null;
    }

    @Override
    public void delete(CcDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public void insert(CcDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKey(CcDetailHolder oldObj_,
        CcDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(CcDetailHolder oldObj_,
        CcDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

}
