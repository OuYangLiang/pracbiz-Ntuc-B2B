package com.pracbiz.b2bportal.customized.unity.eai.backend.job.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.service.BusinessRuleService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author youwenwu
 */
public class CostDNGenerator extends DnGenerator
{
    public CostDNGenerator(BusinessRuleService businessRuleService)
    {
        super(businessRuleService);
    }


    public DnHeaderHolder computDnHeader(BigDecimal dnOid,
            GrnHeaderHolder grnHeader, InvHeaderHolder invHeader)
            throws Exception
    {
        DnHeaderHolder dnHeader = new DnHeaderHolder();
        dnHeader.setDnOid(dnOid);
        dnHeader.setDocAction("A");
        dnHeader.setActionDate(new Date());
        dnHeader.setDnType(DnType.CST_IOC.name());
        dnHeader.setDnDate(new Date());
        dnHeader.setPoNo(invHeader.getPoNo());
        dnHeader.setPoDate(invHeader.getPoDate());
        dnHeader.setInvNo(invHeader.getInvNo());
        dnHeader.setInvDate(invHeader.getInvDate());
        dnHeader.setBuyerOid(invHeader.getBuyerOid());
        dnHeader.setBuyerCode(invHeader.getBuyerCode());
        dnHeader.setBuyerName(invHeader.getBuyerName());
        dnHeader.setBuyerAddr1(invHeader.getBuyerAddr1());
        dnHeader.setBuyerAddr2(invHeader.getBuyerAddr2());
        dnHeader.setBuyerAddr3(invHeader.getBuyerAddr3());
        dnHeader.setBuyerAddr4(invHeader.getBuyerAddr4());
        dnHeader.setBuyerCity(invHeader.getBuyerCity());
        dnHeader.setBuyerState(invHeader.getBuyerState());
        dnHeader.setBuyerCtryCode(invHeader.getBuyerCtryCode());
        dnHeader.setBuyerPostalCode(invHeader.getBuyerPostalCode());
        dnHeader.setSupplierOid(invHeader.getSupplierOid());
        dnHeader.setSupplierCode(invHeader.getSupplierCode());
        dnHeader.setSupplierName(invHeader.getSupplierName());
        dnHeader.setSupplierAddr1(invHeader.getSupplierAddr1());
        dnHeader.setSupplierAddr2(invHeader.getSupplierAddr2());
        dnHeader.setSupplierAddr3(invHeader.getSupplierAddr3());
        dnHeader.setSupplierAddr4(invHeader.getSupplierAddr4());
        dnHeader.setSupplierCity(invHeader.getSupplierCity());
        dnHeader.setSupplierState(invHeader.getSupplierState());
        dnHeader.setSupplierCtryCode(invHeader.getSupplierCtryCode());
        dnHeader.setSupplierPostalCode(invHeader.getSupplierPostalCode());
        dnHeader.setDeptCode(invHeader.getDeptCode());
        dnHeader.setDeptName(invHeader.getDeptName());
        dnHeader.setSubDeptCode(invHeader.getSubDeptCode());
        dnHeader.setSubDeptName(invHeader.getSubDeptName());
        dnHeader.setVatRate(invHeader.getVatRate());
        dnHeader.setDnStatus(DnStatus.NEW);
        return dnHeader;
    }


    public List<DnDetailExHolder> computDnDetail(BigDecimal dnOid,
            DnHeaderHolder dnHeader, PoHolder poHolder, InvHolder invHolder,
            GrnHolder grnHolder) throws Exception
    {
        List<DnDetailExHolder> dnDetails = new ArrayList<DnDetailExHolder>();
        int index = 1;
        BigDecimal totalCost = BigDecimal.ZERO;
        for (InvDetailHolder invDetail : invHolder.getDetails())
        {
            String buyerItemCode = invDetail.getBuyerItemCode();
            for (PoDetailHolder poDetail : poHolder.getDetails())
            {
                if (poDetail.getBuyerItemCode().equals(buyerItemCode))
                {
                    BigDecimal poUnitPrice = "P".equals(poDetail
                            .getOrderBaseUnit()) ? poDetail.getPackCost()
                            : poDetail.getUnitCost();
                    if (invDetail.getUnitPrice().compareTo(poUnitPrice) > 0)
                    {
                        BigDecimal unitPrice = invDetail.getUnitPrice().subtract(poUnitPrice);
                        dnDetails.add(this.initDnDetail(dnOid, invHolder
                                .getHeader(), invDetail, index++, invDetail
                                .getInvQty(), unitPrice));
                        totalCost = totalCost.add(unitPrice.multiply(invDetail
                                .getInvQty()));
                    }
                    break;
                }
            }
        }
        dnHeader.setTotalCost(totalCost);
        dnHeader.setTotalVat(dnHeader.getTotalCost().multiply(
                dnHeader.getVatRate()).divide(BigDecimal.valueOf(100)));
        dnHeader.setTotalCostWithVat(dnHeader.getTotalCost().add(
                dnHeader.getTotalVat()));
        return dnDetails;
    }


    private DnDetailExHolder initDnDetail(BigDecimal dnOid,InvHeaderHolder invHeader,
            InvDetailHolder invDetail, Integer lineSeqNo, BigDecimal debitQty,
            BigDecimal unitCost)
    {
        DnDetailExHolder dnDetail = new DnDetailExHolder();
        dnDetail.setDnOid(dnOid);
        dnDetail.setLineSeqNo(lineSeqNo);
        dnDetail.setBuyerItemCode(invDetail.getBuyerItemCode());
        dnDetail.setSupplierItemCode(invDetail.getSupplierItemCode());
        dnDetail.setBarcode(invDetail.getBarcode());
        dnDetail.setItemDesc(invDetail.getItemDesc());
        dnDetail.setBrand(invDetail.getBrand());
        dnDetail.setColourCode(invDetail.getColourCode());
        dnDetail.setColourDesc(invDetail.getColourDesc());
        dnDetail.setSizeCode(invDetail.getSizeCode());
        dnDetail.setSizeDesc(invDetail.getSizeDesc());
        dnDetail.setPoNo(invHeader.getPoNo());
        dnDetail.setPoDate(invHeader.getPoDate());
        dnDetail.setInvNo(invHeader.getInvNo());
        dnDetail.setInvDate(invHeader.getInvDate());
        dnDetail.setPackingFactor(invDetail.getPackingFactor());
        dnDetail.setDebitBaseUnit(invDetail.getInvBaseUnit());
        dnDetail.setOrderUom(invDetail.getInvUom());
        dnDetail.setDebitQty(debitQty);
        dnDetail.setUnitCost(unitCost);
        dnDetail.setCostDiscountAmount(BigDecimal.ZERO);
        dnDetail.setCostDiscountPercent(BigDecimal.ZERO);
        dnDetail.setNetUnitCost(invDetail.getNetPrice());
        dnDetail.setItemCost(dnDetail.getDebitQty().multiply(
                dnDetail.getUnitCost()));

        return dnDetail;
    }


    @Override
    public boolean autoGenerateDn(BigDecimal buyerOid) throws Exception
    {
        return businessRuleService.isAutoGenCostDn(buyerOid);
    }

}
