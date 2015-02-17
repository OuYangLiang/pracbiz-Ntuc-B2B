//*****************************************************************************
//
// File Name       :  MissingGrnReport.java
// Date Created    :  Dec 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 10, 2013 11:47:55 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;


/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class MissingGrnReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(BuyerHolder buyer, List<MissingGrnReportParameter> params) throws Exception 
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
    
    private void initSummarySheet(WritableWorkbook wwb, List<MissingGrnReportParameter> params, BuyerHolder buyer,String sheetName) throws Exception
    {
        //create sheet
        WritableSheet ws = wwb.createSheet(sheetName, 0);
        //create excel style
        WritableCellFormat titleFormat = new WritableCellFormat(
            new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD));
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.RIGHT);
        format1.setIndentation(1);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.CENTRE);
        format2.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setAlignment(Alignment.CENTRE);
        format3.setVerticalAlignment(VerticalAlignment.CENTRE);
        format3.setBackground(Colour.GRAY_50);
        
        
        ws.addCell(new Label(0, 0,"Missing Goods Receipt Note Report",titleFormat));
        ws.mergeCells(0, 0, 7, 0);
        ws.addCell(new Label(0, 2, "Executed:"));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(new Date(),DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)));
        ws.addCell(new Label(0, 3, "Buyer:"));
        ws.addCell(new Label(1, 3, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")"));
        
       
        ws.addCell(new Label(0, 8, "No.", format3));
        ws.mergeCells(0, 8, 0, 9);
        
        ws.addCell(new Label(1, 8, "Supplier Code", format3));
        ws.mergeCells(1, 8, 2, 8);
        ws.addCell(new Label(3, 8, "Store", format3));
        ws.mergeCells(3, 8, 4, 9);
        ws.addCell(new Label(5, 8, "Inv No. (date)", format3));
        ws.mergeCells(5, 8, 7, 8);
        
        ws.addCell(new Label(1, 9, "Po No. (date)", format3));
        ws.mergeCells(1, 9, 2, 9);
        ws.addCell(new Label(5, 9, "Inv Amt / Inv Amt w GST", format3));
        ws.mergeCells(5, 9, 7, 9);
        
        int col = 0;
        int row = 10;
        
        for (int i = 0 ; i < params.size(); i++)
        {
            MissingGrnReportParameter param = params.get(i);
            
            ws.addCell(new Label(0, row + i, (i + 1) + "", format2));
            ws.mergeCells(0, (row + i), 0, (row + i + 1));
            
            ws.addCell(new Label(1, row + i, param.getSupplierCode(), format2));
            ws.mergeCells(1, (row + i), 2, (row + i));
            ws.addCell(new Label(3, (row + i), param.getStoreCode(), format2));
            ws.mergeCells(3, (row + i), 4, (row + i + 1));
            ws.addCell(new Label(5, (row + i), param.getInvNo() == null ? ""
                : param.getInvNo()
                    + (param.getInvDate() == null ? "" : "(" + DateUtil.getInstance().convertDateToString(param.getInvDate(), DateUtil.DATE_FORMAT_DDMMYYYY) + ")"), format2));
            ws.mergeCells(5, (row + i), 7, (row + i));
            
            ws.addCell(new Label(1, (row + i + 1), param.getPoNo() + "(" + DateUtil.getInstance().convertDateToString(param.getPoDate(), DateUtil.DATE_FORMAT_DDMMYYYY) + ")", format2));
            ws.mergeCells(1, (row + i + 1), 2, (row + i + 1));
            
            ws.addCell(new Label(5, (row + i + 1),
                param.getInvAmt() == null ? "" : MatchingReportParameter.convertBigDecimalToDouble(param.getInvAmt())
                    + (param.getInvAmtWithVat() == null ? "" : "/" + MatchingReportParameter.convertBigDecimalToDouble(param.getInvAmtWithVat())), format2));
            ws.mergeCells(5, (row + i + 1), 7, (row + i + 1));
            
            col = col + 1;
            
            if (i != params.size() -1 )
            {
                ws.addCell(new Label(1, (row + i + 2), "", format2));
                ws.mergeCells(0, (row + i + 2), 7, (row + i + 2));
            }
            
            row = row + 2;
        }
        
        //set column's width
        ws.setColumnView(0, 12);
        ws.setColumnView(1, 12);
        ws.setColumnView(2, 12);
        ws.setColumnView(3, 12);
        ws.setColumnView(4, 12);
        ws.setColumnView(5, 12);
        ws.setColumnView(6, 12);
        ws.setColumnView(7, 12);
    }
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "PO No"));
        col = col +1;
        ws.addCell(new Label(col, row, "PO Subtype"));
        col = col +1;
        ws.addCell(new Label(col, row, "PO Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv No"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
    }
    
    private void initFlattenedSheet(WritableSheet sheet, List<MissingGrnReportParameter> params) throws Exception
    {
        WritableCellFormat format = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format.setAlignment(Alignment.CENTRE);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        
        int row = 1;
        for (int i = 0 ; i < params.size(); i++)
        {
            MissingGrnReportParameter param = params.get(i);
            
            sheet.addCell(new Label(0, row, param.getPoNo(), format));
            sheet.addCell(new Label(1, row, param.getPoSubType(), format));
            sheet.addCell(new Label(2, row, DateUtil.getInstance().convertDateToString(param.getPoDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format));
            sheet.addCell(new Label(3, row, param.getStoreCode(), format));
            sheet.addCell(new Label(4, row, param.getStoreName() == null ? buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(param.getBuyerCode(), param.getStoreCode()).getStoreName() : param.getStoreName(), format));
            sheet.addCell(new Label(5, row, param.getInvNo(), format));
            sheet.addCell(new Label(6, row, param.getInvDate() == null ? "" : DateUtil.getInstance().convertDateToString(
                    param.getInvDate(), DateUtil.DATE_FORMAT_DDMMYYYY), format));
            sheet.addCell(new Label(7, row, param.getInvAmt() == null ? "" : param.getInvAmt().toString(), format));
            sheet.addCell(new Label(8, row, param.getSupplierCode(), format));
            sheet.addCell(new Label(9, row, param.getSupplierName(), format));
            
            row ++ ;
        }
        
        sheet.setColumnView(0, 12);
        sheet.setColumnView(1, 12);
        sheet.setColumnView(2, 12);
        sheet.setColumnView(3, 12);
        sheet.setColumnView(4, 30);
        sheet.setColumnView(5, 12);
        sheet.setColumnView(6, 12);
        sheet.setColumnView(7, 12);
        sheet.setColumnView(8, 12);
        sheet.setColumnView(9, 40);
    }
    
    
//    public static void main(String[] args) throws Exception
//    {
//       List<MissingGrnReportParameter> mrps = new ArrayList<MissingGrnReportParameter>();
//        
//       for (int i = 0 ; i < 10 ; i ++ )
//       {
//           MissingGrnReportParameter mrp = new MissingGrnReportParameter();
//           mrp.setPoNo("poNo " + i);
//           mrp.setPoSubType("poSubType " + i);
//           mrp.setPoDate(new Date());
//           mrp.setStoreCode("storeCode " + i);
////           mrp.setPoAmt();
//           mrp.setInvNo("invNo " + i);
//           mrp.setInvDate(new Date());
//           mrp.setInvAmt(BigDecimal.ZERO);
//           mrp.setSupplierCode("supplierCode " + i);
//           
//           
//           mrps.add(mrp);
//       }
//       
//       MissingGrnReport mgr = new MissingGrnReport();
//       byte[] bytes = mgr.exportExcel(mrps);
//       FileUtil.getInstance().writeByteToDisk(bytes, "E:/1001.xls");
//       
//    }
    
}
