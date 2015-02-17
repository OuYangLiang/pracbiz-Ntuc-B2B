package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public interface BuyerStoreAreaService extends
        BaseService<BuyerStoreAreaHolder>,
        DBActionService<BuyerStoreAreaHolder>
{
    public BuyerStoreAreaHolder selectBuyerStoreAreaByKey(BigDecimal areaOid)
            throws Exception;
    
    
    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaByBuyer(String buyerCode)
            throws Exception;
    

    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaByUserOid(
            BigDecimal userOid) throws Exception;
    
    
    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaFromTmpAreaUserByUserOid(
            BigDecimal userOid) throws Exception;


    public void assignUserToArea(CommonParameterHolder cp,
            List<BuyerStoreAreaUserTmpHolder> addList,
            List<BuyerStoreAreaUserTmpHolder> deleteList) throws Exception;
    

    public BuyerStoreAreaHolder selectBuyerStoreAreaByBuyerCodeAndAreaCode(
            String buyerCode, String areaCode) throws Exception;
}
