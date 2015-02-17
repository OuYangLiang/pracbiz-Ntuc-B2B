package com.pracbiz.b2bportal.core.eai.file.validator.ebxml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import org.jdom2.Document;
import org.jdom2.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.ebxml.PoDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.PoFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class EbxmlPoFileValidator extends PoFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(EbxmlPoFileValidator.class);
    private static final String EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS = "yyyy-MM-ddTHH:mm:ss";
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private PoDocFileHandler ebxmlPoDocFileHandler;
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
            throws Exception
    {
        List<String> errorMessages = null;
        InputStream inputStream = null;
       
        try
        {
            log.info(":::: Start to validator Ebxml Po");
            errorMessages =  new ArrayList<String>();
            try
            {
                inputStream = new ByteArrayInputStream(input);
//                EbxmlParseHelper.getInstance().validateEbxml(inputStream, getXSDSchema());
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
            
            validatorHeaderExtend(document, errorMessages);
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
        log.info(":::: End to validator Ebxml Po");
        return errorMessages;
    
    }
    
    public void validatorHeader(Document document, List<String> errors) throws Exception
    {
        String result = null;
        /*po date and action date*/
        String poDate = document.getRootElement().getAttributeValue(
                "creationDate");
        FieldContentValidator validatePoDate = null;
        if (poDate != null && poDate.contains("T"))
        {
            poDate = poDate.replaceAll("T", " ");
            //validatePoDate = new DateValidator(EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS, validatePoDate);
        }
        validatePoDate = new EmptyValidator();
        validatePoDate = new DateValidator(DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS, validatePoDate);
        result = validatePoDate.validate(poDate, "creationDate");
        if (result != null)
        {
            errors.add("creationDate is invalid, the correct format is '"
                + DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS + " or " + EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS);
        }
        
        /*delivery date from*/
        String deliveryDateFrom = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyStartDate");
        FieldContentValidator validateDeliveryDateFrom = null;
        if (deliveryDateFrom != null && deliveryDateFrom.contains("T"))
        {
            deliveryDateFrom = deliveryDateFrom.replaceAll("T", " ");
        }
//        validateDeliveryDate = new EmptyValidator();
        validateDeliveryDateFrom = new DateValidator(DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS, validateDeliveryDateFrom);
        result = validateDeliveryDateFrom.validate(deliveryDateFrom, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyStartDate");
        
        String fromResult = null;
        if (result != null)
        {
            errors.add("lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyStartDate is invalid, the correct format is '"
                + DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS + " or " + EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS);
            fromResult = result;
        }
        
        /*delivery date to*/
        String deliveryDateTo = EbxmlParseHelper.getInstance().obtainValueFrom(
            document, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyEndDate");
        if (deliveryDateTo != null && deliveryDateTo.contains("T"))
        {
            deliveryDateTo = deliveryDateTo.replaceAll("T", " ");
        }
        FieldContentValidator validateDeliveryDateTo = null;
        validateDeliveryDateTo = new DateValidator(DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS, validateDeliveryDateTo);
        result = validateDeliveryDateTo.validate(deliveryDateTo, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyEndDate");
        
        String toResult = null;
        if (result != null)
        {
            errors.add("lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyEndDate is invalid, the correct format is '"
                + DocFileHandler.EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS + " or " + EBXML_FILE_DATE_FORMAT_YYYYMMDDTHHMMSS);
            toResult = result;
        }
        
        if (deliveryDateFrom != null && !deliveryDateFrom.isEmpty()
            && deliveryDateTo != null && !deliveryDateTo.isEmpty()
            && fromResult == null && toResult == null
            && DateUtil.getInstance().convertStringToDate(deliveryDateTo, DateUtil.EBXML_DATE_FORMAT_YYYYMMDDHHMMSS)
            .before(DateUtil.getInstance().convertStringToDate(deliveryDateFrom, DateUtil.EBXML_DATE_FORMAT_YYYYMMDDHHMMSS)))
        {
            errors.add("PartyEndDate should not be earlier than PartyStartDate.");
        }
//        FileParserUtil.getInstance().addError(errors, validateExpiryDate.validate(expiryDate, "lineItem(0).shiptoDetails.shiptoParty.partyInformation.partyDates.partyEndDate"));
        
        /*po no*/
        String poNo = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "typedEntityIdentification.entityIdentification.uniqueCreatorIdentification");
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        FileParserUtil.getInstance().addError(errors, validatePoNo.validate(poNo, "typedEntityIdentification.entityIdentification.uniqueCreatorIdentification"));

        /*buyer country code*/
        String ctryCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document,
                "buyer.partyInformation.nameAndAddress.countryISOcode");
        FieldContentValidator validateBuyerCountryCode = null;
        validateBuyerCountryCode = new LengthValidator(2, validateBuyerCountryCode);
        FileParserUtil.getInstance().addError(errors, validateBuyerCountryCode.validate(ctryCode, "buyer.partyInformation.nameAndAddress.countryISOcode"));
        
        /*buyer name*/
        String buyerName = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "buyer.partyInformation.nameAndAddress.name");
        FieldContentValidator validateBuyerName = null;
        validateBuyerName = new LengthValidator(100, validateBuyerName);
        FileParserUtil.getInstance().addError(errors, validateBuyerName.validate(buyerName, "buyer.partyInformation.nameAndAddress.name"));

        /*supplierCode*/
        String supplierCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "seller.alternatePartyIdentification");
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        validateSupplierCode = new RegexValidator(validateSupplierCode, validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE));
        FileParserUtil.getInstance().addError(errors, validateSupplierCode.validate(supplierCode, "seller.alternatePartyIdentification"));
        
        /*supplier country code*/
        String supCtryCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document,
                "seller.partyInformation.nameAndAddress.countryISOcode");
        FieldContentValidator validateSupplierCountryCode = null;
        validateSupplierCountryCode = new LengthValidator(2, validateSupplierCountryCode);
        FileParserUtil.getInstance().addError(errors, validateSupplierCountryCode.validate(supCtryCode, "seller.partyInformation.nameAndAddress.countryISOcode"));
        
        /*supplier name*/
        String supplierName = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "seller.partyInformation.nameAndAddress.name");
        FieldContentValidator validateSupplierName = null;
        validateSupplierName = new LengthValidator(100, validateSupplierName);
        FileParserUtil.getInstance().addError(errors, validateSupplierName.validate(supplierName, "seller.partyInformation.nameAndAddress.name"));

        String shipToCode = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "shipParty.alternatePartyIdentification");
        if (shipToCode != null
                && !shipToCode.toLowerCase(Locale.getDefault()).contains(
                        "ignore"))
        {
            /*ship to code*/
            FieldContentValidator validateShipToCode = null;
            validateShipToCode = new LengthValidator(20, validateShipToCode);
            FileParserUtil.getInstance().addError(errors, validateShipToCode.validate(shipToCode, "shipParty.alternatePartyIdentification"));

            /*ship to conutry code*/
            String shipToCtryCode = EbxmlParseHelper
                    .getInstance()
                    .obtainValueFrom(document,
                            "shipParty.partyInformation.nameAndAddress.countryISOcode");
            FieldContentValidator validateShipToCountryCode = null;
            validateShipToCountryCode = new LengthValidator(2, validateShipToCountryCode);
            FileParserUtil.getInstance().addError(errors, validateShipToCountryCode.validate(shipToCtryCode, "shipParty.partyInformation.nameAndAddress.countryISOcode"));
            
            /*ship to name*/
            String shipToName = EbxmlParseHelper.getInstance().obtainValueFrom(
                    document, "shipParty.partyInformation.nameAndAddress.name");
            FieldContentValidator validateShipToName = null;
            validateShipToName = new LengthValidator(100, validateShipToName);
            FileParserUtil.getInstance().addError(errors, validateShipToName.validate(shipToName, "shipParty.partyInformation.nameAndAddress.name"));
        }
        
        /*remarks*/
        String remarks = EbxmlParseHelper.getInstance().obtainValueFrom(
                document, "remarks");
        FieldContentValidator validateOrderRemarks = null;
        validateOrderRemarks = new LengthValidator(500, validateOrderRemarks);
        FileParserUtil.getInstance().addError(errors, validateOrderRemarks.validate(remarks, "remarks"));
        
    }

    
    private void validatorDetail(Document document, List<String> errors) throws Exception
    {
        String detailNode = "lineItem";
        List<Element> poItems = document.getRootElement().getChildren(
                detailNode);

        if (poItems == null || poItems.isEmpty())
        {
            errors.add("There is no lineItem elements.");
            return;
        }

        for (int i = 0; i < poItems.size(); i++)
        {
            /*packingFactor*/
            String packingFactor = EbxmlParseHelper.getInstance().obtainValueFrom(document,detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfNextLevelTradeItemWithinInnerPack");
            if (packingFactor != null)
            {
                FieldContentValidator validatePackingFactor = null;
                validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
                validatePackingFactor = new NumberValidator();
                validatePackingFactor = new MaxValidator(BigDecimal.valueOf(999999.99), validatePackingFactor);
                FileParserUtil.getInstance().addError(errors, validatePackingFactor.validate(packingFactor, detailNode
                    + "("
                    + i
                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.tradeItemHierarchy.quantityOfNextLevelTradeItemWithinInnerPack"));
            }
            
            /*unit cost*/
            String unitCost = EbxmlParseHelper.getInstance()
                    .obtainValueFrom(document,
                            detailNode + "(" + i + ").netPrice.amount");
            FieldContentValidator validateUnitCost = null;
            validateUnitCost = new EmptyValidator();
            validateUnitCost = new NoSpaceValidator(validateUnitCost);
            validateUnitCost = new NumberValidator(validateUnitCost);
            validateUnitCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateUnitCost);
            FileParserUtil.getInstance().addError(errors, validateUnitCost.validate(unitCost, detailNode + "(" + i + ").netPrice.amount"));
            
            /*orderQty*/
            String orderQty = EbxmlParseHelper.getInstance().obtainValueFrom(
                    document, detailNode + "(" + i + ").requestedQuantity");
            FieldContentValidator validateOrderQty = null;
            validateOrderQty = new EmptyValidator();
            validateOrderQty = new NoSpaceValidator(validateOrderQty);
            validateOrderQty = new NumberValidator(validateOrderQty);
            validateOrderQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateOrderQty);
            FileParserUtil.getInstance().addError(errors, validateOrderQty.validate(orderQty,  detailNode + "(" + i + ").requestedQuantity"));
            
            /*barCode*/
            String barCode = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.alternateItemIdentification");
            
            if (barCode != null)
            {
                FieldContentValidator validateBarCode = null;
                validateBarCode = new LengthValidator(50, validateBarCode);
                FileParserUtil.getInstance().addError(errors, validateBarCode.validate(barCode,  detailNode
                    + "("
                    + i
                    + ").itemIdentification.alternateItemIdentification"));
            }
            
            /*buyerItemCode*/
            String buyerItemCode = EbxmlParseHelper.getInstance().obtainValueFrom( document, detailNode
                                    + "("
                                    + i
                                    + ").itemIdentification.additionalItemIdentification");
            FieldContentValidator validateBuyerItemCode = null;
            validateBuyerItemCode = new EmptyValidator();
            validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
            FileParserUtil.getInstance().addError(errors, validateBuyerItemCode.validate(buyerItemCode, detailNode
                + "("
                + i
                + ").itemIdentification.additionalItemIdentification"));
            
            /*brand*/
            String brand = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.brandName");
            if (brand != null)
            {
                FieldContentValidator validateBrand = null;
                validateBrand = new LengthValidator(20, validateBrand);
                FileParserUtil.getInstance().addError(errors, validateBrand.validate(brand, detailNode
                    + "("
                    + i
                    + ").itemInformation.tradeItemDescription.brandName"));
            }
            
            /*itemDesc*/
            String itemDesc = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradeItemDescription.descriptionShort.description.text");
            FieldContentValidator validateItemDesc = null;
            validateItemDesc = new EmptyValidator();
            validateItemDesc = new LengthValidator(100, validateItemDesc);
            FileParserUtil.getInstance().addError(errors, validateItemDesc.validate(itemDesc, detailNode
                + "("
                + i
                + ").itemInformation.tradeItemDescription.descriptionShort.description.text"));
            
            /*orderUom*/
            String orderUom = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode
                                    + "("
                                    + i
                                    + ").itemInformation.tradingPartnerNeutralTradeItemInformation.packagingType.packagingTypeCode");
            FieldContentValidator validateOrderUom = null;
            validateOrderUom = new EmptyValidator();
            validateOrderUom = new LengthValidator(20, validateOrderUom);
            FileParserUtil.getInstance().addError(errors, validateOrderUom.validate(orderUom, detailNode
                + "("
                + i
                + ").itemInformation.tradingPartnerNeutralTradeItemInformation.packagingType.packagingTypeCode"));
            
            /*itemCost*/
            String itemCost = EbxmlParseHelper.getInstance()
                    .obtainValueFrom(document,
                            detailNode + "(" + i + ").netAmount.amount");
            FieldContentValidator validateItemCost = null;
            validateItemCost = new EmptyValidator();
            validateItemCost = new NoSpaceValidator(validateItemCost);
            validateItemCost = new NumberValidator(validateItemCost);
            validateItemCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemCost);
            FileParserUtil.getInstance().addError(errors, validateItemCost.validate(itemCost, "itemCost"));
            
            /*focQty*/
            
            String focQty = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode + "(" + i + ").freeQuantity");
            log.info("focQty : " + focQty);
            FieldContentValidator validateFocQty = null;
            validateFocQty = new NoSpaceValidator(validateFocQty);
            validateFocQty = new NumberValidator(validateFocQty);
            validateFocQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateFocQty);
            FileParserUtil.getInstance().addError(errors, validateFocQty.validate(focQty,  detailNode + "(" + i + ").freeQuantity"));
            

            String curr = EbxmlParseHelper.getInstance().obtainValueFrom(document, detailNode + "(" + i
                            + ").netAmount.amount.@currencyISOcode");
            
            if (curr != null && curr.trim().equalsIgnoreCase(""))
            {
                FieldContentValidator validateCurr = null;
                validateCurr = new LengthValidator(255, validateCurr);
                FileParserUtil.getInstance().addError(errors, validateCurr.validate(focQty, detailNode + "(" + i
                    + ").netAmount.amount.@currencyISOcode"));
                
            }

            List<Element> shiptoDetails = poItems.get(i).getChildren("shiptoDetails");
            if (shiptoDetails != null)
            {
                for (int m = 0; m < shiptoDetails.size(); m++)
                {
                    Element shiptoParty = shiptoDetails.get(m).getChild("shiptoParty");
                    Element deliveryQty = shiptoDetails.get(m).getChild("deliveryQuantity");
                    Element freeQuantity = shiptoDetails.get(m).getChild("freeQuantity");
                    
                    String additionalShipToCode = "";
                    Element additionalPartyIdentification = shiptoParty.getChild("additionalPartyIdentification");
                    if (additionalPartyIdentification == null)
                    {
                        additionalShipToCode = EbxmlParseHelper.getInstance().trimString(shiptoParty.getChild("alternatePartyIdentification").getText());
                    }
                    else
                    {
                        additionalShipToCode = EbxmlParseHelper.getInstance().trimString(additionalPartyIdentification.getText());
                    }
                    String locationCode = additionalShipToCode;
                    FieldContentValidator validateLocationCode = null;
                    validateLocationCode = new EmptyValidator();
                    validateLocationCode = new LengthValidator(20, validateLocationCode);
                    FileParserUtil.getInstance().addError(errors, validateLocationCode.validate(locationCode, "additionalPartyIdentification and alternatePartyIdentification"));
                    
                    String locCtryCode = EbxmlParseHelper.getInstance().trimString(shiptoParty
                            .getChild("partyInformation")
                            .getChild("nameAndAddress")
                            .getChild("countryISOcode").getText());
                    FieldContentValidator validateLocCtryCode = null;
                    validateLocCtryCode = new LengthValidator(2, validateLocCtryCode);
                    FileParserUtil.getInstance().addError(errors, validateLocCtryCode.validate(locCtryCode, "shiptoDetails("+ i +").shiptoParty.countryISOcode.partyInformation.nameAndAddress.countryISOcode"));
                    
                    
                    String locName = EbxmlParseHelper.getInstance().trimString(shiptoParty
                            .getChild("partyInformation")
                            .getChild("nameAndAddress").getChild("name")
                            .getText());
                    FieldContentValidator validateLocName = null;
                    validateLocName = new LengthValidator(100, validateLocName);
                    FileParserUtil.getInstance().addError(errors, validateLocName.validate(locName, "shiptoDetails("+ i +").shiptoParty.partyInformation.nameAndAddress.name"));
                
                    String locShipQtyStr = EbxmlParseHelper.getInstance().trimString(deliveryQty.getText());
                    FieldContentValidator validatorLocShipQtyStr= null;
                    validatorLocShipQtyStr = new EmptyValidator();
                    validatorLocShipQtyStr = new NoSpaceValidator(validatorLocShipQtyStr);
                    validatorLocShipQtyStr = new NumberValidator(validatorLocShipQtyStr);
                    validatorLocShipQtyStr = new MaxValidator(BigDecimal.valueOf(999999.9999), validatorLocShipQtyStr);
                    FileParserUtil.getInstance().addError(errors, validatorLocShipQtyStr.validate(locShipQtyStr, "shiptoDetails("+ i +").deliveryQuantity"));
                    
                    if (null != freeQuantity)
                    {
                        String locFocQtyStr = EbxmlParseHelper.getInstance().trimString(freeQuantity.getText());
                        FieldContentValidator validatorLocFocQtyStr = null;
                        validatorLocFocQtyStr = new NoSpaceValidator(validatorLocFocQtyStr);
                        validatorLocFocQtyStr = new NumberValidator(validatorLocFocQtyStr);
                        validatorLocFocQtyStr = new MaxValidator(BigDecimal.valueOf(999999.9999), validatorLocFocQtyStr);
                        FileParserUtil.getInstance().addError(errors, validatorLocFocQtyStr.validate(locFocQtyStr, "shiptoDetails("+ i +").freeQuantity"));
                        
                    }
                }
            }
        }
    }
    
    public void validatorHeaderExtend(Document document, List<String> errors)throws Exception
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        FieldContentValidator validateFieldType = null;
        
        String gln = EbxmlParseHelper.getInstance().obtainValueFrom(document,
                "buyer.gln");
        validateFieldType = new LengthValidator(255, validateFieldType);
        fileParseUtil.addError(errors, validateFieldType.validate(gln, "buyer.gln"));
    
        String telFax = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "seller.partyInformation.contact.communicationChannel(0).@communicationNumber");
        validateFieldType = new LengthValidator(255, validateFieldType);
        fileParseUtil.addError(errors, validateFieldType.validate(telFax, "seller.partyInformation.contact.communicationChannel(0).@communicationNumber"));

        String email = EbxmlParseHelper
                .getInstance()
                .obtainValueFrom(document,
                        "seller.partyInformation.contact.communicationChannel(1).@communicationNumber");
        validateFieldType = new LengthValidator(255, validateFieldType);
        fileParseUtil.addError(errors, validateFieldType.validate(email, "seller.partyInformation.contact.communicationChannel(1).@communicationNumber"));
    }


    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            ebxmlPoDocFileHandler.readFileContent((PoDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
    
    private String getXSDSchema()
    {
        return "xsd/ebxml/po/Order.xsd";
    }
}
