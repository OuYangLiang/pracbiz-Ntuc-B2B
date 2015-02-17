//*****************************************************************************
//
// File Name       :  CcDocMsg.java
// Date Created    :  Dec 23, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 23, 2013 5:09:00 PM $
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
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CcDocMsg extends DocMsg
{
    private CcHolder data;
    
    
    @Override
    public boolean isGeneratePdf()
    {
        return true;
    }

    
    @Override
    public MsgType getMsgType()
    {
        return MsgType.CC;
    }

    
    @Override
    public String computePdfFilename()
    {
        return this.data.getCcHeader().computePdfFilename();
    }

    
    @Override
    public ReportEngineParameter<CcHolder> computeReportEngineParameter()
    {
        ReportEngineParameter<CcHolder> rlt = new ReportEngineParameter<CcHolder>();
        rlt.setBuyer(this.getBuyer());
        rlt.setSupplier(this.getSupplier());
        rlt.setMsgTransactions(this.convertToMsgTransactions());
        rlt.setData(this.getData());
        
        return rlt;
    }

    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

    
    public CcHolder getData()
    {
        return data;
    }
    

    public void setData(CcHolder data)
    {
        this.data = data;
    }
    

}
