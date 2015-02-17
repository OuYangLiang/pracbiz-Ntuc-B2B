package com.pracbiz.b2bportal.core.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.core.holder.ModuleHolder;
import com.pracbiz.b2bportal.core.holder.extension.ModuleExHolder;

public interface ModuleMapper extends BaseMapper<ModuleHolder>
{
    List<ModuleExHolder> selectModulesByUser(BigDecimal userOid);

    List<ModuleExHolder> selectModulesByGroup(BigDecimal groupOid);
}