package com.pracbiz.b2bportal.core.eai.backend;

/*import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.pracbiz.b2bportal.base.email.EmailEngine;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.EncodeUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.core.holder.ResetPasswordRequestHistoryHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;
import com.pracbiz.b2bportal.core.service.ResetPasswordRequestHistoryService;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;
import com.pracbiz.b2bportal.core.util.CustomAppConfigHelper;*/

/**
 * TODO To provide an overview of this class.
 *
 * @author youwenwu
 */
public class SendEmailJob// implements Job, CoreCommonConstants
{
    /*private static boolean isRunning = false;
    private static final Logger log = LoggerFactory.getLogger(SendEmailJob.class);
    
    private EmailEngine emailEngine;
    private CustomAppConfigHelper appConfig;
    private ControlParameterService controlParameterService;
    private ResetPasswordRequestHistoryService resetPasswordRequestHistoryService;
    private String driverClass;
    private String url;
    private String username;
    private String password;
    private String buyerOids;

    @Override
    public void execute(JobExecutionContext ctx) throws JobExecutionException
    {
        if (isRunning)
        {
            log.info(":::: Previous job is still running.");
            return;
        }

        isRunning = true;

        try
        {
            process();
        }
        finally
        {
            isRunning = false;
        }
    }
    
    public static void main(String[] args) throws UnknownHostException
    {
        log.info(InetAddress.getLocalHost().getHostAddress());
    }
    
    
    private void process()
    {
        log.info(":::: Start to process.");
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try
        {

            Class.forName(driverClass);

            conn = DriverManager.getConnection(url, username, password);

            if (!conn.isClosed())
            {
                log.info("Succeeded connecting to the Database!");
            }

            log.info("--create statement----");
            statement = conn.createStatement();
            log.info("--create statement successfully----");
            if (buyerOids == null || buyerOids.trim().isEmpty())
            {
                log.info("---no buyerOids achieved, return----");
                return;
            }
            String sql = "SELECT * FROM USER_PROFILE WHERE LOGIN_PWD IS NULL AND BUYER_OID IN (" + buyerOids + ")";
            log.info(sql);
            rs = statement.executeQuery(sql);
            log.info("---get rs----");
            String requestUrl = appConfig.getServerUrl() + "/user/";
            log.info("---rs size---"+rs.getFetchSize());
            while (rs.next())
            {
                
                String loginId = rs.getString("login_id");
                
                log.info("---login Id---" + loginId);
                ResetPasswordRequestHistoryHolder param = new ResetPasswordRequestHistoryHolder();
                param.setLoginId(loginId);
                param.setValid(true);
                List<ResetPasswordRequestHistoryHolder> rprhs = resetPasswordRequestHistoryService
                        .select(param);
                if (rprhs != null && !rprhs.isEmpty())
                {
                    for (ResetPasswordRequestHistoryHolder rprh : rprhs)
                    {
                        rprh.setValid(false);
                        resetPasswordRequestHistoryService.updateByPrimaryKeySelective(null, rprh);
                    }
                }
                String hashValue = EncodeUtil.getInstance().computeSha2Digest(
                        EncodeUtil.getInstance().generateSecureBytes(16));
                String hyperlink = requestUrl.substring(0,
                        requestUrl.lastIndexOf('/') + 1)
                        + "setPassword.action?h=" + hashValue;
                ResetPasswordRequestHistoryHolder newResetPwdRecord = new ResetPasswordRequestHistoryHolder();
                newResetPwdRecord.setHashCode(hashValue);
                newResetPwdRecord.setLoginId(loginId);
                newResetPwdRecord.setClientIp(InetAddress.getLocalHost().getHostAddress());
                newResetPwdRecord.setRequestTime(new Date());
                newResetPwdRecord.setValid(Boolean.TRUE);
                
                resetPasswordRequestHistoryService.resetPasswordWithoutAudit(
                        null, newResetPwdRecord);
                
                Date expireDate = DateUtil
                        .getInstance()
                        .dateAfterDays(
                                newResetPwdRecord.getRequestTime(),
                                controlParameterService
                                .selectCacheControlParameterBySectIdAndParamId(
                                        CoreCommonConstants.SECT_ID_CTRL,
                                        CoreCommonConstants.PARAM_ID_MAIL_EXPIRE_DAYS)
                                        .getNumValue());
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("loginId", loginId);
                map.put("name", rs.getString("user_name"));
                map.put("hyperlink", hyperlink);
                map.put("expireDate",
                        DateUtil.getInstance().convertDateToString(expireDate,
                                DateUtil.DATE_FORMAT_DDMMYYYYHHMMSS));
                map.put("helpdeskNumber",
                        controlParameterService
                                .selectCacheControlParameterBySectIdAndParamId(
                                        CoreCommonConstants.SECT_ID_CTRL,
                                        CoreCommonConstants.PARAM_ID_HELPDESK_NO)
                                .getStringValue());
                emailEngine.sendHtmlEmail(
                        new String[] { rs.getString("email") },
                        "Suppliers Portal: Creation of New User",
                        "mail_setPassword_link.vm", map);
            }
        }
        catch (ClassNotFoundException e)
        {
            log.info("Sorry,can`t find the Driver!");
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        catch (SQLException e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logTicketNo(log, e);
        }
        finally
        {
            try
            {
                if(rs != null)
                {
                    rs.close();
                }
            }
            catch(SQLException e)
            {
                ErrorHelper.getInstance().logTicketNo(log, e);
            }
                
            try
            {
                if(statement != null)
                {
                    statement.close();
                }
            }
            catch(SQLException e)
            {
                ErrorHelper.getInstance().logTicketNo(log, e);
            }
                
            try
            {
                if(conn != null)
                {
                    conn.close();
                }
            }
            catch(SQLException e)
            {
                ErrorHelper.getInstance().logTicketNo(log, e);
            }
            
        }
        log.info(":::: process successfully.");
    }


    public void setEmailEngine(EmailEngine emailEngine)
    {
        this.emailEngine = emailEngine;
    }


    public void setAppConfig(CustomAppConfigHelper appConfig)
    {
        this.appConfig = appConfig;
    }


    public void setControlParameterService(
            ControlParameterService controlParameterService)
    {
        this.controlParameterService = controlParameterService;
    }


    public void setResetPasswordRequestHistoryService(
            ResetPasswordRequestHistoryService resetPasswordRequestHistoryService)
    {
        this.resetPasswordRequestHistoryService = resetPasswordRequestHistoryService;
    }


    public void setDriverClass(String driverClass)
    {
        this.driverClass = driverClass;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public void setPassword(String password)
    {
        this.password = password;
    }


    public void setBuyerOids(String buyerOids)
    {
        this.buyerOids = buyerOids;
    }
    
    */

}
