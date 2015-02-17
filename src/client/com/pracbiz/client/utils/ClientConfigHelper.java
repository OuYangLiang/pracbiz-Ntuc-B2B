//*****************************************************************************
//
// File Name       :  ClientConfigHelper.java
// Date Created    :  Feb 27, 2013
// Last Changed By :  $Author: jiangming $
// Last Changed On :  $Date: Feb 27, 2013 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.client.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.core.io.Resource;

import com.pracbiz.b2bportal.base.util.BaseXmlConfig;

/**
 * TODO To provide an overview of this class.
 * 
 * @author jiangming
 */
public class ClientConfigHelper extends BaseXmlConfig
{
    public static final String VALUE_TOKEN_FILE_EXTENSION = ".tok";
    public static final String VALUE_PROC_FILE_EXTENSION = ".proc";
    public static final String VALUE_ERROR_FILE_EXTENSION = ".err";
    public static final String VALUE_ZIP_FILE_EXTENSION = ".zip";
    public static final String VALUE_PGP_FILE_EXTENSION=".pgp";
    public static final String VALUE_PDF_FILE_EXTENSION=".pdf";
    public static final String VALUE_XML_FILE_EXTENSION=".xml";
    public static final String VALUE_ACK_FILE_EXTENSION=".ack";
    public static final String VALUE_PRINT_JOB_FILE_EXTENSION=".printJob";
    
    private static final String PORTAL = "portal(";
    public static final String B2B_PORTAL_HOST="webServiceSetting.remoteServer";
    public static final String OUT_BOUND_DOC_FILE_PATTERN="outboundMsgSetting.pattern";

    // proxy setting
    public static final String PROXY_SETTING = "proxySetting";
    public static final String PROXY_ATTR_USEPROXY = "useProxy";
    public static final String PROXY_ATTR_USENTLM = "useNTLM";
    public static final String PROXY_ATTR_PROXY_CONFIG = "proxyConfig";
    public static final String PROXY_PROP_HOST = "proxySetting.manual.host";
    public static final String PROXY_PROP_PORT = "proxySetting.manual.port";
    public static final String PROXY_PROP_USERNAME = "proxySetting.manual.username";
    public static final String PROXY_PROP_PASSWORD = "proxySetting.manual.password";
    public static final String PROXY_PROP_NTLMDOMAIN = "proxySetting.manual.proxyNtlmDomain";
    public static final String PROXY_AUTO_PROP_USERNAME = "proxySetting.auto.username";
    public static final String PROXY_AUTO_PROP_PASSWORD = "proxySetting.auto.password";
    public static final String PROXY_YES = "Y";
    public static final String PROXY_NO = "N";
    public static final String PROXY_USENTLM_YES = "Y";
    public static final String PROXY_USENTLM_NO = "N";
    public static final String PROXY_CONFIG_AUTO = "AUTO";
    public static final String PROXY_CONFIG_MANUAL = "MANUAL";

    // rest web service uri setting
    public static final String WEB_SERVICE_SETTING = "webServiceSetting";
    public static final String CHANNEL_PROP_TIMEOUT = "webServiceSetting.timeout";
    public static final String CHANNEL_PROP_RETRY_TIMES = "webServiceSetting.retryTimes";
    public static final String CHANNEL_PROP_RETRY_INTERVAL = "webServiceSetting.retryInterval";
    public static final String CHANNEL_PROP_CLIENT_MBOXID = "webServiceSetting.clientMailboxID";
    
    // client's print setting
    public static final String PRINT_SETTING="printSetting";
    public static final String PRINT_AUTO="autoPrint";
    public static final String PRINT_AUTO_CONFIG_YES="Y";
    public static final String PRINTER="printSetting.printer";
    public static final String PRINTER_SUPPLIER="supplierCode";
    
    
    //*****************************************************
    // init methods
    //*****************************************************
    
    protected ClientConfigHelper(File cfgFile) throws ConfigurationException,
        MalformedURLException
    {
        super(cfgFile);
    }

    public static ClientConfigHelper getBeanInstance(Resource configPath)
        throws ConfigurationException, MalformedURLException, IOException
    {
        ClientConfigHelper theSiteConfig = new ClientConfigHelper(
            configPath.getFile());

        theSiteConfig.initConfigMap();
        return theSiteConfig;
    }
    
    
    //*****************************************************
    // public methods
    //*****************************************************
    
    
    public List<String> getAllServerId()
    {
        return this.listValue("portal[@identifier]");
    }
    
    
    public String getLocalMailboxRoot()
    {
        return stringValue("local-mailbox-root");
    }
    
    
    public Map<String, String> getProxyProp()
    {
        Map<String, String> prop = new HashMap<String, String>();

        prop.put(PROXY_ATTR_USEPROXY, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_USEPROXY + "]"));
        prop.put(PROXY_ATTR_USENTLM, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_USENTLM + "]"));
        prop.put(PROXY_ATTR_PROXY_CONFIG, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_PROXY_CONFIG + "]"));

        prop.put(PROXY_PROP_HOST, stringValue(PROXY_PROP_HOST));
        prop.put(PROXY_PROP_PORT, stringValue(PROXY_PROP_PORT));
        prop.put(PROXY_PROP_USERNAME, stringValue(PROXY_PROP_USERNAME));
        prop.put(PROXY_PROP_PASSWORD, stringValue(PROXY_PROP_PASSWORD));
        prop.put(PROXY_PROP_NTLMDOMAIN, stringValue(PROXY_PROP_NTLMDOMAIN));

        prop.put(PROXY_AUTO_PROP_USERNAME,
            stringValue(PROXY_AUTO_PROP_USERNAME));
        prop.put(PROXY_AUTO_PROP_PASSWORD,
            stringValue(PROXY_AUTO_PROP_PASSWORD));

        return prop;
    }
    
    
    public boolean useProxy()
    {
        Map<String, String> proxyProp = getProxyProp();

        if((proxyProp == null)
            || (!(proxyProp.containsKey(PROXY_ATTR_USEPROXY))))
        {
            return false;
        }
        String useProxy = (String)proxyProp.get(PROXY_ATTR_USEPROXY);
        return (PROXY_YES.equalsIgnoreCase(useProxy));
    }

    
    public boolean useNTLM()
    {
        Map<String, String> proxyProp = getProxyProp();

        if((proxyProp == null)
            || (!(proxyProp.containsKey(PROXY_ATTR_USENTLM))))
        {
            return false;
        }
        String useNTLM = (String)proxyProp.get(PROXY_ATTR_USENTLM);
        return (PROXY_USENTLM_YES.equalsIgnoreCase(useNTLM));
    }

    
    public boolean autoProxy()
    {
        Map<String, String> proxyProp = getProxyProp();

        if((proxyProp == null)
            || (!(proxyProp.containsKey(PROXY_ATTR_PROXY_CONFIG))))
        {
            return false;
        }
        String autoProxy = (String)proxyProp.get(PROXY_ATTR_PROXY_CONFIG);
        return (PROXY_CONFIG_AUTO.equalsIgnoreCase(autoProxy));
    }
    
    
    public int getChannelTimeout(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return Integer.parseInt(stringValue(PORTAL + i + ")." + CHANNEL_PROP_TIMEOUT));
            }
        }
        
        return 5000;
    }

    
    public int getRetryInterval(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return Integer.parseInt(stringValue(PORTAL + i + ")." + CHANNEL_PROP_RETRY_INTERVAL));
            }
        }
        
        return 5000;
    }
    
    
    public int getRetryTimes(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return Integer.parseInt(stringValue(PORTAL + i + ")." + CHANNEL_PROP_RETRY_TIMES));
            }
        }
        
        return 5000;
    }
    
    
    public String getB2bHost(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return stringValue(PORTAL + i + ")." + B2B_PORTAL_HOST);
            }
        }
        
        return null;
    }
    
    
    /*public Map<String, String> getPrintProp()
    {
        Map<String, String> prop = new HashMap<String, String>();

        prop.put(PROXY_ATTR_USEPROXY, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_USEPROXY + "]"));
        prop.put(PROXY_ATTR_USENTLM, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_USENTLM + "]"));
        prop.put(PROXY_ATTR_PROXY_CONFIG, stringValue(PROXY_SETTING + "[@"
            + PROXY_ATTR_PROXY_CONFIG + "]"));

        prop.put(PROXY_PROP_HOST, stringValue(PROXY_PROP_HOST));
        prop.put(PROXY_PROP_PORT, stringValue(PROXY_PROP_PORT));
        prop.put(PROXY_PROP_USERNAME, stringValue(PROXY_PROP_USERNAME));
        prop.put(PROXY_PROP_PASSWORD, stringValue(PROXY_PROP_PASSWORD));
        prop.put(PROXY_PROP_NTLMDOMAIN, stringValue(PROXY_PROP_NTLMDOMAIN));

        prop.put(PROXY_AUTO_PROP_USERNAME,
            stringValue(PROXY_AUTO_PROP_USERNAME));
        prop.put(PROXY_AUTO_PROP_PASSWORD,
            stringValue(PROXY_AUTO_PROP_PASSWORD));

        return prop;
    }*/
    
    
    public List<String> getOutboundMsgPatterns(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return listValue(PORTAL + i + ")." + OUT_BOUND_DOC_FILE_PATTERN);
            }
        }
        
        return null;
    }
    

    public int getMaxZipFile(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return Integer.parseInt(stringValue(PORTAL + i + ").dispatchSetting.maxFilesPerZip"));
            }
        }
        
        return 5;
        
    }
    
    
    public List<String> getSupplierCodes(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return listValue(PORTAL + i + ")." + PRINTER + "[@"+PRINTER_SUPPLIER+"]");
            }
        }
        
        return null;
    }

    
    public String getPrinterBySupplierCode(String serverId, String supplierCode)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                List<String> supCodes =  listValue(PORTAL + i + ")." + PRINTER + "[@"+PRINTER_SUPPLIER+"]");
                
                
                for (int j = 0; j < supCodes.size(); j ++)
                {
                    String supCode = supCodes.get(j);
                    
                    if (supCode.equals(supplierCode))
                    {
                        return stringValue(PORTAL + i + ")." + PRINTER + "(" + j + ")");
                    }
                }
            }
        }
        
        return null;
    }
    
    
    public boolean autoPrint(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                String autoPrint = stringValue(PORTAL + i + ")." + PRINT_SETTING + "[@" + PRINT_AUTO+"]");
                if (PRINT_AUTO_CONFIG_YES.equalsIgnoreCase(autoPrint))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    public List<String> getMboxIds(String serverId)
    {
        List<String> ids = this.getAllServerId();
        
        for (int i = 0; i < ids.size(); i ++)
        {
            String id = ids.get(i);
            
            if (id.equals(serverId))
            {
                return listValue(PORTAL + i + ").support-portal-mailboxes.list.value");
            }
        }
        
        return null;
    }
    
    
    public Map<String, String> getMboxIdViaBuyerCodeAndBuyerGivenSupplierCode(String buyerCode, String buyerGivenSupplierCode)
    {
        List<String> buyerGivenSupCodes = this.listValue("dispersion.setting[@supplierCode]");
        
        for (int i = 0; i < buyerGivenSupCodes.size(); i++)
        {
            String supCode = buyerGivenSupCodes.get(i);
            
            if (supCode.equalsIgnoreCase(buyerGivenSupplierCode))
            {
                String bCode = this.stringValue("dispersion.setting(" + i + ")[@buyerCode]");
                
                if (bCode.equalsIgnoreCase(buyerCode))
                {
                    Map<String, String> rlt = new HashMap<String, String>();
                    rlt.put("portailId", this.stringValue("dispersion.setting(" + i + ")[@portalId]"));
                    rlt.put("mailbox", this.stringValue("dispersion.setting(" + i + ")[@mailbox]"));
                    
                    return rlt;
                }
            }
        }
        
        return null;
    }
    
    
    public boolean autoDispersion()
    {
        String str = this.stringValue("dispersion[@auto-dispersion]");
        
        if (null == str)
            str = "N";
        
        if ("Y".equalsIgnoreCase(str.trim()))
            return true;
        
        return false;
    }
    
    
    public boolean isDispatchPdf()
    {
        String str = this.stringValue("dispersion[@dispatchPdf]");
        
        if (null == str)
            str = "N";
        
        if ("Y".equalsIgnoreCase(str.trim()))
            return true;
        
        return false;
    }
    
    
    public String getDispersionPrivateIn()
    {
        return stringValue("dispersion.privateIn");
    }
    
    
    public String getDispersionPrivateOut()
    {
        return stringValue("dispersion.privateOut");
    }
    
    
    public String getDispersionDoc()
    {
        return stringValue("dispersion.doc");
    }
    
    
   /*public static void main(String[] args) throws ConfigurationException,
        MalformedURLException
    {
        File src = new File(
            "/Users/ouyang/Documents/workspace/b2bportal/b2bportal/trunk/config/client/application/config_index.xml");
        
        ClientConfigHelper cfg = new ClientConfigHelper(src);
        
        System.out.println(cfg.autoDispersion());
        System.out.println(cfg.getMboxIdViaBuyerCodeAndBuyerGivenSupplierCode("UNITY", "52186"));
        System.out.println(cfg.getDispersionPrivateIn());
        System.out.println(cfg.getDispersionPrivateOut());
        System.out.println(cfg.getDispersionDoc());
        System.out.println(cfg.isDispatchPdf());
    }*/
    
}
