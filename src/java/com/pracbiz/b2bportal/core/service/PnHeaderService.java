//*****************************************************************************
//
// File Name       :  PnHeaderService.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-11 $
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

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface PnHeaderService extends DBActionService<PnHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>
{
    public PnHeaderHolder selectPnHeaderByKey(BigDecimal pnOid) throws Exception;
    
    
    public List<PnHeaderHolder> selectPnHeadersByBuyerOidPnNoAndSupplierCode(
        BigDecimal buyerOid, String pnNo, String supplierCode) throws Exception;
    
    
    public PnHeaderHolder selectPnHeaderByPnNo(BigDecimal buyerOid,
        String pnNo, String buyerGivenSupplierCode) throws Exception;
    
    
    public List<PnSummaryHolder> selectAllRecordToExport(PnSummaryHolder param) throws Exception;
}
