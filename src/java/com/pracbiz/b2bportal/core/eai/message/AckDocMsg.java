//*****************************************************************************
//
// File Name       :  AckDocMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 11:17:33 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;


/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class AckDocMsg extends DocMsg
{
    private final MsgType msgType;
    
    public AckDocMsg(MsgType msgType)
    {
        this.msgType = msgType;
    }

    @Override
    public MsgType getMsgType()
    {
        return msgType;
    }

    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

    @Override
    public boolean isGeneratePdf()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public String computePdfFilename()
    {
        // TODO unnecessary
        return null;
    }

    @Override
    public ReportEngineParameter<?> computeReportEngineParameter()
    {
        // TODO unnecessary
        return null;
    }

}
