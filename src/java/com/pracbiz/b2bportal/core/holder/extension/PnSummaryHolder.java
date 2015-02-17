//*****************************************************************************
//
// File Name       :  PnSummaryHolder.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class PnSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = 6190704434882683345L;

    private Date pnDate;
    private String bankCode;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String payMethodDesc;
    private String pnRemarks;
    private BigDecimal netTotalAmount;
    private String pnNo;
    private Date pnDateFrom;
    private Date pnDateTo;
    private BigDecimal pnOid;
    private PnStatus pnStatus;
    private Boolean duplicate;
    private Integer detailCount;

    public BigDecimal getPnOid()
    {
        return pnOid;
    }

    public void setPnOid(BigDecimal pnOid)
    {
        this.pnOid = pnOid;
    }

    public Date getPnDateFrom()
    {
        return pnDateFrom == null ? null : (Date)pnDateFrom.clone();
    }

    public void setPnDateFrom(Date pnDateFrom)
    {
        this.pnDateFrom = pnDateFrom == null ? null : (Date)pnDateFrom.clone();
    }

    public Date getPnDateTo()
    {
        return pnDateTo == null ? null : (Date)pnDateTo.clone();
    }

    public void setPnDateTo(Date pnDateTo)
    {
        this.pnDateTo = pnDateTo == null ? null : (Date)pnDateTo.clone();
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPnDate()
    {
        return pnDate == null ? null : (Date)pnDate.clone();
    }

    public void setPnDate(Date pnDate)
    {
        this.pnDate = pnDate == null ? null : (Date)pnDate.clone();
    }

    public String getBankCode()
    {
        return bankCode;
    }

    public void setBankCode(String bankCode)
    {
        this.bankCode = bankCode;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount()
    {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount)
    {
        this.discountAmount = discountAmount;
    }

    public String getPayMethodDesc()
    {
        return payMethodDesc;
    }

    public void setPayMethodDesc(String payMethodDesc)
    {
        this.payMethodDesc = payMethodDesc;
    }

    public String getPnRemarks()
    {
        return pnRemarks;
    }

    public void setPnRemarks(String pnRemarks)
    {
        this.pnRemarks = pnRemarks;
    }

    public BigDecimal getNetTotalAmount()
    {
        return netTotalAmount;
    }

    public void setNetTotalAmount(BigDecimal netTotalAmount)
    {
        this.netTotalAmount = netTotalAmount;
    }

    public String getPnNo()
    {
        return pnNo;
    }

    public void setPnNo(String pnNo)
    {
        this.pnNo = pnNo;
    }

    public PnStatus getPnStatus()
    {
        return pnStatus;
    }

    public void setPnStatus(PnStatus pnStatus)
    {
        this.pnStatus = pnStatus;
    }

    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public Integer getDetailCount()
    {
        return detailCount;
    }

    public void setDetailCount(Integer detailCount)
    {
        this.detailCount = detailCount;
    }
    
}
