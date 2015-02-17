//*****************************************************************************
//
// File Name       :  CanonicalInvFileValidator.java
// Date Created    :  Sep 24, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Sep 24, 2013 9:47:34 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

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






import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.eai.file.canonical.GrnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.InvDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.InvFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MinValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CanonicalInvFileValidator extends InvFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CanonicalInvFileValidator.class);
    private static final int HEADER_COLUMN_COUNT = 54;
    private static final int DETAIL_COLUMN_COUNT = 28;
    
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private InvDocFileHandler canonicalInvDocFileHandler;
    @Autowired private CustomAppConfigHelper appConfig;
    
    @Override
    public List<String> validateFile(DocMsg docMsg, byte[] input) throws Exception
    {
        List<String> errorMessages = null;
        InputStream inputStream = null;
        try
        {
            inputStream = new ByteArrayInputStream(input);
            Map<String, List<String[]>> map = FileParserUtil.getInstance()
                .readLinesAndGroupByRecordType(inputStream);

            errorMessages = new ArrayList<String>();
            //record type is 'HDR'
            List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
            //record type is 'HEX'
            List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            //record type is 'DET'
            List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
            //record type is 'DEX'
            List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
            log.info(":::: Start to validator canonical Inv");
            if(headerList == null || headerList.isEmpty())
            {
                errorMessages.add("Header is expected.");
                return errorMessages;
            }

            if(detailList == null || detailList.isEmpty())
            {
                errorMessages.add("Detail is expected.");
                return errorMessages;
            }

            this.validateHeader(headerList, errorMessages, docMsg);
            if (!errorMessages.isEmpty())
            {
                return errorMessages;
            }

            if(headerExList != null && !headerExList.isEmpty())
            {
                validateCanonicalHeaderExtends(headerExList, errorMessages);
                if (!errorMessages.isEmpty())
                {
                    return errorMessages;
                }
            }

            this.validateDetail(headerList, detailList, errorMessages);
            
            if (!errorMessages.isEmpty())
            {
                return errorMessages;
            }
            
            if(detailExList != null && !detailExList.isEmpty())
            {
                validateCanonicalDetailExtends(detailExList, errorMessages);
                if (!errorMessages.isEmpty())
                {
                    return errorMessages;
                }
            }

        }
        finally
        {
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        log.info(":::: End to validator canonical Inv");
        return errorMessages;
    }
    
    private void validateHeader(List<String[]> header, List<String> rlt, DocMsg docMsg)
    {
        String result = null;
        String[] contents = header.get(0);
        
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        if (contents.length != HEADER_COLUMN_COUNT)
        {
            rlt.add("Header row has " + contents.length + " columns. "
                + HEADER_COLUMN_COUNT + " columns expected.");
            
            return ;
        }
        
        int maxInvNoLength = this.getMaxInvNoLength();
        String invNo = contents[1].trim();
        FieldContentValidator validateInvNo = null;
        validateInvNo = new EmptyValidator();
        validateInvNo = new LengthValidator(maxInvNoLength, validateInvNo);
        validateInvNo = new RegexValidator(validateInvNo, validationConfig.getPattern(VLD_PTN_KEY_INVOICE_NO));
        fileParseUtil.addError(rlt, validateInvNo.validate(invNo, getPrefixForHeader() + "[Inv No]"));
        
        
        String docAction = contents[2].trim();
        FieldContentValidator validateDocAction = null;
        validateDocAction = new EmptyValidator();
        validateDocAction = new SpecialCharValidator(validateDocAction, false,
            "A", "R", "D");
        fileParseUtil.addError(rlt, validateDocAction.validate(docAction, getPrefixForHeader() + "[Doc Action]"));
        
        String actionDate = contents[3].trim();
        FieldContentValidator validateActionDate = null;
        validateActionDate = new EmptyValidator();
        validateActionDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateActionDate);
        fileParseUtil.addError(rlt, validateActionDate.validate(actionDate, getPrefixForHeader() + "[Action Date]"));
        
        String invType = contents[4].trim();
        FieldContentValidator validateInvType = null;
        validateInvType = new EmptyValidator();
        validateInvType = new SpecialCharValidator(validateInvType, false,
            InvType.CON.name(), InvType.SOR.name(), InvType.SVC.name());
        fileParseUtil.addError(rlt, validateInvType.validate(invType, getPrefixForHeader() + "[Inv Type]"));
        
        String invDate = contents[5].trim();
        FieldContentValidator validateInvDate = null;
        validateInvDate = new EmptyValidator();
        validateInvDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateInvDate);
        fileParseUtil.addError(rlt, validateInvDate.validate(invDate, getPrefixForHeader() + "[Inv Date]"));
        
        String poNo = contents[6].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        fileParseUtil.addError(rlt, validatePoNo.validate(poNo, getPrefixForHeader() + "[Po No]"));
        
        String poDate = contents[7].trim();
        FieldContentValidator validatePoDate = null;
        validatePoDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePoDate);
        fileParseUtil.addError(rlt, validatePoDate.validate(poDate, getPrefixForHeader() + "[Po Date]"));
        
        String deliveryNo = contents[8].trim();
        FieldContentValidator validateDeliveryNo = null;
        validateDeliveryNo = new LengthValidator(20, validateDeliveryNo);
        validateDeliveryNo = new RegexValidator(validateInvNo, validationConfig.getPattern(VLD_PTN_KEY_DELIVERY_NO));
        fileParseUtil.addError(rlt, validateDeliveryNo.validate(deliveryNo, getPrefixForHeader() + "[Delivery No]"));
        
        
        String deliveryDate = contents[9].trim();
        FieldContentValidator validateDeliveryDate = null;
        validateDeliveryDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateDeliveryDate);
        fileParseUtil.addError(rlt, validateDeliveryDate.validate(deliveryDate, getPrefixForHeader() + "[Create Date]"));
        
        String buyerCode = contents[10].trim();
        FieldContentValidator validateBuyerCode = null;
        validateBuyerCode = new EmptyValidator();
        validateBuyerCode = new LengthValidator(20, validateBuyerCode);
        validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getReceiverCode());
        fileParseUtil.addError(rlt, validateBuyerCode.validate(buyerCode, getPrefixForHeader() + "[Buyer Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_BUYER_CODE)).validate(buyerCode, "buyerCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Buyer Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String buyerName = contents[11].trim();
        FieldContentValidator validateBuyerName = null;
        validateBuyerName = new LengthValidator(100, validateBuyerName);
        fileParseUtil.addError(rlt, validateBuyerName.validate(buyerName, getPrefixForHeader() + "[Buyer Name]"));
        
        String buyerAddr1 = contents[12].trim();
        FieldContentValidator validateBuyerAddr1 = null;
        validateBuyerAddr1 = new LengthValidator(100, validateBuyerAddr1);
        fileParseUtil.addError(rlt, validateBuyerAddr1.validate(buyerAddr1, getPrefixForHeader() + "[Buyer Addr1]"));
        
        String buyerAddr2 = contents[13].trim();
        FieldContentValidator validateBuyerAddr2 = null;
        validateBuyerAddr2 = new LengthValidator(100, validateBuyerAddr2);
        fileParseUtil.addError(rlt, validateBuyerAddr2.validate(buyerAddr2, getPrefixForHeader() + "[Buyer Addr2]"));
        
        String buyerAddr3 = contents[14].trim();
        FieldContentValidator validateBuyerAddr3 = null;
        validateBuyerAddr3 = new LengthValidator(100, validateBuyerAddr3);
        fileParseUtil.addError(rlt, validateBuyerAddr3.validate(buyerAddr3, getPrefixForHeader() + "[Buyer Addr3]"));
        
        String buyerAddr4 = contents[15].trim();
        FieldContentValidator validateBuyerAddr4 = null;
        validateBuyerAddr4 = new LengthValidator(100, validateBuyerAddr4);
        fileParseUtil.addError(rlt, validateBuyerAddr4.validate(buyerAddr4, getPrefixForHeader() + "[Buyer Addr4]"));
        
        String buyerCity = contents[16].trim();
        FieldContentValidator validateBuyerCity = null;
        validateBuyerCity = new LengthValidator(50, validateBuyerCity);
        fileParseUtil.addError(rlt, validateBuyerCity.validate(buyerCity, getPrefixForHeader() + "[Buyer City]"));
        
        String buyerState = contents[17].trim();
        FieldContentValidator validateBuyerState = null;
        validateBuyerState = new LengthValidator(50, validateBuyerState);
        fileParseUtil.addError(rlt, validateBuyerState.validate(buyerState, getPrefixForHeader() + "[Buyer State]"));
        
        String buyerCountyCode = contents[18].trim();
        FieldContentValidator validateBuyerCountyCode = null;
        validateBuyerCountyCode = new LengthValidator(2, validateBuyerCountyCode);
        fileParseUtil.addError(rlt, validateBuyerCountyCode.validate(buyerCountyCode, getPrefixForHeader() + "[Buyer Country Code]"));
        
        String buyerPostalCode = contents[19].trim();
        FieldContentValidator validateBuyerPostalCode = null;
        validateBuyerPostalCode = new LengthValidator(15, validateBuyerPostalCode);
        fileParseUtil.addError(rlt, validateBuyerPostalCode.validate(buyerPostalCode, getPrefixForHeader() + "[Buyer Postal Code]"));
        
        String supplierCode = contents[20].trim();
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getSenderCode());
        fileParseUtil.addError(rlt, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + "[Supplier Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, "supplierCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Supplier Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String supplierName = contents[21].trim();
        FieldContentValidator validateSupplierName = null;
        validateSupplierName = new LengthValidator(100, validateSupplierName);
        fileParseUtil.addError(rlt, validateSupplierName.validate(supplierName, getPrefixForHeader() + "[Supplier Name]"));
        
        String supplierAddr1 = contents[22].trim();
        FieldContentValidator validateSupplierAddr1 = null;
        validateSupplierAddr1 = new LengthValidator(100, validateSupplierAddr1);
        fileParseUtil.addError(rlt, validateSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "[Supplier Addr1]"));
        
        String supplierAddr2 = contents[23].trim();
        FieldContentValidator validateSupplierAddr2 = null;
        validateSupplierAddr2 = new LengthValidator(100, validateSupplierAddr2);
        fileParseUtil.addError(rlt, validateSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "[Supplier Addr2]"));
        
        String supplierAddr3 = contents[24].trim();
        FieldContentValidator validateSupplierAddr3 = null;
        validateSupplierAddr3 = new LengthValidator(100, validateSupplierAddr3);
        fileParseUtil.addError(rlt, validateSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "[Supplier Addr3]"));
        
        String supplierAddr4 = contents[25].trim();
        FieldContentValidator validateSupplierAddr4 = null;
        validateSupplierAddr4 = new LengthValidator(100, validateSupplierAddr4);
        fileParseUtil.addError(rlt, validateSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "[Supplier Addr4]"));
        
        String supplierCity = contents[26].trim();
        FieldContentValidator validateSupplierCity = null;
        validateSupplierCity = new LengthValidator(50, validateSupplierCity);
        fileParseUtil.addError(rlt, validateSupplierCity.validate(supplierCity, getPrefixForHeader() + "[Supplier City]"));
        
        String supplierState = contents[27].trim();
        FieldContentValidator validateSupplierState = null;
        validateSupplierState = new LengthValidator(50, validateSupplierState);
        fileParseUtil.addError(rlt, validateSupplierState.validate(supplierState, getPrefixForHeader() + "[Supplier State]"));
        
        String supplierCountyCode = contents[28].trim();
        FieldContentValidator validateSupplierCountyCode = null;
        validateSupplierCountyCode = new LengthValidator(2, validateSupplierCountyCode);
        fileParseUtil.addError(rlt, validateSupplierCountyCode.validate(supplierCountyCode, getPrefixForHeader() + "[Supplier Country Code]"));
        
        String supplierPostalCode = contents[29].trim();
        FieldContentValidator validateSupplierPostalCode = null;
        validateSupplierPostalCode = new LengthValidator(15, validateSupplierPostalCode);
        fileParseUtil.addError(rlt, validateSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "[Supplier Postal Code]"));
        
        String supplierBizRegNo = contents[30].trim();
        FieldContentValidator validateSupplierBizRegNo = null; 
        validateSupplierBizRegNo = new LengthValidator(50, validateSupplierBizRegNo);
        fileParseUtil.addError(rlt, validateSupplierBizRegNo.validate(supplierBizRegNo, getPrefixForHeader() + "[Supplier Biz Reg No]"));
        
        String supplierVatRegNo = contents[31].trim();
        FieldContentValidator validateSupplierVatRegNo = null; 
        validateSupplierVatRegNo = new LengthValidator(50, validateSupplierVatRegNo);
        fileParseUtil.addError(rlt, validateSupplierVatRegNo.validate(supplierVatRegNo, getPrefixForHeader() + "[Supplier Vat Reg No]"));
        
        String storeCode = contents[32].trim();
        FieldContentValidator validateStoreCode = null;
        validateStoreCode = new LengthValidator(20, validateStoreCode);
        fileParseUtil.addError(rlt, validateStoreCode.validate(storeCode, getPrefixForHeader() + "[Store Code]"));
        
        String storeName = contents[33].trim();
        FieldContentValidator validateStoreName = null;
        validateStoreName = new LengthValidator(100, validateStoreName);
        fileParseUtil.addError(rlt, validateStoreName.validate(storeName, getPrefixForHeader() + "[Store Name]"));

        String storeAddr1 = contents[34].trim();
        FieldContentValidator validateStoreAddr1 = null;
        validateStoreAddr1 = new LengthValidator(100, validateStoreAddr1);
        fileParseUtil.addError(rlt, validateStoreAddr1.validate(storeAddr1, getPrefixForHeader() + "[Store Addr1]"));
        
        String storeAddr2 = contents[35].trim();
        FieldContentValidator validateStoreAddr2 = null;
        validateStoreAddr2 = new LengthValidator(100, validateStoreAddr2);
        fileParseUtil.addError(rlt, validateStoreAddr2.validate(storeAddr2, getPrefixForHeader() + "[Store Addr2]"));
        
        String storeAddr3 = contents[36].trim();
        FieldContentValidator validateStoreAddr3 = null;
        validateStoreAddr3 = new LengthValidator(100, validateStoreAddr3);
        fileParseUtil.addError(rlt, validateStoreAddr3.validate(storeAddr3, getPrefixForHeader() + "[Store Addr3]"));
        
        String storeAddr4 = contents[37].trim();
        FieldContentValidator validateStoreAddr4 = null;
        validateStoreAddr4 = new LengthValidator(100, validateStoreAddr4);
        fileParseUtil.addError(rlt, validateStoreAddr4.validate(storeAddr4, getPrefixForHeader() + "[Store Addr4]"));
        
        String storeCity = contents[38].trim();
        FieldContentValidator validateStoreCity = null;
        validateStoreCity = new LengthValidator(50, validateStoreCity);
        fileParseUtil.addError(rlt, validateStoreCity.validate(storeCity, getPrefixForHeader() + "[Store City]"));
        
        String storeState = contents[39].trim();
        FieldContentValidator validateStoreState = null;
        validateStoreState = new LengthValidator(50, validateStoreState);
        fileParseUtil.addError(rlt, validateStoreState.validate(storeState, getPrefixForHeader() + "[Store State]"));
        
        String storeCountyCode = contents[40].trim();
        FieldContentValidator validateStoreCountyCode = null;
        validateStoreCountyCode = new LengthValidator(2, validateStoreCountyCode);
        fileParseUtil.addError(rlt, validateStoreCountyCode.validate(storeCountyCode, getPrefixForHeader() + "[Store Country Code]"));
        
        String storePostalCode = contents[41].trim();
        FieldContentValidator validateStorePostalCode = null;
        validateStorePostalCode = new LengthValidator(15, validateStorePostalCode);
        fileParseUtil.addError(rlt, validateStorePostalCode.validate(storePostalCode, getPrefixForHeader() + "[Store Postal Code]"));
        
        String payTermCode = contents[42].trim();
        FieldContentValidator validatePayTermCode = null;
        validatePayTermCode = new LengthValidator(20, validatePayTermCode);
        fileParseUtil.addError(rlt, validatePayTermCode.validate(payTermCode, getPrefixForHeader() + "[Pay Term Code]"));
        
        String payTermDesc = contents[43].trim();
        FieldContentValidator validatePayTermDesc = null;
        validatePayTermDesc = new LengthValidator(100, validatePayTermDesc);
        fileParseUtil.addError(rlt, validatePayTermDesc.validate(payTermDesc, getPrefixForHeader() + "[Pay Term Desc]"));
        
        String payInstruct = contents[44].trim();
        FieldContentValidator validatePayInstruct = null;
        validatePayInstruct = new LengthValidator(500, validatePayInstruct);
        fileParseUtil.addError(rlt, validatePayInstruct.validate(payInstruct, getPrefixForHeader() + "[Pay Instruct]"));
        
        String additionalDisAmount = contents[45].trim();
        FieldContentValidator validateAdditionalDisAmount = null;
        validateAdditionalDisAmount = new EmptyValidator();
        validateAdditionalDisAmount = new NoSpaceValidator(validateAdditionalDisAmount);
        validateAdditionalDisAmount = new NumberValidator(validateAdditionalDisAmount);
        validateAdditionalDisAmount = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validateAdditionalDisAmount);
        fileParseUtil.addError(rlt, validateAdditionalDisAmount.validate(additionalDisAmount, getPrefixForHeader() + "[Additional Discount Amount]"));
        
        String additionalDiscPercent = contents[46].trim();
        FieldContentValidator validateAdditionalDiscPercent = null;
        validateAdditionalDiscPercent = new NoSpaceValidator(validateAdditionalDiscPercent);
        validateAdditionalDiscPercent = new NumberValidator(validateAdditionalDiscPercent);
        validateAdditionalDiscPercent = new MaxValidator(new BigDecimal(
            "99.99"), validateAdditionalDiscPercent);
        fileParseUtil.addError(rlt, validateAdditionalDiscPercent.validate(additionalDiscPercent, getPrefixForHeader() + "[Additional Discount Percent]"));
        
        
        String invAmountNoVat = contents[47].trim();
        FieldContentValidator validateInvAmountNoVat = null;
        validateInvAmountNoVat = new EmptyValidator();
        validateInvAmountNoVat = new NoSpaceValidator(validateInvAmountNoVat);
        validateInvAmountNoVat = new NumberValidator(validateInvAmountNoVat);
        validateInvAmountNoVat = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validateInvAmountNoVat);
        fileParseUtil.addError(rlt, validateInvAmountNoVat.validate(invAmountNoVat, getPrefixForHeader() + "[Inv Amount No Vat]"));
        
        
        String vatAmount = contents[48].trim();
        FieldContentValidator validateVatAmount = null;
        validateVatAmount = new EmptyValidator();
        validateVatAmount = new NoSpaceValidator(validateVatAmount);
        validateVatAmount = new NumberValidator(validateVatAmount);
        validateVatAmount = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validateVatAmount);
        fileParseUtil.addError(rlt, validateVatAmount.validate(vatAmount, getPrefixForHeader() + "[Vat Amount]"));
        
        String invAmountWithVat = contents[49].trim();
        FieldContentValidator validateInvAmountWithVat = null;
        validateInvAmountWithVat = new EmptyValidator();
        validateInvAmountWithVat = new NoSpaceValidator(validateInvAmountWithVat);
        validateInvAmountWithVat = new NumberValidator(validateInvAmountWithVat);
        validateInvAmountWithVat = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validateInvAmountWithVat);
        fileParseUtil.addError(rlt, validateInvAmountWithVat.validate(invAmountWithVat, getPrefixForHeader() + "[Inv Amount With Vat]"));
        
        String vatRate = contents[50].trim();
        FieldContentValidator validateVatRate = null;
        validateVatRate = new EmptyValidator();
        validateVatRate = new NoSpaceValidator(validateVatRate);
        validateVatRate = new NumberValidator(validateVatRate);
        validateVatRate = new MaxValidator(new BigDecimal(
            "99.99"), validateVatRate);
        fileParseUtil.addError(rlt, validateVatRate.validate(vatRate, getPrefixForHeader() + "[Vat Rate]"));
        
        String invRemarks = contents[51].trim();
        FieldContentValidator validateInvRemarks = null;
        validateInvRemarks = new LengthValidator(500, validateInvRemarks);
        fileParseUtil.addError(rlt, validateInvRemarks.validate(invRemarks, getPrefixForHeader() + "[Inv Remarks]"));
        
        String cashDisAmount = contents[52].trim();
        FieldContentValidator validateCashDisAmount = null;
        validateCashDisAmount = new EmptyValidator();
        validateCashDisAmount = new NoSpaceValidator(validateCashDisAmount);
        validateCashDisAmount = new NumberValidator(validateCashDisAmount);
        validateCashDisAmount = new MaxValidator(new BigDecimal(
            "99999999999.9999"), validateCashDisAmount);
        fileParseUtil.addError(rlt, validateCashDisAmount.validate(cashDisAmount, getPrefixForHeader() + "[Additional Discount Amount]"));
        
        String cashDiscPercent = contents[53].trim();
        FieldContentValidator validateCashDiscPercent = null;
        validateCashDiscPercent = new NoSpaceValidator(validateCashDiscPercent);
        validateCashDiscPercent = new NumberValidator(validateCashDiscPercent);
        validateCashDiscPercent = new MaxValidator(new BigDecimal(
            "99.99"), validateCashDiscPercent);
        fileParseUtil.addError(rlt, validateCashDiscPercent.validate(cashDiscPercent, getPrefixForHeader() + "[Additional Discount Percent]"));
        
    }
    
    private void validateDetail(List<String[]> header, List<String[]> details, List<String> rlt)
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        BigDecimal netAmounts = BigDecimal.ZERO;
        
        for (int i = 0 ; i < details.size(); i++)
        {
            String[] detailContents = details.get(i);
            if (detailContents.length != DETAIL_COLUMN_COUNT)
            {
                rlt.add(getPrefixForDetail(i + 1) +" has " + detailContents.length + " columns. "
                    + DETAIL_COLUMN_COUNT + " columns expected.");
                
                return ;
            }
            
            int maxInvNoLength = this.getMaxInvNoLength();
            String invNo = detailContents[1].trim();
            FieldContentValidator validateInvNo = null;
            validateInvNo = new EmptyValidator();
            validateInvNo = new LengthValidator(maxInvNoLength, validateInvNo);
            validateInvNo = new SpecialCharValidator(validateInvNo, false, header.get(0)[1]);
            fileParseUtil.addError(rlt, validateInvNo.validate(invNo, getPrefixForDetail(i + 1) + "[Inv No]"));
            
            String seqNum = detailContents[2].trim();
            FieldContentValidator validateSeqNum = null;
            validateSeqNum = new EmptyValidator();
            validateSeqNum = new NumberValidator(validateSeqNum);
            validateSeqNum = new MinValidator(Integer.valueOf(1), validateSeqNum);
            validateSeqNum = new MaxValidator(Integer.valueOf(9999), validateSeqNum);
            fileParseUtil.addError(rlt, validateSeqNum.validate(seqNum, getPrefixForDetail(i + 1) + "[Detail Seq Num]"));
            
            String buyerItemCode = detailContents[3].trim();
            FieldContentValidator validateBuyerItemCode = null;
            validateBuyerItemCode = new EmptyValidator();
            validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
            fileParseUtil.addError(rlt, validateBuyerItemCode.validate(buyerItemCode, getPrefixForDetail(i + 1) +"[Buyer Item Code]"));
            
            
            String supplierItemCode = detailContents[4].trim();
            FieldContentValidator validateSupplierItemCode = null;
            validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
            fileParseUtil.addError(rlt, validateSupplierItemCode.validate(supplierItemCode, getPrefixForDetail(i + 1) +"[Supplier Item Code]"));
            
            String barcode = detailContents[5].trim();
            FieldContentValidator validateBarcode = null;
            validateBarcode = new LengthValidator(50, validateBarcode);
            fileParseUtil.addError(rlt, validateBarcode.validate(barcode, getPrefixForDetail(i + 1) + "[Barcode]"));
            
            String itemDesc = detailContents[6].trim();
            FieldContentValidator validateItemDesc = null;
            validateItemDesc = new LengthValidator(100, validateItemDesc);
            fileParseUtil.addError(rlt, validateItemDesc.validate(itemDesc, getPrefixForDetail(i + 1) +"[Item Desc]"));
            
            String brand = detailContents[7].trim();
            FieldContentValidator validateBrand = null;
            validateBrand = new LengthValidator(20, validateBrand);
            fileParseUtil.addError(rlt, validateBrand.validate(brand, getPrefixForDetail(i + 1) +"[Brand]"));
            
            String colorCode = detailContents[8].trim();
            FieldContentValidator validateColorCode = null;
            validateColorCode = new LengthValidator(20, validateColorCode);
            fileParseUtil.addError(rlt, validateColorCode.validate(colorCode, getPrefixForDetail(i + 1) +"[Color Code]"));
            
            String colorDesc = detailContents[9].trim();
            FieldContentValidator validateColorDesc = null;
            validateColorDesc = new LengthValidator(50, validateColorDesc);
            fileParseUtil.addError(rlt, validateColorDesc.validate(colorDesc, getPrefixForDetail(i + 1) +"[Color Desc]"));
            
            String sizeCode = detailContents[10].trim();
            FieldContentValidator validateSizeCode = null;
            validateSizeCode = new LengthValidator(20, validateSizeCode);
            fileParseUtil.addError(rlt, validateSizeCode.validate(sizeCode, getPrefixForDetail(i + 1) +"[Size Code]"));
            
            String sizeDesc = detailContents[11].trim();
            FieldContentValidator validateSizeDesc = null;
            validateSizeDesc = new LengthValidator(50, validateSizeDesc);
            fileParseUtil.addError(rlt, validateSizeDesc.validate(sizeDesc, getPrefixForDetail(i + 1) +"[Size Desc]"));
            
            String packingFactor = detailContents[12].trim();
            FieldContentValidator validatePackingFactor = null;
            validatePackingFactor = new EmptyValidator();
            validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
            validatePackingFactor = new NumberValidator(validatePackingFactor);
            validatePackingFactor = new MaxValidator(new BigDecimal(
                "999999.99"), validatePackingFactor);
            fileParseUtil.addError(rlt, validatePackingFactor.validate(packingFactor, getPrefixForDetail(i + 1) +"[Packing Factor]"));
            
            String invBaseUnit = detailContents[13].trim();
            FieldContentValidator validateInvBaseUnit = null;
            validateInvBaseUnit = new SpecialCharValidator(validateInvBaseUnit,
                false, "P", "U");
            fileParseUtil.addError(rlt, validateInvBaseUnit.validate(invBaseUnit, getPrefixForDetail(i + 1) +"[Inv Base Unit]"));
            
            String invUom = detailContents[14].trim();
            FieldContentValidator validateInvUom = null;
            validateInvUom = new EmptyValidator();
            validateInvUom = new LengthValidator(20, validateInvUom);
            fileParseUtil.addError(rlt, validateInvUom.validate(invUom, getPrefixForDetail(i + 1) +"[Inv Uom]"));
            
            
            String invQty = detailContents[15].trim();
            FieldContentValidator validateInvQty = null; 
            validateInvQty = new EmptyValidator();
            validateInvQty = new NoSpaceValidator(validateInvQty);
            validateInvQty = new NumberValidator(validateInvQty);
            validateInvQty = new MaxValidator(new BigDecimal(
                "999999.9999"), validateInvQty);
            fileParseUtil.addError(rlt, validateInvQty.validate(invQty, getPrefixForDetail(i + 1) +"[Inv Qty]"));
            
            String focBaseUnit = detailContents[16].trim();
            FieldContentValidator validateFocBaseUnit = null;
            validateFocBaseUnit = new SpecialCharValidator(validateFocBaseUnit,
                false, "P", "U");
            fileParseUtil.addError(rlt, validateFocBaseUnit.validate(focBaseUnit, getPrefixForDetail(i + 1) +"[Foc Base Unit]"));
            
            String focUom = detailContents[17].trim();
            FieldContentValidator validateFocUom = null;
            validateFocUom = new LengthValidator(20, validateFocUom);
            fileParseUtil.addError(rlt, validateFocUom.validate(focUom, getPrefixForDetail(i + 1) +"[Foc Uom]"));
            
            String focQty = detailContents[18].trim();
            FieldContentValidator validateFocQty = null;
            validateFocQty = new NumberValidator(validateFocQty);
            validateFocQty = new MaxValidator(new BigDecimal(
                 "999999.9999"), validateFocQty);
            fileParseUtil.addError(rlt, validateFocQty.validate(focQty, getPrefixForDetail(i + 1) +"[Foc Qty]"));
            
            String unitPrice = detailContents[19].trim();
            FieldContentValidator validateUnitPrice = null;
            validateUnitPrice = new NoSpaceValidator(validateUnitPrice);
            validateUnitPrice = new NumberValidator(validateUnitPrice);
            validateUnitPrice = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateUnitPrice);
            fileParseUtil.addError(rlt, validateUnitPrice.validate(unitPrice, getPrefixForDetail(i + 1) +"[Unit Price]"));
            
            String discountAmount = detailContents[20].trim();
            FieldContentValidator validateDiscountAmount = null;
            validateDiscountAmount = new EmptyValidator();
            validateDiscountAmount = new NoSpaceValidator(validateDiscountAmount);
            validateDiscountAmount = new NumberValidator(validateDiscountAmount);
            validateDiscountAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateDiscountAmount);
            fileParseUtil.addError(rlt, validateDiscountAmount.validate(discountAmount, getPrefixForDetail(i + 1) +"[Discount Amount]"));
            
            String discountPercent = detailContents[21].trim();
            FieldContentValidator validateDiscountPercent = null;
            validateDiscountPercent = new NoSpaceValidator(validateDiscountPercent);
            validateDiscountPercent = new NumberValidator(validateDiscountPercent);
            validateDiscountPercent = new MaxValidator(new BigDecimal(
                "99.99"), validateDiscountPercent);
            fileParseUtil.addError(rlt, validateDiscountPercent.validate(discountPercent, getPrefixForDetail(i + 1) +"[Discount Percent]"));
            
            String netPrice = detailContents[22].trim();
            FieldContentValidator validateNetPrice = null;
            validateNetPrice = new NoSpaceValidator(validateNetPrice);
            validateNetPrice = new NumberValidator(validateNetPrice);
            validateNetPrice = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateNetPrice);
            fileParseUtil.addError(rlt, validateNetPrice.validate(netPrice, getPrefixForDetail(i + 1) +"[Net Price]"));
            
            String itemAmount = detailContents[23].trim();
            FieldContentValidator validateItemAmount = null;
            validateItemAmount = new EmptyValidator();
            validateItemAmount = new NoSpaceValidator(validateItemAmount);
            validateItemAmount = new NumberValidator(validateItemAmount);
            validateItemAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateItemAmount);
            fileParseUtil.addError(rlt, validateItemAmount.validate(itemAmount, getPrefixForDetail(i + 1) +"[Item Amount]"));
            
            String netAmount = detailContents[24].trim();
            FieldContentValidator validateNetAmount = null;
            validateNetAmount = new EmptyValidator();
            validateNetAmount = new NoSpaceValidator(validateNetAmount);
            validateNetAmount = new NumberValidator(validateNetAmount);
            validateNetAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateNetAmount);
            fileParseUtil.addError(rlt, validateNetAmount.validate(netAmount, getPrefixForDetail(i + 1) +"[Net Amount]"));
            
            String itemShareCost = detailContents[25].trim();
            FieldContentValidator validateItemShareCost = null;
            validateItemShareCost = new NoSpaceValidator(validateItemShareCost);
            validateItemShareCost = new NumberValidator(validateItemShareCost);
            validateItemShareCost = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateItemShareCost);
            fileParseUtil.addError(rlt, validateItemShareCost.validate(itemShareCost, getPrefixForDetail(i + 1) +"[Item Buyer Share Cost]"));
            
            String totalGrossAmount = detailContents[26].trim();
            FieldContentValidator validateTotalGrossAmount = null;
            validateTotalGrossAmount = new NoSpaceValidator(validateTotalGrossAmount);
            validateTotalGrossAmount = new NumberValidator(validateTotalGrossAmount);
            validateTotalGrossAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateTotalGrossAmount);
            fileParseUtil.addError(rlt, validateTotalGrossAmount.validate(totalGrossAmount, getPrefixForDetail(i + 1) +"[Total Gross Amount]"));
            
            String itemRemarks = detailContents[27].trim();
            FieldContentValidator validateItemRemarks = null;
            validateItemRemarks = new LengthValidator(100, validateItemRemarks);
            fileParseUtil.addError(rlt, validateItemRemarks.validate(itemRemarks, getPrefixForDetail(i + 1) +"[Item Remarks]"));
           
            netAmounts = netAmounts.add(new BigDecimal(netAmount));
        }
        
        
        BigDecimal invAmountNoVat = new BigDecimal(header.get(0)[47].trim());
        if (invAmountNoVat.compareTo(netAmounts) != 0)
        {
            fileParseUtil.addError(rlt, "Sum of the item cost does not equal to invoice total amount.");
        }
        
    }

    
    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            canonicalInvDocFileHandler.readFileContent((InvDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
}
