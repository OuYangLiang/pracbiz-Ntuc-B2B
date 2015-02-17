//*****************************************************************************
//
// File Name       :  GrnService.java
// Date Created    :  Aug 3, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 3, 2013 10:44:44 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;




import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface GrnService extends BaseService<GrnHolder>,
    DBActionService<GrnHolder>
{
    public GrnHolder selectByKey(BigDecimal grnOid) throws Exception;
    
    
    public BigDecimal computeGrnAmtByGrnOidAndPoOid(BigDecimal grnOid, BigDecimal poOid) throws Exception;
    
    
    public void updateGrn(GrnHolder grn)throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> invOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<GrnSummaryHolder> params, boolean storeFlag) throws Exception;
}
