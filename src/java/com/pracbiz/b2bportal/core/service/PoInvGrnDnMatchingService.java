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
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface PoInvGrnDnMatchingService extends
        BaseService<PoInvGrnDnMatchingHolder>,
        DBActionService<PoInvGrnDnMatchingHolder>,
        PaginatingService<PoInvGrnDnMatchingExHolder>
{
    public PoInvGrnDnMatchingHolder selectByKey(BigDecimal matchingOid)
            throws Exception;

    
    public void moveFile(PoInvGrnDnMatchingHolder holder) throws Exception;


    public byte[] exportExcel(List<BigDecimal> matchingOids, boolean isBuyer)
            throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectAllRecordToExport(PoInvGrnDnMatchingHolder holder);
    
    
    public PoInvGrnDnMatchingHolder selectByPoOidAndStore(BigDecimal poOid,
        String store) throws Exception;
    
    
    public void createNewMatchingRecord(PoInvGrnDnMatchingHolder param) throws Exception;
    
    
    public void updateDocRelationshipsForMatchingRecord(
        PoInvGrnDnMatchingHolder param) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordsWhichCanDoMatching(
        BigDecimal buyerOid, int expiryDays, int toleranceDays) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectVoidInvoiceMatchingRecords(
            BigDecimal buyerOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectOutDatedPoRecords(
            BigDecimal buyerOid) throws Exception;
    
    
    public void deleteRecordByKey(BigDecimal matchingOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectApprovedMatchingRecordByInvStatusActionDateRange(BigDecimal buyerOid,
            Date begin, Date end, BigDecimal supplierOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordByMatchingDateRangeUnionAllPending(BigDecimal buyerOid,
            Date begin, Date end, BigDecimal supplierOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectOutstandingRecords(
            BigDecimal buyerOid, Date reportDate, String moreThanDays, String supplierCode, String supplierName) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectSupplierRejectedRecordsBySupplierStatusActionDateRangeAndBuyer(
            BigDecimal buyerOid, Date begin, Date end, BigDecimal supplierOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectSupplierAcceptedRecordsBySupplierStatusActionDateRangeAndBuyer(
            BigDecimal buyerOid, Date begin, Date end, BigDecimal supplierOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectDiscrepancyRecordByBuyerAndSupplier(
            BigDecimal buyerOid, BigDecimal supplierOid, String discrepancyType, BigDecimal currentUserType) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectMatchingRecordByMatchingDateRangeAndBuyer(
            Date begin, Date end, BigDecimal buyerOid) throws Exception;
    
    
    public PoInvGrnDnMatchingHolder selectEffectiveRecordByInvOid(BigDecimal invOid) throws Exception;
    
    
    public List<PoInvGrnDnMatchingHolder> selectBuyerResolutionRecords(BigDecimal buyerOid,
            Date reportDate, String supplierCode, String supplierName) throws Exception; 
    
    
    public List<PoInvGrnDnMatchingHolder> selectSupplierResolutionRecords(BigDecimal supplierOid) throws Exception; 
    
    
    public void changeInvDateToFirstGrnDate(PoInvGrnDnMatchingHolder holder) throws Exception;
}
