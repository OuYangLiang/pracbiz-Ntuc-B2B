package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;

public interface CnDetailService extends BaseService<CnDetailHolder>,
        DBActionService<CnDetailHolder>
{
    public List<CnDetailHolder> selectByCnOid(BigDecimal cnOid) throws Exception;
}
