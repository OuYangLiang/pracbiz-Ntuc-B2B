package com.pracbiz.b2bportal.core.report.excel;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
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

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author YinChi
 */
public class DefaultStandardPoExcelReportEngine extends ExcelReportEngine<PoHolder>
    implements CoreCommonConstants
{
    @Autowired transient private BuyerStoreService buyerStoreService;
    
    private static final String DATE_FORMAT_DDMMMYY = "dd-MMM-yy";


    @Override
    protected void generateWorkSheet(WritableWorkbook wwb,
        ReportEngineParameter<PoHolder> parameter, int sheetIndex, int flag)
        throws Exception
    {
        boolean storeFlag = flag == ExcelReportEngine.EXCEL_TYPE_IS_STORE;
        PoHolder poHolder = parameter.getData();
        PoHeaderHolder header = poHolder.getPoHeader();
        List<PoDetailHolder> detailList = poHolder.getDetails();
        List<PoLocationHolder> locationList = poHolder.getLocations();
        List<PoLocationDetailHolder> locationDetailList = poHolder.getLocationDetails();
        WritableSheet sheet = wwb.createSheet(header.getPoNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        if (header.getPoType().equals(PoType.CON))
        {
            initConPoSheet(sheet, header, detailList, storeFlag);
        }
        else
        {
            initSorPoSheet(sheet, header, detailList, locationList, locationDetailList, storeFlag);
        }
    }

    
    //********************************
    //con po export excel
    //********************************
    private void initConPoSheet(WritableSheet sheet, PoHeaderHolder header,
        List<PoDetailHolder> detailList, boolean storeFlag)
        throws RowsExceededException, WriteException, Exception
    {
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.LEFT);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        format4.setWrap(true);
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format5.setAlignment(Alignment.CENTRE);
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        WritableCellFormat format6 = new jxl.write.WritableCellFormat(new jxl.write.NumberFormat("#,##0.00"));  
        format6.setAlignment(Alignment.RIGHT);
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        
        BigDecimalUtil bUtil = BigDecimalUtil.getInstance();
        sheet.addCell(new Label(0, 0, "BUYER NAME", format1));
        sheet.addCell(new Label(1, 0, header.getBuyerName(), format1));
        sheet.addCell(new Label(0, 1, "PURCHASE ORDER", format1));
        sheet.addCell(new Label(1, 1, header.getPoNo(), format1));
        sheet.addCell(new Label(0, 2, "ORDER DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getPoDate(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(0, 3, "DELIVERY DATE FROM", format1));
        sheet.addCell(new Label(1, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDateFrom(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(2, 3, "DELIVERY DATE TO", format1));
        sheet.addCell(new Label(3, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDateTo(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(0, 4, "SPECIAL INSTRUCTIONS", format1));
        sheet.addCell(new Label(1, 4, "", format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "PO TYPE", format1));
        sheet.addCell(new Label(1, 7, header.getPoType().name(), format1));
        sheet.addCell(new Label(0, 8, "TOTAL NUMBER OF ITEMS", format1));
        if (!storeFlag)
        {
            sheet.addCell(new Label(0, 9, "TOTAL BEFORE DISCOUNT AMOUNT ($)", format1));
            sheet.addCell(new Label(1, 9, this.formatBigDecimal(header.getTotalGrossCost()), format1));
        }
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 11, "SEQ", format4));
        sheet.setColumnView(col, 27);
        sheet.addCell(new Label(col+1, 11, "SKU NO./BARCODE", format4));
        sheet.setColumnView(col+1, 30);
        sheet.addCell(new Label(col+2, 11, "DESCRIPTION", format4));
        sheet.setColumnView(col+2, 43);
        sheet.addCell(new Label(col+3, 11, "ARTICLE NO.", format4));
        sheet.setColumnView(col+3, 12);
        sheet.addCell(new Label(col+4, 11, "COLOUR", format4));
        sheet.setColumnView(col+4, 9);
        sheet.addCell(new Label(col+5, 11, "SIZE", format4));
        sheet.setColumnView(col+5, 7);
        sheet.addCell(new Label(col+6, 11, "QTY SOLD", format4));
        sheet.setColumnView(col+6, 7);
        sheet.addCell(new Label(col+7, 11, "UOM", format4));
        sheet.setColumnView(col+7, 7);
        
        if (!storeFlag)
        {
            sheet.addCell(new Label(col+8, 11, "UNIT COST", format4));
            sheet.setColumnView(col+8, 10);
            sheet.addCell(new Label(col+9, 11, "PO COST BEFORE SHARED COST ON DISCOUNT", format4));
            sheet.setColumnView(col+9, 15);
            sheet.addCell(new Label(col+10, 11, "SHARED COST ON DISCOUNT", format4));
            sheet.setColumnView(col+10, 15);
            sheet.addCell(new Label(col+11, 11, "TOTAL PO COST", format4));
            sheet.setColumnView(col+11, 15);
            sheet.addCell(new Label(col+12, 11, "TOTAL PO SELL", format4));
            sheet.setColumnView(col+12, 15);
            sheet.addCell(new Label(col+13, 11, "TOTAL CUSTOMER DISCOUNT", format4));
            sheet.setColumnView(col+13, 15);
        }
        
        int row = 12;
        for (PoDetailHolder detail : detailList)
        {
            int colIndex = 0;
            BigDecimal unitPrice = "P".equalsIgnoreCase(detail.getOrderBaseUnit()) ? detail.getPackCost() : detail.getUnitCost();
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 11), format5));
            sheet.addCell(new Label(colIndex+1, row, detail.getBuyerItemCode() + ((detail.getBarcode()== null || detail.getBarcode().isEmpty()) ? "" : "/" + detail.getBarcode()) , format5));
            sheet.addCell(new Label(colIndex+2, row, detail.getItemDesc(), format5));
            sheet.addCell(new Label(colIndex+3, row, detail.getSupplierItemCode(), format5));
            sheet.addCell(new Label(colIndex+4, row, detail.getColourDesc(), format5));
            sheet.addCell(new Label(colIndex+5, row, detail.getSizeDesc(), format5));
            sheet.addCell(new Label(colIndex+6, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getOrderQty(), 2), format6));
            sheet.addCell(new Label(colIndex+7, row, detail.getOrderUom(), format5));
            
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex+8, row, bUtil.convertBigDecimalToStringIntegerWithScale(unitPrice, 2), format6));
                sheet.addCell(new Label(colIndex+9, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemCost().subtract(detail.getCostDiscountAmount()), 2), format6));
                sheet.addCell(new Label(colIndex+10, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemSharedCost(), 2), format6));
                sheet.addCell(new Label(colIndex+11, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemGrossCost(), 2), format6));
                sheet.addCell(new Label(colIndex+12, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemRetailAmount(), 2), format6));
                sheet.addCell(new Label(colIndex+13, row, bUtil.convertBigDecimalToStringIntegerWithScale(detail.getRetailDiscountAmount(), 2), format6));
            }
            row ++;
        }
    
        //set total number of items
        sheet.addCell(new Label(1, 8, String.valueOf(row - 12), format1));
    }
    
    
    //********************************
    //sor po export excel
    //********************************
    public void initSorPoSheet(WritableSheet sheet, PoHeaderHolder header,
        List<PoDetailHolder> detailList, List<PoLocationHolder> locationList,
        List<PoLocationDetailHolder> locationDetailList, boolean storeFlag)
        throws RowsExceededException, WriteException, Exception
    {
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.LEFT);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        format4.setWrap(true);
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format5.setAlignment(Alignment.CENTRE);
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        WritableCellFormat format6 = new jxl.write.WritableCellFormat(new jxl.write.NumberFormat("#,##0.00"));  
        format6.setAlignment(Alignment.RIGHT);
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        sheet.addCell(new Label(0, 0, "BUYER NAME", format1));
        sheet.addCell(new Label(1, 0, header.getBuyerName(), format1));
        sheet.addCell(new Label(0, 1, "PURCHASE ORDER", format1));
        sheet.addCell(new Label(1, 1, header.getPoNo(), format1));
        sheet.addCell(new Label(0, 2, "ORDER DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getPoDate(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(0, 3, "DELIVERY DATE FROM", format1));
        sheet.addCell(new Label(1, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDateFrom(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(2, 3, "DELIVERY DATE TO", format1));
        sheet.addCell(new Label(3, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDateTo(), DATE_FORMAT_DDMMMYY), format1));
        sheet.addCell(new Label(0, 4, "SPECIAL INSTRUCTIONS", format1));
        sheet.addCell(new Label(1, 4, "", format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        if (!storeFlag)
        {
            sheet.addCell(new Label(0, 8, "TOTAL NET AMOUNT ($)", format1));
            sheet.addCell(new Label(1, 8, this.formatBigDecimal(header.getTotalCost()), format1));
        }
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "S/No", format4));
        sheet.setColumnView(col, 27);
        sheet.addCell(new Label(col+1, 10, "EAN", format4));
        sheet.setColumnView(col+1, 30);
        sheet.addCell(new Label(col+2, 10, "Description", format4));
        sheet.setColumnView(col+2, 43);
        sheet.addCell(new Label(col+3, 10, "NTUC Stock", format4));
        sheet.setColumnView(col+3, 12);
        sheet.addCell(new Label(col+4, 10, "Packing", format4));
        sheet.setColumnView(col+4, 9);
        sheet.addCell(new Label(col+5, 10, "UOM", format4));
        sheet.setColumnView(col+5, 7);
        col = col + 5;
        if (!storeFlag)
        {
            sheet.addCell(new Label(col + 1, 10, "Unit Price ($)", format4));
            sheet.setColumnView(col + 1, 9);
            col = col + 1;
        }
        sheet.addCell(new Label(col + 1, 10, "ST/WH No", format4));
        sheet.setColumnView(col + 1, 10);
        sheet.addCell(new Label(col + 2, 10, "ST/WH Name", format4));
        sheet.setColumnView(col + 2, 16);
        sheet.addCell(new Label(col + 3, 10, "Delivery", format4));
        sheet.setColumnView(col + 3, 10);
        if (!storeFlag)
        {
            sheet.addCell(new Label(col + 4, 10, "Amount ($)", format4));
            sheet.setColumnView(col + 4, 11);
        }
        List<BuyerStoreHolder> allBuyerStores = buyerStoreService.selectBuyerStoresByBuyer(header.getBuyerCode());
        Map<String, BuyerStoreHolder> map = this.convertStoreListToMapUseStoreCodeAsKey(allBuyerStores);
        
        int row = 11;
        //this po has only one po_location.
        if (header.getPoSubType().equals("2"))
        {
            for (PoDetailHolder detail : detailList)
            {
                int colIndex = 0;
                BigDecimal packingFactor = "P".equalsIgnoreCase(detail.getOrderBaseUnit()) ? detail.getPackingFactor() : BigDecimal.ONE;
                BigDecimal unitPrice = "P".equalsIgnoreCase(detail.getOrderBaseUnit()) ? detail.getPackCost() : detail.getUnitCost();
                sheet.addCell(new Label(colIndex, row, String.valueOf(row - 10), format5));
                sheet.addCell(new Label(colIndex + 1, row, detail.getBarcode(), format5));
                sheet.addCell(new Label(colIndex + 2, row, detail.getItemDesc(), format2));
                sheet.addCell(new Label(colIndex + 3, row, detail.getBuyerItemCode(), format5));
                sheet.addCell(new jxl.write.Number(colIndex + 4, row, packingFactor.doubleValue(), format5));
                sheet.addCell(new Label(colIndex + 5, row, detail.getOrderUom(), format5));
                colIndex = colIndex + 5;
                if (!storeFlag)
                {
                    sheet.addCell(new jxl.write.Number(colIndex + 1, row, unitPrice.doubleValue(), format6));
                    colIndex = colIndex + 1;
                }
                sheet.addCell(new Label(colIndex + 1, row, this.mergeLocationCode(header.getShipToCode(), map), format2));
                sheet.addCell(new Label(colIndex + 2, row, header.getShipToName(), format5));
                sheet.addCell(new jxl.write.Number(colIndex + 3, row, detail.getOrderQty().doubleValue(), format5));
                if (!storeFlag)
                {
                    sheet.addCell(new jxl.write.Number(colIndex + 4, row, unitPrice.multiply(detail.getOrderQty()).doubleValue(), format6));
                }
                row ++;
            }
        }
        //po has multiple location
        else if (locationList != null && !locationList.isEmpty())
        {
            //group po location by line seq no
            Map<Integer, PoLocationHolder> locationMap = new HashMap<Integer, PoLocationHolder>();
            for (PoLocationHolder location : locationList)
            {
                locationMap.put(location.getLineSeqNo(), location);
            }
            //group po location detail by detail line seq no
            Map<Integer, List<PoLocationDetailHolder>> locationDetailMap = new HashMap<Integer, List<PoLocationDetailHolder>>();
            for (PoLocationDetailHolder locationDetail : locationDetailList)
            {
                if (locationDetailMap.containsKey(locationDetail.getDetailLineSeqNo()))
                {
                    locationDetailMap.get(locationDetail.getDetailLineSeqNo()).add(locationDetail);
                }
                else
                {
                    List<PoLocationDetailHolder> locationDetails = new ArrayList<PoLocationDetailHolder>();
                    locationDetails.add(locationDetail);
                    locationDetailMap.put(locationDetail.getDetailLineSeqNo(), locationDetails);
                }
            }
            //group po detail by line seq no 
            Map<Integer, PoDetailHolder> detailMap = new HashMap<Integer, PoDetailHolder>();
            for (PoDetailHolder detail : detailList)
            {
                detailMap.put(detail.getLineSeqNo(), detail);
            }
            int sno = 1;
            for (PoDetailHolder detail : detailList)
            {
                List<PoLocationDetailHolder> locationDetails = locationDetailMap.get(detail.getLineSeqNo());
                for (PoLocationDetailHolder locationDetail : locationDetails)
                {
                    int colIndex = 0;
                    BigDecimal packingFactor = "P".equalsIgnoreCase(detail.getOrderBaseUnit()) ? detail.getPackingFactor() : BigDecimal.ONE;
                    BigDecimal unitPrice = "P".equalsIgnoreCase(detail.getOrderBaseUnit()) ? detail.getPackCost() : detail.getUnitCost();
                    sheet.addCell(new Label(colIndex, row, String.valueOf(sno), format5));
                    sheet.addCell(new Label(colIndex + 1, row, detail.getBarcode(), format5));
                    sheet.addCell(new Label(colIndex + 2, row, detail.getItemDesc(), format2));
                    sheet.addCell(new Label(colIndex + 3, row, detail.getBuyerItemCode(), format5));
                    sheet.addCell(new jxl.write.Number(colIndex + 4, row, Double.valueOf(packingFactor.intValue()), format5));
                    sheet.addCell(new Label(colIndex + 5, row, detail.getOrderUom(), format5));
                    colIndex = colIndex + 5;
                    if (!storeFlag)
                    {
                        sheet.addCell(new jxl.write.Number(colIndex + 1, row, unitPrice.doubleValue(), format6));
                        colIndex = colIndex + 1;
                    }
                    PoLocationHolder location = locationMap.get(locationDetail.getLocationLineSeqNo());
                    sheet.addCell(new Label(colIndex + 1, row, this.mergeLocationCode(location.getLocationCode(), map), format2));
                    sheet.addCell(new Label(colIndex + 2, row, location.getLocationName(), format5));
                    sheet.addCell(new jxl.write.Number(colIndex + 3, row, locationDetail.getLocationShipQty().doubleValue(), format5));
                    if (!storeFlag)
                    {
                        sheet.addCell(new jxl.write.Number(colIndex + 4, row, locationDetail.getLocationShipQty().multiply(unitPrice).doubleValue(), format6));
                    }
                    row ++;
                }
                sno ++;
            }
            
            sheet.addCell(new Label(0, row + 2, "The goods are purchased in accordance with the specification on this purchase order and the terms and", format1));
            sheet.mergeCells(0, row + 2, 2, row + 2);
            sheet.addCell(new Label(0, row + 3, "conditions in the master agreement. This is a computer generated document. No signature is required.", format1));
            sheet.mergeCells(0, row + 3, 2, row + 3);
        }
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 11), format1));
    
    }
    
    
    private String mergeLocationCode(String locationCode, Map<String, BuyerStoreHolder> map)
    {
        String result = locationCode;
        if (locationCode == null || map == null || map.isEmpty() || !map.containsKey(locationCode.toUpperCase(Locale.US)))
        {
            return result;
        }
        BuyerStoreHolder store = map.get(locationCode.toUpperCase(Locale.US));
        return (store.getIsWareHouse() ? "WH" : "ST") + result;
    }
    
    
    private Map<String, BuyerStoreHolder> convertStoreListToMapUseStoreCodeAsKey(List<BuyerStoreHolder> list)
    {
        if (list == null || list.isEmpty())
        {
            return null;
        }
        Map<String, BuyerStoreHolder> map = new HashMap<String, BuyerStoreHolder>();
        for (BuyerStoreHolder store : list)
        {
            map.put(store.getStoreCode().toUpperCase(), store);
        }
        return map;
    }
    
    
    private String formatBigDecimal(BigDecimal obj) 
        throws ParseException
    {
        if (obj == null) return null;
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        return df.format(obj.doubleValue());
    }

}
