package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DisputeStatus;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class GrnHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = -8560918922086774664L;

    private BigDecimal grnOid;

    private String grnNo;

    private String docAction;

    private Date actionDate;

    private Date grnDate;

    private Date createDate;

    private String poNo;

    private Date poDate;

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

    private String receiveStoreCode;

    private String receiveStoreName;

    private String receiveStoreAddr1;

    private String receiveStoreAddr2;

    private String receiveStoreAddr3;

    private String receiveStoreAddr4;

    private String receiveStoreCity;

    private String receiveStoreState;

    private String receiveStoreCtryCode;

    private String receiveStorePostalCode;

    private BigDecimal totalExpectedQty;

    private BigDecimal totalReceivedQty;

    private BigDecimal itemCount;

    private BigDecimal discountAmount;

    private BigDecimal netCost;

    private BigDecimal totalCost;

    private BigDecimal totalRetailAmount;

    private String grnRemarks;
    
    private Boolean duplicate;
    
    private GrnStatus grnStatus;
    
    private Boolean dispute;
    
    private DisputeStatus disputeStatus;
    
    private String disputeBuyerBy;
    
    private Date disputeBuyerDate;
    
    private String disputeBuyerRemarks;
    
    private String disputeSupplierBy;
    
    private Date disputeSupplierDate;
    
    private String disputeSupplierRemarks;
    
    public String computePdfFilename()
    {
        return MsgType.GRN.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getGrnNo())
            + DOC_FILENAME_DELIMITOR + this.getGrnOid() + ".pdf";
    }

    public BigDecimal getGrnOid() {
        return grnOid;
    }

    public void setGrnOid(BigDecimal grnOid) {
        this.grnOid = grnOid;
    }

    public String getGrnNo() {
        return grnNo;
    }

    public void setGrnNo(String grnNo) {
        this.grnNo = grnNo == null ? null : grnNo.trim();
    }

    public String getDocAction() {
        return docAction;
    }

    public void setDocAction(String docAction) {
        this.docAction = docAction == null ? null : docAction.trim();
    }

    public Date getActionDate() {
        return actionDate  == null ? null : (Date)actionDate.clone();
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate == null ? null : (Date)actionDate.clone();
    }

    public Date getGrnDate() {
        return grnDate == null ? null : (Date)grnDate.clone();
    }

    public void setGrnDate(Date grnDate) {
        this.grnDate = grnDate == null ? null : (Date)grnDate.clone();
    }

    public Date getCreateDate() {
        return createDate == null ? null : (Date)createDate.clone();
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate == null ? null : (Date)createDate.clone();
    }

    public String getPoNo() {
        return poNo;
    }

    public void setPoNo(String poNo) {
        this.poNo = poNo == null ? null : poNo.trim();
    }

    public Date getPoDate() {
        return poDate == null ? null : (Date)poDate.clone();
    }

    public void setPoDate(Date poDate) {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
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

    public String getReceiveStoreCode() {
        return receiveStoreCode;
    }

    public void setReceiveStoreCode(String receiveStoreCode) {
        this.receiveStoreCode = receiveStoreCode == null ? null : receiveStoreCode.trim();
    }

    public String getReceiveStoreName() {
        return receiveStoreName;
    }

    public void setReceiveStoreName(String receiveStoreName) {
        this.receiveStoreName = receiveStoreName == null ? null : receiveStoreName.trim();
    }

    public String getReceiveStoreAddr1() {
        return receiveStoreAddr1;
    }

    public void setReceiveStoreAddr1(String receiveStoreAddr1) {
        this.receiveStoreAddr1 = receiveStoreAddr1 == null ? null : receiveStoreAddr1.trim();
    }

    public String getReceiveStoreAddr2() {
        return receiveStoreAddr2;
    }

    public void setReceiveStoreAddr2(String receiveStoreAddr2) {
        this.receiveStoreAddr2 = receiveStoreAddr2 == null ? null : receiveStoreAddr2.trim();
    }

    public String getReceiveStoreAddr3() {
        return receiveStoreAddr3;
    }

    public void setReceiveStoreAddr3(String receiveStoreAddr3) {
        this.receiveStoreAddr3 = receiveStoreAddr3 == null ? null : receiveStoreAddr3.trim();
    }

    public String getReceiveStoreAddr4() {
        return receiveStoreAddr4;
    }

    public void setReceiveStoreAddr4(String receiveStoreAddr4) {
        this.receiveStoreAddr4 = receiveStoreAddr4 == null ? null : receiveStoreAddr4.trim();
    }

    public String getReceiveStoreCity() {
        return receiveStoreCity;
    }

    public void setReceiveStoreCity(String receiveStoreCity) {
        this.receiveStoreCity = receiveStoreCity == null ? null : receiveStoreCity.trim();
    }

    public String getReceiveStoreState() {
        return receiveStoreState;
    }

    public void setReceiveStoreState(String receiveStoreState) {
        this.receiveStoreState = receiveStoreState == null ? null : receiveStoreState.trim();
    }

    public String getReceiveStoreCtryCode() {
        return receiveStoreCtryCode;
    }

    public void setReceiveStoreCtryCode(String receiveStoreCtryCode) {
        this.receiveStoreCtryCode = receiveStoreCtryCode == null ? null : receiveStoreCtryCode.trim();
    }

    public String getReceiveStorePostalCode() {
        return receiveStorePostalCode;
    }

    public void setReceiveStorePostalCode(String receiveStorePostalCode) {
        this.receiveStorePostalCode = receiveStorePostalCode == null ? null : receiveStorePostalCode.trim();
    }

    public BigDecimal getTotalExpectedQty() {
        return totalExpectedQty;
    }

    public void setTotalExpectedQty(BigDecimal totalExpectedQty) {
        this.totalExpectedQty = totalExpectedQty;
    }

    public BigDecimal getTotalReceivedQty() {
        return totalReceivedQty;
    }

    public void setTotalReceivedQty(BigDecimal totalReceivedQty) {
        this.totalReceivedQty = totalReceivedQty;
    }

    public BigDecimal getItemCount() {
        return itemCount;
    }

    public void setItemCount(BigDecimal itemCount) {
        this.itemCount = itemCount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetCost() {
        return netCost;
    }

    public void setNetCost(BigDecimal netCost) {
        this.netCost = netCost;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalRetailAmount() {
        return totalRetailAmount;
    }

    public void setTotalRetailAmount(BigDecimal totalRetailAmount) {
        this.totalRetailAmount = totalRetailAmount;
    }

    public String getGrnRemarks() {
        return grnRemarks;
    }

    public void setGrnRemarks(String grnRemarks) {
        this.grnRemarks = grnRemarks == null ? null : grnRemarks.trim();
    }
    
    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public GrnStatus getGrnStatus()
    {
        return grnStatus;
    }

    public void setGrnStatus(GrnStatus grnStatus)
    {
        this.grnStatus = grnStatus;
    }
    

    public Boolean getDispute()
    {
        return dispute;
    }

    public void setDispute(Boolean dispute)
    {
        this.dispute = dispute;
    }

    public DisputeStatus getDisputeStatus()
    {
        return disputeStatus;
    }

    public void setDisputeStatus(DisputeStatus disputeStatus)
    {
        this.disputeStatus = disputeStatus;
    }

    public Date getDisputeBuyerDate()
    {
        return disputeBuyerDate == null ? null : (Date)disputeBuyerDate.clone();
    }

    public void setDisputeBuyerDate(Date disputeBuyerDate)
    {
        this.disputeBuyerDate = disputeBuyerDate == null ? null : (Date)disputeBuyerDate.clone();
    }

    public String getDisputeBuyerRemarks()
    {
        return disputeBuyerRemarks;
    }

    public void setDisputeBuyerRemarks(String disputeBuyerRemarks)
    {
        this.disputeBuyerRemarks = disputeBuyerRemarks;
    }

    public Date getDisputeSupplierDate()
    {
        return disputeSupplierDate == null ? null : (Date)disputeSupplierDate.clone();
    }

    public void setDisputeSupplierDate(Date disputeSupplierDate)
    {
        this.disputeSupplierDate = disputeSupplierDate == null ? null : (Date)disputeSupplierDate.clone();
    }

    public String getDisputeSupplierRemarks()
    {
        return disputeSupplierRemarks;
    }

    public void setDisputeSupplierRemarks(String disputeSupplierRemarks)
    {
        this.disputeSupplierRemarks = disputeSupplierRemarks;
    }
    
    public String getDisputeBuyerBy()
    {
        return disputeBuyerBy;
    }

    public void setDisputeBuyerBy(String disputeBuyerBy)
    {
        this.disputeBuyerBy = disputeBuyerBy;
    }

    public String getDisputeSupplierBy()
    {
        return disputeSupplierBy;
    }

    public void setDisputeSupplierBy(String disputeSupplierBy)
    {
        this.disputeSupplierBy = disputeSupplierBy;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        DateUtil dataUtil = DateUtil.getInstance();
        
        buffer.append(grnNo).append(delimiterChar)
        .append(docAction).append(delimiterChar)
        .append(dataUtil.convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(dataUtil.convertDateToString(grnDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(poNo).append(delimiterChar)
        .append(dataUtil.convertDateToString(poDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(dataUtil.convertDateToString(createDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(buyerCode).append(delimiterChar)
        .append(buyerName).append(delimiterChar)
        .append(supplierCode).append(delimiterChar)
        .append(supplierName).append(delimiterChar)
        .append(receiveStoreCode).append(delimiterChar)
        .append(receiveStoreName).append(delimiterChar)
        .append(totalExpectedQty).append(delimiterChar)
        .append(totalReceivedQty).append(delimiterChar)
        .append(itemCount).append(delimiterChar)
        .append(discountAmount).append(delimiterChar)
        .append(netCost).append(delimiterChar)
        .append(totalCost).append(delimiterChar)
        .append(totalRetailAmount).append(delimiterChar)
        .append(grnRemarks);
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(grnOid);
    }
}