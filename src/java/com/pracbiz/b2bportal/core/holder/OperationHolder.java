package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class OperationHolder extends BaseHolder
{
    private static final long serialVersionUID = 8553425366347388004L;

    private String opnId;

    private String opnDesc;

    private Short opnOrder;

    private Boolean implicit;

    private String optType;

    private String moduleId;

    private String buttonTitleKey;

    private Boolean needAuth;


    public String getOpnId()
    {
        return opnId;
    }


    public void setOpnId(String opnId)
    {
        this.opnId = opnId == null ? null : opnId.trim();
    }


    public String getOpnDesc()
    {
        return opnDesc;
    }


    public void setOpnDesc(String opnDesc)
    {
        this.opnDesc = opnDesc == null ? null : opnDesc.trim();
    }


    public Short getOpnOrder()
    {
        return opnOrder;
    }


    public void setOpnOrder(Short opnOrder)
    {
        this.opnOrder = opnOrder;
    }


    public Boolean getImplicit()
    {
        return implicit;
    }


    public void setImplicit(Boolean implicit)
    {
        this.implicit = implicit;
    }


    public String getOptType()
    {
        return optType;
    }


    public void setOptType(String optType)
    {
        this.optType = optType == null ? null : optType.trim();
    }


    public String getModuleId()
    {
        return moduleId;
    }


    public void setModuleId(String moduleId)
    {
        this.moduleId = moduleId == null ? null : moduleId.trim();
    }


    public String getButtonTitleKey()
    {
        return buttonTitleKey;
    }


    public void setButtonTitleKey(String buttonTitleKey)
    {
        this.buttonTitleKey = buttonTitleKey == null ? null : buttonTitleKey
                .trim();
    }


    public Boolean getNeedAuth()
    {
        return needAuth;
    }


    public void setNeedAuth(Boolean needAuth)
    {
        this.needAuth = needAuth;
    }


    @Override
    public String getCustomIdentification()
    {
        return opnId;
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((opnId == null) ? 0 : opnId.hashCode());
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
        OperationHolder other = (OperationHolder)obj;
        if(opnId == null)
        {
            if(other.opnId != null)
                return false;
        }
        else if(!opnId.equals(other.opnId))
            return false;
        return true;
    }

}