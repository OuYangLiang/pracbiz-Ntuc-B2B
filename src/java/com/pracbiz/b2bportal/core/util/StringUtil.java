package com.pracbiz.b2bportal.core.util;

import org.apache.commons.lang.StringUtils;




public final class StringUtil
{
    private static StringUtil instance;
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String AMPERSAND_MARK_ENTITY_NAME = "&amp;";
    private static final String AMPERSAND_MARK_CHARACTER = "&";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String RIGHT_DOUBLE_QUOTATION_MARK = "\"";
    
    private StringUtil()
    {
        
    }
    
    public static StringUtil getInstance()
    {
        synchronized (StringUtil.class)
        {
            if(instance == null)
            {
                instance = new StringUtil();
            }
        }
        return instance;
    }
    
    public String convertDocNo(String input)
    {
        if(input == null)
        {
            return null;
        }
        return input.replaceAll("/", "##-##").replaceAll("\\\\", "##=##");
    }
    
    public String convertToDocNo(String input)
    {
        if(input == null)
        {
            return null;
        }
        return input.replaceAll("##-##", "/").replaceAll("##=##", "\\\\");
    }
    
    public String convertObjectToString(Object o)
    {
        if (o == null)
        {
            return "";
        }
        return String.valueOf(o);
    }
    
    public String replaceSpecialCharactersForPageValue(String json)
    {
        if (json == null)
        {
            return null;
        }
        String result = StringUtils.replace(json,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
        
        result = StringUtils.replace(result, AMPERSAND_MARK_ENTITY_NAME,
            AMPERSAND_MARK_CHARACTER);
        
        return  StringUtils.replace(result,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER); 
    }
    
    public String replaceSpecialCharactersForJson(String json)
    {
        if (json == null)
        {
            return null;
        }
        
        String result = StringUtils.replace(json, RIGHT_SINGLE_QUOTATION_MARK_CHARACTER,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME);
        
        return StringUtils.replace(result,  RIGHT_DOUBLE_QUOTATION_MARK,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
    }
    
    public String convertDocNoWithNoLeading(String input, String prefix)
    {
        if(input == null)
        {
            return null;
        }
        String output = null;
        if (input.startsWith(prefix))
        {
            if (input.length() == 1)
            {
                output = input;
                return output;
            }
            
            output = this.convertDocNoWithNoLeading(input.substring(1, input.length()), prefix);
            return output;
        }
        else
        {
            output = input;
            return output;
        }
    }
    
    public String convertStringWithPrefixOrPostfix(String input, boolean postfix, char character, int length)
    {
        StringBuffer newString = new StringBuffer();
        
        if (input == null)
        {
            input = "";
        }
        
        if (input.length() < length)
        {
            int strLength = length - input.length();
            newString = newString.append(input);
            for (int i = 0; i < strLength ; i++)
            {
                if (postfix)
                {
                    newString =  newString.append(character);
                }
                else
                {
                    newString = newString.insert(0, character);
                }
            }
        }
        
        if (input.length() >= length)
        {
            newString.append(input.substring(0, length));
        }
        
        return newString.toString();
    }
}
