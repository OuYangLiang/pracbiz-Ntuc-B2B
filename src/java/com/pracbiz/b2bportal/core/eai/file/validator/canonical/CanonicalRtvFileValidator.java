package com.pracbiz.b2bportal.core.eai.file.validator.canonical;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.eai.file.validator.RtvFileValidator;
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
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class CanonicalRtvFileValidator extends RtvFileValidator implements CoreCommonConstants
{
    private static final int RTV_HEADER_LENGTH = 38;
    private static final int RTV_DETAIL_LENGTH = 28;
    private static final int RTV_LOCATION_LENGTH = 8;
    @Autowired private ValidationConfigHelper validationConfig;
    
    public List<String> validateFile(DocMsg docMsg, byte[] input) throws Exception
    {
        List<String> rlt = null;
        InputStream inputStream = null;
        
        try
        {
            rlt = new ArrayList<String>();
            inputStream = new ByteArrayInputStream(input);
            Map<String, List<String[]>> map = FileParserUtil.getInstance().readLinesAndGroupByRecordType(inputStream);
            
            List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
            List<String[]> detailList = map.get(FileParserUtil.RECORD_TYPE_DETAIL);
            List<String[]> locList = map.get(FileParserUtil.RECORD_TYPE_LOCATION);
            List<String[]> headerExList = map.get(FileParserUtil.RECORD_TYPE_HEADER_EXTENDED);
            List<String[]> detailExList = map.get(FileParserUtil.RECORD_TYPE_DETAIL_EXTENDED);
            List<String[]> locExList = map.get(FileParserUtil.RECORD_TYPE_LOCATION_DETAIL_EXTENDED);
            //check header empty
            if (null == headerList || headerList.isEmpty())
            {
                rlt.add("RTV header is expected.");
                return rlt;
            }
            //check detail empty
            if (null == detailList || detailList.isEmpty())
            {
                rlt.add("RTV detail is expected.");
                return rlt;
            }
            
            //check header line length
            String[] headerContent = headerList.get(0);
            if (headerContent.length != RTV_HEADER_LENGTH)
            {
                rlt.add("header has "+ headerContent.length + " fields, 38 is expected.");
                return rlt;
            }
            
            //check each detail line length
            for (int i = 0; i < detailList.size(); i++)
            {
                String[] detailContent = detailList.get(i);
                if (detailContent.length != RTV_DETAIL_LENGTH)
                {
                    rlt.add("detail has" + detailContent.length + " fields, 28 is expected");
                    return rlt;
                }
            }
            
            //check each location line length
            if (null != locList && !locList.isEmpty())
            {
                for (int i = 0; i < locList.size(); i++)
                {
                    String[] locContent = locList.get(i);
                    if (locContent.length != RTV_LOCATION_LENGTH)
                    {
                        rlt.add("detail has" + locContent.length + " fields, 8 is expected");
                        return rlt;
                    }
                }
            }
            
            //validate Rtv Header
            String rtvNo = this.validateHeader(headerList.get(0), rlt, docMsg);
            if (!rlt.isEmpty())
            {
                return rlt;
            }
            //valdiate Rtv Details
            List<String> lineSeqs = this.validateDetail(detailList, rlt, rtvNo);
            if (!rlt.isEmpty())
            {
                return rlt;
            }
            //validate Rtv Locations
            if (null != locList && !locList.isEmpty())
            {
                this.validateLocation(locList, rlt , lineSeqs, rtvNo);
                if (!rlt.isEmpty())
                {
                    return rlt;
                }
            }
            //validate Rtv Header Extend
            if (null != headerExList && !headerExList.isEmpty())
            {
                this.validateCanonicalHeaderExtends(headerExList, rlt);
                if (!rlt.isEmpty())
                {
                    return rlt;
                }
            }
            //validate Rtv Detail Extend
            if (null != detailExList && !detailExList.isEmpty())
            {
                this.validateCanonicalDetailExtends(detailExList, rlt);
                if (!rlt.isEmpty())
                {
                    return rlt;
                }
            }
            //validate Rtv location detail extend
            if (null != locExList && !locExList.isEmpty())
            {
                this.validateCanonicalLocationExtends(locExList, rlt);
                if (!rlt.isEmpty())
                {
                    return rlt;
                }
            }
        }
        finally
        {
            if (null != inputStream)
            {
                inputStream.close();
            }
        }
        
        return rlt;
    }
    
    private String validateHeader(String[] content, List<String> rlt, DocMsg docMsg)
    {
        String result = null;
        //check RtvNo
        String rtvNo = content[1].trim();
        FieldContentValidator validateRtvNo = null;
        validateRtvNo = new EmptyValidator();
        validateRtvNo = new NoSpaceValidator(validateRtvNo);
        validateRtvNo = new LengthValidator(20, validateRtvNo);
        FileParserUtil.getInstance().addError(rlt, validateRtvNo.validate(rtvNo, getPrefixForHeader() + "[Rtv No]"));
        
        //check DocAction
        String DocAction = content[2].trim();
        FieldContentValidator validateDocAction = null;
        validateDocAction = new EmptyValidator();
        validateDocAction = new SpecialCharValidator(validateDocAction, false, "A", "R", "D");
        FileParserUtil.getInstance().addError(rlt, validateDocAction.validate(DocAction, getPrefixForHeader() + "[Doc Action]"));
        
        //check ActionDate
        String actionDate = content[3].trim();
        FieldContentValidator validateActionDate = null;
        validateActionDate = new EmptyValidator();
        validateActionDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS, validateActionDate);
        FileParserUtil.getInstance().addError(rlt, validateActionDate.validate(actionDate, getPrefixForHeader() + "[Action Date]"));
        
        //check RtvDate
        String rtvDate = content[4].trim();
        FieldContentValidator validateRtvDate = null;
        validateRtvDate = new EmptyValidator();
        validateRtvDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS, validateRtvDate);
        FileParserUtil.getInstance().addError(rlt, validateRtvDate.validate(rtvDate, getPrefixForHeader() + "[Rtv Date]"));
        
        //check CollectionDate
        String collectionDate = content[5].trim();
        FieldContentValidator validatecollectionDate = null;
        validatecollectionDate = new EmptyValidator();
        validatecollectionDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS, validatecollectionDate);
        FileParserUtil.getInstance().addError(rlt, validatecollectionDate.validate(collectionDate, getPrefixForHeader() + "[Collection Date]"));
        
        //check DoNo
        String doNo = content[6].trim();
        FieldContentValidator validateDoNo = null;
        validateDoNo = new LengthValidator(20, validateDoNo);
        FileParserUtil.getInstance().addError(rlt, validateDoNo.validate(doNo, getPrefixForHeader() + "[Do No]"));
        
        //check DoDate
        String doDate = content[7].trim();
        FieldContentValidator validateDoDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS);
        FileParserUtil.getInstance().addError(rlt, validateDoDate.validate(doDate, getPrefixForHeader() + "[Do Date]"));
        
         //check InvNo
        String invNo = content[8].trim();
        FieldContentValidator validateInvNo = null;
        validateInvNo = new LengthValidator(20, validateInvNo);
        FileParserUtil.getInstance().addError(rlt, validateInvNo.validate(invNo, getPrefixForHeader() + "[Inv No]"));
        
        //check InvDate
        String invDate = content[9].trim();
        FieldContentValidator validateInvDate = null;
        validateInvDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS);
        FileParserUtil.getInstance().addError(rlt, validateInvDate.validate(invDate, getPrefixForHeader() + "[Inv Date]"));
        
        //check BuyerCode
        String buyerCode = content[10].trim();
        FieldContentValidator validateBuyerCode = null;
        validateBuyerCode = new EmptyValidator();
        validateBuyerCode = new SpecialCharValidator(validateBuyerCode, false, docMsg.getSenderCode());
        validateBuyerCode = new NoSpaceValidator(validateBuyerCode);
        validateBuyerCode = new LengthValidator(20, validateBuyerCode);
        FileParserUtil.getInstance().addError(rlt, validateBuyerCode.validate(buyerCode, getPrefixForHeader() + "[Buyer Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_BUYER_CODE)).validate(buyerCode, "buyerCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Buyer Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        //check BuyerName
        String buyerName = content[11].trim();
        FieldContentValidator validateBuyerName = null;
        validateBuyerName = new LengthValidator(100, validateBuyerName);
        FileParserUtil.getInstance().addError(rlt, validateBuyerName.validate(buyerName, getPrefixForHeader() + "[Buyer Name]"));
        
          //check BuyerAddr1
        String buyerAddr1 = content[12].trim();
        FieldContentValidator validateBuyerAddr1 = null;
        validateBuyerAddr1 = new LengthValidator(100, validateBuyerAddr1);
        FileParserUtil.getInstance().addError(rlt, validateBuyerAddr1.validate(buyerAddr1, getPrefixForHeader() + "[Buyer Addr1]"));
        
         //check BuyerAddr2
        String buyerAddr2 = content[13].trim();
        FieldContentValidator validateBuyerAddr2 = null;
        validateBuyerAddr2 = new LengthValidator(100, validateBuyerAddr2);
        FileParserUtil.getInstance().addError(rlt, validateBuyerAddr2.validate(buyerAddr2, getPrefixForHeader() + "[Buyer Addr2]"));
        
         //check BuyerAddr3
        String buyerAddr3 = content[14].trim();
        FieldContentValidator validateBuyerAddr3 = null;
        validateBuyerAddr3 = new LengthValidator(100, validateBuyerAddr3);
        FileParserUtil.getInstance().addError(rlt, validateBuyerAddr3.validate(buyerAddr3, getPrefixForHeader() + "[Buyer Addr3]"));
        
        //check BuyerAddr4
        String buyerAddr4 = content[15].trim();
        FieldContentValidator validateBuyerAddr4 = null;
        validateBuyerAddr4 = new LengthValidator(100, validateBuyerAddr4);
        FileParserUtil.getInstance().addError(rlt, validateBuyerAddr4.validate(buyerAddr4, getPrefixForHeader() + "[Buyer Addr4]"));
        
         //check BuyerCity
        String buyerCity = content[16].trim();
        FieldContentValidator validateBuyerCity = null;
        validateBuyerCity = new LengthValidator(50, validateBuyerCity);
        FileParserUtil.getInstance().addError(rlt, validateBuyerCity.validate(buyerCity, getPrefixForHeader() + "[Buyer City]"));
        
        //check BuyerState
        String buyerState = content[17].trim();
        FieldContentValidator validateBuyerState = null;
        validateBuyerState = new LengthValidator(50, validateBuyerState);
        FileParserUtil.getInstance().addError(rlt, validateBuyerState.validate(buyerState, getPrefixForHeader() + "[Buyer State]"));
        
        //check BuyerCtryCode
        String buyerCtryCode = content[18].trim();
        FieldContentValidator validateBuyerCtryCode = null;
        validateBuyerCtryCode = new LengthValidator(2, validateBuyerCtryCode);
        FileParserUtil.getInstance().addError(rlt, validateBuyerCtryCode.validate(buyerCtryCode, getPrefixForHeader() + "[Buyer Ctry Code]"));
        
        //check BuyerPostalCode
        String buyerPostalCode = content[19].trim();
        FieldContentValidator validateBuyerPostalCode = null;
        validateBuyerPostalCode = new LengthValidator(15, validateBuyerPostalCode);
        FileParserUtil.getInstance().addError(rlt, validateBuyerPostalCode.validate(buyerPostalCode, getPrefixForHeader() + "[Buyer Postal Code]"));
        result = new RegexValidator("^[0-9]*$").validate(buyerPostalCode, "buyerPostalCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Buyer Postal Code] only '0-9' allowed. ");
        }
        
         //check SupplierCode
        String supplierCode = content[20].trim();
        FieldContentValidator validateSupplierCode = null;
        validateSupplierCode = new EmptyValidator();
        validateSupplierCode = new SpecialCharValidator(validateSupplierCode, false, docMsg.getReceiverCode());
        validateSupplierCode = new NoSpaceValidator(validateSupplierCode);
        validateSupplierCode = new LengthValidator(20, validateSupplierCode);
        FileParserUtil.getInstance().addError(rlt, validateSupplierCode.validate(supplierCode, getPrefixForHeader() + "[Supplier Code]"));
        result = new RegexValidator(validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE)).validate(supplierCode, "supplierCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Supplier Code] only 'a-z,A-Z,0-9,-(hyphens)' allowed. ");
        }
        
        //check SupplierName
        String supplierName = content[21].trim();
        FieldContentValidator validateSupplierName = null;
        validateSupplierName = new LengthValidator(100, validateSupplierName);
        FileParserUtil.getInstance().addError(rlt, validateSupplierName.validate(supplierName, getPrefixForHeader() + "[Supplier Name]"));
        
        //check SupplierAddr1
        String supplierAddr1 = content[22].trim();
        FieldContentValidator validateSupplierAddr1 = null;
        validateSupplierAddr1 = new LengthValidator(100, validateSupplierAddr1);
        FileParserUtil.getInstance().addError(rlt, validateSupplierAddr1.validate(supplierAddr1, getPrefixForHeader() + "[Supplier Addr1]"));
        
        //check SupplierAddr2
        String supplierAddr2 = content[23].trim();
        FieldContentValidator validateSupplierAddr2 = null;
        validateSupplierAddr2 = new LengthValidator(100, validateSupplierAddr2);
        FileParserUtil.getInstance().addError(rlt, validateSupplierAddr2.validate(supplierAddr2, getPrefixForHeader() + "[Supplier Addr2]"));
        
        //check SupplierAddr3
        String supplierAddr3 = content[24].trim();
        FieldContentValidator validateSupplierAddr3 = null;
        validateSupplierAddr3 = new LengthValidator(100, validateSupplierAddr3);
        FileParserUtil.getInstance().addError(rlt, validateSupplierAddr3.validate(supplierAddr3, getPrefixForHeader() + "[Supplier Addr3]"));
        
        //check SupplierAddr4
        String supplierAddr4 = content[25].trim();
        FieldContentValidator validateSupplierAddr4 = null;
        validateSupplierAddr4 = new LengthValidator(100, validateSupplierAddr4);
        FileParserUtil.getInstance().addError(rlt, validateSupplierAddr4.validate(supplierAddr4, getPrefixForHeader() + "[Supplier Addr4]"));
        
        //check SupplierCity
        String supplierCity = content[26].trim();
        FieldContentValidator validateSupplierCity = null;
        validateSupplierCity = new LengthValidator(50, validateSupplierCity);
        FileParserUtil.getInstance().addError(rlt, validateSupplierCity.validate(supplierCity, getPrefixForHeader() + "[Supplier City]"));
        
        //check SupplierState
        String supplierState = content[27].trim();
        FieldContentValidator validateSupplierState = null;
        validateSupplierState = new LengthValidator(50, validateSupplierState);
        FileParserUtil.getInstance().addError(rlt, validateSupplierState.validate(supplierState, getPrefixForHeader() + "[Supplier State]"));
        
        //check SupplierCtryCode
        String supplierCtryCode = content[28].trim();
        FieldContentValidator validateSupplierCtryCode = null;
        validateSupplierCtryCode = new LengthValidator(2, validateSupplierCtryCode);
        FileParserUtil.getInstance().addError(rlt, validateSupplierCtryCode.validate(supplierCtryCode, getPrefixForHeader() + "[Supplier Ctry Code]"));
        
        //check SupplierPostalCode
        String supplierPostalCode = content[29].trim();
        FieldContentValidator validateSupplierPostalCode = null;
        validateSupplierPostalCode = new LengthValidator(15, validateSupplierPostalCode);
        FileParserUtil.getInstance().addError(rlt, validateSupplierPostalCode.validate(supplierPostalCode, getPrefixForHeader() + "[Supplier Postal Code]"));
        result = new RegexValidator("^[0-9]*$").validate(supplierPostalCode, "supplierPostalCode");
        if (result != null)
        {
            rlt.add(getPrefixForHeader() + "[Supplier Postal Code] only '0-9' allowed. ");
        }
        
        //check DeptCode
        String deptCode = content[30].trim();
        FieldContentValidator validateDeptCode = null;
        validateDeptCode = new LengthValidator(20, validateDeptCode);
        FileParserUtil.getInstance().addError(rlt, validateDeptCode.validate(deptCode, getPrefixForHeader() + "[Dept Code]"));
        
        //check DeptName
        String deptName = content[31].trim();
        FieldContentValidator validateDeptName = null;
        validateDeptName = new LengthValidator(100, validateDeptName);
        FileParserUtil.getInstance().addError(rlt, validateDeptName.validate(deptName, getPrefixForHeader() + "[Dept Name]"));
        
        //check SubDeptCode
        String subDeptCode = content[32].trim();
        FieldContentValidator validateSubDeptCode = null;
        validateSubDeptCode = new LengthValidator(20, validateSubDeptCode);
        FileParserUtil.getInstance().addError(rlt, validateSubDeptCode.validate(subDeptCode, getPrefixForHeader() + "[Sub Dept Code]"));
        
        //check SubDeptName
        String subDeptName = content[33].trim();
        FieldContentValidator validateSubDeptName = null;
        validateSubDeptName = new LengthValidator(100, validateSubDeptName);
        FileParserUtil.getInstance().addError(rlt, validateSubDeptName.validate(subDeptName, getPrefixForHeader() + "[Sub Dept Name]"));
        
        //check TotalCost
        String totalCost = content[34].trim();
        FieldContentValidator validateTotalCost = null;
        validateTotalCost = new EmptyValidator();
        validateTotalCost = new NoSpaceValidator(validateTotalCost);
        validateTotalCost = new NumberValidator(validateTotalCost);
        validateTotalCost = new MaxValidator(BigDecimal.valueOf(
                99999999999.9999), validateTotalCost);
        FileParserUtil.getInstance().addError(rlt, validateTotalCost.validate(totalCost, getPrefixForHeader() + "[Total Cost]"));
        
        //check ReasonCode
        String reasonCode = content[35].trim();
        FieldContentValidator validateReasonCode = null;
        validateReasonCode = new LengthValidator(20, validateReasonCode);
        FileParserUtil.getInstance().addError(rlt, validateReasonCode.validate(reasonCode, getPrefixForHeader() + "[Reason Code]"));
        
          //check ReasonDesc
        String reasonDesc = content[36].trim();
        FieldContentValidator validateReasonDesc = null;
        validateReasonDesc = new LengthValidator(100, validateReasonDesc);
        FileParserUtil.getInstance().addError(rlt, validateReasonDesc.validate(reasonDesc, getPrefixForHeader() + "[Reason Desc]"));
        
        //check RtvRemarks
        String rtvRemarks = content[37].trim();
        FieldContentValidator validateRtvRemarks = null;
        validateRtvRemarks = new LengthValidator(500, validateRtvRemarks);
        FileParserUtil.getInstance().addError(rlt, validateRtvRemarks.validate(rtvRemarks, getPrefixForHeader() + "[Rtv Remarks]"));
        
        return rtvNo;
    }
    
    
    private List<String> validateDetail(List<String[]> contents, List<String> rlt, String docNo)
    {
        List<String> lineSeqs = new ArrayList<String>();
        for (int i = 0; i < contents.size(); i++)
        {
            String[] content = contents.get(i);
            //check RtvNo
            String rtvNo = content[1].trim();
            FieldContentValidator validateRtvNo = null;
            validateRtvNo = new EmptyValidator();
            validateRtvNo = new NoSpaceValidator(validateRtvNo);
            validateRtvNo = new SpecialCharValidator(validateRtvNo, false, docNo);
            validateRtvNo = new LengthValidator(20, validateRtvNo);
            FileParserUtil.getInstance().addError(rlt, validateRtvNo.validate(rtvNo, getPrefixForDetail(i + 1) + "[Rtv No]"));
            
            //check LineSeqNo
            String lineSeqNo = content[2].trim();
            FieldContentValidator validateLineSeqNo = null;
            validateLineSeqNo = new EmptyValidator();
            validateLineSeqNo = new IntegerValidator(validateLineSeqNo);
            validateLineSeqNo = new MinValidator(Integer.valueOf(1), validateLineSeqNo);
            validateLineSeqNo = new MaxValidator(Integer.valueOf(9999), validateLineSeqNo);
            FileParserUtil.getInstance().addError(rlt, validateLineSeqNo.validate(lineSeqNo, getPrefixForDetail(i + 1) + "[Line Seq No]"));
            lineSeqs.add(lineSeqNo);
            
            //check BuyerItemCode
            String buyerItemCode = content[3].trim();
            FieldContentValidator validateBuyerItemCode = null;
            validateBuyerItemCode = new EmptyValidator();
            validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
            FileParserUtil.getInstance().addError(rlt, validateBuyerItemCode.validate(buyerItemCode, getPrefixForDetail(i + 1) + "[Buyer Item Code]"));
            
            //check SupplierItemCode
            String supplierItemCode = content[4].trim();
            FieldContentValidator validateSupplierItemCode = null;
            validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
            FileParserUtil.getInstance().addError(rlt, validateSupplierItemCode.validate(supplierItemCode, getPrefixForDetail(i + 1) + "[Supplier Item Code]"));
            
            //check Barcode
            String barcode = content[5].trim();
            FieldContentValidator validateBarcode = null;
            validateBarcode = new LengthValidator(50, validateBarcode);
            FileParserUtil.getInstance().addError(rlt, validateBarcode.validate(barcode, getPrefixForDetail(i + 1) + "[Barcode]"));
            
            //check ItemDesc
            String itemDesc = content[6].trim();
            FieldContentValidator validateItemDesc = null;
            validateItemDesc = new LengthValidator(100, validateItemDesc);
            FileParserUtil.getInstance().addError(rlt, validateItemDesc.validate(itemDesc, getPrefixForDetail(i + 1) + "[Item Desc]"));
            
            //check Brand
            String brand = content[7].trim();
            FieldContentValidator validateBrand = null;
            validateBrand = new LengthValidator(20, validateBrand);
            FileParserUtil.getInstance().addError(rlt, validateBrand.validate(brand, getPrefixForDetail(i + 1) + "[Brand]"));
            
            //check ColourCode
            String colourCode = content[8].trim();
            FieldContentValidator validateColourCode = null;
            validateColourCode = new LengthValidator(20, validateColourCode);
            FileParserUtil.getInstance().addError(rlt, validateColourCode.validate(colourCode, getPrefixForDetail(i + 1) + "[Colour Code]"));
            
            //check ColourDesc
            String colourDesc = content[9].trim();
            FieldContentValidator validateColourDesc = null;
            validateColourDesc = new LengthValidator(50, validateColourDesc);
            FileParserUtil.getInstance().addError(rlt, validateColourDesc.validate(colourDesc, getPrefixForDetail(i + 1) + "[Colour Desc]"));
            
            //check SizeCode
            String sizeCode = content[10].trim();
            FieldContentValidator validateSizeCode = null;
            validateSizeCode = new LengthValidator(20, validateSizeCode);
            FileParserUtil.getInstance().addError(rlt, validateSizeCode.validate(sizeCode, getPrefixForDetail(i + 1) + "[Size Code]"));
            
            //check SizeDesc
            String sizeDesc = content[11].trim();
            FieldContentValidator validateSizeDesc = null;
            validateSizeDesc = new LengthValidator(50, validateSizeDesc);
            FileParserUtil.getInstance().addError(rlt, validateSizeDesc.validate(sizeDesc, getPrefixForDetail(i + 1) + "[Size Desc]"));
            
            //check DoNo
            String doNo = content[12].trim();
            FieldContentValidator validateDoNo = null;
            validateDoNo = new LengthValidator(20, validateDoNo);
            FileParserUtil.getInstance().addError(rlt, validateDoNo.validate(doNo, getPrefixForDetail(i + 1) + "[Do No]"));
            
            //check DoDate
            String doDate = content[13].trim();
            FieldContentValidator validateDoDate = null;
            validateDoDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS);
            FileParserUtil.getInstance().addError(rlt, validateDoDate.validate(doDate, getPrefixForDetail(i + 1) + "[Do Date]"));
            
            //check InvNo
            String invNo = content[14].trim();
            FieldContentValidator validateInvNo = null;
            validateInvNo = new LengthValidator(20, validateInvNo);
            FileParserUtil.getInstance().addError(rlt, validateInvNo.validate(invNo, getPrefixForDetail(i + 1) + "[Inv No]"));
            
            //check InvDate
            String invDate = content[15].trim();
            FieldContentValidator validateInvDate = null;
            validateInvDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS);
            FileParserUtil.getInstance().addError(rlt, validateInvDate.validate(invDate, getPrefixForDetail(i + 1) + "[Inv Date]"));
            
            //check PackingFactor
            String packingFactor = content[16].trim();
            FieldContentValidator validatePackingFactor = null;
            validatePackingFactor = new EmptyValidator();
            validatePackingFactor = new NoSpaceValidator(validatePackingFactor);
            validatePackingFactor = new NumberValidator(validatePackingFactor);
            validatePackingFactor = new MaxValidator(BigDecimal.valueOf(999999.99), validatePackingFactor);
            FileParserUtil.getInstance().addError(rlt, validatePackingFactor.validate(packingFactor, getPrefixForDetail(i + 1) + "[Packing Factor]"));
            
            //check ReturnBaseUnit
            String returnBaseUnit = content[17].trim();
            FieldContentValidator validateReturnBaseUnit = null;
            validateReturnBaseUnit = new EmptyValidator();
            validateReturnBaseUnit = new SpecialCharValidator(validateReturnBaseUnit, false, "P", "U");
            FileParserUtil.getInstance().addError(rlt, validateReturnBaseUnit.validate(returnBaseUnit, getPrefixForDetail(i + 1) + "[Return Base Unit]"));
            
            //check ReturnUom
            String returnUom = content[18].trim();
            FieldContentValidator validateReturnUom = null;
            validateReturnUom = new EmptyValidator();
            validateReturnUom = new LengthValidator(20, validateReturnUom);
            FileParserUtil.getInstance().addError(rlt, validateReturnUom.validate(returnUom, getPrefixForDetail(i + 1) + "[Return Uom]"));
            
            //check ReturnQty
            String returnQty = content[19].trim();
            FieldContentValidator validateReturnQty = null;
            validateReturnQty = new EmptyValidator();
            validateReturnQty = new NoSpaceValidator(validateReturnQty);
            validateReturnQty = new NumberValidator(validateReturnQty);
            validateReturnQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateReturnQty);
            FileParserUtil.getInstance().addError(rlt, validateReturnQty.validate(returnQty, getPrefixForDetail(i + 1) + "[Return Qty]"));
            
            //check UnitCost
            String unitCost = content[20].trim();
            FieldContentValidator validateUnitCost = null;
            validateUnitCost = new EmptyValidator();
            validateUnitCost = new NoSpaceValidator(validateUnitCost);
            validateUnitCost = new NumberValidator(validateUnitCost);
            validateUnitCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateUnitCost);
            FileParserUtil.getInstance().addError(rlt, validateUnitCost.validate(unitCost, getPrefixForDetail(i + 1) + "[Unit Cost]"));
            
            //check CostDiscountAmount
            String costDiscountAmount = content[21].trim();
            FieldContentValidator validateCostDiscountAmount = null;
            validateCostDiscountAmount = new NoSpaceValidator();
            validateCostDiscountAmount = new NumberValidator(validateCostDiscountAmount);
            validateCostDiscountAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateCostDiscountAmount);
            FileParserUtil.getInstance().addError(rlt, validateCostDiscountAmount.validate(costDiscountAmount, getPrefixForDetail(i + 1) + "[Cost Discount Amount]"));
            
            //check ItemCost
            String itemCost = content[22].trim();
            FieldContentValidator validateItemCost = null;
            validateItemCost = new EmptyValidator();
            validateItemCost = new NoSpaceValidator(validateItemCost);
            validateItemCost = new NumberValidator(validateItemCost);
            validateItemCost = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemCost);
            FileParserUtil.getInstance().addError(rlt, validateItemCost.validate(itemCost, getPrefixForDetail(i + 1) + "[Item Cost]"));
            
            //check RetailPrice
            String retailPrice = content[23].trim();
            FieldContentValidator validateRetailPrice = null;
            validateRetailPrice = new NoSpaceValidator();
            validateRetailPrice = new NumberValidator(validateRetailPrice);
            validateRetailPrice = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateRetailPrice);
            FileParserUtil.getInstance().addError(rlt, validateRetailPrice.validate(retailPrice, getPrefixForDetail(i + 1) + "[Retail Price]"));
            
            //check ItemRetailAmount
            String itemRetailAmount = content[24].trim();
            FieldContentValidator validateItemRetailAmount = null;
            validateItemRetailAmount = new NoSpaceValidator();
            validateItemRetailAmount = new NumberValidator(validateItemRetailAmount);
            validateItemRetailAmount = new MaxValidator(BigDecimal.valueOf(99999999999.9999), validateItemRetailAmount);
            FileParserUtil.getInstance().addError(rlt, validateItemRetailAmount.validate(itemRetailAmount, getPrefixForDetail(i + 1) + "[Item Retail Amount]"));
            
            //check ReasonCode
            String reasonCode = content[25].trim();
            FieldContentValidator validateReasonCode = null;
            validateReasonCode = new LengthValidator(20, validateReasonCode);
            FileParserUtil.getInstance().addError(rlt, validateReasonCode.validate(reasonCode, getPrefixForDetail(i + 1) + "[Reason Code]"));
            
            //check ReasonDesc
            String reasonDesc = content[26].trim();
            FieldContentValidator validateReasonDesc = null;
            validateReasonDesc = new LengthValidator(100, validateReasonDesc);
            FileParserUtil.getInstance().addError(rlt, validateReasonDesc.validate(reasonDesc, getPrefixForDetail(i + 1) + "[Reason Desc]"));
            
            //check lineRefNo
            String lineRefNo = content[27].trim();
            FieldContentValidator validateLineRefNo = null;
            validateLineRefNo = new LengthValidator(20, validateLineRefNo);
            FileParserUtil.getInstance().addError(rlt, validateLineRefNo.validate(lineRefNo, getPrefixForDetail(i + 1) + "[Line Ref No]"));
            
        }
        
        return lineSeqs;
    }
    
    
    private void validateLocation(List<String[]> contents, List<String> rlt, List<String> lineSeqs, String docNo)
    {
        for (int i = 0; i < contents.size(); i++)
        {
            String[] content = contents.get(i);
            //check RtvNo
            String rtvNo = content[1].trim();
            FieldContentValidator validateRtvNo = null;
            validateRtvNo = new EmptyValidator();
            validateRtvNo = new NoSpaceValidator(validateRtvNo);
            validateRtvNo = new SpecialCharValidator(validateRtvNo, false, docNo);
            validateRtvNo = new LengthValidator(20, validateRtvNo);
            FileParserUtil.getInstance().addError(rlt, validateRtvNo.validate(rtvNo, getPrefixForLocation(i + 1) + "[Rtv No]"));
            
            //LineSeqNo
            String lineSeqNo = content[2].trim();
            FieldContentValidator validateLineSeqNo = null;
            validateLineSeqNo = new EmptyValidator();
            validateLineSeqNo = new IntegerValidator(validateLineSeqNo);
            validateLineSeqNo = new MaxValidator(Integer.valueOf(9999), validateLineSeqNo);
            FileParserUtil.getInstance().addError(rlt, validateLineSeqNo.validate(lineSeqNo, getPrefixForLocation(i + 1) + "[Line Seq No]"));
            if (!lineSeqs.contains(lineSeqNo))
            {
                rlt.add(getPrefixForLocation(i + 1) + "the LineSeqNo ["+ lineSeqNo +"] has no corresponding detail LineSeqNo.");
            }
            
            //LocationLineSeqNo
            String locationLineSeqNo = content[3].trim();
            FieldContentValidator validateLocationLineSeqNo = null;
            validateLocationLineSeqNo = new EmptyValidator();
            validateLocationLineSeqNo = new IntegerValidator(validateLocationLineSeqNo);
            validateLocationLineSeqNo = new MaxValidator(Integer.valueOf(9999), validateLocationLineSeqNo);
            FileParserUtil.getInstance().addError(rlt, validateLocationLineSeqNo.validate(locationLineSeqNo, getPrefixForLocation(i + 1) + "[Location Line Seq No]"));
            
            //LocationCode
            String locationCode = content[4].trim();
            FieldContentValidator validateLocationCode = null;
            validateLocationCode = new EmptyValidator();
            validateLocationCode = new NoSpaceValidator(validateLocationCode);
            validateLocationCode = new LengthValidator(20, validateLocationCode);
            FileParserUtil.getInstance().addError(rlt, validateLocationCode.validate(locationCode, getPrefixForLocation(i + 1) + "[Location Code]"));
            
            //LocationName
            String locationName = content[5].trim();
            FieldContentValidator validateLocationName = null;
            validateLocationName = new LengthValidator(100, validateLocationName);
            FileParserUtil.getInstance().addError(rlt, validateLocationName.validate(locationName, getPrefixForLocation(i + 1) + "[Location Name]"));
            
            //LocationShipQty
            String locationShipQty = content[6].trim();
            FieldContentValidator validateLocationShipQty = null;
            validateLocationShipQty = new EmptyValidator();
            validateLocationShipQty = new NoSpaceValidator(validateLocationShipQty);
            validateLocationShipQty = new NumberValidator(validateLocationShipQty);
            validateLocationShipQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateLocationShipQty);
            FileParserUtil.getInstance().addError(rlt, validateLocationShipQty.validate(locationShipQty, getPrefixForLocation(i + 1) + "[Location Ship Qty]"));
            
            //LocationFocQty
            String locationFocQty = content[7].trim();
            FieldContentValidator validateLocationFocQty = null;
            validateLocationFocQty = new NoSpaceValidator();
            validateLocationFocQty = new NumberValidator(validateLocationFocQty);
            validateLocationFocQty = new MaxValidator(BigDecimal.valueOf(999999.9999), validateLocationFocQty);
            FileParserUtil.getInstance().addError(rlt, validateLocationFocQty.validate(locationFocQty, getPrefixForLocation(i + 1) + "[Location Foc Qty]"));
            
        }
        
    }

    @Override
    protected void initData(byte[] input, DocMsg docMsg) throws Exception
    {
        RtvDocMsg rtvDocMsg = (RtvDocMsg)docMsg;
        InputStream inputStream = null;
        RtvHolder rtvHolder = null;
        RtvHeaderHolder rtvHeader = null;
        try
        {
            rtvHolder = new RtvHolder();
            inputStream = new ByteArrayInputStream(input);
            Map<String, List<String[]>> map = FileParserUtil.getInstance()
                .readLinesAndGroupByRecordType(inputStream);
            //record type is 'HDR'
            List<String[]> headerList = map.get(FileParserUtil.RECORD_TYPE_HEADER);
            rtvHeader = new RtvHeaderHolder();
            
            String rtvNo = headerList.get(0)[1].trim();
            
            rtvHeader.setRtvNo(rtvNo);
            rtvHolder.setRtvHeader(rtvHeader);
            rtvDocMsg.setData(rtvHolder);
        }
        finally
        {
            if (inputStream != null)
            {
                inputStream.close();
            }
        }
    }
    
}
