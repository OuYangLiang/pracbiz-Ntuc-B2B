package com.pracbiz.b2bportal.base.print;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPrintPage;


public class PdfFilePrinter implements FilePrinter
{
    protected static final Logger log = LoggerFactory.getLogger(PdfFilePrinter.class);
    
    private static double PIX_INCH = 1.0/72.0;
    private static double MARGIN_X_PIX = 0.3 / PIX_INCH;
    private static double MARGIN_Y_PIX = 0.2 / PIX_INCH;
    
    public void print(String printerName_, String fileName_) throws Exception
    {
        PrintService service = null;
        AttributeSet aset = null;
        FileInputStream inputStream = null;
        byte[] pdfContent = null;
        PDFFile pdfFile = null;
        PDFPrintPage pages = null;
        Book book = null;
        Paper paper = null;
        PrinterJob pjob = null;
        ByteBuffer bb = null;
        PrintService[] services = null;
        
        try
        {
            //look up the printer
            aset = new HashAttributeSet();
            book = new Book();
            paper = new Paper();
            aset.add(new PrinterName(printerName_, null));            
            services = PrintServiceLookup.lookupPrintServices(null, aset);
            if (services != null && services.length > 0) 
            {
                service = services[0];
            }
            else 
            {
                throw new PrinterNotExistException("Can not find the printer ["+printerName_+"]");
            }
            
            //get the pdf file
            inputStream = FileUtils.openInputStream(new File(fileName_));
            pdfContent = new byte[inputStream.available()];
            inputStream.read(pdfContent, 0, inputStream.available());
            bb = ByteBuffer.wrap(pdfContent);
            pdfFile = new PDFFile(bb);
            pages = new PDFPrintPage(pdfFile);            
            PageFormat pf = PrinterJob.getPrinterJob().defaultPage();
            String heightLog = ", Height=";
            String yLog = ", Y=" ;
            //setup the area of printing content
            log.debug("Page Aspect Ratio: " + pdfFile.getPage(0).getAspectRatio());
            log.debug("[PAGE] Width=" + pdfFile.getPage(0).getWidth() + heightLog + pdfFile.getPage(0).getHeight());
            boolean isFmtPortrait = true;
            if (pdfFile.getNumPages() >= 1 && 
                    ( (pdfFile.getPage(0).getWidth()) > (pdfFile.getPage(0).getHeight()))) 
            {
                isFmtPortrait = false;
                log.debug("Page Orientation BEFORE: " + pf.getOrientation());
                log.debug("Imageable Area BEFORE: Width=" + pf.getImageableWidth() + heightLog + pf.getImageableHeight());
                log.debug("X,Y BEFORE: X=" + pf.getImageableX() + yLog + pf.getImageableY());
                
                pf.setOrientation(PageFormat.LANDSCAPE);
                log.debug("Page Orientation AFTER: " + pf.getOrientation());
                log.debug("Imageable Area AFTER: Width=" + pf.getImageableWidth() + heightLog + pf.getImageableHeight());
                log.debug("X,Y AFTER: X=" + pf.getImageableX() + yLog + pf.getImageableY());
                log.debug("Page orientation==LANDSCAPE? " + (PrinterJob.getPrinterJob().defaultPage().getOrientation()==PageFormat.LANDSCAPE));
            }
            
            if (isFmtPortrait)
            {
                double width = pdfFile.getPage(0).getWidth()-2*MARGIN_X_PIX;
                double height = pdfFile.getPage(0).getHeight()-2*MARGIN_Y_PIX;
                log.debug("Portrait Image Area: X=" + MARGIN_X_PIX +
                                    ", Y=" + MARGIN_Y_PIX + ", Width=" + width +
                                    ", Height=" + height);
                paper.setImageableArea(MARGIN_X_PIX, MARGIN_Y_PIX, 
                                        pdfFile.getPage(0).getWidth()-2*MARGIN_X_PIX,
                                        pdfFile.getPage(0).getHeight()-2*MARGIN_Y_PIX);
                paper.setSize(pdfFile.getPage(0).getWidth(), pdfFile.getPage(0).getHeight());
            }
            else
            {
                double width = pdfFile.getPage(0).getHeight()-2*MARGIN_X_PIX;
                double height = pdfFile.getPage(0).getWidth()-2*MARGIN_Y_PIX;
                log.debug("Portrait Image Area: X=" + MARGIN_X_PIX +
                        ", Y=" + MARGIN_Y_PIX + ", Width=" + width +
                        ", Height=" + height);
                paper.setImageableArea(MARGIN_X_PIX, MARGIN_Y_PIX, 
                        pdfFile.getPage(0).getHeight()-2*MARGIN_X_PIX,
                        pdfFile.getPage(0).getWidth()-2*MARGIN_Y_PIX);
                paper.setSize(pdfFile.getPage(0).getHeight(), pdfFile.getPage(0).getWidth());
            }
            
            pf.setPaper(paper);
            
            //setup print job
            pjob = PrinterJob.getPrinterJob();
            pjob.setPageable(book);
            pjob.setPrintService(service);
            pjob.setJobName(fileName_);
            
            pf = pjob.validatePage(pf);
            book.append(pages, pf, pdfFile.getNumPages());
            pjob.setPageable(book);
            pjob.print();
            
            log.info("Done Printing with Printer Name [" + service.getName() +"] and File Name ["+fileName_+"]");
        }
        finally
        {
            aset = null;
            service = null;
            services = null;
           
            inputStream = null;
            pdfContent = null;
            bb = null;
            pdfFile = null;
            pages = null;
            paper = null;
            book = null;                
            pjob = null;
        }
    }
}
