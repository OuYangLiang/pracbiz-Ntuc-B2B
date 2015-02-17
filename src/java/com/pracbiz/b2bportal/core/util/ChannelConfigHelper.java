//*****************************************************************************
//
// File Name       :  ChannelConfigHelper.java
// Date Created    :  Nov 29, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 29, 2012 9:24:17 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.core.io.Resource;

import com.pracbiz.b2bportal.base.util.BaseXmlConfig;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class ChannelConfigHelper extends BaseXmlConfig
{
    public final static String CHANNEL_ELEMENT = "channel";
    public final static String CHANNEL_ATTR_NAME = "name";
    public final static String CHANNEL_ATTR_TYPE = "type";
    public final static String CHANNEL_ATTR_NOOP = "noop";
    
    public final static String CHANNEL_TYPE_BUYER = "buyer";
    public final static String CHANNEL_TYPE_SUPPLIER = "supplier";
    
    public final static String CHANNEL_NOOP_YES = "yes";
    public final static String CHANNEL_NOOP_NO = "no";
    
    public final static String CHANNEL_PROP_URL = "url";
    public final static String CHANNEL_PROP_TARGET_FOLDER = "targetFolder";
    public final static String CHANNEL_PROP_MBOX_ID = "mailboxId";
    public final static String CHANNEL_PROP_LOCAL_KEY = "localPgpKeyId";
    public final static String CHANNEL_PROP_REMOTE_KEY = "remotePgpKeyId";
    public final static String CHANNEL_PROP_FILE_FORMAT = "fileFormat";
    
    public final static String CHANNEL_FILE_TYPE_PREFIX = "FILE_";
    
    private final Map<String, Map<String, String>> cacheChannels = this
        .initChannels();

    protected ChannelConfigHelper(File cfgFile) throws ConfigurationException,
        MalformedURLException
    {
        super(cfgFile);
    }
    
    public static ChannelConfigHelper getBeanInstance(Resource configPath)
        throws ConfigurationException, MalformedURLException, IOException
    {
        ChannelConfigHelper theSiteConfig = new ChannelConfigHelper(
            configPath.getFile());

        theSiteConfig.initConfigMap();
        return theSiteConfig;
    }
    
    private Map<String, Map<String, String>> initChannels()
    {
        Map<String, Map<String, String>> rlt = new HashMap<String, Map<String, String>>();
        List<String> noops = this.listValue(CHANNEL_ELEMENT + ".[@" + CHANNEL_ATTR_NOOP + "]");
        
        for (int i = 0; i < noops.size(); i++)
        {
            String noop = noops.get(i);
            if (CHANNEL_NOOP_YES.equalsIgnoreCase(noop))
            {
                noops.remove(i);
                i--;
                
                continue;
            }
            
            String name = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")[@" + CHANNEL_ATTR_NAME + "]");
            String type = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")[@" + CHANNEL_ATTR_TYPE + "]");
            String url = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_URL);
            String targetFolder = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_TARGET_FOLDER);
            String mailbox = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_MBOX_ID);
            String localKey = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_LOCAL_KEY);
            String remoteKey = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_REMOTE_KEY);
            List<String> fileTypes = this.listValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_FILE_FORMAT + ".file[@type]");
            
            Map<String, String> map = new HashMap<String, String>();
            map.put(CHANNEL_ATTR_NAME, name);
            map.put(CHANNEL_ATTR_TYPE, type);
            map.put(CHANNEL_PROP_URL, url);
            map.put(CHANNEL_PROP_TARGET_FOLDER, targetFolder);
            map.put(CHANNEL_PROP_MBOX_ID, mailbox);
            map.put(CHANNEL_PROP_LOCAL_KEY, localKey);
            map.put(CHANNEL_PROP_REMOTE_KEY, remoteKey);
            
            for (int j = 0; j< fileTypes.size(); j++)
            {
                String fileType = fileTypes.get(j);
                String fileFormat = this.stringValue(CHANNEL_ELEMENT + "(" + i + ")." + CHANNEL_PROP_FILE_FORMAT + ".file("+ j + ")[@format]");
                
                map.put(CHANNEL_FILE_TYPE_PREFIX + fileType, fileFormat);
            }
            
            rlt.put(name, map);
        }
        
        return rlt;
    }
    
    //*****************************************************
    // Customized methods
    //*****************************************************
    
    public List<String> getBuyerChannels()
    {
        List<String> rlt = new ArrayList<String>();
        
        for (String channel : cacheChannels.keySet())
        {
            if(cacheChannels.get(channel).get(CHANNEL_ATTR_TYPE)
                .equalsIgnoreCase(CHANNEL_TYPE_BUYER))
            {
                rlt.add(channel);
            }
        }
        
        return rlt;
    }
    
    public List<String> getSupplierChannels()
    {
        List<String> rlt = new ArrayList<String>();
        
        for (String channel : cacheChannels.keySet())
        {
            if(cacheChannels.get(channel).get(CHANNEL_ATTR_TYPE)
                .equalsIgnoreCase(CHANNEL_TYPE_SUPPLIER))
            {
                rlt.add(channel);
            }
        }
        
        return rlt;
    }
    
    public String getChannelMailbox(String channel)
    {
        return cacheChannels.get(channel).get(CHANNEL_PROP_MBOX_ID);
    }
    
    public String getChannelUrl(String channel)
    {
        return cacheChannels.get(channel).get(CHANNEL_PROP_URL);
    }
    
    public String getChannelTargetFolder(String channel)
    {
        return cacheChannels.get(channel).get(CHANNEL_PROP_TARGET_FOLDER);
    }
    
    public String getChannelLocalKeyId(String channel)
    {
        return cacheChannels.get(channel).get(CHANNEL_PROP_LOCAL_KEY);
    }
    
    public String getChannelRemoteKeyId(String channel)
    {
        return cacheChannels.get(channel).get(CHANNEL_PROP_REMOTE_KEY);
    }
    
    public String getChannelFileFormat(String channel, MsgType msgType)
    {
        return cacheChannels.get(channel).get(CHANNEL_FILE_TYPE_PREFIX + msgType.name());
    }
    
    /*public static void main(String[] args) throws ConfigurationException,
        MalformedURLException
    {
        ChannelConfigHelper c = new ChannelConfigHelper(
            new File(
                "/Users/ouyang/Documents/workspace/b2bportal/ec-portal/trunk/config/core/application/config_index.xml"));
        
        System.out.println(c.getBuyerChannels());
        System.out.println(c.getChannelFileFormat("WEBPORTAL1", "INV"));
    }*/

}
