//*****************************************************************************
//
// File Name       :  WatsonsInvExportJob.java
// Date Created    :  Dec 19, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 19, 2013 2:38:20 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.watsons.eai.backend;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.backend.BaseJob;
import com.pracbiz.b2bportal.core.eai.backend.SupplierMasterImportJob;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;
import com.pracbiz.b2bportal.core.util.PropertiesUtil;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class WatsonsInvExportJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(WatsonsInvExportJob.class);
    private MailBoxUtil mboxUtil;
    private static final String ID = "[WatsonsInvExportJob]";
    private String WATSONS_DATE_FORMAT = "ddMMyyHHmmss";
    private static String DELIMITER_WATSONS = "^";
    private static String SPLIT_WATSONS = "\\^";
    private static String WATSON_FILE_PREFIX = "inv.";
    private static String FILE_TXT_POSTFIX = ".txt";
    
    private BuyerService buyerService;
    private StandardEmailSender standardEmailSender;
    private BusinessRuleService businessRuleService;
    private BuyerMsgSettingService buyerMsgSettingService;
    
    @Override
    protected void init()
    {
        buyerService = this.getBean("buyerService", BuyerService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        mboxUtil = this.getBean("mboxUtil", MailBoxUtil.class);
        businessRuleService = this.getBean("businessRuleService", BusinessRuleService.class);
        buyerMsgSettingService = this.getBean("buyerMsgSettingService", BuyerMsgSettingService.class);
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
    
    
    private void realProcess()
    {
        log.info(" :::: Start to process.");

        try
        {
            List<BuyerHolder> buyers = buyerService.selectActiveBuyers();
            
            if (buyers == null || buyers.isEmpty())
            {
                log.info("There is no active buyer in system.");
                return;
            }
            
            for (final BuyerHolder buyer : buyers)
            {
               boolean canProcess = businessRuleService.isSkipMatching(buyer.getBuyerOid());
               BuyerMsgSettingHolder msgSetting = buyerMsgSettingService.selectByKey(buyer.getBuyerOid(), MsgType.INV.name());
               
               if (!canProcess || !msgSetting.getFileFormat().equalsIgnoreCase(DocFileHandler.WATSONS))
               {
                   continue;
               }
               
               processFile(buyer);
            }

            log.info(" :::: Processed.");
        }

        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
    }
    
    
    private void processFile(final BuyerHolder buyer) 
    {
        File tempPath = new File(mboxUtil.getBuyerMailBox(buyer.getMboxId()) + PS + "tmp");

        File[] invFiles = tempPath.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file)
            {
                String[] filename = file.getName().split("_");
                if (filename[0].equalsIgnoreCase(MsgType.INV.name()))
                {
                    return true;
                }
                
                return false;
            }

        });

        if (null == invFiles || invFiles.length == 0)
        {
            log.debug(" :::: No Invoice files found for Buyer ["
                    + buyer.getBuyerName() + "].");
            return;
        }

        List<File> stableFiles = Arrays.asList(FileUtil.getInstance().getStableFiles(invFiles));

        if (stableFiles == null || stableFiles.isEmpty())
        {
            return;
        }
        
        File inPath = new File(mboxUtil.getBuyerInPath(buyer.getMboxId()));
        String fileName = inPath
            + PS
            + WATSON_FILE_PREFIX
            + DateUtil.getInstance().convertDateToString(new Date(),
                WATSONS_DATE_FORMAT) + FILE_TXT_POSTFIX;
        File archInPath = new File(mboxUtil.getBuyerArchInPath(buyer.getMboxId()));
        List<File> successFiles = new ArrayList<File>();
        
        try
        {
            for (File batchFile : stableFiles)
            {
                boolean success = processBatchFile(buyer, batchFile, fileName);
                
                if (success)
                {
                    successFiles.add(batchFile);
                }
            }
            
            if (!successFiles.isEmpty())
            {
                for (File successFile : successFiles)
                {
                    FileUtil.getInstance().moveFile(successFile, archInPath.getPath());
                }
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    private boolean processBatchFile(BuyerHolder buyer, File batchFile, String filePath) throws Exception
    {
        File file = new File(filePath);
        String[] contents = null;
        boolean success = true;
        try
        {
            contents = FileParserUtil.getInstance().readLines(batchFile);
            if (contents != null)
            {
                if (!contents[0].startsWith("T1"))
                {
                    return false;
                }
                StringBuffer sb = new StringBuffer();
                for (int i = 0 ; i < contents.length; i++)
                {
                    if (contents[i].startsWith("T1"))
                    {
                        sb.append(resetHeaderLine(contents[i]) + END_LINE);
                    }
                    else
                    {
                        sb.append(contents[i] + END_LINE);
                    }
                }
                if (!sb.toString().isEmpty())
                {
                    if (!file.exists() || file.isDirectory())
                    {
                       if (file.createNewFile())
                       {
                           log.info("Has been create file [" + file.getName() + "] successful.");
                       }
                    }
                    
                    FileUtil.getInstance().appendLine(file, sb.toString());
                }
            }
        }
        catch(IOException e)
        {
           success = false;
           ErrorHelper.getInstance().logError(log, e);
           if (file.exists() && !file.delete())
           {
               throw new IOException();
           }
        }
        
        return success;
    }
    
    private String formatSupplierCode(String supplierCode)
    {
        Properties pros = PropertiesUtil.getProperties("watsons-supplier-code.properties");
        
        if (pros == null)
        {
            return supplierCode;
        }
        
        return null == pros.get(supplierCode) ? supplierCode : pros.get(supplierCode).toString();
    }
    
    private String resetHeaderLine(String headerContent)
    {
        String[] header = headerContent.split(SPLIT_WATSONS);
        String supplierCode = formatSupplierCode(header[26].trim());
        
        StringBuffer content = new StringBuffer();
        content
        .append(header[0]).append(DELIMITER_WATSONS)
        .append(header[1]).append(DELIMITER_WATSONS)
        .append(header[2]).append(DELIMITER_WATSONS)
        .append(header[3]).append(DELIMITER_WATSONS)
        .append(header[4]).append(DELIMITER_WATSONS)
        .append(header[5]).append(DELIMITER_WATSONS)
        .append(header[6]).append(DELIMITER_WATSONS)
        .append(header[7]).append(DELIMITER_WATSONS)
        .append(header[8]).append(DELIMITER_WATSONS)
        .append(header[9]).append(DELIMITER_WATSONS)
        .append(header[10]).append(DELIMITER_WATSONS)
        .append(header[11]).append(DELIMITER_WATSONS)
        .append(header[12]).append(DELIMITER_WATSONS) //invBAN
        .append(header[13]).append(DELIMITER_WATSONS)
        .append(header[14]).append(DELIMITER_WATSONS) //invoiceName
        .append(header[15]).append(DELIMITER_WATSONS) //invAddr
        .append(header[16]).append(DELIMITER_WATSONS) //invContact
        .append(header[17]).append(DELIMITER_WATSONS) //CWMark
        .append(header[18]).append(DELIMITER_WATSONS)
        .append(header[19]).append(DELIMITER_WATSONS)
        .append(header[20]).append(DELIMITER_WATSONS)
        .append(header[21]).append(DELIMITER_WATSONS) //reference 2
        .append(header[22]).append(DELIMITER_WATSONS) //recepit seller
        .append(header[23]).append(DELIMITER_WATSONS) //remarks
        .append(header[24]).append(DELIMITER_WATSONS) //reserved
        .append(header[25]).append(DELIMITER_WATSONS) //seller BAN
        .append(StringUtil.getInstance().convertStringWithPrefixOrPostfix(supplierCode, true, ' ', 13)).append(DELIMITER_WATSONS) //seller EAN
        .append(header[27]).append(DELIMITER_WATSONS) //seller Name
        .append(header[28]).append(DELIMITER_WATSONS) //seller Addr
        .append(header[29]).append(DELIMITER_WATSONS) //seller tel
        .append(header[30]).append(DELIMITER_WATSONS) //seller Rep
        .append(header[31]).append(DELIMITER_WATSONS) //seller
        .append(header[32]).append(DELIMITER_WATSONS) // digitApproval Agency
        .append(header[33]).append(DELIMITER_WATSONS) // digital Approval Date
        .append(header[34]).append(DELIMITER_WATSONS);
        
        return content.toString();
    }
}
