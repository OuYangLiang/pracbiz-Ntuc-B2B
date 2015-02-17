package com.pracbiz.b2bportal.core.action.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.ClientActionType;
import com.pracbiz.b2bportal.core.exception.GnupgException;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.ClientAuditTrailService;
import com.pracbiz.b2bportal.core.service.RestRequestAuthenticationService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class OutboxController extends ActionSupport implements
    CoreCommonConstants
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory
        .getLogger(OutboxController.class);
    private String id;
    private File[] uploadFiles;
    private String[] uploadFilesFileName;

    @Autowired
    private transient RestRequestAuthenticationService restRequestAuthenticationService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient ClientAuditTrailService clientAuditTrailService;
    @Autowired
    private transient GnupgUtil gnupgUtil;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    @Autowired
    private transient CustomAppConfigHelper appConfig;

    public void create() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        File targetFolder = null;
        File archFolder = null;
        File invalidFolder = null;
        String mboxId = null;

        try
        {
            String[] nonceMbs = restRequestAuthenticationService
                .splitMailBoxAndNonceFromRequestHeader(request
                    .getHeader("WS-Authorization"));

            mboxId = nonceMbs[0];

            SupplierHolder supplier = supplierService
                .selectSupplierByMboxId(mboxId);

            targetFolder = supplier.getRequireTranslationOut() ? new File(
                mboxUtil.getSupplierEaiOutPath(supplier.getMboxId()))
                : new File(mboxUtil.getSupplierOutPath(supplier.getMboxId()));
                
            archFolder = new File(mboxUtil.getFolderInSupplierArchOutPath(
                mboxId, DateUtil.getInstance().getCurrentYearAndMonth()));
            
            invalidFolder = new File(mboxUtil.getSupplierInvalidPath(mboxId));

            // mboxId = nonceMbs[0];

            if(!targetFolder.isDirectory() && !targetFolder.mkdirs())
            {
                throw new IOException("Failed to create directory ["
                    + targetFolder.getPath() + "].");
            }

            if(uploadFiles == null)
            {
                throw new Exception("No upload files found.");
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);

            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());

            return;
        }

        log.info(this.getText("B2BPSA0409",
            new String[] {request.getRemoteHost(), targetFolder.getPath()}));
        
        List<File> pgpFiles = new ArrayList<File>();

        for (int i = 0; i < uploadFiles.length; i++)
        {
            String filename = uploadFilesFileName[i];
            File savedFile = new File(targetFolder, filename);
            FileUtil.getInstance().copyFile(uploadFiles[i], savedFile, false);
            
            log.info(this.getText(
                "B2BPSA0411",
                new String[] {request.getRemoteHost(), filename,
                    targetFolder.getPath()}));
            
            pgpFiles.add(savedFile);
        }

        restRequestAuthenticationService.sendResponseToClient(response,
            HttpServletResponse.SC_OK, "");
        
        if (!archFolder.isDirectory())
        {
            FileUtil.getInstance().createDir(archFolder);
        }
        
        String userId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), mboxId);
        
        for (File pgpFile : pgpFiles)
        {
            File zip = new File(targetFolder, pgpFile.getName()
                .substring(0, pgpFile.getName().lastIndexOf('.')));
            
            try
            {
                
                log.info("Decrypting file [" + pgpFile.getName() + "].");
                
                gnupgUtil.decrypt(userId, pgpFile, zip);
            }
            catch(GnupgException e)
            {
                ErrorHelper.getInstance().logError(log, e);
                FileUtil.getInstance().moveFile(pgpFile, invalidFolder.getPath());
                
                continue;
            }
            
            
            try
            {
                log.info("Extracting file [" + zip.getName() + "].");
                GZIPHelper.getInstance().unZip(zip, targetFolder.getPath());
                
                log.info("Archiving file [" + pgpFile.getName() + "].");
                
                if (!pgpFile.renameTo(new File(archFolder, pgpFile.getName())))
                {
                    log.error("Failed to archive file [" + pgpFile.getName() + "].");
                }
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
                FileUtil.getInstance().moveFile(pgpFile, invalidFolder.getPath());
                FileUtil.getInstance().deleleAllFile(zip);
                
                continue;
            }
            
            
            try
            {
                clientAuditTrailService.insertAuditTrail(zip,
                    request.getRemoteHost(), true, null,
                    ClientActionType.OUTBOX);
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            finally
            {
                FileUtil.getInstance().deleleAllFile(zip);
            }
            
        }
        
        
    }

    //******************************************************
    // getter and setter methods
    //******************************************************

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public File[] getUploadFiles()
    {
        return uploadFiles == null ? null : uploadFiles.clone();
    }

    public void setUploadFiles(File[] uploadFiles)
    {
        this.uploadFiles = uploadFiles == null ? null : uploadFiles.clone();
    }

    public String[] getUploadFilesFileName()
    {
        return uploadFilesFileName == null ? null : uploadFilesFileName.clone();
    }

    public void setUploadFilesFileName(String[] uploadFilesFileName)
    {
        this.uploadFilesFileName = uploadFilesFileName == null ? null
            : uploadFilesFileName.clone();
    }

}
