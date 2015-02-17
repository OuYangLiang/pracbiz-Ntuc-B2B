package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface BuyerStoreUserService extends
        BaseService<BuyerStoreUserHolder>,
        DBActionService<BuyerStoreUserHolder>
{
    public List<BuyerStoreUserHolder> selectStoreUserByStoreOid(
            BigDecimal storeOid) throws Exception;


    public List<BuyerStoreUserHolder> selectStoreUserByUserOid(
            BigDecimal userOid) throws Exception;
    
    public BuyerStoreUserHolder selectStoreUserByStoreOidAndUserOid(
        BigDecimal storeOid, BigDecimal userOid) throws Exception;
    

    public int defineRelationBetweenUserAndStore(BigDecimal storeOid,
            BigDecimal userOid) throws Exception;

}
