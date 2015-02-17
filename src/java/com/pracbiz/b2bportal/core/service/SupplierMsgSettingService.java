package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;

public interface SupplierMsgSettingService extends
    BaseService<SupplierMsgSettingHolder>,
    DBActionService<SupplierMsgSettingHolder>
{
    public List<SupplierMsgSettingHolder> selectSupplierMsgSettingBySupplierOid(
        BigDecimal supplierOid) throws Exception;
    
    
    public SupplierMsgSettingHolder selectByKey(BigDecimal supplierOid,
        String type) throws Exception;
    
    
    public void updateEmailAddressBySupplierOid(BigDecimal supplierOid, String email)
            throws Exception;
    
    
    public void updateEmptyEmailAddressBySupplierOid(BigDecimal supplierOid, String email) throws Exception;
}
