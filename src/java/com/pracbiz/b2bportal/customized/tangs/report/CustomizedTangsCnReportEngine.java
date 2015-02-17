//*****************************************************************************
//
// File Name       :  DefaultStandardInvReportEngine.java
// Date Created    :  2012-12-24
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.tangs.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.MsgTransactionsHolder;
import com.pracbiz.b2bportal.core.report.DefaultCnReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.MsgTransactionsService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class CustomizedTangsCnReportEngine extends DefaultCnReportEngine
    implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CustomizedTangsCnReportEngine.class);
    
    @Autowired private BuyerStoreService buyerStoreService;
    @Autowired private MsgTransactionsService msgTransactionsService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<CnHolder> parameter, int flag)
    {
        List<CnDetailHolder> cnDetails = parameter.getData().getDetailList();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for(CnDetailHolder cnDetail : cnDetails)
        {
            map = cnDetail.toMapValues();

            if(parameter.getData().getDetailExtendedList() != null)
            {
                List<CnDetailExtendedHolder> cnDetailExs = parameter.getData().getDetailExtendedList();
                
                for (CnDetailExtendedHolder cnDetailEx : cnDetailExs)
                {
                    Integer lineSeq = cnDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(cnDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = cnDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(cnDetailEx.getFieldName().toUpperCase(), cnDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(cnDetailEx.getFieldName().toUpperCase(), cnDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(cnDetailEx.getFieldName().toUpperCase(), cnDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(cnDetailEx.getFieldName().toUpperCase(), cnDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(cnDetailEx.getFieldName().toUpperCase(), cnDetailEx.getDateValue());
                    }
                    
                }
                
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<CnHolder> parameter, int flag)
    {
        CnHeaderHolder cnHeader = parameter.getData().getHeader();
        Map<String, Object> map = null;
        try
        {
            cnHeader.setAllEmptyStringToNull();
            map = cnHeader.toMapValues();
            
            if(parameter.getData().getHeaderExtendedList() != null
                && !parameter.getData().getHeaderExtendedList().isEmpty()
                && parameter.getData().getHeaderExtendedList().size() > 0)
            {
                for(CnHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendedList())
                {   
                    String fieldType = headerEx.getFieldType();
                    if(fieldType.equals(EXTENDED_TYPE_BOOLEAN))
                    {
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getBoolValue());
                    }
    
                    if(fieldType.equals(EXTENDED_TYPE_FLOAT))
                    {
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getFloatValue());
                    }
    
                    if(fieldType.equals(EXTENDED_TYPE_INTEGER))
                    {
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getIntValue());
                    }
    
                    if(fieldType.equals(EXTENDED_TYPE_STRING))
                    {
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getStringValue());
                    }

                    if(fieldType.equals(EXTENDED_TYPE_DATE))
                    {
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getDateValue());
                    }
                }
            }
            
            BuyerStoreHolder buyerStore = null;
            
            if(null != parameter.getData().getHeader().getStoreCode())
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getHeader().getStoreCode());
            }
            
            if (buyerStore != null)
            {
                map.putAll(buyerStore.toMapValues());
            }
            
            if (parameter.getData().getHeader().getStoreCode() != null)
            {
                map.put("STORE_CODE", parameter.getData().getHeader().getStoreCode());
            }
            
            if (parameter.getData().getHeader().getStoreName() != null)
            {
                map.put("STORE_NAME", parameter.getData().getHeader().getStoreName());
            }
            
            if (parameter.getData().getHeader().getStoreAddr1() != null)
            {
                map.put("STORE_ADDR1", parameter.getData().getHeader().getStoreAddr1());
            }
            
            if (parameter.getData().getHeader().getStoreAddr2() != null)
            {
                map.put("STORE_ADDR2", parameter.getData().getHeader().getStoreAddr2());
            }
            
            if (parameter.getData().getHeader().getStoreAddr3() != null)
            {
                map.put("STORE_ADDR3", parameter.getData().getHeader().getStoreAddr3());
            }
            
            if (parameter.getData().getHeader().getStoreAddr4() != null)
            {
                map.put("STORE_ADDR4", parameter.getData().getHeader().getStoreAddr4());
            }
           
            map.putAll(parameter.getSupplier().toMapValues());
            if(null != parameter.getSupplier().getSupplierSource())
            {
                map.put("SUPPLIER_SOURCE", parameter.getSupplier()
                    .getSupplierSource().toString());
            }
            
            if(null != parameter.getSupplier().getGstRegNo())
            {
                map.put("SUPPLIER_GST_REG_NO", parameter.getSupplier().getGstRegNo());
            }
            
            MsgTransactionsHolder msg = msgTransactionsService.selectByKey(cnHeader.getCnOid());
            if (msg != null)
            {
                map.put("SENT_DATE", msg.getSentDate());
            }
        }
        catch(Exception e)
        {
           ErrorHelper.getInstance().logError(log, e);
        }
        
        return map;
    }

    @Override
    protected String reportTemplate(CnHolder data, int flag)
    {
        return "reports/customized/cn/tangs/CUSTOMIZED_TANGS_CN.jasper";
    }

}
