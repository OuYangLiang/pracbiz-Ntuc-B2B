//*****************************************************************************
//
// File Name       :  CountryServiceImpl.java
// Date Created    :  Sep 21, 2012
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
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

import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.mapper.CountryMapper;
import com.pracbiz.b2bportal.core.service.CountryService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class CountryServiceImpl implements CountryService
{
    @Autowired 
    private CountryMapper mapper;
    
    @Override
    public List<CountryHolder> select(CountryHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public CountryHolder selectByCtryCode(String ctryCode) throws Exception
    {
        if (null == ctryCode)
        {
            throw new IllegalArgumentException();
        }

        CountryHolder param = new CountryHolder();
        param.setCtryCode(ctryCode);

        List<CountryHolder> rlt = this.select(param);

        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }

        return null;
    }
}
