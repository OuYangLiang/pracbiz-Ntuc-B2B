package com.pracbiz.b2bportal.base.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.springframework.core.io.Resource;

public class ValidationConfigHelper extends BaseXmlConfig
{
    private static final String VLD_PTN_KEY = "VLD_PTN_KEY";


    public ValidationConfigHelper(File cfgFile) throws ConfigurationException,
            MalformedURLException
    {
        super(cfgFile);
    }


    public static ValidationConfigHelper getBeanInstance(Resource configPath)
            throws ConfigurationException, MalformedURLException, IOException
    {
        ValidationConfigHelper cfg = new ValidationConfigHelper(
                configPath.getFile());

        cfg.initConfigMap();

        return cfg;
    }


    protected void initConfigMap()
    {
        super.initConfigMap();

        this.configMap.put(VLD_PTN_KEY, getPatterns());
    }


    @SuppressWarnings("unchecked")
    public Map<String, String> getCachePatterns()
    {
        return (Map<String, String>) this.configMap.get(VLD_PTN_KEY);
    }


    public String getCachePattern(String patternKey)
    {
        Map<String, String> mapPtn = getCachePatterns();

        if (mapPtn.containsKey(patternKey))
        {
            return (String) mapPtn.get(patternKey);
        }
        else
        {
            return null;
        }
    }


    public String getPattern(String patternKey)
    {
        if (patternKey == null)
        {
            return null;
        }

        List<String> listKeys = this.listValue("validation.patterns.pattern[@key]");

        if (listKeys == null || listKeys.size() < 1)
        {
            return null;
        }

        for (int i = 0; i < listKeys.size(); i++)
        {
            String key = (String) listKeys.get(i);

            if (patternKey.equalsIgnoreCase(key))
            {
                return this.stringValue("validation.patterns.pattern(" + i
                        + ")");
            }
        }

        return null;
    }


    private Map<String, String> getPatterns()
    {
        Map<String, String> mapRlt = new HashMap<String, String>();

        List<String> listKeys = this.listValue("validation.patterns.pattern[@key]");

        if (listKeys == null || listKeys.size() < 1)
        {
            return mapRlt;
        }

        for (int i = 0; i < listKeys.size(); i++)
        {
            String key = (String) listKeys.get(i);

            String value = this.stringValue("validation.patterns.pattern(" + i
                    + ")");

            if (value == null || value.trim().length() < 1)
            {
                value = null;
            }

            mapRlt.put(key, value);
        }

        return mapRlt;
    }

}
