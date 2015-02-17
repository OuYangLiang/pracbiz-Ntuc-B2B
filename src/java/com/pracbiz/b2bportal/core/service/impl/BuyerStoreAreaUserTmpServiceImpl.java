package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaUserTmpMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAreaUserTmpServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreAreaUserTmpHolder> implements
        BuyerStoreAreaUserTmpService
{

    @Autowired
    private BuyerStoreAreaUserTmpMapper mapper;


    @Override
    public List<BuyerStoreAreaUserTmpHolder> select(
            BuyerStoreAreaUserTmpHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(BuyerStoreAreaUserTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(
            BuyerStoreAreaUserTmpHolder oldObj_,
            BuyerStoreAreaUserTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreAreaUserTmpHolder oldObj_,
            BuyerStoreAreaUserTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(BuyerStoreAreaUserTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<BuyerStoreAreaUserTmpHolder> selectAreaUserTmpByAreaOid(
            BigDecimal areaOid) throws Exception
    {
        if (areaOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
        param.setAreaOid(areaOid);

        return mapper.select(param);
    }


    @Override
    public List<BuyerStoreAreaUserTmpHolder> selectAreaUserTmpByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
        param.setUserOid(userOid);

        return mapper.select(param);
    }


    @Override
    public BuyerStoreAreaUserTmpHolder selectByUserOidAndAreaOid(
            BigDecimal userOid, BigDecimal areaOid) throws Exception
    {
        if (userOid == null || areaOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
        param.setUserOid(userOid);
        param.setAreaOid(areaOid);
        List<BuyerStoreAreaUserTmpHolder> list = mapper.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        return list.get(0);
    }

}
