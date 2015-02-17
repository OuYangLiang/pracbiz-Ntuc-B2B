//*****************************************************************************
//
// File Name       :  DefaultCcReportEngine.java
// Date Created    :  2013-12-25
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: 2013-12-25 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.core.holder.SalesHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public abstract class DefaultSalesReportEngine extends
    DefaultReportEngine<SalesHolder>
{

    @Override
    protected abstract Map<String, Object> reportParameter(
        ReportEngineParameter<SalesHolder> parameter, int flag);

    
    @Override
    protected abstract List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<SalesHolder> parameter, int flag);
    
    
}
