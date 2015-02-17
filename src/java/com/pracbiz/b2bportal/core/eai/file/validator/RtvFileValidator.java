package com.pracbiz.b2bportal.core.eai.file.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;

public abstract class RtvFileValidator extends FileValidator
{
    @Autowired RtvHeaderService rtvHeaderService;
    
    @Autowired DnHeaderService dnHeaderService;
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        List<String> errorMessage = new ArrayList<String>();
        RtvDocMsg rtvDocMsg = (RtvDocMsg)docMsg;
        // TODO Auto-generated method stub
        
        BigDecimal buyerOid = rtvDocMsg.getSenderOid();
        String rtvNo = rtvDocMsg.getData().getRtvHeader().getRtvNo();
        String buyerGivenSupplierCode = rtvDocMsg.getReceiverCode();
        
        RtvHeaderHolder rtvHeader = rtvHeaderService.selectRtvHeaderByRtvNo(
            rtvDocMsg.getSenderOid(), rtvNo, buyerGivenSupplierCode);
        
        
        if (rtvHeader != null)
        {
            DnHeaderHolder dnHeader = dnHeaderService.selectDnHeaderByRtvNo(buyerOid, buyerGivenSupplierCode, rtvNo);
            
            if (dnHeader != null)
            {
                errorMessage.add("This RTV has been generator a DN.");
            }
        }
        
        return errorMessage;
    }

}
