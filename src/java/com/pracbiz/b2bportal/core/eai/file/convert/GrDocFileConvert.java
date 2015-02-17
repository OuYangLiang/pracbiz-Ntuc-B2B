//*****************************************************************************
//
// File Name       :  GiDocFileConvert.java
// Date Created    :  Nov 12, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 12, 2013 3:40:46 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.convert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.apache.commons.io.FileUtils;




import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GrDocFileConvert implements CoreCommonConstants
{
    public List<File> createFile(File filePath, Map<String, List<String>> map, BuyerHolder buyer, String sourceContent) throws Exception
    {
        List<File> fileLst = new ArrayList<File>();
        String fileName = null;
        
        for (Map.Entry<String, List<String>> entry : map.entrySet())
        {
            fileName = filePath
                + File.separator
                + "GRN_"
                + buyer.getBuyerCode()
                + "_"
                + StringUtil.getInstance().convertDocNoWithNoLeading(entry.getValue().get(0).substring(58, 68).trim(), LEADING_ZERO)
                + "_"
                + StringUtil.getInstance().convertDocNoWithNoLeading(
                    entry.getValue().get(0).substring(2, 12).trim(), LEADING_ZERO) + ".txt";
            
            File file = new File(fileName);
            if (file.exists())
            {
                List<File> files = new ArrayList<File>();
                String newSourceFile = "source"
                    + DOC_FILENAME_DELIMITOR
                    + DateUtil.getInstance().convertDateToString(new Date(),
                      DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                    + ".txt";
                
                while(FileUtil.getInstance().isFileExist(filePath.getPath(), newSourceFile))
                {
                    newSourceFile = "source"
                        + DOC_FILENAME_DELIMITOR
                        + DateUtil.getInstance().convertDateToString(new Date(),
                          DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                        + ".txt";
                }
                
                File newSrcFile = new File(newSourceFile);
                byte[] bytes = sourceContent.getBytes(CommonConstants.ENCODING_UTF8);
                FileUtil.getInstance().writeByteToDisk(bytes, newSrcFile.getPath());
                
                files.add(file);
                files.add(newSrcFile);
                GZIPHelper.getInstance().doStandardZip(files, "GRN", buyer.getBuyerCode(), filePath.getPath());
            }
            
            //****** write target file *********
            StringBuffer contents = new StringBuffer();
            for (String content : entry.getValue())
            {
                contents.append(content);
            }
            
            byte[] data = contents.toString().getBytes(CommonConstants.ENCODING_UTF8);
            FileUtil.getInstance().writeByteToDisk(data, fileName);
            
            if (!fileLst.contains(file))
            {
                fileLst.add(file);
            }
        }
        
        return fileLst;
    }

    public Map<String, List<String>> readFile(File file) throws Exception
    {
        InputStream inputStream = null;
        Map<String , byte[]> fileMap = new HashMap<String, byte[]>();
        if (file.getName().endsWith(".zip"))
        {
            fileMap = GZIPHelper.getInstance().readFileFromZip(file);
        }
        else
        {
            fileMap.put(file.getName(), FileUtils.readFileToByteArray(file));
        }
        
        
        Map<String, List<String>> grMap = new HashMap<String, List<String>>();
        for (Map.Entry<String, byte[]> entry : fileMap.entrySet())
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
        
        return grMap;
    }
}
