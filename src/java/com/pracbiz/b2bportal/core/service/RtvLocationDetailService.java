package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author yinchi
 */
public interface RtvLocationDetailService extends DBActionService<RtvLocationDetailHolder>
{
    public List<RtvLocationDetailHolder> selectRtvLocationDetailByRtvOid(BigDecimal rtvOid) throws Exception;
}
