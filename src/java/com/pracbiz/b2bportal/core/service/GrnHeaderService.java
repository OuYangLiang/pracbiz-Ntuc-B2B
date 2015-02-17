//*****************************************************************************
//
// File Name       :  GrnHeaderService.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (����, 01 ���� 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface GrnHeaderService extends DBActionService<GrnHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>,BaseService<GrnHeaderHolder>
{
    public GrnHeaderHolder selectGrnHeaderByKey(BigDecimal grnOid) throws Exception;


    public List<GrnHeaderHolder> selectGrnHeaderByPoNoBuyerSupplierAndStoreCode(
            String poNo, BigDecimal buyerOid, BigDecimal supplierOid,
            String receiveStoreCode) throws Exception;
    
    
    public List<GrnHeaderHolder> selectGrnHeadersByPoNoAndStoreCode(
        BigDecimal buyerOid, String buyerGiveSupplierCode, String poNo,
        String storeCode) throws Exception;
    
    
    public List<GrnHeaderHolder> selectGrnHeaderByPoNo(String poNo) throws Exception;
    
    
    public List<GrnHeaderHolder> selectGrnHeadersByPoNo(BigDecimal buyerOid,
        String buyerGiveSupplierCode, String poNo) throws Exception;
    
    
    public Date selectMaxGrnReceiveDateByBuyerSupplierPoReceiveStoreCode(String buyerCode,
            String supplierCode, String poNo, String receiveStoreCode)
            throws Exception;
    
    
    public List<GrnHeaderHolder> selectGrnHeadersByBuyerOidGrnNoAndSupplierCode(
        BigDecimal buyerOid, String grnNo, String supplierCode)
        throws Exception;
    
    
    public GrnHeaderHolder selectGrnHeaderByGrnNo(
        BigDecimal buyerOid, String grnNo, String buyerGivenSupplierCode)
        throws Exception;
    
    
    public List<GrnHeaderHolder> selectGrnHeaderByStoreTypeUserCurrentDay(BigDecimal userOid, BigDecimal buyerOid)throws Exception;
    
    
    
    public List<GrnSummaryHolder> selectAllRecordToExport(GrnSummaryHolder param) throws Exception;
}
