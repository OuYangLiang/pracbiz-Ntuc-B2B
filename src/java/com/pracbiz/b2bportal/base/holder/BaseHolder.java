package com.pracbiz.b2bportal.base.holder;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.ErrorHelper;

public abstract class BaseHolder implements Serializable
{
    private static final long serialVersionUID = 2462759252343383070L;

    protected static final Logger log = LoggerFactory.getLogger(BaseHolder.class);
    
    private int startRecordNum;
    private int numberOfRecordsToSelect;
    private String sortField;
    private String sortOrder;
    private int requestPage = 1;
    private int pageSize;
    private int dojoIndex;
    
    
    public abstract String getCustomIdentification();
    
    
    public String getLogicalKey()
    {
        return "Unspecified";
    }


    public int getPageSize()
    {
        return pageSize;
    }


    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }


    public int getDojoIndex()
    {
        return dojoIndex;
    }


    public void setDojoIndex(int dojoIndex)
    {
        this.dojoIndex = dojoIndex;
    }


    public int getStartRecordNum()
    {
        return startRecordNum;
    }


    public void setStartRecordNum(int startRecordNum)
    {
        this.startRecordNum = startRecordNum;
    }


    public int getNumberOfRecordsToSelect()
    {
        return numberOfRecordsToSelect;
    }


    public void setNumberOfRecordsToSelect(int numberOfRecordsToSelect)
    {
        this.numberOfRecordsToSelect = numberOfRecordsToSelect;
    }


    public String getSortField()
    {
        return sortField;
    }


    public void setSortField(String sortField)
    {
        this.sortField = sortField;
    }


    public String getSortOrder()
    {
        return sortOrder;
    }


    public void setSortOrder(String sortOrder)
    {
        this.sortOrder = sortOrder;
    }


    public int getRequestPage()
    {
        return requestPage;
    }


    public void setRequestPage(int requestPage)
    {
        this.requestPage = requestPage;
    }


    public String toString()
    {
        try
        {
            return BeanUtils.describe(this).toString();
        }
        catch (Exception exception)
        {
            return exception.getMessage();
        }
    }


    public void trimAllString()  throws Exception
    {
        Method[] methods = this.getClass().getMethods();
        if (methods != null && methods.length > 0)
        {
            for (int i = 0; i < methods.length; i++)
            {
                Method method = methods[i];
                
                if (method.getName().equals("getCustomIdentification") ||
                    method.getName().equals("getLogicalKey") ||
                    !(method.getName().startsWith("get")))
                {
                    continue;
                }
                
                Object resultObj = method.invoke(this, new Object[] {});
                
                if (!(resultObj instanceof String))
                {
                    continue;
                }
                
                String resultStr = (String) resultObj;
                String setMethodName = "set"
                        + method.getName().substring(3,
                                method.getName().length());

                try
                {
                    Method setMethod = this.getClass()
                            .getMethod(setMethodName,
                                    new Class[] { String.class });
                    setMethod.invoke(this,
                            new Object[] { resultStr.trim() });
                }
                catch (NoSuchMethodException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
    }


    public void setAllEmptyStringToNull() throws Exception
    {
        Method[] methods = this.getClass().getMethods();
        
        if (methods == null || methods.length == 0)
        {
            return;
        }
        
        for (int i = 0; i < methods.length; i++)
        {
            Method method = methods[i];
            
            if (method.getName().equals("getCustomIdentification") ||
                method.getName().equals("getLogicalKey") ||
                !(method.getName().startsWith("get")))
            {
                continue;
            }
            
            Object resultObj = method.invoke(this, new Object[] {});
            
            if (!(resultObj instanceof String))
            {
                continue;
            }
            
            String result = (String) resultObj;
            if ("".equals(result.trim()))
            {
                String setMethodName = "set"
                        + method.getName().substring(3,
                                method.getName().length());
                try
                {
                    Method setMethod = this.getClass().getMethod(
                            setMethodName,
                            new Class[] { String.class });
                    setMethod.invoke(this, new Object[] { null });
                }
                catch (NoSuchMethodException e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
            }
        }
    }


    public Map<String, Object> toMapValues()
    {
        Map<String, Object> rlt = new HashMap<String, Object>();

        Method[] methods = this.getClass().getDeclaredMethods();
        if (methods == null || methods.length == 0)
        {
            return rlt;
        }

        for (Method method : methods)
        {
            if (!Modifier.isPublic(method.getModifiers()))
            {
                continue;
            }
            
            if (method.getName().equals("getCustomIdentification") ||
                method.getName().equals("getLogicalKey") ||
                !(method.getName().startsWith("get")))
            {
                continue;
            }
            
            Object resultObj = null;

            try
            {
                resultObj = method.invoke(this, new Object[] {});
            }
            catch (Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
                continue;
            }

            char[] array = method.getName().substring(3).toCharArray();
            StringBuffer sb = new StringBuffer();

            for (char c : array)
            {
                if (Character.isUpperCase(c))
                {
                    sb.append('_');
                    sb.append(c);
                }
                else
                {
                    sb.append(Character.toUpperCase(c));
                }
            }

            String key = sb.toString();

            if (key.startsWith("_"))
            {
                key = key.substring(1);
            }
            
            if (resultObj instanceof Enum<?>)
            {
                resultObj = resultObj.toString();
            }
            
            rlt.put(key, resultObj);
        }

        return rlt;
    }
}
