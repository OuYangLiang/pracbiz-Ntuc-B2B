//*****************************************************************************
//
// File Name       :  RtvSummaryHolder.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-13 $
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

import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class RtvSummaryHolder extends MsgTransactionsExHolder
{
    private static final long serialVersionUID = -609115272648516281L;

    private Date rtvDate;
    private Date rtvDateFrom;
    private Date rtvDateTo;
    private String reasonDesc;
    private String rtvNo;
    private String doNo;
    private String invNo;
    private Date invDate;
    private Date invDateFrom;
    private Date invDateTo;
    private BigDecimal rtvOid;
    private RtvStatus rtvStatus;
    private Boolean duplicate;
    private String storeCode;
    private Integer detailCount;

    public BigDecimal getRtvOid()
    {
        return rtvOid;
    }

    public void setRtvOid(BigDecimal rtvOid)
    {
        this.rtvOid = rtvOid;
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
        return invDate == null ? null : (Date)invDate.clone();
    }

    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date)invDate.clone();
    }

    public Date getInvDateFrom()
    {
        return invDateFrom == null ? null : (Date)invDateFrom.clone();
    }

    public void setInvDateFrom(Date invDateFrom)
    {
        this.invDateFrom = invDateFrom == null ? null : (Date)invDateFrom.clone();
    }

    public Date getInvDateTo()
    {
        return invDateTo == null ? null : (Date)invDateTo.clone();
    }

    public void setInvDateTo(Date invDateTo)
    {
        this.invDateTo = invDateTo == null ? null : (Date)invDateTo.clone();
    }

    public String getDoNo()
    {
        return doNo;
    }

    public void setDoNo(String doNo)
    {
        this.doNo = doNo;
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date)rtvDate.clone();
    }

    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date)rtvDate.clone();
    }

    public Date getRtvDateFrom()
    {
        return rtvDateFrom == null ? null : (Date)rtvDateFrom.clone();
    }

    public void setRtvDateFrom(Date rtvDateFrom)
    {
        this.rtvDateFrom = rtvDateFrom == null ? null : (Date)rtvDateFrom.clone();
    }

    public Date getRtvDateTo()
    {
        return rtvDateTo == null ? null : (Date)rtvDateTo.clone();
    }

    public void setRtvDateTo(Date rtvDateTo)
    {
        this.rtvDateTo = rtvDateTo == null ? null : (Date)rtvDateTo.clone();
    }

    public String getReasonDesc()
    {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc;
    }

    public String getRtvNo()
    {
        return rtvNo;
    }

    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }

    public RtvStatus getRtvStatus()
    {
        return rtvStatus;
    }

    public void setRtvStatus(RtvStatus rtvStatus)
    {
        this.rtvStatus = rtvStatus;
    }

    public Boolean getDuplicate()
    {
        return duplicate;
    }

    public void setDuplicate(Boolean duplicate)
    {
        this.duplicate = duplicate;
    }

    public String getStoreCode()
    {
        return storeCode;
    }

    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
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
