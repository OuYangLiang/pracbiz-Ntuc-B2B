package com.pracbiz.b2bportal.core.report.excel;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class MissingGrnReportParameter extends BaseHolder
{
    private static final long serialVersionUID = -7196517988832565814L;
    
    private String poNo;
    private String poSubType;
    private Date poDate;
    private String storeCode;
    private String storeName;
    private String invNo;
    private Date invDate;
    private BigDecimal invAmt;
    private BigDecimal invAmtWithVat;
    private String supplierCode;
    private String supplierName;
    private String buyerCode;
    private String msgType;


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public String getPoSubType()
    {
        return poSubType;
    }


    public void setPoSubType(String poSubType)
    {
        this.poSubType = poSubType;
    }


    public Date getPoDate()
    {
        return poDate == null ? null : (Date) poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date) poDate.clone();
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public String getStoreName()
    {
        return storeName;
    }


    public void setStoreName(String storeName)
    {
        this.storeName = storeName;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public Date getInvDate()
    {
        return invDate == null ? null : (Date) invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date) invDate.clone();
    }


    public BigDecimal getInvAmt()
    {
        return invAmt;
    }


    public void setInvAmt(BigDecimal invAmt)
    {
        this.invAmt = invAmt;
    }


    public BigDecimal getInvAmtWithVat()
    {
        return invAmtWithVat;
    }


    public void setInvAmtWithVat(BigDecimal invAmtWithVat)
    {
        this.invAmtWithVat = invAmtWithVat;
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


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getMsgType()
    {
        return msgType;
    }


    public void setMsgType(String msgType)
    {
        this.msgType = msgType;
    }


    @Override
    public String getCustomIdentification()
    {
        return null;
    }

}
