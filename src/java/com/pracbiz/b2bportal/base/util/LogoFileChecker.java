//*****************************************************************************
//
// File Name       :  LogoFileChecker.java
// Date Created    :  Sep 6, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Sep 6, 2013 3:01:17 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public final class LogoFileChecker
{
    public final static int FILETYPE_JPG = 0;
    public final static int FILETYPE_GIF = 1;
    public final static int FILETYPE_PNG = 2;
    public final static int FILETYPE_BMP = 3;
    
    public boolean isFileAValidLogo(File file, int fileType) throws IOException
    {
        int n = 4;
        String magicCode = null;
        
        if (fileType == FILETYPE_JPG)
        {
            magicCode = "ffd8ffe0";
        }
        else if (fileType == FILETYPE_GIF)
        {
            magicCode = "47494638";
        }
        else if (fileType == FILETYPE_PNG)
        {
            magicCode = "89504e47";
        }
        else // FILETYPE_BMP
        {
            n = 2;
            magicCode = "424d";
        }
        
        if (!this.bytesToHexString(this.readByteFromFile(file, n)).equalsIgnoreCase(magicCode))
            return false;
        
        return true;
    }
    
    
    private String bytesToHexString(byte[] src)
    {
        StringBuilder stringBuilder = new StringBuilder();
        if(src == null || src.length <= 0)
        {
            return null;
        }
        for(int i = 0; i < src.length; i++)
        {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if(hv.length() < 2)
            {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    
    private byte[] readByteFromFile(File file, int n) throws IOException
    {
        InputStream is = null;
        
        try
        {
            is = new FileInputStream(file);  
            byte[] bt = new byte[n];
            int numOfReadBytes = is.read(bt);
            
            if (n != numOfReadBytes)
                throw new IOException("Number of read byte is incorrect.");
            
            return bt;
        }
        finally
        {
            if (null != is)
                is.close();
        }
        
    }
    
    
    //*****************************************************
    // singleton class
    //*****************************************************
    
    private static LogoFileChecker instance;
    private LogoFileChecker(){}
    
    public static LogoFileChecker getInstance()
    {
        synchronized(LogoFileChecker.class)
        {
            if (instance == null)
            {
                instance = new LogoFileChecker();
            }
        }
        
        return instance;
    }
    
    
    /*public static void main(String[] args) throws IOException
    {
        File file = new File("/Users/ouyang/Desktop/2.gif");
        
        System.out.println(LogoFileChecker.getInstance().isFileAValidLogo(file, LogoFileChecker.FILETYPE_GIF));
    }*/
}
