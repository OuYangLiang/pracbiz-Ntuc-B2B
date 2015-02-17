//*****************************************************************************
//
// File Name       :  BuyerStoreServiceImpl.java
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

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreAreaUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.BuyerStoreUserTmpHolder;
import com.pracbiz.b2bportal.core.mapper.BuyerStoreAreaMapper;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreAreaUserTmpService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserService;
import com.pracbiz.b2bportal.core.service.BuyerStoreUserTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class BuyerStoreAreaServiceImpl extends
        DBActionServiceDefaultImpl<BuyerStoreAreaHolder> implements
        BuyerStoreAreaService
{
    @Autowired
    private BuyerStoreAreaMapper mapper;
    @Autowired
    private BuyerStoreAreaUserService buyerStoreAreaUserService;
    @Autowired
    private BuyerStoreAreaUserTmpService buyerStoreAreaUserTmpService;
    @Autowired
    private BuyerStoreUserService buyerStoreUserService;
    @Autowired
    private BuyerStoreUserTmpService buyerStoreUserTmpService;


    @Override
    public BuyerStoreAreaHolder selectBuyerStoreAreaByKey(BigDecimal areaOid)
            throws Exception
    {
        if (areaOid == null)
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreAreaHolder param = new BuyerStoreAreaHolder();
        param.setAreaOid(areaOid);

        List<BuyerStoreAreaHolder> rlt = mapper.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        return null;
    }


    @Override
    public void delete(BuyerStoreAreaHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(BuyerStoreAreaHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(BuyerStoreAreaHolder oldObj,
            BuyerStoreAreaHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(BuyerStoreAreaHolder oldObj,
            BuyerStoreAreaHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }


    @Override
    public List<BuyerStoreAreaHolder> select(BuyerStoreAreaHolder param)
            throws Exception
    {
        return mapper.select(param);
    }
    
    
    @Override
    public void assignUserToArea(CommonParameterHolder cp,List<BuyerStoreAreaUserTmpHolder> addList, 
            List<BuyerStoreAreaUserTmpHolder> deleteList) throws Exception
    {
        if (addList != null && !addList.isEmpty())
        {
            for (BuyerStoreAreaUserTmpHolder obj : addList)
            {
                BuyerStoreUserTmpHolder bsu = new BuyerStoreUserTmpHolder();
                bsu.setUserOid(obj.getUserOid());
                buyerStoreUserTmpService.auditDelete(cp, bsu);
                buyerStoreUserService.auditDelete(cp, bsu);
                buyerStoreAreaUserTmpService.auditInsert(cp, obj);
                buyerStoreAreaUserService.auditInsert(cp, obj);
            }
        }
        if (deleteList != null && !deleteList.isEmpty())
        {
            for (BuyerStoreAreaUserTmpHolder obj : deleteList)
            {
                BuyerStoreAreaHolder area = this.selectBuyerStoreAreaByKey(obj.getAreaOid());
                if (area == null)
                {
                    continue;
                }
                BuyerStoreAreaUserTmpHolder param = new BuyerStoreAreaUserTmpHolder();
                param.setAreaOid(obj.getAreaOid());
                param.setUserOid(obj.getUserOid());
                List<BuyerStoreAreaUserHolder> areaUserList = buyerStoreAreaUserService.select(param);
                if (areaUserList == null || areaUserList.isEmpty())//select all 
                {
                    List<BuyerStoreAreaHolder> allAreas = this.selectBuyerStoreAreaByBuyer(area.getBuyerCode());
                    if (allAreas == null || allAreas.isEmpty())
                    {
                        continue;
                    }
                    for (BuyerStoreAreaHolder holder : allAreas)
                    {
                        if (holder.getAreaOid().equals(obj.getAreaOid()))
                        {
                            allAreas.remove(holder);
                            break;
                        }
                    }
                    
                    List<BuyerStoreAreaUserTmpHolder> result = this.convertToBuyerStoreAreaUserTmp(allAreas, obj.getUserOid());
                    
                    param = new BuyerStoreAreaUserTmpHolder();
                    param.setUserOid(obj.getUserOid());
                    buyerStoreAreaUserTmpService.auditDelete(cp, param);
                    buyerStoreAreaUserService.auditDelete(cp, param);
                    for (BuyerStoreAreaUserTmpHolder bsau : result)
                    {
                        buyerStoreAreaUserTmpService.auditInsert(cp, bsau);
                        buyerStoreAreaUserService.auditInsert(cp, bsau);
                    }
                }
                else//normal select
                {
                    buyerStoreAreaUserTmpService.auditDelete(cp, obj);
                    buyerStoreAreaUserService.auditDelete(cp, obj);
                }
                
            }
        }
    }
    
    
    private List<BuyerStoreAreaUserTmpHolder> convertToBuyerStoreAreaUserTmp(List<BuyerStoreAreaHolder> buyerStoreAreaList, BigDecimal userOid)
    {
        List<BuyerStoreAreaUserTmpHolder> result = new ArrayList<BuyerStoreAreaUserTmpHolder>();
        if (buyerStoreAreaList == null)
        {
            return result;
        }
        for (BuyerStoreAreaHolder obj : buyerStoreAreaList)
        {
            BuyerStoreAreaUserTmpHolder holder = new BuyerStoreAreaUserTmpHolder();
            holder.setAreaOid(obj.getAreaOid());
            holder.setUserOid(userOid);
            result.add(holder);
        }
        return result;
    }


    @Override
    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaByBuyer(
            String buyerCode) throws Exception
    {
        if (buyerCode == null || buyerCode.trim().equals(""))
        {
            throw new IllegalArgumentException();
        }

        BuyerStoreAreaHolder param = new BuyerStoreAreaHolder();
        param.setBuyerCode(buyerCode);

        return mapper.select(param);
    }


    @Override
    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectBuyerStoreAreasByUserOid(userOid);
    }


    @Override
    public List<BuyerStoreAreaHolder> selectBuyerStoreAreaFromTmpAreaUserByUserOid(
            BigDecimal userOid) throws Exception
    {
        if (userOid == null)
        {
            throw new IllegalArgumentException();
        }
        return mapper.selectBuyerStoreAreaFromTmpAreaUserByUserOid(userOid);
    }


/*    private Map<BigDecimal, BuyerStoreAreaUserHolder> convertAreaUserToMapUseUserOidAsKey(
            List<BuyerStoreAreaUserHolder> areaUsers)
    {
        Map<BigDecimal, BuyerStoreAreaUserHolder> rlt = new HashMap<BigDecimal, BuyerStoreAreaUserHolder>();
        for (BuyerStoreAreaUserHolder bsau : areaUsers)
        {
            rlt.put(bsau.getUserOid(), bsau);
        }
        return rlt;
    }*/


    /*private Map<BigDecimal, BuyerStoreAreaUserTmpHolder> convertAreaUserTmpToMapUseUserOidAsKey(
            List<BuyerStoreAreaUserTmpHolder> tmpAreaUsers)
    {
        Map<BigDecimal, BuyerStoreAreaUserTmpHolder> rlt = new HashMap<BigDecimal, BuyerStoreAreaUserTmpHolder>();
        for (BuyerStoreAreaUserTmpHolder bsau : tmpAreaUsers)
        {
            rlt.put(bsau.getUserOid(), bsau);
        }
        return rlt;
    }*/


    @Override
    public BuyerStoreAreaHolder selectBuyerStoreAreaByBuyerCodeAndAreaCode(
            String buyerCode, String areaCode) throws Exception
    {
        if (buyerCode == null || buyerCode.isEmpty() || areaCode == null || areaCode.isEmpty())
        {
            throw new IllegalArgumentException();
        }
        BuyerStoreAreaHolder param = new BuyerStoreAreaHolder();
        param.setBuyerCode(buyerCode);
        param.setAreaCode(areaCode);
        List<BuyerStoreAreaHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }

}
