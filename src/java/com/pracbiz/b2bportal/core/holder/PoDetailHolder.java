package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class PoDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 5930404233684727208L;

    private BigDecimal poOid;

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

    private String orderBaseUnit;

    private String orderUom;

    private BigDecimal orderQty;

    private String focBaseUnit;

    private String focUom;

    private BigDecimal focQty;

    private BigDecimal unitCost;

    private BigDecimal packCost;

    private BigDecimal costDiscountAmount;

    private BigDecimal costDiscountPercent;

    private BigDecimal retailDiscountAmount;

    private BigDecimal netUnitCost;

    private BigDecimal netPackCost;

    private BigDecimal itemCost;

    private BigDecimal itemSharedCost;

    private BigDecimal itemGrossCost;

    private BigDecimal retailPrice;

    private BigDecimal itemRetailAmount;

    private String itemRemarks;

    public BigDecimal getPoOid() {
        return poOid;
    }

    public void setPoOid(BigDecimal poOid) {
        this.poOid = poOid;
    }

    public Integer getLineSeqNo() {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo) {
        this.lineSeqNo = lineSeqNo;
    }

    public String getBuyerItemCode() {
        return buyerItemCode;
    }

    public void setBuyerItemCode(String buyerItemCode) {
        this.buyerItemCode = buyerItemCode == null ? null : buyerItemCode.trim();
    }

    public String getSupplierItemCode() {
        return supplierItemCode;
    }

    public void setSupplierItemCode(String supplierItemCode) {
        this.supplierItemCode = supplierItemCode == null ? null : supplierItemCode.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand == null ? null : brand.trim();
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode == null ? null : colourCode.trim();
    }

    public String getColourDesc() {
        return colourDesc;
    }

    public void setColourDesc(String colourDesc) {
        this.colourDesc = colourDesc == null ? null : colourDesc.trim();
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode == null ? null : sizeCode.trim();
    }

    public String getSizeDesc() {
        return sizeDesc;
    }

    public void setSizeDesc(String sizeDesc) {
        this.sizeDesc = sizeDesc == null ? null : sizeDesc.trim();
    }

    public BigDecimal getPackingFactor() {
        return packingFactor;
    }

    public void setPackingFactor(BigDecimal packingFactor) {
        this.packingFactor = packingFactor;
    }

    public String getOrderBaseUnit() {
        return orderBaseUnit;
    }

    public void setOrderBaseUnit(String orderBaseUnit) {
        this.orderBaseUnit = orderBaseUnit == null ? null : orderBaseUnit.trim();
    }

    public String getOrderUom() {
        return orderUom;
    }

    public void setOrderUom(String orderUom) {
        this.orderUom = orderUom == null ? null : orderUom.trim();
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public String getFocBaseUnit() {
        return focBaseUnit;
    }

    public void setFocBaseUnit(String focBaseUnit) {
        this.focBaseUnit = focBaseUnit == null ? null : focBaseUnit.trim();
    }

    public String getFocUom() {
        return focUom;
    }

    public void setFocUom(String focUom) {
        this.focUom = focUom == null ? null : focUom.trim();
    }

    public BigDecimal getFocQty() {
        return focQty;
    }

    public void setFocQty(BigDecimal focQty) {
        this.focQty = focQty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getPackCost() {
        return packCost;
    }

    public void setPackCost(BigDecimal packCost) {
        this.packCost = packCost;
    }

    public BigDecimal getCostDiscountAmount() {
        return costDiscountAmount;
    }

    public void setCostDiscountAmount(BigDecimal costDiscountAmount) {
        this.costDiscountAmount = costDiscountAmount;
    }

    public BigDecimal getCostDiscountPercent() {
        return costDiscountPercent;
    }

    public void setCostDiscountPercent(BigDecimal costDiscountPercent) {
        this.costDiscountPercent = costDiscountPercent;
    }

    public BigDecimal getRetailDiscountAmount() {
        return retailDiscountAmount;
    }

    public void setRetailDiscountAmount(BigDecimal retailDiscountAmount) {
        this.retailDiscountAmount = retailDiscountAmount;
    }

    public BigDecimal getNetUnitCost() {
        return netUnitCost;
    }

    public void setNetUnitCost(BigDecimal netUnitCost) {
        this.netUnitCost = netUnitCost;
    }

    public BigDecimal getNetPackCost() {
        return netPackCost;
    }

    public void setNetPackCost(BigDecimal netPackCost) {
        this.netPackCost = netPackCost;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
    }

    public BigDecimal getItemSharedCost() {
        return itemSharedCost;
    }

    public void setItemSharedCost(BigDecimal itemSharedCost) {
        this.itemSharedCost = itemSharedCost;
    }

    public BigDecimal getItemGrossCost() {
        return itemGrossCost;
    }

    public void setItemGrossCost(BigDecimal itemGrossCost) {
        this.itemGrossCost = itemGrossCost;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getItemRetailAmount() {
        return itemRetailAmount;
    }

    public void setItemRetailAmount(BigDecimal itemRetailAmount) {
        this.itemRetailAmount = itemRetailAmount;
    }

    public String getItemRemarks() {
        return itemRemarks;
    }

    public void setItemRemarks(String itemRemarks) {
        this.itemRemarks = itemRemarks == null ? null : itemRemarks.trim();
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
        .append(orderBaseUnit).append(delimiterChar)
        .append(orderUom).append(delimiterChar)
        .append(orderQty).append(delimiterChar)
        .append(focBaseUnit).append(delimiterChar)
        .append(focUom).append(delimiterChar)
        .append(focQty).append(delimiterChar)
        .append(unitCost).append(delimiterChar)
        .append(packCost).append(delimiterChar)
        .append(costDiscountAmount).append(delimiterChar)
        .append(costDiscountPercent).append(delimiterChar)
        .append(retailDiscountAmount).append(delimiterChar)
        .append(netUnitCost).append(delimiterChar)
        .append(netPackCost).append(delimiterChar)
        .append(itemCost).append(delimiterChar)
        .append(itemSharedCost).append(delimiterChar)
        .append(itemGrossCost).append(delimiterChar)
        .append(retailPrice).append(delimiterChar)
        .append(itemRetailAmount).append(delimiterChar)
        .append(itemRemarks);
        
        return buffer.toString();
    }
    

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid + "" + lineSeqNo);
    }
}