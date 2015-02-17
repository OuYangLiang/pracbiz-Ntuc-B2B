//*****************************************************************************
//
// File Name       :  PoBatchSummary.java
// Date Created    :  May 15, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: May 15, 2013 1:13:36 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class PoBatchSummary
{
    private List<PoBatchSummaryLine> items;

    public List<PoBatchSummaryLine> getItems()
    {
        return items;
    }
    
    
    public int numberOfItems()
    {
        return items == null ? 0 : items.size();
    }
    
    
    public void addItem(PoBatchSummaryLine item)
    {
        if (null == items)
        {
            items = new ArrayList<PoBatchSummaryLine>();
        }
        
        items.add(item);
    }
    
    
    public PoBatchSummaryLine findItemByPoNo(String poNo)
    {
        if (null != items)
        {
            for (PoBatchSummaryLine item : items)
            {
                if (poNo.trim().equalsIgnoreCase(item.getPoNo()))
                {
                    return item;
                }
            }
        }
        
        return null;
    }
    
    
    public void removeItemByPoNo(String poNo)
    {
        if (null != items)
        {
            for (int i = 0; i < items.size(); i++)
            {
                PoBatchSummaryLine item = items.get(i);
                
                if (poNo.trim().equalsIgnoreCase(item.getPoNo()))
                {
                    items.remove(i);
                    i--;
                }
            }
        }
    }
    
}
