package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaUserMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAreaUserServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreAreaUserHolder> implements
        BuyerStoreAreaUserService
{

    @Autowired
    private BuyerStoreAreaUserMapper mapper;


    @Override
    public List<BuyerStoreAreaUserHolder> select(BuyerStoreAreaUserHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(BuyerStoreAreaUserHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerStoreAreaUserHolder oldObj_,
            BuyerStoreAreaUserHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreAreaUserHolder oldObj_,
            BuyerStoreAreaUserHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(BuyerStoreAreaUserHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<BuyerStoreAreaUserHolder> selectAreaUserByAreaOid(
            BigDecimal areaOid) throws Exception
    {
        if (areaOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserHolder param = new BuyerStoreAreaUserHolder();
        param.setAreaOid(areaOid);

        return mapper.select(param);
    }


    @Override
    public List<BuyerStoreAreaUserHolder> selectAreaUserByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserHolder param = new BuyerStoreAreaUserHolder();
        param.setUserOid(userOid);

        return mapper.select(param);
    }


    @Override
    public BuyerStoreAreaUserHolder selectByUserOidAndAreaOid(
            BigDecimal userOid, BigDecimal areaOid) throws Exception
    {
        if (userOid == null || areaOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserHolder param = new BuyerStoreAreaUserHolder();
        param.setUserOid(userOid);
        param.setAreaOid(areaOid);
        List<BuyerStoreAreaUserHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

}
