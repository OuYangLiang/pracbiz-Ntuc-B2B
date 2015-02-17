package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnExportingReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    @Autowired private transient CustomAppConfigHelper appConfig;
    @Autowired private transient PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    @Autowired private transient PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    
    public byte[] exportExcel(List<DnHeaderHolder> parameters, BuyerHolder buyer) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            
            List<DnHeaderHolder> matchingDns = new ArrayList<DnHeaderHolder>();
            List<DnHeaderHolder> rtvDns = new ArrayList<DnHeaderHolder>();
            
            for (DnHeaderHolder header : parameters)
            {
                if (DnType.STK_RTV.name().equalsIgnoreCase(header.getDnType()))
                {
                    rtvDns.add(header);
                }
                else if (DnType.STK_QOC.name().equalsIgnoreCase(header.getDnType())
                    || DnType.CST_IOC.name().equalsIgnoreCase(header.getDnType()))
                {
                    matchingDns.add(header);
                }
            }
            
            int sheetIndex = 0;
            if (!rtvDns.isEmpty())
            {
                WritableSheet rtvDnSheet = wwb.createSheet("RTV-DN Summary Report", sheetIndex);
                createRtvDnSummaryFieldAndItems(rtvDnSheet, buyer, groupByDate(rtvDns));
                sheetIndex++;
                WritableSheet rtvDnFlattenedSheet = wwb.createSheet("RTV-DN Flattened Summary Report", sheetIndex);
                initRtvDnFlattenedTitle(rtvDnFlattenedSheet);
                initRtvDnFlattenedItems(rtvDnFlattenedSheet, rtvDns);
                sheetIndex++;
            }
            if (!matchingDns.isEmpty())
            {
                WritableSheet matchingDnSheet = wwb.createSheet("Matching-DN Summary Report", sheetIndex);
                createMatchingDnSummaryFieldAndItems(matchingDnSheet, buyer, groupByDate(matchingDns));
                sheetIndex++;
                WritableSheet matchingDnFlattenedSheet = wwb.createSheet("Matching-DN Flattened Summary Report", sheetIndex);
                initMatchingDnFlattenedTitle(matchingDnFlattenedSheet);
                initMatchingDnFlattenedItems(matchingDnFlattenedSheet, matchingDns);
                sheetIndex++;
            }
            
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
    
    
    private Map<String, List<DnHeaderHolder>> groupByDate(List<DnHeaderHolder> list)
    {
        if (list == null)
        {
            return null;
        }
        
        Collections.sort(list, new Comparator<DnHeaderHolder>(){

            @Override
            public int compare(DnHeaderHolder m1, DnHeaderHolder m2)
            {
                return m1.getExportedDate().before(m2.getExportedDate()) ? 1 : -1;
            }
            
        });
        
        Map<String, List<DnHeaderHolder>> map = new HashMap<String, List<DnHeaderHolder>>();
        
        for (DnHeaderHolder parameter : list)
        {
            String date = DateUtil.getInstance().convertDateToString(parameter.getExportedDate());
            if (map.containsKey(date))
            {
                map.get(date).add(parameter);
            }
            else
            {
                List<DnHeaderHolder> parameters = new ArrayList<DnHeaderHolder>();
                parameters.add(parameter);
                map.put(date, parameters);
            }
        }
        return map;
    }
    
    
    private void createRtvDnSummaryFieldAndItems(WritableSheet ws, BuyerHolder buyer,  Map<String, List<DnHeaderHolder>> parameters) throws Exception
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
        
        ws.addCell(new Label(0, 0, "RTV-Debit Note Export Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Buyer:", summaryFormat));
        ws.mergeCells(0, 2, 1, 2);
        ws.addCell(new Label(2, 2, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(2, 2, 4, 2);
        
        
        
        int row = 4;
        for (Map.Entry<String, List<DnHeaderHolder>> entry : parameters.entrySet())
        {
            ws.addCell(new Label(0, row, "Export Date:", summaryFormat));
            ws.mergeCells(0, row, 1, row);
            ws.addCell(new Label(2, row, entry.getKey(), summaryFormat));
            ws.mergeCells(2, row, 4, row);
            
            ws.addCell(new Label(0, row + 2, "The following debit notes have been exported to your back end system:", summaryFormat));
            ws.mergeCells(0, row + 2, 12, row + 2);
            int col = 0;
            ws.addCell(new Label(col, row + 3, "No.", shf));
            col = col + 1;
            ws.addCell(new Label(col, row + 3, "Supplier Code", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "RTV No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "GI No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN Date", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN Amount", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            
            //create items
            
            row = row + 4;
            BigDecimal totalDnAmount = BigDecimal.ZERO;
            List<DnHeaderHolder> reports = entry.getValue();
            
            for (int i = 0; i < reports.size(); i++)
            {
                col = 0;
                DnHeaderHolder parameter = reports.get(i);
                
                ws.addCell(new Label(col, row, String.valueOf(i + 1), shf1));
                col = col + 1;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getSupplierCode()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getRtvNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getGiNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getDnNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(parameter.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(parameter.getTotalCost())), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                totalDnAmount = totalDnAmount.add(parameter.getTotalCost());
                row = row + 1;
            }
            
            ws.addCell(new Label(13, row, "Total:", shf2));
            ws.mergeCells(13, row, 15, row);
            
            ws.addCell(new Label(16, row, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(totalDnAmount)), shf1));
            ws.mergeCells(16, row, 18, row);
            row = row + 3;
        }
    }
    
    private void initRtvDnFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "RTV No"));
        col = col +1;
        ws.addCell(new Label(col, row, "GI No"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN No"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
    }
    
    private void initRtvDnFlattenedItems(WritableSheet ws, 
        List<DnHeaderHolder> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return;
        }
        int col = 0;
        
        for (int i = 0; i < reports.size(); i++)
        {
            col = 0;
            DnHeaderHolder report = reports.get(i);
            
            BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getBuyerCode(), report.getStoreCode());
            
            ws.addCell(new Label(col, i + 1, report.getRtvNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getGiNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, (buyerStore.getIsWareHouse() ? (appConfig.getWarehousePrefix() == null ? "" : appConfig.getWarehousePrefix()) : (appConfig.getStorePrefix() == null ? "" : appConfig.getStorePrefix())) + report.getStoreCode() ));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getStoreName() == null ? buyerStore.getStoreName() : report.getStoreName()));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getTotalCost())));
            
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
    
    
    private void createMatchingDnSummaryFieldAndItems(WritableSheet ws, BuyerHolder buyer,  Map<String, List<DnHeaderHolder>> parameters) throws Exception
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
        
        ws.addCell(new Label(0, 0, "Matching-Debit Note Export Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Buyer:", summaryFormat));
        ws.mergeCells(0, 2, 1, 2);
        ws.addCell(new Label(2, 2, buyer.getBuyerName() + "(" + buyer.getBuyerCode() + ")", summaryFormat));
        ws.mergeCells(2, 2, 4, 2);
        
        
        
        int row = 4;
        for (Map.Entry<String, List<DnHeaderHolder>> entry : parameters.entrySet())
        {
            ws.addCell(new Label(0, row, "Export Date:", summaryFormat));
            ws.mergeCells(0, row, 1, row);
            ws.addCell(new Label(2, row, entry.getKey(), summaryFormat));
            ws.mergeCells(2, row, 4, row);
            
            ws.addCell(new Label(0, row + 2, "The following debit notes have been exported to your back end system:", summaryFormat));
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
            ws.addCell(new Label(col, row + 3, "GRN Nos", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN No", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN Date", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            col = col + 3;
            ws.addCell(new Label(col, row + 3, "DN Amount", shf));
            ws.mergeCells(col, row + 3, col + 2, row + 3);
            
            //create items
            
            row = row + 4;
            BigDecimal totalDnAmount = BigDecimal.ZERO;
            List<DnHeaderHolder> reports = entry.getValue();
            
            for (int i = 0; i < reports.size(); i++)
            {
                col = 0;
                DnHeaderHolder parameter = reports.get(i);
                
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
                
                PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
                param.setBuyerCode(parameter.getBuyerCode());
                param.setSupplierCode(parameter.getSupplierCode());
                param.setPoNo(parameter.getPoNo());
                param.setInvNo(parameter.getInvNo());
                param.setDnNo(parameter.getDnNo());
                List<PoInvGrnDnMatchingHolder> matching = poInvGrnDnMatchingService.select(param);
                
                List<PoInvGrnDnMatchingGrnHolder> matchingGrns = poInvGrnDnMatchingGrnService.selectByMatchOid(matching.get(0).getMatchingOid());
                
                
                ws.addCell(new Label(col, row, this.getGrnNos(matchingGrns), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(parameter.getDnNo()), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(parameter.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(parameter.getTotalCost())), shf1));
                ws.mergeCells(col, row, col + 2, row);
                col = col + 3;
                
                totalDnAmount = totalDnAmount.add(parameter.getTotalCost());
                row = row + 1;
            }
            
            ws.addCell(new Label(16, row, "Total:", shf2));
            ws.mergeCells(16, row, 18, row);
            
            ws.addCell(new Label(19, row, StringUtil.getInstance().convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(totalDnAmount)), shf1));
            ws.mergeCells(19, row, 21, row);
            row = row + 3;
        }
    }
    
    private void initMatchingDnFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "PO No"));
        col = col +1;
        ws.addCell(new Label(col, row, "INV No"));
        col = col +1;
        ws.addCell(new Label(col, row, "GRN Nos"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN No"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Date"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Store Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Store Name"));
        col = col +1;
        ws.addCell(new Label(col, row, "DN Amt"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Supplier Name"));
    }
    
    private void initMatchingDnFlattenedItems(WritableSheet ws, 
        List<DnHeaderHolder> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return;
        }
        int col = 0;
        
        for (int i = 0; i < reports.size(); i++)
        {
            col = 0;
            DnHeaderHolder report = reports.get(i);
            
            BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getBuyerCode(), report.getStoreCode());
            
            ws.addCell(new Label(col, i + 1, report.getPoNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getInvNo()));
            col = col +1;
            
            PoInvGrnDnMatchingHolder param = new PoInvGrnDnMatchingHolder();
            param.setBuyerCode(report.getBuyerCode());
            param.setSupplierCode(report.getSupplierCode());
            param.setPoNo(report.getPoNo());
            param.setInvNo(report.getInvNo());
            param.setDnNo(report.getDnNo());
            List<PoInvGrnDnMatchingHolder> matching = poInvGrnDnMatchingService.select(param);
            
            List<PoInvGrnDnMatchingGrnHolder> matchingGrns = poInvGrnDnMatchingGrnService.selectByMatchOid(matching.get(0).getMatchingOid());
            
            ws.addCell(new Label(col, i + 1, this.getGrnNos(matchingGrns)));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, report.getDnNo()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, DateUtil.getInstance().convertDateToString(
                report.getDnDate(), DateUtil.DEFAULT_DATE_FORMAT)));
            col = col +1;
            ws.addCell(new Label(col, i + 1, (buyerStore.getIsWareHouse() ? (appConfig.getWarehousePrefix() == null ? "" : appConfig.getWarehousePrefix()) : (appConfig.getStorePrefix() == null ? "" : appConfig.getStorePrefix())) + report.getStoreCode() ));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getStoreName() == null ? buyerStore.getStoreName() : report.getStoreName()));
            col = col +1;
            
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance().convertObjectToString(report.getTotalCost())));
            
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getSupplierName()));
        }
        
        ws.setColumnView(0, 13);
        ws.setColumnView(1, 13);
        ws.setColumnView(2, 16);
        ws.setColumnView(3, 12);
        ws.setColumnView(4, 14);
        ws.setColumnView(5, 15);
        ws.setColumnView(6, 25);
        ws.setColumnView(7, 12);
        ws.setColumnView(8, 13);
        ws.setColumnView(9, 40);
    }
    
    private String getGrnNos(List<PoInvGrnDnMatchingGrnHolder> matchingGrns)
    {
        if (matchingGrns == null || matchingGrns.isEmpty())
        {
            return "";
        }
        
        StringBuffer sb = new StringBuffer();
        for (PoInvGrnDnMatchingGrnHolder grn : matchingGrns)
        {
            sb.append(grn.getGrnNo()).append(",");
        }
        
        String grns = sb.toString();
        if (grns.length() != 0)
        {
            grns = grns.substring(0, grns.length() - 1);
        }
        
        return grns;
    }
}
