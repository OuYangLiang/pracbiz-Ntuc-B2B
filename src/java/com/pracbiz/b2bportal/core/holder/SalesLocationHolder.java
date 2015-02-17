package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import java.math.BigDecimal;

public class SalesLocationHolder extends BaseHolder
{

    private static final long serialVersionUID = 8968571281182940906L;

    private BigDecimal salesOid;
    private Integer lineSeqNo;
    private Integer detailLineSeqNo;
    private String locationCode;
    private String locationName;
    private String locationAddr1;
    private String locationAddr2;
    private String locationAddr3;
    private String locationAddr4;
    private String locationAddr5;
    private String locationCity;
    private String locationState;
    private String locationCtryCode;
    private String locationPostalCode;
    private String locationContactTel;
    private BigDecimal salesQty;

    public BigDecimal getSalesOid()
    {
        return salesOid;
    }

    public void setSalesOid(BigDecimal salesOid)
    {
        this.salesOid = salesOid;
    }

    public Integer getLineSeqNo()
    {
        return lineSeqNo;
    }

    public void setLineSeqNo(Integer lineSeqNo)
    {
        this.lineSeqNo = lineSeqNo;
    }

    public Integer getDetailLineSeqNo()
    {
        return detailLineSeqNo;
    }

    public void setDetailLineSeqNo(Integer detailLineSeqNo)
    {
        this.detailLineSeqNo = detailLineSeqNo;
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

    public String getLocationAddr1()
    {
        return locationAddr1;
    }

    public void setLocationAddr1(String locationAddr1)
    {
        this.locationAddr1 = locationAddr1;
    }

    public String getLocationAddr2()
    {
        return locationAddr2;
    }

    public void setLocationAddr2(String locationAddr2)
    {
        this.locationAddr2 = locationAddr2;
    }

    public String getLocationAddr3()
    {
        return locationAddr3;
    }

    public void setLocationAddr3(String locationAddr3)
    {
        this.locationAddr3 = locationAddr3;
    }

    public String getLocationAddr4()
    {
        return locationAddr4;
    }

    public void setLocationAddr4(String locationAddr4)
    {
        this.locationAddr4 = locationAddr4;
    }

    public String getLocationAddr5()
    {
        return locationAddr5;
    }

    public void setLocationAddr5(String locationAddr5)
    {
        this.locationAddr5 = locationAddr5;
    }

    public String getLocationCity()
    {
        return locationCity;
    }

    public void setLocationCity(String locationCity)
    {
        this.locationCity = locationCity;
    }

    public String getLocationState()
    {
        return locationState;
    }

    public void setLocationState(String locationState)
    {
        this.locationState = locationState;
    }

    public String getLocationCtryCode()
    {
        return locationCtryCode;
    }

    public void setLocationCtryCode(String locationCtryCode)
    {
        this.locationCtryCode = locationCtryCode;
    }

    public String getLocationPostalCode()
    {
        return locationPostalCode;
    }

    public void setLocationPostalCode(String locationPostalCode)
    {
        this.locationPostalCode = locationPostalCode;
    }

    public String getLocationContactTel()
    {
        return locationContactTel;
    }

    public void setLocationContactTel(String locationContactTel)
    {
        this.locationContactTel = locationContactTel;
    }

    public BigDecimal getSalesQty()
    {
        return salesQty;
    }

    public void setSalesQty(BigDecimal salesQty)
    {
        this.salesQty = salesQty;
    }

    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(salesOid + "" + lineSeqNo);
    }
}