//*****************************************************************************
//
// File Name       :  TradingPartnerExHolder.java
// Date Created    :  Sep 4, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 4, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class TradingPartnerExHolder extends TradingPartnerHolder
{
    private static final long serialVersionUID = -3934581258666314969L;
    private String buyerCode;
    private String buyerName;
    private String supplierCode;
    private String supplierName;
    private List<BigDecimal> tradingPartnerOids;
    private String tradingPartnerDesc;
    private String activeStatusValue;
    private String termCondition1;
    private String termCondition2;
    private String termCondition3;
    private String termCondition4;
    private String supplierAddr1;
    private String supplierAddr2;
    private String supplierAddr3;
    private String supplierAddr4;
    private boolean filterWithSupplier;
    private List<BigDecimal> supplierOids;


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    public List<BigDecimal> getTradingPartnerOids()
    {
        return tradingPartnerOids;
    }


    public void setTradingPartnerOids(List<BigDecimal> tradingPartnerOids)
    {
        this.tradingPartnerOids = tradingPartnerOids;
    }


    public void addTradingPartnerOid(BigDecimal tradingPartnerOid)
    {
        if (tradingPartnerOids == null)
        {
            tradingPartnerOids = new ArrayList<BigDecimal>();
        }

        tradingPartnerOids.add(tradingPartnerOid);
    }


    public String getTradingPartnerDesc()
    {
        return tradingPartnerDesc;
    }


    public void setTradingPartnerDesc(String tradingPartnerDesc)
    {
        this.tradingPartnerDesc = tradingPartnerDesc;
    }


    public String getActiveStatusValue()
    {
        return activeStatusValue;
    }


    public void setActiveStatusValue(String activeStatusValue)
    {
        this.activeStatusValue = activeStatusValue;
    }


    public String getTermCondition1()
    {
        return termCondition1;
    }


    public void setTermCondition1(String termCondition1)
    {
        this.termCondition1 = termCondition1;
    }


    public String getTermCondition2()
    {
        return termCondition2;
    }


    public void setTermCondition2(String termCondition2)
    {
        this.termCondition2 = termCondition2;
    }


    public String getTermCondition3()
    {
        return termCondition3;
    }


    public void setTermCondition3(String termCondition3)
    {
        this.termCondition3 = termCondition3;
    }


    public String getTermCondition4()
    {
        return termCondition4;
    }


    public void setTermCondition4(String termCondition4)
    {
        this.termCondition4 = termCondition4;
    }


    public String getSupplierAddr1()
    {
        return supplierAddr1;
    }


    public void setSupplierAddr1(String supplierAddr1)
    {
        this.supplierAddr1 = supplierAddr1;
    }


    public String getSupplierAddr2()
    {
        return supplierAddr2;
    }


    public void setSupplierAddr2(String supplierAddr2)
    {
        this.supplierAddr2 = supplierAddr2;
    }


    public String getSupplierAddr3()
    {
        return supplierAddr3;
    }


    public void setSupplierAddr3(String supplierAddr3)
    {
        this.supplierAddr3 = supplierAddr3;
    }


    public String getSupplierAddr4()
    {
        return supplierAddr4;
    }


    public void setSupplierAddr4(String supplierAddr4)
    {
        this.supplierAddr4 = supplierAddr4;
    }


    public boolean isFilterWithSupplier()
    {
        return filterWithSupplier;
    }


    public void setFilterWithSupplier(boolean filterWithSupplier)
    {
        this.filterWithSupplier = filterWithSupplier;
    }


    public List<BigDecimal> getSupplierOids()
    {
        return supplierOids;
    }


    public void setSupplierOids(List<BigDecimal> supplierOids)
    {
        this.supplierOids = supplierOids;
    }

    
    public void addSupplierOid(BigDecimal supplierOid)
    {
        if (supplierOids == null)
        {
            supplierOids = new ArrayList<BigDecimal>();
        }
        
       supplierOids.add(supplierOid);
    }
}
