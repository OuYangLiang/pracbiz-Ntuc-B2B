//*****************************************************************************
//
// File Name       :  DefaultStandardPO1ReportEngine.java
// Date Created    :  2012-12-17
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-17 $
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


import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DefaultStandardPoReportEngine extends DefaultPoReportEngine
    implements CoreCommonConstants
{
    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<PoHolder> parameter, int flag)
    {
        List<PoDetailHolder> PoDetails = parameter.getData().getDetails();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;

        for(PoDetailHolder poDetail : PoDetails)
        {
            map = poDetail.toMapValues();
           
            if(parameter.getData().getDetailExtendeds() != null)
            {
                for(PoDetailExtendedHolder detailEx: parameter.getData().getDetailExtendeds())
                {
                    if (detailEx.getLineSeqNo().intValue() == poDetail.getLineSeqNo().intValue())
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
                            map.put(detailEx.getFieldName().toUpperCase(), detailEx.getDateValue().toString());
                        }
                    }
                }
            }
            
            dataSource.add(map);
        }

        return dataSource;
    }

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<PoHolder> parameter, int flag)
    {
        String fieldType  = null;
        Map<String, Object> map = parameter.getData().getPoHeader().toMapValues();
        
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
                    }
                }
            }
        }
        map.putAll(parameter.getBuyer().toMapValues());
        if(null != parameter.getSupplier().getSupplierSource())
        {
            map.put("SUPPLIER_SOURCE", parameter.getSupplier().getSupplierSource().toString());
        }
        
        map.put("PO_TYPE", parameter.getData().getPoHeader().getPoType().toString());
        map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());

        return map;
    }

    @Override
    protected String reportTemplate(PoHolder data, int flag)
    {
        //for buyer store user.
        if (flag == 1 || flag == 3)
        {
            if(PoType.SOR.equals(data.getPoHeader().getPoType()))
            {
                return "reports/standard/po/STANDARD_SOR_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
            }
            else
            {
                return "reports/standard/po/STANDARD_CON_PO" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
            }
        }
        else
        {
            if(PoType.SOR.equals(data.getPoHeader().getPoType()))
            {
                return "reports/standard/po/STANDARD_SOR_PO.jasper";
            }
            else
            {
                return "reports/standard/po/STANDARD_CON_PO.jasper";
            }
        }
        
    }

}
