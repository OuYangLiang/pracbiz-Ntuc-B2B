package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.holder.UserProfileHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */

public class UserProfileExHolder extends UserProfileHolder
{
    
    private static final long serialVersionUID = -169726948645020295L;
    private String userTypeDesc;
    private String companyName;
    private BigDecimal groupOid;
    private String groupName;
    private BigDecimal currentUserType;
    private String supplierCode;
    private String supplierName;
    private String buyerCode;
    private String buyerName;
    private boolean priceDiscrepancy;
    private boolean qtyDiscrepancy;
    
    public String getUserTypeDesc()
    {
        return userTypeDesc;
    }


    public void setUserTypeDesc(String userTypeDesc)
    {
        this.userTypeDesc = userTypeDesc;
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


    /**
     * Getter of currentUserType.
     * @return Returns the currentUserType.
     */
    public BigDecimal getCurrentUserType()
    {
        return currentUserType;
    }


    /**
     * Setter of currentUserType.
     * @param currentUserType The currentUserType to set.
     */
    public void setCurrentUserType(BigDecimal currentUserType)
    {
        this.currentUserType = currentUserType;
    }


    /**
     * Getter of supplierCode.
     * @return Returns the supplierCode.
     */
    public String getSupplierCode()
    {
        return supplierCode;
    }


    /**
     * Setter of supplierCode.
     * @param supplierCode The supplierCode to set.
     */
    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
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
     * Getter of buyerCode.
     * @return Returns the buyerCode.
     */
    public String getBuyerCode()
    {
        return buyerCode;
    }


    /**
     * Setter of buyerCode.
     * @param buyerCode The buyerCode to set.
     */
    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
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


    public boolean isPriceDiscrepancy()
    {
        return priceDiscrepancy;
    }


    public void setPriceDiscrepancy(boolean priceDiscrepancy)
    {
        this.priceDiscrepancy = priceDiscrepancy;
    }


    public boolean isQtyDiscrepancy()
    {
        return qtyDiscrepancy;
    }


    public void setQtyDiscrepancy(boolean qtyDiscrepancy)
    {
        this.qtyDiscrepancy = qtyDiscrepancy;
    }


}
