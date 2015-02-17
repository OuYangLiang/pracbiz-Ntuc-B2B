package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface BuyerStoreAreaUserService extends
        BaseService<BuyerStoreAreaUserHolder>,
        DBActionService<BuyerStoreAreaUserHolder>
{
    public List<BuyerStoreAreaUserHolder> selectAreaUserByAreaOid(
            BigDecimal areaOid) throws Exception;


    public List<BuyerStoreAreaUserHolder> selectAreaUserByUserOid(
            BigDecimal userOid) throws Exception;
    
    
    public BuyerStoreAreaUserHolder selectByUserOidAndAreaOid(
            BigDecimal userOid, BigDecimal areaOid) throws Exception;

}
