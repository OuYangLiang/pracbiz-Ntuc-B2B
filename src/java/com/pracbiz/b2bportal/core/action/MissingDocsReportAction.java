//*****************************************************************************
//
// File Name       :  MissingGrnReport.java
// Date Created    :  Dec 10, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Dec 10, 2013 11:29:30 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerHolder;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReport;
import com.pracbiz.b2bportal.core.report.excel.MissingGiReportParameter;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReport;
import com.pracbiz.b2bportal.core.report.excel.MissingGrnReportParameter;
import com.pracbiz.b2bportal.core.service.BuyerService;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class MissingDocsReportAction extends TransactionalDocsBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(ReadStatusReportAction.class);
    private static final long serialVersionUID = 3523235911279618709L;
    
    private static final String EXCEL_IS_GENERATING = "isGenerating";
    private static final String GRN_DATA = "grnData";
    private static final String GI_DATA = "giData";
    private static final String BUYER_DATA = "buyerData";
    
    @Autowired transient private BuyerService buyerService;
    @Autowired transient private MissingGrnReport missingGrnReport;
    @Autowired transient private PoLocationService poLocationService;
    @Autowired transient private MissingGiReport missingGiReport;
    @Autowired transient private RtvLocationService rtvLocationService;
    @Autowired transient private ControlParameterService controlParameterService;
    
    private transient InputStream rptResult;
    private String rptFileName;
    private String errorMsg;
    private String data;
    private transient MissingGrnReportParameter param;
    private Date begin;
    private Date end;
    private BigDecimal buyerOid;
    private Map<String,String> msgTypes;
    
    
    public String init()
    {
        try
        {
            if (param == null)
            {
                param = new MissingGrnReportParameter();
                initSearchParam();
                begin = DateUtil.getInstance().getFirstTimeOfDay(new Date());
                end = DateUtil.getInstance().getLastTimeOfDay(new Date());
            }
        }
        catch (Exception e)
        {
            this.handleException(e);

            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    public String exportExcel()
    {
        try
        {
            this.getSession().put(EXCEL_IS_GENERATING, "Y");
            byte[] datas = null;
            
            BuyerHolder buyer = (BuyerHolder)this.getSession().get(BUYER_DATA);
            
            
            //Missing Grn Report
            if ((List<MissingGrnReportParameter>)this.getSession().get(GRN_DATA) != null)
            {
                datas = missingGrnReport.exportExcel(buyer, (List<MissingGrnReportParameter>)this.getSession().get(GRN_DATA));
                rptFileName = "Missing GRN Notification Report - "  + buyer.getBuyerCode() + " - " + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec();
            }
            //Missing Gi Report
            if ((List<MissingGiReportParameter>)this.getSession().get(GI_DATA) != null)
            {
                datas = missingGiReport.exportExcel(buyer, (List<MissingGiReportParameter>)this.getSession().get(GI_DATA));
                rptFileName = "Missing GI Notification Report - " + buyer.getBuyerCode() + " - " + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec();
            }
            
            if (datas != null && datas.length > 0)
            {
                rptResult = new ByteArrayInputStream(datas);
            }
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        finally
        {
            this.getSession().remove(EXCEL_IS_GENERATING);
            this.getSession().remove(BUYER_DATA);
            this.getSession().remove(GRN_DATA);
            this.getSession().remove(GI_DATA);
        }
      
        return SUCCESS;
    }
    
    
    public String checkExcelData()
    {
        try
        {
            boolean flag = this.hasErrors();
            
            BuyerHolder buyer = buyerService.selectBuyerByKey(buyerOid);
            
            if (!flag && null == buyer)
            {
                data = "buyernotexist";
                flag = true;
            }
            
            if (!flag && (null == DateUtil.getInstance().getFirstTimeOfDay(begin) 
                || null == DateUtil.getInstance().getLastTimeOfDay(end)))
            {
                data = "datanull";
                flag = true;
            }
            
            if (!flag && DateUtil.getInstance().getLastTimeOfDay(end).before(DateUtil.getInstance().getFirstTimeOfDay(begin)))
            {
                data = "tobefore";
                flag = true;
            }
            
            if (!flag)
            {
                ControlParameterHolder maxDayOfReport = controlParameterService
                    .selectCacheControlParameterBySectIdAndParamId(
                        "CTRL", "MAX_DAY_OF_REPORT");
                
                int days = DateUtil.getInstance().daysAfterDate(DateUtil.getInstance().getFirstTimeOfDay(begin), 
                    DateUtil.getInstance().getLastTimeOfDay(end));
                
                if ((days + 1) > maxDayOfReport.getNumValue())
                {
                    data = "maxDay," + maxDayOfReport.getNumValue();
                    flag = true;
                }
            }
            
            if (!flag && null != this.getSession().get(EXCEL_IS_GENERATING))
            {
                data = "generating";
                flag = true;
            }
            if (!flag)
            {
                param.trimAllString();
                param.setAllEmptyStringToNull();
                
                if ("grn".equalsIgnoreCase(param.getMsgType()))
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<MissingGrnReportParameter> params = poLocationService
                        .selectMissingGrnReprotRecords(
                            buyer.getBuyerOid(),
                            (null != param.getSupplierCode() && !param
                            .getSupplierCode().trim().isEmpty()) ? param
                                .getSupplierCode().trim() : null, DateUtil
                                .getInstance().getFirstTimeOfDay(begin),
                                DateUtil.getInstance().getLastTimeOfDay(end));
                    
                    if (params == null || params.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(GRN_DATA, params);
                    }
                    
                }
                else if ("gi".equalsIgnoreCase(param.getMsgType()))
                {
                    this.getSession().put(BUYER_DATA, buyer);
                    
                    List<MissingGiReportParameter> params = rtvLocationService
                        .selectMisssingGiReportRecords(
                                buyer.getBuyerOid(),
                                (null != param.getSupplierCode() && !param
                                        .getSupplierCode().trim().isEmpty()) ? param
                                        .getSupplierCode().trim() : null, DateUtil
                                        .getInstance().getFirstTimeOfDay(begin),
                                DateUtil.getInstance().getLastTimeOfDay(end));
                    
                    if (params == null || params.isEmpty())
                    {
                        data = "empty";
                    }
                    else
                    {
                        this.getSession().put(GI_DATA, params);
                    }
                }
                else
                {
                    data = "empty";
                }
            }
            
        }
        catch(Exception e)
        {
            this.handleException(e);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    @SuppressWarnings("unchecked")
    private void initSearchParam() throws Exception
    {
        List<String> urls = (List<String>) this.getSession().get(SESSION_KEY_PERMIT_URL);
        msgTypes = new HashMap<String, String>();
        if (urls.contains("/missingDocsReport/missGrn.action"))
        {
            msgTypes.put("grn", "Goods Receipt Note");
        }
        if (urls.contains("/missingDocsReport/missGi.action"))
        {
            msgTypes.put("gi", "Goods Issued");
        }
        
        buyers = buyerService.selectAvailableBuyersByUserOid(getProfileOfCurrentUser());
    }
    

    public InputStream getRptResult()
    {
        return rptResult;
    }


    public void setRptResult(InputStream rptResult)
    {
        this.rptResult = rptResult;
    }


    public String getRptFileName()
    {
        return rptFileName;
    }


    public void setRptFileName(String rptFileName)
    {
        this.rptFileName = rptFileName;
    }


    public String getData()
    {
        return data;
    }


    public void setData(String data)
    {
        this.data = data;
    }


    public List<? extends Object> getBuyers()
    {
        return buyers;
    }


    public void setBuyers(List<? extends Object> buyers)
    {
        this.buyers = buyers;
    }


    public MissingGrnReportParameter getParam()
    {
        return param;
    }


    public void setParam(MissingGrnReportParameter param)
    {
        this.param = param;
    }


    public String getErrorMsg()
    {
        return errorMsg;
    }


    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }


    public Date getBegin()
    {
        return begin == null ? null : (Date) begin.clone();
    }


    public void setBegin(Date begin)
    {
        this.begin = begin == null ? null : (Date) begin.clone();
    }


    public Date getEnd()
    {
        return end == null ? null : (Date) end.clone();
    }


    public void setEnd(Date end)
    {
        this.end = end == null ? null : (Date) end.clone();
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public Map<String, String> getMsgTypes()
    {
        return msgTypes;
    }


    public void setMsgTypes(Map<String, String> msgTypes)
    {
        this.msgTypes = msgTypes;
    }
    
}
