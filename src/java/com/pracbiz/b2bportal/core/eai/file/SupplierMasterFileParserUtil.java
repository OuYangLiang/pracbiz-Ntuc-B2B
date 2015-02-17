//*****************************************************************************
//
// File Name       :  SmFileParser.java
// Date Created    :  Jul 12, 2011
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.ValidationConfigHelper;
import com.pracbiz.b2bportal.core.constants.SupplierSourceType;
import com.pracbiz.b2bportal.core.eai.file.validator.util.DateValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.EmptyValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.FieldContentValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.LengthValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.NoSpaceValidator;
import com.pracbiz.b2bportal.core.eai.file.validator.util.RegexValidator;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public final class SupplierMasterFileParserUtil
{   
    public static final String NOT_SPECIFIED = "not specified";
    private static final int SUPPLIER_MASTER_LENGTH = 23;
    private static final String VLD_PTN_KEY_SUPPLIER_CODE = "SUPPLIER_CODE";
    private static SupplierMasterFileParserUtil instance;
   

    private SupplierMasterFileParserUtil()
    {
    }


    public static SupplierMasterFileParserUtil getInstance()
    {
        synchronized (SupplierMasterFileParserUtil.class)
        {
            if (null == instance)
            {
                instance = new SupplierMasterFileParserUtil();
            }

            return instance;
        }
    }


    public List<SupplierHolder> parseFile(File file) throws Exception
    {

        List<List<String>> lines = FileParserUtil.getInstance().readCSVFile(
                file);

        if (lines == null || lines.size() < 1)
        {
            return null;
        }

        List<SupplierHolder> rlt = new ArrayList<SupplierHolder>();

        for (int i = 1; i < lines.size(); i++)
        {
            List<String> line = lines.get(i);
            if (line == null || line.size() < SUPPLIER_MASTER_LENGTH)
            {
                continue;
            }

            SupplierHolder sm = new SupplierHolder();
            sm.setSupplierCode(line.get(0));
            sm.setSupplierName(line.get(1));
            sm.setSupplierAlias(line.get(2));
            sm.setRegNo(line.get(3));
            sm.setGstRegNo(line.get(4));
            sm.setTransMode(line.get(5));
            
//            sm.setGstPercent(BigDecimalUtil.getInstance()
//                    .convertStringToBigDecimal(line.get(5), 6));
            
            if (null == line.get(6) || line.get(6).trim().isEmpty() || line.get(6).trim().toUpperCase(Locale.US).startsWith("L"))
            {
                sm.setSupplierSource(SupplierSourceType.LOCAL);
            }
            else if (line.get(6).trim().toUpperCase(Locale.US).startsWith("O"))
            {
                sm.setSupplierSource(SupplierSourceType.OVERSEA);
            }
            
            sm.setAddress1((line.get(7) == null || line.get(7).trim().isEmpty()) ? NOT_SPECIFIED : line.get(7));
            sm.setAddress2(line.get(8));
            sm.setAddress3(line.get(9));
            sm.setAddress4(line.get(10));
            sm.setState(line.get(12));
            sm.setPostalCode(line.get(13));
            sm.setCtryCode((line.get(14) == null || line.get(14).trim().isEmpty())? "SG" : line.get(14));
            sm.setCurrCode((line.get(15) == null || line.get(15).trim().isEmpty()) ? "SGD" : line.get(15));
            sm.setContactName((line.get(16) == null || line.get(16).trim().isEmpty()) ? NOT_SPECIFIED : line.get(16));
            sm.setContactTel((line.get(17) == null || line.get(17).trim().isEmpty()) ? NOT_SPECIFIED : line.get(17));
            sm.setContactMobile(line.get(18));
            sm.setContactFax(line.get(19));
            sm.setContactEmail((line.get(20) == null || line.get(20).trim().isEmpty()) ? NOT_SPECIFIED : line.get(20));
            
            if (null == line.get(21) || line.get(21).trim().isEmpty()
                || line.get(21).trim().toUpperCase(Locale.US).startsWith("T")
                || line.get(21).trim().toUpperCase(Locale.US).startsWith("Y"))
            {
                sm.setActive(true);
            }
            else
            {
                sm.setActive(false);
            }
            
            if (null == line.get(22) || line.get(22).trim().isEmpty())
            {
                sm.setCreateDate(new Date());
            }
            else
            {
                sm.setCreateDate(DateUtil.getInstance().convertStringToDate(
                    line.get(22), DateUtil.DATE_FORMAT_DDMMYYYY));
            }
            sm.setCreateBy("SYSTEM");
            
            sm.trimAllString();
            sm.setAllEmptyStringToNull();
            rlt.add(sm);
        }

        return rlt;
    }


    public List<String> validateFile(File file, ValidationConfigHelper validationConfig) throws Exception
    {
        List<String> rlt = new ArrayList<String>();

        List<List<String>> contents = FileParserUtil.getInstance().readCSVFile(
                file);

        if (contents == null || contents.isEmpty())
        {
            rlt.add("[ " + file.getName() + " ] is empty.");

            return rlt;
        }

        boolean isEmptyFile = false;

        if (!isEmptyFile)
        {
            isEmptyFile = true;

            for (int i = 1; i < contents.size(); i++)
            {
                List<String> line = contents.get(i);

                if (line == null || line.isEmpty())
                {
                    continue;
                }

                isEmptyFile = false;

                int lineNumber = i + 1;
                isEmptyFile = false;
                
                // check line length
                if (line.size() != SUPPLIER_MASTER_LENGTH)
                {
                    rlt.add("Line [" + lineNumber + "] ------ This line has ["
                            + line.size() + "] columns. "
                            + SUPPLIER_MASTER_LENGTH + " columns expected.");

                    continue;
                }

                // check supplier master file
                // check SupplierCode
                String supplierCode = line.get(0).trim();
                FieldContentValidator validateSupplierCode = null;
                validateSupplierCode = new EmptyValidator();
                validateSupplierCode = new NoSpaceValidator(validateSupplierCode);
                validateSupplierCode = new RegexValidator(validateSupplierCode, validationConfig.getPattern(VLD_PTN_KEY_SUPPLIER_CODE));
                validateSupplierCode = new LengthValidator(20, validateSupplierCode);
                FileParserUtil.getInstance().addError(rlt, validateSupplierCode.validate(supplierCode,  "Line [" + lineNumber + "] ------ [Supplier Code]"));
                
                // check SupplierName
                String supplierName = line.get(1).trim();
                FieldContentValidator validateSupplierName = null;
                validateSupplierName = new EmptyValidator();
                validateSupplierName = new LengthValidator(100, validateSupplierName);
                FileParserUtil.getInstance().addError(rlt, validateSupplierName.validate(supplierName, "Line [" + lineNumber + "] ------ [Supplier Name]"));
                
                // check SupplierAlias
                String supplierAlias = line.get(2).trim();
                FieldContentValidator validateSupplierAlias = null;
                validateSupplierAlias = new LengthValidator(50, validateSupplierAlias);
                FileParserUtil.getInstance().addError(rlt, validateSupplierAlias.validate(supplierAlias, "Line [" + lineNumber + "] ------ [Supplier Alias]"));
                
                // check RegNo
                String regNo = line.get(3).trim();
                FieldContentValidator validateRegNo = null;
                validateRegNo = new LengthValidator(50, validateRegNo);
                FileParserUtil.getInstance().addError(rlt, validateRegNo.validate(regNo, "Line [" + lineNumber + "] ------ [Reg No]"));
                
                // check GstRegNo
                String gstRegNo = line.get(4).trim();
                FieldContentValidator validateGstRegNo = null;
                validateGstRegNo = new LengthValidator(50, validateGstRegNo);
                FileParserUtil.getInstance().addError(rlt, validateGstRegNo.validate(gstRegNo, "Line [" + lineNumber + "] ------ [Gst Reg No]"));
                
                // check TransMode
                String transMode = line.get(5).trim();
                FieldContentValidator validateTransMode = null;
                validateTransMode = new LengthValidator(20, validateTransMode);
                FileParserUtil.getInstance().addError(rlt, validateTransMode.validate(transMode, "Line [" + lineNumber + "] ------ [Trans Mode]"));
                
                // check Address1
                String address1 = line.get(7).trim();
                FieldContentValidator validateAddress1 = null;
//                validateAddress1 = new EmptyValidator();
                validateAddress1 = new LengthValidator(100, validateAddress1);
                FileParserUtil.getInstance().addError(rlt, validateAddress1.validate(address1, "Line [" + lineNumber + "] ------ [Address1]"));
                
                // check Address2
                String address2 = line.get(8).trim();
                FieldContentValidator validateAddress2 = null;
                validateAddress2 = new LengthValidator(100, validateAddress2);
                FileParserUtil.getInstance().addError(rlt, validateAddress2.validate(address2, "Line [" + lineNumber + "] ------ [Address2]"));
                
                // check Address3
                String address3 = line.get(9).trim();
                FieldContentValidator validateAddress3 = null;
                validateAddress3 = new LengthValidator(100, validateAddress3);
                FileParserUtil.getInstance().addError(rlt, validateAddress3.validate(address3, "Line [" + lineNumber + "] ------ [Address3]"));
                
                // check Address4
                String address4 = line.get(10).trim();
                FieldContentValidator validateAddress4 = null;
                validateAddress4 = new LengthValidator(100, validateAddress4);
                FileParserUtil.getInstance().addError(rlt, validateAddress4.validate(address4, "Line [" + lineNumber + "] ------ [Address4]"));
                
                // check State
                String state = line.get(12).trim();
                FieldContentValidator validateState = null;
                validateState = new LengthValidator(50, validateState);
                FileParserUtil.getInstance().addError(rlt, validateState.validate(state, "Line [" + lineNumber + "] ------ [State]"));
                
                // check PostalCode
                String postalCode = line.get(13).trim();
                FieldContentValidator validatePostalCode = null;
                validatePostalCode = new RegexValidator(validatePostalCode, "^[0-9]*$");
                validatePostalCode = new LengthValidator(15, validatePostalCode);
                FileParserUtil.getInstance().addError(rlt, validatePostalCode.validate(postalCode, "Line [" + lineNumber + "] ------ [Postal Code]"));
                
                // check CtryCode
                String ctryCode = line.get(14).trim();
                FieldContentValidator validateCtryCode = null;
                validateCtryCode = new LengthValidator(2, validateCtryCode);
                FileParserUtil.getInstance().addError(rlt, validateCtryCode.validate(ctryCode, "Line [" + lineNumber + "] ------ [Ctry Code]"));
                
                // check CurrCode
                String currCode = line.get(15).trim();
                FieldContentValidator validateCurrCode = null;
                validateCurrCode = new LengthValidator(3, validateCurrCode);
                FileParserUtil.getInstance().addError(rlt, validateCurrCode.validate(currCode, "Line [" + lineNumber + "] ------ [Curr Code]"));
                
                // check ContactName
                String contactName = line.get(16).trim();
                FieldContentValidator validateContactName = null;
                validateContactName = new LengthValidator(50, validateContactName);
                FileParserUtil.getInstance().addError(rlt, validateContactName.validate(contactName, "Line [" + lineNumber + "] ------ [Contact Name]"));
                
                // check ContactTel
                String contactTel = line.get(17).trim();
                FieldContentValidator validateContactTel = null;
//                validateContactTel = new RegexValidator(validateContactTel, "^[(]?(\\d+)([/\\s-()]?(\\d+)[)]?)*$");
                validateContactTel = new LengthValidator(30, validateContactTel);
                FileParserUtil.getInstance().addError(rlt, validateContactTel.validate(contactTel, "Line [" + lineNumber + "] ------ [Contact Tel]"));
                
                // check ContactMobile
                String contactMobile = line.get(18).trim();
                FieldContentValidator validateContactMobile = null;
//                validateContactMobile = new RegexValidator(validateContactMobile, "^[\\+]?(\\d+)$");
                validateContactMobile = new LengthValidator(30, validateContactMobile);
                FileParserUtil.getInstance().addError(rlt, validateContactMobile.validate(contactMobile, "Line [" + lineNumber + "] ------ [Contact Mobile]"));
                
                // check ContactFax
                String contactFax = line.get(19).trim();
                FieldContentValidator validateContactFax = null;
//                validateContactFax = new RegexValidator(validateContactFax, "^[(]?(\\d+)([/\\s-()]?(\\d+)[)]?)*$");
                validateContactFax = new LengthValidator(30, validateContactFax);
                FileParserUtil.getInstance().addError(rlt, validateContactFax.validate(contactFax, "Line [" + lineNumber + "] ------ [Contact Fax]"));
                
                // check ContactEmail
                String contactEmail = line.get(20).trim();
                FieldContentValidator validateContactEmail = null;
                validateContactEmail = new RegexValidator(validateContactEmail, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
                validateContactEmail = new LengthValidator(100, validateContactEmail);
                FileParserUtil.getInstance().addError(rlt, validateContactEmail.validate(contactEmail, "Line [" + lineNumber + "] ------ [Contact Email]"));
                
                // check CreateDate
                if (null != line.get(22).trim() && !(line.get(22).trim().isEmpty()))
                {
                    String createDate = line.get(22).trim();
                    FieldContentValidator validateCreateDate = null;
                    validateCreateDate = new DateValidator(DateUtil.DATE_FORMAT_DDMMYYYY, validateCreateDate);
                    FileParserUtil.getInstance().addError(rlt, validateCreateDate.validate(createDate, "Line [" + lineNumber + "] ------ [Create Date]"));
                }
                
            }
        }
        
        if (isEmptyFile)
        {
            rlt.add("[ " + file.getName() + " ] is empty.");
            return rlt;
        }

        if (rlt.isEmpty())
        {
            return null;
        }

        return rlt;
    }

}
