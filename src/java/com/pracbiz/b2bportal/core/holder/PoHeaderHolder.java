package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


import org.apache.struts2.json.annotations.JSON;


import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class PoHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = 1237646978536480319L;

    private BigDecimal poOid;

    private String poNo;

    private String docAction;

    private Date actionDate;

    private PoType poType;
    
    private String poSubType;
    
    private String poSubType2;
    
    private PoStatus poStatus;

    private Date poDate;

    private Date deliveryDateFrom;
    
    private Date deliveryDateTo;

    private Date expiryDate;

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

    private String deptCode;

    private String deptName;

    private String subDeptCode;

    private String subDeptName;

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

    private String shipToCode;

    private String shipToName;

    private String shipToAddr1;

    private String shipToAddr2;

    private String shipToAddr3;

    private String shipToAddr4;

    private String shipToCity;

    private String shipToState;

    private String shipToCtryCode;

    private String shipToPostalCode;

    private String creditTermCode;

    private String creditTermDesc;

    private BigDecimal totalCost;

    private BigDecimal additionalDiscountAmount;

    private BigDecimal additionalDiscountPercent;

    private BigDecimal netCost;

    private BigDecimal grossProfitMargin;

    private BigDecimal totalSharedCost;

    private BigDecimal totalGrossCost;

    private BigDecimal totalRetailAmount;

    private String orderRemarks;
    
    private Date periodStartDate;
    
    private Date periodEndDate;
    
    private Boolean duplicate;
    
    public String computePdfFilename()
    {
        return MsgType.PO.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getPoNo())
            + DOC_FILENAME_DELIMITOR + this.getPoOid() + ".pdf";
    }
    
    

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


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo == null ? null : poNo.trim();
    }


    public String getDocAction()
    {
        return docAction;
    }


    public void setDocAction(String docAction)
    {
        this.docAction = docAction == null ? null : docAction.trim();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getActionDate()
    {
        return actionDate == null ? null : (Date)actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }


    public PoType getPoType()
    {
        return poType;
    }


    public void setPoType(PoType poType)
    {
        this.poType = poType;
    }


    public String getPoSubType2()
    {
        return poSubType2;
    }


    public void setPoSubType2(String poSubType2)
    {
        this.poSubType2 = poSubType2;
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
    public Date getDeliveryDateFrom()
    {
        return deliveryDateFrom == null ? null : (Date)deliveryDateFrom.clone();
    }


    public void setDeliveryDateFrom(Date deliveryDateFrom)
    {
        this.deliveryDateFrom = deliveryDateFrom == null ? null : (Date)deliveryDateFrom
            .clone();
    }
    
    
    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getDeliveryDateTo()
    {
        return deliveryDateTo == null ? null : (Date)deliveryDateTo.clone();
    }


    public void setDeliveryDateTo(Date deliveryDateTo)
    {
        this.deliveryDateTo = deliveryDateTo == null ? null : (Date)deliveryDateTo
            .clone();
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
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }


    public String getBuyerAddr1()
    {
        return buyerAddr1;
    }


    public void setBuyerAddr1(String buyerAddr1)
    {
        this.buyerAddr1 = buyerAddr1 == null ? null : buyerAddr1.trim();
    }


    public String getBuyerAddr2()
    {
        return buyerAddr2;
    }


    public void setBuyerAddr2(String buyerAddr2)
    {
        this.buyerAddr2 = buyerAddr2 == null ? null : buyerAddr2.trim();
    }


    public String getBuyerAddr3()
    {
        return buyerAddr3;
    }


    public void setBuyerAddr3(String buyerAddr3)
    {
        this.buyerAddr3 = buyerAddr3 == null ? null : buyerAddr3.trim();
    }


    public String getBuyerAddr4()
    {
        return buyerAddr4;
    }


    public void setBuyerAddr4(String buyerAddr4)
    {
        this.buyerAddr4 = buyerAddr4 == null ? null : buyerAddr4.trim();
    }


    public String getBuyerCity()
    {
        return buyerCity;
    }


    public void setBuyerCity(String buyerCity)
    {
        this.buyerCity = buyerCity == null ? null : buyerCity.trim();
    }


    public String getBuyerState()
    {
        return buyerState;
    }


    public void setBuyerState(String buyerState)
    {
        this.buyerState = buyerState == null ? null : buyerState.trim();
    }


    public String getBuyerCtryCode()
    {
        return buyerCtryCode;
    }


    public void setBuyerCtryCode(String buyerCtryCode)
    {
        this.buyerCtryCode = buyerCtryCode == null ? null : buyerCtryCode
            .trim();
    }


    public String getBuyerPostalCode()
    {
        return buyerPostalCode;
    }


    public void setBuyerPostalCode(String buyerPostalCode)
    {
        this.buyerPostalCode = buyerPostalCode == null ? null : buyerPostalCode
            .trim();
    }


    public String getDeptCode()
    {
        return deptCode;
    }


    public void setDeptCode(String deptCode)
    {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }


    public String getDeptName()
    {
        return deptName;
    }


    public void setDeptName(String deptName)
    {
        this.deptName = deptName == null ? null : deptName.trim();
    }


    public String getSubDeptCode()
    {
        return subDeptCode;
    }


    public void setSubDeptCode(String subDeptCode)
    {
        this.subDeptCode = subDeptCode == null ? null : subDeptCode.trim();
    }


    public String getSubDeptName()
    {
        return subDeptName;
    }


    public void setSubDeptName(String subDeptName)
    {
        this.subDeptName = subDeptName == null ? null : subDeptName.trim();
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
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }


    public String getSupplierAddr1()
    {
        return supplierAddr1;
    }


    public void setSupplierAddr1(String supplierAddr1)
    {
        this.supplierAddr1 = supplierAddr1 == null ? null : supplierAddr1.trim();
    }


    public String getSupplierAddr2()
    {
        return supplierAddr2;
    }


    public void setSupplierAddr2(String supplierAddr2)
    {
        this.supplierAddr2 = supplierAddr2 == null ? null : supplierAddr2.trim();
    }


    public String getSupplierAddr3()
    {
        return supplierAddr3;
    }


    public void setSupplierAddr3(String supplierAddr3)
    {
        this.supplierAddr3 = supplierAddr3 == null ? null : supplierAddr3.trim();
    }


    public String getSupplierAddr4()
    {
        return supplierAddr4;
    }


    public void setSupplierAddr4(String supplierAddr4)
    {
        this.supplierAddr4 = supplierAddr4 == null ? null : supplierAddr4.trim();
    }


    public String getSupplierCity()
    {
        return supplierCity;
    }


    public void setSupplierCity(String supplierCity)
    {
        this.supplierCity = supplierCity == null ? null : supplierCity.trim();
    }


    public String getSupplierState()
    {
        return supplierState;
    }


    public void setSupplierState(String supplierState)
    {
        this.supplierState = supplierState == null ? null : supplierState
            .trim();
    }


    public String getSupplierCtryCode()
    {
        return supplierCtryCode;
    }


    public void setSupplierCtryCode(String supplierCtryCode)
    {
        this.supplierCtryCode = supplierCtryCode == null ? null
            : supplierCtryCode.trim();
    }


    public String getSupplierPostalCode()
    {
        return supplierPostalCode;
    }


    public void setSupplierPostalCode(String supplierPostalCode)
    {
        this.supplierPostalCode = supplierPostalCode == null ? null
            : supplierPostalCode.trim();
    }


    public String getShipToCode()
    {
        return shipToCode;
    }


    public void setShipToCode(String shipToCode)
    {
        this.shipToCode = shipToCode == null ? null : shipToCode.trim();
    }


    public String getShipToName()
    {
        return shipToName;
    }


    public void setShipToName(String shipToName)
    {
        this.shipToName = shipToName == null ? null : shipToName.trim();
    }


    public String getShipToAddr1()
    {
        return shipToAddr1;
    }


    public void setShipToAddr1(String shipToAddr1)
    {
        this.shipToAddr1 = shipToAddr1 == null ? null : shipToAddr1.trim();
    }


    public String getShipToAddr2()
    {
        return shipToAddr2;
    }


    public void setShipToAddr2(String shipToAddr2)
    {
        this.shipToAddr2 = shipToAddr2 == null ? null : shipToAddr2.trim();
    }


    public String getShipToAddr3()
    {
        return shipToAddr3;
    }


    public void setShipToAddr3(String shipToAddr3)
    {
        this.shipToAddr3 = shipToAddr3 == null ? null : shipToAddr3.trim();
    }


    public String getShipToAddr4()
    {
        return shipToAddr4;
    }


    public void setShipToAddr4(String shipToAddr4)
    {
        this.shipToAddr4 = shipToAddr4 == null ? null : shipToAddr4.trim();
    }


    public String getShipToCity()
    {
        return shipToCity;
    }


    public void setShipToCity(String shipToCity)
    {
        this.shipToCity = shipToCity == null ? null : shipToCity.trim();
    }


    public String getShipToState()
    {
        return shipToState;
    }


    public void setShipToState(String shipToState)
    {
        this.shipToState = shipToState == null ? null : shipToState.trim();
    }


    public String getShipToCtryCode()
    {
        return shipToCtryCode;
    }


    public void setShipToCtryCode(String shipToCtryCode)
    {
        this.shipToCtryCode = shipToCtryCode == null ? null : shipToCtryCode.trim();
    }


    public String getShipToPostalCode()
    {
        return shipToPostalCode;
    }


    public void setShipToPostalCode(String shipToPostalCode)
    {
        this.shipToPostalCode = shipToPostalCode == null ? null
            : shipToPostalCode.trim();
    }


    public String getCreditTermCode()
    {
        return creditTermCode;
    }


    public void setCreditTermCode(String creditTermCode)
    {
        this.creditTermCode = creditTermCode == null ? null : creditTermCode
            .trim();
    }


    public String getCreditTermDesc()
    {
        return creditTermDesc;
    }


    public void setCreditTermDesc(String creditTermDesc)
    {
        this.creditTermDesc = creditTermDesc == null ? null : creditTermDesc.trim();
    }


    public BigDecimal getTotalCost()
    {
        return totalCost;
    }


    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }


    public BigDecimal getAdditionalDiscountAmount()
    {
        return additionalDiscountAmount;
    }


    public void setAdditionalDiscountAmount(BigDecimal additionalDiscountAmount)
    {
        this.additionalDiscountAmount = additionalDiscountAmount;
    }


    public BigDecimal getAdditionalDiscountPercent()
    {
        return additionalDiscountPercent;
    }


    public void setAdditionalDiscountPercent(BigDecimal additionalDiscountPercent)
    {
        this.additionalDiscountPercent = additionalDiscountPercent;
    }


    public BigDecimal getNetCost()
    {
        return netCost;
    }


    public void setNetCost(BigDecimal netCost)
    {
        this.netCost = netCost;
    }


    public BigDecimal getGrossProfitMargin()
    {
        return grossProfitMargin;
    }


    public void setGrossProfitMargin(BigDecimal grossProfitMargin)
    {
        this.grossProfitMargin = grossProfitMargin;
    }


    public BigDecimal getTotalSharedCost()
    {
        return totalSharedCost;
    }


    public void setTotalSharedCost(BigDecimal totalSharedCost)
    {
        this.totalSharedCost = totalSharedCost;
    }


    public BigDecimal getTotalGrossCost()
    {
        return totalGrossCost;
    }


    public void setTotalGrossCost(BigDecimal totalGrossCost)
    {
        this.totalGrossCost = totalGrossCost;
    }


    public BigDecimal getTotalRetailAmount()
    {
        return totalRetailAmount;
    }


    public void setTotalRetailAmount(BigDecimal totalRetailAmount)
    {
        this.totalRetailAmount = totalRetailAmount;
    }


    public String getOrderRemarks()
    {
        return orderRemarks;
    }


    public void setOrderRemarks(String orderRemarks)
    {
        this.orderRemarks = orderRemarks == null ? null : orderRemarks.trim();
    }


    public String getPoSubType()
    {
        return poSubType;
    }


    public void setPoSubType(String poSubType)
    {
        this.poSubType = poSubType;
    }


    public PoStatus getPoStatus()
    {
        return poStatus;
    }


    public void setPoStatus(PoStatus status)
    {
        this.poStatus = status;
    }


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


    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(delimiterChar)
        .append(poNo).append(delimiterChar)
        .append(docAction).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(poType).append(delimiterChar)
        .append(poSubType).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(poDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(deliveryDateFrom, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(deliveryDateTo, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(expiryDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(buyerCode).append(delimiterChar)
        .append(buyerName).append(delimiterChar)
        .append(buyerAddr1).append(delimiterChar)
        .append(buyerAddr2).append(delimiterChar)
        .append(buyerAddr3).append(delimiterChar)
        .append(buyerAddr4).append(delimiterChar)
        .append(buyerCity).append(delimiterChar)
        .append(buyerState).append(delimiterChar)
        .append(buyerCtryCode).append(delimiterChar)
        .append(buyerPostalCode).append(delimiterChar)
        .append(supplierCode).append(delimiterChar)
        .append(supplierName).append(delimiterChar)
        .append(supplierAddr1).append(delimiterChar)
        .append(supplierAddr2).append(delimiterChar)
        .append(supplierAddr3).append(delimiterChar)
        .append(supplierAddr4).append(delimiterChar)
        .append(supplierCity).append(delimiterChar)
        .append(supplierState).append(delimiterChar)
        .append(supplierCtryCode).append(delimiterChar)
        .append(supplierPostalCode).append(delimiterChar)
        .append(shipToCode).append(delimiterChar)
        .append(shipToName).append(delimiterChar)
        .append(shipToAddr1).append(delimiterChar)
        .append(shipToAddr2).append(delimiterChar)
        .append(shipToAddr3).append(delimiterChar)
        .append(shipToAddr4).append(delimiterChar)
        .append(shipToCity).append(delimiterChar)
        .append(shipToState).append(delimiterChar)
        .append(shipToCtryCode).append(delimiterChar)
        .append(shipToPostalCode).append(delimiterChar)
        .append(deptCode).append(delimiterChar)
        .append(deptName).append(delimiterChar)
        .append(subDeptCode).append(delimiterChar)
        .append(subDeptName).append(delimiterChar)
        .append(creditTermCode).append(delimiterChar)
        .append(creditTermDesc).append(delimiterChar)
        .append(totalCost).append(delimiterChar)
        .append(additionalDiscountAmount).append(delimiterChar)
        .append(additionalDiscountPercent).append(delimiterChar)
        .append(netCost).append(delimiterChar)
        .append(grossProfitMargin).append(delimiterChar)
        .append(totalSharedCost).append(delimiterChar)//document is total shared cost
        .append(totalGrossCost).append(delimiterChar)
        .append(totalRetailAmount).append(delimiterChar)
        .append(orderRemarks).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(periodStartDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(periodEndDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(poSubType2);
        return buffer.toString();
    }
    

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(poOid);
    }
    
    public static PoHeaderHolder achieveValidPoHeader(List<PoHeaderHolder> poHeaders)
    {
        if (poHeaders == null)
        {
            return null;
        }
        for (PoHeaderHolder poHeader : poHeaders)
        {
            if (poHeader.getPoStatus().equals(PoStatus.NEW) || poHeader.getPoStatus().equals(PoStatus.INVOICED) 
                    || poHeader.getPoStatus().equals(PoStatus.CREDITED) 
                    || poHeader.getPoStatus().equals(PoStatus.PARTIAL_INVOICED))
            {
                return poHeader;
            }
        }
        for (PoHeaderHolder poHeader : poHeaders)
        {
            if (poHeader.getPoStatus().equals(PoStatus.AMENDED))
            {
                return poHeader;
            }
        }
        return null;
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getPoNo();
    }
}