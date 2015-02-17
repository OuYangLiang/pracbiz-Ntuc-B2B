//*****************************************************************************
//
// File Name       :  PrintFileUtil.java
// Date Created    :  Jul 3, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jul 3, 2013 11:11:21 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public final class PrintFileUtil
{
    public static final String ROOT = "printFile";
    public static final String FILE_PATH = "docPath";
    public static final String FILE_NAME = "fileName";
    public static final String SUPPLIER_CODE = "supplierCode";
    
    public byte[] generateXML(String path, String filename, String supCode) throws IOException
    {
        OutputStream os = null;
        byte[] rlt = null;

        Document document = new Document();
        Element root = new Element(ROOT);
        root.addContent(new Element(FILE_NAME).setText(filename));
        root.addContent(new Element(FILE_PATH).setText(path));
        root.addContent(new Element(SUPPLIER_CODE).setText(supCode));
        
        document.setRootElement(root);

        try
        {
            XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
            os = new ByteArrayOutputStream();
            o.output(document, os);

            rlt = ((ByteArrayOutputStream)os).toByteArray();
        }
        finally
        {
            if (os != null){os.close();os = null;}
        }

        return rlt;
    }
    
    
    public Map<String, String> parsePrintFile(File file) throws JDOMException, IOException
    {
        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(file);
        Element root = document.getRootElement();
        
        Map<String, String> rlt = new HashMap<String, String>();
        rlt.put(FILE_NAME, root.getChild(FILE_NAME).getText());
        rlt.put(FILE_PATH, root.getChild(FILE_PATH).getText());
        rlt.put(SUPPLIER_CODE, root.getChild(SUPPLIER_CODE).getText());
        
        return rlt;
    }
    
    
    //*****************************************************
    // singleton class
    //*****************************************************
    
    private static PrintFileUtil instance;
    private PrintFileUtil(){}
    
    public static PrintFileUtil getInstance()
    {
        synchronized(PrintFileUtil.class)
        {
            if (instance == null)
            {
                instance = new PrintFileUtil();
            }
        }
        
        return instance;
    }
}
