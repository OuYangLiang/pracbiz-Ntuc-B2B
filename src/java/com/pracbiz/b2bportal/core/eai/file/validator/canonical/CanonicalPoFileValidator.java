package com.pracbiz.b2bportal.core.eai.file.validator.canonical;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.PoDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.PoFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.IntegerValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MinValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CanonicalPoFileValidator extends PoFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CanonicalPoFileValidator.class);
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private PoDocFileHandler canonicalPoDocFileHandler;
    
    private static String PO_TYPE_CON = "CON";
    private static String PO_BASE_UNIT_PACKAGE = "P";
    private static String PO_BASE_UNIT_UNIT = "U";
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
            throws Exception
    {
        InputStream inputStream = null;
        List<String> errors = null;
        try
        {
            errors = new ArrayList<String>();
            inputStream = new ByteArrayInputStream(input);
            Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(inputStream);
            List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
            List<String[]> headerExList= map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            List<String[]> detailList= map.get(FileParserUtil.RECORD_TYPE_DETAIL);
            List<String[]> detailExList= map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
            List<String[]> locationList = map.get(FileParserUtil.RECORD_TYPE_LOCATION);
            List<String[]> locationExList= map.get(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED);
            if (headerList == null)
            {
                errors.add("header is expected.");
                return errors;
            }
            else if (detailList == null)
            {
                errors.add("details are expected.");
                return errors;
            }
            else if (locationList == null)
            {
                errors.add("locations are expected.");
                return errors;
            }
            
            String[] headerContent = headerList.get(0);
            if (headerContent.length != 58)
            {
                errors.add(getPrefixForHeader() + "header has "+ headerContent.length + " fields, 58 is expected.");
                return errors;
            }
            
            
            for (int i = 0; i < detailList.size(); i++)
            {
                String[] detailContent = detailList.get(i);
                if (detailContent.length != 32)
                {
                    errors.add(getPrefixForDetail(i + 1) + ", detail has "+ detailContent.length + " fields, 32 is expected.");
                    return errors;
                }
            }
            
            for (int i = 0; i < locationList.size(); i++)
            {
                String[] locationContent = locationList.get(i);
                if (locationContent.length != 9)
                {
                    errors.add(getPrefixForLocation(i + 1) + ", location has "+ locationContent.length + " fields, 9 is expected.");
                    return errors;
                }
            }
            
            
            validateHeader(headerContent, errors, docMsg);
            if (!errors.isEmpty())
            {
                return errors;
            }
            
            String poType = headerContent[4].trim();
            
            for (int i = 0; i < detailList.size(); i++)
            {
                String[] detailContent = detailList.get(i);
                validateDetail(headerContent[1], detailContent, errors, i+1, poType);
            }
            if (!errors.isEmpty())
            {
                return errors;
            }
            
            for (int i = 0; i < locationList.size(); i++)
            {
                String[] locationContent = locationList.get(i);
                validateLocation(detailList, locationContent, errors, i+1);
            }
            if (!errors.isEmpty())
            {
                return errors;
            }

            if (headerExList != null && !headerExList.isEmpty())
            {
                validateCanonicalHeaderExtends(headerExList, errors);
                if (!errors.isEmpty())
                {
                    return errors;
                }

            }
          
            if (detailExList != null && !detailExList.isEmpty())
            {
                validateCanonicalDetailExtends(detailExList, errors);
                if (!errors.isEmpty())
                {
                    return errors;
                }
            }
           
            
            if (locationExList != null && !locationExList.isEmpty())
            {
                validateCanonicalLocationExtends(locationExList, errors);
            }
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        
        return errors;
    }
    
    
    private PoHeaderHolder validateHeader(String[] content, List<String> errors, DocMsg docMsg) throws Exception
    {
        PoHeaderHolder header = new PoHeaderHolder();
        String result = null;
        String poNo = content[1].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        result = validatePoNo.validate(poNo, getPrefixForHeader() + "poNo");
        if (result == null)
        {
            header.setPoNo(poNo);
        }
        FileParserUtil.getInstance().addError(errors, result);
       
        
        String docAction = content[2].trim();
        FieldContentValidator validateDocAction = null;
        validateDocAction = new EmptyValidator();
        validateDocAction = new SpecialCharValidator(validateDocAction, false, docAction);
        FileParserUtil.getInstance().addError(errors, validateDocAction.validate(docAction, "docAction"));
        
        String actionDate = content[3].trim();
        FieldContentValidator validateActionDate = null;
        validateActionDate = new EmptyValidator();
        validateActionDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateActionDate);
        FileParserUtil.getInstance().addError(errors, validateActionDate.validate(actionDate, getPrefixForHeader() + "actionDate"));
        
        String poType = content[4].trim();
        FieldContentValidator validatePoType = null;
        validatePoType = new EmptyValidator();
        validatePoType = new SpecialCharValidator(validatePoType, false, "SOR", "CON", "SVC");
        FileParserUtil.getInstance().addError(errors, validatePoType.validate(poType, getPrefixForHeader() + "poType"));

        String poSubType = content[5].trim();
        FieldContentValidator validatePoSubType = null;
        validatePoSubType = new EmptyValidator();
        validatePoSubType = new SpecialCharValidator(validatePoSubType, false, "1", "2");
        FileParserUtil.getInstance().addError(errors, validatePoSubType.validate(poSubType, getPrefixForHeader() + "poSubType"));
        
        String poDate = content[6].trim();
        FieldContentValidator validatePoDate = null;
        validatePoDate = new EmptyValidator();
        validatePoDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePoDate);
        FileParserUtil.getInstance().addError(errors, validatePoDate.validate(poDate, getPrefixForHeader() + "poDate"));
        
        String deliveryDateFrom = content[7].trim();
        FieldContentValidator validateDeliveryDateFrom = null;
        validateDeliveryDateFrom = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateDeliveryDateFrom);
        FileParserUtil.getInstance().addError(errors, validateDeliveryDateFrom.validate(deliveryDateFrom, getPrefixForHeader() + "deliveryDateFrom"));
        String fromResult = validateDeliveryDateFrom.validate(deliveryDateFrom, getPrefixForHeader() + "deliveryDateFrom");
        
        String deliveryDateTo = content[8].trim();
        FieldContentValidator validateDeliveryDateTo = null;
        validateDeliveryDateTo = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateDeliveryDateTo);
        FileParserUtil.getInstance().addError(errors, validateDeliveryDateTo.validate(deliveryDateTo, getPrefixForHeader() + "deliveryDateTo"));
        String toResult = validateDeliveryDateTo.validate(deliveryDateTo, getPrefixForHeader() + "deliveryDateTo");
        
        if (deliveryDateFrom != null && !deliveryDateFrom.isEmpty()
            && deliveryDateTo != null && !deliveryDateTo.isEmpty()
            && fromResult == null && toResult == null
            && DateUtil.getInstance().convertStringToDate(deliveryDateTo, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)
            .before(DateUtil.getInstance().convertStringToDate(deliveryDateFrom, DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS)))
        {
            errors.add("DeliveryDateTo should not be earlier than DeliveryDateFrom.");
        }
        
        String expiryDate = content[9].trim();
        FieldContentValidator validateExpiryDate = null;
        validateExpiryDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateExpiryDate);
        FileParserUtil.getInstance().addError(errors, validateExpiryDate.validate(expiryDate, getPrefixForHeader() + "expiryDate"));
        
        String buyerCode = content[10].trim();
        FieldContentValidator validateBuyerCode = null;
        validateBuyerCode = new EmptyValidator();
        validateBuyerCode = new LengthValidator(20, validateBuyerCode);
        validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getSenderCode());
        FileParserUtil.getInstance().addError(errors, validateBuyerCode.validate(buyerCode, getPrefixForHeader() + "buyerCode"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_BUYER_CODE)).validate(buyerCode, "buyerCode");
        if (result != null)
        {
            errors.add(getPrefixForHeader() + "[Buyer Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String buyerName = content[11].trim();
        if (!buyerName.isEmpty())
        {
            FieldContentValidator validateBuyerName = null;
            validateBuyerName = new LengthValidator(100);
            FileParserUtil.getInstance().addError(errors, validateBuyerName.validate(buyerName, getPrefixForHeader() + "buyerName"));
        }
        
        String buyerAddr1 = content[12].trim();
        if (!buyerAddr1.isEmpty())
        {
            FieldContentValidator validateBuyerAddr1 = null;
            validateBuyerAddr1 = new LengthValidator(100);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr1.validate(buyerAddr1, getPrefixForHeader() + "buyerAddr1"));
        }
        
        String buyerAddr2 = content[13].trim();
        if (!buyerAddr2.isEmpty())
        {
            FieldContentValidator validateBuyerAddr2 = null;
            validateBuyerAddr2 = new LengthValidator(100);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr2.validate(buyerAddr2, getPrefixForHeader() + "buyerAddr2"));
        }
        
        String buyerAddr3 = content[14].trim();
        if (!buyerAddr3.isEmpty())
        {
            FieldContentValidator validateBuyerAddr3 = null;
            validateBuyerAddr3 = new LengthValidator(100);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr3.validate(buyerAddr3, getPrefixForHeader() + "buyerAddr3"));
        }
        
        String buyerAddr4 = content[15].trim();
        if (!buyerAddr4.isEmpty())
        {
            FieldContentValidator validateBuyerAddr4 = null;
            validateBuyerAddr4 = new LengthValidator(100);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr4.validate(buyerAddr4, getPrefixForHeader() + "buyerAddr4"));
        }
        
        String buyerCity = content[16].trim();
        if (!buyerCity.isEmpty())
        {
            FieldContentValidator validateBuyerCity = null;
            validateBuyerCity = new LengthValidator(50);
            FileParserUtil.getInstance().addError(errors, validateBuyerCity.validate(buyerCity, getPrefixForHeader() + "buyerCity"));
        }
        
        String buyerState = content[17].trim();
        if (!buyerState.isEmpty())
        {
            FieldContentValidator validateBuyerState = null;
            validateBuyerState = new LengthValidator(50);
            FileParserUtil.getInstance().addError(errors, validateBuyerState.validate(buyerState, getPrefixForHeader() + "buyerState"));
        }
        
        String buyerCountryCode = content[18].trim();
        if (!buyerCountryCode.isEmpty())
        {
            FieldContentValidator validateBuyerCountryCode = null;
            validateBuyerCountryCode = new LengthValidator(2);
            FileParserUtil.getInstance().addError(errors, validateBuyerCountryCode.validate(buyerCountryCode, getPrefixForHeader() + "buyerCountryCode"));
        }
        
        String buyerPostalCode = content[19].trim();
        if (!buyerPostalCode.isEmpty())
        {
            FieldContentValidator validateBuyerPostalCode = null;
            validateBuyerPostalCode = new LengthValidator(15);
            FileParserUtil.getInstance().addError(errors, validateBuyerPostalCode.validate(buyerPostalCode, getPrefixForHeader() + "buyerPostalCode"));
        }
        
        String supplierCode = content[20].trim();
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getReceiverCode());
        FileParserUtil.getInstance().addError(errors, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + "supplierCode"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, "supplierCode");
        if (result != null)
        {
            errors.add(getPrefixForHeader() + "[Supplier Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String supplierName = content[21].trim();
        if (!supplierName.isEmpty())
        {
            FieldContentValidator validateSupplierName = null;
            validateSupplierName = new LengthValidator(100, validateSupplierName);
            FileParserUtil.getInstance().addError(errors, validateSupplierName.validate(supplierName, getPrefixForHeader() + "supplierName"));
        }
        
        String supplierAddr1 = content[22].trim();
        if (!supplierAddr1.isEmpty())
        {
            FieldContentValidator validateSupplierAddr1 = null;
            validateSupplierAddr1 = new LengthValidator(100, validateSupplierAddr1);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "supplierAddr1"));
        }
        
        String supplierAddr2 = content[23].trim();
        if (!supplierAddr2.isEmpty())
        {
            FieldContentValidator validateSupplierAddr2 = null;
            validateSupplierAddr2 = new LengthValidator(100, validateSupplierAddr2);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "supplierAddr2"));
        }
        
        String supplierAddr3 = content[24].trim();
        if (!supplierAddr3.isEmpty())
        {
            FieldContentValidator validateSupplierAddr3 = null;
            validateSupplierAddr3 = new LengthValidator(100, validateSupplierAddr3);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "supplierAddr3"));
        }
        
        String supplierAddr4 = content[25].trim();
        if (!supplierAddr4.isEmpty())
        {
            FieldContentValidator validateSupplierAddr4 = null;
            validateSupplierAddr4 = new LengthValidator(100, validateSupplierAddr4);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "supplierAddr4"));
        }
        
        String supplierCity = content[26].trim();
        if (!supplierCity.isEmpty())
        {
            FieldContentValidator validateSupplierCity = null;
            validateSupplierCity = new LengthValidator(50, validateSupplierCity);
            FileParserUtil.getInstance().addError(errors, validateSupplierCity.validate(supplierCity, getPrefixForHeader() + "supplierCity"));
        }
        
        String supplierState = content[27].trim();
        if (!supplierState.isEmpty())
        {
            FieldContentValidator validateSupplierState = null;
            validateSupplierState = new LengthValidator(50, validateSupplierState);
            FileParserUtil.getInstance().addError(errors, validateSupplierState.validate(supplierState, getPrefixForHeader() + "supplierState"));
        }
        
        String supplierCountryCode = content[28].trim();
        if (!supplierCountryCode.isEmpty())
        {
            FieldContentValidator validateSupplierCountryCode = null;
            validateSupplierCountryCode = new LengthValidator(2, validateSupplierCountryCode);
            FileParserUtil.getInstance().addError(errors, validateSupplierCountryCode.validate(supplierCountryCode, getPrefixForHeader() + "supplierCountryCode"));
        }
        
        String supplierPostalCode = content[29].trim();
        if (!supplierPostalCode.isEmpty())
        {
            FieldContentValidator validateSupplierPostalCode = null;
            validateSupplierPostalCode = new LengthValidator(15, validateSupplierPostalCode);
            FileParserUtil.getInstance().addError(errors, validateSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "supplierPostalCode"));
        }
        
        String shipToCode = content[30].trim();
        if (!shipToCode.isEmpty())
        {
            FieldContentValidator validateShipToCode = null;
            validateShipToCode = new LengthValidator(20, validateShipToCode);
            FileParserUtil.getInstance().addError(errors, validateShipToCode.validate(shipToCode, getPrefixForHeader() + "shipToCode"));
        }
        
        String shipToName = content[31].trim();
        if (!shipToName.isEmpty())
        {
            FieldContentValidator validateShipToName = null;
            validateShipToName = new LengthValidator(100, validateShipToName);
            FileParserUtil.getInstance().addError(errors, validateShipToName.validate(shipToName, getPrefixForHeader() + "shipToName"));
        }
        
        String shipToAddr1 = content[32].trim();
        if (!shipToAddr1.isEmpty())
        {
            FieldContentValidator validateShipToAddr1 = null;
            validateShipToAddr1 = new LengthValidator(100, validateShipToAddr1);
            FileParserUtil.getInstance().addError(errors, validateShipToAddr1.validate(shipToAddr1, getPrefixForHeader() + "shipToAddr1"));
        }
        
        String shipToAddr2 = content[33].trim();
        if (!shipToAddr2.isEmpty())
        {
            FieldContentValidator validateShipToAddr2 = null;
            validateShipToAddr2 = new LengthValidator(100, validateShipToAddr2);
            FileParserUtil.getInstance().addError(errors, validateShipToAddr2.validate(shipToAddr2, getPrefixForHeader() + "shipToAddr2"));
        }
        
        String shipToAddr3 = content[34].trim();
        if (!shipToAddr3.isEmpty())
        {
            FieldContentValidator validateShipToAddr3 = null;
            validateShipToAddr3 = new LengthValidator(100, validateShipToAddr3);
            FileParserUtil.getInstance().addError(errors, validateShipToAddr3.validate(shipToAddr3, getPrefixForHeader() + "shipToAddr3"));
        }
        
        String shipToAddr4 = content[35].trim();
        if (!shipToAddr4.isEmpty())
        {
            FieldContentValidator validateShipToAddr4 = null;
            validateShipToAddr4 = new LengthValidator(100, validateShipToAddr4);
            FileParserUtil.getInstance().addError(errors, validateShipToAddr4.validate(shipToAddr4, getPrefixForHeader() + "shipToAddr4"));
        }
        
        String shipToCity = content[36].trim();
        if (!shipToCity.isEmpty())
        {
            FieldContentValidator validateShipToCity = null;
            validateShipToCity = new LengthValidator(50, validateShipToCity);
            FileParserUtil.getInstance().addError(errors, validateShipToCity.validate(shipToCity, getPrefixForHeader() + "shipToCity"));
        }
        
        String shipToState = content[37].trim();
        if (!shipToState.isEmpty())
        {
            FieldContentValidator validateShipToState = null;
            validateShipToState = new LengthValidator(50, validateShipToState);
            FileParserUtil.getInstance().addError(errors, validateShipToState.validate(shipToState, getPrefixForHeader() + "shipToState"));
        }
        
        String shipToCountryCode = content[38].trim();
        if (!shipToCountryCode.isEmpty())
        {
            FieldContentValidator validateShipToCountryCode = null;
            validateShipToCountryCode = new LengthValidator(2, validateShipToCountryCode);
            FileParserUtil.getInstance().addError(errors, validateShipToCountryCode.validate(shipToCountryCode, getPrefixForHeader() + "shipToCountryCode"));
        }
        
        String shipToPostalCode = content[39].trim();
        if (!shipToPostalCode.isEmpty())
        {
            FieldContentValidator validateShipToPostalCode = null;
            validateShipToPostalCode = new LengthValidator(15, validateShipToPostalCode);
            FileParserUtil.getInstance().addError(errors, validateShipToPostalCode.validate(shipToPostalCode, getPrefixForHeader() + "shipToPostalCode"));
        }
        
        String deptCode = content[40].trim();
        if (!deptCode.isEmpty())
        {
            FieldContentValidator validateDeptCode = null;
            validateDeptCode = new LengthValidator(20, validateDeptCode);
            FileParserUtil.getInstance().addError(errors, validateDeptCode.validate(deptCode, getPrefixForHeader() + "deptCode"));
        }
        
        String deptName = content[41].trim();
        if (!deptName.isEmpty())
        {
            FieldContentValidator validateDeptName = null;
            validateDeptName = new LengthValidator(100, validateDeptName);
            FileParserUtil.getInstance().addError(errors, validateDeptName.validate(deptName, getPrefixForHeader() + "deptName"));
        }
        
        String subDeptCode = content[42].trim();
        if (!subDeptCode.isEmpty())
        {
            FieldContentValidator validateSubDeptCode = null;
            validateSubDeptCode = new LengthValidator(20, validateSubDeptCode);
            FileParserUtil.getInstance().addError(errors, validateSubDeptCode.validate(subDeptCode, getPrefixForHeader() + "subDeptCode"));
        }
        
        String subDeptName = content[43].trim();
        if (!subDeptName.isEmpty())
        {
            FieldContentValidator validateSubDeptName = null;
            validateSubDeptName = new LengthValidator(100, validateSubDeptName);
            FileParserUtil.getInstance().addError(errors, validateSubDeptName.validate(subDeptName, getPrefixForHeader() + "subDeptName"));
        }
        
        String creditTermCode = content[44].trim();
        if (!creditTermCode.isEmpty())
        {
            FieldContentValidator validateCreditTermCode = null;
            validateCreditTermCode = new LengthValidator(20, validateCreditTermCode);
            FileParserUtil.getInstance().addError(errors, validateCreditTermCode.validate(creditTermCode, getPrefixForHeader() + "creditTermCode"));
        }
        
        String creditTermDesc = content[45].trim();
        if (!creditTermDesc.isEmpty())
        {
            FieldContentValidator validateCreditTermDesc = null;
            validateCreditTermDesc = new LengthValidator(100, validateCreditTermDesc);
            FileParserUtil.getInstance().addError(errors, validateCreditTermDesc.validate(creditTermDesc, getPrefixForHeader() + "creditTermDesc"));
        }
        
        String totalCost = content[46].trim();
        FieldContentValidator validateTotalCost = null;
        validateTotalCost = new EmptyValidator();
        validateTotalCost = new NoSpaceValidator(validateTotalCost);
        validateTotalCost = new NumberValidator(validateTotalCost);
        validateTotalCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalCost);
        FileParserUtil.getInstance().addError(errors, validateTotalCost.validate(totalCost, getPrefixForHeader() + "totalCost"));
        
        String additionalDiscountAmount = content[47].trim();
        FieldContentValidator validateAdditionalDiscountAmount = null;
        validateAdditionalDiscountAmount = new EmptyValidator();
        validateAdditionalDiscountAmount = new NoSpaceValidator(validateAdditionalDiscountAmount);
        validateAdditionalDiscountAmount = new NumberValidator(validateAdditionalDiscountAmount);
        validateAdditionalDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateAdditionalDiscountAmount);
        FileParserUtil.getInstance().addError(errors, validateAdditionalDiscountAmount.validate(additionalDiscountAmount, getPrefixForHeader() + "additionalDiscountAmount"));
        
        String additionalDiscountPercent = content[48].trim();
        if (additionalDiscountPercent != null)
        {
            FieldContentValidator validateAdditionalDiscountPercent = null;
            validateAdditionalDiscountPercent = new NoSpaceValidator();
            validateAdditionalDiscountPercent = new NumberValidator(validateAdditionalDiscountPercent);
            validateAdditionalDiscountPercent = new MaxValidator(BigDecimal.valueOf(99.99), validateAdditionalDiscountPercent);
            FileParserUtil.getInstance().addError(errors, validateAdditionalDiscountPercent.validate(additionalDiscountPercent, getPrefixForHeader() + "additionalDiscountPercent"));
        }

        String netCost = content[49].trim();
        FieldContentValidator validateNetCost = null;
        validateNetCost = new EmptyValidator();
        validateNetCost = new NoSpaceValidator(validateNetCost);
        validateNetCost = new NumberValidator(validateNetCost);
        validateNetCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateNetCost);
        FileParserUtil.getInstance().addError(errors, validateNetCost.validate(netCost, getPrefixForHeader() + "netCost"));
        
        String grossProfitMargin = content[50].trim();
        FieldContentValidator validateGrossProfitMargin = null;
        if (poType != null && !poType.isEmpty() && poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateGrossProfitMargin = new EmptyValidator();
        }
        validateGrossProfitMargin = new NoSpaceValidator(validateGrossProfitMargin);
        validateGrossProfitMargin = new NumberValidator(validateGrossProfitMargin);
        validateGrossProfitMargin = new MaxValidator(BigDecimal.valueOf(999.99), validateGrossProfitMargin);
        FileParserUtil.getInstance().addError(errors, validateGrossProfitMargin.validate(grossProfitMargin, getPrefixForHeader() + "grossProfitMargin"));
        
        String totalSharedCost = content[51].trim();
        FieldContentValidator validateTotalSharedCost = null;
        if (poType != null && !poType.isEmpty() && poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateTotalSharedCost = new EmptyValidator();
        }
        validateTotalSharedCost = new NoSpaceValidator(validateTotalSharedCost);
        validateTotalSharedCost = new NumberValidator(validateTotalSharedCost);
        validateTotalSharedCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalSharedCost);
        FileParserUtil.getInstance().addError(errors, validateTotalSharedCost.validate(totalSharedCost, getPrefixForHeader() + "totalSharedCost"));
        
        String totalGrossCost = content[52].trim();
        FieldContentValidator validateTotalGrossCost = null;
        if (poType != null && !poType.isEmpty() && poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateTotalGrossCost = new EmptyValidator();
        }
        validateTotalGrossCost = new NoSpaceValidator(validateTotalGrossCost);
        validateTotalGrossCost = new NumberValidator(validateTotalGrossCost);
        validateTotalGrossCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalGrossCost);
        FileParserUtil.getInstance().addError(errors, validateTotalGrossCost.validate(totalGrossCost, getPrefixForHeader() + "totalGrossCost"));
        
        String totalRetailAmount = content[53].trim();
        FieldContentValidator validateTotalRetailAmount = null;
        if (!totalRetailAmount.isEmpty())
        {
            validateTotalRetailAmount = new NoSpaceValidator(validateTotalRetailAmount);
            validateTotalRetailAmount = new NumberValidator(validateTotalRetailAmount);
            validateTotalRetailAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalRetailAmount);
            FileParserUtil.getInstance().addError(errors, validateTotalRetailAmount.validate(totalRetailAmount, getPrefixForHeader() + "totalRetailAmount"));
        }
        
        String orderRemarks = content[54].trim();
        FieldContentValidator validateOrderRemarks = null;
        if (!orderRemarks.isEmpty())
        {
            validateOrderRemarks = new LengthValidator(500, validateOrderRemarks);
            FileParserUtil.getInstance().addError(errors, validateOrderRemarks.validate(orderRemarks, getPrefixForHeader() + "orderRemarks"));
        }
        
        String periodStartDate = content[55].trim();
        FieldContentValidator validatePeriodStartDate = null;
        if (poType != null && !poType.isEmpty() && poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validatePeriodStartDate = new EmptyValidator();
        }
        validatePeriodStartDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePeriodStartDate);
        FileParserUtil.getInstance().addError(errors, validatePeriodStartDate.validate(periodStartDate, getPrefixForHeader() + "periodStartDate"));

        
        String periodEndDate = content[56].trim();
        FieldContentValidator validatePeriodEndDate = null;
        if (poType != null && !poType.isEmpty() && poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validatePeriodEndDate = new EmptyValidator();
        }
        validatePeriodEndDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePeriodEndDate);
        FileParserUtil.getInstance().addError(errors, validatePeriodEndDate.validate(periodEndDate, getPrefixForHeader() + "periodEndDate"));

        
        String poSubType2 = content[57].trim();
        FieldContentValidator validatePoSubType2 = null;
        if (!poSubType2.isEmpty())
        {
            validatePoSubType2 = new LengthValidator(10, validatePoSubType2);
            FileParserUtil.getInstance().addError(errors, validatePoSubType2.validate(poSubType2, getPrefixForHeader() + "poSubType2"));
        }
        
        return header;
    }
    
    private void validateDetail(String headerPoNo, String[] content, List<String> errors, int no, String poType) throws Exception
    {
        String poNo = content[1].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        validatePoNo = new SpecialCharValidator(validatePoNo, false, headerPoNo);
        FileParserUtil.getInstance().addError(errors, validatePoNo.validate(poNo, getPrefixForDetail(no) + "poNo"));
        
        String seqNo = content[2].trim();
        FieldContentValidator validateSeqNo = null;
        validateSeqNo = new EmptyValidator();
        validateSeqNo = new IntegerValidator(validateSeqNo);
        validateSeqNo = new MinValidator(Integer.valueOf(1), validateSeqNo);
        validateSeqNo = new MaxValidator(Integer.valueOf(9999), validateSeqNo);
        FileParserUtil.getInstance().addError(errors, validateSeqNo.validate(seqNo, getPrefixForDetail(no) + "seqNo"));
        
        String buyerItemCode = content[3].trim();
        FieldContentValidator validateBuyerItemCode = null;
        validateBuyerItemCode = new EmptyValidator();
        validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
        FileParserUtil.getInstance().addError(errors, validateBuyerItemCode.validate(buyerItemCode, getPrefixForDetail(no) + "buyerItemCode"));
        
        String supplierItemCode = content[4].trim();
        FieldContentValidator validateSupplierItemCode = null;
        validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
        FileParserUtil.getInstance().addError(errors, validateSupplierItemCode.validate(supplierItemCode, getPrefixForDetail(no) + "supplierItemCode"));
        
        String barCode = content[5].trim();
        FieldContentValidator validateBarCode = null;
        validateBarCode = new LengthValidator(50, validateBarCode);
        FileParserUtil.getInstance().addError(errors, validateBarCode.validate(barCode, getPrefixForDetail(no) + "barCode"));
        
        String itemDesc = content[6].trim();
        FieldContentValidator validateItemDesc = null;
        validateItemDesc = new EmptyValidator();
        validateItemDesc = new LengthValidator(100, validateItemDesc);
        FileParserUtil.getInstance().addError(errors, validateItemDesc.validate(itemDesc, getPrefixForDetail(no) + "itemDesc"));
        
        String brand = content[7].trim();
        FieldContentValidator validateBrand = null;
        validateBrand = new LengthValidator(20, validateBrand);
        FileParserUtil.getInstance().addError(errors, validateBrand.validate(brand, getPrefixForDetail(no) + "brand"));
        
        String colourCode = content[8].trim();
        FieldContentValidator validateColourCode = null;
        validateColourCode = new LengthValidator(20, validateColourCode);
        FileParserUtil.getInstance().addError(errors, validateColourCode.validate(colourCode, getPrefixForDetail(no) + "colourCode"));
        
        String colourDesc = content[9].trim();
        FieldContentValidator validateColourDesc = null;
        validateColourDesc = new LengthValidator(50, validateColourDesc);
        FileParserUtil.getInstance().addError(errors, validateColourDesc.validate(colourDesc, getPrefixForDetail(no) + "colourDesc"));
        
        String sizeCode = content[10].trim();
        FieldContentValidator validateSizeCode = null;
        validateSizeCode = new LengthValidator(20, validateSizeCode);
        FileParserUtil.getInstance().addError(errors, validateSizeCode.validate(sizeCode, getPrefixForDetail(no) + "sizeCode"));
        
        String sizeDesc = content[11].trim();
        FieldContentValidator validateSizeDesc = null;
        validateSizeDesc = new LengthValidator(50, validateSizeDesc);
        FileParserUtil.getInstance().addError(errors, validateSizeDesc.validate(sizeDesc, getPrefixForDetail(no) + "sizeDesc"));
            
        String packingFactor = content[12].trim();
        FieldContentValidator validatePackingFactor = null;
        validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
        validatePackingFactor = new NumberValidator(validatePackingFactor);
        validatePackingFactor = new MaxValidator(BigDecimal.valueOf(999999.99), validatePackingFactor);
        FileParserUtil.getInstance().addError(errors, validatePackingFactor.validate(packingFactor, getPrefixForDetail(no) + "packingFactor"));
        
        String orderBaseUnit = content[13].trim();
        FieldContentValidator validateOrderBaseUnit = null;
        validateOrderBaseUnit = new SpecialCharValidator(false, PO_BASE_UNIT_PACKAGE, PO_BASE_UNIT_UNIT);
        FileParserUtil.getInstance().addError(errors, validateOrderBaseUnit.validate(orderBaseUnit, getPrefixForDetail(no) + "orderBaseUnit"));
        
        String orderUom = content[14].trim();
        FieldContentValidator validateOrderUom = null;
        validateOrderUom = new EmptyValidator();
        validateOrderUom = new LengthValidator(20, validateOrderUom);
        FileParserUtil.getInstance().addError(errors, validateOrderUom.validate(orderUom, getPrefixForDetail(no) + "orderUom"));
        
        String orderQty = content[15].trim();
        FieldContentValidator validateOrderQty = null;
        validateOrderQty = new EmptyValidator();
        validateOrderQty = new NoSpaceValidator(validateOrderQty);
        validateOrderQty = new NumberValidator(validateOrderQty);
        validateOrderQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateOrderQty);
        FileParserUtil.getInstance().addError(errors, validateOrderQty.validate(orderQty, getPrefixForDetail(no) + "orderQty"));
        
        String focBaseUnit = content[16].trim();
        FieldContentValidator validateFocBaseUnit = null;
        validateFocBaseUnit = new SpecialCharValidator(false, PO_BASE_UNIT_PACKAGE, PO_BASE_UNIT_UNIT);
        FileParserUtil.getInstance().addError(errors, validateFocBaseUnit.validate(focBaseUnit, getPrefixForDetail(no) + "focBaseUnit"));
        
        String focUom = content[17].trim();
        FieldContentValidator validateFocUom = null;
        validateFocUom = new LengthValidator(20, validateFocUom);
        FileParserUtil.getInstance().addError(errors, validateFocUom.validate(focUom, getPrefixForDetail(no) + "focUom"));
            
        String focQty = content[18].trim();
        FieldContentValidator validateFocQty = null;
        validateFocQty = new NoSpaceValidator(validateFocQty);
        validateFocQty = new NumberValidator(validateFocQty);
        validateFocQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateFocQty);
        FileParserUtil.getInstance().addError(errors, validateFocQty.validate(focQty, getPrefixForDetail(no) + "focQty"));
        
        String unitCost = content[19].trim();
        FieldContentValidator validateUnitCost = null;
        validateUnitCost = new EmptyValidator();
        validateUnitCost = new NoSpaceValidator(validateUnitCost);
        validateUnitCost = new NumberValidator(validateUnitCost);
        validateUnitCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateUnitCost);
        FileParserUtil.getInstance().addError(errors, validateUnitCost.validate(unitCost, getPrefixForDetail(no) + "unitCost"));
        
        String packCost = content[20].trim();
        FieldContentValidator validatePackCost = null;
        validatePackCost = new NoSpaceValidator();
        validatePackCost = new NumberValidator(validatePackCost);
        validatePackCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validatePackCost);
        FileParserUtil.getInstance().addError(errors, validatePackCost.validate(packCost, getPrefixForDetail(no) + "packCost"));
        
        String costDiscountAmount = content[21].trim();
        FieldContentValidator validateCostDiscountAmount = null;
        validateCostDiscountAmount = new EmptyValidator();
        validateCostDiscountAmount = new NoSpaceValidator(validateCostDiscountAmount);
        validateCostDiscountAmount = new NumberValidator(validateCostDiscountAmount);
        validateCostDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateCostDiscountAmount);
        FileParserUtil.getInstance().addError(errors, validateCostDiscountAmount.validate(costDiscountAmount, getPrefixForDetail(no) + "costDiscountAmount"));
        
        String costDiscountPercent = content[22].trim();
        FieldContentValidator validateCostDiscountPercent = null;
        validateCostDiscountPercent = new NoSpaceValidator();
        validateCostDiscountPercent = new NumberValidator(validateCostDiscountPercent);
        validateCostDiscountPercent = new MaxValidator(BigDecimal.valueOf(99.99), validateCostDiscountPercent);
        FileParserUtil.getInstance().addError(errors, validateCostDiscountPercent.validate(costDiscountPercent, getPrefixForDetail(no) + "costDiscountPercent"));
        
        String retailDiscountAmount = content[23].trim();
        FieldContentValidator validateRetailDiscountAmount = null;
        validateRetailDiscountAmount = new NoSpaceValidator();
        validateRetailDiscountAmount = new NumberValidator(validateRetailDiscountAmount);
        validateRetailDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateRetailDiscountAmount);
        FileParserUtil.getInstance().addError(errors, validateRetailDiscountAmount.validate(retailDiscountAmount, getPrefixForDetail(no) + "retailDiscountAmount"));
        
        String netUnitCost = content[24].trim();
        FieldContentValidator validateNetUnitCost = null;
        validateNetUnitCost = new EmptyValidator();
        validateNetUnitCost = new NoSpaceValidator(validateNetUnitCost);
        validateNetUnitCost = new NumberValidator(validateNetUnitCost);
        validateNetUnitCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateNetUnitCost);
        FileParserUtil.getInstance().addError(errors, validateNetUnitCost.validate(netUnitCost, getPrefixForDetail(no) + "netUnitCost"));
        
        String netPackCost = content[25].trim();
        FieldContentValidator validateNetPackCost = null;
        validateNetPackCost = new NoSpaceValidator(validateNetPackCost);
        validateNetPackCost = new NumberValidator(validateNetPackCost);
        validateNetPackCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateNetPackCost);
        FileParserUtil.getInstance().addError(errors, validateNetPackCost.validate(netPackCost, getPrefixForDetail(no) + "netPackCost"));

        String itemCost = content[26].trim();
        FieldContentValidator validateItemCost = null;
        validateItemCost = new EmptyValidator();
        validateItemCost = new NoSpaceValidator(validateItemCost);
        validateItemCost = new NumberValidator(validateItemCost);
        validateItemCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemCost);
        FileParserUtil.getInstance().addError(errors, validateItemCost.validate(itemCost, getPrefixForDetail(no) + "itemCost"));

        String itemSharedCost = content[27].trim();
        FieldContentValidator validateItemSharedCost = null;
        if (poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateItemSharedCost = new EmptyValidator();
        }
        validateItemSharedCost = new NoSpaceValidator(validateItemSharedCost);
        validateItemSharedCost = new NumberValidator(validateItemSharedCost);
        validateItemSharedCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemSharedCost);
        FileParserUtil.getInstance().addError(errors, validateItemSharedCost.validate(itemSharedCost, getPrefixForDetail(no) + "itemSharedCost"));
        
        String itemGrossCost = content[28].trim();
        FieldContentValidator validateItemGrossCost = null;
        if (poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateItemGrossCost = new EmptyValidator();
        }
        validateItemGrossCost = new NoSpaceValidator(validateItemGrossCost);
        validateItemGrossCost = new NumberValidator(validateItemGrossCost);
        validateItemGrossCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemGrossCost);
        FileParserUtil.getInstance().addError(errors, validateItemGrossCost.validate(itemGrossCost, getPrefixForDetail(no) + "itemGrossCost"));
        
        String retailPrice = content[29].trim();
        FieldContentValidator validateRetailPrice = null;
        if (poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateRetailPrice = new EmptyValidator();
        }
        validateRetailPrice = new NoSpaceValidator(validateRetailPrice);
        validateRetailPrice = new NumberValidator(validateRetailPrice);
        validateRetailPrice = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateRetailPrice);
        FileParserUtil.getInstance().addError(errors, validateRetailPrice.validate(retailPrice, getPrefixForDetail(no) + "retailPrice"));
        
        String itemRetailAmount = content[30].trim();
        FieldContentValidator validateItemRetailAmount = null;
        if (poType.equalsIgnoreCase(PO_TYPE_CON))
        {
            validateItemRetailAmount = new EmptyValidator();
        }
        validateItemRetailAmount = new NoSpaceValidator(validateItemRetailAmount);
        validateItemRetailAmount = new NumberValidator(validateItemRetailAmount);
        validateItemRetailAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemRetailAmount);
        FileParserUtil.getInstance().addError(errors, validateItemRetailAmount.validate(itemRetailAmount, getPrefixForDetail(no) + "itemRetailAmount"));
        
        String itemRemarks = content[31].trim();
        if (!itemRemarks.isEmpty())
        {
            FieldContentValidator validateItemRemarks = null;
            validateItemRemarks = new LengthValidator(100, validateItemRemarks);
            FileParserUtil.getInstance().addError(errors, validateItemRemarks.validate(itemRemarks, getPrefixForDetail(no) + "itemRemarks"));
        }
      
    }
    
    
    private void validateLocation(List<String[]>detailList, String[] content, List<String> errors, int no) throws Exception
    {
        String poNo = content[1].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        FileParserUtil.getInstance().addError(errors, validatePoNo.validate(poNo, getPrefixForLocation(no) + "poNo"));
        
        String lineSeqNo = content[2].trim();
        FieldContentValidator validateLineSeqNo = null;
        validateLineSeqNo = new EmptyValidator();
        validateLineSeqNo = new IntegerValidator(validateLineSeqNo);
        validateLineSeqNo = new MaxValidator(Integer.valueOf(9999), validateLineSeqNo);
        boolean checkLineSeqNo = false;
        for (String[] detail : detailList)
        {
            if (detail[2].trim().equalsIgnoreCase(lineSeqNo))
            {
                checkLineSeqNo = true;
            }
        }
        if (checkLineSeqNo)
        {
            FileParserUtil.getInstance().addError(errors, validateLineSeqNo.validate(lineSeqNo, getPrefixForLocation(no) + "lineSeqNo"));
        }
        else
        {
            errors.add(getPrefixForLocation(no) + " detailLineSeqNo has no corresponding lineSeqNum in detail.");
           
        }
       
        
        String locationSeqNo = content[3].trim();
        FieldContentValidator validateLocationSeqNo = null;
        validateLocationSeqNo = new EmptyValidator();
        validateLocationSeqNo = new IntegerValidator(validateLocationSeqNo);
        validateLocationSeqNo = new MaxValidator(Integer.valueOf(9999), validateLocationSeqNo);
        FileParserUtil.getInstance().addError(errors, validateLocationSeqNo.validate(locationSeqNo, getPrefixForLocation(no) + "locationSeqNo"));
        
        String locationCode = content[4].trim();
        FieldContentValidator validateLocationCode = null;
        validateLocationCode = new EmptyValidator();
        validateLocationCode = new LengthValidator(20, validateLocationCode);
        FileParserUtil.getInstance().addError(errors, validateLocationCode.validate(locationCode, getPrefixForLocation(no) + "locationCode"));
        
        String locationName = content[5].trim();
        if (!locationName.isEmpty())
        {
            FieldContentValidator validateLocationName = null;
            validateLocationName = new LengthValidator(100, validateLocationName);
            FileParserUtil.getInstance().addError(errors, validateLocationName.validate(locationName, getPrefixForLocation(no) + "locationName"));
        }
        
        String locationShipQty = content[6].trim();
        FieldContentValidator validateLocationShipQty = null;
        validateLocationShipQty = new EmptyValidator();
        validateLocationShipQty = new NoSpaceValidator(validateLocationShipQty);
        validateLocationShipQty = new NumberValidator(validateLocationShipQty);
        validateLocationShipQty = new MaxValidator(BigDecimal.valueOf(999999), validateLocationShipQty);
        FileParserUtil.getInstance().addError(errors, validateLocationShipQty.validate(locationShipQty, getPrefixForLocation(no) + "locationShipQty"));
        
        String locationFocQty = content[7].trim();
        if (!locationFocQty.isEmpty())
        {
            FieldContentValidator validateLocationFocQty = null;
            validateLocationFocQty = new NoSpaceValidator(validateLocationFocQty);
            validateLocationFocQty = new NumberValidator(validateLocationFocQty);
            validateLocationFocQty = new MaxValidator(BigDecimal.valueOf(999999), validateLocationFocQty);
            FileParserUtil.getInstance().addError(errors, validateLocationFocQty.validate(locationFocQty, getPrefixForLocation(no) + "locationFocQty"));
        }
        

        String lineRefNo = content[8].trim();
        FieldContentValidator validateLineRefNo = null;
        if (!lineRefNo.isEmpty())
        {
            validateLineRefNo = new LengthValidator(20, validateLineRefNo);
            FileParserUtil.getInstance().addError(errors, validateLineRefNo.validate(lineRefNo, getPrefixForHeader() + "lineRefNo"));
        }
    }


    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            canonicalPoDocFileHandler.readFileContent((PoDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }

}
