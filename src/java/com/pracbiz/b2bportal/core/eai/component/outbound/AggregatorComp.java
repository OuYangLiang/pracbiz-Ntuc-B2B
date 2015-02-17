//*****************************************************************************
//
// File Name       :  AggregatorComp.java
// Date Created    :  Nov 22, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 22, 2012 10:21:45 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.component.outbound;

import java.util.LinkedList;
import java.util.List;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.pracbiz.b2bportal.core.eai.message.BatchMsg;
import com.pracbiz.b2bportal.core.eai.message.DocMsg;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class AggregatorComp implements Callable
{
    @Override
    public Object onCall(MuleEventContext ctx)
    {
        List<?> list = (List<?>)ctx.getMessage().getPayload();
        
        List<DocMsg> docs = new LinkedList<DocMsg>();
        
        for (Object obj : list)
        {
            if (obj instanceof DocMsg)
            {
                docs.add((DocMsg)obj);
            }
        }
        
        BatchMsg batch = docs.get(0).getBatch();
        batch.setDocs(docs);
        return batch;
    }

}
