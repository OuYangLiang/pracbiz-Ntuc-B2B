package com.pracbiz.b2bportal.core.eai.file.translator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public class FpIdocPoTranslator extends SourceTranslator
{
    private static final Logger log = LoggerFactory.getLogger(FpIdocPoTranslator.class);

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
        XMLOutputter xo = null;
        
        Map<String, byte[]> filesMap = new HashMap<String, byte[]>();
        
        try
        {
            List<String> fileNames = new ArrayList<String>();
            for (Map.Entry<String, byte[]> entry : files.entrySet())
            {
                inputStream = new ByteArrayInputStream(entry.getValue());
                Document document = FileParserUtil.getInstance().build(inputStream);
                
                Element root = document.getRootElement();
               
                List<Element> eles = root.getChildren();
                for (Element element : eles)
                {
                    PoHeaderHolder header = this.supplierCodeAndDocNo(element);
                    String filePath = outPath
                        + File.separator
                        + "PO_"
                        + buyer.getBuyerCode()
                        + "_"
                        + header.getSupplierCode()
                        + "_"
                        + header.getPoNo() + ".xml";
                    
                    Element newEle = element.clone();
                    
                    File file = new File(filePath);
                    
                    
                    if (fileNames.contains(filePath))
                    {
                        List<File> fileList = new ArrayList<File>();
                        String newSourceFile = "source"
                            + DOC_FILENAME_DELIMITOR
                            + DateUtil.getInstance().convertDateToString(new Date(),
                              DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC) 
                            + ".txt";
                        
                        while(FileUtil.getInstance().isFileExist(filePath, newSourceFile))
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
                        
                        Element newElement = new Element(root.getName());
                        newElement.addContent(newEle);
                        Format format = Format.getCompactFormat();
                        format.setEncoding(CommonConstants.ENCODING_UTF8);
                        format.setIndent("  ");
                        xo = new XMLOutputter(format);
                        Document doc = new Document();
                        doc.addContent(newElement);
                        
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();                    
                        try
                        {
                            xo.output(doc, bos);
                            byte[] data = bos.toByteArray();
                            FileUtil.getInstance().writeByteToDisk(data, file.getPath());
                        }
                        finally
                        {
                            bos.close();
                            bos = null;
                        }
                        
                        fileList.add(file);
                        fileList.add(newSrcFile);
                        
                        GZIPHelper.getInstance().doStandardZip(fileList, "PO", buyer.getBuyerCode(), file.getParent());
                    }
                    else
                    {
                        log.info("start to create PO idoc file [" + file.getName() + "]");
                        Element newElement = new Element(root.getName());
                        newElement.addContent(newEle);
                        Format format = Format.getCompactFormat();
                        format.setEncoding(CommonConstants.ENCODING_UTF8);
                        format.setIndent("  ");
                        xo = new XMLOutputter(format);
                        Document doc = new Document();
                        doc.addContent(newElement);
                        
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();                    
                        try
                        {
                            xo.output(doc, bos);
                            byte[] data = bos.toByteArray();
                            
                            filesMap.put(filePath, data);
                            
                            fileNames.add(filePath);
                        }
                        finally
                        {
                            bos.close();
                            bos = null;
                        }
                    }
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
    
    
    private PoHeaderHolder supplierCodeAndDocNo(Element element)throws Exception
    {
        PoHeaderHolder header = new PoHeaderHolder();

        List<Element> e1edk02Elements = element.getChildren("E1EDK02");
        
        Element qualf001 = getElementByChildElement(e1edk02Elements, "QUALF", "001");
        String poNo = null;
        if (qualf001 != null)
        {
            poNo = qualf001.getChildTextTrim("BELNR");
        }
        header.setPoNo(StringUtil.getInstance().convertDocNoWithNoLeading(poNo, LEADING_ZERO));
        List<Element> e1edka1 = element.getChildren("E1EDKA1");
        Element lf = this.getElementByChildElement(e1edka1, "PARVW", "LF");
        
        if (lf != null)
        {
            header.setSupplierCode(StringUtil.getInstance().convertDocNoWithNoLeading(lf.getChildTextTrim("PARTN"), LEADING_ZERO));
        }
        
        return header;
       
    }
    
    private Element getElementByChildElement(List<Element> elements, String childNode, String childNodeValue)
    {
        if (elements == null || elements.isEmpty() || childNode == null || childNode.trim().isEmpty() 
                || childNodeValue == null || childNodeValue.trim().isEmpty())
        {
            return null;
        }
        
        for (Element element : elements)
        {
            String qualf = element.getChildText(childNode);
            if (qualf.trim().isEmpty())
            {
                continue;
            }
            
            if (qualf.trim().equals(childNodeValue.trim()))
            {
                return element;
            }
        }
        
        return null;
    }


}
