//*****************************************************************************
//
// File Name       :  DefaultInvReportEngine.java
// Date Created    :  Dec 12, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 12, 2012 3:07:51 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import java.util.List;
import java.util.Map;


import com.pracbiz.b2bportal.core.holder.CnHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public abstract class DefaultCnReportEngine extends DefaultReportEngine<CnHolder>
{

    @Override
    protected abstract Map<String, Object> reportParameter(
        ReportEngineParameter<CnHolder> parameter, int flag);
    

    @Override
    protected abstract List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<CnHolder> parameter, int flag);

}
