package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PnHeaderHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = 7110232153130805955L;

    private BigDecimal pnOid;

    private String pnNo;

    private String docAction;

    private Date actionDate;

    private Date pnDate;

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

    private String payMethodCode;

    private String payMethodDesc;

    private String bankCode;

    private BigDecimal totalAmount;

    private BigDecimal discountAmount;

    private BigDecimal netTotalAmount;

    private String pnRemarks;
    
    private Boolean duplicate;
    
    private PnStatus pnStatus;
    
    public String computePdfFilename()
    {
        return MsgType.PN.name() + DOC_FILENAME_DELIMITOR
            + this.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.getSupplierCode() + DOC_FILENAME_DELIMITOR 
            + StringUtil.getInstance().convertDocNo(this.getPnNo())
            + DOC_FILENAME_DELIMITOR + this.getPnOid() + ".pdf";
    }

    public BigDecimal getPnOid() {
        return pnOid;
    }

    public void setPnOid(BigDecimal pnOid) {
        this.pnOid = pnOid;
    }

    public String getPnNo() {
        return pnNo;
    }

    public void setPnNo(String pnNo) {
        this.pnNo = pnNo == null ? null : pnNo.trim();
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

    public Date getPnDate() {
        return pnDate == null ? null : (Date)pnDate.clone();
    }

    public void setPnDate(Date pnDate) {
        this.pnDate = pnDate == null ? null : (Date)pnDate.clone();
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

    public String getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(String payMethodCode) {
        this.payMethodCode = payMethodCode == null ? null : payMethodCode.trim();
    }

    public String getPayMethodDesc() {
        return payMethodDesc;
    }

    public void setPayMethodDesc(String payMethodDesc) {
        this.payMethodDesc = payMethodDesc == null ? null : payMethodDesc.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetTotalAmount() {
        return netTotalAmount;
    }

    public void setNetTotalAmount(BigDecimal netTotalAmount) {
        this.netTotalAmount = netTotalAmount;
    }

    public String getPnRemarks() {
        return pnRemarks;
    }

    public void setPnRemarks(String pnRemarks) {
        this.pnRemarks = pnRemarks == null ? null : pnRemarks.trim();
    }
    
    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public PnStatus getPnStatus()
    {
        return pnStatus;
    }

    public void setPnStatus(PnStatus pnStatus)
    {
        this.pnStatus = pnStatus;
    }

    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(pnNo).append(delimiterChar)
        .append(docAction).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(actionDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(pnDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(buyerCode).append(delimiterChar)
        .append(buyerName).append(delimiterChar)
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
        .append(payMethodCode).append(delimiterChar)
        .append(payMethodDesc).append(delimiterChar)
        .append(bankCode).append(delimiterChar)
        .append(totalAmount).append(delimiterChar)
        .append(discountAmount).append(delimiterChar)
        .append(netTotalAmount);
        
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(pnOid);
    }
}