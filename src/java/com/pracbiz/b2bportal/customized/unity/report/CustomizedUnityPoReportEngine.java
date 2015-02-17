//*****************************************************************************
//
// File Name       :  UnityPoReportEngine.java
// Date Created    :  2013-1-6
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-1-6 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.unity.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.report.DefaultPoReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CustomizedUnityPoReportEngine extends DefaultPoReportEngine implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CustomizedUnityPoReportEngine.class);
    @Autowired BuyerStoreService buyerStoreService;
    @Autowired CountryService countryService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<PoHolder> parameter, int flag)
    {
        List<PoDetailHolder> poDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        Map<String, Object> copyMap = null;
        
        Map<String, PoLocationHolder> poLocMap = new HashMap<String, PoLocationHolder>();
        Map<String, List<PoLocationDetailHolder>> poLocDetailMap = new HashMap<String, List<PoLocationDetailHolder>>();
        String key = null;
        
        List<PoLocationDetailHolder> poLocDetails = parameter.getData().getLocationDetails();
        //group PoLocationDetail as lineSeqNum and poOid
        for(PoLocationDetailHolder locDetail : poLocDetails)
        {
            key = locDetail.getPoOid() + "@@" + locDetail.getDetailLineSeqNo();
            if (poLocDetailMap.containsKey(key))
            {
                poLocDetailMap.get(locDetail.getPoOid() + "@@" + locDetail.getDetailLineSeqNo()).add(locDetail);
            }
            else
            {
                List<PoLocationDetailHolder> lst = new ArrayList<PoLocationDetailHolder>();
                lst.add(locDetail);
                
                poLocDetailMap.put(locDetail.getPoOid() + "@@" + locDetail.getDetailLineSeqNo(), lst);
            }
        }
        
        //group PoLocation as line_seq_num and po_oid 
        List<PoLocationHolder> poLocs = parameter.getData().getLocations();
        for(PoLocationHolder poLocation : poLocs)
        {
            poLocMap.put(poLocation.getPoOid() + "@@" + poLocation.getLineSeqNo(), poLocation);
        }
        
        
        for(PoDetailHolder poDetail : poDetails)
        {
            map = poDetail.toMapValues();
            
            if(parameter.getData().getDetailExtendeds() != null)
            {
                for(PoDetailExtendedHolder detailEx: parameter.getData().getDetailExtendeds())
                {
                    String fieldType = detailEx.getFieldType();
                    //boolean value
                    if(fieldType.equals(EXTENDED_TYPE_BOOLEAN))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(),detailEx.getBoolValue());
                    }

                    //float value
                    if(fieldType.equals(EXTENDED_TYPE_FLOAT))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getFloatValue());
                    }

                    //integer value
                    if(fieldType.equals(EXTENDED_TYPE_INTEGER))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getIntValue());
                    }

                    //string value
                    if(fieldType.equals(EXTENDED_TYPE_STRING))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getStringValue());
                    }
                    
                    //date value
                    if(fieldType.equals(EXTENDED_TYPE_DATE))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getDateValue());
                    }
                }
            }
            
            key = poDetail.getPoOid() + "@@" + poDetail.getLineSeqNo();
            if(poLocDetailMap.containsKey(key))
            {
                List<PoLocationDetailHolder> locDetails = poLocDetailMap.get(key);
                
                for(PoLocationDetailHolder locDetail : locDetails)
                {
                    copyMap = new HashMap<String, Object>();
                    copyMap.putAll(map);
                    key = locDetail.getPoOid() + "@@" + locDetail.getLocationLineSeqNo();
                    
                    copyMap.put("LOCATION_CODE", poLocMap.get(key).getLocationCode());
                    copyMap.put("ORDER_QTY", locDetail.getLocationShipQty());
                    copyMap.put("FOC_QTY", locDetail.getLocationFocQty());
                    
                    dataSource.add(copyMap);
                }
            }
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<PoHolder> parameter, int flag)
    {
        String fieldType  = null;
        Map<String, Object> map = parameter.getData().getPoHeader().toMapValues();
        map.putAll(parameter.getBuyer().toMapValues());
        
        if(parameter.getData().getHeaderExtendeds() != null
            && !parameter.getData().getHeaderExtendeds().isEmpty()
            && parameter.getData().getHeaderExtendeds().size() > 0)
        {
            for(PoHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
            {
                fieldType = headerEx.getFieldType();
                
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
        
        for(PoLocationHolder location: parameter.getData().getLocations())
        {
            map.putAll(location.toMapValues());
            
            for(PoLocationDetailHolder locationDetail: parameter.getData().getLocationDetails())
            {
                locationDetail.toMapValues();
                
                if(parameter.getData().getPoLocDetailExtendeds() != null  )
                {
                    for(PoLocationDetailExtendedHolder poLocDetailExtended: parameter.getData().getPoLocDetailExtendeds())
                    {
                        fieldType = poLocDetailExtended.getFieldType();

                        if(fieldType.equals(EXTENDED_TYPE_BOOLEAN))
                        {
                            map.put(poLocDetailExtended.getFieldName(),
                                poLocDetailExtended.getBoolValue());
                        }
                        if(fieldType.equals(EXTENDED_TYPE_FLOAT))
                        {
                            map.put(poLocDetailExtended.getFieldName(),
                                poLocDetailExtended.getFloatValue());
                        }
                        if(fieldType.equals(EXTENDED_TYPE_INTEGER))
                        {
                            map.put(poLocDetailExtended.getFieldName(),
                                poLocDetailExtended.getIntValue());
                        }
                        if(fieldType.equals(EXTENDED_TYPE_STRING))
                        {
                            map.put(poLocDetailExtended.getFieldName(),
                                poLocDetailExtended.getStringValue());
                        }
                        if(fieldType.equals(EXTENDED_TYPE_DATE))
                        {
                            map.put(poLocDetailExtended.getFieldName(),
                                poLocDetailExtended.getDateValue());
                        }
                    }
                }
            }
        }
        
        
        BuyerStoreHolder buyerStore = null;
        CountryHolder buyerCtry = null;
        CountryHolder supplierCtry = null;
        try
        {
            if (parameter.getData().getLocations().size() > 1)
            {
                map.put("STORE_ADDR1", "Refer to location code below");
            }
            else
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(),  parameter.getData().getLocations().get(0).getLocationCode());
            }
        
            if(null != parameter.getBuyer().getContactTel())
            {
                map.put("BUYER_CONTACT_TEL", parameter.getBuyer().getContactTel());
            }
            
            if(null != parameter.getBuyer().getContactFax())
            {
                map.put("BUYER_CONTACT_FAX", parameter.getBuyer().getContactFax());
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
            
            if(null != parameter.getData().getPoHeader().getBuyerCtryCode())
            {
                buyerCtry = countryService.selectByCtryCode(parameter
                    .getData().getPoHeader().getBuyerCtryCode());
            }
            
            if(null != parameter.getData().getPoHeader().getSupplierCtryCode())
            {
                supplierCtry = countryService.selectByCtryCode(parameter
                    .getData().getPoHeader().getSupplierCtryCode());
            }
            
            if(null != buyerStore)
            {
                map.putAll(buyerStore.toMapValues());
            }
            
            if(null != buyerCtry)
            {
                map.put("BUYER_CTRY_DESC",buyerCtry.getCtryDesc());
            }
            
            if(null != supplierCtry)
            {
                map.put("SUPPLIER_CTRY_DESC",buyerCtry.getCtryDesc());
            }
            
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
        }
        catch(Exception e)
        {
           ErrorHelper.getInstance().logError(log, e);
        }
        return map;
    }

    @Override
    protected String reportTemplate(PoHolder data, int flag)
    {
        if (flag == 1 || flag == 3)
        {
            return "reports/customized/po/unity/CUSTOMIZED_UNITY_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        return "reports/customized/po/unity/CUSTOMIZED_UNITY_PO.jasper";
        
    }

}
