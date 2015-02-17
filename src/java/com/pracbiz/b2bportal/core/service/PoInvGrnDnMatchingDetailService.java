package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingDetailHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;

public interface PoInvGrnDnMatchingDetailService extends
        BaseService<PoInvGrnDnMatchingDetailExHolder>,
        DBActionService<PoInvGrnDnMatchingDetailHolder>
{
    public List<PoInvGrnDnMatchingDetailExHolder> selectByMatchingOid(
            BigDecimal matchingOid) throws Exception;
    
    public PoInvGrnDnMatchingDetailExHolder selectByMatchingOidAndSeq(
            BigDecimal matchingOid, int seq) throws Exception;
    
    public void insertDetailList(
            List<PoInvGrnDnMatchingDetailExHolder> detailList) throws Exception;
}
