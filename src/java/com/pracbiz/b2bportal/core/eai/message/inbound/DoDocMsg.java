//*****************************************************************************
//
// File Name       :  DoDocMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 11:07:18 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.inbound;


import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.holder.DoHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class DoDocMsg extends DocMsg
{

    private DoHolder data;
    
    
    @Override
    public MsgType getMsgType()
    {
        return MsgType.DO;
    }

    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

    @Override
    public boolean isGeneratePdf()
    {
        return false;
    }

    public DoHolder getData()
    {
        return data;
    }

    public void setData(DoHolder data)
    {
        this.data = data;
    }

    @Override
    public String computePdfFilename()
    {
        // TODO unnecessary
        return null;
    }

    @Override
    public ReportEngineParameter<DoHolder> computeReportEngineParameter()
    {
        // TODO unnecessary
        return null;
    }

    
    
}
