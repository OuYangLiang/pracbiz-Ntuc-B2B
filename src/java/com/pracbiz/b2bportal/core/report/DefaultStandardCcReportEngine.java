//*****************************************************************************
//
// File Name       :  DefaultStandardCcReportEngine.java
// Date Created    :  2013-12-25
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-12-25 $
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


import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class DefaultStandardCcReportEngine extends DefaultCcReportEngine implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory.getLogger(DefaultStandardGrnReportEngine.class);
    
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<CcHolder> parameter, int flag)
    {
        List<CcDetailHolder> ccDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
    
        for(CcDetailHolder ccDetail : ccDetails)
        {
            map = ccDetail.toMapValues();
            
            if(parameter.getData().getDetailExtendeds() != null)
            {
                List<CcDetailExtendedHolder> ccDetailExs = parameter.getData().getDetailExtendeds();
                
                for (CcDetailExtendedHolder ccDetailEx : ccDetailExs)
                {
                    Integer lineSeq = ccDetailEx.getLineSeqNo();
                    
                    if (!lineSeq.equals(ccDetailEx.getLineSeqNo()))
                    {
                        continue;
                    }
                    
                    String fieldType = ccDetailEx.getFieldType();
                    //boolean value
                    if(EXTENDED_TYPE_BOOLEAN.equals(
                        fieldType))
                    {
                        map.put(ccDetailEx.getFieldName().toUpperCase(), ccDetailEx.getBoolValue());
                    }
    
                    //float value
                    if(EXTENDED_TYPE_FLOAT.equals(fieldType))
                    {
                        map.put(ccDetailEx.getFieldName().toUpperCase(), ccDetailEx.getFloatValue());
                    }
    
                    //integer value
                    if(EXTENDED_TYPE_INTEGER.equals(fieldType))
                    {
                        map.put(ccDetailEx.getFieldName().toUpperCase(), ccDetailEx.getIntValue());
                    }
    
                    //string value
                    if(EXTENDED_TYPE_STRING.equals(fieldType))
                    {
                        map.put(ccDetailEx.getFieldName().toUpperCase(), ccDetailEx.getStringValue());
                    }
                    
                    //date value
                    if(EXTENDED_TYPE_DATE.equals(fieldType))
                    {
                        map.put(ccDetailEx.getFieldName().toUpperCase(), ccDetailEx.getDateValue());
                    }
    
                }
               
            }
            
            dataSource.add(map);
        }
    
        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<CcHolder> parameter, int flag)
    {
        Map<String, Object> map = null;
        
        try
        {   parameter.getData().getCcHeader().setAllEmptyStringToNull();
            map = parameter.getData().getCcHeader().toMapValues();
            
            if(parameter.getData().getHeaderExtendeds() != null
                && !parameter.getData().getHeaderExtendeds().isEmpty()
                && parameter.getData().getHeaderExtendeds().size() > 0)
            {
                
                for(CcHeaderExtendedHolder headerEx: parameter.getData().getHeaderExtendeds())
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
    protected String reportTemplate(CcHolder data, int flag)
    {
        if (flag == 1)
        {
            return "reports/customized/cc/unity/CUSTOMIZED_UNITY_CC" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/customized/cc/unity/CUSTOMIZED_UNITY_CC.jasper";
        }
    }

}
