package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.holder.BuyerHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class BuyerExHolder extends BuyerHolder
{
    private static final long serialVersionUID = 7689589641246301226L;
    private String ctryDesc;
    private String currDesc;
    private String gstPercentStr;
    private BigDecimal currentUserBuyerOid;

    
    public String getCtryDesc()
    {
        return ctryDesc;
    }

    public void setCtryDesc(String ctryDesc)
    {
        this.ctryDesc = ctryDesc;
    }

    public String getCurrDesc()
    {
        return currDesc;
    }

    public void setCurrDesc(String currDesc)
    {
        this.currDesc = currDesc;
    }

    public String getGstPercentStr()
    {
        return gstPercentStr;
    }

    public void setGstPercentStr(String gstPercentStr)
    {
        this.gstPercentStr = gstPercentStr;
    }

    public BigDecimal getCurrentUserBuyerOid()
    {
        return currentUserBuyerOid;
    }

    public void setCurrentUserBuyerOid(BigDecimal currentUserBuyerOid)
    {
        this.currentUserBuyerOid = currentUserBuyerOid;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((getBuyerOid() == null) ? 0 : getBuyerOid().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        BuyerExHolder other = (BuyerExHolder)obj;
        if(getBuyerOid() == null)
        {
            if(other.getBuyerOid() != null)
                return false;
        }
        else if(!getBuyerOid().equals(other.getBuyerOid()))
            return false;
        return true;
    }
}
