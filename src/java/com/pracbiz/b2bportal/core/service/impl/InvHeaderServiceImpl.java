//*****************************************************************************
//
// File Name       :  InvHeaderServiceImpl.java
// Date Created    :  2012-12-11
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-11 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.DBActionServiceDefaultImpl;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.extension.InvSummaryHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;
import com.pracbiz.b2bportal.core.mapper.InvHeaderMapper;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class InvHeaderServiceImpl extends
    DBActionServiceDefaultImpl<InvHeaderHolder> implements InvHeaderService, CoreCommonConstants, ApplicationContextAware
{
    private ApplicationContext ctx;
    
    @Autowired
    private InvHeaderMapper mapper;
    @Autowired 
    private PoHeaderService poHeaderService;
    
    
    @Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
        this.ctx = ctx;
    }
    
    
    private InvHeaderService getMeBean()
    {
        return ctx.getBean("invHeaderService", InvHeaderService.class);
    }
    

    @Override
    public InvHeaderHolder selectInvHeaderByKey(BigDecimal invOid)
        throws Exception
    {
        if (invOid == null)
        {
            throw new IllegalArgumentException();
        }

        InvHeaderHolder parameter = new InvHeaderHolder();
        parameter.setInvOid(invOid);

        List<InvHeaderHolder> rlts = mapper.select(parameter);

        if (rlts != null && !rlts.isEmpty())
        {
            return rlts.get(0);
        }

        return null;
    }

    @Override
    public int getCountOfSummary(MsgTransactionsExHolder param)
        throws Exception
    {
        if(!(param instanceof InvSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getCountOfSummary((InvSummaryHolder)param);
    }

    @Override
    public List<MsgTransactionsExHolder> getListOfSummary(
        MsgTransactionsExHolder param) throws Exception
    {
        if(!(param instanceof InvSummaryHolder))
        {
            throw new IllegalArgumentException();
        }
        
        return mapper.getListOfSummary((InvSummaryHolder)param);
    }

    @Override
    public void delete(InvHeaderHolder oldObj_) throws Exception
    {
        mapper.delete(oldObj_);

    }

    @Override
    public void insert(InvHeaderHolder newObj_) throws Exception
    {
        mapper.insert(newObj_);

    }

    @Override
    public void updateByPrimaryKey(InvHeaderHolder oldObj_,
        InvHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKey(newObj_);

    }

    @Override
    public void updateByPrimaryKeySelective(InvHeaderHolder oldObj_,
        InvHeaderHolder newObj_) throws Exception
    {
        mapper.updateByPrimaryKeySelective(newObj_);

    }

    @Override
    public List<InvHeaderHolder> select(InvHeaderHolder param) throws Exception
    {
        return mapper.select(param);
    }


    @Override
    public InvHeaderHolder selectInvHeaderByPoOidAndStoreCode(BigDecimal poOid,
            String shipToCode) throws Exception
    {
        if(poOid == null)
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setPoOid(poOid);
        param.setShipToCode(shipToCode);
        param.setInvStatus(InvStatus.SUBMIT);
        List<InvHeaderHolder> rlt = this.select(param);
        if(rlt == null || rlt.isEmpty())
        {
            return null;
        }
        return rlt.get(0);
    }


    @Override
    public InvHeaderHolder selectInvHeaderByPoOid(BigDecimal poOid)
            throws Exception
    {
        if(poOid == null)
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setPoOid(poOid);
        param.setInvStatus(InvStatus.SUBMIT);
        List<InvHeaderHolder> rlt = this.select(param);
        if(rlt == null || rlt.isEmpty())
        {
            return null;
        }
        return rlt.get(0);
    }
    
    
    @Override
    public List<InvHeaderHolder> selectInvHeadersByPoOid(BigDecimal poOid)
            throws Exception
            {
        if(poOid == null)
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setPoOid(poOid);
        return select(param);
    }

    @Override
    public List<InvHeaderHolder> selectInvToGenerateDn(
            BigDecimal buyerOid, int maxBufferDays, int minBufferDays) throws Exception
    {
        if(buyerOid == null)
        {
            throw new IllegalArgumentException();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("buyerOid", buyerOid);
        map.put("maxBufferDays", maxBufferDays);
        map.put("minBufferDays", minBufferDays);
        return mapper.selectInvToGenerateDn(map);
    }

    @Override
    public InvHeaderHolder selectInvHeaderByInvNo(String invNo)
            throws Exception
    {
        if (invNo == null || invNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setInvNo(invNo);
        List<InvHeaderHolder> result = this.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        return result.get(0);
    }
    

    @Override
    public InvHeaderHolder selectEffectiveInvHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo,
            String storeCode) throws Exception
    {
        if (buyerCode == null || buyerCode.trim().isEmpty()
                || buyerSupplierCode == null
                || buyerSupplierCode.trim().isEmpty() || poNo == null
                || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setBuyerCode(buyerCode);
        param.setSupplierCode(buyerSupplierCode);
        param.setPoNo(poNo);
        param.setShipToCode(storeCode);
        List<InvHeaderHolder> invHeaders = select(param);
        if (invHeaders == null || invHeaders.isEmpty())
        {
            return null;
        }
        for (InvHeaderHolder header : invHeaders)
        {
            if (!(header.getInvStatus().equals(InvStatus.VOID) 
                    || header.getInvStatus().equals(InvStatus.VOID_OUTDATED)))
            {
                return header;
            }
        }
        return null;
    }

    @Override
    public List<InvHeaderHolder> selectInvHeaderByBuyerSupplierPoNoAndStore(
            String buyerCode, String buyerSupplierCode, String poNo,
            String storeCode) throws Exception
    {
        if (buyerCode == null || buyerCode.trim().isEmpty()
                || buyerSupplierCode == null
                || buyerSupplierCode.trim().isEmpty() || poNo == null
                || poNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        InvHeaderHolder param = new InvHeaderHolder();
        param.setBuyerCode(buyerCode);
        param.setSupplierCode(buyerSupplierCode);
        param.setPoNo(poNo);
        param.setShipToCode(storeCode);
        return select(param);
    }

    @Override
    public InvHeaderHolder selectEffectiveInvHeaderByInNo(BigDecimal buyerOid,
        String buyerSupplierCode, String invNo) throws Exception
    {
        if(buyerOid == null || buyerSupplierCode == null
            || buyerSupplierCode.trim().isEmpty() || invNo == null
            || invNo.trim().isEmpty())
        {
            throw new IllegalArgumentException();
        }
        
        InvHeaderHolder param = new InvHeaderHolder();
        param.setInvNo(invNo);
        param.setBuyerOid(buyerOid);
        param.setSupplierCode(buyerSupplierCode);
        
        List<InvHeaderHolder> result = mapper.select(param);
        if (result == null || result.isEmpty())
        {
            return null;
        }
        
        for (InvHeaderHolder rlt : result)
        {
            if (InvStatus.NEW.equals(rlt.getInvStatus()) || InvStatus.SUBMIT.equals(rlt.getInvStatus()))
            {
                return rlt;
            }
        }
        
        return null;
    }

    
    @Override
    public void voidInvoice(BigDecimal invOid, CommonParameterHolder cp) throws Exception
    {
        InvHeaderHolder oldInv = selectInvHeaderByKey(invOid);
        
        //recover po status to new, amended or partial invoiced
        List<PoHeaderHolder> pos = poHeaderService.selectPoHeadersByPoNo(oldInv.getBuyerOid(), oldInv.getPoNo(), oldInv.getSupplierCode());
        PoHeaderHolder oldObj = poHeaderService.selectPoHeaderByKey(oldInv.getPoOid());
        PoHeaderHolder newObj = new PoHeaderHolder();
        BeanUtils.copyProperties(oldObj, newObj);
        
        List<InvHeaderHolder> invs = selectInvHeadersByPoOid(oldInv.getPoOid());
        if (invs.size() > 1)
        {
            newObj.setPoStatus(PoStatus.PARTIAL_INVOICED);
        }
        else if (invs.size() == 1)
        {
            if (pos.size() == 1)
            {
                newObj.setPoStatus(PoStatus.NEW);
            }
            if (pos.size() > 1)
            {
                newObj.setPoStatus(PoStatus.AMENDED);
            }
        }
        poHeaderService.auditUpdateByPrimaryKey(cp, oldObj, newObj);
        
        //change invoice status to void
        InvHeaderHolder newInv = new InvHeaderHolder();
        BeanUtils.copyProperties(oldInv, newInv);
        newInv.setInvStatus(InvStatus.VOID);
        this.getMeBean().auditUpdateByPrimaryKey(cp, oldInv, newInv);
    }


    @Override
    public List<InvSummaryHolder> selectAllRecordToExport(InvSummaryHolder param)
        throws Exception
    {
        return mapper.selectAllRecordToExport(param);
    }

}
