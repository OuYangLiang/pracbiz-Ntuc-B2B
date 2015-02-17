//*****************************************************************************
//
// File Name       :  TransformerVisitor.java
// Date Created    :  Nov 26, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 26, 2012 5:35:15 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.visitor;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.ItemInDocFileHandler;
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
import com.pracbiz.b2bportal.core.service.DnService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class TransformerVisitor implements DocMsgVisitor
{
    private DocFileHandler<PoDocMsg, PoHolder> poDocFileHanlder;
    private DocFileHandler<GrnDocMsg, GrnHolder> grnDocFileHanlder;
    private DocFileHandler<DnDocMsg, DnHolder> dnDocFileHanlder;
    private DocFileHandler<PnDocMsg, PnHolder> pnDocFileHanlder;
    private DocFileHandler<RtvDocMsg, RtvHolder> rtvDocFileHanlder;
    private DocFileHandler<InvDocMsg, InvHolder> invDocFileHanlder;
    private DocFileHandler<DoDocMsg, DoHolder> doDocFileHanlder;
    private ItemInDocFileHandler itemInDocFileHandler;
    private DocFileHandler<GiDocMsg, GiHolder> giDocFileHanlder;
    private DocFileHandler<CcDocMsg, CcHolder> ccDocFileHandler;
    private DocFileHandler<SalesDocMsg, SalesHolder> salesDocFileHandler;
    private DocFileHandler<CnDocMsg, CnHolder> cnDocFileHandler;
    //private DocFileHandler<AckDocMsg> ackDocFileHanlder;
    
    @Autowired
    private DnService dnService;
    
    @Override
    public void visit(PoDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            poDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            poDocFileHanlder.parseSourceFile(doc);
            poDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(GrnDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            grnDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            grnDocFileHanlder.parseSourceFile(doc);
            grnDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(PnDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            pnDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            pnDocFileHanlder.parseSourceFile(doc);
            pnDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(DnDocMsg doc) throws Exception
    {
        if (doc.isOriginalFileGeneratedOnPortal())
        {
            doc.setData(dnService.selectDnByKey(doc.getDocOid()));
        }
        else
        {
            dnDocFileHanlder.parseSourceFile(doc);
        }
        
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            dnDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(RtvDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            rtvDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            rtvDocFileHanlder.parseSourceFile(doc);
            rtvDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(DoDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            doDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            doDocFileHanlder.parseSourceFile(doc);
            doDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(InvDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            invDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            invDocFileHanlder.parseSourceFile(doc);
            invDocFileHanlder.createTargerFile(doc);
        }
    }

    @Override
    public void visit(PocnDocMsg doc) throws Exception
    {
        doc.setTargetFilename(doc.getOriginalFilename());
    }

    @Override
    public void visit(AckDocMsg doc) throws Exception
    {
        // unnecessary
    }
    
    @Override
    public void visit(ItemInDocMsg doc) throws Exception
    {
        itemInDocFileHandler.translate(doc);
    }
    
    @Override
    public void visit(GiDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            giDocFileHanlder.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            giDocFileHanlder.parseSourceFile(doc);
            giDocFileHanlder.createTargerFile(doc);
        }
    }
    
    @Override
    public void visit(CcDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            ccDocFileHandler.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            ccDocFileHandler.parseSourceFile(doc);
            ccDocFileHandler.createTargerFile(doc);
        }
    }
    
    @Override
    public void visit(SalesDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
            doc.getInputFormat()))
        {
            salesDocFileHandler.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            salesDocFileHandler.parseSourceFile(doc);
            salesDocFileHandler.createTargerFile(doc);
        }
    }
    
    @Override
    public void visit(CnDocMsg doc) throws Exception
    {
        if(doc.getOutputFormat().equalsIgnoreCase(
                doc.getInputFormat()))
        {
            cnDocFileHandler.parseSourceFile(doc);
            doc.setTargetFilename(doc.getOriginalFilename());
        }
        else
        {
            cnDocFileHandler.parseSourceFile(doc);
            cnDocFileHandler.createTargerFile(doc);
        }
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


    public void setItemInDocFileHandler(ItemInDocFileHandler itemInDocFileHandler)
    {
        this.itemInDocFileHandler = itemInDocFileHandler;
    }

    public void setGiDocFileHanlder(DocFileHandler<GiDocMsg, GiHolder> giDocFileHanlder)
    {
        this.giDocFileHanlder = giDocFileHanlder;
    }

    public void setCcDocFileHandler(
        DocFileHandler<CcDocMsg, CcHolder> ccDocFileHandler)
    {
        this.ccDocFileHandler = ccDocFileHandler;
    }

    public void setSalesDocFileHandler(
        DocFileHandler<SalesDocMsg, SalesHolder> salesDocFileHandler)
    {
        this.salesDocFileHandler = salesDocFileHandler;
    }

    public void setCnDocFileHandler(
            DocFileHandler<CnDocMsg, CnHolder> cnDocFileHandler)
    {
        this.cnDocFileHandler = cnDocFileHandler;
    }
    

//    public void setAckDocFileHanlder(DocFileHandler<AckDocMsg> ackDocFileHanlder)
//    {
//        this.ackDocFileHanlder = ackDocFileHanlder;
//    }
}
