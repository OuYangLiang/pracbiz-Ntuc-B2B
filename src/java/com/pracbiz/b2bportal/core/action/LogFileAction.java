package com.pracbiz.b2bportal.core.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.pracbiz.b2bportal.base.action.GridResult;
import com.pracbiz.b2bportal.base.holder.BaseHolder;
import com.pracbiz.b2bportal.base.holder.MessageTargetHolder;
import com.pracbiz.b2bportal.base.util.CommonConstants;
import com.pracbiz.b2bportal.base.util.DateUtil;
import com.pracbiz.b2bportal.base.util.ErrorHelper;
import com.pracbiz.b2bportal.base.util.GZIPHelper;
import com.pracbiz.b2bportal.core.holder.ControlParameterHolder;
import com.pracbiz.b2bportal.core.holder.LogFileHolder;
import com.pracbiz.b2bportal.core.service.ControlParameterService;

/**
 * TODO To provide an overview of this class.
 * 
 * @author GeJianKui
 */
public class LogFileAction extends ProjectBaseAction
{
    private static final Logger log = LoggerFactory.getLogger(LogFileAction.class);
    private static final long serialVersionUID = -5857082697968865583L;
    public static final String SESSION_KEY_SEARCH_PARAMETER_LOG_FILE = "SEARCH_PARAMETER_LOG_FILE";
    private static final String SESSION_PARAMETER_OID_NOT_FOUND_MSG = "selected files is not found in session scope.";
    private static final String SESSION_FILE_PARAMETER = "session.parameter.LogFileAction.selectedFiles";
    private static final String REQUEST_PARAMTER_FILE = "selectedFiles";
    private static final String REQUEST_FILE_DELIMITER = "\\|";

    private static final Map<String, String> sortMap;

    static
    {
        sortMap = new HashMap<String, String>();
        sortMap.put("fileName", "getFileName");
        sortMap.put("fileSize", "getFileSize");
        sortMap.put("lastModifiedTime", "getLastModifiedTime");
    }

    @Autowired
    private transient ControlParameterService controlParameterService;
    private transient Comparator<BaseHolder> comparator; 
    private static String logFilePath;
    private static List<String> logFileNameRules;
    private LogFileHolder logFile;
    private transient InputStream logResult;
    private String logFileName;
    private String contentType;
    private int startLine = 1;
    private int endLine = 500;


    public LogFileAction()
    {
        this.initMsg();
    }


    public String putParamIntoSession() throws Exception
    {
        this.getSession().put(SESSION_FILE_PARAMETER, 
                this.getRequest().getParameter(REQUEST_PARAMTER_FILE));
        return SUCCESS;
    }


    // ***************************************
    // Search for summary
    // ***************************************

    public String init()
    {
        clearSearchParameter(SESSION_KEY_SEARCH_PARAMETER_LOG_FILE);

        logFile = (LogFileHolder) getSession().get(
                SESSION_KEY_SEARCH_PARAMETER_LOG_FILE);

        return SUCCESS;

    }
    
    
    public void validateSearch()
    {
        boolean flag = this.hasErrors();
        try
        {
            if (null == logFile || flag)
            {
                return;
            }
            Date now = DateUtil.getInstance().getMaxTimeOfDate(new Date());

            if (logFile.getSearchLastModifiedFrom() != null
                    && !"".equals(logFile.getSearchLastModifiedFrom()
                            .toString().trim())
                    && logFile.getSearchLastModifiedTo() != null
                    && !"".equals(logFile.getSearchLastModifiedTo().toString()
                            .trim()))
            {

                logFile.setSearchLastModifiedTo(DateUtil.getInstance()
                        .getMaxTimeOfDate(logFile.getSearchLastModifiedTo()));

                if (DateUtil.getInstance().compareDate(
                        logFile.getSearchLastModifiedFrom(),
                        logFile.getSearchLastModifiedTo()) > 0)
                {
                    flag = true;
                    this.addActionError(this.getText("B2BPC1302"));
                }
            }
            if (!flag
                    && logFile.getSearchLastModifiedTo() != null
                    && !"".equals(logFile.getSearchLastModifiedTo().toString()
                            .trim()))
            {
                logFile.setSearchLastModifiedTo(DateUtil.getInstance()
                        .getMaxTimeOfDate(logFile.getSearchLastModifiedTo()));
            }
            if (!flag
                    && logFile.getSearchLastModifiedFrom() != null
                    && !"".equals(logFile.getSearchLastModifiedFrom()
                            .toString().trim()) 
                    && DateUtil.getInstance().compareDate(
                            logFile.getSearchLastModifiedFrom(), now) > 0)
            {
                    this.addActionError(this.getText("B2BPC1303"));
                    flag = true;
            }
        }
        catch (Exception e)
        {
            log.error("validateSearch:");
            log.error("Error occur on validateSearch() Report ", e);
            ErrorHelper.getInstance().logError(log, this, e);
        }
    }

    
    public String search()
    {
        
        if (null == logFile)
        {
            logFile = new LogFileHolder();
        }
        try
        {
            logFile.trimAllString();
            logFile.setAllEmptyStringToNull();
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        getSession().put(SESSION_KEY_SEARCH_PARAMETER_LOG_FILE, logFile);

        return SUCCESS;
    }


    public String data() throws Exception
    {
        try
        {
            logFile = (LogFileHolder) getSession().get(
                    SESSION_KEY_SEARCH_PARAMETER_LOG_FILE);

            if (null == logFile)
            {
                logFile = new LogFileHolder();
                getSession().put(SESSION_KEY_SEARCH_PARAMETER_LOG_FILE, logFile);
            }
            logFile.setRequestPage(start / count + 1);
            logFile.setPageSize(count);
            this.obtainListRecordsOfPagination(logFile, sortMap, "hashCode");
        }
        catch (Exception e)
        {
            ErrorHelper.getInstance().logError(log, this, e);
        }

        return SUCCESS;
    }


    public String saveDownload()
    {
        try
        {
            msg.setTitle(this.getText(INFORMATION_MSG_TITLE_KEY));
            MessageTargetHolder mt = new MessageTargetHolder();
            mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
            mt.setTargetURI(INIT);
            mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);
            Object selectedOids = this.getSession().get(SESSION_FILE_PARAMETER);
            if (null == selectedOids || "".equals(selectedOids))
            {
                msg.saveError(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                msg.addMessageTarget(mt);
                log.info(SESSION_PARAMETER_OID_NOT_FOUND_MSG);
                return FORWARD_COMMON_MESSAGE;
            }

            this.getSession().remove(SESSION_FILE_PARAMETER);

            String[] parts = selectedOids.toString().split(
                    REQUEST_FILE_DELIMITER);
                    
            if (parts.length == 1)
            {
                File file = this.listFileByHashCode(parts[0]);
                BigDecimal fl = new BigDecimal(file.length());
                if (fl.divide(BigDecimal.valueOf(1048576)).compareTo(BigDecimal.valueOf(5)) > 0)
                {
                    msg.saveError(this.getText("B2BPC1309"));
                    msg.addMessageTarget(mt);
                    log.info(this.getText("B2BPC1309"));
                    return FORWARD_COMMON_MESSAGE;
                }
                logFileName = new String(file.getName().getBytes("UTF-8"), "ISO-8859-1");
                contentType = "application/octet-stream";
                logResult = new java.io.FileInputStream(file);
                log.info(this.getText("B2BPC1310", new String[] { logFileName }));
                return SUCCESS;
            }
            List<File> files = new ArrayList<File>();
            Long fileLength = 0L;
            for (String hashCode : parts)
            {
                File file = this.listFileByHashCode(hashCode);
                fileLength += file.length();
                files.add(file);
            }
            logFileName = "LOG_"
                    + DateUtil.getInstance().getCurrentLogicTimeStamp()
                    + ".zip";
            GZIPHelper.getInstance().doZip(files, logFilePath, logFileName);
            File file = new File(logFilePath, logFileName);
            
            BigDecimal fl = new BigDecimal(fileLength);
            if (fl.divide(BigDecimal.valueOf(1048576)).compareTo(BigDecimal.valueOf(20)) > 0)
            {
                msg.saveError(this.getText("B2BPC1301"));
                msg.addMessageTarget(mt);
                log.info(this.getText("B2BPC1301"));
                if (!file.delete())
                {
                    log.info(this.getText("B2BPC1304"));
                }
                return FORWARD_COMMON_MESSAGE;
            }
            
            logResult = new java.io.FileInputStream(file);
            if (!file.delete())
            {
                log.info(this.getText("B2BPC1304"));
            }
            contentType = "application/octet-stream";
            log.info(this.getText("B2BPC1310", new String[] { logFileName }));
        }
        catch (Exception e)
        {
            handleException(e);
        }
        return SUCCESS;
    }


    // ***************************************
    // View function
    // ***************************************

    public void validateView()
    {
        try
        {
            boolean flag = this.hasFieldErrors();

            if (!flag && (logFile == null || logFile.getHashCode() == null))
            {
                this.addActionError(this.getText("B2BPC1305"));
                flag = true;
            }
            if (!flag)
            {
                File file = listFileByHashCode(logFile.getHashCode());
                if (!file.exists())
                {
                    this.addActionError(this.getText("B2BPC1306"));
                    flag = true;
                }
            }
            if (!flag && (endLine < 0 || startLine < 0))
            {
                this.addActionError(this.getText("B2BPC1307"));
                flag = true;
            }
            if (!flag && (endLine < startLine))
            {
                this.addActionError(this.getText("B2BPC1308"));
                flag = true;
            }
        }
        catch (Exception e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);

            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
    }


    public String view()
    {
        File file = this.listFileByHashCode(logFile.getHashCode());
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;
        try
        {
            logFile.setFileName(file.getName());
            logFile.setFileSize(file.length() / 1024);
            logFile.setLastModifiedTime(DateUtil.getInstance()
                    .getDateFromTimeStamp(file.lastModified()));
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, CommonConstants.ENCODING_UTF8);
            reader = new BufferedReader(isr);
            
            String tempString = null;
            int line = 1;
            int contentLine = 0;
            int numOfLines = endLine - startLine;
            StringBuffer content = new StringBuffer("");
            while ((tempString = reader.readLine()) != null)
            {
                if (line >= startLine && contentLine <= numOfLines)
                {
                    content.append(line + " - " + tempString + "\n");
                    contentLine++;
                }
                line++;
            }
            reader.close();
            logFile.setTotalLine(line - 1);
            logFile.setStringContent(content.toString());
        }
        catch (IOException e)
        {
            String tickNo = ErrorHelper.getInstance().logTicketNo(log, this, e);
            this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                    new String[] { tickNo }));
        }
        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();
                    reader = null;
                }
                
                if (isr != null)
                {
                    isr.close();
                    isr = null;
                }
                
                if (fis != null)
                {
                    fis.close();
                    fis = null;
                }
            }
            catch (IOException e1)
            {
                String tickNo = ErrorHelper.getInstance().logTicketNo(log,
                        this, e1);
                this.addActionError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                        new String[] { tickNo }));
            }
        }
        return SUCCESS;
    }


    // *****************************************************
    // private method
    // *****************************************************

    protected void obtainListRecordsOfPagination(LogFileHolder param,
            Map<String, String> sortFieldMap, String itemIdentifier)
            throws Exception
    {
        initHideConditions();

        List<File> files = this.getCountOfSummary();

        calculateRecordNum(param);

        initSorting(param, sortFieldMap);

        List<BaseHolder> recordList = this.getListOfSummary(files);

        int index = start;
        for (BaseHolder bh : recordList)
            bh.setDojoIndex(++index);

        gridRlt = new GridResult();
        gridRlt.setIdentifier(itemIdentifier);
        gridRlt.setItems(recordList);
        gridRlt.setTotalRecords(files.size());
    }


    private List<BaseHolder> getListOfSummary(List<File> files)
    {
        List<BaseHolder> holderRlt = new ArrayList<BaseHolder>();
        if (files != null && !files.isEmpty())
        {
            for (File file : files)
            {
                LogFileHolder lgh = new LogFileHolder();
                lgh.setFileName(file.getName());
                lgh.setLastModifiedTime(DateUtil.getInstance()
                        .getDateFromTimeStamp(file.lastModified()));
                lgh.setFileSize(file.length() / 1024);
                lgh.setHashCode(String.valueOf(file.hashCode()));
                holderRlt.add(lgh);
            }
            setComparator();
            Collections.sort(holderRlt, comparator);
            if (logFile.getStartRecordNum() == 0
                    && logFile.getNumberOfRecordsToSelect() == 0)
            {
                return holderRlt;
            }
            List<BaseHolder> selectRecords = new ArrayList<BaseHolder>();
            for (int i = 0; i < holderRlt.size(); i++)
            {

                if (i >= logFile.getStartRecordNum()
                        && selectRecords.size() < logFile
                                .getNumberOfRecordsToSelect())
                {
                    selectRecords.add(holderRlt.get(i));
                }
            }
            return selectRecords;
        }

        return holderRlt;
    }


    private List<File> getCountOfSummary() throws Exception
    {
        List<File> files = new ArrayList<File>();
        if (null == logFilePath || "".equals(logFilePath))
        {
            return files;
        }
        File filePath = new File(logFilePath);
        if (filePath.isDirectory())
        {
            files = listFilesForLogFile(filePath);

            return files;
        }
        return files;
    }


    private List<File> listFilesForLogFile(File filePath)
    {
        List<File> rlt = new ArrayList<File>();
        File files[] = filePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file)
            {
                String filename = file.getName().toLowerCase();
                boolean flag = false;
                if (null == logFileNameRules || logFileNameRules.isEmpty())
                {
                    return false;
                }
                for (String regex : logFileNameRules)
                {
                    regex = replaceWildcardForFilenameRules(regex);
                    if (filename.matches(regex))
                    {
                        flag = true;
                    }
                }
                if (flag && logFile.getSearchFileName() != null
                        && !"".equals(logFile.getSearchFileName()))
                {
                    if (filename.contains(logFile.getSearchFileName()))
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }
                }
                if (flag
                        && logFile.getSearchLastModifiedFrom() != null
                        && !"".equals(logFile.getSearchLastModifiedFrom()
                                .toString()))
                {
                    Date lastModified = DateUtil.getInstance()
                            .getDateFromTimeStamp(file.lastModified());
                    if (DateUtil.getInstance().compareDate(lastModified,
                            logFile.getSearchLastModifiedFrom()) > 0)
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }
                }
                if (flag
                        && logFile.getSearchLastModifiedTo() != null
                        && !"".equals(logFile.getSearchLastModifiedTo()
                                .toString()))
                {
                    Date lastModified = DateUtil.getInstance()
                            .getDateFromTimeStamp(file.lastModified());
                    if (DateUtil.getInstance().compareDate(
                            logFile.getSearchLastModifiedTo(), lastModified) > 0)
                    {
                        flag = true;
                    }
                    else
                    {
                        flag = false;
                    }
                }
                return flag;
            }
        });
        if (files == null || files.length == 0)
        {
            return rlt;
        }
        for (File file : files)
        {
            rlt.add(file);
        }
        return rlt;
    }


    private void initHideConditions() throws Exception
    {
        logFileNameRules = new ArrayList<String>();

        List<ControlParameterHolder> ctrlParams = controlParameterService
                .selectCacheControlParametersBySectId(SECT_ID_CTRL);
        for (ControlParameterHolder ctrl : ctrlParams)
        {
            if (PARAM_ID_LOG_FILE_PATH.equalsIgnoreCase(ctrl.getParamId()))
            {
                logFilePath = ctrl.getStringValue();
            }
            else if (PARAM_ID_LOG_FILE_NAME.equalsIgnoreCase(ctrl
                    .getParamId()))
            {
                String filename = ctrl.getStringValue();
                if (null != filename && filename.contains(","))
                {
                    String[] fileRules = filename.split(",");
                    for (String rule : fileRules)
                    {
                        logFileNameRules.add(rule.trim());
                    }
                }
                else if (null != filename)
                {
                    logFileNameRules.add(filename.trim());
                }
            }
        }
    }


    private void initSorting(LogFileHolder param,
            Map<String, String> sortFieldMap)
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


    private void calculateRecordNum(LogFileHolder param)
    {
        param.setStartRecordNum(start);
        param.setNumberOfRecordsToSelect(count);
    }

    
    public void setComparator()
    {
        comparator = new Comparator<BaseHolder>() {
            public int compare(BaseHolder b1, BaseHolder b2)
            {
                if (logFile.getSortField() == null
                        || "".equals(logFile.getSortField()))
                {
                    return 0;
                }
                LogFileHolder l1 = (LogFileHolder) b1;
                LogFileHolder l2 = (LogFileHolder) b2;
                Object result1 = "";
                Object result2 = "";
                try
                {
                    Method[] methods = l1.getClass().getMethods();
                    for (Method m : methods)
                    {
                        if (m.getName().startsWith("set"))
                        {
                            continue;
                        }
                        if (m.getName().equals(logFile.getSortField()))
                        {
                            result1 = m.invoke(l1, new Object[] {});
                            break;
                        }

                    }
                    Method[] methods2 = l2.getClass().getDeclaredMethods();
                    for (Method m : methods2)
                    {
                        if (m.getName().startsWith("set"))
                        {
                            continue;
                        }
                        if (m.getName().equals(logFile.getSortField()))
                        {
                            result2 = m.invoke(l2, new Object[] {});
                            break;
                        }
                    }
                    if (logFile.getSortOrder().equals("ASC"))
                    {
                        if (result1 instanceof String)
                        {
                            return result1.toString().compareTo(result2.toString());
                        }
                        else if (result1 instanceof Date)
                        {
                            return ((Date) result1).compareTo(((Date) result2));
                        }
                        else if (result1 instanceof Long)
                        {
                            return ((Long) result1).compareTo(((Long) result2));
                        }
                    }
                    else
                    {
                        if (result2 instanceof String)
                        {
                            return result2.toString().compareTo(result1.toString());
                        }
                        else if (result2 instanceof Date)
                        {
                            return ((Date) result2).compareTo(((Date) result1));
                        }
                        else if (result2 instanceof Long)
                        {
                            return ((Long) result2).compareTo(((Long) result1));
                        }
                    }
                }
                catch (Exception e)
                {
                    ErrorHelper.getInstance().logError(log, e);
                }
                return 0;
            }
        };
    }


    private void handleException(Exception e)
    {
        String tickNo = ErrorHelper.getInstance().logTicketNo(log, e);

        msg.setTitle(this.getText(EXCEPTION_MSG_TITLE_KEY));
        msg.saveError(this.getText(EXCEPTION_MSG_CONTENT_KEY,
                new String[] { tickNo }));

        MessageTargetHolder mt = new MessageTargetHolder();
        mt.setTargetBtnTitle(this.getText(BACK_TO_LIST));
        mt.setTargetURI(INIT);
        mt.addRequestParam(REQ_PARAMETER_KEEP_SEARCH_CONDITION, VALUE_YES);

        msg.addMessageTarget(mt);
    }


    private File listFileByHashCode(final String hashCode)
    {
        File filePath = new File(logFilePath);
        File files[] = filePath.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file)
            {
                String filename = file.getName().toLowerCase();
                boolean flag = false;
                for (String regex : logFileNameRules)
                {
                    regex = replaceWildcardForFilenameRules(regex);
                    if (filename.matches(regex))
                    {
                        flag = true;
                    }
                }
                if (flag && hashCode.equals(String.valueOf(file.hashCode())))
                {
                    return true;
                }
                return false;
            }
        });
        if (files == null || files.length == 0 || files.length > 1)
        {
            return null;
        }
        return files[0];
    }

    
    private String replaceWildcardForFilenameRules(String nameRules)
    {
        String regex = nameRules;
        regex = regex.replace(".", "#");
        regex = regex.replaceAll("#", "\\\\.");
        regex = regex.replace("*", "#");
        regex = regex.replaceAll("#", ".*");
        regex = regex.replace("?", "#");
        regex = regex.replaceAll("#", ".?");
        regex = "^" + regex + "$";
        return regex;
    }

    // *****************************************************
    // Getter and Setter method
    // *****************************************************

    public LogFileHolder getLogFile()
    {
        return logFile;
    }


    public void setLogFile(LogFileHolder logFile)
    {
        this.logFile = logFile;
    }


    public InputStream getLogResult()
    {
        return logResult;
    }


    public void setLogResult(InputStream logResult)
    {
        this.logResult = logResult;
    }


    public String getLogFileName()
    {
        return logFileName;
    }


    public void setLogFileName(String logFileName)
    {
        this.logFileName = logFileName;
    }


    public String getContentType()
    {
        return contentType;
    }


    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


    public int getStartLine()
    {
        return startLine;
    }


    public void setStartLine(int startLine)
    {
        this.startLine = startLine;
    }


    public int getEndLine()
    {
        return endLine;
    }


    public void setEndLine(int endLine)
    {
        this.endLine = endLine;
    }

}
