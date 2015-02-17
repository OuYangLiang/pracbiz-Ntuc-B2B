package com.pracbiz.b2bportal.base.action;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class GridResult
{
    private Integer totalRecords;
    private List<? extends BaseHolder> items;

    private String identifier;


    public String getIdentifier()
    {
        return identifier;
    }


    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }


    public Integer getNumRows()
    {
        return getTotalRecords();
    }


    public Integer getTotalRecords()
    {
        return totalRecords;
    }


    public void setTotalRecords(Integer totalRecords)
    {
        this.totalRecords = totalRecords;
    }


    public List<? extends BaseHolder> getItems()
    {
        return items;
    }


    public void setItems(List<BaseHolder> items)
    {
        this.items = items;
    }

}
