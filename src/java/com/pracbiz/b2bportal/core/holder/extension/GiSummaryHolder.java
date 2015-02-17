//*****************************************************************************
//
// File Name       :  GrnSummaryHolder.java
// Date Created    :  2012-12-12
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-12 $
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

import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GiSummaryHolder extends MsgTransactionsExHolder
{

    private static final long serialVersionUID = 2109693614996363027L;

    private Date giDate;
    private String giNo;
    private String invNo;
    private String rtvNo;
    private Date giDateFrom;
    private Date giDateTo;
    private BigDecimal giOid;
    private Date rtvDateFrom;
    private Date rtvDateTo;
    private Date rtvDate;
    private GrnStatus giStatus;
    private Boolean duplicate;
    private String storeCode;
    private Boolean dispute;
    private Integer detailCount;
    
    
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT) 
    public Date getGiDate()
    {
        return giDate == null ? null : (Date)giDate.clone();
    }


    public void setGiDate(Date giDate)
    {
        this.giDate = giDate == null ? null : (Date)giDate.clone();
    }


    public String getGiNo()
    {
        return giNo;
    }


    public void setGiNo(String giNo)
    {
        this.giNo = giNo;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getGiDateFrom()
    {
        return giDateFrom == null ? null : (Date)giDateFrom.clone();
    }


    public void setGiDateFrom(Date giDateFrom)
    {
        this.giDateFrom = giDateFrom == null ? null : (Date)giDateFrom.clone();
    }


    public Date getGiDateTo()
    {
        return giDateTo == null ? null : (Date)giDateTo.clone();
    }


    public void setGiDateTo(Date giDateTo)
    {
        this.giDateTo = giDateTo  == null ? null : (Date)giDateTo.clone();
    }


    public BigDecimal getGiOid()
    {
        return giOid;
    }


    public void setGiOid(BigDecimal giOid)
    {
        this.giOid = giOid;
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

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT) 
    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date)rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate == null ? null : (Date)rtvDate.clone();
    }


    public GrnStatus getGiStatus()
    {
        return giStatus;
    }


    public void setGiStatus(GrnStatus giStatus)
    {
        this.giStatus = giStatus;
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


    public Boolean getDispute()
    {
        return dispute;
    }


    public void setDispute(Boolean dispute)
    {
        this.dispute = dispute;
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
