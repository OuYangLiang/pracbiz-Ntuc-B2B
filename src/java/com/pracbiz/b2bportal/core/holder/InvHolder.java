package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
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






import org.apache.commons.lang.StringUtils;






import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.util.BigDecimalUtil;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.InvType;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;
import com.pracbiz.b2bportal.core.holder.extension.InvHeaderExHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.DateJsonValueProcessor;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class InvHolder extends BaseHolder
{
    private static final long serialVersionUID = 5948138879959142856L;
    private static final String RIGHT_SINGLE_QUOTATION_MARK_ENTITY_NAME = "&prime;";
    private static final String RIGHT_SINGLE_QUOTATION_MARK_CHARACTER = "'";
    private static final String AMPERSAND_MARK_ENTITY_NAME = "&amp;";
    private static final String AMPERSAND_MARK_CHARACTER = "&";
    private static final String PO_DETAIL_EX_FIELD_NAME = "netAmount";
    private InvHeaderHolder header;
    private List<InvDetailHolder> details;
    private List<InvHeaderExtendedHolder> headerExtendeds;
    private List<InvDetailExtendedHolder> detailExtendeds;
    
    
    public void setInvHeaer(PoHeaderHolder poHeader,SupplierHolder supplier,String invNo,BigDecimal invAmountNoVat, PoStatus poStatus, BigDecimal oid, PoType poType)
    {
        InvHeaderExHolder header = new InvHeaderExHolder();
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        
        header.setInvOid(oid);
        header.setInvNo(invNo);
        header.setPoOid(poHeader.getPoOid());
        header.setPoNo(poHeader.getPoNo());
        header.setPoDate(poHeader.getPoDate());
        header.setPayTermCode(poHeader.getCreditTermCode());
        header.setPayTermDesc(poHeader.getCreditTermDesc());
        header.setAdditionalDiscountAmount(BigDecimal.ZERO);
        
        header.setAdditionalDiscountPercent(format.format(poHeader.getAdditionalDiscountPercent(), 2));
        header.setInvAmountNoVat(format.format(invAmountNoVat.subtract(header.getAdditionalDiscountAmount()), 2));
        if (PoType.CON.equals(poType))
        {
            header.setInvAmountNoVat(invAmountNoVat);
        }
        header.setDocAction("A");//need to confirm
        header.setActionDate(new Date());
        header.setInvDate(new Date());
        header.setPoType(poHeader.getPoType());
        header.setInvType(InvType.getInvTypeByPoType(poHeader.getPoType()));
        header.setDeliveryDate(poHeader.getDeliveryDateFrom() == null ? null : poHeader.getDeliveryDateFrom());
        
        BigDecimal vatRate = supplier.getGstPercent() == null ? BigDecimal.ZERO : supplier.getGstPercent();
        BigDecimal vatAmount = invAmountNoVat.multiply(vatRate.divide(BigDecimal.valueOf(100)));
        header.setVatRate(format.format(vatRate,2));
        header.setVatAmount(format.format(vatAmount, 2));
        header.setInvAmountWithVat(format.format(vatAmount.add(invAmountNoVat), 2));
        
        header.setCashDiscountPercent(header.getCashDiscountPercent() == null ? BigDecimal.ZERO : header.getCashDiscountPercent());
        header.setCashDiscountAmount(header.getCashDiscountAmount() == null ? BigDecimal.ZERO : header.getCashDiscountAmount());
        header.setInvTotalBeforeAdditional(invAmountNoVat);
        header.setTotalPay(header.getInvAmountWithVat().subtract(header.getCashDiscountAmount()));
        
        //buyer info
        header.setBuyerOid(poHeader.getBuyerOid());
        header.setBuyerCode(poHeader.getBuyerCode());
        header.setBuyerAddr1(poHeader.getBuyerAddr1());
        header.setBuyerAddr2(poHeader.getBuyerAddr2());
        header.setBuyerAddr3(poHeader.getBuyerAddr3());
        header.setBuyerAddr4(poHeader.getBuyerAddr4());
        header.setBuyerCity(poHeader.getBuyerCity());
        header.setBuyerCtryCode(poHeader.getBuyerCtryCode());
        header.setBuyerState(poHeader.getBuyerState());
        header.setBuyerPostalCode(poHeader.getBuyerPostalCode());
        header.setBuyerName(poHeader.getBuyerName());
        
        //supplier info
        header.setSupplierOid(supplier.getSupplierOid());
        header.setSupplierCode(poHeader.getSupplierCode());
        header.setSupplierName(supplier.getSupplierName());
        header.setSupplierAddr1(supplier.getAddress1());
        header.setSupplierAddr2(supplier.getAddress2());
        header.setSupplierAddr3(supplier.getAddress3());
        header.setSupplierAddr4(supplier.getAddress4());
        header.setSupplierCity(poHeader.getShipToCity());//need to confirm
        header.setSupplierCtryCode(supplier.getCtryCode());
        header.setSupplierState(supplier.getState());
        header.setSupplierPostalCode(supplier.getPostalCode());
        header.setSupplierBizRegNo(supplier.getRegNo());
        header.setSupplierVatRegNo(supplier.getGstRegNo());
        header.setAutoInvNumber(supplier.getAutoInvNumber());
        
        //store info
        header.setShipToCode(poHeader.getShipToCode());
        header.setShipToName(poHeader.getShipToName());
        header.setShipToCity(poHeader.getShipToCity());
        header.setShipToCtryCode(poHeader.getShipToCtryCode());
        header.setShipToState(poHeader.getShipToState());
        header.setShipToPostalCode(poHeader.getShipToPostalCode());
        header.setShipToAddr1(poHeader.getShipToAddr1());
        header.setShipToAddr2(poHeader.getShipToAddr2());
        header.setShipToAddr3(poHeader.getShipToAddr3());
        header.setShipToAddr4(poHeader.getShipToAddr4());
        
        header.setPoStatus(poStatus);
        this.header = header;
    }
    
    
    public void setInvDetailByPoDetails(List<PoDetailHolder> poDetails, BigDecimal oid, PoType poType)
    {
        details = new ArrayList<InvDetailHolder>();
        for (PoDetailHolder poDetail: poDetails)
        {
            InvDetailHolder invDetail = this.convertToInvDetailFromPoDetail(poDetail, poType);
            invDetail.setInvOid(oid);
            details.add(invDetail);
        }
        
    }
    
    public void setInvDetailByPoDetailExsForConPo(List<PoDetailExtendedHolder> poDetailExs, PoType poType)
    {
        Map<Integer, PoDetailExtendedHolder> detailExMaps = new HashMap<Integer, PoDetailExtendedHolder>();
        for (PoDetailExtendedHolder poDetailEx: poDetailExs)
        {
            if (poDetailEx.getFieldName().equalsIgnoreCase(PO_DETAIL_EX_FIELD_NAME))
            {
                detailExMaps.put(poDetailEx.getLineSeqNo(), poDetailEx);
            }
        }
        
        if (details != null && !details.isEmpty())
        {
            for (InvDetailHolder invDetail : details)
            {
                Integer key = invDetail.getLineSeqNo();
                if (detailExMaps.containsKey(key))
                {   
                    invDetail.setNetAmount(BigDecimalUtil.getInstance().format(detailExMaps.get(key).getFloatValue(), 2));
                    BigDecimal conTotalCost = invDetail.getNetAmount().add(invDetail.getItemSharedCost());
                    invDetail.setConTotalCost(BigDecimalUtil.getInstance().format(conTotalCost, 2));
                }
            }
        }
    }
    
    public BigDecimal calculateInvAmountNoVat()
    {
        if (details == null || details.isEmpty()) return BigDecimal.ZERO;
            
        BigDecimal invAmountNoVat = BigDecimal.ZERO;
        for (InvDetailHolder invDetail: details)
        {
            invAmountNoVat = invAmountNoVat.add(invDetail.getNetAmount());
        }
        
        return invAmountNoVat;
    }
    
    
    public BigDecimal calculateInvAmountNoVatForConPo()
    {
        if (details == null || details.isEmpty()) return BigDecimal.ZERO;
            
        BigDecimal invAmountNoVat = BigDecimal.ZERO;
        for (InvDetailHolder invDetail: details)
        {
            invAmountNoVat = invAmountNoVat.add(invDetail.getItemGrossAmount());
        }
        
        return invAmountNoVat;
    }
    
    
    public void setShipInfoForInvHeaderByLocation(PoLocationHolder poLocation)
    {
        if (header == null) return;
        
        header.setShipToCode(poLocation.getLocationCode());
        header.setShipToName(poLocation.getLocationName());
        header.setShipToCity(poLocation.getLocationCity());
        header.setShipToCtryCode(poLocation.getLocationCtryCode());
        header.setShipToState(poLocation.getLocationState());
        header.setShipToPostalCode(poLocation.getLocationPostalCode());
        header.setShipToAddr1(poLocation.getLocationAddr1());
        header.setShipToAddr2(poLocation.getLocationAddr2());
        header.setShipToAddr3(poLocation.getLocationAddr3());
        header.setShipToAddr4(poLocation.getLocationAddr4());
    }
    
    
    public void setInvDetailByPoLocation(
        List<PoLocationDetailHolder> poLocationDetails,
        List<PoDetailHolder> poDetails, BigDecimal oid,
        PoType poType)
    {
        details = new ArrayList<InvDetailHolder>();
        Map<Integer, PoDetailHolder> poDetailMaps = this.convertToMap(poDetails);
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        for (PoLocationDetailHolder locationDetail : poLocationDetails)
        {
            Integer LineSeqNo = locationDetail.getDetailLineSeqNo();
            if (!poDetailMaps.containsKey(LineSeqNo))
            {
                continue;
            }
            
            PoDetailHolder detail = poDetailMaps.get(LineSeqNo);
            InvDetailExHolder invDetail = this.convertToInvDetailFromPoDetail(detail, poType);
            invDetail.setInvOid(oid);
            invDetail.setLineRefNo(locationDetail.getLineRefNo());
            
            invDetail.setFocQty(format.formatWithRoundingMode(locationDetail.getLocationFocQty(), 2 , BigDecimal.ROUND_FLOOR));
            invDetail.setInvQty(format.formatWithRoundingMode(locationDetail.getLocationShipQty(),2, BigDecimal.ROUND_FLOOR));
            
            BigDecimal shipQty = locationDetail.getLocationShipQty() == null ? BigDecimal.ZERO
                : locationDetail.getLocationShipQty();
            BigDecimal qty = detail.getOrderQty();
            
            BigDecimal unitPrice = invDetail.getUnitPrice() == null ? BigDecimal.ZERO : invDetail.getUnitPrice();
            BigDecimal itemAmount = shipQty.multiply(unitPrice).setScale(4, BigDecimal.ROUND_HALF_UP);
            invDetail.setItemAmount(format.format(itemAmount, 2));
            invDetail.setPoQty(shipQty);
            invDetail.setPoFocQty(format.formatWithRoundingMode(locationDetail.getLocationFocQty(), 2, BigDecimal.ROUND_FLOOR));
                
            if (qty != null && qty.compareTo(BigDecimal.ZERO) != 0)
            {
                BigDecimal discountPercent = shipQty.divide(qty, 4,
                    BigDecimal.ROUND_HALF_UP);
                
                BigDecimal discountAmount = detail.getCostDiscountAmount() == null ? BigDecimal.ZERO
                    : detail.getCostDiscountAmount();
                
                BigDecimal discount = discountAmount.multiply(discountPercent).setScale(4, BigDecimal.ROUND_HALF_UP);
                invDetail.setDiscountAmount(format.format(discount, 2));
                BigDecimal netAmount = itemAmount.subtract(discount);
                invDetail.setNetAmount(format.format(netAmount,2));
                if (itemAmount.compareTo(BigDecimal.ZERO) != 0)
                {
                    invDetail.setDiscountPercent(format.format(discount.divide(itemAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)), 2));
                }
                else
                {
                    invDetail.setDiscountPercent(BigDecimal.ZERO);
                }
            }
            else
            { 
                invDetail.setDiscountAmount(BigDecimal.ZERO);
                invDetail.setNetAmount(BigDecimal.ZERO);
                invDetail.setDiscountPercent(BigDecimal.ZERO);
                
            }
            details.add(invDetail);
        }
        
    }
    
    
    public InvDetailExHolder convertToInvDetailFromPoDetail(PoDetailHolder poDetail, PoType poType)
    {
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        InvDetailExHolder invDetail = new InvDetailExHolder();
        invDetail.setLineSeqNo(poDetail.getLineSeqNo());
        invDetail.setBuyerItemCode(poDetail.getBuyerItemCode());
        invDetail.setSupplierItemCode(poDetail.getSupplierItemCode());
        invDetail.setBarcode(poDetail.getBarcode());
        invDetail.setItemDesc(poDetail.getItemDesc());
        invDetail.setBrand(poDetail.getBrand());
        invDetail.setColourCode(poDetail.getColourCode());
        invDetail.setColourDesc(poDetail.getColourDesc());
        invDetail.setSizeCode(poDetail.getSizeCode());
        invDetail.setSizeDesc(poDetail.getSizeDesc());
        invDetail.setPackingFactor(poDetail.getPackingFactor());
        invDetail.setInvBaseUnit(poDetail.getOrderBaseUnit());
        invDetail.setInvUom(poDetail.getOrderUom());
        invDetail.setInvQty(format.formatWithRoundingMode(poDetail.getOrderQty(), 2, BigDecimal.ROUND_FLOOR));
        invDetail.setFocBaseUnit(poDetail.getFocBaseUnit());
        invDetail.setFocUom(poDetail.getFocUom());
        invDetail.setFocQty(format.formatWithRoundingMode(poDetail.getFocQty(), 2, BigDecimal.ROUND_FLOOR));
        invDetail.setUnitPrice(format.formatWithRoundingMode(poDetail.getUnitCost(), 2, BigDecimal.ROUND_FLOOR));
        invDetail.setItemRemarks(poDetail.getItemRemarks());
        invDetail.setItemAmount(format.format(poDetail.getItemCost(), 2));
        invDetail.setItemGrossAmount(format.format(poDetail.getItemGrossCost(), 2));
        invDetail.setItemSharedCost(format.format(poDetail.getItemSharedCost(), 2));
        invDetail.setDiscountAmount(format.format(poDetail.getCostDiscountAmount(), 2));
        invDetail.setDiscountPercent(format.format(poDetail.getCostDiscountPercent(), 2));
        invDetail.setPoFocQty(format.formatWithRoundingMode(poDetail.getFocQty(), 2, BigDecimal.ROUND_FLOOR));
        invDetail.setPoQty(format.formatWithRoundingMode(poDetail.getOrderQty(), 2, BigDecimal.ROUND_FLOOR));
        
        this.setItemUnitPriceAndNetUnitPrice(invDetail, poDetail);
        BigDecimal itemAmount = invDetail.getItemAmount() == null ? BigDecimal.ZERO : invDetail.getItemAmount();
        BigDecimal discount = invDetail.getDiscountAmount() == null ? BigDecimal.ZERO : invDetail.getDiscountAmount();
        BigDecimal netAmount = itemAmount.subtract(discount);
        invDetail.setNetAmount(format.format(netAmount, 2));
        
       if (PoType.CON.equals(poType))
       {
           //for consignment po.
           BigDecimal itemGrossCost = poDetail.getItemGrossCost() == null ? BigDecimal.ZERO : poDetail.getItemGrossCost();
           BigDecimal retailDiscAmt = poDetail.getRetailDiscountAmount() == null ? BigDecimal.ZERO : poDetail.getRetailDiscountAmount();
           BigDecimal costDiscAmt = poDetail.getCostDiscountAmount() == null ? BigDecimal.ZERO : poDetail.getCostDiscountAmount();
           netAmount = (itemGrossCost.subtract(retailDiscAmt)).subtract(costDiscAmt);
           invDetail.setNetAmount(format.format(netAmount, 2));
           invDetail.setTotalCustomerDisc(format.format(poDetail.getRetailDiscountAmount(), 2));
           BigDecimal conTotalCost = invDetail.getNetAmount().add(invDetail.getItemSharedCost());
           invDetail.setConTotalCost(format.format(conTotalCost, 2));
       }
        
        return invDetail;
    }
    
    
    public void setTermCondition(TermConditionHolder term)
    {
        if (term == null || header == null) return;
        
        header.setFooterLine1(term.getTermCondition1());
        header.setFooterLine2(term.getTermCondition2());
        header.setFooterLine3(term.getTermCondition3());
        header.setFooterLine4(term.getTermCondition4());
    }
    
    
    private Map<Integer, PoDetailHolder> convertToMap(List<PoDetailHolder> poDetails)
    {
        Map<Integer, PoDetailHolder> map = new HashMap<Integer, PoDetailHolder>();
        for (PoDetailHolder poDetail: poDetails)
        {
            map.put(poDetail.getLineSeqNo(), poDetail);
        }
        
        return map;
    }
    
    
    private void setItemUnitPriceAndNetUnitPrice(InvDetailExHolder invDetail, PoDetailHolder poDetail)
    {
        BigDecimalUtil format = BigDecimalUtil.getInstance();
        String invBaseUnit = invDetail.getInvBaseUnit();
        if (CoreCommonConstants.INV_QTY_BASE_PACK.equalsIgnoreCase(invBaseUnit))
        {
            invDetail.setUnitPrice(format.formatWithRoundingMode(poDetail.getPackCost(), 2, BigDecimal.ROUND_FLOOR));
            invDetail.setNetPrice(format.formatWithRoundingMode(poDetail.getNetPackCost(), 2, BigDecimal.ROUND_FLOOR));
            invDetail.setPoUnitPrice(format.formatWithRoundingMode(poDetail.getPackCost(), 2, BigDecimal.ROUND_FLOOR));
        }
        
        if (CoreCommonConstants.INV_QTY_BASE_UNIT.equalsIgnoreCase(invBaseUnit))
        {
            invDetail.setUnitPrice(format.formatWithRoundingMode(poDetail.getUnitCost(), 2, BigDecimal.ROUND_FLOOR));
            invDetail.setNetPrice(format.formatWithRoundingMode(poDetail.getNetUnitCost(), 2, BigDecimal.ROUND_FLOOR));
            invDetail.setPoUnitPrice(format.formatWithRoundingMode(poDetail.getUnitCost(), 2, BigDecimal.ROUND_FLOOR));
        }
    }
    
    
    public void conertJsonToInvoice(String invHeaderJson, String invDetailJson)
    {
        JsonConfig jsonConfig = new JsonConfig();     
        jsonConfig.setRootClass(InvHeaderExHolder.class);
        jsonConfig.registerJsonValueProcessor(java.util.Date.class, new DateJsonValueProcessor(DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS}) );
        JSONObject headerJson = JSONObject.fromObject(replaceSpecialCharactersForPageValue(invHeaderJson));
        if (headerJson.get("deliveryDate").toString().isEmpty())
        {
            headerJson.remove("deliveryDate");
        }
        InvHeaderExHolder header = (InvHeaderExHolder)JSONObject.toBean(headerJson,jsonConfig);
        this.setHeader(header);
        JSONArray array = JSONArray.fromObject(replaceSpecialCharactersForPageValue(invDetailJson), jsonConfig);
        List<InvDetailHolder> details = new ArrayList<InvDetailHolder>();
        for (int i = 0; i < array.size(); i++)
        {
            JSONObject object = (JSONObject)array.get(i);
            InvDetailExHolder detail = (InvDetailExHolder)JSONObject
                .toBean(object, InvDetailExHolder.class);
            details.add(detail);
        }
        
        this.setDetails(details);
    }
    
    
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
    
    
    public static Map<String, InvDetailHolder> convertInvDetailListToMapItemCodeAsKey(
            List<InvDetailHolder> invDetailList)
    {
        if (invDetailList == null || invDetailList.isEmpty())
        {
            return null;
        }
        Map<String, InvDetailHolder> invDetailsMap = new HashMap<String, InvDetailHolder>();
        for (InvDetailHolder invDetail : invDetailList)
        {
            invDetailsMap.put(invDetail.getBuyerItemCode(), invDetail);
        }
        return invDetailsMap;
    }


    public CnHolder toCreditNote(PoHeaderHolder poHeader, List<PoDetailExtendedHolder> detailExs, SupplierHolder supplier, BigDecimal oid, CnStatus cnStatus)
    {
        CnHolder cn = new CnHolder();
        Map<Integer, List<PoDetailExtendedHolder>> map = new HashMap<Integer, List<PoDetailExtendedHolder>>();
        if (detailExs != null && !detailExs.isEmpty())
        {
            for (PoDetailExtendedHolder detailEx : detailExs)
            {
                if (map.containsKey(detailEx.getLineSeqNo()))
                {
                    map.get(detailEx.getLineSeqNo()).add(detailEx);
                }
                else
                {
                    List<PoDetailExtendedHolder> detailExtends = new ArrayList<PoDetailExtendedHolder>();
                    detailExtends.add(detailEx);
                    map.put(detailEx.getLineSeqNo(), detailExtends);
                }
            }
        }
        
        cn.setCnHeaderAndHeaderExtended(this.header, poHeader, supplier, oid, cnStatus);
        cn.setCnDetailAndDetailExtended(this.details, this.header, map, oid);
       
        return cn;
    }
    
    public InvHeaderHolder getHeader()
    {
        return header;
    }


    public void setHeader(InvHeaderHolder header)
    {
        this.header = header;
    }


    public List<InvDetailHolder> getDetails()
    {
        return details;
    }


    public void setDetails(List<InvDetailHolder> details)
    {
        this.details = details;
    }


    public List<InvHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }


    public void setHeaderExtendeds(List<InvHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }


    public List<InvDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }


    public void setDetailExtendeds(List<InvDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }


    @Override
    public String getCustomIdentification()
    {
        return header == null ? null : (header.getInvOid() == null ? null
            : header.getInvOid().toString());
    }

    
    @Override
    public String getLogicalKey()
    {
        return header.getLogicalKey();
    }
   
}
