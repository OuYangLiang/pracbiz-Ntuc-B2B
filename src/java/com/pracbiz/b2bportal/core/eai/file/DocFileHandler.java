//*****************************************************************************
//
// File Name       :  FileHandler.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 4:20:26 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.file;

import java.io.File;

import com.pracbiz.b2bportal.core.eai.file.exception.InvalidDocFilenameException;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public interface DocFileHandler<T extends DocMsg, D>
{
    public static final String WATSONS = "watsons";
    public static final String CANONICAL = "canonical";
    public static final String EBXML = "ebxml";
    public static final String FP_IDOC = "FP-Idoc";
    public static final String FAIRPRICE = "fairprice";
    public static final String TANGS = "tangs";
    public static final String CANONICAL_FILE_EXTEND = ".txt";
    public static final String EBXML_FILE_EXTEND = ".xml";
    public static final String CANONICAL_FILE_DATE_FORMAT_DDMMYYYYHHMMSS="dd/MM/yyyy HH:mm:ss";
    public static final String EBXML_FILE_DATE_FORMAT_YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
    //public static final String CANONICAL_FILE_DATE_FORMAT_DDMMYYYY="dd/MM/yyyy";
    
    
    
    public void fillFilenameInfoToDocMsg(DocMsg docMsg)
        throws InvalidDocFilenameException;

    
    public void parseSourceFile(T docMsg) throws Exception;

    
    public void createTargerFile(T docMsg) throws Exception;
    
    
    public void createFile(D data, File targetFile, String expectedFormat)throws Exception;
    
    
    public byte[] getFileByte(D data, File targetFile, String expectedFormat)throws Exception;
    
    
    public String getTargetFilename(D data, String expectedFormat);
}
