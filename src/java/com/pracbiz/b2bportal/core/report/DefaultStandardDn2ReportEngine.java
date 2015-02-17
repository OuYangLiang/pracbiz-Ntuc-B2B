//*****************************************************************************
//
// File Name       :  DefaultStandardDNReportEngine.java
// Date Created    :  2012-12-24
// Last Changed By :  $Author: LiYong $
// Last Changed On :  $Date: 2012-12-24 $
// Revision        :  $Rev: 15 $
// Description     :  TODO To fill in a brief description of the purpose of
//                    this class.
//
// PracBiz Pte Ltd.  Copyright (c) 2012.  All Rights Reserved.
//
//*****************************************************************************

package com.pracbiz.b2bportal.core.report;

import com.pracbiz.b2bportal.core.constants.DnType;
import com.pracbiz.b2bportal.core.holder.DnHolder;
import com.pracbiz.b2bportal.core.util.CoreCommonConstants;

public class DefaultStandardDn2ReportEngine extends DefaultStandardDnReportEngine
{
    @Override
    protected String reportTemplate(DnHolder data, int flag)
    {
        if(DnType.CST_CR.name().equals(data.getDnHeader().getDnType())
                || DnType.CST_IOC.name().equals(data.getDnHeader().getDnType()))
        {
            return "reports/standard/dn/STANDARD_COST_DN.jasper";
        }
        else
        {   //will redistribution pdf template according diffrent dn type.
            if (flag == 3)
            {
                return "reports/standard/dn/STANDARD_STOCK_DN" + CoreCommonConstants.REPORT_TEMPLATE_FOR_STORE_EXTENSION+ ".jasper";
            }
            
            return "reports/standard/dn/STANDARD_STOCK_DN_2.jasper";
        }
    }
}
