//*****************************************************************************
//
// File Name       :  KeyExchangeController.java
// Date Created    :  Jul 9, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jul 9, 2013 11:54:26 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action.rest;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.service.RestRequestAuthenticationService;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;


/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class KeyController
{
    private static final Logger log = LoggerFactory.getLogger(KeyController.class);
    
    private static final String CLIENT = "Client [";
    
    @Autowired
    private transient RestRequestAuthenticationService restRequestAuthenticationService;
    @Autowired
    private transient GnupgUtil gnupgUtil;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    @Autowired
    private transient CustomAppConfigHelper appConfig;
    
    private String id;
    private File[] uploadFiles;
    private String[] uploadFilesFileName;
    
    public void index() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        JSONObject json = null;
        
        try
        {
            log.info(CLIENT + request.getRemoteHost() + "] checking whether key is setup.");
            
            String[] nonceMbs = restRequestAuthenticationService
                .splitMailBoxAndNonceFromRequestHeader(request
                    .getHeader("WS-Authorization"));
            
            String mboxId = nonceMbs[0];
            
            String clientPubKeyId = GnupgUtilWrapper.getInstance().wrapClientId(mboxId);
            
            boolean clientPubKeyExist = gnupgUtil.isKeyExistInServer(clientPubKeyId);
            
            if (clientPubKeyExist)
                log.info(CLIENT + request.getRemoteHost() + "], key has setup.");
            else
                log.info(CLIENT + request.getRemoteHost() + "], key is not setup.");
            
            json = new JSONObject();
            json.put("keyImported", clientPubKeyExist);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_OK, json.toString());
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    
    public void show() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        File exportedPubKeyFile = null;
        
        try
        {
            String[] nonceMbs = restRequestAuthenticationService
                .splitMailBoxAndNonceFromRequestHeader(request
                    .getHeader("WS-Authorization"));
            
            String mboxId = nonceMbs[0];
            
            String supPubKeyId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), mboxId);
            
            log.info(CLIENT + request.getRemoteHost() + "] trying to download pub key [" + supPubKeyId + "].");
            
            exportedPubKeyFile = new File(mboxUtil.getSupplierMailBox(mboxId), supPubKeyId + ".key");
            
            gnupgUtil.exportKey(exportedPubKeyFile.getPath(), supPubKeyId);
            
            this.sendKeyFile(response, exportedPubKeyFile.getPath());
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        finally
        {
            if (null != exportedPubKeyFile)
            {
                FileUtil.getInstance().deleleAllFile(exportedPubKeyFile);
            }
        }
    }
    
    
    public void create() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        File importedClientPubKeyFile = null;
        
        log.info(CLIENT + request.getRemoteHost() + "] trying to upload pub key.");
        
        try
        {
            String[] nonceMbs = restRequestAuthenticationService
                .splitMailBoxAndNonceFromRequestHeader(request
                    .getHeader("WS-Authorization"));
            
            String mboxId = nonceMbs[0];
            String filename = uploadFilesFileName[0];
            importedClientPubKeyFile = new File(mboxUtil.getSupplierMailBox(mboxId), filename);
            
            FileUtil.getInstance().copyFile(uploadFiles[0], importedClientPubKeyFile, false);
            
            log.info("Client pub kye received as file [" + importedClientPubKeyFile + "].");
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_OK, "");
            
            log.info("Importing pub key from temp file ["
                + importedClientPubKeyFile.getPath() + "] for Client ["
                + request.getRemoteHost() + "].");
            
            gnupgUtil.importKey(importedClientPubKeyFile.getPath());
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        finally
        {
            if (null != importedClientPubKeyFile)
            {
                FileUtil.getInstance().deleleAllFile(importedClientPubKeyFile);
            }
        }
        
    }
    
    
    //******************************************************
    // private methods
    //******************************************************
    
    private void sendKeyFile(HttpServletResponse response, String filePath) throws IOException
    {
        RandomAccessFile file = null;
        PrintStream ps = null;
        try
        {
            file = new RandomAccessFile(filePath, "r");
            response.setContentType("application/pgp-encrypted-signed");
            response.setContentLength((int) file.length());
            response.setHeader("content", "application/pgp-encrypted-signed");
            response.setHeader("filename", FilenameUtils.getName(filePath));
            ps = new PrintStream(response.getOutputStream(), true, CommonConstants.ENCODING_UTF8);
            byte buf[] = new byte[1024];
            int len;
            while (true)
            {
                len = file.read(buf);
                if (len <= 0) break;
                ps.write(buf, 0, len);
            }
        }
        finally
        {
            try
            {
                if (null != file) file.close();
                if (null != ps) ps.close();
            }
            catch (Exception iox)
            {
                log.error(iox.getMessage());
            }
            file = null;
            ps = null;
        }
    }
    
    //******************************************************
    // getter and setter methods
    //******************************************************
    
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
        this.uploadFilesFileName = uploadFilesFileName == null ? null : uploadFilesFileName.clone();
    }
    
    
    public String getId()
    {
        return id;
    }


    public void setId(String id)
    {
        this.id = id;
    }
}
