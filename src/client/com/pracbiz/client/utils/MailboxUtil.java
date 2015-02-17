//*****************************************************************************
//
// File Name       :  MailboxUtil.java
// Date Created    :  Jul 23, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Jul 23, 2013 5:17:29 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public final class MailboxUtil implements CoreCommonConstants
{
    @Autowired 
    private ClientConfigHelper clientConfig;
    
    
    private String getServerRoot(String serverId)
    {
        return clientConfig.getLocalMailboxRoot() + PS + serverId;
    }
    
    
    public File getInPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "in");
        
        return file;
    }
    
    
    public File getOutPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "out");
        
        return file;
    }
    
    
    public File getArchInPath(String serverId, String mboxId)
    {
        File file = new File (getServerRoot(serverId) + PS + mboxId + PS + "archive" + PS + "in" + PS + DateUtil.getInstance().getCurrentYearAndMonth());
        
        return file;
    }
    
    
    public File getArchOutPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "archive" + PS + "out" + PS + DateUtil.getInstance().getCurrentYearAndMonth());
        
        return file;
    }
    
    
    public File getPrivateInPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "private_in");
        
        return file;
    }
    
    
    public File getPrivateOutPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "private_out");
        
        return file;
    }
    
    
    public File getInvalidInPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "invalid" + PS + "in");
        
        return file;
    }
    
    
    public File getInvalidOutPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "invalid" + PS + "out");
        
        return file;
    }
    
    
    public File getSystemTempPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "temp");
        
        return file;
    }
    
    
    public File getWorkPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "working" + PS + "in");
        
        return file;
    }
    
    
    public File getPrintPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "print");
        
        return file;
    }
    
    
    public File getDocPath(String serverId, String mboxId)
    {
        File file = new File( getServerRoot(serverId) + PS + mboxId + PS + "doc" + PS + DateUtil.getInstance().getCurrentYearAndMonth());
        
        return file;
    }
    
    
    public void createMailboxes(String serverId, String mboxId) throws IOException
    {
        FileUtil.getInstance().createDir(this.getPrintPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getOutPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getInPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getWorkPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getInvalidInPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getInvalidOutPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getPrivateInPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getPrivateOutPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getArchInPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getArchOutPath(serverId, mboxId));
        FileUtil.getInstance().createDir(this.getDocPath(serverId, mboxId));
    }
    
}
