package com.pracbiz.b2bportal.core.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import com.pracbiz.b2bportal.base.email.EmailConfigHelper;
import com.pracbiz.b2bportal.base.util.BaseXmlConfig;
import com.pracbiz.b2bportal.core.interceptor.SecurityInterceptorHelpHolder;


public final class CustomAppConfigHelper extends BaseXmlConfig
{
    private static final String REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER = "report-config.customized-report.buyer(";
    @Autowired
    private EmailConfigHelper emailConfigHelper;
    
    private static final String PO_STANDARD_EXCEL_TEMPLATE = "standardPOExcelDefaultEngine";

    private CustomAppConfigHelper(File cfgFile) throws ConfigurationException,
            MalformedURLException
    {
        super(cfgFile);
    }


    public static CustomAppConfigHelper getBeanInstance(Resource configPath)
            throws ConfigurationException, MalformedURLException, IOException
    {
        
        CustomAppConfigHelper theSiteConfig = new CustomAppConfigHelper(
                configPath.getFile());

        theSiteConfig.initConfigMap();
        return theSiteConfig;
    }


    // ------ custom methods -------//
    
    public String getSysDateInputFmt()
    {
        return stringValue("site.sysDateTime.date.inputFormat");
    }

    
    public String getSysDateDisplayFmt()
    {
        return stringValue("site.sysDateTime.date.displayFormat");
    }

    
    public String getSysTimestampDisplayFmt()
    {
        return getSysDateDisplayFmt() + " "
                + stringValue("site.sysDateTime.time.displayFormat");
    }


    public List<String> getLoginModel()
    {
        return listValue("login-model.value");
    }
    
    public boolean autoLoginEnable()
    {
        String autoLogin = stringValue("auth.[@autoLogin]");
        
        if (StringUtils.isBlank(autoLogin))
        {
            return false;
        }
        
        return autoLogin.equalsIgnoreCase("y");
    }
    
    
    // The Windows domain name
    public String getDomain()
    {
        return stringValue("auth.ntlm-auth-domain");
    }

    
    // The domain controller IP address
    public String getDomainCtrl()
    {
        return stringValue("auth.ntlm-auth-domain-controller");
    }

    
    // The domain controller hostname
    public String getDomainCtrlName()
    {
        return stringValue("auth.ntlm-auth-domain-controller-name");
    }

    
    // The computer account for the connection to the DC
    public String getDomainServiceAccount()
    {
        return stringValue("auth.ntlm-auth-service-account");
    }
    
    
    // The password of the computer account
    public String getDomainServiceAccountPwd()
    {
        return stringValue("auth.ntlm-auth-service-account-password");
    }
    
    
    public String getJcifsCachePolicy()
    {
        return stringValue("jcifs.netbios-cachePolicy");
    }
    
    
    public String getJcifsSmbSoTimeOut()
    {
        return stringValue("jcifs.smb-client-soTimeout");
    }
    
    
    public String getMaxInvNoLength()
    {
        return stringValue("maxInvNoLength");
    }
    
    
    public String getStorePrefix()
    {
        return stringValue("prefix.store");
    }
    
    
    public String getWarehousePrefix()
    {
        return stringValue("prefix.warehouse");
    }
    
    /*public BatchType getBatchTypeByFilename(String filename) throws Exception
    {
        List<String> fileRegexes = this.listValue("fileConfig.batchFile.file");

        for(int i = 0; i < fileRegexes.size(); i++)
        {
            String regex = fileRegexes.get(i);

            if(filename.matches(regex))
            {
                return BatchType
                    .valueOf(this.stringValue("fileConfig.batchFile.file(" + i
                        + ")[@type]"));
            }
        }

        throw new Exception("Un-recognized batch file: " + filename);
    }*/
    
    
    /*public MsgType getMsgTypeByFilenameAndFileFormat(String filename,
        String format) throws Exception
    {
        List<String> regexes = this.listValue("fileConfig.docFile.file");

        for(int i = 0; i < regexes.size(); i++)
        {
            String regex = regexes.get(i);

            if(filename.matches(regex))
            {
                String cfgFormat = this.stringValue("fileConfig.docFile.file("
                    + i + ")[@format]");

                if(format.equalsIgnoreCase(cfgFormat))
                {
                    return MsgType.valueOf(this
                        .stringValue("fileConfig.docFile.file(" + i
                            + ")[@type]"));
                }
            }

        }

        throw new Exception("Un-recognized doc file: " + filename);
    }*/
    
    
//    public String getBuyerMailboxRootPath()
//    {
//        return this.stringValue("generic.mailbox.buyer.path");
//    }
//
//    
//    public String getSupplierMailboxRootPath()
//    {
//        return this.stringValue("generic.mailbox.supplier.path");
//    }
//    
//    
//    public String getChannelMailboxRootPath()
//    {
//        return this.stringValue("generic.mailbox.channel.path");
//    }
    
    
    public List<String> getFileFormatListByMsgType(String msgType)
    {
        List<String> names = listValue("generic.msgType-config.msgType.[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            if (name.equalsIgnoreCase(msgType))
            {
                return listValue("generic.msgType-config.msgType("+i+").doc-format-list.value");
            }
        }
        return new ArrayList<String>();
    }
    
    public String getDefaultFileFormat(String msgType)
    {
        List<String> names = listValue("generic.msgType-config.msgType.[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            if (name.equalsIgnoreCase(msgType))
            {
                return stringValue("generic.msgType-config.msgType("+i+").doc-format-list.[@default]");
            }
        }
        return null;
    }
    
    public boolean isBasicData(String msgType)
    {
        List<String> names = listValue("generic.msgType-config.msgType.[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            if (name.equalsIgnoreCase(msgType))
            {
                String basicData = stringValue("generic.msgType-config.msgType("+i+").[@basicData]");
                return basicData == null || !basicData.equalsIgnoreCase("y") ? false : true;
            }
        }
        return false;
    }
    
    
    public List<String> getSubTypesByMsgType(String msgType)
    {
        List<String> names = listValue("generic.msgType-config.msgType.[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            if (name.equalsIgnoreCase(msgType))
            {
                return listValue("generic.msgType-config.msgType("+i+").subTypes.value");
            }
        }
        return new ArrayList<String>();
    }
    
    public String getDefaultSubTypeByMsgType(String msgType)
    {
        List<String> names = listValue("generic.msgType-config.msgType.[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            if (name.equalsIgnoreCase(msgType))
            {
                return stringValue("generic.msgType-config.msgType("+i+").subTypes.[@default]");
            }
        }
        return null;
    }
    
    
    public Map<String, String> getStandardReports(String msgType, String subType)
    {
        Map<String, String> rlt = new HashMap<String, String>();
        List<String> types = this.listValue("report-config.standard-report.report.[@type]");
        for (int i = 0; i < types.size(); i++)
        {
            String type = types.get(i);
            if(type.equalsIgnoreCase(msgType))
            {
                List<String> subTypes = this.listValue("report-config.standard-report.report(" + i + ").subType.[@name]");
                for (int j = 0; j < subTypes.size() ; j++)
                {
                    if (subTypes.get(j).equalsIgnoreCase(subType))
                    {
                        rlt = this.getReportTemplates("report-config.standard-report.report(" + i + ").subType(" + j + ")");
                    }
                }
            }
        }
        return rlt;
    }


    public Map<String, String> getCustomizedReports(
            String buyerCode, String msgType, String subType)
    {
        Map<String, String> rlt = new HashMap<String, String>();
        List<String> buyers = this.listValue("report-config.customized-report.buyer.[@id]");
        for (int i = 0; i < buyers.size(); i++)
        {
            String buyer = buyers.get(i);
            if(buyer.equalsIgnoreCase(buyerCode))
            {
                List<String> types = this.listValue(REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER + i + ").report.[@type]");
                for (int j = 0; j < types.size(); j++)
                {
                    String type = types.get(j);
                    if(type.equalsIgnoreCase(msgType))
                    {
                        List<String> subTypes = this.listValue(REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER + i + ").report(" + j + ").subType.[@name]");
                        for (int m = 0; m < subTypes.size() ; m++)
                        {
                            if (subTypes.get(m).equalsIgnoreCase(subType))
                            {
                                rlt = this.getReportTemplates(REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER + i + ").report(" + j + ").subType(" + m + ")");
                            }
                        }
                    }
                }
            }
        }
        return rlt;
    }
    
    
    public String getStandardReport(String msgType, String subType, String reportName)
    {
        if (null == reportName || reportName.isEmpty())
        {
            return this.getDefaultStandardReport(msgType, subType);
        }
        
        return getStandardReports(msgType, subType).get(reportName);
    }
    
    
    public String getDefaultStandardReport(String msgType, String subType)
    {
        List<String> types = this.listValue("report-config.standard-report.report[@type]");
        
        for (int i = 0; i < types.size(); i++)
        {
            String type = types.get(i);
            
            if (type.equalsIgnoreCase(msgType))
            {
                List<String> subTypes = this.listValue("report-config.standard-report.report(" + i + ").subType.[@name]");
                for (int m = 0; m < subTypes.size() ; m++)
                {
                    if (subTypes.get(m).equalsIgnoreCase(subType))
                    {
                        return this.getStandardReport(
                                msgType, subType,
                                this.stringValue("report-config.standard-report.report(" + i
                                        + ").subType(" + m + ")[@default]"));
                    }
                }
            }
        }
        
        return null;
    }
    
    
    public String getCustomizedExcelReport(String buyerCode, String msgType)
    {
        String template = null;
        List<String> buyers = this.listValue("excel-report-config.customized-excel.buyer[@id]");
        
        for (int i = 0; i < buyers.size(); i++)
        {
            String buyer = buyers.get(i);
            
            if (buyer.equalsIgnoreCase(buyerCode))
            {
                List<String> types = this
                    .listValue("excel-report-config.customized-excel.buyer(" + i
                        + ").report[@type]");
                
                for (int j = 0; j < types.size(); j++)
                {
                    String type = types.get(j);
                    
                    if (type.equalsIgnoreCase(msgType))
                    {
                        template = this.stringValue("excel-report-config.customized-excel.buyer(" + i
                            + ").report("+ j+ ").reportTemplate[@bean]");
                    }
                }
            }
        }
        
        if (null == template && "PO".equalsIgnoreCase(msgType))
        {
            template = PO_STANDARD_EXCEL_TEMPLATE;
        }
        
        return template;
    }
    
    public String getCustomizedReport(String buyerCode, String msgType, String subType, String reportName)
    {
        if (null == reportName || reportName.isEmpty())
        {
            return this.getDefaultCustomizedReport(buyerCode, msgType, subType);
        }
        
        return getCustomizedReports(buyerCode, msgType, subType).get(reportName);
    }
    
    
    public String getDefaultCustomizedReport(String buyerCode, String msgType, String subType)
    {
        List<String> buyers = this.listValue("report-config.customized-report.buyer[@id]");
        
        for (int i = 0; i < buyers.size(); i++)
        {
            String buyer = buyers.get(i);
            
            if (buyer.equalsIgnoreCase(buyerCode))
            {
                List<String> types = this
                    .listValue(REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER + i
                        + ").report[@type]");
                
                for (int j = 0; j < types.size(); j++)
                {
                    String type = types.get(j);
                    
                    if (type.equalsIgnoreCase(msgType))
                    {
                        List<String> subTypes = this.listValue("report-config.standard-report.report(" + i + ").subType.[@name]");
                        for (int m = 0; m < subTypes.size() ; m++)
                        {
                            if (subTypes.get(m).equalsIgnoreCase(subType))
                            {
                                return this.getCustomizedReport(buyerCode, msgType, subType,
                                        this.stringValue(REPORT_CONFIG_CUSTOMIZED_REPORT_BUYER
                                                + i + ").report(" + j + ").subType(" + m + ")[@default]"));
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    
    private Map<String, String> getReportTemplates(String parantElement)
    {
        Map<String, String> map = new HashMap<String, String>();
        List<String> names = this.listValue(parantElement + ".reportTemplate[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            String bean = this.stringValue(parantElement + ".reportTemplate(" + i + ")[@bean]");
            map.put(name, bean);
        }
        return map;
    }
    
    
    public String getAdminEmail()
    {
        return  emailConfigHelper.getAdminAddr();
    }
    
    
    public String getServerUrl()
    {
        return  this.stringValue("site.serverUrl");
    }
    
    
    public String getServerId()
    {
        return  this.stringValue("site.identifier").trim();
    }

    
    public String getMaxZipFile()
    {
        return this.stringValue("dispatchSetting.maxFilesPerZip");
    }
    
    
    public String getMaxSizeOfUploaded()
    {
        return this.stringValue("dispatchSetting.maxSizeOfUploaded");
    }
    
    public String getEmailPattern()
    {
        List<String> keys = this.listValue("validation.patterns.pattern[@key]");
        for (int i = 0; i < keys.size(); i++)
        {
            String key = keys.get(i);
            if ("EMAIL_ADDRESS".equals(key))
            {
                return this.stringValue("validation.patterns.pattern(" + i + ")");
            }
        }
        return null;
    }
    
    
    public List<SecurityInterceptorHelpHolder> getSecurityInterceptorMethods()
    {
        List<SecurityInterceptorHelpHolder> methods = new ArrayList<SecurityInterceptorHelpHolder>();
        List<String> names = this.listValue("method[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            String param = this.stringValue("method("+i+")[@param]");
            String moduleKey = this.stringValue("method("+i+")[@moduleKey]");
            String separator = this.stringValue("method("+i+")[@separator]");
            SecurityInterceptorHelpHolder obj = new SecurityInterceptorHelpHolder();
            obj.setName(name);
            obj.setParam(param);
            obj.setModuleKey(moduleKey);
            obj.setSeparator(separator);
            methods.add(obj);
        }
        return methods;
    }
    
    
    public List<String> getAttachmentsForSAJob()
    {
        return listValue("attachmentsForSAJob.attachment");
    }
    
    
    public Map<String, String> getSupplierMasterGstConfig()
    {
        Map<String, String> map = new HashMap<String, String>();
        List<String> names = this.listValue("supplierMasterGstConfig.buyer[@name]");
        for (int i = 0; i < names.size(); i++)
        {
            String name = names.get(i);
            String type = this.stringValue("supplierMasterGstConfig.buyer("+i+")[@type]");
            map.put(name, type);
        }
        return map;
    }
    
    public Map<String, String> getmatchingResolutionReportOrder()
    {
        Map<String, String> map = new HashMap<String, String>();
        List<String> codes = this.listValue("matchingResolutionReportOrder.buyer[@code]");
        for (int i = 0; i < codes.size(); i++)
        {
            String code = codes.get(i);
            String bean = this.stringValue("matchingResolutionReportOrder.buyer(" + i + ")");
            map.put(code, bean);
        }
        return map;
    }
    
    public String getCustomizedPreTranslator(String buyerCode, String msgType)
    {
        String template = null;
        List<String> buyers = this.listValue("pre-processing-translator.buyer[@id]");
       
        for (int i = 0; i < buyers.size(); i++)
        {
            String buyer = buyers.get(i);
           
            if (buyer.equalsIgnoreCase(buyerCode))
            {
                List<String> types = this
                    .listValue("pre-processing-translator.buyer(" + i
                        + ").msgType[@type]");
               
                for (int j = 0; j < types.size(); j++)
                {
                    String type = types.get(j);
                   
                    if (type.equalsIgnoreCase(msgType))
                    {
                        template = this.stringValue("pre-processing-translator.buyer(" + i
                            + ").msgType("+ j+ ").translator[@bean]");
                    }
                }
            }
        }

        return template;
    }
    
    
    public static void main(String[] args) throws Exception
    {
        // Add testing code here
        
//        CustomAppConfigHelper c = new CustomAppConfigHelper(
//            new File(
//                "E://workspace//NtucB2B_fp-pahse-2a//config//core//application//config_index.xml"));
        
    }
}
