package com.pracbiz.b2bportal.customized.unity.eai.backend;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.eai.backend.BaseJob;
import com.pracbiz.b2bportal.core.eai.backend.SupplierMasterImportJob;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.DnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DocSendJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(DocSendJob.class);
    private static final String ID = "[DocSendJob]";

    private DnHeaderService dnHeaderService;
    private BuyerService buyerService;
    private BuyerMsgSettingService buyerMsgSettingService;
    private StandardEmailSender standardEmailSender;
    private MailBoxUtil mboxUtil;
    private BusinessRuleService businessRuleService;
    private DnDocFileHandler dnDocFileHandler;
    private DnService dnService;


    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        dnHeaderService = this.getBean("dnHeaderService", DnHeaderService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
        dnDocFileHandler = this.getBean("canonicalDnDocFileHandler", DnDocFileHandler.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        dnService = this.getBean("dnService", DnService.class);
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
        List<BuyerHolder> buyerList = null;
        
        try
        {
            buyerList = buyerService.select(new BuyerHolder());
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        if (buyerList == null || buyerList.isEmpty())
        {
            log.info(":::: No buyer exist in system.");
            return;
        }
        
        for (BuyerHolder buyer : buyerList)
        {
            try
            {
                processBuyer(buyer);
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }

        log.info(":::: process successfully.");
    }
    
    
    private void processBuyer(BuyerHolder buyer) throws Exception 
    {
        DnHeaderHolder param = new DnHeaderHolder();
        param.setBuyerOid(buyer.getBuyerOid());
        param.setSentToSupplier(false);
        param.setMarkSentToSupplier(true);
        List<DnHeaderHolder> dnHeaders = dnHeaderService.select(param);
        
        if (dnHeaders == null || dnHeaders.isEmpty())
        {
            log.info("no dn need to send for buyer [" + buyer.getBuyerCode() + "]");
            return;
        }
        
        log.info("find " + dnHeaders.size() + " dns need to send for buyer [" + buyer.getBuyerCode() + "]");
        
        String expectedFormat = buyerMsgSettingService.selectByKey(
                buyer.getBuyerOid(), MsgType.DN.name()).getFileFormat();
        if (!expectedFormat.equalsIgnoreCase(DocFileHandler.CANONICAL))
        {
            log.info("only canonical dn is supported, buyer's format is [" + expectedFormat + "]");
            return;
        }
        
        String out = mboxUtil.getBuyerOutPath(buyer.getMboxId()) + PS;
        
        String zipName = BatchType.DN.name() + "_" + buyer.getBuyerCode() + "_" 
                + DateUtil.getInstance().getCurrentLogicTimeStamp()
                + ".zip";
        
        String rootPath = mboxUtil.getBuyerMailBox(buyer.getBuyerCode());
        
        List<File> dnFiles = new ArrayList<File>();
        
        for (DnHeaderHolder dnHeader : dnHeaders)
        {
            try
            {
                DnHolder dn = dnService.selectDnByKey(dnHeader.getDnOid());
                String extension = FileUtil.getInstance().getExtension(dnDocFileHandler.getTargetFilename(dn, expectedFormat));
                String dnFilename = MsgType.DN.name() + DOC_FILENAME_DELIMITOR
                        + dnHeader.getBuyerCode() + DOC_FILENAME_DELIMITOR
                        + dnHeader.getSupplierCode() + DOC_FILENAME_DELIMITOR
                        + StringUtil.getInstance().convertDocNo(dnHeader.getDnNo()) + "." + extension;
                File dnFile = new File(rootPath + PS + dnFilename);
                dnDocFileHandler.createFile(dn, dnFile, expectedFormat);
                if (dnFile != null)
                {
                    log.info("dn file [" + dnFile.getName() + "] will be generated.");
                    dnFiles.add(dnFile);
                }
                
                //create dn csv file for buyer
                boolean needTranslate = businessRuleService.isDnNeedTranslate(buyer.getBuyerOid());
                
                if (needTranslate)
                {
                    String fileStyle = businessRuleService.selectDnTranslateFileStyle(buyer.getBuyerOid());
                    
                    if ("UnityFileStype".equals(fileStyle))
                    {
                        File targetCSVPath = new File(mboxUtil.getBuyerEaiPath(buyer.getMboxId()) + PS + "in");
                        
                        if (!targetCSVPath.isDirectory())
                        {
                            FileUtil.getInstance().createDir(targetCSVPath);
                        }
                        
                        File targetCSVFile = new File (mboxUtil.getBuyerEaiPath(buyer.getMboxId()) + PS + "in" + PS + dn.getUnityCsvFileName());
                        String contents = dn.toUnityCsvFile(dnHeader.getDnType());
                        
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
                
                // update database
                dnHeader.setSentToSupplier(true);
                dnHeader.setMarkSentToSupplier(true);
                dnHeader.setExported(true);
                dnHeader.setExportedDate(new Date());
                
                this.initDnStatus(dnHeader);
                
                dnHeaderService.updateByPrimaryKey(null, dnHeader);
            }
            catch (Exception e)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo, e);
            }
        }

        if (dnFiles.isEmpty())
        {
            return;
        }
        
        GZIPHelper.getInstance().doZip(dnFiles, out, zipName);
        
        for (File file : dnFiles)
        {
            FileUtil.getInstance().deleleAllFile(file);
        }
    }


    // *****************************************************
    // private methods
    // *****************************************************
    private void initDnStatus(DnHeaderHolder dnHeader)throws Exception
    {

        List<DnHeaderHolder> dupDns = dnHeaderService
            .selectDnHeadersByBuyerOidDnNoAndSupplierCode(dnHeader
                .getBuyerOid(), dnHeader.getDnNo(), dnHeader.getSupplierCode());
        
        boolean duplicate = false;
        if (null != dupDns && !dupDns.isEmpty())
        {
            for (DnHeaderHolder dupDn : dupDns)
            {
                if (dnHeader.getDnOid().equals(dupDn.getDnOid()))
                {
                    continue;
                }
                
                if (!dupDn.getDuplicate())
                {
                    dupDn.setDuplicate(true);
                    dnHeaderService.updateByPrimaryKey(null, dupDn);
                }
                
                if (DnStatus.NEW.equals(dupDn.getDnStatus()) || DnStatus.AMENDED.equals(dupDn.getDnStatus()))
                {
                    dupDn.setDnStatus(DnStatus.OUTDATED);
                    dnHeaderService.updateByPrimaryKey(null, dupDn);
                }
            }
        }
        
        if ((null != dupDns && dupDns.size() > 1))
        {
            duplicate = true;
        }
        
        dnHeader.setDuplicate(duplicate);
        
        if (dnHeader.getDuplicate())
        {
            dnHeader.setDnStatus(DnStatus.AMENDED);
        }
        
    }

}
