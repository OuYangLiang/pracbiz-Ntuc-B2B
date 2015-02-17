package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.ReadStatusReportHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

public interface MsgTransactionsService extends
        BaseService<MsgTransactionsHolder>,
        DBActionService<MsgTransactionsHolder>
{
    public List<MsgTransactionsHolder> selectMsgTransactionsByBuyerOid(
            BigDecimal buyerOid) throws Exception;


    public List<MsgTransactionsHolder> selectMsgTransactionsBySupplierOid(
            BigDecimal supplierOid) throws Exception;
    
    public boolean existMsgTransactionsByKey(BigDecimal docOid)
        throws Exception;
    
    
    public MsgTransactionsHolder selectByKey(BigDecimal docOid) throws Exception;
    
    
    public MsgTransactionsHolder selectByMsgTypeAndOriginalFileName(
            String msgType, String originalFilename) throws Exception;
    
    
    public List<ReadStatusReportHolder> selectMsgsForReport(
        MsgTransactionsHolder param) throws Exception;
    
    
    public void insertSupplierLiveDate(MsgTransactionsHolder msg,
            List<SupplierHolder> updateSupplierList,
            List<RoleUserTmpHolder> updateSupplierUserRoleList,
            List<RoleGroupTmpHolder> updateSupplierUserGroupRoleList)
            throws Exception;
}
