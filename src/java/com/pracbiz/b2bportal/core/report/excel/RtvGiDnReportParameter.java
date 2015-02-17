//*****************************************************************************
//
// File Name       :  RtvGiDnReportParameter.java
// Date Created    :  Apr 17, 2014
// Last Changed By :  $Author: eidt $
// Last Changed On :  $Date: Apr 17, 2014 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report.excel;

import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvGiDnReportParameter
{
    private String dnNo;
    
    private String giNo;
    
    private String rtvNo;
    
    private Date dnDate;
    
    private Date giDate;

    private Date rtvDate;
    
    private BigDecimal dnAmtWvat;
    
    private BigDecimal dnAmtWoVat;
    
    private BigDecimal dnUnitPrice;
    
    private BigDecimal rtvUnitPrice;
    
    private BigDecimal dnQty;
    
    private BigDecimal giQty;
    
    private BigDecimal rtvQty;
    
    private Date closedDate;
    
    private String buyerItemCode;
    
    private String itemDesc;
    
    private String buyerCode;
    
    private String supplierCode;
    
    private String supplierName;
    
    private String storeCode;
    
    private String storeName;
    
    private String classCode;
    
    private String uom;
    
    private Boolean offer;
    
    private BigDecimal rtvDnPriceDiff;
    
    private BigDecimal rtvDnPriceDiffPercent;
    
    private BigDecimal rtvGiQtyDiff;
    
    private BigDecimal rtvGiQtyDiffPercent;
    
    private BigDecimal giDnQtyDiff;
    
    private BigDecimal giDnQtyDiffPercent;
    
    private BigDecimal storeAmt;
    


    public String getDnNo()
    {
        return dnNo;
    }


    public void setDnNo(String dnNo)
    {
        this.dnNo = dnNo;
    }


    public String getGiNo()
    {
        return giNo;
    }


    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getDnDate()
    {
        return dnDate == null ? null : (Date)dnDate.clone();
    }


    public void setDnDate(Date dnDate)
    {
        this.dnDate = dnDate == null ? null : (Date)dnDate.clone();
    }


    public Date getGiDate()
    {
        return giDate == null ? null : (Date)giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date)giDate.clone();
    }


    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date)rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date)rtvDate.clone();
    }


    public BigDecimal getDnAmtWvat()
    {
        return dnAmtWvat;
    }


    public void setDnAmtWvat(BigDecimal dnAmtWvat)
    {
        this.dnAmtWvat = dnAmtWvat;
    }


    public BigDecimal getDnAmtWoVat()
    {
        return dnAmtWoVat;
    }


    public void setDnAmtWoVat(BigDecimal dnAmtWoVat)
    {
        this.dnAmtWoVat = dnAmtWoVat;
    }


    public BigDecimal getDnUnitPrice()
    {
        return dnUnitPrice;
    }


    public void setDnUnitPrice(BigDecimal dnUnitPrice)
    {
        this.dnUnitPrice = dnUnitPrice;
    }


    public BigDecimal getRtvUnitPrice()
    {
        return rtvUnitPrice;
    }


    public void setRtvUnitPrice(BigDecimal rtvUnitPrice)
    {
        this.rtvUnitPrice = rtvUnitPrice;
    }


    public BigDecimal getDnQty()
    {
        return dnQty;
    }


    public void setDnQty(BigDecimal dnQty)
    {
        this.dnQty = dnQty;
    }


    public BigDecimal getGiQty()
    {
        return giQty;
    }


    public void setGiQty(BigDecimal giQty)
    {
        this.giQty = giQty;
    }


    public BigDecimal getRtvQty()
    {
        return rtvQty;
    }


    public void setRtvQty(BigDecimal rtvQty)
    {
        this.rtvQty = rtvQty;
    }


    public Date getClosedDate()
    {
        return closedDate == null ? null : (Date)closedDate.clone();
    }


    public void setClosedDate(Date closedDate)
    {
        this.closedDate = closedDate == null ? null : (Date)closedDate.clone();
    }


    public String getBuyerItemCode()
    {
        return buyerItemCode;
    }


    public void setBuyerItemCode(String buyerItemCode)
    {
        this.buyerItemCode = buyerItemCode;
    }


    public String getItemDesc()
    {
        return itemDesc;
    }


    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
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


    public BigDecimal getRtvDnPriceDiff()
    {
        return rtvDnPriceDiff;
    }


    public void setRtvDnPriceDiff(BigDecimal rtvDnPriceDiff)
    {
        this.rtvDnPriceDiff = rtvDnPriceDiff;
    }


    public BigDecimal getRtvGiQtyDiff()
    {
        return rtvGiQtyDiff;
    }


    public void setRtvGiQtyDiff(BigDecimal rtvGiQtyDiff)
    {
        this.rtvGiQtyDiff = rtvGiQtyDiff;
    }


    public BigDecimal getGiDnQtyDiff()
    {
        return giDnQtyDiff;
    }


    public void setGiDnQtyDiff(BigDecimal giDnQtyDiff)
    {
        this.giDnQtyDiff = giDnQtyDiff;
    }


    public BigDecimal getStoreAmt()
    {
        return storeAmt;
    }


    public void setStoreAmt(BigDecimal storeAmt)
    {
        this.storeAmt = storeAmt;
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public String getStoreName()
    {
        return storeName;
    }


    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    public String getUom()
    {
        return uom;
    }


    public void setUom(String uom)
    {
        this.uom = uom;
    }


    public BigDecimal getRtvDnPriceDiffPercent()
    {
        return rtvDnPriceDiffPercent;
    }


    public void setRtvDnPriceDiffPercent(BigDecimal rtvDnPriceDiffPercent)
    {
        this.rtvDnPriceDiffPercent = rtvDnPriceDiffPercent;
    }


    public BigDecimal getRtvGiQtyDiffPercent()
    {
        return rtvGiQtyDiffPercent;
    }


    public void setRtvGiQtyDiffPercent(BigDecimal rtvGiQtyDiffPercent)
    {
        this.rtvGiQtyDiffPercent = rtvGiQtyDiffPercent;
    }


    public BigDecimal getGiDnQtyDiffPercent()
    {
        return giDnQtyDiffPercent;
    }


    public void setGiDnQtyDiffPercent(BigDecimal giDnQtyDiffPercent)
    {
        this.giDnQtyDiffPercent = giDnQtyDiffPercent;
    }


    public Boolean getOffer()
    {
        return offer;
    }


    public void setOffer(Boolean offer)
    {
        this.offer = offer;
    }
    
}
