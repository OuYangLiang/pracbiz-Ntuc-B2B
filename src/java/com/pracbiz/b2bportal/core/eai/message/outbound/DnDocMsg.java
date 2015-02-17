//*****************************************************************************
//
// File Name       :  DnDocMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 11:04:23 AM$
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
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class DnDocMsg extends DocMsg
{
    private DnHolder data;
    
    @Override
    public MsgType getMsgType()
    {
        return MsgType.DN;
    }

    
    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

    @Override
    public boolean isGeneratePdf()
    {
        return true;
    }

    
    public DnHolder getData()
    {
        return data;
    }

    
    public void setData(DnHolder data)
    {
        this.data = data;
    }

    
    @Override
    public String computePdfFilename()
    {
        return this.getData().getDnHeader().computePdfFilename();
    }

    
    @Override
    public ReportEngineParameter<DnHolder> computeReportEngineParameter()
    {
        ReportEngineParameter<DnHolder> rlt = new ReportEngineParameter<DnHolder>();
        rlt.setBuyer(this.getBuyer());
        rlt.setSupplier(this.getSupplier());
        rlt.setMsgTransactions(this.convertToMsgTransactions());
        rlt.setData(this.getData());
        
        return rlt;
    }

    
}
