package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;

public interface AllowedAccessCompanyService extends
        BaseService<AllowedAccessCompanyHolder>,
        DBActionService<AllowedAccessCompanyHolder>
{
    public List<AllowedAccessCompanyHolder> selectByUserOid(BigDecimal userOid)
            throws Exception;


    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid)
            throws Exception;
}
