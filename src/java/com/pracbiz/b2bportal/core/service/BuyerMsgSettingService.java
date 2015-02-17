package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface BuyerMsgSettingService extends
    BaseService<BuyerMsgSettingHolder>, DBActionService<BuyerMsgSettingHolder>
{
    public List<BuyerMsgSettingHolder> selectBuyerMsgSettingsByBuyerOid(
        BigDecimal buyerOid) throws Exception;

    public BuyerMsgSettingHolder selectByKey(BigDecimal buyerOid,
        String type) throws Exception;
}
