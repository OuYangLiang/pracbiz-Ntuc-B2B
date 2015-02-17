package com.pracbiz.b2bportal.core.eai.file.translator;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public class FpGrTranslator extends SourceTranslator
{

    @Override
    protected Map<String, List<String>> validateFile(Map<String, byte[]> files)
        throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Map<String, byte[]> translate(Map<String, byte[]> files,
        String outPath, BuyerHolder buyer, File sourceFile)
        throws Exception
    {
        InputStream inputStream = null;
        String fileName = null;
        Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
        
        try
        {
            Map<String, List<String>> grMap = new HashMap<String, List<String>>();
            for (Map.Entry<String, byte[]> entry : files.entrySet())
            {
                inputStream = new ByteArrayInputStream(entry.getValue());
                List<String> contents = FileParserUtil.getInstance().readLines(inputStream);
                for (String content : contents)
                {
                    if (content.trim().isEmpty())
                    {
                        continue;
                    }
                    String grNo = content.substring(2, 12).trim();
                    String supplierCode = content.substring(58, 68).trim();
                    String key = grNo + "@@" + supplierCode + entry.getKey();
                    
                    if (grMap.containsKey(key))
                    {
                        grMap.get(key).add(content + END_LINE);
                    }
                    else
                    {
                        List<String> lst = new ArrayList<String>();
                        lst.add(content + END_LINE);
                        grMap.put(key, lst);
                    }
                }
            }
            
            List<String> fileNames = new ArrayList<String>();
            
            for (Map.Entry<String, List<String>> entry : grMap.entrySet())
            {
                fileName = outPath
                    + File.separator
                    + "GRN_"
                    + buyer.getBuyerCode()
                    + "_"
                    + StringUtil.getInstance().convertDocNoWithNoLeading(entry.getValue().get(0).substring(58, 68).trim(), LEADING_ZERO)
                    + "_"
                    + StringUtil.getInstance().convertDocNoWithNoLeading(
                        entry.getValue().get(0).substring(2, 12).trim(), LEADING_ZERO) + ".txt";
                
                
                if (fileNames.contains(fileName))
                {
                    List<File> fileList = new ArrayList<File>();
                    String newSourceFile = "source"
                        + DOC_FILENAME_DELIMITOR
                        + DateUtil.getInstance().convertDateToString(new Date(),
                          DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                        + ".txt";
                    
                    while(FileUtil.getInstance().isFileExist(outPath, newSourceFile))
                    {
                        newSourceFile = "source"
                            + DOC_FILENAME_DELIMITOR
                            + DateUtil.getInstance().convertDateToString(new Date(),
                              DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                            + ".txt";
                    }
                    
                    File newSrcFile = new File(newSourceFile);
                    byte[] bytes = sourceFile.getName().getBytes(CommonConstants.ENCODING_UTF8);
                    FileUtil.getInstance().writeByteToDisk(bytes, newSrcFile.getPath());
                    
                    File file = new File(fileName);
                    
                    StringBuffer contents = new StringBuffer();
                    for (String content : entry.getValue())
                    {
                        contents.append(content);
                    }
                    
                    FileUtil.getInstance().writeByteToDisk(contents.toString().getBytes(CommonConstants.ENCODING_UTF8), file.getPath());
                    
                    fileList.add(file);
                    fileList.add(newSrcFile);
                    GZIPHelper.getInstance().doStandardZip(fileList, "GRN", buyer.getBuyerCode(), outPath);
                }
                else
                {
                    //****** write target file *********
                    StringBuffer contents = new StringBuffer();
                    for (String content : entry.getValue())
                    {
                        contents.append(content);
                    }
                    
                    byte[] data = contents.toString().getBytes(CommonConstants.ENCODING_UTF8);
                    
                    filesMap.put(fileName, data);
                    
                    fileNames.add(fileName);
                }
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        return filesMap;
    }

}
