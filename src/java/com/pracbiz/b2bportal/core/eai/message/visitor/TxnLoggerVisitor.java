//*****************************************************************************
//
// File Name       :  TxnLoggerVisitor.java
// Date Created    :  2012-12-6
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-01  $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.eai.message.visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.core.constants.CcStatus;
import com.pracbiz.b2bportal.core.constants.CnStatus;
import com.pracbiz.b2bportal.core.constants.DisputeStatus;
import com.pracbiz.b2bportal.core.constants.DnStatus;
import com.pracbiz.b2bportal.core.constants.GiStatus;
import com.pracbiz.b2bportal.core.constants.GrnStatus;
import com.pracbiz.b2bportal.core.constants.InvStatus;
import com.pracbiz.b2bportal.core.constants.PnStatus;
import com.pracbiz.b2bportal.core.constants.PoStatus;
import com.pracbiz.b2bportal.core.constants.RtvStatus;
import com.pracbiz.b2bportal.core.eai.message.AckDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.CnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.DoDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.InvDocMsg;
import com.pracbiz.b2bportal.core.eai.message.inbound.ItemInDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.CcDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.DnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GiDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.GrnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PoDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.PocnDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.RtvDocMsg;
import com.pracbiz.b2bportal.core.eai.message.outbound.SalesDocMsg;
import com.pracbiz.b2bportal.core.holder.CcDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcDetailHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CcHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CcHolder;
import com.pracbiz.b2bportal.core.holder.ClassHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnDetailHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.CnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.CnHolder;
import com.pracbiz.b2bportal.core.holder.DnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.DoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DoDetailHolder;
import com.pracbiz.b2bportal.core.holder.DoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.DoHolder;
import com.pracbiz.b2bportal.core.holder.DocClassHolder;
import com.pracbiz.b2bportal.core.holder.DocSubclassHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiDetailHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GiHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GiHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnDetailHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.GrnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvDetailHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.InvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnDetailHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PnHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PnHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoDetailHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoHeaderHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.PoLocationHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvDetailHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.RtvHeaderHolder;
import com.pracbiz.b2bportal.core.holder.RtvHolder;
import com.pracbiz.b2bportal.core.holder.RtvLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesDateLocationDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesDetailHolder;
import com.pracbiz.b2bportal.core.holder.SalesHeaderExtendedHolder;
import com.pracbiz.b2bportal.core.holder.SalesHolder;
import com.pracbiz.b2bportal.core.holder.SalesLocationHolder;
import com.pracbiz.b2bportal.core.holder.SubclassHolder;
import com.pracbiz.b2bportal.core.holder.extension.DnDetailExHolder;
import com.pracbiz.b2bportal.core.mapper.DocClassMapper;
import com.pracbiz.b2bportal.core.mapper.DocSubclassMapper;
import com.pracbiz.b2bportal.core.service.CcDetailExtendedService;
import com.pracbiz.b2bportal.core.service.CcDetailService;
import com.pracbiz.b2bportal.core.service.CcHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.CcHeaderService;
import com.pracbiz.b2bportal.core.service.ClassService;
import com.pracbiz.b2bportal.core.service.CnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.CnDetailService;
import com.pracbiz.b2bportal.core.service.CnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.CnHeaderService;
import com.pracbiz.b2bportal.core.service.DnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.DnDetailService;
import com.pracbiz.b2bportal.core.service.DnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.DnHeaderService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.DoDetailExtendedService;
import com.pracbiz.b2bportal.core.service.DoDetailService;
import com.pracbiz.b2bportal.core.service.DoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.DoHeaderService;
import com.pracbiz.b2bportal.core.service.GiDetailExtendedService;
import com.pracbiz.b2bportal.core.service.GiDetailService;
import com.pracbiz.b2bportal.core.service.GiHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.GiHeaderService;
import com.pracbiz.b2bportal.core.service.GrnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.GrnDetailService;
import com.pracbiz.b2bportal.core.service.GrnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.GrnHeaderService;
import com.pracbiz.b2bportal.core.service.InvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.InvDetailService;
import com.pracbiz.b2bportal.core.service.InvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.InvHeaderService;
import com.pracbiz.b2bportal.core.service.PnDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PnDetailService;
import com.pracbiz.b2bportal.core.service.PnHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PnHeaderService;
import com.pracbiz.b2bportal.core.service.PoDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoDetailService;
import com.pracbiz.b2bportal.core.service.PoHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.PoHeaderService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.PoLocationDetailService;
import com.pracbiz.b2bportal.core.service.PoLocationService;
import com.pracbiz.b2bportal.core.service.RtvDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvDetailService;
import com.pracbiz.b2bportal.core.service.RtvHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.RtvHeaderService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.RtvLocationDetailService;
import com.pracbiz.b2bportal.core.service.RtvLocationService;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailExtendedService;
import com.pracbiz.b2bportal.core.service.SalesDateLocationDetailService;
import com.pracbiz.b2bportal.core.service.SalesDateService;
import com.pracbiz.b2bportal.core.service.SalesDetailExtendedService;
import com.pracbiz.b2bportal.core.service.SalesDetailService;
import com.pracbiz.b2bportal.core.service.SalesHeaderExtendedService;
import com.pracbiz.b2bportal.core.service.SalesHeaderService;
import com.pracbiz.b2bportal.core.service.SalesLocationService;
import com.pracbiz.b2bportal.core.service.SubclassService;

/**
 * TODO To provide an overview of this class.
 *
 * @author liyong
 */
public class TxnLoggerVisitor implements DocMsgVisitor
{

    //****************************************
    //private 
    //****************************************
    @Autowired  private DnHeaderService dnHeaderService;
    @Autowired  private DnHeaderExtendedService dnHeaderExtendedService;
    @Autowired  private DnDetailService dnDetailService;
    @Autowired  private DnDetailExtendedService dnDetailExtendedService;
    @Autowired  private DnService dnService;
    
    @Autowired  private DoHeaderService doHeaderService;
    @Autowired  private DoHeaderExtendedService doHeaderExtendedService;
    @Autowired  private DoDetailService doDetailService;
    @Autowired  private DoDetailExtendedService doDetailExtendedService;
    
    @Autowired  private GrnHeaderService grnHeaderService;
    @Autowired  private GrnHeaderExtendedService grnHeaderExtendedService;
    @Autowired  private GrnDetailService grnDetailService;
    @Autowired  private GrnDetailExtendedService grnDetailExtendedService;
    
    @Autowired  private InvHeaderService invHeaderService;
    @Autowired  private InvHeaderExtendedService InvHeaderExtendedService;
    @Autowired  private InvDetailService invDetailService;
    @Autowired  private InvDetailExtendedService invDetailExtendedService;
    
    @Autowired  private PnHeaderService pnHeaderService;
    @Autowired  private PnHeaderExtendedService pnHeaderExtendedService;
    @Autowired  private PnDetailService pnDetailService;
    @Autowired  private PnDetailExtendedService pnDetailExtendedService;
    
    @Autowired  private PoHeaderService poHeaderService;
    @Autowired  private PoHeaderExtendedService poHeaderExtendedSerivce;
    @Autowired  private PoDetailService poDetailService;
    @Autowired  private PoDetailExtendedService poDetailExtendedService;
    @Autowired  private PoLocationService poLocationService;
    @Autowired  private PoLocationDetailService poLocationDetailService;
    @Autowired  private PoLocationDetailExtendedService poLocationDetailExtendedService;
    @Autowired  private ClassService classService;
    @Autowired  private SubclassService subclassService;
    @Autowired  private DocClassMapper docClassMapper;
    @Autowired  private DocSubclassMapper docSubclassMapper;

    @Autowired  private RtvHeaderService rtvHeaderService;
    @Autowired  private RtvHeaderExtendedService rtvHeaderExtendedService;
    @Autowired  private RtvDetailService rtvDetailService;
    @Autowired  private RtvDetailExtendedService rtvDetailExtendedService;
    @Autowired  private RtvLocationService rtvLocationService;
    @Autowired  private RtvLocationDetailService rtvLocationDetailService;
    @Autowired  private RtvLocationDetailExtendedService rtvLocationDetailExtendedService;
    
    @Autowired  private GiHeaderService giHeaderService;
    @Autowired  private GiHeaderExtendedService giHeaderExtendedService;
    @Autowired  private GiDetailService giDetailService;
    @Autowired  private GiDetailExtendedService giDetailExtendedService;
    
    @Autowired private CcHeaderService ccHeaderService;
    @Autowired private CcHeaderExtendedService ccHeaderExtendedService;
    @Autowired private CcDetailService ccDetailService;
    @Autowired private CcDetailExtendedService ccDetailExtendedService;
    
    @Autowired private SalesHeaderService salesHeaderService;
    @Autowired private SalesDetailService salesDetailService;
    @Autowired private SalesLocationService salesLocationService;
    @Autowired private SalesDateLocationDetailService salesDateLocationDetailService;
    @Autowired private SalesDateService salesDateService;
    @Autowired private SalesHeaderExtendedService salesHeaderExtendedService;
    @Autowired private SalesDetailExtendedService salesDetailExtendedService;
    @Autowired private SalesDateLocationDetailExtendedService salesDateLocationDetailExtendedService;

    @Autowired private CnHeaderService cnHeaderService;
    @Autowired private CnHeaderExtendedService cnHeaderExtendedService;
    @Autowired private CnDetailService cnDetailService;
    @Autowired private CnDetailExtendedService cnDetailExtendedService;
    //****************************************
    //visitor 
    //****************************************
    
    @Override
    public void visit(DnDocMsg doc) throws Exception
    {
        DnHolder dn = doc.getData();
        
        List<DnHeaderHolder> dupDns = dnHeaderService.selectDnHeadersByBuyerOidDnNoAndSupplierCode(doc
            .getBuyer().getBuyerOid(), dn.getDnHeader().getDnNo(), dn
            .getDnHeader().getSupplierCode());
        boolean duplicate = false;
        
        if (null != dupDns && !dupDns.isEmpty())
        {
           for (DnHeaderHolder dupDn : dupDns)
           {
               if (!dupDn.getSentToSupplier())
               {
                   continue;
               }
               
               if (!dupDn.getDuplicate())
               {
                   dupDn.setDuplicate(true);
                   dnHeaderService.updateByPrimaryKeySelective(null, dupDn);
                   duplicate = true;
               }
               
               if (DnStatus.NEW.equals(dupDn.getDnStatus()) || DnStatus.AMENDED.equals(dupDn.getDnStatus()))
               {
                   dupDn.setDnStatus(DnStatus.OUTDATED);
                   dnHeaderService.updateByPrimaryKeySelective(null, dupDn);
                   duplicate = true;
               }
           }
        }
        
        dn.getDnHeader().setDuplicate(duplicate);
        if (dn.getDnHeader().getDuplicate())
        {
            dn.getDnHeader().setDnStatus(DnStatus.AMENDED);
            doc.setAmended(true);
        }
        
        dn.getDnHeader().setMarkSentToSupplier(true);
        dn.getDnHeader().setSentToSupplier(true);
        dnHeaderService.insert(dn.getDnHeader());

        //insert dn header extenede
        if(dn.getHeaderExtended() != null && !dn.getHeaderExtended().isEmpty())
        {
            for(DnHeaderExtendedHolder dnHex : dn.getHeaderExtended())
            {
                dnHeaderExtendedService.insert(dnHex);
            }
        }

        //insert dn detail
        for(DnDetailExHolder detail : dn.getDnDetail())
        {
            dnDetailService.insert(detail);
        }

        //insert dn detail extended
        if(dn.getDetailExtended() != null && !dn.getDetailExtended().isEmpty())
        {
           List<DnDetailExtendedHolder> detailExs = dn.getDetailExtended();
           for(DnDetailExtendedHolder detailEx: detailExs)
           {
               dnDetailExtendedService.insert(detailEx);
           }
        }
        
        dnService.createDnClassInfo(dn.getDnHeader(), dn.getDnDetail());
    }

    @Override
    public void visit(DoDocMsg doc) throws Exception
    {
        DoHolder doHolder = doc.getData();

        //insert do header
        doHeaderService.insert(doHolder.getDoHeader());

        //insert do header extended
        if(doHolder.getHeaderExtended() != null
            && !doHolder.getHeaderExtended().isEmpty())
        {
            for(DoHeaderExtendedHolder doHex : doHolder.getHeaderExtended())
            {
                doHeaderExtendedService.insert(doHex);
            }
        }

        //insert do detail
        for(DoDetailHolder detail : doHolder.getDetails())
        {
            doDetailService.insert(detail);
        }

        //insert do detail extended
        if(doHolder.getDetailExtendeds() != null
            && !doHolder.getDetailExtendeds().isEmpty())
        {
            List<DoDetailExtendedHolder> detailExs = doHolder.getDetailExtendeds();
            for(DoDetailExtendedHolder detailEx: detailExs)
            {
                doDetailExtendedService.insert(detailEx);
            }
        }
    }

    @Override
    public void visit(GrnDocMsg doc) throws Exception
    {
        GrnHolder grn = doc.getData();

        GrnHeaderHolder dupGrn = grnHeaderService.selectGrnHeaderByGrnNo(doc
            .getBuyer().getBuyerOid(), grn.getHeader().getGrnNo(), grn
            .getHeader().getSupplierCode());
 
        if (null != dupGrn)
        {
            if (!dupGrn.getDuplicate())
            {
                dupGrn.setDuplicate(true);
                grnHeaderService.updateByPrimaryKey(null, dupGrn);
            }
            
            if (GrnStatus.NEW.equals(dupGrn.getGrnStatus()) || GrnStatus.AMENDED.equals(dupGrn.getGrnStatus()))
            {
                dupGrn.setGrnStatus(GrnStatus.OUTDATED);
                grnHeaderService.updateByPrimaryKey(null, dupGrn);
            }
        }
        
        
        grn.getHeader().setDuplicate((null == dupGrn ) ? false : true);
        
        if (grn.getHeader().getDuplicate())
        {
            grn.getHeader().setGrnStatus(GrnStatus.AMENDED);
            doc.setAmended(true);
        }
        
        grn.getHeader().setDispute(false);
        grn.getHeader().setDisputeStatus(DisputeStatus.PENDING);
        
        //insert grn header 
        grnHeaderService.insert(grn.getHeader());

        //insert grn header extended 
        if(grn.getHeaderExtendeds() != null
            && !grn.getHeaderExtendeds().isEmpty())
        {
            for(GrnHeaderExtendedHolder grnHex : grn.getHeaderExtendeds())
            {
                grnHeaderExtendedService.insert(grnHex);
            }
        }

        //insert grn detail 
        for(GrnDetailHolder detail : grn.getDetails())
        {
            grnDetailService.insert(detail);
        }

        //insert grn detail extended
        if(grn.getDetailExtendeds() != null
            && !grn.getDetailExtendeds().isEmpty())
        {
            List<GrnDetailExtendedHolder> detailExs = grn.getDetailExtendeds();
            for(GrnDetailExtendedHolder detailEx: detailExs)
            {
                grnDetailExtendedService.insert(detailEx);
            }
        }
    }

    @Override
    public void visit(InvDocMsg doc) throws Exception
    {
        InvHolder inv = doc.getData();
        //update duplicate inv status
        
        List<InvHeaderHolder> invHeaders = invHeaderService.selectInvHeaderByBuyerSupplierPoNoAndStore(inv.getHeader().getBuyerCode(), 
            inv.getHeader().getSupplierCode(), inv.getHeader().getPoNo(), inv.getHeader().getShipToCode());
    
        if (invHeaders != null && !invHeaders.isEmpty())
        {
            for (InvHeaderHolder invHeader : invHeaders)
            {
                if (InvStatus.VOID.equals(invHeader.getInvStatus()))
                {
                    invHeader.setInvStatus(InvStatus.VOID_OUTDATED);
                    invHeaderService.updateByPrimaryKeySelective(null, invHeader);
                }
            }
        }
        
        //update po status
        List<PoHeaderHolder> poHeaders = poHeaderService
                .selectPoHeadersByPoNoBuyerCodeAndSupplierCode(inv.getHeader()
                        .getPoNo(), inv.getHeader().getBuyerCode(), inv
                        .getHeader().getSupplierCode());
        if (poHeaders != null && !poHeaders.isEmpty())
        {
            PoHeaderHolder poHeader = PoHeaderHolder.achieveValidPoHeader(poHeaders);
            inv.getHeader().setPoOid(poHeader.getPoOid());
            PoHeaderHolder newPoHeader = new PoHeaderHolder();
            BeanUtils.copyProperties(poHeader, newPoHeader);
            List<PoLocationHolder> locations = poLocationService.selectOptionalLocationsByPoOid(poHeader.getPoOid());
            if (locations == null || locations.isEmpty() || locations.size() == 1)
            {
                newPoHeader.setPoStatus(PoStatus.INVOICED);
            }
            else
            {
                newPoHeader.setPoStatus(PoStatus.PARTIAL_INVOICED);
            }
            poHeaderService.updateByPrimaryKeySelective(poHeader, newPoHeader);
        }
        
        //insert inv header 
        inv.getHeader().setInvStatus(InvStatus.SUBMIT);
        invHeaderService.insert(inv.getHeader());

        //insert inv header extended
        if(inv.getHeaderExtendeds() != null
            && !inv.getHeaderExtendeds().isEmpty())
        {
            for(InvHeaderExtendedHolder invHex : inv.getHeaderExtendeds())
            {
                InvHeaderExtendedService.insert(invHex);
            }
        }

        //insert inv detail
        for(InvDetailHolder detail : inv.getDetails())
        {
            invDetailService.insert(detail);
        }

        //insert inv detail extended
        List<InvDetailExtendedHolder> detailExs = inv.getDetailExtendeds();
        if(detailExs != null
            && !detailExs.isEmpty())
        {
            for(InvDetailExtendedHolder detailEx: detailExs)
            {
                invDetailExtendedService.insert(detailEx);
            }
        }
        
    }

    @Override
    public void visit(PnDocMsg doc) throws Exception
    {
        PnHolder pn = doc.getData();
        
        PnHeaderHolder dupPn = pnHeaderService.selectPnHeaderByPnNo(doc
            .getBuyer().getBuyerOid(), pn.getPnHeader().getPnNo(), pn.getPnHeader().getSupplierCode());
        
        if (null != dupPn)
        {
            if (!dupPn.getDuplicate())
            {
                dupPn.setDuplicate(true);
                pnHeaderService.updateByPrimaryKey(null, dupPn);
            }
            
            if (PnStatus.NEW.equals(dupPn.getPnStatus()) || PnStatus.AMENDED.equals(dupPn.getPnStatus()))
            {
                dupPn.setPnStatus(PnStatus.OUTDATED);
                pnHeaderService.updateByPrimaryKey(null, dupPn);
            }
        }
        
        pn.getPnHeader().setDuplicate((dupPn == null) ? false : true);
        
        if (pn.getPnHeader().getDuplicate())
        {
            pn.getPnHeader().setPnStatus(PnStatus.AMENDED);
            doc.setAmended(true);
        }

        //insert pn header
        pnHeaderService.insert(pn.getPnHeader());

        //insert pn header extended
        if(pn.getHeaderExtendeds() != null
            && !pn.getHeaderExtendeds().isEmpty())
        {
            for(PnHeaderExtendedHolder pnHex : pn.getHeaderExtendeds())
            {
                pnHeaderExtendedService.insert(pnHex);
            }
        }

        //insert pn detail
        for(PnDetailHolder detail : pn.getDetails())
        {
            pnDetailService.insert(detail);
        }

        //insert pn detail extended
        if(pn.getDetailExtendeds() != null
            && !pn.getDetailExtendeds().isEmpty())
        {
            List<PnDetailExtendedHolder> pnDetailExs = pn.getDetailExtendeds();
            
            for (PnDetailExtendedHolder pnDetailEx : pnDetailExs)
            {
                pnDetailExtendedService.insert(pnDetailEx);
            }
        }
    }

    @Override
    public void visit(PoDocMsg doc) throws Exception
    {
        PoHolder po = doc.getData();

        //insert PoHeader
        
        PoHeaderHolder dupPo = poHeaderService.selectEffectivePoHeaderByPoNo(doc
            .getBuyer().getBuyerOid(), po.getPoHeader().getPoNo(), po
            .getPoHeader().getSupplierCode());
  
        if (null != dupPo )
        {
            if (!dupPo.getDuplicate())
            {
                dupPo.setDuplicate(true);
                poHeaderService.updateByPrimaryKeySelective(null, dupPo);
            }
            
            if (PoStatus.NEW.equals(dupPo.getPoStatus()) || PoStatus.AMENDED.equals(dupPo.getPoStatus()))
            {
                dupPo.setPoStatus(PoStatus.OUTDATED);
                poHeaderService.updateByPrimaryKeySelective(null, dupPo);
            }
        }
        
        po.getPoHeader().setDuplicate((dupPo == null) ? false : true);
        if (po.getPoHeader().getDuplicate())
        {
            po.getPoHeader().setPoStatus(PoStatus.AMENDED);
            doc.setAmended(true);
        }
        
        poHeaderService.insert(po.getPoHeader());

        //insert PoHeaderExtended
        if(po.getHeaderExtendeds() != null
            && !po.getHeaderExtendeds().isEmpty())
        {
            for(PoHeaderExtendedHolder poHEX : po.getHeaderExtendeds())
            {
                poHeaderExtendedSerivce.insert(poHEX);
            }
        }

        //insert PoDetail
        for(PoDetailHolder poDetail : po.getDetails())
        {
            poDetailService.insert(poDetail);
        }

        //insert PoDetailExtended
        if(po.getDetailExtendeds() != null
            && !po.getDetailExtendeds().isEmpty())
        {
            for (PoDetailExtendedHolder detail : po.getDetailExtendeds())
            {
                poDetailExtendedService.insert(detail);
            }
        }

        //insert PoLocation
        if(po.getLocations() != null && !po.getLocations().isEmpty())
        {
            for(int i = 0; i < po.getLocations().size(); i++)
            {
                poLocationService.insert(po.getLocations().get(i));
            }
        }
        
        //insert PoLocationDetail
        if(po.getLocationDetails() != null && !po.getLocationDetails().isEmpty())
        {
            for(int i = 0; i < po.getLocationDetails().size(); i++)
            {
                poLocationDetailService.insert(po.getLocationDetails().get(i));
            }
        }

        //insert PoLocationDetailExtended
        if(po.getPoLocDetailExtendeds() != null && !po.getPoLocDetailExtendeds().isEmpty())
        {
            for (PoLocationDetailExtendedHolder detail : po.getPoLocDetailExtendeds())
            {
                poLocationDetailExtendedService.insert(detail);
            }
        }
        
        //insert PoClass
        List<String> classCodes = new ArrayList<String>();
        for(PoDetailHolder poDetail : po.getDetails())
        {
            ClassHolder classHolder = classService.selectClassByItemCodeAndBuyerOid(
                poDetail.getBuyerItemCode(), po.getPoHeader().getBuyerOid());
            if (classHolder == null || classCodes.contains(classHolder.getClassCode()))
            {
                continue;
            }
            classCodes.add(classHolder.getClassCode());
        }
        
        if (!classCodes.isEmpty())
        {
            for (String classCode : classCodes)
            {
                DocClassHolder poClass = new DocClassHolder();
                poClass.setDocOid(po.getPoHeader().getPoOid());
                poClass.setClassCode(classCode);
                docClassMapper.insert(poClass);
            }
        }
        
        //insert PoSubClass
        Map<String, SubclassHolder> subclassMap = new HashMap<String, SubclassHolder>();
        for(PoDetailHolder poDetail : po.getDetails())
        {
            SubclassHolder subclassHolder = subclassService.selectSubclassExByItemCodeAndBuyerOid(
                poDetail.getBuyerItemCode(), po.getPoHeader().getBuyerOid());
            if (subclassHolder == null || subclassMap.containsKey(subclassHolder.getSubclassCode()))
            {
                continue;
            }
            subclassMap.put(subclassHolder.getSubclassCode(), subclassHolder);
        }
        
        if (!subclassMap.isEmpty())
        {
            for (Map.Entry<String, SubclassHolder> entry : subclassMap.entrySet())
            {
                DocSubclassHolder poSubclass = new DocSubclassHolder();
                poSubclass.setDocOid(po.getPoHeader().getPoOid());
                poSubclass.setSubclassCode(entry.getKey());
                ClassHolder classHolder = classService.selectByKey(entry.getValue().getClassOid());
                poSubclass.setClassCode(classHolder.getClassCode());
                poSubclass.setAuditFinished(false);
                docSubclassMapper.insert(poSubclass);
            }
        }
    }

    @Override
    public void visit(RtvDocMsg doc) throws Exception
    {
        RtvHolder rtv = doc.getData();
        
        RtvHeaderHolder dupRtv = rtvHeaderService.selectRtvHeaderByRtvNo(doc
            .getBuyer().getBuyerOid(), rtv.getRtvHeader().getRtvNo(), rtv
            .getRtvHeader().getSupplierCode());
        
        if (null != dupRtv )
        {
            if (!dupRtv.getDuplicate())
            {
                dupRtv.setDuplicate(true);
                rtvHeaderService.updateByPrimaryKey(null, dupRtv);
            }
            
            if(RtvStatus.NEW.equals(dupRtv.getRtvStatus())
                || RtvStatus.AMENDED.equals(dupRtv.getRtvStatus()))
            {
                dupRtv.setRtvStatus(RtvStatus.OUTDATED);
                rtvHeaderService.updateByPrimaryKey(null, dupRtv);
            }
        }
        
        rtv.getRtvHeader().setDuplicate((null == dupRtv) ? false : true);
        
        if (rtv.getRtvHeader().getDuplicate())
        {
            rtv.getRtvHeader().setRtvStatus(RtvStatus.AMENDED);
            doc.setAmended(true);
        }
 
        //insert rtv header
        rtvHeaderService.insert(rtv.getRtvHeader());
        
        //insert rtv header extended
        if(rtv.getHeaderExtended() != null && !rtv.getHeaderExtended().isEmpty())
        {
            for(RtvHeaderExtendedHolder rtvHex: rtv.getHeaderExtended())
            {
                rtvHeaderExtendedService.insert(rtvHex);
            }
        }
        
        //insert rtv detail
        for(RtvDetailHolder detail: rtv.getRtvDetail())
        {
            rtvDetailService.insert(detail);
        }
        
        //insert rtv detail extended
        if(rtv.getDetailExtended() != null && !rtv.getDetailExtended().isEmpty())
        {
            List<RtvDetailExtendedHolder> detailExs = rtv.getDetailExtended();
            for (RtvDetailExtendedHolder detailEx: detailExs)
            {
                rtvDetailExtendedService.insert(detailEx);
            }
        }
        
        //insert RtvLocation
        if(rtv.getLocations() != null && !rtv.getLocations().isEmpty())
        {
            for(int i = 0; i < rtv.getLocations().size(); i++)
            {
                rtvLocationService.insert(rtv.getLocations().get(i));
            }
        }
        
        //insert RtvLocationDetail
        if(rtv.getLocationDetails() != null && !rtv.getLocationDetails().isEmpty())
        {
            for(int i = 0; i < rtv.getLocationDetails().size(); i++)
            {
                rtvLocationDetailService.insert(rtv.getLocationDetails().get(i));
            }
        }

        //insert RtvLocationDetailExtended
        if(rtv.getRtvLocDetailExtendeds() != null && !rtv.getRtvLocDetailExtendeds().isEmpty())
        {
            for (RtvLocationDetailExtendedHolder detail : rtv.getRtvLocDetailExtendeds())
            {
                rtvLocationDetailExtendedService.insert(detail);
            }
        }
    }
    
    @Override
    public void visit(AckDocMsg doc) throws Exception
    {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public void visit(ItemInDocMsg doc) throws Exception
    {
       // TODO Auto-generated method stub
    }

    @Override
    public void visit(PocnDocMsg doc) throws Exception
    {
        List<PoHeaderHolder> headers = poHeaderService.selectPoHeadersByPoNo(doc
            .getBuyer().getBuyerOid(), doc.getRefNo(), doc.getReceiverCode());
        
        for (PoHeaderHolder header : headers)
        {
            if (!header.getPoStatus().equals(PoStatus.CANCELLED))
            {
                header.setPoStatus(PoStatus.CANCELLED);
                poHeaderService.updateByPrimaryKey(null, header);
            }
        }
    }

    @Override
    public void visit(GiDocMsg doc) throws Exception
    {
        GiHolder gi = doc.getData();
        
        GiHeaderHolder dupGi = giHeaderService.selectGiHeaderByGiNo(doc
            .getBuyer().getBuyerOid(), gi.getGiHeader().getGiNo(), gi
            .getGiHeader().getSupplierCode());
        
        if (null != dupGi )
        {
            if (!dupGi.getDuplicate())
            {
                dupGi.setDuplicate(true);
                giHeaderService.updateByPrimaryKey(null, dupGi);
            }
            
            if(GiStatus.NEW.equals(dupGi.getGiStatus())
                || GiStatus.AMENDED.equals(dupGi.getGiStatus()))
            {
                dupGi.setGiStatus(GiStatus.OUTDATED);
                giHeaderService.updateByPrimaryKey(null, dupGi);
            }
        }
        
        gi.getGiHeader().setDuplicate((null == dupGi) ? false : true);
        
        if (gi.getGiHeader().getDuplicate())
        {
            gi.getGiHeader().setGiStatus(GiStatus.AMENDED);
            doc.setAmended(true);
        }
        
        giHeaderService.insert(gi.getGiHeader());
        
        if(gi.getHeaderExtended() != null && !gi.getHeaderExtended().isEmpty())
        {
            for(GiHeaderExtendedHolder giHex: gi.getHeaderExtended())
            {
                giHeaderExtendedService.insert(giHex);
            }
        }
        
        for(GiDetailHolder detail: gi.getDetails())
        {
            giDetailService.insert(detail);
        }
        
        if(gi.getDetailExtended() != null && !gi.getDetailExtended().isEmpty())
        {
            List<GiDetailExtendedHolder> detailExs = gi.getDetailExtended();
            for (GiDetailExtendedHolder detailEx: detailExs)
            {
                giDetailExtendedService.insert(detailEx);
            }
        }
    }

    @Override
    public void visit(CcDocMsg doc) throws Exception
    {

        CcHolder cc = doc.getData();
        cc.getCcHeader().setCcStatus(CcStatus.NEW);
        //update duplicate cc status
        CcHeaderHolder dupCc = ccHeaderService
            .selectEffectiveCcHeaderByInNo(doc.getBuyer().getBuyerOid(), cc
                .getCcHeader().getSupplierCode(), cc.getCcHeader().getInvNo());    
        

        if (null != dupCc )
        {
            if (!dupCc.getDuplicate())
            {
                dupCc.setDuplicate(true);
                ccHeaderService.updateByPrimaryKey(null, dupCc);
            }
            
            if(CcStatus.NEW.equals(dupCc.getCcStatus())
                || CcStatus.AMENDED.equals(dupCc.getCcStatus()))
            {
                dupCc.setCcStatus(CcStatus.OUTDATED);
                ccHeaderService.updateByPrimaryKey(null, dupCc);
            }
        }
        
        
        //insert inv header 
        cc.getCcHeader().setDuplicate((null == dupCc) ? false : true);
        
        if (cc.getCcHeader().getDuplicate())
        {
            cc.getCcHeader().setCcStatus(CcStatus.AMENDED);
            doc.setAmended(true);
        }
        ccHeaderService.insert(cc.getCcHeader());

        //insert cc header extended
        if(cc.getHeaderExtendeds() != null
            && !cc.getHeaderExtendeds().isEmpty())
        {
            for(CcHeaderExtendedHolder ccHex : cc.getHeaderExtendeds())
            {
                ccHeaderExtendedService.insert(ccHex);
            }
        }

        //insert cc detail
        for(CcDetailHolder detail : cc.getDetails())
        {
            ccDetailService.insert(detail);
        }

        //insert cc detail extended
        List<CcDetailExtendedHolder> detailExs = cc.getDetailExtendeds();
        if(detailExs != null
            && !detailExs.isEmpty())
        {
            for(CcDetailExtendedHolder detailEx: detailExs)
            {
                ccDetailExtendedService.insert(detailEx);
            }
        }
    
    }

    @Override
    public void visit(SalesDocMsg doc) throws Exception
    {
        SalesHolder holder = doc.getData();
        // insert sales header
        salesHeaderService.insert(holder.getSalesHeader());
        
        // insert sales details
        for (SalesDetailHolder detail : holder.getDetails())
        {
            salesDetailService.insert(detail);
        }
        
        //insert new sales location
        for (SalesLocationHolder location : holder.getLocations())
        {
            salesLocationService.insert(location);
        }
        
        // insert sales date
        for (SalesDateHolder salesDate : holder.getSalesDates())
        {
            salesDateService.insert(salesDate);
        }
        
        //insert new sales location detail
        for (SalesDateLocationDetailHolder locDetail : holder.getSalesDateLocationDetail())
        {
            salesDateLocationDetailService.insert(locDetail);
        }
        
        //insert header extended
        if (null != holder.getHeaderExtendeds() && !holder.getHeaderExtendeds().isEmpty())
        {
            for (SalesHeaderExtendedHolder extended : holder.getHeaderExtendeds())
            {
                salesHeaderExtendedService.insert(extended);
            }
        }
        
        //insert detail extended
        if (null != holder.getDetailExtendeds() && !holder.getDetailExtendeds().isEmpty())
        {
            for (SalesDetailExtendedHolder extended : holder.getDetailExtendeds())
            {
                salesDetailExtendedService.insert(extended);
            }
        }
        
        //insert loc detail extended
        if (null != holder.getSalesDateLocationDetailExtendeds() && !holder.getSalesDateLocationDetailExtendeds().isEmpty())
        {
            for (SalesDateLocationDetailExtendedHolder extended : holder.getSalesDateLocationDetailExtendeds())
            {
                salesDateLocationDetailExtendedService.insert(extended);
            }
        }
    }

    @Override
    public void visit(CnDocMsg doc) throws Exception
    {

        CnHolder cn = doc.getData();
        CnHeaderHolder cnHeader = cn.getHeader();
        PoHeaderHolder poHeader = poHeaderService.selectEffectivePoHeaderByPoNo(cnHeader.getBuyerOid(), cnHeader.getPoNo(), cnHeader.getSupplierCode());
        
        if (!poHeader.getPoStatus().equals(PoStatus.CREDITED))
        {
            poHeader.setPoStatus(PoStatus.CREDITED);
            poHeaderService.updateByPrimaryKeySelective(null, poHeader);
        }
        cn.getHeader().setCtrlStatus(CnStatus.SUBMIT);
        cn.getHeader().setDuplicate(false);
        
        //insert inv header 
        cnHeaderService.insert(cn.getHeader());

        //insert cn header extended
        if(cn.getHeaderExtendedList() != null
            && !cn.getHeaderExtendedList().isEmpty())
        {
            for(CnHeaderExtendedHolder cnHex : cn.getHeaderExtendedList())
            {
                cnHeaderExtendedService.insert(cnHex);
            }
        }

        //insert cn detail
        for(CnDetailHolder detail : cn.getDetailList())
        {
            cnDetailService.insert(detail);
        }

        //insert cn detail extended
        List<CnDetailExtendedHolder> detailExs = cn.getDetailExtendedList();
        if(detailExs != null
            && !detailExs.isEmpty())
        {
            for(CnDetailExtendedHolder detailEx: detailExs)
            {
                cnDetailExtendedService.insert(detailEx);
            }
        }
    
    }
}
