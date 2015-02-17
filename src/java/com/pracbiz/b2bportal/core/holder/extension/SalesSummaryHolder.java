package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class SalesSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = 4646178171941544602L;
    
    private BigDecimal salesOid;
    private String salesNo;
    private String salesDataType;
    private Date salesDate;
    private String storeCode;
    private String storeName;
    private BigDecimal totalQty;
    private BigDecimal totalGrossSalesAmount;
    private BigDecimal totalDiscountAmount;
    private BigDecimal totalVatAmount;
    private BigDecimal totalNetSalesAmount;
    private Date periodStartDate;
    private Date periodEndDate;

    private Date salesDateFrom;
    private Date salesDateTo;
    private Date periodStartDateFrom;
    private Date periodStartDateTo;
    private Date periodEndDateFrom;
    private Date periodEndDateTo;

    
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

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPeriodStartDate()
    {
        return periodStartDate == null ? null : (Date)periodStartDate.clone();
    }

    public void setPeriodStartDate(Date periodStartDate)
    {
        this.periodStartDate = periodStartDate == null ? null : (Date)periodStartDate.clone();
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPeriodEndDate()
    {
        return periodEndDate == null ? null : (Date)periodEndDate.clone();
    }

    public void setPeriodEndDate(Date periodEndDate)
    {
        this.periodEndDate = periodEndDate == null ? null : (Date)periodEndDate.clone();
    }

    public Date getSalesDateFrom()
    {
        return salesDateFrom == null ? null : (Date)salesDateFrom.clone();
    }

    public void setSalesDateFrom(Date salesDateFrom)
    {
        this.salesDateFrom = salesDateFrom == null ? null : (Date)salesDateFrom.clone();
    }

    public Date getSalesDateTo()
    {
        return salesDateTo == null ? null : (Date)salesDateTo.clone();
    }

    public void setSalesDateTo(Date salesDateTo)
    {
        this.salesDateTo = salesDateTo == null ? null : (Date)salesDateTo.clone();
    }

    public Date getPeriodStartDateFrom()
    {
        return periodStartDateFrom == null ? null : (Date)periodStartDateFrom.clone();
    }

    public void setPeriodStartDateFrom(Date periodStartDateFrom)
    {
        this.periodStartDateFrom = periodStartDateFrom == null ? null : (Date)periodStartDateFrom.clone();
    }

    public Date getPeriodStartDateTo()
    {
        return periodStartDateTo == null ? null : (Date)periodStartDateTo.clone();
    }

    public void setPeriodStartDateTo(Date periodStartDateTo)
    {
        this.periodStartDateTo = periodStartDateTo == null ? null : (Date)periodStartDateTo.clone();
    }

    public Date getPeriodEndDateFrom()
    {
        return periodEndDateFrom == null ? null : (Date)periodEndDateFrom.clone();
    }

    public void setPeriodEndDateFrom(Date periodEndDateFrom)
    {
        this.periodEndDateFrom = periodEndDateFrom == null ? null : (Date)periodEndDateFrom.clone();
    }

    public Date getPeriodEndDateTo()
    {
        return periodEndDateTo == null ? null : (Date)periodEndDateTo.clone();
    }

    public void setPeriodEndDateTo(Date periodEndDateTo)
    {
        this.periodEndDateTo = periodEndDateTo == null ? null : (Date)periodEndDateTo.clone();
    }

}
