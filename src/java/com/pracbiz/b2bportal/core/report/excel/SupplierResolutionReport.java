package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class SupplierResolutionReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(List<MatchingReportParameter> acceptedList, List<MatchingReportParameter> rejectedList, SupplierHolder supplier) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            Map<String, List<MatchingReportParameter>> map = groupParametersByBuyer(rejectedList);
            Map<String, WritableSheet> detailSheetMap = new HashMap<String, WritableSheet>();
            if (map != null && !map.isEmpty())
            {
                int index = 2;
                for (Map.Entry<String, List<MatchingReportParameter>> entry : map.entrySet())
                {
                    WritableSheet detail = wwb.createSheet(entry.getKey(), index);
                    detailSheetMap.put(entry.getKey(), detail);
                    createDetailField(detail, supplier, entry.getValue());
                    index ++ ;
                }
            }

            WritableSheet summary = wwb.createSheet("Summary Report", 0);
            createSummaryField(summary, supplier, acceptedList, rejectedList, detailSheetMap);
            
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, acceptedList, rejectedList);
            
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
    
    
    private void createSummaryField(WritableSheet ws, SupplierHolder supplier, List<MatchingReportParameter> acceptedList, 
            List<MatchingReportParameter> rejectedList, Map<String, WritableSheet> detailSheetMap) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "Resolution Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 5, 2);
        
        ws.addCell(new Label(0, 3, "Supplier:", summaryFormat));
        ws.addCell(new Label(1, 3, supplier.getSupplierName() + "(" + supplier.getSupplierCode() + ")", summaryFormat));
        ws.mergeCells(1, 3, 5, 3);
        
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        int row = 5;
        if (acceptedList !=  null && !acceptedList.isEmpty())
        {
            row = row + 1;
            ws.addCell(new Label(0, row, "You have accepted the following unmatched matching records:", summaryFormat));
            ws.mergeCells(0, row, 8, row);
            Map<String, List<MatchingReportParameter>> acceptedMap = groupParametersByBuyer(acceptedList);
            for (Map.Entry<String, List<MatchingReportParameter>> entry : acceptedMap.entrySet())
            {
                row = createAcceptedSummaryTitle(ws, entry.getValue(), row);
            }
        }
        
        if (rejectedList != null && !rejectedList.isEmpty())
        {
            row = row + 1;
            ws.addCell(new Label(0, row, "You have rejected the following unmatched matching records:", summaryFormat));
            ws.mergeCells(0, row, 8, row);
            Map<String, List<MatchingReportParameter>> rejectedMap = groupParametersByBuyer(rejectedList);
            for (Map.Entry<String, List<MatchingReportParameter>> entry : rejectedMap.entrySet())
            {
                row = createRejectedSummaryTitle(ws, entry.getValue(), row, detailSheetMap);
            }
        }
        
    }
    
    
    private int createAcceptedSummaryTitle(WritableSheet ws, List<MatchingReportParameter> acceptedList, int row) throws Exception
    {
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setBackground(Colour.GRAY_25);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        
        int col = 0;
        row = row + 1;
        
        ws.addCell(new Label(col, row, "Buyer:", shf2));
        ws.addCell(new Label(col + 1, row, acceptedList.get(0).getBuyerName() + "(" + acceptedList.get(0).getBuyerCode() + ")", shf2));
        ws.mergeCells(col + 1, row, col + 4, row);
        
        row = row + 1;
        ws.addCell(new Label(col, row, "No.", shf1));
        ws.mergeCells(col, row, col, row + 1);
        col = col + 1;
        ws.addCell(new Label(col, row, "Supplier Code", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "PO No. (PO Date)", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Store Code", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "Store PO Amt", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "INV No. (INV Date)", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "INV Amt/INV Amt w GST", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "GRN No.(GRN Date)", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "GRN Amt", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Match Status", shf1));
        ws.mergeCells(col, row, col + 2, row + 1);
        row = row + 2;
        
        return createAccptedSummaryItem(ws, acceptedList, row);
    }
    
    
    private int createRejectedSummaryTitle(WritableSheet ws, List<MatchingReportParameter> rejectedList, int row, 
            Map<String, WritableSheet> detailSheetMap) throws Exception
    {
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setBackground(Colour.GRAY_25);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        
        int col = 0;
        row = row + 1;
        
        ws.addCell(new Label(col, row, "Buyer:", shf2));
        ws.addCell(new Label(col + 1, row, rejectedList.get(0).getBuyerName() + "(" + rejectedList.get(0).getBuyerCode() + ")", shf2));
        ws.mergeCells(col + 1, row, col + 4, row);
        
        row = row + 1;
        ws.addCell(new Label(col, row, "No.", shf1));
        ws.mergeCells(col, row, col, row + 1);
        col = col + 1;
        ws.addCell(new Label(col, row, "Supplier Code", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "PO No. (PO Date)", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Store Code", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "Store PO Amt", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "INV No. (INV Date)", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "INV Amt/INV Amt w GST", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "GRN No.(GRN Date)", shf1));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "GRN Amt", shf1));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Match Status", shf1));
        ws.mergeCells(col, row, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Buyer Status", shf1));
        ws.mergeCells(col, row, col + 1, row + 1);
        row = row + 2;
        
        
        return createRejectedSummaryItem(ws, rejectedList, row, detailSheetMap);
    }
    
    
    private int createAccptedSummaryItem(WritableSheet ws, List<MatchingReportParameter> matchingList, int row) throws Exception
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return row;
        }
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        for (int i = 0; i < matchingList.size(); i++)
        {
            int col = 0;
            MatchingReportParameter matching = matchingList.get(i);
            
            ws.addCell(new Label(col, row, String.valueOf(i + 1), shf1));
            ws.mergeCells(col, row, col, row + 1);
            col = col + 1;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getSupplierCode()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, MatchingReportParameter.getNoAndDate(matching.getPoNo(), matching.getPoDate()), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getStoreCode()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getPoAmt())), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(matching.getInvNo(), matching.getInvDate()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getInvAmt()))
                    + "/" + StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getInvAmtWithVat())), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, MatchingReportParameter.getGrnNosAndGrnDates(matching.getGrnNos(), matching.getGrnDates()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, "", shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getMatchingResult().getMatchingStatus().name()), shf1));
            ws.mergeCells(col, row, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(0, row + 2, "", shf2));
            ws.mergeCells(0, row + 2, col + 1, row + 2);
            
            row = row + 3;
        }
        
        return row;
    }
    
    
    private int createRejectedSummaryItem(WritableSheet ws, List<MatchingReportParameter> matchingList, int row, 
            Map<String, WritableSheet> detailSheetMap) throws Exception
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return row;
        }
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setVerticalAlignment(VerticalAlignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        for (int i = 0; i < matchingList.size(); i++)
        {
            int col = 0;
            MatchingReportParameter matching = matchingList.get(i);
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(i+1), shf1));
            ws.addHyperlink(new WritableHyperlink(col, row, StringUtil.getInstance().convertObjectToString(i+1), 
                    detailSheetMap.get(matching.getBuyerCode()), 0, 0));
            ws.mergeCells(col, row, col, row + 1);
            col = col + 1;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getSupplierCode()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, MatchingReportParameter.getNoAndDate(matching.getPoNo(), matching.getPoDate()), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getStoreCode()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getPoAmt())), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(matching.getInvNo(), matching.getInvDate()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getInvAmt()))
                    + "/" + StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getInvAmtWithVat())), shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, MatchingReportParameter.getGrnNosAndGrnDates(matching.getGrnNos(), matching.getGrnDates()), shf1));
            ws.mergeCells(col, row, col + 2, row);
            
            ws.addCell(new Label(col, row + 1, "", shf1));
            ws.mergeCells(col, row + 1, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getMatchingResult().getMatchingStatus().name()), shf1));
            ws.mergeCells(col, row, col + 2, row + 1);
            col = col + 3;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getBuyerStatus()), shf1));
            ws.mergeCells(col, row, col + 1, row + 1);
            col = col + 2;
            
            ws.addCell(new Label(0, row + 2, "", shf2));
            ws.mergeCells(0, row + 2, col + 1, row + 2);
            
            row = row + 3;
        }
        
        return row;
    }
    
    
    private void createDetailField(WritableSheet ws, SupplierHolder supplier, List<MatchingReportParameter> rejectedList) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        summaryFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        ws.addCell(new Label(0, 0, "Resolution Report - Detail", titleFormat));
        
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 5, 2);
        
        ws.addCell(new Label(0, 3, "Buyer:", summaryFormat));
        ws.addCell(new Label(1, 3, rejectedList.get(0).getBuyerName() + "(" + rejectedList.get(0).getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(1, 3, 5, 3);
        
        ws.addCell(new Label(0, 4, "Supplier:", summaryFormat));
        ws.addCell(new Label(1, 4, supplier.getSupplierName() + "(" + supplier.getSupplierCode() + ")", summaryFormat));
        ws.mergeCells(1, 4, 5, 4);
        
        Map<String, List<MatchingReportParameter>> map = groupParametersByPo(rejectedList);
        
        if (map != null && !map.isEmpty())
        {
            int row = 6;
            for (Map.Entry<String, List<MatchingReportParameter>> entry : map.entrySet())
            {
                List<MatchingReportParameter> list = entry.getValue();
                row = row + 4;
                row = createDetailPoInfo(ws, entry.getKey(), list.get(0).getPoDate(), list, row);
            }
        }
        
    }
    
    
    private int createDetailPoInfo(WritableSheet ws, String poNo, Date poDate, List<MatchingReportParameter> parameters, int row) throws Exception
    {
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        ws.addCell(new Label(0, row, "PO No:", shf1));
        ws.addCell(new Label(1, row, poNo, shf1));
        ws.mergeCells(1, row, 4, row);
        
        row = row + 1;
        ws.addCell(new Label(0, row, "PO Date:", shf1));
        ws.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(poDate), shf1));
        ws.mergeCells(1, row, 4, row);
        
        row = row + 1;
        int poTotalRow = row;
        
        BigDecimal poTotal = BigDecimal.ZERO;
        for (MatchingReportParameter parameter : parameters)
        {
            row = createDetailTitle(ws, parameter, row + 2);
            poTotal = poTotal.add(parameter.getPoAmt());
        }
        
        ws.addCell(new Label(0, poTotalRow, "PO Total:", shf1));
        ws.addCell(new Label(1, poTotalRow, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(poTotal)), shf1));
        ws.mergeCells(1, poTotalRow, 4, poTotalRow);
        
        return row;
        
    }
    
    
    private int createDetailTitle(WritableSheet ws, MatchingReportParameter parameter, int row) throws Exception
    {
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setBackground(Colour.GRAY_50);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        shf2.setBackground(Colour.GRAY_25);
        
        row = row + 1;
        ws.addCell(new Label(0, row, "Store " + parameter.getStoreCode(), shf1));
        ws.mergeCells(0, row, 10, row);
        
        ws.addCell(new Label(11, row, "Unit Price (PO-INV)", shf1));
        ws.mergeCells(11, row, 17, row);
        
        ws.addCell(new Label(18, row, "Qty (GRN-INV)", shf1));
        ws.mergeCells(18, row, 25, row);
        
        row = row + 1;
        
        ws.addCell(new Label(0, row, "Seq", shf2));
        
        ws.addCell(new Label(1, row, "Item Code", shf2));
        ws.mergeCells(1, row, 2, row);
        
        ws.addCell(new Label(3, row, "Barcode", shf2));
        ws.mergeCells(3, row, 5, row);
        
        ws.addCell(new Label(6, row, "UOM", shf2));
        
        ws.addCell(new Label(7, row, "Description", shf2));
        ws.mergeCells(7, row, 10, row);
        
        ws.addCell(new Label(11, row, "PO", shf2));
        
        ws.addCell(new Label(12, row, "INV", shf2));
        
        ws.addCell(new Label(13, row, "Status", shf2));
        ws.mergeCells(13, row, 14, row);
        
        ws.addCell(new Label(15, row, "Remarks", shf2));
        ws.mergeCells(15, row, 17, row);
        
        ws.addCell(new Label(18, row, "PO", shf2));
        
        ws.addCell(new Label(19, row, "GRN", shf2));
        
        ws.addCell(new Label(20, row, "INV", shf2));
        
        ws.addCell(new Label(21, row, "Status", shf2));
        ws.mergeCells(21, row, 22, row);

        ws.addCell(new Label(23, row, "Remarks", shf2));
        ws.mergeCells(23, row, 25, row);
        
        List<PoInvGrnDnMatchingDetailExHolder> detailList = parameter.getMatchingDetails();
        int seq = 1;
        for (PoInvGrnDnMatchingDetailHolder detail : detailList)
        {
            row = createDetailItem(ws, detail, row, seq ++);
        }
        return row;
        
    }
    
    
    private int createDetailItem(WritableSheet ws, PoInvGrnDnMatchingDetailHolder detail, int row, int seq) throws Exception
    {
        row = row + 1;
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf3 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf3.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf3.setAlignment(Alignment.RIGHT);
        shf3.setIndentation(1);
        
        ws.addCell(new Label(0, row, StringUtil.getInstance()
                .convertObjectToString(seq), shf1));
        
        ws.addCell(new Label(0, row, StringUtil.getInstance()
                .convertObjectToString(seq), shf1));
        
        ws.addCell(new Label(1, row, StringUtil.getInstance()
                .convertObjectToString(detail.getBuyerItemCode()), shf1));
        ws.mergeCells(1, row, 2, row);
        
        ws.addCell(new Label(3, row, StringUtil.getInstance()
                .convertObjectToString(detail.getBarcode()), shf1));
        ws.mergeCells(3, row, 5, row);
        
        ws.addCell(new Label(6, row, StringUtil.getInstance()
                .convertObjectToString(detail.getUom()), shf1));
        
        ws.addCell(new Label(7, row, StringUtil.getInstance()
                .convertObjectToString(detail.getItemDesc()), shf1));
        ws.mergeCells(7, row, 10, row);
        
        ws.addCell(new Label(11, row, StringUtil.getInstance()
                .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getPoPrice())), shf3));
        
        ws.addCell(new Label(12, row, StringUtil.getInstance()
                .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getInvPrice())), shf3));
        
        ws.addCell(new Label(13, row, StringUtil.getInstance()
                .convertObjectToString(detail.getPriceStatus()), 
                PoInvGrnDnMatchingPriceStatus.REJECTED.equals(detail.getPriceStatus()) ? shf2 : shf1));
        ws.mergeCells(13, row, 14, row);
        
        ws.addCell(new Label(15, row, StringUtil.getInstance()
                .convertObjectToString(detail.getPriceStatusActionRemarks()), shf1));
        ws.mergeCells(15, row, 17, row);
        
        ws.addCell(new Label(18, row, StringUtil.getInstance()
                .convertObjectToString(detail.getPoQty()), shf1));
        
        ws.addCell(new Label(19, row, StringUtil.getInstance()
                .convertObjectToString(detail.getGrnQty()), shf1));
        
        ws.addCell(new Label(20, row, StringUtil.getInstance()
                .convertObjectToString(detail.getInvQty()), shf1));
        
        ws.addCell(new Label(21, row, StringUtil.getInstance()
                .convertObjectToString(detail.getQtyStatus()), 
                PoInvGrnDnMatchingQtyStatus.REJECTED.equals(detail.getQtyStatus()) ? shf2 : shf1));
        ws.mergeCells(21, row, 22, row);
        
        ws.addCell(new Label(23, row, StringUtil.getInstance()
                .convertObjectToString(detail.getQtyStatusActionRemarks()), shf1));
        ws.mergeCells(23, row, 25, row);
        
        return row;
    }
    
    
    private Map<String, List<MatchingReportParameter>> groupParametersByBuyer(List<MatchingReportParameter> matchingList)
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return null;
        }
        
        Map<String, List<MatchingReportParameter>> map = new HashMap<String, List<MatchingReportParameter>>();
        
        for (MatchingReportParameter parameter : matchingList)
        {
            if (map.containsKey(parameter.getBuyerCode()))
            {
                map.get(parameter.getBuyerCode()).add(parameter);
            }
            else
            {
                List<MatchingReportParameter> list = new ArrayList<MatchingReportParameter>();
                list.add(parameter);
                map.put(parameter.getBuyerCode(), list);
            }
        }
        return map;
    }
    
    
    private Map<String, List<MatchingReportParameter>> groupParametersByPo(List<MatchingReportParameter> matchingList)
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return null;
        }
        
        Map<String, List<MatchingReportParameter>> map = new HashMap<String, List<MatchingReportParameter>>();
        
        for (MatchingReportParameter parameter : matchingList)
        {
            if (map.containsKey(parameter.getPoNo()))
            {
                map.get(parameter.getPoNo()).add(parameter);
            }
            else
            {
                List<MatchingReportParameter> list = new ArrayList<MatchingReportParameter>();
                list.add(parameter);
                map.put(parameter.getPoNo(), list);
            }
        }
        return map;
    }
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "Po No"));
        col = col +1;
        ws.addCell(new Label(col, row, "Po Sub Type"));
        col = col +1;
        ws.addCell(new Label(col, row, "Po Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "PO Store Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "GRN No"));
        col = col +1;
        ws.addCell(new Label(col, row, "GRN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "GRN Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv No"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Amt with GST"));
        col = col +1;
        ws.addCell(new Label(col, row, "Buyer Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Buyer Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Matching Status"));
    }
    
    private void initFlattenedItems(WritableSheet ws, 
        List<MatchingReportParameter> acceptList, List<MatchingReportParameter> rejectList) throws Exception
    {
        if ((acceptList == null || acceptList.isEmpty()) && (rejectList == null || rejectList.isEmpty()))
        {
            return;
        }
        int col = 0;
        
        List<MatchingReportParameter> reports = new ArrayList<MatchingReportParameter>();
        if (acceptList != null && !acceptList.isEmpty())
        {
            reports.addAll(acceptList);
        }
        if (rejectList != null && !rejectList.isEmpty())
        {
            reports.addAll(rejectList);
        }
        for (int i = 0; i < reports.size(); i++)
        {
            col = 0;
            MatchingReportParameter report = reports.get(i);
            
            ws.addCell(new Label(col, i + 1, report.getPoNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getPoSubType2()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getPoDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getStoreCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getStoreName() == null ? buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getBuyerCode(), report.getStoreCode()).getStoreName() : report.getStoreName()));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getPoAmt())));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, MatchingReportParameter.getGrnNos(report.getGrnNos())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, MatchingReportParameter.getGrnDates(report.getGrnDates())));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, ""));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getInvNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getInvDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getInvAmtWithVat())));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getBuyerCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getBuyerName()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance()
                .convertObjectToString(report.getMatchingResult()
                    .getMatchingStatus())));
        }
        
        ws.setColumnView(0, 13);
        ws.setColumnView(1, 12);
        ws.setColumnView(2, 12);
        ws.setColumnView(3, 10);
        ws.setColumnView(4, 25);
        ws.setColumnView(5, 15);
        ws.setColumnView(6, 13);
        ws.setColumnView(7, 12);
        ws.setColumnView(8, 10);
        ws.setColumnView(9, 13);
        ws.setColumnView(10, 12);
        ws.setColumnView(11, 18);
        ws.setColumnView(12, 14);
        ws.setColumnView(13, 30);
        ws.setColumnView(14, 18);
    }
}
