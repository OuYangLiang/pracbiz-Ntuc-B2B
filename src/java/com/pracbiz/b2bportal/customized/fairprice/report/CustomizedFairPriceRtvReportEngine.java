//*****************************************************************************
//
// File Name       :  CustomizedUnityRtvReportEngine.java
// Date Created    :  2013-11-26
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2013-11-26 11:48:48 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.customized.fairprice.report;

import java.math.BigDecimal;
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
import com.pracbiz.b2bportal.core.holder.ItemHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationHolder;
import com.pracbiz.b2bportal.core.report.DefaultRtvReportEngine;
import com.pracbiz.b2bportal.core.report.DefaultStandardRtvReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.CountryService;
import com.pracbiz.b2bportal.core.service.ItemService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class CustomizedFairPriceRtvReportEngine extends DefaultRtvReportEngine implements CoreCommonConstants
{
    protected static final Logger log = LoggerFactory
        .getLogger(DefaultStandardRtvReportEngine.class);
    
    private static final String LINE_WRAP = "\r\n";
    
    @Autowired private BuyerStoreService buyerStoreService;
    @Autowired private ItemService itemService;
    @Autowired private CountryService countryService;


    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<RtvHolder> parameter, int flag)
    {
        List<RtvDetailHolder> rtvDetails = parameter.getData().getRtvDetail();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        Map<String, List<RtvLocationDetailHolder>> locDetailMap = new HashMap<String, List<RtvLocationDetailHolder>>();
        
        Map<String, RtvLocationHolder> rtvLocMap = new HashMap<String, RtvLocationHolder>();
        
        List<RtvLocationDetailHolder> rtvLocationDetails = parameter.getData().getLocationDetails();
        
        List<RtvLocationHolder> rtvLocs = parameter.getData().getLocations();

        if(null != rtvLocationDetails && !rtvLocationDetails.isEmpty())
        {
            for (RtvLocationDetailHolder rtvLocDetail : rtvLocationDetails)
            {
                String key = rtvLocDetail.getDetailLineSeqNo() +"@@"+rtvLocDetail.getRtvOid();
                if (locDetailMap.containsKey(key))
                {
                    locDetailMap.get(key).add(rtvLocDetail);
                }
                else
                {
                    List<RtvLocationDetailHolder> lst = new ArrayList<RtvLocationDetailHolder>();
                    lst.add(rtvLocDetail);
                    locDetailMap.put(key, lst);
                }
            }
        }
        
        if(null != rtvLocs && !rtvLocs.isEmpty())
        {
            for (RtvLocationHolder rtvLoc : rtvLocs)
            {
                String key = rtvLoc.getLineSeqNo() +"@@"+rtvLoc.getRtvOid();
                rtvLocMap.put(key, rtvLoc);
            }
        }
        
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
                        map.put(rtvDetailEx.getFieldName().toUpperCase(), rtvDetailEx.getDateValue());
                    }
                }
            }
            
            try
            {
                ItemHolder item = itemService
                    .selectItemByBuyerOidAndBuyerItemCode(parameter.getBuyer().getBuyerOid(), rtvDetail.getBuyerItemCode());
                
                if (item != null)
                {
                    map.put("CLASS_CODE", item.getClassCode());
                }
            }
            catch(Exception e)
            {
                ErrorHelper.getInstance().logError(log, e);
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
        Map<String, BigDecimal> orderUnitMap = new HashMap<String, BigDecimal>();
        
        try
        {
            parameter.getData().getRtvHeader().setAllEmptyStringToNull();
            map = parameter.getData().getRtvHeader().toMapValues();
            
            for(RtvDetailHolder rtvDetail : parameter.getData().getRtvDetail())
            {
                String key = rtvDetail.getReturnUom();
                if (orderUnitMap.containsKey(key))
                {
                    orderUnitMap.put(key, orderUnitMap.get(key).add(rtvDetail.getReturnQty()));
                }
                else
                {
                    orderUnitMap.put(key, rtvDetail.getReturnQty());
                }
            }
            
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
                        map.put(headerEx.getFieldName().toUpperCase(), headerEx.getDateValue());
                    }
                }
            }
           
            this.initBuyerParameter(parameter, map);
            this.initSupplierParameter(parameter, map);
            map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
            if (parameter.getData().getLocations() != null && !parameter.getData().getLocations().isEmpty())
            {
              String locationCode = parameter.getData().getLocations().get(0).getLocationCode();
                
              BuyerStoreHolder buyerStore = buyerStoreService
                  .selectBuyerStoreByBuyerCodeAndStoreCode(parameter
                      .getBuyer().getBuyerCode(), locationCode);

              if(buyerStore != null)
              {
                  map.putAll(buyerStore.toMapValues());
                  if (buyerStore.getStoreCtryCode() != null)
                  {
                      CountryHolder country = countryService.selectByCtryCode(buyerStore.getStoreCtryCode());
                      if (country.getCtryDesc() != null)
                      {
                          map.put("STORE_CTRY_CODE", country.getCtryDesc());
                      }
                  }
              }
            }
            StringBuffer paramName = new StringBuffer();
            StringBuffer paramValue = new StringBuffer();
            
            if (!orderUnitMap.isEmpty())
            {
                for(Map.Entry<String, BigDecimal> entry : orderUnitMap.entrySet())
                {
                    paramName = paramName .append("Total ").append(entry.getKey()).append( ": " + LINE_WRAP);
                     
                    paramValue= paramValue.append(entry.getValue().setScale(2, BigDecimal.ROUND_HALF_UP)).append(LINE_WRAP);
                }
            }
            
            map.put("TOTAL_ORDER_UNIT_NAME", paramName.substring(0, paramName.lastIndexOf(LINE_WRAP)));
            map.put("TOTAL_ORDER_UNIT_VALUE", paramValue.substring(0, paramValue.lastIndexOf(LINE_WRAP)));
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
            return "reports/customized/rtv/fairprice/CUSTOMIZED_FAIR_PRICE_RTV" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION + ".jasper";
        }
        else
        {
            return "reports/customized/rtv/fairprice/CUSTOMIZED_FAIR_PRICE_RTV.jasper";
        }
        
    }
    
    //************************
    //private method
    //************************
    private void initBuyerParameter(ReportEngineParameter<RtvHolder> parameter, Map<String, Object> map)throws Exception
    {
        map.putAll(parameter.getBuyer().toMapValues());
        
        if(null != parameter.getBuyer().getContactTel())
        {
            map.put("BUYER_CONTACT_TEL", parameter.getBuyer().getContactTel());
        }
        
        if(null != parameter.getBuyer().getContactFax())
        {
            map.put("BUYER_CONTACT_FAX", parameter.getBuyer().getContactFax());
        }
        
        if(null != parameter.getBuyer().getRegNo())
        {
            map.put("BUYER_REG_NO", parameter.getBuyer().getRegNo());
        }
        
        if(null != parameter.getBuyer().getGstRegNo())
        {
            map.put("BUYER_GST_REG_NO", parameter.getBuyer().getGstRegNo());
        }
        
        if(null != parameter.getBuyer().getAddress1())
        {
            map.put("BUYER_ADDR_1", parameter.getBuyer().getAddress1());
        }
        
        if(null != parameter.getBuyer().getAddress2())
        {
            map.put("BUYER_ADDR_2", parameter.getBuyer().getAddress2());
        }
        
        if(null != parameter.getBuyer().getAddress3())
        {
            map.put("BUYER_ADDR_3", parameter.getBuyer().getAddress3());
        }
        
        if(null != parameter.getBuyer().getAddress4())
        {
            map.put("BUYER_ADDR_4", parameter.getBuyer().getAddress4());
        }
        
        if(null != parameter.getBuyer().getOtherRegNo())
        {
            map.put("BUYER_OTHER_REG_NO", parameter.getBuyer().getOtherRegNo());
        }
        
    }
    
    
    private void initSupplierParameter(ReportEngineParameter<RtvHolder> parameter, Map<String, Object> map)throws Exception
    {
        map.putAll(parameter.getSupplier().toMapValues());
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
            map.put("SUPPLIER_CONTACT_EMAIL", parameter.getSupplier().getContactEmail());
        }
        
        if(null != parameter.getSupplier().getAddress1())
        {
            map.put("SUPPLIER_ADDR_1", parameter.getSupplier().getAddress1());
        }
        
        if(null != parameter.getSupplier().getAddress2())
        {
            map.put("SUPPLIER_ADDR_2", parameter.getSupplier().getAddress2());
        }
        
        if(null != parameter.getSupplier().getAddress3())
        {
            map.put("SUPPLIER_ADDR_3", parameter.getSupplier().getAddress3());
        }
        
        if(null != parameter.getSupplier().getAddress4())
        {
            map.put("SUPPLIER_ADDR_4", parameter.getSupplier().getAddress4());
        }
        
        if(null != parameter.getSupplier().getCtryCode())
        {
            CountryHolder country = countryService.selectByCtryCode(parameter.getSupplier().getCtryCode());
            if (country != null)
            {
                map.put("SUPPLIER_CTRY_CODE", country.getCtryDesc());
            }
        }
        
        if(null != parameter.getSupplier().getPostalCode())
        {
            map.put("SUPPLIER_POSTAL_CODE", parameter.getSupplier().getPostalCode());
        }
    }
}
