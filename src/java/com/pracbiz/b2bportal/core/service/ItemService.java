//*****************************************************************************
//
// File Name       :  ItemService.java
// Date Created    :  Oct 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Oct 10, 2013 5:11:14 PM $
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
import java.util.Map;


import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.TransactionBatchHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public interface ItemService extends BaseService<ItemHolder>,
    DBActionService<ItemHolder>
{
    public void deleteAndInsertItem(BuyerHolder buyer,
        Map<String, List<String>> fileMap, TransactionBatchHolder transBatch,
        String fileFormat, List<String> successList) throws Exception;


    public void updateItem(BuyerHolder buyer,
        Map<String, List<String>> fileMap, TransactionBatchHolder transBatch,
        String fileFormat, List<String> successList) throws Exception;
    
    public List<ItemHolder> selectClassAndSubclassCodeByBuyerOid(BigDecimal buyerOid) throws Exception;

    
    public int selectCountOfItem(ItemHolder item) throws Exception;
    
    
    public int selectCountOfItemByBuyer(BigDecimal buyerOid) throws Exception;
    
    
    public ItemHolder selectItemByBuyerOidAndBuyerItemCode(BigDecimal buyerOid, String BuyerItemCode)throws Exception;
    
    
    public void deleteItemByBuyerOid(BigDecimal buyerOid)throws Exception;
    
    
    public List<String> selectActiveItemsByUserOid(BigDecimal userOid) throws Exception;
    
    
    public List<ItemHolder> selectBuyerItemCodeByBuyerOid(BigDecimal buyerOid) throws Exception;
}

