package com.pracbiz.b2bportal.core.report.excel;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class StandardMatchingReport
{
    @Autowired private transient BuyerStoreService buyerStoreService;
    private static final String LABEL_INV = "INV";
    
    public byte[] exportExcel(List<MatchingReportParameter> reports,
            Map<String, List<BusinessRuleHolder>> businessRuleMap,
            Map<String, BigDecimal> toleranceMap, boolean isBuyer) throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] output = null;
        
        try
        {
            WritableWorkbook wwb = Workbook.createWorkbook(out);

            Map<String, List<MatchingReportParameter>> reportMapByBuyer = groupReportsByBuyer(reports);
            if (reportMapByBuyer == null)
            {
                return null;
            }
            int sheetIndex = 0;
            WritableSheet summary = wwb.createSheet("Summary Report", sheetIndex);
            sheetIndex++;
            createSummaryFields(summary);
            WritableSheet flattened = wwb.createSheet("Flattened Summary Report", sheetIndex);
            initFlattenedTitle(flattened);
            int flattenedRow = 1;
            sheetIndex++;
            int rowIndex = 3;
            for (Entry<String, List<MatchingReportParameter>> entryByBuyer : reportMapByBuyer
                    .entrySet())
            {
                String buyerCode = entryByBuyer.getKey();
                //Map<String, Boolean> busniessRule = new HashMap<String, Boolean>();

                List<MatchingReportParameter> reportByBuyer = entryByBuyer
                        .getValue();
                rowIndex = initSummaryTitle(summary, /*busniessRule, */rowIndex,
                        buyerCode, reportByBuyer.get(0).getBuyerName());
                Map<String, WritableSheet> hrefMap = new HashMap<String, WritableSheet>();
                
                Map<String, List<MatchingReportParameter>> reportMapBySupplier = groupReportsBySupplier(reportByBuyer);
                for (Entry<String, List<MatchingReportParameter>> entryBySupplier : reportMapBySupplier
                        .entrySet())
                {
                    String supplierCode = entryBySupplier.getKey();
                    List<MatchingReportParameter> suppList = entryBySupplier
                            .getValue();
                    WritableSheet detail = wwb.createSheet(buyerCode + "-"
                            + supplierCode, sheetIndex);
                    
                    hrefMap.put(supplierCode, detail);
                    sheetIndex++;
                    int dRow = 6;
                    initDetailFields(detail, suppList.get(0));
                    Map<String, List<MatchingReportParameter>> reportMapByPo = groupReportsByPo(suppList);
                    for (Entry<String, List<MatchingReportParameter>> poEntry : reportMapByPo
                            .entrySet())
                    {
                        List<MatchingReportParameter> list = poEntry.getValue();
                        dRow = initDetailPoFields(detail, list, dRow, isBuyer);
                        dRow = dRow + 1;
                        
                        for (MatchingReportParameter report : list)
                        {
                            dRow = appendDetailTitle(detail, dRow, report,
                                    /*busniessRule, */toleranceMap.get(buyerCode), isBuyer);
                            dRow = dRow + 2;
                        }
                        dRow = dRow + 2;
                    }
                }
                
              //sort by supplier code, po no and store code.
                Collections.sort(reportByBuyer, new Comparator<MatchingReportParameter>()
                {
                    @Override
                    public int compare(MatchingReportParameter m1, MatchingReportParameter m2)
                    {
                        return m1.getStoreCode().compareTo(m2.getStoreCode());
                    }
                });
                
                Collections.sort(reportByBuyer, new Comparator<MatchingReportParameter>()
                {
                    @Override
                    public int compare(MatchingReportParameter m1, MatchingReportParameter m2)
                    {
                        return m1.getPoNo().compareTo(m2.getPoNo());
                    }
                });
                
                Collections.sort(reportByBuyer, new Comparator<MatchingReportParameter>()
                {
                    @Override
                    public int compare(MatchingReportParameter m1, MatchingReportParameter m2)
                    {
                        return m1.getSupplierCode().compareTo(m2.getSupplierCode());
                    }
                });
                
                for (MatchingReportParameter report : reportByBuyer)
                {
                    appendSummaryItem(summary, rowIndex - 1, report,
                        /*busniessRule, */hrefMap.get(report.getSupplierCode()), isBuyer);
                    initFlattenedItems(flattened, flattenedRow, report, isBuyer);
                    rowIndex = rowIndex + 3;
                    flattenedRow++;
                }
                
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


    private Map<String, List<MatchingReportParameter>> groupReportsByBuyer(
            List<MatchingReportParameter> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return null;
        }
        Map<String, List<MatchingReportParameter>> result = new HashMap<String, List<MatchingReportParameter>>();
        for (MatchingReportParameter report : reports)
        {
            if (result.containsKey(report.getBuyerCode()))
            {
                result.get(report.getBuyerCode()).add(report);
            }
            else
            {
                List<MatchingReportParameter> list = new ArrayList<MatchingReportParameter>();
                list.add(report);
                result.put(report.getBuyerCode(), list);
            }
        }
        return result;
    }


    private Map<String, List<MatchingReportParameter>> groupReportsBySupplier(
            List<MatchingReportParameter> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return null;
        }
        Map<String, List<MatchingReportParameter>> result = new HashMap<String, List<MatchingReportParameter>>();
        for (MatchingReportParameter report : reports)
        {
            if (result.containsKey(report.getSupplierCode()))
            {
                result.get(report.getSupplierCode()).add(report);
            }
            else
            {
                List<MatchingReportParameter> list = new ArrayList<MatchingReportParameter>();
                list.add(report);
                result.put(report.getSupplierCode(), list);
            }
        }
        return result;
    }


    private Map<String, List<MatchingReportParameter>> groupReportsByPo(
            List<MatchingReportParameter> reports) throws Exception
    {
        if (reports == null || reports.isEmpty())
        {
            return null;
        }
        Map<String, List<MatchingReportParameter>> result = new HashMap<String, List<MatchingReportParameter>>();
        for (MatchingReportParameter report : reports)
        {
            if (result.containsKey(report.getPoNo()))
            {
                result.get(report.getPoNo()).add(report);
            }
            else
            {
                List<MatchingReportParameter> list = new ArrayList<MatchingReportParameter>();
                list.add(report);
                result.put(report.getPoNo(), list);
            }
        }
        return result;
    }


    // *****************************************************
    // private methods
    // *****************************************************

    private void createSummaryFields(WritableSheet ws) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        summaryFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        ws.addCell(new Label(0, 0,
                "PO - Invoice - GRN - DN Matching Report - Summary",
                titleFormat));
        ws.mergeCells(0, 0, 7, 0);
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(1, 2, 3, 2);
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
    }
    
    private void initFlattenedItems(WritableSheet ws, int row, 
        MatchingReportParameter report, boolean isBuyer) throws Exception
    {
        int col = 0;
        
        ws.addCell(new Label(col, row, report.getPoNo()));
        col = col +1;
        ws.addCell(new Label(col, row, report.getPoSubType2()));
        col = col +1;
        ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(
            report.getPoDate(), DateUtil.DEFAULT_DATE_FORMAT)));
        col = col +1;
        ws.addCell(new Label(col, row, report.getStoreCode()));
        col = col +1;
        
        String storeName = "";
        if (report.getStoreName() == null)
        {
            BuyerStoreHolder buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(report.getBuyerCode(), report.getStoreCode());
            if (null != buyerStore && null != buyerStore.getStoreName())
            {
                storeName = buyerStore.getStoreName();
            }
        }
        else
        {
            storeName = report.getStoreName();
        }
        ws.addCell(new Label(col, row, storeName));
        col = col +1;
        if (isBuyer)
        {
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(report.getPoAmt())));
        }
        else
        {
            ws.addCell(new Label(col, row, ""));
        }
        col = col +1;
        ws.addCell(new Label(col, row, MatchingReportParameter.getGrnNos(report.getGrnNos())));
        col = col +1;
        ws.addCell(new Label(col, row, MatchingReportParameter.getGrnDates(report.getGrnDates())));
        col = col +1;
        
        ws.addCell(new Label(col, row, ""));
        col = col +1;
        ws.addCell(new Label(col, row, report.getInvNo()));
        col = col +1;
        ws.addCell(new Label(col, row, DateUtil.getInstance().convertDateToString(
            report.getInvDate(), DateUtil.DEFAULT_DATE_FORMAT)));
        col = col +1;
        if (isBuyer)
        {
            ws.addCell(new Label(col, row, StringUtil.getInstance().convertObjectToString(report.getInvAmt())));
        }
        else
        {
            ws.addCell(new Label(col, row, ""));
        }
        col = col +1;
        ws.addCell(new Label(col, row, report.getSupplierCode()));
        col = col +1;
        ws.addCell(new Label(col, row, report.getSupplierName()));
        col = col +1;
        ws.addCell(new Label(col, row, report.getMatchingResult()
            .getMatchingStatus().equals(PoInvGrnDnMatchingStatus.UNMATCHED) 
            ? "PRICE&QTY_UNMATCHED" 
            : StringUtil.getInstance().convertObjectToString(report.getMatchingResult().getMatchingStatus())));
        
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
        ws.setColumnView(11, 10);
        ws.setColumnView(12, 14);
        ws.setColumnView(13, 40);
        ws.setColumnView(14, 23);
    }
    
    private void initDetailFields(WritableSheet ws,
            MatchingReportParameter report) throws Exception
    {
        WritableCellFormat titleFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD));
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        summaryFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        ws.addCell(new Label(0, 0,
                "PO - Invoice - GRN - DN Matching Report - Details",
                titleFormat));
        ws.mergeCells(0, 0, 6, 0);
        ws.addCell(new Label(0, 2, "Printed:", summaryFormat));
        ws.mergeCells(0, 2, 1, 2);
        ws.addCell(new Label(2, 2, DateUtil.getInstance().convertDateToString(
                new Date(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS), summaryFormat));
        ws.mergeCells(2, 2, 4, 2);
        ws.addCell(new Label(0, 3, "Buyer:", summaryFormat));
        ws.mergeCells(0, 3, 1, 3);
        ws.addCell(new Label(2, 3, StringUtil.getInstance()
                .convertObjectToString(report.getBuyerName() + "("
                        + report.getBuyerCode() + ")"), summaryFormat));
        ws.mergeCells(2, 3, 4, 3);
        ws.addCell(new Label(0, 4, "Supplier:", summaryFormat));
        ws.mergeCells(0, 4, 1, 4);
        ws.addCell(new Label(2, 4, StringUtil.getInstance()
                .convertObjectToString(report.getSupplierName() + "("
                        + report.getSupplierCode() + ")"), summaryFormat));
        ws.mergeCells(2, 4, 4, 4);
    }


    private int initSummaryTitle(WritableSheet ws,
            /*Map<String, Boolean> busniessRule, */int rowIndex, String buyerName,
            String buyerCode) throws Exception
    {
        int col = 0;
        int row = rowIndex;
        WritableCellFormat shf = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf.setAlignment(Alignment.CENTRE);
        shf.setBackground(Colour.GRAY_50);
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12));
        summaryFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        ws.addCell(new Label(0, row, "Buyer:", summaryFormat));
        ws.addCell(new Label(1, row, StringUtil.getInstance()
                .convertObjectToString(buyerName + "(" + buyerCode + ")"),
                summaryFormat));
        ws.mergeCells(1, row, 3, row);
        row = row + 2;
        ws.addCell(new Label(col, row, "Supplier Code", shf));
        ws.mergeCells(col, row, col + 1, row + 1);
        ws.addCell(new Label(col, row + 2, "PO No. (PO Date)", shf));
        ws.mergeCells(col, row + 2, col + 1, row + 2);
        col = col + 2;
        ws.addCell(new Label(col, row, "Store Code", shf));
        ws.mergeCells(col, row, col + 1, row + 1);
        ws.addCell(new Label(col, row + 2, "Store PO Amt", shf));
        ws.mergeCells(col, row + 2, col + 1, row + 2);
        col = col + 2;
        ws.addCell(new Label(col, row, "INV No. (INV Date)", shf));
        ws.mergeCells(col, row, col + 2, row + 1);
        ws.addCell(new Label(col, row + 2, "INV Amt/INV Amt w GST", shf));
        ws.mergeCells(col, row + 2, col + 2, row + 2);
        col = col + 3;
        ws.addCell(new Label(col, row, "GRN No.(GRN Date)", shf));
        ws.mergeCells(col, row, col + 2, row + 1);
        ws.addCell(new Label(col, row + 2, "GRN Amt", shf));
        ws.mergeCells(col, row + 2, col + 2, row + 2);
        col = col + 3;
        ws.addCell(new Label(col, row, "DN No. (DN Date)", shf));
        ws.mergeCells(col, row, col + 2, row + 1);
        ws.addCell(new Label(col, row + 2, "DN Amt/DN Amt w GST", shf));
        ws.mergeCells(col, row + 2, col + 2, row + 2);
        col = col + 3;
        int begin = col;
        ws.addCell(new Label(col, row + 1, "PO-INV(unitPrice)", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 2);
        col = col + 3;
        ws.addCell(new Label(col, row + 1, "GRN-INV(qty)", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 2);
        col = col + 3;
        ws.addCell(new Label(col, row + 1, "PO-DN-INV(amount)", shf));
        ws.mergeCells(col, row + 1, col + 2, row + 2);
        col = col + 3;
        ws.addCell(new Label(begin, row, "Matching Result", shf));
        ws.mergeCells(begin, row, col + 3, row);
        ws.addCell(new Label(col, row + 1, "Matching Status", shf));
        ws.mergeCells(col, row + 1, col + 3, row + 2);
//        ws.addCell(new Label(col + 4, row, "Approve Status", shf));
//        ws.mergeCells(col + 4, row, col + 5, row + 2);
        return row + 4;
    }


    private void appendSummaryItem(WritableSheet ws, int i,
            MatchingReportParameter report, /*Map<String, Boolean> busniessRule,*/
            WritableSheet hrefSheet, boolean isBuyer) throws Exception
    {
        int col = 0;
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        shf1.setBackground(Colour.GRAY_25);
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        WritableCellFormat shf3 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf3.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf3.setAlignment(Alignment.CENTRE);
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getSupplierCode()), shf1));
        ws.addHyperlink(new WritableHyperlink(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getSupplierCode()), hrefSheet, 0,
                0));
        ws.mergeCells(col, i, col + 1, i);
        ws.addCell(new Label(col, i + 1,
                StringUtil.getInstance().convertObjectToString(report.getPoNo())
                        + "("
                        + DateUtil.getInstance().convertDateToString(
                                report.getPoDate(),
                                DateUtil.DEFAULT_DATE_FORMAT) + ")", shf2));
        ws.mergeCells(col, i + 1, col + 1, i + 1);
        col = col + 2;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getStoreCode()), shf1));
        ws.mergeCells(col, i, col + 1, i);
        if (isBuyer)
        {
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(report
                    .getPoAmt())), shf2));
        }
        else
        {
            ws.addCell(new Label(col, i + 1, "", shf2));
        }
        ws.mergeCells(col, i + 1, col + 1, i + 1);
        col = col + 2;
        ws.addCell(new Label(col, i, MatchingReportParameter.getNoAndDate(
                report.getInvNo(), report.getInvDate()), shf1));
        ws.mergeCells(col, i, col + 2, i);
        if (isBuyer)
        {
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(report
                    .getInvAmt()))
                    + "/"
                    + StringUtil.getInstance()
                    .convertObjectToString(convertBigDecimalToDouble(report
                        .getInvAmtWithVat())), shf2));
        }
        else
        {
            ws.addCell(new Label(col, i + 1, "", shf2));
        }
        ws.mergeCells(col, i + 1, col + 2, i + 1);
        col = col + 3;
        ws.addCell(new Label(col, i, MatchingReportParameter.getGrnNosAndGrnDates(
                report.getGrnNos(), report.getGrnDates()), shf1));
        ws.mergeCells(col, i, col + 2, i);
        
        ws.addCell(new Label(col, i + 1, "", shf2));
        ws.mergeCells(col, i + 1, col + 2, i + 1);
        col = col + 3;
        ws.addCell(new Label(col, i, MatchingReportParameter.getNoAndDate(
                report.getDnNo(), report.getDnDate()), shf1));
        ws.mergeCells(col, i, col + 2, i);
        if (isBuyer)
        {
            ws.addCell(new Label(col, i + 1, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(report
                    .getDnAmt()))
                    + "/"
                    + StringUtil.getInstance()
                    .convertObjectToString(convertBigDecimalToDouble(report
                        .getDnAmtWithVat())), shf2));
        }
        else
        {
            ws.addCell(new Label(col, i + 1, "", shf2));
        }
        ws.mergeCells(col, i + 1, col + 2, i + 1);
        col = col + 3;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getMatchingResult()
                    .getCountOfUnmatchedUnitPrice()), StringUtil.getInstance()
                    .convertObjectToString(
                        report.getMatchingResult()
                        .getCountOfUnmatchedUnitPrice()).trim()
                        .equals("0") ? shf2 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "", StringUtil.getInstance()
                    .convertObjectToString(
                        report.getMatchingResult()
                        .getCountOfUnmatchedUnitPrice()).trim()
                        .equals("0") ? shf2 : shf3));
        }
        ws.mergeCells(col, i, col + 2, i + 1);
        col = col + 3;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getMatchingResult()
                        .getCountOfUnmatchedQty()),
                        StringUtil.getInstance()
                        .convertObjectToString(
                                report.getMatchingResult()
                                .getCountOfUnmatchedQty()).trim()
                                .equals("0") ? shf2 : shf3));
        ws.mergeCells(col, i, col + 2, i + 1);
        col = col + 3;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(report.getMatchingResult()
                    .getCountOfUnmatchedAmt()),
                    StringUtil.getInstance()
                    .convertObjectToString(
                        report.getMatchingResult()
                        .getCountOfUnmatchedAmt()).trim()
                        .equals("0") ? shf2 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "",
                    StringUtil.getInstance()
                    .convertObjectToString(
                        report.getMatchingResult()
                        .getCountOfUnmatchedAmt()).trim()
                        .equals("0") ? shf2 : shf3));
        }
        ws.mergeCells(col, i, col + 2, i + 1);
        col = col + 3;
        ws.addCell(new Label(col, i, report.getMatchingResult()
                        .getMatchingStatus().equals(PoInvGrnDnMatchingStatus.UNMATCHED) 
                        ? "PRICE&QTY_UNMATCHED" 
                        : StringUtil.getInstance().convertObjectToString(report.getMatchingResult().getMatchingStatus()),
                        (StringUtil.getInstance()
                        .convertObjectToString(
                                report.getMatchingResult()
                                        .getMatchingStatus().name())
                        .trim()
                        .equalsIgnoreCase(
                                PoInvGrnDnMatchingStatus.MATCHED.name()))
                        ||
                        (StringUtil.getInstance()
                        .convertObjectToString(
                                report.getMatchingResult()
                                        .getMatchingStatus().name())
                        .trim()
                        .equalsIgnoreCase(
                                PoInvGrnDnMatchingStatus.MATCHED_BY_DN.name()))
                        ? shf2
                        : shf3));
        ws.mergeCells(col, i, col + 3, i + 1);
//        ws.addCell(new Label(col + 4, i, report.getActionStatus().name(), shf2));
//        ws.mergeCells(col + 4, i, col + 5, i + 1);
        ws.addCell(new Label(0, i + 2, ""));
        ws.mergeCells(0, i + 2, col + 3, i + 2);
    }


    private int initDetailPoFields(WritableSheet ws,
            List<MatchingReportParameter> reports, int rowIndex, boolean isBuyer) throws Exception
    {
        WritableCellFormat summaryFormat = new WritableCellFormat(
                new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD));
        summaryFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
        int row = rowIndex;
        ws.addCell(new Label(0, row, "PO No:", summaryFormat));
        ws.mergeCells(0, row, 1, row);
        ws.addCell(new Label(2, row, StringUtil.getInstance()
                .convertObjectToString(reports.get(0).getPoNo()), summaryFormat));
        ws.mergeCells(2, row, 4, row);
        ws.addCell(new Label(0, row + 1, "PO Type:", summaryFormat));
        ws.mergeCells(0, row + 1, 1, row + 1);
        ws.addCell(new Label(2, row + 1, StringUtil.getInstance()
                .convertObjectToString(reports.get(0).getPoType()), summaryFormat));
        ws.mergeCells(2, row + 1, 4, row + 1);
        ws.addCell(new Label(0, row + 2, "PO Date:", summaryFormat));
        ws.mergeCells(0, row + 2, 1, row + 2);
        ws.addCell(new Label(2, row + 2, DateUtil.getInstance()
                .convertDateToString(reports.get(0).getPoDate()), summaryFormat));
        ws.mergeCells(2, row + 2, 4, row + 2);
        BigDecimal poAmt = BigDecimal.ZERO;
        for (MatchingReportParameter report : reports)
        {
            poAmt = poAmt.add(report.getPoAmt());
        }
        ws.addCell(new Label(0, row + 3, "PO Total:", summaryFormat));
        ws.mergeCells(0, row + 3, 1, row + 3);
        if (isBuyer)
        {
            ws.addCell(new Label(2, row + 3, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(poAmt)), summaryFormat));
        }
        else
        {
            ws.addCell(new Label(2, row + 3, "", summaryFormat));
        }
        ws.mergeCells(2, row + 3, 4, row + 3);
        return row + 4;
    }


    private int appendDetailTitle(WritableSheet ws, int row,
            MatchingReportParameter summary, /*Map<String, Boolean> busniessRule,*/
            BigDecimal tolerance, boolean isBuyer) throws Exception
    {
        int col = 0;
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
        WritableCellFormat shf3 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf3.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf3.setAlignment(Alignment.CENTRE);
        shf3.setBackground(Colour.LIGHT_GREEN);//
        WritableCellFormat shf4 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf4.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf4.setAlignment(Alignment.CENTRE);
        shf4.setBackground(Colour.IVORY);//
        WritableCellFormat shf5 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.BOLD));
        shf5.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf5.setAlignment(Alignment.CENTRE);
        shf5.setBackground(Colour.LIGHT_TURQUOISE2);//
        WritableCellFormat shf6 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf6.setAlignment(Alignment.CENTRE);
        WritableCellFormat shf7 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf7.setAlignment(Alignment.CENTRE);
        WritableCellFormat shf8 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf8.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf8.setAlignment(Alignment.RIGHT);
        shf8.setIndentation(1);
        WritableCellFormat shf9 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf9.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf9.setAlignment(Alignment.LEFT);
        shf9.setIndentation(1);
        ws.addCell(new Label(col, row, "Store " + summary.getStoreCode(), shf1));
        ws.mergeCells(col, row, col + 9, row);
        ws.addCell(new Label(col, row + 1, "Seq", shf2));
        ws.addCell(new Label(col + 1, row + 1, "Item Code", shf2));
        ws.mergeCells(col + 1, row + 1, col + 2, row + 1);
        ws.addCell(new Label(col + 3, row + 1, "Barcode", shf2));
        ws.mergeCells(col + 3, row + 1, col + 4, row + 1);
        ws.addCell(new Label(col + 5, row + 1, "UOM", shf2));
        ws.addCell(new Label(col + 6, row + 1, "Description", shf2));
        ws.mergeCells(col + 6, row + 1, col + 9, row + 1);
        col = col + 10;
        int begin = 0;
        ws.addCell(new Label(col, row, "Unit Price (PO-INV)", shf3));
        ws.mergeCells(col, row, col + 1, row);
        ws.addCell(new Label(col, row + 1, "PO", shf2));
        ws.addCell(new Label(col + 1, row + 1, LABEL_INV, shf2));
        col = col + 2;
        ws.addCell(new Label(col, row, "Qty (GRN-INV)", shf4));
        ws.mergeCells(col, row, col + 2, row);
        ws.addCell(new Label(col, row + 1, "PO", shf2));
        ws.addCell(new Label(col + 1, row + 1, "GRN", shf2));
        ws.addCell(new Label(col + 2, row + 1, LABEL_INV, shf2));
        col = col + 3;
        begin = col;
        ws.addCell(new Label(col, row + 1, "PO", shf2));
        ws.addCell(new Label(col + 1, row + 1, "DN", shf2));
        ws.addCell(new Label(col + 2, row + 1, LABEL_INV, shf2));
        ws.addCell(new Label(col + 3, row + 1, "EXP-INV", shf2));
        ws.addCell(new Label(col, row, "Amount (PO-DN-INV)", shf5));
        ws.mergeCells(col, row, col + 3, row);
        col = col + 4;

        int j = row + 2;
        int result = row + 2;
        List<PoInvGrnDnMatchingDetailExHolder> details = summary.getMatchingDetails();
        if (details != null && !details.isEmpty())
        {
            int sequence = 1;
            for (PoInvGrnDnMatchingDetailHolder d : details)
            {
                appendDetailItem(ws, j, sequence, d, /*busniessRule, */tolerance, isBuyer);
                sequence++;
                j++;
            }
            result += details.size();
        }
        ws.addCell(new Label(begin - 2, result, "Net total amount", shf9));
        ws.mergeCells(begin - 2, result, begin, result);
        ws.addCell(new Label(begin - 2, result + 1, "GST", shf9));
        ws.mergeCells(begin - 2, result + 1, begin, result + 1);
        ws.addCell(new Label(begin - 2, result + 2,
                "Total amount with GST", shf9));
        ws.mergeCells(begin - 2, result + 2, begin, result + 2);
        if (isBuyer)
        {
            ws.addCell(new Label(begin, result, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getPoAmt())), shf8));
        }
        else
        {
            ws.addCell(new Label(begin, result, "", shf8));
        }
        begin++;
        if (isBuyer)
        {
            ws.addCell(new Label(begin, result, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getDnAmt())), shf8));
            ws.addCell(new Label(begin, result + 1, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getDnVat())), shf8));
            ws.addCell(new Label(begin, result + 2, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getDnAmtWithVat())), shf8));
        }
        else
        {
            ws.addCell(new Label(begin, result, "", shf8));
            ws.addCell(new Label(begin, result + 1, "", shf8));
            ws.addCell(new Label(begin, result + 2, "", shf8));
        }
        begin++;
        if (isBuyer)
        {
            ws.addCell(new Label(begin, result, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getInvAmt())), shf8));
            ws.addCell(new Label(begin, result + 1, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getInvVat())), shf8));
            ws.addCell(new Label(begin, result + 2, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getInvAmtWithVat())), shf8));
        }
        else
        {
            ws.addCell(new Label(begin, result, "", shf8));
            ws.addCell(new Label(begin, result + 1, "", shf8));
            ws.addCell(new Label(begin, result + 2, "", shf8));
        }
        begin++;
        if (isBuyer)
        {
            ws.addCell(new Label(begin, result, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(summary
                    .getExpInvAmt())), shf8));
        }
        else
        {
            ws.addCell(new Label(begin, result, "", shf8));
        }
        ws.addCell(new Label(begin, result + 1, "", shf8));
        ws.addCell(new Label(begin, result + 2, "", shf8));

        return result + 2;
    }


    private void appendDetailItem(WritableSheet ws, int i, int sequence,
            PoInvGrnDnMatchingDetailHolder detail, /*Map<String, Boolean> busniessRule,*/
            BigDecimal tolerance, boolean isBuyer) throws Exception
    {
        int col = 0;
        WritableCellFormat shf1 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf1.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf1.setAlignment(Alignment.CENTRE);
        WritableCellFormat shf2 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf2.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf2.setAlignment(Alignment.CENTRE);
        WritableCellFormat shf3 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.RED,
                ScriptStyle.NORMAL_SCRIPT));
        shf3.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf3.setAlignment(Alignment.RIGHT);
        shf3.setIndentation(1);
        WritableCellFormat shf4 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf4.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf4.setAlignment(Alignment.RIGHT);
        shf4.setIndentation(1);
        WritableCellFormat shf5 = new WritableCellFormat(new WritableFont(
                WritableFont.TIMES, 12));
        shf5.setBorder(Border.ALL, BorderLineStyle.THIN);
        shf5.setAlignment(Alignment.LEFT);
        shf5.setIndentation(1);
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(sequence), shf2));
        col++;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getBuyerItemCode()), shf5));
        ws.mergeCells(col, i, col + 1, i);
        col = col + 2;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getBarcode()), shf5));
        ws.mergeCells(col, i, col + 1, i);
        col = col + 2;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getUom()), shf2));
        col++;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getItemDesc()), shf5));
        ws.mergeCells(col, i, col + 3, i);
        col = col + 4;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getPoPrice())), calculateUnitPriceEqual(
                        detail.getPoPrice(), detail.getInvPrice()) ? shf4 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "", calculateUnitPriceEqual(
                        detail.getPoPrice(), detail.getInvPrice()) ? shf4 : shf3));
        }
        col++;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getInvPrice())), calculateUnitPriceEqual(
                        detail.getPoPrice(), detail.getInvPrice()) ? shf4 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "", calculateUnitPriceEqual(
                        detail.getPoPrice(), detail.getInvPrice()) ? shf4 : shf3));
        }
        col++;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getPoQty()),
                calculateQtyEqual(detail.getPoQty(), detail.getGrnQty(),
                        detail.getInvQty()) ? shf2 : shf1));
        col++;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getGrnQty()),
                calculateQtyEqual(detail.getPoQty(), detail.getGrnQty(),
                        detail.getInvQty()) ? shf2 : shf1));
        col++;
        ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(detail.getInvQty()),
                calculateQtyEqual(detail.getPoQty(), detail.getGrnQty(),
                        detail.getInvQty()) ? shf2 : shf1));
        col++;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getPoAmt())),
                    calculateAmtEqual(detail.getExpInvAmt(), detail.getInvAmt(),
                        detail.getDnAmt(), true, tolerance) ? shf4 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "",
                    calculateAmtEqual(detail.getExpInvAmt(), detail.getInvAmt(),
                        detail.getDnAmt(), true, tolerance) ? shf4 : shf3));
        }
        col++;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getDnAmt())),
                    calculateAmtEqual(detail.getExpInvAmt(), detail.getInvAmt(),
                        detail.getDnAmt(), true, tolerance) ? shf4 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "",
                    calculateAmtEqual(detail.getExpInvAmt(), detail.getInvAmt(),
                        detail.getDnAmt(), true, tolerance) ? shf4 : shf3));
        }
        col++;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getInvAmt())), calculateAmtEqual(
                        detail.getExpInvAmt(), detail.getInvAmt(), detail.getDnAmt(),
                        true, tolerance) ? shf4 : shf3));
        }
        else
        {
            ws.addCell(new Label(col, i, "", calculateAmtEqual(
                        detail.getExpInvAmt(), detail.getInvAmt(), detail.getDnAmt(),
                        true, tolerance) ? shf4 : shf3));
        }
        col++;
        if (isBuyer)
        {
            ws.addCell(new Label(col, i, StringUtil.getInstance()
                .convertObjectToString(convertBigDecimalToDouble(detail
                    .getExpInvAmt())), shf4));
        }
        else
        {
            ws.addCell(new Label(col, i, "", shf4));
        }
    }


    private String convertBigDecimalToDouble(BigDecimal input)
    {
        if (input == null)
        {
            return null;
        }
        return new DecimalFormat("#,##0.00").format(input);
    }


    private boolean calculateUnitPriceEqual(BigDecimal poPrice,
            BigDecimal invPrice) throws Exception
    {
        BigDecimal pPrice = BigDecimal.ZERO;
        BigDecimal iPrice = BigDecimal.ZERO;
        if (poPrice != null)
        {
            pPrice = poPrice;
        }
        if (invPrice != null)
        {
            iPrice = invPrice;
        }
        return pPrice.compareTo(iPrice) == 0;
    }


    private boolean calculateQtyEqual(BigDecimal poQty,
            BigDecimal grnQty, BigDecimal invQty) throws Exception
    {
        BigDecimal pQty = BigDecimal.ZERO;
        BigDecimal gQty = BigDecimal.ZERO;
        BigDecimal iQty = BigDecimal.ZERO;
        if (poQty != null)
        {
            pQty = poQty;
        }
        if (grnQty != null)
        {
            gQty = grnQty;
        }
        if (invQty != null)
        {
            iQty = invQty;
        }
        return gQty.compareTo(iQty) == 0 && pQty.compareTo(iQty) == 0 && gQty.compareTo(pQty) == 0;
    }


    private boolean calculateAmtEqual(BigDecimal expInvAmt,
            BigDecimal invAmt, BigDecimal dnAmt, boolean flag,
            BigDecimal tolerance) throws Exception
    {
        BigDecimal eIAmount = BigDecimal.ZERO;
        BigDecimal invAmount = BigDecimal.ZERO;
        BigDecimal dnAmount = BigDecimal.ZERO;
        BigDecimal tolAmount = BigDecimal.ZERO;
        if (expInvAmt != null)
        {
            eIAmount = expInvAmt;
        }
        if (invAmt != null)
        {
            invAmount = invAmt;
        }
        if (dnAmt != null)
        {
            dnAmount = dnAmt;
        }
        if (tolerance != null)
        {
            tolAmount = tolerance;
        }
        if (flag)
        {
            return eIAmount.add(dnAmount).subtract(invAmount).abs()
                    .compareTo(tolAmount) <= 0;
        }
        return eIAmount.subtract(invAmount).abs().compareTo(tolAmount) <= 0;
    }

}
