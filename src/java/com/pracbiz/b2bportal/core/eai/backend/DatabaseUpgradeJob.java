package com.pracbiz.b2bportal.core.eai.backend;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.holder.BuyerStoreHolder;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.holder.GrnHolder;
import com.pracbiz.b2bportal.core.holder.InvHolder;
import com.pracbiz.b2bportal.core.holder.PoHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingGrnHolder;
import com.pracbiz.b2bportal.core.holder.PoInvGrnDnMatchingHolder;
import com.pracbiz.b2bportal.core.holder.extension.PoInvGrnDnMatchingDetailExHolder;
import com.pracbiz.b2bportal.core.service.BuyerStoreService;
import com.pracbiz.b2bportal.core.service.DnService;
import com.pracbiz.b2bportal.core.service.GrnService;
import com.pracbiz.b2bportal.core.service.InvoiceService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingDetailService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingGrnService;
import com.pracbiz.b2bportal.core.service.PoInvGrnDnMatchingService;
import com.pracbiz.b2bportal.core.service.PoService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class DatabaseUpgradeJob extends BaseJob implements CoreCommonConstants
{
    private static final Logger log = LoggerFactory
            .getLogger(DatabaseUpgradeJob.class);
    private static final String ID = "[UnityDataBaseUpgradeJob]";

    private PoInvGrnDnMatchingService poInvGrnDnMatchingService;
    private StandardEmailSender standardEmailSender;
    private PoInvGrnDnMatchingGrnService poInvGrnDnMatchingGrnService;
    private PoService poService;
    private InvoiceService invoiceService;
    private GrnService grnService;
    private PoInvGrnDnMatchingDetailService poInvGrnDnMatchingDetailService;
    private DnService dnService;
    private BuyerStoreService buyerStoreService;
    
    
    @Override
    protected void init()
    {
        poInvGrnDnMatchingService = this.getBean("poInvGrnDnMatchingService", PoInvGrnDnMatchingService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
        poInvGrnDnMatchingGrnService = this.getBean("poInvGrnDnMatchingGrnService", PoInvGrnDnMatchingGrnService.class);
        poService = this.getBean("poService", PoService.class);
        invoiceService = this.getBean("invoiceService", InvoiceService.class);
        grnService = this.getBean("grnService", GrnService.class);
        poInvGrnDnMatchingDetailService = this.getBean("poInvGrnDnMatchingDetailService", PoInvGrnDnMatchingDetailService.class);
        dnService = this.getBean("dnService", DnService.class);
        buyerStoreService = this.getBean("buyerStoreService", BuyerStoreService.class);
        
    }


    @Override
    protected void process()
    {
        try
        {
            synchronized(SupplierMasterImportJob.lock)
            {
                while (SupplierMasterImportJob.isAnyJobRunning)
                {
                    try
                    {
                        SupplierMasterImportJob.lock.wait();
                    }
                    catch (InterruptedException e)
                    {
                        ErrorHelper.getInstance().logError(log, e);
                    }
                }
                
                SupplierMasterImportJob.isAnyJobRunning = true;
            }
            
            realProcess();
        }
        finally
        {
            synchronized (SupplierMasterImportJob.lock)
            {
                SupplierMasterImportJob.isAnyJobRunning = false;

                SupplierMasterImportJob.lock.notifyAll();
            }
        }
    }
    
    
    private void realProcess()
    {
        log.info("Start to upgrade the database.");
        
        try
        {
            List<PoInvGrnDnMatchingHolder> matchingList = poInvGrnDnMatchingService.select(new PoInvGrnDnMatchingHolder());
            
            List<PoInvGrnDnMatchingDetailExHolder> allDetailList = new ArrayList<PoInvGrnDnMatchingDetailExHolder>();
            if (matchingList == null || matchingList.isEmpty())
            {
                return;
            }
            
            log.info("find " + matchingList.size() + " matching records .");
            
            for (PoInvGrnDnMatchingHolder existMatching : matchingList)
            {
                log.info("process matching reocrd [" + existMatching.getMatchingOid() + "]");
                PoHolder po = poService.selectPoByKey(existMatching.getPoOid());
                
                InvHolder inv = null;
                if (existMatching.getInvOid() != null)
                {
                    inv = invoiceService.selectInvoiceByKey(existMatching.getInvOid());
                }
                
                DnHolder dn = null;
                if (existMatching.getDnOid() != null)
                {
                    dn = dnService.selectDnByKey(existMatching.getDnOid());
                }
                
                List<GrnHolder> grnList = new ArrayList<GrnHolder>();
                List<PoInvGrnDnMatchingGrnHolder> matchingGrns = poInvGrnDnMatchingGrnService.selectByMatchOid(existMatching.getMatchingOid());
                for (PoInvGrnDnMatchingGrnHolder matchingGrn : matchingGrns)
                {
                    grnList.add(grnService.selectByKey(matchingGrn.getGrnOid()));
                }
                
                String storeName = "";
                if (storeName == null || storeName.trim().isEmpty())
                {
                    BuyerStoreHolder store = buyerStoreService.selectBuyerStoreByBuyerCodeAndStoreCode(existMatching.getBuyerCode(), existMatching.getPoStoreCode());
                    if (store != null)
                    {
                        storeName = store.getStoreName();
                    }
                }
                
                PoInvGrnDnMatchingHolder matching = new PoInvGrnDnMatchingHolder(
                        po, existMatching.getPoStoreCode(), storeName, po.amtOfStore(existMatching.getPoStoreCode()),
                        inv, grnList, dn);
                
                List<PoInvGrnDnMatchingDetailExHolder> detailList = matching.getDetailList();
                
                for (PoInvGrnDnMatchingDetailExHolder detail : detailList)
                {
                    detail.setMatchingOid(existMatching.getMatchingOid());
                    allDetailList.add(detail);
                }
                
            }
            
            poInvGrnDnMatchingDetailService.insertDetailList(allDetailList);
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
        }
        
        log.info("Upgrade the database successfully.");
    }
    
}
