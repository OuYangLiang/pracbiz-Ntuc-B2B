//*****************************************************************************
//
// File Name       :  RtvServiceImpl.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 3:41:18 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;


import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jxl.Workbook;
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


import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.holder.extension.RtvSummaryHolder;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.service.RtvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvDetailService;
import com.pracbiz.b2bportal.core.service.RtvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;
import com.pracbiz.b2bportal.core.service.RtvService;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvServiceImpl implements RtvService
{
    @Autowired RtvHeaderService rtvHeaderService;
    @Autowired RtvDetailService rtvDetailService;
    @Autowired RtvHeaderExtendedService rtvHeaderExtendedService;
    @Autowired RtvDetailExtendedService rtvDetailExtendedService;
    @Autowired RtvLocationService rtvLocationService;
    @Autowired RtvLocationDetailService rtvLocationDetailService;
    @Autowired RtvLocationDetailExtendedService rtvLocaionDetailExtendedService;
    @Autowired ItemService itemService;
    
    @Override
    public void insert(RtvHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditInsert(CommonParameterHolder cp, RtvHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKeySelective(RtvHolder oldObj_, RtvHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        RtvHolder oldObj_, RtvHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(RtvHolder oldObj_, RtvHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp,
        RtvHolder oldObj_, RtvHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void delete(RtvHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void auditDelete(CommonParameterHolder cp, RtvHolder oldObj_)
        throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public RtvHolder selectRtvByKey(BigDecimal rtvOid) throws Exception
    {
        if (rtvOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        RtvHeaderHolder header = rtvHeaderService.selectRtvHeaderByKey(rtvOid);
        List<RtvDetailHolder> details = rtvDetailService.selectRtvDetailByKey(rtvOid);
        List<RtvHeaderExtendedHolder> headerExs = rtvHeaderExtendedService.selectHeaderExtendedByKey(rtvOid);
        List<RtvDetailExtendedHolder> detailExs = rtvDetailExtendedService.selectDetailExtendedByKey(rtvOid);
        List<RtvLocationHolder> locations = rtvLocationService.selectRtvLocationByRtvOid(rtvOid);
        List<RtvLocationDetailHolder> locationDetails = rtvLocationDetailService.selectRtvLocationDetailByRtvOid(rtvOid);
        List<RtvLocationDetailExtendedHolder> locationDetailExs = rtvLocaionDetailExtendedService.selectRtvLocationDetailExByRtvOid(rtvOid);
        
        RtvHolder rtv = new RtvHolder();
        rtv.setRtvHeader(header);;
        rtv.setRtvDetail(details);
        rtv.setHeaderExtended(headerExs);
        rtv.setDetailExtended(detailExs);
        rtv.setLocations(locations);
        rtv.setLocationDetails(locationDetails);
        rtv.setRtvLocDetailExtendeds(locationDetailExs);
        
        return rtv;
    }


    @Override
    public byte[] exportExcel(List<BigDecimal> rtvOids, boolean storeFlag)
        throws Exception
    {
        if (rtvOids == null || rtvOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for (BigDecimal rtvOid : rtvOids)
        {
            this.initSheetByRtvOid(rtvOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportSummaryExcel(List<RtvSummaryHolder> params,
        boolean storeFlag) throws Exception
    {
        if (params == null || params.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        this.initSheetContent(wwb, params, storeFlag);
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }
    
    //****************
    //private method
    //****************
    private void initSheetByRtvOid(BigDecimal rtvOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        RtvHolder rtv = this.selectRtvByKey(rtvOid);
        RtvHeaderHolder header = rtv.getRtvHeader();
        List<RtvDetailHolder> detailList = rtv.getRtvDetail();
//        List<RtvLocationHolder> locationList = rtv.getLocations();
//        List<RtvLocationDetailHolder> locationDetailList = rtv.getLocationDetails();
        List<RtvDetailExtendedHolder> detailExs = rtv.getDetailExtended();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getRtvNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.CENTRE);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        WritableCellFormat format4 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format4.setBorder(Border.ALL, BorderLineStyle.THIN);
        format4.setAlignment(Alignment.CENTRE);
        format4.setVerticalAlignment(VerticalAlignment.CENTRE);
        format4.setWrap(true);
        WritableCellFormat format5 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format5.setBorder(Border.ALL, BorderLineStyle.THIN);
        format5.setAlignment(Alignment.RIGHT);
        format5.setVerticalAlignment(VerticalAlignment.CENTRE);
        format5.setWrap(true);
        
        WritableCellFormat format6 = new jxl.write.WritableCellFormat(new jxl.write.NumberFormat("#,##0.00"));  
        format6.setAlignment(Alignment.RIGHT);
        format6.setBorder(Border.ALL, BorderLineStyle.THIN);
        
        sheet.addCell(new Label(0, 0, "BUYER NAME", format1));
        sheet.addCell(new Label(1, 0, header.getBuyerName(), format1));
        sheet.addCell(new Label(0, 1, "RTV NO", format1));
        sheet.addCell(new Label(1, 1, header.getRtvNo(), format1));
        sheet.addCell(new Label(0, 2, "RTV DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getRtvDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "INV NO", format1));
        sheet.addCell(new Label(1, 3, header.getInvNo(), format1));
        sheet.addCell(new Label(0, 4, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 4, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 6, "TOTAL NUMBER OF ITEMS", format1));
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "Item No.", format4));
        sheet.setColumnView(col, 25);
        sheet.addCell(new Label(col+1, 10, "Article", format4));
        sheet.setColumnView(col+1, 25);
        sheet.addCell(new Label(col+2, 10, "Description", format4));
        sheet.setColumnView(col+2, 40);
        sheet.addCell(new Label(col+3, 10, "Class", format4));
        sheet.setColumnView(col+3, 20);
        sheet.addCell(new Label(col+4, 10, "Order Unit", format4));
        sheet.setColumnView(col+4, 12);
        sheet.addCell(new Label(col+5, 10, "Pack Size", format4));
        sheet.setColumnView(col+5, 10);
        sheet.addCell(new Label(col+6, 10, "Qty Return", format4));
        sheet.setColumnView(col+6, 10);
        col = col + 7;
        if (!storeFlag)
        {
            sheet.addCell(new Label(col, 10, "Order Unit Gross Cost ($)", format5));
            sheet.setColumnView(col, 25);
            sheet.addCell(new Label(col + 1, 10, "Return Cost ($)", format5));
            sheet.setColumnView(col + 1, 15);
            col = col + 2;
        }
        sheet.addCell(new Label(col, 10, "Vendor Article", format4));
        sheet.setColumnView(col, 15);
        sheet.addCell(new Label(col + 1, 10, "Returned Reason", format4));
        sheet.setColumnView(col + 1, 30);
        sheet.addCell(new Label(col + 2, 10, "Offer", format4));
        sheet.setColumnView(col + 2, 7);
      
        Map<Integer, List<RtvDetailExtendedHolder>> detailExMap = new HashMap<Integer, List<RtvDetailExtendedHolder>>();
//        Map<String, List<RtvLocationDetailHolder>> locDetailMap = new HashMap<String, List<RtvLocationDetailHolder>>();
//        Map<String, RtvLocationHolder> rtvLocMap = new HashMap<String, RtvLocationHolder>();
        
        for(RtvDetailExtendedHolder detailEx : detailExs)
        {
            if (detailExMap.containsKey(detailEx.getLineSeqNo()))
            {
                detailExMap.get(detailEx.getLineSeqNo()).add(detailEx);
            }
            else
            {
                List<RtvDetailExtendedHolder> rtvDetailExs = new ArrayList<RtvDetailExtendedHolder>();
                rtvDetailExs.add(detailEx);
                detailExMap.put(detailEx.getLineSeqNo(), rtvDetailExs);
            }
        }

        ItemHolder item = null;
        int row = 11;
        for (RtvDetailHolder detail : detailList)
        {
            int colIndex = 0;
            
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 10), format2));
            sheet.addCell(new Label(colIndex+1, row, detail.getBuyerItemCode(), format2));
            sheet.addCell(new Label(colIndex+2, row, detail.getItemDesc(), format2));
            item = itemService
                .selectItemByBuyerOidAndBuyerItemCode(header.getBuyerOid(), detail.getBuyerItemCode());
            
            if (item != null)
            {
                sheet.addCell(new Label(colIndex+3, row, item.getClassCode(), format2));
            }
            else
            {
                sheet.addCell(new Label(colIndex+3, row, "", format2));
            }
            
            sheet.addCell(new Label(colIndex+4, row, detail.getReturnUom(), format2));
            sheet.addCell(new Label(colIndex+5, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getPackingFactor(), 0), format3));
            sheet.addCell(new Label(colIndex+6, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getReturnQty(), 2), format3));
            colIndex = colIndex + 7;
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getUnitCost(), 2), format3));
                sheet.addCell(new Label(colIndex + 1, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemCost(), 2), format3));
                colIndex = colIndex + 2;
            }
            sheet.addCell(new Label(colIndex, row, detail.getSupplierItemCode(), format2));
            sheet.addCell(new Label(colIndex + 1, row, detail.getReasonDesc(), format2));
            if (detailExMap.containsKey(detail.getLineSeqNo()))
            {
                for (RtvDetailExtendedHolder detailEx : detailExMap.get(detail.getLineSeqNo()))
                {
                    if (detailEx.getFieldName().equalsIgnoreCase("Offer"))
                    {
                        sheet.addCell(new Label(colIndex + 2, row, detailEx.getBoolValue()? "Y" : "N", format2));
                    }
                }
               
            }
            
            row ++;
        }
        //set total number of items
        sheet.addCell(new Label(1, 6, String.valueOf(row - 11), format1));
        
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<RtvSummaryHolder> params, boolean storeFlag)throws Exception
    {
        int sheetIndex = 0;
        WritableSheet sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.LEFT);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.RIGHT);
        
        
        RtvSummaryHolder param = null;
        int row = 1;
        for (int i = 0 ; i < params.size() ; i++)
        {
            if ((i + 1) % 65535 == 0)
            {
                sheetIndex ++ ;
                sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
                row = 1;
            }
            
            param = params.get(i);
            sheet.addCell(new Label(0, row, param.getRtvNo(), format1));
            sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getRtvDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(2, row, param.getInvNo(), format1));
            sheet.addCell(new Label(3, row, DateUtil.getInstance().convertDateToString(param.getInvDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(4, row, param.getBuyerCode(), format1));
            sheet.addCell(new Label(5, row, param.getBuyerName(), format1));
            sheet.addCell(new Label(6, row, param.getSupplierCode(), format1));
            sheet.addCell(new Label(7, row, param.getSupplierName(), format1));
            sheet.addCell(new Label(8, row, param.getReasonDesc(), format1));
            sheet.addCell(new Label(9, row, param.getDetailCount().toString(), format2));
            sheet.addCell(new Label(10, row, param.getReadStatus().name(), format1));
            sheet.addCell(new Label(11, row, ""));
            
            row ++;
        }
    }

    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("RtvSummaryReport_" + (sheetIndex + 1), sheetIndex);
        sheet.getSettings().setZoomFactor(100);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.LEFT);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10, WritableFont.BOLD));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.RIGHT);
        
        
        sheet.setColumnView(0, 15);
        sheet.addCell(new Label(0, 0, "RTV NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "RTV DATE", format1));
        sheet.setColumnView(2, 17);
        sheet.addCell(new Label(2, 0, "INVOICE NO", format1));
        sheet.setColumnView(3, 15);
        sheet.addCell(new Label(3, 0, "INVOICE DATE", format1));
        sheet.setColumnView(4, 17);
        sheet.addCell(new Label(4, 0, "BUYER CODE", format1));
        sheet.setColumnView(5, 17);
        sheet.addCell(new Label(5, 0, "BUYER NAME", format1));
        sheet.setColumnView(6, 17);
        sheet.addCell(new Label(6, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(7, 17);
        sheet.addCell(new Label(7, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(8, 17);
        sheet.addCell(new Label(8, 0, "REASON DESC", format1));
        sheet.setColumnView(9, 20);
        sheet.addCell(new Label(9, 0, "TOTAL NO OF ITEM", format1));
        sheet.setColumnView(10, 15);
        sheet.addCell(new Label(10, 0, "READ STATUS", format1));
        sheet.addCell(new Label(11, 0, ""));
        
        return sheet;
    }
}
