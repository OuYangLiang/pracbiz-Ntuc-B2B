package com.pracbiz.b2bportal.core.eai.file.validator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;

public abstract class CnFileValidator extends FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(CnFileValidator.class);
    @Autowired private CnHeaderService cnHeaderService;
    @Autowired private PoHeaderService poHeaderService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        log.info(":::: Start to validate logical for CN");
        List<String> errorMessage = new ArrayList<String>();
        CnDocMsg cnDocMsg = (CnDocMsg)docMsg;
        
        CnHolder cn = cnDocMsg.getData();
        CnHeaderHolder cnHeader = cn.getHeader();
        
        CnHeaderHolder oldCnHeader = cnHeaderService.selectEffectiveCnHeaderByKey(docMsg.getReceiverOid(), docMsg.getSenderCode(), cnHeader.getCnNo());
        
        if (oldCnHeader != null)
        {
            errorMessage.add("CN No [" + oldCnHeader.getCnNo() + "] has been used before.");
            return errorMessage;
        }
        
        List<PoHeaderHolder> poHeaders = poHeaderService.selectPoHeadersByPoNoBuyerCodeAndSupplierCode(cnHeader.getPoNo(), 
                docMsg.getReceiverCode(), docMsg.getSenderCode());
        
        if (poHeaders == null || poHeaders.isEmpty())
        {
            errorMessage.add("Reference PoNo [" + cnHeader.getPoNo() + "] does not exist in portal.");
            return errorMessage;
        }
        
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg.getReceiverOid(), cnHeader.getPoNo(), docMsg.getSenderCode());
        if (poHeader.getPoStatus().equals(PoStatus.INVOICED))
        {
            errorMessage.add("Reference PoNo [" + cnHeader.getPoNo() + "] has been invoiced.");
            return errorMessage;
        }
        if (poHeader.getPoStatus().equals(PoStatus.CREDITED))
        {
            errorMessage.add("Reference PoNo [" + cnHeader.getPoNo() + "] has been credited.");
            return errorMessage;
        }
        
        log.info(":::: End to validate logical for CN");
        return errorMessage;
    }
}
