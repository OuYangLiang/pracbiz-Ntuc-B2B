package com.pracbiz.client.core.jobs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.xadisk.additional.XAFileInputStreamWrapper;
import org.xadisk.additional.XAFileOutputStreamWrapper;
import org.xadisk.bridge.proxies.interfaces.Session;
import org.xadisk.bridge.proxies.interfaces.XAFileInputStream;
import org.xadisk.bridge.proxies.interfaces.XAFileOutputStream;
import org.xadisk.bridge.proxies.interfaces.XAFileSystem;
import org.xadisk.bridge.proxies.interfaces.XAFileSystemProxy;
import org.xadisk.filesystem.exceptions.NoTransactionAssociatedException;
import org.xadisk.filesystem.standalone.StandaloneFileSystemConfiguration;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.exception.GnupgException;
import com.pracbiz.b2bportal.core.util.GnupgUtil;
import com.pracbiz.b2bportal.core.util.GnupgUtilWrapper;
import com.pracbiz.client.core.inbound.InboundPDFDocHandlerComp;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.MailboxUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class InboundFileJob extends QuartzJobBean
{
    private static final String FILE_LOG_PREFIX = "File [";
    private static final Logger log = LoggerFactory.getLogger(InboundFileJob.class);
    private static final String PS = File.separator;
    
    private static boolean isRunning = false;
    
    private ClientConfigHelper clientConfig;
    private InboundPDFDocHandlerComp pdfDocHandlerComp;
    private GnupgUtil gnupgUtil;
    private MailboxUtil mboxUtil;
    
    protected void executeInternal(JobExecutionContext context)
        throws JobExecutionException
    {
        if(isRunning)
        {
            return;
        }
        
        isRunning = true;
        
        try
        {
            synchronized(DownloadJob.lock)
            {
                while (DownloadJob.anyJobProcessing)
                {
                    try
                    {
                        DownloadJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                DownloadJob.anyJobProcessing = true;
            }
            
            if (null == DownloadJob.keySetup || DownloadJob.keySetup.isEmpty())
            {
                log.info("Key is not setup, please let Download job run first.");
                return;
            }
            
            List<String> serverIds = clientConfig.getAllServerId();
            for (String serverId : serverIds)
            {
                List<String> mboxIds = clientConfig.getMboxIds(serverId);
                
                for (String mboxId : mboxIds)
                {
                    if (!DownloadJob.keySetup.get(serverId.trim() + mboxId.trim()))
                    {
                        log.info("Key is not setup, please let Download job run first.");
                        return;
                    }
                }
            }
            
            process();
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            synchronized (DownloadJob.lock)
            {
                DownloadJob.anyJobProcessing = false;

                DownloadJob.lock.notifyAll();
            }
            
            isRunning = false;
        }
    }
    
    
    private void process() throws UnsupportedEncodingException
    {
        log.info("Start to process.");
        
        List<String> serverIds = clientConfig.getAllServerId();
        
        for (String serverId : serverIds)
        {
            List<String> mboxIds = clientConfig.getMboxIds(serverId.trim());
            
            for (String mboxId : mboxIds)
            {
                log.info("Start to process for mailbox [" + mboxId + "] for portal [" + serverId + "].");
                
                this.process(serverId.trim(), mboxId.trim());
                
                log.info("Mailbox [" + mboxId + "] for portal [" + serverId + "] process ended.");
            }
        }
        
        log.info(" :::: Ended to process.");
    }
   
    
    private void process(String serverId, String mboxId) throws UnsupportedEncodingException
    {
        File inDir = mboxUtil.getInPath(serverId, mboxId);
        
        File[] filefiles = inDir.listFiles(new FileFilter()
        {

            @Override
            public boolean accept(File file)
            {
                return file.getName().toLowerCase(Locale.US)
                    .endsWith(ClientConfigHelper.VALUE_TOKEN_FILE_EXTENSION);
            }

        });
        
        if (null == filefiles || filefiles.length == 0)
        {
            log.info("No token files detected in directory [" + inDir.getPath() + "].");
            
            return;
        }
        
        log.info(filefiles.length + " token file(s) detected in directory [" + inDir.getPath() + "].");
        
        for (File tokfile : filefiles)
        {
            File pgpFile = new File(tokfile.getParent(), tokfile.getName()
                .replaceAll(ClientConfigHelper.VALUE_TOKEN_FILE_EXTENSION,
                    ClientConfigHelper.VALUE_PGP_FILE_EXTENSION));
            
            File zipFile = new File(tokfile.getParent(), tokfile.getName()
                .substring(0, tokfile.getName().lastIndexOf('.')));
            
            try
            {
                gnupgUtil.decrypt(GnupgUtilWrapper.getInstance().wrapClientId(mboxId),
                    pgpFile, zipFile);
                
                try
                {
                    FileUtil.getInstance().moveFile(pgpFile, mboxUtil.getArchInPath(serverId, mboxId).getPath());
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
            catch(GnupgException e)
            {
                ErrorHelper.getInstance().logError(log, e);
                
                try
                {
                    FileUtil.getInstance().deleleAllFile(tokfile);
                    FileUtil.getInstance().moveFile(pgpFile, mboxUtil.getInvalidInPath(serverId, mboxId).getPath());
                }
                catch(IOException e1)
                {
                    ErrorHelper.getInstance().logError(log, e1);
                }
                
                continue;
            }
            
            processBatchFiles(serverId, mboxId, tokfile);
        }
        
    }
    
    
    private void processBatchFiles(String serverId, String mboxId, File tokfile)
    {
        XAFileSystem xafs = null;
        Session session = null;
        StandaloneFileSystemConfiguration config = new StandaloneFileSystemConfiguration(
            mboxUtil.getSystemTempPath(serverId, mboxId).getPath(), "id-1");
        
        File zipFile = new File(tokfile.getParent(), tokfile.getName()
            .substring(0, tokfile.getName().lastIndexOf('.')));
        
        try
        {
            log.info("Start to process file [" + tokfile.getName() + "].");
            
            log.info("Booting XADisk.");
            xafs = XAFileSystemProxy.bootNativeXAFileSystem(config);
            xafs.waitForBootup(-1);
            log.info("XADisk is now ready to use.");
            
            session = xafs.createSessionForLocalTransaction();
            
            if (!session.fileExists(zipFile))
            {
                log.error(FILE_LOG_PREFIX + zipFile + "] not found, but its token file exists.");
                log.info("Remove file [" + tokfile.getName() + "].");
                
                session.deleteFile(tokfile);
                session.commit();
                
                return;
            }
            
            File workDir = new File(mboxUtil.getWorkPath(serverId, mboxId).getPath() + PS
                + FileUtil.getInstance().trimAllExtension(zipFile.getName()));
            
            final File batchfile = new File(workDir, zipFile.getName());
            
            session.createFile(workDir, true);
            session.moveFile(zipFile, batchfile);
            session.deleteFile(tokfile);
            
            List<File> extractFiles = this.unZip(batchfile, workDir, session);
            
            log.info("Extracting file [" + batchfile.getName() + "], list of extracted files:");
            for (File file : extractFiles)
            {
                log.info(FILE_LOG_PREFIX + file.getName() + "].");
            }
            
            for (File docFile : extractFiles)
            {
                String filename = docFile.getName();
                if (StringUtils.lowerCase(filename).endsWith(
                    ClientConfigHelper.VALUE_PDF_FILE_EXTENSION))
                {
                    if (clientConfig.autoPrint(serverId))
                        pdfDocHandlerComp.processPDF(serverId, docFile, mboxUtil.getDocPath(serverId, mboxId), mboxUtil.getPrintPath(serverId, mboxId), session);
                    
                    if (clientConfig.isDispatchPdf())
                    {
                        log.info("Copy file [" + docFile.getName() + "] to [" + mboxUtil.getPrivateInPath(serverId, mboxId).getPath() + "].");
                        session.copyFile(docFile, new File(mboxUtil.getPrivateInPath(serverId, mboxId), docFile.getName()));
                    }
                    
                    if (session.fileExists(docFile))
                        session.deleteFile(docFile);
                    
                }
                else
                {
                    log.info("Move file [" + docFile.getName() + "] to [" + mboxUtil.getPrivateInPath(serverId, mboxId).getPath() + "].");
                    session.moveFile(docFile, new File(mboxUtil.getPrivateInPath(serverId, mboxId), docFile.getName()));
                }
                
                //generateAckComp.generateAck(docFile, outDir, archOutDir, session);
            }
            
            log.info("Archiving file [" + batchfile.getName() + "] to directory [" + mboxUtil.getArchInPath(serverId, mboxId).getPath() + "].");
            session.moveFile(batchfile, new File(mboxUtil.getArchInPath(serverId, mboxId), zipFile.getName()));
            log.info(FILE_LOG_PREFIX + batchfile.getName() + "] archived to directory [" + mboxUtil.getArchInPath(serverId, mboxId).getPath() + "] successfully.");
            
            log.info("Clean the work folder [" + workDir.getPath() + "].");
            session.deleteFile(workDir);
            log.info("Work folder [" + workDir.getPath() + "] cleaned successfully.");
            
            session.commit();
            
            log.info(FILE_LOG_PREFIX + tokfile.getName() + "] processed successfully.");
            
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            try
            {
                session.rollback();
            }
            catch(NoTransactionAssociatedException e1)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
        }
        finally
        {
            if (null != xafs)
            {
                try
                {
                    xafs.shutdown();
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
        
    }
    
    
    public List<File> unZip(File zipFile, File outputPath, Session session) throws Exception
    {
        List<File> rlt = new ArrayList<File>();
        
        ZipInputStream zis = null;
        BufferedInputStream bis = null;
        XAFileInputStream xafis = null;
        XAFileInputStreamWrapper xafisw = null;

        byte[] buf = new byte[1024];
        int readBytes;

        try
        {
            xafis = session.createXAFileInputStream(zipFile);
            xafisw = new XAFileInputStreamWrapper(xafis);
            bis = new BufferedInputStream(xafisw);
            zis = new ZipInputStream(bis);

            ZipEntry entry = null;

            while((entry = zis.getNextEntry()) != null)
            {
                String filename = entry.getName();
                File newFile = new File(outputPath, filename);
                session.createFile(newFile, false);

                XAFileOutputStream xafos = null;
                XAFileOutputStreamWrapper xafosw = null;
                BufferedOutputStream bos = null;
                try
                {
                    xafos = session.createXAFileOutputStream(newFile, false);
                    xafosw = new XAFileOutputStreamWrapper(xafos);
                    bos = new BufferedOutputStream(xafosw);

                    while((readBytes = zis.read(buf)) != -1)
                    {
                        bos.write(buf, 0, readBytes);
                    }
                    bos.flush();
                    
                    rlt.add(newFile);
                }
                finally
                {
                    if (null != bos)
                    {
                        bos.close();
                        bos = null;
                    }
                }

            }
        }
        finally
        {
            if (null != zis)
            {
                zis.close();
                zis = null;
            }
        }
        
        return rlt;
    }
    
    
    // *********************************************
    // setter and getter method
    // *********************************************
    
    public void setClientConfig(ClientConfigHelper clientConfig)
    {
        this.clientConfig = clientConfig;
    }

    
    public void setPdfDocHandlerComp(InboundPDFDocHandlerComp pdfDocHandlerComp)
    {
        this.pdfDocHandlerComp = pdfDocHandlerComp;
    }


    public void setGnupgUtil(GnupgUtil gnupgUtil)
    {
        this.gnupgUtil = gnupgUtil;
    }


    public void setMboxUtil(MailboxUtil mboxUtil)
    {
        this.mboxUtil = mboxUtil;
    }
    
}
