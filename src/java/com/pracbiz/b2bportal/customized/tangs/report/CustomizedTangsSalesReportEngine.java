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

package com.pracbiz.b2bportal.customized.tangs.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.report.DefaultSalesReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author yinchi
 */
public class CustomizedTangsSalesReportEngine extends DefaultSalesReportEngine implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(CustomizedTangsSalesReportEngine.class);
    @Autowired BuyerStoreService buyerStoreService;
    @Autowired CountryService countryService;
    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<SalesHolder> parameter, int flag)
    {

        Map<String, Object> map = null;
        
        try
        {   
            parameter.getData().getSalesHeader().setAllEmptyStringToNull();
            map = parameter.getData().getSalesHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtendeds() != null
                && !parameter.getData().getHeaderExtendeds().isEmpty()
                && parameter.getData().getHeaderExtendeds().size() > 0)
            {
                
                for(SalesHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
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
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
            
            if(null != parameter.getSupplier().getContactTel())
            {
                map.put("SUPPLIER_CONTACT_TEL", parameter.getSupplier().getContactTel());
            }
            
            if(null != parameter.getSupplier().getContactFax())
            {
                map.put("SUPPLIER_CONTACT_FAX", parameter.getSupplier().getContactFax());
            }
            
            if(null != parameter.getBuyer().getGstRegNo())
            {
                map.put("BUYER_GST_REG_NO", parameter.getBuyer().getGstRegNo());
            }
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }
    
        return map;
    
    }
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<SalesHolder> parameter, int flag)
    {

        List<SalesDetailHolder> salesDetails = parameter.getData().getDetails();
        List<SalesDateLocationDetailHolder> locationDetails = parameter.getData().getSalesDateLocationDetail();
        List<SalesDateHolder> salesDates = parameter.getData().getSalesDates();
        List<SalesLocationHolder> salesLocs = parameter.getData().getLocations();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for (SalesDateLocationDetailHolder locDetail : locationDetails)
        {
            map = new HashMap<String, Object>();
            for (SalesDetailHolder salesDetail : salesDetails)
            {
                if (locDetail.getDetailLineSeqNo().equals(salesDetail.getLineSeqNo()))
                {
                    map.putAll(salesDetail.toMapValues());
                    
                    Date receiptDate = salesDetail.getReceiptDate();
                    if (null != receiptDate)
                    {
                        map.put("RECEIPT_DATE", DateUtil.getInstance().convertDateToString(receiptDate, DateUtil.DATE_FORMAT_MMDDYYYYHHMMSS).split(" ")[1]);
                    }
                }
            }
            
            for (SalesDateHolder salesDate : salesDates)
            {
                if (locDetail.getDateLineSeqNo().equals(salesDate.getLineSeqNo()))
                {
                    map.put("SALES_DATE", salesDate.getSalesDate());
                }
            }
            
            for (SalesLocationHolder salesLoc : salesLocs)
            {
                if (locDetail.getLocationLineSeqNo().equals(salesLoc.getLineSeqNo()))
                {
                    map.putAll(salesLoc.toMapValues());
                }
            }
            map.putAll(locDetail.toMapValues());
            
            dataSource.add(map);
        }
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m2.get("BUYER_ITEM_CODE")).compareTo((String)m1.get("BUYER_ITEM_CODE"));
            }
        });
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m2.get("LOCATION_CODE")).compareTo((String)m1.get("LOCATION_CODE"));
            }
        });
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((Date)m1.get("SALES_DATE")).before((Date)m2.get("SALES_DATE")) ? -1 : 1;
            }
        });
    
        return dataSource;
    
    }
    @Override
    protected String reportTemplate(SalesHolder data, int isForStore)
    {
        return "reports/customized/sales/tangs/SALES_DATA.jasper";
    }
    
    

}
