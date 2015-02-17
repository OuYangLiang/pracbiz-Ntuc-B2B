//*****************************************************************************
//
// File Name       :  DefaultReportEngine.java
// Date Created    :  Dec 12, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 12, 2012 2:57:28 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;

import com.pracbiz.b2bportal.core.util.PdfReportUtil;

/**
 * TODO To provide an overview of this class.
 * class parameter T will be replaced with concrete holder class,
 * e.g. POHolder, GrnHolder, etc.
 *
 * @author ouyang
 */
public abstract class DefaultReportEngine<T>
{
    public static final int PDF_TYPE_STANDARD = 0;
    public static final int PDF_TYPE_BY_ITEM = 1;
    public static final int PDF_TYPE_BY_STORE = 2;
    public static final int PDF_TYPE_BY_STORE_STORE = 3;
    
    public byte[] generateReport(ReportEngineParameter<T> parameter, int flag)
        throws JRException, IOException
    {
        return PdfReportUtil.getInstance().generatePdf(
            reportTemplate(parameter.getData(), flag), reportDatasource(parameter, flag),
            reportParameter(parameter, flag));
    }
    
    
    protected abstract String reportTemplate(T data, int isForStore);
    
    protected abstract Map<String, Object> reportParameter(ReportEngineParameter<T> parameter, int flag);
    
    protected abstract List<Map<String, Object>> reportDatasource(ReportEngineParameter<T> parameter,int flag);
}
