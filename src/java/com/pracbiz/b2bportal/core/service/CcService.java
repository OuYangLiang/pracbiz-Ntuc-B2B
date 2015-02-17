//*****************************************************************************
//
// File Name       :  CcService.java
// Date Created    :  Jan 2, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 2, 2014 11:41:20 AM $
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
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface CcService extends DBActionService<CcHolder>
{
    public byte[] exportExcel(List<BigDecimal> invOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<CcSummaryHolder> params, boolean storeFlag) throws Exception;
    
    
    public CcHolder selectCcByKey(BigDecimal invOid) throws Exception;
}
