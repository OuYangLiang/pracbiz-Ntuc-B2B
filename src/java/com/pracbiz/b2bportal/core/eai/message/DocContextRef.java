//*****************************************************************************
//
// File Name       :  DocContextRef.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 10:20:19 AM$
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
public class DocContextRef
{
    private String senderMailboxId;
    private String workDir;

    private String receiverMailboxId;
    private String receiverChannel;
    private String receiverChannelMailboxId;

    public String getSenderMailboxId()
    {
        return senderMailboxId;
    }

    public void setSenderMailboxId(String senderMailboxId)
    {
        this.senderMailboxId = senderMailboxId;
    }

    public String getWorkDir()
    {
        return workDir;
    }

    public void setWorkDir(String workDir)
    {
        this.workDir = workDir;
    }

    public String getReceiverMailboxId()
    {
        return receiverMailboxId;
    }

    public void setReceiverMailboxId(String receiverMailboxId)
    {
        this.receiverMailboxId = receiverMailboxId;
    }

    public String getReceiverChannel()
    {
        return receiverChannel;
    }

    public void setReceiverChannel(String receiverChannel)
    {
        this.receiverChannel = receiverChannel;
    }

    public String getReceiverChannelMailboxId()
    {
        return receiverChannelMailboxId;
    }

    public void setReceiverChannelMailboxId(String receiverChannelMailboxId)
    {
        this.receiverChannelMailboxId = receiverChannelMailboxId;
    }
}
