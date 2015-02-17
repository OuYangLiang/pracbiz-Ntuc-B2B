//*****************************************************************************
//
// File Name       :  JobControlAction.java
// Date Created    :  Aug 26, 2013
// Last Changed By :  $Author: ouyang $
// Last Changed On :  $Date: Aug 26, 2013 10:24:41 PM$
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2013.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.JobHolder;
import com.pracbiz.b2bportal.core.service.JobService;

/**
 * TODO To provide an overview of this class.
 *
 * @author ouyang
 */
public class JobControlAction extends ProjectBaseAction
{
    private static final long serialVersionUID = 6919663618114960340L;
    private static final Logger log = LoggerFactory.getLogger(JobControlAction.class);
    private static final Map<String, String> sortMap;
    private static final String SPACE = " ";
    public static final String SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL = "SEARCH_PARAMETER_JOB_CONTROL";
    
    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("jobName", "JOB_NAME");
        sortMap.put("jobDescription", "JOB_DESCRIPTION");
        sortMap.put("cronExpression", "CRON_EXPRESSION");
        sortMap.put("updateDate", "UPDATE_DATE");
        sortMap.put("updateBy", "UPDATE_BY");
    }
    
    @Autowired
    private transient JobService jobService;
    
    private JobHolder param;
    private String second;
    private String minute;
    private String hour;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String year;
    private int isSuccess;
    
    
    public JobControlAction()
    {
        this.initMsg();
    }
    
    
    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL);

        if (param == null)
        {
            param = (JobHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL);
        }
        return SUCCESS;
    }
    
    
    public String data()
    {
        try
        {
            JobHolder searchParam = (JobHolder) getSession()
                .get(SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL);

            if (null == searchParam)
            {
                searchParam = new JobHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL, searchParam);
            }
            this.obtainListRecordsOfPagination(jobService, searchParam, sortMap, "jobOid", MODULE_KEY_JOB_CONTROL);
            getSession().remove(CommonConstants.SESSION_CHANGED);
            getSession().put(SESSION_KEY_SEARCH_PARAMETER_JOB_CONTROL, searchParam);
        }
        catch(Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }
        
        return SUCCESS;
    }
    
    
    //*****************************************************
    // edit
    //*****************************************************
    
    public String initEdit()
    {
        try
        {
            String sourceOid = this.getRequest().getParameter("sourceOid");
            
            param = jobService.selectByKey(new BigDecimal(sourceOid));
            
            if (null == param)
                throw new Exception("Record not found based on source oid " + sourceOid);
            
            String[] cronExpression = param.getCronExpression().split(" ");
            
            second      = cronExpression[0];
            minute      = cronExpression[1];
            hour        = cronExpression[2];
            dayOfMonth  = cronExpression[3];
            month       = cronExpression[4];
            dayOfWeek   = cronExpression[5];
            year        = cronExpression.length == 7 ? cronExpression[6] : null;
        }
        catch(Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    public void validateSaveEdit()
    {
        try
        {
            boolean flag = this.hasFieldErrors();
            
            if (!flag && null != year && !year.trim().isEmpty())
            {
                year = year.trim();
                
                if (year.length() < 4 && !"*".equals(year))
                {
                    this.addActionError(this.getText("B2BPC2708"));
                    flag = true;
                }
            }
            
            if (!flag && null != year && !year.trim().isEmpty() && !"*".equals(year))
            {
                String currentYear = DateUtil.getInstance().getCurrentYearAndMonth().substring(0, 4);
                
                if (year.trim().substring(0, 4).compareTo(currentYear) < 0)
                {
                    this.addActionError(this.getText("B2BPC2709"));
                    flag = true;
                }
            }
            
            String cronExpression = second + SPACE + minute + SPACE + hour + SPACE + dayOfMonth + SPACE + month + SPACE + dayOfWeek + SPACE + year;
            
            if (!flag && null != second && !second.trim().isEmpty() && null != minute && !minute.trim().isEmpty() && null != hour && !hour.trim().isEmpty() 
                && (this.validationOfSecondMinuteHour(second) || this.validationOfSecondMinuteHour(minute) || this.validationOfSecondMinuteHour(hour)))
            {
                
                this.addActionError(this.getText("B2BPC2707",new String[] {cronExpression}));
                flag = true;
                
            }
            
            if (!flag && null != month && !month.trim().isEmpty() && this.validationOfMonth(month))
            {
                this.addActionError(this.getText("B2BPC2707", new String[]{cronExpression}));
                flag = true;
            }
            
            if (!flag && null != dayOfMonth && !dayOfMonth.trim().isEmpty() && this.valdiationOfDayOfMonth(dayOfMonth))
            {
                this.addActionError(this.getText("B2BPC2707", new String[]{cronExpression}));
                flag = true;
            }
            
            if (!flag && null != dayOfWeek && !dayOfWeek.trim().isEmpty() && this.validationOfDayOfWeek(dayOfWeek))
            {
                this.addActionError(this.getText("B2BPC2707", new String[]{cronExpression}));
                flag = true;
            }
            if (!flag)
            {
                if (null != year && !year.trim().isEmpty())
                    cronExpression = cronExpression + SPACE + year;
                
                CronTrigger trigger = new CronTrigger();
                
                try
                {
                    trigger.setCronExpression(cronExpression);
                }
                catch (ParseException e)
                {
                    flag = true;
                    
                    this.addActionError(this.getText("B2BPC2707", new String[]{cronExpression}));
                }
            }
            
            
            if (flag)
            {
                String sourceOid = this.getRequest().getParameter("sourceOid");
                
                param = jobService.selectByKey(new BigDecimal(sourceOid));
                
            }
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, e);
            
            this.addActionError(e.getMessage());
        }
        
    }
    
    
    public String saveEdit()
    {
        try
        {
            String sourceOid = this.getRequest().getParameter("sourceOid");
            
            JobHolder oldObj = jobService.selectByKey(new BigDecimal(sourceOid));
            JobHolder newObj = new JobHolder();
            BeanUtils.copyProperties(oldObj, newObj);
            
            if (null == oldObj)
                throw new Exception("Record not found based on source oid " + sourceOid);
            
            String cronExpression = second + SPACE + minute + SPACE + hour + SPACE + dayOfMonth + SPACE + month + SPACE + dayOfWeek;
            
            if (null != year && !year.trim().isEmpty())
                cronExpression = cronExpression + SPACE + year;
            
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            
            CronTrigger trigger = new CronTrigger();
            trigger.setName(oldObj.getTriggerName());
            trigger.setJobName(oldObj.getJobName());
            trigger.setJobGroup(oldObj.getJobGroup());
            trigger.setGroup(oldObj.getTriggerGroup());
            trigger.setCronExpression(cronExpression);
            
            try
            {
                sched.rescheduleJob(oldObj.getTriggerName(), oldObj.getTriggerGroup(), trigger);
            }
            catch (SchedulerException e)
            {
                sched.shutdown(true);
                
                msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
                msg.saveError(e.getMessage());
                
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                
                msg.addMessageTarget(mt);
                
                return FORWARD_COMMON_MESSAGE;
            }
            finally
            {
                sched.shutdown(true);
            }
            
            
            newObj.setCronExpression(cronExpression);
            newObj.setUpdateBy(this.getLoginIdOfCurrentUser());
            newObj.setUpdateDate(new Date());
            
            jobService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), oldObj, newObj);
            
            log.info(this.getText("B2BPC2710", new String[]{oldObj.getCronExpression(), newObj.getCronExpression(), this.getLoginIdOfCurrentUser()}));
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    public String saveEditEnabled()
    {
        isSuccess = 0;
        try
        {
            
            String sourceOid = this.getRequest().getParameter("sourceOid");
            
            JobHolder oldObj = jobService.selectByKey(new BigDecimal(sourceOid));
            JobHolder newObj = new JobHolder();
            BeanUtils.copyProperties(oldObj, newObj);
            
            if (null == oldObj)
                throw new Exception("Record not found based on source oid " + sourceOid);
            
            String[] cronExpressions = oldObj.getCronExpression().split(SPACE);
            String isEnabled = this.getRequest().getParameter("enabled");
            
            String cronExpression = cronExpressions[0] + SPACE + cronExpressions[1] + SPACE + cronExpressions[2] + SPACE + cronExpressions[3] + SPACE + cronExpressions[4] + SPACE + cronExpressions[5];
            
            if (!Boolean.parseBoolean(isEnabled))
            {
                cronExpression = cronExpression + SPACE + "2099";
            }
            
            SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
            Scheduler sched = schedFact.getScheduler();
            
            CronTrigger trigger = new CronTrigger();
            trigger.setName(oldObj.getTriggerName());
            trigger.setJobName(oldObj.getJobName());
            trigger.setJobGroup(oldObj.getJobGroup());
            trigger.setGroup(oldObj.getTriggerGroup());
            trigger.setCronExpression(cronExpression);
            
            try
            {
                sched.rescheduleJob(oldObj.getTriggerName(), oldObj.getTriggerGroup(), trigger);
            }
            catch (SchedulerException e)
            {
                sched.shutdown(true);
                
                msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
                msg.saveError(e.getMessage());
                
                MessageTargetHolder mt = new MessageTargetHolder();
                mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
                mt.setTargetURI(INIT);
                mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
                
                msg.addMessageTarget(mt);
                
                return FORWARD_COMMON_MESSAGE;
            }
            finally
            {
                sched.shutdown(true);
            }
            
            
            newObj.setEnabled(Boolean.parseBoolean(isEnabled));
            newObj.setUpdateBy(this.getLoginIdOfCurrentUser());
            newObj.setUpdateDate(new Date());
            
            jobService.auditUpdateByPrimaryKeySelective(this.getCommonParameter(), oldObj, newObj);
            
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            
            msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
            msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY, new String[]{tickNo}));
            
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            
            msg.addMessageTarget(mt);
            
            isSuccess = 1;
            
            return FORWARD_COMMON_MESSAGE;
        }
        
        return SUCCESS;
    }
    
    
    //*****************************************************
    // private methods
    //*****************************************************
    
    private boolean validationOfSecondMinuteHour(String value)
    {
        if (null != value && !value.trim().isEmpty())
        {
            if (value.matches("\\*|([0-9]+([\\/\\-][0-9]+)?)"))
            {
                return false;
            }
            
            if (value.matches("[0-9]+([,][0-9]+)*"))
            {
                return false;
            }
        }
        return true;
    }
    
    
    private boolean validationOfMonth(String value)
    {
        if (value.matches("\\*|([0-9]+([\\/\\-][0-9]+)?)"))
        {
            return false;
        }
        
        if (value.matches("[0-9]+([,][0-9]+)*"))
        {
            return false;
        }
        
        String regex = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))" +
        		"((\\-)((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+)))?" +
        		"(,((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))" +
        		"((\\-)((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+)))?)?";
        if (value.matches(regex))
        {
            return false;
        }
        
        regex = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))((\\/)([0-9]+))?";
        if (value.matches(regex))
        {
            return false;
        }
        return true;
    }
    
    
    private boolean valdiationOfDayOfMonth(String value)
    {
        if (value.matches("(\\?)|(L)|(\\*)|((([12][0-9])|([1-9])|(3[0-1]))(W)?)"))
        {
            return false;
        }
        return true;
    }
    
    
    private boolean validationOfDayOfWeek(String value)
    {
        if (value.matches("\\*|([0-9]+([\\/\\-][0-9]+)?)"))
        {
            return false;
        }
        
        if (value.matches("[0-9]+([,][0-9]+)*"))
        {
            return false;
        }
        
        String regex = "((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+))" +
                "((\\-)((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+)))?" +
                "(,((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+))" +
                "((\\-)((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+)))?)?";
        if (value.matches(regex))
        {
            return false;
        }
        
        regex = "((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+))((\\/)([0-9]+))?";
        if (value.matches(regex))
        {
            return false;
        }
        
        regex = "(\\?)|(L)|(((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|(([0-9]))(\\#)([0-9]+)))";
        if (value.matches(regex))
        {
            return false;
        }
        
        return true;
    }
    
    
    //*****************************************************
    // setter and getter methods
    //*****************************************************

    public JobHolder getParam()
    {
        return param;
    }


    public void setParam(JobHolder param)
    {
        this.param = param;
    }


    public String getSecond()
    {
        return second;
    }


    public void setSecond(String second)
    {
        this.second = second;
    }


    public String getMinute()
    {
        return minute;
    }


    public void setMinute(String minute)
    {
        this.minute = minute;
    }


    public String getHour()
    {
        return hour;
    }


    public void setHour(String hour)
    {
        this.hour = hour;
    }


    public String getDayOfMonth()
    {
        return dayOfMonth;
    }


    public void setDayOfMonth(String dayOfMonth)
    {
        this.dayOfMonth = dayOfMonth;
    }


    public String getMonth()
    {
        return month;
    }


    public void setMonth(String month)
    {
        this.month = month;
    }


    public String getDayOfWeek()
    {
        return dayOfWeek;
    }


    public void setDayOfWeek(String dayOfWeek)
    {
        this.dayOfWeek = dayOfWeek;
    }


    public String getYear()
    {
        return year;
    }


    public void setYear(String year)
    {
        this.year = year;
    }


    public int getIsSuccess()
    {
        return isSuccess;
    }


    public void setIsSuccess(int isSuccess)
    {
        this.isSuccess = isSuccess;
    }
    
//    public static void main(String[] args)
//    {
//        String a = "MON-TUE,";
//        String regex = "\\*|([0-9]+([\\/\\-][0-9]+)?)";
//        String regex1 = "[0-9]+([,][0-9]+)*";
//        String regex3 = "[0-9]+";
//        
//        String startMonth = "(JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)";
//        String regex2 = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))";
//        String endMonth = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))((\\-)((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+)))?";
//        String anotherMonthRanger = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))((\\-)((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+)))?(,((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))((\\-)((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+)))?)?";
//        String ai = "((JAN)|(FEB)|(MAR)|(APR)|(MAY)|(JUN)|(JUL)|(AUG)|(SEP)|(OCT)|(NOV)|(DEC)|([0-9]+))((\\/)([0-9]+))?";
//        String monthOf = "(\\?)|(L)|(\\*)|((([12][0-9])|([1-9])|(3[0-1]))(W)?)";
//        String weekOf = "((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+))((\\-)((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+)))?(,((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+))((\\-)((SUN)|(MON)|(TUE)|(WED)|(THU)|(FRI)|(SAT)|([0-9]+)))?)?";
//        System.out.println(weekOf);
//        System.out.println(a.matches(weekOf));
//    }
}
