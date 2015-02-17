//*****************************************************************************
//
// File Name       :  ItemBarcodeServiceImpl.java
// Date Created    :  Dec 3, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 3, 2013 10:05:44 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.ItemBarcodeHolder;
import com.pracbiz.b2bportal.core.mapper.ItemBarcodeMapper;
import com.pracbiz.b2bportal.core.service.ItemBarcodeService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class ItemBarcodeServiceImpl  extends DBActionServiceDefaultImpl<ItemBarcodeHolder> implements ItemBarcodeService
{
    @Autowired ItemBarcodeMapper mapper;

    @Override
    public void delete(ItemBarcodeHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
        
    }
    

    @Override
    public void insert(ItemBarcodeHolder newObj) throws Exception
    {
        mapper.insert(newObj);
        
    }
    

    @Override
    public void updateByPrimaryKey(ItemBarcodeHolder oldObj,
        ItemBarcodeHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
        
    }
    

    @Override
    public void updateByPrimaryKeySelective(ItemBarcodeHolder oldObj,
        ItemBarcodeHolder newObj) throws Exception
    {
       mapper.updateByPrimaryKeySelective(newObj);
        
    }
    

    @Override
    public int deleteItemBarcodeByBuyerOid(BigDecimal buyerOid) throws Exception
    {
        return mapper.deleteItemByBuyerOid(buyerOid);
    }
    
    
    @Override
    public int deleteItemBarcodeByItemOid(BigInteger itemOid) throws Exception
    {
        if (itemOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        ItemBarcodeHolder itemBarcode = new ItemBarcodeHolder();
        itemBarcode.setItemOid(itemOid);
        return mapper.delete(itemBarcode);
    }
    
}
