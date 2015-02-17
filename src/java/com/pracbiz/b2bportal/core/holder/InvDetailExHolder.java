package com.pracbiz.b2bportal.core.holder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.ErrorHelper;

public class InvDetailExHolder extends InvDetailHolder
{
    private static final long serialVersionUID = 7235643527197682692L;

    private BigDecimal poQty;
    private BigDecimal poUnitPrice;
    private BigDecimal poFocQty;

    public BigDecimal getPoQty()
    {
        return poQty;
    }

    public void setPoQty(BigDecimal poQty)
    {
        this.poQty = poQty;
    }

    public BigDecimal getPoUnitPrice()
    {
        return poUnitPrice;
    }

    public void setPoUnitPrice(BigDecimal poUnitPrice)
    {
        this.poUnitPrice = poUnitPrice;
    }

    public BigDecimal getPoFocQty()
    {
        return poFocQty;
    }

    public void setPoFocQty(BigDecimal poFocQty)
    {
        this.poFocQty = poFocQty;
    }
    
    @Override
    public Map<String, Object> toMapValues()
    {
        Map<String,Object> rlt = super.toMapValues();
        
        Method[] methods = this.getClass().getSuperclass().getDeclaredMethods();
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
            
            if (method.getName().equals("getCustomIdentification"))
            {
                continue;
            }

            if (!method.getName().startsWith("get"))
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
