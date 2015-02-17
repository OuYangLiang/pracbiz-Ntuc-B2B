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

package com.pracbiz.b2bportal.core.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.CountryHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardInvReportEngine extends DefaultInvReportEngine
    implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(DefaultStandardInvReportEngine.class);
    
    @Autowired private BuyerStoreService buyerStoreService;
    @Autowired private CountryService countryService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<InvHolder> parameter, int flag)
    {
        List<InvDetailHolder> invDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for(InvDetailHolder invDetail : invDetails)
        {
            map = invDetail.toMapValues();

            if(parameter.getData().getDetailExtendeds() != null)
            {
                List<InvDetailExtendedHolder> invDetailExs = parameter.getData().getDetailExtendeds();
                
                for (InvDetailExtendedHolder invDetailEx : invDetailExs)
                {
                    Integer lineSeq = invDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(invDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = invDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(invDetailEx.getFieldName().toUpperCase(), invDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(invDetailEx.getFieldName().toUpperCase(), invDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(invDetailEx.getFieldName().toUpperCase(), invDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(invDetailEx.getFieldName().toUpperCase(), invDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(invDetailEx.getFieldName().toUpperCase(), invDetailEx.getDateValue());
                    }
                    
                }
                
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<InvHolder> parameter, int flag)
    {
        InvHeaderHolder invHeader = parameter.getData().getHeader();
        Map<String, Object> map = null;
        try
        {
            invHeader.setAllEmptyStringToNull();
            map = invHeader.toMapValues();
            
            if(parameter.getData().getHeaderExtendeds() != null
                && !parameter.getData().getHeaderExtendeds().isEmpty()
                && parameter.getData().getHeaderExtendeds().size() > 0)
            {
                for(InvHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
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
            
            if(null != parameter.getData().getHeader().getShipToCode())
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getHeader().getShipToCode());
            }
            
            if (buyerStore != null)
            {
                map.putAll(buyerStore.toMapValues());
            }
            
            if (parameter.getData().getHeader().getShipToCode() != null)
            {
                map.put("STORE_CODE", parameter.getData().getHeader().getShipToCode());
            }
            
            if (parameter.getData().getHeader().getShipToName() != null)
            {
                map.put("STORE_NAME", parameter.getData().getHeader().getShipToName());
            }
            
            if (parameter.getData().getHeader().getShipToAddr1() != null)
            {
                map.put("STORE_ADDR1", parameter.getData().getHeader().getShipToAddr1());
            }
            
            if (parameter.getData().getHeader().getShipToAddr2() != null)
            {
                map.put("STORE_ADDR2", parameter.getData().getHeader().getShipToAddr2());
            }
            
            if (parameter.getData().getHeader().getShipToAddr3() != null)
            {
                map.put("STORE_ADDR3", parameter.getData().getHeader().getShipToAddr3());
            }
            
            if (parameter.getData().getHeader().getShipToAddr4() != null)
            {
                map.put("STORE_ADDR4", parameter.getData().getHeader().getShipToAddr4());
            }
           
            map.putAll(parameter.getSupplier().toMapValues());
            if(null != parameter.getSupplier().getSupplierSource())
            {
                map.put("SUPPLIER_SOURCE", parameter.getSupplier()
                    .getSupplierSource().toString());
            }
            
            if(null != parameter.getSupplier().getCtryCode())
            {
                CountryHolder country = countryService.selectByCtryCode(parameter.getSupplier().getCtryCode());
                if (country != null && country.getCtryDesc() != null)
                {
                    map.put("SUPPLIER_CTRY_DESC", country.getCtryDesc());
                }
            }
            
            if(null != parameter.getSupplier().getAddress1())
            {
                map.put("SUPPLIER_ADDR1", parameter.getSupplier().getAddress1());
            }
            
            if(null != parameter.getSupplier().getAddress2())
            {
                map.put("SUPPLIER_ADDR2", parameter.getSupplier().getAddress2());
            }
            
            if(null != parameter.getSupplier().getAddress3())
            {
                map.put("SUPPLIER_ADDR3", parameter.getSupplier().getAddress3());
            }
            
            if(null != parameter.getSupplier().getAddress4())
            {
                map.put("SUPPLIER_ADDR4", parameter.getSupplier().getAddress4());
            }
            
        }
        catch(Exception e)
        {
           ErrorHelper.getInstance().logError(log, e);
        }
        
        return map;
    }

    @Override
    protected String reportTemplate(InvHolder data, int flag)
    {
        return "reports/standard/inv/STANDARD_INV.jasper";
    }

}
