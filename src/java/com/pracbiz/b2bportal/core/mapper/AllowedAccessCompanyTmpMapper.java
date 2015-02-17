package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;

public interface AllowedAccessCompanyTmpMapper extends
        BaseMapper<AllowedAccessCompanyTmpHolder>,
        DBActionMapper<AllowedAccessCompanyTmpHolder>
{
    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid);
}
