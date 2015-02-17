//*****************************************************************************
//
// File Name       :  GrnDisputeSummaryReports.java
// Date Created    :  Nov 8, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 8, 2013 1:01:17 PM $
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
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GrnDisputeSummaryReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(List<GrnHeaderHolder> grnHeaderList, String title) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("Summary Report", 1);
            createSummaryField(sheet,  title);
            createItem(sheet, grnHeaderList);
            wwb.write();
            wwb.close();
            output = out.toByteArray();
        }
        finally
        {
            if (out != null)
            {
                out.close();
                out = null;
            }
        }
        return output;
    }
    
    
    private void createSummaryField(WritableSheet ws,  String title) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, title, titleFormat));
        
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 3, 2);
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 10, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10, WritableFont.BOLD));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.LEFT);
        ws.setColumnView(0, 5);
        ws.addCell(new Label(0, 5, "No.", shf1));
        
        ws.setColumnView(1, 10);
        ws.addCell(new Label(1, 5, "GRN No.", shf1));
        
        ws.setColumnView(2, 10);
        ws.addCell(new Label(2, 5, "GRN Date", shf1));
        
        ws.setColumnView(3, 15);
        ws.addCell(new Label(3, 5, "PO No.", shf1));
        
        ws.setColumnView(4, 15);
        ws.addCell(new Label(4, 5, "PO Date", shf1));
        
        ws.setColumnView(5, 15);
        ws.addCell(new Label(5, 5, "Buyer Code", shf1));
        
        ws.setColumnView(6, 15);
        ws.addCell(new Label(6, 5, "Buyer Name", shf1));
        
        ws.setColumnView(7, 15);
        ws.addCell(new Label(7, 5, "Supplier Code", shf1));
        
        ws.setColumnView(8, 15);
        ws.addCell(new Label(8, 5, "Supplier Name", shf1));
        
        ws.setColumnView(9, 15);
        ws.addCell(new Label(9, 5, "Receive Store Code", shf1));
        
        ws.setColumnView(10, 15);
        ws.addCell(new Label(10, 5, "Receive Store Name", shf1));
        
        ws.setColumnView(11, 10);
        ws.addCell(new Label(11, 5, "Dispute Status", shf1));
        
        ws.setColumnView(12, 15);
        ws.addCell(new Label(12, 5, "Dispute Supplier", shf1));
        
        ws.setColumnView(13, 20);
        ws.addCell(new Label(13, 5, "Supplier Dispute Date", shf1));
        
        ws.setColumnView(14, 25);
        ws.addCell(new Label(14, 5, "Supplier Dispute Remarks", shf1));
        
        ws.setColumnView(15, 15);
        ws.addCell(new Label(15, 5, "Dispute Buyer", shf1));
        
        ws.setColumnView(16, 20);
        ws.addCell(new Label(16, 5, "Buyer Dispute Date", shf1));
        
        ws.setColumnView(17, 25);
        ws.addCell(new Label(17, 5, "Buyer Dispute Remarks", shf1));
    }
    
    
    private void createItem(WritableSheet ws, List<GrnHeaderHolder> grnHeaderList) throws Exception
    {
        if (grnHeaderList == null || grnHeaderList.isEmpty())
        {
            return;
        }
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 10));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.LEFT);
//        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
//                WritableFont.TIMES, 12));
        
        int row = 5;
        for (int i = 0; i < grnHeaderList.size(); i++)
        {
            int col = 0;
            GrnHeaderHolder grnHeader = grnHeaderList.get(i);
            ws.addCell(new Label((col + 0), (row + i + 1), String.valueOf(i + 1), shf1));
            ws.addCell(new Label((col + 1), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getGrnNo()), shf1));
            ws.addCell(new Label((col + 2), (row + i + 1), DateUtil.getInstance().convertDateToString(grnHeader.getGrnDate(), DateUtil.DATE_FORMAT_DDMMYYYY), shf1));
            ws.addCell(new Label((col + 3), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getPoNo()), shf1));
            ws.addCell(new Label((col + 4), (row + i + 1), DateUtil.getInstance().convertDateToString(grnHeader.getPoDate(), DateUtil.DATE_FORMAT_DDMMYYYY), shf1));
            ws.addCell(new Label((col + 5), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getBuyerCode()), shf1));
            ws.addCell(new Label((col + 6), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getBuyerName()), shf1)); 
            ws.addCell(new Label((col + 7), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getSupplierCode()), shf1));
            ws.addCell(new Label((col + 8), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getSupplierName()), shf1));
            ws.addCell(new Label((col + 9), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getReceiveStoreCode()), shf1));
            ws.addCell(new Label((col + 10), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getReceiveStoreName() == null ? buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(grnHeader.getBuyerCode(), grnHeader.getReceiveStoreCode()).getStoreName() : grnHeader.getReceiveStoreName()), shf1));
            ws.addCell(new Label((col + 11), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getDisputeStatus()), shf1));
            ws.addCell(new Label((col + 12), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getDisputeSupplierBy()), shf1));
            ws.addCell(new Label((col + 13), (row + i + 1), DateUtil.getInstance().convertDateToString(grnHeader.getDisputeSupplierDate(), DateUtil.DATE_FORMAT_DDMMYYYY), shf1));
            ws.addCell(new Label((col + 14), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getDisputeSupplierRemarks()), shf1));
            ws.addCell(new Label((col + 15), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getDisputeBuyerBy()), shf1));
            ws.addCell(new Label((col + 16), (row + i + 1), DateUtil.getInstance().convertDateToString(grnHeader.getDisputeBuyerDate(), DateUtil.DATE_FORMAT_DDMMYYYY), shf1));
            ws.addCell(new Label((col + 17), (row + i + 1), StringUtil.getInstance().convertObjectToString(grnHeader.getDisputeBuyerRemarks()), shf1));
            ws.addCell(new Label((col + 18), (row + i + 1), ""));
        }
    }

}
