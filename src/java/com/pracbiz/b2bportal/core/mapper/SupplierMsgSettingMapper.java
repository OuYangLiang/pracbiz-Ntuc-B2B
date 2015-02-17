package com.pracbiz.b2bportal.core.mapper;


import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;

public interface SupplierMsgSettingMapper extends
    BaseMapper<SupplierMsgSettingHolder>,
    DBActionMapper<SupplierMsgSettingHolder>
{
    void updateEmailAddressBySupplierOid(Map<String, Object> map);
    
    void updateEmptyEmailAddressBySupplierOid(Map<String, Object> map);
}