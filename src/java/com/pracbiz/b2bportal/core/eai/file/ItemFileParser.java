package com.pracbiz.b2bportal.core.eai.file;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


import au.com.bytecode.opencsv.CSVReader;


import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MaxValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.MinValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.SpecialCharValidator;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.extension.MsgTransactionsExHolder;

public class ItemFileParser
{
    private static final Logger log = LoggerFactory.getLogger(ItemFileParser.class);
    
    private static String CANONICAL_FILE_SPEARATE_SYMBOL = "|";
    
    private static final String FILE_FORMAT_CONANICAL = "canonical";
    private static final String FILE_FORMAT_FAIRPRICE = "fairprice";
    
    private static final int ITEM_FILE_LENGTH = 22;
    private static final int ITEM_FILE_LENGTH_FP = 13;
    
    public List<String> parseCanonicalLineAsList(String line)
    {
        List<String> rlt = Arrays.asList(this.parseCanonicalLineAsArray(line));
        
        return rlt;
    }
    
    
    public String[] parseCanonicalLineAsArray(String line)
    {
        String[] rlt = StringUtils.delimitedListToStringArray(line, CANONICAL_FILE_SPEARATE_SYMBOL);
        
        return rlt;
    }
    
    
    public List<List<String>> parseCanonicalFileAsList(File file) throws IOException
    {
        String[] lines = FileParserUtil.getInstance().readLines(file);
        
        if (null == lines || lines.length == 0)
            return null;
        
        List<List<String>> rlt = new ArrayList<List<String>>();
        
        for (String line : lines)
        {
            List<String> lineList = this.parseCanonicalLineAsList(line);
            
            rlt.add(lineList);
        }
        
        return rlt;
    }
    
    
    public List<String[]> parseCanonicalFileAsArray(File file) throws IOException
    {
        String[] lines = FileParserUtil.getInstance().readLines(file);
        
        if (null == lines || lines.length == 0)
            return null;
        
        List<String[]> rlt = new ArrayList<String[]>();
        
        for (String line : lines)
        {
            String[] lineArray = this.parseCanonicalLineAsArray(line);
            
            rlt.add(lineArray);
        }
        
        return rlt;
    }
    
    
    public List<String> parseFairpriceLineAsList(String line) throws IOException
    {
        List<String> rlt = Arrays.asList(this.parseFairpriceLineAsArray(line));
        
        return rlt;
    }
    
    
    public String[] parseFairpriceLineAsArray(String line) throws IOException
    {
        StringReader sr = new StringReader(line);
        CSVReader reader = new CSVReader(sr, ',', '\"');
        String[] lineContent = null;
        try
        {
            lineContent = reader.readNext();
        }
        finally
        {
           if (reader != null)
           {
               reader.close();
               reader = null;
           }
           if (sr != null)
           {
               sr.close();
               sr = null;
           }
        }
        
        return lineContent;
    }
    
    
    public List<List<String>> parseFairpriceFileAsList(File file) throws IOException
    {
        String[] lines = FileParserUtil.getInstance().readLines(file);
        
        if (null == lines || lines.length == 0)
            return null;
        
        List<List<String>> rlt = new ArrayList<List<String>>();
        
        for (String line : lines)
        {
            List<String> lineList = this.parseFairpriceLineAsList(line);
            
            rlt.add(lineList);
        }
        
        return rlt;
    }
    
    
    public List<String[]> parseFairpriceFileAsArray(File file) throws IOException
    {
        String[] lines = FileParserUtil.getInstance().readLines(file);
        
        if (null == lines || lines.length == 0)
            return null;
        
        List<String[]> rlt = new ArrayList<String[]>();
        
        for (String line : lines)
        {
            String[] lineArray = this.parseFairpriceLineAsArray(line);
            
            rlt.add(lineArray);
        }
        
        return rlt;
    }
    
    
    public Map<String, ItemHolder> initiateFileItemMap(BuyerHolder buyer,
        MsgTransactionsExHolder msgTrans, List<String> content, String fileFormat)
    {
        Map<String, ItemHolder> insertItemMap = new HashMap<String, ItemHolder>();
        
        if (FILE_FORMAT_CONANICAL.equalsIgnoreCase(fileFormat))
        {
            for (int i = 1; i < content.size(); i++)
            {
                List<String> line = ItemFileParser.getInstance().parseCanonicalLineAsList(content.get(i));
                
                if (insertItemMap.containsKey(line.get(0).trim()))
                {
                    // we only handle the barcodes part for duplicated lines.
                    ItemHolder item = insertItemMap.get(line.get(0).trim());
                    
                    item.addBarcode(line.get(15).trim());
                    item.addBarcode(line.get(16).trim());
                    item.addBarcode(line.get(17).trim());
                    item.addBarcode(line.get(18).trim());
                    item.addBarcode(line.get(19).trim());
                    
                    continue;
                }
                
                ItemHolder item = initItemHolderConanical(buyer.getBuyerOid(), line, msgTrans);
                insertItemMap.put(item.getBuyerItemCode(), item);
            }
        }
        else if (FILE_FORMAT_FAIRPRICE.equalsIgnoreCase(fileFormat))
        {
            for (int i = 1; i < content.size(); i++)
            {
                List<String> line = null;
                try
                {
                    line = ItemFileParser.getInstance().parseFairpriceLineAsList(content.get(i));
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                    
                    continue;
                }
                
                if (insertItemMap.containsKey(line.get(0).trim()))
                {
                    // we only handle the barcodes part for duplicated lines.
                    ItemHolder item = insertItemMap.get(line.get(0).trim());
                    
                    item.addBarcode(line.get(10).trim());
                    continue;
                }
                
                ItemHolder item = initItemHolderFairprice(buyer.getBuyerOid(), line, msgTrans);
                insertItemMap.put(item.getBuyerItemCode(), item);
            }
        }
        
        return insertItemMap;
    }
    
    
    public ItemHolder initItemHolderConanical(BigDecimal buyerOid, List<String> line, MsgTransactionsExHolder msgTrans)
    {
        ItemHolder item = new ItemHolder();
        
        item.setBuyerOid(buyerOid);
        item.setBuyerItemCode(line.get(0).trim());
        item.setItemDesc(line.get(1).trim());
        item.setSupplierItemCode(line.get(2).trim());
        item.setBrand(line.get(3).trim());
        item.setColourCode(line.get(4).trim());
        item.setColourDesc(line.get(5).trim());
        item.setSizeCode(line.get(6).trim());
        item.setSizeDesc(line.get(7).trim());
        item.setUom(line.get(8).trim());
        item.setClassCode(line.get(9).trim());
        item.setClassDesc(line.get(10).trim());
        item.setSubclassCode(line.get(11).trim());
        item.setSubclassDesc(line.get(12).trim());
        item.setUnitCost(BigDecimalUtil.getInstance().convertStringToBigDecimal(line.get(13).trim(), 4));
        item.setRetailPrice(BigDecimalUtil.getInstance().convertStringToBigDecimal(line.get(14).trim(), 4));
        item.setActive(line.get(20).trim().equalsIgnoreCase("Y") ? true : false);
        item.setCreateDate(DateUtil.getInstance().convertStringToDate(line.get(21).trim(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        item.setUpdateDate(new Date());
        item.setDocOid(msgTrans.getDocOid());
        
        item.addBarcode(line.get(15).trim());
        item.addBarcode(line.get(16).trim());
        item.addBarcode(line.get(17).trim());
        item.addBarcode(line.get(18).trim());
        item.addBarcode(line.get(19).trim());
        
        return item;
    }
    
    
    public ItemHolder initItemHolderFairprice(BigDecimal buyerOid, List<String> line, MsgTransactionsExHolder msgTrans)
    {
        ItemHolder item = new ItemHolder();
        
        item.setBuyerOid(buyerOid);
        item.setBuyerItemCode(line.get(0).trim());
        item.setSupplierItemCode(line.get(1).trim());
        item.setItemDesc(line.get(2).trim());
        item.setBrand(line.get(3).trim());
        item.setUom(line.get(4).trim());
        item.setClassCode(line.get(5).trim());
        if (!line.get(6).trim().isEmpty() && !line.get(7).trim().isEmpty())
        {
            item.setSubclassCode(line.get(6).trim() + "-" + line.get(7).trim());
        }
        else if (!line.get(6).trim().isEmpty() && line.get(7).trim().isEmpty())
        {
            item.setSubclassCode(line.get(6).trim());
        }
        else if (line.get(6).trim().isEmpty() && !line.get(7).trim().isEmpty())
        {
            item.setSubclassCode(line.get(7).trim());
        }
        item.setUnitCost(BigDecimalUtil.getInstance().convertStringToBigDecimal(line.get(8).trim(), 4));
        item.setRetailPrice(BigDecimalUtil.getInstance().convertStringToBigDecimal(line.get(9).trim(), 4));
        
        item.setActive(line.get(11).trim().equalsIgnoreCase("Y") ? true : false);
        if (!line.get(12).trim().isEmpty())
        {
            item.setCreateDate(DateUtil.getInstance().convertStringToDate(line.get(12).trim(), DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        }
        item.setUpdateDate(new Date());
        item.setDocOid(msgTrans.getDocOid());
        item.addBarcode(line.get(10).trim());
        
        return item;
    }
    
    
    public List<String> filterErrorContent(String filename, List<String> fileContent, String fileFormat)
    {
        int lineNum = 0;
        List<String> fileErrors = new ArrayList<String>();
        Map<String, String> itemCodeMap = new HashMap<String, String>();
        
        if (fileContent == null || fileContent.size() < 2)
        {
            fileErrors.add("[" + filename + "] is empty.");

            return fileErrors;
        }
        
        boolean isEmptyFile = true;
        for (Iterator<String> itr = fileContent.iterator();itr.hasNext();)
        {
            
            
            lineNum ++;
           
            if (lineNum == 1)
            {
                // skip header line.
                itr.next();
                continue;
            }
            
            String line = itr.next();
            line = line.trim();
          
            if (line.isEmpty())
            {
                continue;
            }
            
            isEmptyFile = false;
            
            List<String> lineErrors = null;
            
            if (FILE_FORMAT_CONANICAL.equalsIgnoreCase(fileFormat))
            {
                lineErrors = this.checkConanicalFileLine(line, lineNum, itemCodeMap);
            }
            else if (FILE_FORMAT_FAIRPRICE.equalsIgnoreCase(fileFormat))
            {
                try
                {
                    lineErrors = this.checkFairpriceFileLine(line, lineNum, itemCodeMap);
                }
                catch(IOException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                    
                    lineErrors = new ArrayList<String>();
                    lineErrors.add("Failed to read line: " + line + ".");
                }
            }
            
            if (lineErrors != null && !lineErrors.isEmpty())
            {
                fileErrors.addAll(lineErrors);
                itr.remove();
            }
        }
        
        if (isEmptyFile)
        {
            fileErrors.add("[" + filename + "] is empty.");

            return fileErrors;
        }
        
        if (fileErrors.isEmpty())
        {
            return null;
        }
        
        return fileErrors;
    }
    
    
    private List<String> checkConanicalFileLine(String content, int lineNumber, Map<String, String> itemCodeMap)
    {
        List<String> rlt = new ArrayList<String>();
        
        String[] line = ItemFileParser.getInstance().parseCanonicalLineAsArray(content);

        // check line length
        if (line.length != ITEM_FILE_LENGTH)
        {
            rlt.add("Line [" + lineNumber + "] ------ This line has ["
                    + line.length + "] columns. "
                    + ITEM_FILE_LENGTH + " columns expected.");
            
            return rlt;
        }

        // check buyerItemCode
        String buyerItemCode = line[0].trim();
        FieldContentValidator validateBuyerItemCode = null;
        validateBuyerItemCode = new EmptyValidator();
        validateBuyerItemCode = new NoSpaceValidator(validateBuyerItemCode);
        validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
        FileParserUtil.getInstance().addError(rlt, validateBuyerItemCode.validate(buyerItemCode,  "Line [" + lineNumber + "] ------ [Buyer Item Code]"));
        if (buyerItemCode != null && !buyerItemCode.isEmpty())
        {
            if (itemCodeMap.containsKey(buyerItemCode))
            {
                //rlt.add("Line [" + lineNumber + "] ------ [Buyer Item Code] has been exists at line " + itemCodeMap.get(buyerItemCode) + ".");
            }
            else
            {
                itemCodeMap.put(buyerItemCode, String.valueOf(lineNumber));
            }
        }
        
        // check itemDesc
        String itemDesc = line[1].trim();
        FieldContentValidator validateItemDesc = null;
        validateItemDesc = new EmptyValidator();
        validateItemDesc = new LengthValidator(100, validateItemDesc);
        FileParserUtil.getInstance().addError(rlt, validateItemDesc.validate(itemDesc, "Line [" + lineNumber + "] ------ [Item Desc]"));
        
        // check supplierItemCode
        String supplierItemCode = line[2].trim();
        FieldContentValidator validateSupplierItemCode = null;
        validateSupplierItemCode = new NoSpaceValidator(validateSupplierItemCode);
        validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
        FileParserUtil.getInstance().addError(rlt, validateSupplierItemCode.validate(supplierItemCode, "Line [" + lineNumber + "] ------ [Supplier Item Code]"));
        
        // check brand
        String brand = line[3].trim();
        FieldContentValidator validateBrand = null;
        validateBrand = new LengthValidator(50, validateBrand);
        FileParserUtil.getInstance().addError(rlt, validateBrand.validate(brand, "Line [" + lineNumber + "] ------ [Brand]"));
        
        // check colourCode
        String colourCode = line[4].trim();
        FieldContentValidator validateColourCode = null;
        validateColourCode = new LengthValidator(20, validateColourCode);
        FileParserUtil.getInstance().addError(rlt, validateColourCode.validate(colourCode, "Line [" + lineNumber + "] ------ [Colour Code]"));
        
        // check colourDesc
        String colourDesc = line[5].trim();
        FieldContentValidator validateColourDesc = null;
        validateColourDesc = new LengthValidator(50, validateColourDesc);
        FileParserUtil.getInstance().addError(rlt, validateColourDesc.validate(colourDesc, "Line [" + lineNumber + "] ------ [Colour Desc]"));
        
        // check sizeCode
        String sizeCode = line[6].trim();
        FieldContentValidator validateSizeCode = null;
        validateSizeCode = new LengthValidator(10, validateSizeCode);
        FileParserUtil.getInstance().addError(rlt, validateSizeCode.validate(sizeCode, "Line [" + lineNumber + "] ------ [Size Code]"));
        
        // check sizeDesc
        String sizeDesc = line[7].trim();
        FieldContentValidator validateSizeDesc = null;
        validateSizeDesc = new LengthValidator(20, validateSizeDesc);
        FileParserUtil.getInstance().addError(rlt, validateSizeDesc.validate(sizeDesc, "Line [" + lineNumber + "] ------ [Address1]"));
        
        // check uom
        String uom = line[8].trim();
        FieldContentValidator validateUom = null;
        validateUom = new EmptyValidator();
        validateUom = new LengthValidator(20, validateUom);
        FileParserUtil.getInstance().addError(rlt, validateUom.validate(uom, "Line [" + lineNumber + "] ------ [Uom]"));
        
        // check classCode
        String classCode = line[9].trim();
        FieldContentValidator validateClassCode = null;
        validateClassCode = new LengthValidator(20, validateClassCode);
        FileParserUtil.getInstance().addError(rlt, validateClassCode.validate(classCode, "Line [" + lineNumber + "] ------ [Class Code]"));
        
        // check classDesc
        String classDesc = line[10].trim();
        FieldContentValidator validateClassDesc = null;
        validateClassDesc = new LengthValidator(50, validateClassDesc);
        FileParserUtil.getInstance().addError(rlt, validateClassDesc.validate(classDesc, "Line [" + lineNumber + "] ------ [Class Desc]"));
        
        // check subclassCode
        String subclassCode = line[11].trim();
        if (subclassCode != null && !subclassCode.trim().equals("") && (classCode == null || classCode.trim().equals("")))
        {
            rlt.add("Line [" + lineNumber + "] ------ [Sub Class Code] needs corresponding [Class Code]. ");
        }
        else
        {
            FieldContentValidator validateSubclassCode = null;
            validateSubclassCode = new LengthValidator(20, validateSubclassCode);
            FileParserUtil.getInstance().addError(rlt, validateSubclassCode.validate(subclassCode, "Line [" + lineNumber + "] ------ [Sub Class Code]"));
        }
        
        // check subclassDesc
        String subclassDesc = line[12].trim();
        FieldContentValidator validateSubclassDesc = null;
        validateSubclassDesc = new LengthValidator(50, validateSubclassDesc);
        FileParserUtil.getInstance().addError(rlt, validateSubclassDesc.validate(subclassDesc, "Line [" + lineNumber + "] ------ [Sub Class Desc]"));
        
        // check unitCost
        String unitCost = line[13].trim();
        FieldContentValidator validateUnitCost = null;
        validateUnitCost = new EmptyValidator();
        validateUnitCost = new NumberValidator(validateUnitCost);
        validateUnitCost = new NoSpaceValidator(validateUnitCost);
        validateUnitCost = new MinValidator(0, validateUnitCost);
        validateUnitCost = new MaxValidator(new BigDecimal("99999999999.9999"), validateUnitCost);
        FileParserUtil.getInstance().addError(rlt, validateUnitCost.validate(unitCost, "Line [" + lineNumber + "] ------ [Unit Cost]"));
        
        // check retailPrice
        String retailPrice = line[14].trim();
        FieldContentValidator validateRetailPrice = null;
        validateRetailPrice = new NumberValidator(validateRetailPrice);
        validateRetailPrice = new NoSpaceValidator(validateRetailPrice);
        validateRetailPrice = new MinValidator(0, validateRetailPrice);
        validateRetailPrice = new MaxValidator(new BigDecimal("99999999999.9999"), validateRetailPrice);
        FileParserUtil.getInstance().addError(rlt, validateRetailPrice.validate(retailPrice, "Line [" + lineNumber + "] ------ [Retail Price]"));
        
        // check barCode1
        String barCode1 = line[15].trim();
        FieldContentValidator validateBarCode1 = null;
        validateBarCode1 = new LengthValidator(50, validateBarCode1);
        FileParserUtil.getInstance().addError(rlt, validateBarCode1.validate(barCode1, "Line [" + lineNumber + "] ------ [Barcode1]"));
        
        // check barCode2
        String barCode2 = line[16].trim();
        FieldContentValidator validateBarCode2 = null;
        validateBarCode2 = new LengthValidator(50, validateBarCode2);
        FileParserUtil.getInstance().addError(rlt, validateBarCode2.validate(barCode2, "Line [" + lineNumber + "] ------ [BarCode2]"));
        
        // check barCode3
        String barCode3 = line[17].trim();
        FieldContentValidator validateBarCode3 = null;
        validateBarCode3 = new LengthValidator(50, validateBarCode3);
        FileParserUtil.getInstance().addError(rlt, validateBarCode3.validate(barCode3, "Line [" + lineNumber + "] ------ [BarCode3]"));
        
        // check barCode4
        String barCode4 = line[18].trim();
        FieldContentValidator validateBarCode4 = null;
        validateBarCode4 = new LengthValidator(50, validateBarCode4);
        FileParserUtil.getInstance().addError(rlt, validateBarCode4.validate(barCode4, "Line [" + lineNumber + "] ------ [BarCode4]"));
        
        // check barCode4
        String barCode5 = line[19].trim();
        FieldContentValidator validateBarCode5 = null;
        validateBarCode5 = new LengthValidator(50, validateBarCode5);
        FileParserUtil.getInstance().addError(rlt, validateBarCode5.validate(barCode5, "Line [" + lineNumber + "] ------ [BarCode5]"));
        
        // check active
        String active = line[20].trim();
        FieldContentValidator validateActive = null;
        validateActive = new EmptyValidator();
        validateActive = new SpecialCharValidator(validateActive, false, "Y", "N");
        FileParserUtil.getInstance().addError(rlt, validateActive.validate(active, "Line [" + lineNumber + "] ------ [Active]"));
        
        // check CreateDate
        String createDate = line[21].trim();
        FieldContentValidator validateCreateDate = null;
        validateCreateDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS, validateCreateDate);
        FileParserUtil.getInstance().addError(rlt, validateCreateDate.validate(createDate, "Line [" + lineNumber + "] ------ [Create Date]"));
        
        return rlt;
    }
    
    
    private List<String> checkFairpriceFileLine(String content, int lineNumber, Map<String, String> itemCodeMap) throws IOException
    {
        List<String> rlt = new ArrayList<String>();
        
        String[] line = ItemFileParser.getInstance().parseFairpriceLineAsArray(content);
     
        // check line length
        if (line.length != ITEM_FILE_LENGTH_FP)
        {
            rlt.add("Line [" + lineNumber + "] ------ This line has ["
                    + line.length + "] columns. "
                    + ITEM_FILE_LENGTH_FP + " columns expected.");
            
            return rlt;
        }
        
        // check buyerItemCode
        String buyerItemCode = line[0].trim();
        FieldContentValidator validateBuyerItemCode = null;
        validateBuyerItemCode = new EmptyValidator();
        validateBuyerItemCode = new NoSpaceValidator(validateBuyerItemCode);
        validateBuyerItemCode = new LengthValidator(20, validateBuyerItemCode);
        FileParserUtil.getInstance().addError(rlt, validateBuyerItemCode.validate(buyerItemCode,  "Line [" + lineNumber + "] ------ [Buyer Item Code]"));
        if (buyerItemCode != null && !buyerItemCode.isEmpty())
        {
            if (itemCodeMap.containsKey(buyerItemCode))
            {
                //rlt.add("Line [" + lineNumber + "] ------ [Buyer Item Code] has been exists at line " + itemCodeMap.get(buyerItemCode) + ".");
            }
            else
            {
                itemCodeMap.put(buyerItemCode, String.valueOf(lineNumber));
            }
        }
        
        // check supplierItemCode
        String supplierItemCode = line[1].trim();
        FieldContentValidator validateSupplierItemCode = null;
        validateSupplierItemCode = new NoSpaceValidator(validateSupplierItemCode);
        validateSupplierItemCode = new LengthValidator(20, validateSupplierItemCode);
        FileParserUtil.getInstance().addError(rlt, validateSupplierItemCode.validate(supplierItemCode, "Line [" + lineNumber + "] ------ [Supplier Item Code]"));
        
        // check itemDesc
        String itemDesc = line[2].trim();
        FieldContentValidator validateItemDesc = null;
        validateItemDesc = new EmptyValidator();
        validateItemDesc = new LengthValidator(100, validateItemDesc);
        FileParserUtil.getInstance().addError(rlt, validateItemDesc.validate(itemDesc, "Line [" + lineNumber + "] ------ [Item Desc]"));
        
        // check brand
        String brand = line[3].trim();
        FieldContentValidator validateBrand = null;
        validateBrand = new LengthValidator(50, validateBrand);
        FileParserUtil.getInstance().addError(rlt, validateBrand.validate(brand, "Line [" + lineNumber + "] ------ [Brand]"));
        
        // check uom
        String uom = line[4].trim();
        FieldContentValidator validateUom = null;
        validateUom = new EmptyValidator();
        validateUom = new LengthValidator(20, validateUom);
        FileParserUtil.getInstance().addError(rlt, validateUom.validate(uom, "Line [" + lineNumber + "] ------ [Uom]"));
        
        // check classCode
        String classCode = line[5].trim();
        FieldContentValidator validateClassCode = null;
        validateClassCode = new LengthValidator(20, validateClassCode);
        FileParserUtil.getInstance().addError(rlt, validateClassCode.validate(classCode, "Line [" + lineNumber + "] ------ [Class Code]"));
        
        
        // check brand type
        String brandType = line[6].trim();
        FieldContentValidator validateBrandType = null;
        validateBrandType = new SpecialCharValidator(validateBrandType, false, "NH", "HO");
        FileParserUtil.getInstance().addError(rlt, validateBrandType.validate(brandType, "Line [" + lineNumber + "] ------ [Brand Type]"));
        
        
        // check purchase group
        String purchaseGroup = line[7].trim();
        FieldContentValidator validatePurchaseGroup = null;
        validatePurchaseGroup = new SpecialCharValidator(validatePurchaseGroup, false, "F01", "F02");
        FileParserUtil.getInstance().addError(rlt, validatePurchaseGroup.validate(purchaseGroup, "Line [" + lineNumber + "] ------ [Purchase Group]"));
        
        if(((brandType != null && !brandType.trim().isEmpty())
            || (purchaseGroup != null && !purchaseGroup.trim().isEmpty()))
            && (classCode == null || classCode.trim().isEmpty()))
        {
            rlt.add("Line [" + lineNumber + "] ------ Get [Sub Class Code], but no corresponding [Class Code]. ");
        }
        
        // check unitCost
        String unitCost = line[8].trim();
        FieldContentValidator validateUnitCost = null;
        validateUnitCost = new EmptyValidator();
        validateUnitCost = new NumberValidator(validateUnitCost);
        validateUnitCost = new NoSpaceValidator(validateUnitCost);
        validateUnitCost = new MinValidator(0, validateUnitCost);
        validateUnitCost = new MaxValidator(new BigDecimal("99999999999.9999"), validateUnitCost);
        FileParserUtil.getInstance().addError(rlt, validateUnitCost.validate(unitCost, "Line [" + lineNumber + "] ------ [Unit Cost]"));
        
        
        // check retailPrice
        String retailPrice = line[9].trim();
        FieldContentValidator validateRetailPrice = null;
        validateRetailPrice = new NumberValidator(validateRetailPrice);
        validateRetailPrice = new NoSpaceValidator(validateRetailPrice);
        validateRetailPrice = new MinValidator(0, validateRetailPrice);
        validateRetailPrice = new MaxValidator(new BigDecimal("99999999999.9999"), validateRetailPrice);
        FileParserUtil.getInstance().addError(rlt, validateRetailPrice.validate(retailPrice, "Line [" + lineNumber + "] ------ [Retail Price]"));
        
        // check barCode
        String barCode = line[10].trim();
        FieldContentValidator validateBarCode1 = null;
        validateBarCode1 = new LengthValidator(50, validateBarCode1);
        FileParserUtil.getInstance().addError(rlt, validateBarCode1.validate(barCode, "Line [" + lineNumber + "] ------ [Barcode1]"));
        
        // check active
        String active = line[11].trim();
        FieldContentValidator validateActive = null;
        validateActive = new EmptyValidator();
        validateActive = new SpecialCharValidator(validateActive, false, "Y", "N");
        FileParserUtil.getInstance().addError(rlt, validateActive.validate(active, "Line [" + lineNumber + "] ------ [Active]"));
        
        // check CreateDate
        String createDate = line[12].trim();
        FieldContentValidator validateCreateDate = null;
        validateCreateDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS, validateCreateDate);
        FileParserUtil.getInstance().addError(rlt, validateCreateDate.validate(createDate, "Line [" + lineNumber + "] ------ [Create Date]"));
        
        return rlt;
    }
    
    
    //*****************************************************
    // singleton class
    //*****************************************************
    
    private static ItemFileParser instance;
    private ItemFileParser(){}
    
    public static ItemFileParser getInstance()
    {
        synchronized(ItemFileParser.class)
        {
            if (instance == null)
            {
                instance = new ItemFileParser();
            }
        }
        
        return instance;
    }
    
    
    //*****************************************************
    // test
    //*****************************************************
    
    /*public static void main(String[] args) throws IOException
    {
        File canonical = new File("/Users/ouyang/Desktop/tmp/IM_buyerfu_20131001000000.txt");
        File fairprice = new File("/Users/ouyang/Desktop/tmp/IM_FP_20131227120409.csv");
        
        {
            System.out.println("\n\nTest canonical : read as List...");
            
            long begin = System.currentTimeMillis();
            List<List<String>> rlt = ItemFileParser.getInstance().parseCanonicalFileAsList(canonical);
            long end = System.currentTimeMillis();
            
            System.out.println("takes: " + (end - begin));
            System.out.println(rlt.size());
            System.out.println(rlt.get(0).get(3));
        }
        
        
        {
            System.out.println("\n\nTest canonical : read as Array...");
            
            long begin = System.currentTimeMillis();
            List<String[]> rlt = ItemFileParser.getInstance().parseCanonicalFileAsArray(canonical);
            long end = System.currentTimeMillis();
            
            System.out.println("takes: " + (end - begin));
            System.out.println(rlt.size());
            System.out.println(rlt.get(0)[3]);
        }
        
        
        {
            System.out.println("\n\nTest fairprice : read as List...");
        
            long begin = System.currentTimeMillis();
            List<List<String>> rlt = ItemFileParser.getInstance().parseFairpriceFileAsList(fairprice);
            long end = System.currentTimeMillis();
            
            System.out.println("takes: " + (end - begin));
            System.out.println(rlt.size());
            System.out.println(rlt.get(0).get(2));
        }
        
        
        {
            System.out.println("\n\nTest fairprice : read as Array...");
            
            long begin = System.currentTimeMillis();
            List<String[]> rlt = ItemFileParser.getInstance().parseFairpriceFileAsArray(fairprice);
            long end = System.currentTimeMillis();
            
            System.out.println("takes: " + (end - begin));
            System.out.println(rlt.size());
            System.out.println(rlt.get(0)[2]);
        }
    }*/
}

