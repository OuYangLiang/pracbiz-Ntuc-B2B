package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.GiSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface GiHeaderService extends PaginatingService<MsgTransactionsExHolder>,
        DBActionService<GiHeaderHolder>
{
    public List<GiHeaderHolder> selectGiHeadersWithoutDn(BigDecimal buyerOid) throws Exception;
    
    
    public GiHeaderHolder selectGiHeaderByGiNo(BigDecimal buyerOid,
        String giNo, String buyerGivenSupplierCode) throws Exception;
    
    
    public List<GiHeaderHolder> selectGiHeadersByBuyerOidGrnNoAndSupplierCode(
        BigDecimal buyerOid, String giNo, String supplierCode)
        throws Exception;
    
    
    public GiHeaderHolder selectGiHeaderByKey(BigDecimal giOid) throws Exception;
    
    
    public List<GiSummaryHolder> selectAllRecordToExport(GiSummaryHolder param) throws Exception;

}
