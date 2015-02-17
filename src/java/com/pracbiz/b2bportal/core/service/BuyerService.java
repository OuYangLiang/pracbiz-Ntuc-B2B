package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.UserProfileHolder;

public interface BuyerService extends BaseService<BuyerHolder>,
    DBActionService<BuyerHolder>, PaginatingService<BuyerHolder>
{
    public BuyerHolder selectBuyerByKey(BigDecimal buyerOid) throws Exception;


    public BuyerHolder selectBuyerWithBlobsByKey(BigDecimal buyerOid)
        throws Exception;


    public void deleteBuyer(CommonParameterHolder cp, BigDecimal buyerOid)
        throws Exception;


    public void insertBuyerWithMsgSetting(CommonParameterHolder cp,
        BuyerHolder buyer) throws Exception;

    
    public void updateBuyerWithMsgSetting(CommonParameterHolder cp,
        BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception;

    
    public List<BuyerHolder> selectActiveBuyers() throws Exception;

    
    public List<BuyerHolder> selectBuyerBySupplierOid(BigDecimal supplierOid)
        throws Exception;
    
    public BuyerHolder selectBuyerByBuyerCode(String buyerCode)
        throws Exception;
    
    public BuyerHolder selectBuyerByBuyerCodeWithBLOBs(String buyerCode)
        throws Exception;

    
    public BuyerHolder selectBuyerByMboxId(String mboxId) throws Exception;
    

    public void updateBuyerWithBuyerRule(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception;
    
    
    public void updateBuyerWithBuyerOperation(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception;
    
    
    public void updateBuyerWithBuyerGivenSupplierOperation(CommonParameterHolder cp,
            BuyerHolder oldBuyer, BuyerHolder newBuyer) throws Exception;
    
    
    public List<BuyerHolder> selectWithBLOBs(BuyerHolder param) throws Exception;
    
    
    public void updateByPrimaryKeyWithBLOBs(BuyerHolder oldObj_,
            BuyerHolder newObj_) throws Exception;
    

    public void auditUpdateByPrimaryKeyWithBLOBs(CommonParameterHolder cp,
            BuyerHolder oldObj_, BuyerHolder newObj_) throws Exception;
    
    
    public List<BuyerHolder> selectBuyerByBuyerOids(List<BigDecimal> buyerOids)
        throws Exception;

    
    public List<BuyerHolder> selectBuyersByGroupOid(BigDecimal groupOid)
        throws Exception;
    
    
    public List<BuyerHolder> selectAvailableBuyersByUserOid(
        UserProfileHolder user) throws Exception;
    
    
    public List<BuyerHolder> selectBuyersBySetOid(BigDecimal setOid)throws Exception;
    
    
    public List<BuyerHolder> selectBuyersByGroupOidAndSupplierOid(
        BigDecimal groupOid, BigDecimal supplierOid) throws Exception;
}

