package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.ReserveMessageHolder;

public interface ReserveMessageService extends
    BaseService<ReserveMessageHolder>, DBActionService<ReserveMessageHolder>,
    PaginatingService<ReserveMessageHolder>
{
    public List<ReserveMessageHolder> selectValidMessages() throws Exception;
    
    
    public ReserveMessageHolder selectReserveMessageByKey(BigDecimal rsrvMsgOid) throws Exception;
    
    
    public List<ReserveMessageHolder> selectValidMessageByAnnouncementType(String announcementType) throws Exception;
}
