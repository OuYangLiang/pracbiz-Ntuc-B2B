//*****************************************************************************
//
// File Name       :  CcServiceImpl.java
// Date Created    :  Jan 2, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 2, 2014 11:46:10 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;




import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;




import org.springframework.beans.factory.annotation.Autowired;




import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.extension.CcSummaryHolder;
import com.pracbiz.b2bportal.core.service.CcDetailExtendedService;
import com.pracbiz.b2bportal.core.service.CcDetailService;
import com.pracbiz.b2bportal.core.service.CcHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.CcHeaderService;
import com.pracbiz.b2bportal.core.service.CcService;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CcServiceImpl implements CcService
{
    @Autowired CcHeaderService ccHeaderService;
    @Autowired CcDetailService ccDetailService;
    @Autowired CcHeaderExtendedService ccHeaderExtendedService;
    @Autowired CcDetailExtendedService ccDetailExtendedService;
    @Override
    public void insert(CcHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditInsert(CommonParameterHolder cp, CcHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(CcHolder oldObj_, CcHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        CcHolder oldObj_, CcHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(CcHolder oldObj_, CcHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp,
        CcHolder oldObj_, CcHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void delete(CcHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditDelete(CommonParameterHolder cp, CcHolder oldObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public byte[] exportExcel(List<BigDecimal> invOids, boolean storeFlag)
        throws Exception
    {
        if(invOids == null || invOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal invOid : invOids)
        {
            this.initSheetByInvOid(invOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportSummaryExcel(List<CcSummaryHolder> params,
        boolean storeFlag) throws Exception
    {
        if (params == null || params.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        this.initSheetContent(wwb, params, storeFlag);
        wwb.write();
        wwb.close();
        return out.toByteArray();
    
    }


    @Override
    public CcHolder selectCcByKey(BigDecimal invOid) throws Exception
    {
       if (invOid == null)
       {
           throw new IllegalArgumentException();
       }
       
       CcHeaderHolder ccHeader = ccHeaderService.selectCcHeaderByKey(invOid);
       List<CcDetailHolder> details = ccDetailService.selectCcDetailByKey(invOid);
       List<CcHeaderExtendedHolder> headerExtendeds = ccHeaderExtendedService.selectHeaderExtendedByKey(invOid);
       List<CcDetailExtendedHolder> detailExtendeds = ccDetailExtendedService.selectDetailExtendedByKey(invOid);
       
       CcHolder cc = new CcHolder();
       cc.setCcHeader(ccHeader);
       cc.setDetails(details);
       cc.setHeaderExtendeds(headerExtendeds);
       cc.setDetailExtendeds(detailExtendeds);
       
       return cc;
    }


    //**********************
    //private method
    //**********************
    private void initSheetByInvOid(BigDecimal invOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        CcHolder cc = this.selectCcByKey(invOid);
        CcHeaderHolder header = cc.getCcHeader();
        List<CcDetailHolder> details = cc.getDetails();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getInvNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(85);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.CENTRE);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        format4.setWrap(true);
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        format5.setAlignment(Alignment.RIGHT);
        format5.setVerticalAlignment(VerticalAlignment.CENTRE);
        format5.setWrap(true);
        
        WritableCellFormat format6 = new jxl.write.WritableCellFormat(new jxl.write.NumberFormat("#,##0.00"));  
        format6.setAlignment(Alignment.RIGHT);
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        sheet.addCell(new Label(0, 0, "BUYER NAME", format1));
        sheet.addCell(new Label(1, 0, header.getBuyerName(), format1));
        sheet.addCell(new Label(0, 1, "INVOICE NO", format1));
        sheet.addCell(new Label(1, 1, header.getInvNo(), format1));
        sheet.addCell(new Label(0, 2, "INVOICE DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getInvDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "DELIVERY DATE", format1));
        sheet.addCell(new Label(1, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 4, "SPECIAL INSTRUCTIONS", format1));
        sheet.addCell(new Label(1, 4, "", format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        if (!storeFlag)
        {
            sheet.addCell(new Label(0, 8, "TOTAL AMOUNT INCL. GST ($)", format1));
            sheet.addCell(new Label(1, 8, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getInvAmountWithVat(), 2), format1));
        }
        
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "Description", format4));
        sheet.setColumnView(col, 50);
        sheet.addCell(new Label(col+1, 10, "Quantity", format5));
        sheet.setColumnView(col+1, 15);
        
        if (!storeFlag)
        {
            sheet.addCell(new Label(col+2, 10, "Unit Price ($)", format5));
            sheet.setColumnView(col+2, 15);
            sheet.addCell(new Label(col+3, 10, "Amount ($)", format5));
            sheet.setColumnView(col+3, 15);
        }
       
        int row = 11;
        for (CcDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, detail.getItemDesc(), format2));
            sheet.addCell(new Label(colIndex+1, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getInvQty(), 2), format3));
            
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex+2, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getUnitPrice(), 2), format3));
                sheet.addCell(new Label(colIndex+3, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemAmount(), 2), format3));
            }
            row ++;
        }
        
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 11), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<CcSummaryHolder> params, boolean storeFlag)throws Exception
    {
        int sheetIndex = 0;
        WritableSheet sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
        sheet.getSettings().setZoomFactor(100);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.LEFT);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.RIGHT);
        
        CcSummaryHolder param = null;
        int row = 1;
        for (int i = 0 ; i < params.size() ; i++)
        {
            if ((i + 1) % 65535 == 0)
            {
                sheetIndex ++ ;
                sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
                row = 1;
            }
            
            param = params.get(i);
            sheet.addCell(new Label(0, row , param.getInvNo(), format2));
            sheet.addCell(new Label(1, row , DateUtil.getInstance().convertDateToString(param.getPoDate(), "yyyy/MM/dd"), format1));
            sheet.addCell(new Label(2, row , param.getBuyerCode(), format1));
            sheet.addCell(new Label(3, row , param.getBuyerName(), format1));
            sheet.addCell(new Label(4, row , param.getSupplierCode(), format1));
            sheet.addCell(new Label(5, row , param.getSupplierName(), format1));
            sheet.addCell(new Label(6, row , param.getDetailCount().toString(), format2));
            int col = 7;
            if (!storeFlag)
            {
                sheet.addCell(new Label(col, row , param.getInvAmountWithVat().toString(), format2));
                col ++ ;
                
            }
            sheet.addCell(new Label(col, row , param.getReadStatus().name(), format1));
            sheet.addCell(new Label((col + 1), row , ""));
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("CcSummaryReport_" + (sheetIndex + 1), sheetIndex);
        sheet.getSettings().setZoomFactor(100);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.LEFT);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10, WritableFont.BOLD));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.RIGHT);
        
        
        sheet.setColumnView(0, 15);
        sheet.addCell(new Label(0, 0, "INVOICE NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "INVOICE DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "BUYER CODE", format1));
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "BUYER NAME", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(6, 20);
        sheet.addCell(new Label(6, 0, "TOTAL NO OF ITEM", format2));
        
        int col = 7;
        if (!storeFlag)
        {
            sheet.setColumnView(col, 25);
            sheet.addCell(new Label(col, 0, "TOTAL AMOUNT INCL. GST ($)", format2));
            col ++ ;
        }
        sheet.setColumnView(col, 15);
        sheet.addCell(new Label(col, 0, "READ STATUS", format1));
        sheet.addCell(new Label((col + 1), 0, ""));
        
        return sheet;
    }
}
