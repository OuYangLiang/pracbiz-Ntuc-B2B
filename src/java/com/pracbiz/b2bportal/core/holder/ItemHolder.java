package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ItemHolder extends BaseHolder
{
    private static final long serialVersionUID = 2494389256795623172L;
    private BigInteger itemOid;
    private String buyerItemCode;
    private BigDecimal buyerOid;
    private BigDecimal docOid;
    private String itemDesc;
    private String supplierItemCode;
    private String brand;
    private String colourCode;
    private String colourDesc;
    private String sizeCode;
    private String sizeDesc;
    private String uom;
    private String classCode;
    private String classDesc;
    private String subclassCode;
    private String subclassDesc;
    private BigDecimal unitCost;
    private BigDecimal retailPrice;
    private Boolean active;
    private Date createDate;
    private Date updateDate;
    
    private List<String> _barcodes;
    

    public List<String> getBarcodes()
    {
        return _barcodes;
    }


    public void setBarcodes(List<String> barcodes)
    {
        this._barcodes = barcodes;
    }
    
    
    public void addBarcode(String barcode)
    {
        if (null == barcode || barcode.trim().isEmpty())
            return;
        
        if (null == _barcodes)
            _barcodes = new LinkedList<String>();
        
        if (!_barcodes.contains(barcode))
        {
            _barcodes.add(barcode);
        }
    }


    public BigInteger getItemOid()
    {
        return itemOid;
    }


    public void setItemOid(BigInteger itemOid)
    {
        this.itemOid = itemOid;
    }


    public String getBuyerItemCode()
    {
        return buyerItemCode;
    }


    public void setBuyerItemCode(String buyerItemCode)
    {
        this.buyerItemCode = buyerItemCode;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public BigDecimal getDocOid()
    {
        return docOid;
    }


    public void setDocOid(BigDecimal docOid)
    {
        this.docOid = docOid;
    }


    public String getItemDesc()
    {
        return itemDesc;
    }


    public void setItemDesc(String itemDesc)
    {
        this.itemDesc = itemDesc;
    }


    public String getSupplierItemCode()
    {
        return supplierItemCode;
    }


    public void setSupplierItemCode(String supplierItemCode)
    {
        this.supplierItemCode = supplierItemCode;
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


    public String getUom()
    {
        return uom;
    }


    public void setUom(String uom)
    {
        this.uom = uom;
    }


    public String getClassCode()
    {
        return classCode;
    }


    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }


    public String getClassDesc()
    {
        return classDesc;
    }


    public void setClassDesc(String classDesc)
    {
        this.classDesc = classDesc;
    }


    public String getSubclassCode()
    {
        return subclassCode;
    }


    public void setSubclassCode(String subclassCode)
    {
        this.subclassCode = subclassCode;
    }


    public String getSubclassDesc()
    {
        return subclassDesc;
    }


    public void setSubclassDesc(String subclassDesc)
    {
        this.subclassDesc = subclassDesc;
    }


    public BigDecimal getUnitCost()
    {
        return unitCost;
    }


    public void setUnitCost(BigDecimal unitCost)
    {
        this.unitCost = unitCost;
    }


    public BigDecimal getRetailPrice()
    {
        return retailPrice;
    }


    public void setRetailPrice(BigDecimal retailPrice)
    {
        this.retailPrice = retailPrice;
    }




    public Boolean getActive()
    {
        return active;
    }


    public void setActive(Boolean active)
    {
        this.active = active;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }


    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date) updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date) updateDate.clone();
    }

    
    @Override
    public String getCustomIdentification()
    {
        return itemOid.toString();
    }

}

