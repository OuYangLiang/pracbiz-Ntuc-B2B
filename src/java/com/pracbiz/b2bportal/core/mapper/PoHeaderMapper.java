package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoSummaryHolder;

public interface PoHeaderMapper extends BaseMapper<PoHeaderHolder>,
        DBActionMapper<PoHeaderHolder>,PaginatingMapper<MsgTransactionsExHolder>
{
    public List<PoHeaderHolder> selectBuyerLocalPoHeaderNotInPoInvGrnDnMatching();
    
    
    public List<PoHeaderHolder> selectPoHeaderWhichIsNotFullyInPoInvGrnDnMatching(BigDecimal buyerOid);
    
    
    public List<BaseHolder> getListOfSummaryQuickPo(PoSummaryHolder param);
    
    
    public List<PoHeaderHolder> selectPoHeaderToGenerateBatchInvoice(Map<String, Object> map);
    
    
    public List<PoSummaryHolder> selectAllRecordToExport(PoSummaryHolder param);
}
