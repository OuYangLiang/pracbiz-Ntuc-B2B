package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;

public interface GiDetailService extends BaseService<GiDetailHolder>,
        DBActionService<GiDetailHolder>
{
    public List<GiDetailHolder> selectByGiOid(BigDecimal giOid) throws Exception;
    
    
    public List<GiDetailHolder> selectGiDetailByKey(BigDecimal giOid)
        throws Exception;
}
