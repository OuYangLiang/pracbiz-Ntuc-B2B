//*****************************************************************************
//
// File Name       :  PoLocationService.java
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

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public interface SalesLocationService extends BaseService<SalesLocationHolder> ,DBActionService<SalesLocationHolder>
{
    public List<SalesLocationHolder> selectSalesLocationByKey(BigDecimal salesOid) throws Exception;
}
