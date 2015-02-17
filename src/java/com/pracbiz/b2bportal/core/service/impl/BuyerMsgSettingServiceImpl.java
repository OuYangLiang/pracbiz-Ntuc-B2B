package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerMsgSettingMapper;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingService;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class BuyerMsgSettingServiceImpl extends DBActionServiceDefaultImpl<BuyerMsgSettingHolder>
    implements BuyerMsgSettingService
{
    @Autowired
    private BuyerMsgSettingMapper mapper;
    
    @Override
    public List<BuyerMsgSettingHolder> select(BuyerMsgSettingHolder param)
        throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(BuyerMsgSettingHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(BuyerMsgSettingHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(BuyerMsgSettingHolder oldObj,
        BuyerMsgSettingHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerMsgSettingHolder oldObj,
        BuyerMsgSettingHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public List<BuyerMsgSettingHolder> selectBuyerMsgSettingsByBuyerOid(
        BigDecimal buyerOid) throws Exception
    {
        if(buyerOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerMsgSettingHolder param = new BuyerMsgSettingHolder();
        param.setBuyerOid(buyerOid);
        return mapper.select(param);
    }


    @Override
    public BuyerMsgSettingHolder selectByKey(BigDecimal buyerOid,
        String type) throws Exception
    {
        if(buyerOid == null || null == type || type.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        BuyerMsgSettingHolder param = new BuyerMsgSettingHolder();
        param.setBuyerOid(buyerOid);
        param.setMsgType(type);
        
        List<BuyerMsgSettingHolder> rlt = this.select(param);
        
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }

}
