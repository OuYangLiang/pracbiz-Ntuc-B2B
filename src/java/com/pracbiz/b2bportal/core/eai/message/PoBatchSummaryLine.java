//*****************************************************************************
//
// File Name       :  PoBatchSummaryLine.java
// Date Created    :  May 15, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: May 15, 2013 1:16:37 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

import java.math.BigDecimal;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class PoBatchSummaryLine
{
    private String poNo;
    private BigDecimal numOfItems;
    private BigDecimal totalItemQty;
    private BigDecimal totalAmt;

    public String getPoNo()
    {
        return poNo;
    }

    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }

    public BigDecimal getNumOfItems()
    {
        return numOfItems;
    }

    public void setNumOfItems(BigDecimal numOfItems)
    {
        this.numOfItems = numOfItems;
    }

    public BigDecimal getTotalItemQty()
    {
        return totalItemQty;
    }

    public void setTotalItemQty(BigDecimal totalItemQty)
    {
        this.totalItemQty = totalItemQty;
    }

    public BigDecimal getTotalAmt()
    {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt)
    {
        this.totalAmt = totalAmt;
    }
}
