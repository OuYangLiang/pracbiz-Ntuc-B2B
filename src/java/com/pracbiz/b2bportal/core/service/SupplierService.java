package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;

public interface SupplierService extends BaseService<SupplierHolder>,
    DBActionService<SupplierHolder>, PaginatingService<SupplierHolder>
{
    public SupplierHolder selectSupplierByKey(BigDecimal supplierOid)
        throws Exception;

    
    public SupplierHolder selectSupplierByCode(String supplierCode)
        throws Exception;

    
    public SupplierHolder selectSupplierByMboxId(String mboxId)
        throws Exception;

    
    public SupplierHolder selectSupplierWithBlobsByKey(BigDecimal supplierOid)
        throws Exception;

    
    public void deleteSupplier(CommonParameterHolder cp, BigDecimal supplierOid)
        throws Exception;

    
    public List<SupplierHolder> selectSupplierByGroupOidAndBuyerOid(
        BigDecimal groupOid, BigDecimal buyerOid) throws Exception;

    
    public List<SupplierHolder> selectSupplierByTmpGroupOidAndBuyerOid(
        BigDecimal groupOid, BigDecimal buyerOid) throws Exception;

    public List<SupplierHolder> selectSupplierByBuyerOid(BigDecimal buyerOid)
        throws Exception;

    public List<SupplierHolder> selectSupplierBySupplierOids(
        List<BigDecimal> supplierOids) throws Exception;

    
    public void insertSupplierWithMsgSetting(CommonParameterHolder cp,
        SupplierHolder supplier) throws Exception;

    
    public void updateSupplierWithMsgSetting(CommonParameterHolder cp,
        SupplierHolder oldSupplier, SupplierHolder newSupplier)
        throws Exception;

    
    public List<SupplierHolder> selectActiveSuppliers() throws Exception;
    
    
    public List<SupplierHolder> selectWithBLOBs(SupplierHolder param) throws Exception;
    
    
    public void updateByPrimaryKeyWithBLOBs(SupplierHolder oldObj_,
            SupplierHolder newObj_) throws Exception;
    

    public void auditUpdateByPrimaryKeyWithBLOBs(CommonParameterHolder cp,
            SupplierHolder oldObj_, SupplierHolder newObj_) throws Exception;
    
    
    public void insertSupplierMasterBatch(MsgTransactionsHolder msg,
            List<SupplierHolder> newSms, List<SupplierHolder> oldSms,
            List<TradingPartnerHolder> newTps,
            List<TradingPartnerHolder> oldTps, Boolean isSendEmail)
            throws Exception;
    
    
    public List<SupplierHolder> selectSuppliersBySetOid(BigDecimal setOid) throws Exception;
    
    
    public void updateSupplierShared(CommonParameterHolder cp,
            SupplierHolder oldSupplier, SupplierHolder newSupplier)
            throws Exception;
    
    
    public List<SupplierHolder> selectSupplierByBuyerOidAndUserOid(
            BigDecimal buyerOid, BigDecimal currentUserOid) throws Exception;
    
    
    public List<SupplierHolder> selectAvailableSuppliersByUserOid(
            UserProfileHolder user) throws Exception;
    
    
    public void insertNewSupplierFromSupplierMaster(SupplierHolder supplier,
            TradingPartnerHolder tp, List<ControlParameterHolder> msgList,
            Boolean isSendEmail) throws Exception;
    
    
    public void updateOldSupplierFromSupplierMaster(SupplierHolder supplier,
            TradingPartnerHolder tp, boolean flag) throws Exception;
    
    
    public void updateSupplierBySupplierOid(BigDecimal setOid,
        BigDecimal supplierOid)throws Exception;
    
    
    public List<SupplierHolder> selectGrantSuppliersBySupplierSideUser(
        UserProfileHolder user) throws Exception;
}
