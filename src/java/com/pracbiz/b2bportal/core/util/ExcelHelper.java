package com.pracbiz.b2bportal.core.util;

import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;


public final class ExcelHelper
{
    
    private static ExcelHelper instance;

    private ExcelHelper()
    {
    }

    
    public static ExcelHelper getInstance()
    {
        synchronized(ExcelHelper.class)
        {
            if(instance == null)
            {
                instance = new ExcelHelper();
            }
        }

        return instance;
    }
    
    
    public String getStringCellValue(Cell cell)
    {
        if (cell == null)
        {
            return "";
        }
        DataFormatter formater = new DataFormatter(Locale.US);
        return formater.formatCellValue(cell);
    }

    
    public boolean isEmptyRow(Row row) throws Exception
    {
        int cellCount = row.getPhysicalNumberOfCells();
        if (cellCount == 0)
        {
            return true;
        }
        for (int i = 0; i < cellCount - 1; i++)
        {
            if (!getStringCellValue(row.getCell(i)).trim().isEmpty())
            {
                return false;
            }
        }
        return true;
    }
}
