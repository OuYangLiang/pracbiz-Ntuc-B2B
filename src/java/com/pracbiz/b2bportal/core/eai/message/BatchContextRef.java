//*****************************************************************************
//
// File Name       :  BatchContextRef.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 9:55:14 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class BatchContextRef
{
    private String tokenFilename;
    private String tokenDir;
    private String workDir;
    private String senderMailboxId;

    public String getTokenFilename()
    {
        return tokenFilename;
    }

    public void setTokenFilename(String tokenFilename)
    {
        this.tokenFilename = tokenFilename;
    }

    public String getTokenDir()
    {
        return tokenDir;
    }

    public void setTokenDir(String tokenDir)
    {
        this.tokenDir = tokenDir;
    }

    public String getWorkDir()
    {
        return workDir;
    }

    public void setWorkDir(String workDir)
    {
        this.workDir = workDir;
    }

    public String getSenderMailboxId()
    {
        return senderMailboxId;
    }

    public void setSenderMailboxId(String senderMailboxId)
    {
        this.senderMailboxId = senderMailboxId;
    }
}
