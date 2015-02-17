package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.eai.file.canonical.DnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DocClassHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.DocClassMapper;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.DnDetailService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SubclassService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnServiceImpl extends DBActionServiceDefaultImpl<DnHolder>
        implements DnService, ApplicationContextAware, CoreCommonConstants
{
    @Autowired
    private DnHeaderService dnHeaderService;
    @Autowired
    private DnDetailService dnDetailService;
    @Autowired
    private MsgTransactionsService msgTransactionsService;
    @Autowired 
    private OidService oidService;
    @Autowired
    private BuyerService buyerService;
    @Autowired 
    private DnDocFileHandler dnDocFileHandler;
    @Autowired 
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private MailBoxUtil mboxUtil;
    @Autowired
    private ClassService classService;
    @Autowired
    private SubclassService subclassService;
    @Autowired
    private DocClassMapper docClassMapper;
    @Autowired
    private DocSubclassMapper docSubclassMapper;
    
    private ApplicationContext ctx;
    
    @Override
    public void delete(DnHolder oldObj) throws Exception
    {
        if (oldObj == null || oldObj.getDnHeader() == null)
        {
            throw new IllegalArgumentException();
        }
        DnDetailExHolder dnDetail = new DnDetailExHolder();
        dnDetail.setDnOid(oldObj.getDnHeader().getDnOid());
        dnDetailService.delete(dnDetail);
        dnHeaderService.delete(oldObj.getDnHeader());
        MsgTransactionsHolder oldMsg = msgTransactionsService
                .selectByKey(oldObj.getDnHeader().getDnOid());
        msgTransactionsService.delete(oldMsg);
    }


    @Override
    public void insert(DnHolder newObj) throws Exception
    {
        if (newObj == null || newObj.getDnHeader() == null || newObj.getDnDetail() == null)
        {
            throw new IllegalArgumentException();
        }
        dnHeaderService.insert(newObj.getDnHeader());
        for (DnDetailExHolder dnDetail : newObj.getDnDetail())
        {
            dnDetailService.insert(dnDetail);
        }
    }


    @Override
    public void updateByPrimaryKey(DnHolder oldObj, DnHolder newObj)
            throws Exception
    {
        if (newObj == null || newObj.getDnHeader() == null || newObj.getDnDetail() == null)
        {
            throw new IllegalArgumentException();
        }
        dnHeaderService.updateByPrimaryKey(oldObj.getDnHeader(), newObj.getDnHeader());
        DnDetailExHolder dnDetail = new DnDetailExHolder();
        dnDetail.setDnOid(newObj.getDnHeader().getDnOid());
        dnDetailService.delete(dnDetail);
        for (DnDetailExHolder obj : newObj.getDnDetail())
        {
            dnDetailService.insert(obj);
        }
    }


    @Override
    public void updateByPrimaryKeySelective(DnHolder oldObj, DnHolder newObj)
            throws Exception
    {
        if (newObj == null || newObj.getDnHeader() == null || newObj.getDnDetail() == null)
        {
            throw new IllegalArgumentException();
        }
        dnHeaderService.updateByPrimaryKeySelective(oldObj.getDnHeader(), newObj.getDnHeader());
        DnDetailExHolder dnDetail = new DnDetailExHolder();
        dnDetail.setDnOid(newObj.getDnHeader().getDnOid());
        dnDetailService.delete(dnDetail);
        for (DnDetailExHolder obj : newObj.getDnDetail())
        {
            dnDetailService.insert(obj);
        }
    }

    @Override
    public void insertDnWithMsgTransaction(CommonParameterHolder cp, DnHolder dn,
            MsgTransactionsHolder msg) throws Exception
    {
        if (dn == null || msg == null || dn.getDnHeader() == null || dn.getDnDetail() == null)
        {
             throw new IllegalArgumentException();
        }
        msgTransactionsService.insert(msg);
        if (cp == null)
        {
            this.insert(dn);
        }
        else
        {
            this.getMeBean().auditInsert(cp, dn);
        }
    }


    @Override
    public void updateDnWithMsgTransaction(CommonParameterHolder cp, DnHolder oldDn, DnHolder dn,
            MsgTransactionsHolder msg) throws Exception
    {
        if (dn == null || msg == null || dn.getDnHeader() == null || dn.getDnDetail() == null)
        {
             throw new IllegalArgumentException();
        }
        this.getMeBean().auditUpdateByPrimaryKey(cp, oldDn, dn);
        MsgTransactionsHolder oldMsg = msgTransactionsService.selectByKey(msg.getDocOid());
        msgTransactionsService.updateByPrimaryKeySelective(oldMsg, msg);
    }


    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private DnService getMeBean()
    {
        return ctx.getBean("dnService", DnService.class);
    }


    @Override
    public DnHolder selectDnByKey(BigDecimal dnOid) throws Exception
    {
        if (dnOid == null)
        {
            throw new IllegalArgumentException();
        }
        DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByKey(dnOid);
        List<DnDetailExHolder> dnDetail = dnDetailService.selectDnDetailByKey(dnOid);
        DnHolder dnHolder = new DnHolder();
        dnHolder.setDnHeader(dnHeader);
        dnHolder.setDnDetail(dnDetail);
        return dnHolder;
    }


    @Override
    public void saveClose(CommonParameterHolder cp, DnHolder dn,
            boolean generateNew) throws Exception
    {
        DnHolder oldDn = this.selectDnByKey(dn.getDnHeader().getDnOid());
        
        BuyerHolder buyer = buyerService.selectBuyerByKey(dn.getDnHeader().getBuyerOid());
        
        String fileFormat = buyerMsgSettingService.selectByKey(
                buyer.getBuyerOid(), MsgType.DN.name()).getFileFormat();
        
        if (generateNew)
        {
            dn.getDnHeader().setDnStatus(DnStatus.OUTDATED);
            
            this.getMeBean().auditUpdateByPrimaryKey(cp, oldDn, dn);
            
            BigDecimal dnOid = oidService.getOid();
            dn.getDnHeader().setDnOid(dnOid);
            dn.getDnHeader().setExported(true);
            dn.getDnHeader().setExportedDate(new Date());
            dn.getDnHeader().setDnStatus(DnStatus.AMENDED);
            dn.getDnHeader().setDispute(false);
            dn.getDnHeader().setDisputeBy(null);
            dn.getDnHeader().setDisputeDate(null);
            dn.getDnHeader().setBuyerStatus(DnBuyerStatus.PENDING);
            dn.getDnHeader().setQtyStatus(DnQtyStatus.PENDING);
            dn.getDnHeader().setPriceStatus(DnPriceStatus.PENDING);
            dn.getDnHeader().setClosed(true);
            dn.getDnHeader().setClosedBy(SYSTEM);
            dn.getDnHeader().setClosedDate(new Date());
            
            BigDecimal totalCost = BigDecimal.ZERO;
            
            for (DnDetailHolder detail : dn.getDnDetail())
            {
                detail.setDnOid(dnOid);
                detail.setDisputePrice(null);
                detail.setDisputePriceRemarks(null);
                detail.setDisputeQty(null);
                detail.setDisputeQtyRemarks(null);
                detail.setPriceStatus(DnPriceStatus.PENDING);
                detail.setPriceStatusActionBy(null);
                detail.setPriceStatusActionDate(null);
                detail.setPriceStatusActionRemarks(null);
                detail.setQtyStatus(DnQtyStatus.PENDING);
                detail.setQtyStatusActionBy(null);
                detail.setQtyStatusActionDate(null);
                detail.setQtyStatusActionRemarks(null);
                
                detail.setUnitCost(detail.getConfirmPrice());
                detail.setNetUnitCost(detail.getUnitCost());
                detail.setDebitQty(detail.getConfirmQty());
                detail.setItemCost(detail.getDebitQty().multiply(detail.getUnitCost()));
                totalCost = totalCost.add(detail.getItemCost());
            }
            
            dn.getDnHeader().setTotalCost(totalCost);
            dn.getDnHeader().setTotalVat(totalCost.multiply(dn.getDnHeader().getVatRate()).divide(BigDecimal.valueOf(100)));
            dn.getDnHeader().setTotalCostWithVat(totalCost.add(dn.getDnHeader().getTotalVat()));
            
            List<File> files = new ArrayList<File>();
            
            String rootPath = mboxUtil.getBuyerMailBox(buyer.getMboxId());
            
            String originalFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                    + DOC_FILENAME_DELIMITOR + dn.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dn.getDnHeader().getDnNo()) + "."
                    + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, fileFormat));
            
            msgTransactionsService.insert(dn.getDnHeader().convertToMsgTransaction(originalFilename));
            this.getMeBean().auditInsert(cp, dn);
            
            File originalFile = new File(rootPath, originalFilename);
            files.add(originalFile);
            dnDocFileHandler.createFile(dn, originalFile, fileFormat);
            
            String targetFilename = "CN" + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                    + DOC_FILENAME_DELIMITOR + dn.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(dn.getDnHeader().getDnNo()) + "."
                    + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, fileFormat));
            
            String archivePath = mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                DateUtil.getInstance().getYearAndMonth(new Date()));
            
            File rlt = new File(archivePath);
            
            if (!rlt.isDirectory())
            {
                FileUtil.getInstance().createDir(rlt);
            }
            
            FileUtil.getInstance().copyFile(originalFile, new File(mboxUtil.getBuyerInPath(buyer.getMboxId()), targetFilename), true);
            FileUtil.getInstance().copyFile(originalFile, new File(archivePath, targetFilename), true);
            
            String zipFileName = BatchType.DN.name() + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() + DOC_FILENAME_DELIMITOR + 
                    DateUtil.getInstance().getLogicTimeStampWithoutMsec(new Date()) + ".zip";
            
            String outPath = mboxUtil.getBuyerOutPath(buyer.getMboxId());
            
            GZIPHelper.getInstance().doZip(files, outPath, zipFileName);
            
            
            for (File file : files)
            {
                FileUtil.getInstance().deleleAllFile(file);
            }
        }
        else
        {
            if (dn.getDnHeader().getBuyerStatus().equals(DnBuyerStatus.REJECTED))
            {
                String targetFilename = "CN" + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                        + DOC_FILENAME_DELIMITOR + dn.getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                        + StringUtil.getInstance().convertDocNo(dn.getDnHeader().getDnNo()) + "."
                        + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, fileFormat));
                
                File originalFile = new File(mboxUtil.getBuyerInPath(buyer.getMboxId()), targetFilename);
                
                String archivePath = mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                    DateUtil.getInstance().getYearAndMonth(new Date()));
                
                File rlt = new File(archivePath);
                
                if (!rlt.isDirectory())
                {
                    FileUtil.getInstance().createDir(rlt);
                }
                
                File archiveFile = new File(mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                    DateUtil.getInstance().getYearAndMonth(new Date())), targetFilename);
                
                dnDocFileHandler.createFile(dn, originalFile, fileFormat);
                dnDocFileHandler.createFile(dn, archiveFile, fileFormat);
                dn.getDnHeader().setExported(true);
                dn.getDnHeader().setExportedDate(new Date());
            }
            
            this.getMeBean().auditUpdateByPrimaryKey(cp, oldDn, dn);
        }
        
    }


    @Override
    public byte[] exportExcel(List<BigDecimal> dnOids, boolean storeFlag)
        throws Exception
    {
        if(dnOids == null || dnOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal dnOid : dnOids)
        {
            this.initSheetByDnOid(dnOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }


    @Override
    public byte[] exportSummaryExcel(List<DnSummaryHolder> params,
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
    private void initSheetByDnOid(BigDecimal dnOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        DnHolder dn = this.selectDnByKey(dnOid);
        DnHeaderHolder header = dn.getDnHeader();
        List<DnDetailExHolder> details = dn.getDnDetail();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getDnNo(), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
        sheet.setPageSetup(PageOrientation.LANDSCAPE);
        sheet.getSettings().setFitWidth(1);
        
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
        sheet.addCell(new Label(0, 1, "DN NO", format1));
        sheet.addCell(new Label(1, 1, header.getDnNo(), format1));
        sheet.addCell(new Label(0, 2, "DN DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getDnDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "ORDER NO", format1));
        sheet.addCell(new Label(1, 3, header.getPoNo(), format1));
        sheet.addCell(new Label(0, 4, "INVOICE NO", format1));
        sheet.addCell(new Label(1, 4, header.getInvNo(), format1));
        sheet.addCell(new Label(0, 5, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 5, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 7, "TOTAL NUMBER OF ITEMS", format1));
        if (!storeFlag)
        {
            sheet.addCell(new Label(0, 8, "TOTAL COST INCL. GST ($)", format1));
            sheet.addCell(new Label(1, 8, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getTotalCostWithVat(), 2), format1));
        }
        
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 10, "SEQ", format4));
        sheet.setColumnView(col, 25);
        sheet.addCell(new Label(col+1, 10, "SKU NO.", format4));
        sheet.setColumnView(col+1, 25);
        sheet.addCell(new Label(col+2, 10, "BARCODE", format4));
        sheet.setColumnView(col+2, 25);
        sheet.addCell(new Label(col+3, 10, "DESCRIPTION", format4));
        sheet.setColumnView(col+3, 40);
        sheet.addCell(new Label(col+4, 10, "ARTICEL NO.", format4));
        sheet.setColumnView(col+4, 20);
        sheet.addCell(new Label(col+5, 10, "COLOUR", format4));
        sheet.setColumnView(col+5, 12);
        sheet.addCell(new Label(col+6, 10, "SIZE", format4));
        sheet.setColumnView(col+6, 12);
        sheet.addCell(new Label(col+7, 10, "QTY RETURN", format5));
        sheet.setColumnView(col+7, 20);
        sheet.addCell(new Label(col+8, 10, "QTY UOM", format5));
        sheet.setColumnView(col+8, 10);
      
        if (!storeFlag)
        {
            sheet.addCell(new Label(col+9, 10, "UNIT COST ($)", format5));
            sheet.setColumnView(col+9, 20);
            sheet.addCell(new Label(col+10, 10, "EXTENDED COST ($)", format5));
            sheet.setColumnView(col+10, 20);
            sheet.addCell(new Label(col+11, 10, "UNIT SELL ($)", format5));
            sheet.setColumnView(col+11, 20);
            sheet.addCell(new Label(col+12, 10, "EXTENDED SELL ($)", format5));
            sheet.setColumnView(col+12, 20);
        }
       
        int row = 11;
        for (DnDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 10), format2));
            sheet.addCell(new Label(colIndex+1, row, detail.getBuyerItemCode(), format2));
            sheet.addCell(new Label(colIndex+2, row, detail.getBarcode(), format2));
            sheet.addCell(new Label(colIndex+3, row, detail.getItemDesc(), format2));
            sheet.addCell(new Label(colIndex+4, row, detail.getSupplierItemCode(), format2));
            sheet.addCell(new Label(colIndex+5, row, detail.getColourDesc(), format2));
            sheet.addCell(new Label(colIndex+6, row, detail.getSizeDesc(), format2));
            sheet.addCell(new Label(colIndex+7, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getDebitQty(), 2), format3));
            sheet.addCell(new Label(colIndex+8, row, detail.getOrderUom(), format3));
          
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex+9, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getUnitCost(), 2), format3));
                sheet.addCell(new Label(colIndex+10, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemCost(), 2), format3));
                sheet.addCell(new Label(colIndex+11, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getRetailPrice(), 2), format3));
                sheet.addCell(new Label(colIndex+12, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getItemRetailAmount(), 2), format3));
            }
            row ++;
        }
        
        //set total number of items
        sheet.addCell(new Label(1, 7, String.valueOf(row - 11), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<DnSummaryHolder> params, boolean storeFlag)throws Exception
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
        
        DnSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row, param.getDnNo(), format1));
            sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getDnDate(), "dd-MMM-yy"), format1));
            sheet.addCell(new Label(2, row, param.getBuyerCode(), format1));
            sheet.addCell(new Label(3, row, param.getBuyerName(), format1));
            sheet.addCell(new Label(4, row, param.getSupplierCode(), format1));
            sheet.addCell(new Label(5, row, param.getSupplierName(), format1));
            sheet.addCell(new Label(6, row, param.getRtvNo(), format1));
            sheet.addCell(new Label(7, row, param.getGiNo(), format1));
            sheet.addCell(new Label(8, row, param.getStoreCode(), format1));
            sheet.addCell(new Label(9, row, param.getDnStatus().name(), format1));
            sheet.addCell(new Label(10, row, param.getPriceStatus() == null ? "" : param.getPriceStatus().name(), format1));
            sheet.addCell(new Label(11, row, param.getQtyStatus()== null ? "" : param.getQtyStatus().name(), format1));
            sheet.addCell(new Label(12, row, (param.getClosed() == null || !param.getClosed())? "N" : "Y", format1));
            sheet.addCell(new Label(13, row, param.getPoNo(), format1));
            sheet.addCell(new Label(14, row, param.getInvNo(), format1));
            sheet.addCell(new Label(15, row, param.getDetailCount().toString(), format2));
            sheet.addCell(new Label(16, row, param.getReadStatus().name(), format1));
            sheet.addCell(new Label(17, row, ""));
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("DnSummaryReport_" + (sheetIndex + 1), sheetIndex);
        sheet.getSettings().setZoomFactor(75);
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
        sheet.addCell(new Label(0, 0, "DN NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "DN DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "BUYER CODE", format1));
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "BUYER NAME", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "SUPPLIER CODE", format1));
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "SUPPLIER NAME", format1));
        sheet.setColumnView(6, 20);
        sheet.addCell(new Label(6, 0, "RTV NO", format1));
        sheet.setColumnView(7, 20);
        sheet.addCell(new Label(7, 0, "GI NO", format1));
        sheet.setColumnView(8, 20);
        sheet.addCell(new Label(8, 0, "STORE CODE", format1));
        sheet.setColumnView(9, 20);
        sheet.addCell(new Label(9, 0, "DN STATUS", format1));
        sheet.setColumnView(10, 20);
        sheet.addCell(new Label(10, 0, "PRICE STATUS", format1));
        sheet.setColumnView(11, 20);
        sheet.addCell(new Label(11, 0, "QTY STATUS", format1));
        sheet.setColumnView(12, 13);
        sheet.addCell(new Label(12, 0, "CLOSED", format1));
        sheet.setColumnView(13, 20);
        sheet.addCell(new Label(13, 0, "PO NO", format1));
        sheet.setColumnView(14, 25);
        sheet.addCell(new Label(14, 0, "INVOICE NO", format1));
        sheet.setColumnView(15, 20);
        sheet.addCell(new Label(15, 0, "TOTAL NO OF ITEM", format1));
        sheet.setColumnView(16, 20);
        sheet.addCell(new Label(16, 0, "READ STATUS", format1));
        sheet.addCell(new Label(17, 0, ""));
        
        return sheet;
    }


    @Override
    public void createDnClassInfo(DnHeaderHolder header, List<DnDetailExHolder> details)
        throws Exception
    {
      //insert dnClass
        List<String> classCodes = new ArrayList<String>();
        for(DnDetailExHolder dnDetail : details)
        {
            ClassHolder classHolder = classService.selectClassByItemCodeAndBuyerOid(
                dnDetail.getBuyerItemCode(), header.getBuyerOid());
            if (classHolder == null || classCodes.contains(classHolder.getClassCode()))
            {
                continue;
            }
            classCodes.add(classHolder.getClassCode());
        }
        
        if (!classCodes.isEmpty())
        {
            for (String classCode : classCodes)
            {
                DocClassHolder poClass = new DocClassHolder();
                poClass.setDocOid(header.getDnOid());
                poClass.setClassCode(classCode);
                docClassMapper.insert(poClass);
            }
        }
        
        //insert dnSubClass
        Map<String, SubclassHolder> subclassMap = new HashMap<String, SubclassHolder>();
        for(DnDetailExHolder poDetail : details)
        {
            SubclassHolder subclassHolder = subclassService.selectSubclassExByItemCodeAndBuyerOid(
                poDetail.getBuyerItemCode(), header.getBuyerOid());
            if (subclassHolder == null || subclassMap.containsKey(subclassHolder.getSubclassCode()))
            {
                continue;
            }
            subclassMap.put(subclassHolder.getSubclassCode(), subclassHolder);
        }
        
        if (!subclassMap.isEmpty())
        {
            for (Map.Entry<String, SubclassHolder> entry : subclassMap.entrySet())
            {
                DocSubclassHolder poSubclass = new DocSubclassHolder();
                poSubclass.setDocOid(header.getDnOid());
                poSubclass.setSubclassCode(entry.getKey());
                ClassHolder classHolder = classService.selectByKey(entry.getValue().getClassOid());
                poSubclass.setClassCode(classHolder.getClassCode());
                poSubclass.setAuditFinished(false);
                docSubclassMapper.insert(poSubclass);
            }
        }
    }
}
