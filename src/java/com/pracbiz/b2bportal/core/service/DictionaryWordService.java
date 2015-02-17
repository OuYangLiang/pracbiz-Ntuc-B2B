package com.pracbiz.b2bportal.core.service;

import java.util.List;

import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.core.holder.DictionaryWordHolder;

public interface DictionaryWordService extends
    BaseService<DictionaryWordHolder>, DBActionService<DictionaryWordHolder>
{
    public List<DictionaryWordHolder> selectAllDictionaryWords()
        throws Exception;
}
