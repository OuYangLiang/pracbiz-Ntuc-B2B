package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserTmpMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreUserTmpServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreUserTmpHolder> implements
        BuyerStoreUserTmpService
{
    @Autowired
    private BuyerStoreUserTmpMapper mapper;


    @Override
    public List<BuyerStoreUserTmpHolder> select(BuyerStoreUserTmpHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(BuyerStoreUserTmpHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerStoreUserTmpHolder oldObj_,
            BuyerStoreUserTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreUserTmpHolder oldObj_,
            BuyerStoreUserTmpHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(BuyerStoreUserTmpHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<BuyerStoreUserTmpHolder> selectStoreUserTmpByStoreOid(
            BigDecimal storeOid) throws Exception
    {
        if (storeOid == null)
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectStoreUserTmpByStoreOid(storeOid);
    }


    @Override
    public List<BuyerStoreUserTmpHolder> selectStoreUserTmpByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreUserTmpHolder param = new BuyerStoreUserTmpHolder();
        param.setUserOid(userOid);

        return mapper.select(param);
    }


    @Override
    public BuyerStoreUserTmpHolder selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
        BigDecimal userOid, BigDecimal storeOid) throws Exception
    {
        if (userOid == null || storeOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreUserTmpHolder param = new BuyerStoreUserTmpHolder();
        param.setUserOid(userOid);
        param.setStoreOid(storeOid);
        List<BuyerStoreUserTmpHolder> list = mapper.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
    
}
