//*****************************************************************************
//
// File Name       :  BuyerOperationHolder.java
// Date Created    :  Dec 17, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 17, 2012 1:31:09 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class BuyerOperationHolder extends BaseHolder
{
    private static final long serialVersionUID = 1565345518304734570L;
    private BigDecimal buyerOid;
    private String opnId;

    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }

    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }

    public String getOpnId()
    {
        return opnId;
    }

    public void setOpnId(String opnId)
    {
        this.opnId = opnId;
    }

    @Override
    public String getCustomIdentification()
    {
        return buyerOid == null ? null : buyerOid.toString() + opnId;
    }
    
}
