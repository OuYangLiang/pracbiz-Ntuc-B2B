package com.pracbiz.b2bportal.core.eai.file.convert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import org.apache.commons.io.FileUtils;
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
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class PoIdocFileCovert implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(PoIdocFileCovert.class);
    
    public void createFile(String filePath, Element element, String rootElement, List<File> rlt, BuyerHolder buyer, String sourceContent) throws Exception
    { 
        XMLOutputter xo = null;
        OutputStream os = null;
        try
        {
              File file = new File(filePath);
              if (file.exists())
              {
                  List<File> files = new ArrayList<File>();
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
                  byte[] bytes = sourceContent.getBytes(CommonConstants.ENCODING_UTF8);
                  FileUtil.getInstance().writeByteToDisk(bytes, newSrcFile.getPath());
                  
                  files.add(file);
                  files.add(newSrcFile);
                  
                  GZIPHelper.getInstance().doStandardZip(files, "PO", buyer.getBuyerCode(), file.getParent());
              }
              
              log.info("start to create PO idoc file [" + file.getName() + "]");
              os = new FileOutputStream(file);
              Element newElement = new Element(rootElement);
              newElement.addContent(element);
              Format format = Format.getCompactFormat();
              format.setEncoding(CommonConstants.ENCODING_UTF8);
              format.setIndent("  ");
              xo = new XMLOutputter(format);
              Document document = new Document();
              document.addContent(newElement);
              xo.output(document, os);
              
              if (!rlt.contains(file))
              {
                  rlt.add(file);
              }
        }
        finally
        {
            if (os != null)
            {
                os.close();
            }
        }
        
    }

    public List<File> convertFile(File file, BuyerHolder buyer, String outputPath, String sourceContent) throws Exception
    {
        InputStream inputStream = null;
        List<File> rlt = null;
        try
        {
            rlt = new ArrayList<File>();
            Map<String , byte[]> fileMap = new HashMap<String, byte[]>();
            if (file.getName().endsWith(".zip"))
            {
                fileMap = GZIPHelper.getInstance().readFileFromZip(file);
            }
            else
            {
                fileMap.put(file.getName(), FileUtils.readFileToByteArray(file));
            }
            
            for (Map.Entry<String, byte[]> entry : fileMap.entrySet())
            {
                inputStream = new ByteArrayInputStream(entry.getValue());
                Document document = FileParserUtil.getInstance().build(inputStream);
                
                Element root = document.getRootElement();
               
                List<Element> eles = root.getChildren();
                for (Element element : eles)
                {
                    PoHeaderHolder header = this.supplierCodeAndDocNo(element);
                    String filePath = outputPath
                        + File.separator
                        + "PO_"
                        + buyer.getBuyerCode()
                        + "_"
                        + header.getSupplierCode()
                        + "_"
                        + header.getPoNo() + ".xml";
                    
                    Element newEle = element.clone();
                    this.createFile(filePath, newEle, root.getName(), rlt, buyer, sourceContent);
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
        
        return rlt;
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
