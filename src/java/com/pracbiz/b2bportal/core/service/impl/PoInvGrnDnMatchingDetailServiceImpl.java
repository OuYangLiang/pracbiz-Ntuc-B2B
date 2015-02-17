package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.mapper.PoInvGrnDnMatchingDetailMapper;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;

public class PoInvGrnDnMatchingDetailServiceImpl extends
        DBActionServiceDefaultImpl<PoInvGrnDnMatchingDetailHolder> implements
        PoInvGrnDnMatchingDetailService
{
    @Autowired private PoInvGrnDnMatchingDetailMapper mapper;
    
    @Override
    public List<PoInvGrnDnMatchingDetailExHolder> select(
            PoInvGrnDnMatchingDetailExHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void insert(PoInvGrnDnMatchingDetailHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }


    @Override
    public void updateByPrimaryKeySelective(
            PoInvGrnDnMatchingDetailHolder oldObj_,
            PoInvGrnDnMatchingDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }


    @Override
    public void updateByPrimaryKey(PoInvGrnDnMatchingDetailHolder oldObj_,
            PoInvGrnDnMatchingDetailHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }


    @Override
    public void delete(PoInvGrnDnMatchingDetailHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<PoInvGrnDnMatchingDetailExHolder> selectByMatchingOid(
            BigDecimal matchingOid) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingDetailExHolder param = new PoInvGrnDnMatchingDetailExHolder();
        param.setMatchingOid(matchingOid);
        return this.select(param);
    }


    @Override
    public PoInvGrnDnMatchingDetailExHolder selectByMatchingOidAndSeq(
        BigDecimal matchingOid, int seq) throws Exception
    {
        if (matchingOid == null)
        {
            throw new IllegalArgumentException();
        }
        PoInvGrnDnMatchingDetailExHolder param = new PoInvGrnDnMatchingDetailExHolder();
        param.setMatchingOid(matchingOid);
        param.setSeq(seq);
        List<PoInvGrnDnMatchingDetailExHolder> list = this.select(param);
        if (null != list && !list.isEmpty())
        {
            return list.get(0);
        }
        return null;
    }


    @Override
    public void insertDetailList(
            List<PoInvGrnDnMatchingDetailExHolder> detailList) throws Exception
    {
        if (detailList == null || detailList.isEmpty())
        {
            return;
        }
        
        for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
        {
            mapper.insert(detail);
        }
        
    }

}
