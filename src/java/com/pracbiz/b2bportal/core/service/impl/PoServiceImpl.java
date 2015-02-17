package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.PoService;

public class PoServiceImpl extends DBActionServiceDefaultImpl<PoHolder> implements PoService
{
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private PoDetailService poDetailService;
    @Autowired transient private PoLocationService poLocationService;
    @Autowired transient private PoLocationDetailService poLocationDetailService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private PoHeaderExtendedService poHeaderExtendedService;
    @Autowired transient private PoDetailExtendedService poDetailExtendedService;
    @Autowired transient private PoLocationDetailExtendedService poLocationDetailExtendedService;
    @Autowired transient private BuyerService buyerService;
    
    @Override
    public void updateByPrimaryKeySelective(PoHolder oldObj_, PoHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateByPrimaryKey(PoHolder oldObj_, PoHolder newObj_)
        throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(PoHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PoHolder selectPoByKey(BigDecimal poOid) throws Exception
    {
        if (poOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        PoHeaderHolder poHeader = poHeaderService.selectPoHeaderByKey(poOid);
        List<PoHeaderExtendedHolder> poHeaderExs = poHeaderExtendedService
                .selectHeaderExtendedByKey(poOid);
        List<PoDetailHolder> details = poDetailService
            .selectPoDetailsByPoOid(poOid);
        List<PoDetailExtendedHolder> poDetailExs = poDetailExtendedService
            .selectDetailExtendedByKey(poOid);
        List<PoLocationHolder> locations = poLocationService
            .selectLocationsByPoOid(poOid);
        List<PoLocationDetailHolder> locationDetails = poLocationDetailService
            .selectPoLocationDetailsByPoOid(poOid);
        
        PoHolder po = new PoHolder();
        po.setPoHeader(poHeader);
        po.setHeaderExtendeds(poHeaderExs);
        po.setDetails(details);
        po.setDetailExtendeds(poDetailExs);
        po.setLocations(locations);
        po.setLocationDetails(locationDetails);
        
        return po;
    }

    
    @Override
    public byte[] exportSummaryExcel(List<PoSummaryHolder> params, boolean storeFlag)
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
    
    
    public ReportEngineParameter<PoHolder> retrieveParameter(
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        PoHeaderHolder header = poHeaderService.selectPoHeaderByKey(msg.getDocOid());
        BuyerHolder buyer = buyerService.selectBuyerWithBlobsByKey(msg.getBuyerOid());
        List<PoDetailHolder> details = poDetailService
            .selectPoDetailsByPoOid(msg.getDocOid());
        List<PoHeaderExtendedHolder> headerExtendeds = poHeaderExtendedService
            .selectHeaderExtendedByKey(msg.getDocOid());

        List<PoDetailExtendedHolder> detailExtendeds = poDetailExtendedService
            .selectDetailExtendedByKey(msg.getDocOid());
        
        List<PoLocationHolder> locations = poLocationService
            .selectLocationsByPoOid(msg.getDocOid());
        List<PoLocationDetailHolder> locationDetails = poLocationDetailService
            .selectPoLocationDetailsByPoOid(msg.getDocOid());
        List<PoLocationDetailExtendedHolder> poLocDetailExtendeds = poLocationDetailExtendedService
            .selectPoLocationDetailExtendedsByPoOid(msg.getDocOid());
    
        ReportEngineParameter<PoHolder> reportEngineParameter = new ReportEngineParameter<PoHolder>();

        PoHolder data = new PoHolder();

        data.setPoHeader(header);
        data.setDetails(details);
        data.setHeaderExtendeds(headerExtendeds);
        data.setDetailExtendeds(detailExtendeds);
        data.setLocations(locations);
        data.setLocationDetails(locationDetails);
        data.setPoLocDetailExtendeds(poLocDetailExtendeds);
        
        reportEngineParameter.setBuyer(buyer);
        reportEngineParameter.setData(data);
        reportEngineParameter.setMsgTransactions(msg);
        reportEngineParameter.setSupplier(supplier);

        return reportEngineParameter;
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("PoSummaryReport_" + (sheetIndex + 1), sheetIndex);
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
        sheet.addCell(new Label(0, 0, "PO NO", format1));
        
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "PO DATE", format1));
        
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "BUYER CODE", format1));
        
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "SUPPLIER CODE", format1));
        
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "SUPPLIER NAME", format1));
        
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "TOTAL NO OF ITEM", format1));
        
        int col = 6;
        if (!storeFlag)
        {
            sheet.setColumnView(col, 20);
            sheet.addCell(new Label(col, 0, "TOTAL NET AMOUNT", format1));
            
            col ++ ;
        }
        sheet.setColumnView(col, 15);
        sheet.addCell(new Label(col, 0, "READ STATUS", format1));
        sheet.addCell(new Label((col + 1), 0, ""));
        
        return sheet;
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<PoSummaryHolder> params, boolean storeFlag)throws Exception
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
        
        
        PoSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row , param.getPoNo(), format2));
            sheet.addCell(new Label(1, row , DateUtil.getInstance().convertDateToString(param.getPoDate(), "yyyy/MM/dd"), format2));
            sheet.addCell(new Label(2, row , param.getBuyerCode(), format1));
            sheet.addCell(new Label(3, row , param.getSupplierCode(), format2));
            sheet.addCell(new Label(4, row , param.getSupplierName(), format1));
            sheet.addCell(new Label(5, row , param.getDetailCount().toString(), format2));
            int col = 6;
            if (!storeFlag)
            {
                sheet.addCell(new Label(col, row , BigDecimalUtil.getInstance().convertBigDecimalToStringIntegerWithScale(param.getTotalCost(), 2), format2));
                col ++;
            }
            sheet.addCell(new Label(col, row , param.getReadStatus().name(), format1));
            sheet.addCell(new Label(col + 1, row , ""));
            row ++;
        }
    }

    @Override
    public void insert(PoHolder po) throws Exception
    {
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(po.getOldPoOid());
        msg.setDocOid(po.getPoHeader().getPoOid());
        msg.setCreateDate(new Date());
        msg.setProcDate(new Date());
        msg.setSentDate(new Date());
        msg.setOutDate(new Date());
        msg.setOriginalFilename(po.getSupplierFileName());
        msg.setExchangeFilename(po.getSupplierFileName());
        msg.setReportFilename(po.getPdfName());
        msg.setReadStatus(ReadStatus.UNREAD);
        msg.setReadDate(null);
        msg.setBatchOid(null);
        msg.setAllEmptyStringToNull();
        msgTransactionsService.insert(msg);
        
        //update the old po status to "outdated"
        PoHeaderHolder newObj = new PoHeaderHolder();
        BeanUtils.copyProperties(po.getOldPoHeader(), newObj);
        newObj.setDuplicate(true);
        newObj.setPoStatus(PoStatus.OUTDATED);
        poHeaderService.updateByPrimaryKey(po.getOldPoHeader(), newObj);
        
        //insert new po header
        po.getPoHeader().setDuplicate(true);
        po.getPoHeader().setPoStatus(PoStatus.AMENDED);
        poHeaderService.insert(po.getPoHeader());
        
        //insert new po detail 
        for (PoDetailHolder detail : po.getDetails())
        {
            poDetailService.insert(detail);
        }
        
        //insert new po location
        for (PoLocationHolder location : po.getLocations())
        {
            poLocationService.insert(location);
        }
        
        //insert new po location detail
        for (PoLocationDetailHolder locDetail : po.getLocationDetails())
        {
            poLocationDetailService.insert(locDetail);
        }
        
        //insert new po header extended
        if (null != po.getHeaderExtendeds() && !po.getHeaderExtendeds().isEmpty())
        {
            for (PoHeaderExtendedHolder headerEx : po.getHeaderExtendeds())
            {
                poHeaderExtendedService.insert(headerEx);
            }
        }
        
        //insert new po detail extended
        if (null != po.getDetailExtendeds() && !po.getDetailExtendeds().isEmpty())
        {
            for (PoDetailExtendedHolder detailEx : po.getDetailExtendeds())
            {
                poDetailExtendedService.insert(detailEx);
            }
        }
        
        //insert new po location detail extended
        if (null != po.getPoLocDetailExtendeds() && !po.getPoLocDetailExtendeds().isEmpty())
        {
            for (PoLocationDetailExtendedHolder locEx : po.getPoLocDetailExtendeds())
            {
                poLocationDetailExtendedService.insert(locEx);
            }
        }
    }
    
}
