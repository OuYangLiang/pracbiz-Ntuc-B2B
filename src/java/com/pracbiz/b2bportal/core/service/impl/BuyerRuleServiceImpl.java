package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerRuleHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerRuleMapper;
import com.pracbiz.b2bportal.core.service.BuyerRuleService;

public class BuyerRuleServiceImpl extends
    DBActionServiceDefaultImpl<BuyerRuleHolder> implements BuyerRuleService
{
    @Autowired
    private BuyerRuleMapper mapper;
    @Override
    public List<BuyerRuleHolder> selectByBuyerOid(BigDecimal buyerOid)
        throws Exception
    {
        if(buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerRuleHolder param = new BuyerRuleHolder();
        param.setBuyerOid(buyerOid);
        return this.select(param);
    }

    @Override
    public List<BuyerRuleHolder> select(BuyerRuleHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void delete(BuyerRuleHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);

    }

    @Override
    public void insert(BuyerRuleHolder newObj) throws Exception
    {
        mapper.insert(newObj);

    }

    @Override
    public void updateByPrimaryKey(BuyerRuleHolder oldObj,
        BuyerRuleHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);

    }

    @Override
    public void updateByPrimaryKeySelective(BuyerRuleHolder oldObj,
        BuyerRuleHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);

    }

    @Override
    public BuyerRuleHolder selectByBuyerOidRuleOid(BigDecimal buyerOid,
            BigDecimal ruleOid) throws Exception
    {
        if(buyerOid == null || ruleOid == null)
        {
            throw new IllegalArgumentException();
        }
        BuyerRuleHolder param = new BuyerRuleHolder();
        param.setBuyerOid(buyerOid);
        param.setRuleOid(ruleOid);
        List<BuyerRuleHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


}
