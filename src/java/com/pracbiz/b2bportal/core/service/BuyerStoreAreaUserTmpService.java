package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface BuyerStoreAreaUserTmpService extends
        BaseService<BuyerStoreAreaUserTmpHolder>,
        DBActionService<BuyerStoreAreaUserTmpHolder>
{
    public List<BuyerStoreAreaUserTmpHolder> selectAreaUserTmpByAreaOid(
            BigDecimal areaOid) throws Exception;


    public List<BuyerStoreAreaUserTmpHolder> selectAreaUserTmpByUserOid(
            BigDecimal userOid) throws Exception;

    
    public BuyerStoreAreaUserTmpHolder selectByUserOidAndAreaOid(
        BigDecimal userOid, BigDecimal areaOid) throws Exception;
}
