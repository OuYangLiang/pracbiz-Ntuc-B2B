//*****************************************************************************
//
// File Name       :  GroupTradingPartnerTmpServiceImpl.java
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
import com.pracbiz.b2bportal.core.holder.GroupTradingPartnerTmpHolder;
import com.pracbiz.b2bportal.core.mapper.GroupTradingPartnerTmpMapper;
import com.pracbiz.b2bportal.core.service.GroupTradingPartnerTmpService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class GroupTradingPartnerTmpServiceImpl extends
    DBActionServiceDefaultImpl<GroupTradingPartnerTmpHolder> implements
    GroupTradingPartnerTmpService
{
    @Autowired
    private GroupTradingPartnerTmpMapper mapper;


    @Override
    public List<GroupTradingPartnerTmpHolder> selectGroupTradingPartnerTmpByGroupOid(
            BigDecimal groupOid) throws Exception
    {
        if (groupOid == null)
        {
            throw new IllegalArgumentException();
        }

        GroupTradingPartnerTmpHolder param = new GroupTradingPartnerTmpHolder();
        param.setGroupOid(groupOid);
        return mapper.select(param);
    }


    @Override
    public void delete(GroupTradingPartnerTmpHolder oldObj) throws Exception
    {
        mapper.delete(oldObj);
    }


    @Override
    public void insert(GroupTradingPartnerTmpHolder newObj) throws Exception
    {
        mapper.insert(newObj);
    }

    
    @Override
    public void updateByPrimaryKey(GroupTradingPartnerTmpHolder oldObj,
        GroupTradingPartnerTmpHolder newObj) throws Exception
    {
        mapper.updateByPrimaryKey(newObj);

    }


    @Override
    public void updateByPrimaryKeySelective(
        GroupTradingPartnerTmpHolder oldObj, GroupTradingPartnerTmpHolder newObj)
        throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj);
    }

}
