//*****************************************************************************
//
// File Name       :  PoLocationService.java
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
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReportParameter;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface PoLocationService extends DBActionService<PoLocationHolder>
{
    public List<PoLocationHolder> selectLocationsByPoOid(BigDecimal poOid) throws Exception;
    
    
    public List<PoLocationHolder> selectOptionalLocations(PoLocationHolder parameter) throws Exception;
    
    
    public List<PoLocationHolder> selectOptionalLocationsByPoOid(BigDecimal poOid) throws Exception;
    
    
    public List<PoLocationHolder> selectLocationsByPoOidAndStoreCode(BigDecimal poOid, String locationCode) throws Exception;

    
    public PoLocationHolder selectLocationByPoOidAndLineSeqNo(BigDecimal poOid, int lineSeqNo)throws Exception;
    
    
    public List<MissingGrnReportParameter> selectMissingGrnReprotRecords(BigDecimal buyerOid, String supplierCode, Date begin, Date end)throws Exception;
}
