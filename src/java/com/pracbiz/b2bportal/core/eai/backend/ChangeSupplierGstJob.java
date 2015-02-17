package com.pracbiz.b2bportal.core.eai.backend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.StandardEmailSender;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class ChangeSupplierGstJob extends BaseJob implements
        CoreCommonConstants
{
    private static final Logger log = LoggerFactory.getLogger(ChangeSupplierGstJob.class);
    private static final String ID = "[ChangeSupplierGstJob]";
    private SupplierService supplierService;
    private ControlParameterService controlParameterService;
    private StandardEmailSender standardEmailSender;
    
    @Override
    protected void init()
    {
        supplierService = this.getBean("supplierService", SupplierService.class);
        controlParameterService = this.getBean("controlParameterService", ControlParameterService.class);
        standardEmailSender = this.getBean("standardEmailSender", StandardEmailSender.class);
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
        log.info(ID + "Start to process.");
        
        try
        {
            ControlParameterHolder fromDateHolder = controlParameterService.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_NEW_GST_FROM_DATE);
            if (fromDateHolder == null || fromDateHolder.getDateValue() == null)
            {
                log.info(ID + "can not obtain the from date configuration.");
                return;
            }
            if (!DateUtil.getInstance().convertDateToString(new Date()).equals(DateUtil.getInstance().
                    convertDateToString(fromDateHolder.getDateValue())))
            {
                log.info(ID + "today is not the from date.");
                return;
            }
            fromDateHolder.setDateValue(null);
            controlParameterService.updateByPrimaryKey(null, fromDateHolder);
            
            BigDecimal newGst = BigDecimal.ZERO;
            ControlParameterHolder newGstHolder = controlParameterService.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_NEW_GST);
            if (newGstHolder != null && newGstHolder.getStringValue() != null && !newGstHolder.getStringValue().trim().isEmpty())
            {
                newGst = new BigDecimal(newGstHolder.getStringValue());
                newGstHolder.setStringValue(null);
                controlParameterService.updateByPrimaryKey(null, newGstHolder);
            }
            
            BigDecimal currentGst = BigDecimal.ZERO;
            ControlParameterHolder currentGstHolder = controlParameterService.selectCacheControlParameterBySectIdAndParamId(SECT_ID_CTRL, PARAM_ID_CURRENT_GST);
            if (currentGstHolder != null && currentGstHolder.getStringValue() != null && !currentGstHolder.getStringValue().trim().isEmpty())
            {
                currentGst = new BigDecimal(currentGstHolder.getStringValue());
                currentGstHolder.setStringValue(String.valueOf(newGst));
                controlParameterService.updateByPrimaryKey(null, currentGstHolder);
            }
            List<SupplierHolder> suppliers = supplierService.selectActiveSuppliers();
            
            if (suppliers != null && !suppliers.isEmpty())
            {
                for (SupplierHolder supplier : suppliers)
                {
                    if (supplier.getGstRegNo() == null || supplier.getGstRegNo().trim().isEmpty())
                    {
                        log.info(ID + "gst is disabled for supplier[" + supplier.getSupplierCode() + "], no need to update gst percent");
                        continue;
                    }
                    if (supplier.getGstPercent().compareTo(currentGst) == 0)
                    {
                        log.info(ID + "update gst percent for supplier[" + supplier.getSupplierCode() + "]");
                        supplier.setGstPercent(newGst);
                        supplierService.updateByPrimaryKeySelective(null, supplier);
                    }
                }
            }
            
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);
            standardEmailSender.sendStandardEmail(ID, tickNo, e);
            return;
        }
        
        log.info(ID + "Process ended.");
    }

}
