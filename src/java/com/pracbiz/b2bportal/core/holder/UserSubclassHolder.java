package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class UserSubclassHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 6813447814600172339L;
    private BigDecimal userOid;
    private BigDecimal subclassOid;

    
    public UserSubclassHolder(BigDecimal userOid, BigDecimal subclassOid)
    {
        this.userOid = userOid;
        this.subclassOid = subclassOid;
    }
    
    
    public UserSubclassHolder()
    {
        
    }
    

    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public BigDecimal getSubclassOid()
    {
        return subclassOid;
    }


    public void setSubclassOid(BigDecimal subclassOid)
    {
        this.subclassOid = subclassOid;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((userOid == null) ? 0 : userOid.hashCode())
            + ((subclassOid == null) ? 0 : subclassOid.hashCode());
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
        UserSubclassHolder other = (UserSubclassHolder)obj;
        if(userOid == null)
        {
            if(other.userOid != null)
                return false;
        }
        else if(!userOid.equals(other.userOid))
            return false;
        
        if(subclassOid == null)
        {
            if(other.subclassOid != null)
                return false;
        }
        else if(!subclassOid.equals(other.subclassOid))
            return false;
        
        return true;
    }

    @Override
    public String getCustomIdentification()
    {
        return userOid.toString() + subclassOid.toString();
    }

}
