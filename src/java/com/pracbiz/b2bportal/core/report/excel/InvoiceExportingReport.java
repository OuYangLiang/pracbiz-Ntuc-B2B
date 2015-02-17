package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




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
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class InvoiceExportingReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    @Autowired private transient CustomAppConfigHelper appConfig;
    
    public byte[] exportExcel(List<MatchingReportParameter> parameters, BuyerHolder buyer) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            WritableSheet sheet = wwb.createSheet("Summary Report", 0);
            createSummaryFieldAndItems(sheet, buyer, groupByDate(parameters));
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, parameters);
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
    
    
    private Map<String, List<MatchingReportParameter>> groupByDate(List<MatchingReportParameter> list)
    {
        if (list == null)
        {
            return null;
        }
        
        Collections.sort(list, new Comparator<MatchingReportParameter>(){

            @Override
            public int compare(MatchingReportParameter m1, MatchingReportParameter m2)
            {
                return m1.getApproveInvDate().before(m2.getApproveInvDate()) ? 1 : -1;
            }
            
        });
        
        Map<String, List<MatchingReportParameter>> map = new HashMap<String, List<MatchingReportParameter>>();
        
        for (MatchingReportParameter parameter : list)
        {
            String date = DateUtil.getInstance().convertDateToString(parameter.getApproveInvDate());
            if (map.containsKey(date))
            {
                map.get(date).add(parameter);
            }
            else
            {
                List<MatchingReportParameter> parameters = new ArrayList<MatchingReportParameter>();
                parameters.add(parameter);
                map.put(date, parameters);
            }
        }
        return map;
    }
    
    
    private void createSummaryFieldAndItems(WritableSheet ws, BuyerHolder buyer,  Map<String, List<MatchingReportParameter>> parameters) throws Exception
    {
        if (parameters == null || parameters.isEmpty())
        {
            return;
        }
        
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        
        ws.addCell(new Label(0, 0, "Invoice Export Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Buyer:", summaryFormat));
        ws.mergeCells(0, 2, 1, 2);
        ws.addCell(new Label(2, 2, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(2, 2, 4, 2);
        
        
        
        int row = 4;
        for (Map.Entry<String, List<MatchingReportParameter>> entry : parameters.entrySet())
        {
            ws.addCell(new Label(0, row, "Export Date:", summaryFormat));
            ws.mergeCells(0, row, 1, row);
            ws.addCell(new Label(2, row, entry.getKey(), summaryFormat));
            ws.mergeCells(2, row, 4, row);
            
            ws.addCell(new Label(0, row + 2, "The following invoices have been exported to your back end system:", summaryFormat));
            ws.mergeCells(0, row + 2, 12, row + 2);
            int col = 0;
            ws.addCell(new Label(col, row + 3, "No.", shf));
            col = col + 1;
            ws.addCell(new Label(col, row + 3, "Supplier Code", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "PO No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "INV No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "INV Date", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "First GRN Date", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "INV Amount", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            
            //create items
            
            row = row + 4;
            BigDecimal totalInvAmount = BigDecimal.ZERO;
            List<MatchingReportParameter> reports = entry.getValue();
            
            for (int i = 0; i < reports.size(); i++)
            {
                col = 0;
                MatchingReportParameter parameter = reports.get(i);
                
                ws.addCell(new Label(col, row, String.valueOf(i + 1), shf1));
                col = col + 1;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getSupplierCode()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getPoNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getInvNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(parameter.getInvDate(), DateUtil.DEFAULT_DATE_FORMAT), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(parameter.getFirstGrnDate(), DateUtil.DEFAULT_DATE_FORMAT), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(parameter.getInvAmt())), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                totalInvAmount = totalInvAmount.add(parameter.getInvAmt());
                row = row + 1;
            }
            
            ws.addCell(new Label(13, row, "Total:", shf2));
            ws.mergeCells(13, row, 15, row);
            
            ws.addCell(new Label(16, row, StringUtil.getInstance().convertObjectToString(totalInvAmount), shf2));
            ws.mergeCells(16, row, 18, row);
            row = row + 3;
        }
    }
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "PO No"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv No"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "First GRN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "Inv Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
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
            
            BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getBuyerCode(), report.getStoreCode());
            
            ws.addCell(new Label(col, i + 1, report.getPoNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getInvNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getInvDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getFirstGrnDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, (buyerStore.getIsWareHouse() ? (appConfig.getWarehousePrefix() == null ? "" : appConfig.getWarehousePrefix()) : (appConfig.getStorePrefix() == null ? "" : appConfig.getStorePrefix())) + report.getStoreCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getStoreName() == null ? buyerStore.getStoreName() : report.getStoreName()));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getInvAmt())));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierName()));
        }
        
        ws.setColumnView(0, 13);
        ws.setColumnView(1, 13);
        ws.setColumnView(2, 12);
        ws.setColumnView(3, 14);
        ws.setColumnView(4, 15);
        ws.setColumnView(5, 25);
        ws.setColumnView(6, 12);
        ws.setColumnView(7, 13);
        ws.setColumnView(8, 40);
    }
}
