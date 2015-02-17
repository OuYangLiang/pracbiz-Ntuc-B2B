//*****************************************************************************
//
// File Name       :  EbxmlInvFileParser.java
// Date Created    :  Sep 24, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Sep 24, 2013 9:38:19 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.ebxml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;




import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;




import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.ebxml.InvDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.InvFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class EbxmlInvFileValidator extends InvFileValidator 
{
    private static final Logger log = LoggerFactory.getLogger(EbxmlInvFileValidator.class);
    public static final String EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
    public static final String EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS="yyyy-MM-ddTHH:mm:ss";
    public static final String EBXML_FILE_DATE_FORMAT_YYYYMMDD="yyyy-MM-dd";
    
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private InvDocFileHandler ebxmlInvDocFileHandler;
    @Autowired private CustomAppConfigHelper appConfig;
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)throws Exception
    {
        List<String> errorMessages = null;
        InputStream inputStream = null;
        try
        {
            log.info(":::: Start to validator Ebxml Inv");
            errorMessages =  new ArrayList<String>();
            try
            {
                inputStream = new ByteArrayInputStream(input);
                EbxmlParseHelper.getInstance().validateEbxml(inputStream, getXSDSchema());
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
                errorMessages.add(" :::: Use the schema (XSD) validation XML file is failure." + e.getMessage());
                return errorMessages;
            }
            inputStream = new ByteArrayInputStream(input);
            Document document = FileParserUtil.getInstance().build(inputStream);
           
            validatorHeader(document, errorMessages);
            if (!errorMessages.isEmpty())
            {
                return errorMessages;
            }
            
            validatorDetail(document, errorMessages);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        log.info(":::: End to validator Ebxml Inv");
        return errorMessages;
    
    }

    //*******************
    //private methods
    //*******************
    private void validatorHeader(Document document, List<String> errorMessage)
    {
        String result = null;
        
        int maxInvNoLength = getMaxInvNoLength();
        String invNo = EbxmlParseHelper.getInstance().obtainValueFrom(document, "requestForPaymentIdentification.uniqueCreatorIdentification");
        FieldContentValidator validatorInvNo = null;
        validatorInvNo = new EmptyValidator();
        validatorInvNo = new LengthValidator(maxInvNoLength, validatorInvNo);
        validatorInvNo = new RegexValidator(validatorInvNo, validationConfig.getPattern(VLD_PTN_KEY_INVOICE_NO));
        FileParserUtil.getInstance().addError(errorMessage, validatorInvNo.validate(invNo, getPrefixForHeader() + "[requestForPaymentIdentification.uniqueCreatorIdentification]"));
        
        String creationDate = document.getRootElement().getAttributeValue("creationDate");
        if (creationDate != null)
        {
            creationDate = creationDate.replaceAll("T", " ");
        }
        FieldContentValidator validatorCreationDate = null;
        validatorCreationDate = new EmptyValidator();
        validatorCreationDate = new DateValidator(EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS, validatorCreationDate);
        result = validatorCreationDate.validate(creationDate, getPrefixForHeader() + "[creationDate]");
        if (result != null)
        {
            errorMessage.add("[creationDate is invalid, the correct format is "+EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS +" or "+ EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS +"]" );
        }
        
        String poNo = EbxmlParseHelper.getInstance().obtainValueFrom(document, "orderIdentification.referenceIdentification");
        FieldContentValidator validatorPoNo = null;
        validatorPoNo = new EmptyValidator();
        validatorPoNo = new LengthValidator(20, validatorPoNo);
        FileParserUtil.getInstance().addError(errorMessage, validatorPoNo.validate(poNo, getPrefixForHeader() + "[orderIdentification.referenceIdentification]"));
        
        String poDate = EbxmlParseHelper.getInstance().obtainValueFrom(document, "orderIdentification.referenceDateOnly");
        if (poDate != null)
        {
            poDate = poDate.replaceAll("T", " ");
        }
        FieldContentValidator validatorPoDate = null;
        validatorPoDate = new EmptyValidator();
        validatorPoDate = new DateValidator(EBXML_FILE_DATE_FORMAT_YYYYMMDD, validatorPoDate);
        result = validatorPoDate.validate(poDate, getPrefixForHeader() + "[orderIdentification.referenceDateOnly]");
        if (result != null)
        {
            errorMessage.add("[orderIdentification.referenceDateOnly is invalid, the correct format is "+EBXML_FILE_DATE_FORMAT_YYYYMMDD +" ]" );
        }
//        FileParserUtil.getInstance().addError(errorMessage, validatorPoDate.validate(poDate, getPrefixForHeader() + "[orderIdentification.referenceDateOnly]"));
        
        String deliveryDate = EbxmlParseHelper.getInstance().obtainValueFrom(document, "deliveryNote.referenceDateOnly");
        if (deliveryDate != null)
        {
            deliveryDate = deliveryDate.replaceAll("T", " ");
        }
        result = validatorPoDate.validate(deliveryDate, getPrefixForHeader() + "[deliveryNote.referenceDateOnly]");
        if (result != null)
        {
            errorMessage.add("[deliveryNote.referenceDateOnly is invalid, the correct format is "+EBXML_FILE_DATE_FORMAT_YYYYMMDD +"]" );
        }
//        FileParserUtil.getInstance().addError(errorMessage, validatorPoDate.validate(deliveryDate, getPrefixForHeader() + "[deliveryNote.referenceDateOnly]"));
        
        String buyerName = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.name");
        FieldContentValidator validatorBuyerName = null;
        validatorBuyerName = new LengthValidator(100, validatorBuyerName);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerName.validate(buyerName, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.name]"));
        
        String buyerAddr1 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressOne");
        FieldContentValidator validatorBuyerAddr1 = null;
        validatorBuyerAddr1 = new LengthValidator(100, validatorBuyerAddr1);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerAddr1.validate(buyerAddr1, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.streetAddressOne]"));
        
        String buyerAddr2 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressTwo");
        FieldContentValidator validatorBuyerAddr2 = null;
        validatorBuyerAddr2 = new LengthValidator(100, validatorBuyerAddr2);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerAddr2.validate(buyerAddr2, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.streetAddressTwo]"));
        
        String buyerAddr3 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressThree");
        FieldContentValidator validatorBuyerAddr3 = null;
        validatorBuyerAddr3 = new LengthValidator(100, validatorBuyerAddr3);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerAddr3.validate(buyerAddr3, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.streetAddressThree]"));
        
        String buyerAddr4 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.streetAddressFour");
        FieldContentValidator validatorBuyerAddr4 = null;
        validatorBuyerAddr4 = new LengthValidator(100, validatorBuyerAddr4);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerAddr4.validate(buyerAddr4, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.streetAddressFour]"));
        
        String buyerCity = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.city");
        FieldContentValidator validatorBuyerCity = null;
        validatorBuyerCity = new LengthValidator(50, validatorBuyerCity);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerCity.validate(buyerCity, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.city]"));
        
        String buyerState = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.state");
        FieldContentValidator validatorBuyerState = null;
        validatorBuyerState = new LengthValidator(50, validatorBuyerState);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerState.validate(buyerState, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.state]"));
        
        String buyerCountryCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.countryISOCode");
        FieldContentValidator validatorBuyerCountryCode = null;
        validatorBuyerCountryCode = new LengthValidator(2, validatorBuyerCountryCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerCountryCode.validate(buyerCountryCode, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.countryISOCode]"));
        
        String buyerPostalCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "buyer.partyInformation.nameAndAddress.postalCode");
        FieldContentValidator validatorBuyerPostalCode = null;
        validatorBuyerPostalCode = new LengthValidator(15, validatorBuyerPostalCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorBuyerPostalCode.validate(buyerPostalCode, getPrefixForHeader() + "[buyer.partyInformation.nameAndAddress.postalCode]"));
        
        String supplierName = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.name");
        FieldContentValidator validatorSupplierName = null;
        validatorSupplierName = new LengthValidator(100, validatorSupplierName);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierName.validate(supplierName, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.name]"));
        
        String supplierAddr1 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressOne");
        FieldContentValidator validatorSupplierAddr1 = null;
        validatorSupplierAddr1 = new LengthValidator(100, validatorSupplierAddr1);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.streetAddressOne]"));
        
        String supplierAddr2 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressTwo");
        FieldContentValidator validatorSupplierAddr2 = null;
        validatorSupplierAddr2 = new LengthValidator(100, validatorSupplierAddr2);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.streetAddressTwo]"));
        
        String supplierAddr3 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressThree");
        FieldContentValidator validatorSupplierAddr3 = null;
        validatorSupplierAddr3 = new LengthValidator(100, validatorSupplierAddr3);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.streetAddressThree]"));
        
        String supplierAddr4 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.streetAddressFour");
        FieldContentValidator validatorSupplierAddr4 = null;
        validatorSupplierAddr4 = new LengthValidator(100, validatorSupplierAddr4);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.streetAddressFour]"));
        
        String supplierCity = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.city");
        FieldContentValidator validatorSupplierCity = null;
        validatorSupplierCity = new LengthValidator(50, validatorSupplierCity);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierCity.validate(supplierCity, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.city]"));
       
        String supplierState = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.state");
        FieldContentValidator validatorSupplierState = null;
        validatorSupplierState = new LengthValidator(50, validatorSupplierState);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierState.validate(supplierState, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.state]"));
       
        String supplierCountryCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.countryISOCode");
        FieldContentValidator validatorSupplierCountryCode = null;
        validatorSupplierCountryCode = new LengthValidator(2, validatorSupplierCountryCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierCountryCode.validate(supplierCountryCode, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.countryISOCode]"));
       
        String supplierPostalCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.nameAndAddress.postalCode");
        FieldContentValidator validatorSupplierPostalCode = null;
        validatorSupplierPostalCode = new LengthValidator(15, validatorSupplierPostalCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "[seller.partyInformation.nameAndAddress.postalCode]"));
       
        String supplierBizRegNo = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.bizRegNo");
        FieldContentValidator validatorSupplierBizRegNo = null;
        validatorSupplierBizRegNo = new LengthValidator(50, validatorSupplierBizRegNo);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierBizRegNo.validate(supplierBizRegNo, getPrefixForHeader() + "[extension.bizRegNo]"));
       
        String supplierVatRegNo = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.partyTaxInformation.taxRegistrationNumber");
        FieldContentValidator validatorSupplierVatRegNo = null;
        validatorSupplierVatRegNo = new LengthValidator(50, validatorSupplierVatRegNo);
        FileParserUtil.getInstance().addError(errorMessage, validatorSupplierVatRegNo.validate(supplierVatRegNo, getPrefixForHeader() + "[seller.partyInformation.partyTaxInformation.taxRegistrationNumber]"));
       
        String shipToCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.store.@code");
        FieldContentValidator validatorShipToCode = null;
        validatorShipToCode = new LengthValidator(20, validatorShipToCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToCode.validate(shipToCode, getPrefixForHeader() + "[extension.store.@code]"));
       
        String shipToName = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.store");
        FieldContentValidator validatorShipToName = null;
        validatorShipToName = new LengthValidator(100, validatorShipToName);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToName.validate(shipToName, getPrefixForHeader() + "[extension.store]"));
       
        String shipToAddr1 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address1");
        FieldContentValidator validatorShipToAddr1 = null;
        validatorShipToAddr1 = new LengthValidator(100, validatorShipToAddr1);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToAddr1.validate(shipToAddr1, getPrefixForHeader() + "[extension.storeInformation.address1]"));
       
        String shipToAddr2 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address2");
        FieldContentValidator validatorShipToAddr2 = null;
        validatorShipToAddr2 = new LengthValidator(100, validatorShipToAddr2);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToAddr2.validate(shipToAddr2, getPrefixForHeader() + "[extension.storeInformation.address2]"));
       
        String shipToAddr3 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address3");
        FieldContentValidator validatorShipToAddr3 = null;
        validatorShipToAddr3 = new LengthValidator(100, validatorShipToAddr3);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToAddr3.validate(shipToAddr3, getPrefixForHeader() + "[extension.storeInformation.address3]"));
       
        String shipToAddr4 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.address4");
        FieldContentValidator validatorShipToAddr4 = null;
        validatorShipToAddr4 = new LengthValidator(100, validatorShipToAddr4);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToAddr4.validate(shipToAddr4, getPrefixForHeader() + "[extension.storeInformation.address4]"));
       
        String shipToCity = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.city");
        FieldContentValidator validatorShipToCity = null;
        validatorShipToCity = new LengthValidator(50, validatorShipToCity);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToCity.validate(shipToCity, getPrefixForHeader() + "[extension.storeInformation.city]"));
       
        String shipToState = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.state");
        FieldContentValidator validatorShipToState = null;
        validatorShipToState = new LengthValidator(50, validatorShipToState);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToState.validate(shipToState, getPrefixForHeader() + "[extension.storeInformation.state]"));
       
        String shipToCtryCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.ctryCode");
        FieldContentValidator validatorShipToCtryCode = null;
        validatorShipToCtryCode = new LengthValidator(2, validatorShipToCtryCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToCtryCode.validate(shipToCtryCode, getPrefixForHeader() + "[extension.storeInformation.ctryCode]"));
       
        String shipToPostalCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.storeInformation.postalCode");
        FieldContentValidator validatorShipToPostalCode = null;
        validatorShipToPostalCode = new LengthValidator(15, validatorShipToPostalCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorShipToPostalCode.validate(shipToPostalCode, getPrefixForHeader() + "[extension.storeInformation.postalCode]"));
       
        String payTermCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.creditTerms.@code");
        FieldContentValidator validatorPayTermCode = null;
        validatorPayTermCode = new LengthValidator(20, validatorPayTermCode);
        FileParserUtil.getInstance().addError(errorMessage, validatorPayTermCode.validate(payTermCode, getPrefixForHeader() + "[extension.creditTerms.@code]"));
       
        String payTermDesc = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.creditTerms");
        FieldContentValidator validatorPayTermDesc = null;
        validatorPayTermDesc = new LengthValidator(100, validatorPayTermDesc);
        FileParserUtil.getInstance().addError(errorMessage, validatorPayTermDesc.validate(payTermDesc, getPrefixForHeader() + "[extension.creditTerms]"));
       
        String payInstruct = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.payInstruct");
        FieldContentValidator validatorPayInstruct = null;
        validatorPayInstruct = new LengthValidator(100, validatorPayInstruct);
        FileParserUtil.getInstance().addError(errorMessage, validatorPayInstruct.validate(payInstruct, getPrefixForHeader() + "[extension.payInstruct]"));
        
        String additionalDiscountAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "allowanceCharge.monetaryAmountOrPercentage.amount");
        FieldContentValidator validatorAdditionalDiscAmount = null;
        validatorAdditionalDiscAmount = new NoSpaceValidator(validatorAdditionalDiscAmount);
        validatorAdditionalDiscAmount = new NumberValidator(validatorAdditionalDiscAmount);
        validatorAdditionalDiscAmount = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validatorAdditionalDiscAmount);
        FileParserUtil.getInstance().addError(errorMessage, validatorAdditionalDiscAmount.validate(additionalDiscountAmount, getPrefixForHeader() + "[allowanceCharge.monetaryAmountOrPercentage.amount]"));
        
        String additionalDiscountPercent = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.discountPercent");
        FieldContentValidator validatorAdditionalDiscPercent = null;
        validatorAdditionalDiscPercent = new NoSpaceValidator(validatorAdditionalDiscPercent);
        validatorAdditionalDiscPercent = new NumberValidator(validatorAdditionalDiscPercent);
        validatorAdditionalDiscPercent = new MaxValidator(new BigDecimal(
            "99.99"), validatorAdditionalDiscPercent);
        FileParserUtil.getInstance().addError(errorMessage, validatorAdditionalDiscPercent.validate(additionalDiscountPercent, getPrefixForHeader() + "[extension.discountPercent]"));
        
        String invAmountNoVat = EbxmlParseHelper.getInstance().obtainValueFrom(document, "netAmount.amount");
        FieldContentValidator validatorInvAmountNoVat = null;
        validatorInvAmountNoVat = new EmptyValidator();
        validatorInvAmountNoVat = new NoSpaceValidator(validatorInvAmountNoVat);
        validatorInvAmountNoVat = new NumberValidator(validatorInvAmountNoVat);
        validatorInvAmountNoVat = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validatorInvAmountNoVat);
        FileParserUtil.getInstance().addError(errorMessage, validatorInvAmountNoVat.validate(invAmountNoVat, getPrefixForHeader() + "[netAmount.amount]"));
        
        String vatAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.partyTaxInformation.taxAmount.amount");
        FieldContentValidator validatorVatAmount = null;
        validatorVatAmount = new EmptyValidator();
        validatorVatAmount = new NoSpaceValidator(validatorVatAmount);
        validatorVatAmount = new NumberValidator(validatorVatAmount);
        validatorVatAmount = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validatorVatAmount);
        FileParserUtil.getInstance().addError(errorMessage, validatorVatAmount.validate(vatAmount, getPrefixForHeader() + "[seller.partyInformation.partyTaxInformation.taxAmount.amount]"));
        
        String invAmountWithVat = EbxmlParseHelper.getInstance().obtainValueFrom(document, "totalAmount.amount");
        FieldContentValidator validatorInvAmountWithVat = null;
        validatorInvAmountWithVat = new EmptyValidator();
        validatorInvAmountWithVat = new NoSpaceValidator(validatorInvAmountWithVat);
        validatorInvAmountWithVat = new NumberValidator(validatorInvAmountWithVat);
        validatorInvAmountWithVat = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validatorInvAmountWithVat);
        FileParserUtil.getInstance().addError(errorMessage, validatorInvAmountWithVat.validate(invAmountWithVat, getPrefixForHeader() + "[totalAmount.amount]"));
       
        String vatRate = EbxmlParseHelper.getInstance().obtainValueFrom(document, "seller.partyInformation.partyTaxInformation.taxPercent");
        FieldContentValidator validatorVatRate = null;
        validatorVatRate = new EmptyValidator();
        validatorVatRate = new NoSpaceValidator(validatorVatRate);
        validatorVatRate = new NumberValidator(validatorVatRate);
        validatorVatRate = new MaxValidator(new BigDecimal(
            "99.99"), validatorVatRate);
        FileParserUtil.getInstance().addError(errorMessage, validatorVatRate.validate(vatRate, getPrefixForHeader() + "[seller.partyInformation.partyTaxInformation.taxPercent]"));
       
        String invRemarks = EbxmlParseHelper.getInstance().obtainValueFrom(document, "remarks");
        FieldContentValidator validatorInvRemarks = null;
        validatorInvRemarks = new LengthValidator(500, validatorInvRemarks);
        FileParserUtil.getInstance().addError(errorMessage, validatorInvRemarks.validate(invRemarks, getPrefixForHeader() + "[remarks]"));
        
        String footerLine1 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=1]");
        FieldContentValidator validatorFooterLine1 = null;
        validatorFooterLine1 = new LengthValidator(100, validatorFooterLine1);
        FileParserUtil.getInstance().addError(errorMessage, validatorFooterLine1.validate(footerLine1, getPrefixForHeader() + "[extension.footer.line[number=1]]"));
        
        
        String footerLine2 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=2]");
        FieldContentValidator validatorFooterLine2 = null;
        validatorFooterLine2 = new LengthValidator(100, validatorFooterLine2);
        FileParserUtil.getInstance().addError(errorMessage, validatorFooterLine2.validate(footerLine2, getPrefixForHeader() + "[extension.footer.line[number=2]]"));
        
        String footerLine3 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=3]");
        FieldContentValidator validatorFooterLine3 = null;
        validatorFooterLine3 = new LengthValidator(100, validatorFooterLine3);
        FileParserUtil.getInstance().addError(errorMessage, validatorFooterLine3.validate(footerLine3, getPrefixForHeader() + "[extension.footer.line[number=3]]"));
        
        String footerLine4 = EbxmlParseHelper.getInstance().obtainValueFrom(document, "extension.footer.line[number=4]");
        FieldContentValidator validatorFooterLine4 = null;
        validatorFooterLine4 = new LengthValidator(100, validatorFooterLine4);
        FileParserUtil.getInstance().addError(errorMessage, validatorFooterLine4.validate(footerLine4, getPrefixForHeader() + "[extension.footer.line[number=4]]"));
        
    }
    
    private void validatorDetail(Document document, List<String> errorMessage)
    {

        List<Element> lineItems = document.getRootElement().getChildren("lineItem");
        BigDecimal netAmounts = BigDecimal.ZERO;
        
        boolean validateTotalNetAmount = false;
        for (int i = 0; i < lineItems.size(); i++)
        {
            String buyerItemCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.alternateTradeItemIdentification[type=BUYER_ASSIGNED].value");
            FieldContentValidator validatorBuyerItemCode = null;
            validatorBuyerItemCode = new EmptyValidator();
            validatorBuyerItemCode = new LengthValidator(20, validatorBuyerItemCode);
            FileParserUtil.getInstance().addError(errorMessage, validatorBuyerItemCode.validate(buyerItemCode, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").tradeItemIdentification.alternateTradeItemIdentification[type=BUYER_ASSIGNED].value]"));
            
            String supplierItemCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=SUPPLIER_ASSIGNED].value");
            FieldContentValidator validatorSupplierItemCode = null;
            validatorSupplierItemCode = new LengthValidator(20, validatorSupplierItemCode);
            FileParserUtil.getInstance().addError(errorMessage, validatorSupplierItemCode.validate(supplierItemCode, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=SUPPLIER_ASSIGNED].value]"));
            
            String barcode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=BUYER_ASSIGNED].value");
            FieldContentValidator validatorBarcode = null;
            validatorBarcode = new LengthValidator(20, validatorBarcode);
            FileParserUtil.getInstance().addError(errorMessage, validatorBarcode.validate(barcode, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").tradeItemIdentification.additionalTradeItemIdentification[type=BUYER_ASSIGNED].value]"));
           
            String itemDesc = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.descriptionShort.description.text");
            FieldContentValidator validatorItemDesc = null;
            validatorItemDesc = new LengthValidator(100, validatorItemDesc);
            FileParserUtil.getInstance().addError(errorMessage, validatorItemDesc.validate(itemDesc, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradeItemDescription.descriptionShort.description.text]"));
           
            String brand = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradeItemDescription.brandName");
            FieldContentValidator validatorBrand = null;
            validatorBrand = new LengthValidator(20, validatorBrand);
            FileParserUtil.getInstance().addError(errorMessage, validatorBrand.validate(brand, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradeItemDescription.brandName]"));
           
            String colourCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorCodeValue");
            FieldContentValidator validatorColourCode = null;
            validatorColourCode = new LengthValidator(20, validatorColourCode);
            FileParserUtil.getInstance().addError(errorMessage, validatorColourCode.validate(colourCode, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorCodeValue]"));
           
            String colourDesc = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorDescription.description.text");
            FieldContentValidator validatorColourDesc = null;
            validatorColourDesc = new LengthValidator(50, validatorColourDesc);
            FileParserUtil.getInstance().addError(errorMessage, validatorColourDesc.validate(colourDesc, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemColorDescription.colorDescription.description.text]"));
           
            String sizeCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.sizeCodeValue");
            FieldContentValidator validatorSizeCode = null;
            validatorSizeCode = new LengthValidator(20, validatorSizeCode);
            FileParserUtil.getInstance().addError(errorMessage, validatorSizeCode.validate(sizeCode, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.sizeCodeValue]"));
           
            String sizeDesc = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.descriptiveSize.description.text");
            FieldContentValidator validatorSizeDesc = null;
            validatorSizeDesc = new LengthValidator(50, validatorSizeDesc);
            FileParserUtil.getInstance().addError(errorMessage, validatorSizeDesc.validate(sizeDesc, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemSizeDescription.descriptiveSize.description.text]"));
           
            String packingFactor = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfInnerPack");
            FieldContentValidator validatorPackingFactor = null;
            validatorPackingFactor = new NoSpaceValidator(validatorPackingFactor);
            validatorPackingFactor = new NumberValidator(validatorPackingFactor);
            validatorPackingFactor = new MaxValidator(new BigDecimal("999999.99"), validatorPackingFactor);
            FileParserUtil.getInstance().addError(errorMessage, validatorPackingFactor.validate(packingFactor, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfInnerPack]"));
           
            String invBaseUnit = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.baseUnit");
            FieldContentValidator validatorInvBaseUnit = null;
            validatorInvBaseUnit = new SpecialCharValidator(validatorInvBaseUnit, false, "P", "U");
            FileParserUtil.getInstance().addError(errorMessage, validatorInvBaseUnit.validate(invBaseUnit, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.baseUnit]"));
           
            String invUom = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.uom");
            FieldContentValidator validatorInvUom = null;
            validatorInvUom = new EmptyValidator();
            validatorInvUom = new LengthValidator(20, validatorInvUom);
            FileParserUtil.getInstance().addError(errorMessage, validatorInvUom.validate(invUom, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.uom]"));
           
            String invQty = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").invoicedQuantity");
            FieldContentValidator validatorInvQty = null;
            validatorInvQty = new EmptyValidator();
            validatorInvQty = new NoSpaceValidator(validatorInvQty);
            validatorInvQty = new NumberValidator(validatorInvQty);
            validatorInvQty = new MaxValidator(new BigDecimal("999999.9999"), validatorInvQty);
            FileParserUtil.getInstance().addError(errorMessage, validatorInvQty.validate(invQty, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").invoicedQuantity]"));
           
            String focBaseUnit = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.focBaseUnit");
            FieldContentValidator validatorFocBaseUnit = null;
            validatorFocBaseUnit = new SpecialCharValidator(validatorFocBaseUnit, false, "P", "U");
            FileParserUtil.getInstance().addError(errorMessage, validatorFocBaseUnit.validate(focBaseUnit, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.focBaseUnit]"));
           
            String focUom = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.focUom");
            FieldContentValidator validatorFocUom = null;
            validatorFocUom = new LengthValidator(20, validatorFocUom);
            FileParserUtil.getInstance().addError(errorMessage, validatorFocUom.validate(focUom, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.focUom]"));
           
            String focQty = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").shiptoDetails.freeQuantity");
            FieldContentValidator validatorFocQty = null;
            validatorFocQty = new NoSpaceValidator(validatorFocQty);
            validatorFocQty = new NumberValidator(validatorFocQty);
            validatorFocQty = new MaxValidator(new BigDecimal("999999.9999"), validatorFocQty);
            FileParserUtil.getInstance().addError(errorMessage, validatorFocQty.validate(focQty, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").shiptoDetails.freeQuantity]"));
           
            String unitPrice = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").unitPrice.amount");
            FieldContentValidator validatorUnitPrice = null;
            validatorUnitPrice = new NoSpaceValidator(validatorUnitPrice);
            validatorUnitPrice = new NumberValidator(validatorUnitPrice);
            validatorUnitPrice = new MaxValidator(new BigDecimal("99999999999.9999"), validatorUnitPrice);
            FileParserUtil.getInstance().addError(errorMessage, validatorUnitPrice.validate(unitPrice, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").unitPrice.amount]"));
           
            String discountAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.discountAmount.amount");
            FieldContentValidator validatorDiscAmount = null;
            validatorDiscAmount = new NoSpaceValidator(validatorDiscAmount);
            validatorDiscAmount = new NumberValidator(validatorDiscAmount);
            validatorDiscAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validatorDiscAmount);
            FileParserUtil.getInstance().addError(errorMessage, validatorDiscAmount.validate(discountAmount, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.discountAmount.amount]"));
           
            String discountPercent = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.discountPercent.amount");
            FieldContentValidator validatorDiscPercent = null;
            validatorDiscPercent = new NoSpaceValidator(validatorDiscPercent);
            validatorDiscPercent = new NumberValidator(validatorDiscPercent);
            validatorDiscPercent = new MaxValidator(new BigDecimal("99.99"), validatorDiscPercent);
            FileParserUtil.getInstance().addError(errorMessage, validatorDiscPercent.validate(discountPercent, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.discountPercent.amount]"));
           
            String netPrice = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").netPrice.amount");
            FieldContentValidator validatorNetPrice = null;
            validatorNetPrice = new NoSpaceValidator(validatorNetPrice);
            validatorNetPrice = new NumberValidator(validatorNetPrice);
            validatorNetPrice = new MaxValidator(new BigDecimal("99999999999.9999"), validatorNetPrice);
            FileParserUtil.getInstance().addError(errorMessage, validatorNetPrice.validate(netPrice, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").netPrice.amount]"));
           
            String itemAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").totalLineAmount.amount");
            FieldContentValidator validatorItemAmount = null;
            validatorItemAmount = new EmptyValidator();
            validatorItemAmount = new NoSpaceValidator(validatorItemAmount);
            validatorItemAmount = new NumberValidator(validatorItemAmount);
            validatorItemAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validatorItemAmount);
            FileParserUtil.getInstance().addError(errorMessage, validatorItemAmount.validate(itemAmount, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").totalLineAmount.amount]"));
           
            String netAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.netAmount.amount");
            FieldContentValidator validatorNetAmount = null;
            validatorNetAmount = new NoSpaceValidator(validatorNetAmount);
            validatorNetAmount = new NumberValidator(validatorNetAmount);
            validatorNetAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validatorNetAmount);
            FileParserUtil.getInstance().addError(errorMessage, validatorNetAmount.validate(netAmount, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.netAmount.amount]"));
           
            String itemSharedCost = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.itemSharedCost.amount");
            FieldContentValidator validatorItemSharedCost = null;
            validatorItemSharedCost = new NoSpaceValidator(validatorItemSharedCost);
            validatorItemSharedCost = new NumberValidator(validatorItemSharedCost);
            validatorItemSharedCost = new MaxValidator(new BigDecimal("99999999999.9999"), validatorItemSharedCost);
            FileParserUtil.getInstance().addError(errorMessage, validatorItemSharedCost.validate(itemSharedCost, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.itemSharedCost.amount]"));
           
            String itemGrossAmount = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.itemGrossAmount.amount");
            FieldContentValidator validatorItemGrossAmount = null;
            validatorItemGrossAmount = new NoSpaceValidator(validatorItemGrossAmount);
            validatorItemGrossAmount = new NumberValidator(validatorItemGrossAmount);
            validatorItemGrossAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validatorItemGrossAmount);
            FileParserUtil.getInstance().addError(errorMessage, validatorItemGrossAmount.validate(itemGrossAmount, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.itemGrossAmount.amount]"));
           
            String itemRemarks = EbxmlParseHelper.getInstance().obtainValueFrom(document, "lineItem(" + i + ").extension.itemRemarks");
            FieldContentValidator validatorItemRemarks = null;
            validatorItemRemarks = new LengthValidator(100, validatorItemRemarks);
            FileParserUtil.getInstance().addError(errorMessage, validatorItemRemarks.validate(itemRemarks, getPrefixForDetail(i + 1) + "[lineItem(" + i + ").extension.itemRemarks]"));
           
            if (netAmount != null)
            {
                validateTotalNetAmount = true;
                netAmounts = netAmounts.add(new BigDecimal(netAmount));
            }
            else
            {
                netAmounts = netAmounts.add(BigDecimal.ZERO);
            }
        }
        
        if (validateTotalNetAmount)
        {
            String invAmountNoVat = EbxmlParseHelper.getInstance().obtainValueFrom(document, "netAmount.amount");
            
            if (invAmountNoVat != null && new BigDecimal(invAmountNoVat).compareTo(netAmounts) != 0)
            {
                FileParserUtil.getInstance().addError(errorMessage, "Sum of the item cost does not equal to invoice total amount.");
            }
        }
    }

    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            ebxmlInvDocFileHandler.readFileContent((InvDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        
    }
    
    private String getXSDSchema()
    {
        return "xsd/ebxml/inv/Invoice.xsd";
    }
    
}
