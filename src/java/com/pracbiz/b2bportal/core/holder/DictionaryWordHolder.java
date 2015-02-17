package com.pracbiz.b2bportal.core.holder;

import com.pracbiz.b2bportal.base.holder.BaseHolder;

public class DictionaryWordHolder extends BaseHolder
{
    private static final long serialVersionUID = -4026279861675018884L;
    private String keyWord;


    public String getKeyWord()
    {
        return keyWord;
    }


    public void setKeyWord(String keyWord)
    {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }


    @Override
    public String getCustomIdentification()
    {
        return keyWord;
    }

}