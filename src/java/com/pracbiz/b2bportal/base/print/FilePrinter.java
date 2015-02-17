package com.pracbiz.b2bportal.base.print;

public interface FilePrinter
{
    public void print(String printerName_, String fileName_) throws PrinterNotExistException, Exception;
}
