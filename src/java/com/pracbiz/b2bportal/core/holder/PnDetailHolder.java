package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class PnDetailHolder extends BaseHolder {
    /**
     * 
     */
    private static final long serialVersionUID = -5265624722013739714L;

    private BigDecimal pnOid;

    private Integer lineSeqNo;

    private String docType;

    private String docRefNo;

    private Date docDate;

    private String payTransNo;

    private String payRefNo;

    private BigDecimal grossAmount;

    private BigDecimal discountAmount;

    private BigDecimal netAmount;

    private String itemRemarks;

    public BigDecimal getPnOid() {
        return pnOid;
    }

    public void setPnOid(BigDecimal pnOid) {
        this.pnOid = pnOid;
    }

    public Integer getLineSeqNo() {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo) {
        this.lineSeqNo = lineSeqNo;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType == null ? null : docType.trim();
    }

    public String getDocRefNo() {
        return docRefNo;
    }

    public void setDocRefNo(String docRefNo) {
        this.docRefNo = docRefNo == null ? null : docRefNo.trim();
    }

    public Date getDocDate() {
        return docDate == null ? null : (Date)docDate.clone();
    }

    public void setDocDate(Date docDate) {
        this.docDate = docDate  == null ? null : (Date)docDate.clone();
    }

    public String getPayTransNo() {
        return payTransNo;
    }

    public void setPayTransNo(String payTransNo) {
        this.payTransNo = payTransNo == null ? null : payTransNo.trim();
    }

    public String getPayRefNo() {
        return payRefNo;
    }

    public void setPayRefNo(String payRefNo) {
        this.payRefNo = payRefNo == null ? null : payRefNo.trim();
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getItemRemarks() {
        return itemRemarks;
    }

    public void setItemRemarks(String itemRemarks) {
        this.itemRemarks = itemRemarks == null ? null : itemRemarks.trim();
    }
    
    public String toStringWithDelimiterCharacter(String delimiterChar) throws Exception
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(lineSeqNo).append(delimiterChar)
        .append(docType).append(delimiterChar)
        .append(docRefNo).append(delimiterChar)
        .append(DateUtil.getInstance().convertDateToString(docDate, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)).append(delimiterChar)
        .append(payTransNo).append(delimiterChar)
        .append(payRefNo).append(delimiterChar)
        .append(grossAmount).append(delimiterChar)
        .append(discountAmount).append(delimiterChar)
        .append(netAmount);
        return buffer.toString();
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(pnOid + "" + lineSeqNo);
    }
}