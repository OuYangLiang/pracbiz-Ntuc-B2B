package com.pracbiz.b2bportal.core.holder.extension;

import java.io.Serializable;
import java.math.BigDecimal;

import com.pracbiz.b2bportal.core.holder.OperationUrlHolder;

public class OperationUrlExHolder extends OperationUrlHolder implements Serializable
{
    private static final long serialVersionUID = 4744811555810624873L;
    private BigDecimal userOid;
    private BigDecimal groupOid;


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public BigDecimal getGroupOid()
    {
        return groupOid;
    }


    public void setGroupOid(BigDecimal groupOid)
    {
        this.groupOid = groupOid;
    }

}
