package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

public class RtvHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = 8347970587554167565L;

    private BigDecimal rtvOid;

    private String rtvNo;

    private String docAction;

    private Date actionDate;

    private Date rtvDate;
    
    private Date collectionDate;

    private String doNo;

    private Date doDate;

    private String invNo;

    private Date invDate;

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

    private BigDecimal totalCost;

    private String reasonCode;

    private String reasonDesc;

    private String rtvRemarks;
    
    private Boolean duplicate;
    
    private RtvStatus rtvStatus;
    
    public String computePdfFilename()
    {
        return MsgType.RTV.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getRtvNo())
            + DOC_FILENAME_DELIMITOR + this.getRtvOid() + ".pdf";
    }

    public BigDecimal getRtvOid() {
        return rtvOid;
    }

    public void setRtvOid(BigDecimal rtvOid) {
        this.rtvOid = rtvOid;
    }

    public String getRtvNo() {
        return rtvNo;
    }

    public void setRtvNo(String rtvNo) {
        this.rtvNo = rtvNo == null ? null : rtvNo.trim();
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction == null ? null : docAction.trim();
    }

    public Date getActionDate() {
        return actionDate == null ? null : (Date)actionDate.clone();
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }

    public Date getRtvDate() {
        return rtvDate == null ? null : (Date)rtvDate.clone();
    }

    public void setRtvDate(Date rtvDate) {
        this.rtvDate = rtvDate == null ? null : (Date)rtvDate.clone();
    }

    public Date getCollectionDate()
    {
        return collectionDate == null ? null : (Date) collectionDate.clone();
    }

    public void setCollectionDate(Date collectionDate)
    {
        this.collectionDate = collectionDate == null ? null : (Date) collectionDate.clone();
    }

    public String getDoNo() {
        return doNo;
    }

    public void setDoNo(String doNo) {
        this.doNo = doNo == null ? null : doNo.trim();
    }

    public Date getDoDate() {
        return doDate == null ? null : (Date)doDate.clone();
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate == null ? null : (Date)doDate.clone();
    }

    public String getInvNo() {
        return invNo;
    }

    public void setInvNo(String invNo) {
        this.invNo = invNo == null ? null : invNo.trim();
    }

    public Date getInvDate() {
        return invDate == null ? null : (Date)invDate.clone();
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
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

    public String getBuyerAddr1() {
        return buyerAddr1;
    }

    public void setBuyerAddr1(String buyerAddr1) {
        this.buyerAddr1 = buyerAddr1 == null ? null : buyerAddr1.trim();
    }

    public String getBuyerAddr2() {
        return buyerAddr2;
    }

    public void setBuyerAddr2(String buyerAddr2) {
        this.buyerAddr2 = buyerAddr2 == null ? null : buyerAddr2.trim();
    }

    public String getBuyerAddr3() {
        return buyerAddr3;
    }

    public void setBuyerAddr3(String buyerAddr3) {
        this.buyerAddr3 = buyerAddr3 == null ? null : buyerAddr3.trim();
    }

    public String getBuyerAddr4() {
        return buyerAddr4;
    }

    public void setBuyerAddr4(String buyerAddr4) {
        this.buyerAddr4 = buyerAddr4 == null ? null : buyerAddr4.trim();
    }

    public String getBuyerCity() {
        return buyerCity;
    }

    public void setBuyerCity(String buyerCity) {
        this.buyerCity = buyerCity == null ? null : buyerCity.trim();
    }

    public String getBuyerState() {
        return buyerState;
    }

    public void setBuyerState(String buyerState) {
        this.buyerState = buyerState == null ? null : buyerState.trim();
    }

    public String getBuyerCtryCode() {
        return buyerCtryCode;
    }

    public void setBuyerCtryCode(String buyerCtryCode) {
        this.buyerCtryCode = buyerCtryCode == null ? null : buyerCtryCode.trim();
    }

    public String getBuyerPostalCode() {
        return buyerPostalCode;
    }

    public void setBuyerPostalCode(String buyerPostalCode) {
        this.buyerPostalCode = buyerPostalCode == null ? null : buyerPostalCode.trim();
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode == null ? null : deptCode.trim();
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName == null ? null : deptName.trim();
    }

    public String getSubDeptCode() {
        return subDeptCode;
    }

    public void setSubDeptCode(String subDeptCode) {
        this.subDeptCode = subDeptCode == null ? null : subDeptCode.trim();
    }

    public String getSubDeptName() {
        return subDeptName;
    }

    public void setSubDeptName(String subDeptName) {
        this.subDeptName = subDeptName == null ? null : subDeptName.trim();
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

    public String getSupplierAddr1() {
        return supplierAddr1;
    }

    public void setSupplierAddr1(String supplierAddr1) {
        this.supplierAddr1 = supplierAddr1 == null ? null : supplierAddr1.trim();
    }

    public String getSupplierAddr2() {
        return supplierAddr2;
    }

    public void setSupplierAddr2(String supplierAddr2) {
        this.supplierAddr2 = supplierAddr2 == null ? null : supplierAddr2.trim();
    }

    public String getSupplierAddr3() {
        return supplierAddr3;
    }

    public void setSupplierAddr3(String supplierAddr3) {
        this.supplierAddr3 = supplierAddr3 == null ? null : supplierAddr3.trim();
    }

    public String getSupplierAddr4() {
        return supplierAddr4;
    }

    public void setSupplierAddr4(String supplierAddr4) {
        this.supplierAddr4 = supplierAddr4 == null ? null : supplierAddr4.trim();
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity == null ? null : supplierCity.trim();
    }

    public String getSupplierState() {
        return supplierState;
    }

    public void setSupplierState(String supplierState) {
        this.supplierState = supplierState == null ? null : supplierState.trim();
    }

    public String getSupplierCtryCode() {
        return supplierCtryCode;
    }

    public void setSupplierCtryCode(String supplierCtryCode) {
        this.supplierCtryCode = supplierCtryCode == null ? null : supplierCtryCode.trim();
    }

    public String getSupplierPostalCode() {
        return supplierPostalCode;
    }

    public void setSupplierPostalCode(String supplierPostalCode) {
        this.supplierPostalCode = supplierPostalCode == null ? null : supplierPostalCode.trim();
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode == null ? null : reasonCode.trim();
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc == null ? null : reasonDesc.trim();
    }

    public String getRtvRemarks() {
        return rtvRemarks;
    }

    public void setRtvRemarks(String rtvRemarks) {
        this.rtvRemarks = rtvRemarks == null ? null : rtvRemarks.trim();
    }
    
    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public RtvStatus getRtvStatus()
    {
        return rtvStatus;
    }

    public void setRtvStatus(RtvStatus rtvStatus)
    {
        this.rtvStatus = rtvStatus;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar)
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(rtvNo).append(delimiterChar)
        .append(docAction).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(rtvDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(collectionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(doNo).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(doDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(invNo).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(invDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
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
        .append(deptCode).append(delimiterChar)
        .append(deptName).append(delimiterChar)
        .append(subDeptCode).append(delimiterChar)
        .append(subDeptName).append(delimiterChar)
        .append(totalCost).append(delimiterChar)
        .append(reasonCode).append(delimiterChar)
        .append(reasonDesc).append(delimiterChar)
        .append(rtvRemarks);
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(rtvOid);
    }
}