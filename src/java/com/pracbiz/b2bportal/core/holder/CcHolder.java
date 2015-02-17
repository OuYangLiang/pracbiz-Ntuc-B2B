//*****************************************************************************
//
// File Name       :  CcHolder.java
// Date Created    :  Dec 23, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 23, 2013 5:53:53 PM $
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
public class CcHolder extends BaseHolder
{
    private static final long serialVersionUID = -8224705756884505736L;
    private CcHeaderHolder ccHeader;
    private List<CcDetailHolder> details;
    private List<CcHeaderExtendedHolder> headerExtendeds;
    private List<CcDetailExtendedHolder> detailExtendeds;


    public CcHeaderHolder getCcHeader()
    {
        return ccHeader;
    }


    public void setCcHeader(CcHeaderHolder ccHeader)
    {
        this.ccHeader = ccHeader;
    }


    public List<CcDetailHolder> getDetails()
    {
        return details;
    }


    public void setDetails(List<CcDetailHolder> details)
    {
        this.details = details;
    }


    public List<CcHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }


    public void setHeaderExtendeds(List<CcHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }


    public List<CcDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }


    public void setDetailExtendeds(List<CcDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }


    @Override
    public String getCustomIdentification()
    {
        return ccHeader.getCustomIdentification();
    }
    
}
