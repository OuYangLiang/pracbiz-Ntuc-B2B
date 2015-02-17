package com.pracbiz.b2bportal.customized.unity.eai.backend;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.eai.backend.BaseJob;
import com.pracbiz.b2bportal.core.eai.backend.SupplierMasterImportJob;
import com.pracbiz.b2bportal.core.eai.file.canonical.DnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnDetailService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.InvDetailService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.service.RunningNumberService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;
import com.pracbiz.b2bportal.customized.unity.eai.backend.job.util.CostDNGenerator;
import com.pracbiz.b2bportal.customized.unity.eai.backend.job.util.DnGenerator;
import com.pracbiz.b2bportal.customized.unity.eai.backend.job.util.StockDNGenerator;

public class DNGeneratorJob extends BaseJob implements 
        CoreCommonConstants
{
    private static final String RULE_FUNGROUP_DN = "Dn";
    private static final String RULE_FUNID_BACKEND = "Backend";
    private static final Logger log = LoggerFactory.getLogger(DNGeneratorJob.class);
    private static final String ID = "[DNGeneratorJob]";

    private MailBoxUtil mboxUtil;
    private BuyerService buyerService;
    private GrnHeaderService grnHeaderService;
    private GrnDetailService grnDetailService;
    private InvHeaderService invHeaderService;
    private InvDetailService invDetailService;
    private BusinessRuleService businessRuleService;
    private PoService poService;
    private DnService dnService;
    private RunningNumberService runningNumberService;
    private OidService oidService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private StandardEmailSender standardEmailSender;
    private DnHeaderService dnHeaderService;
    private DnDocFileHandler dnDocFileHandler;


    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        dnHeaderService = this.getBean("dnHeaderService", DnHeaderService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        dnDocFileHandler = this.getBean("canonicalDnDocFileHandler", DnDocFileHandler.class);
        grnHeaderService = this.getBean("grnHeaderService", GrnHeaderService.class);
        grnDetailService = this.getBean("grnDetailService", GrnDetailService.class);
        invHeaderService = this.getBean("invHeaderService", InvHeaderService.class);
        invDetailService = this.getBean("invDetailService", InvDetailService.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        poService = this.getBean("poService", PoService.class);
        dnService = this.getBean("dnService", DnService.class);
        runningNumberService = this.getBean("runningNumberService", RunningNumberService.class);
        oidService = this.getBean("oidService", OidService.class);
    }


    @Override
    protected void process()
    {   
        try
        {
            synchronized(SupplierMasterImportJob.lock)
            {
                while (SupplierMasterImportJob.isAnyJobRunning)
                {
                    try
                    {
                        SupplierMasterImportJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                SupplierMasterImportJob.isAnyJobRunning = true;
            }
            
            realProcess();
        }
        finally
        {
            synchronized (SupplierMasterImportJob.lock)
            {
                SupplierMasterImportJob.isAnyJobRunning = false;

                SupplierMasterImportJob.lock.notifyAll();
            }
        }
    
    }

    
    protected void realProcess()
    {
        log.info(":::: Start to process.");
        try
        {
            List<BuyerHolder> buyerList = buyerService.selectActiveBuyers();
            if (buyerList == null || buyerList.isEmpty())
            {
                log.info(":::: No buyer exist in system.");
                return;
            }
            
            for (BuyerHolder buyer : buyerList)
            {
                
                
                int matchingMaxBufferDays = businessRuleService.selectGlobalDnGeneratingJobMatchingMaxBuffingDays(buyer.getBuyerOid());
                
                int matchingMinBufferDays = businessRuleService.selectGlobalDnGeneratingJobMatchingMinBuffingDays(buyer.getBuyerOid());
                
                List<InvHeaderHolder> invHeaders = invHeaderService.selectInvToGenerateDn(buyer.getBuyerOid(), matchingMaxBufferDays, matchingMinBufferDays);
                
                if (invHeaders == null || invHeaders.isEmpty())
                {
                    log.info(":::: No eligible invoice exist in system for buyer [" + buyer.getBuyerCode() + "] to generate dn.");
                    continue;
                }
                
                log.info("find " + invHeaders.size() + " to generate dn for buyer [" + buyer.getBuyerCode() + "] to generate dn");
                
                
                String expectedFormat = buyerMsgSettingService.selectByKey(
                        buyer.getBuyerOid(), MsgType.DN.name()).getFileFormat();
                
                String out = mboxUtil.getBuyerOutPath(buyer.getMboxId()) + PS;
                
                boolean autoSendStockDn = businessRuleService.isAutoSendStockDn(buyer.getBuyerOid());
                
                boolean autoSendCostDn = businessRuleService.isAutoSendCostDn(buyer.getBuyerOid());
                
                String zipName = BatchType.DN.name() + "_" + buyer.getBuyerCode() + "_" 
                        + DateUtil.getInstance().getCurrentLogicTimeStamp()
                        + ".zip";
                
                List<File> dnFiles = new ArrayList<File>();
                
                for (InvHeaderHolder invHeader : invHeaders)
                {
                    List<GrnHeaderHolder> grnHeaders = grnHeaderService.selectGrnHeadersByPoNoAndStoreCode( buyer.getBuyerOid(),
                                    invHeader.getSupplierCode(), invHeader.getPoNo(), invHeader.getShipToCode());
                    GrnHolder grnHolder = mergerGrnList(grnHeaders);
                    if (grnHolder == null)
                    {
                        log.info(":::: No eligible grn exist in system for invoice [" + invHeader.getInvNo() + "].");
                        continue;
                    }
                    InvHolder invHolder = new InvHolder();
                    invHolder.setHeader(invHeader);
                    invHolder.setDetails(invDetailService
                            .selectInvDetailByKey(invHeader.getInvOid()));
                    PoHolder poHolder = poService.selectPoByKey(invHeader.getPoOid());
                    DnGenerator stockGenerator = new StockDNGenerator(businessRuleService);
                    stockGenerator.generateDn(poHolder, invHolder, grnHolder,
                            oidService.getOid(), expectedFormat,
                            dnDocFileHandler, out);
                    DnGenerator costGenerator = new CostDNGenerator(businessRuleService);
                    costGenerator.generateDn(poHolder, invHolder, grnHolder,
                            oidService.getOid(), expectedFormat,
                            dnDocFileHandler, out);
                    if ((stockGenerator.isGenerateFlag() && costGenerator
                            .isGenerateFlag())
                            || !(stockGenerator.isGenerateFlag() || costGenerator
                                    .isGenerateFlag()))
                    {
                        log.info(":::: Need not generate any dn for invoice [" + invHeader.getInvNo() + "]." 
                                + stockGenerator.isGenerateFlag() + "-" + costGenerator.isGenerateFlag());
                        continue;
                    }
                    
                    
                    DnGenerator dnGenerator = null;
                    boolean autoSend = false;
                    if (stockGenerator.isGenerateFlag() && stockGenerator.autoGenerateDn(buyer.getBuyerOid()))
                    {
                        autoSend = autoSendStockDn;
                        dnGenerator = stockGenerator;
                    }
                    else if (costGenerator.isGenerateFlag() && costGenerator.autoGenerateDn(buyer.getBuyerOid()))
                    {
                        autoSend = autoSendCostDn;
                        dnGenerator = costGenerator;
                    }
                    
                    if (dnGenerator != null)
                    {
                        DnHeaderHolder dnHeader = dnGenerator.getDnHolder().getDnHeader();
                        dnHeader.setStoreCode(invHeader.getShipToCode());
                        dnHeader.setStoreName(invHeader.getShipToName());
                        dnHeader.setStoreAddr1(invHeader.getShipToAddr1());
                        dnHeader.setStoreAddr2(invHeader.getShipToAddr2());
                        dnHeader.setStoreAddr3(invHeader.getShipToAddr3());
                        dnHeader.setStoreAddr4(invHeader.getShipToAddr4());
                        dnHeader.setStoreCity(invHeader.getShipToCity());
                        dnHeader.setStoreState(invHeader.getShipToState());
                        dnHeader.setStoreCtryCode(invHeader.getShipToCtryCode());
                        dnHeader.setStorePostalCode(invHeader.getShipToPostalCode());
                        
                        File dnFile = generateDnFile(dnGenerator, buyer, expectedFormat, out, autoSend);;
                        
                        if (dnFile != null)
                        {
                            log.info("dn file [" + dnFile.getName() + "] will be generated.");
                            dnFiles.add(dnFile);
                        }
                    }
                }
                
                if (dnFiles.isEmpty())
                {
                    continue;
                }
                
                GZIPHelper.getInstance().doZip(dnFiles, out, zipName);
                
                for (File file : dnFiles)
                {
                    FileUtil.getInstance().deleleAllFile(file);
                }
            }
            log.info(":::: process successfully.");
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private File generateDnFile(DnGenerator dnGenerator, BuyerHolder buyer, String expectedFormat, 
            String out, /*String zipName, */boolean autoSend) throws Exception
    {
        String dnNo = initDnNo(buyer.getBuyerOid(), dnGenerator.getDnHolder().getDnHeader().getStoreCode());
        dnGenerator.getDnHolder().getDnHeader().setDnNo(dnNo);
        String originalFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR + buyer.getBuyerCode() 
                + DOC_FILENAME_DELIMITOR + dnGenerator.getDnHolder().getDnHeader().getSupplierCode() + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(dnGenerator.getDnHolder().getDnHeader().getDnNo()) + "."
                + FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dnGenerator.getDnHolder(), expectedFormat));
        
        dnGenerator.setTargetFile(new File(out + originalFilename));
        
        if (autoSend)
        {
            dnGenerator.getDnHolder().getDnHeader().setMarkSentToSupplier(true);
            dnGenerator.getDnHolder().getDnHeader().setSentToSupplier(true);
            dnGenerator.getDnHolder().getDnHeader().setExported(true);
            dnGenerator.getDnHolder().getDnHeader().setExportedDate(new Date());
        }
        else
        {
            dnGenerator.getDnHolder().getDnHeader().setMarkSentToSupplier(false);
            dnGenerator.getDnHolder().getDnHeader().setSentToSupplier(false);
        }
        
        this.initDnStatus(dnGenerator);
        
        MsgTransactionsHolder msg = dnGenerator.getDnHolder().getDnHeader().convertToMsgTransaction(originalFilename);
        msg.setGeneratedOnPortal(true);
        dnService.insertDnWithMsgTransaction(null,
                dnGenerator.getDnHolder(),msg);
        
        dnService.createDnClassInfo(dnGenerator.getDnHolder().getDnHeader(), dnGenerator.getDnHolder().getDnDetail());
        
        if (autoSend)
        {
            dnDocFileHandler.createFile(dnGenerator.getDnHolder(), dnGenerator.getTargetFile(), expectedFormat);
            this.generateCSVFileForBuyer(buyer, dnGenerator.getDnHolder());
            return dnGenerator.getTargetFile();
        }
        else
        {
            return null;
        }
    }
    
    
    private String initDnNo(BigDecimal buyerOid, String storeCode) throws Exception
    {
        List<BusinessRuleHolder> dnBusinessRules = businessRuleService.selectRulesByBuyerOidAndFuncGroupAndFuncId(buyerOid, RULE_FUNGROUP_DN, RULE_FUNID_BACKEND);
        if (dnBusinessRules == null || dnBusinessRules.isEmpty())
        {
            return null;
        }
        String runningNumber = runningNumberService.generateNumber(buyerOid, "D", 5);
        for (BusinessRuleHolder obj : dnBusinessRules)
        {
            if ("DnNoStyle1".equals(obj.getRuleId()))
            {
                return "DN/" + storeCode + "/" + DateUtil.getInstance().convertDateToString(new Date(), "yy")
                        + "/" + runningNumber;
            }
        }
        return "";
    }
    
    
    private GrnHolder mergerGrnList(List<GrnHeaderHolder> grnHeaders) throws Exception
    {
        if (grnHeaders == null || grnHeaders.isEmpty())
        {
            return null;
        }
        GrnHolder grnHolder = new GrnHolder();
        grnHolder.setHeader(grnHeaders.get(0));
        GrnDetailHolder templateDetail = new GrnDetailHolder();
        List<GrnDetailHolder> grnDetails = new ArrayList<GrnDetailHolder>();
        Map<String ,BigDecimal> map = new HashMap<String, BigDecimal>();
        for (GrnHeaderHolder header : grnHeaders)
        {
            List<GrnDetailHolder> details = grnDetailService.selectGrnDetailByKey(header.getGrnOid());
            if (details == null || details.isEmpty())
            {
                continue;
            }
            templateDetail = details.get(0);
            for (GrnDetailHolder detail : details)
            {
                if (map.containsKey(detail.getBuyerItemCode()))
                {
                    map.put(detail.getBuyerItemCode(), map.get(detail.getBuyerItemCode()).add(detail.getReceiveQty()));
                    continue;
                }
                map.put(detail.getBuyerItemCode(), detail.getReceiveQty());
            }
        }
        for (Map.Entry<String, BigDecimal> entry : map.entrySet())
        {
            GrnDetailHolder detail = new GrnDetailHolder();
            BeanUtils.copyProperties(templateDetail, detail);
            detail.setBuyerItemCode(entry.getKey());
            detail.setReceiveQty(entry.getValue());
            detail.setUnitCost(templateDetail.getUnitCost() == null ? BigDecimal.ZERO : templateDetail.getUnitCost());
            grnDetails.add(detail);
        }
        grnHolder.setDetails(grnDetails);
        return grnHolder;
    }

    
    private void initDnStatus(DnGenerator stockGenerator)throws Exception
    {

        List<DnHeaderHolder> dupDns = dnHeaderService.selectDnHeadersByBuyerOidDnNoAndSupplierCode(
            stockGenerator.getDnHolder().getDnHeader().getBuyerOid(),
            stockGenerator.getDnHolder().getDnHeader().getDnNo(),
            stockGenerator.getDnHolder().getDnHeader().getSupplierCode());
        
        boolean duplicate = false;
        
        if (null != dupDns && !dupDns.isEmpty())
        {
            for (DnHeaderHolder dupDn : dupDns)
            {
                if (!dupDn.getSentToSupplier())
                {
                    continue;
                }
                
                if (!dupDn.getDuplicate())
                {
                    dupDn.setDuplicate(true);
                    dnHeaderService.updateByPrimaryKey(null, dupDn);
                    duplicate = true;
                }
                
                if (DnStatus.NEW.equals(dupDn.getDnStatus()) || DnStatus.AMENDED.equals(dupDn.getDnStatus()))
                {
                    dupDn.setDnStatus(DnStatus.OUTDATED);
                    dnHeaderService.updateByPrimaryKey(null, dupDn);
                    duplicate = true;
                }
            }
        }
        
        
        stockGenerator.getDnHolder().getDnHeader().setDuplicate(duplicate);
        
        if (stockGenerator.getDnHolder().getDnHeader().getDuplicate())
        {
            stockGenerator.getDnHolder().getDnHeader().setDnStatus(DnStatus.AMENDED);
        }
    }
    
    
    private void generateCSVFileForBuyer(BuyerHolder buyer, DnHolder dn) throws Exception
    {
        boolean needTranslate = businessRuleService.isDnNeedTranslate(buyer.getBuyerOid());
        
        if (needTranslate)
        {
            List<BusinessRuleHolder> dnBusinessRules = businessRuleService.selectRulesByBuyerOidAndFuncGroupAndFuncId(buyer.getBuyerOid(), RULE_FUNGROUP_DN, RULE_FUNID_BACKEND);
            
            for (BusinessRuleHolder obj : dnBusinessRules)
            {
                if ("UnityFileStype".equals(obj.getRuleId()))
                {
                    File targetCSVPath = new File(mboxUtil.getBuyerEaiPath(buyer.getMboxId()) + PS + "in");
                    
                    if (!targetCSVPath.isDirectory())
                    {
                        FileUtil.getInstance().createDir(targetCSVPath);
                    }
                    
                    File targetCSVFile = new File (mboxUtil.getBuyerEaiPath(buyer.getMboxId()) + PS + "in" + PS + dn.getUnityCsvFileName());
                    String contents = dn.toUnityCsvFile(dn.getDnHeader().getDnType());
                    
                    String archivePath = mboxUtil.getFolderInBuyerArchInPath(buyer.getMboxId(), 
                        DateUtil.getInstance().getYearAndMonth(new Date()));
                    
                    File rlt = new File(archivePath);
                    
                    if (!rlt.isDirectory())
                    {
                        FileUtil.getInstance().createDir(rlt);
                    }
                    
                    FileParserUtil.getInstance().writeContent(targetCSVFile, contents);
                    
                    FileUtil.getInstance().copyFile(targetCSVFile, new File(archivePath, dn.getUnityCsvFileName()), true);
                }
            }
        }
    }
    
}
