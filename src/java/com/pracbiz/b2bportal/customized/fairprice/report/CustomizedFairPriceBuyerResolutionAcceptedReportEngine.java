package com.pracbiz.b2bportal.customized.fairprice.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.report.DefaultBuyerResolutionAcceptedReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public class CustomizedFairPriceBuyerResolutionAcceptedReportEngine extends
    DefaultBuyerResolutionAcceptedReportEngine implements CoreCommonConstants
{
    @Autowired
    private transient CustomAppConfigHelper appConfig;

    @Override
    protected Map<String, Object> reportParameter(
        ReportEngineParameter<List<MatchingReportParameter>> parameter, int flag)
    {
        Map<String, Object> map = parameter.getBuyer().toMapValues();
        map.put("BUYER_LOGO", parameter.getBuyer().initBuyerLogo());
        return map;
    }

    @Override
    protected List<Map<String, Object>> reportDatasource(
        ReportEngineParameter<List<MatchingReportParameter>> parameter, int flag)
    {
        List<MatchingReportParameter> reports = parameter.getData();
        List<Map<String, Object>> dataSource = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = null;
        
        for (MatchingReportParameter report : reports)
        {
            List<InvDetailHolder> invDetails = report.getInvDetails();
            
            if (invDetails != null && !invDetails.isEmpty())
            {
                for (int j = 0; j < invDetails.size(); j++)
                {
                    map = invDetails.get(j).toMapValues();
                    
                    map.put("MATCHING_DATE", report.getMatchingDate());
                    map.put("PO_NO", report.getPoNo());
                    map.put("STORE_CODE", report.getStoreCode());
                    map.put("SUPPLIER_CODE", report.getSupplierCode());
                    map.put("GRN_NO", getGrnNos(report.getGrnNos()));
                    map.put("GRN_DATE", getGrnDates(report.getGrnDates()));
                    map.put("INV_NO", report.getInvNo());
                    map.put("MATCHING_DATE_VALUE", DateUtil.getInstance().convertDateToString(report.getMatchingDate()));
                    dataSource.add(map);
                }
            }
        }
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m2.get("BUYER_ITEM_CODE")).compareTo((String)m1.get("BUYER_ITEM_CODE"));
            }
        });
        
        Map<String, String> order = appConfig.getmatchingResolutionReportOrder(); 
        
        if (null != order.get(reports.get(0).getBuyerCode()))
        {
            if ("supplierCode".equalsIgnoreCase(order.get(reports.get(0).getBuyerCode())))
            {
                Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
                    @Override
                    public int compare(Map<String, Object> m1, Map<String, Object> m2)
                    {
                        return ((String)m1.get("SUPPLIER_CODE")).compareTo((String)m2.get("SUPPLIER_CODE"));
                    }
                });
            }
            if ("storeCode".equalsIgnoreCase(order.get(reports.get(0).getBuyerCode())))
            {
                Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
                    @Override
                    public int compare(Map<String, Object> m1, Map<String, Object> m2)
                    {
                        return ((String)m1.get("STORE_CODE")).compareTo((String)m2.get("STORE_CODE"));
                    }
                });
            }
        }
        
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((String)m1.get("PO_NO")).compareTo((String)m2.get("PO_NO"));
            }
        });
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((Date)m1.get("MATCHING_DATE")).before((Date)m2.get("MATCHING_DATE")) ? -1 : 1;
            }
        });
        
        List<String> keys = new ArrayList<String>();
        for (Map<String, Object> data : dataSource)
        {
            String key = DateUtil.getInstance().convertDateToString((Date)data.get("MATCHING_DATE"), DateUtil.DATE_FORMAT_LOGIC_TIMESTAMP_WITHOUT_MSEC)
                + "-" + (String)data.get("SUPPLIER_CODE") + "-" + (String)data.get("STORE_CODE") + "-" + (String)data.get("PO_NO");
            
            if(keys.contains(key))
            {
                data.put("SUPPLIER_CODE", "");
                data.put("STORE_CODE", "");
                data.put("PO_NO", "");
                data.put("GRN_NO", "");
                data.put("GRN_DATE", "");
                data.put("INV_NO", "");
            }
            else
            {
                keys.add(key);
            }
        }
        
        return dataSource;
    }

    @Override
    protected String reportTemplate(List<MatchingReportParameter> data, int isForStore)
    {
        return "reports/customized/buyerResolution/fairprice/CUSTOMIZED_FAIR_PRICE_BUYER_RESOLUTION_ACCEPTED.jasper";
    }
    
    private String getGrnNos(String[] param)
    {
        String result = "";
        if (param == null || param.length == 0)
        {
            return result;
        }
        if (param.length == 1)
        {
            result = param[0];
        }
        else
        {
            result = result + param[0];
            for (int i = 1; i < param.length; i++)
            {
                result = "\r\n" + param[i];
            }
        }
        return result;
    }
    
    private String getGrnDates(Date[] param)
    {
        String result = "";
        if (param == null || param.length == 0)
        {
            return result;
        }
        if (param.length == 1)
        {
            result = DateUtil.getInstance().convertDateToString(param[0]);
        }
        else
        {
            result = result + DateUtil.getInstance().convertDateToString(param[0]);
            for (int i = 1; i < param.length; i++)
            {
                result = "\r\n" + DateUtil.getInstance().convertDateToString(param[i]);
            }
        }
        return result;
    }

}
