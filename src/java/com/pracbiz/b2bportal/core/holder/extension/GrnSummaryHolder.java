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


import com.pracbiz.b2bportal.core.constants.DisputeStatus;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class GrnSummaryHolder extends MsgTransactionsExHolder
{

    private static final long serialVersionUID = 2109693614996363027L;

    private Date grnDate;
    private String grnNo;
    private String invNo;
    private String poNo;
    private Date grnDateFrom;
    private Date grnDateTo;
    private BigDecimal grnOid;
    private Date poDateFrom;
    private Date poDateTo;
    private Date poDate;
    private GrnStatus grnStatus;
    private Boolean duplicate;
    private String storeCode;
    private Boolean dispute;
    private DisputeStatus disputeStatus;
    private String receiveStoreCode;
    private Integer detailCount;


    public Date getPoDateFrom()
    {
        return poDateFrom == null ? null : (Date)poDateFrom.clone();
    }


    public void setPoDateFrom(Date poDateFrom)
    {
        this.poDateFrom = poDateFrom == null ? null : (Date)poDateFrom.clone();
    }


    public Date getPoDateTo()
    {
        return poDateTo == null ? null : (Date)poDateTo.clone();
    }


    public void setPoDateTo(Date poDateTo)
    {
        this.poDateTo = poDateTo == null ? null : (Date)poDateTo.clone();
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getPoDate()
    {
        return poDate == null ? null : (Date)poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date)poDate.clone();
    }


    public BigDecimal getGrnOid()
    {
        return grnOid;
    }


    public void setGrnOid(BigDecimal grnOid)
    {
        this.grnOid = grnOid;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getGrnDate()
    {
        return grnDate == null ? null : (Date)grnDate.clone();
    }


    public void setGrnDate(Date grnDate)
    {
        this.grnDate = grnDate == null ? null : (Date)grnDate.clone();
    }


    public String getGrnNo()
    {
        return grnNo;
    }


    public void setGrnNo(String grnNo)
    {
        this.grnNo = grnNo;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    public Date getGrnDateFrom()
    {
        return grnDateFrom == null ? null : (Date)grnDateFrom.clone();
    }


    public void setGrnDateFrom(Date grnDateFrom)
    {
        this.grnDateFrom = grnDateFrom == null ? null : (Date)grnDateFrom
            .clone();
    }


    public Date getGrnDateTo()
    {
        return grnDateTo == null ? null : (Date)grnDateTo.clone();
    }


    public void setGrnDateTo(Date grnDateTo)
    {
        this.grnDateTo = grnDateTo == null ? null : (Date)grnDateTo.clone();
    }


    public GrnStatus getGrnStatus()
    {
        return grnStatus;
    }


    public void setGrnStatus(GrnStatus grnStatus)
    {
        this.grnStatus = grnStatus;
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


    public DisputeStatus getDisputeStatus()
    {
        return disputeStatus;
    }


    public void setDisputeStatus(DisputeStatus disputeStatus)
    {
        this.disputeStatus = disputeStatus;
    }


    public String getReceiveStoreCode()
    {
        return receiveStoreCode;
    }


    public void setReceiveStoreCode(String receiveStoreCode)
    {
        this.receiveStoreCode = receiveStoreCode;
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
