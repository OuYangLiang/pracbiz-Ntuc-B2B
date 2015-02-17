//*****************************************************************************
//
// File Name       :  InvFileValidator.java
// Date Created    :  Sep 23, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Sep 23, 2013 9:30:38 AM $
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoLocationService;


/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public abstract class InvFileValidator extends FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(InvFileValidator.class);
    public static final String VLD_PTN_KEY_INVOICE_NO = "INV_NO";
    public static final String VLD_PTN_KEY_DELIVERY_NO = "DELIVERY_NO";
    @Autowired private PoHeaderService poHeaderService;
    @Autowired private PoLocationService poLocationService;
    @Autowired private InvHeaderService invHeaderService;
    @Autowired private BusinessRuleService businessRuleService;
    @Autowired private PoDetailService poDetailService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        log.info(":::: Start to validator  logic Inv");
        
        List<String> errorMessages = new ArrayList<String>();
        InvDocMsg invDocMsg = (InvDocMsg)docMsg;
        InvHolder inv = invDocMsg.getData();
        
        InvHeaderHolder oldInvHeader = invHeaderService.selectEffectiveInvHeaderByInNo(docMsg.getReceiverOid(), 
                docMsg.getSenderCode(), inv.getHeader().getInvNo());
        
        if (oldInvHeader != null)
        {
            errorMessages.add("Inv No [" + oldInvHeader.getInvNo() + "] has been used before.");
            return errorMessages;
        }
        
        String poNo = inv.getHeader().getPoNo();
        String shipToCode =  inv.getHeader().getShipToCode();
        
        List<PoHeaderHolder> poHeaders = poHeaderService.selectPoHeadersByPoNoBuyerCodeAndSupplierCode(poNo, 
                docMsg.getReceiverCode(), docMsg.getSenderCode());
        
        if (poHeaders == null || poHeaders.isEmpty())
        {
            errorMessages.add("Reference PoNo [" + poNo + "] does not exist in portal.");
            return errorMessages;
        }
        
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg.getReceiverOid(), poNo, docMsg.getSenderCode());
        if (poHeader.getPoStatus().equals(PoStatus.INVOICED))
        {
            errorMessages.add("Reference PoNo [" + poNo + "] has been invoiced.");
            return errorMessages;
        }
        if (poHeader.getPoStatus().equals(PoStatus.CREDITED))
        {
            errorMessages.add("Reference PoNo [" + poNo + "] has been credited.");
            return errorMessages;
        }
        
        
        if(poHeader.getPoStatus().equals(PoStatus.NEW)
                || poHeader.getPoStatus().equals(PoStatus.PARTIAL_INVOICED)
                || poHeader.getPoStatus().equals(PoStatus.AMENDED))
        {
            List<PoLocationHolder> locations = poLocationService
                    .selectOptionalLocationsByPoOid(poHeader.getPoOid());
            boolean exist = false;
            for(PoLocationHolder location : locations)
            {
                if(location.getLocationCode().equalsIgnoreCase(shipToCode))
                {
                    exist = true;
                    break;
                }
            }
            if (!exist)
            {
                errorMessages.add("Store code [" + shipToCode+ "] of reference PoNo [" + poNo + "] does not exist or has been invoiced.");
                return errorMessages;
            }
        }
        
        Boolean isPreventINVItemsNotExistInPO = businessRuleService.isPreventINVItemsNotExistInPO(docMsg.getReceiverOid());
        if (isPreventINVItemsNotExistInPO)
        {
            List<PoDetailHolder> poDetailList = poDetailService.selectPoDetailsByPoOid(poHeader.getPoOid());
            
            for (InvDetailHolder detail : inv.getDetails())
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
                    errorMessages.add("Item [" + detail.getBuyerItemCode()+ "] does not exist in corresponding PO.");
                }
            }
        }
        
        log.info(":::: End to validator  logic Inv");
        return errorMessages;
    }
    
}
