package com.pracbiz.b2bportal.core.eai.file.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;








import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;








import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;

public abstract class PoFileValidator extends FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(InvFileValidator.class);
    @Autowired private PoHeaderService poHeaderService;
    @Autowired private BusinessRuleService businessRuleService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        log.info(":::: Start to validator logic po");
        List<String> errorMessages = null;
        boolean canBeProcess = true;
        errorMessages = new ArrayList<String>();
        PoDocMsg poDocMsg = (PoDocMsg)docMsg;
        
        String poNo = poDocMsg.getData().getPoHeader().getPoNo();
        
        List<PoHeaderHolder> poHeaders = poHeaderService.selectPoHeadersByPoNoBuyerCodeAndSupplierCode(poNo, 
                docMsg.getSenderCode(), docMsg.getReceiverCode());
        
        
        if (poHeaders != null &&!poHeaders.isEmpty())
        {
            for (PoHeaderHolder poHeader : poHeaders)
            {
                if(poHeader.getPoStatus().equals(PoStatus.INVOICED)
                    || poHeader.getPoStatus().equals(PoStatus.PARTIAL_INVOICED)
                    || poHeader.getPoStatus().equals(PoStatus.CREDITED))
                {
                    canBeProcess = false;
                }
            }
            
            if (!canBeProcess)
            {
                errorMessages.add("Reference PoNo [" + poNo + "] has been invoiced or partial invoiced or credited.");
            }
            
        }
        
        PoHeaderHolder po = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg.getSenderOid(), poNo, docMsg.getReceiverCode());
        if (po != null)
        {
            if (!poDocMsg.getData().getPoHeader().getPoType().equals(po.getPoType()))
            {
                errorMessages.add("There was a [" + po.getPoType() + "] type PO with same PO No [" + poNo + "] in system already.");
            }
        }
        
        boolean isNeedValidateConPoDataLogic = businessRuleService.isNeedValidateConPoDataLogic(poDocMsg.getBuyer().getBuyerOid());
        
        if (isNeedValidateConPoDataLogic)
        {
            // consignment po logic validation
            if (poDocMsg.getData().getPoHeader().getPoType().equals(PoType.CON))
            {
                PoHeaderHolder header = poDocMsg.getData().getPoHeader();
                List<PoDetailHolder> details = poDocMsg.getData().getDetails();
                BigDecimal totalItemCost = BigDecimal.ZERO;
                BigDecimal totalNetCost = BigDecimal.ZERO;
                BigDecimal totalItemSharedCost = BigDecimal.ZERO;
                BigDecimal totalItemGrossCost = BigDecimal.ZERO;
                BigDecimal netAmount = BigDecimal.ZERO;
                
                for (PoDetailHolder detail : details)
                {
                    totalItemCost = totalItemCost.add(detail.getItemCost());
                    totalItemSharedCost = totalItemSharedCost.add(detail.getItemSharedCost());
                    totalItemGrossCost = totalItemGrossCost.add(detail.getItemGrossCost());
                    
                    BigDecimal itemGrossCost = detail.getItemGrossCost();
                    BigDecimal retailDiscAmt = detail.getRetailDiscountAmount();
                    BigDecimal discAmt = detail.getCostDiscountAmount();
                    netAmount = itemGrossCost.subtract(retailDiscAmt).subtract(discAmt);
                    totalNetCost = totalNetCost.add(netAmount);
                    
                    if (detail.getOrderBaseUnit().equalsIgnoreCase("U"))
                    {
                        if (detail.getItemCost().compareTo(detail.getOrderQty().multiply(detail.getUnitCost())) != 0)
                        {
                            errorMessages.add("Detaial # " + detail.getLineSeqNo() + " 's item cost is not correct.");
                        }
                    }
                    else
                    {
                        if (detail.getItemCost().compareTo(detail.getOrderQty().multiply(detail.getPackCost())) != 0)
                        {
                            errorMessages.add("Detaial # " + detail.getLineSeqNo() + " 's item cost is correct.");
                        }
                    }
                    
                    if (netAmount.add(detail.getItemSharedCost()).compareTo(detail.getItemGrossCost()) != 0)
                    {
                        errorMessages.add("Detaial # " + detail.getLineSeqNo() + " 's item gross cost is incorrect.");
                    }
                }
                
                if (totalItemCost.compareTo(header.getTotalCost()) != 0)
                {
                    errorMessages.add("Header's total cost do not matches details. ");
                }
                
                if (totalItemSharedCost.compareTo(header.getTotalSharedCost()) != 0)
                {
                    errorMessages.add("Header's total shared cost do not matches details.");
                }
                
                if (totalItemGrossCost.compareTo(header.getTotalGrossCost()) != 0)
                {
                    errorMessages.add("Header's total gross cost do not matches details.");
                }
                
                if (totalNetCost.compareTo(header.getNetCost()) != 0)
                {
                    errorMessages.add("Header's net cost do not matches details.");
                }
                
                if (header.getPeriodStartDate().compareTo(header.getPeriodEndDate()) >= 0)
                {
                    errorMessages.add("The period start date should be earlier than period end date.");
                }
            }
        
        }
        
        log.info(":::: End to validator logic po");
        return errorMessages;
    
    }
}
