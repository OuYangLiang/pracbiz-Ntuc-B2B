package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.mapper.PoInvGrnDnMatchingGrnMapper;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;

public class PoInvGrnDnMatchingGrnServiceImpl extends
        DBActionServiceDefaultImpl<PoInvGrnDnMatchingGrnHolder> implements
        PoInvGrnDnMatchingGrnService
{
    @Autowired
    private PoInvGrnDnMatchingGrnMapper poInvGrnDnMatchingGrnMapper;
    @Override
    public List<PoInvGrnDnMatchingGrnHolder> select(
            PoInvGrnDnMatchingGrnHolder param) throws Exception
    {
        return poInvGrnDnMatchingGrnMapper.select(param);
    }


    @Override
    public void delete(PoInvGrnDnMatchingGrnHolder oldObj) throws Exception
    {
        poInvGrnDnMatchingGrnMapper.delete(oldObj);
    }


    @Override
    public void insert(PoInvGrnDnMatchingGrnHolder newObj) throws Exception
    {
        poInvGrnDnMatchingGrnMapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(PoInvGrnDnMatchingGrnHolder oldObj,
            PoInvGrnDnMatchingGrnHolder newObj) throws Exception
    {
        poInvGrnDnMatchingGrnMapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(PoInvGrnDnMatchingGrnHolder oldObj,
            PoInvGrnDnMatchingGrnHolder newObj) throws Exception
    {
        poInvGrnDnMatchingGrnMapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public List<PoInvGrnDnMatchingGrnHolder> selectByMatchOid(
            BigDecimal matchingOid) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingGrnHolder param = new PoInvGrnDnMatchingGrnHolder();
        param.setMatchingOid(matchingOid);
        return poInvGrnDnMatchingGrnMapper.select(param);
    }
    
    @Override
    public int selectUnclosedMatchingForDisputeGrn(int toleranceDays, BigDecimal grnOid)
        throws Exception
    {
        
        if (grnOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("toleranceDays", toleranceDays);
        map.put("grnOid", grnOid);
        
        return poInvGrnDnMatchingGrnMapper.selectUnclosedMatchingForDisputeGrn(map);
    }
    

}
