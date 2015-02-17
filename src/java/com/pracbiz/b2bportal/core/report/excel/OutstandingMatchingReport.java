package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
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
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class OutstandingMatchingReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(List<MatchingReportParameter> matchingList, BuyerHolder buyer) throws Exception
    {
        
        Collections.sort(matchingList, new Comparator<MatchingReportParameter>() {

            @Override
            public int compare(MatchingReportParameter o1,
                    MatchingReportParameter o2)
            {
                return o2.getDaysSpaned().compareTo(
                        o1.getDaysSpaned());
            }
        }
        );
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("Summary Report", 0);
            createSummaryField(sheet, buyer);
            createItem(sheet, matchingList);
            
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, matchingList);
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
    
    
    public void createSummaryField(WritableSheet ws, BuyerHolder buyer) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "Outstanding Matching Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 3, 2);
        
        ws.addCell(new Label(0, 3, "Buyer:", summaryFormat));
        ws.addCell(new Label(1, 3, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(1, 3, 3, 3);
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        int col = 0, row = 7;
        ws.addCell(new Label(col, row, "No.", shf));
        ws.mergeCells(col, row, col, row + 1);
        col = col + 1;
        ws.addCell(new Label(col, row, "Supplier Code", shf));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "PO No. (PO Date)", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Store Code", shf));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "Store PO Amt", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "INV No. (INV Date)", shf));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "INV Amt/INV Amt w GST", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "GRN No.(GRN Date)", shf));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "Grn Amt", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Match Status", shf));
        ws.mergeCells(col, row, col + 2, row + 1);
        col = col + 3;
        ws.addCell(new Label(col, row, "Days Elapsed", shf));
        ws.mergeCells(col, row, col + 1, row + 1);
        col = col + 2;
        ws.addCell(new Label(col, row, "GCM Status", shf));
        ws.mergeCells(col, row, col + 1, row + 1);
        col = col + 2;
        ws.addCell(new Label(col, row, "Store Status", shf));
        ws.mergeCells(col, row, col + 1, row + 1);
        col = col + 2;
    }
    
    
    private void createItem(WritableSheet ws, List<MatchingReportParameter> matchingList) throws Exception
    {
        if (matchingList == null || matchingList.isEmpty())
        {
            return;
        }
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        int row = 9;
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
                    + "/" + StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(matching.getExpInvAmt())), shf1));
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
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getDaysSpaned()), shf1));
            ws.mergeCells(col, row, col + 1, row + 1);
            col = col + 2;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getPriceStatus()), shf1));
            ws.mergeCells(col, row, col + 1, row + 1);
            col = col + 2;
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(matching.getQtyStatus()), shf1));
            ws.mergeCells(col, row, col + 1, row + 1);
            col = col + 2;
            
            ws.addCell(new Label(0, row + 2, "", shf2));
            ws.mergeCells(0, row + 2, col + 1, row + 2);
            
            row = row + 3;
        }
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
        ws.addCell(new Label(col, row, "Inv Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Matching Status"));
        col = col +1;
        ws.addCell(new Label(col, row, "Days Elapsed"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Status"));
        col = col +1;
        ws.addCell(new Label(col, row, "GCM Status"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Status"));
    }
    
    private void initFlattenedItems(WritableSheet ws, 
        List<MatchingReportParameter> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return;
        }
        int col = 0;
        
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
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getInvAmt())));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierName()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance()
                .convertObjectToString(report.getMatchingResult()
                    .getMatchingStatus())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.getDaysSpaned())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.getActionStatus())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.getPriceStatus())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.getQtyStatus())));
        }
        
        ws.setColumnView(0, 13);
        ws.setColumnView(1, 12);
        ws.setColumnView(2, 12);
        ws.setColumnView(3, 10);
        ws.setColumnView(4, 25);
        ws.setColumnView(5, 15);
        ws.setColumnView(6, 13);
        ws.setColumnView(7, 12);
        ws.setColumnView(8, 12);
        ws.setColumnView(9, 13);
        ws.setColumnView(10, 12);
        ws.setColumnView(11, 10);
        ws.setColumnView(12, 14);
        ws.setColumnView(13, 40);
        ws.setColumnView(14, 18);
        ws.setColumnView(15, 12);
        ws.setColumnView(16, 14);
        ws.setColumnView(17, 14);
        ws.setColumnView(18, 14);
    }
}
