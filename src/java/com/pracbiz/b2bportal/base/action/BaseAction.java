package com.pracbiz.b2bportal.base.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageHolder;
import com.pracbiz.b2bportal.base.service.PaginatingService;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;
import com.pracbiz.b2bportal.core.util.MailBoxUtil;

public abstract class BaseAction extends ActionSupport
{
    private static final long serialVersionUID = 1915040530562597672L;
    protected static final String REQ_PARAMETER_KEEP_SEARCH_CONDITION = "keepSp";
    
    @Autowired
    protected CustomAppConfigHelper appConfig;
    @Autowired
    protected MailBoxUtil mboxUtil;
    @Autowired
    protected EmailEngine emailEngine;
    
    protected GridResult gridRlt;
    
    protected int start;
    protected int count;
    protected String sort;
    
    protected MessageHolder msg;
    
    //*****************************************************
    // protected method
    //*****************************************************
    
    protected void initMsg()
    {
        msg = new MessageHolder();
    }
    
    
    protected HttpServletRequest getRequest()
    {
        return ServletActionContext.getRequest();
    }
    
    
    protected HttpSession getHttpSession()
    {
        return ServletActionContext.getRequest().getSession();
    }
    
    
    protected Map<String, Object> getSession()
    {
        return ActionContext.getContext().getSession();
    }
    
    
    @SuppressWarnings({"unchecked", "rawtypes" })
    protected void obtainListRecordsOfPagination(PaginatingService service_,
        BaseHolder param, Map<String, String> sortFieldMap,
        String itemIdentifier, String moduleKey) throws Exception
    {
        param.setRequestPage(start / count + 1);
        param.setPageSize(count);
        this.getHttpSession().removeAttribute(CommonConstants.SESSION_CHANGED);
        
        int recordCount = service_.getCountOfSummary(param);

        calculateRecordNum(param);

        initSorting(param, sortFieldMap);

        List<BaseHolder> recordList = service_.getListOfSummary(param);
        
        if (moduleKey != null && !moduleKey.isEmpty())
        {
            this.getSession().put(moduleKey, convertBaseHolderToList(recordList));
        }
        
        int index = start;
        for (BaseHolder bh : recordList)
            bh.setDojoIndex(++index);
        
        gridRlt = new GridResult();
        gridRlt.setIdentifier(itemIdentifier);
        gridRlt.setItems(recordList);
        gridRlt.setTotalRecords(recordCount);
    }
    
    
    protected boolean isKeepSearchParameter()
    {
        String keepSP = ServletActionContext.getRequest().getParameter(
            REQ_PARAMETER_KEEP_SEARCH_CONDITION);

        if(keepSP == null)
        {
            return false;
        }

        if(CommonConstants.VALUE_YES.equals(keepSP.trim()))
        {
            return true;
        }

        return false;
    }

    
    protected void clearSearchParameter(String sessionKey_)
    {
        if(!isKeepSearchParameter())
        {
            getSession().remove(sessionKey_);
        }
    }
    
    
    public String toString()
    {
        try
        {
            return BeanUtils.describe(this).toString();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
    
    
    //*****************************************************
    // private method
    //*****************************************************
    
    private void initSorting(BaseHolder param, Map<String, String> sortFieldMap)
    {
        if (sortFieldMap != null && sort != null && !sort.isEmpty())
        {
            String field = null;
            String order = null;

            if (sort.startsWith("-"))
            {
                field = sortFieldMap.get(sort.substring(1));
                order = "DESC";
            }
            else
            {
                field = sortFieldMap.get(sort);
                order = "ASC";
            }

            if (field != null)
            {
                param.setSortField(field);
                param.setSortOrder(order);
            }
        }
    }
    
    
    private void calculateRecordNum(BaseHolder param)
    {
        param.setStartRecordNum(start);
        param.setNumberOfRecordsToSelect(count);
    }
    
    
    private List<String> convertBaseHolderToList(List<BaseHolder> recordList)
    {
        if (recordList == null || recordList.isEmpty())
        {
            return new ArrayList<String>();
        }
        List<String> oidList = new ArrayList<String>();
        for (BaseHolder holder : recordList)
        {
            oidList.add(holder.getCustomIdentification().replaceAll(" ", ""));
        }
        return oidList;
    }


    // *****************************************************
    // setters and getters
    // *****************************************************

    public GridResult getGridRlt()
    {
        return gridRlt;
    }


    public void setStart(int start)
    {
        this.start = start;
    }


    public void setCount(int count)
    {
        this.count = count;
    }


    public void setSort(String sort)
    {
        this.sort = sort;
    }


    public MessageHolder getMsg()
    {
        return msg;
    }


    public void setMsg(MessageHolder msg)
    {
        this.msg = msg;
    }
    
    
    public InputStream getNeverMind() throws UnsupportedEncodingException
    {
        return new ByteArrayInputStream("x".getBytes(CommonConstants.ENCODING_UTF8));
    }

}
