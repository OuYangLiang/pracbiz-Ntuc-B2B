//*****************************************************************************
//
// File Name       :  VelocityHelper.java
// Date Created    :  2008-1-23
// Last Changed By :  $Author: fangyi $
// Last Changed On :  $Date: 2013-01-11 14:49:59 +0800 (Fri, 11 Jan 2013) $
// Revision        :  $Rev: 1560 $
// Description     :  To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2008.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.util;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class VelocityHelper
{
    protected final Log log = LogFactory.getLog(getClass());

    protected Properties prop = null;

    public final static String DEFAULT_UNICODE = "UTF-8";

    public static final String LOG_DATA_TEMPLATE_STR = "dataTemplate:";


    public VelocityHelper()
    {
        prop = new Properties();
        prop.setProperty(Velocity.RESOURCE_LOADER, "class");
        prop.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
    }


    public String getXmlContentForClientAuditTrail(String batchFilename,
            List<String> docFilenames) throws Exception
    {
        String dataTemplate = null;
        StringWriter writer = null;

        try
        {
            VelocityContext context = new VelocityContext();
            context.put("batchFileName", batchFilename);
            context.put("fileNameList", docFilenames);
            writer = new StringWriter();
            Velocity.init(prop);
            Template t = Velocity.getTemplate(
                    getFilenameOfXmlContentForClientAuditTrail(),
                    DEFAULT_UNICODE);
            t.merge(context, writer);
            dataTemplate = writer.toString().trim();
        }
        catch (Exception exception)
        {
            throw exception;
        }
        finally
        {
            if (null != writer)
            {
                writer.close();
            }
            writer = null;
        }
        return dataTemplate;
    }


    private String getFilenameOfXmlContentForClientAuditTrail()
    {
        return "client_audit_trail_template.vm";
    }

}
