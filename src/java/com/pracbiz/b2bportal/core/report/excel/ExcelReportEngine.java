package com.pracbiz.b2bportal.core.report.excel;

import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public abstract class ExcelReportEngine<T>
{
    public static final int EXCEL_TYPE_STANDARD = 0;
    public static final int EXCEL_TYPE_IS_STORE = 1;
    
    protected abstract void generateWorkSheet(WritableWorkbook wwb, ReportEngineParameter<T> parameter, int sheetIndex, int flag) throws Exception;
    
}
