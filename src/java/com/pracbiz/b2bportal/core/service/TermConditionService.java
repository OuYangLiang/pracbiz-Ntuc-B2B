package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.TermConditionHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface TermConditionService extends BaseService<TermConditionHolder>,
    DBActionService<TermConditionHolder>
{
    public List<TermConditionHolder> selectTermsConditionsBySupplierOid(
        BigDecimal supplierOid) throws Exception;

    
    public TermConditionHolder selectDefaultTermConditionBySupplierOid(
        BigDecimal supplierOid) throws Exception;

    
    public TermConditionHolder selectTermsConditionBySupplierOidAndTcCode(
        BigDecimal supplierOid, String tcCode) throws Exception;

    
    public TermConditionHolder selectTermConditionByKey(BigDecimal tcOid)
        throws Exception;
}
