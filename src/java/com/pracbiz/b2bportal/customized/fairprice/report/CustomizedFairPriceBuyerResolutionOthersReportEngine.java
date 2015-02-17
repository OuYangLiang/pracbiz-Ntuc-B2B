package com.pracbiz.b2bportal.customized.fairprice.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.report.DefaultBuyerResolutionOthersReportEngine;
import com.pracbiz.b2bportal.core.report.ReportEngineParameter;
import com.pracbiz.b2bportal.core.report.excel.MatchingReportParameter;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 *
 * @author YinChi
 */
public class CustomizedFairPriceBuyerResolutionOthersReportEngine extends
    DefaultBuyerResolutionOthersReportEngine implements CoreCommonConstants
{

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
            map = new HashMap<String, Object>();
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
        
        Collections.sort(dataSource, new Comparator<Map<String, Object>>(){
            @Override
            public int compare(Map<String, Object> m1, Map<String, Object> m2)
            {
                return ((Date)m1.get("MATCHING_DATE")).before((Date)m2.get("MATCHING_DATE")) ? -1 : 1;
            }
        });
        return dataSource;
    }

    @Override
    protected String reportTemplate(List<MatchingReportParameter> data, int isForStore)
    {
        return "reports/customized/buyerResolution/fairprice/CUSTOMIZED_FAIR_PRICE_BUYER_RESOLUTION_OTHER.jasper";
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
