package com.pracbiz.b2bportal.core.action.rest;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.constants.ClientActionType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.exception.GnupgException;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.ClientAuditTrailService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
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
public class InboxController extends ActionSupport implements
        CoreCommonConstants
{
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(InboxController.class);
    public static final String SESSION_FILE_LIST = "SESSION_FILE_LIST";
    private String id;
    
    @Autowired
    private transient RestRequestAuthenticationService restRequestAuthenticationService;
    @Autowired
    private transient SupplierService supplierService;
    @Autowired
    private transient CustomAppConfigHelper appConfig;
    @Autowired
    private transient ClientAuditTrailService clientAuditTrailService;
    @Autowired
    private transient MsgTransactionsService msgTransactionsService;
    @Autowired
    private transient GnupgUtil gnupgUtil;
    @Autowired
    private transient MailBoxUtil mboxUtil;
    


    public void index() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        log.info(this.getText("B2BPSA0401",
                new String[] { request.getRemoteHost() }));
        
        JSONObject json = null;
        File[] files = null;
        
        try
        {
            String[] nonceMbs = restRequestAuthenticationService
                .splitMailBoxAndNonceFromRequestHeader(request
                    .getHeader("WS-Authorization"));
            
            SupplierHolder supplier = supplierService
                .selectSupplierByMboxId(nonceMbs[0]);
            
            files = retrieveItemCountFromInbox(supplier, request);
            
            json = new JSONObject();
            json.put("itemCount", null == files? 0 : files.length);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            restRequestAuthenticationService.sendResponseToClient(response,
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
        
        if (null == files || files.length == 0)
        {
            request.getSession().invalidate();
        }
        else
        {
            List<File> fileList = new ArrayList<File>();
            for (File file : files)
            {
                fileList.add(file);
            }
            request.getSession().setAttribute(SESSION_FILE_LIST, fileList);
        }
        
        restRequestAuthenticationService.sendResponseToClient(response,
            HttpServletResponse.SC_OK, json.toString());
        
        log.info(this.getText("B2BPSA0405",
            new String[] {Integer.toString(null == files? 0 : files.length), request.getRemoteHost()}));
    }

    
    public void show() throws IOException
    {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpServletResponse response = ServletActionContext.getResponse();
        
        File file = null;
        
        @SuppressWarnings("unchecked")
        List<File> files = (List<File>)request.getSession().getAttribute(SESSION_FILE_LIST);
        file = files.get(0);
        files.remove(0);
        
        File zip = new File(file.getParent(), file.getName().substring(0, file.getName().lastIndexOf('.')));
        
        log.info("Response file [" + file.getPath() + "] for client ["
            + request.getRemoteHost() + "].");
        sendZipFile(response, file.getPath());
        
        try
        {
            if (files.isEmpty())
            {
                request.getSession().invalidate();
            }
            else
            {
                request.getSession().setAttribute(SESSION_FILE_LIST, files);
            }
            
            clientAuditTrailService.insertAuditTrail(zip,
                request.getRemoteHost(), true, null, ClientActionType.INBOX);
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            FileUtil.getInstance().deleleAllFile(file);
            FileUtil.getInstance().deleleAllFile(zip);
        }
    }
    
    

    //******************************************************
    // private methods
    //******************************************************
    
    private void sendZipFile(HttpServletResponse response, String filePath) throws IOException
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


    private File[] retrieveItemCountFromInbox(SupplierHolder supplier,
            HttpServletRequest request) throws IOException
    {
        String mboxId = supplier.getMboxId();
        File targetFolder = null;
        if (supplier.getRequireTranslationIn())
        {
            targetFolder = new File(mboxUtil.getSupplierEaiInPath(supplier.getMboxId()));
        }
        else
        {
            targetFolder = new File(mboxUtil.getSupplierInPath(supplier.getMboxId()));
        }

        LOG.info(this.getText("B2BPSA0406",
            new String[] {request.getRemoteHost(), targetFolder.getPath()}));

        File[] individualFiles = getIndividualFiles(targetFolder);
        
        if (null != individualFiles && individualFiles.length > 0)
        {
            for (File individualFile : individualFiles)
            {
                try
                {
                    String filename = individualFile.getName();
                    
                    if (filename.toUpperCase(Locale.US).endsWith(".PDF"))   continue;
                    
                    String[] parts = FileUtil.getInstance().trimAllExtension(filename).split("_");
                    
                    BigDecimal oid = new BigDecimal(parts[parts.length-1]);
                    String buyerCode = parts[1];
                    String bSupCode = parts[2];
                    
                    MsgTransactionsHolder msg = msgTransactionsService.selectByKey(oid);
                    SupplierHolder sup = supplierService.selectSupplierByKey(msg.getSupplierOid());
                    if(("direct".equalsIgnoreCase(sup.getTransMode())
                        || null == sup.getTransMode() || sup.getTransMode()
                        .trim().isEmpty())
                        && msg.getReadStatus().equals(ReadStatus.UNREAD)
                        && msg.getBuyerCode().equalsIgnoreCase(buyerCode)
                        && msg.getSupplierCode().equalsIgnoreCase(bSupCode))
                    {
                        msg.setReadStatus(ReadStatus.READ);
                        msg.setReadDate(new Date());
                        
                        msgTransactionsService.updateByPrimaryKey(null, msg);
                    }
                }
                catch (Exception e)
                {
                    log.error("Failed to update read status for file [" + individualFile.getName() + "].");
                    
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
            
            this.compress(individualFiles, targetFolder.getPath(), mboxId);
        }
        
        File[] zipFiles = getCompressedFiles(targetFolder);
        
        if (zipFiles != null && zipFiles.length > 0)
        {
            String clientPubKeyId = GnupgUtilWrapper.getInstance().wrapClientId(mboxId);
            String portalPriKeyId = GnupgUtilWrapper.getInstance().wrapSupplierUserId(appConfig.getServerId(), mboxId);
            
            for (File zip : zipFiles)
            {
                log.info("Encrypting file [" + zip.getPath() + "].");
                try
                {
                    File gpg = new File(zip.getParentFile(), zip.getName() + ".pgp");
                    
                    if (!gpg.exists())
                    {
                        gnupgUtil.encryptAndSign(portalPriKeyId, clientPubKeyId,
                            portalPriKeyId, zip, gpg);
                    }
                    
                    //FileUtil.getInstance().deleleAllFile(zip);
                }
                catch(GnupgException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
        
        File[] pgpFiles = this.getGpgFile(targetFolder.getPath());
        
        return pgpFiles;
    }


    private File[] getIndividualFiles(File folder) throws IOException
    {
        if (!folder.isDirectory())
        {
            FileUtil.getInstance().createDir(folder);
        }

        File[] individualFiles = folder.listFiles(new FileFilter() {

            public boolean accept(File file)
            {
                if (file.isDirectory())
                {
                    return false;
                }
                
                if (file.getName().toUpperCase().endsWith(".ZIP") ||
                    file.getName().toUpperCase().endsWith(".PGP")
                        || ".DS_Store".equals(file.getName()))
                {
                    return false;
                }

                return true;
            }
        });
        
        return individualFiles;
    }


    private String getTimeStamp()
    {
        return DateUtil.getLogicTimeStamp(new Date());
    }


    private void compress(File[] files, String path, String mboxId) throws IOException
    {
        String ts = getTimeStamp();
        int max = Integer.parseInt(appConfig.getMaxZipFile());

        File[] tmpFiles = null;

        int times = files.length / max;
        if (files.length % max != 0) times += 1;

        for (int i = 0; i < times; i++)
        {
            List<File> doZipFiles = new ArrayList<File>();
            if (files.length - i * max < max)
                tmpFiles = new File[files.length - i * max];
            else
                tmpFiles = new File[max];

            for (int j = 0; j < tmpFiles.length; j++)
            {
                tmpFiles[j] = files[i * max + j];
                doZipFiles.add(files[i * max + j]);
            }

            String zipName = mboxId + "_" + tmpFiles.length + "_" + ts
                    + getThreeNumberOfInt(i + 1) + ".zip";
            GZIPHelper.getInstance().doZip(doZipFiles, path, zipName);

            log.info(" :::: Generated file[" + zipName + "] successfully.");
        }

        for (File file : files)
        {
            if (!file.delete())
            {
                log.error("Failed to remove file [" + file.getPath() + "].");
            }
        }
        
    }


    private File[] getCompressedFiles(File folder)
    {
        if (!folder.isDirectory())
        {
            boolean flag = folder.mkdirs();
            if (!flag)
            {
                return null;
            }
        }

        return folder.listFiles(new FileFilter() {

            public boolean accept(File file)
            {
                return file.getName().toUpperCase().endsWith(".ZIP");
            }

        });
    }
    
    private File[] getGpgFile(String filepath)
    {
        File path = new File(filepath);

        File[] rlt = path.listFiles(new FileFilter()
        {
            public boolean accept(File file)
            {
                return file.getName().toUpperCase().endsWith(".PGP");
            }
        });

        if (rlt == null || rlt.length < 1)
        {
            return null;
        }
        
        return rlt;
    }


    private String getThreeNumberOfInt(int i)
    {
        String str = Integer.toString(i);
        if (str.length() == 1)
            return "00" + str;
        else if (str.length() == 2)
            return "0" + str;
        else
            return "" + str;
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

}
