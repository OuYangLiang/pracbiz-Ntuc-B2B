package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class BuyerStoreHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 2988589517746616188L;

    private BigDecimal storeOid;

    private String buyerCode;

    private String storeCode;

    private String storeName;

    private String storeAddr1;

    private String storeAddr2;

    private String storeAddr3;

    private String storeAddr4;
    
    private String storeAddr5;

    private String storeCity;

    private String storeState;

    private String storePostalCode;

    private String storeCtryCode;
    
    private String contactPerson;
    
    private String contactTel;
    
    private String contactEmail;
    
    private Date createDate;
    
    private Date updateDate;

    private BigDecimal areaOid;
    
    private String action;
    
    private Boolean isWareHouse;


    public BigDecimal getStoreOid()
    {
        return storeOid;
    }


    public void setStoreOid(BigDecimal storeOid)
    {
        this.storeOid = storeOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getStoreCode()
    {
        return storeCode;
    }


    public void setStoreCode(String storeCode)
    {
        this.storeCode = storeCode;
    }


    public String getStoreName()
    {
        return storeName;
    }


    public void setStoreName(String storeName)
    {
        this.storeName = storeName == null ? null : storeName.trim();
    }


    public String getStoreAddr1()
    {
        return storeAddr1;
    }


    public void setStoreAddr1(String storeAddr1)
    {
        this.storeAddr1 = storeAddr1 == null ? null : storeAddr1.trim();
    }


    public String getStoreAddr2()
    {
        return storeAddr2;
    }


    public void setStoreAddr2(String storeAddr2)
    {
        this.storeAddr2 = storeAddr2 == null ? null : storeAddr2.trim();
    }


    public String getStoreAddr3()
    {
        return storeAddr3;
    }


    public void setStoreAddr3(String storeAddr3)
    {
        this.storeAddr3 = storeAddr3 == null ? null : storeAddr3.trim();
    }


    public String getStoreAddr4()
    {
        return storeAddr4;
    }


    public void setStoreAddr4(String storeAddr4)
    {
        this.storeAddr4 = storeAddr4 == null ? null : storeAddr4.trim();
    }
    
    
    public String getStoreAddr5()
    {
        return storeAddr5;
    }


    public void setStoreAddr5(String storeAddr5)
    {
        this.storeAddr5 = storeAddr5;
    }


    public String getStoreCity()
    {
        return storeCity;
    }


    public void setStoreCity(String storeCity)
    {
        this.storeCity = storeCity == null ? null : storeCity.trim();
    }


    public String getStoreState()
    {
        return storeState;
    }


    public void setStoreState(String storeState)
    {
        this.storeState = storeState == null ? null : storeState.trim();
    }


    public String getStorePostalCode()
    {
        return storePostalCode;
    }


    public void setStorePostalCode(String storePostalCode)
    {
        this.storePostalCode = storePostalCode == null ? null : storePostalCode
                .trim();
    }


    public String getStoreCtryCode()
    {
        return storeCtryCode;
    }


    public void setStoreCtryCode(String storeCtryCode)
    {
        this.storeCtryCode = storeCtryCode == null ? null : storeCtryCode
                .trim();
    }


    public String getContactPerson()
    {
        return contactPerson;
    }


    public void setContactPerson(String contactPerson)
    {
        this.contactPerson = contactPerson;
    }


    public String getContactTel()
    {
        return contactTel;
    }


    public void setContactTel(String contactTel)
    {
        this.contactTel = contactTel;
    }


    public String getContactEmail()
    {
        return contactEmail;
    }


    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }


    public Date getUpdateDate()
    {
        return updateDate == null ? null : (Date) updateDate.clone();
    }


    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate == null ? null : (Date) updateDate.clone();
    }


    public BigDecimal getAreaOid()
    {
        return areaOid;
    }


    public void setAreaOid(BigDecimal areaOid)
    {
        this.areaOid = areaOid;
    }


    public String getAction()
    {
        return action;
    }


    public void setAction(String action)
    {
        this.action = action;
    }


    public Boolean getIsWareHouse()
    {
        return isWareHouse;
    }


    public void setIsWareHouse(Boolean isWareHouse)
    {
        this.isWareHouse = isWareHouse;
    }


    @Override
    public String getCustomIdentification()
    {
        return storeCode;
    }
    
}