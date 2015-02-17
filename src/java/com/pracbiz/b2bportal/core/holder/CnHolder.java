package com.pracbiz.b2bportal.core.holder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.CnType;

public class CnHolder extends BaseHolder
{
    private static final long serialVersionUID = 1L;
    
    private static final String HEX_FIELD_PER_START_DATE = "periodStartDate";
    private static final String HEX_FIELD_PER_END_DATE = "periodStartDate";
    private static final String HEX_FIELD_TYPE_DATE = "D";
    
    private CnHeaderHolder header;
    private List<CnHeaderExtendedHolder> headerExtendedList;
    private List<CnDetailHolder> detailList;
    private List<CnDetailExtendedHolder> detailExtendedList;
    
    public void setCnHeaderAndHeaderExtended(InvHeaderHolder invHeader, PoHeaderHolder poHeader, SupplierHolder supplier, BigDecimal oid, CnStatus cnStatus)
    {
        List<CnHeaderExtendedHolder> cnHeaderExs = new ArrayList<CnHeaderExtendedHolder>();
        CnHeaderHolder cnHeader = new CnHeaderHolder();
        
        cnHeader.setCnOid(oid);
        cnHeader.setCnNo(invHeader.getInvNo());
        cnHeader.setDocAction("A");
        cnHeader.setActionDate(invHeader.getActionDate());
        cnHeader.setCnType(CnType.CON);
        cnHeader.setCnDate(invHeader.getInvDate());
        cnHeader.setPoNo(invHeader.getPoNo());
        cnHeader.setPoDate(invHeader.getPoDate());
        
        cnHeader.setBuyerOid(invHeader.getBuyerOid());
        cnHeader.setBuyerCode(invHeader.getBuyerCode());
        cnHeader.setBuyerName(invHeader.getBuyerName());
        cnHeader.setBuyerAddr1(invHeader.getBuyerAddr1());
        cnHeader.setBuyerAddr2(invHeader.getBuyerAddr2());
        cnHeader.setBuyerAddr3(invHeader.getBuyerAddr3());
        cnHeader.setBuyerAddr4(invHeader.getBuyerAddr4());
        cnHeader.setBuyerCity(invHeader.getBuyerCity());
        cnHeader.setBuyerState(invHeader.getBuyerState());
        cnHeader.setBuyerCtryCode(invHeader.getBuyerCtryCode());
        cnHeader.setBuyerPostalCode(invHeader.getBuyerPostalCode());
        
        cnHeader.setSupplierOid(supplier.getSupplierOid());
        cnHeader.setSupplierCode(invHeader.getSupplierCode());
        cnHeader.setSupplierName(invHeader.getSupplierName());
        cnHeader.setSupplierAddr1(supplier.getAddress1());
        cnHeader.setSupplierAddr2(supplier.getAddress2());
        cnHeader.setSupplierAddr3(supplier.getAddress3());
        cnHeader.setSupplierAddr4(supplier.getAddress4());
        cnHeader.setSupplierCity(invHeader.getSupplierCity());
        cnHeader.setSupplierState(supplier.getState());
        cnHeader.setSupplierCtryCode(supplier.getCtryCode());
        cnHeader.setSupplierPostalCode(supplier.getPostalCode());
        
        cnHeader.setTotalCost(invHeader.getInvAmountNoVat());
        cnHeader.setTotalVat(invHeader.getVatAmount());
        cnHeader.setTotalCostWithVat(invHeader.getInvAmountWithVat());
        cnHeader.setVatRate(invHeader.getVatRate());
        cnHeader.setCtrlStatus(cnStatus);
        cnHeader.setDuplicate(false);
        
        cnHeader.setCnRemarks(invHeader.getInvRemarks());
        
        if (poHeader != null)
        {
            if (poHeader.getPeriodStartDate() != null)
            {
                CnHeaderExtendedHolder cnHeaderEx = new CnHeaderExtendedHolder();
                cnHeaderEx.setCnOid(oid);
                cnHeaderEx.setFieldName(HEX_FIELD_PER_START_DATE);
                cnHeaderEx.setFieldType(HEX_FIELD_TYPE_DATE);
                cnHeaderEx.setDateValue(poHeader.getPeriodStartDate());
                cnHeaderExs.add(cnHeaderEx);
            }
            
            if (poHeader.getPeriodEndDate() != null)
            {
                CnHeaderExtendedHolder cnHeaderEx = new CnHeaderExtendedHolder();
                cnHeaderEx.setCnOid(oid);
                cnHeaderEx.setFieldName(HEX_FIELD_PER_END_DATE);
                cnHeaderEx.setFieldType(HEX_FIELD_TYPE_DATE);
                cnHeaderEx.setDateValue(poHeader.getPeriodEndDate());
                cnHeaderExs.add(cnHeaderEx);
            }
        }
        this.header = cnHeader;
        this.headerExtendedList = cnHeaderExs;
    }
    
    
    public void setCnDetailAndDetailExtended(List<InvDetailHolder> invDetails, InvHeaderHolder invHeader, Map<Integer, List<PoDetailExtendedHolder>> poDetailExsMap, BigDecimal oid)
    {
        List<CnDetailHolder> cnDetails = new ArrayList<CnDetailHolder>();
        List<CnDetailExtendedHolder> cnDetailExs = new ArrayList<CnDetailExtendedHolder>();
        for (InvDetailHolder invDetail : invDetails)
        {
            CnDetailHolder cnDetail = new CnDetailHolder();
            cnDetail.setCnOid(oid);
            cnDetail.setLineSeqNo(invDetail.getLineSeqNo());
            cnDetail.setBuyerItemCode(invDetail.getBuyerItemCode());
            cnDetail.setBarcode(invDetail.getBarcode());
            cnDetail.setItemDesc(invDetail.getItemDesc());
            cnDetail.setBrand(invDetail.getBrand());
            cnDetail.setColourCode(invDetail.getColourCode());
            cnDetail.setColourDesc(invDetail.getColourDesc());
            cnDetail.setSizeCode(invDetail.getSizeCode());
            cnDetail.setSizeDesc(invDetail.getSizeDesc());
            cnDetail.setPoNo(invHeader.getPoNo());
            cnDetail.setPoDate(invHeader.getPoDate());
            cnDetail.setPackingFactor(invDetail.getPackingFactor());
            cnDetail.setCreditBaseUnit(invDetail.getInvBaseUnit());
            cnDetail.setCreditUom(invDetail.getInvUom());
            cnDetail.setCreditQty(invDetail.getInvQty());
            cnDetail.setUnitCost(invDetail.getUnitPrice());
            cnDetail.setCostDiscountAmount(invDetail.getDiscountAmount());
            cnDetail.setCostDiscountPercent(invDetail.getDiscountPercent());
            cnDetail.setNetUnitCost(invDetail.getNetPrice());
            cnDetail.setItemCost(invDetail.getItemAmount());
            cnDetail.setItemSharedCost(invDetail.getItemSharedCost());
            cnDetail.setRetailPrice(BigDecimal.ZERO);
            cnDetail.setItemRetailAmount(BigDecimal.ZERO);
            cnDetail.setLineRefNo(invDetail.getLineRefNo());
            cnDetail.setItemGrossCost(invDetail.getItemGrossAmount());
            
            cnDetails.add(cnDetail);
            
            
            if (poDetailExsMap.containsKey(invDetail.getLineSeqNo()))
            {
                for(PoDetailExtendedHolder poDetailEx : poDetailExsMap.get(invDetail.getLineSeqNo()))
                {
                    if (poDetailEx.getFieldName().equalsIgnoreCase("periodStartDate"))
                    {
                        CnDetailExtendedHolder  cnDetailExPeriodStartDate = this.initCnDetailExByPoDetailEx(poDetailEx, invDetail.getLineSeqNo(), oid);
                        cnDetailExs.add(cnDetailExPeriodStartDate);
                    }
                    
                    if (poDetailEx.getFieldName().equalsIgnoreCase("periodEndDate"))
                    {
                        CnDetailExtendedHolder cnDetailExPeriodEndDate = this.initCnDetailExByPoDetailEx(poDetailEx, invDetail.getLineSeqNo(), oid);
                        cnDetailExs.add(cnDetailExPeriodEndDate);
                    }
                }
            }
        }
        
        invHeader.getPoOid();
        this.detailList = cnDetails;
        this.detailExtendedList = cnDetailExs;
    }
    

    private CnDetailExtendedHolder initCnDetailExByPoDetailEx(PoDetailExtendedHolder poDetailEx, int lineSeqNo, BigDecimal oid)
    {
        CnDetailExtendedHolder cnDetailEx = new CnDetailExtendedHolder();
        cnDetailEx.setCnOid(oid);
        cnDetailEx.setFieldName(poDetailEx.getFieldName());
        cnDetailEx.setFieldType(poDetailEx.getFieldType());
        cnDetailEx.setLineSeqNo(lineSeqNo);
        cnDetailEx.setBoolValue(poDetailEx.getBoolValue());
        cnDetailEx.setStringValue(poDetailEx.getStringValue());
        cnDetailEx.setFloatValue(poDetailEx.getFloatValue());
        cnDetailEx.setDateValue(poDetailEx.getDateValue());
        cnDetailEx.setIntValue(poDetailEx.getIntValue());
        
        return cnDetailEx;
    }
    
    
    public CnHeaderHolder getHeader()
    {
        return header;
    }


    public void setHeader(CnHeaderHolder header)
    {
        this.header = header;
    }


    public List<CnHeaderExtendedHolder> getHeaderExtendedList()
    {
        return headerExtendedList;
    }


    public void setHeaderExtendedList(List<CnHeaderExtendedHolder> headerExList)
    {
        this.headerExtendedList = headerExList;
    }


    public List<CnDetailHolder> getDetailList()
    {
        return detailList;
    }


    public void setDetailList(List<CnDetailHolder> detailList)
    {
        this.detailList = detailList;
    }


    public List<CnDetailExtendedHolder> getDetailExtendedList()
    {
        return detailExtendedList;
    }


    public void setDetailExtendedList(
            List<CnDetailExtendedHolder> detailExtendedList)
    {
        this.detailExtendedList = detailExtendedList;
    }


    @Override
    public String getCustomIdentification()
    {
        return header.getCustomIdentification();
    }

}
