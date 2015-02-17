package com.pracbiz.b2bportal.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.pracbiz.b2bportal.base.action.BaseAction;

public enum PoInvGrnDnMatchingStatus
{
    PENDING("MatchingStatus.pending"),
    MATCHED("MatchingStatus.matched"),
    MATCHED_BY_DN("MatchingStatus.matchedByDn"),
    UNMATCHED("MatchingStatus.unmatched"),
    AMOUNT_UNMATCHED("MatchingStatus.amount_unmatched"),
    PRICE_UNMATCHED("MatchingStatus.price_unmatched"),
    QTY_UNMATCHED("MatchingStatus.qty_unmatched"),
    INSUFFICIENT_GRN("MatchingStatus.insufficient_grn"),
    INSUFFICIENT_INV("MatchingStatus.insufficient_inv"),
    OUTDATED("MatchingStatus.outdated"),
    UNMATCHED_ALL("MatchingStatus.unmatched_all");

    private String key;


    private PoInvGrnDnMatchingStatus(String key)
    {
        this.key = key;
    }


    public String getKey()
    {
        return key;
    }
    
    
    public static Map<String, String> toMapValue(BaseAction action)
    {
        Map<String,String> rlt = new HashMap<String,String>();
        for (PoInvGrnDnMatchingStatus ms : PoInvGrnDnMatchingStatus.values())
        {
            rlt.put(ms.name(), action.getText(ms.getKey()));
        }
        return rlt;
    }
}
