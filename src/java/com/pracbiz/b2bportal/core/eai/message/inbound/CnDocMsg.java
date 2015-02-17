package com.pracbiz.b2bportal.core.eai.message.inbound;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

public class CnDocMsg extends DocMsg
{
    private CnHolder data;
    
    public CnHolder getData()
    {
        return data;
    }


    public void setData(CnHolder data)
    {
        this.data = data;
    }


    @Override
    public boolean isGeneratePdf()
    {
        return true;
    }


    @Override
    public MsgType getMsgType()
    {
        return MsgType.CN;
    }


    @Override
    public String computePdfFilename()
    {
        return this.getData().getHeader().computePdfFilename();
    }


    @Override
    public ReportEngineParameter<CnHolder> computeReportEngineParameter()
    {
        ReportEngineParameter<CnHolder> rlt = new ReportEngineParameter<CnHolder>();
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

}
