package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.ScriptStyle;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableHyperlink;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnSupplierResolutionReport
{
    public byte[] exportExcel(List<DnHolder> dnList, SupplierHolder supplier) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);
            
            Map<String, List<DnHolder>> map = this.groupDnByBuyer(dnList);
            
            Map<String, WritableSheet> detailSheetMap = new HashMap<String, WritableSheet>();
            
            int index = 2;
            for (Map.Entry<String, List<DnHolder>> entry : map.entrySet())
            {
                WritableSheet detail = wwb.createSheet(entry.getKey(), index);
                detailSheetMap.put(entry.getKey(), detail);
                createDetailField(detail, entry.getKey(), entry.getValue().get(0).getDnHeader().getBuyerName(), entry.getValue());
                index ++ ;
            }
            
            WritableSheet summary = wwb.createSheet("Summary Report", 0);
            createSummaryField(summary, supplier, dnList, detailSheetMap);
            
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", 1);
            initFlattenedTitle(flattened);
            initFlattenedItems(flattened, dnList);
            
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
    
    
    private void createSummaryField(WritableSheet ws, SupplierHolder supplier, List<DnHolder> dnList, Map<String, WritableSheet> detailSheetMap) 
            throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "Resolution Report", titleFormat));
        
        ws.addCell(new Label(0, 2, "Exported:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 2, 2);
        
        ws.addCell(new Label(0, 3, "Supplier:", summaryFormat));
        ws.addCell(new Label(1, 3, supplier.getSupplierName() + "(" + supplier.getSupplierCode() + ")", summaryFormat));
        ws.mergeCells(1, 3, 2, 3);
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        int col = 0, row = 7;
        
        ws.addCell(new Label(col, row, "No.", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Buyer Code", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "DN No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "RTV No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "GI No (date)", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Buyer Status", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "DN Status", shf));
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf1_red = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf1_red.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1_red.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        col = 0; row = row + 1;
        
        
        for (int i = 0; i < dnList.size(); i ++)
        {
            DnHeaderHolder header = dnList.get(i).getDnHeader();
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(i + 1), shf1));
            ws.addHyperlink(new WritableHyperlink(col, row, StringUtil.getInstance().convertObjectToString(i+1), 
                    detailSheetMap.get(header.getBuyerCode()), 0, 0));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.getBuyerCode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getDnNo(), header.getDnDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getRtvNo(), header.getRtvDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, MatchingReportParameter.getNoAndDate(header.getGiNo(), header.getGiDate()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, header.getDispute() != null && header.getDispute() ? StringUtil.getInstance().convertObjectToString(
                    header.getBuyerStatus().name()) : "", DnBuyerStatus.REJECTED.equals(header.getBuyerStatus()) ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(header.getDnStatus().name()), DnStatus.OUTDATED.equals(header.getDnStatus()) ? shf1_red : shf1));
            
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
        ws.setColumnView(5, 30);
        ws.setColumnView(6, 30);
        ws.setColumnView(7, 30);
    }
    
    
    private void createDetailField(WritableSheet ws, String buyerCode, String buyerName, List<DnHolder> dnList) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        
        ws.addCell(new Label(0, 0, "Resolution Report - Detail", titleFormat));
        
        ws.addCell(new Label(0, 2, "Buyer:", summaryFormat));
        ws.addCell(new Label(1, 2, buyerName + "(" + buyerCode + ")", summaryFormat));
        ws.mergeCells(1, 2, 2, 2);
        
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        int row = 5;
        for (DnHolder dn : dnList)
        {
            List<DnDetailExHolder> detailList = dn.getDnDetail();
            row = createDetailItem(ws, dn.getDnHeader().getDnNo(), detailList, row);
        }
        
        ws.setColumnView(1, 20);
        ws.setColumnView(2, 20);
        ws.setColumnView(3, 20);
        ws.setColumnView(4, 20);
        ws.setColumnView(5, 20);
        ws.setColumnView(6, 20);
        ws.setColumnView(7, 20);
        ws.setColumnView(8, 20);
        ws.setColumnView(9, 20);
        ws.setColumnView(10, 20);
        ws.setColumnView(11, 20);
        ws.setColumnView(12, 20);
        ws.setColumnView(13, 20);
        ws.setColumnView(14, 20);
        
    }
    
    
    private int createDetailItem(WritableSheet ws, String dnNo, List<DnDetailExHolder> detailList, int row) throws Exception
    {
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_25);
        
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf1_red = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf1_red.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1_red.setAlignment(Alignment.CENTRE);
        
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        
        int col = 0;
        
        ws.addCell(new Label(col, row, "DN No:", shf1));
        ws.addCell(new Label(col + 1, row, dnNo, shf1));
        
        row = row + 1;
        
        ws.addCell(new Label(col, row, "Seq.", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Item Code", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Barcode", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "UOM", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Description", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Price", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Dispute Price", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Price Status", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Price Remarks", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Confirm Price", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Qty", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Dispute Qty", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Qty Status", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Qty Remarks", shf));
        col = col + 1;
        ws.addCell(new Label(col, row, "Confirm Qty", shf));
        
        col = 0; row = row + 1;
        
        for (int i = 0; i < detailList.size(); i ++)
        {
            DnDetailHolder detail = detailList.get(i);
            
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(i + 1), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getBuyerItemCode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getBarcode()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getOrderUom()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getItemDesc()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getUnitCost())), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getDisputePrice())), detail.isDisputePriceChanged() ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getPriceStatus().name()), DnPriceStatus.REJECTED.equals(detail.getPriceStatus()) ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getPriceStatusActionRemarks()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getConfirmPrice())), detail.isConfirmPriceChanged() ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getDebitQty())), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getDisputeQty())), detail.isDisputeQtyChanged() ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getQtyStatus().name()), DnQtyStatus.REJECTED.equals(detail.getQtyStatus()) ? shf1_red : shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(detail.getQtyStatusActionRemarks()), shf1));
            col = col + 1;
            ws.addCell(new Label(col, row, StringUtil.getInstance()
                    .convertObjectToString(MatchingReportParameter.convertBigDecimalToDouble(detail.getConfirmQty())), detail.isConfirmQtyChanged() ? shf1_red : shf1));
            
            row = row + 1;
            
            ws.addCell(new Label(col, row, "", shf2));
            ws.mergeCells(0, row, col, row);
            
            row = row + 1;
            
            col = 0;
        }
        
        return row;
    }
    
    
    private void initFlattenedTitle(WritableSheet ws) throws Exception
    {
        int row = 0;
        int col = 0;
        
        ws.addCell(new Label(col, row, "Buyer Code"));
        col = col +1;
        ws.addCell(new Label(col, row, "Buyer Name"));
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
        ws.addCell(new Label(col, row, "Buyer Status"));
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
            
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getBuyerCode()));
            col = col +1;
            ws.addCell(new Label(col, i + 1, report.getDnHeader().getBuyerName()));
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
                report.getDnHeader().getBuyerStatus().name())));
        }
        
        ws.setColumnView(0, 14);
        ws.setColumnView(1, 30);
        ws.setColumnView(2, 13);
        ws.setColumnView(3, 12);
        ws.setColumnView(4, 13);
        ws.setColumnView(5, 12);
        ws.setColumnView(6, 13);
        ws.setColumnView(7, 12);
        ws.setColumnView(8, 14);
    }
    
    
    private Map<String, List<DnHolder>> groupDnByBuyer(List<DnHolder> dnList)
    {
        if (dnList == null || dnList.isEmpty())
        {
            return null;
        }
        
        Map<String, List<DnHolder>> map = new HashMap<String, List<DnHolder>>();
        
        for (DnHolder dn : dnList)
        {
            String buyerCode = dn.getDnHeader().getBuyerCode();
            
            if (map.containsKey(buyerCode))
            {
                map.get(buyerCode).add(dn);
            }
            else
            {
                List<DnHolder> dns = new ArrayList<DnHolder>();
                dns.add(dn);
                map.put(buyerCode, dns);
            }
        }
        
        return map;
    }
}
