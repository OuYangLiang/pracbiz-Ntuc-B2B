package com.pracbiz.b2bportal.core.holder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import org.apache.commons.lang.StringUtils;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class GrnHolder extends BaseHolder
{
    /**
     * 
     */
    private static final long serialVersionUID = 3598116933118465208L;
    private GrnHeaderHolder header;
    private List<GrnDetailHolder> details;
    private List<GrnHeaderExtendedHolder> headerExtendeds;
    private List<GrnDetailExtendedHolder> detailExtendeds;
    
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String AMPERSAND_MARK_ENTITY_NAME = "&amp;";
    private static final String AMPERSAND_MARK_CHARACTER = "&";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";

    
    public static Map<String, GrnDetailHolder> convertGrnDetailListToMapItemCodeAsKey(
            List<GrnDetailHolder> grnDetailList)
    {
        if (grnDetailList == null || grnDetailList.isEmpty())
        {
            return null;
        }
        Map<String, GrnDetailHolder> grnDetailsMap = new HashMap<String, GrnDetailHolder>();
        for (GrnDetailHolder grnDetail : grnDetailList)
        {
            grnDetailsMap.put(grnDetail.getBuyerItemCode(), grnDetail);
        }
        return grnDetailsMap;
    }
    
    
    public void conertJsonToGrn(String grnHeaderJson, String grnDetailJson)
    {
        JsonConfig jsonConfig = new JsonConfig();     
        jsonConfig.setRootClass(GrnHeaderHolder.class);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS}) );
        JSONObject headerJson = JSONObject.fromObject(replaceSpecialCharactersForPageValue(grnHeaderJson));
        GrnHeaderHolder header = (GrnHeaderHolder)JSONObject.toBean(headerJson,jsonConfig);
        this.setHeader(header);
        JSONArray array = JSONArray.fromObject(replaceSpecialCharactersForPageValue(grnDetailJson), jsonConfig);
        List<GrnDetailHolder> details = new ArrayList<GrnDetailHolder>();
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject object = (JSONObject)array.get(i);
            GrnDetailHolder detail = (GrnDetailHolder)JSONObject
                .toBean(object, GrnDetailHolder.class);
            details.add(detail);
        }
        
        this.setDetails(details);
    }

    
    //*****************
    //private method
    //*****************
    private String replaceSpecialCharactersForPageValue(String json)
    {
        if (json == null)
        {
            return null;
        }
        String result = StringUtils.replace(json,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER);
        
        result = StringUtils.replace(result, AMPERSAND_MARK_ENTITY_NAME,
            AMPERSAND_MARK_CHARACTER);
        
        return  StringUtils.replace(result,
            RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME,
            RIGHT_SINGLE_QUOTATION_MARK_CHARACTER); 
    }
    
    
    //******************
    //getter and setter 
    //******************
    
    public GrnHeaderHolder getHeader()
    {
        return header;
    }


    public void setHeader(GrnHeaderHolder header)
    {
        this.header = header;
    }


    public List<GrnDetailHolder> getDetails()
    {
        return details;
    }


    public void setDetails(List<GrnDetailHolder> details)
    {
        this.details = details;
    }


    public List<GrnHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }


    public void setHeaderExtendeds(List<GrnHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }


    public List<GrnDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }


    public void setDetailExtendeds(List<GrnDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }


    @Override
    public String getCustomIdentification()
    {
        return header.getCustomIdentification();
    }


    @Override
    public String getLogicalKey()
    {
        return header.getGrnNo();
    }
    
}
