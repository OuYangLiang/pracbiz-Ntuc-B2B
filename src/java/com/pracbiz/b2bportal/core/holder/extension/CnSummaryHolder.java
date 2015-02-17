package com.pracbiz.b2bportal.core.holder.extension;

import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CnSummaryHolder extends MsgTransactionsExHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String cnNo;
    private String cnType;
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
    private String buyerCode;
    private String buyerName;
    private String supplierCode;
    private String supplierName;
    private String storeCode;
    private String storeName;
    private String cnRemarks;
    private CnStatus ctrlStatus;
    private Boolean duplicate;
    private Date createDate;
    private Date procDate;
    private Date sentDate;
    private Date outDate;
    private ReadStatus readStatus;
    private Date readDate;
    
    private Date cnDateFrom;
    private Date cnDateTo;
    private Date sentDateFrom;
    private Date sentDateTo;


    public String getCnNo()
    {
        return cnNo;
    }


    public void setCnNo(String cnNo)
    {
        this.cnNo = cnNo;
    }


    public String getCnType()
    {
        return cnType;
    }


    public void setCnType(String cnType)
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getGiDate()
    {
        return giDate == null ? null : (Date) giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date) giDate.clone();
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


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getProcDate()
    {
        return procDate == null ? null : (Date) procDate.clone();
    }


    public void setProcDate(Date procDate)
    {
        this.procDate = procDate == null ? null : (Date) procDate.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getSentDate()
    {
        return sentDate == null ? null : (Date) sentDate.clone();
    }


    public void setSentDate(Date sentDate)
    {
        this.sentDate = sentDate == null ? null : (Date) sentDate.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getOutDate()
    {
        return outDate == null ? null : (Date) outDate.clone();
    }


    public void setOutDate(Date outDate)
    {
        this.outDate = outDate == null ? null : (Date) outDate.clone();
    }


    public ReadStatus getReadStatus()
    {
        return readStatus;
    }


    public void setReadStatus(ReadStatus readStatus)
    {
        this.readStatus = readStatus;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getReadDate()
    {
        return readDate == null ? null : (Date) readDate.clone();
    }


    public void setReadDate(Date readDate)
    {
        this.readDate = readDate == null ? null : (Date) readDate.clone();
    }


    public Date getCnDateFrom()
    {
        return cnDateFrom == null ? null : (Date) cnDateFrom.clone();
    }


    public void setCnDateFrom(Date cnDateFrom)
    {
        this.cnDateFrom = cnDateFrom == null ? null : (Date) cnDateFrom.clone();
    }


    public Date getCnDateTo()
    {
        return cnDateTo == null ? null : (Date) cnDateTo.clone();
    }


    public void setCnDateTo(Date cnDateTo)
    {
        this.cnDateTo = cnDateTo == null ? null : (Date) cnDateTo.clone();
    }


    public Date getSentDateFrom()
    {
        return sentDateFrom == null ? null : (Date) sentDateFrom.clone();
    }


    public void setSentDateFrom(Date sentDateFrom)
    {
        this.sentDateFrom = sentDateFrom == null ? null : (Date) sentDateFrom.clone();
    }


    public Date getSentDateTo()
    {
        return sentDateTo == null ? null : (Date) sentDateTo.clone();
    }


    public void setSentDateTo(Date sentDateTo)
    {
        this.sentDateTo = sentDateTo == null ? null : (Date) sentDateTo.clone();
    }


}
