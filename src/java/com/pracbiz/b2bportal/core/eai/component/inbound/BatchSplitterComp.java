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

package com.pracbiz.b2bportal.core.eai.component.inbound;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
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
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.FilenameParserVisitor;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
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
    private static final String ID = "[InboundBatchSplitterComp]";

    @Autowired
    private ChannelConfigHelper channelConfig;
    @Autowired
    private SupplierMsgSettingService supplierMsgSettingService;
    @Autowired
    private FilenameParserVisitor filenameParserVisitor;
    @Autowired
    private StandardEmailSender standardEmailSender;
    @Autowired
    private MailBoxUtil mboxUtil;
    @Autowired
    private FileValidator canonicalInvFileValidator;
    @Autowired
    private FileValidator ebxmlInvFileValidator;
    @Autowired
    private FileValidator canonicalCnFileValidator;


    @Override
    public Object onCall(MuleEventContext ctx) throws Exception
    {
        BatchMsg batch = (BatchMsg) ctx.getMessage().getPayload();

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

            if (DeploymentMode.LOCAL.equals(batch.getSupplier()
                    .getDeploymentMode()))
            {
                rlt = initDocForLocalSupplier(batch, extractedFiles);
            }
            else
            // remote supplier
            {
                rlt = initDocForRemoteSupplier(batch, extractedFiles);
            }
            
            SupplierMsgSettingHolder setting = supplierMsgSettingService.selectByKey(batch.getSupplier().getSupplierOid(), batch.getBatchType().name());
            if (setting == null)
            {
                throw new Exception("could not obtain msg setting for supplier [" + batch.getSupplier().getSupplierCode() + "] and msgType [" + batch.getBatchType().name() + "]");
            }
            
            
            if (batch.getSupplier().getClientEnabled() != null && batch.getSupplier().getClientEnabled())
            {
                Map<String, List<String>> fileErrors = validateFile(batch.getSupplier(), batchFile, batch.getBatchType().name(), setting.getFileFormat());
                log.info("validate result :"+ fileErrors);
                if (fileErrors != null && !fileErrors.isEmpty())
                {
                    for (Object o : rlt)
                    {
                        DocMsg docMsg = (DocMsg)o;
                        if (fileErrors.containsKey(docMsg.getOriginalFilename()))
                        {
                            docMsg.setValid(false);
                            docMsg.setErrorMsg(fileErrors.get(docMsg.getOriginalFilename()));
                        }
                    }
                }
            }
            

            log.info(":::: Batch object with No. [" + batch.getBatchNo()
                    + "] processed successfully.");

            return rlt;
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);

            //File invalidPath = new File(appConfig.getSupplierMailboxRootPath()
            //+ PS + batch.getContext().getSenderMailboxId() + PS
            //+ "invalid" + PS
            //+ DateUtil.getInstance().getYearAndMonth(batch.getInDate()));
            File invalidPath = new File(mboxUtil
                .getFolderInSupplierInvalidPath(batch.getContext()
                    .getSenderMailboxId(), DateUtil.getInstance()
                    .getYearAndMonth(batch.getInDate())));

            if (!invalidPath.isDirectory())
            {
                FileUtil.getInstance().createDir(invalidPath);
            }

            try
            {
                FileUtil.getInstance().moveFile(batchFile,
                        invalidPath.getPath());
                FileUtil.getInstance().deleleAllFile(workDir);
                FileUtil.getInstance().changeFileExtension(
                        new File(batch.getContext().getTokenDir(), batch
                                .getContext().getTokenFilename()), ".err");
            }
            catch (IOException e1)
            {
                String tickNo1 = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo1, e);
            }
        }

        return null;
    }


    // *****************************************************
    // private methods
    // *****************************************************

    private List<Object> initDocForRemoteSupplier(BatchMsg batch,
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
                    docMsg.getOriginalFilename().lastIndexOf("."))
                    + ".pdf");
            docMsg.setOutputFormat(channelConfig.getChannelFileFormat(batch
                    .getSupplier().getChannel(), docMsg.getMsgType()));

            rlt.add(docMsg);
        }

        return rlt;
    }


    private List<Object> initDocForLocalSupplier(BatchMsg batch,
            List<File> extractedFiles) throws InvalidDocFilenameException,
            Exception
    {
        List<Object> rlt = new LinkedList<Object>();

        Map<MsgType, SupplierMsgSettingHolder> settingCache = new HashMap<MsgType, SupplierMsgSettingHolder>();

        for (File docFile : extractedFiles)
        {
            MsgType msgType = DefaultDocFileHandler.parseTypeByFilename(docFile
                    .getName());

            if (!settingCache.containsKey(msgType))
            {
                SupplierMsgSettingHolder setting = supplierMsgSettingService
                        .selectByKey(batch.getSenderOid(), msgType.name());

                settingCache.put(msgType, setting);
            }

            DocMsg docMsg = initDocMsg(batch, docFile, msgType);
            docMsg.setOutputFormat(settingCache.get(msgType) == null ? null : settingCache.get(msgType).getFileFormat());

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
        //docMsg.setSenderCode(batch.getSenderCode()); sender code should be buyer-given-supplier code, not supplier code.
        docMsg.setSenderName(batch.getSenderName());
        docMsg.setInDate(batch.getInDate());
        docMsg.setSupplier(batch.getSupplier());
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
        File[] files = path.listFiles(new FileFilter() {
            public boolean accept(File pathname)
            {
                return !batchFile.equals(pathname.getName());
            }
        });

        return Arrays.asList(files);
    }
    
    
    private Map<String, List<String>> validateFile(SupplierHolder supplier, File batchFile, String batchType, String fileFormat) throws Exception
    {
        if (MsgType.INV.name().equalsIgnoreCase(batchType))
        {
            if (FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
            {
                return canonicalInvFileValidator.validate(batchFile, fileFormat, supplier.getSupplierCode(), supplier.getSupplierOid());
            }
            else if (FileValidator.EBXML.equalsIgnoreCase(fileFormat))
            {
                return ebxmlInvFileValidator.validate(batchFile, fileFormat, supplier.getSupplierCode(), supplier.getSupplierOid());
            }
        }
        else if (MsgType.CN.name().equalsIgnoreCase(batchType))
        {
            if (FileValidator.CANONICAL.equalsIgnoreCase(fileFormat))
            {
                return canonicalCnFileValidator.validate(batchFile, fileFormat, supplier.getSupplierCode(), supplier.getSupplierOid());
            }
        }
        return null;
    }

}
