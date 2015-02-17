package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;


import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.CnType;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class CnHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private BigDecimal cnOid;
    private String cnNo;
    private String docAction;
    private Date actionDate;
    private CnType cnType;
    private String cnTypeDesc;
    private Date cnDate;
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
    private String cnRemarks;
    private CnStatus ctrlStatus;

    private Boolean duplicate;

    public String computePdfFilename()
    {
        return MsgType.CN.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getCnNo())
            + DOC_FILENAME_DELIMITOR + this.getCnOid() + ".pdf";
    }
    
    
    public MsgTransactionsHolder toMsgTransactions()
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();
        msg.setDocOid(cnOid);
        msg.setMsgType(MsgType.CN.name());
        msg.setMsgRefNo(cnNo);
        msg.setBuyerOid(buyerOid);
        msg.setBuyerCode(buyerCode);
        msg.setBuyerName(buyerName);
        msg.setSupplierOid(supplierOid);
        msg.setSupplierCode(supplierCode);
        msg.setSupplierName(supplierName);
        msg.setCreateDate(new Date());
        msg.setReadStatus(ReadStatus.UNREAD);
        msg.setActive(true);
        msg.setValid(true);
        msg.setReportFilename(this.computePdfFilename());
        msg.setGeneratedOnPortal(true);
        
        return msg;
    }
    
    

    public BigDecimal getCnOid()
    {
        return cnOid;
    }


    public void setCnOid(BigDecimal cnOid)
    {
        this.cnOid = cnOid;
    }


    public String getCnNo()
    {
        return cnNo;
    }


    public void setCnNo(String cnNo)
    {
        this.cnNo = cnNo;
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
        return actionDate == null ? null : (Date) actionDate.clone();
    }


    public void setActionDate(Date actionDate)
    {
        this.actionDate = actionDate == null ? null : (Date) actionDate.clone();
    }


    public CnType getCnType()
    {
        return cnType;
    }


    public void setCnType(CnType cnType)
    {
        this.cnType = cnType;
    }


    public String getCnTypeDesc()
    {
        return cnTypeDesc;
    }


    public void setCnTypeDesc(String cnTypeDesc)
    {
        this.cnTypeDesc = cnTypeDesc;
    }


    public Date getCnDate()
    {
        return cnDate == null ? null : (Date) cnDate.clone();
    }


    public void setCnDate(Date cnDate)
    {
        this.cnDate = cnDate == null ? null : (Date) cnDate.clone();
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
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
        this.reasonCode = reasonCode;
    }


    public String getReasonDesc()
    {
        return reasonDesc;
    }


    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc;
    }


    public String getCnRemarks()
    {
        return cnRemarks;
    }


    public void setCnRemarks(String cnRemarks)
    {
        this.cnRemarks = cnRemarks;
    }


    public CnStatus getCtrlStatus()
    {
        return ctrlStatus;
    }


    public void setCtrlStatus(CnStatus ctrlStatus)
    {
        this.ctrlStatus = ctrlStatus;
    }


    public Boolean getDuplicate()
    {
        return duplicate;
    }


    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }


    public String toStringWithDelimiterCharacter(String delimiterChar)
            throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(delimiterChar)
                .append(cnNo)
                .append(delimiterChar)
                .append(docAction)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(actionDate,
                        DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS))
                .append(delimiterChar)
                .append(cnType)
                .append(delimiterChar)
                .append(cnTypeDesc)
                .append(delimiterChar)
                .append(DateUtil.getInstance().convertDateToString(cnDate,
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
                .append(reasonDesc).append(delimiterChar).append(cnRemarks)
                .append(delimiterChar);
        return buffer.toString();
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return cnNo;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(cnOid);
    }
}