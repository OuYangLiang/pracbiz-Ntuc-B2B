//*****************************************************************************
//
// File Name       :  GrnServiceImpl.java
// Date Created    :  Aug 3, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 3, 2013 10:45:31 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
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
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.extension.GrnSummaryHolder;
import com.pracbiz.b2bportal.core.service.GrnDetailService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.PoDetailService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class GrnServiceImpl implements GrnService
{
    @Autowired transient private GrnHeaderService grnHeaderService;
    @Autowired transient private GrnDetailService grnDetailService;
    @Autowired transient private PoDetailService poDetailService;

    @Override
    public GrnHolder selectByKey(BigDecimal grnOid) throws Exception
    {
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        GrnHolder rlt = new GrnHolder();
        
        GrnHeaderHolder header = grnHeaderService.selectGrnHeaderByKey(grnOid);
        
        if (null == header)
            return null;
        
        List<GrnDetailHolder> details = grnDetailService.selectGrnDetailByKey(grnOid);
        
        rlt.setHeader(header);
        rlt.setDetails(details);
        
        return rlt;
    }

    @Override
    public BigDecimal computeGrnAmtByGrnOidAndPoOid(BigDecimal grnOid, BigDecimal poOid)
            throws Exception
    {
        if (grnOid == null || poOid == null)
        {
            throw new IllegalArgumentException();
        }
        GrnHolder grn = selectByKey(grnOid);
        if (grn == null)
        {
            return BigDecimal.ZERO;
        }
        List<PoDetailHolder> poDetails = poDetailService.selectPoDetailsByPoOid(poOid);
        if (poDetails == null || poDetails.isEmpty())
        {
            return BigDecimal.ZERO;
        }
        List<GrnDetailHolder> grnDetails = grn.getDetails();
        if (grnDetails == null || grnDetails.isEmpty())
        {
            return BigDecimal.ZERO;
        }
        BigDecimal totalCost = BigDecimal.ZERO;
        for (GrnDetailHolder grnDetail : grnDetails)
        {
            for (PoDetailHolder poDetail : poDetails)
            {
                if (grnDetail.getBuyerItemCode().equals(poDetail.getBuyerItemCode()))
                {
                    grnDetail.setUnitCost(grnDetail.getOrderBaseUnit().equalsIgnoreCase("P") ? poDetail.getPackCost() : poDetail.getUnitCost());
                    continue;
                }
            }
            if (grnDetail.getUnitCost() == null)
            {
                grnDetail.setUnitCost(BigDecimal.ZERO);
            }
            totalCost = totalCost.add(grnDetail.getUnitCost().multiply(grnDetail.getReceiveQty()));
        }
        return totalCost;
    }

    @Override
    public void updateGrn(GrnHolder grn) throws Exception
    {
        if (grn == null)
        {
            throw new IllegalArgumentException();
        }
        
        grn.getHeader().setAllEmptyStringToNull();
        grnHeaderService.updateByPrimaryKey(null, grn.getHeader());
        
        
        for (GrnDetailHolder grnDetail : grn.getDetails())
        {
            grnDetail.setAllEmptyStringToNull();
            grnDetailService.updateByPrimaryKeySelective(null, grnDetail);
        }
    }

    @Override
    public void auditDelete(CommonParameterHolder cp, GrnHolder oldObj)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditInsert(CommonParameterHolder cp, GrnHolder newObj)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void auditUpdateByPrimaryKey(CommonParameterHolder cp,
        GrnHolder oldObj, GrnHolder newObj) throws Exception
    {
        this.updateGrn(newObj);
        
    }

    @Override
    public void auditUpdateByPrimaryKeySelective(CommonParameterHolder cp,
        GrnHolder oldObj, GrnHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(GrnHolder oldObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void insert(GrnHolder newObj) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKey(GrnHolder oldObj, GrnHolder newObj)
        throws Exception
    {
        this.updateGrn(newObj);
        
    }

    @Override
    public void updateByPrimaryKeySelective(GrnHolder oldObj, GrnHolder newObj)
        throws Exception
    {
        this.updateGrn(newObj);
        
    }

    @Override
    public List<GrnHolder> select(GrnHolder param) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] exportExcel(List<BigDecimal> grnOids, boolean storeFlag)
        throws Exception
    {
        if(grnOids == null || grnOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal grnOid : grnOids)
        {
            this.initSheetByGrnOid(grnOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }

    @Override
    public byte[] exportSummaryExcel(List<GrnSummaryHolder> params, boolean storeFlag)
        throws Exception
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
    private void initSheetByGrnOid(BigDecimal grnOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        GrnHolder grn = this.selectByKey(grnOid);
        GrnHeaderHolder header = grn.getHeader();
        List<GrnDetailHolder> details = grn.getDetails();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getGrnNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
        WritableCellFormat format1 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format1.setAlignment(Alignment.LEFT);
        WritableCellFormat format2 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format2.setAlignment(Alignment.CENTRE);
        format2.setBorder(Border.ALL, BorderLineStyle.THIN);
        format2.setVerticalAlignment(VerticalAlignment.CENTRE);
        WritableCellFormat format3 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 10));
        format3.setAlignment(Alignment.RIGHT);
        format3.setBorder(Border.ALL, BorderLineStyle.THIN);
        format3.setVerticalAlignment(VerticalAlignment.CENTRE);
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
        sheet.addCell(new Label(0, 1, "GRN NO", format1));
        sheet.addCell(new Label(1, 1, header.getGrnNo(), format1));
        sheet.addCell(new Label(0, 2, "GRN DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getGrnDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "RECEIVE_STORE_CODE", format1));
        sheet.addCell(new Label(1, 3, header.getReceiveStoreCode(), format1));
        sheet.addCell(new Label(0, 4, "SPECIAL INSTRUCTIONS", format1));
        sheet.addCell(new Label(1, 4, "", format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 9, "SEQ", format4));
        sheet.setColumnView(col, 25);
        sheet.addCell(new Label(col+1, 9, "SKU NO/BARCODE", format4));
        sheet.setColumnView(col+1, 35);
        sheet.addCell(new Label(col+2, 9, "DESCRIPTION", format4));
        sheet.setColumnView(col+2, 35);
        sheet.addCell(new Label(col+3, 9, "ARTICLE NO", format4));
        sheet.setColumnView(col+3, 25);
        sheet.addCell(new Label(col+4, 9, "COLOUR", format4));
        sheet.setColumnView(col+4, 12);
        sheet.addCell(new Label(col+5, 9, "SIZE", format4));
        sheet.setColumnView(col+5, 12);
        sheet.addCell(new Label(col+6, 9, "UOM", format4));
        sheet.setColumnView(col+6, 10);
        sheet.addCell(new Label(col+7, 9, "QTY ORDER", format5));
        sheet.setColumnView(col+7, 15);
        sheet.addCell(new Label(col+8, 9, "FOC QTY", format5));
        sheet.setColumnView(col+8, 15);
        sheet.addCell(new Label(col+9, 9, "QTY RECEIVED", format5));
        sheet.setColumnView(col+9, 15);
        sheet.addCell(new Label(col+10, 9, "FOC QTY RECIEVED", format5));
        sheet.setColumnView(col+10, 15);
       
        int row = 10;
        for (GrnDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 9), format2));
            sheet.addCell(new Label(colIndex+1, row, detail.getBuyerItemCode() + (detail.getBarcode()== null ? "" : "/" + detail.getBarcode()), format2));
            sheet.addCell(new Label(colIndex+2, row, detail.getItemDesc(), format2));
            sheet.addCell(new Label(colIndex+3, row, detail.getSupplierItemCode(), format2));
            sheet.addCell(new Label(colIndex+4, row, detail.getColourDesc(), format2));
            sheet.addCell(new Label(colIndex+5, row, detail.getSizeDesc(), format2));
            sheet.addCell(new Label(colIndex+6, row, detail.getOrderUom(), format2));
            sheet.addCell(new Label(colIndex+7, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getOrderQty(), 2), format3));
            sheet.addCell(new Label(colIndex+8, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getFocQty(), 2), format3));
            sheet.addCell(new Label(colIndex+9, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getReceiveQty(), 2), format3));
            sheet.addCell(new Label(colIndex+10, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getFocReceiveQty(), 2), format3));
            row ++;
        }
        sheet.addCell(new Label(0, row + 2, "THIS IS A COMPUTER-GENERATED DOCUMENT. NO SIGNATURE IS REQUIRED.", format1));
        sheet.mergeCells(0, row + 2, 2, row + 2);
        
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 10), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<GrnSummaryHolder> params, boolean storeFlag)throws Exception
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
        
        GrnSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row , param.getGrnNo(), format1));
            sheet.addCell(new Label(1, row , DateUtil.getInstance().convertDateToString(param.getGrnDate(), "yyyy/MM/dd"), format1));
            sheet.addCell(new Label(2, row , param.getPoNo(), format1));
            sheet.addCell(new Label(3, row , DateUtil.getInstance().convertDateToString(param.getPoDate(), "yyyy/MM/dd"), format1));
            sheet.addCell(new Label(4, row , param.getBuyerCode(), format1));
            sheet.addCell(new Label(5, row , param.getBuyerName(), format1));
            sheet.addCell(new Label(6, row , param.getSupplierCode(), format1));
            sheet.addCell(new Label(7, row , param.getSupplierName(), format1));
            sheet.addCell(new Label(8, row , param.getDetailCount().toString(), format2));
            sheet.addCell(new Label(9, row , param.getReadStatus().toString(), format1));
            sheet.addCell(new Label(10, row , ""));
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("GrnSummaryReport_" + (sheetIndex + 1), sheetIndex);
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
        
        
        sheet.setColumnView(0, 20);
        sheet.addCell(new Label(0, 0, "GRN NO", format1));
        sheet.setColumnView(1, 20);
        sheet.addCell(new Label(1, 0, "GRN DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "PO NO", format1));
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "PO DATE", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "BUYER CODE", format1));
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "BUYER NAME", format1));
        sheet.setColumnView(6, 20);
        sheet.addCell(new Label(6, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(7, 20);
        sheet.addCell(new Label(7, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(8, 20);
        sheet.addCell(new Label(8, 0, "TOTAL NO OF ITEM", format2));
        sheet.setColumnView(9, 20);
        sheet.addCell(new Label(9, 0, "READ STATUS", format1));
        sheet.addCell(new Label(10, 0, ""));
        
        return sheet;
    }

}
