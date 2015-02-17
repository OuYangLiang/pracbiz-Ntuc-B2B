//*****************************************************************************
//
// File Name       :  CustomizedUnityRtvReportEngine.java
// Date Created    :  2013-6-5
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-6-5 下午02:48:48 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.unity.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.report.DefaultRtvReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultStandardRtvReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CustomizedUnityRtvReportEngine extends DefaultRtvReportEngine implements CoreCommonConstants,ApplicationContextAware
{
    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardRtvReportEngine.class);
    
    transient private ApplicationContext ctx;
    private BuyerStoreService buyerStoreService;
    

    @Override
    public void setApplicationContext(ApplicationContext ctx)
        throws BeansException
    {
        this.ctx = ctx;
    }

    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<RtvHolder> parameter, int flag)
    {
        List<RtvDetailHolder> rtvDetails = parameter.getData().getRtvDetail();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        Map<String, List<RtvLocationDetailHolder>> locDetailMap = new HashMap<String, List<RtvLocationDetailHolder>>();
        
        Map<String, RtvLocationHolder> rtvLocMap = new HashMap<String, RtvLocationHolder>();
        
        List<RtvLocationDetailHolder> rtvLocationDetails = parameter.getData().getLocationDetails();
        
        List<RtvLocationHolder> rtvLocs = parameter.getData().getLocations();

        if(null != rtvLocationDetails && !rtvLocationDetails.isEmpty())
        {
            for (RtvLocationDetailHolder rtvLocDetail : rtvLocationDetails)
            {
                String key = rtvLocDetail.getDetailLineSeqNo() +"@@"+rtvLocDetail.getRtvOid();
                if (locDetailMap.containsKey(key))
                {
                    locDetailMap.get(key).add(rtvLocDetail);
                }
                else
                {
                    List<RtvLocationDetailHolder> lst = new ArrayList<RtvLocationDetailHolder>();
                    lst.add(rtvLocDetail);
                    locDetailMap.put(key, lst);
                }
            }
        }
        
        if(null != rtvLocs && !rtvLocs.isEmpty())
        {
            for (RtvLocationHolder rtvLoc : rtvLocs)
            {
                String key = rtvLoc.getLineSeqNo() +"@@"+rtvLoc.getRtvOid();
                rtvLocMap.put(key, rtvLoc);
            }
        }
        
        for(RtvDetailHolder rtvDetail : rtvDetails)
        {
            String key = rtvDetail.getLineSeqNo() + "@@" + rtvDetail.getRtvOid();
            
            map = rtvDetail.toMapValues();
            
            BigDecimal rtvQty = rtvDetail.getReturnQty();
            
            if(parameter.getData().getDetailExtended() != null)
            {
                List<RtvDetailExtendedHolder> rtvDetailExs = parameter.getData().getDetailExtended();
                for (RtvDetailExtendedHolder rtvDetailEx : rtvDetailExs)
                {
                    Integer lineSeq = rtvDetailEx.getLineSeqNo();
                    if (!lineSeq.equals(rtvDetail.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = rtvDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(fieldType))
                    {
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getDateValue());
                    }
                }
            }
            Map<String, Object> newMap = null;
            if (locDetailMap.containsKey(key))
            {
                BigDecimal rtvStoreQty = BigDecimal.ZERO;
                for (RtvLocationDetailHolder rtvLocDetail : locDetailMap.get(key))
                {
                    newMap = new HashMap<String, Object>();
                    newMap.putAll(map);
                    newMap.put("RETURN_QTY", rtvLocDetail.getLocationShipQty());
                    
                    newMap.put("LOCATION", rtvLocMap.get(rtvLocDetail.getLocationLineSeqNo() +"@@"+ rtvLocDetail.getRtvOid()).getLocationCode());
                    
                    rtvStoreQty = rtvStoreQty.add(rtvLocDetail.getLocationShipQty());
                    
                    dataSource.add(newMap);
                }
                
                if (rtvQty.subtract(rtvStoreQty).compareTo(BigDecimal.ZERO) > 0)
                {
                    newMap = new HashMap<String, Object>();
                    newMap.putAll(map);
                    newMap.put("RETURN_QTY", rtvQty.subtract(rtvStoreQty));
                    dataSource.add(newMap);
                }
            }
            else
            {
                dataSource.add(map);
            }
        }
        
       
        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<RtvHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
        
        try
        {
            parameter.getData().getRtvHeader().setAllEmptyStringToNull();
            map = parameter.getData().getRtvHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtended() != null
                && !parameter.getData().getHeaderExtended().isEmpty()
                && parameter.getData().getHeaderExtended().size() > 0)
            {
                for(RtvHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtended())
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
            
            map.putAll(parameter.getBuyer().toMapValues());
            
            if(null != parameter.getBuyer().getContactTel())
            {
                map.put("BUYER_CONTACT_TEL", parameter.getBuyer().getContactTel());
            }
            
            if(null != parameter.getBuyer().getContactFax())
            {
                map.put("BUYER_CONTACT_FAX", parameter.getBuyer().getContactFax());
            }
            
            if(null != parameter.getBuyer().getRegNo())
            {
                map.put("BUYER_REG_NO", parameter.getBuyer().getRegNo());
            }
            
            if(null != parameter.getBuyer().getGstRegNo())
            {
                map.put("BUYER_GST_REG_NO", parameter.getBuyer().getGstRegNo());
            }
            
            if(null != parameter.getSupplier().getContactTel())
            {
                map.put("SUPPLIER_CONTACT_TEL", parameter.getSupplier().getContactTel());
            }
            
            if(null != parameter.getSupplier().getContactFax())
            {
                map.put("SUPPLIER_CONTACT_FAX", parameter.getSupplier().getContactFax());
            }
            
            if(null != parameter.getSupplier().getContactEmail())
            {
                map.put("SUPPLIER_CONTACT_EMAIL", parameter.getSupplier().getContactEmail());
            }
            
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
        
            String rtvNo = parameter.getData().getRtvHeader().getRtvNo();
            
            String[] rtvNoSplits = rtvNo.split("/");
        
            if (rtvNoSplits.length == 4)
            {
                buyerStoreService = (BuyerStoreService)ctx.getBean("buyerStoreService");
                BuyerStoreHolder buyerStore = buyerStoreService
                    .selectBuyerStoreByBuyerCodeAndStoreCode(parameter
                        .getBuyer().getBuyerCode(), rtvNoSplits[1]);

                if(buyerStore != null)
                {
                    map.putAll(buyerStore.toMapValues());
                }
            }
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        
        return map;
    }

    @Override
    protected String reportTemplate(RtvHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/customized/rtv/unity/CUSTOMIZED_UNITY_RTV" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/customized/rtv/unity/CUSTOMIZED_UNITY_RTV.jasper";
        }
    }

}
