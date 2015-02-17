//*****************************************************************************
//
// File Name       :  MissingGiReportParameter.java
// Date Created    :  Feb 25, 2014
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Feb 25, 2014 9:26:58 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report.excel;


import java.math.BigDecimal;
import java.util.Date;


/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class MissingGiReportParameter
{
    private BigDecimal rtvOid;

    private String rtvNo;

    private Date rtvDate;

    private Date collectionDate;

    private String supplierCode;

    private String supplierName;

    private BigDecimal totalCost;

    private String reasonDesc;

    private String rtvRemarks;

    private String locationCode;
    
    private String locationName;


    public BigDecimal getRtvOid()
    {
        return rtvOid;
    }


    public void setRtvOid(BigDecimal rtvOid)
    {
        this.rtvOid = rtvOid;
    }


    public String getRtvNo()
    {
        return rtvNo;
    }


    public void setRtvNo(String rtvNo)
    {
        this.rtvNo = rtvNo;
    }


    public Date getRtvDate()
    {
        return rtvDate == null ? null : (Date)rtvDate.clone();
    }


    public void setRtvDate(Date rtvDate)
    {
        this.rtvDate = rtvDate  == null ? null : (Date)rtvDate.clone();
    }


    public Date getCollectionDate()
    {
        return collectionDate  == null ? null : (Date)collectionDate.clone();
    }


    public void setCollectionDate(Date collectionDate)
    {
        this.collectionDate = collectionDate == null ? null : (Date)collectionDate.clone();
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


    public BigDecimal getTotalCost()
    {
        return totalCost;
    }


    public void setTotalCost(BigDecimal totalCost)
    {
        this.totalCost = totalCost;
    }


    public String getReasonDesc()
    {
        return reasonDesc;
    }


    public void setReasonDesc(String reasonDesc)
    {
        this.reasonDesc = reasonDesc;
    }


    public String getRtvRemarks()
    {
        return rtvRemarks;
    }


    public void setRtvRemarks(String rtvRemarks)
    {
        this.rtvRemarks = rtvRemarks;
    }


    public String getLocationCode()
    {
        return locationCode;
    }


    public void setLocationCode(String locationCode)
    {
        this.locationCode = locationCode;
    }


    public String getLocationName()
    {
        return locationName;
    }


    public void setLocationName(String locationName)
    {
        this.locationName = locationName;
    }
    
}
