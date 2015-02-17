//*****************************************************************************
//
// File Name       :  GroupTmpExHolder.java
// Date Created    :  Sep 3, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 3, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.GroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class GroupTmpExHolder extends GroupTmpHolder
{
    private static final long serialVersionUID = -7736087109622046791L;
    private BigDecimal currentUserTypeOid;
    private String userTypeId;
    private String groupTypeValue;
    private String actionTypeValue;
    private String ctrlStatusValue;
    private String buyerName;
    private String supplierName;
    private List<UserProfileTmpExHolder> users;
    private List<TradingPartnerHolder> tradingPartners;
    private List<SupplierHolder> suppliers;
    private List<RoleHolder> roles;
    private String company;
    private String lastUpdateDate;
    private GroupTmpExHolder oldVersion;


    public void addRoles(RoleHolder role)
    {
        if (roles == null)
        {
            roles = new ArrayList<RoleHolder>();
        }
        
        roles.add(role);
        
    }
    
    
    public void addTradingPartners(TradingPartnerHolder tradingPartner)
    {
        if (tradingPartners == null)
        {
            tradingPartners = new ArrayList<TradingPartnerHolder>();
        }
        
        tradingPartners.add(tradingPartner);
    }
    
    
    public void addUsers(UserProfileTmpExHolder user)
    {
        if (users == null)
        {
            users = new ArrayList<UserProfileTmpExHolder>();
        }
        
        users.add(user);
    }
    
    
    public void addSuppliers(SupplierHolder supplier)
    {
        if (suppliers == null)
        {
            suppliers = new ArrayList<SupplierHolder>();
        }
        
        suppliers.add(supplier);
    }
    
    /**
     * Getter of currentUserTypeOid.
     * 
     * @return Returns the currentUserTypeOid.
     */
    public BigDecimal getCurrentUserTypeOid()
    {
        return currentUserTypeOid;
    }

    /**
     * Setter of currentUserTypeOid.
     * 
     * @param currentUserTypeOid The currentUserTypeOid to set.
     */
    public void setCurrentUserTypeOid(BigDecimal currentUserTypeOid)
    {
        this.currentUserTypeOid = currentUserTypeOid;
    }

    /**
     * Getter of userTypeId.
     * @return Returns the userTypeId.
     */
    public String getUserTypeId()
    {
        return userTypeId;
    }

    /**
     * Setter of userTypeId.
     * @param userTypeId The userTypeId to set.
     */
    public void setUserTypeId(String userTypeId)
    {
        this.userTypeId = userTypeId;
    }

    /**
     * Getter of groupTypeValue.
     * @return Returns the groupTypeValue.
     */
    public String getGroupTypeValue()
    {
        return groupTypeValue;
    }

    /**
     * Setter of groupTypeValue.
     * @param groupTypeValue The groupTypeValue to set.
     */
    public void setGroupTypeValue(String groupTypeValue)
    {
        this.groupTypeValue = groupTypeValue;
    }

    /**
     * Getter of actionTypeValue.
     * @return Returns the actionTypeValue.
     */
    public String getActionTypeValue()
    {
        return actionTypeValue;
    }

    /**
     * Setter of actionTypeValue.
     * @param actionTypeValue The actionTypeValue to set.
     */
    public void setActionTypeValue(String actionTypeValue)
    {
        this.actionTypeValue = actionTypeValue;
    }

    /**
     * Getter of ctrlStatusValue.
     * @return Returns the ctrlStatusValue.
     */
    public String getCtrlStatusValue()
    {
        return ctrlStatusValue;
    }

    /**
     * Setter of ctrlStatusValue.
     * @param ctrlStatusValue The ctrlStatusValue to set.
     */
    public void setCtrlStatusValue(String ctrlStatusValue)
    {
        this.ctrlStatusValue = ctrlStatusValue;
    }

    /**
     * Getter of buyerName.
     * @return Returns the buyerName.
     */
    public String getBuyerName()
    {
        return buyerName;
    }

    /**
     * Setter of buyerName.
     * @param buyerName The buyerName to set.
     */
    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }

    /**
     * Getter of supplierName.
     * @return Returns the supplierName.
     */
    public String getSupplierName()
    {
        return supplierName;
    }

    /**
     * Setter of supplierName.
     * @param supplierName The supplierName to set.
     */
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }

    /**
     * Getter of users.
     * @return Returns the users.
     */
    public List<UserProfileTmpExHolder> getUsers()
    {
        return users;
    }

    /**
     * Setter of users.
     * @param users The users to set.
     */
    public void setUsers(List<UserProfileTmpExHolder> users)
    {
        this.users = users;
    }

    /**
     * Getter of tradingPartners.
     * @return Returns the tradingPartners.
     */
    public List<TradingPartnerHolder> getTradingPartners()
    {
        return tradingPartners;
    }

    /**
     * Setter of tradingPartners.
     * @param tradingPartners The tradingPartners to set.
     */
    public void setTradingPartners(List<TradingPartnerHolder> tradingPartners)
    {
        this.tradingPartners = tradingPartners;
    }

    /**
     * Getter of suppliers.
     * @return Returns the suppliers.
     */
    public List<SupplierHolder> getSuppliers()
    {
        return suppliers;
    }

    /**
     * Setter of suppliers.
     * @param suppliers The suppliers to set.
     */
    public void setSuppliers(List<SupplierHolder> suppliers)
    {
        this.suppliers = suppliers;
    }

    /**
     * Getter of roles.
     * @return Returns the roles.
     */
    public List<RoleHolder> getRoles()
    {
        return roles;
    }

    /**
     * Setter of roles.
     * @param roles The roles to set.
     */
    public void setRoles(List<RoleHolder> roles)
    {
        this.roles = roles;
    }


    /**
     * Getter of company.
     * @return Returns the company.
     */
    public String getCompany()
    {
        return company;
    }


    /**
     * Setter of company.
     * @param company The company to set.
     */
    public void setCompany(String company)
    {
        this.company = company;
    }


    /**
     * Getter of lastUpdateDate.
     * @return Returns the lastUpdateDate.
     */
    public String getLastUpdateDate()
    {
        return lastUpdateDate;
    }


    /**
     * Setter of lastUpdateDate.
     * @param lastUpdateDate The lastUpdateDate to set.
     */
    public void setLastUpdateDate(String lastUpdateDate)
    {
        this.lastUpdateDate = lastUpdateDate;
    }


    /**
     * Getter of oldVersion.
     * @return Returns the oldVersion.
     */
    public GroupTmpExHolder getOldVersion()
    {
        return oldVersion;
    }


    /**
     * Setter of oldVersion.
     * @param oldVersion The oldVersion to set.
     */
    public void setOldVersion(GroupTmpExHolder oldVersion)
    {
        this.oldVersion = oldVersion;
    }

}
