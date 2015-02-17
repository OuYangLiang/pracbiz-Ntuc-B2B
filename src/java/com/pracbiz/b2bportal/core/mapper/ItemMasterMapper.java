package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.ItemMasterHolder;
import com.pracbiz.b2bportal.core.holder.extension.ItemMasterSummaryHolder;

public interface ItemMasterMapper extends BaseMapper<ItemMasterHolder>,
        DBActionMapper<ItemMasterHolder>, PaginatingMapper<ItemMasterSummaryHolder>
{

}
