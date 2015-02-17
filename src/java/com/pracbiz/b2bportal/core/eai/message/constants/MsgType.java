//*****************************************************************************
//
// File Name       :  MsgType.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 10:31:58 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.core.eai.message.AckDocMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
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
public enum MsgType
{
    PO("MsgType.PO", Direction.outbound),
    POACK("MsgType.POACK", Direction.inbound),
    GRN("MsgType.GRN", Direction.outbound),
    GRNACK("MsgType.GRNACK", Direction.inbound),
    DN("MsgType.DN", Direction.outbound),
    DNACK("MsgType.DNACK", Direction.inbound),
    PN("MsgType.PN", Direction.outbound),
    PNACK("MsgType.PNACK", Direction.inbound),
    RTV("MsgType.RTV", Direction.outbound),
    RTVACK("MsgType.RTVACK", Direction.inbound),
    POCN("MsgType.POCN", Direction.outbound),
    GI("MsgType.GI", Direction.outbound),
    GIACK("MsgType.GIACK", Direction.inbound),
    CC("MsgType.CC", Direction.outbound),
    CCACK("MsgType.CCACK", Direction.inbound),
    CN("MsgType.CN", Direction.inbound),
    
    SM("MsgType.SM", Direction.outbound),
    INV("MsgType.INV", Direction.inbound),
    INVACK("MsgType.INVACK", Direction.outbound),
    DO("MsgType.DO", Direction.inbound),
    DOACK("MsgType.DOACK", Direction.outbound),
    ST("MsgType.ST", Direction.outbound),
    UM("MsgType.UM", Direction.outbound),
    SA("MsgType.SA", Direction.outbound),
    SL("MsgType.SL", Direction.outbound),
    DPR("MsgType.DPR", Direction.outbound),
    ITEMIN("MsgType.ITEMIN", Direction.inbound),
    DSD("MsgType.DSD", Direction.outbound);

    private String key;
    private Direction direction;

    private MsgType(String key, Direction direction)
    {
        this.key = key;
        this.direction = direction;
    }

    public String getKey()
    {
        return key;
    }
    
    public Direction getDirection()
    {
        return direction;
    }

    public DocMsg initDocMsg()
    {
        DocMsg rlt = null;
        
        switch(this)
        {
            case PO:
                rlt = new PoDocMsg();
                break;
            case GRN:
                rlt = new GrnDocMsg();
                break;
            case DN:
                rlt = new DnDocMsg();
                break;
            case PN:
                rlt = new PnDocMsg();
                break;
            case RTV:
                rlt = new RtvDocMsg();
                break;
            case INV:
                rlt = new InvDocMsg();
                break;
            case DO:
                rlt = new DoDocMsg();
                break;
            case ITEMIN:
                rlt = new ItemInDocMsg();
                break;
            case POCN:
                rlt = new PocnDocMsg();
                break;
            case GI:
                rlt = new GiDocMsg();
                break;
            case CC:
                rlt = new CcDocMsg();
                break;
            case DSD:
                rlt = new SalesDocMsg();
                break;
            case CN:
                rlt = new CnDocMsg();
                break;
            case POACK:
            case GRNACK:
            case DNACK:
            case PNACK:
            case RTVACK:
            case INVACK:
            case DOACK:
            case GIACK:
            case CCACK:
                rlt = new AckDocMsg(this);
                break;
            default:
            	break;
        }
        
        return rlt;
    }

    
    public static Map<String, String> toMapValue()
    {
        Map<String, String> rlt = new HashMap<String, String>();
        for(MsgType mt : MsgType.values())
        {
            rlt.put(mt.name(), mt.getKey());
        }
        return rlt;
    }
}
