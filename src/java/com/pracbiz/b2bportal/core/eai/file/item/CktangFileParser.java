package com.pracbiz.b2bportal.core.eai.file.item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

import com.pracbiz.b2bportal.base.action.BaseAction;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.ExcelHelper;


public class CktangFileParser extends FileParser implements CoreCommonConstants
{
    public String processFormat()
    {
        return "CKTANG";
    }
    
    
    public String itemInFileContent(String fileFormat)
    {
        if (fileFormat == null || fileFormat.trim().isEmpty())
        {
            return null;
        }
        if (!processFormat().equalsIgnoreCase(fileFormat))
        {
            return this.successor.itemInFileContent(fileFormat);
        }
        return "application/vnd.ms-excel";
    }

    
    public void translate(DocMsg doc) throws Exception
    {
        if (doc.getInputFormat() != null && !processFormat().equalsIgnoreCase(doc.getInputFormat()))
        {
            this.successor.translate(doc);
            return;
        }
        String excelFileName = new File(doc.getBatch().getContext().getWorkDir(), doc.getOriginalFilename()).getAbsolutePath();
        File csvFile = new File(doc.getContext().getWorkDir(), FileUtil.getInstance().replaceFileNameExtension(doc.getOriginalFilename(), "csv"));
        String csvFileName = csvFile.getAbsolutePath();
        excelTransToCsv(excelFileName, csvFileName);
        doc.setTargetFilename(csvFile.getName());
    }

    
    private void excelTransToCsv(String xlsFileName, String csvFileName)
            throws Exception
    {
        ICsvMapWriter writer = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try
        {
            fos = new FileOutputStream(csvFileName);
            osw = new OutputStreamWriter(fos, CommonConstants.ENCODING_UTF8);
            writer = new CsvMapWriter(osw,
                    CsvPreference.EXCEL_PREFERENCE);
            String[] header = obtainCsvHeader(xlsFileName);
            writer.writeHeader(header);
            Map<Integer, Map<String, String>> map = readExcel(xlsFileName, header);
            for (Map.Entry<Integer, Map<String, String>> entry : map.entrySet())
            {
                Map<String, String> rowMap = entry.getValue();
                writer.write(rowMap, header);
            }
        }
        finally
        {
            
            if (writer != null)
            {
                writer.close();
            }
            if (osw != null)
            {
                osw.close();
            }
            if (fos != null)
            {
                fos.close();
            }
        }
    }
    
    private String[] obtainCsvHeader(String xlsFileName) throws Exception
    {
        String[] header = null;

        Workbook wb = WorkbookFactory.create(new FileInputStream(xlsFileName));
        
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        header = new String[row.getPhysicalNumberOfCells()];
        for (int i = 0; i < header.length; i++)
        {
            header[i] = ExcelHelper.getInstance().getStringCellValue(row.getCell(i));
        }

        return header;
    }


    private Map<Integer, Map<String, String>> readExcel(String xlsFileName, String[] header)
            throws Exception
    {
        Map<Integer, Map<String, String>> map = new HashMap<Integer, Map<String, String>>();
        Workbook wb = WorkbookFactory.create(new FileInputStream(xlsFileName));
        for (int sheet = 0; sheet < wb.getNumberOfSheets(); sheet++)
        {
            Sheet s = wb.getSheetAt(sheet);
            if (s.getPhysicalNumberOfRows() == 0)
            {
                continue;
            }
            for (int i = 1; i < s.getPhysicalNumberOfRows(); i++)//read from row 2.
            {
                Row row = s.getRow(i);
                if (ExcelHelper.getInstance().isEmptyRow(row))
                {
                    continue;
                }
                
                Map<String, String> rowMap = new HashMap<String, String>();
                for (int j = 0; j < header.length; j++)
                {
                    String content = "";
                    if (row.getPhysicalNumberOfCells() >= j + 1)
                    {
                        content = ExcelHelper.getInstance().getStringCellValue(row.getCell(j));
                    }
                    if (content == null
                            || "".equals(content.trim()))
                    {
                        rowMap.put(header[j], "");
                    }
                    else
                    {   
                        rowMap.put(header[j], content.trim());
                    }   
                }
                map.put(row.getRowNum() + 1, rowMap);
            }
        }

        return new TreeMap<Integer, Map<String, String>>(map);
    }
    
    @Override
    public List<String> validateItemInFile(String fileFormat, File file, String fileName, BaseAction action) throws Exception
    {
        if (fileFormat != null && !processFormat().equalsIgnoreCase(fileFormat))
        {
            return this.successor.validateItemInFile(fileFormat, file, fileName, action);
        }
        
        List<String> errors = new ArrayList<String>();
        boolean flag = false;
        if (!(fileName.endsWith(".xls") || fileName.endsWith(".xlsx")))
        {
            errors.add(action.getText("B2BPC2803"));
            flag = true;
        }
        if (!flag)
        {
            String[] header = obtainCsvHeader(file.getAbsolutePath());
            if (header.length < 4)
            {
                errors.add(action.getText("B2BPC2817"));
            }
            Map<Integer, Map<String, String>> map = readExcel(file.getAbsolutePath(), header);
            if (!flag)
            {
                if (map.isEmpty())
                {
                    errors.add(action.getText("B2BPC2816"));
                }
                for (Map.Entry<Integer, Map<String, String>> entry : map.entrySet())
                {
    				Map<String, String> rowMap = entry.getValue();
                    String barCode = rowMap.get(header[0]).trim();
                    String ean = rowMap.get(header[1]).trim();
                    String vendorDesc = rowMap.get(header[2]).trim();
                    String price = rowMap.get(header[3]).trim();
                    if (!Pattern.matches("\\d{13}", barCode))
                    {
                        errors.add(action.getText("B2BPC2804", new String[]{String.valueOf(entry.getKey())}));
                    }
                    if (!Pattern.matches("^[0-9,a-z,A-Z]{13}$", ean))
                    {
                        errors.add(action.getText("B2BPC2805", new String[]{String.valueOf(entry.getKey())}));
                    }
                    if (vendorDesc.trim().length() > 44)
                    {
                    	errors.add(action.getText("B2BPC2806", new String[]{String.valueOf(entry.getKey())}));
                    }
                    try
                    {
                    	if (!price.isEmpty() && (new BigDecimal(price)).compareTo(BigDecimal.ZERO) < 0)
                    	{
                    		errors.add(action.getText("B2BPC2807", new String[]{String.valueOf(entry.getKey())}));
                    	}
                    }
                    catch (Exception e)
                    {
                    	errors.add(action.getText("B2BPC2807", new String[]{String.valueOf(entry.getKey())}));
                    }
                }
            }
        }
        return errors;
    }
}
