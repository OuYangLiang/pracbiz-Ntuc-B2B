package com.pracbiz.b2bportal.core.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RunningNumberHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvHeaderExHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.mapper.InvDetailMapper;
import com.pracbiz.b2bportal.core.mapper.InvHeaderMapper;
import com.pracbiz.b2bportal.core.mapper.RunningNumberMapper;
import com.pracbiz.b2bportal.core.report.DefaultReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.InvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.InvDetailService;
import com.pracbiz.b2bportal.core.service.InvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public class InvoiceServiceImpl extends DBActionServiceDefaultImpl<InvHolder> implements InvoiceService,ApplicationContextAware
{
    private static final String OUT = "out";
    private ApplicationContext context;
    @Autowired transient private RunningNumberMapper numberMapper;
    @Autowired transient private PoHeaderService poHeaderService;
    @Autowired transient private MsgTransactionsService msgTransactionsService;
    @Autowired transient private BuyerMsgSettingReportService buyerMsgSettingReportService;
    @Autowired transient private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired transient private CustomAppConfigHelper appConfig;
    @Autowired transient private InvDetailMapper detailMapper;
    @Autowired transient private InvHeaderMapper headerMapper;
    @Autowired @Qualifier("canonicalInvDocFileHandler") DocFileHandler<?, InvHolder> canonicalInvDocFileHandler;
    @Autowired transient private InvHeaderService invHeaderService;
    @Autowired transient private InvDetailService invDetailService;
    @Autowired transient private InvHeaderExtendedService invHeaderExtendedService;
    @Autowired transient private InvDetailExtendedService invDetailExtendedService;
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private SupplierService supplierService;
    @Autowired transient private MailBoxUtil mboxUtil;
    
    @Override
    public String doRunningNumber(SupplierHolder supplier) throws Exception
    {
        RunningNumberHolder parameter = new RunningNumberHolder();
        parameter.setCompanyOid(supplier.getSupplierOid());
        parameter.setNumberType("I");
        
        List<RunningNumberHolder> list = numberMapper.select(parameter);
        
        RunningNumberHolder oldObject = null;
        if (list != null && !list.isEmpty())
            oldObject = list.get(0);
        
        RunningNumberHolder newObject= new RunningNumberHolder();
        if(oldObject == null)
        {
            newObject.setCompanyOid(supplier.getSupplierOid());
            newObject.setNumberType("I");
            newObject.setMaxNumber(supplier.getStartNumber()+1);
            numberMapper.insert(newObject);
            
            return StringUtils.leftPad(supplier.getStartNumber().toString(), 14, "0");
        }
        else
        {
            newObject.setMaxNumber(oldObject.getMaxNumber()+1);
            newObject.setCompanyOid(oldObject.getCompanyOid());
            newObject.setNumberType(oldObject.getNumberType());
            numberMapper.updateByPrimaryKey(newObject);
            
            return StringUtils.leftPad(oldObject.getMaxNumber().toString(), 14, "0");
        }
    }

    @Override
    public void insert(InvHolder newObj_) throws Exception
    {
        InvHeaderHolder header = newObj_.getHeader();
        header.setAllEmptyStringToNull();
        headerMapper.insert(header);
        
        if (newObj_.getHeaderExtendeds() != null && !newObj_.getHeaderExtendeds().isEmpty())
        {
            for (InvHeaderExtendedHolder headerEx : newObj_.getHeaderExtendeds())
            {
                invHeaderExtendedService.insert(headerEx);
            }
        }
        
        for (InvDetailHolder detail: newObj_.getDetails())
        {
            detail.setAllEmptyStringToNull();
            detailMapper.insert(detail);
        }
        
        if (newObj_.getDetailExtendeds() != null && !newObj_.getDetailExtendeds().isEmpty())
        {
            for (InvDetailExtendedHolder detailEx : newObj_.getDetailExtendeds())
            {
                invDetailExtendedService.insert(detailEx);
            }
        }
    }

    @Override
    public void updateByPrimaryKeySelective(InvHolder oldObj_, InvHolder newObj_)
        throws Exception
    {
        InvHeaderHolder header = newObj_.getHeader();
        header.setAllEmptyStringToNull();
        
        InvHeaderExtendedHolder headerExParam = new InvHeaderExtendedHolder();
        headerExParam.setInvOid(header.getInvOid());
        invHeaderExtendedService.delete(headerExParam);
        
        //update invoice header
        headerMapper.updateByPrimaryKeySelective(header);
        
        if (newObj_.getHeaderExtendeds() != null && !newObj_.getHeaderExtendeds().isEmpty())
        {
            for (InvHeaderExtendedHolder headerEx : newObj_.getHeaderExtendeds())
            {
                 invHeaderExtendedService.insert(headerEx);
            }
        }
        
        //update invoice detail
        InvDetailExtendedHolder detailExParam = new InvDetailExtendedHolder();
        detailExParam.setInvOid(header.getInvOid());
        invDetailExtendedService.delete(detailExParam);
        
        InvDetailHolder param = new InvDetailHolder();
        param.setInvOid(header.getInvOid());
        detailMapper.delete(param);
        
        
        if (newObj_.getDetails() == null || newObj_.getDetails().isEmpty())
        {
            return;
        }
        for (InvDetailHolder detail : newObj_.getDetails())
        {
            detailMapper.insert(detail);
        }
        
        if (newObj_.getDetailExtendeds() != null && !newObj_.getDetailExtendeds().isEmpty())
        {
            for (InvDetailExtendedHolder detailEx : newObj_.getDetailExtendeds())
            {
                invDetailExtendedService.insert(detailEx);
            }
        }
    }

    @Override
    public void updateByPrimaryKey(InvHolder oldObj_, InvHolder newObj_)
        throws Exception
    {
        InvHeaderHolder header = newObj_.getHeader();
        header.setAllEmptyStringToNull();
        
        InvHeaderExtendedHolder headerExParam = new InvHeaderExtendedHolder();
        headerExParam.setInvOid(header.getInvOid());
        invHeaderExtendedService.delete(headerExParam);
        
        //update invoice header and header extended
        headerMapper.updateByPrimaryKey(header);
        
        if (newObj_.getHeaderExtendeds() != null && !newObj_.getHeaderExtendeds().isEmpty())
        {
            for (InvHeaderExtendedHolder headerEx : newObj_.getHeaderExtendeds())
            {
                 invHeaderExtendedService.insert(headerEx);
            }
        }
        
        //update invoice detail and detail extended
        InvDetailExtendedHolder detailExParam = new InvDetailExtendedHolder();
        detailExParam.setInvOid(header.getInvOid());
        invDetailExtendedService.delete(detailExParam);
        
        InvDetailHolder param = new InvDetailHolder();
        param.setInvOid(header.getInvOid());
        detailMapper.delete(param);
        
        if (newObj_.getDetails() == null || newObj_.getDetails().isEmpty())
        {
            return;
        }
        for (InvDetailHolder detail : newObj_.getDetails())
        {
            detailMapper.insert(detail);
        }
        
        if (newObj_.getDetailExtendeds() != null && !newObj_.getDetailExtendeds().isEmpty())
        {
            for (InvDetailExtendedHolder detailEx : newObj_.getDetailExtendeds())
            {
                invDetailExtendedService.insert(detailEx);
            }
        }
       
    }

    @Override
    public void delete(InvHolder oldObj_) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void createInvoice(CommonParameterHolder cp,InvHolder invoice, MsgTransactionsHolder msg,
        PoHeaderHolder poHeader, BuyerHolder buyer, SupplierHolder supplier) throws Exception
    {
        
        List<InvHeaderHolder> invHeaders = invHeaderService.selectInvHeaderByBuyerSupplierPoNoAndStore(invoice.getHeader().getBuyerCode(), 
                invoice.getHeader().getSupplierCode(), invoice.getHeader().getPoNo(), invoice.getHeader().getShipToCode());
        
        if (invHeaders != null && !invHeaders.isEmpty())
        {
            for (InvHeaderHolder invHeader : invHeaders)
            {
                invHeader.setInvStatus(InvStatus.VOID_OUTDATED);
                invHeaderService.updateByPrimaryKeySelective(null, invHeader);
            }
            
            Collections.sort(invHeaders, new Comparator<InvHeaderHolder>(){
                @Override
                public int compare(InvHeaderHolder m1, InvHeaderHolder m2)
                {
                    return m1.getInvDate().before(m2.getInvDate()) ? 1 : -1;
                }
            });
            
            invoice.getHeader().setOldInvNo(invHeaders.get(0).getInvNo());
        }
        
        
        this.insertInv(cp, invoice, msg, poHeader);
        
        String mailboxRoot =  mboxUtil.getSupplierMboxRoot();//appConfig.getSupplierMailboxRootPath();
        String suppMboxId = supplier.getMboxId();

        this.generateInv(invoice, buyer, supplier, msg, mailboxRoot, suppMboxId);
    }

    @Override
    public void setApplicationContext(ApplicationContext context)
        throws BeansException
    {
        this.context = context;
    }
    
    public InvoiceService getMeBean()
    {
        return context.getBean("invoiceService",InvoiceService.class);
    }
    
    
    public byte[] generatePdf(InvHolder invoice, BuyerHolder buyer,
        SupplierHolder supplier, MsgTransactionsHolder msg, BuyerMsgSettingReportHolder setting) throws Exception
    {
          InvHeaderHolder header = invoice.getHeader();
          
          String template = null;
          
          if (setting.getCustomizedReport())
          {
              template = appConfig.getCustomizedReport(
                  header.getBuyerCode(), MsgType.INV.name(), setting.getSubType(),
                  setting.getReportTemplate());
          }
          else
          {
              template = appConfig.getStandardReport(MsgType.INV.name(), setting.getSubType(), setting.getReportTemplate());
          }
        
          @SuppressWarnings("unchecked")
          DefaultReportEngine<InvHolder> engine = context.getBean(template, DefaultReportEngine.class);
          
          ReportEngineParameter<InvHolder>  parameter = new ReportEngineParameter<InvHolder>();
          parameter.setBuyer(buyer);
          parameter.setData(invoice);
          parameter.setMsgTransactions(msg);
          parameter.setSupplier(supplier);
          return engine.generateReport(parameter, DefaultReportEngine.PDF_TYPE_STANDARD);//0 means standard pdf 
    }


    @Override
    public void createAndSentInvoice(CommonParameterHolder cp,
        InvHolder invoice, MsgTransactionsHolder msg, PoHeaderHolder poHeader,
        BuyerHolder buyer, SupplierHolder supplier) throws Exception
    {
        List<InvHeaderHolder> invHeaders = invHeaderService.selectInvHeaderByBuyerSupplierPoNoAndStore(invoice.getHeader().getBuyerCode(), 
                invoice.getHeader().getSupplierCode(), invoice.getHeader().getPoNo(), invoice.getHeader().getShipToCode());
        
        if (invHeaders != null && !invHeaders.isEmpty())
        {
            for (InvHeaderHolder invHeader : invHeaders)
            {
                invHeader.setInvStatus(InvStatus.VOID_OUTDATED);
                invHeaderService.updateByPrimaryKeySelective(null, invHeader);
            }
            
            Collections.sort(invHeaders, new Comparator<InvHeaderHolder>(){
                @Override
                public int compare(InvHeaderHolder m1, InvHeaderHolder m2)
                {
                    return m1.getInvDate().before(m2.getInvDate()) ? 1 : -1;
                }
            });
            
            invoice.getHeader().setOldInvNo(invHeaders.get(0).getInvNo());
            
            if (null != invoice.getHeader().getOldInvNo())
            {
                invoice.getHeader().setInvRemarks((invoice.getHeader().getInvRemarks() == null || invoice.getHeader().getInvRemarks().isEmpty())
                    ? invoice.getHeader().getOldInvNo() 
                    : invoice.getHeader().getInvRemarks()+"-"+invoice.getHeader().getOldInvNo());
            }
        }
        SupplierMsgSettingHolder suppSetting = supplierMsgSettingService
            .selectByKey(supplier.getSupplierOid(), MsgType.INV.name());
        String expectedFormat = suppSetting.getFileFormat();
        String mailboxRoot = mboxUtil.getSupplierMboxRoot();//appConfig.getSupplierMailboxRootPath();
        String suppMboxId = supplier.getMboxId();
        
        String fileName = canonicalInvDocFileHandler.getTargetFilename(invoice, expectedFormat);
        msg.setOriginalFilename(fileName);
        
        this.insertInv(cp, invoice, msg, poHeader);
        
        this.generateInv(invoice, buyer, supplier, msg, mailboxRoot, suppMboxId);
        
        // create invoice file
        if (!FileUtil.getInstance().isSourcePathExist(new File(mboxUtil.getSupplierOutPath(supplier.getMboxId()))))
        {
            FileUtil.getInstance().createDir(new File(mboxUtil.getSupplierOutPath(supplier.getMboxId())));
        }
        String filePath = mboxUtil.getSupplierOutPath(supplier.getMboxId()) + File.separator + fileName;
        File targetFile = new File(filePath);
        canonicalInvDocFileHandler.createFile(invoice, targetFile, expectedFormat);
    }
    
    @Override
    public void sentInvoice(CommonParameterHolder cp, InvHolder invoice,
        MsgTransactionsHolder msg, SupplierHolder supplier) throws Exception
    {
        SupplierMsgSettingHolder suppSetting = supplierMsgSettingService
        .selectByKey(supplier.getSupplierOid(), MsgType.INV.name());
        String expectedFormat = suppSetting.getFileFormat();
        
        String fileName = canonicalInvDocFileHandler.getTargetFilename(invoice, expectedFormat);
        MsgTransactionsHolder newObj = new MsgTransactionsHolder();
        BeanUtils.copyProperties(msg, newObj);
        newObj.setOriginalFilename(fileName);
        newObj.setAllEmptyStringToNull();
        msgTransactionsService.auditUpdateByPrimaryKeySelective(cp, msg, newObj);
        
        InvHolder newInvoice = new InvHolder();
        if (null != invoice.getHeader().getOldInvNo())
        {
            invoice.getHeader().setInvRemarks((invoice.getHeader().getInvRemarks() == null || invoice.getHeader().getInvRemarks().isEmpty())
                ? invoice.getHeader().getOldInvNo() 
                : invoice.getHeader().getInvRemarks()+"-"+invoice.getHeader().getOldInvNo());
        }
        BeanUtils.copyProperties(invoice, newInvoice);
        this.getMeBean().auditUpdateByPrimaryKeySelective(cp, invoice , newInvoice);
        
        String mailboxRoot =  mboxUtil.getSupplierMboxRoot();//appConfig.getSupplierMailboxRootPath();
        String suppMboxId = supplier.getMboxId();
        BuyerHolder buyer = buyerService.selectBuyerByKey(invoice.getHeader().getBuyerOid());
        this.generateInv(invoice, buyer, supplier, msg, mailboxRoot, suppMboxId);
        
        // create invoice file
        
        String filePath = mboxUtil.getSupplierOutPath(supplier.getMboxId()) + File.separator + fileName;
        File targetFile = new File(filePath);
        canonicalInvDocFileHandler.createFile(invoice, targetFile, expectedFormat);
    }
    
    private void generateInv(InvHolder invoice,BuyerHolder buyer,SupplierHolder supplier, MsgTransactionsHolder msg,String mailboxRoot,String suppMboxId) throws Exception
    {
        InvHeaderHolder header = invoice.getHeader();
        BuyerMsgSettingReportHolder buyerSetting = buyerMsgSettingReportService.selectByKey(
            buyer.getBuyerOid(), MsgType.INV.name(), header.getInvType().name());
        
        
        byte[] content = this.generatePdf(invoice, buyer, supplier, msg, buyerSetting);
        String docFilePath = mailboxRoot + File.separator + suppMboxId
            + File.separator + "doc" + File.separator + OUT + File.separator
            + DateUtil.getInstance().getYearAndMonth(header.getInvDate());
        
        if (!new File(docFilePath).exists())
        {
            FileUtil.getInstance().createDir(new File(docFilePath));
        }
            
        String rptFilePath = docFilePath + File.separator + header.computePdfFilename();
        
        FileUtil.getInstance().writeByteToDisk(content,rptFilePath);
    }
    
    
    private void insertInv(CommonParameterHolder cp,
        InvHolder invoice, MsgTransactionsHolder msg, PoHeaderHolder poHeader) throws Exception
    {
        InvHeaderExHolder header = (InvHeaderExHolder)invoice.getHeader();
        PoHeaderHolder newObj = new PoHeaderHolder();
        BeanUtils.copyProperties(poHeader, newObj);
        newObj.setPoStatus(header.getPoStatus());
        newObj.setActionDate(new Date());
        newObj.setAllEmptyStringToNull();
        poHeaderService.auditUpdateByPrimaryKey(cp, poHeader, newObj);
        
        msg.setAllEmptyStringToNull();
        msgTransactionsService.auditInsert(cp, msg);
        this.getMeBean().auditInsert(cp, invoice);
    }

    @Override
    public InvHolder selectInvoiceByKey(BigDecimal invOid) throws Exception
    {
        InvHolder invHolder = new InvHolder();
        invHolder.setHeader(invHeaderService.selectInvHeaderByKey(invOid));
        invHolder.setDetails(invDetailService.selectInvDetailByKey(invOid));
        invHolder.setHeaderExtendeds(invHeaderExtendedService.selectHeaderExtendedByKey(invOid));
        invHolder.setDetailExtendeds(invDetailExtendedService.selectDetailExtendedByKey(invOid));
        return invHolder;
    }

    @Override
    public void editInvoice(CommonParameterHolder cp, InvHolder invoice)
            throws Exception
    {
        this.getMeBean().auditUpdateByPrimaryKey(cp, this.selectInvoiceByKey(invoice.getHeader().getInvOid()), invoice);
       //update msg transaction for report file name.
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(invoice.getHeader().getInvOid());
        msg.setReportFilename(invoice.getHeader().computePdfFilename());
        msgTransactionsService.updateByPrimaryKeySelective(null, msg);
        //delete old report file and generate new one.
        BuyerHolder buyer = buyerService.selectBuyerByKey(invoice.getHeader().getBuyerOid());
        SupplierHolder supplier = supplierService.selectSupplierByKey(invoice.getHeader().getSupplierOid());
        String mailboxRoot =  mboxUtil.getSupplierMboxRoot();//appConfig.getSupplierMailboxRootPath();
        String suppMboxId = supplier.getMboxId();
        File docFile = new File(mailboxRoot + File.separator + suppMboxId
        + File.separator + "doc" + File.separator + OUT + File.separator
        + DateUtil.getInstance().getYearAndMonth(invoice.getHeader().getInvDate()) + File.separator
        + msg.getReportFilename());
        if (docFile.exists() && !docFile.delete())
        {
            throw new IOException("failed to delete file ["+docFile.getName()+"]");
        }
        this.generateInv(invoice, buyer, supplier, msg, mailboxRoot, suppMboxId);
    }

    @Override
    public void editAndSendInvoice(CommonParameterHolder cp, InvHolder invoice)
            throws Exception
    {
        if (null != invoice.getHeader().getOldInvNo())
        {
            invoice.getHeader().setInvRemarks((invoice.getHeader().getInvRemarks() == null || invoice.getHeader().getInvRemarks().isEmpty())
                ? invoice.getHeader().getOldInvNo() 
                : invoice.getHeader().getInvRemarks()+"-"+invoice.getHeader().getOldInvNo());
        }
        invoice.getHeader().setInvStatus(InvStatus.SUBMIT);
        this.getMeBean().auditUpdateByPrimaryKey(cp, this.selectInvoiceByKey(invoice.getHeader().getInvOid()), invoice);
        BuyerHolder buyer = buyerService.selectBuyerByKey(invoice.getHeader().getBuyerOid());
        SupplierHolder supplier = supplierService.selectSupplierByKey(invoice.getHeader().getSupplierOid());
        SupplierMsgSettingHolder suppSetting = supplierMsgSettingService
                .selectByKey(supplier.getSupplierOid(), MsgType.INV.name());
        String expectedFormat = suppSetting.getFileFormat();
        String mailboxRoot = mboxUtil.getSupplierMboxRoot();//appConfig.getSupplierMailboxRootPath();
        String suppMboxId = supplier.getMboxId();

        String fileName = canonicalInvDocFileHandler.getTargetFilename(invoice,
                expectedFormat);
        MsgTransactionsHolder msg = msgTransactionsService.selectByKey(invoice.getHeader().getInvOid());
        msg.setReportFilename(invoice.getHeader().computePdfFilename());
        msg.setOriginalFilename(fileName);
        msgTransactionsService.updateByPrimaryKeySelective(null, msg);
       //delete old report file and generate new one.
        File docFile = new File(mailboxRoot + File.separator + suppMboxId
        + File.separator + "doc" + File.separator + OUT + File.separator
        + DateUtil.getInstance().getYearAndMonth(invoice.getHeader().getInvDate()) + File.separator
        + msg.getReportFilename());
        if (docFile.exists() && !docFile.delete())
        {
            throw new IOException("failed to delete file ["+docFile.getName()+"]");
        }
        this.generateInv(invoice, buyer, supplier, msg, mailboxRoot, suppMboxId);

        // create invoice file
        String filePath = mailboxRoot + File.separator + suppMboxId
                + File.separator + OUT + File.separator + fileName;
        File targetFile = new File(filePath);
        canonicalInvDocFileHandler.createFile(invoice, targetFile,
                expectedFormat);
    }

    
    @Override
    public byte[] exportExcel(List<BigDecimal> invOids, boolean storeFlag)
        throws Exception
    {
        if(invOids == null || invOids.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WritableWorkbook wwb = Workbook.createWorkbook(out);
        int sheetIndex = 0;
        for(BigDecimal invOid : invOids)
        {
            this.initSheetByInvOid(invOid, wwb, sheetIndex, storeFlag);
            sheetIndex++;
        }
        wwb.write();
        wwb.close();
        return out.toByteArray();
    }

    
    @Override
    public byte[] exportSummaryExcel(List<InvSummaryHolder> params,
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
    private void initSheetByInvOid(BigDecimal invOid, WritableWorkbook wwb, int sheetIndex, boolean storeFlag) throws Exception
    {
        InvHolder inv = this.selectInvoiceByKey(invOid);
        InvHeaderHolder header = inv.getHeader();
        List<InvDetailHolder> details = inv.getDetails();
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        
        WritableSheet sheet = wwb.createSheet(header.getInvNo(), sheetIndex);
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
        sheet.addCell(new Label(0, 1, "INVOICE NO", format1));
        sheet.addCell(new Label(1, 1, header.getInvNo(), format1));
        sheet.addCell(new Label(0, 2, "INVOICE DATE", format1));
        sheet.addCell(new Label(1, 2, DateUtil.getInstance().convertDateToString(header.getInvDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 3, "DELIVERY DATE", format1));
        sheet.addCell(new Label(1, 3, DateUtil.getInstance().convertDateToString(header.getDeliveryDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 4, "PO NO", format1));
        sheet.addCell(new Label(1, 4, header.getPoNo(), format1));
        sheet.addCell(new Label(0, 5, "PO DATE", format1));
        sheet.addCell(new Label(1, 5, DateUtil.getInstance().convertDateToString(header.getPoDate(), "dd-MMM-yy"), format1));
        sheet.addCell(new Label(0, 6, "SUPPLIER NAME", format1));
        sheet.addCell(new Label(1, 6, header.getSupplierName(), format1));
        sheet.addCell(new Label(0, 7, "SUPPLIER CODE", format1));
        sheet.addCell(new Label(1, 7, header.getSupplierCode(), format1));
        sheet.addCell(new Label(0, 8, "TOTAL NUMBER OF ITEMS", format1));
        
        if (!storeFlag)
        {
            sheet.addCell(new Label(0, 9, "TRADE DISCOUNT AMOUNT", format1));
            sheet.addCell(new Label(1, 9, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getAdditionalDiscountAmount(), 2), format1));
            sheet.addCell(new Label(0, 10, "TOTAL BEFORE GST", format1));
            sheet.addCell(new Label(1, 10, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getInvAmountNoVat().subtract(header.getAdditionalDiscountAmount()), 2), format1));
            sheet.addCell(new Label(0, 11, "GST AMOUNT", format1));
            sheet.addCell(new Label(1, 11, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getVatAmount(), 2), format1));
            sheet.addCell(new Label(0, 12, "TOTAL BEFORE CASH DISCOUNT", format1));
            sheet.addCell(new Label(1, 12, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getInvAmountWithVat(), 2), format1));
            sheet.addCell(new Label(0, 13, "CASH DISCOUNT AMOUNT", format1));
            sheet.addCell(new Label(1, 13, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getCashDiscountAmount(), 2), format1));
            sheet.addCell(new Label(0, 14, "PLEASE PAY", format1));
            sheet.addCell(new Label(1, 14, decimalUtil.convertBigDecimalToStringIntegerWithScale(header.getInvAmountWithVat().subtract(header.getCashDiscountAmount()), 2), format1));
        }
        
        int col = 0;
        sheet.setRowView(10, 510);
        sheet.addCell(new Label(col, 16, "SEQ", format4));
        sheet.setColumnView(col, 25);
        sheet.addCell(new Label(col+1, 16, "SKU NO.", format4));
        sheet.setColumnView(col+1, 25);
        sheet.addCell(new Label(col+2, 16, "BARCODE", format4));
        sheet.setColumnView(col+2, 20);
        sheet.addCell(new Label(col+3, 16, "DESCRIPTION", format4));
        sheet.setColumnView(col+3, 35);
        sheet.addCell(new Label(col+4, 16, "ARTICLE NO.", format4));
        sheet.setColumnView(col+4, 20);
        sheet.addCell(new Label(col+5, 16, "COLOUR", format4));
        sheet.setColumnView(col+5, 12);
        sheet.addCell(new Label(col+6, 16, "SIZE", format4));
        sheet.setColumnView(col+6, 12);
        sheet.addCell(new Label(col+7, 16, "QTY", format4));
        sheet.setColumnView(col+7, 15);
        sheet.addCell(new Label(col+8, 16, "FOC QTY", format4));
        sheet.setColumnView(col+8, 15);
        sheet.addCell(new Label(col+9, 16, "UOM", format4));
        sheet.setColumnView(col+9, 12);
   
        if (!storeFlag)
        {
            sheet.addCell(new Label(col+10, 16, "UNIT COST ($)", format5));
            sheet.setColumnView(col+10, 20);
            sheet.addCell(new Label(col+11, 16, "DISCOUNT PERCENT (%)", format5));
            sheet.setColumnView(col+11, 20);
            sheet.addCell(new Label(col+12, 16, "TRADE DISCOUNT ($)", format5));
            sheet.setColumnView(col+12, 25);
            sheet.addCell(new Label(col+13, 16, "TOTAL ($)", format5));
            sheet.setColumnView(col+13, 12);
        }
       
        int row = 17;
        for (InvDetailHolder detail : details)
        {
            int colIndex = 0;
            sheet.addCell(new Label(colIndex, row, String.valueOf(row - 16), format2));
            sheet.addCell(new Label(colIndex+1, row, detail.getBuyerItemCode(), format2));
            sheet.addCell(new Label(colIndex+2, row, detail.getBarcode(), format2));
            sheet.addCell(new Label(colIndex+3, row, detail.getItemDesc(), format2));
            sheet.addCell(new Label(colIndex+4, row, detail.getSupplierItemCode(), format2));
            sheet.addCell(new Label(colIndex+5, row, detail.getColourDesc(), format2));
            sheet.addCell(new Label(colIndex+6, row, detail.getSizeDesc(), format2));
            sheet.addCell(new Label(colIndex+7, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getInvQty(), 2), format3));
            sheet.addCell(new Label(colIndex+8, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getFocQty(), 2), format3));
            sheet.addCell(new Label(colIndex+9, row, detail.getInvUom(), format5));
       
            if (!storeFlag)
            {
                sheet.addCell(new Label(colIndex+10, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getUnitPrice(), 2), format3));
                sheet.addCell(new Label(colIndex+11, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getDiscountPercent(), 2), format3));
                sheet.addCell(new Label(colIndex+12, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getDiscountAmount(), 2), format3));
                sheet.addCell(new Label(colIndex+13, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(detail.getNetAmount(), 2), format3));
            }
            
            row ++;
        }
        
        sheet.addCell(new Label(0, row + 2, "THIS IS A COMPUTER-GENERATED INVOICE. NO SIGNATURE IS REQUIRED.", format1));
        sheet.mergeCells(0, row + 2, 2, row + 2);
        //set total number of items
        sheet.addCell(new Label(1, 8, String.valueOf(row - 17), format1));
    }
    
    
    private void initSheetContent(WritableWorkbook wwb, List<InvSummaryHolder> params, boolean storeFlag)throws Exception
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
        
        BigDecimalUtil decimalUtil = BigDecimalUtil.getInstance();
        InvSummaryHolder param = null;
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
            sheet.addCell(new Label(0, row, param.getInvNo(), format1));
            sheet.addCell(new Label(1, row, DateUtil.getInstance().convertDateToString(param.getInvDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(2, row, param.getPoNo(), format1));
            sheet.addCell(new Label(3, row, DateUtil.getInstance().convertDateToString(param.getPoDate(), "dd-MM-yyyy"), format1));
            sheet.addCell(new Label(4, row, param.getInvType(), format1));
            sheet.addCell(new Label(5, row, param.getShipToCode(), format1));
            sheet.addCell(new Label(6, row, param.getInvRemarks(), format1));
            sheet.addCell(new Label(7, row, param.getMatchingStatus() == null ? "" : param.getMatchingStatus().name(), format1));
            
            int col = 8;
            if (!storeFlag)
            {
                sheet.addCell(new Label(col, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(param.getVatAmount(), 2), format1));
                sheet.addCell(new Label(col + 1, row, decimalUtil.convertBigDecimalToStringIntegerWithScale(param.getInvAmountWithVat(), 2), format1));
                col = col + 2 ;
            }
            sheet.addCell(new Label(col, row, param.getDetailCount().toString(), format2));
            sheet.addCell(new Label(col + 1, row, param.getReadStatus().name(), format1));
            sheet.addCell(new Label((col + 2), row, ""));
            
            row ++;
        }
    }
    
    
    private WritableSheet initSheetHeader(WritableWorkbook wwb, int sheetIndex, boolean storeFlag)throws Exception
    {
        WritableSheet sheet = wwb.createSheet("InvSummaryReport_" + (sheetIndex + 1), sheetIndex);
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
        sheet.addCell(new Label(0, 0, "INVOICE NO", format1));
        sheet.setColumnView(1, 15);
        sheet.addCell(new Label(1, 0, "INVOICE DATE", format1));
        sheet.setColumnView(2, 20);
        sheet.addCell(new Label(2, 0, "PO NO", format1));
        sheet.setColumnView(3, 20);
        sheet.addCell(new Label(3, 0, "PO DATE", format1));
        sheet.setColumnView(4, 20);
        sheet.addCell(new Label(4, 0, "INVOICE TYPE", format1));
        sheet.setColumnView(5, 20);
        sheet.addCell(new Label(5, 0, "STORE CODE", format1));
        sheet.setColumnView(6, 20);
        sheet.addCell(new Label(6, 0, "INVOICE REMARKS", format1));
        sheet.setColumnView(7, 20);
        sheet.addCell(new Label(7, 0, "MATCHING STATUS", format1));
        
        int col = 8;
        if (!storeFlag)
        {
            sheet.setColumnView(col, 15);
            sheet.addCell(new Label(col, 0, "GST AMOUNT", format2));
            sheet.setColumnView(col + 1, 20);
            sheet.addCell(new Label(col + 1, 0, "INVOICE AMOUNT WITH GST", format2));
            col = col + 2 ;
        }
        sheet.setColumnView(col, 20);
        sheet.addCell(new Label(col, 0, "TOTAL NO OF ITEM", format2));
        sheet.setColumnView(col + 1, 15);
        sheet.addCell(new Label(col + 1, 0, "READ STATUS", format1));
        sheet.addCell(new Label((col + 2), 0, ""));
        
        return sheet;
    }
   
}
