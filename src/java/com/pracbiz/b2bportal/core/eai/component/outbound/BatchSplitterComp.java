//*****************************************************************************
//
// File Name       :  BatchSplitterComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:20:23 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.exception.InvalidDocFilenameException;
import com.pracbiz.b2bportal.core.eai.file.validator.FileValidator;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocContextRef;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummary;
import com.pracbiz.b2bportal.core.eai.message.PoBatchSummaryLine;
import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.FilenameParserVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;
import com.pracbiz.b2bportal.core.service.NotificationService;
import com.pracbiz.b2bportal.core.util.ChannelConfigHelper;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class BatchSplitterComp implements Callable, CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(BatchSplitterComp.class);
    private static final String ID = "[OutboundBatchSplitterComp]";
    
    @Autowired
    private MailBoxUtil mboxUtil;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private BuyerMsgSettingService buyerMsgSettingService;
    @Autowired
    private BusinessRuleService businessRuleService;
    @Autowired
    private FilenameParserVisitor filenameParserVisitor;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private FileValidator canonicalPoFileValidator;
    @Autowired
    private FileValidator canonicalRtvFileValidator;
    @Autowired
    private FileValidator canonicalPocnFileValidator;
    @Autowired
    private FileValidator canonicalGrnFileValidator;
    @Autowired
    private FileValidator canonicalPnFileValidator;
    @Autowired
    private FileValidator fairpriceGrFileValidator;
    @Autowired
    private FileValidator fairpriceGiFileValidator;
    @Autowired
    private FileValidator ebxmlPoFileValidator;
    @Autowired
    private FileValidator idocPoFileValidator;
    @Autowired
    private FileValidator idocRtvFileValidator;
    @Autowired
    private FileValidator watsonsPoFileValidator;
    @Autowired
    private FileValidator canonicalSalesFileValidator;

    @Override
    public Object onCall(MuleEventContext ctx)
    {
        BatchMsg batch = (BatchMsg)ctx.getMessage().getPayload();
        
        log.info(":::: Batch object with No. [" + batch.getBatchNo()
            + "] received.");
        
        File workDir = new File(batch.getContext().getWorkDir());
        File batchFile = new File(workDir, batch.getBatchFileName());
        
        try
        {
            GZIPHelper.getInstance().unZip(batchFile, workDir.getPath());

            List<File> extractedFiles = this.retrieveExtractedFiles(workDir,
                batchFile.getName());
            
            List<Object> rlt = null;

            if(DeploymentMode.LOCAL
                .equals(batch.getBuyer().getDeploymentMode()))
            {
                rlt = initDocForLocalBuyer(batch, extractedFiles);
                
                // to set po batch info from the csv file.
                if (BatchType.PO.equals(batch.getBatchType()))
                {
                    File poBatchSummaryFile = this.retrievePoBatchSummaryFile(workDir);
                    
                    if (null != poBatchSummaryFile)
                    {
                        batch.setPoBatchSummary(parsePoBatchSummaryFile(poBatchSummaryFile));
                    }
                }
                
                // to set source file names into batch
                File sourceNameFile = this.retrieveSourceNameFile(workDir);
                
                if (null != sourceNameFile)
                {
                    this.parseSourceNameFile(sourceNameFile, batch);
                }
            }
            else//remote buyer
            {
                rlt = initDocForRemoteBuyer(batch, extractedFiles);
            }
            
            if (rlt == null || rlt.isEmpty())
            {
                return null;
            }
            
            BuyerMsgSettingHolder setting = buyerMsgSettingService.selectByKey(batch.getBuyer().getBuyerOid(), batch.getBatchType().name());
            
            if (null == setting)
            {
                throw new Exception("Msg type [" + batch.getBatchType().name() + "] is not setup for Buyer [" + batch.getBuyer().getBuyerName() + "].");
            }
            
            Map<String, List<String>> fileErrors = validateFile(batch.getBuyer(), batchFile, batch.getBatchType().name(), setting.getFileFormat());
            
            if (fileErrors != null && !fileErrors.isEmpty())
            {
                boolean isContinueProcessErrorBatch = businessRuleService.isContinueProcessErrorBatch(batch.getBuyer().getBuyerOid());
                
                List<DocMsg> docs = new LinkedList<DocMsg>();
                
                for (Object o : rlt)
                {
                    DocMsg docMsg = (DocMsg)o;
                    if (fileErrors.containsKey(docMsg.getOriginalFilename()))
                    {
                        docMsg.setValid(false);
                        docMsg.setErrorMsg(fileErrors.get(docMsg.getOriginalFilename()));
                    }
                    else if (!isContinueProcessErrorBatch)
                    {
                        docMsg.setActive(false);
                        docMsg.setRemarks("skip processing");
                    }
                    docs.add(docMsg);
                }
                
                boolean continueProcess;
                if (isContinueProcessErrorBatch)
                {
                    for (Object o : rlt)
                    {
                        DocMsg doc = (DocMsg)o;
                        if (false != doc.isValid())
                        {
                            return rlt;
                        }
                    }
                    continueProcess = false;
                }
                else
                {
                    continueProcess = false;
                }
                
                if (!continueProcess)
                {
                    File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(batch
                        .getContext().getSenderMailboxId(), DateUtil.getInstance()
                        .getYearAndMonth(batch.getInDate())));
                
                    if (!invalidPath.isDirectory())
                    {
                        FileUtil.getInstance().createDir(invalidPath);
                    }
                    
                    FileUtil.getInstance().copyFile(
                            new File(batch.getContext().getWorkDir(),
                                batch.getBatchFileName()),
                            new File(invalidPath, batch.getBatchFileName()), true);
                      
                    FileUtil.getInstance().deleleAllFile(
                            new File(batch.getContext().getWorkDir()));
    
                    FileUtil.getInstance().deleleAllFile(
                            new File(batch.getContext().getTokenDir(),
                                    batch.getContext().getTokenFilename()));
                    
                    batch.setDocs(docs);
                    notificationService.sendOutboundNotificationEmail(batch);
                        
                    return null;
                }
            }
            
            log.info(":::: Batch object with No. [" + batch.getBatchNo()
                + "] processed successfully.");
            
            return rlt;
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            
            
            // File invalidPath = new File(appConfig.getBuyerMailboxRootPath()
            //+ PS + batch.getContext().getSenderMailboxId() + PS + "invalid" + PS
            //+ DateUtil.getInstance().getYearAndMonth(batch.getInDate()));
            File invalidPath = new File(mboxUtil.getFolderInBuyerInvalidPath(
                batch.getContext().getSenderMailboxId(), DateUtil.getInstance()
                    .getYearAndMonth(batch.getInDate())));
            
            try
            {
                if(!invalidPath.isDirectory())
                {
                    FileUtil.getInstance().createDir(invalidPath);
                }
                FileUtil.getInstance().moveFile(batchFile,
                    invalidPath.getPath());
                FileUtil.getInstance().deleleAllFile(workDir);
                FileUtil.getInstance().changeFileExtension(
                    new File(batch.getContext().getTokenDir(), batch
                        .getContext().getTokenFilename()), ".err");
            }
            catch(IOException e1)
            {
                String tickNo1 = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo1, e);
            }
        }
        
        return null;
    }
    
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private List<Object> initDocForRemoteBuyer(BatchMsg batch,
        List<File> extractedFiles) throws InvalidDocFilenameException,
        Exception
    {
        List<Object> rlt = new LinkedList<Object>();

        for (File docFile : extractedFiles)
        {
            // to filter pdf file
            if (docFile.getName().toLowerCase(Locale.US).endsWith(".pdf"))
            {
                continue;
            }
            
            MsgType msgType = DefaultDocFileHandler.parseTypeByFilename(docFile
                .getName());

            DocMsg docMsg = initDocMsg(batch, docFile, msgType);
            docMsg.setReportFilename(docMsg.getOriginalFilename().substring(0,
                docMsg.getOriginalFilename().lastIndexOf(".")) + ".pdf");
            docMsg.setOutputFormat(channelConfig.getChannelFileFormat(batch
                .getBuyer().getChannel(), docMsg.getMsgType()));
            
            
            rlt.add(docMsg);
        }

        return rlt;
    }

    private List<Object> initDocForLocalBuyer(BatchMsg batch,
        List<File> extractedFiles) throws InvalidDocFilenameException,
        Exception
    {
        List<Object> rlt = new LinkedList<Object>();

        Map<MsgType, BuyerMsgSettingHolder> settingCache =
            new HashMap<MsgType, BuyerMsgSettingHolder>();

        for(File docFile : extractedFiles)
        {
            MsgType msgType = DefaultDocFileHandler.parseTypeByFilename(docFile
                .getName());

            if(!settingCache.containsKey(msgType))
            {
                BuyerMsgSettingHolder setting = buyerMsgSettingService
                    .selectByKey(batch.getSenderOid(), msgType.name());

                settingCache.put(msgType, setting);
            }

            DocMsg docMsg = initDocMsg(batch, docFile, msgType);
            docMsg.setOutputFormat(settingCache.get(msgType).getFileFormat());

            rlt.add(docMsg);
        }

        return rlt;
    }
    
    private DocMsg initDocMsg(BatchMsg batch, File docFile, MsgType msgType)
        throws Exception
    {
        DocMsg docMsg = msgType.initDocMsg();
        docMsg.setBatch(batch);
        docMsg.setOriginalFilename(docFile.getName());
        docMsg.setSenderOid(batch.getSenderOid());
        docMsg.setSenderCode(batch.getSenderCode());
        docMsg.setSenderName(batch.getSenderName());
        docMsg.setInDate(batch.getInDate());
        docMsg.setBuyer(batch.getBuyer());
        docMsg.accept(filenameParserVisitor);
        docMsg.setContext(initDocContext(batch));
        return docMsg;
    }
    
    private DocContextRef initDocContext(BatchMsg batch)
    {
        DocContextRef context = new DocContextRef();
        context.setSenderMailboxId(batch.getContext().getSenderMailboxId());
        context.setWorkDir(batch.getContext().getWorkDir());
        return context;
    }
    
    private List<File> retrieveExtractedFiles(File path, final String batchFile)
    {
        File[] files = path.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return !batchFile.equals(pathname.getName())
                    && !pathname.getName().toUpperCase(Locale.US).equals("SUMMARY.CSV")
                    && !pathname.getName().matches("(?i)source_[0-9]{14}.(?i)txt");
            }
        });

        return Arrays.asList(files);
    }
    
    
    private File retrievePoBatchSummaryFile(File path)
    {
        File[] files = path.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return pathname.getName().toUpperCase(Locale.US).equals("SUMMARY.CSV");
            }
        });
        
        if (null != files && files.length == 1)
        {
            return files[0];
        }
        
        return null;
    }
    
    
    private PoBatchSummary parsePoBatchSummaryFile(File file) throws IOException
    {
        PoBatchSummary rlt = new PoBatchSummary();
        FileInputStream fis = null;
        InputStreamReader isr = null;
        CSVReader reader = null;
        
        try
        {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            reader = new CSVReader(isr, ',');
            List<String[]> contents = reader.readAll();
            
            if (null == contents || contents.size() <= 1)
            {
                return null;
            }
            
            for (int i = 1; i < contents.size(); i++)
            {
                String[] content = contents.get(i);
                
                if (null == content || content.length != 4)
                {
                    continue;
                }
                
                PoBatchSummaryLine item = new PoBatchSummaryLine();
                item.setPoNo(content[0].trim());
                item.setNumOfItems(new BigDecimal(content[1].trim()));
                item.setTotalItemQty(new BigDecimal(content[2].trim()));
                item.setTotalAmt(new BigDecimal(content[3].trim()));
                
                rlt.addItem(item);
            }
        }
        finally
        {
            if (reader != null)
            {
                reader.close();
                reader = null;
            }
            if (isr != null)
            {
                isr.close();
                isr = null;
            }
            if (fis != null)
            {
                fis.close();
                fis = null;
            }
        }
        
        return rlt;
    }
    
    
    private File retrieveSourceNameFile(File path)
    {
        File[] files = path.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                return pathname.getName().toUpperCase(Locale.US).matches("(?i)source_[0-9]{14}.(?i)txt");
            }
        });
        
        if (null != files && files.length == 1)
        {
            return files[0];
        }
        
        return null;
    }
    
    
    private void parseSourceNameFile(File sourceNameFile, BatchMsg batch) throws Exception
    {
        String[] names = FileParserUtil.getInstance().readLines(sourceNameFile);
        List<String> sourceFileNames = new ArrayList<String>();
        
        if (names != null && names.length != 0)
        {
            if (names[0].startsWith("type:"))
            {
                batch.setTransformedBy("Portal");
                for (int i = 1; i < names.length; i++)
                {
                    sourceFileNames.add(names[i].trim());
                }
            }
            else
            {
                batch.setTransformedBy("GT");
                for (int i = 0; i < names.length; i++)
                {
                    sourceFileNames.add(names[i].trim());
                }
            }
        }
        batch.setSourceFileNames(sourceFileNames);
    }
    
    private Map<String, List<String>> validateFile(BuyerHolder buyer,File batchFile, String batchType, String fileFormat) throws Exception
    {
        if (MsgType.PO.name().equalsIgnoreCase(batchType))
        {
            if (FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
            {
                return canonicalPoFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
            }
            else if (FileValidator.EBXML.equalsIgnoreCase(fileFormat))
            {
                return ebxmlPoFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
            }
            else if (FileValidator.FP_IDOC.equalsIgnoreCase(fileFormat))
            {
                return idocPoFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
            }
            else if (FileValidator.WATSONS.equalsIgnoreCase(fileFormat))
            {
                return watsonsPoFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
            }
        }
        
        if (MsgType.RTV.name().equalsIgnoreCase(batchType) && FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
        {
            return canonicalRtvFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.RTV.name().equalsIgnoreCase(batchType) && FileValidator.FP_IDOC.equalsIgnoreCase(fileFormat))
        {
            return idocRtvFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.POCN.name().equalsIgnoreCase(batchType))
        {
            return canonicalPocnFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.PN.name().equalsIgnoreCase(batchType) && FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
        {
            return canonicalPnFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.GRN.name().equalsIgnoreCase(batchType) && FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
        {
            return canonicalGrnFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.GRN.name().equalsIgnoreCase(batchType) && FileValidator.FAIRPRICE.equalsIgnoreCase(fileFormat))
        {
            return fairpriceGrFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.GI.name().equalsIgnoreCase(batchType) && FileValidator.FAIRPRICE.equalsIgnoreCase(fileFormat))
        {
            return fairpriceGiFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        if (MsgType.DSD.name().equalsIgnoreCase(batchType) && FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
        {
            return canonicalSalesFileValidator.validate(batchFile, fileFormat, buyer.getBuyerCode(), buyer.getBuyerOid());
        }
        
        return null;
    }
}
