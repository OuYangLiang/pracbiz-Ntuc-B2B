package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerMsgSettingReportHolder;

public interface BuyerMsgSettingReportService extends
        BaseService<BuyerMsgSettingReportHolder>,
        DBActionService<BuyerMsgSettingReportHolder>
{
    public BuyerMsgSettingReportHolder selectByKey(BigDecimal buyerOid,
            String msgType, String subType) throws Exception;
}
