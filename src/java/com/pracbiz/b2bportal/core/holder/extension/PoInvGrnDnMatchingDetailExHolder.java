package com.pracbiz.b2bportal.core.holder.extension;

import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;

public class PoInvGrnDnMatchingDetailExHolder extends
    PoInvGrnDnMatchingDetailHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = -6680055366469122429L;
    
    private String priceStatusValue;
    private String qtyStatusValue;
    private Boolean priceMatched;
    private Boolean qtyMatched;
    private Boolean privileged;
    private PoInvGrnDnMatchingDetailExHolder oldDetail;
    private String classCode;
    private String subclassCode;
    private String priceStatusActionByName;
    private String qtyStatusActionByName;
    private Boolean isJustShow;
    
    public String getPriceStatusValue()
    {
        return priceStatusValue;
    }
    
    public void setPriceStatusValue(String priceStatusValue)
    {
        this.priceStatusValue = priceStatusValue;
    }
    
    public String getQtyStatusValue()
    {
        return qtyStatusValue;
    }
    
    public void setQtyStatusValue(String qtyStatusValue)
    {
        this.qtyStatusValue = qtyStatusValue;
    }

    public Boolean getPriceMatched()
    {
        return priceMatched;
    }

    public void setPriceMatched(Boolean priceMatched)
    {
        this.priceMatched = priceMatched;
    }

    public Boolean getQtyMatched()
    {
        return qtyMatched;
    }

    public void setQtyMatched(Boolean qtyMatched)
    {
        this.qtyMatched = qtyMatched;
    }

    public Boolean getPrivileged()
    {
        return privileged;
    }

    public void setPrivileged(Boolean privileged)
    {
        this.privileged = privileged;
    }

    public PoInvGrnDnMatchingDetailExHolder getOldDetail()
    {
        return oldDetail;
    }

    public void setOldDetail(PoInvGrnDnMatchingDetailExHolder oldDetail)
    {
        this.oldDetail = oldDetail;
    }

    public String getClassCode()
    {
        return classCode;
    }

    public void setClassCode(String classCode)
    {
        this.classCode = classCode;
    }

    public String getSubclassCode()
    {
        return subclassCode;
    }

    public void setSubclassCode(String subclassCode)
    {
        this.subclassCode = subclassCode;
    }

    public String getPriceStatusActionByName()
    {
        return priceStatusActionByName;
    }

    public void setPriceStatusActionByName(String priceStatusActionByName)
    {
        this.priceStatusActionByName = priceStatusActionByName;
    }

    public String getQtyStatusActionByName()
    {
        return qtyStatusActionByName;
    }

    public void setQtyStatusActionByName(String qtyStatusActionByName)
    {
        this.qtyStatusActionByName = qtyStatusActionByName;
    }

    public Boolean getIsJustShow()
    {
        return isJustShow;
    }

    public void setIsJustShow(Boolean isJustShow)
    {
        this.isJustShow = isJustShow;
    }
    
}
