package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.PoType;

public class PoHolder extends BaseHolder
{
    private static final long serialVersionUID = -6912226360407980764L;
    private static String PO_SUB_TYPE_1="1";
    private static String PO_SUB_TYPE_2="2";
    private PoHeaderHolder poHeader;
    private List<PoHeaderExtendedHolder> headerExtendeds;
    private List<PoDetailHolder> details;
    private List<PoDetailExtendedHolder> detailExtendeds;
    private List<PoLocationHolder> locations;
    private List<PoLocationDetailHolder> locationDetails;
    private List<PoLocationDetailExtendedHolder> poLocDetailExtendeds;
    private BigDecimal oldPoOid;
    private String pdfName;
    private String supplierFileName;
    private PoHeaderHolder oldPoHeader;
    
    
    /*public static void main(String[] args) throws Exception
    {
        PoHolder po = new PoHolder();
        PoHeaderHolder header = new PoHeaderHolder();
        List<PoDetailHolder> details = new ArrayList<PoDetailHolder>();
        List<PoLocationHolder> locs = new ArrayList<PoLocationHolder>();
        List<PoLocationDetailHolder> locDets = new ArrayList<PoLocationDetailHolder>();
        po.setPoHeader(header);
        po.setDetails(details);
        po.setLocations(locs);
        po.setLocationDetails(locDets);
        
        header.setPoSubType("1");
        
        
        //init 2 details, item 1, qty 100, unit price 100, total 10000
        // item 2, qty 50, unit price 10, total 500
        
        PoDetailHolder detail = new PoDetailHolder();
        detail.setLineSeqNo(Integer.valueOf(1));
        detail.setOrderQty(BigDecimal.valueOf(100));
        detail.setOrderBaseUnit("P");
        detail.setPackCost(BigDecimal.valueOf(100));
        detail.setUnitCost(BigDecimal.valueOf(100));
        detail.setPackingFactor(BigDecimal.ONE);
        detail.setItemCost(BigDecimal.valueOf(10000));
        
        po.getDetails().add(detail);
        
        detail = new PoDetailHolder();
        detail.setLineSeqNo(Integer.valueOf(2));
        detail.setOrderQty(BigDecimal.valueOf(50));
        detail.setOrderBaseUnit("P");
        detail.setPackCost(BigDecimal.valueOf(10));
        detail.setUnitCost(BigDecimal.valueOf(10));
        detail.setPackingFactor(BigDecimal.ONE);
        detail.setItemCost(BigDecimal.valueOf(500));
        po.getDetails().add(detail);
        
        // init 2 store: A and B
        
        PoLocationHolder loc = new PoLocationHolder();
        loc.setLocationCode("A");
        loc.setLineSeqNo(Integer.valueOf(1));
        po.getLocations().add(loc);
        
        loc = new PoLocationHolder();
        loc.setLocationCode("B");
        loc.setLineSeqNo(Integer.valueOf(2));
        po.getLocations().add(loc);
        
        
        // init loc details
        // store A, item 1, qty 30, item 2, 25
        // store B, item 1, qty 70, item 2, 25
        
        PoLocationDetailHolder locDet = new PoLocationDetailHolder();
        locDet.setDetailLineSeqNo(Integer.valueOf(1));
        locDet.setLocationLineSeqNo(Integer.valueOf(1));
        locDet.setLocationShipQty(BigDecimal.valueOf(30));
        po.getLocationDetails().add(locDet);
        
        locDet = new PoLocationDetailHolder();
        locDet.setDetailLineSeqNo(Integer.valueOf(2));
        locDet.setLocationLineSeqNo(Integer.valueOf(1));
        locDet.setLocationShipQty(BigDecimal.valueOf(25));
        po.getLocationDetails().add(locDet);
        
        locDet = new PoLocationDetailHolder();
        locDet.setDetailLineSeqNo(Integer.valueOf(1));
        locDet.setLocationLineSeqNo(Integer.valueOf(2));
        locDet.setLocationShipQty(BigDecimal.valueOf(70));
        po.getLocationDetails().add(locDet);
        
        locDet = new PoLocationDetailHolder();
        locDet.setDetailLineSeqNo(Integer.valueOf(2));
        locDet.setLocationLineSeqNo(Integer.valueOf(2));
        locDet.setLocationShipQty(BigDecimal.valueOf(25));
        po.getLocationDetails().add(locDet);
        
        
        System.out.println(po.amtOfStore("A"));
        System.out.println(po.amtOfStore("B"));
        
        
        PoInvGrnDnMatchingJob p = new PoInvGrnDnMatchingJob();
        
        System.out.println(p.calculatePoAmtByPoLocation(po, "A"));
        System.out.println(p.calculatePoAmtByPoLocation(po, "B"));
    }*/
    
    
    public BigDecimal amtOfStore(String storeCode) throws Exception
    {
        if ("2".equals(this.getPoHeader().getPoSubType()))
        {
            return this.getPoHeader().getTotalCost();
        }
        
        if ("1".equals(this.getPoHeader().getPoSubType()))
        {
            if (null == storeCode || storeCode.trim().isEmpty())
            {
                throw new IllegalArgumentException();
            }
            
            BigDecimal rlt = BigDecimal.ZERO;
            
            Integer locSeqNo = null;
            
            for (PoLocationHolder loc : this.getLocations())
            {
                if (loc.getLocationCode().equalsIgnoreCase(storeCode))
                {
                    locSeqNo = loc.getLineSeqNo();
                    break;
                }
            }
            
            
            for (PoDetailHolder detail : this.getDetails())
            {
                BigDecimal price = detail.getOrderBaseUnit().equals("P") ? detail
                    .getPackCost() : detail.getUnitCost();

                for (PoLocationDetailHolder locDetail : this.getLocationDetails())
                {
                    if (!locDetail.getLocationLineSeqNo().equals(locSeqNo))
                        continue;
                    
                    if (!locDetail.getDetailLineSeqNo().equals(detail.getLineSeqNo()))
                        continue;
                    
                    rlt = rlt.add(locDetail.getLocationShipQty().multiply(price));
                }
            }
            
            return rlt;
        }
        
        throw new Exception("Unkown po type.");
    }
    
    
    public BigDecimal calculateTotalQty()
    {
        BigDecimal rlt = BigDecimal.ZERO;
        
        if (null != details)
        {
            for (PoDetailHolder detail : details)
            {
                rlt = rlt.add(detail.getOrderQty());
            }
        }
        
        return rlt;
    }
    
    
    public List<PoDetailHolder> obtainPoDetailsByStore(String storeCode)
    {
        if (storeCode == null || storeCode.isEmpty())
        {
            return null;
        }
        Map<String, PoLocationHolder> locationMap = convertLocationsToMap();
        
        PoLocationHolder location = locationMap.get(storeCode);
        
        if (location == null)
        {
            return null;
        }
        
        Map<Integer, List<PoLocationDetailHolder>> locationDetailMap = convertLocationDetailsToMap();
        
        List<PoLocationDetailHolder> locationDetailList = locationDetailMap.get(location.getLineSeqNo());
        
        if (locationDetailList == null)
        {
            return null;
        }
        
        Map<Integer, PoDetailHolder> detailMap = convertDetailsToMap();
        
        List<PoDetailHolder> poDetailList = new ArrayList<PoDetailHolder>();
        
        for (PoLocationDetailHolder locationDetail : locationDetailList)
        {
            PoDetailHolder detail = detailMap.get(locationDetail.getDetailLineSeqNo());
            detail.setOrderQty(locationDetail.getLocationShipQty());
            detail.setFocQty(locationDetail.getLocationFocQty());
            poDetailList.add(detail);
        }
        
        
        return poDetailList;
    }

    
    public InvHolder toInvoice(SupplierHolder supplier, String invNo,
        String storeCode, TermConditionHolder termCondition, Integer storeCount, BigDecimal oid)
    {
        InvHolder invoice = new InvHolder();
        PoType poType = poHeader.getPoType();
        if(PoType.SOR.equals(poType))
        {
            if (PO_SUB_TYPE_1.equals(poHeader.getPoSubType()))
            {
                Map<String, PoLocationHolder> locationMap = this
                    .convertLocationsToMap();
                
                Map<Integer, List<PoLocationDetailHolder>> locationDetailMap = this
                    .convertLocationDetailsToMap();
                
                PoLocationHolder location = locationMap.get(storeCode);
                List<PoLocationDetailHolder> poLocationDetails = locationDetailMap
                    .get(location.getLineSeqNo());

                invoice.setInvDetailByPoLocation(poLocationDetails, details, oid, poType);
                BigDecimal invAmountNoVat = invoice.calculateInvAmountNoVat();
                
                PoStatus status = PoStatus.INVOICED;
                
                if (storeCount != null && storeCount > 1)  
                    status = PoStatus.PARTIAL_INVOICED;
                
                invoice.setInvHeaer(poHeader, supplier, invNo,invAmountNoVat, status, oid, poType);
                invoice.setShipInfoForInvHeaderByLocation(location);
                invoice.setTermCondition(termCondition);
                return invoice;
            }
            else if (PO_SUB_TYPE_2.equals(poHeader.getPoSubType()))
            {
                invoice.setInvDetailByPoDetails(details, oid, poType);
                BigDecimal invAmountNoVat = invoice.calculateInvAmountNoVat();
                invoice.setInvHeaer(poHeader, supplier,invNo,invAmountNoVat, PoStatus.INVOICED, oid, poType);
                invoice.setTermCondition(termCondition);
                return invoice;
            }
                
        }
        
        if(PoType.CON.equals(poType))
        {
            invoice.setInvDetailByPoDetails(details, oid, poType);
            
            if (detailExtendeds != null && !detailExtendeds.isEmpty())
            {
                invoice.setInvDetailByPoDetailExsForConPo(detailExtendeds, poType);
            }
            BigDecimal invAmountNoVat = BigDecimal.ZERO;
            for (InvDetailHolder detail : invoice.getDetails())
            {
                invAmountNoVat = invAmountNoVat.add(detail.getConTotalCost());
            }
            
            invoice.setInvHeaer(poHeader, supplier, invNo,invAmountNoVat, PoStatus.INVOICED, oid, poType);
            invoice.setTermCondition(termCondition);
            return invoice;
                
        }
//        else // other invoice.
//        {
//            
//        }
        
        return invoice;
    }
    
    
    public Map<String, PoLocationHolder> convertLocationsToMap()
    {
        Map<String,PoLocationHolder> map = new HashMap<String, PoLocationHolder>();
        for (PoLocationHolder location : locations)
        {
            map.put(location.getLocationCode(), location);
        }
        
        return map;
    }
    
    
    public Map<Integer, List<PoLocationDetailHolder>> convertLocationDetailsToMap()
    {
        Map<Integer,List<PoLocationDetailHolder>> map = new HashMap<Integer, List<PoLocationDetailHolder>>();
        for (PoLocationDetailHolder detail : locationDetails)
        {
            Integer key = detail.getLocationLineSeqNo();
            if (map.containsKey(key))
            {
                List<PoLocationDetailHolder> details = map.get(key);
                details.add(detail);
            }
            else
            {
                List<PoLocationDetailHolder> details = new ArrayList<PoLocationDetailHolder>();
                details.add(detail);
                map.put(key, details);
            }
        }
        
        return map;
    }
    
    public static Map<String, PoDetailHolder> convertPoDetailListToMapItemCodeAsKey(
            List<PoDetailHolder> poDetailList)
    {
        Map<String, PoDetailHolder> poDetailsMap = new HashMap<String, PoDetailHolder>();
        for (PoDetailHolder poDetail : poDetailList)
        {
            poDetailsMap.put(poDetail.getBuyerItemCode(), poDetail);
        }
        return poDetailsMap;
    }
    
    public Map<Integer, PoDetailHolder> convertDetailsToMap()
    {
        Map<Integer, PoDetailHolder> map = new HashMap<Integer, PoDetailHolder>();
        for (PoDetailHolder detail : details)
        {
            map.put(detail.getLineSeqNo(), detail);
        }
        
        return map;
    }
    
    
    //*****************************************************
    // setter and getter methods
    //*****************************************************
    
    public PoHeaderHolder getPoHeader()
    {
        return poHeader;
    }

    
    public void setPoHeader(PoHeaderHolder poHeader)
    {
        this.poHeader = poHeader;
    }
    

    public List<PoHeaderExtendedHolder> getHeaderExtendeds()
    {
        return headerExtendeds;
    }

    
    public void setHeaderExtendeds(List<PoHeaderExtendedHolder> headerExtendeds)
    {
        this.headerExtendeds = headerExtendeds;
    }

    
    public List<PoDetailHolder> getDetails()
    {
        return details;
    }

    
    public void setDetails(List<PoDetailHolder> details)
    {
        this.details = details;
    }

    
    public List<PoDetailExtendedHolder> getDetailExtendeds()
    {
        return detailExtendeds;
    }

    
    public void setDetailExtendeds(List<PoDetailExtendedHolder> detailExtendeds)
    {
        this.detailExtendeds = detailExtendeds;
    }

    
    public List<PoLocationHolder> getLocations()
    {
        return locations;
    }

    
    public void setLocations(List<PoLocationHolder> locations)
    {
        this.locations = locations;
    }

    
    public List<PoLocationDetailHolder> getLocationDetails()
    {
        return locationDetails;
    }

    
    public void setLocationDetails(List<PoLocationDetailHolder> locationDetails)
    {
        this.locationDetails = locationDetails;
    }

    
    public List<PoLocationDetailExtendedHolder> getPoLocDetailExtendeds()
    {
        return poLocDetailExtendeds;
    }


    public void setPoLocDetailExtendeds(List<PoLocationDetailExtendedHolder> poLocDetailExtendeds)
    {
        this.poLocDetailExtendeds = poLocDetailExtendeds;
    }


    public BigDecimal getOldPoOid()
    {
        return oldPoOid;
    }


    public void setOldPoOid(BigDecimal oldPoOid)
    {
        this.oldPoOid = oldPoOid;
    }


    public String getPdfName()
    {
        return pdfName;
    }


    public void setPdfName(String pdfName)
    {
        this.pdfName = pdfName;
    }


    public String getSupplierFileName()
    {
        return supplierFileName;
    }


    public void setSupplierFileName(String supplierFileName)
    {
        this.supplierFileName = supplierFileName;
    }


    public PoHeaderHolder getOldPoHeader()
    {
        return oldPoHeader;
    }


    public void setOldPoHeader(PoHeaderHolder oldPoHeader)
    {
        this.oldPoHeader = oldPoHeader;
    }


    @Override
    public String getCustomIdentification()
    {
        return poHeader == null ? null : poHeader.getCustomIdentification();
    }

    
    @Override
    public String getLogicalKey()
    {
        return poHeader.getLogicalKey();
    }
}
