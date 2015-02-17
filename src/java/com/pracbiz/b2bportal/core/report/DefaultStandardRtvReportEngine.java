//*****************************************************************************
//
// File Name       :  DefaultStandardRTVReportEngine.java
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

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardRtvReportEngine extends DefaultRtvReportEngine
    implements CoreCommonConstants
{

    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardRtvReportEngine.class);

    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<RtvHolder> parameter, int flag)
    {
        List<RtvDetailHolder> rtvDetails = parameter.getData().getRtvDetail();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for(RtvDetailHolder rtvDetail : rtvDetails)
        {
            map = rtvDetail.toMapValues();

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
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getDateValue().toString());
                    }
                }
            }
            
            dataSource.add(map);
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
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getDateValue().toString());
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
    protected String reportTemplate(RtvHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/standard/rtv/STANDARD_RTV" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/standard/rtv/STANDARD_RTV.jasper";
        }
    }

}
