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
import com.pracbiz.b2bportal.core.eai.file.canonical.GrnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.GrnFileValidator;
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

public class CanonicalGrnFileValidator extends GrnFileValidator implements CoreCommonConstants
{
    private static int HEADER_COLUMN_COUNT = 22;
    private static int DETAIL_COLUMN_COUNT = 26;
    private static final Logger log = LoggerFactory.getLogger(CanonicalGrnFileValidator.class);
    
    @Autowired private ValidationConfigHelper validationConfig;
    @Autowired private GrnDocFileHandler canonicalGrnDocFileHandler;
    
    public List<String> validateFile(DocMsg docMsg, byte[] input) throws Exception
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
            log.info(":::: Start to validator Grn");
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
                validateCanonicalHeaderExtends(headerExList, errorMessages);
                
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
                validateCanonicalDetailExtends(detailExList, errorMessages);
            }
                
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
        log.info(":::: End to validator Grn");
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
        
        String grnNo = contents[1].trim();
        FieldContentValidator validateGrnNo = null;
        validateGrnNo = new EmptyValidator();
        validateGrnNo = new LengthValidator(20, validateGrnNo);
        fileParseUtil.addError(rlt, validateGrnNo.validate(grnNo, getPrefixForHeader() + "[Grn No]"));
        
        
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
        
        
        String grnDate = contents[4].trim();
        FieldContentValidator validateGrnDate = null;
        validateGrnDate = new EmptyValidator();
        validateGrnDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateGrnDate);
        fileParseUtil.addError(rlt, validateGrnDate.validate(grnDate, getPrefixForHeader() + "[Grn Date]"));
        
        String poNo = contents[5].trim();
        FieldContentValidator validatePoNo = null;
        validatePoNo = new EmptyValidator();
        validatePoNo = new LengthValidator(20, validatePoNo);
        fileParseUtil.addError(rlt, validatePoNo.validate(poNo, getPrefixForHeader() + "[Po No]"));
        
        String poDate = contents[6].trim();
        FieldContentValidator validatePoDate = null;
        validatePoDate = new EmptyValidator();
        validatePoDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validatePoDate);
        fileParseUtil.addError(rlt, validatePoDate.validate(poDate, getPrefixForHeader() + "[Po Date]"));
        
        String createDate = contents[7].trim();
        FieldContentValidator validateCreateDate = null;
        validateCreateDate = new EmptyValidator();
        validateCreateDate = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateCreateDate);
        fileParseUtil.addError(rlt, validateCreateDate.validate(createDate, getPrefixForHeader() + "[Create Date]"));
        
        String buyerCode = contents[8].trim();
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
        
        String buyerName = contents[9].trim();
        FieldContentValidator validateBuyerName = null;
        validateBuyerName = new LengthValidator(100, validateBuyerName);
        fileParseUtil.addError(rlt, validateBuyerName.validate(buyerName, getPrefixForHeader() + "[Buyer Name]"));
        
        String supplierCode = contents[10].trim();
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
        
        String supplierName = contents[11].trim();
        FieldContentValidator validateSupplierName = null;
        validateSupplierName = new LengthValidator(100, validateSupplierName);
        fileParseUtil.addError(rlt, validateSupplierName.validate(supplierName, getPrefixForHeader() + "[Supplier Name]"));
        
        String receiveStoreCode = contents[12].trim();
        FieldContentValidator validateReceiveStoreCode = null;
        validateReceiveStoreCode = new LengthValidator(20, validateReceiveStoreCode);
        fileParseUtil.addError(rlt, validateReceiveStoreCode.validate(receiveStoreCode, getPrefixForHeader() + "[Receive Store Code]"));
        
        String receiveStoreName = contents[13].trim();
        FieldContentValidator validateReceiveStoreName = null;
        validateReceiveStoreName = new LengthValidator(100, validateReceiveStoreName);
        fileParseUtil.addError(rlt, validateReceiveStoreName.validate(receiveStoreName, getPrefixForHeader() + "[Receive Store Name]"));
        
        String totalExpectedQty = contents[14].trim();
        FieldContentValidator validateTotalExpectedQty = null;
        validateTotalExpectedQty = new NoSpaceValidator(validateTotalExpectedQty);
        validateTotalExpectedQty = new NumberValidator(validateTotalExpectedQty);
        validateTotalExpectedQty = new MaxValidator(new BigDecimal(
                "999999.9999"), validateTotalExpectedQty);
        fileParseUtil.addError(rlt, validateTotalExpectedQty.validate(totalExpectedQty, getPrefixForHeader() + "[Total Expected Qty]"));
        
        
        String totalReceivedQty = contents[15].trim();
        FieldContentValidator validateReceivedQty = null; 
        validateReceivedQty = new NoSpaceValidator(validateReceivedQty);
        validateReceivedQty = new NumberValidator(validateReceivedQty);
        validateReceivedQty = new MaxValidator(new BigDecimal(
                "999999.9999"), validateReceivedQty);
        fileParseUtil.addError(rlt, validateReceivedQty.validate(totalReceivedQty, getPrefixForHeader() + "[Total Received Qty]"));
        
        String itemCount = contents[16].trim();
        FieldContentValidator validateItemCount = null;
        validateItemCount = new NoSpaceValidator(validateItemCount);
        validateItemCount = new NumberValidator(validateItemCount);
        validateItemCount = new MaxValidator(new BigDecimal(
                "9999"), validateItemCount);
        fileParseUtil.addError(rlt, validateItemCount.validate(itemCount, getPrefixForHeader() + "[Item Count]"));
        
        String discountAmount = contents[17].trim();
        FieldContentValidator validateDiscountAmount = null;
        validateDiscountAmount = new NoSpaceValidator(validateDiscountAmount);
        validateDiscountAmount = new NumberValidator(validateDiscountAmount);
        validateDiscountAmount = new MaxValidator(new BigDecimal("99999999999.9999"), validateDiscountAmount);
        fileParseUtil.addError(rlt, validateDiscountAmount.validate(discountAmount, getPrefixForHeader() + "[Total Received Qty]"));
        
        String netCost = contents[18].trim();
        FieldContentValidator validateNetCost = null;
        validateNetCost = new NoSpaceValidator(validateNetCost);
        validateNetCost = new NumberValidator(validateNetCost);
        validateNetCost = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateNetCost);
        fileParseUtil.addError(rlt, validateNetCost.validate(netCost, getPrefixForHeader() + "[Net Cost]"));
        
        String totalCost = contents[19].trim();
        FieldContentValidator validateTotalCost = null;
        validateTotalCost = new NoSpaceValidator(validateTotalCost);
        validateTotalCost = new NumberValidator(validateTotalCost);
        validateTotalCost = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateTotalCost);
        fileParseUtil.addError(rlt, validateTotalCost.validate(totalCost, getPrefixForHeader() + "[Total Cost]"));
        
        String totalRetailAmount = contents[20].trim();
        FieldContentValidator validateTotalRetailAmount = null;
        validateTotalRetailAmount = new NoSpaceValidator(validateTotalRetailAmount);
        validateTotalRetailAmount = new NumberValidator(validateTotalRetailAmount);
        validateTotalRetailAmount = new MaxValidator(new BigDecimal(
                "99999999999.9999"), validateTotalRetailAmount);
        fileParseUtil.addError(rlt, validateTotalRetailAmount.validate(totalRetailAmount, getPrefixForHeader() + "[Total Retail Amount]"));
        
        String grnRemarks = contents[21].trim();
        FieldContentValidator validateGrnRemarks = null;
        validateGrnRemarks = new LengthValidator(500, validateGrnRemarks);
        fileParseUtil.addError(rlt, validateGrnRemarks.validate(grnRemarks, getPrefixForHeader() + "[Grn Remarks]"));
        
    }
    
    private void validateDetail(String headerGrnNo, List<String[]> details, List<String> rlt)
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        for (int i = 0 ; i < details.size(); i++)
        {
            String[] detailContents = details.get(i);
            
            if (detailContents.length != DETAIL_COLUMN_COUNT)
            {
                rlt.add(getPrefixForDetail(i + 1) +" has " + detailContents.length + " columns. "
                    + DETAIL_COLUMN_COUNT + " columns expected.");
                
                return ;
            }
            
            String grnNo = detailContents[1].trim();
            FieldContentValidator validateGrnNo = null;
            validateGrnNo = new EmptyValidator();
            validateGrnNo = new LengthValidator(20, validateGrnNo);
            validateGrnNo = new SpecialCharValidator(validateGrnNo, false, headerGrnNo);
            fileParseUtil.addError(rlt, validateGrnNo.validate(grnNo, getPrefixForDetail(i + 1) + "[Grn No]"));
            
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
            
            String orderBaseUnit = detailContents[13].trim();
            FieldContentValidator validateOrderBaseUnit = null;
            validateOrderBaseUnit = new EmptyValidator();
            validateOrderBaseUnit = new SpecialCharValidator(validateOrderBaseUnit,
                    false, "P", "U");
            fileParseUtil.addError(rlt, validateOrderBaseUnit.validate(orderBaseUnit, getPrefixForDetail(i + 1) +"[Order Base Unit]"));
            
            String orderUom = detailContents[14].trim();
            FieldContentValidator validateOrderUom = null;
            validateOrderUom = new EmptyValidator();
            validateOrderUom = new LengthValidator(20, validateOrderUom);
            fileParseUtil.addError(rlt, validateOrderUom.validate(orderUom, getPrefixForDetail(i + 1) +"[Order Uom]"));
            
            
            String orderQty = detailContents[15].trim();
            FieldContentValidator validateOrderQty = null; 
            validateOrderQty = new EmptyValidator();
            validateOrderQty = new NoSpaceValidator(validateOrderQty);
            validateOrderQty = new NumberValidator(validateOrderQty);
            validateOrderQty = new MaxValidator(new BigDecimal(
                    "999999.9999"), validateOrderQty);
            fileParseUtil.addError(rlt, validateOrderQty.validate(orderQty, getPrefixForDetail(i + 1) +"[Order Qty]"));
            
            String receiveQty = detailContents[16].trim();
            FieldContentValidator validateReceiveQty = null;
            validateReceiveQty = new NoSpaceValidator(validateReceiveQty);
            validateReceiveQty = new NumberValidator(validateReceiveQty);
            validateReceiveQty = new MaxValidator(new BigDecimal(
                    "999999.9999"), validateReceiveQty);
            fileParseUtil.addError(rlt, validateReceiveQty.validate(receiveQty, getPrefixForDetail(i + 1) +"[Receive Qty]"));
            
            String focBaseUnit = detailContents[17].trim();
            FieldContentValidator validateFocBaseUnit = null;
            validateFocBaseUnit = new SpecialCharValidator(validateFocBaseUnit,
                    false, "P", "U");
            fileParseUtil.addError(rlt, validateFocBaseUnit.validate(focBaseUnit, getPrefixForDetail(i + 1) +"[Foc Base Unit]"));
            
            String focUom = detailContents[18].trim();
            FieldContentValidator validateFocUom = null;
            validateFocUom = new LengthValidator(20, validateFocUom);
            fileParseUtil.addError(rlt, validateFocUom.validate(focUom, getPrefixForDetail(i + 1) +"[Foc Uom]"));
            
            String focQty = detailContents[19].trim();
            FieldContentValidator validateFocQty = null;
            validateFocQty = new NumberValidator(validateFocQty);
            validateFocQty = new MaxValidator(new BigDecimal(
                    "999999.9999"), validateFocQty);
            fileParseUtil.addError(rlt, validateFocQty.validate(focQty, getPrefixForDetail(i + 1) +"[Foc Qty]"));
            
            String focReceiveQty = detailContents[20].trim();
            FieldContentValidator validateFocReceiveQty = new NumberValidator();
            validateFocReceiveQty = new MaxValidator(new BigDecimal(
                    "999999.9999"), validateFocReceiveQty);
            fileParseUtil.addError(rlt, validateFocReceiveQty.validate(focReceiveQty, getPrefixForDetail(i + 1) +"[Foc Receive Qty]"));
            
            String unitCost = detailContents[21].trim();
            FieldContentValidator validateUnitCost = null;
            validateUnitCost = new NoSpaceValidator(validateUnitCost);
            validateUnitCost = new NumberValidator(validateUnitCost);
            validateUnitCost = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateUnitCost);
            fileParseUtil.addError(rlt, validateUnitCost.validate(unitCost, getPrefixForDetail(i + 1) +"[Unit Cost]"));
            
            String itemCost = detailContents[22].trim();
            FieldContentValidator validateItemCost = null;
            validateItemCost = new EmptyValidator();
            validateItemCost = new NoSpaceValidator(validateItemCost);
            validateItemCost = new NumberValidator(validateItemCost);
            validateItemCost = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateItemCost);
            fileParseUtil.addError(rlt, validateItemCost.validate(itemCost, getPrefixForDetail(i + 1) +"[Item Cost]"));
            
            String retailPrice = detailContents[23].trim();
            FieldContentValidator validateRetailPrice = null;
            validateRetailPrice = new NoSpaceValidator(validateRetailPrice);
            validateRetailPrice = new NumberValidator(validateRetailPrice);
            validateRetailPrice = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateRetailPrice);
            fileParseUtil.addError(rlt, validateRetailPrice.validate(retailPrice, getPrefixForDetail(i + 1) +"[Retail Price]"));
            
            String itemRetailAmount = detailContents[24].trim();
            FieldContentValidator validateItemRetailAmount = null;
            validateItemRetailAmount = new NoSpaceValidator(validateItemRetailAmount);
            validateItemRetailAmount = new NumberValidator(validateItemRetailAmount);
            validateItemRetailAmount = new MaxValidator(new BigDecimal(
                    "99999999999.9999"), validateItemRetailAmount);
            fileParseUtil.addError(rlt, validateItemRetailAmount.validate(itemRetailAmount, getPrefixForDetail(i + 1) +"[Item Retail Amount]"));
            
            String itemRemarks = detailContents[25].trim();
            FieldContentValidator validateItemRemarks = null;
            validateItemRemarks = new LengthValidator(100, validateItemRemarks);
            fileParseUtil.addError(rlt, validateItemRemarks.validate(itemRemarks, getPrefixForDetail(i + 1) +"[Item Remarks]"));
           
        }
    }

    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        try
        {
            canonicalGrnDocFileHandler.readFileContent((GrnDocMsg) docMsg, input);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    }
}
