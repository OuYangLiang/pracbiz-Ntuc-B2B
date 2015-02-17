package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GiDetailHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal giOid;

    private Integer lineSeqNo;

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

    private String rtvBaseUnit;

    private String rtvUom;

    private BigDecimal rtvQty;

    private BigDecimal issuedQty;

    private BigDecimal unitCost;

    private BigDecimal itemCost;

    private String itemRemarks;


    public BigDecimal getGiOid()
    {
        return giOid;
    }


    public void setGiOid(BigDecimal giOid)
    {
        this.giOid = giOid;
    }


    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }


    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
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


    public BigDecimal getPackingFactor()
    {
        return packingFactor;
    }


    public void setPackingFactor(BigDecimal packingFactor)
    {
        this.packingFactor = packingFactor;
    }


    public String getRtvBaseUnit()
    {
        return rtvBaseUnit;
    }


    public void setRtvBaseUnit(String rtvBaseUnit)
    {
        this.rtvBaseUnit = rtvBaseUnit;
    }


    public String getRtvUom()
    {
        return rtvUom;
    }


    public void setRtvUom(String rtvUom)
    {
        this.rtvUom = rtvUom;
    }


    public BigDecimal getRtvQty()
    {
        return rtvQty;
    }


    public void setRtvQty(BigDecimal rtvQty)
    {
        this.rtvQty = rtvQty;
    }


    public BigDecimal getIssuedQty()
    {
        return issuedQty;
    }


    public void setIssuedQty(BigDecimal issuedQty)
    {
        this.issuedQty = issuedQty;
    }


    public BigDecimal getUnitCost()
    {
        return unitCost;
    }


    public void setUnitCost(BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }


    public BigDecimal getItemCost()
    {
        return itemCost;
    }


    public void setItemCost(BigDecimal itemCost)
    {
        this.itemCost = itemCost;
    }


    public String getItemRemarks()
    {
        return itemRemarks;
    }


    public void setItemRemarks(String itemRemarks)
    {
        this.itemRemarks = itemRemarks;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(giOid + "" + lineSeqNo);
    }

}