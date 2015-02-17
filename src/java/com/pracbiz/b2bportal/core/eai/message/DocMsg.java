//*****************************************************************************
//
// File Name       :  DocMsg.java
// Date Created    :  Nov 23, 2012
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Nov 23, 2012 9:56:45 AM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.base.util.FileUtil;
import com.pracbiz.b2bportal.core.constants.DeploymentMode;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.eai.message.constants.Direction;
import com.pracbiz.b2bportal.core.eai.message.constants.MsgType;
import com.pracbiz.b2bportal.core.eai.message.visitor.DocMsgVisitor;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public abstract class DocMsg
{   
    private BigDecimal docOid;
    private BigDecimal remoteOid;
    private String refNo;
    private BigDecimal senderOid;
    private String senderCode;
    private String senderName;
    private BigDecimal receiverOid;
    private String receiverCode;
    private String receiverName;
    private Date inDate;
    private Date procDate;
    private Date sentDate;
    private Date outDate;
    private Date alertDate;
    private String originalFilename;
    private String targetFilename;
    private String reportFilename;
    private String remarks;
    private boolean valid = true;
    private boolean active = true;
    private DocContextRef context;
    private String outputFormat;
    private String inputFormat;
    private BuyerHolder buyer;
    private SupplierHolder supplier;
    private BatchMsg batch;
    private boolean amended;
    private List<String> errorMsg;
    
    private static final String TRANS_MODE_DIRECT = "direct";
    
    private boolean originalFileGeneratedOnPortal;

    public boolean isOriginalFileGeneratedOnPortal()
    {
        return originalFileGeneratedOnPortal;
    }

    public void setOriginalFileGeneratedOnPortal(
        boolean originalFileGeneratedOnPortal)
    {
        this.originalFileGeneratedOnPortal = originalFileGeneratedOnPortal;
    }

    public BigDecimal getDocOid()
    {
        return docOid;
    }

    public void setDocOid(BigDecimal docOid)
    {
        this.docOid = docOid;
    }

    public String getRefNo()
    {
        return refNo;
    }

    public void setRefNo(String refNo)
    {
        this.refNo = refNo;
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

    public BigDecimal getReceiverOid()
    {
        return receiverOid;
    }

    public void setReceiverOid(BigDecimal receiverOid)
    {
        this.receiverOid = receiverOid;
    }

    public String getReceiverCode()
    {
        return receiverCode;
    }

    public void setReceiverCode(String receiverCode)
    {
        this.receiverCode = receiverCode;
    }

    public String getReceiverName()
    {
        return receiverName;
    }

    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public Date getInDate()
    {
        return inDate == null ? null : (Date)inDate.clone();
    }

    public void setInDate(Date inDate)
    {
        this.inDate = inDate == null ? null : (Date)inDate.clone();
    }

    public Date getProcDate()
    {
        return procDate == null ? null : (Date)procDate.clone();
    }

    public void setProcDate(Date procDate)
    {
        this.procDate = procDate == null ? null : (Date)procDate.clone();
    }

    public Date getSentDate()
    {
        return sentDate == null ? null : (Date)sentDate.clone();
    }

    public void setSentDate(Date sentDate)
    {
        this.sentDate = sentDate == null ? null : (Date)sentDate.clone();
    }

    public Date getOutDate()
    {
        return outDate == null ? null : (Date)outDate.clone();
    }

    public void setOutDate(Date outDate)
    {
        this.outDate = outDate == null ? null : (Date)outDate.clone();
    }

    public Date getAlertDate()
    {
        return alertDate == null ? null : (Date)alertDate.clone();
    }

    public void setAlertDate(Date alertDate)
    {
        this.alertDate = alertDate == null ? null : (Date)alertDate.clone();
    }

    public String getOriginalFilename()
    {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename)
    {
        this.originalFilename = originalFilename;
    }

    public String getTargetFilename()
    {
        return targetFilename;
    }

    public void setTargetFilename(String targetFilename)
    {
        this.targetFilename = targetFilename;
    }

    public String getReportFilename()
    {
        return reportFilename;
    }

    public void setReportFilename(String reportFilename)
    {
        this.reportFilename = reportFilename;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
    
    public DocContextRef getContext()
    {
        return context;
    }

    public void setContext(DocContextRef context)
    {
        this.context = context;
    }
    
    public String getOutputFormat()
    {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat)
    {
        this.outputFormat = outputFormat;
    }

    public String getInputFormat()
    {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat)
    {
        this.inputFormat = inputFormat;
    }

    public BigDecimal getRemoteOid()
    {
        return remoteOid;
    }

    public void setRemoteOid(BigDecimal remoteOid)
    {
        this.remoteOid = remoteOid;
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

    public boolean isValid()
    {
        return valid;
    }

    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
    
    public BatchMsg getBatch()
    {
        return batch;
    }

    public void setBatch(BatchMsg batch)
    {
        this.batch = batch;
    }

    public boolean isAmended()
    {
        return amended;
    }

    public void setAmended(boolean amended)
    {
        this.amended = amended;
    }

    public List<String> getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(List<String> errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public void renameDocFileWithNewMsgId() throws IOException
    {
        if (Direction.outbound.equals(this.getMsgDirection()))
        {
            if (DeploymentMode.LOCAL.equals(this.getBuyer().getDeploymentMode()))
            {
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getOriginalFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewOriginalFilenameWithMsgIdForLocalSender()));
                this.originalFilename = getNewOriginalFilenameWithMsgIdForLocalSender();
            }
            else
            {
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getOriginalFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewOriginalFilenameWithMsgIdForRemoteSender()));
                this.originalFilename = getNewOriginalFilenameWithMsgIdForRemoteSender();
                
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getReportFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewReportFilenameWithMsgIdForRemoteSender()));
                this.reportFilename = getNewReportFilenameWithMsgIdForRemoteSender();
            }
        }
        else
        {
            if (DeploymentMode.LOCAL.equals(this.getSupplier().getDeploymentMode()))
            {
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getOriginalFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewOriginalFilenameWithMsgIdForLocalSender()));
                this.originalFilename = getNewOriginalFilenameWithMsgIdForLocalSender();
            }
            else
            {
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getOriginalFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewOriginalFilenameWithMsgIdForRemoteSender()));
                this.originalFilename = getNewOriginalFilenameWithMsgIdForRemoteSender();
                
                FileUtil.getInstance().renameFile(
                    new File(this.getContext().getWorkDir(),
                        this.getReportFilename()),
                    new File(this.getContext().getWorkDir(),
                        getNewReportFilenameWithMsgIdForRemoteSender()));
                this.reportFilename = getNewReportFilenameWithMsgIdForRemoteSender();
            }
        }
        
    }
    
    public String computeRemoteAckFilename()
    {
        String filename = this.getOriginalFilename();

        String[] parts = filename.split("_");
        StringBuffer rlt = new StringBuffer(filename);
        rlt.insert(parts[0].length(), "ACK");
        filename = rlt.toString();

        int index1 = filename.lastIndexOf('_');
        int index2 = filename.lastIndexOf('.');

        return filename.substring(0, index1) + "_" + this.getRemoteOid()
            + filename.substring(index2);
    }
    
    public MsgTransactionsHolder convertToMsgTransactions()
    {
        MsgTransactionsHolder msg = new MsgTransactionsHolder();

        msg.setDocOid(this.getDocOid());
        msg.setMsgType(this.getMsgType().toString());
        msg.setMsgRefNo(this.getRefNo());
        
        if (buyer != null)
        {
            msg.setBuyerOid(this.getBuyer().getBuyerOid());
            msg.setBuyerCode(this.getBuyer().getBuyerCode());
            msg.setBuyerName(this.getBuyer().getBuyerName());
        }
        
        SupplierHolder supplier = this.getSupplier();
        
        if (supplier != null)
        {
            msg.setSupplierOid(supplier.getSupplierOid());
            msg.setSupplierCode(Direction.outbound.equals(getMsgDirection()) ? receiverCode : senderCode);
            msg.setSupplierName(supplier.getSupplierName());
        }
        
        msg.setCreateDate(this.getInDate());
        msg.setProcDate(this.getProcDate());
        msg.setSentDate(this.getSentDate());
        msg.setOutDate(this.getOutDate());
        msg.setAlertDate(this.getAlertDate());
        msg.setOriginalFilename(this.getOriginalFilename());
        msg.setExchangeFilename(this.getTargetFilename());
        msg.setReportFilename(this.getReportFilename());
        msg.setActive(this.isActive());
        msg.setValid(this.isValid());
        msg.setReadStatus(ReadStatus.UNREAD);
        
        //Trans mode is 'dircet' & client enable is 'flase' & direction is 'outbound'
        if(supplier != null && supplier.getTransMode() != null
            && supplier.getTransMode().trim().equalsIgnoreCase(
                TRANS_MODE_DIRECT) && !supplier.getClientEnabled()
            && this.getMsgDirection().equals(Direction.outbound))
        {
            msg.setReadStatus(ReadStatus.READ);
            msg.setReadDate(new Date());
        }

        msg.setRemarks(this.getRemarks());
        
        return msg;
    }
    
    public final Direction getMsgDirection()
    {
        return this.getMsgType().getDirection();
    }

    public abstract boolean isGeneratePdf();
    
    public abstract MsgType getMsgType();

    public abstract String computePdfFilename();
    
    @SuppressWarnings({"rawtypes" })
    public abstract ReportEngineParameter computeReportEngineParameter();
    
    public abstract void accept(DocMsgVisitor visitor) throws Exception;
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private String getNewReportFilenameWithMsgIdForRemoteSender()
    {
        int index1 = this.getOriginalFilename().lastIndexOf("_");
        int index2 = this.getOriginalFilename().lastIndexOf(".");

        return this.getReportFilename().substring(0, index1) + "_"
            + this.getDocOid() + this.getReportFilename().substring(index2);
    }
    
    private String getNewOriginalFilenameWithMsgIdForRemoteSender()
    {
        int index1 = this.getOriginalFilename().lastIndexOf("_");
        int index2 = this.getOriginalFilename().lastIndexOf(".");

        return this.getOriginalFilename().substring(0, index1) + "_"
            + this.getDocOid() + this.getOriginalFilename().substring(index2);
    }

    private String getNewOriginalFilenameWithMsgIdForLocalSender()
    {
        int index = this.getOriginalFilename().lastIndexOf(".");
        return this.getOriginalFilename().substring(0, index) + "_"
            + this.getDocOid() + this.getOriginalFilename().substring(index);
    }
    
}
