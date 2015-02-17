//*****************************************************************************
//
// File Name       :  TradingPartnerService.java
// Date Created    :  Sep 4, 2012
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Sep 4, 2012 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.holder.extension.TradingPartnerExHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public interface TradingPartnerService extends
    BaseService<TradingPartnerHolder>, DBActionService<TradingPartnerHolder>,
    PaginatingService<TradingPartnerExHolder>
{
    public List<TradingPartnerExHolder> selectTradingPartnerByGroupOidAndSupplierOid(
        BigDecimal groupOid, BigDecimal supplierOid) throws Exception;
    
    
    public List<TradingPartnerExHolder> selectTradingPartnerBySupplierOid(
            BigDecimal supplierOid) throws Exception;

    
    public List<TradingPartnerExHolder> selectTradingPartnerByTmpGroupOidAndSupplierOid(
        BigDecimal groupOid, BigDecimal supplierOid) throws Exception;

    
    public List<TradingPartnerHolder> selectTradingPartnerByTradingPartnerOids(
        List<BigDecimal> tradingPartnerOids) throws Exception;

    
    public TradingPartnerHolder selectTradingPartnerByKey(
        BigDecimal tradingPartnerOid) throws Exception;

    
    public List<TradingPartnerExHolder> selectTradingPartnerTradingPartnerOids(
        TradingPartnerExHolder record) throws Exception;

    
    public void removeTradingPartner(CommonParameterHolder cp,
        TradingPartnerHolder tp) throws Exception;
    
    
    public TradingPartnerHolder selectByBuyerAndBuyerGivenSupplierCode(
        BigDecimal buyerOid, String buyerGivenSupplierCode) throws Exception;
    
    
    public List<TradingPartnerHolder> selectBySupplierOid(BigDecimal buyerOid)
        throws Exception;

    
    public List<TradingPartnerHolder> selectByBuyerOid(BigDecimal buyerOid)
        throws Exception;
    
    
    public List<TradingPartnerExHolder> selectBySupplierOids(List<BigDecimal> supplierOids)
        throws Exception;
    
}
