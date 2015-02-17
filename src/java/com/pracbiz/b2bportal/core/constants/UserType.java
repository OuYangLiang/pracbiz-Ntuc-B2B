package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

public enum UserType
{
    SYSADMIN("UserType.SysAdmin"),

    BUYERADMIN("UserType.BuyerAdmin"),
    
    BUYERUSER("UserType.BuyerUser"),
    
    SUPPLIERADMIN("UserType.SupplierAdmin"),
    
    SUPPLIERUSER("UserType.SupplierUser"),
    
    STOREADMIN("UserType.StoreAdmin"),
    
    STOREUSER("UserType.StoreUser");

    private String key;


    private UserType(String key)
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
        for (UserType rt : UserType.values())
        {
            rlt.put(rt.name(), rt.getKey());
        }
        return rlt;
    }
}
