//*****************************************************************************
//
// File Name       :  PoDocMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 10:36:35 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.outbound;


import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class PocnDocMsg extends DocMsg
{
    private PoHolder data;
    
    
    @Override
    public MsgType getMsgType()
    {
        return MsgType.POCN;
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

    
    public PoHolder getData()
    {
        return data;
    }

    
    public void setData(PoHolder data)
    {
        this.data = data;
    }

    
    @Override
    public String computePdfFilename()
    {
        return null;
    }

    
    @Override
    public ReportEngineParameter<PoHolder> computeReportEngineParameter()
    {
        return null;
    }

    
    
}
