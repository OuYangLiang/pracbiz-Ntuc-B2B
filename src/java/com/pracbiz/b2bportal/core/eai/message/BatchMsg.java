//*****************************************************************************
//
// File Name       :  BatchMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 9:50:24 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.core.eai.message.constants.BatchType;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;

/**
 * TODO To provide an overview of this class.
 * 
 * @author ouyang
 */
public class BatchMsg
{
    private String batchFileName;
    private BatchType batchType;
    private String batchNo;
    private Date inDate;
    private List<DocMsg> docs;
    private BigDecimal senderOid;
    private String senderCode;
    private String senderName;
    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private BatchContextRef context;
    private PoBatchSummary poBatchSummary;
    private List<String> sourceFileNames;
    private String transformedBy;
    
    
    public PoBatchSummary getPoBatchSummary()
    {
        return poBatchSummary;
    }

    public void setPoBatchSummary(PoBatchSummary poBatchSummary)
    {
        this.poBatchSummary = poBatchSummary;
    }

    public String getBatchFileName()
    {
        return batchFileName;
    }

    public void setBatchFileName(String batchFileName)
    {
        this.batchFileName = batchFileName;
    }

    public BatchType getBatchType()
    {
        return batchType;
    }

    public void setBatchType(BatchType batchType)
    {
        this.batchType = batchType;
    }

    public String getBatchNo()
    {
        return batchNo;
    }

    public void setBatchNo(String batchNo)
    {
        this.batchNo = batchNo;
    }

    public Date getInDate()
    {
        return inDate == null ? null : (Date)inDate.clone();
    }

    public void setInDate(Date inDate)
    {
        this.inDate = inDate == null ? null : (Date)inDate.clone();
    }

    public List<DocMsg> getDocs()
    {
        return docs;
    }

    public void setDocs(List<DocMsg> docs)
    {
        this.docs = docs;
    }

    public BigDecimal getSenderOid()
    {
        return senderOid;
    }

    public void setSenderOid(BigDecimal senderOid)
    {
        this.senderOid = senderOid;
    }

    public String getSenderCode()
    {
        return senderCode;
    }

    public void setSenderCode(String senderCode)
    {
        this.senderCode = senderCode;
    }

    public String getSenderName()
    {
        return senderName;
    }

    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }

    public BatchContextRef getContext()
    {
        return context;
    }

    public void setContext(BatchContextRef context)
    {
        this.context = context;
    }

    public BuyerHolder getBuyer()
    {
        return buyer;
    }

    public void setBuyer(BuyerHolder buyer)
    {
        this.buyer = buyer;
    }

    public SupplierHolder getSupplier()
    {
        return supplier;
    }

    public void setSupplier(SupplierHolder supplier)
    {
        this.supplier = supplier;
    }
    
    public List<String> getSourceFileNames()
    {
        return sourceFileNames;
    }

    public void setSourceFileNames(List<String> sourceFileNames)
    {
        this.sourceFileNames = sourceFileNames;
    }


    public String getTransformedBy()
    {
        return transformedBy;
    }

    public void setTransformedBy(String transformedBy)
    {
        this.transformedBy = transformedBy;
    }
    

    public int calculateAmendedDocs()
    {
        if (docs == null || docs.isEmpty())
        {
            return 0;
        }
        
        int count = 0;
        
        for (DocMsg doc : docs)
        {
            if (doc.isAmended())
            {
                count ++;
            }
        }
        
        return count;
    }
}
