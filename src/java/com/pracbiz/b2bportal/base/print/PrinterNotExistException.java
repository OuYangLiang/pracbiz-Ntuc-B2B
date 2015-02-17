package com.pracbiz.b2bportal.base.print;

public class PrinterNotExistException extends Exception
{
    private static final long serialVersionUID = -5247191045930977409L;

    public PrinterNotExistException()
    {
        super();
    }
    
    public PrinterNotExistException(String message)
    {
        super(message);
    }
    
    public PrinterNotExistException(Throwable cause)
    {
        super(cause);
    }
    
    public PrinterNotExistException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
