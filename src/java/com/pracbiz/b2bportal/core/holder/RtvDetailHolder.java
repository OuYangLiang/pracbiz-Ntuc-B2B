package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class RtvDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -3035165628623539042L;

    private BigDecimal rtvOid;

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

    private String doNo;

    private Date doDate;

    private String invNo;

    private Date invDate;

    private BigDecimal packingFactor;

    private String returnBaseUnit;

    private String returnUom;

    private BigDecimal returnQty;

    private BigDecimal unitCost;

    private BigDecimal costDiscountAmount;

    private BigDecimal itemCost;

    private BigDecimal retailPrice;

    private BigDecimal itemRetailAmount;

    private String reasonCode;

    private String reasonDesc;
    
    private String lineRefNo;

    public BigDecimal getRtvOid() {
        return rtvOid;
    }

    public void setRtvOid(BigDecimal rtvOid) {
        this.rtvOid = rtvOid;
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

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo == null ? null : doNo.trim();
    }

    public Date getDoDate() {
        return doDate == null ? null : (Date)doDate.clone();
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate == null ? null : (Date)doDate.clone();
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo == null ? null : invNo.trim();
    }

    public Date getInvDate() {
        return invDate == null ? null : (Date)invDate.clone();
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
    }

    public BigDecimal getPackingFactor() {
        return packingFactor;
    }

    public void setPackingFactor(BigDecimal packingFactor) {
        this.packingFactor = packingFactor;
    }

    public String getReturnBaseUnit() {
        return returnBaseUnit;
    }

    public void setReturnBaseUnit(String returnBaseUnit) {
        this.returnBaseUnit = returnBaseUnit == null ? null : returnBaseUnit.trim();
    }

    public String getReturnUom() {
        return returnUom;
    }

    public void setReturnUom(String returnUom) {
        this.returnUom = returnUom == null ? null : returnUom.trim();
    }

    public BigDecimal getReturnQty() {
        return returnQty;
    }

    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }


    public BigDecimal getCostDiscountAmount() {
        return costDiscountAmount;
    }

    public void setCostDiscountAmount(BigDecimal costDiscountAmount) {
        this.costDiscountAmount = costDiscountAmount;
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

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode == null ? null : reasonCode.trim();
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc == null ? null : reasonDesc.trim();
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
        .append(doNo).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(doDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(invNo).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(invDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(packingFactor).append(delimiterChar)
        .append(returnBaseUnit).append(delimiterChar)
        .append(returnUom).append(delimiterChar)
        .append(returnQty).append(delimiterChar)
        .append(unitCost).append(delimiterChar)
        .append(costDiscountAmount).append(delimiterChar)
        .append(itemCost).append(delimiterChar)
        .append(retailPrice).append(delimiterChar)
        .append(itemRetailAmount).append(delimiterChar)
        .append(reasonCode).append(delimiterChar)
        .append(reasonDesc).append(delimiterChar)
        .append(lineRefNo);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(rtvOid + "" + lineSeqNo);
    }
}