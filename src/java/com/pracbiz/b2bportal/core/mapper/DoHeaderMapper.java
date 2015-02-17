package com.pracbiz.b2bportal.core.mapper;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.base.mapper.PaginatingMapper;
import com.pracbiz.b2bportal.core.holder.DoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public interface DoHeaderMapper extends BaseMapper<DoHeaderHolder>,
    DBActionMapper<DoHeaderHolder>, PaginatingMapper<MsgTransactionsExHolder>
{

}
