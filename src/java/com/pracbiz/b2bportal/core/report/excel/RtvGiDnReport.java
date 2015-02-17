//*****************************************************************************
//
// File Name       :  RtvGiDnReport.java
// Date Created    :  Apr 17, 2014
// Last Changed By :  $Author: eidt $
// Last Changed On :  $Date: Apr 17, 2014 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class RtvGiDnReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(BuyerHolder buyer, Map<String, Map<String, List<RtvGiDnReportParameter>>> params) throws Exception 
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = null;
        byte[] bytes = null;
        try
        {
            try
            {
                wwb = Workbook.createWorkbook(out);
                
                if (params == null || params.isEmpty())
                {
                    return null;
                }
                
                this.initSummarySheet(wwb, params, buyer,"Summary Report");
                
                WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
                
                this.initFlattenedTitle(flattened);
                this.initFlattenedSheet(flattened, params);
                this.initDetailSheet(wwb, params, buyer);
                
                wwb.write();
            
            }
            finally
            {
                if (wwb != null)
                {
                    wwb.close();
                }
            }
            
            bytes = out.toByteArray();
        }
        finally
        {
            if (out != null)
            {
                out.close();
            }
        }
       
        return bytes;
    }
    
    private void initSummarySheet(WritableWorkbook wwb, Map<String, Map<String, List<RtvGiDnReportParameter>>> params, BuyerHolder buyer,String sheetName) throws Exception
    {
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        //create sheet
        WritableSheet ws = wwb.createSheet(sheetName, 0);
        //create excel style
        WritableCellFormat titleFormat = new WritableCellFormat(
            new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD));
        
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.RIGHT);
        format1.setIndentation(1);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.LEFT);
        format2.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setAlignment(Alignment.CENTRE);
        format3.setVerticalAlignment(VerticalAlignment.CENTRE);
        format3.setBackground(Colour.GRAY_50);
        
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED));
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        format5.setAlignment(Alignment.CENTRE);
        format5.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format6 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        format6.setAlignment(Alignment.CENTRE);
        format6.setVerticalAlignment(VerticalAlignment.CENTRE);
        format6.setBackground(Colour.GRAY_25);
        
        
        ws.addCell(new Label(0, 0,"RTV-GI-DN Exception Report - Summary",titleFormat));
        ws.mergeCells(0, 0, 8, 0);
        
        ws.addCell(new Label(0, 2, "Printed:", format2));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(new Date(),DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), format2));
        ws.mergeCells(1, 2, 3, 2);
        
        ws.addCell(new Label(0, 3, "Buyer:", format2));
        ws.addCell(new Label(1, 3, buyer.getBuyerCode() + "(" + buyer.getBuyerName()  + ")", format2));
        ws.mergeCells(1, 3, 3, 3);

        ws.addCell(new Label(0, 5, "Supplier Code", format3));
        ws.mergeCells(0, 5, 1, 6);
        ws.addCell(new Label(2, 5, "Store Code", format3));
        ws.mergeCells(2, 5, 3, 6);
        ws.addCell(new Label(4, 5, "Dn No.(Dn Date)", format3));
        ws.mergeCells(4, 5, 6, 6);
        ws.addCell(new Label(7, 5, "Gi No.(Gi Date)", format3));
        ws.mergeCells(7, 5, 9, 6);
        ws.addCell(new Label(10, 5, "Matching Result", format3));
        ws.mergeCells(10, 5, 22, 5);
        
        ws.addCell(new Label(0, 7, "RTV No. (RTV Date)", format3));
        ws.mergeCells(0, 7, 1, 7);
        ws.addCell(new Label(2, 7, "Store RTV Amt", format3));
        ws.mergeCells(2, 7, 3, 7);
        ws.addCell(new Label(4, 7, "DN Amt/DN Amt w GST", format3));
        ws.mergeCells(4, 7, 6, 7);
        ws.addCell(new Label(7, 7, "GI Amt", format3));
        ws.mergeCells(7, 7, 9, 7);
        ws.addCell(new Label(10, 6, "RTV-DN(unitPrice)", format3));
        ws.mergeCells(10, 6, 12, 7);
        ws.addCell(new Label(13, 6, "RTV-GI(Qty)", format3));
        ws.mergeCells(13, 6, 15, 7);
        ws.addCell(new Label(16, 6, "GI-DN(Qty)", format3));
        ws.mergeCells(16, 6, 18, 7);
        ws.addCell(new Label(19, 6, "Matching Status", format3));
        ws.mergeCells(19, 6, 22, 7);
        
        int row = 8;
        
        Map<String, List<RtvGiDnReportParameter>> map = null;
        
        for (Map.Entry<String, Map<String, List<RtvGiDnReportParameter>>> entry : params.entrySet())
        {
            map = entry.getValue();
            
            for (Map.Entry<String, List<RtvGiDnReportParameter>> storeEntry : map.entrySet())
            {
                int giDnQtyUnmatchedCount = 0;
                int rtvGiQtyUnmatchedCount = 0;
                int rtvDnPriceUnmatchedCount = 0;
                BigDecimal storeAmt = BigDecimal.ZERO;
                
                for (RtvGiDnReportParameter lst : storeEntry.getValue())
                {
                    storeAmt = storeAmt.add(lst.getStoreAmt());
                    
                    if (lst.getGiDnQtyDiff() != null)
                    {
                        giDnQtyUnmatchedCount++;
                    }
                    
                    if (lst.getRtvGiQtyDiff() != null)
                    {
                        rtvGiQtyUnmatchedCount++;
                    }
                    
                    if (lst.getRtvDnPriceDiff() != null)
                    {
                        rtvDnPriceUnmatchedCount++;
                    }
                }
                
                RtvGiDnReportParameter param = storeEntry.getValue().get(0);
                ws.addCell(new Label(0, row, param.getSupplierCode(), format6));
                ws.mergeCells(0, row, 1, row);
                ws.addCell(new Label(2, row, param.getStoreCode(), format6));
                ws.mergeCells(2, row, 3, row);
                ws.addCell(new Label(4, row, param.getDnNo()+ " (" + DateUtil.getInstance().convertDateToString(
                    param.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT) + ")", format6));
                ws.mergeCells(4, row, 6, row);
                ws.addCell(new Label(7, row,  param.getGiNo()+ " (" + DateUtil.getInstance().convertDateToString(
                    param.getGiDate(), DateUtil.DEFAULT_DATE_FORMAT) + ")", format6));
                ws.mergeCells(7, row, 9, row);
                
                ws.addCell(new Label(0, row + 1, param.getRtvNo()+ " (" + DateUtil.getInstance().convertDateToString(
                    param.getRtvDate(), DateUtil.DEFAULT_DATE_FORMAT) + ")", format4));
                ws.mergeCells(0, row + 1, 1, row + 1);
                ws.addCell(new Label(2, row + 1, decimalUtil.convertBigDecimalToStringIntegerWithScale(storeAmt, 2), format4));
                ws.mergeCells(2, row + 1, 3, row + 1);
                ws.addCell(new Label(4, row + 1, decimalUtil.convertBigDecimalToStringIntegerWithScale(param.getDnAmtWoVat(), 2) 
                    + " / " +
                    decimalUtil.convertBigDecimalToStringIntegerWithScale(param.getDnAmtWvat(), 2), format4));
                ws.mergeCells(4, row + 1, 6, row + 1);
                ws.addCell(new Label(7, row + 1, "", format4));
                ws.mergeCells(7, row + 1, 9, row + 1);
                ws.addCell(new Label(10, row, rtvDnPriceUnmatchedCount+"", rtvDnPriceUnmatchedCount > 0? format5 : format4));
                ws.mergeCells(10, row, 12, row + 1);
                ws.addCell(new Label(13, row, rtvGiQtyUnmatchedCount+"", rtvGiQtyUnmatchedCount > 0 ? format5 : format4));
                ws.mergeCells(13, row, 15, row + 1);
                ws.addCell(new Label(16, row, giDnQtyUnmatchedCount+"", giDnQtyUnmatchedCount > 0 ? format5 : format4));
                ws.mergeCells(16, row, 18, row + 1);
                String matchingStatus = "";
                if ((giDnQtyUnmatchedCount > 0 || rtvGiQtyUnmatchedCount > 0) && rtvDnPriceUnmatchedCount > 0)
                {
                    matchingStatus = "QTY&PRICE_UNMATCHED";
                }
                else if (giDnQtyUnmatchedCount > 0 || rtvGiQtyUnmatchedCount > 0)
                {
                    matchingStatus = "QTY_UNMATCHED";
                }
                else if (rtvDnPriceUnmatchedCount > 0)
                {
                    matchingStatus = "PRICE_UNMATCHED";
                }
                
                ws.addCell(new Label(19, row, matchingStatus, format5));
                ws.mergeCells(19, row, 22, row + 1);
                
                row = row + 3;
            }
        }
    }
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "RTV No"));
        col = col +1;
        ws.addCell(new Label(col, row, "RTV Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "RTV Store Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "GI No"));
        col = col +1;
        ws.addCell(new Label(col, row, "GI Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN No"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Matching Status"));
    }
    
    private void initFlattenedSheet(WritableSheet sheet, Map<String, Map<String, List<RtvGiDnReportParameter>>> params) throws Exception
    {
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        int row = 1;
        Map<String, List<RtvGiDnReportParameter>> map = null;
        
        for (Map.Entry<String, Map<String, List<RtvGiDnReportParameter>>> entry : params.entrySet())
        {
            map = entry.getValue();
            
            for (Map.Entry<String, List<RtvGiDnReportParameter>> storeEntry : map.entrySet())
            {
                int giDnQtyUnmatchedCount = 0;
                int rtvGiQtyUnmatchedCount = 0;
                int rtvDnPriceUnmatchedCount = 0;
                BigDecimal storeAmt = BigDecimal.ZERO;
                
                for (RtvGiDnReportParameter lst : storeEntry.getValue())
                {
                    storeAmt = storeAmt.add(lst.getStoreAmt());
                    
                    if (lst.getGiDnQtyDiff() != null)
                    {
                        giDnQtyUnmatchedCount++;
                    }
                    
                    if (lst.getRtvGiQtyDiff() != null)
                    {
                        rtvGiQtyUnmatchedCount++;
                    }
                    
                    if (lst.getRtvDnPriceDiff() != null)
                    {
                        rtvDnPriceUnmatchedCount++;
                    }
                }
                
                RtvGiDnReportParameter param = storeEntry.getValue().get(0);
                sheet.addCell(new Label(0, row, param.getRtvNo(), format));
                sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getRtvDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format));
                sheet.addCell(new Label(2, row, param.getStoreCode(), format));
                sheet.addCell(new Label(3, row, param.getStoreName() == null ? buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(param.getBuyerCode(), param.getStoreCode()).getStoreName() : param.getStoreName(), format));
                sheet.addCell(new Label(4, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(storeAmt, 2), format));
                sheet.addCell(new Label(5, row, param.getGiNo(), format));
                sheet.addCell(new Label(6, row, param.getGiDate() == null ? "" : DateUtil.getInstance().convertDateToString(
                        param.getGiDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format));
                sheet.addCell(new Label(7, row, param.getDnNo(), format));
                sheet.addCell(new Label(8, row, param.getDnDate() == null ? "" : DateUtil.getInstance().convertDateToString(
                        param.getDnDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format));
                sheet.addCell(new Label(9, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(param.getDnAmtWoVat(), 2), format));
                sheet.addCell(new Label(10, row, param.getSupplierCode(), format));
                sheet.addCell(new Label(11, row, param.getSupplierName(), format)); 
                
                String matchingStatus = "";
                if ((giDnQtyUnmatchedCount > 0 || rtvGiQtyUnmatchedCount > 0) && rtvDnPriceUnmatchedCount > 0)
                {
                    matchingStatus = "QTY&PRICE_UNMATCHED";
                }
                else if (giDnQtyUnmatchedCount > 0 || rtvGiQtyUnmatchedCount > 0)
                {
                    matchingStatus = "QTY_UNMATCHED";
                }
                else if (rtvDnPriceUnmatchedCount > 0)
                {
                    matchingStatus = "PRICE_UNMATCHED";
                }
                sheet.addCell(new Label(12, row, matchingStatus, format));
                
                row = row + 1;
            }
        }
        
        
        sheet.setColumnView(0, 15);
        sheet.setColumnView(1, 15);
        sheet.setColumnView(2, 12);
        sheet.setColumnView(3, 15);
        sheet.setColumnView(4, 12);
        sheet.setColumnView(5, 15);
        sheet.setColumnView(6, 15);
        sheet.setColumnView(7, 15);
        sheet.setColumnView(8, 15);
        sheet.setColumnView(9, 15);
        sheet.setColumnView(10, 15);
        sheet.setColumnView(11, 25);
        sheet.setColumnView(12, 25);
    }
    
    
    private void initDetailSheet(WritableWorkbook wwb, Map<String, Map<String, List<RtvGiDnReportParameter>>> params, BuyerHolder buyer) throws Exception
    {
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD ));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.RIGHT);
        format1.setIndentation(1);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.LEFT);
        format2.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setAlignment(Alignment.CENTRE);
        format3.setVerticalAlignment(VerticalAlignment.CENTRE);
        format3.setBackground(Colour.GRAY_50);
        
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.RED));
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        format5.setAlignment(Alignment.CENTRE);
        format5.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format6 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        format6.setAlignment(Alignment.CENTRE);
        format6.setVerticalAlignment(VerticalAlignment.CENTRE);
        format6.setBackground(Colour.LIGHT_GREEN);
        
        WritableCellFormat format7 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format7.setBorder(Border.ALL, BorderLineStyle.THIN);
        format7.setAlignment(Alignment.CENTRE);
        format7.setVerticalAlignment(VerticalAlignment.CENTRE);
        format7.setBackground(Colour.TAN);
        
        WritableCellFormat format8 = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 9, WritableFont.BOLD));
        format8.setBorder(Border.ALL, BorderLineStyle.THIN);
        format8.setAlignment(Alignment.CENTRE);
        format8.setVerticalAlignment(VerticalAlignment.CENTRE);
        format8.setBackground(Colour.LIGHT_TURQUOISE);
    
        Map<String, List<RtvGiDnReportParameter>> map = null;
        int sheetIndex = 3;
        for (Map.Entry<String, Map<String, List<RtvGiDnReportParameter>>> entry : params.entrySet())
        {
            map = entry.getValue();
            String[] keySplit = entry.getKey().split("@-@");
            //create sheet
            WritableSheet ws = wwb.createSheet(buyer.getBuyerCode() + "-" + keySplit[1], sheetIndex);
            //create excel style
            WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD));
            
            ws.addCell(new Label(0, 0,"RTV-GI-DN Exception Report - Details",titleFormat));
            ws.mergeCells(0, 0, 8, 0);
            
            ws.addCell(new Label(0, 2, "Printed:", format2));
            ws.mergeCells(0, 2, 1, 2);
            ws.addCell(new Label(2, 2, DateUtil.getInstance().convertDateToString(new Date(),DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), format2));
            ws.mergeCells(2, 2, 4, 2);
            
            ws.addCell(new Label(0, 3, "Buyer:", format2));
            ws.mergeCells(0, 3, 1, 3);
            ws.addCell(new Label(2, 3, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", format2));
            ws.mergeCells(2, 3, 4, 3);
            
            ws.addCell(new Label(0, 4, "Supplier:", format2));
            ws.mergeCells(0, 4, 1, 4);
            ws.addCell(new Label(2, 4, keySplit[0]+"("+ keySplit[1] +")", format2));
            ws.mergeCells(2, 4, 4, 4);
            
            int row = 6;
            
            for (Map.Entry<String, List<RtvGiDnReportParameter>> storeEntry : map.entrySet())
            {
                BigDecimal storeAmt = BigDecimal.ZERO;
                int seqNo = 1;
                for (RtvGiDnReportParameter lst : storeEntry.getValue())
                {
                    storeAmt = storeAmt.add(lst.getStoreAmt());
                }

                RtvGiDnReportParameter param = storeEntry.getValue().get(0);
                
                ws.addCell(new Label(0, row, "RTV NO :", format2));
                ws.mergeCells(0, row, 1, row);
                ws.addCell(new Label(2, row, param.getRtvNo(), format2));
                ws.mergeCells(2, row, 4, row);
                
                ws.addCell(new Label(0, row+1, "RTV_DATE :", format2));
                ws.mergeCells(0, row+1, 1, row+1);
                ws.addCell(new Label(2, row+1, DateUtil.getInstance().convertDateToString(param.getRtvDate(),DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), format2));
                ws.mergeCells(2, row+1, 4, row+1);
                
                ws.addCell(new Label(0, row+2, "RTV_TOTAL :", format2));
                ws.mergeCells(0, row+2, 1, row+2);
                ws.addCell(new Label(2, row+2, decimalUtil.convertBigDecimalToStringIntegerWithScale(storeAmt, 2), format2));
                ws.mergeCells(2, row+2, 4, row+2);
                
                row = row + 4;
                ws.addCell(new Label(0, row, "Store  " + storeEntry.getKey().split("@=@")[0], format3));
                ws.mergeCells(0, row, 10, row);
                ws.addCell(new Label(11, row, "Unit Price (RTV-DN)", format6));
                ws.mergeCells(11, row, 13, row);
                ws.addCell(new Label(14, row, "Qty (RTV-GI)", format7));
                ws.mergeCells(14, row, 16, row);
                ws.addCell(new Label(17, row, "Qty (GI-DN)", format8));
                ws.mergeCells(17, row, 19, row);
                
                row = row + 1;
                ws.addCell(new Label(0, row, "Seq", format3));
                ws.addCell(new Label(1, row, "Item Code", format3));
                ws.mergeCells(1, row, 2, row);
                ws.addCell(new Label(3, row, "Class", format3));
                ws.mergeCells(3, row, 4, row);
                ws.addCell(new Label(5, row, "UOM", format3));
                ws.addCell(new Label(6, row, "Offer(Y/N)", format3));
                ws.addCell(new Label(7, row, "Description", format3));
                ws.mergeCells(7, row, 10, row);
                ws.addCell(new Label(11, row, "RTV", format3));
                ws.addCell(new Label(12, row, "DN", format3));
                ws.addCell(new Label(13, row, "Diff", format3));
                ws.addCell(new Label(14, row, "RTV", format3));
                ws.addCell(new Label(15, row, "GI", format3));
                ws.addCell(new Label(16, row, "Diff", format3));
                ws.addCell(new Label(17, row, "GI", format3));
                ws.addCell(new Label(18, row, "DN", format3));
                ws.addCell(new Label(19, row, "Diff", format3));
                
                row = row + 1;
                for (RtvGiDnReportParameter lst : storeEntry.getValue())
                {
                    ws.addCell(new Label(0, row, seqNo + "", format4));
                    ws.addCell(new Label(1, row, lst.getBuyerItemCode(), format4));
                    ws.mergeCells(1, row, 2, row);
                    ws.addCell(new Label(3, row, lst.getClassCode(), format2));
                    ws.mergeCells(3, row, 4, row);
                    ws.addCell(new Label(5, row, lst.getUom(), format4));
                    ws.addCell(new Label(6, row, lst.getOffer()==null?"":(lst.getOffer()?"Y":"N"), format4));
                    ws.addCell(new Label(7, row, lst.getItemDesc(), format2));
                    ws.mergeCells(7, row, 10, row);
                    ws.addCell(new Label(11, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvUnitPrice(), 2), lst.getRtvDnPriceDiff()!=null?format5:format4));
                    ws.addCell(new Label(12, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getDnUnitPrice(), 2), lst.getRtvDnPriceDiff()!=null?format5:format4));
                    ws.addCell(new Label(13, row, (lst.getRtvDnPriceDiff()==null||lst.getRtvDnPriceDiff().compareTo(BigDecimal.ZERO)==0)?"":(decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvDnPriceDiff(), 2)+"("+decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvDnPriceDiffPercent(), 2)+"%)"), lst.getRtvDnPriceDiff()!=null?format5:format4));
                    ws.addCell(new Label(14, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvQty(), 2), lst.getRtvGiQtyDiff()!=null?format5:format4));
                    ws.addCell(new Label(15, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getGiQty(), 2), lst.getRtvGiQtyDiff()!=null?format5:format4));
                    ws.addCell(new Label(16, row, (lst.getRtvGiQtyDiff()==null||lst.getRtvGiQtyDiff().compareTo(BigDecimal.ZERO)==0)?"":(decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvGiQtyDiff(), 2)+ "(" + decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getRtvGiQtyDiffPercent(), 2) + "%)"), lst.getRtvGiQtyDiff()!=null?format5:format4));
                    ws.addCell(new Label(17, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getGiQty(), 2), lst.getGiDnQtyDiff()!=null?format5:format4));
                    ws.addCell(new Label(18, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getDnQty(), 2), lst.getGiDnQtyDiff()!=null?format5:format4));
                    ws.addCell(new Label(19, row, (lst.getGiDnQtyDiff()==null || lst.getGiDnQtyDiff().compareTo(BigDecimal.ZERO)==0)?"":(decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getGiDnQtyDiff(), 2) +"("+decimalUtil.convertBigDecimalToStringIntegerWithScale(lst.getGiDnQtyDiffPercent(), 2)+"%)"), lst.getGiDnQtyDiff()!=null?format5:format4));
                    
                    seqNo ++;
                    row++;
                }
                
                row ++;
            }
            
            ws.setColumnView(13, 25);
            ws.setColumnView(16, 25);
            ws.setColumnView(19, 25);
            
            sheetIndex ++;
        }
    }
}
