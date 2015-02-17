//*****************************************************************************
//
// File Name       :  DocMsgVisitor.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 9:15:26 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.visitor;

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

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public interface DocMsgVisitor
{
    public void visit(PoDocMsg doc) throws Exception;
    
    
    public void visit(GrnDocMsg doc) throws Exception;
    
    
    public void visit(PnDocMsg doc) throws Exception;
    
    
    public void visit(DnDocMsg doc) throws Exception;
    
    
    public void visit(RtvDocMsg doc) throws Exception;
    
    
    public void visit(DoDocMsg doc) throws Exception;
    
    
    public void visit(InvDocMsg doc) throws Exception;
    
    
    public void visit(AckDocMsg doc) throws Exception;
    
    
    public void visit(ItemInDocMsg doc) throws Exception;
    
    
    public void visit(PocnDocMsg doc) throws Exception;
    
    
    public void visit(GiDocMsg doc) throws Exception;
    
    
    public void visit(CcDocMsg doc) throws Exception;
    
    
    public void visit(SalesDocMsg doc) throws Exception;
    
    
    public void visit(CnDocMsg doc) throws Exception;
}
