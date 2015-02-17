package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;

public interface CnHeaderExtendedService extends BaseService<CnHeaderExtendedHolder>,
        DBActionService<CnHeaderExtendedHolder>
{
    public List<CnHeaderExtendedHolder> selectByKey(BigDecimal cnOid) throws Exception;
}
