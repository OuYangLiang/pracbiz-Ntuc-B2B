package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class DoDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -2156523711726347274L;

    private BigDecimal doOid;

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

    private String deliveryBaseUnit;

    private String deliveryUom;

    private BigDecimal deliveryQty;

    private String focBaseUnit;

    private String focUom;

    private BigDecimal focQty;

    private String itemRemarks;

    public BigDecimal getDoOid() {
        return doOid;
    }

    public void setDoOid(BigDecimal doOid) {
        this.doOid = doOid;
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

    public String getDeliveryBaseUnit() {
        return deliveryBaseUnit;
    }

    public void setDeliveryBaseUnit(String deliveryBaseUnit) {
        this.deliveryBaseUnit = deliveryBaseUnit == null ? null : deliveryBaseUnit.trim();
    }

    public String getDeliveryUom() {
        return deliveryUom;
    }

    public void setDeliveryUom(String deliveryUom) {
        this.deliveryUom = deliveryUom == null ? null : deliveryUom.trim();
    }

    public BigDecimal getDeliveryQty() {
        return deliveryQty;
    }

    public void setDeliveryQty(BigDecimal deliveryQty) {
        this.deliveryQty = deliveryQty;
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

    public String getItemRemarks() {
        return itemRemarks;
    }

    public void setItemRemarks(String itemRemarks) {
        this.itemRemarks = itemRemarks == null ? null : itemRemarks.trim();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(doOid + "" + lineSeqNo);
    }
}