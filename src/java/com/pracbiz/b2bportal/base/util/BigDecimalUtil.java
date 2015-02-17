//*****************************************************************************
//
// File Name       :  BigDecimalUtil.java
// Date Created    :  Jun 29, 2011
// Last Changed By :  $Author: $
// Last Changed On :  $Date: $
// Revision        :  $Rev: $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2011.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.base.util;

import java.math.BigDecimal;

/**
 * TODO To provide an overview of this class.
 *
 * @author jiangming_2009
 */
public final class BigDecimalUtil
{
    private static BigDecimalUtil instance;
    
    private BigDecimalUtil(){}
    
    public static BigDecimalUtil getInstance()
    {
        synchronized(BigDecimalUtil.class)
        {
            if (instance == null)
            {
                instance = new BigDecimalUtil();
            }
        }
        
        return instance;
    }
    
    public BigDecimal convertStringToBigDecimal(String value, int length)
    {
        if (value == null || "".equals(value.trim()))
        {
            return null;
        }
        
        BigDecimal data = new BigDecimal(value.trim());
        
        return data.divide(BigDecimal.ONE, length, BigDecimal.ROUND_HALF_UP);
    }
    
    
    public BigDecimal format(BigDecimal value, int length)
    {
        if (value == null)
        {
            return BigDecimal.ZERO.divide(BigDecimal.ONE, length, BigDecimal.ROUND_HALF_UP);
        }
        
        return value.divide(BigDecimal.ONE, length, BigDecimal.ROUND_HALF_UP);
    }
    
    
    public BigDecimal formatWithRoundingMode(BigDecimal value, int length, int roundingMode)
    {
        if (value == null)
        {
            return BigDecimal.ZERO.divide(BigDecimal.ONE, length, roundingMode);
        }
        
        return value.divide(BigDecimal.ONE, length, roundingMode);
    }
    
    
    public String convertBigDecimalToStringIntegerWithNoScale(BigDecimal value, int scale)
    {
        if (value ==  null)
        {
            return BigDecimal.ZERO.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).toString();
        }
        
        if (BigDecimal.valueOf(value.intValue()).compareTo(value)==0)
        {
            return value.divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).toString();
        }
        else
        {
            return value.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP).toString();
        }
    }
    
    
    public String convertBigDecimalToStringIntegerWithScale(BigDecimal value, int scale)
    {
        if (value ==  null)
        {
            return BigDecimal.ZERO.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP).toString();
        }
        
        return value.divide(BigDecimal.ONE, scale, BigDecimal.ROUND_HALF_UP).toString();
    }
    
}
