package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;

public interface ReserveMessageMapper extends BaseMapper<ReserveMessageHolder>,
    DBActionMapper<ReserveMessageHolder>
{
    public List<ReserveMessageHolder> selectValidMessages();
    
    
    int getCountOfSummary(ReserveMessageHolder param);


    List<ReserveMessageHolder> getListOfSummary(ReserveMessageHolder param);
    
    
    public ReserveMessageHolder selectReserveMessageByKey(BigDecimal rsrvMsgOid) throws Exception;
    
    
    public List<ReserveMessageHolder> selectValidMessagesByAnnouncementType(Map<String, String> announcementType) throws Exception;
}