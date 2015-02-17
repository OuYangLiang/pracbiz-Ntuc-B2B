//*****************************************************************************
//
// File Name       :  GiHolder.java
// Date Created    :  Nov 12, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 12, 2013 5:53:33 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder;

import java.util.List;


import com.pracbiz.b2bportal.base.holder.BaseHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class GiHolder extends BaseHolder
{
    private static final long serialVersionUID = 3324569535529584881L;
    private GiHeaderHolder giHeader;
    private List<GiHeaderExtendedHolder> headerExtended;
    private List<GiDetailHolder> details;
    private List<GiDetailExtendedHolder> detailExtended;
    
    
    public GiHeaderHolder getGiHeader()
    {
        return giHeader;
    }
    
    
    public void setGiHeader(GiHeaderHolder giHeader)
    {
        this.giHeader = giHeader;
    }
    
    
    public List<GiHeaderExtendedHolder> getHeaderExtended()
    {
        return headerExtended;
    }
    
    
    public void setHeaderExtended(List<GiHeaderExtendedHolder> headerExtended)
    {
        this.headerExtended = headerExtended;
    }
    
    
    public List<GiDetailHolder> getDetails()
    {
        return details;
    }
    
    
    public void setDetails(List<GiDetailHolder> details)
    {
        this.details = details;
    }


    public List<GiDetailExtendedHolder> getDetailExtended()
    {
        return detailExtended;
    }


    public void setDetailExtended(List<GiDetailExtendedHolder> detailExtended)
    {
        this.detailExtended = detailExtended;
    }


    @Override
    public String getCustomIdentification()
    {
        return giHeader.getCustomIdentification();
    }
    
}
