package com.pracbiz.b2bportal.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;

import com.pracbiz.b2bportal.base.action.BaseAction;

public final class ErrorHelper
{
    private static ErrorHelper instance;
    
    private ErrorHelper(){}
    
    public static ErrorHelper getInstance()
    {
        synchronized(ErrorHelper.class)
        {
            if (instance == null)
            {
                instance = new ErrorHelper();
            }
        }
        
        return instance;
    }
    
    public void logError(Logger log, Exception ex)
    {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        
        log.error(sw.toString());
    }
    
    
    public void logError(Logger log, BaseAction action, Exception ex)
    {
        StringBuffer strBuffer = new StringBuffer(200);
        
        strBuffer.append("Exception occured. [KEY-"
            + action.toString() + "].");
        
        log.error(strBuffer.toString(), ex);
    }

    
    public String logTicketNo(Logger log, Exception ex)
    {
        StringBuffer strBuffer = new StringBuffer(200);
        
        String ticketNo = String.valueOf(System.currentTimeMillis());
        
        strBuffer.append("Exception occured. [TICKET-" + ticketNo + "],");
        
        log.error(strBuffer.toString(), ex);
        
        return ticketNo;
    }
    
    
    public String logTicketNo(Logger log, BaseAction action, Exception ex)
    {
        StringBuffer strBuffer = new StringBuffer(200);
        
        String ticketNo = String.valueOf(System.currentTimeMillis());
        
        strBuffer.append("Exception occured. [TICKET-" + ticketNo + "], [KEY-"
            + action.toString() + "].");
        
        log.error(strBuffer.toString(), ex);
        
        return ticketNo;
    }
    
    
    public String getStackTrace(Exception ex)
    {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
    
}
