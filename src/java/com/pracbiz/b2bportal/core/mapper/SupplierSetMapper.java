package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.SupplierSetHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierSetExHolder;

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public interface SupplierSetMapper extends BaseMapper<SupplierSetHolder>,
        DBActionMapper<SupplierSetHolder>, PaginatingMapper<SupplierSetExHolder>
{
    
}
