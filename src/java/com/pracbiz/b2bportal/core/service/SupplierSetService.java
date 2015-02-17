package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.SupplierSetHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierSetExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public interface SupplierSetService extends BaseService<SupplierSetHolder>,
        DBActionService<SupplierSetHolder>,
        PaginatingService<SupplierSetExHolder>
{
    public void insertSupplierSet(CommonParameterHolder cp,
        SupplierSetExHolder supplierSetEx) throws Exception;


    public SupplierSetHolder selectBySetId(String setId) throws Exception;


    public SupplierSetHolder selectByKey(BigDecimal setOid) throws Exception;


    public void updateSupplierSet(CommonParameterHolder cp,
            SupplierSetExHolder supplierSetEx) throws Exception;
    
    
    public void deleteSupplierSet(CommonParameterHolder cp,
        SupplierSetHolder supplierSetEx)throws Exception;
}
