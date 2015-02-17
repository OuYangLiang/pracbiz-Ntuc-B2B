package com.pracbiz.b2bportal.core.mapper;

import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.BusinessRuleHolder;
import com.pracbiz.b2bportal.core.holder.extension.BusinessRuleExHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public interface BusinessRuleMapper extends BaseMapper<BusinessRuleHolder>,
        DBActionMapper<BusinessRuleHolder>
{
    public List<BusinessRuleHolder> selectRulesByBuyerOidAndFuncGroupAndFuncId(BusinessRuleHolder parameter);
    
    
    public BusinessRuleExHolder selectRulesByBuyerOidAndFuncGroupAndFuncIdAndRuleId(BusinessRuleHolder parameter);
}