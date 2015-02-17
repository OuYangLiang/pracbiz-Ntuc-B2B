package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

public final class ExcelExportGenerator
{
    private static ExcelExportGenerator instance;
    private ExcelExportGenerator(){}
    
    public static ExcelExportGenerator getInstance()
    {
        synchronized(ExcelExportGenerator.class)
        {
            if (instance == null)
            {
                instance = new ExcelExportGenerator();
            }
        }
        
        return instance;
    }
    
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public byte[] generateExcel(List<ExcelReportEngine> engines, List<ReportEngineParameter> parameters, int flag) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for (int i = 0; i < engines.size(); i++)
        {
            engines.get(i).generateWorkSheet(wwb, parameters.get(i), sheetIndex, flag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }
}
