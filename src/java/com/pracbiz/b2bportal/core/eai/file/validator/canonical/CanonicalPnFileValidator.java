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

import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.canonical.PnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.PnFileValidator;
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
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CanonicalPnFileValidator extends PnFileValidator implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CanonicalPnFileValidator.class);
    private static int HEADER_COLUMN_COUNT = 23;
    private static int DETAIL_COLUMN_COUNT = 11;
    
    @Autowired private ValidationConfigHelper validationConfig;
    
    @Override
    protected List<String> validateFile(DocMsg docMsg, byte[] input) throws Exception
    {
        List<String> errorMessages = null;
        InputStream inputStream = null;
        try
        {
            inputStream = new ByteArrayInputStream(input);
            Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(inputStream);
          
            errorMessages =  new ArrayList<String>();
            //record type is 'HDR'
            List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
            //record type is 'HEX'
            List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            //record type is 'DET'
            List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
            //record type is 'DEX'
            List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
            log.info("::::: Start to validator Pn. ");
            if (headerList == null || headerList.isEmpty())
            {
                errorMessages.add("Header is expected.");
                return errorMessages;
            }
            
            if (detailList == null || detailList.isEmpty())
            {
                errorMessages.add("Detail is expected.");
                return errorMessages;
            }
            
            this.validateHeader(headerList, errorMessages, docMsg);
            if (!errorMessages.isEmpty())
            {
                return errorMessages;
            }
           
            if (headerExList != null && !headerExList.isEmpty())
            {
                this.validateCanonicalHeaderExtends(headerExList, errorMessages);
                if (!errorMessages.isEmpty())
                {
                    return errorMessages;
                }
            }
            
            this.validateDetail(headerList.get(0)[1], detailList, errorMessages);
            if (!errorMessages.isEmpty())
            {
                return errorMessages;
            }
            
            if (detailExList != null && !detailExList.isEmpty())
            {
                this.validateCanonicalDetailExtends(detailExList, errorMessages);
                if (!errorMessages.isEmpty())
                {
                    return errorMessages;
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
        log.info(":::: End to validator Pn");
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
        
        String pnNo = contents[1].trim();
        FieldContentValidator validatePnNo = null;
        validatePnNo = new EmptyValidator();
        validatePnNo = new LengthValidator(20, validatePnNo);
        fileParseUtil.addError(rlt, validatePnNo.validate(pnNo, getPrefixForHeader() + "[Pn No]"));
        
        
        String docAction = contents[2].trim();
        FieldContentValidator validateDocAction = null;
        validateDocAction = new EmptyValidator();
        validateDocAction = new SpecialCharValidator(validateDocAction, false,
            "A", "R", "D");
        fileParseUtil.addError(rlt, validateDocAction.validate(docAction, getPrefixForHeader() + "[Doc Action]"));
        
        String actionDate = contents[3].trim();
        FieldContentValidator validateActionDate = null;
        validateActionDate = new EmptyValidator();
        validateActionDate = new DateValidator(PnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateActionDate);
        fileParseUtil.addError(rlt, validateActionDate.validate(actionDate, getPrefixForHeader() + "[Action Date]"));
        
        
        String pnDate = contents[4].trim();
        FieldContentValidator validatePnDate = null;
        validatePnDate = new EmptyValidator();
        validatePnDate = new DateValidator(PnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePnDate);
        fileParseUtil.addError(rlt, validatePnDate.validate(pnDate, getPrefixForHeader() + "[Pn Date]"));
        
        String buyerCode = contents[5].trim();
        FieldContentValidator validateBuyerCode = null;
        validateBuyerCode = new EmptyValidator();
        validateBuyerCode = new LengthValidator(20, validateBuyerCode);
        validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getSenderCode());
        fileParseUtil.addError(rlt, validateBuyerCode.validate(buyerCode, getPrefixForHeader() + "[Buyer Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_BUYER_CODE)).validate(buyerCode, "buyerCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Buyer Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String buyerName = contents[6].trim();
        FieldContentValidator validateBuyerName = null;
        validateBuyerName = new LengthValidator(100, validateBuyerName);
        fileParseUtil.addError(rlt, validateBuyerName.validate(buyerName, getPrefixForHeader() + "[Buyer Name]"));
        
        String supplierCode = contents[7].trim();
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getReceiverCode());
        fileParseUtil.addError(rlt, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + "[Supplier Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, "supplierCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Supplier Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        String supplierName = contents[8].trim();
        FieldContentValidator validateSupplierName = null;
        validateSupplierName = new LengthValidator(100, validateSupplierName);
        fileParseUtil.addError(rlt, validateSupplierName.validate(supplierName, getPrefixForHeader() + "[Supplier Name]"));
        
        
        String supplierAddr1 = contents[9].trim();
        FieldContentValidator validateSupplierAddr1 = null;
        validateSupplierAddr1 = new LengthValidator(100, validateSupplierAddr1);
        fileParseUtil.addError(rlt, validateSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "[Supplier Addr1]"));
        
        String supplierAddr2 = contents[10].trim();
        FieldContentValidator validateSupplierAddr2 = null;
        validateSupplierAddr2 = new LengthValidator(100, validateSupplierAddr2);
        fileParseUtil.addError(rlt, validateSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "[Supplier Addr2]"));
        
        String supplierAddr3 = contents[11].trim();
        FieldContentValidator validateSupplierAddr3 = null;
        validateSupplierAddr3 = new LengthValidator(100, validateSupplierAddr3);
        fileParseUtil.addError(rlt, validateSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "[Supplier Addr3]"));
        
        String supplierAddr4 = contents[12].trim();
        FieldContentValidator validateSupplierAddr4 = null;
        validateSupplierAddr4 = new LengthValidator(100, validateSupplierAddr4);
        fileParseUtil.addError(rlt, validateSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "[Supplier Addr4]"));
        
        String supplierCity = contents[13].trim();
        FieldContentValidator validateSupplierCity = null;
        validateSupplierCity = new LengthValidator(50, validateSupplierCity);
        fileParseUtil.addError(rlt, validateSupplierCity.validate(supplierCity, getPrefixForHeader() + "[Supplier City]"));
        
        String supplierState = contents[14].trim();
        FieldContentValidator validateSupplierState = null;
        validateSupplierState = new LengthValidator(50, validateSupplierState);
        fileParseUtil.addError(rlt, validateSupplierState.validate(supplierState, getPrefixForHeader() + "[Supplier State]"));
        
        String supplierCountryCode = contents[15].trim();
        FieldContentValidator validateSupplierCountryCode = null;
        validateSupplierCountryCode = new LengthValidator(2, validateSupplierCountryCode);
        fileParseUtil.addError(rlt, validateSupplierCountryCode.validate(supplierCountryCode, getPrefixForHeader() + "[Supplier Country Code]"));
        
        String supplierPostalCode = contents[16].trim();
        FieldContentValidator validateSupplierPostalCode = null;
        validateSupplierPostalCode = new LengthValidator(15, validateSupplierPostalCode);
        fileParseUtil.addError(rlt, validateSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "[Supplier Postal Code]"));
        
        String payMethodCode = contents[17].trim();
        FieldContentValidator validatePayMethodCode = null;
        validatePayMethodCode = new LengthValidator(20, validatePayMethodCode);
        fileParseUtil.addError(rlt, validatePayMethodCode.validate(payMethodCode, getPrefixForHeader() + "[Pay Method Code]"));
        
        
        String payMethodDesc = contents[18].trim();
        FieldContentValidator validatePayMethodDesc = null; 
        validatePayMethodDesc = new LengthValidator(100, validatePayMethodDesc);
        fileParseUtil.addError(rlt, validatePayMethodDesc.validate(payMethodDesc, getPrefixForHeader() + "[Pay Method Desc]"));
        
        String bankCode = contents[19].trim();
        FieldContentValidator validateBankCode = null;
        validateBankCode = new LengthValidator(20, validateBankCode);
        fileParseUtil.addError(rlt, validateBankCode.validate(bankCode, getPrefixForHeader() + "[Bank Code]"));
        
        String totalAmount = contents[20].trim();
        FieldContentValidator validateTotalAmount = null;
        validateTotalAmount = new EmptyValidator();
        validateTotalAmount = new NoSpaceValidator(validateTotalAmount);
        validateTotalAmount = new NumberValidator(validateTotalAmount);
        validateTotalAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validateTotalAmount);
        fileParseUtil.addError(rlt, validateTotalAmount.validate(totalAmount, getPrefixForHeader() + "[Total Amount]"));
        
        String totalDiscAmount = contents[21].trim();
        FieldContentValidator validateTotalDiscAmount = null;
        validateTotalDiscAmount = new EmptyValidator();
        validateTotalDiscAmount = new NoSpaceValidator(validateTotalDiscAmount);
        validateTotalDiscAmount = new NumberValidator(validateTotalDiscAmount);
        validateTotalDiscAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateTotalDiscAmount);
        fileParseUtil.addError(rlt, validateTotalDiscAmount.validate(totalDiscAmount, getPrefixForHeader() + "[Total Discount Amount]"));
        
        String netTotalAmount = contents[22].trim();
        FieldContentValidator validateNetTotalAmount = null;
        validateNetTotalAmount = new EmptyValidator();
        validateNetTotalAmount = new NoSpaceValidator(validateNetTotalAmount);
        validateNetTotalAmount = new NumberValidator(validateNetTotalAmount);
        validateNetTotalAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateNetTotalAmount);
        fileParseUtil.addError(rlt, validateNetTotalAmount.validate(netTotalAmount, getPrefixForHeader() + "[Net Total Amount]"));
        
    }
    
    
    private void validateDetail(String headerPnNo, List<String[]> details, List<String> rlt)
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        for (int i = 0 ; i < details.size(); i++)
        {
            String[] detailContents = details.get(i);
            
            if (detailContents.length != DETAIL_COLUMN_COUNT)
            {
                rlt.add(getPrefixForHeaderEx(i + 1) + " has " + detailContents.length + " columns. "
                    + DETAIL_COLUMN_COUNT + " columns expected.");
                
                return ;
            }
            
            String pnNo = detailContents[1].trim();
            FieldContentValidator validatePnNo = null;
            validatePnNo = new EmptyValidator();
            validatePnNo = new LengthValidator(20, validatePnNo);
            validatePnNo = new SpecialCharValidator(validatePnNo, false, headerPnNo);
            fileParseUtil.addError(rlt, validatePnNo.validate(pnNo, getPrefixForDetail(i + 1) + "[Pn No]"));
            
            String seqNum = detailContents[2].trim();
            FieldContentValidator validateSeqNum = null;
            validateSeqNum = new EmptyValidator();
            validateSeqNum = new NumberValidator(validateSeqNum);
            validateSeqNum = new MinValidator(Integer.valueOf(1), validateSeqNum);
            validateSeqNum = new MaxValidator(Integer.valueOf(9999), validateSeqNum);
            fileParseUtil.addError(rlt, validateSeqNum.validate(seqNum, getPrefixForDetail(i + 1) + "[Detail Seq Num]"));
            
            String docDate = detailContents[3].trim();
            FieldContentValidator validateDocDate = null;
            validateDocDate = new DateValidator(PnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateDocDate);
            fileParseUtil.addError(rlt, validateDocDate.validate(docDate, getPrefixForDetail(i + 1) + "[Doc Date]"));
            
            String docType = detailContents[4].trim();
            FieldContentValidator validateDocType = null;
            validateDocType = new EmptyValidator();
            validateDocType = new SpecialCharValidator(validateDocType, false, "I", "C", "R");
            fileParseUtil.addError(rlt, validateDocType.validate(docType, getPrefixForDetail(i + 1) +"[Doc Type]"));
            
            String docRefNo = detailContents[5].trim();
            FieldContentValidator validateDocRefNo = null;
            validateDocRefNo = new LengthValidator(20, validateDocRefNo);
            fileParseUtil.addError(rlt, validateDocRefNo.validate(docRefNo, getPrefixForDetail(i + 1) + "[Doc Ref No]"));
            
            String payTransNo = detailContents[6].trim();
            FieldContentValidator validatePayTransNo = null;
            validatePayTransNo = new EmptyValidator();
            validatePayTransNo = new LengthValidator(20, validatePayTransNo);
            fileParseUtil.addError(rlt, validatePayTransNo.validate(payTransNo, getPrefixForDetail(i + 1) +"[Pay Trans No]"));
            
            String payRefNo = detailContents[7].trim();
            FieldContentValidator validatePayRefNo = null;
            validatePayRefNo = new LengthValidator(20, validatePayRefNo);
            fileParseUtil.addError(rlt, validatePayRefNo.validate(payRefNo, getPrefixForDetail(i + 1) +"[Pay Ref No]"));
            
            String grossAmount = detailContents[8].trim();
            FieldContentValidator validateGrossAmount = null;
            validateGrossAmount = new EmptyValidator();
            validateGrossAmount = new NoSpaceValidator(validateGrossAmount);
            validateGrossAmount = new NumberValidator(validateGrossAmount);
            validateGrossAmount = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateGrossAmount);
            fileParseUtil.addError(rlt, validateGrossAmount.validate(grossAmount, getPrefixForDetail(i + 1) +"[Gross Amount]"));
            
            String discountAmount = detailContents[9].trim();
            FieldContentValidator validateDiscAmount = null;
            validateDiscAmount = new EmptyValidator();
            validateDiscAmount = new NoSpaceValidator(validateDiscAmount);
            validateDiscAmount = new NumberValidator(validateDiscAmount);
            validateDiscAmount = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateGrossAmount);
            fileParseUtil.addError(rlt, validateDiscAmount.validate(discountAmount, getPrefixForDetail(i + 1) +"[Discount Amount]"));
            
            String netAmount = detailContents[10].trim();
            FieldContentValidator validateNetAmount = null;
            validateNetAmount = new EmptyValidator();
            validateNetAmount = new NoSpaceValidator(validateNetAmount);
            validateNetAmount = new NumberValidator(validateNetAmount);
            validateNetAmount = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateNetAmount);
            fileParseUtil.addError(rlt, validateNetAmount.validate(netAmount, getPrefixForDetail(i + 1) +"[Net Amount]"));
        }
    }


    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
}
