package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;
import com.pracbiz.b2bportal.core.mapper.ReserveMessageMapper;
import com.pracbiz.b2bportal.core.service.ReserveMessageService;

public class ReserveMessageServiceImpl extends
    DBActionServiceDefaultImpl<ReserveMessageHolder> implements
    ReserveMessageService
{
    @Autowired
    private ReserveMessageMapper mapper;

    @Override
    public List<ReserveMessageHolder> select(ReserveMessageHolder param)
        throws Exception
    {
        return mapper.select(param);
    }

    
    @Override
    public void insert(ReserveMessageHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    
    @Override
    public void updateByPrimaryKeySelective(ReserveMessageHolder oldObj_,
        ReserveMessageHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
    }

    
    @Override
    public void updateByPrimaryKey(ReserveMessageHolder oldObj_,
        ReserveMessageHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);
    }

    
    @Override
    public void delete(ReserveMessageHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }

    @Override
    public int getCountOfSummary(ReserveMessageHolder param) throws Exception
    {
        return mapper.getCountOfSummary(param);
    }

    
    @Override
    public List<ReserveMessageHolder> getListOfSummary(
        ReserveMessageHolder param) throws Exception
    {
        return mapper.getListOfSummary(param);
    }

    
    @Override
    public List<ReserveMessageHolder> selectValidMessages() throws Exception
    {
        return mapper.selectValidMessages();
    }


    @Override
    public ReserveMessageHolder selectReserveMessageByKey(BigDecimal rsrvMsgOid)
            throws Exception
    {
        if(rsrvMsgOid == null)
        {
            return null;
        }
        ReserveMessageHolder obj = new ReserveMessageHolder();
        obj.setRsrvMsgOid(rsrvMsgOid);
        List<ReserveMessageHolder> list = this.select(obj);
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public List<ReserveMessageHolder> selectValidMessageByAnnouncementType(String announcementType)
        throws Exception
    {
        Map<String, String> param = new HashMap<String, String>();
        if (announcementType != null)
        {
            param.put("announcementType", announcementType);
        }
        
        return mapper.selectValidMessagesByAnnouncementType(param);
    }
    
}
