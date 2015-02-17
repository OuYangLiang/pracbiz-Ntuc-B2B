package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.ReadStatusReportHolder;
import com.pracbiz.b2bportal.core.holder.RoleGroupTmpHolder;
import com.pracbiz.b2bportal.core.holder.RoleUserTmpHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierMsgSettingHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.MsgTransactionsMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupMapper;
import com.pracbiz.b2bportal.core.mapper.RoleGroupTmpMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserMapper;
import com.pracbiz.b2bportal.core.mapper.RoleUserTmpMapper;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.SupplierMsgSettingService;
import com.pracbiz.b2bportal.core.service.SupplierService;

public class MsgTransactionsServiceImpl extends
        DBActionServiceDefaultImpl<MsgTransactionsHolder> implements
        MsgTransactionsService
{

    @Autowired
    private MsgTransactionsMapper mapper;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private RoleUserMapper roleUserMapper;
    @Autowired
    private RoleUserTmpMapper roleUserTmpMapper;
    @Autowired
    private RoleGroupMapper roleGroupMapper;
    @Autowired
    private RoleGroupTmpMapper roleGroupTmpMapper;
    @Autowired 
    private SupplierMsgSettingService supplierMsgSettingService;
    
    @Override
    public List<MsgTransactionsHolder> select(MsgTransactionsHolder param)
            throws Exception
    {
        MsgTransactionsExHolder parameter = new MsgTransactionsExHolder();
        BeanUtils.copyProperties(param, parameter);
        
        return mapper.select(parameter);
    }


    @Override
    public void insert(MsgTransactionsHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);
    }

    @Override
    public void updateByPrimaryKeySelective(MsgTransactionsHolder oldObj_,
            MsgTransactionsHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);
        
    }

    @Override
    public void updateByPrimaryKey(MsgTransactionsHolder oldObj_,
            MsgTransactionsHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }


    @Override
    public void delete(MsgTransactionsHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);
    }


    @Override
    public List<MsgTransactionsHolder> selectMsgTransactionsByBuyerOid(
            BigDecimal buyerOid) throws Exception
    {
        if(buyerOid == null)
        {
            return null;
        }
        MsgTransactionsHolder holder = new MsgTransactionsHolder();
        holder.setBuyerOid(buyerOid);
        List<MsgTransactionsHolder> list = this.select(holder);
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list;
    }


    @Override
    public List<MsgTransactionsHolder> selectMsgTransactionsBySupplierOid(
            BigDecimal supplierOid) throws Exception
    {
        if(supplierOid == null)
        {
            return null;
        }
        MsgTransactionsHolder holder = new MsgTransactionsHolder();
        holder.setSupplierOid(supplierOid);
        List<MsgTransactionsHolder> list = this.select(holder);
        if(list == null || list.isEmpty())
        {
            return null;
        }
        return list;
    }



    @Override
    public boolean existMsgTransactionsByKey(BigDecimal docOid)
        throws Exception
    {
        MsgTransactionsHolder rlt = this.selectByKey(docOid);
        
        if (rlt == null)
        {
            return false;
        }
        
        return true;
    }


    @Override
    public MsgTransactionsHolder selectByKey(BigDecimal docOid)
        throws Exception
    {
        if(docOid == null)
        {
            throw new IllegalArgumentException();
        }
        
        MsgTransactionsHolder param = new MsgTransactionsHolder();
        param.setDocOid(docOid);
        
        List<MsgTransactionsHolder> rlt = this.select(param);
        if (rlt != null && !rlt.isEmpty())
        {
            return rlt.get(0);
        }
        
        return null;
    }


    @Override
    public MsgTransactionsHolder selectByMsgTypeAndOriginalFileName(
            String msgType, String originalFilename) throws Exception
    {
        if (msgType == null || msgType.trim().isEmpty()
                || originalFilename == null
                || originalFilename.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        MsgTransactionsHolder param = new MsgTransactionsHolder();
        param.setMsgType(msgType);
        param.setOriginalFilename(originalFilename);
        List<MsgTransactionsHolder> list = this.select(param);
        if (list == null || list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }


    @Override
    public List<ReadStatusReportHolder> selectMsgsForReport(
        MsgTransactionsHolder param) throws Exception
    {
        return mapper.selectMsgsForReport(param);
    }
    
    
    @Override
    public void insertSupplierLiveDate(MsgTransactionsHolder msg,
            List<SupplierHolder> updateSupplierList,
            List<RoleUserTmpHolder> updateSupplierUserRoleList,
            List<RoleGroupTmpHolder> updateSupplierUserGroupRoleList)
            throws Exception
    {
        if (msg != null)
        {
            mapper.insert(msg);
        }
        
        if (updateSupplierList != null && !updateSupplierList.isEmpty())
        {
            for (SupplierHolder supplier : updateSupplierList)
            {
                supplierService.updateByPrimaryKeySelective(null, supplier);
                
                SupplierMsgSettingHolder msgSetting = supplierMsgSettingService.selectByKey(supplier.getSupplierOid(), MsgType.PO.name());
                
                if (msgSetting != null && msgSetting.getRcpsAddrs() != null)
                {
                    supplierMsgSettingService.updateEmptyEmailAddressBySupplierOid(supplier.getSupplierOid(), msgSetting.getRcpsAddrs());
                }
            }
        }
        
        if (updateSupplierUserRoleList != null && !updateSupplierUserRoleList.isEmpty())
        {
            for (RoleUserTmpHolder roleUser : updateSupplierUserRoleList)
            {
                RoleUserTmpHolder param = new RoleUserTmpHolder();
                param.setUserOid(roleUser.getUserOid());
                roleUserMapper.delete(param);
                roleUserTmpMapper.delete(param);
                roleUserMapper.insert(roleUser);
                roleUserTmpMapper.insert(roleUser);
            }
        }
        
        if (updateSupplierUserGroupRoleList != null && !updateSupplierUserGroupRoleList.isEmpty())
        {
            for (RoleGroupTmpHolder roleGroup : updateSupplierUserGroupRoleList)
            {
                RoleGroupTmpHolder param = new RoleGroupTmpHolder();
                param.setGroupOid(roleGroup.getGroupOid());
                roleGroupMapper.delete(param);
                roleGroupTmpMapper.delete(param);
                roleGroupMapper.insert(roleGroup);
                roleGroupTmpMapper.insert(roleGroup);
            }
        }

    }

}
