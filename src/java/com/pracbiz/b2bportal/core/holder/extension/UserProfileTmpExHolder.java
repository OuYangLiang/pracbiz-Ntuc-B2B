package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.RoleHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */

public class UserProfileTmpExHolder extends UserProfileTmpHolder
{
    private static final long serialVersionUID = -4554007715664664763L;
    private String userTypeDesc;
    private BigDecimal currentUserType;
    private BigDecimal currentBuyerOid;
    private BigDecimal currentSupplierOid;
    private String paramActive;
    private String paramBlocked;
    private String companyName;
    private BigDecimal groupOid;
    private String groupName;
    private String actionTypeValue;
    private String ctrlStatusValue;
    private List<BigDecimal> userOids;
    private UserProfileTmpExHolder oldVersion;
    private List<RoleTmpHolder> selectedRolesList;
    private List<RoleHolder> oldSelectedRolesList;
    private List<BuyerStoreHolder> selectedStoresList;
    private List<BuyerStoreHolder> oldSelectedStoresList;
    private List<BuyerStoreAreaHolder> selectedAreasList;
    private List<BuyerStoreAreaHolder> oldSelectedAreasList;
    private List<BuyerStoreHolder> selectedWareHouseList;
    private List<BuyerStoreHolder> oldSelectedWareHouseList;
    private List<ClassExHolder> selectedClassList;
    private List<ClassExHolder> oldSelectedClassList;
    private List<SubclassExHolder> selectedSubclassList;
    private List<SubclassExHolder> oldSelectedSubclassList;
    private List<BuyerHolder> selectedBuyersList;
    private List<BuyerHolder> oldSelectedBuyersList;
    private Boolean isLocked;
    private String company;

    public String getUserTypeDesc()
    {
        return userTypeDesc;
    }


    public void setUserTypeDesc(String userTypeDesc)
    {
        this.userTypeDesc = userTypeDesc;
    }


    public BigDecimal getCurrentUserType()
    {
        return currentUserType;
    }


    public void setCurrentUserType(BigDecimal currentUserType)
    {
        this.currentUserType = currentUserType;
    }


    public String getParamBlocked()
    {
        return paramBlocked;
    }


    public void setParamBlocked(String paramBlocked)
    {
        this.paramBlocked = paramBlocked;
    }


    public String getCompanyName()
    {
        return companyName;
    }


    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public String getGroupName()
    {
        return groupName;
    }


    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }


    public String getActionTypeValue()
    {
        return actionTypeValue;
    }


    public void setActionTypeValue(String actionTypeValue)
    {
        this.actionTypeValue = actionTypeValue;
    }


    public String getCtrlStatusValue()
    {
        return ctrlStatusValue;
    }


    public void setCtrlStatusValue(String ctrlStatusValue)
    {
        this.ctrlStatusValue = ctrlStatusValue;
    }


    /**
     * Getter of userOids.
     * @return Returns the userOids.
     */
    public List<BigDecimal> getUserOids()
    {
        return userOids;
    }


    /**
     * Setter of userOids.
     * @param userOids The userOids to set.
     */
    public void setUserOids(List<BigDecimal> userOids)
    {
        this.userOids = userOids;
    }
    
    
    public void addUserOids(BigDecimal userOid)
    {
        if (userOids == null)
        {
            userOids = new ArrayList<BigDecimal>();
        }
        
        userOids.add(userOid);
    }


    public BigDecimal getCurrentBuyerOid()
    {
        return currentBuyerOid;
    }


    public void setCurrentBuyerOid(BigDecimal currentBuyerOid)
    {
        this.currentBuyerOid = currentBuyerOid;
    }


    public BigDecimal getCurrentSupplierOid()
    {
        return currentSupplierOid;
    }


    public void setCurrentSupplierOid(BigDecimal currentSupplierOid)
    {
        this.currentSupplierOid = currentSupplierOid;
    }


    public UserProfileTmpExHolder getOldVersion()
    {
        return oldVersion;
    }


    public void setOldVersion(UserProfileTmpExHolder oldVersion)
    {
        this.oldVersion = oldVersion;
    }


    public List<RoleTmpHolder> getSelectedRolesList()
    {
        return selectedRolesList;
    }


    public void setSelectedRolesList(List<RoleTmpHolder> selectedRolesList)
    {
        this.selectedRolesList = selectedRolesList;
    }


    public List<RoleHolder> getOldSelectedRolesList()
    {
        return oldSelectedRolesList;
    }


    public void setOldSelectedRolesList(List<RoleHolder> oldSelectedRolesList)
    {
        this.oldSelectedRolesList = oldSelectedRolesList;
    }


    public Boolean getLocked()
    {
        return isLocked;
    }


    public void setIsLocked(Boolean isLocked)
    {
        this.isLocked = isLocked;
    }


    public String getParamActive()
    {
        return paramActive;
    }


    public List<BuyerStoreHolder> getSelectedStoresList()
    {
        return selectedStoresList;
    }


    public void setSelectedStoresList(List<BuyerStoreHolder> selectedStoresList)
    {
        this.selectedStoresList = selectedStoresList;
    }


    public List<BuyerStoreHolder> getOldSelectedStoresList()
    {
        return oldSelectedStoresList;
    }


    public void setOldSelectedStoresList(
            List<BuyerStoreHolder> oldSelectedStoresList)
    {
        this.oldSelectedStoresList = oldSelectedStoresList;
    }


    public List<BuyerStoreAreaHolder> getSelectedAreasList()
    {
        return selectedAreasList;
    }


    public void setSelectedAreasList(List<BuyerStoreAreaHolder> selectedAreasList)
    {
        this.selectedAreasList = selectedAreasList;
    }


    public List<BuyerStoreAreaHolder> getOldSelectedAreasList()
    {
        return oldSelectedAreasList;
    }


    public void setOldSelectedAreasList(
            List<BuyerStoreAreaHolder> oldSelectedAreasList)
    {
        this.oldSelectedAreasList = oldSelectedAreasList;
    }


    public void setParamActive(String paramActive)
    {
        this.paramActive = paramActive;
    }


    public List<BuyerStoreHolder> getSelectedWareHouseList()
    {
        return selectedWareHouseList;
    }


    public void setSelectedWareHouseList(
        List<BuyerStoreHolder> selectedWareHouseList)
    {
        this.selectedWareHouseList = selectedWareHouseList;
    }


    public List<BuyerStoreHolder> getOldSelectedWareHouseList()
    {
        return oldSelectedWareHouseList;
    }


    public void setOldSelectedWareHouseList(
        List<BuyerStoreHolder> oldSelectedWareHouseList)
    {
        this.oldSelectedWareHouseList = oldSelectedWareHouseList;
    }


    public List<BuyerHolder> getSelectedBuyersList()
    {
        return selectedBuyersList;
    }


    public void setSelectedBuyersList(List<BuyerHolder> selectedBuyersList)
    {
        this.selectedBuyersList = selectedBuyersList;
    }


    public List<BuyerHolder> getOldSelectedBuyersList()
    {
        return oldSelectedBuyersList;
    }


    public void setOldSelectedBuyersList(List<BuyerHolder> oldSelectedBuyersList)
    {
        this.oldSelectedBuyersList = oldSelectedBuyersList;
    }


    public String getCompany()
    {
        return company;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public List<ClassExHolder> getSelectedClassList()
    {
        return selectedClassList;
    }


    public void setSelectedClassList(List<ClassExHolder> selectedClassList)
    {
        this.selectedClassList = selectedClassList;
    }


    public List<ClassExHolder> getOldSelectedClassList()
    {
        return oldSelectedClassList;
    }


    public void setOldSelectedClassList(List<ClassExHolder> oldSelectedClassList)
    {
        this.oldSelectedClassList = oldSelectedClassList;
    }


    public List<SubclassExHolder> getSelectedSubclassList()
    {
        return selectedSubclassList;
    }


    public void setSelectedSubclassList(List<SubclassExHolder> selectedSubclassList)
    {
        this.selectedSubclassList = selectedSubclassList;
    }


    public List<SubclassExHolder> getOldSelectedSubclassList()
    {
        return oldSelectedSubclassList;
    }


    public void setOldSelectedSubclassList(
            List<SubclassExHolder> oldSelectedSubclassList)
    {
        this.oldSelectedSubclassList = oldSelectedSubclassList;
    }

    
    
}
