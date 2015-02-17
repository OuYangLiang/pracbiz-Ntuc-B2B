//*****************************************************************************
//
// File Name       :  DefaultStandardGiReportEngine.java
// Date Created    :  2013-11-14
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-11-14 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
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
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardGiReportEngine extends DefaultGiReportEngine
    implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardGiReportEngine.class);

    @Autowired BuyerStoreService buyerStoreService;
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<GiHolder> parameter, int flag)
    {
        List<GiDetailHolder> giDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        for(GiDetailHolder giDetail : giDetails)
        {
            map = giDetail.toMapValues();
            
            if(parameter.getData().getDetailExtended() != null)
            {
                List<GiDetailExtendedHolder> giDetailExs = parameter.getData().getDetailExtended();
                
                for (GiDetailExtendedHolder giDetailEx : giDetailExs)
                {
                    Integer lineSeq = giDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(giDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = giDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(giDetailEx.getFieldName().toUpperCase(), giDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(giDetailEx.getFieldName().toUpperCase(), giDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(giDetailEx.getFieldName().toUpperCase(), giDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(giDetailEx.getFieldName().toUpperCase(), giDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(giDetailEx.getFieldName().toUpperCase(), giDetailEx.getDateValue());
                    }

                }
               
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<GiHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
        
        try
        {   parameter.getData().getGiHeader().setAllEmptyStringToNull();
            map = parameter.getData().getGiHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtended() != null
                && !parameter.getData().getHeaderExtended().isEmpty()
                && parameter.getData().getHeaderExtended().size() > 0)
            {
                
                for(GiHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtended())
                {
                    String fieldType = headerEx.getFiledType();
                    
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
            if(null != parameter.getData().getGiHeader().getIssuingStoreCode())
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getGiHeader().getIssuingStoreCode());
            }
            if (buyerStore != null)
            {
                map.putAll(buyerStore.toMapValues());
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
    protected String reportTemplate(GiHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/gi/STANDARD_GI" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/gi/STANDARD_GI.jasper";
        }
    }

}
