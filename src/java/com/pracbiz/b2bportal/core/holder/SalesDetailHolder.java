package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

public class SalesDetailHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 8012138163022500469L;

    private BigDecimal salesOid;
    private Integer lineSeqNo;
    private String posId;
    private String receiptNo;
    private Date receiptDate;
    private Date businessDate;
    private String buyerItemCode;
    private String supplierItemCode;
    private String barcode;
    private String itemDesc;
    private String brand;
    private String deptCode;
    private String deptName;
    private String subDeptCode;
    private String subDeptName;
    private String colourCode;
    private String colourDesc;
    private String sizeCode;
    private String sizeDesc;
    private BigDecimal packingFactor;
    private String salesBaseUnit;
    private String salesUom;
    private BigDecimal salesQty;
    private BigDecimal itemCost;
    private BigDecimal salesPrice;
    private BigDecimal itemSalesAmount;
    private BigDecimal salesDiscountAmount;
    private BigDecimal vatAmount;
    private BigDecimal itemNetSalesAmount;
    private String itemRemarks;
    
    
    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer bf = new StringBuffer();
        bf.append(delimiterChar).append(lineSeqNo)
          .append(delimiterChar).append(posId)
          .append(delimiterChar).append(receiptNo)
          .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(receiptDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
          .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(businessDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
          .append(delimiterChar).append(buyerItemCode)
          .append(delimiterChar).append(supplierItemCode)
          .append(delimiterChar).append(barcode)
          .append(delimiterChar).append(itemDesc)
          .append(delimiterChar).append(brand)
          .append(delimiterChar).append(deptCode)
          .append(delimiterChar).append(deptName)
          .append(delimiterChar).append(subDeptCode)
          .append(delimiterChar).append(subDeptName)
          .append(delimiterChar).append(colourCode)
          .append(delimiterChar).append(colourDesc)
          .append(delimiterChar).append(sizeCode)
          .append(delimiterChar).append(sizeDesc)
          .append(delimiterChar).append(packingFactor)
          .append(delimiterChar).append(salesBaseUnit)
          .append(delimiterChar).append(salesUom)
          .append(delimiterChar).append(salesQty)
          .append(delimiterChar).append(itemCost)
          .append(delimiterChar).append(salesPrice)
          .append(delimiterChar).append(itemSalesAmount)
          .append(delimiterChar).append(salesDiscountAmount)
          .append(delimiterChar).append(vatAmount)
          .append(delimiterChar).append(itemNetSalesAmount)
          .append(delimiterChar).append(itemRemarks);
        
        return bf.toString();
    }

    public BigDecimal getSalesOid()
    {
        return salesOid;
    }

    public void setSalesOid(BigDecimal salesOid)
    {
        this.salesOid = salesOid;
    }

    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
    }

    public String getPosId()
    {
        return posId;
    }

    public void setPosId(String posId)
    {
        this.posId = posId;
    }

    public String getReceiptNo()
    {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo)
    {
        this.receiptNo = receiptNo;
    }

    public Date getReceiptDate()
    {
        return receiptDate == null ? null : (Date)receiptDate.clone();
    }

    public void setReceiptDate(Date receiptDate)
    {
        this.receiptDate = receiptDate == null ? null : (Date)receiptDate
            .clone();
    }
    
    public Date getBusinessDate()
    {
        return businessDate == null ? null : (Date)businessDate.clone();
    }


    public void setBusinessDate(Date businessDate)
    {
        this.businessDate = businessDate == null ? null : (Date)businessDate.clone();
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

    public String getDeptCode()
    {
        return deptCode;
    }

    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getSubDeptCode()
    {
        return subDeptCode;
    }

    public void setSubDeptCode(String subDeptCode)
    {
        this.subDeptCode = subDeptCode;
    }

    public String getSubDeptName()
    {
        return subDeptName;
    }

    public void setSubDeptName(String subDeptName)
    {
        this.subDeptName = subDeptName;
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

    public String getSalesBaseUnit()
    {
        return salesBaseUnit;
    }

    public void setSalesBaseUnit(String salesBaseUnit)
    {
        this.salesBaseUnit = salesBaseUnit;
    }

    public String getSalesUom()
    {
        return salesUom;
    }

    public void setSalesUom(String salesUom)
    {
        this.salesUom = salesUom;
    }

    public BigDecimal getSalesQty()
    {
        return salesQty;
    }

    public void setSalesQty(BigDecimal salesQty)
    {
        this.salesQty = salesQty;
    }

    public BigDecimal getItemCost()
    {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost)
    {
        this.itemCost = itemCost;
    }
    

    public BigDecimal getSalesPrice()
    {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice)
    {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getItemSalesAmount()
    {
        return itemSalesAmount;
    }

    public void setItemSalesAmount(BigDecimal itemSalesAmount)
    {
        this.itemSalesAmount = itemSalesAmount;
    }

    public BigDecimal getSalesDiscountAmount()
    {
        return salesDiscountAmount;
    }

    public void setSalesDiscountAmount(BigDecimal salesDiscountAmount)
    {
        this.salesDiscountAmount = salesDiscountAmount;
    }

    public BigDecimal getVatAmount()
    {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount)
    {
        this.vatAmount = vatAmount;
    }

    public BigDecimal getItemNetSalesAmount()
    {
        return itemNetSalesAmount;
    }

    public void setItemNetSalesAmount(BigDecimal itemNetSalesAmount)
    {
        this.itemNetSalesAmount = itemNetSalesAmount;
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
        return String.valueOf(salesOid + "" + lineSeqNo);
    }
}