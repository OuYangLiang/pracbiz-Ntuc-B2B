//*****************************************************************************
//
// File Name       :  PnServiceImpl.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 7:14:23 PM $
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
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.CurrencyHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.extension.PnSummaryHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.CurrencyService;
import com.pracbiz.b2bportal.core.service.PnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PnDetailService;
import com.pracbiz.b2bportal.core.service.PnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PnHeaderService;
import com.pracbiz.b2bportal.core.service.PnService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class PnServiceImpl implements PnService
{
    @Autowired PnHeaderService pnHeaderService;
    @Autowired PnDetailService pnDetailService;
    @Autowired PnHeaderExtendedService pnHeaderExtendedService;
    @Autowired PnDetailExtendedService pnDetailExtendedService;
    @Autowired CurrencyService currencyService;
    @Autowired BuyerService buyerService;
   
    
    @Override
    public void insert(PnHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditInsert(CommonParameterHolder cp, PnHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKeySelective(PnHolder oldObj_, PnHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        PnHolder oldObj_, PnHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKey(PnHolder oldObj_, PnHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp,
        PnHolder oldObj_, PnHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(PnHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditDelete(CommonParameterHolder cp, PnHolder oldObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PnHolder selectPnByKey(BigDecimal pnOid) throws Exception
    {
        if (pnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PnHeaderHolder header = pnHeaderService.selectPnHeaderByKey(pnOid);
        List<PnDetailHolder> details = pnDetailService.selectPnDetailByKey(pnOid);
        List<PnHeaderExtendedHolder> headerExs = pnHeaderExtendedService.selectHeaderExtendedByKey(pnOid);
        List<PnDetailExtendedHolder> detailExs = pnDetailExtendedService.selectDetailExtendedByKey(pnOid);
        
        PnHolder pn = new PnHolder();
        pn.setPnHeader(header);
        pn.setDetails(details);
        pn.setHeaderExtendeds(headerExs);
        pn.setDetailExtendeds(detailExs);
        
        return pn;
    }

    @Override
    public byte[] exportExcel(List<BigDecimal> pnOids, boolean storeFlag)
        throws Exception
    {
        if(pnOids == null || pnOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal pnOid : pnOids)
        {
            this.initSheetByPnOid(pnOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }

    @Override
    public byte[] exportSummaryExcel(List<PnSummaryHolder> params,
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
    
    
    //**********************
    //private method
    //**********************
    private void initSheetByPnOid(BigDecimal pnOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        PnHolder pn = this.selectPnByKey(pnOid);
        PnHeaderHolder header = pn.getPnHeader();
        List<PnDetailHolder> details = pn.getDetails();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getPnNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
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
        sheet.addCell(new Label(0, 1, "PN NO", format1));
        sheet.addCell(new Label(1, 1, header.getPnNo(), format1));
        sheet.addCell(new Label(0, 2, "PN DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getPnDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "REMARKS", format1));
        sheet.addCell(new Label(1, 3, header.getPnRemarks(), format1));
        sheet.addCell(new Label(0, 4, "PAYMENT MODE", format1));
        sheet.addCell(new Label(1, 4, header.getPayMethodDesc(), format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "DOCUMENT DATE", format4));
        sheet.setColumnView(col, 20);
        sheet.addCell(new Label(col + 1, 10, "DOCUMENT TO", format4));
        sheet.setColumnView(col + 1, 20);
        sheet.addCell(new Label(col + 2, 10, "DOC TYPE", format4));
        sheet.setColumnView(col + 2, 27);
        sheet.addCell(new Label(col + 3, 10, "REFERENCE", format4));
        sheet.setColumnView(col + 3, 30);
        sheet.addCell(new Label(col + 4, 10, "CURRENCY", format4));
        sheet.setColumnView(col + 4, 27);
 
        if (!storeFlag)
        {
            sheet.addCell(new Label(col + 5, 10, "GROSS ($)", format5));
            sheet.setColumnView(col + 5, 30);
            sheet.addCell(new Label(col + 6, 10, "DISCOUNT ($)", format5));
            sheet.setColumnView(col + 6, 27);
            sheet.addCell(new Label(col + 7, 10, "NETT ($)", format5));
            sheet.setColumnView(col + 7, 30);
        }
        
        BuyerHolder buyer = buyerService.selectBuyerByKey(header.getBuyerOid());
        CurrencyHolder curr = currencyService.selectByCurrCode(buyer.getCurrCode()); 
       
        int row = 11;
        for (PnDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, DateUtil.getInstance().convertDateToString(detail.getDocDate(), "dd-MM-yyyy"), format2));
            sheet.addCell(new Label(colIndex + 1, row, detail.getDocRefNo(), format2));
            sheet.addCell(new Label(colIndex + 2, row, detail.getDocType(), format2));
            sheet.addCell(new Label(colIndex + 3, row, detail.getPayRefNo(), format2));
            if (curr != null)
                sheet.addCell(new Label(colIndex + 4, row, curr.getCurrDesc(), format2));
            else
                sheet.addCell(new Label(colIndex + 4, row, "", format2));
            
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex + 5, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getGrossAmount(), 2), format3));
                sheet.addCell(new Label(colIndex + 6, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getDiscountAmount(), 2), format3));
                sheet.addCell(new Label(colIndex + 7, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getNetAmount(), 2), format3));
            }
        
            row ++;
        }
        
        sheet.addCell(new Label(0, row + 2, "THIS IS A COMPUTER-GENERATED DOCUMENT. NO SIGNATURE IS REQUIRED.", format1));
        sheet.mergeCells(0, row + 2, 2, row + 2);
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 11), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<PnSummaryHolder> params, boolean storeFlag)throws Exception
    {
        int sheetIndex = 0;
        WritableSheet sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
        sheet.getSettings().setZoomFactor(90);
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
        
        PnSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row, param.getPnNo(), format1));
            sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getPnDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(2, row, param.getBuyerCode(), format1));
            sheet.addCell(new Label(3, row, param.getBuyerName(), format1));
            sheet.addCell(new Label(4, row, param.getSupplierCode(), format1));
            sheet.addCell(new Label(5, row, param.getSupplierName(), format1));
            sheet.addCell(new Label(6, row, param.getBankCode(), format1));
            sheet.addCell(new Label(7, row, param.getPayMethodDesc(), format1));
            sheet.addCell(new Label(8, row, BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(param.getDiscountAmount(), 2), format1));
            sheet.addCell(new Label(9, row, BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(param.getTotalAmount(), 2), format1));
            sheet.addCell(new Label(10, row, BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(param.getNetTotalAmount(), 2), format1));
            sheet.addCell(new Label(11, row, param.getDetailCount().toString(), format1));
            sheet.addCell(new Label(12, row, ""));
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("PnSummaryReport_" + (sheetIndex + 1), sheetIndex);
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
        sheet.addCell(new Label(0, 0, "PN NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "PN DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "BUYER CODE", format1));
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "BUYER NAME", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(6, 15);
        sheet.addCell(new Label(6, 0, "BANK CODE", format1));
        sheet.setColumnView(7, 20);
        sheet.addCell(new Label(7, 0, "PAY METHOD DESC", format1));
        sheet.setColumnView(8, 20);
        sheet.addCell(new Label(8, 0, "DISCOUNT AMOUNT", format1));
        sheet.setColumnView(9, 20);
        sheet.addCell(new Label(9, 0, "TOTAL AMOUNT", format1));
        sheet.setColumnView(10, 20);
        sheet.addCell(new Label(10, 0, "NET AMOUNT", format1));
        sheet.setColumnView(11, 20);
        sheet.addCell(new Label(11, 0, "TOTAL NO OF ITEM", format1));
        sheet.addCell(new Label(12, 0, ""));
        
        return sheet;
    }
    
}
