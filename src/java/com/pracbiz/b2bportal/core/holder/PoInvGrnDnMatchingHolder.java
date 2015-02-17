package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.struts2.json.annotations.JSON;

import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingBuyerStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingInvStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingPriceStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingQtyStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingStatus;
import com.pracbiz.b2bportal.core.constants.PoInvGrnDnMatchingSupplierStatus;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class PoInvGrnDnMatchingHolder extends BaseHolder
{
    private static final long serialVersionUID = -6301118999045591879L;

    private BigDecimal matchingOid;

    private BigDecimal buyerOid;

    private String buyerCode;

    private String buyerName;

    private BigDecimal supplierOid;

    private String supplierCode;

    private String supplierName;

    private BigDecimal poOid;

    private String poStoreCode;
    
    private String poStoreName;

    private String poNo;

    private Date poDate;

    private BigDecimal poAmt;

    private BigDecimal invOid;

    private String invNo;

    private Date invDate;

    private BigDecimal invAmt;

    private BigDecimal dnOid;

    private String dnNo;

    private Date dnDate;

    private BigDecimal dnAmt;

    private Date createDate;
    
    private PoInvGrnDnMatchingStatus matchingStatus;
    private Date matchingDate;
    
    private PoInvGrnDnMatchingInvStatus invStatus;
    private Date invStatusActionDate;
    private String invStatusActionRemarks;
    private String invStatusActionBy;

    private PoInvGrnDnMatchingSupplierStatus supplierStatus;
    private Date supplierStatusActionDate;
    private String supplierStatusActionRemarks;
    private String supplierStatusActionBy;

    private PoInvGrnDnMatchingQtyStatus qtyStatus;

    private PoInvGrnDnMatchingPriceStatus priceStatus;

    private PoInvGrnDnMatchingBuyerStatus buyerStatus;

    private List<PoInvGrnDnMatchingGrnHolder> grnList;
    private List<PoInvGrnDnMatchingDetailExHolder> detailList;

    private Date createDateFrom;

    private Date createDateTo;
    
    private Date invStatusActionDateFrom;
    
    private Date invStatusActionDateTo;
    
    private Date supplierStatusActionDateFrom;
    
    private Date supplierStatusActionDateTo;
    
    private Date matchingDateFrom;
    
    private Date matchingDateTo;
    
    private BigDecimal listOid;

    private Boolean closed;
    
    private Date closedDate;
    
    private String closedBy;
    
    private String closedRemarks;
    
    private Boolean revised;
    
    private Date revisedDate;
    
    private Boolean acceptFlag;


    public PoInvGrnDnMatchingHolder()
    {
        // default constructor
    }


    public PoInvGrnDnMatchingHolder(PoHolder poHolder, String store, String storeName,
            BigDecimal poAmt, InvHolder invHolder,
            List<GrnHolder> grnHolderList, DnHolder dnHolder)
    {
        PoHeaderHolder poHeader = poHolder.getPoHeader();
        this.buyerOid = poHeader.getBuyerOid();
        this.buyerCode = poHeader.getBuyerCode();
        this.buyerName = poHeader.getBuyerName();

        this.supplierOid = poHeader.getSupplierOid();
        this.supplierCode = poHeader.getSupplierCode();
        this.supplierName = poHeader.getSupplierName();

        this.poOid = poHeader.getPoOid();
        this.poStoreCode = store;
        this.poStoreName = storeName;
        this.poNo = poHeader.getPoNo();
        this.poDate = poHeader.getPoDate();
        this.poAmt = poAmt;

        if (invHolder != null && invHolder.getHeader() != null)
        {
            InvHeaderHolder invHeader = invHolder.getHeader();
            this.invOid = invHeader.getInvOid();
            this.invNo = invHeader.getInvNo();
            this.invDate = invHeader.getInvDate();
            this.invAmt = invHeader.getInvAmountNoVat();
        }

        if (null != dnHolder && dnHolder.getDnHeader() != null)
        {
            DnHeaderHolder dnHeader = dnHolder.getDnHeader();
            this.dnOid = dnHeader.getDnOid();
            this.dnNo = dnHeader.getDnNo();
            this.dnDate = dnHeader.getDnDate();
            this.dnAmt = dnHeader.getTotalCost();
        }

        if (grnHolderList != null && !grnHolderList.isEmpty())
        {
            this.grnList = new ArrayList<PoInvGrnDnMatchingGrnHolder>();

            for (GrnHolder grnHolder : grnHolderList)
            {
                GrnHeaderHolder grnHeader = grnHolder.getHeader();
                PoInvGrnDnMatchingGrnHolder pigdmg = new PoInvGrnDnMatchingGrnHolder();
                pigdmg.setGrnOid(grnHeader.getGrnOid());
                pigdmg.setGrnNo(grnHeader.getGrnNo());
                pigdmg.setGrnDate(grnHeader.getGrnDate());
                pigdmg.setGrnAmt(grnHeader.getTotalCost());

                this.grnList.add(pigdmg);
            }
        }
        
        this.initDetail(poHolder, invHolder, grnHolderList, dnHolder, store);

        this.createDate = new Date();
        this.matchingStatus = PoInvGrnDnMatchingStatus.PENDING;
        this.invStatus = PoInvGrnDnMatchingInvStatus.PENDING;
        this.supplierStatus = PoInvGrnDnMatchingSupplierStatus.PENDING;
        this.priceStatus = PoInvGrnDnMatchingPriceStatus.PENDING;
        this.qtyStatus = PoInvGrnDnMatchingQtyStatus.PENDING;
        this.buyerStatus = PoInvGrnDnMatchingBuyerStatus.PENDING;
        this.closed = false;
        this.revised = false;
        this.acceptFlag = false;
    }
    
    
    private void initDetail(PoHolder poHolder, InvHolder invHolder,
            List<GrnHolder> grnHolderList, DnHolder dnHolder, String storeCode)
    {
        List<PoDetailHolder> poDetailList = retrievePoDetail(poHolder,
                storeCode);
        List<GrnDetailHolder> grnDetailList = retrieveGrnDetail(grnHolderList);
        List<InvDetailHolder> invDetailList = invHolder == null ? null
                : invHolder.getDetails();
        List<DnDetailExHolder> dnDetailList = dnHolder == null ? null : dnHolder
                .getDnDetail();

        Map<String, PoDetailHolder> poDetailsMap = PoHolder
                .convertPoDetailListToMapItemCodeAsKey(poDetailList);
        Map<String, InvDetailHolder> invDetailsMap = InvHolder
                .convertInvDetailListToMapItemCodeAsKey(invDetailList);
        Map<String, GrnDetailHolder> grnDetailsMap = GrnHolder
                .convertGrnDetailListToMapItemCodeAsKey(grnDetailList);
        Map<String, DnDetailHolder> dnDetailsMap = DnHolder
                .convertDnDetailListToMapItemCodeAsKey(dnDetailList);
        String key = poDetailList.get(0).getBuyerItemCode();
        List<PoInvGrnDnMatchingDetailExHolder> details = new ArrayList<PoInvGrnDnMatchingDetailExHolder>();

        BigDecimal poAmt = BigDecimal.ZERO;
        while (!((poDetailsMap == null || poDetailsMap.isEmpty())
                && (invDetailsMap == null || invDetailsMap.isEmpty())
                && (grnDetailsMap == null || grnDetailsMap.isEmpty()) && (dnDetailsMap == null || dnDetailsMap
                .isEmpty())))
        {
            PoInvGrnDnMatchingDetailExHolder detail = new PoInvGrnDnMatchingDetailExHolder();
            PoDetailHolder poDetail = null;
            poDetail = poDetailsMap.get(key);
            if (poDetail != null)
            {
                detail.setPoQty(poDetail.getOrderQty());
                detail.setPoPrice(poDetail.getOrderBaseUnit().equals(
                        "P") ? poDetail.getPackCost() : poDetail
                        .getUnitCost());
                detail.setPoAmt(detail.getPoQty().multiply(
                        detail.getPoPrice()));
                poAmt = poAmt.add(detail.getPoAmt());
                poDetailsMap.remove(key);
            }

            InvDetailHolder invDetail = null;
            if (invDetailsMap != null)
            {
                invDetail = invDetailsMap.get(key);
                if (invDetail != null)
                {
                    detail.setInvPrice(invDetail.getUnitPrice());
                    detail.setInvQty(invDetail.getInvQty());
                    detail.setInvAmt(invDetail.getItemAmount());
                    invDetailsMap.remove(key);
                }
            }
            GrnDetailHolder grnDetail = null;
            if (grnDetailsMap != null)
            {
                grnDetail = grnDetailsMap.get(key);
                if (grnDetail != null)
                {
                    detail.setGrnQty(grnDetail.getReceiveQty());
                    detail
                            .setExpInvAmt((detail.getPoPrice() == null ? BigDecimal.ZERO
                                    : detail.getPoPrice()).multiply(detail
                                    .getGrnQty() == null ? BigDecimal.ZERO
                                    : detail.getGrnQty()));

                    grnDetailsMap.remove(key);
                }
            }
            DnDetailHolder dnDetail = null;
            if (dnDetailsMap != null)
            {
                dnDetail = dnDetailsMap.get(key);
                if (dnDetail != null)
                {
                    detail.setDnAmt(dnDetail.getItemCost());
                    dnDetailsMap.remove(key);
                }
            }

            detail.setBuyerItemCode(key);
            initCommonItemInfoForDetail(detail, poDetail, invDetail,
                    grnDetail, dnDetail);
            detail.setSeq(details.size() + 1);
            detail.setPriceStatus(PoInvGrnDnMatchingPriceStatus.PENDING);
            detail.setQtyStatus(PoInvGrnDnMatchingQtyStatus.PENDING);
            details.add(detail);

            key = retrieveKeyForItemCycle(poDetailsMap, invDetailsMap,
                    grnDetailsMap, dnDetailsMap);

        }
        this.setPoAmt(poAmt);

        this.detailList = details;
    }
    
    
    private List<GrnDetailHolder> retrieveGrnDetail(
            List<GrnHolder> grnHolderList)
    {
        if (grnHolderList == null || grnHolderList.isEmpty())
        {
            return null;
        }
        List<GrnDetailHolder> grnDetailList = new ArrayList<GrnDetailHolder>();
        for (GrnHolder grnHolder : grnHolderList)
        {
            grnDetailList.addAll(grnHolder.getDetails());
        }
        Map<String, List<GrnDetailHolder>> grnDetailListMap = new HashMap<String, List<GrnDetailHolder>>();
        for (GrnDetailHolder grnDetail : grnDetailList)
        {
            if (grnDetailListMap.containsKey(grnDetail.getBuyerItemCode()))
            {
                List<GrnDetailHolder> grnDetails = grnDetailListMap
                        .get(grnDetail.getBuyerItemCode());
                grnDetails.add(grnDetail);
                grnDetailListMap.put(grnDetail.getBuyerItemCode(), grnDetails);
            }
            else
            {
                List<GrnDetailHolder> grnDetails = new ArrayList<GrnDetailHolder>();
                grnDetails.add(grnDetail);
                grnDetailListMap.put(grnDetail.getBuyerItemCode(), grnDetails);
            }
        }
        List<GrnDetailHolder> rlt = new ArrayList<GrnDetailHolder>();
        for (Entry<String, List<GrnDetailHolder>> entry : grnDetailListMap
                .entrySet())
        {
            List<GrnDetailHolder> grnDetails = entry.getValue();
            if (grnDetails.size() == 1)
            {
                rlt.add(grnDetails.get(0));
                continue;
            }
            BigDecimal qty = BigDecimal.ZERO;
            for (GrnDetailHolder grnDetail : grnDetails)
            {
                qty = qty.add(grnDetail.getReceiveQty());
            }
            GrnDetailHolder grnDetail = grnDetails.get(0);
            grnDetail.setReceiveQty(qty);
            rlt.add(grnDetail);
        }
        return rlt;
    }


    private List<PoDetailHolder> retrievePoDetail(PoHolder poHolder,
            String storeCode)
    {
        if ("1".equals(poHolder.getPoHeader().getPoSubType()))
        {
            Map<String, PoLocationHolder> poLocMap = convertPoLocationsListToMap(poHolder
                    .getLocations());
            PoLocationHolder poLoc = poLocMap.get(storeCode);

            Map<Integer, List<PoLocationDetailHolder>> poLocDetailMap = convertPoLocationDetailListToMap(poHolder
                    .getLocationDetails());

            List<PoLocationDetailHolder> poLocDetailList = poLocDetailMap
                    .get(poLoc.getLineSeqNo());

            Map<Integer, PoDetailHolder> poDetailMap = convertPoDetailListToMap(poHolder
                    .getDetails());
            List<PoDetailHolder> rlt = new ArrayList<PoDetailHolder>();
            for (PoLocationDetailHolder poLocDetail : poLocDetailList)
            {
                PoDetailHolder poDetail = poDetailMap.get(poLocDetail
                        .getDetailLineSeqNo());
                poDetail.setOrderQty(poLocDetail.getLocationShipQty());
                rlt.add(poDetail);
            }
            return rlt;
        }
        else if ("2".equals(poHolder.getPoHeader().getPoSubType()))
        {
            return poHolder.getDetails();
        }
        return null;
    }


    private Map<Integer, PoDetailHolder> convertPoDetailListToMap(
            List<PoDetailHolder> poDetailList)
    {
        if (poDetailList == null || poDetailList.isEmpty())
        {
            return null;
        }
        Map<Integer, PoDetailHolder> poDetailMap = new HashMap<Integer, PoDetailHolder>();
        for (PoDetailHolder poDetail : poDetailList)
        {
            poDetailMap.put(poDetail.getLineSeqNo(), poDetail);
        }
        return poDetailMap;
    }


    private Map<Integer, List<PoLocationDetailHolder>> convertPoLocationDetailListToMap(
            List<PoLocationDetailHolder> poLocDetails)
    {
        if (poLocDetails == null || poLocDetails.isEmpty())
        {
            return null;
        }
        Map<Integer, List<PoLocationDetailHolder>> rlt = new HashMap<Integer, List<PoLocationDetailHolder>>();
        for (PoLocationDetailHolder poLocDetail : poLocDetails)
        {
            if (rlt.containsKey(poLocDetail.getLocationLineSeqNo()))
            {
                List<PoLocationDetailHolder> poLocDetailList = rlt
                        .get(poLocDetail.getLocationLineSeqNo());
                poLocDetailList.add(poLocDetail);
                rlt.put(poLocDetail.getLocationLineSeqNo(), poLocDetailList);
            }
            else
            {
                List<PoLocationDetailHolder> poLocDetailList = new ArrayList<PoLocationDetailHolder>();
                poLocDetailList.add(poLocDetail);
                rlt.put(poLocDetail.getLocationLineSeqNo(), poLocDetailList);
            }
        }
        return rlt;
    }


    private Map<String, PoLocationHolder> convertPoLocationsListToMap(
            List<PoLocationHolder> poLocs)
    {
        if (poLocs == null || poLocs.isEmpty())
        {
            return null;
        }
        Map<String, PoLocationHolder> poLocMap = new HashMap<String, PoLocationHolder>();
        for (PoLocationHolder poLoc : poLocs)
        {
            poLocMap.put(poLoc.getLocationCode(), poLoc);
        }
        return poLocMap;
    }
    
    private void initCommonItemInfoForDetail(
            PoInvGrnDnMatchingDetailHolder detail, PoDetailHolder poDetail,
            InvDetailHolder invDetail, GrnDetailHolder grnDetail,
            DnDetailHolder dnDetail)
    {
        if (poDetail != null)
        {
            detail.setItemDesc(poDetail.getItemDesc());
            detail.setBarcode(poDetail.getBarcode());
            detail.setUom(poDetail.getOrderUom());
            return;
        }
        if (invDetail != null)
        {
            detail.setItemDesc(invDetail.getItemDesc());
            detail.setBarcode(invDetail.getBarcode());
            detail.setUom(invDetail.getInvUom());
            return;
        }
        if (grnDetail != null)
        {
            detail.setItemDesc(grnDetail.getItemDesc());
            detail.setBarcode(grnDetail.getBarcode());
            detail.setUom(grnDetail.getOrderUom());
            return;
        }
        if (dnDetail != null)
        {
            detail.setItemDesc(dnDetail.getItemDesc());
            detail.setBarcode(dnDetail.getBarcode());
            detail.setUom(dnDetail.getOrderUom());
            return;
        }
    }


    private String retrieveKeyForItemCycle(
            Map<String, PoDetailHolder> poDetailsMap,
            Map<String, InvDetailHolder> invDetailsMap,
            Map<String, GrnDetailHolder> grnDetailsMap,
            Map<String, DnDetailHolder> dnDetailsMap)
    {
        if (poDetailsMap != null && !poDetailsMap.isEmpty())
        {
            return (String) poDetailsMap.keySet().toArray()[0];
        }
        if (invDetailsMap != null && !invDetailsMap.isEmpty())
        {
            return (String) invDetailsMap.keySet().toArray()[0];
        }
        if (grnDetailsMap != null && !grnDetailsMap.isEmpty())
        {
            return (String) grnDetailsMap.keySet().toArray()[0];
        }
        if (dnDetailsMap != null && !dnDetailsMap.isEmpty())
        {
            return (String) dnDetailsMap.keySet().toArray()[0];
        }
        return null;
    }
    
    
    public boolean existPriceInvLessPo()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return false;
        }
        
        for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
        {
            BigDecimal poPrice = detail.getPoPrice() == null ? BigDecimal.ZERO : detail.getPoPrice();
            BigDecimal invPrice = detail.getInvPrice() == null ? BigDecimal.ZERO : detail.getInvPrice();
            
            if (poPrice.compareTo(invPrice) > 0)
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean existQtyGrnMoreThanInv()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return false;
        }
        
        for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
        {
            BigDecimal grnQty = detail.getGrnQty() == null ? BigDecimal.ZERO : detail.getGrnQty();
            BigDecimal invQty = detail.getInvQty() == null ? BigDecimal.ZERO : detail.getInvQty();
            
            if (grnQty.compareTo(invQty) > 0)
            {
                return true;
            }
        }
        
        return false;
    }
    
    
    public boolean isPriceAuditFinished()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return true;
        }
        for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
        {
            if (detail.getPriceStatus() == null || detail.getPriceStatus().equals(PoInvGrnDnMatchingPriceStatus.PENDING))
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    public boolean isQtyAuditFinished()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return true;
        }
        for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
        {
            if (detail.getQtyStatus() == null || detail.getQtyStatus().equals(PoInvGrnDnMatchingQtyStatus.PENDING))
            {
                return false;
            }
        }
        
        return true;
    }
    
    
    public PoInvGrnDnMatchingPriceStatus computePriceStatus()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return PoInvGrnDnMatchingPriceStatus.PENDING;
        }
        if (isPriceAuditFinished())
        {
            for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
            {
                if (detail.getPriceStatus().equals(PoInvGrnDnMatchingPriceStatus.REJECTED))
                {
                    return PoInvGrnDnMatchingPriceStatus.REJECTED;
                }
            }
            
            return PoInvGrnDnMatchingPriceStatus.ACCEPTED;
        }
        
        return PoInvGrnDnMatchingPriceStatus.PENDING;
    }
    
    
    public PoInvGrnDnMatchingQtyStatus computeQtyStatus()
    {
        if (detailList == null || detailList.isEmpty())
        {
            return PoInvGrnDnMatchingQtyStatus.PENDING;
        }
        if (isQtyAuditFinished())
        {
            for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
            {
                if (detail.getQtyStatus().equals(PoInvGrnDnMatchingQtyStatus.REJECTED))
                {
                    return PoInvGrnDnMatchingQtyStatus.REJECTED;
                }
            }
            
            return PoInvGrnDnMatchingQtyStatus.ACCEPTED;
        }
        
        return PoInvGrnDnMatchingQtyStatus.PENDING;
    }
    
    
    public PoInvGrnDnMatchingBuyerStatus computeBuyerStatus()
    {
        if (priceStatus.equals(PoInvGrnDnMatchingPriceStatus.REJECTED) 
                || qtyStatus.equals(PoInvGrnDnMatchingQtyStatus.REJECTED))
        {
            return PoInvGrnDnMatchingBuyerStatus.REJECTED;
        }
        else if (priceStatus.equals(PoInvGrnDnMatchingPriceStatus.ACCEPTED) 
                && qtyStatus.equals(PoInvGrnDnMatchingQtyStatus.ACCEPTED))
        {
            return PoInvGrnDnMatchingBuyerStatus.ACCEPTED;
        }
        else
        {
            return PoInvGrnDnMatchingBuyerStatus.PENDING;
        }
    }
    
    
    public void computeBuyerSideStatus()
    {
        this.priceStatus = computePriceStatus();
        this.qtyStatus = computeQtyStatus();
        this.buyerStatus = computeBuyerStatus();
    }


    public BigDecimal getMatchingOid()
    {
        return matchingOid;
    }


    public void setMatchingOid(BigDecimal matchingOid)
    {
        this.matchingOid = matchingOid;
    }


    public BigDecimal getBuyerOid()
    {
        return buyerOid;
    }


    public void setBuyerOid(BigDecimal buyerOid)
    {
        this.buyerOid = buyerOid;
    }


    public String getBuyerCode()
    {
        return buyerCode;
    }


    public void setBuyerCode(String buyerCode)
    {
        this.buyerCode = buyerCode;
    }


    public String getBuyerName()
    {
        return buyerName;
    }


    public void setBuyerName(String buyerName)
    {
        this.buyerName = buyerName;
    }


    public BigDecimal getSupplierOid()
    {
        return supplierOid;
    }


    public void setSupplierOid(BigDecimal supplierOid)
    {
        this.supplierOid = supplierOid;
    }


    public String getSupplierCode()
    {
        return supplierCode;
    }


    public void setSupplierCode(String supplierCode)
    {
        this.supplierCode = supplierCode;
    }


    public String getSupplierName()
    {
        return supplierName;
    }


    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }


    public BigDecimal getPoOid()
    {
        return poOid;
    }


    public void setPoOid(BigDecimal poOid)
    {
        this.poOid = poOid;
    }


    public String getPoStoreCode()
    {
        return poStoreCode;
    }


    public void setPoStoreCode(String poStoreCode)
    {
        this.poStoreCode = poStoreCode;
    }


    public String getPoStoreName()
    {
        return poStoreName;
    }


    public void setPoStoreName(String poStoreName)
    {
        this.poStoreName = poStoreName;
    }


    public String getPoNo()
    {
        return poNo;
    }


    public void setPoNo(String poNo)
    {
        this.poNo = poNo;
    }


    @JSON(format = "yyyy-MM-dd")
    public Date getPoDate()
    {
        return poDate == null ? null : (Date) poDate.clone();
    }


    public void setPoDate(Date poDate)
    {
        this.poDate = poDate == null ? null : (Date) poDate.clone();
    }


    public BigDecimal getPoAmt()
    {
        return poAmt;
    }


    public void setPoAmt(BigDecimal poAmt)
    {
        this.poAmt = poAmt;
    }


    public BigDecimal getInvOid()
    {
        return invOid;
    }


    public void setInvOid(BigDecimal invOid)
    {
        this.invOid = invOid;
    }


    public String getInvNo()
    {
        return invNo;
    }


    public void setInvNo(String invNo)
    {
        this.invNo = invNo;
    }


    @JSON(format = "yyyy-MM-dd")
    public Date getInvDate()
    {
        return invDate == null ? null : (Date) invDate.clone();
    }


    public void setInvDate(Date invDate)
    {
        this.invDate = invDate == null ? null : (Date) invDate.clone();
    }


    public BigDecimal getInvAmt()
    {
        return invAmt;
    }


    public void setInvAmt(BigDecimal invAmt)
    {
        this.invAmt = invAmt;
    }


    public BigDecimal getDnOid()
    {
        return dnOid;
    }


    public void setDnOid(BigDecimal dnOid)
    {
        this.dnOid = dnOid;
    }


    public String getDnNo()
    {
        return dnNo;
    }


    public void setDnNo(String dnNo)
    {
        this.dnNo = dnNo;
    }


    @JSON(format = "yyyy-MM-dd")
    public Date getDnDate()
    {
        return dnDate == null ? null : (Date) dnDate.clone();
    }


    public void setDnDate(Date dnDate)
    {
        this.dnDate = dnDate == null ? null : (Date) dnDate.clone();
    }


    public BigDecimal getDnAmt()
    {
        return dnAmt;
    }


    public void setDnAmt(BigDecimal dnAmt)
    {
        this.dnAmt = dnAmt;
    }


    public PoInvGrnDnMatchingStatus getMatchingStatus()
    {
        return matchingStatus;
    }


    public void setMatchingStatus(PoInvGrnDnMatchingStatus matchingStatus)
    {
        this.matchingStatus = matchingStatus;
    }


    public PoInvGrnDnMatchingInvStatus getInvStatus()
    {
        return invStatus;
    }


    public void setInvStatus(PoInvGrnDnMatchingInvStatus invStatus)
    {
        this.invStatus = invStatus;
    }


    public Date getInvStatusActionDate()
    {
        return invStatusActionDate == null ? null : (Date) invStatusActionDate
                .clone();
    }


    public void setInvStatusActionDate(Date invStatusActionDate)
    {
        this.invStatusActionDate = invStatusActionDate == null ? null
                : (Date) invStatusActionDate.clone();
    }


    public String getInvStatusActionRemarks()
    {
        return invStatusActionRemarks;
    }


    public void setInvStatusActionRemarks(String invStatusActionRemarks)
    {
        this.invStatusActionRemarks = invStatusActionRemarks;
    }


    public String getInvStatusActionBy()
    {
        return invStatusActionBy;
    }


    public void setInvStatusActionBy(String invStatusActionBy)
    {
        this.invStatusActionBy = invStatusActionBy;
    }


    public Date getCreateDate()
    {
        return createDate == null ? null : (Date) createDate.clone();
    }


    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate == null ? null : (Date) createDate.clone();
    }

    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getMatchingDate()
    {
        return matchingDate == null ? null : (Date) matchingDate.clone();
    }


    public void setMatchingDate(Date matchingDate)
    {
        this.matchingDate = matchingDate == null ? null : (Date) matchingDate
                .clone();
    }


    public PoInvGrnDnMatchingSupplierStatus getSupplierStatus()
    {
        return supplierStatus;
    }


    public void setSupplierStatus(
            PoInvGrnDnMatchingSupplierStatus supplierStatus)
    {
        this.supplierStatus = supplierStatus;
    }


    public Date getSupplierStatusActionDate()
    {
        return supplierStatusActionDate == null ? null
                : (Date) supplierStatusActionDate.clone();
    }


    public void setSupplierStatusActionDate(Date supplierStatusActionDate)
    {
        this.supplierStatusActionDate = supplierStatusActionDate == null ? null
                : (Date) supplierStatusActionDate.clone();
    }


    public String getSupplierStatusActionRemarks()
    {
        return supplierStatusActionRemarks;
    }


    public void setSupplierStatusActionRemarks(
            String supplierStatusActionRemarks)
    {
        this.supplierStatusActionRemarks = supplierStatusActionRemarks;
    }


    public String getSupplierStatusActionBy()
    {
        return supplierStatusActionBy;
    }


    public void setSupplierStatusActionBy(String supplierStatusActionBy)
    {
        this.supplierStatusActionBy = supplierStatusActionBy;
    }


    public PoInvGrnDnMatchingQtyStatus getQtyStatus()
    {
        return qtyStatus;
    }


    public void setQtyStatus(PoInvGrnDnMatchingQtyStatus qtyStatus)
    {
        this.qtyStatus = qtyStatus;
    }


    public PoInvGrnDnMatchingPriceStatus getPriceStatus()
    {
        return priceStatus;
    }


    public void setPriceStatus(PoInvGrnDnMatchingPriceStatus priceStatus)
    {
        this.priceStatus = priceStatus;
    }


    public PoInvGrnDnMatchingBuyerStatus getBuyerStatus()
    {
        return buyerStatus;
    }


    public void setBuyerStatus(PoInvGrnDnMatchingBuyerStatus buyerStatus)
    {
        this.buyerStatus = buyerStatus;
    }


    public List<PoInvGrnDnMatchingGrnHolder> getGrnList()
    {
        return grnList;
    }
    

    public void setGrnList(List<PoInvGrnDnMatchingGrnHolder> grnList)
    {
        this.grnList = grnList;
    }


    public List<PoInvGrnDnMatchingDetailExHolder> getDetailList()
    {
        return detailList;
    }


    public void setDetailList(List<PoInvGrnDnMatchingDetailExHolder> detailList)
    {
        this.detailList = detailList;
    }


    public Date getCreateDateFrom()
    {
        return createDateFrom == null ? null : (Date) createDateFrom.clone();
    }


    public void setCreateDateFrom(Date createDateFrom)
    {
        this.createDateFrom = createDateFrom == null ? null : (Date) createDateFrom.clone();
    }


    public Date getCreateDateTo()
    {
        return createDateTo == null ? null : (Date) createDateTo.clone();
    }


    public void setCreateDateTo(Date createDateTo)
    {
        this.createDateTo = createDateTo == null ? null : (Date) createDateTo.clone();
    }


    public BigDecimal getListOid()
    {
        return listOid;
    }


    public void setListOid(BigDecimal listOid)
    {
        this.listOid = listOid;
    }


    public Boolean getClosed()
    {
        return closed;
    }


    public void setClosed(Boolean closed)
    {
        this.closed = closed;
    }


    public Date getInvStatusActionDateFrom()
    {
        return invStatusActionDateFrom == null ? null : (Date) invStatusActionDateFrom.clone();
    }


    public void setInvStatusActionDateFrom(Date invStatusActionDateFrom)
    {
        this.invStatusActionDateFrom = invStatusActionDateFrom == null ? null : (Date) invStatusActionDateFrom.clone();
    }


    public Date getInvStatusActionDateTo()
    {
        return invStatusActionDateTo == null ? null : (Date) invStatusActionDateTo.clone();
    }


    public void setInvStatusActionDateTo(Date invStatusActionDateTo)
    {
        this.invStatusActionDateTo = invStatusActionDateTo == null ? null : (Date) invStatusActionDateTo.clone();
    }


    public Date getSupplierStatusActionDateFrom()
    {
        return supplierStatusActionDateFrom == null ? null : (Date) supplierStatusActionDateFrom.clone();
    }


    public void setSupplierStatusActionDateFrom(Date supplierStatusActionDateFrom)
    {
        this.supplierStatusActionDateFrom = supplierStatusActionDateFrom == null ? null : (Date) supplierStatusActionDateFrom.clone();
    }


    public Date getSupplierStatusActionDateTo()
    {
        return supplierStatusActionDateTo == null ? null : (Date) supplierStatusActionDateTo.clone();
    }


    public void setSupplierStatusActionDateTo(Date supplierStatusActionDateTo)
    {
        this.supplierStatusActionDateTo = supplierStatusActionDateTo == null ? null : (Date) supplierStatusActionDateTo.clone();
    }


    public Date getMatchingDateFrom()
    {
        return matchingDateFrom == null ? null : (Date) matchingDateFrom.clone();
    }


    public void setMatchingDateFrom(Date matchingDateFrom)
    {
        this.matchingDateFrom = matchingDateFrom == null ? null : (Date) matchingDateFrom.clone();
    }


    public Date getMatchingDateTo()
    {
        return matchingDateTo == null ? null : (Date) matchingDateTo.clone();
    }


    public void setMatchingDateTo(Date matchingDateTo)
    {
        this.matchingDateTo = matchingDateTo == null ? null : (Date) matchingDateTo.clone();
    }
    

    public Boolean getRevised()
    {
        return revised;
    }


    public void setRevised(Boolean revised)
    {
        this.revised = revised;
    }


    public Date getRevisedDate()
    {
        return revisedDate == null ? null : (Date) revisedDate.clone();
    }


    public void setRevisedDate(Date revisedDate)
    {
        this.revisedDate = revisedDate == null ? null : (Date) revisedDate.clone();;
    }


    @JSON(format = CoreCommonConstants.SUMMARY_DATA_FORMAT)
    public Date getClosedDate()
    {
        return closedDate == null ? null : (Date) closedDate.clone();
    }


    public void setClosedDate(Date closedDate)
    {
        this.closedDate = closedDate == null ? null : (Date) closedDate.clone();
    }


    public String getClosedBy()
    {
        return closedBy;
    }


    public void setClosedBy(String closedBy)
    {
        this.closedBy = closedBy;
    }


    public String getClosedRemarks()
    {
        return closedRemarks;
    }


    public void setClosedRemarks(String closedRemarks)
    {
        this.closedRemarks = closedRemarks;
    }


    public Boolean getAcceptFlag()
    {
        return acceptFlag;
    }


    public void setAcceptFlag(Boolean acceptFlag)
    {
        this.acceptFlag = acceptFlag;
    }


    @Override
    public String getCustomIdentification()
    {
        return String.valueOf(matchingOid);
    }
    
    
    @Override
    public String getLogicalKey()
    {
        return poNo + "_" + poStoreCode;
    }
}