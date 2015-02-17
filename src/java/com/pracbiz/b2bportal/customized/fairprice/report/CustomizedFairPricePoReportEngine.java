//*****************************************************************************
//
// File Name       :  CustomizedFairPricePoReportEngine.java
// Date Created    :  Apr 18, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: Apr 18, 2013 9:29:41 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.fairprice.report;

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
import com.pracbiz.b2bportal.customized.unity.report.CustomizedUnityPoReportEngine;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CustomizedFairPricePoReportEngine extends DefaultPoReportEngine implements CoreCommonConstants
{

    private static final String SUBREPORT_DIR = "SUBREPORT_DIR";
    public static final String JASPER_EXTENSION = ".jasper";
    public static final String STORE_TYPE_STORE = "ST";
    public static final String STORE_TYPE_WAREHOUSE = "WH";
    private static final Logger log = LoggerFactory.getLogger(CustomizedUnityPoReportEngine.class);
    @Autowired BuyerStoreService buyerStoreService;
    @Autowired CountryService countryService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<PoHolder> parameter, int flag)
    {
        List<Map<String, Object>> dataSource = null;
        
        if(flag == 2 || flag == 3)
        {//for supplier user pdf 
            
            dataSource = getSupplierUserPdfDataSource(parameter);
        }
        else
        {//for standard pdf and buyer store user pdf 
            dataSource = getStandardDataSource(parameter);
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
                    map.put(headerEx.getFieldName().toUpperCase(), headerEx.getDateValue().toString());
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
            if(null != parameter.getData().getPoHeader().getShipToCode())
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getPoHeader().getShipToCode());
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
                map.put("SUPPLIER_CONTACT_EMAIL", parameter.getBuyer().getContactEmail());
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
            
            map.put("COMPANY_NAME", parameter.getBuyer().getBuyerName());
            
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
        }
        catch(Exception e)
        {
           ErrorHelper.getInstance().logError(log, e);
        }
        
        if (flag == 1)
        {
            map.put(SUBREPORT_DIR, "reports/customized/po/fairprice/FAIR_PRICE_PO_SUB_REPORT" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + JASPER_EXTENSION);
        }
        else if (flag == 2)
        {
            map.put(SUBREPORT_DIR, "reports/customized/po/fairprice/FAIR_PRICE_PO_SUB_REPORT" + CoreCommonConstants.REPORT_TEMPLATE_FOR_SUPPLIER_EXTENSION + JASPER_EXTENSION);
        }
        else if (flag == 3)
        {
            map.put(SUBREPORT_DIR, "reports/customized/po/fairprice/FAIR_PRICE_PO_SUB_REPORT" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_BY_STORE_EXTENSION + JASPER_EXTENSION);
        }
        else
        {
            map.put(SUBREPORT_DIR, "reports/customized/po/fairprice/FAIR_PRICE_PO_SUB_REPORT.jasper");
        }
        
        return map;
    }

    @Override
    protected String reportTemplate(PoHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/customized/po/fairprice/CUSTOMIZED_FAIR_PRICE_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + JASPER_EXTENSION;
        }
        
        if (flag == 2)
        {
            return "reports/customized/po/fairprice/CUSTOMIZED_FAIR_PRICE_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_SUPPLIER_EXTENSION + JASPER_EXTENSION;
        }
        
        if (flag == 3)
        {
            return "reports/customized/po/fairprice/CUSTOMIZED_FAIR_PRICE_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_BY_STORE_EXTENSION + JASPER_EXTENSION;
        }
        
        return "reports/customized/po/fairprice/CUSTOMIZED_FAIR_PRICE_PO.jasper";
        
    }
    
    //**********************
    //create sub_data_source
    //**********************
    private void getSubDataSource(Map<String, Object> map,
        List<PoLocationDetailHolder> parameter,
        Map<Integer, PoLocationHolder> poLocationMap,String buyerCode)
    {
        List<Map<String, Object>> subDataSource = new ArrayList<Map<String, Object>>();
        Map<String,String> storeTypeMap = getStoreType(buyerCode);
        String key = null;
        try
        {
            for(PoLocationDetailHolder locationDetail : parameter)
            { //corresponding plural subDateSource record
                Map<String, Object> subDataSourceMap = new HashMap<String, Object>();

                subDataSourceMap.put("LOCATION_SHIP_QTY", locationDetail
                    .getLocationShipQty());
                
                if(poLocationMap.get(locationDetail.getLocationLineSeqNo())
                    .getLineSeqNo().compareTo(locationDetail.getLocationLineSeqNo())==0)
                {
                    key = poLocationMap.get(
                        locationDetail.getLocationLineSeqNo())
                        .getLocationCode();
                    
                    subDataSourceMap.put("LOCATION_NAME", poLocationMap.get(
                        locationDetail.getLocationLineSeqNo())
                        .getLocationName());

                    if (storeTypeMap.containsKey(key))
                    {
                        subDataSourceMap.put("LOCATION_CODE", storeTypeMap.get(key) + poLocationMap.get(
                            locationDetail.getLocationLineSeqNo())
                            .getLocationCode());
                    }
                    else
                    {
                        subDataSourceMap.put("LOCATION_CODE", poLocationMap.get(
                            locationDetail.getLocationLineSeqNo())
                            .getLocationCode());
                    }
                    
                }
                else
                {
                    continue;
                }

                subDataSource.add(subDataSourceMap);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        map.put("SUB_DATA_SOURCE", subDataSource);
    }
    
    private List<Map<String, Object>> getStandardDataSource(ReportEngineParameter<PoHolder> parameter)
    {
        //for buyer store pdf  and standard pdf 
        List<PoDetailHolder> poDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<Integer, List<PoLocationDetailHolder>> poLocationDetailMap = new HashMap<Integer, List<PoLocationDetailHolder>>();
        
        for(PoLocationDetailHolder locationDetail : parameter.getData().getLocationDetails())
        {
            if(poLocationDetailMap.containsKey(locationDetail
                .getDetailLineSeqNo()))
            {
                poLocationDetailMap.get(locationDetail.getDetailLineSeqNo())
                    .add(locationDetail);
            }
            else
            {
                List<PoLocationDetailHolder> poLocationDetailList = new ArrayList<PoLocationDetailHolder>();
                poLocationDetailList.add(locationDetail);
                poLocationDetailMap.put(locationDetail.getDetailLineSeqNo(),
                    poLocationDetailList);
            }
        }
        
        
        Map<Integer, PoLocationHolder> poLocationMap = new HashMap<Integer, PoLocationHolder>();
        for(PoLocationHolder poLocation : parameter.getData().getLocations())
        {
           poLocationMap.put(poLocation.getLineSeqNo(), poLocation);
        }
        
        Map<Integer, List<PoDetailExtendedHolder>> detailExtendsMap = null;
        if (parameter.getData().getDetailExtendeds() != null && !parameter.getData().getDetailExtendeds().isEmpty())
        {
            detailExtendsMap = new HashMap<Integer, List<PoDetailExtendedHolder>>();
            for(PoDetailExtendedHolder detailEx: parameter.getData().getDetailExtendeds())
            {
                if(detailExtendsMap.containsKey(detailEx
                    .getLineSeqNo()))
                {
                    detailExtendsMap.get(detailEx.getLineSeqNo())
                        .add(detailEx);
                }
                else
                {
                    List<PoDetailExtendedHolder> detailExtendList = new ArrayList<PoDetailExtendedHolder>();
                    detailExtendList.add(detailEx);
                    detailExtendsMap.put(detailEx.getLineSeqNo(),
                        detailExtendList);
                }
            }
        }
        
        for(PoDetailHolder poDetail : poDetails)
        {
            Map<String, Object> map = poDetail.toMapValues();
           
            if(parameter.getData().getDetailExtendeds() != null
                && !parameter.getData().getDetailExtendeds().isEmpty()
                && detailExtendsMap != null
                && !detailExtendsMap.isEmpty() 
                && detailExtendsMap.containsKey(poDetail.getLineSeqNo()))
            {
                List<PoDetailExtendedHolder> detailExtends = detailExtendsMap.get(poDetail.getLineSeqNo());
                    
                for(PoDetailExtendedHolder detailEx: detailExtends)
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
                    if(fieldType.equals(EXTENDED_TYPE_INTEGER))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getStringValue());
                    }
                    
                    //string date
                    if(fieldType.equals(EXTENDED_TYPE_DATE))
                    {
                        map.put(detailEx.getFieldName().toUpperCase(), detailEx.getDateValue());
                    }
                }
            }
          
            if(poLocationDetailMap.containsKey(poDetail.getLineSeqNo()))
            {
                getSubDataSource(map, poLocationDetailMap.get(poDetail.getLineSeqNo()), poLocationMap, parameter.getBuyer().getBuyerCode());
            }
            //get subDataSource for pdf 
            dataSource.add(map);
        }
    
        return dataSource;
    }
    
    
    private List<Map<String, Object>>  getSupplierUserPdfDataSource(ReportEngineParameter<PoHolder> parameter)
    {
        //for supplier user pdf 
        List<PoDetailHolder> poDetails = parameter.getData().getDetails();
        List<PoLocationHolder> poLocations = parameter.getData().getLocations();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<Integer, List<PoLocationDetailHolder>> poLocationDetailMap = new HashMap<Integer, List<PoLocationDetailHolder>>();
        
        for(PoLocationDetailHolder locationDetail : parameter.getData().getLocationDetails())
        {
            if(poLocationDetailMap.containsKey(locationDetail
                .getLocationLineSeqNo()))
            {
                poLocationDetailMap.get(locationDetail.getLocationLineSeqNo())
                    .add(locationDetail);
            }
            else
            {
                List<PoLocationDetailHolder> poLocationDetailList = new ArrayList<PoLocationDetailHolder>();
                poLocationDetailList.add(locationDetail);
                poLocationDetailMap.put(locationDetail.getLocationLineSeqNo(),
                    poLocationDetailList);
            }
        }
        
        
        Map<Integer, PoDetailHolder> poDetailsMap = new HashMap<Integer, PoDetailHolder>();
        for(PoDetailHolder poDetail : poDetails)
        {
            poDetailsMap.put(poDetail.getLineSeqNo(), poDetail);
        }

        Map<String,String> storeTypeMap = getStoreType(parameter.getBuyer().getBuyerCode());
        String key = null;
        for(PoLocationHolder poLocation : poLocations)
        {
            Map<String, Object> map = poLocation.toMapValues();
            
            key = poLocation.getLocationCode();
            
            if(storeTypeMap.containsKey(key))
            {
                map.put("LOCATION_CODE", storeTypeMap.get(key) + poLocation
                    .getLocationCode());
            }
            else
            {
                map.put("LOCATION_CODE", poLocation.getLocationCode());
            }
            
            if(poLocationDetailMap.containsKey(poLocation.getLineSeqNo()))
            {
                getSupplierUserPdfSubDataSource(map, poLocationDetailMap.get(poLocation.getLineSeqNo()), poDetailsMap);
            }
            //get subDataSource for pdf 
            dataSource.add(map);
        }
    
        return dataSource;
    }
    
    
    //********************************************
    //create sub data source for supplier user pdf
    //********************************************
    private void getSupplierUserPdfSubDataSource(Map<String, Object> map,
        List<PoLocationDetailHolder> parameter,
        Map<Integer, PoDetailHolder> poDetailsMap)
    {
        List<Map<String, Object>> subDataSource = new ArrayList<Map<String, Object>>();
        try
        {
            for(PoLocationDetailHolder locationDetail : parameter)
            {   //corresponding plural subDateSource record
                Map<String, Object> subDataSourceMap = new HashMap<String, Object>();

                subDataSourceMap.put("LOCATION_SHIP_QTY", locationDetail
                    .getLocationShipQty());

                
                PoDetailHolder poDetail = poDetailsMap.get(locationDetail.getDetailLineSeqNo());
                
                subDataSourceMap.putAll(poDetail.toMapValues());
                
                subDataSource.add(subDataSourceMap);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        map.put("SUB_DATA_SOURCE", subDataSource);
    }
    
    
    private Map<String, String> getStoreType(String buyerCode)
    {
        Map<String, String> map = new HashMap<String, String>();
        List<BuyerStoreHolder> buyerStores = null;
        try
        {
            
            buyerStores = buyerStoreService.selectBuyerStoresByBuyer(buyerCode);
            
            
            for(BuyerStoreHolder buyerStore : buyerStores)
            {
                map.put(buyerStore.getStoreCode(), buyerStore.getIsWareHouse() ? STORE_TYPE_WAREHOUSE : STORE_TYPE_STORE);
            }
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
        
        
        
        return map;
    }
}
