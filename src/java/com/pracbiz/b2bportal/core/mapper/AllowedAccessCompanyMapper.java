package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.AllowedAccessCompanyHolder;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;

public interface AllowedAccessCompanyMapper extends
        BaseMapper<AllowedAccessCompanyHolder>,
        DBActionMapper<AllowedAccessCompanyHolder>
{
    public List<BuyerHolder> selectBuyerByUserOid(BigDecimal userOid);
}
