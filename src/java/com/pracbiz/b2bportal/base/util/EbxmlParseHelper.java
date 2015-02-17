package com.pracbiz.b2bportal.base.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaderJDOMFactory;
import org.jdom2.input.sax.XMLReaderXSDFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EbxmlParseHelper
{
    private final static Logger log = LoggerFactory.getLogger(EbxmlParseHelper.class);
    private static EbxmlParseHelper instance;


    private EbxmlParseHelper()
    {
    }


    public static EbxmlParseHelper getInstance()
    {
        synchronized (EbxmlParseHelper.class)
        {
            if (instance == null)
            {
                instance = new EbxmlParseHelper();
            }
        }

        return instance;
    }


    public Object objectClone(Object originObj) throws IOException, ClassNotFoundException
    {
        ByteArrayOutputStream bao = null;
        ByteArrayInputStream bai = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try
        {
            bao = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bao);
            oos.writeObject(originObj);
            oos.flush();
            oos.close();
            bai = new ByteArrayInputStream(bao.toByteArray());
            ois = new ObjectInputStream(bai);
            Object obj = ois.readObject();
            ois.close();
            oos = null;
            ois = null;
            return obj;
        }
        finally
        {
            if (bao != null)
            {
                bao.close();
                bao = null;
            }
            if (bai != null)
            {
                bai.close();
                bai = null;
            }
        }
    }


    // ****************************************************************
    // ebxml transformer used
    // ****************************************************************
    public BigDecimal bigDecimalFormater(BigDecimal value, int length)
    {
        if (value == null)
        {
            return null;
        }

        return value.divide(BigDecimal.ONE, length, BigDecimal.ROUND_HALF_UP);
    }


    public String dateFormater(Date date)
    {
        if (date == null)
        {
            return null;
        }

        return DateUtil.getInstance().convertDateToString(date,
                DateUtil.DEFAULT_DATE_FORMAT)
                + "T00:00:00";
    }


    /*public Map bigDecimalAndDateFormater(Map map)
    {
        if (map == null || map.isEmpty())
        {
            return null;
        }

        Map rlt = new HashMap();
        for (Iterator it = map.entrySet().iterator(); it.hasNext();)
        {
            Map.Entry obj = (Map.Entry) it.next();
            Object key = obj.getKey();
            Object value = obj.getValue();

            if (value instanceof Date)
            {
                rlt.put(key, dateFormater((Date) value));
                continue;
            }
            else if (value instanceof BigDecimal)
            {
                rlt.put(key, bigDecimalFormater((BigDecimal) value, 6));
                continue;
            }

            rlt.put(key, value);
        }

        map.clear();

        return rlt;
    }*/


    public String obtainValueFrom(Document doc, String location)
    {
        String result = JdomXmlHelper.getInstance().obtainValueFrom(doc,
                location);

        if (result == null || "".equals(result.trim())
                || "-".equals(result.trim()) || "---".equals(result.trim()))
        {
            return null;
        }
        else
        {
            return result.trim();
        }
    }


    public String obtainValueFrom(Document doc, String location,
            String namespace, String childNode)
    {
        Namespace xmlns = Namespace.getNamespace(namespace);

        String result = JdomXmlHelper.getInstance().obtainValueFrom(doc,
                location, childNode, xmlns);

        if (result != null
                && ("".equals(result.trim()) || "-".equals(result.trim())
                        || ".".equals(result.trim()) || "-.".equals(result
                        .trim()))) result = null;

        if (result == null)
        {
            return null;
        }
        else
        {
            return result.trim();
        }
    }


    public BigDecimal convertStringToBigDecimal(String value, int length)
    {
        if (value == null || "".equals(value.trim()))
        {
            return BigDecimal.ZERO.divide(BigDecimal.ONE, length,
                    BigDecimal.ROUND_HALF_UP);
        }

        return new BigDecimal(value).divide(BigDecimal.ONE, length,
                BigDecimal.ROUND_HALF_UP);

    }


    public Date convertStringToDate(String value, String format)
    {
        if (value == null || "-".equals(value))
        {
            return null;
        }

        Date date = null;
        if (value.contains("T"))
        {
            date = DateUtil.getInstance().convertStringToDate(
                    value.replaceAll("T", " "), format);
        }
        else
        {
            date = DateUtil.getInstance().convertStringToDate(value, format);
        }

        return date;
    }
    
    
    public Date obtainDateFrom(Document doc, String location, String format)
    {
        String value = obtainValueFrom(doc, location);
        return convertStringToDate(value, format);
    }
    
    
    public BigDecimal obtainBigDecimalFrom(Document doc, String location, int length)
    {
        String value = obtainValueFrom(doc, location);
        return convertStringToBigDecimal(value, length);
    }

    
    public boolean validateEbxml(File xformFile, String filename)
    {
        if (xformFile == null || filename == null)
        {
            return true;
        }
        
        FileInputStream input = null;
        
        try
        {
            input = new FileInputStream(xformFile);
            return validateEbxml(input, filename);
        }
        catch (FileNotFoundException e1)
        {
            return false;
        }
        finally
        {
            try
            {
                if (null != input)
                	input.close();
            }
            catch (IOException e)
            {
                
            }
        }
    }
    
    public boolean validateEbxml(InputStream inputStream, String filename)
    {
        if (filename == null)
        {
            return true;
        }

        String schemaLocal = this.getClass().getClassLoader()
                .getResource(filename).getPath();

        File xsdfile = new File(schemaLocal);
        XMLReaderJDOMFactory schemafac = null;
        try
        {
            schemafac = new XMLReaderXSDFactory(xsdfile);
        }
        catch (JDOMException e)
        {
            ErrorHelper.getInstance().logError(log, e);
            log.error(" :::: Failed to init the XSD Schema [ "
                    + schemaLocal + "].");
            return false;
        }
        SAXBuilder builder = new SAXBuilder(schemafac);
        
        try
        {
            builder.build(inputStream);
            log.info("Validate ebxml file successfully");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            log.error(" :::: Use the schema (XSD) validation XML file is failure.");
            return false;
        }

        return true;
    }
    
    
    public String convertValue(String input, int size, String replace)
    {
        StringBuffer replaceStr = new StringBuffer();
        for (int i = 0; i < size; i++)
        {
            replaceStr.append(replace);
        }
        if (input == null || input.trim().isEmpty())
        {
            return replaceStr.toString();
        }
        return input;
    }
    
    
    public String trimString(String input)
    {
        if (input == null || input.equals("-") || input.equals("---"))
        {
            return null;
        }
        return input;
    }
}
