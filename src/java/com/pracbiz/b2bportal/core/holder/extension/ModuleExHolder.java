package com.pracbiz.b2bportal.core.holder.extension;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.core.holder.ModuleHolder;

public class ModuleExHolder extends ModuleHolder implements Serializable
{
    private static final long serialVersionUID = 8991391449689268984L;
    private BigDecimal userOid;
    private List<ModuleExHolder> childModules;
    private String moduleTitleAftKey;


    public BigDecimal getUserOid()
    {
        return userOid;
    }


    public void setUserOid(BigDecimal userOid)
    {
        this.userOid = userOid;
    }


    public List<ModuleExHolder> getChildModules()
    {
        return childModules;
    }


    public void setChildModules(List<ModuleExHolder> childModules)
    {
        this.childModules = childModules;
    }


    public String getModuleTitleAftKey()
    {
        return moduleTitleAftKey;
    }


    public void setModuleTitleAftKey(String moduleTitleAftKey)
    {
        this.moduleTitleAftKey = moduleTitleAftKey;
    }
    
}
