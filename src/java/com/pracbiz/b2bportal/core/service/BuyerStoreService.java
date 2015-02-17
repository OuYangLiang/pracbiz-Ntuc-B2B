//*****************************************************************************
//
// File Name       :  BuyerStore.java
// Date Created    :  2013-1-14
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2011-07-01 10:56:27 +0800 (周五, 01 七月 2011) $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface BuyerStoreService extends BaseService<BuyerStoreHolder>,
        DBActionService<BuyerStoreHolder>, PaginatingService<BuyerStoreHolder>
{
    public BuyerStoreHolder selectBuyerStoreByStoreOid(BigDecimal storeOid)
            throws Exception;


    public List<BuyerStoreHolder> selectBuyerStoresByAreaOid(BigDecimal areaOid)
            throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyer(String buyerCode)
            throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresByBuyer(String buyerCode)
            throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresByUserOid(BigDecimal userOid)
            throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresByUserOidAndIsWareHouse(BigDecimal userOid, boolean isWareHouse)
            throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOid(BigDecimal userOid)
            throws Exception;


    public void assignUserToStore(CommonParameterHolder cp,
            List<BuyerStoreUserTmpHolder> addStoreUsers,
            List<BuyerStoreUserTmpHolder> deleteStoreUsers) throws Exception;
    
    
    public BuyerStoreHolder selectBuyerStoreByBuyerCodeAndStoreCode(String buyerCode,
            String storeCode) throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerAndIsWareHouse(
        String buyerCode, boolean isWareHouse) throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerCodeAndAreaOidAndIsWareHouse(
        String buyerCode, BigDecimal areaOid, boolean isWareHouse) throws Exception;

    
    public List<BuyerStoreHolder> selectBuyerStoresWithoutAreaByBuyerAndIsWareHouse(
        String buyerCode, boolean isWareHouse) throws Exception;
    
    
    public List<BuyerStoreHolder> selectBuyerStoresFromTmpStoreUserByUserOidAndIsWareHouse(
        BigDecimal userOid, boolean isWareHouse) throws Exception;

    
    public List<BuyerStoreHolder> selectBuyerStoresByBuyerAndIsWareHouseAndAreaOid(
        String buyerCode, boolean isWareHouse, List<BigDecimal> areaOids)
        throws Exception;
    
    
    public byte[] exportExcel(String buyerCode) throws Exception;
    
}
