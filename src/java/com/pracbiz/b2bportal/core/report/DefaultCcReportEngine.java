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


import com.pracbiz.b2bportal.core.holder.CcHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public abstract class DefaultCcReportEngine extends
    DefaultReportEngine<CcHolder>
{

    @Override
    protected abstract Map<String, Object> reportParameter(
        ReportEngineParameter<CcHolder> parameter, int flag);

    
    @Override
    protected abstract List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<CcHolder> parameter, int flag);
    
    
}
