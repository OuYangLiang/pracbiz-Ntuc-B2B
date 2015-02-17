//*****************************************************************************
//
// File Name       :  InboundGenerateAckComp.java
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xadisk.bridge.proxies.interfaces.Session;
import org.xadisk.bridge.proxies.interfaces.XAFileOutputStream;

import com.pracbiz.client.utils.ClientConfigHelper;


/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming
 */
public class InboundGenerateAckComp
{
    private static final Logger log = LoggerFactory.getLogger(InboundGenerateAckComp.class);
    
    public void generateAck(File file, File outDir, File archOutDir, Session session) throws Exception
    {
        log.info("Generating acknowledgement for file [" + file.getName() + "].");
        
        File workDir = file.getParentFile();
        
        File ackFile = new File(workDir, file.getName() + ClientConfigHelper.VALUE_ACK_FILE_EXTENSION);
        
        byte[] plain = this.generateACK(file.getName());
        
        XAFileOutputStream output = null;
        try
        {
            if (!session.fileExists(ackFile))
            {
                session.createFile(ackFile, false);
                output = session.createXAFileOutputStream(ackFile, true);
                output.write(plain);
            }
        }
        finally
        {
            if(null != output)
            {
                output.close();
                output = null;
            }
        }
        
        log.info("Acknowledgement file [" + ackFile.getName() + "] generated successfully.");
        
        session.copyFile(ackFile, new File(archOutDir, ackFile.getName()));
        session.moveFile(ackFile, new File(outDir, ackFile.getName()));
        
    }
    
    
    private byte[] generateACK(String filename) throws IOException
    {
        OutputStream os = null;
        byte[] rlt = null;

        Document document = new Document();
        Element root = new Element("ackFile");
        root.addContent(new Element("fileName").setText(filename));
        document.setRootElement(root);

        try
        {
            XMLOutputter o = new XMLOutputter(Format.getPrettyFormat());
            os = new ByteArrayOutputStream();
            o.output(document, os);

            rlt = ((ByteArrayOutputStream)os).toByteArray();
        }
        finally
        {
            if (os != null){os.close();os = null;}
        }

        return rlt;
    }
    
}
