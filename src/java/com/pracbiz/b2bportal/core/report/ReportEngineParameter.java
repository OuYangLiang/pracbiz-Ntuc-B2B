//*****************************************************************************
//
// File Name       :  ReportEngineParameter.java
// Date Created    :  Dec 12, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Dec 12, 2012 2:55:54 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

/**
 * TODO To provide an overview of this class.
 * class parameter T will be replaced with concrete holder class,
 * e.g. POHolder, GrnHolder, etc.
 * 
 * @author ouyang
 */
public class ReportEngineParameter<T>
{
    private T data;
    private MsgTransactionsHolder msgTransactions;
    private BuyerHolder buyer;
    private SupplierHolder supplier;

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public MsgTransactionsHolder getMsgTransactions()
    {
        return msgTransactions;
    }

    public void setMsgTransactions(MsgTransactionsHolder msgTransactions)
    {
        this.msgTransactions = msgTransactions;
    }

    public BuyerHolder getBuyer()
    {
        return buyer;
    }

    public void setBuyer(BuyerHolder buyer)
    {
        this.buyer = buyer;
    }

    public SupplierHolder getSupplier()
    {
        return supplier;
    }

    public void setSupplier(SupplierHolder supplier)
    {
        this.supplier = supplier;
    }
}
