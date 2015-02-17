//*****************************************************************************
//
// File Name       :  AuditAccessService.java
// Date Created    :  Sep 26, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 26, 2012 $
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
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface PoInvGrnDnMatchingGrnService extends
        BaseService<PoInvGrnDnMatchingGrnHolder>,
        DBActionService<PoInvGrnDnMatchingGrnHolder>
{
    public List<PoInvGrnDnMatchingGrnHolder> selectByMatchOid(
            BigDecimal matchOid) throws Exception;
    
    
    public int selectUnclosedMatchingForDisputeGrn(int toleranceDays, BigDecimal grnOid)throws Exception;
}
