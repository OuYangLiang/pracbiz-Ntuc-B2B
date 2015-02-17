package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class UserClassHolder extends BaseHolder
{

    /**
     * 
     */
    private static final long serialVersionUID = 7860639304739565113L;
    private BigDecimal userOid;
    private BigDecimal classOid;
    
    
    public UserClassHolder(BigDecimal userOid, BigDecimal classOid)
    {
        this.userOid = userOid;
        this.classOid = classOid;
    }
    
    public UserClassHolder()
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


    public BigDecimal getClassOid()
    {
        return classOid;
    }


    public void setClassOid(BigDecimal classOid)
    {
        this.classOid = classOid;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((userOid == null) ? 0 : userOid.hashCode())
            + ((classOid == null) ? 0 : classOid.hashCode());
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
        UserClassHolder other = (UserClassHolder)obj;
        if(userOid == null)
        {
            if(other.userOid != null)
                return false;
        }
        else if(!userOid.equals(other.userOid))
            return false;
        
        if(classOid == null)
        {
            if(other.classOid != null)
                return false;
        }
        else if(!classOid.equals(other.classOid))
            return false;
        
        return true;
    }
    
    
    @Override
    public String getCustomIdentification()
    {
        return userOid.toString() + classOid.toString();
    }

}
