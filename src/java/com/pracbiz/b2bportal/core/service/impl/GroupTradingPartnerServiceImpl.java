//*****************************************************************************
//
// File Name       :  GroupTradingPartnerServiceImpl.java
// Date Created    :  Sep 6, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 6, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerHolder;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerMapper;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class GroupTradingPartnerServiceImpl extends
    DBActionServiceDefaultImpl<GroupTradingPartnerHolder> implements
    GroupTradingPartnerService
{
    @Autowired
    private GroupTradingPartnerMapper mapper;


    @Override
    public List<GroupTradingPartnerHolder> selectGroupTradingPartnerByGroupOid(
        BigDecimal groupOid) throws Exception
    {
        if(groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupTradingPartnerHolder param = new GroupTradingPartnerHolder();
        param.setGroupOid(groupOid);
        return mapper.select(param);
    }
    

    @Override
    public List<GroupTradingPartnerHolder> selectGroupTradingPartnerByTradingPartnerOid(
        BigDecimal tpOid) throws Exception
    {
        if(tpOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupTradingPartnerHolder param = new GroupTradingPartnerHolder();
        param.setTradingPartnerOid(tpOid);
        return mapper.select(param);
    }


    @Override
    public List<GroupTradingPartnerHolder> select(
        GroupTradingPartnerHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public void delete(GroupTradingPartnerHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(GroupTradingPartnerHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }


    @Override
    public void updateByPrimaryKey(GroupTradingPartnerHolder oldObj,
        GroupTradingPartnerHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);
    }


    @Override
    public void updateByPrimaryKeySelective(GroupTradingPartnerHolder oldObj,
        GroupTradingPartnerHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }
}
