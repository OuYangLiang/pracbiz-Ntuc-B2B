package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerMsgSettingReportMapper;
import com.pracbiz.b2bportal.core.service.BuyerMsgSettingReportService;

public class BuyerMsgSettingReportServiceImpl extends
        DBActionServiceDefaultImpl<BuyerMsgSettingReportHolder> implements
        BuyerMsgSettingReportService
{
    @Autowired
    BuyerMsgSettingReportMapper mapper;
    
    @Override
    public List<BuyerMsgSettingReportHolder> select(
            BuyerMsgSettingReportHolder param) throws Exception
    {
        return mapper.select(param);
    }

    @Override
    public void insert(BuyerMsgSettingReportHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(
            BuyerMsgSettingReportHolder oldObj_,
            BuyerMsgSettingReportHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    @Override
    public void updateByPrimaryKey(BuyerMsgSettingReportHolder oldObj_,
            BuyerMsgSettingReportHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    @Override
    public void delete(BuyerMsgSettingReportHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public BuyerMsgSettingReportHolder selectByKey(BigDecimal buyerOid,
            String msgType, String subType) throws Exception
    {
        if (buyerOid == null || msgType == null || subType == null
                || msgType.trim().isEmpty() || subType.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        BuyerMsgSettingReportHolder param = new BuyerMsgSettingReportHolder();
        param.setBuyerOid(buyerOid);
        param.setMsgType(msgType);
        param.setSubType(subType);
        List<BuyerMsgSettingReportHolder> list = select(param);
        
        if (list == null || list.isEmpty())
        {
            return null;
        }
        
        return list.get(0);
    }

}
