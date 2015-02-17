package com.pracbiz.b2bportal.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.springframework.util.StringUtils;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public final class FileParserUtil
{
    public static final String RECORD_TYPE_HEADER="HDR";
    public static final String RECORD_TYPE_HEADER_EXTENDED="HEX";
    public static final String RECORD_TYPE_DETAIL="DET";
    public static final String RECORD_TYPE_DETAIL_EXTENDED="DEX";
    public static final String RECORD_TYPE_LOCATION="LOC";
    public static final String RECORD_TYPE_LOCATION_DETAIL_EXTENDED="LEX";
    
    private static FileParserUtil instance;

    private FileParserUtil()
    {

    }

    
    public static FileParserUtil getInstance()
    {
        synchronized(FileParserUtil.class)
        {
            if(instance == null)
            {
                instance = new FileParserUtil();
            }
        }

        return instance;
    }

    
    public String[] readLines(File file) throws IOException
    {
        BufferedReader br = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        List<String> content = new ArrayList<String>();

        try
        {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            br = new BufferedReader(isr);

            String line = null;

            while((line = br.readLine()) != null)
            {
                content.add(line);
            }

        }
        finally
        {
            if(null != br)
            {
                br.close();
                br = null;
            }

            if(null != fis)
            {
                fis.close();
                fis = null;
            }
            
            if(null != isr)
            {
                isr.close();
                isr = null;
            }

        }

        String[] rlt = new String[content.size()];
        content.toArray(rlt);
        return rlt;
    }
    
    
    public List<String> readLines(byte[] bytes) throws IOException
    {
    	InputStream inputStream = new ByteArrayInputStream(bytes);
    	
    	try
    	{
    		return this.readLines(inputStream);
    	}
    	finally
    	{
    		if (null != inputStream)
    		{
    			inputStream.close();
    			inputStream = null;
    		}
    	}
    }
    
    
    public List<String> readLines(InputStream inputStream) throws IOException
    {
        BufferedReader br = null;
        List<String> content = new ArrayList<String>();
        Reader reader = null;
        
        try
        {
            reader = new InputStreamReader(inputStream, CommonConstants.ENCODING_UTF8);
            br = new BufferedReader(reader);

            String line = null;

            while((line = br.readLine()) != null)
            {
                content.add(line);
            }
        }
        finally
        {
            if(null != br)
            {
                br.close();
                br = null;
            }
            if(null != reader)
            {
                reader.close();
                reader = null;
            }
        }

        return content;
    }
    
    
    public int getFilesTotalLines(InputStream inputStream)throws Exception
    {
        LineNumberReader lineReader = null;
        int totalLineNum = 0;
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(inputStream, CommonConstants.ENCODING_UTF8);
            lineReader = new LineNumberReader(reader);

            while(lineReader.readLine() != null)
            {
               //To Do Something
            }
            totalLineNum = lineReader.getLineNumber();
        }
        finally
        {
            if(null != lineReader)
            {
                lineReader.close();
                lineReader = null;
            }
            
            if(null != reader)
            {
                reader.close();
                reader = null;
            }
        }
        return totalLineNum;
    }
    
    
    public List<String> readLinesByLineNum(InputStream inputStream, int startLineNum, int endLineNum) throws IOException
    {
        List<String> content = new ArrayList<String>();
        LineNumberReader lineReader = null;
        Reader reader = null;
        try
        {
            reader = new InputStreamReader(inputStream, CommonConstants.ENCODING_UTF8);
            lineReader = new LineNumberReader(reader);

            String line = null;
           
            while((line = lineReader.readLine()) != null)
            {
                if (lineReader.getLineNumber() >= startLineNum && lineReader.getLineNumber() <= endLineNum)
                {
                    content.add(line);
                }
            }
        }
        finally
        {
            if(null != lineReader)
            {
                lineReader.close();
                lineReader = null;
            }
            
            if(null != reader)
            {
                reader.close();
                reader = null;
            }
        }
        return content;
    }
    
    
    public Map<String,List<String[]>> readLinesAndGroupByRecordType(File file) throws Exception
    {
        Map<String,List<String[]>> map = new HashMap<String, List<String[]>>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try
        {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            br = new BufferedReader(isr);

            String line = null;
            String previousLine = null;
            while((line = br.readLine()) != null)
            {
                if (org.apache.commons.lang.StringUtils.isBlank(line))
                    continue;
                
                this.groupData(line, previousLine, map);
                previousLine = line;
            }

        }
        finally
        {
            if(null != br)
            {
                br.close();
                br = null;
            }

            if(null != isr)
            {
                isr.close();
                isr = null;
            }
            
            if(null != fis)
            {
                fis.close();
                fis = null;
            }

        }
        
        
        return map;
    }
    
    
    public Map<String,List<String[]>> readLinesAndGroupByRecordType(InputStream input) throws Exception
    {
        Map<String,List<String[]>> map = new HashMap<String, List<String[]>>();
        BufferedReader br = null;
        Reader reader = null ;
        
        try
        {
            reader = new InputStreamReader(input, CommonConstants.ENCODING_UTF8);
            br = new BufferedReader(reader);
            
            String line = null;
            String previousLine = null;
            while((line = br.readLine()) != null)
            {
                if (org.apache.commons.lang.StringUtils.isBlank(line))
                    continue;
                
                this.groupData(line, previousLine, map);
                previousLine = line;
            }
            
        }
        finally
        {
            if(null != br)
            {
                br.close();
                br = null;
            }
            if(null != reader)
            {
                reader.close();
                reader = null;
            }
        }
        
        
        return map;
    }
    
    
    public Map<String,List<String[]>> readLinesAndGroupByRecordType(byte[] input) throws Exception
    {
        Map<String,List<String[]>> map = null;
        ByteArrayInputStream is = null;
        
        try
        {
            is = new ByteArrayInputStream(input);
            map = readLinesAndGroupByRecordType(is);
        }
        finally
        {
            if(null != is)
            {
                is.close();
                is = null;
            }
        }
        
        return map;
    }
    
    
    public List<List<String>> readCSVFile(File sourceFile) throws Exception
    {
        ICsvListReader listReader = null;
        List<List<String>> list = new ArrayList<List<String>>();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        
        try
        {
            fis = new FileInputStream(sourceFile);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            listReader = new CsvListReader(isr,
                CsvPreference.STANDARD_PREFERENCE);

            List<String> lineList = null;
            while((lineList = listReader.read()) != null)
            {
                List<String> tmp = new ArrayList<String>();
                tmp.addAll(lineList);
                list.add(tmp);
            }

        }
        finally
        {
            if(listReader != null)
            {
                listReader.close();
                listReader = null;
            }
            
            if(isr != null)
            {
                isr.close();
                isr = null;
            }
            
            if(fis != null)
            {
                fis.close();
                fis = null;
            }
        }
        return list;
    }
    
    
    public Document build(File ebxml) throws Exception
    {
        if (ebxml == null)
        {
            throw new NullPointerException();
        }

        if (!ebxml.exists())
        {
            throw new FileNotFoundException("[" + ebxml.getName()
                    + "] not found.");
        }

        SAXBuilder builder = new SAXBuilder();
        builder.setFeature(
                "http://apache.org/xml/features/allow-java-encodings", true);
        Document document = builder.build(ebxml);

        if (document == null)
        {
            return null;
        }

        return document;
    }
    
    
    public Document build(byte[] input) throws Exception
    {
        ByteArrayInputStream is = null;
        
        try
        {
            is = new ByteArrayInputStream(input);
            return build(is);
        }
        finally
        {
            if(null != is)
            {
                is.close();
                is = null;
            }
        }
    }
    
    
    public Document build(InputStream ebxml) throws Exception
    {
        if (ebxml == null)
        {
            throw new NullPointerException();
        }

        SAXBuilder builder = new SAXBuilder();
        builder.setFeature(
                "http://apache.org/xml/features/allow-java-encodings", true);
        Document document = builder.build(ebxml);

        if (document == null)
        {
            return null;
        }

        return document;
    }
    
    
    private void groupData(String content,String previousLine, Map<String,List<String[]>> map)
    {
         String parts[] = StringUtils.delimitedListToStringArray(content, CoreCommonConstants.VERTICAL_SEPARATE);
        //record type is 'HDR'
        if(parts[0].trim().equals(RECORD_TYPE_HEADER))
        {
            initData(map,RECORD_TYPE_HEADER, parts);
            return;
        }
        
        //record type is 'HEX'
        if(parts[0].trim().equals(RECORD_TYPE_HEADER_EXTENDED))
        {
            initExtensionData(map, RECORD_TYPE_HEADER_EXTENDED, parts);
            return;
        }
        
        //record type is 'DET'
        if(parts[0].trim().equals(RECORD_TYPE_DETAIL))
        {
            initData(map,RECORD_TYPE_DETAIL, parts);
            return;
        }
        
        //record type is 'DEX'
        if(parts[0].trim().equals(RECORD_TYPE_DETAIL_EXTENDED))
        {
            String previousLines[] = StringUtils.delimitedListToStringArray(previousLine, CoreCommonConstants.VERTICAL_SEPARATE);
            this.initExtensionData(map, RECORD_TYPE_DETAIL_EXTENDED, parts, previousLines[2]);
            return;
        }
        
        //record type is 'LOC'
        if(parts[0].trim().equals(RECORD_TYPE_LOCATION))
        {
            initData(map,RECORD_TYPE_LOCATION, parts);
            return;
        }
        
        //record type is 'LEX'
        if(parts[0].trim().equals(RECORD_TYPE_LOCATION_DETAIL_EXTENDED))
        {
            String previousLines[] = StringUtils.delimitedListToStringArray(previousLine, CoreCommonConstants.VERTICAL_SEPARATE);
            this.initExtensionData(map, RECORD_TYPE_LOCATION_DETAIL_EXTENDED, parts, previousLines[2],previousLines[3]);
            return;
        }
    }
    
    
    private void initData(Map<String, List<String[]>> map,String key, String[] parts)
    {
        if (map.containsKey(key))
        {
            List<String[]> datas = map.get(key);
            datas.add(parts);
        }
        else
        {
            List<String[]> datas = new ArrayList<String[]>();
            datas.add(parts);
            map.put(key, datas);
        }
    }
    
    
    private void initExtensionData(Map<String, List<String[]>> map,String key, String[] currentLines, String ... lineSeqs)
    {
        List<String[]> currentDatas = rebuildArray(currentLines, lineSeqs);
        if (map.containsKey(key))
        {
            List<String[]> datas = map.get(key);
            datas.addAll(currentDatas);
        }
        else
        {
            map.put(key, currentDatas);
        }
    }
    
    
    private List<String[]> rebuildArray(String[] content, String ... lineSeqs)
    {
        Integer count = this.integerValueOf(content[1]);
        List<String[]> list = new ArrayList<String[]>();
        for (int i = 0 ; i < count; i ++ )
        {
            Integer lineSeqCount = lineSeqs == null ? 0 : lineSeqs.length;
            String[] array = new String[5 + lineSeqCount];
            for (int j = 0; j < lineSeqCount; j++)
            {
                array[1+j] = lineSeqs[j];
            }
            Integer index = 2 + (lineSeqCount == 0 ? 0 : lineSeqCount - 1);
            array[index] = content[2+(i*3)];
            array[index + 1] = content[3+(i*3)];
            array[index + 2] = content[4+(i*3)];
            list.add(array);
        }
        
        return list;
    }

    
    public boolean writeContent(File file, String content) throws IOException
    {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        String contents = StringUtils.replace(content, "null", "");
        try
        {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, CommonConstants.ENCODING_UTF8);
            bw = new BufferedWriter(osw);

            bw.append(contents);

            return true;
        }
        finally
        {
            if(null != bw)
            {
                bw.flush();
                bw.close();
                bw = null;
            }

            if(null != osw)
            {
                osw.close();
                osw = null;
            }
            
            if(null != fos)
            {
                fos.close();
                fos = null;
            }
        }
    }

    
    public BigDecimal decimalValueOf(String value)
    {
        if(null == value)
        {
            return null;
        }

        if(value.trim().isEmpty())
        {
            return null;
        }

        return new BigDecimal(value.trim());
    }

    
    public Date dateValueOf(String value, String fmt)
    {
        if(null == value)
        {
            return null;
        }

        if(value.trim().isEmpty())
        {
            return null;
        }

        return DateUtil.getInstance().convertStringToDate(value, fmt);
    }

    
    public Integer integerValueOf(String value)
    {
        if(null == value)
        {
            return null;
        }

        if(value.trim().isEmpty())
        {
            return null;
        }

        return Integer.parseInt(value.trim());
    }

    
    public void addError(List<String> list, String error)
    {
        if(null == error)
        {
            return;
        }

        list.add(error);
    }
}
