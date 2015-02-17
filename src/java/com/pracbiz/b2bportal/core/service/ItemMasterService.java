package com.pracbiz.b2bportal.core.service;

import java.io.File;
import java.math.BigDecimal;

import com.pracbiz.b2bportal.base.holder.CommonParameterHolder;
import com.pracbiz.b2bportal.base.service.BaseService;
import com.pracbiz.b2bportal.base.service.DBActionService;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.core.holder.ItemMasterHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.extension.ItemMasterSummaryHolder;

public interface ItemMasterService extends BaseService<ItemMasterHolder>,
        DBActionService<ItemMasterHolder>,
        PaginatingService<ItemMasterSummaryHolder>
{
    public void saveItemIn(CommonParameterHolder cp, File file, String path, ItemMasterHolder item,
            MsgTransactionsHolder msg) throws Exception;
    
    
    public void sendItemIn(CommonParameterHolder cp, ItemMasterHolder oldItem, MsgTransactionsHolder msg, File targetFile, String mboxId) throws Exception;
    
    
    public  ItemMasterHolder selectByKey(BigDecimal itemOid) throws Exception;
    
    
    public void deleteItemIn(CommonParameterHolder cp, BigDecimal itemOid) throws Exception;
}
