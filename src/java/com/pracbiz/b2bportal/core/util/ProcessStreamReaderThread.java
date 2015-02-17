//*****************************************************************************
//
// File Name       :  ProcessStreamReaderThread.java
// Date Created    :  2011-12-15
// Last Changed By :  $Author: xuchengqing $
// Last Changed On :  $Date: 2012-01-19 16:27:23 +0800 ( 2012) $
// Revision        :  $Rev: 954 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.ErrorHelper;


class ProcessStreamReaderThread extends Thread
{
    private static final Logger log = LoggerFactory
        .getLogger(ProcessStreamReaderThread.class);
    
    StringBuilder streamBiulder;
    InputStreamReader in;
    String inputStr;

    ProcessStreamReaderThread(InputStream in) throws UnsupportedEncodingException
    {
        super();

        this.in = new InputStreamReader(in, CommonConstants.ENCODING_UTF8);
        this.streamBiulder = new StringBuilder();
        
        this.start();
    }

    public void run()
    {
        BufferedReader bufferReader = null;
        try
        {
            bufferReader = new BufferedReader(in);
            String line = null;
            while((line = bufferReader.readLine()) != null)
            {
                streamBiulder.append(line);
                streamBiulder.append(System.getProperty("line.separator"));
            }
            inputStr = streamBiulder.toString();
        }
        catch(IOException e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        finally
        {
            try
            {
                if(bufferReader != null)
                {
                    bufferReader.close();
                    bufferReader = null;
                }
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            try
            {
                if(in != null)
                {
                    in.close();
                    in = null;
                }
            }
            catch(IOException e)
            {
                ErrorHelper.getInstance().logError(log, e);
            }
            streamBiulder.setLength(0);
            streamBiulder = null;
        }
    }

    String getString()
    {
        return inputStr;
    }
}
