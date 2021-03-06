//*****************************************************************************
//
// File Name       :  DefaultRtvReportEngine.java
// Date Created    :  Dec 12, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 12, 2012 3:07:08 PM$
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

import com.pracbiz.b2bportal.core.holder.RtvHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public abstract class DefaultRtvReportEngine extends DefaultReportEngine<RtvHolder>
{

    @Override
    protected abstract Map<String, Object> reportParameter(
        ReportEngineParameter<RtvHolder> parameter, int flag);
    

    @Override
    protected abstract List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<RtvHolder> parameter, int flag);
   

}
