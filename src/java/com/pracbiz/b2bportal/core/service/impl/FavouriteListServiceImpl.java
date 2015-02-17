package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.FavouriteListSupplierHolder;
import com.pracbiz.b2bportal.core.holder.extension.FavouriteListExHolder;
import com.pracbiz.b2bportal.core.mapper.FavouriteListMapper;
import com.pracbiz.b2bportal.core.mapper.FavouriteListSupplierMapper;
import com.pracbiz.b2bportal.core.service.FavouriteListService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class FavouriteListServiceImpl extends
        DBActionServiceDefaultImpl<FavouriteListExHolder> implements
        FavouriteListService
{

    @Autowired FavouriteListMapper mapper;
    @Autowired FavouriteListSupplierMapper favouriteListSupplierMapper;
    
    @Override
    public List<FavouriteListExHolder> select(FavouriteListExHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(FavouriteListExHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(FavouriteListExHolder oldObj_,
            FavouriteListExHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void updateByPrimaryKey(FavouriteListExHolder oldObj_,
            FavouriteListExHolder newObj_) throws Exception
    {
        // TODO Auto-generated method stub

    }


    @Override
    public void delete(FavouriteListExHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<FavouriteListExHolder> selectFavouriteListByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        FavouriteListExHolder param = new FavouriteListExHolder();
        param.setUserOid(userOid);
        return this.select(param);
    }


    @Override
    public void deleteFavouriteListByUserOid(BigDecimal userOid)
            throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        List<FavouriteListExHolder> favouriteLists = this.selectFavouriteListByUserOid(userOid);
        if (favouriteLists == null || favouriteLists.isEmpty())
        {
            return;
        }
        for (FavouriteListExHolder holder : favouriteLists)
        {
            FavouriteListSupplierHolder param = new FavouriteListSupplierHolder();
            param.setListOid(holder.getListOid());
            favouriteListSupplierMapper.delete(param);
            mapper.delete(holder);
        }
    }

}
