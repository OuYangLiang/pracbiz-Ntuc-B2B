//*****************************************************************************
//
// File Name       :  DefaultFileHandler.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 4:42:57 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.EbxmlParseHelper;
import com.pracbiz.b2bportal.base.util.FileParserUtil;
import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.eai.file.exception.InvalidDocFilenameException;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;
import com.pracbiz.b2bportal.core.eai.message.constants.Direction;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.outbound.GiDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.util.StringUtil;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public abstract class DefaultDocFileHandler<T extends DocMsg, D> implements DocFileHandler<T, D>
{
    protected DocFileHandler<T, D> successor;
    @Autowired
    private ItemService itemService;
    
    
    @Override
    public void parseSourceFile(T docMsg) throws Exception
    {
        if (!processFormat().equalsIgnoreCase(docMsg.getOutputFormat()))
        {
            if (successor == null)
            {
                throw new Exception("Do not support creating file with format [" + docMsg.getOutputFormat() + "].");
            }
            this.successor.parseSourceFile(docMsg);
            return;
        }
        
        File originalFile = new File(docMsg.getContext().getWorkDir(),
            docMsg.getOriginalFilename());
        
        this.readFileContent(docMsg, originalFile);
    }
    
    
    @Override
    public void createTargerFile(T docMsg) throws Exception
    {
        if (!processFormat().equalsIgnoreCase(docMsg.getInputFormat()))
        {
            if (successor == null)
            {
                throw new Exception("Do not support creating file with format [" + docMsg.getOutputFormat() + "].");
            }
            this.successor.createTargerFile(docMsg);
            return;
        }
        
        String targetFile = this.translate(docMsg);
        
        docMsg.setTargetFilename(targetFile);
    }

    
    @Override
    public void fillFilenameInfoToDocMsg(DocMsg docMsg)
        throws InvalidDocFilenameException
    {
        try
        {
            String originalFilename = FileUtil.getInstance().trimAllExtension(
                    docMsg.getOriginalFilename());
            String[] parts = originalFilename.split("_");
            
            if (docMsg.getMsgDirection().equals(Direction.outbound))
            {
                docMsg.setReceiverCode(parts[2]);
                docMsg.setRefNo(StringUtil.getInstance().convertToDocNo(parts[3]));
            }
            else
            {
                docMsg.setSenderCode(parts[2]);
                docMsg.setReceiverCode(parts[1]);
                docMsg.setRefNo(parts[3]);
            }
            
            if(parts.length == 5)
            {
                docMsg.setRemoteOid(new BigDecimal(parts[4]));
            }
        }
        catch(Exception e)
        {
            throw new InvalidDocFilenameException(docMsg.getOriginalFilename(),
                e);
        }
    }
    
    
    public static MsgType parseTypeByFilename(String filename)
        throws InvalidDocFilenameException
    {
        int index = filename.indexOf('_');
        if(index == -1)
            index = filename.length();
        MsgType msgType = null;
        try
        {
            msgType = MsgType.valueOf(filename.substring(0, index).toUpperCase(Locale.US));
        }
        catch(Exception e)
        {
            throw new InvalidDocFilenameException(filename, e);
        }

        return msgType;
    }
    
    
    protected void initBuyerInfo(BuyerHolder buyer, BaseHolder target) throws Exception
    {
        
        target.setAllEmptyStringToNull();
        String[] buyerFieldNames = {"buyerOid","buyerName","address1","address2","address3","address4","state","postalCode","ctryCode"};
        
        Map<?, ?> map = BeanUtils.describe(target);
        for (String fieldName : buyerFieldNames)
        {
            boolean isExist = map.containsKey(fieldName);
            
            if (isExist)
            {
                this.initData(buyer, target, fieldName, fieldName);
                continue;
            }
            String sourceFieldName = fieldName;
            fieldName = "buyer" + StringUtils.capitalize(fieldName.replace("address", "addr"));
            
            if (!map.containsKey(fieldName)) continue;
            this.initData(buyer, target, sourceFieldName, fieldName);
        }
        
    }
    
    
    protected void initSupplierInfo(SupplierHolder supplier, BaseHolder target) throws Exception
    {
        target.setAllEmptyStringToNull();
        String[] suppFieldNames = {"supplierOid","supplierName","address1","address2","address3","address4","state","postalCode","ctryCode"};
        
        Map<?, ?> map = BeanUtils.describe(target);
        for (String fieldName : suppFieldNames)
        {
            boolean isExist = map.containsKey(fieldName);
            
            if (isExist)
            {
                this.initData(supplier, target, fieldName, fieldName);
                continue;
            }
            String sourceFieldName = fieldName;
            fieldName = "supplier" + StringUtils.capitalize(fieldName.replace("address", "addr"));
            
            if (!map.containsKey(fieldName)) continue;
            this.initData(supplier, target, sourceFieldName, fieldName);
        }
    }
    
    
    private void initData(Object source, Object target,String sourceFieldName, String fieldName) throws Exception
    {
        Object oldValue = BeanUtils.getProperty(target, fieldName);
        if (oldValue != null) return; 
        
        Object value = BeanUtils.getProperty(source, sourceFieldName);
        BeanUtils.setProperty(target, fieldName, value);
    }
    
    
    public void readFileContent(T docMsg, File sourceFile) throws Exception
    {
        readFileContent(docMsg, FileUtil.getInstance().readByteFromDisk(sourceFile.getPath()));
        
        if (docMsg instanceof GrnDocMsg || docMsg instanceof GiDocMsg)
        {
            int count = itemService.selectCountOfItemByBuyer(docMsg.getBuyer().getBuyerOid());
            if (count == 0)
            {
                return;
            }
            
            Object holder = docMsg.getClass().getMethod("getData").invoke(docMsg, new Object[]{});
            List<Object> details = (List<Object>) holder.getClass().getMethod("getDetails").invoke(holder, new Object[]{});
            if (details == null || details.isEmpty())
            {
                return;
            }
            
            for (Object detail : details)
            {
                String buyerItemCode = (String) detail.getClass().getMethod("getBuyerItemCode").invoke(detail, new Object[]{});
                String itemDesc = (String) detail.getClass().getMethod("getItemDesc").invoke(detail, new Object[]{});
                if (itemDesc == null || itemDesc.trim().isEmpty())
                {
                    ItemHolder item = itemService.selectItemByBuyerOidAndBuyerItemCode(docMsg.getBuyer().getBuyerOid(), buyerItemCode);
                    if (item == null || item.getItemDesc() == null || item.getItemDesc().trim().isEmpty())
                    {
                        continue;
                    }
                    
                    detail.getClass().getMethod("setItemDesc", String.class).invoke(detail, new Object[]{item.getItemDesc().trim()});
                }
            }
        }
    }
    
    
    @Override
    public void createFile(D data, File targetFile, String expectedFormat)throws Exception
    {
        if(expectedFormat != null
            && !processFormat().equalsIgnoreCase(expectedFormat))
        {
            this.successor.createFile(data, targetFile, expectedFormat);
            return;
        }
        byte[] sourceByte = this.getFileByte(data, targetFile, expectedFormat);
        
        //write the content to target file.
        FileParserUtil.getInstance().writeContent(targetFile, new String(sourceByte, Charset.defaultCharset()));
        
        if (processFormat().equalsIgnoreCase(EBXML) || processFormat().equalsIgnoreCase(FP_IDOC))
        {
            EbxmlParseHelper.getInstance().validateEbxml(targetFile, getXSDSchema());
        }
    }
    
    
    protected String getXSDSchema()
    {
        return null;
    }
    
    
    //*****************************************************
    // abstract methods
    //*****************************************************
    
    
    protected abstract String processFormat();
    public abstract void readFileContent(T docMsg, byte[] input) throws Exception;
    protected abstract String translate(T docMsg) throws Exception;
    
    
    //*****************************************************
    // setter and getter methods
    //*****************************************************
    
    public void setSuccessor(DocFileHandler<T, D> successor)
    {
        this.successor = successor;
    }
}
