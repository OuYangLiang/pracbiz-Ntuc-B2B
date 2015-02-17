//*****************************************************************************
//
// File Name       :  DeliveryOrder.java
// Date Created    :  2012-12-13
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date:  2012-12-13 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.core.constants.PageId;
import com.pracbiz.b2bportal.core.constants.ReadStatus;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.extension.DoSummaryHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.DoHeaderService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author liyong
 */
public class DeliveryOrderAction extends TransactionalDocsBaseAction
{
    private static final long serialVersionUID = 6062936642780059333L;

    public static final String SESSION_KEY_SEARCH_PARAMETER_DO = "SEARCH_PARAMETER_DO";
    private static final String CTRL_PARAMETER_CTRL = "CTRL";

    @Autowired transient private DoHeaderService doHeaderService;
//    @Autowired transient private BuyerStoreService buyerStoreService;
//    @Autowired transient private BuyerStoreAreaUserService buyerStoreAreaUserService;
//    @Autowired transient private BuyerStoreUserService buyerStoreUserService;
    @Autowired transient private ControlParameterService controlParameterService;

    private DoSummaryHolder param;

    public DeliveryOrderAction()
    {
        this.initMsg();
        this.setPageId(PageId.P006.name());
    }

    // *****************************************************
    // summary page
    // *****************************************************
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_DO);

        param = (DoSummaryHolder)getSession().get(
            SESSION_KEY_SEARCH_PARAMETER_DO);

        try
        {
            this.initSearchCriteria();
            this.initSearchCondition();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }

    public String search()
    {
        if(null == param)
        {
            param = new DoSummaryHolder();
        }

        try
        {
            param.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getDoDateFrom()));
            param.setDoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getDoDateTo()));
            param.setPoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getPoDateFrom()));
            param.setPoDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getPoDateTo()));
            param.setSentDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getSentDateFrom()));
            param.setSentDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getSentDateTo()));
            param.setReceivedDateFrom(DateUtil.getInstance().getFirstTimeOfDay(param.getReceivedDateFrom()));
            param.setReceivedDateTo(DateUtil.getInstance().getLastTimeOfDay(param.getReceivedDateTo()));
            param = initSortField(param);
            
            param.trimAllString();
            param.setAllEmptyStringToNull();
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_DO, param);

        return SUCCESS;
    }

    public String data()
    {
        try
        {
            DoSummaryHolder searchParam = (DoSummaryHolder)getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_DO);
            
            if(searchParam == null)
            {
                searchParam = new DoSummaryHolder();
                
                if(getProfileOfCurrentUser().getBuyerOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                        .selectCacheControlParameterBySectIdAndParamId(
                                CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
                    if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
                    {
                        searchParam.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                    }
                    else
                    {
                        searchParam.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                    }
                    searchParam.setDoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
                if (getProfileOfCurrentUser().getSupplierOid() != null)
                {
                    ControlParameterHolder documentWindow = controlParameterService
                        .selectCacheControlParameterBySectIdAndParamId(
                                CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
                    if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
                    {
                        searchParam.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
                    }
                    else
                    {
                        searchParam.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
                    }
                    searchParam.setDoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
                }
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_DO, searchParam);
            }
            searchParam.setUserTypeOid(this.getUserTypeOfCurrentUser());
            searchParam.setBuyerStoreAccessList(this.getBuyerStoreCodeAccess());
            
            searchParam = initSortField(searchParam);
            
            initCurrentUserSearchParam(searchParam);

            searchParam.trimAllString();
            searchParam.setAllEmptyStringToNull();

            this.obtainListRecordsOfPagination(doHeaderService, searchParam, null);
        }
        catch(Exception e)
        {
            this.handleException(e);
            return FORWARD_COMMON_MESSAGE;
        }

        return SUCCESS;
    }
    
    // *****************************************************
    // private method
    // *****************************************************
    private DoSummaryHolder initSortField(DoSummaryHolder param)
    {
        param.setSortField(SORT_FIELD_SENT_DATE);
        param.setSortOrder(SORT_ORDER_DESC);
        
        if(null != getProfileOfCurrentUser().getSupplierOid()) 
        {
            param.setSortField(SORT_FIELD_CREATE_DATE);
            param.setSortOrder(SORT_ORDER_DESC);
        }
        
        return param;
    }
    
    private void initSearchCondition() throws Exception
    {
        readStatus = ReadStatus.toMapValue(this);
        if (param == null)
        {
            param = new DoSummaryHolder();
        }
        if(getProfileOfCurrentUser().getBuyerOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_BUYER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 7 || documentWindow.getNumValue() < 1)
            {
                param.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setDoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
        if (getProfileOfCurrentUser().getSupplierOid() != null)
        {
            ControlParameterHolder documentWindow = controlParameterService
                .selectCacheControlParameterBySectIdAndParamId(
                        CTRL_PARAMETER_CTRL, "DOCUMENT_WINDOW_SUPPLIER");
            if (null == documentWindow.getNumValue() || documentWindow.getNumValue() > 14 || documentWindow.getNumValue() < 1)
            {
                param.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(new Date()));
            }
            else
            {
                param.setDoDateFrom(DateUtil.getInstance().getFirstTimeOfDay(DateUtil.getInstance().dateAfterDays(new Date(), -documentWindow.getNumValue() + 1)));
            }
            param.setDoDateTo(DateUtil.getInstance().getLastTimeOfDay(new Date()));
        }
    }

    // *****************************************************
    // setter and getter
    // *****************************************************
    public DoSummaryHolder getParam()
    {
        return param;
    }

    public void setParam(DoSummaryHolder param)
    {
        this.param = param;
    }

}
