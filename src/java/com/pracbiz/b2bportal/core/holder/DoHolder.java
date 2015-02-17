package com.pracbiz.b2bportal.core.holder;

import java.util.List;

public class DoHolder
{
    private DoHeaderHolder doHeader;
    private List<DoHeaderExtendedHolder> headerExtended;
    private List<DoDetailHolder> details;
    private List<DoDetailExtendedHolder> detailExtendeds;

    public DoHeaderHolder getDoHeader()
    {
        return doHeader;
    }

    
    public void setDoHeader(DoHeaderHolder doHeader)
    {
        this.doHeader = doHeader;
    }

    
    public List<DoHeaderExtendedHolder> getHeaderExtended()
    {
        return headerExtended;
    }

    
    public void setHeaderExtended(List<DoHeaderExtendedHolder> headerExtended)
    {
        this.headerExtended = headerExtended;
    }

    
    public List<DoDetailHolder> getDetails()
    {
        return details;
    }

    
    public void setDetails(List<DoDetailHolder> details)
    {
        this.details = details;
    }


    public List<DoDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }


    public void setDetailExtendeds(List<DoDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }


}
