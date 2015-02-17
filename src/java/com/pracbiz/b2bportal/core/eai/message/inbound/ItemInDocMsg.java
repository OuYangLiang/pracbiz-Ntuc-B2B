package com.pracbiz.b2bportal.core.eai.message.inbound;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

public class ItemInDocMsg extends DocMsg
{
    @Override
    public boolean isGeneratePdf()
    {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public MsgType getMsgType()
    {
        return MsgType.ITEMIN;
    }


    @Override
    public String computePdfFilename()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public ReportEngineParameter<BaseHolder> computeReportEngineParameter()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void accept(DocMsgVisitor visitor) throws Exception
    {
        visitor.visit(this);
    }

}
