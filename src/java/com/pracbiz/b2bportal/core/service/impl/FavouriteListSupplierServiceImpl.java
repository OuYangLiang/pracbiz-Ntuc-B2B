package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.FavouriteListSupplierHolder;
import com.pracbiz.b2bportal.core.mapper.FavouriteListSupplierMapper;
import com.pracbiz.b2bportal.core.service.FavouriteListSupplierService;

public class FavouriteListSupplierServiceImpl extends
        DBActionServiceDefaultImpl<FavouriteListSupplierHolder> implements
        FavouriteListSupplierService
{
    @Autowired FavouriteListSupplierMapper mapper;
    
    @Override
    public List<FavouriteListSupplierHolder> select(
            FavouriteListSupplierHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(FavouriteListSupplierHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(
            FavouriteListSupplierHolder oldObj_,
            FavouriteListSupplierHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(FavouriteListSupplierHolder oldObj_,
            FavouriteListSupplierHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(FavouriteListSupplierHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<FavouriteListSupplierHolder> selectFavouriteListSupplierByListOid(
            BigDecimal listOid) throws Exception
    {
        if (listOid == null)
        {
            throw new IllegalArgumentException();
        }
        FavouriteListSupplierHolder param = new FavouriteListSupplierHolder();
        param.setListOid(listOid);
        return this.select(param);
    }

}
