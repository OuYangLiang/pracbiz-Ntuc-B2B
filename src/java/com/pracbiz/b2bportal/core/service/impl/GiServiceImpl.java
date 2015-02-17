//*****************************************************************************
//
// File Name       :  GiServiceImpl.java
// Date Created    :  Jan 3, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Jan 3, 2014 2:44:54 PM $
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
import java.util.List;




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
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.service.GiDetailExtendedService;
import com.pracbiz.b2bportal.core.service.GiDetailService;
import com.pracbiz.b2bportal.core.service.GiHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.GiService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GiServiceImpl implements GiService
{
    @Autowired GiHeaderService giHeaderService;
    @Autowired GiDetailService giDetailService;
    @Autowired GiHeaderExtendedService giHeaderExtendedService;
    @Autowired GiDetailExtendedService giDetailExtendedService;
    
    @Override
    public void insert(GiHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditInsert(CommonParameterHolder cp, GiHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKeySelective(GiHolder oldObj_, GiHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        GiHolder oldObj_, GiHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKey(GiHolder oldObj_, GiHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp,
        GiHolder oldObj_, GiHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(GiHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditDelete(CommonParameterHolder cp, GiHolder oldObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public GiHolder selectGiByKey(BigDecimal giOid) throws Exception
    {
        if (giOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GiHeaderHolder header = giHeaderService.selectGiHeaderByKey(giOid);
        List<GiDetailHolder> details = giDetailService.selectByGiOid(giOid);
        List<GiHeaderExtendedHolder> headerExs = giHeaderExtendedService.selectHeaderExtendedByKey(giOid);
        List<GiDetailExtendedHolder> detailExs = giDetailExtendedService.selectDetailExtendedByKey(giOid);
        
        GiHolder gi = new GiHolder();
        gi.setGiHeader(header);
        gi.setDetails(details);
        gi.setHeaderExtended(headerExs);
        gi.setDetailExtended(detailExs);
        
        return gi;
    }

    @Override
    public byte[] exportExcel(List<BigDecimal> giOids, boolean storeFlag)
        throws Exception
    {
        if(giOids == null || giOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal giOid : giOids)
        {
            this.initSheetByGiOid(giOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }

    @Override
    public byte[] exportSummaryExcel(List<GiSummaryHolder> params,
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
    
    
    //**********************
    //private method
    //**********************
    private void initSheetByGiOid(BigDecimal giOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        GiHolder gi = this.selectGiByKey(giOid);
        GiHeaderHolder header = gi.getGiHeader();
        List<GiDetailHolder> details = gi.getDetails();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getGiNo(), sheetIndex);
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
        sheet.addCell(new Label(0, 1, "GI NO", format1));
        sheet.addCell(new Label(1, 1, header.getGiNo(), format1));
        sheet.addCell(new Label(0, 2, "GI DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getGiDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "RTV NO", format1));
        sheet.addCell(new Label(1, 3, header.getRtvNo(), format1));
        sheet.addCell(new Label(0, 4, "REMARKS", format1));
        sheet.addCell(new Label(1, 4, header.getGiRemarks(), format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        
        sheet.addCell(new Label(0, 8, "STORE", format1));
        sheet.addCell(new Label(1, 8, header.getIssuingStoreCode(), format1));
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "SEQ", format4));
        sheet.setColumnView(col, 25);
        sheet.addCell(new Label(col + 1, 10, "SKU NO./BARCODE", format4));
        sheet.setColumnView(col + 1, 25);
        sheet.addCell(new Label(col + 2, 10, "DESCRIPTION", format4));
        sheet.setColumnView(col + 2, 35);
        sheet.addCell(new Label(col + 3, 10, "ARTICLE NO.", format4));
        sheet.setColumnView(col + 3, 20);
        sheet.addCell(new Label(col + 4, 10, "COLOUR", format4));
        sheet.setColumnView(col + 4, 12);
        sheet.addCell(new Label(col + 5, 10, "SIZE", format4));
        sheet.setColumnView(col + 5, 12);
        sheet.addCell(new Label(col + 6, 10, "UOM", format4));
        sheet.setColumnView(col + 6, 10);
        sheet.addCell(new Label(col + 7, 10, "QTY RETURNED", format5));
        sheet.setColumnView(col + 7, 15);
        sheet.addCell(new Label(col + 8, 10, "QTY ISSUED", format5));
        sheet.setColumnView(col + 8, 15);
    
        if (!storeFlag)
        {
            sheet.addCell(new Label(col + 9, 10, "UNIT COST ($)", format5));
            sheet.setColumnView(col + 9, 20);
        }
       
        int row = 11;
        for (GiDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 10), format4));
            sheet.addCell(new Label(colIndex + 1, row, detail.getBuyerItemCode() + (detail.getBarcode() == null ? "" : "/" + detail.getBarcode())  , format2));
            sheet.addCell(new Label(colIndex + 2, row, detail.getBarcode(), format2));
            sheet.addCell(new Label(colIndex + 3, row, detail.getSupplierItemCode(), format2));
            sheet.addCell(new Label(colIndex + 4, row, detail.getColourDesc(), format2));
            sheet.addCell(new Label(colIndex + 5, row, detail.getSizeDesc(), format2));
            sheet.addCell(new Label(colIndex + 6, row, detail.getRtvUom(), format2));
            sheet.addCell(new Label(colIndex + 7, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getRtvQty(), 2), format3));
            sheet.addCell(new Label(colIndex + 8, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getIssuedQty(), 2), format3));
        
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex + 9, row, decimalUtil
                    .convertBigDecimalToStringIntegerWithScale(
                        detail.getUnitCost() == null ? BigDecimal.ZERO : detail
                            .getUnitCost(), 2), format3));
            }
            row ++;
        }
        
        sheet.addCell(new Label(0, row + 2, "THIS IS A COMPUTER-GENERATED DOCUMENT. NO SIGNATURE IS REQUIRED.", format1));
        sheet.mergeCells(0, row + 2, 2, row + 2);
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 11), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<GiSummaryHolder> params, boolean storeFlag)throws Exception
    {
        int sheetIndex = 0;
        WritableSheet sheet = this.initSheetHeader(wwb, sheetIndex, storeFlag);
        sheet.getSettings().setZoomFactor(100);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format1.setBorder(Border.ALL, BorderLineStyle.THIN);
        format1.setAlignment(Alignment.LEFT);
        
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(
            WritableFont.TIMES, 10));
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setAlignment(Alignment.RIGHT);
        
        GiSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row, param.getGiNo(), format1));
            sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getGiDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(2, row, param.getBuyerCode(), format1));
            sheet.addCell(new Label(3, row, param.getBuyerName(), format1));
            sheet.addCell(new Label(4, row, param.getSupplierCode(), format1));
            sheet.addCell(new Label(5, row, param.getSupplierName(), format1));
            sheet.addCell(new Label(6, row, param.getRtvNo(), format1));
            sheet.addCell(new Label(7, row, DateUtil.getInstance().convertDateToString(param.getRtvDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(8, row, param.getReadStatus().name(), format1));
            sheet.addCell(new Label(9, 0, param.getDetailCount().toString(), format1));
            sheet.addCell(new Label(9, 0, ""));
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("GiSummaryReport_" + (sheetIndex + 1), sheetIndex);
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
        sheet.addCell(new Label(0, 0, "GI NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "GI DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "BUYER CODE", format1));
        sheet.setColumnView(3, 25);
        sheet.addCell(new Label(3, 0, "BUYER NAME", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(5, 25);
        sheet.addCell(new Label(5, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(6, 20);
        sheet.addCell(new Label(6, 0, "RTV NO", format1));
        sheet.setColumnView(7, 20);
        sheet.addCell(new Label(7, 0, "RTV DATE", format1));
        sheet.setColumnView(8, 15);
        sheet.addCell(new Label(8, 0, "READ STATUS", format1));
        sheet.setColumnView(9, 15);
        sheet.addCell(new Label(9, 0, "TOTAL NO OF ITEM", format1));
        sheet.addCell(new Label(10, 0, ""));
        
        return sheet;
    }
    
}
