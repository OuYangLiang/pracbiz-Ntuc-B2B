package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class GrnDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -4356388149406078254L;

    private BigDecimal grnOid;

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

    private BigDecimal receiveQty;

    private String focBaseUnit;

    private String focUom;

    private BigDecimal focQty;

    private BigDecimal focReceiveQty;

    private BigDecimal unitCost;

    private BigDecimal itemCost;

    private BigDecimal retailPrice;

    private BigDecimal itemRetailAmount;

    private String itemRemarks;
    
    private BigDecimal deliveryQty;

    public BigDecimal getGrnOid() {
        return grnOid;
    }

    public void setGrnOid(BigDecimal grnOid) {
        this.grnOid = grnOid;
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

    public BigDecimal getReceiveQty() {
        return receiveQty;
    }

    public void setReceiveQty(BigDecimal receiveQty) {
        this.receiveQty = receiveQty;
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

    public BigDecimal getFocReceiveQty() {
        return focReceiveQty;
    }

    public void setFocReceiveQty(BigDecimal focReceiveQty) {
        this.focReceiveQty = focReceiveQty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getItemCost() {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost) {
        this.itemCost = itemCost;
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
    
    public BigDecimal getDeliveryQty()
    {
        return deliveryQty;
    }

    public void setDeliveryQty(BigDecimal deliveryQty)
    {
        this.deliveryQty = deliveryQty;
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
        .append(receiveQty).append(delimiterChar)
        .append(focBaseUnit).append(delimiterChar)
        .append(focUom).append(delimiterChar)
        .append(focQty).append(delimiterChar)
        .append(focReceiveQty).append(delimiterChar)
        .append(unitCost).append(delimiterChar)
        .append(itemCost).append(delimiterChar)
        .append(retailPrice).append(delimiterChar)
        .append(itemRetailAmount).append(delimiterChar)
        .append(itemRemarks);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(grnOid + "" + lineSeqNo);
    }
}