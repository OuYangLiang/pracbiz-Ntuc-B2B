package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerRuleHolder;

public interface BuyerRuleService extends BaseService<BuyerRuleHolder>,
    DBActionService<BuyerRuleHolder>
{
    public List<BuyerRuleHolder> selectByBuyerOid(BigDecimal buyerOid)
        throws Exception;
    

    public BuyerRuleHolder selectByBuyerOidRuleOid(BigDecimal buyerOid,
            BigDecimal ruleOid) throws Exception;
}
