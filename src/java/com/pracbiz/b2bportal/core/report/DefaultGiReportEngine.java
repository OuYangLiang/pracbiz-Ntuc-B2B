//*****************************************************************************
//
// File Name       :  DefaultGiReportEngine.java
// Date Created    :  Nov 14, 2013
// Last Changed By :  $Author: liyong $
// Last Changed On :  $Date: Nov 14, 2013 3:06:03 PM$
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

import com.pracbiz.b2bportal.core.holder.GiHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public abstract class DefaultGiReportEngine extends
    DefaultReportEngine<GiHolder>
{

    @Override
    protected abstract Map<String, Object> reportParameter(
        ReportEngineParameter<GiHolder> parameter, int flag);

    
    @Override
    protected abstract List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<GiHolder> parameter, int flag);
    
    
}
