package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class PoSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = -5096148747079460571L;

    private PoType poType;
    private String poSubType;
    private PoStatus poStatus;
    private Date poDate;
    private Date deliveryDate;
    private Date expiryDate;
    private String grnNo;
    private String invNo;
    private String poNo;
    private Date poDateFrom;
    private Date poDateTo;
    private Date deliveryDateFrom;
    private Date deliveryDateTo;
    private String ctrlStatus;
    private BigDecimal poOid;
    private Integer locationCount;
    private Boolean duplicate;
    private String storeCode;
    private Integer detailCount;
    private BigDecimal totalCost;
    private String poSubType2;
    private Boolean quickPoFlag;
    private String invoiceNo;
    private BigDecimal totalAmount;
    

    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public BigDecimal getPoOid()
    {
        return poOid;
    }


    public void setPoOid(BigDecimal poOid)
    {
        this.poOid = poOid;
    }


    public String getCtrlStatus()
    {
        return ctrlStatus;
    }


    public void setCtrlStatus(String ctrlStatus)
    {
        this.ctrlStatus = ctrlStatus;
    }


    public PoType getPoType()
    {
        return poType;
    }


    public void setPoType(PoType poType)
    {
        this.poType = poType;
    }


    public PoStatus getPoStatus()
    {
        return poStatus;
    }


    public void setPoStatus(PoStatus poStatus)
    {
        this.poStatus = poStatus;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPoDate()
    {
        return poDate == null ? null : (Date)poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDeliveryDate()
    {
        return deliveryDate == null ? null : (Date)deliveryDate.clone();
    }


    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate == null ? null : (Date)deliveryDate
            .clone();
    }


    public String getGrnNo()
    {
        return grnNo;
    }


    public void setGrnNo(String grnNo)
    {
        this.grnNo = grnNo;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPoDateFrom()
    {
        return poDateFrom == null ? null : (Date)poDateFrom.clone();
    }


    public void setPoDateFrom(Date poDateFrom)
    {
        this.poDateFrom = poDateFrom == null ? null : (Date)poDateFrom.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPoDateTo()
    {
        return poDateTo == null ? null : (Date)poDateTo.clone();
    }

    
    public void setPoDateTo(Date poDateTo)
    {
        this.poDateTo = poDateTo == null ? null : (Date)poDateTo.clone();
    }
    
    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDeliveryDateFrom()
    {
        return deliveryDateFrom == null ? null : (Date) deliveryDateFrom.clone();
    }
    
    
    public void setDeliveryDateFrom(Date deliveryDateFrom)
    {
        this.deliveryDateFrom = deliveryDateFrom == null ? null : (Date) deliveryDateFrom.clone();
    }
    
    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDeliveryDateTo()
    {
        return deliveryDateTo == null ? null : (Date) deliveryDateTo.clone();
    }
    
    
    public void setDeliveryDateTo(Date deliveryDateTo)
    {
        this.deliveryDateTo = deliveryDateTo == null ? null : (Date) deliveryDateTo.clone();
    }

    
    public String getPoSubType()
    {
        return poSubType;
    }


    public void setPoSubType(String poSubType)
    {
        this.poSubType = poSubType;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getExpiryDate()
    {
        return expiryDate == null ? null : (Date)expiryDate.clone();
    }


    public void setExpiryDate(Date expiryDate)
    {
        this.expiryDate = expiryDate == null ? null : (Date)expiryDate.clone();
    }


    public Integer getLocationCount()
    {
        return locationCount;
    }


    public void setLocationCount(Integer locationCount)
    {
        this.locationCount = locationCount;
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public Integer getDetailCount()
    {
        return detailCount;
    }


    public void setDetailCount(Integer detailCount)
    {
        this.detailCount = detailCount;
    }


    public BigDecimal getTotalCost()
    {
        return totalCost;
    }


    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }


    public String getPoSubType2()
    {
        return poSubType2;
    }


    public void setPoSubType2(String poSubType2)
    {
        this.poSubType2 = poSubType2;
    }


    public Boolean getQuickPoFlag()
    {
        return quickPoFlag;
    }


    public void setQuickPoFlag(Boolean quickPoFlag)
    {
        this.quickPoFlag = quickPoFlag;
    }


    public String getInvoiceNo()
    {
        return invoiceNo;
    }


    public void setInvoiceNo(String invoiceNo)
    {
        this.invoiceNo = invoiceNo;
    }


    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }


    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    
    
}
