//*****************************************************************************
//
// File Name       :  AuditAccessExHolder.java
// Date Created    :  Oct 9, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Oct 9, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.holder.extension;

import java.math.BigDecimal;
import java.util.Date;

import com.pracbiz.b2bportal.core.constants.ArithmeticTerm;
import com.pracbiz.b2bportal.core.holder.AuditAccessHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class AuditAccessExHolder extends AuditAccessHolder
{
    private static final long serialVersionUID = 4702607621958149608L;

    private Date actionDateFrom;
    private Date actionDateTo;
    private BigDecimal buyerOid;
    private BigDecimal supplierOid;
    private ArithmeticTerm arithmeticTerm;
    private String arithmeticTermValue;
    private BigDecimal userTypeOid;
    private String userType;
    private String successValue;
    private String actionTypeValue;
    private String errorDesc;
    private String principalTypeValue;
    
    private BigDecimal currentUserTypeOid;
    

    /**
     * Getter of actionDateFrom.
     * 
     * @return Returns the actionDateFrom.
     */
    public Date getActionDateFrom()
    {
        return actionDateFrom == null ? null : (Date) actionDateFrom.clone();
    }

    /**
     * Setter of actionDateFrom.
     * 
     * @param actionDateFrom The actionDateFrom to set.
     */
    public void setActionDateFrom(Date actionDateFrom)
    {
        this.actionDateFrom = actionDateFrom == null ? null : (Date) actionDateFrom.clone();
    }

    /**
     * Getter of actionDateTo.
     * 
     * @return Returns the actionDateTo.
     */
    public Date getActionDateTo()
    {
        return actionDateTo == null ? null : (Date) actionDateTo.clone();
    }

    /**
     * Setter of actionDateTo.
     * 
     * @param actionDateTo The actionDateTo to set.
     */
    public void setActionDateTo(Date actionDateTo)
    {
        this.actionDateTo = actionDateTo == null ? null : (Date) actionDateTo.clone();
    }

    /**
     * Getter of buyerOid.
     * 
     * @return Returns the buyerOid.
     */
    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }

    /**
     * Setter of buyerOid.
     * 
     * @param buyerOid The buyerOid to set.
     */
    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }

    /**
     * Getter of supplierOid.
     * 
     * @return Returns the supplierOid.
     */
    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }

    /**
     * Setter of supplierOid.
     * 
     * @param supplierOid The supplierOid to set.
     */
    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }

    /**
     * Getter of arithmeticTerm.
     * @return Returns the arithmeticTerm.
     */
    public ArithmeticTerm getArithmeticTerm()
    {
        return arithmeticTerm;
    }

    /**
     * Setter of arithmeticTerm.
     * @param arithmeticTerm The arithmeticTerm to set.
     */
    public void setArithmeticTerm(ArithmeticTerm arithmeticTerm)
    {
        this.arithmeticTerm = arithmeticTerm;
    }

    /**
     * Getter of userTypeOid.
     * @return Returns the userTypeOid.
     */
    public BigDecimal getUserTypeOid()
    {
        return userTypeOid;
    }

    /**
     * Setter of userTypeOid.
     * @param userTypeOid The userTypeOid to set.
     */
    public void setUserTypeOid(BigDecimal userTypeOid)
    {
        this.userTypeOid = userTypeOid;
    }

    /**
     * Getter of successValue.
     * @return Returns the successValue.
     */
    public String getSuccessValue()
    {
        return successValue;
    }

    /**
     * Setter of successValue.
     * @param successValue The successValue to set.
     */
    public void setSuccessValue(String successValue)
    {
        this.successValue = successValue;
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
     * Getter of errorDesc.
     * @return Returns the errorDesc.
     */
    public String getErrorDesc()
    {
        return errorDesc;
    }

    /**
     * Setter of errorDesc.
     * @param errorDesc The errorDesc to set.
     */
    public void setErrorDesc(String errorDesc)
    {
        this.errorDesc = errorDesc;
    }

    /**
     * Getter of principalTypeValue.
     * @return Returns the principalTypeValue.
     */
    public String getPrincipalTypeValue()
    {
        return principalTypeValue;
    }

    /**
     * Setter of principalTypeValue.
     * @param principalTypeValue The principalTypeValue to set.
     */
    public void setPrincipalTypeValue(String principalTypeValue)
    {
        this.principalTypeValue = principalTypeValue;
    }

    /**
     * Getter of userType.
     * @return Returns the userType.
     */
    public String getUserType()
    {
        return userType;
    }

    /**
     * Setter of userType.
     * @param userType The userType to set.
     */
    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    /**
     * Getter of arithmeticTermValue.
     * @return Returns the arithmeticTermValue.
     */
    public String getArithmeticTermValue()
    {
        return arithmeticTermValue;
    }

    /**
     * Setter of arithmeticTermValue.
     * @param arithmeticTermValue The arithmeticTermValue to set.
     */
    public void setArithmeticTermValue(String arithmeticTermValue)
    {
        this.arithmeticTermValue = arithmeticTermValue;
    }
    
    public void setArithmeticTermValue()
    {
        if (ArithmeticTerm.E.equals(this.arithmeticTerm))
        {
            this.setArithmeticTermValue("=");
        }
        
        if (ArithmeticTerm.G.equals(this.arithmeticTerm))
        {
            this.setArithmeticTermValue(">");
        }
        
        if (ArithmeticTerm.GE.equals(this.arithmeticTerm))
        {
            this.setArithmeticTermValue(">=");
        }
        
        if (ArithmeticTerm.L.equals(this.arithmeticTerm))
        {
            this.setArithmeticTermValue("<");
        }
        
        if (ArithmeticTerm.LE.equals(this.arithmeticTerm))
        {
            this.setArithmeticTermValue("<=");
        }
    }

    public BigDecimal getCurrentUserTypeOid()
    {
        return currentUserTypeOid;
    }

    public void setCurrentUserTypeOid(BigDecimal currentUserTypeOid)
    {
        this.currentUserTypeOid = currentUserTypeOid;
    }

}
