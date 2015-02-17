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

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface InvHeaderService extends DBActionService<InvHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>, BaseService<InvHeaderHolder>
{
    public InvHeaderHolder selectInvHeaderByKey(BigDecimal invOid) throws Exception;
    

    public InvHeaderHolder selectInvHeaderByPoOidAndStoreCode(BigDecimal poOid,
            String shipToCode) throws Exception;
    
    
    public InvHeaderHolder selectInvHeaderByPoOid(BigDecimal poOid) throws Exception;
    
    
    public List<InvHeaderHolder> selectInvHeadersByPoOid(BigDecimal poOid) throws Exception;

    
    public List<InvHeaderHolder> selectInvToGenerateDn(
            BigDecimal buyerOid, int maxBufferDays, int minBufferDays) throws Exception;
    
    
    public InvHeaderHolder selectInvHeaderByInvNo(String invNo) throws Exception;
    
    
    public InvHeaderHolder selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo, String storeCode)
            throws Exception;
    
    
    public List<InvHeaderHolder> selectInvHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo,
            String storeCode) throws Exception;
    
    
    public InvHeaderHolder selectEffectiveInvHeaderByInNo(BigDecimal buyerOid,
        String buyerSupplierCode, String invNo) throws Exception;
    
    
    public void voidInvoice(BigDecimal invOid, CommonParameterHolder cp) throws Exception;
    
    
    public List<InvSummaryHolder> selectAllRecordToExport(InvSummaryHolder param) throws Exception;
}
