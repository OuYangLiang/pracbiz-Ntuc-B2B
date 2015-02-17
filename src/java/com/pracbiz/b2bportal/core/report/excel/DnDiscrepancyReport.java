package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnDiscrepancyReport
{
    public byte[] exportExcel(List<DnHeaderHolder> dnHeaders, BuyerHolder buyer) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("Summary Report", 0);
            createSummaryField(sheet, buyer, dnHeaders);
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, dnHeaders);
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
    
    
    private void createSummaryField(WritableSheet ws, BuyerHolder buyer, List<DnHeaderHolder> dnHeaders) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "Discrepancy Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Exported:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 2, 2);
        
        ws.addCell(new Label(0, 3, "Buyer:", summaryFormat));
        ws.addCell(new Label(1, 3, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(1, 3, 2, 3);
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        int col = 0, row = 7;
        
        ws.addCell(new Label(col, row, "No.", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Supplier Code", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "DN No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "RTV No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "GI No (date)", shf));
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        col = 0; row = row + 1;
        
        Collections.sort(dnHeaders, new Comparator<DnHeaderHolder>() {

            public int compare(DnHeaderHolder o1, DnHeaderHolder o2)
            {
                return o1.getSupplierCode().compareTo(o2.getSupplierCode());
            }}
        );
        
        for (int i = 0; i < dnHeaders.size(); i ++)
        {
            DnHeaderHolder header = dnHeaders.get(i);
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(i + 1), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.getSupplierCode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getDnNo(), header.getDnDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getRtvNo(), header.getRtvDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getGiNo(), header.getGiDate()), shf1));
            
            row = row + 1;
            
            ws.addCell(new Label(col, row, "", shf2));
            ws.mergeCells(0, row, col, row);
            
            row = row + 1;
            
            col = 0;
        }
        
        ws.setColumnView(1, 20);
        ws.setColumnView(2, 30);
        ws.setColumnView(3, 30);
        ws.setColumnView(4, 30);
    }
    
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN No"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "RTV No"));
        col = col +1;
        ws.addCell(new Label(col, row, "RTV Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "GI No"));
        col = col +1;
        ws.addCell(new Label(col, row, "GI Date"));
        
    }
    
    private void initFlattenedItems(WritableSheet ws, 
        List<DnHeaderHolder> dnHeaders) throws Exception
    {
        if (dnHeaders == null || dnHeaders.isEmpty())
        {
            return;
        }
        int col = 0;
        
        for (int i = 0; i < dnHeaders.size(); i++)
        {
            col = 0;
            DnHeaderHolder report = dnHeaders.get(i);
            
            ws.addCell(new Label(col, i + 1, report.getSupplierCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierName()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getRtvNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getRtvDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getGiNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getGiDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            
        }
        
        ws.setColumnView(0, 13);
        ws.setColumnView(1, 40);
        ws.setColumnView(2, 12);
        ws.setColumnView(3, 13);
        ws.setColumnView(4, 12);
        ws.setColumnView(5, 13);
        ws.setColumnView(6, 12);
        ws.setColumnView(7, 13);
    }
}
