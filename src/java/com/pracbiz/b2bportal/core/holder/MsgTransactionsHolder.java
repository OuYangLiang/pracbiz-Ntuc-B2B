package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

public class MsgTransactionsHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = 4557678249329023642L;

    private BigDecimal docOid;

    private String msgType;

    private String msgRefNo;

    private BigDecimal buyerOid;

    private String buyerCode;

    private String buyerName;

    private BigDecimal supplierOid;

    private String supplierCode;

    private String supplierName;

    private Date createDate;

    private Date procDate;

    private Date sentDate;

    private Date outDate;

    private Date alertDate;

    private String originalFilename;

    private String exchangeFilename;

    private String reportFilename;

    private Boolean active;

    private Boolean valid;

    private String remarks;
    
    private Boolean generatedOnPortal;

    private BigDecimal batchOid;
    
    private ReadStatus readStatus;
    
    private Date readDate;

    public BigDecimal getDocOid() {
        return docOid;
    }

    public void setDocOid(BigDecimal docOid) {
        this.docOid = docOid;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType == null ? null : msgType.trim();
    }

    public String getMsgRefNo() {
        return msgRefNo;
    }

    public void setMsgRefNo(String msgRefNo) {
        this.msgRefNo = msgRefNo == null ? null : msgRefNo.trim();
    }

    public BigDecimal getBuyerOid() {
        return buyerOid;
    }

    public void setBuyerOid(BigDecimal buyerOid) {
        this.buyerOid = buyerOid;
    }

    public String getBuyerCode() {
        return buyerCode;
    }

    public void setBuyerCode(String buyerCode) {
        this.buyerCode = buyerCode == null ? null : buyerCode.trim();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    public BigDecimal getSupplierOid() {
        return supplierOid;
    }

    public void setSupplierOid(BigDecimal supplierOid) {
        this.supplierOid = supplierOid;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode == null ? null : supplierCode.trim();
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName == null ? null : supplierName.trim();
    }

    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getCreateDate() {
        return createDate == null ? null : (Date)createDate.clone();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }

    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getProcDate() {
        return procDate  == null ? null : (Date)procDate.clone();
    }

    public void setProcDate(Date procDate) {
        this.procDate = procDate == null ? null : (Date)procDate.clone();
    }

    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getSentDate() {
        return sentDate == null ? null : (Date)sentDate.clone();
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate == null ? null : (Date)sentDate.clone();
    }
    
    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getOutDate() {
        return outDate == null ? null : (Date)outDate.clone();
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate == null ? null : (Date)outDate.clone();
    }

    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getAlertDate() {
        return alertDate == null ? null : (Date)alertDate.clone();
    }

    public void setAlertDate(Date alertDate) {
        this.alertDate = alertDate == null ? null : (Date)alertDate.clone();
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename == null ? null : originalFilename.trim();
    }

    public String getExchangeFilename() {
        return exchangeFilename;
    }

    public void setExchangeFilename(String exchangeFilename) {
        this.exchangeFilename = exchangeFilename == null ? null : exchangeFilename.trim();
    }

    public String getReportFilename() {
        return reportFilename;
    }

    public void setReportFilename(String reportFilename) {
        this.reportFilename = reportFilename == null ? null : reportFilename.trim();
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }


    public Boolean getGeneratedOnPortal()
    {
        return generatedOnPortal;
    }

    public void setGeneratedOnPortal(Boolean generatedOnProtal)
    {
        this.generatedOnPortal = generatedOnProtal;
    }

    public BigDecimal getBatchOid() {
        return batchOid;
    }

    public void setBatchOid(BigDecimal batchOid) {
        this.batchOid = batchOid;
    }

    public ReadStatus getReadStatus()
    {
        return readStatus;
    }

    public void setReadStatus(ReadStatus readStatus)
    {
        this.readStatus = readStatus;
    }
    
    @JSON(format=CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getReadDate()
    {
        return readDate == null ? null : (Date)readDate.clone();
    }

    public void setReadDate(Date readDate)
    {
        this.readDate = readDate == null ? null : (Date) readDate.clone();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(docOid);
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return this.getMsgType() + "_" + this.getMsgRefNo();
    }
}