package com.pracbiz.b2bportal.core.holder;

import java.util.List;


import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class PnHolder extends BaseHolder
{
    private static final long serialVersionUID = 5608884545627271529L;
    private PnHeaderHolder pnHeader;
    private List<PnHeaderExtendedHolder> headerExtendeds;
    private List<PnDetailHolder> details;
    private List<PnDetailExtendedHolder> detailExtendeds;

    
    public PnHeaderHolder getPnHeader()
    {
        return pnHeader;
    }

    
    public void setPnHeader(PnHeaderHolder pnHeader)
    {
        this.pnHeader = pnHeader;
    }
    
    
    public List<PnHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }


    public void setHeaderExtendeds(List<PnHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }


    public List<PnDetailHolder> getDetails()
    {
        return details;
    }

    
    public void setDetails(List<PnDetailHolder> details)
    {
        this.details = details;
    }

    
    public List<PnDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }

    
    public void setDetailExtendeds(List<PnDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }


    @Override
    public String getCustomIdentification()
    {
        return pnHeader.getCustomIdentification();
    }

    
}
