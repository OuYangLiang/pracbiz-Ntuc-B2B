//*****************************************************************************
//
// File Name       :  DictionaryWordServiceImpl.java
// Date Created    :  Aug 30, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Aug 30, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.DictionaryWordHolder;
import com.pracbiz.b2bportal.core.mapper.DictionaryWordMapper;
import com.pracbiz.b2bportal.core.service.DictionaryWordService;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class DictionaryWordServiceImpl extends
    DBActionServiceDefaultImpl<DictionaryWordHolder> implements
    DictionaryWordService
{
    @Autowired
    private DictionaryWordMapper mapper;
    
    @Override
    public List<DictionaryWordHolder> select(DictionaryWordHolder param) throws Exception
    {
        return mapper.select(param);
    }

    
    @Override
    public void insert(DictionaryWordHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }
    

    @Override
    public void updateByPrimaryKeySelective(DictionaryWordHolder oldObj_,
        DictionaryWordHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    
    @Override
    public void updateByPrimaryKey(DictionaryWordHolder oldObj_,
        DictionaryWordHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    
    @Override
    public void delete(DictionaryWordHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    
    @Override
    public List<DictionaryWordHolder> selectAllDictionaryWords()
        throws Exception
    {
        DictionaryWordHolder param = new DictionaryWordHolder();

        List<DictionaryWordHolder> rlt = this.select(param);

        return rlt;
    }
}
