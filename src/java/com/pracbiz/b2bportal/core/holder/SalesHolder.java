package com.pracbiz.b2bportal.core.holder;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class SalesHolder extends BaseHolder
{
    private static final long serialVersionUID = 3776498539557452138L;

    private SalesHeaderHolder salesHeader;
    private List<SalesHeaderExtendedHolder> headerExtendeds;
    private List<SalesDetailHolder> details;
    private List<SalesDetailExtendedHolder> detailExtendeds;
    private List<SalesLocationHolder> locations;
    private List<SalesDateHolder> salesDates;
    private List<SalesDateLocationDetailHolder> salesDateLocationDetail;
    private List<SalesDateLocationDetailExtendedHolder> salesDateLocationDetailExtendeds;

    //*****************************************************
    // setter and getter methods
    //*****************************************************

    public SalesHeaderHolder getSalesHeader()
    {
        return salesHeader;
    }

    public void setSalesHeader(SalesHeaderHolder salesHeader)
    {
        this.salesHeader = salesHeader;
    }

    public List<SalesHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }

    public void setHeaderExtendeds(
        List<SalesHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }

    public List<SalesDetailHolder> getDetails()
    {
        return details;
    }

    public void setDetails(List<SalesDetailHolder> details)
    {
        this.details = details;
    }

    public List<SalesDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }

    public void setDetailExtendeds(
        List<SalesDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }

    public List<SalesLocationHolder> getLocations()
    {
        return locations;
    }

    public void setLocations(List<SalesLocationHolder> locations)
    {
        this.locations = locations;
    }

    public List<SalesDateHolder> getSalesDates()
    {
        return salesDates;
    }

    public void setSalesDates(List<SalesDateHolder> salesDates)
    {
        this.salesDates = salesDates;
    }

    public List<SalesDateLocationDetailHolder> getSalesDateLocationDetail()
    {
        return salesDateLocationDetail;
    }

    public void setSalesDateLocationDetail(
        List<SalesDateLocationDetailHolder> salesDateLocationDetail)
    {
        this.salesDateLocationDetail = salesDateLocationDetail;
    }

    public List<SalesDateLocationDetailExtendedHolder> getSalesDateLocationDetailExtendeds()
    {
        return salesDateLocationDetailExtendeds;
    }

    public void setSalesDateLocationDetailExtendeds(
        List<SalesDateLocationDetailExtendedHolder> salesDateLocationDetailExtendeds)
    {
        this.salesDateLocationDetailExtendeds = salesDateLocationDetailExtendeds;
    }

    @Override
    public String getCustomIdentification()
    {
        return salesHeader == null ? null : salesHeader
            .getCustomIdentification();
    }

    @Override
    public String getLogicalKey()
    {
        return salesHeader.getLogicalKey();
    }
}
