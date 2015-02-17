package com.pracbiz.b2bportal.core.eai.file.validator;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.eai.file.DefaultDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.GrnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MinValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocContextRef;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.Direction;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.FilenameParserVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.TradingPartnerHolder;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.TradingPartnerService;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

public abstract class FileValidator
{
    private static final Logger log = LoggerFactory.getLogger(FileValidator.class);
    protected static final String ID = "[FileValidator]";
    public static final String CANONICAL = "canonical";
    public static final String EBXML = "ebxml";
    public static final String FAIRPRICE = "fairprice";
    public static final String FP_IDOC = "FP-Idoc";
    public static final String WATSONS = "watsons";
    private static final String EXTEND_FIELD_VALUE_COLUMN = "[Field Value]";
    
    @Autowired private FilenameParserVisitor filenameParserVisitor;
    @Autowired private TradingPartnerService tradingPartnerService;
    @Autowired private SupplierService supplierService;
    @Autowired private BuyerService buyerService;
    @Autowired private MsgTransactionsService msgTransactionsService;
    @Autowired private CustomAppConfigHelper appConfig;
    @Autowired private StandardEmailSender standardEmailSender;
    
    public final Map<String, List<String>> validate(File file, String fileFormat, String senderCode, BigDecimal senderOid) throws IOException
    {
        Map<String , byte[]> fileMap = new HashMap<String, byte[]>();
        if (file.getName().endsWith(".zip"))
        {
            fileMap = GZIPHelper.getInstance().readFileFromZip(file);
        }
        else
        {
            fileMap.put(file.getName(), FileUtils.readFileToByteArray(file));
        }
        
        if (fileMap.containsKey("summary.csv"))
        {
            fileMap.remove("summary.csv");
        }
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (Entry<String, byte[]> entry : fileMap.entrySet())
        {
            if (entry.getKey().matches("(?i)source_[0-9]{14}.(?i)txt"))
            {
                continue;
            }
            
            try
            {
                MsgType msgType = DefaultDocFileHandler.parseTypeByFilename(entry.getKey());
                
                MsgTransactionsHolder msg = msgTransactionsService
                        .selectByMsgTypeAndOriginalFileName(msgType.name(), entry.getKey());
                if (msg != null)
                {
                    continue;
                }
                
                BatchMsg batch = new BatchMsg();
                
                String[] parts = FileUtil.getInstance()
                        .trimAllExtension(entry.getKey()).split("_");
                final String batchNo = parts[3].trim();
                
                batch.setBatchNo(batchNo);
                
                DocMsg docMsg = msgType.initDocMsg();
                docMsg.setOriginalFilename(entry.getKey());
                docMsg.accept(filenameParserVisitor);
                docMsg.setSenderCode(docMsg.getSenderCode()==null?senderCode:docMsg.getSenderCode());
                docMsg.setSenderOid(senderOid);
                docMsg.setBatch(batch);
                
                List<String> list = validateFile(docMsg, entry.getValue());
                if (list == null || list.isEmpty())
                {
                    list = validateRelationShip(docMsg);
                }
                if (list == null || list.isEmpty())
                {
                    initData(entry.getValue(), docMsg);
                    list = validateLogic(docMsg);
                }
                if (list != null && !list.isEmpty())
                {
                    map.put(entry.getKey(), list);
                }
            }
            catch (Exception e)
            {
                List<String> list = new ArrayList<String>();
                list.add("exception occurs");
                map.put(entry.getKey(), list);
                
                String tickNo1 = ErrorHelper.getInstance().logTicketNo(log, e);
                standardEmailSender.sendStandardEmail(ID, tickNo1, e);
            }
        }
        
        return map;
        
    }
    
    
    private final List<String> validateRelationShip(DocMsg docMsg) throws Exception
    {
        List<String> errors = new ArrayList<String>();
        if (docMsg.getMsgDirection().equals(Direction.outbound))
        {
            BuyerHolder buyer = buyerService.selectBuyerByBuyerCode(docMsg.getSenderCode());
            docMsg.setSenderOid(buyer.getBuyerOid());
            TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(docMsg.getSenderOid(),
                            docMsg.getReceiverCode());
            
            if (null == tradingPartner)
            {
                errors.add("Trading Partner [" + docMsg.getSenderCode()
                        + "-" + docMsg.getReceiverCode() + "] not exists.");
                return errors;
            }
            
            SupplierHolder supplier = supplierService
                    .selectSupplierByKey(tradingPartner.getSupplierOid());
            
            if (null == supplier)
            {
                errors.add("Supplier [" + docMsg.getReceiverCode()
                        + "] not exists.");
                return errors;
            }
            docMsg.setBuyer(buyer);
            docMsg.setReceiverOid(supplier.getSupplierOid());
            docMsg.setReceiverName(supplier.getSupplierName());
            DocContextRef context = new DocContextRef();
            context.setReceiverMailboxId(supplier.getMboxId());
            docMsg.setContext(context);
            docMsg.setSupplier(supplier);
            
            if (!supplier.getActive())
            {
                errors.add("Supplier [" + docMsg.getReceiverCode()
                        + "] is inactive.");
                return errors;
            }
        }
        else
        {
            BuyerHolder buyer = buyerService.selectBuyerByBuyerCodeWithBLOBs(docMsg
                    .getReceiverCode());
            if (null == buyer)
            {
                errors.add("Buyer [" + docMsg.getReceiverCode()
                    + "] not exists.");
                return errors;
            }

            TradingPartnerHolder tradingPartner = tradingPartnerService
                    .selectByBuyerAndBuyerGivenSupplierCode(buyer.getBuyerOid(),
                            docMsg.getSenderCode());
            if (null == tradingPartner)
            {
                errors.add("Trading Partner [" + docMsg.getReceiverCode()
                        + "-" + docMsg.getSenderCode() + "] not exists.");
                return errors;
            }
            
            SupplierHolder supplier = supplierService
                    .selectSupplierByKey(tradingPartner.getSupplierOid());
            
            if (null == supplier)
            {
                errors.add("Supplier [" + docMsg.getReceiverCode()
                        + "] not exists.");
                return errors;
            }
            docMsg.setReceiverOid(buyer.getBuyerOid());
            docMsg.setReceiverName(buyer.getBuyerName());
            DocContextRef context = new DocContextRef();
            context.setReceiverMailboxId(buyer.getMboxId());
            docMsg.setContext(context);
            docMsg.setBuyer(buyer);
            docMsg.setSupplier(supplier);
            
            if (!buyer.getActive())
            {
                
                errors.add("Buyer [" + docMsg.getReceiverCode()
                    + "] is inactive.");
                return errors;
            }
        }
        return errors;
    }
    
    
    protected abstract List<String> validateFile(DocMsg docMsg, byte[] input) throws Exception;
    
    
    protected abstract List<String> validateLogic(DocMsg docMsg) throws Exception;
    
    
    protected abstract void initData(byte[] input, DocMsg docMsg) throws Exception;
    
    public void validateCanonicalHeaderExtends(List<String[]> hexContents, List<String> errorMessage) throws Exception
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        for (int i = 0 ; i < hexContents.size(); i++)
        {
            String[] hexContent = hexContents.get(i);
            
            String fieldName = hexContent[2];
            FieldContentValidator validateFieldName = null;
            validateFieldName = new EmptyValidator();
            validateFieldName = new LengthValidator(30, validateFieldName);
            fileParseUtil.addError(errorMessage, validateFieldName.validate(fieldName, getPrefixForHeaderEx(i + 1) + "[Field Name]"));
            
            String fieldType = hexContent[3];
            FieldContentValidator validateFieldType = null;
            validateFieldType = new EmptyValidator();
            validateFieldType = new SpecialCharValidator(validateFieldType,
                false, "B", "F", "I", "S", "D");
            fileParseUtil.addError(errorMessage, validateFieldType.validate(fieldType, getPrefixForHeaderEx(i + 1) + "[Field Type]"));
            
            String fieldValue = hexContent[4];
            FieldContentValidator validateFieldValue = null;
            if (fieldType.equalsIgnoreCase("B"))
            {
                validateFieldValue = new SpecialCharValidator(validateFieldValue,
                    false, "TRUE", "FALSE");
                fileParseUtil.addError(errorMessage, validateFieldValue.validate(fieldValue, getPrefixForHeaderEx(i + 1) +EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("F"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "99999999999.9999"), validateFieldValue);
                fileParseUtil.addError(errorMessage, validateFieldValue.validate(fieldValue, getPrefixForHeaderEx(i + 1) +EXTEND_FIELD_VALUE_COLUMN));
                
            }
            
            if (fieldType.equalsIgnoreCase("I"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MinValidator(Integer.parseInt("-2147483648"), validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "2147483647"), validateFieldValue);
                fileParseUtil.addError(errorMessage, validateFieldValue.validate(fieldValue, getPrefixForHeaderEx(i + 1) +EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("S"))
            {
                validateFieldValue = new LengthValidator(255, validateFieldValue);
                fileParseUtil.addError(errorMessage, validateFieldValue.validate(fieldValue, getPrefixForHeaderEx(i + 1) +EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("D"))
            {
                if (fieldValue != null)
                {
                    fieldValue = fieldValue.replaceAll("T", " ");
                }
                validateFieldValue = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateFieldValue);
                fileParseUtil.addError(errorMessage, validateFieldValue.validate(fieldValue, getPrefixForHeaderEx(i + 1) +EXTEND_FIELD_VALUE_COLUMN));
            }
           
        }
    
    }
    
    
    public void validateCanonicalDetailExtends(List<String[]> dexContents, List<String> rlt)throws Exception
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        for (int i = 0 ; i < dexContents.size(); i++)
        {
            String[] dexContent = dexContents.get(i);
            
            String seqNum = dexContent[1];
            FieldContentValidator validateSeqNum = null;
            validateSeqNum = new EmptyValidator();
            validateSeqNum = new NumberValidator(validateSeqNum);
            validateSeqNum = new MinValidator(Integer.valueOf(1), validateSeqNum);
            validateSeqNum = new MaxValidator(Integer.valueOf(9999), validateSeqNum);
            fileParseUtil.addError(rlt, validateSeqNum.validate(seqNum, getPrefixForDetailEx(i + 1, seqNum) + "[Line Seq No]"));
            
            
            String fieldName = dexContent[2];
            FieldContentValidator validateFieldName = null;
            validateFieldName = new EmptyValidator();
            validateFieldName = new LengthValidator(30, validateFieldName);
            fileParseUtil.addError(rlt, validateFieldName.validate(fieldName, getPrefixForDetailEx(i + 1, seqNum) + "[Field Name]"));
            
            String fieldType = dexContent[3];
            FieldContentValidator validateFieldType = null;
            validateFieldType = new EmptyValidator();
            validateFieldType = new SpecialCharValidator(validateFieldType,
                false, "B", "F", "I", "S", "D");
            fileParseUtil.addError(rlt, validateFieldType.validate(fieldType, getPrefixForDetailEx(i + 1, seqNum) + "[Field Type]"));
            
            String fieldValue = dexContent[4];
            FieldContentValidator validateFieldValue = null;
            if (fieldType.equalsIgnoreCase("B"))
            {
                validateFieldValue = new SpecialCharValidator(validateFieldValue,
                    false, "TRUE", "FALSE");
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForDetailEx(i + 1, seqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("F"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "99999999999.9999"), validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForDetailEx(i + 1, seqNum) + EXTEND_FIELD_VALUE_COLUMN));
                
            }
            
            if (fieldType.equalsIgnoreCase("I"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MinValidator(Integer.parseInt("-2147483648"), validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "2147483647"), validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForDetailEx(i + 1, seqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("S"))
            {
                validateFieldValue = new LengthValidator(255, validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForDetailEx(i + 1, seqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("D"))
            {
                validateFieldValue = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForDetailEx(i + 1, seqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
        }
    }
    
    
    public void validateCanonicalLocationExtends(List<String[]> lexContents, List<String> rlt)throws Exception
    {
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        
        for (int i = 0 ; i < lexContents.size(); i++)
        {
            String[] lexContent = lexContents.get(i);
            
            String lineSeqNum = lexContent[2];
            FieldContentValidator validateLineSeqNum = null;
            validateLineSeqNum = new EmptyValidator();
            validateLineSeqNum = new NumberValidator();
            validateLineSeqNum = new MinValidator(Integer.valueOf(1), validateLineSeqNum);
            validateLineSeqNum = new MaxValidator(Integer.valueOf(9999), validateLineSeqNum);
            fileParseUtil.addError(rlt, validateLineSeqNum.validate(lineSeqNum, getPrefixForLocationEx(i + 1, lineSeqNum) + "[Location Line Seq No]"));
            
            String detailSeqNum = lexContent[1];
            FieldContentValidator validateDetailSeqNum = null;
            validateDetailSeqNum = new EmptyValidator();
            validateDetailSeqNum = new NumberValidator(validateDetailSeqNum);
            validateDetailSeqNum = new MinValidator(Integer.valueOf(1), validateDetailSeqNum);
            validateDetailSeqNum = new MaxValidator(Integer.valueOf(9999), validateDetailSeqNum);
            fileParseUtil.addError(rlt, validateDetailSeqNum.validate(detailSeqNum, getPrefixForLocationEx(i + 1, lineSeqNum) + "[Detail Line Seq No]"));
            
            
            String fieldName = lexContent[3];
            FieldContentValidator validateFieldName = null;
            validateFieldName = new EmptyValidator();
            validateFieldName = new LengthValidator(30, validateFieldName);
            fileParseUtil.addError(rlt, validateFieldName.validate(fieldName, getPrefixForLocationEx(i + 1, lineSeqNum) + "[Field Name]"));
            
            String fieldType = lexContent[4];
            FieldContentValidator validateFieldType = null;
            validateFieldType = new EmptyValidator();
            validateFieldType = new SpecialCharValidator(validateFieldType,
                    false, "B", "F", "I", "S", "D");
            fileParseUtil.addError(rlt, validateFieldType.validate(fieldType, getPrefixForLocationEx(i + 1, lineSeqNum) + "[Field Type]"));
            
            String fieldValue = lexContent[5];
            FieldContentValidator validateFieldValue = null;
            if (fieldType.equalsIgnoreCase("B"))
            {
                validateFieldValue = new SpecialCharValidator(validateFieldValue,
                    false, "TRUE", "FALSE");
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForLocationEx(i + 1, lineSeqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("F"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "99999999999.9999"), validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForLocationEx(i + 1, lineSeqNum) + EXTEND_FIELD_VALUE_COLUMN));
                
            }
            
            if (fieldType.equalsIgnoreCase("I"))
            {
                validateFieldValue = new NoSpaceValidator(validateFieldValue);
                validateFieldValue = new NumberValidator(validateFieldValue);
                validateFieldValue = new MinValidator(Integer.parseInt("-2147483648"), validateFieldValue);
                validateFieldValue = new MaxValidator(new BigDecimal(
                        "2147483647"), validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForLocationEx(i + 1, lineSeqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("S"))
            {
                validateFieldValue = new LengthValidator(255, validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForLocationEx(i + 1, lineSeqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
            
            if (fieldType.equalsIgnoreCase("D"))
            {
                validateFieldValue = new DateValidator(GrnDocFileHandler.CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS, validateFieldValue);
                fileParseUtil.addError(rlt, validateFieldValue.validate(fieldValue, getPrefixForLocationEx(i + 1, lineSeqNum) + EXTEND_FIELD_VALUE_COLUMN));
            }
        }
    }
    
    
    public String getPrefixForHeader()
    {
        return "Header ------ ";
    }
    
    
    public String getPrefixForHeaderEx(int seq)
    {
        return "Header extended [seq= " + seq + "]------ ";
    }
    
    
    public String getPrefixForDetail(int no)
    {
        return "The " + no + "'s detail ------ ";
    }
    
    
    public String getPrefixForDetailEx(int no, String seq)
    {
        return "The " + no + "'s detail extended [seq= " + seq + "] ------ ";
    }
    
    
    public String getPrefixForLocation(int no)
    {
        return "The " + no + "'s location ------ ";
    }
    
    
    public String getPrefixForLocationEx(int no, String seq)
    {
        return "The " + no + "'s location extended [seq= " + seq + "] ------ ";
    }
    
    public int getMaxInvNoLength()
    {

        int maxInvNoLenth = 1;
        try
        {
            maxInvNoLenth = Integer.parseInt(appConfig.getMaxInvNoLength());
            
            if (maxInvNoLenth > 20)
            {
                maxInvNoLenth = 20;
            }
            
            if (maxInvNoLenth < 1)
            {
                maxInvNoLenth = 1;
            }
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            return 20;
        }
        
        return maxInvNoLenth;
    }
}
