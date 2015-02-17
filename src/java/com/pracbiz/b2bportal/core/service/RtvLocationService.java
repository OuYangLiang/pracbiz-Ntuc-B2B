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
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReportParameter;
import com.pracbiz.b2bportal.core.report.excel.RtvGiDnReportParameter;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public interface RtvLocationService extends DBActionService<RtvLocationHolder>
{
    public List<RtvLocationHolder> selectRtvLocationByRtvOid(BigDecimal rtvOid) throws Exception;
    
    public List<MissingGiReportParameter> selectMisssingGiReportRecords(BigDecimal buyerOid, String supplierCode, Date begin, Date end) throws Exception;
    
    public List<RtvGiDnReportParameter> selectRtvGiDnWarningReportData(BigDecimal buyerOid, Date searchDate);
}
