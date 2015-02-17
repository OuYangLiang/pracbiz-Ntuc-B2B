package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum PageId
{
    P001("PageId.PoSummary"),
    P002("PageId.grnSummary"),
    P003("PageId.rtvSummary"),
    P004("PageId.invSummary"),
    P005("PageId.dnSummary"),
    P006("PageId.doSummary"),
    P007("PageId.pnSummary"),
    P008("PageId.itemSummary"),
    P009("PageId.giSummary"),
    P010("PageId.ccSummary"),
    P011("PageId.quickPoSummary"),
    P012("PageId.salesDataSummary"),
    P013("PageId.cnSummary");
    
    private String key;


    private PageId(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }
    
    
    public static Map<String, String> toMapValue()
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (PageId ms : PageId.values())
        {
            rlt.put(ms.name(), ms.getKey());
        }
        return rlt;
    }
    
    
    public static Map<String, String> toMapValue(BaseAction action)
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (PageId ms : PageId.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
