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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GiDocMsg;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.RtvDetailService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public abstract class GiFileValidator extends FileValidator
{
    @Autowired private GiHeaderService giHeaderService;
    @Autowired private DnHeaderService dnHeaderService;
    @Autowired private BusinessRuleService businessRuleService;
    @Autowired private RtvHeaderService rtvHeaderService;
    @Autowired private RtvDetailService rtvDetailService;
    @Autowired private RtvLocationService rtvLocationService;
    @Autowired private RtvLocationDetailService rtvLocationDetailService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        List<String> errorMessage = new ArrayList<String>();
        GiDocMsg giDocMsg = (GiDocMsg)docMsg;
        
        BigDecimal buyerOid = giDocMsg.getSenderOid();
        String giNo = giDocMsg.getRefNo();
        String supplierCode = giDocMsg.getReceiverCode();
        
        GiHeaderHolder header = giHeaderService.selectGiHeaderByGiNo(buyerOid, giNo, supplierCode);
        if (header != null)
        {
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByGiNo(buyerOid, supplierCode, giNo);

            if (dnHeader != null)
            {
                errorMessage.add("DN has been generated for this GI.");
            }
        }
        
        Boolean isPreventGIItemsNotExistInRtv = businessRuleService.isPreventGIItemsNotExistInRtv(buyerOid);
        Boolean isPreventGIItemsLessThanRtv = businessRuleService.isPreventGIItemsLessThanRtv(buyerOid);
        Boolean isPreventGIItemsQtyMoreThanRtv = businessRuleService.isPreventGIItemsQtyMoreThanRtv(buyerOid);
        
        if (isPreventGIItemsNotExistInRtv || isPreventGIItemsLessThanRtv || isPreventGIItemsQtyMoreThanRtv)
        {
            GiHolder gi = ((GiDocMsg) docMsg).getData();
            RtvHeaderHolder rtvHeader = rtvHeaderService.selectRtvHeaderByRtvNo(buyerOid, gi.getGiHeader().getRtvNo(), gi.getGiHeader().getSupplierCode());
            
            if (rtvHeader == null)
            {
                errorMessage.add("The Corresponding RTV [" + gi.getGiHeader().getRtvNo() + "] does not exist in datebase.");
            }
            else
            {
                List<RtvDetailHolder> rtvDetailList = rtvDetailService.selectRtvDetailByKey(rtvHeader.getRtvOid());
                if (isPreventGIItemsNotExistInRtv)
                {
                    for (GiDetailHolder detail : gi.getDetails())
                    {
                        boolean exist = false;
                        for (RtvDetailHolder rtvDetail : rtvDetailList)
                        {
                            if (detail.getBuyerItemCode().equalsIgnoreCase(rtvDetail.getBuyerItemCode()))
                            {
                                exist = true;
                                break;
                            }
                        }
                        if (!exist)
                        {
                            errorMessage.add("Item [" + detail.getBuyerItemCode()+ "] does not exist in corresponding RTV.");
                        }
                    }
                }
                if (isPreventGIItemsLessThanRtv && gi.getDetails().size() < rtvDetailList.size())
                {
                    errorMessage.add("The count of the GI's details is less than the corresponding RTV's details.");
                }
                if (isPreventGIItemsQtyMoreThanRtv)
                {
                    List<RtvLocationHolder> rtvLocs = rtvLocationService.selectRtvLocationByRtvOid(rtvHeader.getRtvOid());
                    List<RtvLocationDetailHolder> locDetails = rtvLocationDetailService.selectRtvLocationDetailByRtvOid(rtvHeader.getRtvOid());
                    for (GiDetailHolder detail : gi.getDetails())
                    {
                        for (RtvDetailHolder rtvDetail : rtvDetailList)
                        {
                            if (detail.getBuyerItemCode().equalsIgnoreCase(rtvDetail.getBuyerItemCode()))
                            {
                                int detailLineSeqNo = rtvDetail.getLineSeqNo();
                                for (RtvLocationHolder loc : rtvLocs)
                                {
                                    if (loc.getLocationCode().equalsIgnoreCase(gi.getGiHeader().getIssuingStoreCode()))
                                    {
                                        int locLineSeqNo = loc.getLineSeqNo();
                                        for (RtvLocationDetailHolder locDetail : locDetails)
                                        {
                                            if (detailLineSeqNo == locDetail.getDetailLineSeqNo()
                                                && locLineSeqNo == locDetail.getLocationLineSeqNo()
                                                && detail.getIssuedQty().compareTo(locDetail.getLocationShipQty()) > 0)
                                            {
                                                errorMessage.add("The qty of Item [" + detail.getBuyerItemCode()+ "] is more than corresponding RTV's qty.");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return errorMessage;
    }
    
}
