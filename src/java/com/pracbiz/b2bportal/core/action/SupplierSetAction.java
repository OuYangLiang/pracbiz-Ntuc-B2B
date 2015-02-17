package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.SupplierHolder;
import com.pracbiz.b2bportal.core.holder.SupplierSetHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierExHolder;
import com.pracbiz.b2bportal.core.holder.extension.SupplierSetExHolder;
import com.pracbiz.b2bportal.core.service.OidService;
import com.pracbiz.b2bportal.core.service.SupplierService;
import com.pracbiz.b2bportal.core.service.SupplierSetService;

public class SupplierSetAction extends ProjectBaseAction
{

    /**
     * 
     */
    private static final Logger log = LoggerFactory.getLogger(SupplierSetAction.class);
    private static final long serialVersionUID = 1381564364042486020L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_SUPPLIER_SET = "SEARCH_PARAMETER_SUPPLIER_SET";
    private static final String SESSION_OID_PARAMETER = "session.parameter.SupplierSetAction.selectedOids";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected oids is not found in session scope.";
    private static final Map<String, String> sortMap;
    @Autowired private transient SupplierSetService supplierSetService;
    @Autowired private transient SupplierService supplierService;
    @Autowired private transient OidService oidService;
    
    private SupplierSetExHolder param;
    private List<SupplierHolder> availableSuppliers;
    private List<SupplierHolder> selectedSuppliers;
    private List<BigDecimal> selectedSupplierOids;
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("setId", "SET_ID");
        sortMap.put("setDescription", "SET_DESCRIPTION");
    }
    
    
    public SupplierSetAction()
    {
        this.initMsg();
    }
    
    
    public String putParamIntoSession()
    {
            this.getSession().put(SESSION_OID_PARAMETER,
            this.getRequest().getParameter(REQUEST_PARAMTER_OID));

        return SUCCESS;
    }
    
    // *****************************************************
    // summary page
    // *****************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER_SET);
        return SUCCESS;
    }
    
    
    public String search()
    {
        try
        {
            if (param == null)
            {
                param = new SupplierSetExHolder();
            }
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        getSession().put(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER_SET, param);
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            SupplierSetExHolder searchParam = (SupplierSetExHolder)this.getSession().get(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER_SET);
            if (searchParam == null)
            {
                searchParam = new SupplierSetExHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_SUPPLIER_SET, searchParam);
            }
            searchParam.trimAllString();
            searchParam.setAllEmptyStringToNull();
            this.obtainListRecordsOfPagination(supplierSetService, searchParam, sortMap, "setOid", null);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // add supplier set
    // *****************************************************
    public String initAdd()
    {
        try
        {
            availableSuppliers = new ArrayList<SupplierHolder>();
            selectedSuppliers = new ArrayList<SupplierHolder>();
            List<SupplierHolder> allSuppliers = null;
            
            if (this.getUserTypeOfCurrentUser().intValue() == 2)//buyerAdmin
            {
                allSuppliers = supplierService.selectSupplierByBuyerOid(this.getProfileOfCurrentUser().getBuyerOid());
            }
            else //sysAdmin
            {
                allSuppliers = supplierService.select(new SupplierExHolder());
            }
            
            if (allSuppliers != null)
            {
                for (SupplierHolder supplier : allSuppliers)
                {
                    if (supplier.getSetOid() == null)
                    {
                        availableSuppliers.add(supplier);
                    }
                }
            }
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveAdd()
    {
        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
            boolean flag = this.hasActionErrors();
            if (!flag)
            {
                if (param.getSetId() == null)
                {
                    this.addActionError(this.getText("B2BPC2501"));
                    flag = true;
                }
                else if (supplierSetService.selectBySetId(param.getSetId()) != null)
                {
                    this.addActionError(this.getText("B2BPC2502"));
                    flag = true;
                }
                if (null != param.getSetId() && param.getSetId().trim().length() > 20)
                {
                    this.addActionError(this.getText("B2BPC2508"));
                    flag = true;
                }
                if (param.getSetDescription() == null)
                {
                    this.addActionError(this.getText("B2BPC2503"));
                    flag = true;
                }
                if (null != param.getSetDescription() && param.getSetDescription().trim().length() > 150)
                {
                    this.addActionError(this.getText("B2BPC2509"));
                    flag = true;
                }
                if (selectedSupplierOids == null || selectedSupplierOids.isEmpty())
                {
                    this.addActionError(this.getText("B2BPC2504"));
                    flag = true;
                }
                else
                {
                    for (BigDecimal supplierOid : selectedSupplierOids)
                    {
                        SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                        if (supplier == null)
                        {
                            continue;
                        }
                        if (supplier.getSetOid() != null)
                        {
                            this.addActionError(this.getText("B2BPC2505", new String[]{supplier.getSupplierName()}));
                            flag = true;
                        }
                    }
                }
            }
            if (flag)
            {
                List<SupplierHolder> allSuppliers = null;
                
                if (this.getUserTypeOfCurrentUser().intValue() == 2)//buyerAdmin
                {
                    allSuppliers = supplierService
                        .selectSupplierByBuyerOid(this
                            .getProfileOfCurrentUser().getBuyerOid());
                    
                    initSupplierList(allSuppliers,
                        selectedSupplierOids, true, null);
                }
                else //sysAdmin
                {
                    allSuppliers = supplierService
                        .select(new SupplierExHolder());
                    
                    initSupplierList(supplierService
                        .select(new SupplierExHolder()), selectedSupplierOids,
                        true, null);
                }
            }
        }
        catch (Exception e)
        {
            this.handleException(e);
        }
    }
    
    
    public String saveAdd()
    {
        try
        {
            param.setSetOid(oidService.getOid());
            param.setSupplierOidList(selectedSupplierOids);
            
            supplierSetService.insertSupplierSet(this.getCommonParameter(),
                param);
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    // *****************************************************
    // edit supplier set
    // *****************************************************
    public String initViewOrEdit()
    {
        try
        {
            SupplierSetHolder supplierSet = supplierSetService.selectByKey(param.getSetOid());
            BeanUtils.copyProperties(supplierSet, param);
            List<BigDecimal> selectSupplierOids =  new ArrayList<BigDecimal>();
            List<SupplierHolder> selectSuppliers = supplierService.selectSuppliersBySetOid(param.getSetOid());
            if (selectSuppliers != null && !selectSuppliers.isEmpty())
            {
                for (SupplierHolder supplier : selectSuppliers)
                {
                    selectSupplierOids.add(supplier.getSupplierOid());
                }
            }
            
            initSupplierList(
                supplierService.select(new SupplierExHolder()),
                selectSupplierOids, false, param.getSetOid());
            
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        try
        {
            param.trimAllString();
            param.setAllEmptyStringToNull();
            boolean flag = this.hasActionErrors();
            if (!flag)
            {
                if (param.getSetId() == null)
                {
                    this.addActionError(this.getText("B2BPC2501"));
                    flag = true;
                }
                else 
                {
                    SupplierSetHolder supplierSet = supplierSetService.selectBySetId(param.getSetId());
                    if (supplierSet != null && supplierSet.getSetOid().compareTo(param.getSetOid()) != 0)
                    {
                        this.addActionError(this.getText("B2BPC2502"));
                        flag = true;
                    }
                }
                if (null != param.getSetId() && param.getSetId().trim().length() > 20)
                {
                    this.addActionError(this.getText("B2BPC2508"));
                    flag = true;
                }
                if (param.getSetDescription() == null)
                {
                    this.addActionError(this.getText("B2BPC2503"));
                    flag = true;
                }
                if (null != param.getSetDescription() && param.getSetDescription().trim().length() > 150)
                {
                    this.addActionError(this.getText("B2BPC2509"));
                    flag = true;
                }
                if (selectedSupplierOids == null || selectedSupplierOids.isEmpty())
                {
                    this.addActionError(this.getText("B2BPC2504"));
                    flag = true;
                }
                else
                {
                    for (BigDecimal supplierOid : selectedSupplierOids)
                    {
                        SupplierHolder supplier = supplierService.selectSupplierByKey(supplierOid);
                        if (supplier == null)
                        {
                            continue;
                        }
                        if (supplier.getSetOid() != null && supplier.getSetOid().compareTo(param.getSetOid()) != 0)
                        {
                            this.addActionError(this.getText("B2BPC2505", new String[]{supplier.getSupplierName()}));
                            flag = true;
                        }
                    }
                }
            }
            if (flag)
            {
                List<SupplierHolder> allSuppliers = null;
                
                if (this.getUserTypeOfCurrentUser().intValue() == 2)//buyerAdmin
                {
                    allSuppliers = supplierService
                        .selectSupplierByBuyerOid(this
                            .getProfileOfCurrentUser().getBuyerOid());
                    
                    initSupplierList(allSuppliers,
                        selectedSupplierOids, false, param.getSetOid());
                }
                else //sysAdmin
                {
                    allSuppliers = supplierService
                        .select(new SupplierExHolder());
                    
                    initSupplierList(supplierService
                        .select(new SupplierExHolder()), selectedSupplierOids,
                        false, param.getSetOid());
                }
            }
        }
        catch (Exception e)
        {
            this.handleException(e);
        }
    }
    
    
    public String saveEdit()
    {
        try
        {
            param.setSupplierOidList(selectedSupplierOids);
            
            supplierSetService.updateSupplierSet(this.getCommonParameter(), param);
        }
        catch (Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        return SUCCESS;
    }
    
    
    public String saveDelete()
    {
        try
        {
            String selectedOids = this.getSession().get(SESSION_OID_PARAMETER).toString();
            
            if (null == selectedOids)
            {
                throw new Exception(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
            }
            
            this.getSession().remove(SESSION_OID_PARAMETER);
            
            String [] setOids = selectedOids.split("-");
            
            for (String soid : setOids)
            {
                BigDecimal setOid = new BigDecimal(soid);
                
                SupplierSetHolder oldSupSet = supplierSetService.selectByKey(setOid);
                
                if (null != oldSupSet)
                {
                    supplierSetService.deleteSupplierSet(this.getCommonParameter(), oldSupSet);
                    
                    msg.saveSuccess(this.getText("B2BPC2507", new String[]{oldSupSet.getSetId()}));
                }
            }
            
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            msg.addMessageTarget(mt);
            
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }
        
        return FORWARD_COMMON_MESSAGE;
    }
    
    
    // *****************************************************
    // private method
    // *****************************************************
    protected void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
            new String[] {tickNo}));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }
    
    
    private void initSupplierList(List<SupplierHolder> allSuppliers,
        List<BigDecimal> selectSupplierOids, boolean createFlag,
        BigDecimal setOid) throws Exception
    {
        availableSuppliers = new ArrayList<SupplierHolder>();
        selectedSuppliers = new ArrayList<SupplierHolder>();
        if (allSuppliers == null || allSuppliers.isEmpty())
        {
            return;
        }
        if (this.getUserTypeOfCurrentUser().intValue() == 2)//buyerAdmin
        {
            List<SupplierHolder> suppliers = supplierService.selectSupplierByBuyerOid(this
                .getProfileOfCurrentUser().getBuyerOid());
            
            availableSuppliers.addAll(suppliers);
            
        }
        else //sysAdmin
        {
            availableSuppliers.addAll(allSuppliers);
        }
        for (SupplierHolder supplier : allSuppliers)
        {
            if (createFlag && supplier.getSetOid() != null)
            {
                availableSuppliers.remove(supplier);
                continue;
            }
            if (!createFlag && supplier.getSetOid() != null && supplier.getSetOid().compareTo(setOid) != 0)
            {
                availableSuppliers.remove(supplier);
                continue;
            }
            if (null != selectSupplierOids && !selectSupplierOids.isEmpty() && selectSupplierOids.contains(supplier.getSupplierOid()))
            {
                selectedSuppliers.add(supplier);
                availableSuppliers.remove(supplier);
            }
        }
    }
    
    
    // *****************************************************
    // getter and setter method
    // *****************************************************
    public SupplierSetExHolder getParam()
    {
        return param;
    }


    public void setParam(SupplierSetExHolder param)
    {
        this.param = param;
    }


    public List<SupplierHolder> getAvailableSuppliers()
    {
        return availableSuppliers;
    }


    public void setAvailableSuppliers(List<SupplierHolder> availableSuppliers)
    {
        this.availableSuppliers = availableSuppliers;
    }


    public List<SupplierHolder> getSelectedSuppliers()
    {
        return selectedSuppliers;
    }


    public void setSelectedSuppliers(List<SupplierHolder> selectedSuppliers)
    {
        this.selectedSuppliers = selectedSuppliers;
    }

    
    public List<BigDecimal> getSelectedSupplierOids()
    {
        return selectedSupplierOids;
    }

    
    public void setSelectedSupplierOids(List<BigDecimal> selectedSupplierOids)
    {
        this.selectedSupplierOids = selectedSupplierOids;
    }
    
    
    
}
