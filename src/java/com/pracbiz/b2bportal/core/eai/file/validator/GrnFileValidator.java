//*****************************************************************************
//
// File Name       :  GrnFileValidator.java
// Date Created    :  Sep 17, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Sep 17, 2013 4:05:41 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public abstract class GrnFileValidator extends FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(GrnFileValidator.class);
    @Autowired private PoHeaderService poHeaderService;
    @Autowired private BusinessRuleService businessRuleService;
    @Autowired private PoService poService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        log.info(":::: Start to validate GRN logical");
        
        List<String> errorMessages = new ArrayList<String>();
        
        Boolean isPreventGRNItemsNotExistInPO = businessRuleService.isPreventGRNItemsNotExistInPO(docMsg.getSenderOid());
        Boolean isPreventGRNItemsLessThanPO = businessRuleService.isPreventGRNItemsLessThanPO(docMsg.getSenderOid());
        Boolean isPreventGRNItemsQtyMoreThanPO = businessRuleService.isPreventGRNItemsQtyMoreThanPO(docMsg.getSenderOid());
        
        if (isPreventGRNItemsNotExistInPO || isPreventGRNItemsLessThanPO || isPreventGRNItemsQtyMoreThanPO)
        {
            GrnHolder grn = ((GrnDocMsg) docMsg).getData();
            PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(grn.getHeader().getBuyerOid(), 
                    grn.getHeader().getPoNo(), grn.getHeader().getSupplierCode());
            
            PoHolder po = null;
            if (poHeader != null)
            {
                po = poService.selectPoByKey(poHeader.getPoOid());
            }
            
            if (isPreventGRNItemsNotExistInPO || isPreventGRNItemsQtyMoreThanPO)
            {
                if (po == null)
                {
                    errorMessages.add("The Corresponding PO[" + grn.getHeader().getPoNo() + "] in tradingPartner  Buyer[" + grn.getHeader().getBuyerCode() + "]-" +
                    		"Supplier[" + grn.getHeader().getSupplierCode() + "] does not exist in datebase.");
                    return errorMessages;
                }
                
                Map<String, PoLocationHolder> locationMap = po.convertLocationsToMap();
                if (!locationMap.containsKey(grn.getHeader().getReceiveStoreCode()))
                {
                    errorMessages.add("The Corresponding PO[" + grn.getHeader().getPoNo() + "] does not has the location [" + grn.getHeader().getReceiveStoreCode()+ "].");
                    return errorMessages;
                }
            }
            
            if (po != null)
            {
                List<PoDetailHolder> poDetailList = po.obtainPoDetailsByStore(grn.getHeader().getReceiveStoreCode());
                
                if (isPreventGRNItemsNotExistInPO)
                {
                    for (GrnDetailHolder detail : grn.getDetails())
                    {
                        boolean exist = false;
                        for (PoDetailHolder poDetail : poDetailList)
                        {
                            if (detail.getBuyerItemCode().equalsIgnoreCase(poDetail.getBuyerItemCode()))
                            {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist)
                        {
                            errorMessages.add("Item [" + detail.getBuyerItemCode()+ "] does not exist in corresponding PO for store[" +grn.getHeader().getReceiveStoreCode() +  "].");
                        }
                    }
                }
                if (isPreventGRNItemsLessThanPO)
                {
                    
                    for (PoDetailHolder poDetail : poDetailList)
                    {
                        boolean exists = false;
                        for (GrnDetailHolder detail : grn.getDetails())
                        {
                            if (detail.getBuyerItemCode().equalsIgnoreCase(poDetail.getBuyerItemCode()))
                            {
                                exists = true;
                                break;
                            }
                        }
                        
                        if (!exists)
                        {
                            errorMessages.add("The items of GRN is less than corresponding PO's for store[" +grn.getHeader().getReceiveStoreCode() +  "].");
                            break;
                        }
                    }
                }
                if (isPreventGRNItemsQtyMoreThanPO)
                {
                    for (GrnDetailHolder detail : grn.getDetails())
                    {
                        boolean exists = false;
                        for (PoDetailHolder poDetail : poDetailList)
                        {
                            if (detail.getBuyerItemCode().equalsIgnoreCase(poDetail.getBuyerItemCode()))
                            {
                                exists = true;
                                if (detail.getReceiveQty().compareTo(poDetail.getOrderQty()) > 0)
                                {
                                    errorMessages.add("The qty of Item [" + detail.getBuyerItemCode()+ "] is more than corresponding PO's qty.");
                                }
                                break;
                            }
                        }
                        if (!exists)
                        {
                            errorMessages.add("Item[" + detail.getBuyerItemCode() + "] of GRN does not exist in corresponding PO for store[" +grn.getHeader().getReceiveStoreCode() +  "].");
                        }
                    }
                }
            }
        }
        
        log.info(":::: End to validate GRN logical");
        return errorMessages;
    }
    
}
