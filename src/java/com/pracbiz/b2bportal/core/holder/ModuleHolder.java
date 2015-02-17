package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class ModuleHolder extends BaseHolder
{
    private static final long serialVersionUID = -7443497467118978507L;

    private String moduleId;

    private String moduleName;

    private String moduleTitleKey;

    private String parentId;

    private Short moduleLevel;

    private Boolean showable;

    private Integer showSort;

    private String moduleLink;


    public String getModuleId()
    {
        return moduleId;
    }


    public void setModuleId(String moduleId)
    {
        this.moduleId = moduleId == null ? null : moduleId.trim();
    }


    public String getModuleName()
    {
        return moduleName;
    }


    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }


    public String getModuleTitleKey()
    {
        return moduleTitleKey;
    }


    public void setModuleTitleKey(String moduleTitleKey)
    {
        this.moduleTitleKey = moduleTitleKey == null ? null : moduleTitleKey
                .trim();
    }


    public String getParentId()
    {
        return parentId;
    }


    public void setParentId(String parentId)
    {
        this.parentId = parentId == null ? null : parentId.trim();
    }


    public Short getModuleLevel()
    {
        return moduleLevel;
    }


    public void setModuleLevel(Short moduleLevel)
    {
        this.moduleLevel = moduleLevel;
    }


    public Boolean getShowable()
    {
        return showable;
    }


    public void setShowable(Boolean showable)
    {
        this.showable = showable;
    }


    public Integer getShowSort()
    {
        return showSort;
    }


    public void setShowSort(Integer showSort)
    {
        this.showSort = showSort;
    }


    public String getModuleLink()
    {
        return moduleLink;
    }


    public void setModuleLink(String moduleLink)
    {
        this.moduleLink = moduleLink == null ? null : moduleLink.trim();
    }


    @Override
    public String getCustomIdentification()
    {
        return moduleId;
    }

}