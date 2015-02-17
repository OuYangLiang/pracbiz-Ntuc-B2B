package com.pracbiz.b2bportal.core.eai.file.validator.canonical;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.SalesFileValidator;
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
import com.pracbiz.b2bportal.core.eai.message.outbound.SalesDocMsg;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CanonicalSalesFileValidator extends SalesFileValidator implements CoreCommonConstants
{
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private BusinessRuleService businessRuleService;
    @Autowired private CountryService countryService;
    
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
            }
            else if (detailList == null)
            {
                errors.add("details are expected.");
            }
            else if (locationList == null)
            {
                errors.add("locations are expected.");
            }
            
            if (!errors.isEmpty())
            {
                return errors;
            }
            
            String[] headerContent = headerList.get(0);
            if (headerContent.length != 43)
            {
                errors.add(getPrefixForHeader() + "header has "+ headerContent.length + " fields, 43 is expected.");
            }
            
            
            for (int i = 0; i < detailList.size(); i++)
            {
                String[] detailContent = detailList.get(i);
                if (detailContent.length != 31)
                {
                    errors.add(getPrefixForDetail(i + 1) + ", detail has "+ detailContent.length + " fields, 31 is expected.");
                }
            }
            
            for (int i = 0; i < locationList.size(); i++)
            {
                String[] locationContent = locationList.get(i);
                if (locationContent.length != 15)
                {
                    errors.add(getPrefixForLocation(i + 1) + ", location has "+ locationContent.length + " fields, 9 is expected.");
                }
            }

            if (!errors.isEmpty())
            {
                return errors;
            }
            
            boolean isValidation = businessRuleService.isNeedValidateDailySalesDataLogic(docMsg.getSenderOid());
            
            CountryHolder param = new CountryHolder();
            List<CountryHolder> ctrys = countryService.select(param);
            
            List<String> ctryCodes = new ArrayList<String>();
            if (ctrys != null && !ctrys.isEmpty())
            {
                for (CountryHolder ctry : ctrys)
                {
                    ctryCodes.add(ctry.getCtryCode());
                }
            }
            
            SalesHeaderHolder header = validateHeader(headerContent, errors, docMsg, isValidation, ctryCodes);
            if (!errors.isEmpty())
            {
                return errors;
            }
            
            BigDecimal totalNetSalesAmt = BigDecimal.ZERO;
            BigDecimal totalDiscountAmt = BigDecimal.ZERO;
            BigDecimal totalVatAmt = BigDecimal.ZERO;
            
            Map<String ,List<Date>> dMap = new HashMap<String ,List<Date>>();
            
            for (int i = 0; i < detailList.size(); i++)
            {
                String[] detailContent = detailList.get(i);
                SalesDetailHolder detail = validateDetail(header, detailContent, errors, i+1 ,dMap, isValidation);
                totalNetSalesAmt = totalNetSalesAmt.add(detail.getItemNetSalesAmount() == null ? BigDecimal.ZERO : detail.getItemNetSalesAmount());
                totalDiscountAmt = totalDiscountAmt.add(detail.getSalesDiscountAmount() == null ? BigDecimal.ZERO : detail.getSalesDiscountAmount());
                totalVatAmt = totalVatAmt.add(detail.getVatAmount() == null ? BigDecimal.ZERO : detail.getVatAmount());
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
            
            if (isValidation && header.getTotalNetSalesAmount().compareTo(totalNetSalesAmt) != 0)
            {
                errors.add(getPrefixForHeader() + "TotalNetSalesAmount should equal the total of each item NetSalesAmount in detail");
            }
            if (isValidation && header.getTotalDiscountAmount().compareTo(totalDiscountAmt) != 0)
            {
                errors.add(getPrefixForHeader() + "TotalDiscountAmount should equal the total of each item DiscountAmount in detail");
            }
            if (isValidation && header.getTotalVatAmount().compareTo(totalVatAmt) != 0)
            {
                errors.add(getPrefixForHeader() + "TotalVatAmount should equal the total of each item VatAmount in detail");
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
                if (!errors.isEmpty())
                {
                    return errors;
                }
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
    
    
    private SalesHeaderHolder validateHeader(String[] content, List<String> errors, DocMsg docMsg, boolean isValidation, List<String> ctryCodes) throws Exception
    {
        SalesHeaderHolder header = new SalesHeaderHolder();
        String result = null;
        
        String salesNo = content[1].trim();
        FieldContentValidator validateSalesNo = null;
        validateSalesNo = new EmptyValidator();
        validateSalesNo = new LengthValidator(20, validateSalesNo);
        validateSalesNo = new SpecialCharValidator(validateSalesNo, false, docMsg.getBatch().getBatchNo());
        result = validateSalesNo.validate(salesNo, getPrefixForHeader() + "salesNo");
        if (result == null)
        {
            header.setSalesNo(salesNo);
        }
        FileParserUtil.getInstance().addError(errors, result);
       
        
        String docAction = content[2].trim();
        FieldContentValidator validateDocAction = null;
        validateDocAction = new EmptyValidator();
        validateDocAction = new SpecialCharValidator(validateDocAction, false,
            "A", "R", "D");
        FileParserUtil.getInstance().addError(errors, validateDocAction.validate(docAction, "docAction"));
        
        String actionDate = content[3].trim();
        FieldContentValidator validateActionDate = null;
        validateActionDate = new EmptyValidator();
        validateActionDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateActionDate);
        FileParserUtil.getInstance().addError(errors, validateActionDate.validate(actionDate, getPrefixForHeader() + "actionDate"));
        
        String salesDataType = content[4].trim();
        FieldContentValidator validateSalesDataType = null;
        validateSalesDataType = new EmptyValidator();
        validateSalesDataType = new SpecialCharValidator(validateSalesDataType, false, "SOR", "CON");
        FileParserUtil.getInstance().addError(errors, validateSalesDataType.validate(salesDataType, getPrefixForHeader() + "salesDataType"));
        
        String salesDate = content[5].trim();
        FieldContentValidator validateSalesDate = null;
        validateSalesDate = new EmptyValidator();
        validateSalesDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateSalesDate);
        FileParserUtil.getInstance().addError(errors, validateSalesDate.validate(salesDate, getPrefixForHeader() + "salesDate"));
        
        String buyerCode = content[6].trim();
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
        
        String buyerName = content[7].trim();
        if (!buyerName.isEmpty())
        {
            FieldContentValidator validateBuyerName = null;
            validateBuyerName = new LengthValidator(100,validateBuyerName);
            FileParserUtil.getInstance().addError(errors, validateBuyerName.validate(buyerName, getPrefixForHeader() + "buyerName"));
        }
        
        String buyerAddr1 = content[8].trim();
        if (!buyerAddr1.isEmpty())
        {
            FieldContentValidator validateBuyerAddr1 = null;
            validateBuyerAddr1 = new LengthValidator(100,validateBuyerAddr1);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr1.validate(buyerAddr1, getPrefixForHeader() + "buyerAddr1"));
        }
        
        String buyerAddr2 = content[9].trim();
        if (!buyerAddr2.isEmpty())
        {
            FieldContentValidator validateBuyerAddr2 = null;
            validateBuyerAddr2 = new LengthValidator(100,validateBuyerAddr2);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr2.validate(buyerAddr2, getPrefixForHeader() + "buyerAddr2"));
        }
        
        String buyerAddr3 = content[10].trim();
        if (!buyerAddr3.isEmpty())
        {
            FieldContentValidator validateBuyerAddr3 = null;
            validateBuyerAddr3 = new LengthValidator(100,validateBuyerAddr3);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr3.validate(buyerAddr3, getPrefixForHeader() + "buyerAddr3"));
        }
        
        String buyerAddr4 = content[11].trim();
        if (!buyerAddr4.isEmpty())
        {
            FieldContentValidator validateBuyerAddr4 = null;
            validateBuyerAddr4 = new LengthValidator(100,validateBuyerAddr4);
            FileParserUtil.getInstance().addError(errors, validateBuyerAddr4.validate(buyerAddr4, getPrefixForHeader() + "buyerAddr4"));
        }
        
        String buyerCity = content[12].trim();
        if (!buyerCity.isEmpty())
        {
            FieldContentValidator validateBuyerCity = null;
            validateBuyerCity = new LengthValidator(50,validateBuyerCity);
            FileParserUtil.getInstance().addError(errors, validateBuyerCity.validate(buyerCity, getPrefixForHeader() + "buyerCity"));
        }
        
        String buyerState = content[13].trim();
        if (!buyerState.isEmpty())
        {
            FieldContentValidator validateBuyerState = null;
            validateBuyerState = new LengthValidator(50,validateBuyerState);
            FileParserUtil.getInstance().addError(errors, validateBuyerState.validate(buyerState, getPrefixForHeader() + "buyerState"));
        }
        
        String buyerCountryCode = content[14].trim();
        if (!buyerCountryCode.isEmpty())
        {
            FieldContentValidator validateBuyerCountryCode = null;
            validateBuyerCountryCode = new LengthValidator(2,validateBuyerCountryCode);
            FileParserUtil.getInstance().addError(errors, validateBuyerCountryCode.validate(buyerCountryCode, getPrefixForHeader() + "buyerCountryCode"));
            result = validateBuyerCountryCode.validate(buyerCountryCode, getPrefixForHeader() + "buyerCountryCode");
            if (result == null && !ctryCodes.isEmpty() && !ctryCodes.contains(buyerCountryCode))
            {
                errors.add(getPrefixForHeader() + "[Buyer Country Code] is not a correct country code. ");
            }
        }
        
        String buyerPostalCode = content[15].trim();
        if (!buyerPostalCode.isEmpty())
        {
            FieldContentValidator validateBuyerPostalCode = null;
            validateBuyerPostalCode = new LengthValidator(15,validateBuyerPostalCode);
            FileParserUtil.getInstance().addError(errors, validateBuyerPostalCode.validate(buyerPostalCode, getPrefixForHeader() + "buyerPostalCode"));
        }
        
        String supplierCode = content[16].trim();
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
        
        String supplierName = content[17].trim();
        if (!supplierName.isEmpty())
        {
            FieldContentValidator validateSupplierName = null;
            validateSupplierName = new LengthValidator(100,validateSupplierName);
            FileParserUtil.getInstance().addError(errors, validateSupplierName.validate(supplierName, getPrefixForHeader() + "supplierName"));
        }
        
        String supplierAddr1 = content[18].trim();
        if (!supplierAddr1.isEmpty())
        {
            FieldContentValidator validateSupplierAddr1 = null;
            validateSupplierAddr1 = new LengthValidator(100,validateSupplierAddr1);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "supplierAddr1"));
        }
        
        String supplierAddr2 = content[19].trim();
        if (!supplierAddr2.isEmpty())
        {
            FieldContentValidator validateSupplierAddr2 = null;
            validateSupplierAddr2 = new LengthValidator(100,validateSupplierAddr2);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "supplierAddr2"));
        }
        
        String supplierAddr3 = content[20].trim();
        if (!supplierAddr3.isEmpty())
        {
            FieldContentValidator validateSupplierAddr3 = null;
            validateSupplierAddr3 = new LengthValidator(100,validateSupplierAddr3);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "supplierAddr3"));
        }
        
        String supplierAddr4 = content[21].trim();
        if (!supplierAddr4.isEmpty())
        {
            FieldContentValidator validateSupplierAddr4 = null;
            validateSupplierAddr4 = new LengthValidator(100,validateSupplierAddr4);
            FileParserUtil.getInstance().addError(errors, validateSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "supplierAddr4"));
        }
        
        String supplierCity = content[22].trim();
        if (!supplierCity.isEmpty())
        {
            FieldContentValidator validateSupplierCity = null;
            validateSupplierCity = new LengthValidator(50,validateSupplierCity);
            FileParserUtil.getInstance().addError(errors, validateSupplierCity.validate(supplierCity, getPrefixForHeader() + "supplierCity"));
        }
        
        String supplierState = content[23].trim();
        if (!supplierState.isEmpty())
        {
            FieldContentValidator validateSupplierState = null;
            validateSupplierState = new LengthValidator(50,validateSupplierState);
            FileParserUtil.getInstance().addError(errors, validateSupplierState.validate(supplierState, getPrefixForHeader() + "supplierState"));
        }
        
        String supplierCountryCode = content[24].trim();
        if (!supplierCountryCode.isEmpty())
        {
            FieldContentValidator validateSupplierCountryCode = null;
            validateSupplierCountryCode = new LengthValidator(2,validateSupplierCountryCode);
            FileParserUtil.getInstance().addError(errors, validateSupplierCountryCode.validate(supplierCountryCode, getPrefixForHeader() + "supplierCountryCode"));
            result = validateSupplierCountryCode.validate(supplierCountryCode, getPrefixForHeader() + "supplierCountryCode");
            if (result == null && !ctryCodes.isEmpty() && !ctryCodes.contains(supplierCountryCode))
            {
                errors.add(getPrefixForHeader() + "[Supplier Country Code] is not a correct country code. ");
            }
        }
        
        String supplierPostalCode = content[25].trim();
        if (!supplierPostalCode.isEmpty())
        {
            FieldContentValidator validateSupplierPostalCode = null;
            validateSupplierPostalCode = new LengthValidator(15,validateSupplierPostalCode);
            FileParserUtil.getInstance().addError(errors, validateSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "supplierPostalCode"));
        }
        
        String storeCode = content[26].trim();
        if (!storeCode.isEmpty())
        {
            FieldContentValidator validateStoreCode = null;
            validateStoreCode = new LengthValidator(20,validateStoreCode);
            FileParserUtil.getInstance().addError(errors, validateStoreCode.validate(storeCode, getPrefixForHeader() + "storeCode"));
        }
        
        String storeName = content[27].trim();
        if (!storeName.isEmpty())
        {
            FieldContentValidator validateStoreName = null;
            validateStoreName = new LengthValidator(100,validateStoreName);
            FileParserUtil.getInstance().addError(errors, validateStoreName.validate(storeName, getPrefixForHeader() + "storeName"));
        }
        
        String storeAddr1 = content[28].trim();
        if (!storeAddr1.isEmpty())
        {
            FieldContentValidator validateStoreAddr1 = null;
            validateStoreAddr1 = new LengthValidator(100,validateStoreAddr1);
            FileParserUtil.getInstance().addError(errors, validateStoreAddr1.validate(storeAddr1, getPrefixForHeader() + "storeAddr1"));
        }
        
        String storeAddr2 = content[29].trim();
        if (!storeAddr2.isEmpty())
        {
            FieldContentValidator validateStoreAddr2 = null;
            validateStoreAddr2 = new LengthValidator(100,validateStoreAddr2);
            FileParserUtil.getInstance().addError(errors, validateStoreAddr2.validate(storeAddr2, getPrefixForHeader() + "storeAddr2"));
        }
        
        String storeAddr3 = content[30].trim();
        if (!storeAddr3.isEmpty())
        {
            FieldContentValidator validateStoreAddr3 = null;
            validateStoreAddr3 = new LengthValidator(100,validateStoreAddr3);
            FileParserUtil.getInstance().addError(errors, validateStoreAddr3.validate(storeAddr3, getPrefixForHeader() + "storeAddr3"));
        }
        
        String storeAddr4 = content[31].trim();
        if (!storeAddr4.isEmpty())
        {
            FieldContentValidator validateStoreAddr4 = null;
            validateStoreAddr4 = new LengthValidator(100,validateStoreAddr4);
            FileParserUtil.getInstance().addError(errors, validateStoreAddr4.validate(storeAddr4, getPrefixForHeader() + "storeAddr4"));
        }
        
        String storeCity = content[32].trim();
        if (!storeCity.isEmpty())
        {
            FieldContentValidator validateStoreCity = null;
            validateStoreCity = new LengthValidator(50,validateStoreCity);
            FileParserUtil.getInstance().addError(errors, validateStoreCity.validate(storeCity, getPrefixForHeader() + "storeCity"));
        }
        
        String storeState = content[33].trim();
        if (!storeState.isEmpty())
        {
            FieldContentValidator validateStoreState = null;
            validateStoreState = new LengthValidator(50,validateStoreState);
            FileParserUtil.getInstance().addError(errors, validateStoreState.validate(storeState, getPrefixForHeader() + "storeState"));
        }
        
        String storeCountryCode = content[34].trim();
        if (!storeCountryCode.isEmpty())
        {
            FieldContentValidator validateStoreCountryCode = null;
            validateStoreCountryCode = new LengthValidator(2,validateStoreCountryCode);
            FileParserUtil.getInstance().addError(errors, validateStoreCountryCode.validate(storeCountryCode, getPrefixForHeader() + "storeCountryCode"));
            result = validateStoreCountryCode.validate(storeCountryCode, getPrefixForHeader() + "storeCountryCode");
            if (result == null && !ctryCodes.isEmpty() && !ctryCodes.contains(storeCountryCode))
            {
                errors.add(getPrefixForHeader() + "[Store Country Code] is not a correct country code. ");
            }
        }
        
        String storePostalCode = content[35].trim();
        if (!storePostalCode.isEmpty())
        {
            FieldContentValidator validateStorePostalCode = null;
            validateStorePostalCode = new LengthValidator(15,validateStorePostalCode);
            FileParserUtil.getInstance().addError(errors, validateStorePostalCode.validate(storePostalCode, getPrefixForHeader() + "storePostalCode"));
        }
        
        String totalQty = content[36].trim();
        if (!totalQty.isEmpty())
        {
            FieldContentValidator validateTotalQty = null;
            validateTotalQty = new NoSpaceValidator(validateTotalQty);
            validateTotalQty = new NumberValidator(validateTotalQty);
            validateTotalQty = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalQty);
            FileParserUtil.getInstance().addError(errors, validateTotalQty.validate(totalQty, getPrefixForHeader() + "totalQty"));
        }
        
        String totalGrossSalesAmount = content[37].trim();
        String grossResult = null;
        if (!totalGrossSalesAmount.isEmpty())
        {
            FieldContentValidator validateTotalGrossSalesAmount = null;
            validateTotalGrossSalesAmount = new NoSpaceValidator(validateTotalGrossSalesAmount);
            validateTotalGrossSalesAmount = new NumberValidator(validateTotalGrossSalesAmount);
            validateTotalGrossSalesAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalGrossSalesAmount);
            FileParserUtil.getInstance().addError(errors, validateTotalGrossSalesAmount.validate(totalGrossSalesAmount, getPrefixForHeader() + "totalGrossSalesAmount"));
            grossResult = validateTotalGrossSalesAmount.validate(totalGrossSalesAmount, getPrefixForHeader() + "totalGrossSalesAmount");
        }
        
        String totalDiscountAmount = content[38].trim();
        String discountResult = null;
        if (!totalDiscountAmount.isEmpty())
        {
            FieldContentValidator validateTotalDiscountAmount = null;
            validateTotalDiscountAmount = new NoSpaceValidator(validateTotalDiscountAmount);
            validateTotalDiscountAmount = new NumberValidator(validateTotalDiscountAmount);
            validateTotalDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalDiscountAmount);
            FileParserUtil.getInstance().addError(errors, validateTotalDiscountAmount.validate(totalDiscountAmount, getPrefixForHeader() + "totalDiscountAmount"));
            discountResult = validateTotalDiscountAmount.validate(totalDiscountAmount, getPrefixForHeader() + "totalDiscountAmount");
        }
        if (null == discountResult || discountResult.isEmpty())
        {
            header.setTotalDiscountAmount(totalDiscountAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalDiscountAmount));
        }
        
        String totalVatAmount = content[39].trim();
        String vatResult = null;
        if (!totalVatAmount.isEmpty())
        {
            FieldContentValidator validateTotalVatAmount = null;
            validateTotalVatAmount = new NoSpaceValidator(validateTotalVatAmount);
            validateTotalVatAmount = new NumberValidator(validateTotalVatAmount);
            validateTotalVatAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateTotalVatAmount);
            FileParserUtil.getInstance().addError(errors, validateTotalVatAmount.validate(totalVatAmount, getPrefixForHeader() + "totalVatAmount"));
            vatResult = validateTotalVatAmount.validate(totalVatAmount, getPrefixForHeader() + "totalVatAmount");
        }
        if (null == vatResult || vatResult.isEmpty())
        {
            header.setTotalVatAmount(totalVatAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalVatAmount));
        }
        
        String totalNetSalesAmount = content[40].trim();
        String netResult = null;
        if (!totalNetSalesAmount.isEmpty())
        {
            FieldContentValidator validateAdditionalDiscountPercent = null;
            validateAdditionalDiscountPercent = new NoSpaceValidator(validateAdditionalDiscountPercent);
            validateAdditionalDiscountPercent = new NumberValidator(validateAdditionalDiscountPercent);
            validateAdditionalDiscountPercent = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateAdditionalDiscountPercent);
            FileParserUtil.getInstance().addError(errors, validateAdditionalDiscountPercent.validate(totalNetSalesAmount, getPrefixForHeader() + "totalNetSalesAmount"));
            netResult = validateAdditionalDiscountPercent.validate(totalNetSalesAmount, getPrefixForHeader() + "totalNetSalesAmount");
        }
        if (null == netResult || netResult.isEmpty())
        {
            header.setTotalNetSalesAmount(totalNetSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalNetSalesAmount));
        }
        
        if (isValidation && (null == grossResult || grossResult.isEmpty()) && (null == discountResult || discountResult.isEmpty()) 
            && (null == vatResult || vatResult.isEmpty()) && (null == netResult || netResult.isEmpty()))
        {
            BigDecimal grossAmount = totalGrossSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalGrossSalesAmount);
            BigDecimal discountAmount = totalDiscountAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalDiscountAmount);
            BigDecimal vatAmount = totalVatAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalVatAmount);
            BigDecimal netAmount = totalNetSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(totalNetSalesAmount);
            
            if (grossAmount.compareTo(discountAmount.add(vatAmount).add(netAmount)) != 0)
            {
                errors.add(getPrefixForHeader() + "TotalGrossSalesAmount should equal totalDiscountAmount + totalVatAmount + totalNetSalesAmount");
            }
        }
        
        String periodStartDate = content[41].trim();
        FieldContentValidator validatePeriodStartDate = null;
        validatePeriodStartDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePeriodStartDate);
        FileParserUtil.getInstance().addError(errors, validatePeriodStartDate.validate(periodStartDate, getPrefixForHeader() + "periodStartDate"));
        String startResult = validatePeriodStartDate.validate(periodStartDate, getPrefixForHeader() + "periodStartDate");
        
        String periodEndDate = content[42].trim();
        FieldContentValidator validatePeriodEndDate = null;
        validatePeriodEndDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePeriodEndDate);
        FileParserUtil.getInstance().addError(errors, validatePeriodEndDate.validate(periodEndDate, getPrefixForHeader() + "periodEndDate"));
        String endResult = validatePeriodEndDate.validate(periodEndDate, getPrefixForHeader() + "periodEndDate");
        
        if ((null == startResult || startResult.isEmpty()) && (null == endResult || endResult.isEmpty()))
        {
            Date start = DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().convertStringToDate(periodStartDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
            Date end = DateUtil.getInstance().getLastTimeOfDay(DateUtil.getInstance().convertStringToDate(periodEndDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
            if (null != start && null != end && end.before(start))
            {
                errors.add(getPrefixForHeader() + "[Period Date] End date should not earlier than start date ");
            }
        }
        if (null == startResult || startResult.isEmpty())
        {
            header.setPeriodStartDate(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().convertStringToDate(periodStartDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS)));
        }
        if ( null == endResult || endResult.isEmpty())
        {
            header.setPeriodEndDate(DateUtil.getInstance().getLastTimeOfDay(DateUtil.getInstance().convertStringToDate(periodEndDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS)));
        }
        return header;
    }
    
    private SalesDetailHolder validateDetail(SalesHeaderHolder header, String[] content, List<String> errors, int no ,Map<String ,List<Date>> dMap, boolean isValidation) throws Exception
    {
        SalesDetailHolder detail = new SalesDetailHolder();
        String salesNo = content[1].trim();
        FieldContentValidator validateSalesNo = null;
        validateSalesNo = new EmptyValidator();
        validateSalesNo = new LengthValidator(20, validateSalesNo);
        validateSalesNo = new SpecialCharValidator(validateSalesNo, false, header.getSalesNo());
        FileParserUtil.getInstance().addError(errors, validateSalesNo.validate(salesNo, getPrefixForDetail(no) + "salesNo"));
        
        String seqNo = content[2].trim();
        FieldContentValidator validateSeqNo = null;
        validateSeqNo = new EmptyValidator();
        validateSeqNo = new IntegerValidator(validateSeqNo);
        validateSeqNo = new MinValidator(Integer.valueOf(1), validateSeqNo);
        validateSeqNo = new MaxValidator(Integer.valueOf(9999), validateSeqNo);
        FileParserUtil.getInstance().addError(errors, validateSeqNo.validate(seqNo, getPrefixForDetail(no) + "seqNo"));
        
        String posId = content[3].trim();
        FieldContentValidator validatePosId = null;
        validatePosId = new LengthValidator(20, validatePosId);
        FileParserUtil.getInstance().addError(errors, validatePosId.validate(posId, getPrefixForDetail(no) + "posId"));
        
        String receiptNo = content[4].trim();
        FieldContentValidator validateReceiptNo = null;
        validateReceiptNo = new LengthValidator(20, validateReceiptNo);
        FileParserUtil.getInstance().addError(errors, validateReceiptNo.validate(receiptNo, getPrefixForDetail(no) + "receiptNo"));
        
        String receiptDate = content[5].trim();
        FieldContentValidator validateReceiptDate = null;
        validateReceiptDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateReceiptDate);
        FileParserUtil.getInstance().addError(errors, validateReceiptDate.validate(receiptDate, getPrefixForDetail(no) + "receiptDate"));
        
        String businessDate = content[6].trim();
        FieldContentValidator validateBusinessDate = null;
        validateBusinessDate = new EmptyValidator();
        validateBusinessDate = new DateValidator(DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateBusinessDate);
        FileParserUtil.getInstance().addError(errors, validateBusinessDate.validate(businessDate, getPrefixForDetail(no) + "businessDate"));
        String dateResult = validateBusinessDate.validate(businessDate, getPrefixForDetail(no) + "businessDate");
        if (null == dateResult)
        {
            Date busDate = DateUtil.getInstance().convertStringToDate(businessDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS);
            if (null != header.getPeriodStartDate() && busDate.before(header.getPeriodStartDate()))
            {
                errors.add(getPrefixForDetail(no) + "[Business Date] Business date should not earlier than start date in header");
            }
            if (null != header.getPeriodEndDate() && header.getPeriodEndDate().before(busDate))
            {
                errors.add(getPrefixForDetail(no) + "[Business Date] Business date should not later than end date in header");
            }
        }
        
        String buyerItemCode = content[7].trim();
        FieldContentValidator validateBuyerItemCode = null;
        validateBuyerItemCode = new EmptyValidator();
        validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
        FileParserUtil.getInstance().addError(errors, validateBuyerItemCode.validate(buyerItemCode, getPrefixForDetail(no) + "buyerItemCode"));
        
        String supplierItemCode = content[8].trim();
        FieldContentValidator validateSupplierItemCode = null;
        validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
        FileParserUtil.getInstance().addError(errors, validateSupplierItemCode.validate(supplierItemCode, getPrefixForDetail(no) + "supplierItemCode"));
        
        String barCode = content[9].trim();
        FieldContentValidator validateBarCode = null;
        validateBarCode = new LengthValidator(50, validateBarCode);
        FileParserUtil.getInstance().addError(errors, validateBarCode.validate(barCode, getPrefixForDetail(no) + "barCode"));
        
        String itemDesc = content[10].trim();
        FieldContentValidator validateItemDesc = null;
        validateItemDesc = new EmptyValidator();
        validateItemDesc = new LengthValidator(100, validateItemDesc);
        FileParserUtil.getInstance().addError(errors, validateItemDesc.validate(itemDesc, getPrefixForDetail(no) + "itemDesc"));
        
        String brand = content[11].trim();
        FieldContentValidator validateBrand = null;
        validateBrand = new LengthValidator(20, validateBrand);
        FileParserUtil.getInstance().addError(errors, validateBrand.validate(brand, getPrefixForDetail(no) + "brand"));
        
        String deptCode = content[12].trim();
        if (!deptCode.isEmpty())
        {
            FieldContentValidator validateDeptCode = null;
            validateDeptCode = new LengthValidator(20,validateDeptCode);
            FileParserUtil.getInstance().addError(errors, validateDeptCode.validate(deptCode, getPrefixForHeader() + "deptCode"));
        }
        
        String deptName = content[13].trim();
        if (!deptName.isEmpty())
        {
            FieldContentValidator validateDeptName = null;
            validateDeptName = new LengthValidator(100,validateDeptName);
            FileParserUtil.getInstance().addError(errors, validateDeptName.validate(deptName, getPrefixForHeader() + "deptName"));
        }
        
        String subDeptCode = content[14].trim();
        if (!subDeptCode.isEmpty())
        {
            FieldContentValidator validateSubDeptCode = null;
            validateSubDeptCode = new LengthValidator(20,validateSubDeptCode);
            FileParserUtil.getInstance().addError(errors, validateSubDeptCode.validate(subDeptCode, getPrefixForHeader() + "subDeptCode"));
        }
        
        String subDeptName = content[15].trim();
        if (!subDeptName.isEmpty())
        {
            FieldContentValidator validateSubDeptName = null;
            validateSubDeptName = new LengthValidator(100,validateSubDeptName);
            FileParserUtil.getInstance().addError(errors, validateSubDeptName.validate(subDeptName, getPrefixForHeader() + "subDeptName"));
        }
        
        String colourCode = content[16].trim();
        FieldContentValidator validateColourCode = null;
        validateColourCode = new LengthValidator(20, validateColourCode);
        FileParserUtil.getInstance().addError(errors, validateColourCode.validate(colourCode, getPrefixForDetail(no) + "colourCode"));
        
        String colourDesc = content[17].trim();
        FieldContentValidator validateColourDesc = null;
        validateColourDesc = new LengthValidator(50, validateColourDesc);
        FileParserUtil.getInstance().addError(errors, validateColourDesc.validate(colourDesc, getPrefixForDetail(no) + "colourDesc"));
        
        String sizeCode = content[18].trim();
        FieldContentValidator validateSizeCode = null;
        validateSizeCode = new LengthValidator(20, validateSizeCode);
        FileParserUtil.getInstance().addError(errors, validateSizeCode.validate(sizeCode, getPrefixForDetail(no) + "sizeCode"));
        
        String sizeDesc = content[19].trim();
        FieldContentValidator validateSizeDesc = null;
        validateSizeDesc = new LengthValidator(50, validateSizeDesc);
        FileParserUtil.getInstance().addError(errors, validateSizeDesc.validate(sizeDesc, getPrefixForDetail(no) + "sizeDesc"));
            
        String packingFactor = content[20].trim();
        FieldContentValidator validatePackingFactor = null;
        validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
        validatePackingFactor = new NumberValidator(validatePackingFactor);
        validatePackingFactor = new MaxValidator(BigDecimal.valueOf(999999.99), validatePackingFactor);
        FileParserUtil.getInstance().addError(errors, validatePackingFactor.validate(packingFactor, getPrefixForDetail(no) + "packingFactor"));
        
        String salesBaseUnit = content[21].trim();
        FieldContentValidator validateSalesBaseUnit = null;
        validateSalesBaseUnit = new EmptyValidator();
        validateSalesBaseUnit = new SpecialCharValidator(validateSalesBaseUnit,false, "P", "U");
        FileParserUtil.getInstance().addError(errors, validateSalesBaseUnit.validate(salesBaseUnit, getPrefixForDetail(no) + "salesBaseUnit"));
        
        String salesUom = content[22].trim();
        FieldContentValidator validateSalesUom = null;
        validateSalesUom = new LengthValidator(20, validateSalesUom);
        FileParserUtil.getInstance().addError(errors, validateSalesUom.validate(salesUom, getPrefixForDetail(no) + "salesUom"));
        
        String salesQty = content[23].trim();
        FieldContentValidator validateSalesQty = null;
        validateSalesQty = new EmptyValidator();
        validateSalesQty = new NoSpaceValidator(validateSalesQty);
        validateSalesQty = new NumberValidator(validateSalesQty);
        validateSalesQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateSalesQty);
        FileParserUtil.getInstance().addError(errors, validateSalesQty.validate(salesQty, getPrefixForDetail(no) + "salesQty"));
        String qtyResult = validateSalesQty.validate(salesQty, getPrefixForDetail(no) + "salesQty");
        
        String itemCost = content[24].trim();
        if (!itemCost.isEmpty())
        {
            FieldContentValidator validateItemCost = null;
            validateItemCost = new NoSpaceValidator(validateItemCost);
            validateItemCost = new NumberValidator(validateItemCost);
            validateItemCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemCost);
            FileParserUtil.getInstance().addError(errors, validateItemCost.validate(itemCost, getPrefixForDetail(no) + "itemCost"));
        }
        
        String salesPrice = content[25].trim();
        FieldContentValidator validateSalesPrice = null;
        validateSalesPrice = new NoSpaceValidator(validateSalesPrice);
        validateSalesPrice = new NumberValidator(validateSalesPrice);
        validateSalesPrice = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateSalesPrice);
        FileParserUtil.getInstance().addError(errors, validateSalesPrice.validate(salesPrice, getPrefixForDetail(no) + "salesPrice"));
        String priceResult = validateSalesPrice.validate(salesPrice, getPrefixForDetail(no) + "salesPrice");
        
        String itemSalesAmount = content[26].trim();
        String itemAmtResult = null;
        if (!itemSalesAmount.isEmpty())
        {
            FieldContentValidator validateSalesAmount = null;
            validateSalesAmount = new NoSpaceValidator(validateSalesAmount);
            validateSalesAmount = new NumberValidator(validateSalesAmount);
            validateSalesAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateSalesAmount);
            FileParserUtil.getInstance().addError(errors, validateSalesAmount.validate(itemSalesAmount, getPrefixForDetail(no) + "itemSalesAmount"));
            itemAmtResult = validateSalesAmount.validate(itemSalesAmount, getPrefixForDetail(no) + "itemSalesAmount");
        }
        
        if (isValidation && (null == qtyResult || qtyResult.isEmpty()) && (null == priceResult || priceResult.isEmpty()) && (null == itemAmtResult || itemAmtResult.isEmpty()))
        {
            BigDecimal qty = salesQty.isEmpty() ? BigDecimal.ZERO : new BigDecimal(salesQty);
            BigDecimal price = salesPrice.isEmpty() ? BigDecimal.ZERO : new BigDecimal(salesPrice);
            BigDecimal itemAmt = itemSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(itemSalesAmount);
            if (itemAmt.compareTo(qty.multiply(price)) != 0)
            {
                errors.add(getPrefixForDetail(no) + "Item Sales Amount should equals Sales Price * Sales Qty");
            }
        }
        
        String salesDiscountAmount = content[27].trim();
        String discountResult = null;
        if (!salesDiscountAmount.isEmpty())
        {
            FieldContentValidator validateSalesDiscountAmount = null;
            validateSalesDiscountAmount = new NoSpaceValidator(validateSalesDiscountAmount);
            validateSalesDiscountAmount = new NumberValidator(validateSalesDiscountAmount);
            validateSalesDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateSalesDiscountAmount);
            FileParserUtil.getInstance().addError(errors, validateSalesDiscountAmount.validate(salesDiscountAmount, getPrefixForDetail(no) + "salesDiscountAmount"));
            discountResult = validateSalesDiscountAmount.validate(salesDiscountAmount, getPrefixForDetail(no) + "salesDiscountAmount");
        }
        if (null == discountResult || discountResult.isEmpty())
        {
            detail.setSalesDiscountAmount(salesDiscountAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(salesDiscountAmount));
        }
        
        String vatAmt = content[28].trim();
        String vatResult = null;
        if (!vatAmt.isEmpty())
        {
            FieldContentValidator validateVatAmt = null;
            validateVatAmt = new NoSpaceValidator(validateVatAmt);
            validateVatAmt = new NumberValidator(validateVatAmt);
            validateVatAmt = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateVatAmt);
            FileParserUtil.getInstance().addError(errors, validateVatAmt.validate(vatAmt, getPrefixForDetail(no) + "vatAmt"));
            vatResult = validateVatAmt.validate(vatAmt, getPrefixForDetail(no) + "vatAmt");
        }
        if (null == vatResult || vatResult.isEmpty())
        {
            detail.setVatAmount(vatAmt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(vatAmt));
        }
        
        String itemNetSalesAmount = content[29].trim();
        String netResult = null;
        if (!itemNetSalesAmount.isEmpty())
        {
            FieldContentValidator validateItemNetSalesAmount = null;
            validateItemNetSalesAmount = new NoSpaceValidator(validateItemNetSalesAmount);
            validateItemNetSalesAmount = new NumberValidator(validateItemNetSalesAmount);
            validateItemNetSalesAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemNetSalesAmount);
            FileParserUtil.getInstance().addError(errors, validateItemNetSalesAmount.validate(itemNetSalesAmount, getPrefixForDetail(no) + "itemNetSalesAmount"));
            netResult = validateItemNetSalesAmount.validate(itemNetSalesAmount, getPrefixForDetail(no) + "itemNetSalesAmount");
        }
        if (null == netResult || netResult.isEmpty())
        {
            detail.setItemNetSalesAmount(itemNetSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(itemNetSalesAmount));
        }
        
        if (isValidation && (null == itemAmtResult || itemAmtResult.isEmpty()) && (null == discountResult || discountResult.isEmpty()) 
            && (null == vatResult || vatResult.isEmpty()) && (null == netResult || netResult.isEmpty()))
        {
            BigDecimal itemAmount = itemSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(itemSalesAmount);
            BigDecimal discountAmount = salesDiscountAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(salesDiscountAmount);
            BigDecimal vat = vatAmt.isEmpty() ? BigDecimal.ZERO : new BigDecimal(vatAmt);
            BigDecimal itemNetAmount = itemNetSalesAmount.isEmpty() ? BigDecimal.ZERO : new BigDecimal(itemNetSalesAmount);
            if (itemAmount.compareTo(discountAmount.add(vat).add(itemNetAmount)) != 0)
            {
                errors.add(getPrefixForDetail(no) + "Item Sales Amount should equals SalesDiscountAmount + VatAmt + ItemNetSalesAmount");
            }
        }
        
        String itemRemarks = content[30].trim();
        if (!itemRemarks.isEmpty())
        {
            FieldContentValidator validateItemRemarks = null;
            validateItemRemarks = new LengthValidator(100,validateItemRemarks);
            FileParserUtil.getInstance().addError(errors, validateItemRemarks.validate(itemRemarks, getPrefixForDetail(no) + "itemRemarks"));
        }
        
        if (dMap.containsKey(buyerItemCode + "@@" + barCode))
        {
            List<Date> dates = dMap.get(buyerItemCode + "@@" + barCode);
            if (dates.contains(DateUtil.getInstance().convertStringToDate(businessDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS)))
            {
                errors.add(getPrefixForDetail(no) + "Buyer Item Code, Business Date and Barcode should not same with other details at the same time.");
            }
            else
            {
                dates.add(DateUtil.getInstance().convertStringToDate(businessDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
            }
        }
        else
        {
            List<Date> dates = new ArrayList<Date>();
            dates.add(DateUtil.getInstance().convertStringToDate(businessDate, DocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS));
            dMap.put(buyerItemCode + "@@" + barCode, dates);
        }
        
        return detail;
    }
    
    
    private void validateLocation(List<String[]>detailList, String[] content, List<String> errors, int no) throws Exception
    {
        String salesNo = content[1].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        FileParserUtil.getInstance().addError(errors, validatePoNo.validate(salesNo, getPrefixForLocation(no) + "salesNo"));
        
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
        
        String locationSalesQty = content[6].trim();
        FieldContentValidator validateLocationSalesQty = null;
        validateLocationSalesQty = new EmptyValidator();
        validateLocationSalesQty = new NoSpaceValidator(validateLocationSalesQty);
        validateLocationSalesQty = new NumberValidator(validateLocationSalesQty);
        validateLocationSalesQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateLocationSalesQty);
        FileParserUtil.getInstance().addError(errors, validateLocationSalesQty.validate(locationSalesQty, getPrefixForLocation(no) + "locationSalesQty"));
        
        String itemCost = content[7].trim();
        FieldContentValidator validateLocationItemCost = null;
        validateLocationItemCost = new NoSpaceValidator(validateLocationItemCost);
        validateLocationItemCost = new NumberValidator(validateLocationItemCost);
        validateLocationItemCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationItemCost);
        FileParserUtil.getInstance().addError(errors, validateLocationItemCost.validate(itemCost, getPrefixForLocation(no) + "locationItemCost"));
        
        String locationSalesPrice = content[8].trim();
        FieldContentValidator validateLocationSalesPrice = null;
        validateLocationSalesPrice = new NoSpaceValidator(validateLocationSalesPrice);
        validateLocationSalesPrice = new NumberValidator(validateLocationSalesPrice);
        validateLocationSalesPrice = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationSalesPrice);
        FileParserUtil.getInstance().addError(errors, validateLocationSalesPrice.validate(locationSalesPrice, getPrefixForLocation(no) + "locationSalesPrice"));
        
        String locationSalesAmt = content[9].trim();
        FieldContentValidator validateLocationSalesAmount = null;
        validateLocationSalesAmount = new NoSpaceValidator(validateLocationSalesAmount);
        validateLocationSalesAmount = new NumberValidator(validateLocationSalesAmount);
        validateLocationSalesAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationSalesAmount);
        FileParserUtil.getInstance().addError(errors, validateLocationSalesAmount.validate(locationSalesAmt, getPrefixForLocation(no) + "locationSalesAmount"));
        
        String locationDiscAmt = content[10].trim();
        FieldContentValidator validateLocationDiscAmt = null;
        validateLocationDiscAmt = new NoSpaceValidator(validateLocationDiscAmt);
        validateLocationDiscAmt = new NumberValidator(validateLocationDiscAmt);
        validateLocationDiscAmt = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationDiscAmt);
        FileParserUtil.getInstance().addError(errors, validateLocationDiscAmt.validate(locationDiscAmt, getPrefixForLocation(no) + "locationDiscountAmount"));
        
        String locationSalesVatAmt = content[11].trim();
        FieldContentValidator validateLocationSalesVatAmt = null;
        validateLocationSalesVatAmt = new NoSpaceValidator(validateLocationSalesVatAmt);
        validateLocationSalesVatAmt = new NumberValidator(validateLocationSalesVatAmt);
        validateLocationSalesVatAmt = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationSalesVatAmt);
        FileParserUtil.getInstance().addError(errors, validateLocationSalesVatAmt.validate(locationSalesVatAmt, getPrefixForLocation(no) + "locationSalesVatAmount"));
        
        String locationSalesNetAmt = content[12].trim();
        FieldContentValidator validateLocationSalesNetAmt = null;
        validateLocationSalesNetAmt = new NoSpaceValidator(validateLocationSalesNetAmt);
        validateLocationSalesNetAmt = new NumberValidator(validateLocationSalesNetAmt);
        validateLocationSalesNetAmt = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateLocationSalesNetAmt);
        FileParserUtil.getInstance().addError(errors, validateLocationSalesNetAmt.validate(locationSalesNetAmt, getPrefixForLocation(no) + "locationNetAmount"));
        
        
        String locationContactTel = content[13].trim();
        if (!locationContactTel.isEmpty())
        {
            FieldContentValidator validateLocationContactTel = null;
            validateLocationContactTel = new LengthValidator(30,validateLocationContactTel);
            FileParserUtil.getInstance().addError(errors, validateLocationContactTel.validate(locationContactTel, getPrefixForHeader() + "locationContactTel"));
        }
        
        String lineRefNo = content[14].trim();
        FieldContentValidator validateLineRefNo = null;
        if (!lineRefNo.isEmpty())
        {
            validateLineRefNo = new LengthValidator(20,validateLineRefNo);
            FileParserUtil.getInstance().addError(errors, validateLineRefNo.validate(lineRefNo, getPrefixForHeader() + "lineRefNo"));
        }
    }


    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        SalesDocMsg salesDocMsg = (SalesDocMsg)docMsg;
        SalesHolder salesHolder = new SalesHolder();
        SalesHeaderHolder salesHeader = null;
        
        salesHeader = new SalesHeaderHolder();
        Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(new ByteArrayInputStream(input));
        List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
        String salesNo = headerList.get(0)[1].trim();
        salesHeader.setSalesNo(salesNo);
        salesHolder.setSalesHeader(salesHeader);
        salesDocMsg.setData(salesHolder);
    }

}
