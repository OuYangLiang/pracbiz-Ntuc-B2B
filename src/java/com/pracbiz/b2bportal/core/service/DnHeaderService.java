//*****************************************************************************
//
// File Name       :  DnHeaderService.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2014-04-18 $
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
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface DnHeaderService extends BaseService<DnHeaderHolder>,
    PaginatingService<MsgTransactionsExHolder>, DBActionService<DnHeaderHolder>
{
    public DnHeaderHolder selectDnHeaderByKey(BigDecimal dnOid) throws Exception;
    
    
    public DnHeaderHolder selectDnHeaderByBuyerSupplierAndInvNo(
            BigDecimal buyerOid, BigDecimal supplierOid, String invNo)
            throws Exception;
    
    
    public List<DnHeaderHolder> selectDnHeadersByBuyerOidInvNoAndSupplierCode(
        BigDecimal buyerOid, String invNo, String supplierCode)
        throws Exception;  
    
    
    public DnHeaderHolder selectDnHeaderByInvNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String invNo) throws Exception;
    
    
    public DnHeaderHolder selectDnHeaderByDnNo(String dnNo) throws Exception;
    
    
    public List<DnHeaderHolder> selectDnHeadersByBuyerOidDnNoAndSupplierCode(
        BigDecimal buyerOid, String dnNo, String supplierCode)
        throws Exception;  
    
    
    public DnHeaderHolder selectDnHeaderByDnNo(
        BigDecimal buyerOid, String dnNo, String buyerGivenSupplierCode) throws Exception;
    
    
    public List<DnHeaderHolder> selectNoDisputeDnHeadersByBuyerAndBufferingDays(
            BigDecimal buyerOid, int bufferingDays) throws Exception;
    
    
    public List<DnHeaderHolder> selectDisputedAndAuditUnfinishedRecord(BigDecimal buyerOid, BigDecimal supplierOid) throws Exception;
    
    
    public List<DnHeaderHolder> selectClosedDnRecordsBySupplierAndTimeRange(BigDecimal supplierOid, Date begin, Date end) throws Exception;
    
    
    public List<DnHeaderHolder> selectResolutionRecordsByBuyer(BigDecimal buyerOid,
        Date reportDate, String supplierCode, String supplierName) throws Exception;
    
    
    public DnHeaderHolder selectDnHeaderByRtvNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String rtvNo) throws Exception;
    
    
    public List<DnHeaderHolder> selectOutstandingRecordsByBuyer(BigDecimal buyerOid, 
        String supplierCode, String supplierName, String moreThanDays, Date reportDate) throws Exception;
    
    
    public DnHeaderHolder selectEffectiveDnHeaderByOriginalFilename(String originalFilename) throws Exception;
    
    
    public DnHeaderHolder selectDnHeaderByGiNo(BigDecimal buyerOid,
        String buyerGivenSupplierCode, String giNo) throws Exception;
    
    
    public List<DnHeaderHolder> selectExportedDnByBuyerAndSupplierAndTimeRange(BigDecimal buyerOid, BigDecimal supplierOid, Date begin, Date end) throws Exception;
    
    
    public List<DnSummaryHolder> selectAllRecordToExport(DnSummaryHolder param) throws Exception;
    
}
