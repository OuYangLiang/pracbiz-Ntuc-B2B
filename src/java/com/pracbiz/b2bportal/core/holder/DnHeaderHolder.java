package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = -5012845630295605479L;

    private BigDecimal dnOid;

    private String dnNo;

    private String docAction;

    private Date actionDate;

    private String dnType;

    private String dnTypeDesc;

    private Date dnDate;

    private String poNo;

    private Date poDate;

    private String invNo;

    private Date invDate;

    private String rtvNo;

    private Date rtvDate;

    private String giNo;

    private Date giDate;

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

    private BigDecimal totalCost;

    private BigDecimal totalVat;

    private BigDecimal totalCostWithVat;

    private BigDecimal vatRate;

    private String reasonCode;

    private String reasonDesc;

    private String dnRemarks;

    private Boolean sentToSupplier;

    private Boolean markSentToSupplier;

    private Boolean duplicate;

    private DnStatus dnStatus;
    
    private DnPriceStatus priceStatus;
    
    private DnQtyStatus qtyStatus;
    
    private DnBuyerStatus buyerStatus;
    
    private Boolean dispute;
    
    private String disputeBy;
    
    private Date disputeDate;
    
    private Boolean exported;
    
    private Date exportedDate;
    
    private Date exportedDateFrom;
    
    private Date exportedDateTo;
    
    private Boolean closed;
    
    private String closedBy;
    
    private Date closedDate;
    
    private String closedRemarks;
    
    private Boolean priceDisputed;
    
    private Boolean qtyDisputed;
    


    public String computePdfFilename()
    {
        return MsgType.DN.name() + DOC_FILENAME_DELIMITOR + this.getBuyerCode()
                + DOC_FILENAME_DELIMITOR + this.getSupplierCode()
                + DOC_FILENAME_DELIMITOR
                + StringUtil.getInstance().convertDocNo(this.getDnNo())
                + DOC_FILENAME_DELIMITOR + this.getDnOid() + ".pdf";
    }


    public BigDecimal getDnOid()
    {
        return dnOid;
    }


    public void setDnOid(BigDecimal dnOid)
    {
        this.dnOid = dnOid;
    }


    public String getDnNo()
    {
        return dnNo;
    }


    public void setDnNo(String dnNo)
    {
        this.dnNo = dnNo == null ? null : dnNo.trim();
    }


    public String getDocAction()
    {
        return docAction;
    }


    public void setDocAction(String docAction)
    {
        this.docAction = docAction == null ? null : docAction.trim();
    }


    public Date getActionDate()
    {
        return actionDate == null ? null : (Date) actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date) actionDate.clone();
    }


    public String getDnType()
    {
        return dnType;
    }


    public void setDnType(String dnType)
    {
        this.dnType = dnType == null ? null : dnType.trim();
    }


    public String getDnTypeDesc()
    {
        return dnTypeDesc;
    }


    public void setDnTypeDesc(String dnTypeDesc)
    {
        this.dnTypeDesc = dnTypeDesc == null ? null : dnTypeDesc.trim();
    }


    public Date getDnDate()
    {
        return dnDate == null ? null : (Date) dnDate.clone();
    }


    public void setDnDate(Date dnDate)
    {
        this.dnDate = dnDate == null ? null : (Date) dnDate.clone();
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


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date) rtvDate.clone();
    }


    public String getGiNo()
    {
        return giNo;
    }


    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }


    public Date getGiDate()
    {
        return giDate == null ? null : (Date) giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date) giDate.clone();
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
        this.deptName = deptName;
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
        this.supplierAddr1 = supplierAddr1 == null ? null : supplierAddr1
                .trim();
    }


    public String getSupplierAddr2()
    {
        return supplierAddr2;
    }


    public void setSupplierAddr2(String supplierAddr2)
    {
        this.supplierAddr2 = supplierAddr2 == null ? null : supplierAddr2
                .trim();
    }


    public String getSupplierAddr3()
    {
        return supplierAddr3;
    }


    public void setSupplierAddr3(String supplierAddr3)
    {
        this.supplierAddr3 = supplierAddr3 == null ? null : supplierAddr3
                .trim();
    }


    public String getSupplierAddr4()
    {
        return supplierAddr4;
    }


    public void setSupplierAddr4(String supplierAddr4)
    {
        this.supplierAddr4 = supplierAddr4 == null ? null : supplierAddr4
                .trim();
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


    public BigDecimal getTotalCost()
    {
        return totalCost;
    }


    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }


    public BigDecimal getTotalVat()
    {
        return totalVat;
    }


    public void setTotalVat(BigDecimal totalVat)
    {
        this.totalVat = totalVat;
    }


    public BigDecimal getTotalCostWithVat()
    {
        return totalCostWithVat;
    }


    public void setTotalCostWithVat(BigDecimal totalCostWithVat)
    {
        this.totalCostWithVat = totalCostWithVat;
    }


    public BigDecimal getVatRate()
    {
        return vatRate;
    }


    public void setVatRate(BigDecimal vatRate)
    {
        this.vatRate = vatRate;
    }


    public String getReasonCode()
    {
        return reasonCode;
    }


    public void setReasonCode(String reasonCode)
    {
        this.reasonCode = reasonCode == null ? null : reasonCode.trim();
    }


    public String getReasonDesc()
    {
        return reasonDesc;
    }


    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc == null ? null : reasonDesc.trim();
    }


    public String getDnRemarks()
    {
        return dnRemarks;
    }


    public void setDnRemarks(String dnRemarks)
    {
        this.dnRemarks = dnRemarks == null ? null : dnRemarks.trim();
    }


    public Boolean getSentToSupplier()
    {
        return sentToSupplier;
    }


    public void setSentToSupplier(Boolean sentToSupplier)
    {
        this.sentToSupplier = sentToSupplier;
    }


    public Boolean getMarkSentToSupplier()
    {
        return markSentToSupplier;
    }


    public void setMarkSentToSupplier(Boolean markSentToSupplier)
    {
        this.markSentToSupplier = markSentToSupplier;
    }


    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public DnStatus getDnStatus()
    {
        return dnStatus;
    }


    public void setDnStatus(DnStatus dnStatus)
    {
        this.dnStatus = dnStatus;
    }


    public DnPriceStatus getPriceStatus()
    {
        return priceStatus;
    }


    public void setPriceStatus(DnPriceStatus priceStatus)
    {
        this.priceStatus = priceStatus;
    }


    public DnQtyStatus getQtyStatus()
    {
        return qtyStatus;
    }


    public void setQtyStatus(DnQtyStatus qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }


    public DnBuyerStatus getBuyerStatus()
    {
        return buyerStatus;
    }


    public void setBuyerStatus(DnBuyerStatus buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }


    public Boolean getDispute()
    {
        return dispute;
    }


    public void setDispute(Boolean dispute)
    {
        this.dispute = dispute;
    }


    public String getDisputeBy()
    {
        return disputeBy;
    }


    public void setDisputeBy(String disputeBy)
    {
        this.disputeBy = disputeBy;
    }


    public Date getDisputeDate()
    {
        return disputeDate == null ? null : (Date) disputeDate.clone();
    }


    public void setDisputeDate(Date disputeDate)
    {
        this.disputeDate = disputeDate == null ? null : (Date) disputeDate.clone();
    }


    public Boolean getExported()
    {
        return exported;
    }


    public void setExported(Boolean exported)
    {
        this.exported = exported;
    }


    public Date getExportedDate()
    {
        return exportedDate == null ? null : (Date) exportedDate.clone();
    }


    public void setExportedDate(Date exportedDate)
    {
        this.exportedDate = exportedDate == null ? null : (Date) exportedDate.clone();
    }


    public Date getExportedDateFrom()
    {
        return exportedDateFrom == null ? null : (Date) exportedDateFrom.clone();
    }


    public void setExportedDateFrom(Date exportedDateFrom)
    {
        this.exportedDateFrom = exportedDateFrom == null ? null : (Date) exportedDateFrom.clone();
    }


    public Date getExportedDateTo()
    {
        return exportedDateTo == null ? null : (Date) exportedDateTo.clone();
    }


    public void setExportedDateTo(Date exportedDateTo)
    {
        this.exportedDateTo = exportedDateTo == null ? null : (Date) exportedDateTo.clone();
    }


    public Boolean getClosed()
    {
        return closed;
    }


    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }


    public String getClosedBy()
    {
        return closedBy;
    }


    public void setClosedBy(String closedBy)
    {
        this.closedBy = closedBy;
    }


    public Date getClosedDate()
    {
        return closedDate == null ? null : (Date) closedDate.clone();
    }


    public void setClosedDate(Date closedDate)
    {
        this.closedDate = closedDate == null ? null : (Date) closedDate.clone();
    }


    public String getClosedRemarks()
    {
        return closedRemarks;
    }


    public void setClosedRemarks(String closedRemarks)
    {
        this.closedRemarks = closedRemarks;
    }


    public Boolean getPriceDisputed()
    {
        return priceDisputed;
    }


    public void setPriceDisputed(Boolean priceDisputed)
    {
        this.priceDisputed = priceDisputed;
    }


    public Boolean getQtyDisputed()
    {
        return qtyDisputed;
    }


    public void setQtyDisputed(Boolean qtyDisputed)
    {
        this.qtyDisputed = qtyDisputed;
    }


    public String toStringWithDelimiterCharacter(String delimiterChar)
            throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(delimiterChar)
                .append(dnNo)
                .append(delimiterChar)
                .append(docAction)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(actionDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(dnType)
                .append(delimiterChar)
                .append(dnTypeDesc)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(dnDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
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
                .append(delimiterChar)
                .append(rtvNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(rtvDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(giNo)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(giDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar).append(buyerCode).append(delimiterChar)
                .append(buyerName).append(delimiterChar).append(buyerAddr1)
                .append(delimiterChar).append(buyerAddr2).append(delimiterChar)
                .append(buyerAddr3).append(delimiterChar).append(buyerAddr4)
                .append(delimiterChar).append(buyerCity).append(delimiterChar)
                .append(buyerState).append(delimiterChar).append(buyerCtryCode)
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
                .append(delimiterChar).append(deptCode).append(delimiterChar)
                .append(deptName).append(delimiterChar).append(subDeptCode)
                .append(delimiterChar).append(subDeptName)
                .append(delimiterChar).append(totalCost).append(delimiterChar)
                .append(totalVat).append(delimiterChar)
                .append(totalCostWithVat).append(delimiterChar).append(vatRate)
                .append(delimiterChar).append(reasonCode).append(delimiterChar)
                .append(reasonDesc).append(delimiterChar).append(dnRemarks)
                .append(delimiterChar);
        return buffer.toString();
    }
    
    
    public MsgTransactionsHolder convertToMsgTransaction(String originalFilename)
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();
        msg.setDocOid(getDnOid());
        msg.setMsgType("DN");
        msg.setMsgRefNo(getDnNo());
        msg.setBuyerOid(getBuyerOid());
        msg.setBuyerCode(getBuyerCode());
        msg.setBuyerName(getBuyerName());
        msg.setSupplierOid(getSupplierOid());
        msg.setSupplierCode(getSupplierCode());
        msg.setSupplierName(getSupplierName());
        msg.setOriginalFilename(originalFilename);
        msg.setReportFilename(computePdfFilename());
        msg.setCreateDate(new Date());
        msg.setReadStatus(ReadStatus.UNREAD);
        msg.setActive(true);
        msg.setValid(true);
        return msg;
    }
    
    
    public Integer calculateDayElapsed()
    {
        return DateUtil.getInstance().daysAfterDate(disputeDate, new Date());
    }


    @Override
    public String getCustomIdentification()
    {
        return dnOid == null ? null : dnOid.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return dnNo;
    }
}