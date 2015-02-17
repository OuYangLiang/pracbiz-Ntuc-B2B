//*****************************************************************************
//
// File Name       :  PopupSupplierServiceImpl.java
// Date Created    :  Sep 13, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 13, 2012 $
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

import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.mapper.PopupSupplierMapper;
import com.pracbiz.b2bportal.core.service.PopupSupplierService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class PopupSupplierServiceImpl implements PopupSupplierService
{

    @Autowired
    PopupSupplierMapper mapper;

    @Override
    public int getCountOfSummary(SupplierExHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }
    

    @Override
    public List<SupplierExHolder> getListOfSummary(SupplierExHolder param)
        throws Exception
    {
        return mapper.getListOfSummary(param);
    }
}
