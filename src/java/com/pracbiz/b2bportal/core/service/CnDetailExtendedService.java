package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;

public interface CnDetailExtendedService extends BaseService<CnDetailExtendedHolder>,
        DBActionService<CnDetailExtendedHolder>
{
    public List<CnDetailExtendedHolder> selectByCnOid(BigDecimal cnOid) throws Exception;
}
