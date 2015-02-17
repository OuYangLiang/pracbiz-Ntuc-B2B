package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;

public interface AllowedAccessCompanyTmpService extends
        BaseService<AllowedAccessCompanyTmpHolder>,
        DBActionService<AllowedAccessCompanyTmpHolder>
{
    public List<AllowedAccessCompanyTmpHolder> selectByUserOid(BigDecimal userOid)
            throws Exception;


    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid)
            throws Exception;
}
