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
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnOutstandingReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    
    public byte[] exportExcel(List<DnHolder> dns, BuyerHolder buyer) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("Summary Report", 0);
            createSummaryField(sheet, buyer, dns);
            
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, dns);
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
    
    
    private void createSummaryField(WritableSheet ws, BuyerHolder buyer, List<DnHolder> dnHolders) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "DN Outstanding Report", titleFormat));
        
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
        ws.addCell(new Label(col, row, "Store Code", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "DN No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "RTV No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "GI No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "DN Status", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Days Elapsed", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "GCM Status", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Store Status", shf));
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        col = 0; row = row + 1;
        
        for (int i = 0; i < dnHolders.size(); i ++)
        {
            DnHolder dnHolder = dnHolders.get(i);
            DnHeaderHolder header = dnHolder.getDnHeader();
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(i + 1), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.getSupplierCode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.getStoreCode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getDnNo(), header.getDnDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getRtvNo(), header.getRtvDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getGiNo(), header.getGiDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getGiNo(), header.getGiDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.calculateDayElapsed()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(dnHolder.isDisputePriceChanged() ? header.getPriceStatus().name() : ""), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(dnHolder.isDisputeQtyChanged() ? header.getQtyStatus().name() : ""), shf1));
            
            row = row + 1;
            
            ws.addCell(new Label(col, row, "", shf2));
            ws.mergeCells(0, row, col, row);
            
            row = row + 1;
            
            col = 0;
        }
        
        ws.setColumnView(1, 20);
        ws.setColumnView(2, 20);
        ws.setColumnView(3, 30);
        ws.setColumnView(4, 30);
        ws.setColumnView(5, 30);
        ws.setColumnView(6, 30);
        ws.setColumnView(7, 30);
        ws.setColumnView(8, 30);
        ws.setColumnView(9, 30);
    }
    
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Name"));
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
        col = col +1;
        ws.addCell(new Label(col, row, "Days Elapsed"));
        col = col +1;
        ws.addCell(new Label(col, row, "GCM Status"));
        col = col +1;
        ws.addCell(new Label(col, row, "Store Status"));
    }
    
    private void initFlattenedItems(WritableSheet ws, 
        List<DnHolder> dns) throws Exception
    {
        if (dns == null || dns.isEmpty())
        {
            return;
        }
        int col = 0;
        
        for (int i = 0; i < dns.size(); i++)
        {
            col = 0;
            DnHolder report = dns.get(i);
            
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getSupplierCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getSupplierName()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getStoreCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getStoreName() == null ? buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getDnHeader().getBuyerCode(), report.getDnHeader().getStoreCode()).getStoreName() : report.getDnHeader().getStoreName()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getDnNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnHeader().getDnDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getRtvNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnHeader().getRtvDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getGiNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnHeader().getGiDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.getDnHeader().calculateDayElapsed())));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.isDisputePriceChanged() ? report.getDnHeader().getPriceStatus().name() : "")));
            col = col +1;
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(
                report.isDisputeQtyChanged() ? report.getDnHeader().getQtyStatus().name() : "")));
        }
        
        ws.setColumnView(0, 14);
        ws.setColumnView(1, 40);
        ws.setColumnView(2, 10);
        ws.setColumnView(3, 30);
        ws.setColumnView(4, 13);
        ws.setColumnView(5, 12);
        ws.setColumnView(6, 13);
        ws.setColumnView(7, 12);
        ws.setColumnView(8, 13);
        ws.setColumnView(9, 12);
        ws.setColumnView(10, 12);
        ws.setColumnView(11, 14);
        ws.setColumnView(12, 14);
    }
}
