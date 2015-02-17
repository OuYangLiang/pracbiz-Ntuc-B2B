package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnSummaryHolder;

public interface DnService extends DBActionService<DnHolder>
{
    public DnHolder selectDnByKey(BigDecimal dnOid) throws Exception;
    
    public void insertDnWithMsgTransaction(CommonParameterHolder cp, DnHolder dn,
            MsgTransactionsHolder msg) throws Exception;


    public void updateDnWithMsgTransaction(CommonParameterHolder cp, DnHolder oldDn, DnHolder dn,
            MsgTransactionsHolder msg) throws Exception;
    
    
    public void saveClose(CommonParameterHolder cp, DnHolder dn, boolean generateNew) throws Exception;
    
    
    public byte[] exportExcel(List<BigDecimal> dnOids, boolean storeFlag) throws Exception;
    
    
    public byte[] exportSummaryExcel(List<DnSummaryHolder> params, boolean storeFlag) throws Exception;
    
    
    public void createDnClassInfo(DnHeaderHolder header, List<DnDetailExHolder> details) throws Exception;
    
}
