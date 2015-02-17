package com.pracbiz.b2bportal.core.holder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.DnBuyerStatus;
import com.pracbiz.b2bportal.core.constants.DnPriceStatus;
import com.pracbiz.b2bportal.core.constants.DnQtyStatus;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;
import com.pracbiz.b2bportal.core.util.StringUtil;

public class DnHolder extends BaseHolder implements CoreCommonConstants
{
    private static final long serialVersionUID = 1L;
    private DnHeaderHolder dnHeader;
    private List<DnHeaderExtendedHolder> headerExtended;
    private List<DnDetailExHolder> dnDetail;
    private List<DnDetailExtendedHolder> detailExtended;
    

    public static Map<String, DnDetailHolder> convertDnDetailListToMapItemCodeAsKey(
            List<DnDetailExHolder> dnDetailList)
    {
        if (dnDetailList == null || dnDetailList.isEmpty())
        {
            return null;
        }
        Map<String, DnDetailHolder> dnDetailsMap = new HashMap<String, DnDetailHolder>();
        for (DnDetailExHolder dnDetail : dnDetailList)
        {
            dnDetailsMap.put(dnDetail.getBuyerItemCode(), dnDetail);
        }
        return dnDetailsMap;
    }
    
    
    public DnHeaderHolder getDnHeader()
    {
        return dnHeader;
    }

    
    public void setDnHeader(DnHeaderHolder dnHeader)
    {
        this.dnHeader = dnHeader;
    }
    

    public List<DnHeaderExtendedHolder> getHeaderExtended()
    {
        return headerExtended;
    }

    
    public void setHeaderExtended(List<DnHeaderExtendedHolder> headerExtended)
    {
        this.headerExtended = headerExtended;
    }
    

    public List<DnDetailExHolder> getDnDetail()
    {
        return dnDetail;
    }


    public void setDnDetail(List<DnDetailExHolder> dnDetail)
    {
        this.dnDetail = dnDetail;
    }


    public List<DnDetailExtendedHolder> getDetailExtended()
    {
        return detailExtended;
    }


    public void setDetailExtended(List<DnDetailExtendedHolder> detailExtended)
    {
        this.detailExtended = detailExtended;
    }


    @Override
    public String getCustomIdentification()
    {
        return dnHeader.getCustomIdentification();
    }

    
    public String toUnityCsvFile(String type)
    {
        StringBuffer content = new StringBuffer();
        if (DnType.STK_QOC.name().equalsIgnoreCase(type) ||DnType.CST_IOC.name().equalsIgnoreCase(type))
        {
            DnHeaderHolder header = this.dnHeader;
            List<DnDetailExHolder> details = this.dnDetail;
            
            content.append("[DN No.],[Date Shipped],[Order No.],[SO Date],[Invoiced Date],[Customer Code],[Shipping Store Code],[Shipping Store Name],[DN Remarks]")
                .append(END_LINE);
            content.append(header.getDnNo()).append(COMMA_DELIMITOR)
                .append(DateUtil.getInstance().convertDateToString(new Date(), DateUtil.DATE_FORMAT_DDMMYYYY)).append(COMMA_DELIMITOR)
                .append(header.getPoNo()).append(COMMA_DELIMITOR)
                .append(DateUtil.getInstance().convertDateToString(header.getDnDate(), DateUtil.DATE_FORMAT_DDMMYYYY)).append(COMMA_DELIMITOR)
                .append(DateUtil.getInstance().convertDateToString(header.getInvDate(), DateUtil.DATE_FORMAT_DDMMYYYY)).append(COMMA_DELIMITOR)
                .append(header.getSupplierCode()).append(COMMA_DELIMITOR)
                .append(header.getStoreCode()).append(COMMA_DELIMITOR)
                .append(header.getStoreName()).append(COMMA_DELIMITOR)
                .append(type);
            
            if (null != header.getDnRemarks() && !header.getDnRemarks().isEmpty())
            {
                content.append("--").append(header.getDnRemarks());
            }
            content.append(END_LINE);
            
            content.append("[DN No.],[Line No.],[SKU Code],[Description],[Packing Factor],[Order Base Unit],[Order UOM],[Quantity Invoiced]," +
            		"[Quantity Different],[Item Remarks],[Unit Price],[Price Different]");
            content.append(END_LINE);
            
            for (DnDetailHolder detail : details)
            {
                content.append(header.getDnNo()).append(COMMA_DELIMITOR)
                    .append(detail.getLineSeqNo()).append(COMMA_DELIMITOR)
                    .append(detail.getBuyerItemCode()).append(COMMA_DELIMITOR)
                    .append(detail.getItemDesc()).append(COMMA_DELIMITOR)
                    .append(detail.getPackingFactor()).append(COMMA_DELIMITOR)
                    .append(detail.getDebitBaseUnit()).append(COMMA_DELIMITOR)
                    .append(detail.getOrderUom()).append(COMMA_DELIMITOR);
                
                if (DnType.STK_QOC.name().equalsIgnoreCase(type))
                {
                    content.append("").append(COMMA_DELIMITOR)
                    .append(detail.getDebitQty()).append(COMMA_DELIMITOR)
                    .append(detail.getItemRemarks()).append(COMMA_DELIMITOR)
                    .append(detail.getUnitCost()).append(COMMA_DELIMITOR)
                    .append("").append(COMMA_DELIMITOR);
                }
                else
                {
                    content.append(detail.getDebitQty()).append(COMMA_DELIMITOR)
                    .append("").append(COMMA_DELIMITOR)
                    .append(detail.getItemRemarks()).append(COMMA_DELIMITOR)
                    .append("").append(COMMA_DELIMITOR)
                    .append(detail.getUnitCost());
                }
                
                content.append(END_LINE);
            }
        }
        
        return content.toString();
    }
    
    
    public String getUnityCsvFileName()
    {
        return "SINV" + DOC_FILENAME_DELIMITOR
            + this.dnHeader.getBuyerCode() + DOC_FILENAME_DELIMITOR
            + this.dnHeader.getSupplierCode() + DOC_FILENAME_DELIMITOR
            + this.dnHeader.getDnNo().replaceAll("\\*", "-").replaceAll("/", "-")
            .replaceAll("\\\\", "-").replaceAll(":", "-")
            .replaceAll("\\?", "-")
            .replaceAll("\"", "-").replaceAll("<", "-")
            .replaceAll(">", "-").replaceAll("\\|", "-") + DOC_FILENAME_DELIMITOR
            + DateUtil.getInstance().getCurrentLogicTimeStampWithoutMsec() + DOC_FILENAME_DELIMITOR
            + "1" + ".csv";
    }
    
    
    public void conertJsonToDn(String dnHeaderJson, String dnDetailJson)
    {
        JsonConfig jsonConfig = new JsonConfig();     
        jsonConfig.setRootClass(DnHeaderHolder.class);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS}) );
        JSONObject headerJson = JSONObject.fromObject(StringUtil.getInstance().replaceSpecialCharactersForPageValue(dnHeaderJson));
        DnHeaderHolder header = (DnHeaderHolder)JSONObject.toBean(headerJson,jsonConfig);
        this.setDnHeader(header);
        JSONArray array = JSONArray.fromObject(StringUtil.getInstance().replaceSpecialCharactersForPageValue(dnDetailJson), jsonConfig);
        List<DnDetailExHolder> details = new ArrayList<DnDetailExHolder>();
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject object = (JSONObject)array.get(i);
            DnDetailExHolder detail = (DnDetailExHolder)JSONObject
                .toBean(object, DnDetailExHolder.class);
            details.add(detail);
        }
        
        this.setDnDetail(details);
    }
    
    
    public boolean isDisputeQtyChanged()
    {
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.isDisputeQtyChanged())
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean isDisputePriceChanged()
    {
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.isDisputePriceChanged())
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean isDisputeValueChanged()
    {
        return isDisputeQtyChanged() || isDisputePriceChanged();
    }
    
    
    public boolean isConfirmValueChanged()
    {
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.isConfirmPriceChanged() || detail.isConfirmQtyChanged())
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean qtyAuditFinished()
    {
        if (dnDetail == null)
        {
            return true;
        }
        
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.isDisputeQtyChanged() && detail.getQtyStatus().equals(DnQtyStatus.PENDING))
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    public boolean priceAuditFinished()
    {
        if (dnDetail == null)
        {
            return true;
        }
        
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.isDisputePriceChanged() && detail.getPriceStatus().equals(DnPriceStatus.PENDING))
            {
                return false;
            }
        }
        return true;
    }
    
    
    public DnPriceStatus computePriceStatus()
    {
        if (dnDetail == null)
        {
            return null;
        }
        
        if (!priceAuditFinished())
        {
            return DnPriceStatus.PENDING;
        }
        
        boolean anyReject = false;
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.getPriceStatus().equals(DnPriceStatus.ACCEPTED))
            {
                return DnPriceStatus.ACCEPTED;
            }
            if (detail.getPriceStatus().equals(DnPriceStatus.REJECTED))
            {
                anyReject = true;
            }
        }
        return anyReject == true ? DnPriceStatus.REJECTED : DnPriceStatus.PENDING;
    }
    
    
    public DnQtyStatus computeQtyStatus()
    {
        if (!qtyAuditFinished())
        {
            return DnQtyStatus.PENDING;
        }
        
        boolean anyReject = false;
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.getQtyStatus().equals(DnQtyStatus.ACCEPTED))
            {
                return DnQtyStatus.ACCEPTED;
            }
            if (detail.getQtyStatus().equals(DnQtyStatus.REJECTED))
            {
                anyReject = true;
            }
        }
        return anyReject == true ? DnQtyStatus.REJECTED : DnQtyStatus.PENDING;
    }
    
    
    public DnBuyerStatus computeBuyerStatus()
    {
        if (!(priceAuditFinished() && qtyAuditFinished()))
        {
            return DnBuyerStatus.PENDING;
        }
        
        DnPriceStatus priceStatus = dnHeader.getPriceStatus().equals(DnPriceStatus.PENDING) ? 
                DnPriceStatus.REJECTED : dnHeader.getPriceStatus();
        
        DnQtyStatus qtyStatus = dnHeader.getQtyStatus().equals(DnQtyStatus.PENDING) ? 
                DnQtyStatus.REJECTED : dnHeader.getQtyStatus();
        
        if (priceStatus.equals(DnPriceStatus.ACCEPTED) || qtyStatus.equals(DnQtyStatus.ACCEPTED))
        {
            return DnBuyerStatus.ACCEPTED;
        }
        
        return DnBuyerStatus.REJECTED;
    }
    
    
    public int calculateCountOfPrivilege()
    {
        int count = 0;
        for (DnDetailHolder detail : dnDetail)
        {
            if (detail.getPrivileged() != null && detail.getPrivileged())
            {
                count ++;
            }
        }
        
        return count;
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return dnHeader.getLogicalKey();
    }
}
