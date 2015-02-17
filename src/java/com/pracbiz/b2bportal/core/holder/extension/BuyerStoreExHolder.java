package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreExHolder extends BuyerStoreHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 6667586775654374348L;
    private String storeCtryName;
    private String areaCode;
    private String areaName;
    private List<String> buyerCodeList;
    private String buyerName;
    private List<String> buyerStoreAccessList;
    private BigDecimal currentUserType;


    public String getAreaName()
    {
        return areaName;
    }


    public void setAreaName(String areaName)
    {
        this.areaName = areaName;
    }


    public String getStoreCtryName()
    {
        return storeCtryName;
    }


    public void setStoreCtryName(String storeCtryName)
    {
        this.storeCtryName = storeCtryName;
    }


    public String getAreaCode()
    {
        return areaCode;
    }


    public void setAreaCode(String areaCode)
    {
        this.areaCode = areaCode;
    }


    public List<String> getBuyerCodeList()
    {
        return buyerCodeList;
    }


    public void setBuyerCodeList(List<String> buyerCodeList)
    {
        this.buyerCodeList = buyerCodeList;
    }
    
    
    public void addBuyerCode(String buyerCode)
    {
        if (this.buyerCodeList == null)
        {
            this.buyerCodeList = new ArrayList<String>();
        }
        this.buyerCodeList.add(buyerCode);
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public List<String> getBuyerStoreAccessList()
    {
        return buyerStoreAccessList;
    }


    public void setBuyerStoreAccessList(List<String> buyerStoreAccessList)
    {
        this.buyerStoreAccessList = buyerStoreAccessList;
    }


    public BigDecimal getCurrentUserType()
    {
        return currentUserType;
    }


    public void setCurrentUserType(BigDecimal currentUserType)
    {
        this.currentUserType = currentUserType;
    }
    
}
