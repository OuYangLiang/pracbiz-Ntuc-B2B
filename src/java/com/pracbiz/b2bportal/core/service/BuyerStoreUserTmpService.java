package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface BuyerStoreUserTmpService extends
        BaseService<BuyerStoreUserTmpHolder>,
        DBActionService<BuyerStoreUserTmpHolder>
{
    public List<BuyerStoreUserTmpHolder> selectStoreUserTmpByStoreOid(
            BigDecimal storeOid) throws Exception;


    public List<BuyerStoreUserTmpHolder> selectStoreUserTmpByUserOid(
            BigDecimal userOid) throws Exception;
    
    
    public BuyerStoreUserTmpHolder selectBuyerStoresFromTmpStoreUserByUserOidAndStoreOid(
        BigDecimal userOid, BigDecimal storeOid) throws Exception;
    
    
}
