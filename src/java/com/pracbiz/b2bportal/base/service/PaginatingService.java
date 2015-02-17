package com.pracbiz.b2bportal.base.service;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public interface PaginatingService<T extends BaseHolder>
{
    public int getCountOfSummary(T param) throws Exception;


    public List<T> getListOfSummary(T param) throws Exception;
}
