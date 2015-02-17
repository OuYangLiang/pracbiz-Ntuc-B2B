package com.pracbiz.b2bportal.core.eai.file.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.service.PoHeaderService;

public abstract class PocnFileValidator extends FileValidator
{
    @Autowired  private PoHeaderService poHeaderService;
    
    @Override
    protected List<String> validateLogic(DocMsg docMsg)
            throws Exception
    {
        List<String> rlt = new ArrayList<String>();
        
        PoHeaderHolder header = poHeaderService.selectEffectivePoHeaderByPoNo(docMsg
            .getSenderOid(), docMsg.getRefNo(), docMsg.getReceiverCode());
        
        if (null == header)
        {
            rlt.add("Reference PoNo [" + docMsg.getRefNo() + "] does not exist in portal.");
            return rlt;
        }
        if (header.getPoStatus().equals(PoStatus.INVOICED) || header.getPoStatus().equals(PoStatus.PARTIAL_INVOICED))
        {
            rlt.add("Reference PoNo [" + docMsg.getRefNo() + "] has been invoiced.");
            return rlt;
        }
        if (header.getPoStatus().equals(PoStatus.CREDITED))
        {
            rlt.add("Reference PoNo [" + docMsg.getRefNo() + "] has been credited.");
            return rlt;
        }
        if (header.getPoStatus().equals(PoStatus.CANCELLED) || header.getPoStatus().equals(PoStatus.CANCELLED_INVOICED))
        {
            rlt.add("Reference PoNo [" + docMsg.getRefNo() + "] has been cancelled.");
            return rlt;
        }
        
        return null;
    }

}
