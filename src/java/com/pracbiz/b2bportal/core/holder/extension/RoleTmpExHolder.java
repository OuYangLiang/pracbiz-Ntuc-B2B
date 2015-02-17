package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.OperationHolder;
import com.pracbiz.b2bportal.core.holder.RoleTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

public class RoleTmpExHolder extends RoleTmpHolder
{
    private static final long serialVersionUID = -2990396902054410300L;
    private BigDecimal currentUserTypeOid;
    private BigDecimal currentUserBuyerOid;
    private BigDecimal currentUserSupplierOid;
    private String userTypeId;
    private String company;
    private String roleTypeValue;
    private String actionTypeValue;
    private String ctrlStatusValue;
    private List<BigDecimal> roleOids;
    private BigDecimal groupOid;
    private BigDecimal selectedSupplierOid;
    private String allSupplierKey;
    private BigDecimal currentUserOid;
    
    private List<OperationHolder> selectedOperations;
    private List<SupplierHolder> selectedSuppliers;
    private boolean companyChanged;
    private RoleTmpExHolder oldVersion;

    public BigDecimal getCurrentUserBuyerOid()
    {
        return currentUserBuyerOid;
    }


    public void setCurrentUserBuyerOid(BigDecimal currentUserBuyerOid)
    {
        this.currentUserBuyerOid = currentUserBuyerOid;
    }


    public BigDecimal getSelectedSupplierOid()
    {
        return selectedSupplierOid;
    }


    public void setSelectedSupplierOid(BigDecimal selectedSupplierOid)
    {
        this.selectedSupplierOid = selectedSupplierOid;
    }


    public BigDecimal getCurrentUserSupplierOid()
    {
        return currentUserSupplierOid;
    }


    public void setCurrentUserSupplierOid(BigDecimal currentUserSupplierOid)
    {
        this.currentUserSupplierOid = currentUserSupplierOid;
    }


    public String getRoleTypeValue()
    {
        return roleTypeValue;
    }


    public void setRoleTypeValue(String roleTypeValue)
    {
        this.roleTypeValue = roleTypeValue;
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


    public String getUserTypeId()
    {
        return userTypeId;
    }


    public void setUserTypeId(String userTypeId)
    {
        this.userTypeId = userTypeId;
    }


    public String getCompany()
    {
        return company;
    }


    public void setCompany(String company)
    {
        this.company = company;
    }


    public BigDecimal getCurrentUserTypeOid()
    {
        return currentUserTypeOid;
    }


    public void setCurrentUserTypeOid(BigDecimal currentUserTypeOid)
    {
        this.currentUserTypeOid = currentUserTypeOid;
    }
    
    
    /**
     * Getter of roleOids.
     * @return Returns the roleOids.
     */
    public List<BigDecimal> getRoleOids()
    {
        return roleOids;
    }


    /**
     * Setter of roleOids.
     * @param roleOids The roleOids to set.
     */
    public void setRoleOids(List<BigDecimal> roleOids)
    {
        this.roleOids = roleOids;
    }
    
    
    public void addRoleOid(BigDecimal roleOid)
    {
        if (roleOids == null)
        {
            roleOids = new ArrayList<BigDecimal>();
        }
        
        roleOids.add(roleOid);
    }


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }


    public List<OperationHolder> getSelectedOperations()
    {
        return selectedOperations;
    }


    public void setSelectedOperations(List<OperationHolder> selectedOperations)
    {
        this.selectedOperations = selectedOperations;
    }


    public RoleTmpExHolder getOldVersion()
    {
        return oldVersion;
    }


    public void setOldVersion(RoleTmpExHolder oldVersion)
    {
        this.oldVersion = oldVersion;
    }


    public List<SupplierHolder> getSelectedSuppliers()
    {
        return selectedSuppliers;
    }


    public void setSelectedSuppliers(List<SupplierHolder> selectedSuppliers)
    {
        this.selectedSuppliers = selectedSuppliers;
    }


    public boolean isCompanyChanged()
    {
        return companyChanged;
    }


    public void setCompanyChanged(boolean companyChanged)
    {
        this.companyChanged = companyChanged;
    }


    public String getAllSupplierKey()
    {
        return allSupplierKey;
    }


    public void setAllSupplierKey(String allSupplierKey)
    {
        this.allSupplierKey = allSupplierKey;
    }


    public BigDecimal getCurrentUserOid()
    {
        return currentUserOid;
    }


    public void setCurrentUserOid(BigDecimal currentUserOid)
    {
        this.currentUserOid = currentUserOid;
    }

}
