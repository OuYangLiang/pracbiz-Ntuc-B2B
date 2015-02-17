package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;

public class DnDetailHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 6256797418540490270L;

    private BigDecimal dnOid;

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

    private String poNo;

    private Date poDate;

    private String invNo;

    private Date invDate;

    private BigDecimal packingFactor;

    private String debitBaseUnit;

    private String orderUom;

    private BigDecimal debitQty;

    private BigDecimal unitCost;

    private BigDecimal costDiscountAmount;

    private BigDecimal costDiscountPercent;

    private BigDecimal retailDiscountAmount;

    private BigDecimal netUnitCost;

    private BigDecimal itemCost;

    private BigDecimal itemSharedCode;

    private BigDecimal itemGrossCost;

    private BigDecimal retailPrice;

    private BigDecimal itemRetailAmount;

    private String itemRemarks;

    private BigDecimal disputePrice;

    private String disputePriceRemarks;

    private BigDecimal disputeQty;

    private String disputeQtyRemarks;

    private DnPriceStatus priceStatus;

    private String priceStatusActionRemarks;

    private String priceStatusActionBy;

    private Date priceStatusActionDate;

    private DnQtyStatus qtyStatus;

    private String qtyStatusActionRemarks;

    private String qtyStatusActionBy;

    private Date qtyStatusActionDate;

    private BigDecimal confirmPrice;

    private BigDecimal confirmQty;
    
    private Boolean privileged;
    
    private Boolean showOnInit;
    
    private String lineRefNo;
    
    private String classCode;
    
    private String subclassCode;
    
    public BigDecimal getDnOid()
    {
        return dnOid;
    }


    public void setDnOid(BigDecimal dnOid)
    {
        this.dnOid = dnOid;
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


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo == null ? null : poNo.trim();
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
        this.invNo = invNo == null ? null : invNo.trim();
    }


    public Date getInvDate()
    {
        return invDate == null ? null : (Date) invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date) invDate.clone();
    }


    public BigDecimal getPackingFactor()
    {
        return packingFactor;
    }


    public void setPackingFactor(BigDecimal packingFactor)
    {
        this.packingFactor = packingFactor;
    }


    public String getDebitBaseUnit()
    {
        return debitBaseUnit;
    }


    public void setDebitBaseUnit(String debitBaseUnit)
    {
        this.debitBaseUnit = debitBaseUnit == null ? null : debitBaseUnit
                .trim();
    }


    public String getOrderUom()
    {
        return orderUom;
    }


    public void setOrderUom(String orderUom)
    {
        this.orderUom = orderUom == null ? null : orderUom.trim();
    }


    public BigDecimal getDebitQty()
    {
        return debitQty;
    }


    public void setDebitQty(BigDecimal debitQty)
    {
        this.debitQty = debitQty;
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


    public BigDecimal getItemSharedCode()
    {
        return itemSharedCode;
    }


    public void setItemSharedCode(BigDecimal itemSharedCode)
    {
        this.itemSharedCode = itemSharedCode;
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


    public String getItemRemarks()
    {
        return itemRemarks;
    }


    public void setItemRemarks(String itemRemarks)
    {
        this.itemRemarks = itemRemarks == null ? null : itemRemarks.trim();
    }


    public BigDecimal getDisputePrice()
    {
        return disputePrice;
    }


    public void setDisputePrice(BigDecimal disputePrice)
    {
        this.disputePrice = disputePrice;
    }


    public String getDisputePriceRemarks()
    {
        return disputePriceRemarks;
    }


    public void setDisputePriceRemarks(String disputePriceRemarks)
    {
        this.disputePriceRemarks = disputePriceRemarks;
    }


    public BigDecimal getDisputeQty()
    {
        return disputeQty;
    }


    public void setDisputeQty(BigDecimal disputeQty)
    {
        this.disputeQty = disputeQty;
    }


    public String getDisputeQtyRemarks()
    {
        return disputeQtyRemarks;
    }


    public void setDisputeQtyRemarks(String disputeQtyRemarks)
    {
        this.disputeQtyRemarks = disputeQtyRemarks;
    }


    public DnPriceStatus getPriceStatus()
    {
        return priceStatus;
    }


    public void setPriceStatus(DnPriceStatus priceStatus)
    {
        this.priceStatus = priceStatus;
    }


    public String getPriceStatusActionRemarks()
    {
        return priceStatusActionRemarks;
    }


    public void setPriceStatusActionRemarks(String priceStatusActionRemarks)
    {
        this.priceStatusActionRemarks = priceStatusActionRemarks;
    }


    public String getPriceStatusActionBy()
    {
        return priceStatusActionBy;
    }


    public void setPriceStatusActionBy(String priceStatusActionBy)
    {
        this.priceStatusActionBy = priceStatusActionBy;
    }


    public Date getPriceStatusActionDate()
    {
        return priceStatusActionDate == null ? null : (Date) priceStatusActionDate.clone();
    }


    public void setPriceStatusActionDate(Date priceStatusActionDate)
    {
        this.priceStatusActionDate = priceStatusActionDate == null ? null : (Date) priceStatusActionDate.clone();
    }


    public DnQtyStatus getQtyStatus()
    {
        return qtyStatus;
    }


    public void setQtyStatus(DnQtyStatus qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }


    public String getQtyStatusActionRemarks()
    {
        return qtyStatusActionRemarks;
    }


    public void setQtyStatusActionRemarks(String qtyStatusActionRemarks)
    {
        this.qtyStatusActionRemarks = qtyStatusActionRemarks;
    }


    public String getQtyStatusActionBy()
    {
        return qtyStatusActionBy;
    }


    public void setQtyStatusActionBy(String qtyStatusActionBy)
    {
        this.qtyStatusActionBy = qtyStatusActionBy;
    }


    public Date getQtyStatusActionDate()
    {
        return qtyStatusActionDate == null ? null : (Date) qtyStatusActionDate.clone();
    }


    public void setQtyStatusActionDate(Date qtyStatusActionDate)
    {
        this.qtyStatusActionDate = qtyStatusActionDate == null ? null : (Date) qtyStatusActionDate.clone();
    }


    public BigDecimal getConfirmPrice()
    {
        return confirmPrice;
    }


    public void setConfirmPrice(BigDecimal confirmPrice)
    {
        this.confirmPrice = confirmPrice;
    }


    public BigDecimal getConfirmQty()
    {
        return confirmQty;
    }


    public void setConfirmQty(BigDecimal confirmQty)
    {
        this.confirmQty = confirmQty;
    }


    public Boolean getPrivileged()
    {
        return privileged;
    }


    public void setPrivileged(Boolean privileged)
    {
        this.privileged = privileged;
    }


    public Boolean getShowOnInit()
    {
        return showOnInit;
    }


    public void setShowOnInit(Boolean showOnInit)
    {
        this.showOnInit = showOnInit;
    }


    public String getLineRefNo()
    {
        return lineRefNo;
    }


    public void setLineRefNo(String lineRefNo)
    {
        this.lineRefNo = lineRefNo;
    }


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    public String getSubclassCode()
    {
        return subclassCode;
    }


    public void setSubclassCode(String subclassCode)
    {
        this.subclassCode = subclassCode;
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
                .append(delimiterChar).append(packingFactor)
                .append(delimiterChar).append(debitBaseUnit)
                .append(delimiterChar).append(orderUom).append(delimiterChar)
                .append(debitQty).append(delimiterChar).append(unitCost)
                .append(delimiterChar).append(costDiscountAmount)
                .append(delimiterChar).append(costDiscountPercent)
                .append(delimiterChar).append(retailDiscountAmount)
                .append(delimiterChar).append(netUnitCost)
                .append(delimiterChar).append(itemCost).append(delimiterChar)
                .append(itemSharedCode).append(delimiterChar)
                .append(itemGrossCost).append(delimiterChar)
                .append(retailPrice).append(delimiterChar)
                .append(itemRetailAmount).append(delimiterChar);

        return buffer.toString();
    }

    
    public boolean isDisputeQtyChanged()
    {
        BigDecimal qty = getDebitQty() == null ? BigDecimal.ZERO : getDebitQty();
        BigDecimal disputeQty = getDisputeQty() == null ? BigDecimal.ZERO : getDisputeQty();
        
        return qty.compareTo(disputeQty) != 0;
    }
    
    
    public boolean isDisputePriceChanged()
    {
        BigDecimal price = getUnitCost() == null ? BigDecimal.ZERO : getUnitCost();
        BigDecimal disputePrice = getDisputePrice() == null ? BigDecimal.ZERO : getDisputePrice();
        
        return price.compareTo(disputePrice) != 0;
    }
    
    
    public boolean isConfirmQtyChanged()
    {
        BigDecimal qty = getDebitQty() == null ? BigDecimal.ZERO : getDebitQty();
        BigDecimal disputeQty = getDisputeQty() == null ? BigDecimal.ZERO : getDisputeQty();
        BigDecimal oldConfirmQty = qty;

        if (isDisputeQtyChanged() && qtyStatus.equals(DnQtyStatus.ACCEPTED))
        {
            oldConfirmQty = disputeQty;
        }
        
        if (oldConfirmQty.compareTo(confirmQty) == 0)
        {
            return false;
        }
        
        return true;
    }
    
    
    public boolean isConfirmPriceChanged()
    {
        BigDecimal price = getUnitCost() == null ? BigDecimal.ZERO : getUnitCost();
        BigDecimal disputePrice = getDisputePrice() == null ? BigDecimal.ZERO : getDisputePrice();
        BigDecimal oldConfirmPrice = price;
        
        if (isDisputePriceChanged() && priceStatus.equals(DnPriceStatus.ACCEPTED))
        {
            oldConfirmPrice = disputePrice;
        }
        
        if (oldConfirmPrice.compareTo(confirmPrice) == 0)
        {
            return false;
        }
        
        return true;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(dnOid + "" + lineSeqNo);
    }
}