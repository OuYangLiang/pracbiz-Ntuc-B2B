//*****************************************************************************
//
// File Name       :  GiDocMsg.java
// Date Created    :  Nov 13, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 13, 2013 11:27:56 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.outbound;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GiDocMsg extends DocMsg
{
    private GiHolder data;
    
    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

    @Override
    public String computePdfFilename()
    {
        return this.getData().getGiHeader().computePdfFilename();
    }

    @Override
    public ReportEngineParameter<GiHolder> computeReportEngineParameter()
    {
        ReportEngineParameter<GiHolder> rlt = new ReportEngineParameter<GiHolder>();
        rlt.setBuyer(this.getBuyer());
        rlt.setSupplier(this.getSupplier());
        rlt.setMsgTransactions(this.convertToMsgTransactions());
        rlt.setData(this.getData());
        
        return rlt;
    }

    @Override
    public MsgType getMsgType()
    {
        return MsgType.GI;
    }

    @Override
    public boolean isGeneratePdf()
    {
        return true;
    }

    public GiHolder getData()
    {
        return data;
    }

    public void setData(GiHolder data)
    {
        this.data = data;
    }

}
