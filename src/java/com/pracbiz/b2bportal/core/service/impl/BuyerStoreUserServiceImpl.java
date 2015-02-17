package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreUserMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreUserServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreUserHolder> implements
        BuyerStoreUserService
{
    @Autowired
    private BuyerStoreUserMapper mapper;


    @Override
    public List<BuyerStoreUserHolder> select(BuyerStoreUserHolder param)
            throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(BuyerStoreUserHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerStoreUserHolder oldObj_,
            BuyerStoreUserHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreUserHolder oldObj_,
            BuyerStoreUserHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(BuyerStoreUserHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<BuyerStoreUserHolder> selectStoreUserByStoreOid(
            BigDecimal storeOid) throws Exception
    {
        if (storeOid == null)
        {
            throw new IllegalArgumentException();
        }

        return mapper.selectStoreUserByStoreOid(storeOid);
    }


    @Override
    public List<BuyerStoreUserHolder> selectStoreUserByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreUserHolder param = new BuyerStoreUserHolder();
        param.setUserOid(userOid);

        return mapper.select(param);
    }


    @Override
    public BuyerStoreUserHolder selectStoreUserByStoreOidAndUserOid(
        BigDecimal storeOid, BigDecimal userOid) throws Exception
    {
        if (userOid == null || storeOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreUserHolder param = new BuyerStoreUserHolder();
        param.setUserOid(userOid);
        param.setStoreOid(storeOid);

        List<BuyerStoreUserHolder> result = mapper.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result.get(0);
    }


    @Override
    public int defineRelationBetweenUserAndStore(BigDecimal storeOid, BigDecimal userOid)
            throws Exception
    {
        if (storeOid == null || userOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        map.put("storeOid", storeOid);
        map.put("userOid", userOid);
        return mapper.defineRelationBetweenUserAndStore(map);
    }
    
}
