package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.PageOrientation;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailService;
import com.pracbiz.b2bportal.core.service.SalesDateService;
import com.pracbiz.b2bportal.core.service.SalesDetailService;
import com.pracbiz.b2bportal.core.service.SalesHeaderService;
import com.pracbiz.b2bportal.core.service.SalesLocationService;
import com.pracbiz.b2bportal.core.service.SalesService;

public class SalesServiceImpl extends DBActionServiceDefaultImpl<SalesHolder> implements SalesService
{
    @Autowired transient private SalesDateLocationDetailService salesDateLocationDetailService;
    @Autowired transient private SalesDateService salesDateService;
    @Autowired transient private BuyerStoreService buyerStoreService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private SalesDetailService salesDetailService;
    @Autowired transient private SalesHeaderService salesHeaderService;
    @Autowired transient private SalesLocationService salesLocationService;
    
    @Override
    public void insert(SalesHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKeySelective(SalesHolder oldObj_,
        SalesHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKey(SalesHolder oldObj_, SalesHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(SalesHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public SalesHolder selectSalesByKey(BigDecimal salesOid) throws Exception
    {
        if (salesOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        SalesHeaderHolder header = salesHeaderService.selectSalesHeaderByKey(salesOid);
        List<SalesLocationHolder> locs = salesLocationService.selectSalesLocationByKey(salesOid);
        List<SalesDetailHolder> details = salesDetailService.selectSalesDetailByKey(salesOid);
        List<SalesDateHolder> dates = salesDateService.selectSalesDateByKey(salesOid);
        List<SalesDateLocationDetailHolder> locDetails = salesDateLocationDetailService.selectSalesLocationDetailByKey(salesOid);
        
        SalesHolder sales = new SalesHolder();
        sales.setSalesHeader(header);
        sales.setDetails(details);
        sales.setLocations(locs);
        sales.setSalesDates(dates);
        sales.setSalesDateLocationDetail(locDetails);
        
        return sales;
    }

    @Override
    public byte[] exportExcel(List<BigDecimal> salesOids) throws Exception
    {
        if (salesOids == null || salesOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for (BigDecimal salesOid : salesOids)
        {
            this.initSheetBySalesOid(salesOid, wwb, sheetIndex);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }

    
    private String formatBigDecimal(BigDecimal obj) 
        throws ParseException
    {
        if (obj == null) return null;
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        return df.format(obj.doubleValue());
    }
    
    
    private void initSheetBySalesOid(BigDecimal salesOid, WritableWorkbook wwb, int sheetIndex) throws Exception
    {
        SalesHolder salesHolder = this.selectSalesByKey(salesOid);
        SalesHeaderHolder header = salesHolder.getSalesHeader();
        WritableSheet sheet = wwb.createSheet("sheet" + sheetIndex, sheetIndex);
        sheet.getSettings().setZoomFactor(75);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        List<Map<String, Object>> maps = this.initExcelDataSource(salesHolder);
        
        initSalesSheet(sheet, header, maps);
        
    }
    
    
    public void initSalesSheet(WritableSheet sheet, SalesHeaderHolder header,List<Map<String, Object>> maps)
        throws RowsExceededException, WriteException, Exception
    {
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.RIGHT);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setBackground(Colour.GRAY_25);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setBackground(Colour.GRAY_25);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        format4.setWrap(true);
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format5.setAlignment(Alignment.CENTRE);
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format6 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format6.setAlignment(Alignment.CENTRE);
        
        
        sheet.addCell(new Label(0, 0, "SALES DATA REPORT", format6));
        sheet.mergeCells(0, 0, 13, 0);
        sheet.addCell(new Label(0, 1, "COMPANY:", format1));
        sheet.mergeCells(0, 1, 1, 1);
        sheet.addCell(new Label(2, 1, header.getBuyerCode() == null ? (header.getBuyerName() == null ? "" : header.getBuyerName()) : (header.getBuyerName() == null ? header.getBuyerCode() : (header.getBuyerCode() + "  " + header.getBuyerName())), format1));
        sheet.mergeCells(2, 1, 5, 1);
        sheet.addCell(new Label(12, 1, DateUtil.getInstance().convertDateToString(new Date(), DateUtil.EBXML_DATE_FORMAT_YYYYMMDDHHMMSS), format1));
        sheet.mergeCells(12, 2, 13, 2);
        sheet.addCell(new Label(0, 2, "SUPPLIER:", format1));
        sheet.mergeCells(0, 2, 1, 2);
        sheet.addCell(new Label(2, 2, header.getSupplierCode() == null ? (header.getSupplierName() == null ? "" : header.getSupplierName()) : (header.getSupplierName() == null ? header.getSupplierCode() : (header.getSupplierCode() + "  " + header.getSupplierName())), format1));
        sheet.mergeCells(2, 2, 5, 2);
        sheet.addCell(new Label(0, 3, "SALES DATE:", format1));
        sheet.mergeCells(0, 3, 1, 3);
        sheet.addCell(new Label(2, 3, DateUtil.getInstance().convertDateToString(header.getSalesDate(), DateUtil.EXCEL_DATE_FORMAT_DDMMYY), format1));
        
        sheet.addCell(new Label(0, 4, "SEQ", format4));
        sheet.mergeCells(0, 4, 0, 5);
        sheet.addCell(new Label(1, 4, "STORE NO.", format4));
        sheet.mergeCells(1, 4, 1, 5);
        sheet.addCell(new Label(2, 4, "POS NO.", format4));
        sheet.mergeCells(2, 4, 2, 5);
        sheet.addCell(new Label(3, 4, "RECEIPT NO.", format4));
        sheet.mergeCells(3, 4, 3, 5);
        sheet.addCell(new Label(4, 4, "BUSINESS DATE", format4));
        sheet.mergeCells(4, 4, 4, 5);
        sheet.addCell(new Label(5, 4, "RECEIPT TIME", format4));
        sheet.mergeCells(5, 4, 5, 5);
        sheet.addCell(new Label(6, 4, "UPC EAN", format4));
        sheet.mergeCells(6, 4, 6, 5);
        sheet.addCell(new Label(7, 4, "SKU NO.", format4));
        sheet.mergeCells(7, 4, 7, 5);
        sheet.addCell(new Label(8, 4, "DESCRIPTION", format4));
        sheet.mergeCells(8, 4, 8, 5);
        sheet.addCell(new Label(9, 4, "QTY", format4));
        sheet.mergeCells(9, 4, 9, 5);
        sheet.addCell(new Label(10, 4, "GROSS SALES", format4));
        sheet.mergeCells(10, 4, 10, 5);
        sheet.addCell(new Label(11, 4, "DISC", format4));
        sheet.mergeCells(11, 4, 11, 5);
        sheet.addCell(new Label(12, 4, "NET SALES", format4));
        sheet.mergeCells(12, 4, 12, 5);
        sheet.addCell(new Label(13, 4, "VAT AMT GST", format4));
        sheet.mergeCells(13, 4, 13, 5);
        
        int col = 6;
        int lineSeq = 1;
        
        for (Map<String, Object> map : maps)
        {
            sheet.addCell(new Label(0, col, lineSeq + "", format5));
            sheet.addCell(new Label(1, col, (String)map.get("LOCATION_CODE"), format2));
            sheet.addCell(new Label(2, col, (String)map.get("POS_ID"), format2));
            sheet.addCell(new Label(3, col, (String)map.get("RECEIPT_NO"), format2));
            sheet.addCell(new Label(4, col, DateUtil.getInstance().convertDateToString((Date)map.get("SALES_DATE"), DateUtil.DATE_FORMAT_DDMMYYYY), format2));
            sheet.addCell(new Label(5, col, (String)map.get("RECEIPT_DATE"), format2));
            sheet.addCell(new Label(6, col, (String)map.get("BARCODE"), format2));
            sheet.addCell(new Label(7, col, (String)map.get("BUYER_ITEM_CODE"), format2));
            sheet.addCell(new Label(8, col, (String)map.get("ITEM_DESC"), format2));
            sheet.addCell(new Label(9, col, formatBigDecimal((BigDecimal)map.get("SALES_QTY")), format2));
            sheet.addCell(new Label(10, col, formatBigDecimal((BigDecimal)map.get("SALES_AMOUNT")), format2));
            sheet.addCell(new Label(11, col, formatBigDecimal((BigDecimal)map.get("SALES_DISCOUNT_AMOUNT")), format2));
            sheet.addCell(new Label(12, col, formatBigDecimal((BigDecimal)map.get("SALES_NET_AMOUNT")), format2));
            sheet.addCell(new Label(13, col, formatBigDecimal((BigDecimal)map.get("VAT_AMOUNT")), format2));
            col++;
            lineSeq++;
        }
        
        sheet.addCell(new Label(0, col, "TOTAL:", format3));
        sheet.mergeCells(0, col, 8, col);
    
        sheet.addCell(new Label(9, col, formatBigDecimal(header.getTotalQty()), format3));
        sheet.addCell(new Label(10, col, formatBigDecimal(header.getTotalGrossSalesAmount()), format3));
        sheet.addCell(new Label(11, col, formatBigDecimal(header.getTotalDiscountAmount()), format3));
        sheet.addCell(new Label(12, col, formatBigDecimal(header.getTotalNetSalesAmount()), format3));
        sheet.addCell(new Label(13, col, formatBigDecimal(header.getTotalVatAmount()), format3));
        
        sheet.setColumnView(0, 5);
        sheet.setColumnView(1, 12);
        sheet.setColumnView(2, 13);
        sheet.setColumnView(3, 13);
        sheet.setColumnView(4, 12);
        sheet.setColumnView(5, 12);
        sheet.setColumnView(6, 17);
        sheet.setColumnView(7, 12);
        sheet.setColumnView(8, 25);
        sheet.setColumnView(9, 10);
        sheet.setColumnView(10, 12);
        sheet.setColumnView(11, 12);
        sheet.setColumnView(12, 12);
        sheet.setColumnView(13, 13);
    }

    
    private List<Map<String, Object>> initExcelDataSource(SalesHolder salesHolder)
    {
        List<SalesDetailHolder> salesDetails = salesHolder.getDetails();
        List<SalesDateLocationDetailHolder> locationDetails = salesHolder.getSalesDateLocationDetail();
        List<SalesDateHolder> salesDates = salesHolder.getSalesDates();
        List<SalesLocationHolder> salesLocs = salesHolder.getLocations();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for (SalesDateLocationDetailHolder locDetail : locationDetails)
        {
            map = new HashMap<String, Object>();
            for (SalesDetailHolder salesDetail : salesDetails)
            {
                if (locDetail.getDetailLineSeqNo().equals(salesDetail.getLineSeqNo()))
                {
                    map.putAll(salesDetail.toMapValues());
                    
                    Date receiptDate = salesDetail.getReceiptDate();
                    if (null != receiptDate)
                    {
                        map.put("RECEIPT_DATE", DateUtil.getInstance().convertDateToString(receiptDate, DateUtil.DATE_FORMAT_MMDDYYYYHHMMSS).split(" ")[1]);
                    }
                }
            }
            
            for (SalesDateHolder salesDate : salesDates)
            {
                if (locDetail.getDateLineSeqNo().equals(salesDate.getLineSeqNo()))
                {
                    map.put("SALES_DATE", salesDate.getSalesDate());
                }
            }
            
            for (SalesLocationHolder salesLoc : salesLocs)
            {
                if (locDetail.getLocationLineSeqNo().equals(salesLoc.getLineSeqNo()))
                {
                    map.putAll(salesLoc.toMapValues());
                }
            }
            map.putAll(locDetail.toMapValues());
            
            dataSource.add(map);
        }
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m2.get("BUYER_ITEM_CODE")).compareTo((String)m1.get("BUYER_ITEM_CODE"));
            }
        });
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m2.get("LOCATION_CODE")).compareTo((String)m1.get("LOCATION_CODE"));
            }
        });
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((Date)m1.get("SALES_DATE")).before((Date)m2.get("SALES_DATE")) ? -1 : 1;
            }
        });
    
        return dataSource;
    }
}
