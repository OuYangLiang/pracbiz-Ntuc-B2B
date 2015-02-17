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
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public class SalesDocMsg extends DocMsg
{
    private SalesHolder data;
    
    
    @Override
    public MsgType getMsgType()
    {
        return MsgType.DSD;
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

    public SalesHolder getData()
    {
        return data;
    }


    public void setData(SalesHolder data)
    {
        this.data = data;
    }


    @Override
    public String computePdfFilename()
    {
        return this.getData().getSalesHeader().computePdfFilename();
    }

    
    @Override
    public ReportEngineParameter<SalesHolder> computeReportEngineParameter()
    {
        ReportEngineParameter<SalesHolder> rlt = new ReportEngineParameter<SalesHolder>();
        rlt.setData(this.getData());
        rlt.setBuyer(this.getBuyer());
        rlt.setSupplier(this.getSupplier());
        rlt.setMsgTransactions(this.convertToMsgTransactions());
        
        return rlt;
    }

    
    
}
