package com.pracbiz.client.startup;

import org.boris.winrun4j.Service;
import org.boris.winrun4j.ServiceException;

import com.pracbiz.client.core.ClientEngine;


public class WindowsService implements Service
{
    private ClientEngine engine;
    
    public int serviceRequest(int request) throws ServiceException
    {
        if (SERVICE_CONTROL_STOP == request || SERVICE_CONTROL_SHUTDOWN == request)
        {
            try
            {
                if (null == engine)
                {
                    System.out.println("B2B-Client Service is not started.");
                    return 0;
                }
                
                engine.stop();
                
                System.out.println("B2B-Client Service has been stopped successfully.");
            }
            catch(Exception ex)
            {
                System.err.println("B2B-Client Service stopped failed.");
                System.err.println(ex);
            }
            finally
            {
                
            }
        }
        
        return 0;
    }

    public int serviceMain(String[] args) throws ServiceException
    {
        try
        {
            engine = new ClientEngine(false);
            engine.start();
            
            System.out.println("B2B-Client Service has been started successfully.");

        }
        catch(Exception ex)
        {
            System.err.println("B2B-Client Service started failed.");
            System.err.println(ex);
        }

        return 0;
    }

}
