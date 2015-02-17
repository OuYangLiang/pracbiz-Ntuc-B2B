//*****************************************************************************
//
// File Name       :  InvHeaderService.java
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
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface CcHeaderService extends DBActionService<CcHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>, BaseService<CcHeaderHolder>
{
    public CcHeaderHolder selectCcHeaderByKey(BigDecimal invOid) throws Exception;
    
    
    public CcHeaderHolder selectCcHeaderByInvNo(String invNo) throws Exception;
    
    
    public List<CcHeaderHolder> selectCcHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo,
            String storeCode) throws Exception;
    
    
    public CcHeaderHolder selectEffectiveCcHeaderByInNo(BigDecimal buyerOid,
        String buyerSupplierCode, String invNo) throws Exception;
    
    
    public List<CcSummaryHolder> selectAllRecordToExport(CcSummaryHolder param) throws Exception;
}
