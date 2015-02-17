//*****************************************************************************
//
// File Name       :  TansGrnTranslator.java
// Date Created    :  Apr 4, 2014
// Last Changed By :  $Author: eidt $
// Last Changed On :  $Date: Apr 4, 2014 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2014.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file.translator;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.com.bytecode.opencsv.CSVReader;

import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.core.eai.file.DocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.canonical.GrnDocFileHandler;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NumberValidator;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class TangsGrnTranslator extends SourceTranslator
{
    private static final Logger log = LoggerFactory.getLogger(TangsGrnTranslator.class);
    private static final String FILE_PREFIX_GRL = "GRL";
    private static final String DOC_ACTION_ADD = "A";
    @Autowired GrnDocFileHandler conanicalDocFileHandler;
    
    
    @Override
    protected Map<String, List<String>> validateFile(Map<String, byte[]> files)
        throws Exception
    {

        if (files == null || files.isEmpty())
        {
            return null;
        }
        
        Map<String, List<String>> errorMessages = new HashMap<String, List<String>>();
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        for (Map.Entry<String, byte[]>  entry : files.entrySet())
        {
            CSVReader reader = null;
            List<String> contents = fileUtil.readLines(entry.getValue());
            for (int i = 1 ; i < contents.size() ; i++)
            {
                reader = new CSVReader(new StringReader(contents.get(i)), ',');
                String[] rowContents = reader.readNext();
                if (entry.getKey().startsWith(FILE_PREFIX_GRL))
                {
                    validateDetailFile(rowContents, errorMessages, entry.getKey());
                }
                else
                {
                    validateHeaderFile(rowContents, errorMessages, entry.getKey());
                }
            }
        }
        
        
        //invalid the validation yet.
        return null;
    }

    @Override
    protected Map<String, byte[]> translate(Map<String, byte[]> files,
        String outPath, BuyerHolder buyer, File sourceFile)
        throws Exception
    {
        if (files == null || files.isEmpty())
        {
            return null;
        }
        
        Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
        FileParserUtil fileUtil = FileParserUtil.getInstance();
        for (Map.Entry<String, byte[]>  entry : files.entrySet())
        {
            if (entry.getKey().startsWith(FILE_PREFIX_GRL))
            {
                continue;
            }
            
            CSVReader reader = null;
            List<String> contents = fileUtil.readLines(entry.getValue());
            
            String subFilename = FILE_PREFIX_GRL + entry.getKey().substring(entry.getKey().indexOf('_'), entry.getKey().lastIndexOf("_")) + ".csv";
            List<String> detailContents = fileUtil.readLines(files.get(subFilename));

            
            GrnHeaderHolder header = null;
            List<GrnDetailHolder>  details = null;
            List<GrnHeaderExtendedHolder> hexList = null;
            
            for (int i = 1 ; i < contents.size() ; i++)
            {
                reader = new CSVReader(new StringReader(contents.get(i)), ',');
                String[] rowContents = reader.readNext();
                header = initGrnHeader(rowContents, buyer);
                hexList = initGrnHex(rowContents, buyer);
                
                if (files.containsKey(subFilename))
                {
                   details = initGrnDetails(detailContents, buyer, header);
                }
                
                GrnHolder grn = new GrnHolder();
                
                grn.setHeader(header);
                grn.setDetails(details);
                grn.setHeaderExtendeds(hexList);
                
                String conanicalFilename = MsgType.GRN + DOC_FILENAME_DELIMITOR
                    + header.getBuyerCode() + DOC_FILENAME_DELIMITOR
                    + header.getSupplierCode() + DOC_FILENAME_DELIMITOR
                    + StringUtil.getInstance().convertDocNo(header.getGrnNo())+ ".txt";
                
                byte[] bytes = conanicalDocFileHandler.getFileByte(grn, null, DocFileHandler.CANONICAL);
                
                fileMap.put(conanicalFilename, bytes);
            }
        }
        
        return fileMap;
    }
    
    
    private GrnHeaderHolder initGrnHeader(String[] rowContents, BuyerHolder buyer)
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return null;
        }
        
        GrnHeaderHolder grnHeader = new GrnHeaderHolder();
        grnHeader.setGrnNo(rowContents[0].trim());
        grnHeader.setDocAction(DOC_ACTION_ADD);
        grnHeader.setActionDate(new Date());
        grnHeader.setGrnDate(DateUtil.getInstance().convertStringToDate(rowContents[4].trim(), DateUtil.DEFAULT_DATE_FORMAT));
        grnHeader.setPoNo(rowContents[1].trim());
        grnHeader.setPoDate(new Date());
        grnHeader.setCreateDate(new Date());
        grnHeader.setBuyerCode(buyer.getBuyerCode());
        grnHeader.setBuyerName(buyer.getBuyerName());
        grnHeader.setSupplierCode(rowContents[2].trim());
        grnHeader.setSupplierName(rowContents[3].trim());
        grnHeader.setReceiveStoreCode(rowContents[5].trim());
        
        return grnHeader;
    }
    
    
    private List<GrnHeaderExtendedHolder> initGrnHex(String[] rowContents, BuyerHolder buyer) throws Exception
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return null;
        }
        List<GrnHeaderExtendedHolder> hexList = new ArrayList<GrnHeaderExtendedHolder>();
        GrnHeaderExtendedHolder deptCode = new GrnHeaderExtendedHolder();
        deptCode.setFieldName("dept_code");
        deptCode.setFieldType(EXTENDED_TYPE_STRING);
        deptCode.setStringValue(rowContents[6].trim());
       
        
        GrnHeaderExtendedHolder deptName = new GrnHeaderExtendedHolder();
        deptName.setFieldName("dept_name");
        deptName.setFieldType(EXTENDED_TYPE_STRING);
        deptName.setStringValue(rowContents[7].trim());
      
        deptCode.setAllEmptyStringToNull();
        deptName.setAllEmptyStringToNull();
        hexList.add(deptCode);
        hexList.add(deptName);
        
        return hexList;
    }
    
    
    public List<GrnDetailHolder> initGrnDetails(List<String> detailContents,
        BuyerHolder buyer, GrnHeaderHolder header) 
    {
        if(detailContents == null || detailContents.isEmpty())
        {
            return null;
        }

        List<GrnDetailHolder> grnDetails = new ArrayList<GrnDetailHolder>();
        int lineSeq = 1;
        for(int i = 1; i < detailContents.size(); i++)
        {
            CSVReader reader = null;
            try
            {
                reader = new CSVReader(new StringReader(
                    detailContents.get(i)), ',');
                
                String[] rowContents = reader.readNext();
                
                if (!rowContents[0].trim().equalsIgnoreCase(header.getGrnNo()))
                {
                    continue;
                }
                
                GrnDetailHolder grnDetail = new GrnDetailHolder();
                grnDetail.setLineSeqNo(lineSeq);
                grnDetail.setBuyerItemCode(rowContents[2].trim());
                grnDetail.setSupplierItemCode(rowContents[3].trim());
                grnDetail.setItemDesc(rowContents[4].trim());
                grnDetail.setReceiveQty(BigDecimalUtil.getInstance().convertStringToBigDecimal(rowContents[5].trim(), 4));
                grnDetail.setOrderBaseUnit("U");
                grnDetail.setOrderUom("UOM");
                grnDetail.setPackingFactor(BigDecimal.ZERO);
                grnDetail.setOrderQty(BigDecimal.ZERO);
                grnDetail.setItemCost(BigDecimal.ZERO);
                
                grnDetail.setAllEmptyStringToNull();
                lineSeq ++;
                      
                grnDetails.add(grnDetail);
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            finally
            {
                if (reader != null)
                {
                    try
                    {
                        reader.close();
                    }
                    catch(IOException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
            }
           

        }
        return grnDetails;
    }
    
    
    private void validateHeaderFile(String[] rowContents,Map<String, List<String>> errorMessages, String headerFilename)
    {
        List<String> errors = new ArrayList<String>();
        String error = null;
        boolean validatePoDate = new DateValidator(DateUtil.DEFAULT_DATE_FORMAT).isValid(rowContents[4].trim(), DateUtil.DEFAULT_DATE_FORMAT, false);
       
        if (!validatePoDate)
        {
            error =  "[RECEIVING_DATE]" + " is invalid, the correct format is '"+ DateUtil.DEFAULT_DATE_FORMAT +"'";
            errors.add(error);
        }
        if(!errors.isEmpty())
        {
            errorMessages.put(headerFilename, errors);
        }
    }
    
    
    private void validateDetailFile(String[] rowContents, Map<String, List<String>> errorMessages, String headerFilename)
    {
        if (rowContents == null || rowContents.length == 0)
        {
            return ;
        }
        
        List<String> errors = new ArrayList<String>();
        FileParserUtil fileParseUtil = FileParserUtil.getInstance();
        FieldContentValidator validateReceiveQty = null;
        validateReceiveQty = new NumberValidator(validateReceiveQty);
        fileParseUtil.addError(errors, validateReceiveQty.validate(rowContents[5].trim(), "[QTY]"));

        if (!errors.isEmpty())
        {
            errorMessages.put(headerFilename, errors);
        }
    }
}
