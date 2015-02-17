package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SalesDateLocationDetailHolder extends BaseHolder
{
    private static final long serialVersionUID = 6757852225310866465L;
    
    private BigDecimal salesOid;
    private Integer detailLineSeqNo;
    private Integer dateLineSeqNo;
    private Integer locationLineSeqNo;
    private BigDecimal salesQty;
    private BigDecimal itemCost;
    private BigDecimal salesPrice;
    private BigDecimal salesAmount;
    private BigDecimal salesDiscountAmount;
    private BigDecimal vatAmount;
    private BigDecimal salesNetAmount;
    private String lineRefNo;
    
    public String toStringWithDelimiterCharacter(String delimiterChar, SalesLocationHolder salesLoc) throws Exception
    {
        StringBuffer bf = new StringBuffer();
        bf.append(delimiterChar).append(salesLoc.getDetailLineSeqNo())
          .append(delimiterChar).append(salesLoc.getLineSeqNo())
          .append(delimiterChar).append(salesLoc.getLocationCode())
          .append(delimiterChar).append(salesLoc.getLocationName())
          .append(delimiterChar).append(salesQty)
          .append(delimiterChar).append(itemCost)
          .append(delimiterChar).append(salesPrice)
          .append(delimiterChar).append(salesAmount)
          .append(delimiterChar).append(salesDiscountAmount)
          .append(delimiterChar).append(vatAmount)
          .append(delimiterChar).append(salesNetAmount)
          .append(delimiterChar).append(salesLoc.getLocationContactTel())
          .append(delimiterChar).append(lineRefNo);
        
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

    public Integer getDetailLineSeqNo()
    {
        return detailLineSeqNo;
    }

    public void setDetailLineSeqNo(Integer detailLineSeqNo)
    {
        this.detailLineSeqNo = detailLineSeqNo;
    }

    public Integer getDateLineSeqNo()
    {
        return dateLineSeqNo;
    }

    public void setDateLineSeqNo(Integer dateLineSeqNo)
    {
        this.dateLineSeqNo = dateLineSeqNo;
    }

    public Integer getLocationLineSeqNo()
    {
        return locationLineSeqNo;
    }

    public void setLocationLineSeqNo(Integer locationLineSeqNo)
    {
        this.locationLineSeqNo = locationLineSeqNo;
    }

    public BigDecimal getSalesQty()
    {
        return salesQty;
    }

    public void setSalesQty(BigDecimal salesQty)
    {
        this.salesQty = salesQty;
    }

    public BigDecimal getSalesPrice()
    {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice)
    {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getItemCost()
    {
        return itemCost;
    }

    public void setItemCost(BigDecimal itemCost)
    {
        this.itemCost = itemCost;
    }

    public BigDecimal getSalesAmount()
    {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount)
    {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getSalesDiscountAmount()
    {
        return salesDiscountAmount;
    }

    public void setSalesDiscountAmount(BigDecimal salesDiscountAmount)
    {
        this.salesDiscountAmount = salesDiscountAmount;
    }

    public BigDecimal getSalesNetAmount()
    {
        return salesNetAmount;
    }

    public void setSalesNetAmount(BigDecimal salesNetAmount)
    {
        this.salesNetAmount = salesNetAmount;
    }

    public BigDecimal getVatAmount()
    {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount)
    {
        this.vatAmount = vatAmount;
    }

    public String getLineRefNo()
    {
        return lineRefNo;
    }

    public void setLineRefNo(String lineRefNo)
    {
        this.lineRefNo = lineRefNo;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(salesOid + "" + detailLineSeqNo);
    }
}