//*****************************************************************************
//
// File Name       :  CustomizedUnityPnReportEngine.java
// Date Created    :  May 31, 2013
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: May 31, 2013 11:53:49 AM $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.unity.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.report.DefaultPnReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultStandardPnReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CustomizedUnityPnReportEngine extends DefaultPnReportEngine implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(DefaultStandardPnReportEngine.class);

    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<PnHolder> parameter, int flag)
    {
        List<PnDetailHolder> pnDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;


        for(PnDetailHolder pnDetail : pnDetails)
        {
            map = pnDetail.toMapValues();

            if(parameter.getData().getDetailExtendeds() != null)
            {
                List<PnDetailExtendedHolder> pnDetailExs = parameter.getData().getDetailExtendeds();
                
                for (PnDetailExtendedHolder pnDetailEx : pnDetailExs)
                {
                    Integer lineSeq = pnDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(pnDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = pnDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(pnDetailEx.getFieldName().toUpperCase(), pnDetailEx.getBoolValue());
                    }

                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(pnDetailEx.getFieldName().toUpperCase(), pnDetailEx.getFloatValue());
                    }

                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(pnDetailEx.getFieldName().toUpperCase(), pnDetailEx.getIntValue());
                    }

                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(pnDetailEx.getFieldName().toUpperCase(), pnDetailEx.getStringValue());
                    }

                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(pnDetailEx.getFieldName().toUpperCase(), pnDetailEx.getDateValue());
                    }
                }
            }
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<PnHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
       
        try
        {
            parameter.getData().getPnHeader().setAllEmptyStringToNull();
            map = parameter.getData().getPnHeader().toMapValues();
    
            if(parameter.getData().getHeaderExtendeds() != null
                && !parameter.getData().getHeaderExtendeds().isEmpty()
                && parameter.getData().getHeaderExtendeds().size() > 0)
            {
                for(PnHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
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
            
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
        }

        return map;
    }

    @Override
    protected String reportTemplate(PnHolder data, int flag)
    {
        return "reports/customized/pn/unity/CUSTOMIZED_UNITY_PN.jasper";
    }


}
