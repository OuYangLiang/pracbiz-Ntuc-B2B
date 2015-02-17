package com.pracbiz.b2bportal.core.holder.extension;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;

public class InvHeaderExHolder extends InvHeaderHolder
{
    private static final long serialVersionUID = 6342015079888953529L;
    private PoType poType;
    private boolean autoInvNumber;
    private PoStatus poStatus;
    private BigDecimal invTotalBeforeAdditional;
    private BigDecimal totalPay;

    public PoType getPoType()
    {
        return poType;
    }

    public void setPoType(PoType poType)
    {
        this.poType = poType;
    }

    public boolean isAutoInvNumber()
    {
        return autoInvNumber;
    }

    public void setAutoInvNumber(boolean autoInvNumber)
    {
        this.autoInvNumber = autoInvNumber;
    }

    public PoStatus getPoStatus()
    {
        return poStatus;
    }

    public void setPoStatus(PoStatus poStatus)
    {
        this.poStatus = poStatus;
    }
    
    public BigDecimal getInvTotalBeforeAdditional()
    {
        return invTotalBeforeAdditional;
    }

    public void setInvTotalBeforeAdditional(BigDecimal invTotalBeforeAdditional)
    {
        this.invTotalBeforeAdditional = invTotalBeforeAdditional;
    }

    public BigDecimal getTotalPay()
    {
        return totalPay;
    }

    public void setTotalPay(BigDecimal totalPay)
    {
        this.totalPay = totalPay;
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
