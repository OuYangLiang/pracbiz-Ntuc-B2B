//*****************************************************************************
//
// File Name       :  InboundPDFDocHandlerComp.java
// Date Created    :  Mar 1, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Mar 1, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.core.inbound;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xadisk.bridge.proxies.interfaces.Session;
import org.xadisk.bridge.proxies.interfaces.XAFileOutputStream;

import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.client.utils.ClientConfigHelper;
import com.pracbiz.client.utils.PrintFileUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class InboundPDFDocHandlerComp
{
    private static final Logger log = LoggerFactory.getLogger(InboundPDFDocHandlerComp.class);
    
    @Autowired
    private ClientConfigHelper clientConfig;
    
    public void processPDF(String serverId, File file, File docDir, File printDir,
        Session session) throws Exception
    {
        if (!session.fileExistsAndIsDirectory(docDir))
        {
            session.createFile(docDir, true);
        }
        
        if (!session.fileExistsAndIsDirectory(printDir))
        {
            session.createFile(printDir, true);
        }
        
        log.info("Copy file [" + file.getName() + "] to [" + docDir.getPath() + "].");
        session.copyFile(file, new File(docDir,file.getName()));
        
        if (!clientConfig.autoPrint(serverId))
        {
            return;
        }
        
        File printJobFile = new File(printDir, FileUtil.getInstance()
            .trimAllExtension(file.getName())
            + ClientConfigHelper.VALUE_PRINT_JOB_FILE_EXTENSION);
        
        log.info("Generating print job file [" + printJobFile.getName() + "].");
        
        byte[] plain = PrintFileUtil.getInstance().generateXML(
            docDir.getPath(), file.getName(), file.getName().split("_")[2]);
        
        XAFileOutputStream output = null;
        try
        {
            session.createFile(printJobFile, false);
            output = session.createXAFileOutputStream(printJobFile, true);
            output.write(plain);
            
            log.info("Print job file [" + printJobFile.getName() + "] generated successfully.");
        }
        finally
        {
            if(null != output)
            {
                output.close();
                output = null;
            }
        }
        
    }
    
}
