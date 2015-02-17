//*****************************************************************************
//
// File Name       :  RtvService.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 11:43:36 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;


import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface RtvService extends DBActionService<RtvHolder>
{
    public RtvHolder selectRtvByKey(BigDecimal rtvOid) throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> rtvOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<RtvSummaryHolder> params, boolean storeFlag) throws Exception;
}
