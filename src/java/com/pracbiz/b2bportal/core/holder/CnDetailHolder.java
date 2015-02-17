package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

public class CnDetailHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Integer lineSeqNo;
    private BigDecimal cnOid;
    private String buyerItemCode;
    private String supplierItemCode;
    private String barcode;
    private String itemDesc;
    private String brand;
    private String colourCode;
    private String colourDesc;
    private String sizeCode;
    private String sizeDesc;
    private String poNo;
    private Date poDate;
    private String invNo;
    private Date invDate;
    private String rtvNo;
    private Date rtvDate;
    private String giNo;
    private Date giDate;
    private BigDecimal packingFactor;
    private String creditBaseUnit;
    private String creditUom;
    private BigDecimal creditQty;
    private BigDecimal unitCost;
    private BigDecimal costDiscountAmount;
    private BigDecimal costDiscountPercent;
    private BigDecimal retailDiscountAmount;
    private BigDecimal netUnitCost;
    private BigDecimal itemCost;
    private BigDecimal itemSharedCost;
    private BigDecimal itemGrossCost;
    private BigDecimal retailPrice;
    private BigDecimal itemRetailAmount;
    private String reasonCode;
    private String reasonDesc;
    private String lineRefNo;


    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }


    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
    }


    public BigDecimal getCnOid()
    {
        return cnOid;
    }


    public void setCnOid(BigDecimal cnOid)
    {
        this.cnOid = cnOid;
    }


    public String getBuyerItemCode()
    {
        return buyerItemCode;
    }


    public void setBuyerItemCode(String buyerItemCode)
    {
        this.buyerItemCode = buyerItemCode;
    }


    public String getSupplierItemCode()
    {
        return supplierItemCode;
    }


    public void setSupplierItemCode(String supplierItemCode)
    {
        this.supplierItemCode = supplierItemCode;
    }


    public String getBarcode()
    {
        return barcode;
    }


    public void setBarcode(String barcode)
    {
        this.barcode = barcode;
    }


    public String getItemDesc()
    {
        return itemDesc;
    }


    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }


    public String getBrand()
    {
        return brand;
    }


    public void setBrand(String brand)
    {
        this.brand = brand;
    }


    public String getColourCode()
    {
        return colourCode;
    }


    public void setColourCode(String colourCode)
    {
        this.colourCode = colourCode;
    }


    public String getColourDesc()
    {
        return colourDesc;
    }


    public void setColourDesc(String colourDesc)
    {
        this.colourDesc = colourDesc;
    }


    public String getSizeCode()
    {
        return sizeCode;
    }


    public void setSizeCode(String sizeCode)
    {
        this.sizeCode = sizeCode;
    }


    public String getSizeDesc()
    {
        return sizeDesc;
    }


    public void setSizeDesc(String sizeDesc)
    {
        this.sizeDesc = sizeDesc;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public Date getPoDate()
    {
        return poDate == null ? null : (Date) poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date) poDate.clone();
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public Date getInvDate()
    {
        return invDate == null ? null : (Date) invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date) invDate.clone();
    }


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public String getGiNo()
    {
        return giNo;
    }


    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }


    public Date getGiDate()
    {
        return giDate == null ? null : (Date) giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date) giDate.clone();
    }


    public BigDecimal getPackingFactor()
    {
        return packingFactor;
    }


    public void setPackingFactor(BigDecimal packingFactor)
    {
        this.packingFactor = packingFactor;
    }


    public String getCreditBaseUnit()
    {
        return creditBaseUnit;
    }


    public void setCreditBaseUnit(String creditBaseUnit)
    {
        this.creditBaseUnit = creditBaseUnit;
    }


    public String getCreditUom()
    {
        return creditUom;
    }


    public void setCreditUom(String creditUom)
    {
        this.creditUom = creditUom;
    }


    public BigDecimal getCreditQty()
    {
        return creditQty;
    }


    public void setCreditQty(BigDecimal creditQty)
    {
        this.creditQty = creditQty;
    }


    public BigDecimal getUnitCost()
    {
        return unitCost;
    }


    public void setUnitCost(BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }


    public BigDecimal getCostDiscountAmount()
    {
        return costDiscountAmount;
    }


    public void setCostDiscountAmount(BigDecimal costDiscountAmount)
    {
        this.costDiscountAmount = costDiscountAmount;
    }


    public BigDecimal getCostDiscountPercent()
    {
        return costDiscountPercent;
    }


    public void setCostDiscountPercent(BigDecimal costDiscountPercent)
    {
        this.costDiscountPercent = costDiscountPercent;
    }


    public BigDecimal getRetailDiscountAmount()
    {
        return retailDiscountAmount;
    }


    public void setRetailDiscountAmount(BigDecimal retailDiscountAmount)
    {
        this.retailDiscountAmount = retailDiscountAmount;
    }


    public BigDecimal getNetUnitCost()
    {
        return netUnitCost;
    }


    public void setNetUnitCost(BigDecimal netUnitCost)
    {
        this.netUnitCost = netUnitCost;
    }


    public BigDecimal getItemCost()
    {
        return itemCost;
    }


    public void setItemCost(BigDecimal itemCost)
    {
        this.itemCost = itemCost;
    }


    public BigDecimal getItemSharedCost()
    {
        return itemSharedCost;
    }


    public void setItemSharedCost(BigDecimal itemSharedCost)
    {
        this.itemSharedCost = itemSharedCost;
    }


    public BigDecimal getItemGrossCost()
    {
        return itemGrossCost;
    }


    public void setItemGrossCost(BigDecimal itemGrossCost)
    {
        this.itemGrossCost = itemGrossCost;
    }


    public BigDecimal getRetailPrice()
    {
        return retailPrice;
    }


    public void setRetailPrice(BigDecimal retailPrice)
    {
        this.retailPrice = retailPrice;
    }


    public BigDecimal getItemRetailAmount()
    {
        return itemRetailAmount;
    }


    public void setItemRetailAmount(BigDecimal itemRetailAmount)
    {
        this.itemRetailAmount = itemRetailAmount;
    }


    public String getReasonCode()
    {
        return reasonCode;
    }


    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode;
    }


    public String getReasonDesc()
    {
        return reasonDesc;
    }


    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc;
    }


    public String getLineRefNo()
    {
        return lineRefNo;
    }


    public void setLineRefNo(String lineRefNo)
    {
        this.lineRefNo = lineRefNo;
    }
    
    
    public String toStringWithDelimiterCharacter(String delimiterChar)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lineSeqNo)
                .append(delimiterChar)
                .append(buyerItemCode)
                .append(delimiterChar)
                .append(supplierItemCode)
                .append(delimiterChar)
                .append(barcode)
                .append(delimiterChar)
                .append(itemDesc)
                .append(delimiterChar)
                .append(brand)
                .append(delimiterChar)
                .append(colourCode)
                .append(delimiterChar)
                .append(colourDesc)
                .append(delimiterChar)
                .append(sizeCode)
                .append(delimiterChar)
                .append(sizeDesc)
                .append(delimiterChar)
                .append(poNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(poDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(invNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(invDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(rtvNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(rtvDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(giNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(giDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar).append(packingFactor)
                .append(delimiterChar).append(creditBaseUnit)
                .append(delimiterChar).append(creditUom).append(delimiterChar)
                .append(creditQty).append(delimiterChar).append(unitCost)
                .append(delimiterChar).append(costDiscountAmount)
                .append(delimiterChar).append(costDiscountPercent)
                .append(delimiterChar).append(retailDiscountAmount)
                .append(delimiterChar).append(netUnitCost)
                .append(delimiterChar).append(itemCost).append(delimiterChar)
                .append(itemSharedCost).append(delimiterChar)
                .append(itemGrossCost).append(delimiterChar)
                .append(retailPrice).append(delimiterChar)
                .append(itemRetailAmount).append(delimiterChar)
                .append(reasonCode).append(delimiterChar)
                .append(reasonDesc);

        return buffer.toString();
    }
    

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(cnOid) + lineSeqNo;
    }
}