//*****************************************************************************
//
// File Name       :  PnService.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 11:44:39 AM $
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
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface PnService extends DBActionService<PnHolder>
{
    public PnHolder selectPnByKey(BigDecimal pnOid) throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> pnOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<PnSummaryHolder> params, boolean storeFlag) throws Exception;
}
