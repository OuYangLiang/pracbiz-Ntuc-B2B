//*****************************************************************************
//
// File Name       :  PdfReportUtil.java
// Date Created    :  Dec 10, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 10, 2012 4:10:26 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public final class PdfReportUtil
{
    private static PdfReportUtil instance;

    private PdfReportUtil()
    {
    }

    public static PdfReportUtil getInstance()
    {
        synchronized(PdfReportUtil.class)
        {
            if(instance == null)
            {
                instance = new PdfReportUtil();
            }
        }

        return instance;
    }

    
    public byte[] generatePdf(String classpathTemplate,
        List<Map<String, Object>> datasource, Map<String, Object> param)
        throws JRException, IOException
    {
        InputStream template = null;
        JasperReport jr = null;
        JasperPrint jp = null;

        synchronized(PdfReportUtil.class)
        {
            try
            {
                template = this.getClass().getClassLoader()
                    .getResourceAsStream(classpathTemplate);

                jr = (JasperReport)JRLoader.loadObject(template);

                jp = JasperFillManager.fillReport(jr, param,
                    new JRBeanCollectionDataSource(datasource));

            }
            finally
            {
                if(template != null)
                {
                    template.close();
                    template = null;
                }
            }
        }

        return JasperExportManager.exportReportToPdf(jp);
    }
    
    
    public InputStream mergePDFs(String[] files) throws IOException, COSVisitorException
    {
        byte[] rlt = mergePDFsByte(files);
        
        return new ByteArrayInputStream(rlt);
    }
    
    
    public byte[] mergePDFsByte(String[] files) throws IOException, COSVisitorException
    {
        if(files==null || files.length==0)
            return null;
        
        PDFMergerUtility merger = new PDFMergerUtility();
        
        PDDocument destination = null;
        PDDocument source      = null;
        
        ByteArrayOutputStream os= null;
        byte[] rlt = null;
        
        try
        {
            destination = PDDocument.load(files[0]);
            
            for(int i=1; i<files.length; i++)
            {
                source = PDDocument.load(files[i]);
                
                try
                {
                    merger.appendDocument(destination, source);
                }
                finally
                {
                    if (source != null)
                    {
                        source.close();
                    }
                }
            }
            
            os = new ByteArrayOutputStream();
            destination.save(os);
            
            rlt = os.toByteArray();
        }
        finally
        {
            if (destination != null)
            {
                destination.close();
            }
            if(os != null)
            {
                os.close();
            }
        }
        
        return rlt;
    }
    
    public byte[] mergePDFsByte(InputStream[] streams) throws IOException, COSVisitorException
    {
        if(streams==null || streams.length==0)
            return null;
        
        PDFMergerUtility merger = new PDFMergerUtility();
        
        PDDocument destination = null;
        PDDocument source      = null;
        
        ByteArrayOutputStream os= null;
        byte[] rlt = null;
        
        try
        {
            destination = PDDocument.load(streams[0]);
            
            for(int i=1; i<streams.length; i++)
            {
                source = PDDocument.load(streams[i]);
                
                try
                {
                    merger.appendDocument(destination, source);
                }
                finally
                {
                    if (source != null)
                    {
                        source.close();
                    }
                }
            }
            
            os = new ByteArrayOutputStream();
            destination.save(os);
            
            rlt = os.toByteArray();
        }
        finally
        {
            if (destination != null)
            {
                destination.close();
            }
            if(os != null)
            {
                os.close();
            }
        }
        
        return rlt;
    }
}
