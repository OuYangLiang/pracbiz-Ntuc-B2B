package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class InvDetailHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 2414580262997350020L;

    private Integer lineSeqNo;

    private BigDecimal invOid;

    private String buyerItemCode;

    private String supplierItemCode;

    private String barcode;

    private String itemDesc;

    private String brand;

    private String colourCode;

    private String colourDesc;

    private String sizeCode;

    private String sizeDesc;

    private BigDecimal packingFactor;

    private String invBaseUnit;

    private String invUom;

    private BigDecimal invQty;

    private String focBaseUnit;

    private String focUom;

    private BigDecimal focQty;

    private BigDecimal unitPrice;

    private BigDecimal discountAmount;

    private BigDecimal discountPercent;

    private BigDecimal netPrice;

    private BigDecimal itemAmount;

    private BigDecimal netAmount;

    private BigDecimal itemSharedCost;

    private BigDecimal itemGrossAmount;

    private String itemRemarks;
    
    private String lineRefNo;
    
    private BigDecimal totalPoSell;
    
    private BigDecimal totalCustomerDisc;
    
    private BigDecimal conTotalCost;
    
    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
    }

    public BigDecimal getInvOid()
    {
        return invOid;
    }

    public void setInvOid(BigDecimal invOid)
    {
        this.invOid = invOid;
    }

    public String getBuyerItemCode()
    {
        return buyerItemCode;
    }

    public void setBuyerItemCode(String buyerItemCode)
    {
        this.buyerItemCode = buyerItemCode == null ? null : buyerItemCode
            .trim();
    }

    public String getSupplierItemCode()
    {
        return supplierItemCode;
    }

    public void setSupplierItemCode(String supplierItemCode)
    {
        this.supplierItemCode = supplierItemCode == null ? null
            : supplierItemCode.trim();
    }

    public String getBarcode()
    {
        return barcode;
    }

    public void setBarcode(String barcode)
    {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getItemDesc()
    {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

    public String getBrand()
    {
        return brand;
    }

    public void setBrand(String brand)
    {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getColourCode()
    {
        return colourCode;
    }

    public void setColourCode(String colourCode)
    {
        this.colourCode = colourCode == null ? null : colourCode.trim();
    }

    public String getColourDesc()
    {
        return colourDesc;
    }

    public void setColourDesc(String colourDesc)
    {
        this.colourDesc = colourDesc == null ? null : colourDesc.trim();
    }

    public String getSizeCode()
    {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode)
    {
        this.sizeCode = sizeCode == null ? null : sizeCode.trim();
    }

    public String getSizeDesc()
    {
        return sizeDesc;
    }

    public void setSizeDesc(String sizeDesc)
    {
        this.sizeDesc = sizeDesc == null ? null : sizeDesc.trim();
    }

    public BigDecimal getPackingFactor()
    {
        return packingFactor;
    }

    public void setPackingFactor(BigDecimal packingFactor)
    {
        this.packingFactor = packingFactor;
    }

    public String getInvBaseUnit()
    {
        return invBaseUnit;
    }

    public void setInvBaseUnit(String invBaseUnit)
    {
        this.invBaseUnit = invBaseUnit == null ? null : invBaseUnit.trim();
    }

    public String getInvUom()
    {
        return invUom;
    }

    public void setInvUom(String invUom)
    {
        this.invUom = invUom == null ? null : invUom.trim();
    }

    public BigDecimal getInvQty()
    {
        return invQty;
    }

    public void setInvQty(BigDecimal invQty)
    {
        this.invQty = invQty;
    }

    public String getFocBaseUnit()
    {
        return focBaseUnit;
    }

    public void setFocBaseUnit(String focBaseUnit)
    {
        this.focBaseUnit = focBaseUnit == null ? null : focBaseUnit.trim();
    }

    public String getFocUom()
    {
        return focUom;
    }

    public void setFocUom(String focUom)
    {
        this.focUom = focUom == null ? null : focUom.trim();
    }

    public BigDecimal getFocQty()
    {
        return focQty;
    }

    public void setFocQty(BigDecimal focQty)
    {
        this.focQty = focQty;
    }

    public BigDecimal getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountPercent()
    {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent)
    {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getNetPrice()
    {
        return netPrice;
    }

    public void setNetPrice(BigDecimal netPrice)
    {
        this.netPrice = netPrice;
    }

    public BigDecimal getItemAmount()
    {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount)
    {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getNetAmount()
    {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount)
    {
        this.netAmount = netAmount;
    }

    public BigDecimal getItemSharedCost()
    {
        return itemSharedCost;
    }

    public void setItemSharedCost(BigDecimal itemSharedCost)
    {
        this.itemSharedCost = itemSharedCost;
    }

    public BigDecimal getItemGrossAmount()
    {
        return itemGrossAmount;
    }

    public void setItemGrossAmount(BigDecimal itemGrossAmount)
    {
        this.itemGrossAmount = itemGrossAmount;
    }

    public String getItemRemarks()
    {
        return itemRemarks;
    }

    public void setItemRemarks(String itemRemarks)
    {
        this.itemRemarks = itemRemarks == null ? null : itemRemarks.trim();
    }

    public String getLineRefNo()
    {
        return lineRefNo;
    }

    public void setLineRefNo(String lineRefNo)
    {
        this.lineRefNo = lineRefNo;
    }

    
    public BigDecimal getTotalPoSell()
    {
        return totalPoSell;
    }

    public void setTotalPoSell(BigDecimal totalPoSell)
    {
        this.totalPoSell = totalPoSell;
    }

    public BigDecimal getTotalCustomerDisc()
    {
        return totalCustomerDisc;
    }

    public void setTotalCustomerDisc(BigDecimal totalCustomerDisc)
    {
        this.totalCustomerDisc = totalCustomerDisc;
    }

    public BigDecimal getConTotalCost()
    {
        return conTotalCost;
    }

    public void setConTotalCost(BigDecimal conTotalCost)
    {
        this.conTotalCost = conTotalCost;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(lineSeqNo).append(delimiterChar)
            .append(buyerItemCode).append(delimiterChar)
            .append(supplierItemCode).append(delimiterChar)
            .append(barcode).append(delimiterChar)
            .append(itemDesc).append(delimiterChar)
            .append(brand).append(delimiterChar)
            .append(colourCode).append(delimiterChar)
            .append(colourDesc).append(delimiterChar)
            .append(sizeCode).append(delimiterChar)
            .append(sizeDesc).append(delimiterChar)
            .append(packingFactor).append(delimiterChar)
            .append(invBaseUnit).append(delimiterChar)
            .append(invUom).append(delimiterChar)
            .append(invQty).append(delimiterChar)
            .append(focBaseUnit).append(delimiterChar)
            .append(focUom).append(delimiterChar)
            .append(focQty).append(delimiterChar)
            .append(unitPrice).append(delimiterChar)
            .append(discountAmount).append(delimiterChar)
            .append(discountPercent).append(delimiterChar)
            .append(netPrice).append(delimiterChar)
            .append(itemAmount).append(delimiterChar)
            .append(netAmount).append(delimiterChar)
            .append(itemSharedCost).append(delimiterChar)
            .append(itemGrossAmount).append(delimiterChar)
            .append(itemRemarks);
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(invOid + "" + lineSeqNo);
    }
}