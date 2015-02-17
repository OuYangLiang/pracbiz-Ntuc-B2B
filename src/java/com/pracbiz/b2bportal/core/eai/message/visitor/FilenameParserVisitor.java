//*****************************************************************************
//
// File Name       :  FilenameParserVisitor.java
// Date Created    :  Nov 26, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 26, 2012 3:37:41 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.visitor;


import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.message.AckDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.DoDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.ItemInDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.CcDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.DnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GiDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PocnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.SalesDocMsg;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DoHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class FilenameParserVisitor implements DocMsgVisitor
{
    private DocFileHandler<PoDocMsg, PoHolder> poDocFileHanlder;
    private DocFileHandler<GrnDocMsg, GrnHolder> grnDocFileHanlder;
    private DocFileHandler<DnDocMsg, DnHolder> dnDocFileHanlder;
    private DocFileHandler<PnDocMsg, PnHolder> pnDocFileHanlder;
    private DocFileHandler<RtvDocMsg, RtvHolder> rtvDocFileHanlder;
    private DocFileHandler<InvDocMsg, InvHolder> invDocFileHanlder;
    private DocFileHandler<DoDocMsg, DoHolder> doDocFileHanlder;
    private DocFileHandler<AckDocMsg, Object> ackDocFileHanlder;
    private DocFileHandler<ItemInDocMsg, Object> itemInDocFileHandler;
    private DocFileHandler<PocnDocMsg, PoHolder> pocnDocFileHandler;
    private DocFileHandler<GiDocMsg, GiHolder> giDocFileHandler;
    private DocFileHandler<CcDocMsg, CcHolder> ccDocFileHandler;
    private DocFileHandler<SalesDocMsg, SalesHolder> salesDocFileHandler;
    private DocFileHandler<CnDocMsg, CnHolder> cnDocFileHandler;

    @Override
    public void visit(PoDocMsg doc) throws Exception
    {
        poDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(GrnDocMsg doc) throws Exception
    {
        grnDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(PnDocMsg doc) throws Exception
    {
        pnDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(DnDocMsg doc) throws Exception
    {
        dnDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(RtvDocMsg doc) throws Exception
    {
        rtvDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(InvDocMsg doc) throws Exception
    {
        invDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(DoDocMsg doc) throws Exception
    {
        doDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }

    @Override
    public void visit(AckDocMsg doc) throws Exception
    {
        ackDocFileHanlder.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(ItemInDocMsg doc) throws Exception
    {
        itemInDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(PocnDocMsg doc) throws Exception
    {
        pocnDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(GiDocMsg doc) throws Exception
    {
        giDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(CcDocMsg doc) throws Exception
    {
        ccDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(SalesDocMsg doc) throws Exception
    {
        salesDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }
    
    @Override
    public void visit(CnDocMsg doc) throws Exception
    {
        cnDocFileHandler.fillFilenameInfoToDocMsg(doc);
    }

    //*****************************************************
    // private methods
    //*****************************************************
    public void setPoDocFileHanlder(DocFileHandler<PoDocMsg, PoHolder> poDocFileHanlder)
    {
        this.poDocFileHanlder = poDocFileHanlder;
    }

    public void setGrnDocFileHanlder(DocFileHandler<GrnDocMsg, GrnHolder> grnDocFileHanlder)
    {
        this.grnDocFileHanlder = grnDocFileHanlder;
    }

    public void setDnDocFileHanlder(DocFileHandler<DnDocMsg, DnHolder> dnDocFileHanlder)
    {
        this.dnDocFileHanlder = dnDocFileHanlder;
    }

    public void setPnDocFileHanlder(DocFileHandler<PnDocMsg, PnHolder> pnDocFileHanlder)
    {
        this.pnDocFileHanlder = pnDocFileHanlder;
    }

    public void setRtvDocFileHanlder(DocFileHandler<RtvDocMsg, RtvHolder> rtvDocFileHanlder)
    {
        this.rtvDocFileHanlder = rtvDocFileHanlder;
    }

    public void setInvDocFileHanlder(DocFileHandler<InvDocMsg, InvHolder> invDocFileHanlder)
    {
        this.invDocFileHanlder = invDocFileHanlder;
    }

    public void setDoDocFileHanlder(DocFileHandler<DoDocMsg, DoHolder> doDocFileHanlder)
    {
        this.doDocFileHanlder = doDocFileHanlder;
    }

    public void setAckDocFileHanlder(DocFileHandler<AckDocMsg, Object> ackDocFileHanlder)
    {
        this.ackDocFileHanlder = ackDocFileHanlder;
    }

    public void setPocnDocFileHandler(DocFileHandler<PocnDocMsg, PoHolder> pocnDocFileHandler)
    {
        this.pocnDocFileHandler = pocnDocFileHandler;
    }

    public void setItemInDocFileHandler(
            DocFileHandler<ItemInDocMsg, Object> itemInDocFileHandler)
    {
        this.itemInDocFileHandler = itemInDocFileHandler;
    }

    public void setGiDocFileHandler(DocFileHandler<GiDocMsg, GiHolder> giDocFileHandler)
    {
        this.giDocFileHandler = giDocFileHandler;
    }

    public void setCcDocFileHandler(DocFileHandler<CcDocMsg, CcHolder> ccDocFileHandler)
    {
        this.ccDocFileHandler = ccDocFileHandler;
    }

    public void setSalesDocFileHandler(
        DocFileHandler<SalesDocMsg, SalesHolder> salesDocFileHandler)
    {
        this.salesDocFileHandler = salesDocFileHandler;
    }

    public DocFileHandler<CnDocMsg, CnHolder> getCnDocFileHandler()
    {
        return cnDocFileHandler;
    }

    public void setCnDocFileHandler(
            DocFileHandler<CnDocMsg, CnHolder> cnDocFileHandler)
    {
        this.cnDocFileHandler = cnDocFileHandler;
    }

}
