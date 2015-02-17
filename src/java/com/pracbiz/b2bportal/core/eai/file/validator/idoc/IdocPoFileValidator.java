//*****************************************************************************
//
// File Name       :  IdocRtvFileValidator.java
// Date Created    :  Dec 9, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 9, 2013 12:01:05 PM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.idoc;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.idoc.PoDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.PoFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class IdocPoFileValidator extends PoFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(IdocPoFileValidator.class);
    
    private  String VLD_PTN_KEY_INVOICE_NO = "INV_NO";
    
    @Autowired private PoDocFileHandler idocPoDocFileHandler;
    @Autowired private ValidationConfigHelper validationConfig;

    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
        throws Exception
    {
        List<String> errorMessages = null;
        InputStream inputStream = null;
       
        try
        {
            log.info(":::: Start to validator Idoc Po");
            errorMessages =  new ArrayList<String>();
            
            inputStream = new ByteArrayInputStream(input);
            Document document = FileParserUtil.getInstance().build(inputStream);
            
            validatorHeader(document, errorMessages);
            
//            validatorDetail(document, errorMessages);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        log.info(":::: End to validator Idoc Po");
        return errorMessages;
    }

    
    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            idocPoDocFileHandler.readFileContent((PoDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
       
    }
    
    
    private void validatorHeader(Document document, List<String> errors) throws Exception
    {
        Element root = document.getRootElement().getChild("IDOC");
        List<Element> e1edka1Elements = root.getChildren("E1EDKA1");
        List<Element> e1edk14Elements = root.getChildren("E1EDK14");
        Element poTypeEl = getElementByChildElement(e1edk14Elements, "QUALF", "013");
        
        if (poTypeEl == null)
        {
            errors.add("There is no [Po Type] element.");
        }
        else
        {
            if (poTypeEl.getChild("ORGID") == null || poTypeEl.getChildTextTrim("ORGID").isEmpty())
            {
                errors.add("There is no [Po Type] element.");
            }
        }
        
        String poType = getElementByChildElement(e1edk14Elements, "QUALF", "013").getChildTextTrim("ORGID");

        if (poType.equalsIgnoreCase("ZQO"))
        {
            Element supplierEl = getElementByChildElement(e1edka1Elements, "PARVW", "LF");
            Element invNoEl = supplierEl.getChild("BNAME");
            
            if (invNoEl != null && !invNoEl.getTextTrim().isEmpty())
            {
                String invNo = invNoEl.getTextTrim();
                FieldContentValidator validateInvNo = null;
                validateInvNo = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_INVOICE_NO));
                validateInvNo = new LengthValidator(getMaxInvNoLength(), validateInvNo);
                FileParserUtil.getInstance().addError(errors, validateInvNo.validate(invNo,  "[Invoice No] "));
            }
        }
    }
    
//    private void validatorDetail(Document document, List<String> errors) throws Exception
//    {
//        Element root = document.getRootElement().getChild("IDOC");
//        List<Element> e1edp01Elements = root.getChildren("E1EDP01");
//        boolean allFree = true;
//        for (int i = 0; i < e1edp01Elements.size() ; i++)
//        {
//            Element element = e1edp01Elements.get(i);
//            
//            BigDecimal unitCost = null;
//            Element vpreiElement = element.getChild("VPREI");
//            if (vpreiElement != null)
//            {
//                unitCost = BigDecimalUtil.getInstance().convertStringToBigDecimal(vpreiElement.getTextTrim(), 4);
//            }
//            if (unitCost != null && unitCost.compareTo(BigDecimal.ZERO) != 0)
//            {
//                allFree = false;
//            }
//        }
//        
//        if (allFree)
//        {
//            errors.add("There should be at least one item's unit cost is higher than 0.");
//        }
//    }
    
    
    private Element getElementByChildElement(List<Element> elements, String childNode, String childNodeValue)
    {
        if (elements == null || elements.isEmpty() || childNode == null || childNode.trim().isEmpty() 
                || childNodeValue == null || childNodeValue.trim().isEmpty())
        {
            return null;
        }
        
        for (Element element : elements)
        {
            String qualf = element.getChildTextTrim(childNode);
            
            if (qualf.equals(childNodeValue.trim()))
            {
                return element;
            }
        }
        
        return null;
    }
    
}

