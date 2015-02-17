package com.pracbiz.b2bportal.base.service;

import java.util.List;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public interface BaseService<T extends BaseHolder>
{
    public List<T> select(T param) throws Exception;
}
