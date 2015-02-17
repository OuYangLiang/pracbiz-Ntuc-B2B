package com.pracbiz.b2bportal.core.mapper;

import java.util.Map;

import com.pracbiz.b2bportal.base.mapper.BaseMapper;
import com.pracbiz.b2bportal.base.mapper.DBActionMapper;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;

public interface PoInvGrnDnMatchingGrnMapper extends
        BaseMapper<PoInvGrnDnMatchingGrnHolder>,
        DBActionMapper<PoInvGrnDnMatchingGrnHolder>
{
    public int selectUnclosedMatchingForDisputeGrn(Map<String, Object> map);
}