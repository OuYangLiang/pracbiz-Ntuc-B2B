package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class TaskListHolder extends BaseHolder
{

    private static final long serialVersionUID = 1L;
    private int matchingCloseCount;
    private int matchingAuditPriceCount;
    private int matchingAuditQtyCount;
    private int matchingApproveInvCount;
    private int matchingSuppCount;
    
    private int dnSuppCount;
    private int dnPriceCount;
    private int dnQtyCount;
    private int dnCloseCount;
    
    
    public int getMatchingCloseCount()
    {
        return matchingCloseCount;
    }


    public void setMatchingCloseCount(int matchingCloseCount)
    {
        this.matchingCloseCount = matchingCloseCount;
    }

    public int getMatchingAuditPriceCount()
    {
        return matchingAuditPriceCount;
    }


    public void setMatchingAuditPriceCount(int matchingAuditPriceCount)
    {
        this.matchingAuditPriceCount = matchingAuditPriceCount;
    }


    public int getMatchingAuditQtyCount()
    {
        return matchingAuditQtyCount;
    }


    public void setMatchingAuditQtyCount(int matchingAuditQtyCount)
    {
        this.matchingAuditQtyCount = matchingAuditQtyCount;
    }


    public int getMatchingApproveInvCount()
    {
        return matchingApproveInvCount;
    }


    public void setMatchingApproveInvCount(int matchingApproveInvCount)
    {
        this.matchingApproveInvCount = matchingApproveInvCount;
    }


    public int getMatchingSuppCount()
    {
        return matchingSuppCount;
    }


    public void setMatchingSuppCount(int matchingSuppCount)
    {
        this.matchingSuppCount = matchingSuppCount;
    }


    public int getDnSuppCount()
    {
        return dnSuppCount;
    }


    public void setDnSuppCount(int dnSuppCount)
    {
        this.dnSuppCount = dnSuppCount;
    }


    public int getDnPriceCount()
    {
        return dnPriceCount;
    }


    public void setDnPriceCount(int dnPriceCount)
    {
        this.dnPriceCount = dnPriceCount;
    }


    public int getDnQtyCount()
    {
        return dnQtyCount;
    }


    public void setDnQtyCount(int dnQtyCount)
    {
        this.dnQtyCount = dnQtyCount;
    }


    public int getDnCloseCount()
    {
        return dnCloseCount;
    }


    public void setDnCloseCount(int dnCloseCount)
    {
        this.dnCloseCount = dnCloseCount;
    }


    @Override
    public String getCustomIdentification()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
}
