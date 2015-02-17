//*****************************************************************************
//
// File Name       :  DefaultStandardGrnReportEngine.java
// Date Created    :  2012-12-18
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-18 $
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
import com.pracbiz.b2bportal.core.holder.GrnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardGrnReportEngine extends DefaultGrnReportEngine
    implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardGrnReportEngine.class);
    
    @Autowired BuyerStoreService buyerStoreService;

    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<GrnHolder> parameter, int flag)
    {
        List<GrnDetailHolder> grnDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        for(GrnDetailHolder grnDetail : grnDetails)
        {
            map = grnDetail.toMapValues();
            
            if(parameter.getData().getDetailExtendeds() != null)
            {
                List<GrnDetailExtendedHolder> grnDetailExs = parameter.getData().getDetailExtendeds();
                
                for (GrnDetailExtendedHolder grnDetailEx : grnDetailExs)
                {
                    Integer lineSeq = grnDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(grnDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = grnDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(grnDetailEx.getFieldName().toUpperCase(), grnDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(grnDetailEx.getFieldName().toUpperCase(), grnDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(grnDetailEx.getFieldName().toUpperCase(), grnDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(grnDetailEx.getFieldName().toUpperCase(), grnDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(grnDetailEx.getFieldName().toUpperCase(), grnDetailEx.getDateValue());
                    }

                }
               
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<GrnHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
        
        try
        {   parameter.getData().getHeader().setAllEmptyStringToNull();
            map = parameter.getData().getHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtendeds() != null
                && !parameter.getData().getHeaderExtendeds().isEmpty()
                && parameter.getData().getHeaderExtendeds().size() > 0)
            {
                
                for(GrnHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
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
            
            BuyerStoreHolder buyerStore = null;
            if (parameter.getData().getHeader().getReceiveStoreCode() != null )
            {
                buyerStore = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(parameter.getBuyer()
                    .getBuyerCode(), parameter.getData().getHeader().getReceiveStoreCode());
            }
            
            if (buyerStore != null)
            {
                map.putAll(buyerStore.toMapValues());
            }
          
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        return map;
    }
    
    @Override
    protected String reportTemplate(GrnHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/grn/STANDARD_GRN" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/grn/STANDARD_GRN.jasper";
        }
    }

}
