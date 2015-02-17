package com.pracbiz.b2bportal.core.holder;

import java.util.List;


import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class RtvHolder extends BaseHolder
{
    private static final long serialVersionUID = -7322379647458380099L;
    private RtvHeaderHolder rtvHeader;
    private List<RtvHeaderExtendedHolder> headerExtended;
    private List<RtvDetailHolder> rtvDetail;
    private List<RtvDetailExtendedHolder> detailExtended;
    private List<RtvLocationHolder> locations;
    private List<RtvLocationDetailHolder> locationDetails;
    private List<RtvLocationDetailExtendedHolder> rtvLocDetailExtendeds;
    
    public RtvHeaderHolder getRtvHeader()
    {
        return rtvHeader;
    }

    
    public void setRtvHeader(RtvHeaderHolder rtvHeader)
    {
        this.rtvHeader = rtvHeader;
    }

    
    public List<RtvHeaderExtendedHolder> getHeaderExtended()
    {
        return headerExtended;
    }

    
    public void setHeaderExtended(List<RtvHeaderExtendedHolder> headerExtended)
    {
        this.headerExtended = headerExtended;
    }

    
    public List<RtvDetailHolder> getRtvDetail()
    {
        return rtvDetail;
    }


    public void setRtvDetail(List<RtvDetailHolder> rtvDetail)
    {
        this.rtvDetail = rtvDetail;
    }


    public List<RtvDetailExtendedHolder> getDetailExtended()
    {
        return detailExtended;
    }

    
    public void setDetailExtended(
        List<RtvDetailExtendedHolder> detailExtended)
    {
        this.detailExtended = detailExtended;
    }


    public List<RtvLocationHolder> getLocations()
    {
        return locations;
    }


    public void setLocations(List<RtvLocationHolder> locations)
    {
        this.locations = locations;
    }


    public List<RtvLocationDetailHolder> getLocationDetails()
    {
        return locationDetails;
    }


    public void setLocationDetails(List<RtvLocationDetailHolder> locationDetails)
    {
        this.locationDetails = locationDetails;
    }


    public List<RtvLocationDetailExtendedHolder> getRtvLocDetailExtendeds()
    {
        return rtvLocDetailExtendeds;
    }


    public void setRtvLocDetailExtendeds(
        List<RtvLocationDetailExtendedHolder> rtvLocDetailExtendeds)
    {
        this.rtvLocDetailExtendeds = rtvLocDetailExtendeds;
    }


    @Override
    public String getCustomIdentification()
    {
        return rtvHeader.getCustomIdentification();
    }

}
