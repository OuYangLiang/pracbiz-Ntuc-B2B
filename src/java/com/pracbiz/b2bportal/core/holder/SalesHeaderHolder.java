package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class SalesHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    /**
     * 
     */
    private static final long serialVersionUID = 8591323268977318309L;
    
    private BigDecimal salesOid;
    private String salesNo;
    private String docAction;
    private Date actionDate;
    private String salesDataType;
    private Date salesDate;
    private BigDecimal buyerOid;
    private String buyerCode;
    private String buyerName;
    private String buyerAddr1;
    private String buyerAddr2;
    private String buyerAddr3;
    private String buyerAddr4;
    private String buyerCity;
    private String buyerState;
    private String buyerCtryCode;
    private String buyerPostalCode;
    private BigDecimal supplierOid;
    private String supplierCode;
    private String supplierName;
    private String supplierAddr1;
    private String supplierAddr2;
    private String supplierAddr3;
    private String supplierAddr4;
    private String supplierCity;
    private String supplierState;
    private String supplierCtryCode;
    private String supplierPostalCode;
    private String storeCode;
    private String storeName;
    private String storeAddr1;
    private String storeAddr2;
    private String storeAddr3;
    private String storeAddr4;
    private String storeCity;
    private String storeState;
    private String storeCtryCode;
    private String storePostalCode;
    private BigDecimal totalQty;
    private BigDecimal totalGrossSalesAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalNetSalesAmount;
    private Date periodStartDate;
    private Date periodEndDate;
    
    public String computePdfFilename()
    {
        return MsgType.DSD.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getSalesNo())
            + DOC_FILENAME_DELIMITOR + this.getSalesOid() + ".pdf";
    }
    
    
    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer bf = new StringBuffer();
        bf.append(delimiterChar).append(salesNo)
            .append(delimiterChar).append(docAction)
            .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
            .append(delimiterChar).append(salesDataType)
            .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(salesDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
            .append(delimiterChar).append(buyerCode)
            .append(delimiterChar).append(buyerName)
            .append(delimiterChar).append(buyerAddr1)
            .append(delimiterChar).append(buyerAddr2)
            .append(delimiterChar).append(buyerAddr3)
            .append(delimiterChar).append(buyerAddr4)
            .append(delimiterChar).append(buyerCity)
            .append(delimiterChar).append(buyerState)
            .append(delimiterChar).append(buyerCtryCode)
            .append(delimiterChar).append(buyerPostalCode)
            .append(delimiterChar).append(supplierCode)
            .append(delimiterChar).append(supplierName)
            .append(delimiterChar).append(supplierAddr1)
            .append(delimiterChar).append(supplierAddr2)
            .append(delimiterChar).append(supplierAddr3)
            .append(delimiterChar).append(supplierAddr4)
            .append(delimiterChar).append(supplierCity)
            .append(delimiterChar).append(supplierState)
            .append(delimiterChar).append(supplierCtryCode)
            .append(delimiterChar).append(supplierPostalCode)
            .append(delimiterChar).append(storeCode)
            .append(delimiterChar).append(storeName)
            .append(delimiterChar).append(storeAddr1)
            .append(delimiterChar).append(storeAddr2)
            .append(delimiterChar).append(storeAddr3)
            .append(delimiterChar).append(storeAddr4)
            .append(delimiterChar).append(storeCity)
            .append(delimiterChar).append(storeState)
            .append(delimiterChar).append(storeCtryCode)
            .append(delimiterChar).append(storePostalCode)
            .append(delimiterChar).append(totalQty)
            .append(delimiterChar).append(totalGrossSalesAmount)
            .append(delimiterChar).append(totalDiscountAmount)
            .append(delimiterChar).append(totalVatAmount)
            .append(delimiterChar).append(totalNetSalesAmount)
            .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(periodStartDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
            .append(delimiterChar).append(DateUtil.getInstance().convertDateToString(periodEndDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
            
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


    public String getSalesNo()
    {
        return salesNo;
    }


    public void setSalesNo(String salesNo)
    {
        this.salesNo = salesNo;
    }


    public String getDocAction()
    {
        return docAction;
    }


    public void setDocAction(String docAction)
    {
        this.docAction = docAction;
    }


    public Date getActionDate()
    {
        return actionDate == null ? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date)actionDate;
    }


    public String getSalesDataType()
    {
        return salesDataType;
    }


    public void setSalesDataType(String salesDataType)
    {
        this.salesDataType = salesDataType;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getSalesDate()
    {
        return salesDate == null ? null : (Date)salesDate.clone();
    }


    public void setSalesDate(Date salesDate)
    {
        this.salesDate = salesDate == null ? null : (Date)salesDate.clone();
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
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


    public String getBuyerAddr1()
    {
        return buyerAddr1;
    }


    public void setBuyerAddr1(String buyerAddr1)
    {
        this.buyerAddr1 = buyerAddr1;
    }


    public String getBuyerAddr2()
    {
        return buyerAddr2;
    }


    public void setBuyerAddr2(String buyerAddr2)
    {
        this.buyerAddr2 = buyerAddr2;
    }


    public String getBuyerAddr3()
    {
        return buyerAddr3;
    }


    public void setBuyerAddr3(String buyerAddr3)
    {
        this.buyerAddr3 = buyerAddr3;
    }


    public String getBuyerAddr4()
    {
        return buyerAddr4;
    }


    public void setBuyerAddr4(String buyerAddr4)
    {
        this.buyerAddr4 = buyerAddr4;
    }


    public String getBuyerCity()
    {
        return buyerCity;
    }


    public void setBuyerCity(String buyerCity)
    {
        this.buyerCity = buyerCity;
    }


    public String getBuyerState()
    {
        return buyerState;
    }


    public void setBuyerState(String buyerState)
    {
        this.buyerState = buyerState;
    }


    public String getBuyerCtryCode()
    {
        return buyerCtryCode;
    }


    public void setBuyerCtryCode(String buyerCtryCode)
    {
        this.buyerCtryCode = buyerCtryCode;
    }


    public String getBuyerPostalCode()
    {
        return buyerPostalCode;
    }


    public void setBuyerPostalCode(String buyerPostalCode)
    {
        this.buyerPostalCode = buyerPostalCode;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
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


    public String getSupplierAddr1()
    {
        return supplierAddr1;
    }


    public void setSupplierAddr1(String supplierAddr1)
    {
        this.supplierAddr1 = supplierAddr1;
    }


    public String getSupplierAddr2()
    {
        return supplierAddr2;
    }


    public void setSupplierAddr2(String supplierAddr2)
    {
        this.supplierAddr2 = supplierAddr2;
    }


    public String getSupplierAddr3()
    {
        return supplierAddr3;
    }


    public void setSupplierAddr3(String supplierAddr3)
    {
        this.supplierAddr3 = supplierAddr3;
    }


    public String getSupplierAddr4()
    {
        return supplierAddr4;
    }


    public void setSupplierAddr4(String supplierAddr4)
    {
        this.supplierAddr4 = supplierAddr4;
    }


    public String getSupplierCity()
    {
        return supplierCity;
    }


    public void setSupplierCity(String supplierCity)
    {
        this.supplierCity = supplierCity;
    }


    public String getSupplierState()
    {
        return supplierState;
    }


    public void setSupplierState(String supplierState)
    {
        this.supplierState = supplierState;
    }


    public String getSupplierCtryCode()
    {
        return supplierCtryCode;
    }


    public void setSupplierCtryCode(String supplierCtryCode)
    {
        this.supplierCtryCode = supplierCtryCode;
    }


    public String getSupplierPostalCode()
    {
        return supplierPostalCode;
    }


    public void setSupplierPostalCode(String supplierPostalCode)
    {
        this.supplierPostalCode = supplierPostalCode;
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


    public String getStoreAddr1()
    {
        return storeAddr1;
    }


    public void setStoreAddr1(String storeAddr1)
    {
        this.storeAddr1 = storeAddr1;
    }


    public String getStoreAddr2()
    {
        return storeAddr2;
    }


    public void setStoreAddr2(String storeAddr2)
    {
        this.storeAddr2 = storeAddr2;
    }


    public String getStoreAddr3()
    {
        return storeAddr3;
    }


    public void setStoreAddr3(String storeAddr3)
    {
        this.storeAddr3 = storeAddr3;
    }


    public String getStoreAddr4()
    {
        return storeAddr4;
    }


    public void setStoreAddr4(String storeAddr4)
    {
        this.storeAddr4 = storeAddr4;
    }


    public String getStoreCity()
    {
        return storeCity;
    }


    public void setStoreCity(String storeCity)
    {
        this.storeCity = storeCity;
    }


    public String getStoreState()
    {
        return storeState;
    }


    public void setStoreState(String storeState)
    {
        this.storeState = storeState;
    }


    public String getStoreCtryCode()
    {
        return storeCtryCode;
    }


    public void setStoreCtryCode(String storeCtryCode)
    {
        this.storeCtryCode = storeCtryCode;
    }


    public String getStorePostalCode()
    {
        return storePostalCode;
    }


    public void setStorePostalCode(String storePostalCode)
    {
        this.storePostalCode = storePostalCode;
    }


    public BigDecimal getTotalQty()
    {
        return totalQty;
    }


    public void setTotalQty(BigDecimal totalQty)
    {
        this.totalQty = totalQty;
    }


    public BigDecimal getTotalGrossSalesAmount()
    {
        return totalGrossSalesAmount;
    }


    public void setTotalGrossSalesAmount(BigDecimal totalGrossSalesAmount)
    {
        this.totalGrossSalesAmount = totalGrossSalesAmount;
    }


    public BigDecimal getTotalDiscountAmount()
    {
        return totalDiscountAmount;
    }


    public void setTotalDiscountAmount(BigDecimal totalDiscountAmount)
    {
        this.totalDiscountAmount = totalDiscountAmount;
    }


    public BigDecimal getTotalVatAmount()
    {
        return totalVatAmount;
    }


    public void setTotalVatAmount(BigDecimal totalVatAmount)
    {
        this.totalVatAmount = totalVatAmount;
    }


    public BigDecimal getTotalNetSalesAmount()
    {
        return totalNetSalesAmount;
    }


    public void setTotalNetSalesAmount(BigDecimal totalNetSalesAmount)
    {
        this.totalNetSalesAmount = totalNetSalesAmount;
    }


    public Date getPeriodStartDate()
    {
        return periodStartDate == null ? null : (Date)periodStartDate.clone();
    }


    public void setPeriodStartDate(Date periodStartDate)
    {
        this.periodStartDate = periodStartDate == null ? null : (Date)periodStartDate.clone();
    }


    public Date getPeriodEndDate()
    {
        return periodEndDate == null ? null : (Date)periodEndDate.clone();
    }


    public void setPeriodEndDate(Date periodEndDate)
    {
        this.periodEndDate = periodEndDate == null ? null : (Date)periodEndDate.clone();
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(salesOid);
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getSalesNo();
    }
}