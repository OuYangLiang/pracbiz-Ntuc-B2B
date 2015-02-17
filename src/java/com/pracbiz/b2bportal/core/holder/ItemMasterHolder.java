package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.ItemStatus;

public class ItemMasterHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private BigDecimal itemOid;
    private String itemNo;
    private String itemType;
    private String fileName;
    private ItemStatus itemStatus;
    private String buyerCode;
    private String buyerName;
    private String supplierCode;
    private String supplierName;


    


    public BigDecimal getItemOid()
    {
        return itemOid;
    }


    public void setItemOid(BigDecimal itemOid)
    {
        this.itemOid = itemOid;
    }


    public String getItemType()
    {
        return itemType;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public String getItemNo()
    {
        return itemNo;
    }


    public void setItemNo(String itemNo)
    {
        this.itemNo = itemNo;
    }


    public String getFileName()
    {
        return fileName;
    }


    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }


    public ItemStatus getItemStatus()
    {
        return itemStatus;
    }


    public void setItemStatus(ItemStatus itemStatus)
    {
        this.itemStatus = itemStatus;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    @Override
    public String getCustomIdentification()
    {
        return itemNo;
    }

}
