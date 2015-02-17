//*****************************************************************************
//
// File Name       :  FairpriceGrnFileValidator.java
// Date Created    :  Nov 18, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Nov 18, 2013 11:41:15 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.validator.fairprice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.fairprice.GrnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.GrnFileValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.canonical.CanonicalGrnFileValidator;
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
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class FairpriceGrFileValidator extends GrnFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CanonicalGrnFileValidator.class);
    private static int ROW_LENGTH_MIN = 234;
    private static int ROW_LENGTH_MAX = 238;
    private static String HEADER_PREFIX_LINE_ONE = "[Header line 1 ";
    private static String DETAIL_PREFIX_LIEE = "[Detail line ";
    
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private GrnDocFileHandler fairpriceGrnDocFileHandler;
    
    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            fairpriceGrnDocFileHandler.readFileContent((GrnDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }

    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input)
        throws Exception
    {
        log.info("start to validate fairprice gi file.");
        InputStream inputStream = null;
        List<String> errorMsg = null;
        try
        {
            inputStream = new ByteArrayInputStream(input);
            List<String> contents = FileParserUtil.getInstance().readLines(inputStream);
            errorMsg = new ArrayList<String>();
            this.validateHeader(contents.get(0), errorMsg, (GrnDocMsg)docMsg);
            
            if (!errorMsg.isEmpty())
            {
                return errorMsg;
            }
            
            this.validateDetail(contents, errorMsg, (GrnDocMsg)docMsg);
            
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        
        log.info("end to validate fairprice gi file.");
        return errorMsg;
    }
    
    
    private void validateHeader(String content, List<String> rlt, GrnDocMsg docMsg) throws Exception
    {
        if (content.length() < ROW_LENGTH_MIN || content.length() > ROW_LENGTH_MAX)
        {
            rlt.add("line 1 has " + content.length() + " character."
                + ROW_LENGTH_MIN +" - " + ROW_LENGTH_MAX +" characters expected.");
            
            return;
        }
        String result = null;
        
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        String fileType = content.substring(0, 2).trim();
        FieldContentValidator validateFileType = null;
        validateFileType = new EmptyValidator();
        validateFileType = new SpecialCharValidator(validateFileType, false, "GR");
        fileParseUtil.addError(rlt, validateFileType.validate(fileType, HEADER_PREFIX_LINE_ONE + " String 1 to 2 ]"));
        
        
        String grnNo = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(2, 12).trim(), LEADING_ZERO);
        FieldContentValidator validateGrnNo = null;
        validateGrnNo = new EmptyValidator();
        validateGrnNo = new LengthValidator(20, validateGrnNo);
        fileParseUtil.addError(rlt, validateGrnNo.validate(grnNo, HEADER_PREFIX_LINE_ONE + "String from 3 to 12]"));
        
        
        String grnDate = content.substring(46, 54).trim();
        FieldContentValidator validateGrnDate = null;
        validateGrnDate = new EmptyValidator();
        validateGrnDate = new DateValidator(DateUtil.DATE_FORMAT_YYYYMMDD, validateGrnDate);
        fileParseUtil.addError(rlt, validateGrnDate.validate(grnDate, HEADER_PREFIX_LINE_ONE + "String from 47 to 54 ]"));
        
        String poNo = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(12, 22).trim(), LEADING_ZERO);
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        fileParseUtil.addError(rlt, validatePoNo.validate(poNo, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 13 to 22 ]"));
        
        String poDate = content.substring(30, 38).trim();
        FieldContentValidator validatePoDate = null;
        validatePoDate = new EmptyValidator();
        validatePoDate = new DateValidator(DateUtil.DATE_FORMAT_YYYYMMDD, validatePoDate);
        fileParseUtil.addError(rlt, validatePoDate.validate(poDate, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 31 to 38 ]"));
        
        String createDate = content.substring(22, 30).trim();
        FieldContentValidator validateCreateDate = null;
        validateCreateDate = new EmptyValidator();
        validateCreateDate = new DateValidator(DateUtil.DATE_FORMAT_YYYYMMDD, validateCreateDate);
        fileParseUtil.addError(rlt, validateCreateDate.validate(createDate, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 23 to 30 ]"));
        
//        String buyerCode = content.substring(229, 233).trim();
//        FieldContentValidator validateBuyerCode = null;
//        validateBuyerCode = new EmptyValidator();
//        validateBuyerCode = new LengthValidator(20, validateBuyerCode);
//        validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getSenderCode());
//        fileParseUtil.addError(rlt, validateBuyerCode.validate(buyerCode, HEADER_PREFIX_LINE_ONE + "String 230 to 233 ]"));
//        result = new RegexValidator(COMPANY_CODE_REGEX).validate(buyerCode, HEADER_PREFIX_LINE_ONE + "String 230 to 233 ]");
//        if (result != null)
//        {
//            rlt.add(getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String 230 to 233 ] only 'a-z,A-Z,0-9' allowed. ");
//        }
        
        String supplierCode = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(58, 68).trim(), LEADING_ZERO);
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getReceiverCode());
        fileParseUtil.addError(rlt, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 59 to 68 ]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, "supplierCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 59 to 68 ] only 'a-z,A-Z,0-9' allowed. ");
        }
        
        String receiveStoreCode = content.substring(54, 58).trim();
        FieldContentValidator validateReceiveStoreCode = null;
        validateReceiveStoreCode = new LengthValidator(20, validateReceiveStoreCode);
        fileParseUtil.addError(rlt, validateReceiveStoreCode.validate(receiveStoreCode, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String from 55 to 58 ]"));
        
        String itemCount = content.substring(233, content.length()).trim().isEmpty() ? null : content.substring(233, content.length()).trim();
        if (itemCount != null)
        {
            FieldContentValidator validateItemCount = null;
            validateItemCount = new NoSpaceValidator(validateItemCount);
            validateItemCount = new NumberValidator(validateItemCount);
            validateItemCount = new MaxValidator(new BigDecimal(
                    "9999"), validateItemCount);
            fileParseUtil.addError(rlt, validateItemCount.validate(itemCount, getPrefixForHeader() + HEADER_PREFIX_LINE_ONE + "String 234 to 238 ]"));
        }
        
        
        
    }
    
    
    private void validateDetail(List<String> contents, List<String> rlt, GrnDocMsg docMsg)
    {
        String headerGrnNo = StringUtil.getInstance().convertDocNoWithNoLeading(contents.get(0).substring(2, 12).trim(), LEADING_ZERO);
            
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        String result = null;
        int lineNum = 1;
        for (int i = 0 ; i < contents.size(); i++)
        {
            String content = contents.get(i);
            if (content.length() < ROW_LENGTH_MIN || content.length() > ROW_LENGTH_MAX)
            {
                rlt.add("line " + lineNum + " has " + content.length() + " characters. "
                    + ROW_LENGTH_MIN +" - " + ROW_LENGTH_MAX +" characters expected.");
                lineNum ++;
                continue ;
            }
            
            String fileType = content.substring(0, 2).trim();
            FieldContentValidator validateFileType = null;
            validateFileType = new EmptyValidator();
            validateFileType = new SpecialCharValidator(validateFileType, false, "GR");
            fileParseUtil.addError(rlt, validateFileType.validate(fileType, DETAIL_PREFIX_LIEE + " String 1 to 2 ]"));
            
            String grnNo = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(2, 12).trim(), LEADING_ZERO);
            FieldContentValidator validateGrnNo = null;
            validateGrnNo = new EmptyValidator();
            validateGrnNo = new LengthValidator(20, validateGrnNo);
            validateGrnNo = new SpecialCharValidator(validateGrnNo, false, headerGrnNo);
            fileParseUtil.addError(rlt, validateGrnNo.validate(grnNo, DETAIL_PREFIX_LIEE + lineNum + " String 3 to 12 ]"));
            
            String seqNum = content.substring(168, 172).trim();
            FieldContentValidator validateSeqNum = null;
            validateSeqNum = new EmptyValidator();
            validateSeqNum = new NumberValidator(validateSeqNum);
            validateSeqNum = new MinValidator(Integer.valueOf(1), validateSeqNum);
            validateSeqNum = new MaxValidator(Integer.valueOf(9999), validateSeqNum);
            fileParseUtil.addError(rlt, validateSeqNum.validate(seqNum, DETAIL_PREFIX_LIEE + lineNum + " String 169 to 172 ]"));
            
//            String buyerCode = content.substring(229, 233).trim();
//            FieldContentValidator validateBuyerCode = null;
//            validateBuyerCode = new EmptyValidator();
//            validateBuyerCode = new LengthValidator(20, validateBuyerCode);
//            validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getSenderCode());
//            fileParseUtil.addError(rlt, validateBuyerCode.validate(buyerCode, DETAIL_PREFIX_LIEE + lineNum + " String 230 to 233 ]"));
//            result = new RegexValidator(COMPANY_CODE_REGEX).validate(buyerCode, DETAIL_PREFIX_LIEE + lineNum + " String 230 to 233 ]");
//            if (result != null)
//            {
//                rlt.add(getPrefixForHeader() + DETAIL_PREFIX_LIEE + lineNum + " String 230 to 233 ] only 'a-z,A-Z,0-9' allowed. ");
//            }
            
            String supplierCode = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(58, 68).trim(), LEADING_ZERO);
            FieldContentValidator validateSupplierCode = null;
            validateSupplierCode = new EmptyValidator();
            validateSupplierCode = new LengthValidator(20, validateSupplierCode);
            validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getReceiverCode());
            fileParseUtil.addError(rlt, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + DETAIL_PREFIX_LIEE + lineNum + " String from 59 to 68 ]"));
            result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, DETAIL_PREFIX_LIEE + lineNum + " String from 59 to 68 ]");
            if (result != null)
            {
                rlt.add(getPrefixForHeader() + DETAIL_PREFIX_LIEE + lineNum + " String from 59 to 68 ] only 'a-z,A-Z,0-9' allowed. ");
            }
            
            String buyerItemCode = StringUtil.getInstance().convertDocNoWithNoLeading(content.substring(172,190).trim(), LEADING_ZERO);
            FieldContentValidator validateBuyerItemCode = null;
            validateBuyerItemCode = new EmptyValidator();
            validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
            fileParseUtil.addError(rlt, validateBuyerItemCode.validate(buyerItemCode, DETAIL_PREFIX_LIEE + lineNum + " String 173 to 190 ]"));
            
            String barcode = content.substring(190, 208).trim();
            FieldContentValidator validateBarcode = null;
            validateBarcode = new LengthValidator(50, validateBarcode);
            fileParseUtil.addError(rlt, validateBarcode.validate(barcode, DETAIL_PREFIX_LIEE + lineNum + " String 191 to 208]"));
            
            String packingFactor = content.substring(211, 216).trim();
            FieldContentValidator validatePackingFactor = null;
            validatePackingFactor = new EmptyValidator();
            validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
            validatePackingFactor = new NumberValidator(validatePackingFactor);
            validatePackingFactor = new MaxValidator(new BigDecimal(
                    "999999.99"), validatePackingFactor);
            fileParseUtil.addError(rlt, validatePackingFactor.validate(packingFactor, DETAIL_PREFIX_LIEE + lineNum + " String 212 to 216 ]"));
            
            String orderUom = content.substring(208, 211).trim();
            FieldContentValidator validateOrderUom = null;
            validateOrderUom = new EmptyValidator();
            validateOrderUom = new LengthValidator(20, validateOrderUom);
            fileParseUtil.addError(rlt, validateOrderUom.validate(orderUom, DETAIL_PREFIX_LIEE + lineNum + " String 209 to 211 ]"));
            
            String receiveQty = content.substring(216, 229).trim();
            FieldContentValidator validateReceiveQty = null;
            validateReceiveQty = new EmptyValidator();
            validateReceiveQty = new NoSpaceValidator(validateReceiveQty);
            validateReceiveQty = new NumberValidator(validateReceiveQty);
            validateReceiveQty = new MaxValidator(new BigDecimal(
                    "999999.9999"), validateReceiveQty);
            fileParseUtil.addError(rlt, validateReceiveQty.validate(receiveQty, DETAIL_PREFIX_LIEE + lineNum + " String 217 to 229 ]"));
            
            String itemRemarks = content.substring(68, 168).trim();
            FieldContentValidator validateItemRemarks = null;
            validateItemRemarks = new LengthValidator(100, validateItemRemarks);
            fileParseUtil.addError(rlt, validateItemRemarks.validate(itemRemarks, DETAIL_PREFIX_LIEE + lineNum + " String 69 to 168 ]"));
            lineNum ++ ;
        }
    }
}
