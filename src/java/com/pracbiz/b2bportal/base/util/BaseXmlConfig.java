package com.pracbiz.b2bportal.base.util;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.commons.configuration.ConversionException;
import org.apache.commons.configuration.XMLConfiguration;

public class BaseXmlConfig
{
    protected static final String ERR_NOELEMENT = "###NO SUCH ELEMENT###";
    protected static final String ERR_CONVERT = "###CONVERSION ERROR###";
    protected static final String ERR_GENERAL = "###ERROR###";
    
    protected static final String BLANKS = "                                        "; //40 spaces
    protected static final String NL = System.getProperty("line.separator");
    protected static final List<String> EMPTY_LIST = new ArrayList<String>();
    
    protected static final Map<String, Object> configReg = new HashMap<String, Object>();
    protected Map<String, Object> configMap;
    protected Configuration config;
    protected XMLConfiguration xmlConfig;
    
    
    //*****************************************************
    // constructor
    //*****************************************************
    
    protected BaseXmlConfig(File cfgFile) throws ConfigurationException,
        MalformedURLException
    {
        ConfigurationFactory factory = new ConfigurationFactory();
        factory.setConfigurationURL(cfgFile.toURI().toURL());
        config = factory.getConfiguration();
        configMap = new HashMap<String, Object>();

        String fileName = cfgFile.getName();
        if (fileName.toUpperCase(Locale.ENGLISH).endsWith(".XML"))
        {
            if(xmlConfig == null)
            {
                xmlConfig = new XMLConfiguration();
            }
            
            xmlConfig.setFile(cfgFile);
            xmlConfig.load();
        }
    }
    
    
    public static BaseXmlConfig getInstance(File cfgFile)
        throws ConfigurationException, MalformedURLException
    {
        BaseXmlConfig mySiteConfig;
        String fileName = cfgFile.getAbsolutePath();

        if(configReg.containsKey(fileName))
        {
            mySiteConfig = (BaseXmlConfig)configReg.get(fileName);
        }
        else
        {
            synchronized(BaseXmlConfig.class)
            {
                if(configReg.containsKey(fileName))
                {
                    mySiteConfig = (BaseXmlConfig)configReg.get(fileName);
                }
                else
                {
                    mySiteConfig = new BaseXmlConfig(cfgFile);
                    configReg.put(fileName, mySiteConfig);
                }

            }
        }
        
        mySiteConfig.initConfigMap();

        return mySiteConfig;
    }
    
    
    protected void initConfigMap()
    {
        // Please add code here.
    }
    
    
    protected final String stringValue(String key)
    {
        try
        {
            return config.getString(key);
        }
        catch (NoSuchElementException ex)
        {
            return ERR_NOELEMENT;
        }
        catch (ConversionException ex)
        {
            return ERR_CONVERT;
        }
        catch (Exception ex)
        {
            return ERR_GENERAL;
        }
    }

    
    @SuppressWarnings("unchecked")
    protected final List<String> listValue(String key)
    {
        try
        {
            return config.getList(key);
        }
        catch (Exception ex)
        {
            return EMPTY_LIST;
        }
    }

}
