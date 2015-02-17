//*****************************************************************************
//
// File Name       :  DefaultStandardDNReportEngine.java
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;


import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class DefaultStandardDnReportEngine extends DefaultDnReportEngine
    implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardDnReportEngine.class);
    
    @Autowired BuyerStoreService buyerStoreService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<DnHolder> parameter, int flag)
    {
        List<DnDetailExHolder> dnDetails = parameter.getData().getDnDetail();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for(DnDetailExHolder dnDetail : dnDetails)
        {
            DnDetailHolder dnDet = new DnDetailHolder();
            BeanUtils.copyProperties(dnDetail, dnDet);
            map = dnDet.toMapValues();

            if(parameter.getData().getDetailExtended() != null)
            {
                List<DnDetailExtendedHolder> dnDetailExs = parameter.getData().getDetailExtended();
                
                for (DnDetailExtendedHolder dnDetailEx : dnDetailExs)
                {
                    Integer lineSeq = dnDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(dnDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = dnDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(dnDetailEx.getFieldName().toUpperCase(), dnDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(dnDetailEx.getFieldName().toUpperCase(), dnDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(dnDetailEx.getFieldName().toUpperCase(), dnDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(dnDetailEx.getFieldName().toUpperCase(), dnDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(dnDetailEx.getFieldName().toUpperCase(), dnDetailEx.getDateValue());
                    }

                }
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<DnHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
        
        try
        {
            parameter.getData().getDnHeader().setAllEmptyStringToNull();
            map = parameter.getData().getDnHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtended() != null
                && !parameter.getData().getHeaderExtended().isEmpty()
                && parameter.getData().getHeaderExtended().size() > 0)
            {
                for(DnHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtended())
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
            if (parameter.getData().getDnHeader().getStoreCode() != null)
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getDnHeader().getStoreCode());
            }
            
            if (buyerStore != null)
            {
                map.putAll(buyerStore.toMapValues());
            }
            
            if (parameter.getData().getDnHeader().getStoreName() != null && parameter.getData().getDnHeader().getStoreName().isEmpty())
            {
                map.put("STORE_NAME", parameter.getData().getDnHeader().getStoreName());
            }
            
            if (parameter.getData().getDnHeader().getStoreAddr1() != null && parameter.getData().getDnHeader().getStoreAddr1().isEmpty())
            {
                map.put("STORE_ADDR1", parameter.getData().getDnHeader().getStoreAddr1());
            }
            
            if (parameter.getData().getDnHeader().getStoreAddr2() != null && parameter.getData().getDnHeader().getStoreAddr2().isEmpty())
            {
                map.put("STORE_ADDR2", parameter.getData().getDnHeader().getStoreAddr2());
            }
            
            if (parameter.getData().getDnHeader().getStoreAddr3() != null && parameter.getData().getDnHeader().getStoreAddr3().isEmpty())
            {
                map.put("STORE_ADDR3", parameter.getData().getDnHeader().getStoreAddr3());
            }
            
            if (parameter.getData().getDnHeader().getStoreAddr4() != null && parameter.getData().getDnHeader().getStoreAddr4().isEmpty())
            {
                map.put("STORE_ADDR4", parameter.getData().getDnHeader().getStoreAddr4());
            }
            
            map.putAll(parameter.getBuyer().toMapValues());
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        return map;
    }

    @Override
    protected String reportTemplate(DnHolder data, int flag)
    {
        if(DnType.CST_CR.name().equals(data.getDnHeader().getDnType())
                || DnType.CST_IOC.name().equals(data.getDnHeader().getDnType()))
        {
            return "reports/standard/dn/STANDARD_COST_DN.jasper";
        }
        else
        {   //will redistribution pdf template according diffrent dn type.
            if (flag == 3)
            {
                return "reports/standard/dn/STANDARD_STOCK_DN" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION+ ".jasper";
            }
            
            return "reports/standard/dn/STANDARD_STOCK_DN.jasper";
        }
    }
}
