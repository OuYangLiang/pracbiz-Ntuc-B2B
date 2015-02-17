//*****************************************************************************
//
// File Name       :  GiService.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 11:41:57 AM $
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
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public interface GiService extends DBActionService<GiHolder>
{
    public GiHolder selectGiByKey(BigDecimal giOid) throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> giOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<GiSummaryHolder> params, boolean storeFlag) throws Exception;
}
